package br.com.marmoraria.model;

public class Servico {

    private String codigo;
    private String nome;
    private double preco;
    private String unidade;
    private String categoria;

    public Servico(String codigo, String nome, double preco, String unidade, String categoria) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.unidade = unidade;
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return nome;
    }
}
