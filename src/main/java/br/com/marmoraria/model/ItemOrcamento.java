package br.com.marmoraria.model;

import java.io.Serializable;

public class ItemOrcamento implements Serializable {

    private Material material;
    private Servico servico;

    private double larguraMm;
    private double comprimentoMm;
    private int quantidade;

    private double areaM2;
    private double valorTotal;

    public ItemOrcamento() {}

    public void calcular() {
        this.areaM2 = (larguraMm / 1000.0)
                * (comprimentoMm / 1000.0)
                * quantidade;

        double totalMaterial = material != null
                ? areaM2 * material.getPrecoPorMetroQuadrado()
                : 0;

        double totalServico = servico != null
                ? servico.getPrecoUnitario() * quantidade
                : 0;

        this.valorTotal = totalMaterial + totalServico;
    }

    // Getters e Setters
    public Material getMaterial() { return material; }
    public void setMaterial(Material material) { this.material = material; }

    public Servico getServico() { return servico; }
    public void setServico(Servico servico) { this.servico = servico; }

    public double getLarguraMm() { return larguraMm; }
    public void setLarguraMm(double larguraMm) { this.larguraMm = larguraMm; }

    public double getComprimentoMm() { return comprimentoMm; }
    public void setComprimentoMm(double comprimentoMm) { this.comprimentoMm = comprimentoMm; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getAreaM2() { return areaM2; }
    public double getValorTotal() { return valorTotal; }
}
