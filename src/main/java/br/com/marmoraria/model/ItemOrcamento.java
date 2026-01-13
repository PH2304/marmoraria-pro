package br.com.marmoraria.model;

import java.io.Serializable;

public class ItemOrcamento implements Serializable {
    private String id;
    private Material material;
    private Servico servico;
    private double quantidade;
    private double largura;
    private double comprimento;
    private double area;
    private double valorUnitario;
    private double valorTotal;
    private String observacoes;

    public ItemOrcamento(Material material, Servico servico, double quantidade,
                         double largura, double comprimento, String observacoes) {
        this.material = material;
        this.servico = servico;
        this.quantidade = quantidade;
        this.largura = largura;
        this.comprimento = comprimento;
        this.observacoes = observacoes;
        calcularArea();
        calcularValores();
    }

    private void calcularArea() {
        this.area = (largura / 1000) * (comprimento / 1000) * quantidade; // converte mm para metros
    }

    private void calcularValores() {
        double precoMaterial = material != null ? material.getPrecoPorMetroQuadrado() * area : 0;
        double precoServico = servico != null ? servico.getPrecoUnitario() * quantidade : 0;
        this.valorUnitario = precoMaterial + precoServico;
        this.valorTotal = this.valorUnitario * quantidade;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Material getMaterial() { return material; }
    public void setMaterial(Material material) {
        this.material = material;
        calcularValores();
    }

    public Servico getServico() { return servico; }
    public void setServico(Servico servico) {
        this.servico = servico;
        calcularValores();
    }

    public double getQuantidade() { return quantidade; }
    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
        calcularArea();
        calcularValores();
    }

    public double getLargura() { return largura; }
    public void setLargura(double largura) {
        this.largura = largura;
        calcularArea();
        calcularValores();
    }

    public double getComprimento() { return comprimento; }
    public void setComprimento(double comprimento) {
        this.comprimento = comprimento;
        calcularArea();
        calcularValores();
    }

    public double getArea() { return area; }

    public double getValorUnitario() { return valorUnitario; }

    public double getValorTotal() { return valorTotal; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    @Override
    public String toString() {
        return String.format("%s - %.2f x %.2f mm - Área: %.3f m² - Total: R$ %.2f",
                material != null ? material.getNome() : "Sem material",
                largura, comprimento, area, valorTotal);
    }
}