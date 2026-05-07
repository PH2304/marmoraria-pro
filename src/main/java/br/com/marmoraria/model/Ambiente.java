package br.com.marmoraria.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Representa um ambiente/cômodo da obra.
 * Permite organizar itens por ambiente (ex: Cozinha, Banho Master, etc.)
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class Ambiente {

    private String nome;
    private String materialPrincipal;
    private List<ItemOrcamento> itens;
    private TipoAmbiente tipo;

    public enum TipoAmbiente {
        COZINHA("Cozinha"),
        BANHO_SUITE("Banho Suíte"),
        BANHO_SOCIAL("Banho Social"),
        BANHO_MASTER("Banho Master"),
        AREA_SERVICO("Área de Serviço"),
        DESPENSA("Despensa"),
        SALA("Sala"),
        QUARTO("Quarto"),
        VARANDA("Varanda"),
        OUTRO("Outro");

        private final String descricao;
        TipoAmbiente(String descricao) { this.descricao = descricao; }
        public String getDescricao() { return descricao; }

        @Override
        public String toString() { return descricao; }
    }

    public Ambiente(String nome) {
        this.nome = nome;
        this.itens = new ArrayList<>();
        this.tipo = TipoAmbiente.OUTRO;
    }

    public Ambiente(String nome, TipoAmbiente tipo) {
        this.nome = nome;
        this.tipo = tipo;
        this.itens = new ArrayList<>();
    }

    // ==================== GERENCIAMENTO DE ITENS ====================

    public void adicionarItem(ItemOrcamento item) {
        if (item != null) {
            itens.add(item);
        }
    }

    public void removerItem(ItemOrcamento item) {
        itens.remove(item);
    }

    public void removerItem(int index) {
        if (index >= 0 && index < itens.size()) {
            itens.remove(index);
        }
    }

    public void limparItens() {
        itens.clear();
    }

    // ==================== CÁLCULOS ====================

    /**
     * Calcula o subtotal do ambiente
     */
    public double getSubtotal() {
        return itens.stream()
                .mapToDouble(ItemOrcamento::getTotal)
                .sum();
    }

    /**
     * Calcula a área total em m²
     */
    public double getAreaTotal() {
        return itens.stream()
                .mapToDouble(ItemOrcamento::getArea)
                .sum();
    }

    /**
     * Calcula a quantidade total de peças
     */
    public int getQuantidadeTotalPecas() {
        return itens.stream()
                .mapToInt(ItemOrcamento::getQuantidade)
                .sum();
    }

    /**
     * Retorna os tipos de trabalho presentes no ambiente
     */
    public List<TipoTrabalho> getTiposTrabalhoPresentes() {
        return itens.stream()
                .map(ItemOrcamento::getTipoTrabalho)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Retorna os materiais usados no ambiente
     */
    public List<String> getMateriaisUsados() {
        return itens.stream()
                .map(item -> item.getMaterial().getNome())
                .distinct()
                .collect(Collectors.toList());
    }

    // ==================== GETTERS E SETTERS ====================

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getMaterialPrincipal() { return materialPrincipal; }
    public void setMaterialPrincipal(String materialPrincipal) {
        this.materialPrincipal = materialPrincipal;
    }

    public List<ItemOrcamento> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public void setItens(List<ItemOrcamento> itens) {
        this.itens = new ArrayList<>(itens);
    }

    public TipoAmbiente getTipo() { return tipo; }
    public void setTipo(TipoAmbiente tipo) { this.tipo = tipo; }

    public int getQuantidadeItens() { return itens.size(); }

    // ==================== UTILITÁRIOS ====================

    /**
     * Retorna um resumo do ambiente
     */
    public String getResumo() {
        StringBuilder sb = new StringBuilder();
        sb.append(nome.toUpperCase()).append("\n");
        sb.append("-".repeat(nome.length())).append("\n");
        sb.append(String.format("Tipo: %s\n", tipo.getDescricao()));
        if (materialPrincipal != null && !materialPrincipal.isEmpty()) {
            sb.append(String.format("Material Principal: %s\n", materialPrincipal));
        }
        sb.append(String.format("Peças: %d\n", getQuantidadeTotalPecas()));
        sb.append(String.format("Área Total: %.2f m²\n", getAreaTotal()));
        sb.append(String.format("Subtotal: R$ %.2f\n", getSubtotal()));

        if (!itens.isEmpty()) {
            sb.append("\nItens:\n");
            for (int i = 0; i < itens.size(); i++) {
                ItemOrcamento item = itens.get(i);
                sb.append(String.format("  %d. %s - %s\n",
                        i + 1,
                        item.getTipoTrabalho().getDescricao(),
                        item.getDescricao()));
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("%s (%d itens - R$ %.2f)",
                nome, getQuantidadeItens(), getSubtotal());
    }
}