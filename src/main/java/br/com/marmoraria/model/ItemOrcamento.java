package br.com.marmoraria.model;

import java.io.Serializable;

public class ItemOrcamento implements Serializable {

    private Material material;
    private Servico servico;

    private int quantidade;
    private double larguraMm;
    private double comprimentoMm;

    private double areaM2;
    private double valorTotal;

    private String observacoes;

    // 🔹 CONSTRUTOR VAZIO (OBRIGATÓRIO PARA JAVA FX)
    public ItemOrcamento() {
    }

    // 🔹 CONSTRUTOR COMPLETO
    public ItemOrcamento(
            Material material,
            Servico servico,
            int quantidade,
            double larguraMm,
            double comprimentoMm,
            String observacoes
    ) {
        this.material = material;
        this.servico = servico;
        this.quantidade = quantidade;
        this.larguraMm = larguraMm;
        this.comprimentoMm = comprimentoMm;
        this.observacoes = observacoes;

        calcularArea();
        calcularValores();
    }

    // 🔹 CÁLCULO DA ÁREA EM METROS QUADRADOS
    private void calcularArea() {
        this.areaM2 = (larguraMm / 1000.0) *
                (comprimentoMm / 1000.0) *
                quantidade;
    }

    // 🔹 CÁLCULO DO VALOR TOTAL
    private void calcularValores() {
        double valorMaterial = 0;
        double valorServico = 0;

        if (material != null) {
            valorMaterial = material.getPrecoPorMetroQuadrado() * areaM2;
        }

        if (servico != null) {
            valorServico = servico.getPrecoUnitario() * quantidade;
        }

        this.valorTotal = valorMaterial + valorServico;
    }

    // 🔹 GETTERS & SETTERS (OBRIGATÓRIOS PARA TABLEVIEW)

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
        calcularValores();
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
        calcularValores();
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        calcularArea();
        calcularValores();
    }

    public double getLarguraMm() {
        return larguraMm;
    }

    public void setLarguraMm(double larguraMm) {
        this.larguraMm = larguraMm;
        calcularArea();
        calcularValores();
    }

    public double getComprimentoMm() {
        return comprimentoMm;
    }

    public void setComprimentoMm(double comprimentoMm) {
        this.comprimentoMm = comprimentoMm;
        calcularArea();
        calcularValores();
    }

    public double getAreaM2() {
        return areaM2;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
