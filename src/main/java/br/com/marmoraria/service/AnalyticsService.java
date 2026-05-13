package br.com.marmoraria.service;

import br.com.marmoraria.model.Orcamento;
import br.com.marmoraria.util.GerenciadorArquivos;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Serviço de analytics para métricas de uso do sistema.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class AnalyticsService {

    private static final Logger LOGGER = Logger.getLogger(AnalyticsService.class.getName());

    // Contadores
    private static final Map<String, Integer> contadores = new ConcurrentHashMap<>();

    // Tempo de uso
    private static LocalDateTime inicioSessao;
    private static final Map<String, Long> temposUso = new ConcurrentHashMap<>();

    // Eventos
    private static final List<Evento> eventosRecentes = new ArrayList<>();
    private static final int MAX_EVENTOS = 100;

    public static class Evento {
        private final String tipo;
        private final String descricao;
        private final LocalDateTime timestamp;

        public Evento(String tipo, String descricao) {
            this.tipo = tipo;
            this.descricao = descricao;
            this.timestamp = LocalDateTime.now();
        }

        public String getTipo() { return tipo; }
        public String getDescricao() { return descricao; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    /**
     * Inicia a sessão de analytics
     */
    public static void iniciarSessao() {
        inicioSessao = LocalDateTime.now();
        LOGGER.info("Sessão iniciada: " + inicioSessao);
    }

    /**
     * Registra um evento
     */
    public static void registrarEvento(String tipo, String descricao) {
        Evento evento = new Evento(tipo, descricao);
        eventosRecentes.add(0, evento);

        // Manter apenas os últimos eventos
        if (eventosRecentes.size() > MAX_EVENTOS) {
            eventosRecentes.remove(eventosRecentes.size() - 1);
        }

        // Incrementar contador
        contadores.merge(tipo, 1, Integer::sum);

        LOGGER.fine("Evento: " + tipo + " - " + descricao);
    }

    /**
     * Registra início de operação
     */
    public static void registrarInicioOperacao(String operacao) {
        temposUso.put("inicio_" + operacao, System.currentTimeMillis());
    }

    /**
     * Registra fim de operação
     */
    public static void registrarFimOperacao(String operacao) {
        Long inicio = temposUso.remove("inicio_" + operacao);
        if (inicio != null) {
            long duracao = System.currentTimeMillis() - inicio;
            temposUso.merge(operacao, duracao, Long::sum);
            registrarEvento("operacao", operacao + " (" + duracao + "ms)");
        }
    }

    /**
     * Registra abertura de view
     */
    public static void registrarAberturaView(String viewName) {
        registrarEvento("view", "Abertura: " + viewName);
        contadores.merge("views_" + viewName, 1, Integer::sum);
    }

    /**
     * Registra salvamento de orçamento
     */
    public static void registrarSalvamentoOrcamento(Orcamento orcamento) {
        registrarEvento("orcamento", "Salvo: " + orcamento.getNumeroOrcamento() +
                " (R$ " + String.format("%.2f", orcamento.getTotalFinal()) + ")");
        contadores.merge("orcamentos_salvos", 1, Integer::sum);
    }

    /**
     * Registra geração de PDF
     */
    public static void registrarGeracaoPDF(Orcamento orcamento) {
        registrarEvento("pdf", "PDF gerado: " + orcamento.getNumeroOrcamento());
        contadores.merge("pdfs_gerados", 1, Integer::sum);
    }

    /**
     * Registra erro
     */
    public static void registrarErro(String origem, String mensagem) {
        registrarEvento("erro", origem + ": " + mensagem);
        contadores.merge("erros", 1, Integer::sum);
    }

    /**
     * Obtém tempo de sessão
     */
    public static String getTempoSessao() {
        if (inicioSessao == null) return "N/A";

        Duration duracao = Duration.between(inicioSessao, LocalDateTime.now());
        long horas = duracao.toHours();
        long minutos = duracao.toMinutesPart();

        if (horas > 0) {
            return horas + "h " + minutos + "min";
        }
        return minutos + " minutos";
    }

    /**
     * Obtém estatísticas da sessão
     */
    public static Map<String, Object> getEstatisticasSessao() {
        Map<String, Object> stats = new LinkedHashMap<>();

        stats.put("tempoSessao", getTempoSessao());
        stats.put("totalEventos", eventosRecentes.size());
        stats.put("orcamentosSalvos", contadores.getOrDefault("orcamentos_salvos", 0));
        stats.put("pdfsGerados", contadores.getOrDefault("pdfs_gerados", 0));
        stats.put("erros", contadores.getOrDefault("erros", 0));

        // View mais acessada
        String viewMaisAcessada = contadores.entrySet().stream()
                .filter(e -> e.getKey().startsWith("views_"))
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey().replace("views_", ""))
                .orElse("Nenhuma");
        stats.put("viewMaisAcessada", viewMaisAcessada);

        return stats;
    }

    /**
     * Obtém eventos recentes
     */
    public static List<Evento> getEventosRecentes(int limite) {
        int end = Math.min(limite, eventosRecentes.size());
        return new ArrayList<>(eventosRecentes.subList(0, end));
    }

    /**
     * Obtém resumo para dashboard
     */
    public static String getResumo() {
        StringBuilder sb = new StringBuilder();

        sb.append("═══════════════════════════════════\n");
        sb.append("       RESUMO DA SESSÃO\n");
        sb.append("═══════════════════════════════════\n\n");

        sb.append("⏱️  Tempo de uso: ").append(getTempoSessao()).append("\n\n");

        sb.append("📊 Atividade:\n");
        sb.append("   • Orçamentos salvos: ").append(
                contadores.getOrDefault("orcamentos_salvos", 0)).append("\n");
        sb.append("   • PDFs gerados: ").append(
                contadores.getOrDefault("pdfs_gerados", 0)).append("\n");
        sb.append("   • Views abertas: ").append(
                contadores.entrySet().stream()
                        .filter(e -> e.getKey().startsWith("views_"))
                        .mapToInt(Map.Entry::getValue).sum()).append("\n");
        sb.append("   • Erros: ").append(
                contadores.getOrDefault("erros", 0)).append("\n\n");

        sb.append("📈 Dados:\n");
        List<Orcamento> orcamentos = GerenciadorArquivos.carregarTodosOrcamentos();
        sb.append("   • Total orçamentos: ").append(orcamentos.size()).append("\n");

        double faturamento = orcamentos.stream()
                .mapToDouble(Orcamento::getTotalFinal).sum();
        sb.append(String.format("   • Faturamento total: R$ %.2f\n", faturamento));

        sb.append("\n═══════════════════════════════════\n");

        return sb.toString();
    }
}