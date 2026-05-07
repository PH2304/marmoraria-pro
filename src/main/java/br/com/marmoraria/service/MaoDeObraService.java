package br.com.marmoraria.service;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Orcamento;
import br.com.marmoraria.model.TipoTrabalho;

/**
 * Serviço de cálculo de mão de obra baseado na planilha Marmoraria Helomar.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class MaoDeObraService {

    // Constantes baseadas na planilha
    public static final double VALOR_MINIMO_MDO = 1200.00;
    public static final double PERCENTUAL_MDO_PADRAO = 0.30;
    public static final double VALOR_MINIMO_CUBA_ESCULPIDA = 1000.00;
    public static final double CUSTO_VISITA_TECNICA = 350.00;
    public static final double CUSTO_FURO_TORNEIRA = 50.00;
    public static final double TAXA_CANCELAMENTO = 0.30;

    /**
     * Tipos de cuba conforme planilha
     */
    public enum TipoCuba {
        TIPO_1_RASA("Tipo 1 Rasa", 100.00),
        TIPO_1_FUNDA("Tipo 1 Funda", 130.00),
        TIPO_2_RASA("Tipo 2 Rasa", 130.00),
        TIPO_2_FUNDA("Tipo 2 Funda", 190.00);

        private final String descricao;
        private final double custoUnitario;

        TipoCuba(String descricao, double custoUnitario) {
            this.descricao = descricao;
            this.custoUnitario = custoUnitario;
        }

        public String getDescricao() { return descricao; }
        public double getCustoUnitario() { return custoUnitario; }

        @Override
        public String toString() {
            return descricao + " (R$ " + String.format("%.2f", custoUnitario) + ")";
        }
    }

    /**
     * Tipos de pé de mesa
     */
    public enum TipoPeMesa {
        COMUM("Comum", 1.2),
        ENCAIXADO("Encaixado", 1.2),
        CAIXINHA("Caixinha", 1.3);

        private final String descricao;
        private final double fator;

        TipoPeMesa(String descricao, double fator) {
            this.descricao = descricao;
            this.fator = fator;
        }

        public String getDescricao() { return descricao; }
        public double getFator() { return fator; }
    }

    /**
     * Calcula mão de obra padrão (mín. R$ 1.200,00)
     */
    public static double calcularMaoDeObra(double totalMaterial, double percentualMDO) {
        double mdoCalculada = totalMaterial * percentualMDO;
        return Math.max(mdoCalculada, VALOR_MINIMO_MDO);
    }

    /**
     * Calcula mão de obra com valor mínimo opcional
     */
    public static double calcularMaoDeObra(double totalMaterial, double percentualMDO, double valorMinimo) {
        double mdoCalculada = totalMaterial * percentualMDO;
        return Math.max(mdoCalculada, valorMinimo);
    }

    /**
     * Calcula custo de cuba baseado no tipo e quantidade
     */
    public static double calcularCustoCuba(TipoCuba tipo, int quantidade) {
        return tipo.getCustoUnitario() * quantidade;
    }

    /**
     * Calcula custo de cuba esculpida (mín. R$ 1.000,00)
     */
    public static double calcularCustoCubaEsculpida(int quantidade, double custoAdicional) {
        double total = quantidade * custoAdicional;
        return Math.max(total, VALOR_MINIMO_CUBA_ESCULPIDA);
    }

    /**
     * Calcula custo de pé de mesa
     */
    public static double calcularCustoPeMesa(double areaPe, double precoMaterial,
                                             TipoPeMesa tipoPe) {
        return areaPe * precoMaterial * tipoPe.getFator();
    }

    /**
     * Calcula valor de cancelamento (30% do total)
     */
    public static double calcularValorCancelamento(double valorTotalOrcamento) {
        return valorTotalOrcamento * TAXA_CANCELAMENTO;
    }

    /**
     * Calcula valor de visita técnica
     */
    public static double getCustoVisitaTecnica() {
        return CUSTO_VISITA_TECNICA;
    }

    /**
     * Calcula custo de furo de torneira
     */
    public static double calcularCustoFuroTorneira(int quantidade) {
        return quantidade * CUSTO_FURO_TORNEIRA;
    }

    /**
     * Gera resumo da mão de obra para o contrato
     */
    public static String gerarResumoMaoDeObra(Orcamento orcamento) {
        StringBuilder sb = new StringBuilder();

        sb.append("RESUMO DA MÃO DE OBRA\n");
        sb.append("-".repeat(40)).append("\n");
        sb.append(String.format("Total Material: R$ %.2f\n", orcamento.getValorTotal()));
        sb.append(String.format("Margem: %.1f%%\n", orcamento.getMargemLucro()));
        sb.append(String.format("Total com Margem: R$ %.2f\n", orcamento.getValorComLucro()));

        if (orcamento.isIncluirMaoDeObra()) {
            sb.append(String.format("Mão de Obra (%.0f%%): R$ %.2f\n",
                    orcamento.getPercentualMaoDeObra() * 100,
                    orcamento.getValorMaoDeObra()));
            sb.append(String.format("(Valor mínimo: R$ %.2f)\n",
                    orcamento.getValorMinimoMaoDeObra()));
        }

        sb.append(String.format("Frete: R$ %.2f\n", orcamento.getFrete()));
        sb.append(String.format("Crédito: R$ %.2f\n", orcamento.getCredito()));
        sb.append("-".repeat(40)).append("\n");
        sb.append(String.format("TOTAL FINAL: R$ %.2f\n", orcamento.getTotalFinal()));

        return sb.toString();
    }

    /**
     * Verifica se um item é cuba esculpida
     */
    public static boolean isCubaEsculpida(ItemOrcamento item) {
        return item != null && item.getTipoTrabalho() == TipoTrabalho.CUBA_ESCULPIDA;
    }

    /**
     * Obtém o custo adicional de mão de obra para cuba esculpida
     */
    public static double getCustoAdicionalCubaEsculpida() {
        return VALOR_MINIMO_CUBA_ESCULPIDA;
    }

    // Getters para constantes
    public static double getValorMinimoMDO() { return VALOR_MINIMO_MDO; }
    public static double getPercentualPadrao() { return PERCENTUAL_MDO_PADRAO; }
}
