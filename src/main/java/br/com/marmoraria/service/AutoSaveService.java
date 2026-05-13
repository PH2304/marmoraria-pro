package br.com.marmoraria.service;

import br.com.marmoraria.model.Orcamento;
import br.com.marmoraria.util.GerenciadorArquivos;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Serviço de salvamento automático para evitar perda de dados.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class AutoSaveService {

    private static final Logger LOGGER = Logger.getLogger(AutoSaveService.class.getName());
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private static Orcamento orcamentoAtual;
    private static LocalDateTime ultimoSave;
    private static boolean autoSaveAtivo = true;
    private static int intervaloMinutos = 2;

    /**
     * Inicia o monitoramento de auto-save
     */
    public static void iniciar(Orcamento orcamento) {
        orcamentoAtual = orcamento;
        ultimoSave = LocalDateTime.now();

        scheduler.scheduleAtFixedRate(() -> {
            if (autoSaveAtivo && orcamentoAtual != null) {
                salvarAutoSave();
            }
        }, intervaloMinutos, intervaloMinutos, TimeUnit.MINUTES);

        LOGGER.info("Auto-save iniciado (a cada " + intervaloMinutos + " min)");
    }

    /**
     * Força um salvamento agora
     */
    public static boolean salvarAgora(Orcamento orcamento) {
        orcamentoAtual = orcamento;
        return salvarAutoSave();
    }

    /**
     * Executa o salvamento automático
     */
    private static boolean salvarAutoSave() {
        if (orcamentoAtual == null || orcamentoAtual.getItens().isEmpty()) {
            return false;
        }

        try {
            // Adicionar marcador de auto-save
            String obsOriginal = orcamentoAtual.getObservacoes();
            String marcador = "[Auto-salvo em " +
                    LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "]";

            if (obsOriginal == null || !obsOriginal.contains("[Auto-salvo")) {
                orcamentoAtual.setObservacoes(
                        (obsOriginal != null ? obsOriginal + "\n" : "") + marcador
                );
            }

            boolean sucesso = GerenciadorArquivos.salvarOrcamento(orcamentoAtual);
            if (sucesso) {
                ultimoSave = LocalDateTime.now();
                LOGGER.fine("Auto-save realizado: " + orcamentoAtual.getNumeroOrcamento());
            }

            // Remover marcador
            if (obsOriginal == null || !obsOriginal.contains("[Auto-salvo")) {
                orcamentoAtual.setObservacoes(obsOriginal);
            }

            return sucesso;

        } catch (Exception e) {
            LOGGER.warning("Erro no auto-save: " + e.getMessage());
            return false;
        }
    }

    /**
     * Restaura o último auto-save (se disponível)
     */
    public static Orcamento restaurarAutoSave() {
        // Buscar orçamentos com marcador de auto-save
        var orcamentos = GerenciadorArquivos.carregarTodosOrcamentos();

        return orcamentos.stream()
                .filter(o -> o.getObservacoes() != null &&
                        o.getObservacoes().contains("[Auto-salvo"))
                .findFirst()
                .orElse(null);
    }

    /**
     * Verifica se há alterações não salvas
     */
    public static boolean temAlteracoesNaoSalvas(Orcamento atual) {
        if (ultimoSave == null || atual == null) return true;

        Orcamento salvo = GerenciadorArquivos.carregarOrcamento(
                atual.getNumeroOrcamento()).orElse(null);

        if (salvo == null) return true;

        return atual.getItens().size() != salvo.getItens().size() ||
                Math.abs(atual.getTotalFinal() - salvo.getTotalFinal()) > 0.01;
    }

    /**
     * Retorna o tempo desde o último save
     */
    public static String getTempoDesdeUltimoSave() {
        if (ultimoSave == null) return "Nunca";

        long minutos = java.time.Duration.between(ultimoSave, LocalDateTime.now()).toMinutes();

        if (minutos < 1) return "Agora mesmo";
        if (minutos == 1) return "1 minuto atrás";
        if (minutos < 60) return minutos + " minutos atrás";

        long horas = minutos / 60;
        if (horas == 1) return "1 hora atrás";
        return horas + " horas atrás";
    }

    // Getters e Setters
    public static boolean isAutoSaveAtivo() { return autoSaveAtivo; }
    public static void setAutoSaveAtivo(boolean ativo) { autoSaveAtivo = ativo; }
    public static int getIntervaloMinutos() { return intervaloMinutos; }
    public static void setIntervaloMinutos(int minutos) { intervaloMinutos = minutos; }

    /**
     * Encerra o serviço
     */
    public static void shutdown() {
        scheduler.shutdown();
        LOGGER.info("Auto-save encerrado");
    }
}