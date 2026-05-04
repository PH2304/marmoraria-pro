package br.com.marmoraria.util;

import br.com.marmoraria.model.Orcamento;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String PASTA_ORCAMENTOS = "orcamentos";
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public static void garantirPastaOrcamentos() {
        File pasta = new File(PASTA_ORCAMENTOS);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }
    }

    public static boolean salvarOrcamento(Orcamento orcamento) {
        try {
            garantirPastaOrcamentos();
            File arquivo = new File(PASTA_ORCAMENTOS, orcamento.getNumeroOrcamento() + ".json");

            try (FileWriter writer = new FileWriter(arquivo)) {
                writer.write(GSON.toJson(orcamento));
            }
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao salvar orcamento: " + e.getMessage());
            return false;
        }
    }

    public static Orcamento carregarOrcamento(String numeroOrcamento) {
        try {
            File arquivo = new File(PASTA_ORCAMENTOS, numeroOrcamento + ".json");
            if (!arquivo.exists()) {
                return null;
            }

            try (FileReader reader = new FileReader(arquivo)) {
                Orcamento orcamento = GSON.fromJson(reader, Orcamento.class);
                if (orcamento != null) {
                    orcamento.calcularTotais();
                }
                return orcamento;
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar orcamento: " + e.getMessage());
            return null;
        }
    }

    public static List<Orcamento> listarOrcamentos() {
        List<Orcamento> orcamentos = new ArrayList<>();

        try {
            garantirPastaOrcamentos();
            File pasta = new File(PASTA_ORCAMENTOS);
            File[] arquivos = pasta.listFiles((dir, name) -> name.endsWith(".json"));

            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    try (FileReader reader = new FileReader(arquivo)) {
                        Orcamento orcamento = GSON.fromJson(reader, Orcamento.class);
                        if (orcamento != null) {
                            orcamento.calcularTotais();
                            orcamentos.add(orcamento);
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao ler arquivo: " + arquivo.getName());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar orcamentos: " + e.getMessage());
        }

        return orcamentos;
    }

    public static List<String> listarNomesOrcamentos() {
        List<String> nomes = new ArrayList<>();

        try {
            garantirPastaOrcamentos();
            File pasta = new File(PASTA_ORCAMENTOS);
            File[] arquivos = pasta.listFiles((dir, name) -> name.endsWith(".json"));

            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    nomes.add(arquivo.getName().replace(".json", ""));
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar nomes: " + e.getMessage());
        }

        return nomes;
    }

    public static boolean deletarOrcamento(String numeroOrcamento) {
        try {
            File arquivo = new File(PASTA_ORCAMENTOS, numeroOrcamento + ".json");
            return arquivo.exists() && arquivo.delete();
        } catch (Exception e) {
            System.err.println("Erro ao deletar orcamento: " + e.getMessage());
            return false;
        }
    }

    public static String getPastaOrcamentos() {
        return new File(PASTA_ORCAMENTOS).getAbsolutePath();
    }
}
