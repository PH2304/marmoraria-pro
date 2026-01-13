package br.com.marmoraria.service;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Material;
import br.com.marmoraria.model.Servico;

public class CalculadoraService {

    public static double calcularArea(double larguraMM, double comprimentoMM, int quantidade) {
        // Converte mm para metros e calcula área
        double larguraM = larguraMM / 1000;
        double comprimentoM = comprimentoMM / 1000;
        return larguraM * comprimentoM * quantidade;
    }

    public static double calcularCustoMaterial(Material material, double areaM2) {
        if (material == null) return 0;
        return material.getPrecoPorMetroQuadrado() * areaM2;
    }

    public static double calcularCustoServico(Servico servico, double quantidade) {
        if (servico == null) return 0;
        return servico.getPrecoUnitario() * quantidade;
    }

    public static double calcularValorTotal(ItemOrcamento item) {
        double area = calcularArea(item.getLargura(), item.getComprimento(), (int) item.getQuantidade());
        double custoMaterial = calcularCustoMaterial(item.getMaterial(), area);
        double custoServico = calcularCustoServico(item.getServico(), item.getQuantidade());
        return custoMaterial + custoServico;
    }

    public static double calcularMetragemLinear(double comprimentoMM, int quantidade) {
        return (comprimentoMM / 1000) * quantidade;
    }

    public static double calcularPerimetro(double larguraMM, double comprimentoMM, int quantidade) {
        return (2 * (larguraMM + comprimentoMM) / 1000) * quantidade;
    }

    public static double aplicarMargemLucro(double valorCusto, double margemPercentual) {
        return valorCusto * (1 + (margemPercentual / 100));
    }
}
