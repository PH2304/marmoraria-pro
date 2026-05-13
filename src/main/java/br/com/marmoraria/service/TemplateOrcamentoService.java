package br.com.marmoraria.service;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Orcamento;
import br.com.marmoraria.util.GerenciadorArquivos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Sistema de templates de orçamento.
 * Permite salvar e carregar modelos pré-configurados.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class TemplateOrcamentoService {

    private static final String PASTA_TEMPLATES = "templates";
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new br.com.marmoraria.util.LocalDateTimeAdapter())
            .create();

    /**
     * Representa um template de orçamento
     */
    public static class TemplateOrcamento {
        private String id;
        private String nome;
        private String descricao;
        private String categoria;
        private LocalDateTime dataCriacao;
        private double margemLucroPadrao;
        private double fretePadrao;
        private boolean incluirMaoDeObraPadrao;
        private double percentualMaoDeObraPadrao;
        private List<String> observacoesPadrao;
        private int vezesUsado;

        public TemplateOrcamento() {
            this.id = UUID.randomUUID().toString();
            this.dataCriacao = LocalDateTime.now();
            this.observacoesPadrao = new ArrayList<>();
            this.vezesUsado = 0;
        }

        public TemplateOrcamento(String nome, String descricao, String categoria) {
            this();
            this.nome = nome;
            this.descricao = descricao;
            this.categoria = categoria;
        }

        // Getters e Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        public LocalDateTime getDataCriacao() { return dataCriacao; }
        public double getMargemLucroPadrao() { return margemLucroPadrao; }
        public void setMargemLucroPadrao(double m) { this.margemLucroPadrao = m; }
        public double getFretePadrao() { return fretePadrao; }
        public void setFretePadrao(double f) { this.fretePadrao = f; }
        public boolean isIncluirMaoDeObraPadrao() { return incluirMaoDeObraPadrao; }
        public void setIncluirMaoDeObraPadrao(boolean i) { this.incluirMaoDeObraPadrao = i; }
        public double getPercentualMaoDeObraPadrao() { return percentualMaoDeObraPadrao; }
        public void setPercentualMaoDeObraPadrao(double p) { this.percentualMaoDeObraPadrao = p; }
        public List<String> getObservacoesPadrao() { return observacoesPadrao; }
        public int getVezesUsado() { return vezesUsado; }
        public void incrementarUso() { this.vezesUsado++; }
    }

    /**
     * Garante que a pasta de templates existe
     */
    private static void garantirPasta() {
        File pasta = new File(PASTA_TEMPLATES);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }
    }

    /**
     * Salva um template
     */
    public static boolean salvarTemplate(TemplateOrcamento template) {
        try {
            garantirPasta();
            File arquivo = new File(PASTA_TEMPLATES, template.getId() + ".json");

            try (FileWriter writer = new FileWriter(arquivo)) {
                GSON.toJson(template, writer);
            }
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao salvar template: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carrega todos os templates
     */
    public static List<TemplateOrcamento> carregarTodos() {
        List<TemplateOrcamento> templates = new ArrayList<>();
        garantirPasta();

        File pasta = new File(PASTA_TEMPLATES);
        File[] arquivos = pasta.listFiles((dir, name) -> name.endsWith(".json"));

        if (arquivos != null) {
            for (File arquivo : arquivos) {
                try (FileReader reader = new FileReader(arquivo)) {
                    TemplateOrcamento t = GSON.fromJson(reader, TemplateOrcamento.class);
                    if (t != null) {
                        templates.add(t);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao ler template: " + arquivo.getName());
                }
            }
        }

        // Ordenar por mais usados
        templates.sort((a, b) -> Integer.compare(b.getVezesUsado(), a.getVezesUsado()));

        return templates;
    }

    /**
     * Carrega templates por categoria
     */
    public static List<TemplateOrcamento> carregarPorCategoria(String categoria) {
        return carregarTodos().stream()
                .filter(t -> t.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    /**
     * Deleta um template
     */
    public static boolean deletarTemplate(String id) {
        File arquivo = new File(PASTA_TEMPLATES, id + ".json");
        return arquivo.exists() && arquivo.delete();
    }

    /**
     * Cria templates padrão
     */
    public static void criarTemplatesPadrao() {
        // Template: Cozinha Completa
        TemplateOrcamento t1 = new TemplateOrcamento(
                "Cozinha Completa",
                "Bancada de cozinha com saia frontal e frontão",
                "Cozinha"
        );
        t1.setMargemLucroPadrao(30.0);
        t1.setFretePadrao(150.0);
        t1.setIncluirMaoDeObraPadrao(true);
        t1.setPercentualMaoDeObraPadrao(0.30);
        salvarTemplate(t1);

        // Template: Banheiro Social
        TemplateOrcamento t2 = new TemplateOrcamento(
                "Banheiro Social",
                "Bancada com cuba, peitoril e tento do box",
                "Banheiro"
        );
        t2.setMargemLucroPadrao(25.0);
        t2.setFretePadrao(100.0);
        t2.setIncluirMaoDeObraPadrao(true);
        t2.setPercentualMaoDeObraPadrao(0.25);
        salvarTemplate(t2);

        // Template: Área de Serviço
        TemplateOrcamento t3 = new TemplateOrcamento(
                "Área de Serviço",
                "Bancada de área de serviço simples",
                "Serviço"
        );
        t3.setMargemLucroPadrao(20.0);
        t3.setFretePadrao(80.0);
        t3.setIncluirMaoDeObraPadrao(false);
        salvarTemplate(t3);

        System.out.println("✅ Templates padrão criados!");
    }
}
