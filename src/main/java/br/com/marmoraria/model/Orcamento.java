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
    private double margemLucro; // em porcentagem
    private List<ItemOrcamento> itens;
    private double valorTotal;
    private double valorComLucro;
    private String observacoes;
    private String status; // Rascunho, Pendente, Aprovado, Recusado

    public Orcamento() {
        this.id = UUID.randomUUID().toString();
        this.numeroOrcamento = gerarNumeroOrcamento();
        this.dataCriacao = LocalDateTime.now();
        this.itens = new ArrayList<>();
        this.status = "Rascunho";
        this.margemLucro = 30.0; // 30% de margem padrão
    }

    private String gerarNumeroOrcamento() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "ORC-" + dataCriacao.format(formatter);
    }

    public void adicionarItem(ItemOrcamento item) {
        itens.add(item);
        calcularTotais();
    }

    public void removerItem(ItemOrcamento item) {
        itens.remove(item);
        calcularTotais();
    }

    public void calcularTotais() {
        this.valorTotal = itens.stream()
                .mapToDouble(ItemOrcamento::getValorTotal)
                .sum();

        this.valorComLucro = valorTotal * (1 + (margemLucro / 100));
    }

    // Getters e Setters
    public String getId() { return id; }

    public String getNumeroOrcamento() { return numeroOrcamento; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }

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

    public double getMargemLucro() { return margemLucro; }
    public void setMargemLucro(double margemLucro) {
        this.margemLucro = margemLucro;
        calcularTotais();
    }

    public List<ItemOrcamento> getItens() { return itens; }

    public double getValorTotal() { return valorTotal; }

    public double getValorComLucro() { return valorComLucro; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getValorLucro() {
        return valorComLucro - valorTotal;
    }

    public String getDataFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dataCriacao.format(formatter);
    }
}