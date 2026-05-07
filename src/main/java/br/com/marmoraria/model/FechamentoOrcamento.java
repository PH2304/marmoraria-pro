package br.com.marmoraria.model;

public class FechamentoOrcamento {

    private final double subtotalItens;
    private final double subtotalComMargem;
    private final double valorMaoDeObra;
    private final double totalFinal;

    public FechamentoOrcamento(double subtotalItens,
                               double subtotalComMargem,
                               double valorMaoDeObra,
                               double totalFinal) {
        this.subtotalItens = subtotalItens;
        this.subtotalComMargem = subtotalComMargem;
        this.valorMaoDeObra = valorMaoDeObra;
        this.totalFinal = totalFinal;
    }

    public double getSubtotalItens() {
        return subtotalItens;
    }

    public double getSubtotalComMargem() {
        return subtotalComMargem;
    }

    public double getValorMaoDeObra() {
        return valorMaoDeObra;
    }

    public double getTotalFinal() {
        return totalFinal;
    }
}
