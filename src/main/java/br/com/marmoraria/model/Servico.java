package br.com.marmoraria.model;

import java.io.Serializable;

public class Servico implements Serializable {
    private String id;
    private String descricao;
    private double precoUnitario;
    private String unidadeMedida; // m, m², unidade, etc.
    private String categoria; // Corte, Polimento, Instalação, etc.

    public Servico(String id, String descricao, double precoUnitario,
                   String unidadeMedida, String categoria) {
        this.id = id;
        this.descricao = descricao;
        this.precoUnitario = precoUnitario;
        this.unidadeMedida = unidadeMedida;
        this.categoria = categoria;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(double precoUnitario) { this.precoUnitario = precoUnitario; }

    public String getUnidadeMedida() { return unidadeMedida; }
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return descricao + " - R$ " + String.format("%.2f", precoUnitario) + " por " + unidadeMedida;
    }
}
