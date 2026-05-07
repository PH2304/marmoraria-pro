package br.com.marmoraria.util;

import br.com.marmoraria.model.Orcamento;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gerenciador unificado de arquivos para o sistema Marmoraria Pro.
 * Responsável por salvar, carregar, listar e deletar orçamentos em formato JSON.
 *
 * @author Marmoraria Pro Team
 * @version 2.1.0
 */
public class GerenciadorArquivos {

    private static final Logger LOGGER = Logger.getLogger(GerenciadorArquivos.class.getName());

    private static final String PASTA_ORCAMENTOS = "orcamentos";

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    // Construtor privado para evitar instanciação
    private GerenciadorArquivos() {
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada");
    }

    // ==================== INICIALIZAÇÃO ====================

    /**
     * Garante que a pasta de orçamentos existe
     */
    public static void inicializar() {
        garantirPastaOrcamentos();
        LOGGER.info("Gerenciador de arquivos inicializado: " + getPastaOrcamentos());
    }

    /**
     * Cria a pasta de orçamentos se não existir
     */
    private static void garantirPastaOrcamentos() {
        File pasta = new File(PASTA_ORCAMENTOS);
        if (!pasta.exists()) {
            boolean criada = pasta.mkdirs();
            if (criada) {
                LOGGER.info("Pasta de orçamentos criada: " + pasta.getAbsolutePath());
            } else {
                LOGGER.severe("Falha ao criar pasta de orçamentos: " + pasta.getAbsolutePath());
            }
        }
    }

    // ==================== SALVAR ====================

    /**
     * Salva um orçamento em arquivo JSON
     *
     * @param orcamento Orçamento a ser salvo
     * @return true se salvou com sucesso, false caso contrário
     */
    public static boolean salvarOrcamento(Orcamento orcamento) {
        if (orcamento == null) {
            LOGGER.warning("Tentativa de salvar orçamento nulo");
            return false;
        }

        try {
            garantirPastaOrcamentos();

            String nomeArquivo = orcamento.getNumeroOrcamento() + ".json";
            File arquivo = new File(PASTA_ORCAMENTOS, nomeArquivo);

            try (FileWriter writer = new FileWriter(arquivo)) {
                GSON.toJson(orcamento, writer);
            }

            LOGGER.info("Orçamento salvo com sucesso: " + nomeArquivo);
            return true;

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar orçamento: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Salva um orçamento com backup automático
     */
    public static boolean salvarOrcamentoComBackup(Orcamento orcamento) {
        // Criar backup antes de sobrescrever
        String nomeArquivo = orcamento.getNumeroOrcamento() + ".json";
        File arquivoExistente = new File(PASTA_ORCAMENTOS, nomeArquivo);

        if (arquivoExistente.exists()) {
            criarBackupArquivo(arquivoExistente);
        }

        return salvarOrcamento(orcamento);
    }

    // ==================== CARREGAR ====================

    /**
     * Carrega um orçamento específico pelo número
     *
     * @param numeroOrcamento Número do orçamento (sem extensão)
     * @return Optional contendo o orçamento se encontrado, vazio caso contrário
     */
    public static Optional<Orcamento> carregarOrcamento(String numeroOrcamento) {
        if (numeroOrcamento == null || numeroOrcamento.trim().isEmpty()) {
            LOGGER.warning("Tentativa de carregar orçamento com número inválido");
            return Optional.empty();
        }

        try {
            File arquivo = new File(PASTA_ORCAMENTOS, numeroOrcamento + ".json");

            if (!arquivo.exists()) {
                LOGGER.warning("Arquivo não encontrado: " + arquivo.getName());
                return Optional.empty();
            }

            try (FileReader reader = new FileReader(arquivo)) {
                Orcamento orcamento = GSON.fromJson(reader, Orcamento.class);

                if (orcamento != null) {
                    // Recalcular totais para garantir consistência
                    orcamento.calcularTotais();
                    LOGGER.info("Orçamento carregado: " + numeroOrcamento);
                    return Optional.of(orcamento);
                } else {
                    LOGGER.warning("Orçamento carregado é nulo: " + numeroOrcamento);
                    return Optional.empty();
                }
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar orçamento: " + e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Carrega todos os orçamentos salvos
     *
     * @return Lista de orçamentos (vazia se nenhum encontrado)
     */
    public static List<Orcamento> carregarTodosOrcamentos() {
        List<Orcamento> orcamentos = new ArrayList<>();

        try {
            garantirPastaOrcamentos();
            File pasta = new File(PASTA_ORCAMENTOS);
            File[] arquivos = pasta.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

            if (arquivos == null || arquivos.length == 0) {
                LOGGER.info("Nenhum orçamento encontrado");
                return orcamentos;
            }

            for (File arquivo : arquivos) {
                try (FileReader reader = new FileReader(arquivo)) {
                    Orcamento orcamento = GSON.fromJson(reader, Orcamento.class);

                    if (orcamento != null) {
                        orcamento.calcularTotais();
                        orcamentos.add(orcamento);
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Erro ao ler arquivo: " + arquivo.getName(), e);
                }
            }

            // Ordenar por data de criação (mais recente primeiro)
            orcamentos.sort((o1, o2) -> o2.getDataCriacao().compareTo(o1.getDataCriacao()));

            LOGGER.info("Orçamentos carregados: " + orcamentos.size());

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar orçamentos: " + e.getMessage(), e);
        }

        return orcamentos;
    }

    // ==================== LISTAR ====================

    /**
     * Lista os nomes de todos os orçamentos salvos
     *
     * @return Lista de nomes de arquivos (sem extensão)
     */
    public static List<String> listarOrcamentos() {
        List<String> nomes = new ArrayList<>();

        try {
            garantirPastaOrcamentos();
            File pasta = new File(PASTA_ORCAMENTOS);
            File[] arquivos = pasta.listFiles((dir, name) -> name.endsWith(".json"));

            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    String nome = arquivo.getName().replace(".json", "");
                    nomes.add(nome);
                }

                // Ordenar alfabeticamente (que também ordena por data devido ao formato)
                Collections.sort(nomes, Collections.reverseOrder());
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar nomes de orçamentos: " + e.getMessage(), e);
        }

        return nomes;
    }

    /**
     * Lista orçamentos com informações resumidas
     */
    public static List<String> listarOrcamentosResumido() {
        List<String> resumo = new ArrayList<>();
        List<Orcamento> orcamentos = carregarTodosOrcamentos();

        for (Orcamento orc : orcamentos) {
            String info = String.format("%s | %s | R$ %.2f | %s",
                    orc.getNumeroOrcamento(),
                    orc.getDataFormatada(),
                    orc.getTotalFinal(),
                    orc.getStatus());
            resumo.add(info);
        }

        return resumo;
    }

    // ==================== DELETAR ====================

    /**
     * Deleta um orçamento pelo número
     *
     * @param numeroOrcamento Número do orçamento a ser deletado
     * @return true se deletado com sucesso
     */
    public static boolean deletarOrcamento(String numeroOrcamento) {
        if (numeroOrcamento == null || numeroOrcamento.trim().isEmpty()) {
            LOGGER.warning("Tentativa de deletar orçamento com número inválido");
            return false;
        }

        try {
            File arquivo = new File(PASTA_ORCAMENTOS, numeroOrcamento + ".json");

            if (!arquivo.exists()) {
                LOGGER.warning("Arquivo não encontrado para deletar: " + numeroOrcamento);
                return false;
            }

            boolean deletado = arquivo.delete();

            if (deletado) {
                LOGGER.info("Orçamento deletado: " + numeroOrcamento);
            } else {
                LOGGER.warning("Falha ao deletar orçamento: " + numeroOrcamento);
            }

            return deletado;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao deletar orçamento: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Deleta múltiplos orçamentos
     */
    public static int deletarOrcamentos(List<String> numerosOrcamentos) {
        int deletados = 0;
        for (String numero : numerosOrcamentos) {
            if (deletarOrcamento(numero)) {
                deletados++;
            }
        }
        LOGGER.info("Deletados " + deletados + " de " + numerosOrcamentos.size() + " orçamentos");
        return deletados;
    }

    // ==================== VERIFICAÇÕES ====================

    /**
     * Verifica se existem orçamentos salvos
     */
    public static boolean temOrcamentosSalvos() {
        File pasta = new File(PASTA_ORCAMENTOS);
        if (!pasta.exists()) {
            return false;
        }
        File[] arquivos = pasta.listFiles((dir, name) -> name.endsWith(".json"));
        return arquivos != null && arquivos.length > 0;
    }

    /**
     * Retorna o total de orçamentos salvos
     */
    public static int getTotalOrcamentos() {
        File pasta = new File(PASTA_ORCAMENTOS);
        if (!pasta.exists()) {
            return 0;
        }
        File[] arquivos = pasta.listFiles((dir, name) -> name.endsWith(".json"));
        return arquivos == null ? 0 : arquivos.length;
    }

    /**
     * Verifica se um orçamento específico existe
     */
    public static boolean existeOrcamento(String numeroOrcamento) {
        File arquivo = new File(PASTA_ORCAMENTOS, numeroOrcamento + ".json");
        return arquivo.exists();
    }

    // ==================== UTILITÁRIOS ====================

    /**
     * Retorna o caminho absoluto da pasta de orçamentos
     */
    public static String getPastaOrcamentos() {
        return new File(PASTA_ORCAMENTOS).getAbsolutePath();
    }

    /**
     * Cria um backup de um arquivo antes de modificá-lo
     */
    private static void criarBackupArquivo(File arquivoOriginal) {
        try {
            String nomeBackup = arquivoOriginal.getName() + ".backup";
            File arquivoBackup = new File(PASTA_ORCAMENTOS, nomeBackup);

            try (FileInputStream fis = new FileInputStream(arquivoOriginal);
                 FileOutputStream fos = new FileOutputStream(arquivoBackup)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            }

            LOGGER.info("Backup criado: " + nomeBackup);

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Erro ao criar backup: " + e.getMessage(), e);
        }
    }

    /**
     * Exporta um orçamento para JSON String
     */
    public static String exportarParaJSON(Orcamento orcamento) {
        return GSON.toJson(orcamento);
    }

    /**
     * Importa um orçamento de uma String JSON
     */
    public static Optional<Orcamento> importarDeJSON(String json) {
        try {
            Orcamento orcamento = GSON.fromJson(json, Orcamento.class);
            if (orcamento != null) {
                orcamento.calcularTotais();
                return Optional.of(orcamento);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao importar JSON: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * Obtém estatísticas dos arquivos
     */
    public static String getEstatisticas() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTATÍSTICAS DE ARQUIVOS ===\n");
        sb.append("Pasta: ").append(getPastaOrcamentos()).append("\n");
        sb.append("Total de orçamentos: ").append(getTotalOrcamentos()).append("\n");

        long tamanhoTotal = 0;
        File pasta = new File(PASTA_ORCAMENTOS);
        if (pasta.exists()) {
            File[] arquivos = pasta.listFiles((dir, name) -> name.endsWith(".json"));
            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    tamanhoTotal += arquivo.length();
                }
                sb.append("Tamanho total: ").append(String.format("%.2f KB", tamanhoTotal / 1024.0)).append("\n");
                sb.append("Tamanho médio: ").append(String.format("%.2f KB", (tamanhoTotal / 1024.0) / arquivos.length)).append("\n");
            }
        }

        return sb.toString();
    }
}
