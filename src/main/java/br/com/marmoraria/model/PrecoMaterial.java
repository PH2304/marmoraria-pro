// Arquivo: src/main/java/com/marmoraria/model/PrecoMaterial.java
package br.com.marmoraria.model;

import java.time.LocalDateTime;

public class PrecoMaterial {
    private String id;
    private String nome;
    private String tipo; // Mármore, Granito, Quartzo, Serviço
    private double precoBRL;
    private String origem;
    private LocalDateTime dataAtualizacao;
    private String fonte; // LOCAL, API, PADRÃO
    private String observacao;

    public PrecoMaterial(String id, String nome, String tipo, double precoBRL,
                         String origem, LocalDateTime dataAtualizacao,
                         String fonte, String observacao) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.precoBRL = precoBRL;
        this.origem = origem;
        this.dataAtualizacao = dataAtualizacao;
        this.fonte = fonte;
        this.observacao = observacao;
    }

    // Getters e Setters
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public double getPrecoBRL() { return precoBRL; }
    public void setPrecoBRL(double precoBRL) { this.precoBRL = precoBRL; }
    public String getOrigem() { return origem; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
    public String getFonte() { return fonte; }
    public void setFonte(String fonte) { this.fonte = fonte; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public String getDataFormatada() {
        return dataAtualizacao.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getPrecoFormatado() {
        return String.format("R$ %.2f /m²", precoBRL);
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", nome, getPrecoFormatado(), fonte);
    }
}
