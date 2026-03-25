package br.com.marmoraria.service;

public class CalculadoraService {

    public double calcularArea(double largura, double altura) {
        return (largura * altura) / 1000000;
    }

    public double calcularMaterial(double area, int qtd, double preco) {
        return area * qtd * preco;
    }

    public double calcularPerimetro(double largura, double altura) {
        return 2 * ((largura + altura) / 1000);
    }
}