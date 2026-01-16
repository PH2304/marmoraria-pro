package br.com.marmoraria.service;

public class CalculadoraService {

    private CalculadoraService() {}

    public static double calcularArea(
            double larguraMm,
            double comprimentoMm,
            int quantidade
    ) {
        return (larguraMm / 1000.0)
                * (comprimentoMm / 1000.0)
                * quantidade;
    }
}
