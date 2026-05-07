package br.com.marmoraria.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Orcamento implements Serializable {
    private String id;
    private String numeroOrcamento;
    private LocalDateTime dataCriacao;
    private String clienteNome;
    private String clienteTelefone;
    private String clienteEmail;
    private String enderecoObra;
    private String responsavel;
    private double margemLucro;
    private List<ItemOrcamento> itens;
    private double valorTotal;
    private double valorComLucro;
    private double frete;
    private double credito;
    private boolean incluirMaoDeObra;
    private double percentualMaoDeObra;
    private double valorMinimoMaoDeObra;
    private double valorMaoDeObra;
    private double totalFinal;
    private String observacoes;
    private String status;

    public Orcamento() {
        this.id = UUID.randomUUID().toString();
        this.dataCriacao = LocalDateTime.now();  // ← IMPORTANTE: inicializar aqui
        this.numeroOrcamento = gerarNumeroOrcamento();
        this.itens = new ArrayList<>();
        this.status = "Rascunho";
        this.margemLucro = 30.0;
        this.frete = 0.0;
        this.credito = 0.0;
        this.incluirMaoDeObra = false;
        this.percentualMaoDeObra = 0.30;
        this.valorMinimoMaoDeObra = 1200.0;
        this.valorMaoDeObra = 0.0;
        this.valorTotal = 0.0;
        this.valorComLucro = 0.0;
        this.totalFinal = 0.0;
    }

    private String gerarNumeroOrcamento() {
        // Garantir que dataCriacao não seja null
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "ORC-" + dataCriacao.format(formatter);
    }

    public void adicionarItem(ItemOrcamento item) {
        if (itens == null) {
            itens = new ArrayList<>();
        }
        itens.add(item);
        calcularTotais();
    }

    public void removerItem(ItemOrcamento item) {
        if (itens != null) {
            itens.remove(item);
            calcularTotais();
        }
    }

    public void calcularTotais() {
        if (itens == null) {
            itens = new ArrayList<>();
        }

        this.valorTotal = itens.stream()
                .mapToDouble(ItemOrcamento::getValorTotal)
                .sum();

        this.valorComLucro = valorTotal * (1 + (margemLucro / 100));
        this.valorMaoDeObra = incluirMaoDeObra
                ? Math.max(valorComLucro * percentualMaoDeObra, valorMinimoMaoDeObra)
                : 0.0;
        this.totalFinal = valorComLucro + frete + valorMaoDeObra - credito;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumeroOrcamento() {
        return numeroOrcamento;
    }

    public void setNumeroOrcamento(String numeroOrcamento) {
        this.numeroOrcamento = numeroOrcamento;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public String getClienteTelefone() {
        return clienteTelefone;
    }

    public void setClienteTelefone(String clienteTelefone) {
        this.clienteTelefone = clienteTelefone;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }

    public String getEnderecoObra() {
        return enderecoObra;
    }

    public void setEnderecoObra(String enderecoObra) {
        this.enderecoObra = enderecoObra;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public double getMargemLucro() {
        return margemLucro;
    }

    public void setMargemLucro(double margemLucro) {
        this.margemLucro = margemLucro;
        calcularTotais();
    }

    public List<ItemOrcamento> getItens() {
        if (itens == null) {
            itens = new ArrayList<>();
        }
        return itens;
    }

    public void setItens(List<ItemOrcamento> itens) {
        this.itens = itens;
        calcularTotais();
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getValorComLucro() {
        return valorComLucro;
    }

    public void setValorComLucro(double valorComLucro) {
        this.valorComLucro = valorComLucro;
    }

    public double getFrete() {
        return frete;
    }

    public void setFrete(double frete) {
        this.frete = frete;
        calcularTotais();
    }

    public double getCredito() {
        return credito;
    }

    public void setCredito(double credito) {
        this.credito = credito;
        calcularTotais();
    }

    public boolean isIncluirMaoDeObra() {
        return incluirMaoDeObra;
    }

    public void setIncluirMaoDeObra(boolean incluirMaoDeObra) {
        this.incluirMaoDeObra = incluirMaoDeObra;
        calcularTotais();
    }

    public double getPercentualMaoDeObra() {
        return percentualMaoDeObra;
    }

    public void setPercentualMaoDeObra(double percentualMaoDeObra) {
        this.percentualMaoDeObra = percentualMaoDeObra;
        calcularTotais();
    }

    public double getValorMinimoMaoDeObra() {
        return valorMinimoMaoDeObra;
    }

    public void setValorMinimoMaoDeObra(double valorMinimoMaoDeObra) {
        this.valorMinimoMaoDeObra = valorMinimoMaoDeObra;
        calcularTotais();
    }

    public double getValorMaoDeObra() {
        return valorMaoDeObra;
    }

    public double getTotalFinal() {
        return totalFinal;
    }

    public FechamentoOrcamento getFechamento() {
        return new FechamentoOrcamento(valorTotal, valorComLucro, valorMaoDeObra, totalFinal);
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getValorLucro() {
        return valorComLucro - valorTotal;
    }

    public String getDataFormatada() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dataCriacao.format(formatter);
    }
}
