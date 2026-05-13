package br.com.marmoraria.service;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Orcamento;
import br.com.marmoraria.util.GerenciadorArquivos;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Serviço de busca avançada para orçamentos.
 * Filtros combinados, ordenação e pesquisa textual.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class BuscaAvancadaService {

    /**
     * Representa os critérios de busca
     */
    public static class CriteriosBusca {
        private String textoBusca;
        private String cliente;
        private String status;
        private String material;
        private LocalDateTime dataInicio;
        private LocalDateTime dataFim;
        private Double valorMinimo;
        private Double valorMaximo;
        private String ordenarPor = "data"; // data, valor, cliente
        private boolean ordemCrescente = false;
        private int pagina = 1;
        private int itensPorPagina = 10;

        // Getters e Setters
        public String getTextoBusca() { return textoBusca; }
        public void setTextoBusca(String textoBusca) { this.textoBusca = textoBusca; }
        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMaterial() { return material; }
        public void setMaterial(String material) { this.material = material; }
        public LocalDateTime getDataInicio() { return dataInicio; }
        public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
        public LocalDateTime getDataFim() { return dataFim; }
        public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
        public Double getValorMinimo() { return valorMinimo; }
        public void setValorMinimo(Double valorMinimo) { this.valorMinimo = valorMinimo; }
        public Double getValorMaximo() { return valorMaximo; }
        public void setValorMaximo(Double valorMaximo) { this.valorMaximo = valorMaximo; }
        public String getOrdenarPor() { return ordenarPor; }
        public void setOrdenarPor(String ordenarPor) { this.ordenarPor = ordenarPor; }
        public boolean isOrdemCrescente() { return ordemCrescente; }
        public void setOrdemCrescente(boolean ordemCrescente) { this.ordemCrescente = ordemCrescente; }
        public int getPagina() { return pagina; }
        public void setPagina(int pagina) { this.pagina = pagina; }
        public int getItensPorPagina() { return itensPorPagina; }
        public void setItensPorPagina(int itensPorPagina) { this.itensPorPagina = itensPorPagina; }
    }

    /**
     * Resultado da busca com paginação
     */
    public static class ResultadoBusca {
        private final List<Orcamento> orcamentos;
        private final int totalResultados;
        private final int paginaAtual;
        private final int totalPaginas;
        private final long tempoBuscaMs;

        public ResultadoBusca(List<Orcamento> orcamentos, int totalResultados,
                              int paginaAtual, int totalPaginas, long tempoBuscaMs) {
            this.orcamentos = orcamentos;
            this.totalResultados = totalResultados;
            this.paginaAtual = paginaAtual;
            this.totalPaginas = totalPaginas;
            this.tempoBuscaMs = tempoBuscaMs;
        }

        public List<Orcamento> getOrcamentos() { return orcamentos; }
        public int getTotalResultados() { return totalResultados; }
        public int getPaginaAtual() { return paginaAtual; }
        public int getTotalPaginas() { return totalPaginas; }
        public long getTempoBuscaMs() { return tempoBuscaMs; }
        public boolean hasProximaPagina() { return paginaAtual < totalPaginas; }
        public boolean hasPaginaAnterior() { return paginaAtual > 1; }
    }

    /**
     * Executa busca avançada com todos os critérios
     */
    public static ResultadoBusca buscar(CriteriosBusca criterios) {
        long inicio = System.currentTimeMillis();

        // Carregar todos os orçamentos
        List<Orcamento> todos = GerenciadorArquivos.carregarTodosOrcamentos();

        // Aplicar filtros
        List<Orcamento> filtrados = aplicarFiltros(todos, criterios);

        // Ordenar
        filtrados = ordenar(filtrados, criterios.getOrdenarPor(), criterios.isOrdemCrescente());

        // Paginar
        int total = filtrados.size();
        int totalPaginas = (int) Math.ceil((double) total / criterios.getItensPorPagina());
        int fromIndex = (criterios.getPagina() - 1) * criterios.getItensPorPagina();
        int toIndex = Math.min(fromIndex + criterios.getItensPorPagina(), total);

        List<Orcamento> pagina = fromIndex < total ?
                filtrados.subList(fromIndex, toIndex) : new ArrayList<>();

        long tempo = System.currentTimeMillis() - inicio;

        return new ResultadoBusca(pagina, total, criterios.getPagina(), totalPaginas, tempo);
    }

    /**
     * Busca rápida por texto
     */
    public static List<Orcamento> buscaRapida(String texto) {
        CriteriosBusca criterios = new CriteriosBusca();
        criterios.setTextoBusca(texto);
        criterios.setItensPorPagina(50);
        return buscar(criterios).getOrcamentos();
    }

    /**
     * Aplica todos os filtros
     */
    private static List<Orcamento> aplicarFiltros(List<Orcamento> orcamentos, CriteriosBusca c) {
        return orcamentos.stream()
                .filter(o -> filtroTexto(o, c.getTextoBusca()))
                .filter(o -> filtroCliente(o, c.getCliente()))
                .filter(o -> filtroStatus(o, c.getStatus()))
                .filter(o -> filtroMaterial(o, c.getMaterial()))
                .filter(o -> filtroData(o, c.getDataInicio(), c.getDataFim()))
                .filter(o -> filtroValor(o, c.getValorMinimo(), c.getValorMaximo()))
                .collect(Collectors.toList());
    }

    private static boolean filtroTexto(Orcamento o, String texto) {
        if (texto == null || texto.isEmpty()) return true;
        String t = texto.toLowerCase();
        return (o.getNumeroOrcamento() != null && o.getNumeroOrcamento().toLowerCase().contains(t)) ||
                (o.getClienteNome() != null && o.getClienteNome().toLowerCase().contains(t)) ||
                (o.getObservacoes() != null && o.getObservacoes().toLowerCase().contains(t));
    }

    private static boolean filtroCliente(Orcamento o, String cliente) {
        if (cliente == null || cliente.isEmpty()) return true;
        return o.getClienteNome() != null &&
                o.getClienteNome().toLowerCase().contains(cliente.toLowerCase());
    }

    private static boolean filtroStatus(Orcamento o, String status) {
        if (status == null || status.isEmpty() || "Todos".equals(status)) return true;
        return status.equalsIgnoreCase(o.getStatus());
    }

    private static boolean filtroMaterial(Orcamento o, String material) {
        if (material == null || material.isEmpty()) return true;
        return o.getItens().stream()
                .anyMatch(item -> item.getMaterial().getNome()
                        .toLowerCase().contains(material.toLowerCase()));
    }

    private static boolean filtroData(Orcamento o, LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null && fim == null) return true;
        if (o.getDataCriacao() == null) return false;

        if (inicio != null && o.getDataCriacao().isBefore(inicio)) return false;
        if (fim != null && o.getDataCriacao().isAfter(fim)) return false;

        return true;
    }

    private static boolean filtroValor(Orcamento o, Double min, Double max) {
        if (min == null && max == null) return true;

        double valor = o.getTotalFinal();
        if (min != null && valor < min) return false;
        if (max != null && valor > max) return false;

        return true;
    }

    /**
     * Ordena os resultados
     */
    private static List<Orcamento> ordenar(List<Orcamento> orcamentos, String ordenarPor, boolean crescente) {
        Comparator<Orcamento> comparator;

        switch (ordenarPor.toLowerCase()) {
            case "valor":
                comparator = Comparator.comparingDouble(Orcamento::getTotalFinal);
                break;
            case "cliente":
                comparator = Comparator.comparing(o ->
                        o.getClienteNome() != null ? o.getClienteNome().toLowerCase() : "");
                break;
            case "data":
            default:
                comparator = Comparator.comparing(Orcamento::getDataCriacao);
                break;
        }

        if (crescente) {
            return orcamentos.stream().sorted(comparator).collect(Collectors.toList());
        } else {
            return orcamentos.stream().sorted(comparator.reversed()).collect(Collectors.toList());
        }
    }

    /**
     * Retorna sugestões de busca baseadas no histórico
     */
    public static List<String> getSugestoes(String texto) {
        if (texto == null || texto.length() < 2) return Collections.emptyList();

        Set<String> sugestoes = new HashSet<>();
        List<Orcamento> todos = GerenciadorArquivos.carregarTodosOrcamentos();
        String t = texto.toLowerCase();

        for (Orcamento o : todos) {
            if (o.getClienteNome() != null && o.getClienteNome().toLowerCase().contains(t)) {
                sugestoes.add(o.getClienteNome());
            }
            for (ItemOrcamento item : o.getItens()) {
                if (item.getMaterial().getNome().toLowerCase().contains(t)) {
                    sugestoes.add(item.getMaterial().getNome());
                }
            }
            if (sugestoes.size() >= 8) break;
        }

        return new ArrayList<>(sugestoes);
    }

    /**
     * Retorna estatísticas de busca
     */
    public static Map<String, Long> getDistribuicaoStatus() {
        List<Orcamento> todos = GerenciadorArquivos.carregarTodosOrcamentos();
        return todos.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getStatus() != null ? o.getStatus() : "Sem status",
                        Collectors.counting()
                ));
    }
}