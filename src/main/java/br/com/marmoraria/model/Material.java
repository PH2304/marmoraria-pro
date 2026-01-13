package br.com.marmoraria.model;

import java.io.Serializable;

public class Material implements Serializable {
    private String id;
    private String nome;
    private String tipo; // Mármore, Granito, Quartzo, etc.
    private double precoPorMetroQuadrado;
    private double espessura; // em mm
    private String origem;
    private String descricao;

    public Material(String id, String nome, String tipo, double precoPorMetroQuadrado,
                    double espessura, String origem, String descricao) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.precoPorMetroQuadrado = precoPorMetroQuadrado;
        this.espessura = espessura;
        this.origem = origem;
        this.descricao = descricao;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getPrecoPorMetroQuadrado() { return precoPorMetroQuadrado; }
    public void setPrecoPorMetroQuadrado(double precoPorMetroQuadrado) {
        this.precoPorMetroQuadrado = precoPorMetroQuadrado;
    }

    public double getEspessura() { return espessura; }
    public void setEspessura(double espessura) { this.espessura = espessura; }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override
    public String toString() {
        return nome + " (" + tipo + ") - R$ " + String.format("%.2f", precoPorMetroQuadrado) + "/m²";
    }
}