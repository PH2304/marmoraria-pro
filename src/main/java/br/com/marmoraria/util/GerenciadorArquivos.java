package br.com.marmoraria.util;

import br.com.marmoraria.model.Orcamento;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivos {

    private static final String PASTA_ORCAMENTOS = "orcamentos";
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    /**
     * Garante que a pasta de orçamentos existe
     */
    public static void garantirPastaOrcamentos() {
        File pasta = new File(PASTA_ORCAMENTOS);
        if (!pasta.exists()) {
            pasta.mkdirs();
            System.out.println("📁 Pasta de orçamentos criada: " + pasta.getAbsolutePath());
        }
    }

    /**
     * Salva um orçamento em arquivo
     */
    public static boolean salvarOrcamento(Orcamento orcamento) {
        try {
            garantirPastaOrcamentos();

            String nomeArquivo = orcamento.getNumeroOrcamento() + ".json";
            File arquivo = new File(PASTA_ORCAMENTOS, nomeArquivo);

            try (FileWriter writer = new FileWriter(arquivo)) {
                gson.toJson(orcamento, writer);
            }

            System.out.println("💾 Orçamento salvo: " + arquivo.getAbsolutePath());
            return true;

        } catch (IOException e) {
            System.err.println("❌ Erro ao salvar orçamento: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carrega um orçamento pelo número
     */
    public static Orcamento carregarOrcamento(String numeroOrcamento) {
        try {
            File arquivo = new File(PASTA_ORCAMENTOS, numeroOrcamento + ".json");
            if (!arquivo.exists()) {
                return null;
            }

            try (FileReader reader = new FileReader(arquivo)) {
                Orcamento orcamento = gson.fromJson(reader, Orcamento.class);
                System.out.println("📂 Orçamento carregado: " + numeroOrcamento);
                return orcamento;
            }

        } catch (IOException e) {
            System.err.println("❌ Erro ao carregar orçamento: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lista todos os orçamentos salvos
     */
    public static List<Orcamento> listarOrcamentos() {
        List<Orcamento> orcamentos = new ArrayList<>();

        try {
            garantirPastaOrcamentos();
            File pasta = new File(PASTA_ORCAMENTOS);
            File[] arquivos = pasta.listFiles((dir, name) -> name.endsWith(".json"));

            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    try (FileReader reader = new FileReader(arquivo)) {
                        Orcamento orcamento = gson.fromJson(reader, Orcamento.class);
                        if (orcamento != null) {
                            orcamentos.add(orcamento);
                        }
                    } catch (Exception e) {
                        System.err.println("⚠️ Erro ao ler arquivo: " + arquivo.getName());
                    }
                }
            }

            System.out.println("📊 Total de orçamentos carregados: " + orcamentos.size());

        } catch (Exception e) {
            System.err.println("❌ Erro ao listar orçamentos: " + e.getMessage());
        }

        return orcamentos;
    }

    /**
     * Lista os nomes dos arquivos de orçamentos
     */
    public static List<String> listarNomesOrcamentos() {
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
            }

        } catch (Exception e) {
            System.err.println("❌ Erro ao listar nomes: " + e.getMessage());
        }

        return nomes;
    }

    /**
     * Deleta um orçamento
     */
    public static boolean deletarOrcamento(String numeroOrcamento) {
        try {
            File arquivo = new File(PASTA_ORCAMENTOS, numeroOrcamento + ".json");
            if (arquivo.exists()) {
                boolean deletado = arquivo.delete();
                if (deletado) {
                    System.out.println("🗑️ Orçamento deletado: " + numeroOrcamento);
                }
                return deletado;
            }
            return false;

        } catch (Exception e) {
            System.err.println("❌ Erro ao deletar orçamento: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtém o caminho completo da pasta de orçamentos
     */
    public static String getPastaOrcamentos() {
        return new File(PASTA_ORCAMENTOS).getAbsolutePath();
    }

    /**
     * Verifica se existem orçamentos salvos
     */
    public static boolean temOrcamentosSalvos() {
        File pasta = new File(PASTA_ORCAMENTOS);
        if (!pasta.exists()) return false;

        File[] arquivos = pasta.listFiles((dir, name) -> name.endsWith(".json"));
        return arquivos != null && arquivos.length > 0;
    }

    /**
     * Obtém o total de orçamentos salvos
     */
    public static int getTotalOrcamentos() {
        File pasta = new File(PASTA_ORCAMENTOS);
        if (!pasta.exists()) return 0;

        File[] arquivos = pasta.listFiles((dir, name) -> name.endsWith(".json"));
        return arquivos != null ? arquivos.length : 0;
    }
}
