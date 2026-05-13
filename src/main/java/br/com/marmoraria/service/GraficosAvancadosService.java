package br.com.marmoraria.service;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Orcamento;
import br.com.marmoraria.util.GerenciadorArquivos;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Serviço de dados para gráficos avançados do Dashboard.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class GraficosAvancadosService {

    /**
     * Faturamento por mês (últimos 12 meses)
     */
    public static Map<String, Double> getFaturamentoMensal() {
        List<Orcamento> orcamentos = GerenciadorArquivos.carregarTodosOrcamentos();
        Map<String, Double> faturamento = new LinkedHashMap<>();

        // Inicializar últimos 12 meses
        java.time.LocalDate hoje = java.time.LocalDate.now();
        for (int i = 11; i >= 0; i--) {
            java.time.LocalDate data = hoje.minusMonths(i);
            String chave = data.getMonth().getDisplayName(TextStyle.SHORT, new Locale("pt", "BR")) +
                    "/" + (data.getYear() % 100);
            faturamento.put(chave, 0.0);
        }

        // Somar faturamento
        for (Orcamento o : orcamentos) {
            if (o.getDataCriacao() != null) {
                String chave = o.getDataCriacao().getMonth().getDisplayName(TextStyle.SHORT, new Locale("pt", "BR")) +
                        "/" + (o.getDataCriacao().getYear() % 100);
                faturamento.merge(chave, o.getTotalFinal(), Double::sum);
            }
        }

        return faturamento;
    }

    /**
     * Top materiais mais usados
     */
    public static Map<String, Integer> getTopMateriais(int limite) {
        List<Orcamento> orcamentos = GerenciadorArquivos.carregarTodosOrcamentos();
        Map<String, Integer> contagem = new HashMap<>();

        for (Orcamento o : orcamentos) {
            for (ItemOrcamento item : o.getItens()) {
                String nome = item.getMaterial().getNome();
                contagem.merge(nome, item.getQuantidade(), Integer::sum);
            }
        }

        return contagem.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limite)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    /**
     * Distribuição por tipo de trabalho
     */
    public static Map<String, Integer> getDistribuicaoTiposTrabalho() {
        List<Orcamento> orcamentos = GerenciadorArquivos.carregarTodosOrcamentos();
        Map<String, Integer> distribuicao = new HashMap<>();

        for (Orcamento o : orcamentos) {
            for (ItemOrcamento item : o.getItens()) {
                String tipo = item.getTipoTrabalho().getDescricao();
                distribuicao.merge(tipo, 1, Integer::sum);
            }
        }

        return distribuicao.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    /**
     * Ticket médio por mês
     */
    public static Map<String, Double> getTicketMedioMensal() {
        List<Orcamento> orcamentos = GerenciadorArquivos.carregarTodosOrcamentos();
        Map<String, List<Double>> valoresPorMes = new HashMap<>();

        for (Orcamento o : orcamentos) {
            if (o.getDataCriacao() != null) {
                String chave = o.getDataCriacao().getMonth().getDisplayName(TextStyle.SHORT, new Locale("pt", "BR")) +
                        "/" + (o.getDataCriacao().getYear() % 100);
                valoresPorMes.computeIfAbsent(chave, k -> new ArrayList<>())
                        .add(o.getTotalFinal());
            }
        }

        Map<String, Double> ticketMedio = new LinkedHashMap<>();
        valoresPorMes.forEach((mes, valores) -> {
            double media = valores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            ticketMedio.put(mes, media);
        });

        return ticketMedio;
    }

    /**
     * Resumo rápido para cards do dashboard
     */
    public static Map<String, Object> getResumoRapido() {
        List<Orcamento> orcamentos = GerenciadorArquivos.carregarTodosOrcamentos();
        Map<String, Object> resumo = new HashMap<>();

        resumo.put("totalOrcamentos", orcamentos.size());

        double faturamentoTotal = orcamentos.stream()
                .mapToDouble(Orcamento::getTotalFinal).sum();
        resumo.put("faturamentoTotal", faturamentoTotal);

        double ticketMedio = orcamentos.isEmpty() ? 0 :
                faturamentoTotal / orcamentos.size();
        resumo.put("ticketMedio", ticketMedio);

        long orcamentosEsteMes = orcamentos.stream()
                .filter(o -> o.getDataCriacao() != null &&
                        o.getDataCriacao().getMonth() == java.time.LocalDate.now().getMonth())
                .count();
        resumo.put("orcamentosEsteMes", orcamentosEsteMes);

        return resumo;
    }

    /**
     * Taxa de crescimento mensal
     */
    public static double getTaxaCrescimento() {
        List<Orcamento> orcamentos = GerenciadorArquivos.carregarTodosOrcamentos();
        java.time.LocalDate hoje = java.time.LocalDate.now();

        double mesAtual = orcamentos.stream()
                .filter(o -> o.getDataCriacao() != null &&
                        o.getDataCriacao().getMonth() == hoje.getMonth() &&
                        o.getDataCriacao().getYear() == hoje.getYear())
                .mapToDouble(Orcamento::getTotalFinal)
                .sum();

        double mesAnterior = orcamentos.stream()
                .filter(o -> o.getDataCriacao() != null &&
                        o.getDataCriacao().getMonth() == hoje.minusMonths(1).getMonth() &&
                        o.getDataCriacao().getYear() == hoje.minusMonths(1).getYear())
                .mapToDouble(Orcamento::getTotalFinal)
                .sum();

        if (mesAnterior == 0) return 0;
        return ((mesAtual - mesAnterior) / mesAnterior) * 100;
    }
}