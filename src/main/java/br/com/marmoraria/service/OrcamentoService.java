package br.com.marmoraria.service;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Orcamento;

public class OrcamentoService {

    /**
     * Calcula o valor total do orçamento
     */
    public double calcularValorTotal(Orcamento orcamento) {
        if (orcamento == null || orcamento.getItens().isEmpty()) {
            return 0.0;
        }

        return orcamento.getItens()
                .stream()
                .mapToDouble(ItemOrcamento::getValorTotal)
                .sum();
    }

    /**
     * Calcula a área total (em m²) do orçamento
     */
    public double calcularAreaTotal(Orcamento orcamento) {
        if (orcamento == null || orcamento.getItens().isEmpty()) {
            return 0.0;
        }

        return orcamento.getItens()
                .stream()
                .mapToDouble(ItemOrcamento::getAreaM2)
                .sum();
    }

    /**
     * Calcula a quantidade total de peças
     */
    public int calcularQuantidadeTotal(Orcamento orcamento) {
        if (orcamento == null || orcamento.getItens().isEmpty()) {
            return 0;
        }

        return orcamento.getItens()
                .stream()
                .mapToInt(ItemOrcamento::getQuantidade)
                .sum();
    }

    /**
     * Adiciona um item ao orçamento
     */
    public void adicionarItem(Orcamento orcamento, ItemOrcamento item) {
        if (orcamento == null || item == null) {
            return;
        }
        orcamento.getItens().add(item);
    }

    /**
     * Remove um item do orçamento
     */
    public void removerItem(Orcamento orcamento, ItemOrcamento item) {
        if (orcamento == null || item == null) {
            return;
        }
        orcamento.getItens().remove(item);
    }
}
