package br.com.marmoraria.model;

import java.time.LocalDateTime;

public class PrecoAtualizado {
    private String materialId;
    private String materialNome;
    private double precoUSD;  // Preço em dólar internacional
    private double precoBRL;  // Preço convertido para real
    private double taxaConversao;
    private LocalDateTime ultimaAtualizacao;
    private String fonte;     // "API", "CACHE", "DEFAULT"

    public PrecoAtualizado(String materialId, String materialNome,
                           double precoUSD, double taxaConversao) {
        this.materialId = materialId;
        this.materialNome = materialNome;
        this.precoUSD = precoUSD;
        this.taxaConversao = taxaConversao;
        this.precoBRL = precoUSD * taxaConversao;
        this.ultimaAtualizacao = LocalDateTime.now();
        this.fonte = "API";
    }

    // Getters
    public String getMaterialId() { return materialId; }
    public String getMaterialNome() { return materialNome; }
    public double getPrecoUSD() { return precoUSD; }
    public double getPrecoBRL() { return precoBRL; }
    public double getTaxaConversao() { return taxaConversao; }
    public LocalDateTime getUltimaAtualizacao() { return ultimaAtualizacao; }
    public String getFonte() { return fonte; }

    public void setFonte(String fonte) { this.fonte = fonte; }
}