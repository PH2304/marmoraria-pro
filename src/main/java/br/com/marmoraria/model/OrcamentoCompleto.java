package br.com.marmoraria.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Orçamento completo com suporte a ambientes/cômodos.
 * Baseado na estrutura da planilha Marmoraria Helomar.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class OrcamentoCompleto {

    private String id;
    private String numeroOrcamento;
    private LocalDateTime dataCriacao;

    // Dados do cliente
    private String clienteNome;
    private String clienteTelefone;
    private String clienteEmail;
    private String enderecoObra;
    private String responsavel;

    // Ambientes
    private List<Ambiente> ambientes;

    // Configurações financeiras
    private double margemLucro;
    private double frete;
    private double credito;
    private boolean incluirMaoDeObra;
    private double percentualMaoDeObra;
    private double valorMinimoMaoDeObra;

    // Status
    private String status;
    private String observacoes;

    // Metadados
    private String vendedor;
    private String arquiteto;
    private String observacoesContrato;

    public OrcamentoCompleto() {
        this.id = UUID.randomUUID().toString();
        this.dataCriacao = LocalDateTime.now();
        this.numeroOrcamento = gerarNumeroOrcamento();
        this.ambientes = new ArrayList<>();
        this.status = "Rascunho";
        this.margemLucro = 30.0;
        this.frete = 0.0;
        this.credito = 0.0;
        this.incluirMaoDeObra = false;
        this.percentualMaoDeObra = 0.30;
        this.valorMinimoMaoDeObra = 1200.00;
    }

    private String gerarNumeroOrcamento() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "ORC-" + dataCriacao.format(formatter);
    }

    // ==================== AMBIENTES ====================

    public void adicionarAmbiente(Ambiente ambiente) {
        if (ambiente != null) {
            ambientes.add(ambiente);
        }
    }

    public void removerAmbiente(String nomeAmbiente) {
        ambientes.removeIf(a -> a.getNome().equalsIgnoreCase(nomeAmbiente));
    }

    public Optional<Ambiente> getAmbiente(String nome) {
        return ambientes.stream()
                .filter(a -> a.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }

    public List<Ambiente> getAmbientesPorTipo(Ambiente.TipoAmbiente tipo) {
        List<Ambiente> resultado = new ArrayList<>();
        for (Ambiente a : ambientes) {
            if (a.getTipo() == tipo) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    // ==================== CÁLCULOS ====================

    /**
     * Calcula o valor total de todos os itens (sem margem)
     */
    public double getValorTotalItens() {
        double total = 0.0;
        for (Ambiente a : ambientes) {
            total += a.getSubtotal();
        }
        return total;
    }

    /**
     * Calcula o valor total com margem de lucro
     */
    public double getValorComLucro() {
        return getValorTotalItens() * (1 + (margemLucro / 100));
    }

    /**
     * Calcula o valor da mão de obra
     */
    public double getValorMaoDeObra() {
        if (!incluirMaoDeObra) {
            return 0.0;
        }
        double mdoCalculada = getValorComLucro() * percentualMaoDeObra;
        return Math.max(mdoCalculada, valorMinimoMaoDeObra);
    }

    /**
     * Calcula o total final
     */
    public double getTotalFinal() {
        return getValorComLucro() + getValorMaoDeObra() + frete - credito;
    }

    /**
     * Calcula a área total em m²
     */
    public double getAreaTotal() {
        double area = 0.0;
        for (Ambiente a : ambientes) {
            area += a.getAreaTotal();
        }
        return area;
    }

    /**
     * Calcula a quantidade total de peças
     */
    public int getQuantidadeTotalPecas() {
        int qtd = 0;
        for (Ambiente a : ambientes) {
            qtd += a.getQuantidadeTotalPecas();
        }
        return qtd;
    }

    /**
     * Retorna todos os itens de todos os ambientes
     */
    public List<ItemOrcamento> getTodosItens() {
        List<ItemOrcamento> todosItens = new ArrayList<>();
        for (Ambiente a : ambientes) {
            todosItens.addAll(a.getItens());
        }
        return todosItens;
    }

    /**
     * Gera um resumo completo do orçamento
     */
    public String gerarResumo() {
        StringBuilder sb = new StringBuilder();

        sb.append("═".repeat(60)).append("\n");
        sb.append("ORÇAMENTO: ").append(numeroOrcamento).append("\n");
        sb.append("DATA: ").append(getDataFormatada()).append("\n");
        sb.append("STATUS: ").append(status).append("\n");
        sb.append("═".repeat(60)).append("\n\n");

        if (clienteNome != null && !clienteNome.isEmpty()) {
            sb.append("CLIENTE: ").append(clienteNome).append("\n");
            if (enderecoObra != null && !enderecoObra.isEmpty()) {
                sb.append("OBRA: ").append(enderecoObra).append("\n");
            }
            sb.append("\n");
        }

        // Resumo por ambiente
        for (Ambiente a : ambientes) {
            sb.append(a.getResumo()).append("\n\n");
        }

        // Totais
        sb.append("═".repeat(60)).append("\n");
        sb.append("TOTAIS:\n");
        sb.append(String.format("  Subtotal Itens: R$ %.2f\n", getValorTotalItens()));
        sb.append(String.format("  Margem (%.1f%%): R$ %.2f\n",
                margemLucro, getValorComLucro() - getValorTotalItens()));
        sb.append(String.format("  Total c/ Margem: R$ %.2f\n", getValorComLucro()));

        if (incluirMaoDeObra) {
            sb.append(String.format("  Mão de Obra: R$ %.2f\n", getValorMaoDeObra()));
        }

        sb.append(String.format("  Frete: R$ %.2f\n", frete));
        sb.append(String.format("  Crédito: R$ %.2f\n", credito));
        sb.append("═".repeat(60)).append("\n");
        sb.append(String.format("  TOTAL FINAL: R$ %.2f\n", getTotalFinal()));
        sb.append("═".repeat(60)).append("\n");

        if (observacoes != null && !observacoes.isEmpty()) {
            sb.append("\nOBSERVAÇÕES:\n");
            sb.append(observacoes).append("\n");
        }

        return sb.toString();
    }

    /**
     * Estatísticas do orçamento
     */
    public String getEstatisticas() {
        StringBuilder sb = new StringBuilder();

        sb.append("=== ESTATÍSTICAS DO ORÇAMENTO ===\n\n");
        sb.append("Ambientes: ").append(ambientes.size()).append("\n");
        sb.append("Total de peças: ").append(getQuantidadeTotalPecas()).append("\n");
        sb.append(String.format("Área total: %.2f m²\n", getAreaTotal()));
        sb.append(String.format("Valor médio por m²: R$ %.2f\n",
                getAreaTotal() > 0 ? getTotalFinal() / getAreaTotal() : 0));
        sb.append(String.format("Valor médio por peça: R$ %.2f\n",
                getQuantidadeTotalPecas() > 0 ? getTotalFinal() / getQuantidadeTotalPecas() : 0));

        // Materiais usados
        Set<String> materiais = new HashSet<>();
        for (Ambiente a : ambientes) {
            materiais.addAll(a.getMateriaisUsados());
        }
        sb.append("\nMateriais utilizados: ").append(materiais.size()).append("\n");
        for (String m : materiais) {
            sb.append("  • ").append(m).append("\n");
        }

        return sb.toString();
    }

    // ==================== GETTERS E SETTERS ====================

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNumeroOrcamento() { return numeroOrcamento; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }

    public String getDataFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dataCriacao.format(formatter);
    }

    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }

    public String getClienteTelefone() { return clienteTelefone; }
    public void setClienteTelefone(String clienteTelefone) { this.clienteTelefone = clienteTelefone; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public String getEnderecoObra() { return enderecoObra; }
    public void setEnderecoObra(String enderecoObra) { this.enderecoObra = enderecoObra; }

    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }

    public List<Ambiente> getAmbientes() { return Collections.unmodifiableList(ambientes); }

    public double getMargemLucro() { return margemLucro; }
    public void setMargemLucro(double margemLucro) { this.margemLucro = margemLucro; }

    public double getFrete() { return frete; }
    public void setFrete(double frete) { this.frete = frete; }

    public double getCredito() { return credito; }
    public void setCredito(double credito) { this.credito = credito; }

    public boolean isIncluirMaoDeObra() { return incluirMaoDeObra; }
    public void setIncluirMaoDeObra(boolean incluirMaoDeObra) { this.incluirMaoDeObra = incluirMaoDeObra; }

    public double getPercentualMaoDeObra() { return percentualMaoDeObra; }
    public void setPercentualMaoDeObra(double percentualMaoDeObra) { this.percentualMaoDeObra = percentualMaoDeObra; }

    public double getValorMinimoMaoDeObra() { return valorMinimoMaoDeObra; }
    public void setValorMinimoMaoDeObra(double valorMinimoMaoDeObra) { this.valorMinimoMaoDeObra = valorMinimoMaoDeObra; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getVendedor() { return vendedor; }
    public void setVendedor(String vendedor) { this.vendedor = vendedor; }

    public String getArquiteto() { return arquiteto; }
    public void setArquiteto(String arquiteto) { this.arquiteto = arquiteto; }

    public String getObservacoesContrato() { return observacoesContrato; }
    public void setObservacoesContrato(String observacoesContrato) { this.observacoesContrato = observacoesContrato; }
}