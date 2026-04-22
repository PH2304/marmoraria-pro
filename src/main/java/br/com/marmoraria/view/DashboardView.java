package br.com.marmoraria.view;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Orcamento;
import br.com.marmoraria.util.GerenciadorArquivos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardView extends BorderPane {

    private List<Orcamento> orcamentos;

    // Componentes do dashboard
    private Label lblTotalOrcamentos;
    private Label lblTotalFaturamento;
    private Label lblMediaOrcamento;
    private Label lblMateriaisMaisUsados;

    private BarChart<String, Number> barChart;
    private PieChart pieChart;
    private LineChart<String, Number> lineChart;

    private ComboBox<String> cbPeriodo;
    private ComboBox<String> cbTipoGrafico;

    public DashboardView() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #ecf0f1;");

        // Carregar orçamentos
        carregarOrcamentos();

        // Título
        Label titulo = new Label("📊 Dashboard de Estatísticas");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Cards de resumo
        GridPane cardsResumo = criarCardsResumo();

        // Filtros
        HBox filtros = criarFiltros();

        // Gráficos
        TabPane tabPane = criarGraficos();

        // Layout
        VBox mainContent = new VBox(20);
        mainContent.getChildren().addAll(titulo, cardsResumo, filtros, tabPane);

        setCenter(mainContent);

        // Atualizar dados
        atualizarDashboard();
    }

    private void carregarOrcamentos() {
        orcamentos = GerenciadorArquivos.listarOrcamentos();
        if (orcamentos == null) {
            orcamentos = new ArrayList<>();
        }
    }

    private GridPane criarCardsResumo() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(0, 0, 20, 0));

        // Card 1: Total de Orçamentos
        VBox card1 = criarCardResumo("📋 Total de Orçamentos", "0", "#3498db");
        lblTotalOrcamentos = (Label) ((VBox) card1.getChildren().get(1)).getChildren().get(0);

        // Card 2: Faturamento Total
        VBox card2 = criarCardResumo("💰 Faturamento Total", "R$ 0,00", "#27ae60");
        lblTotalFaturamento = (Label) ((VBox) card2.getChildren().get(1)).getChildren().get(0);

        // Card 3: Ticket Médio
        VBox card3 = criarCardResumo("🎫 Ticket Médio", "R$ 0,00", "#e67e22");
        lblMediaOrcamento = (Label) ((VBox) card3.getChildren().get(1)).getChildren().get(0);

        // Card 4: Material Mais Usado
        VBox card4 = criarCardResumo("🏆 Material Mais Usado", "Nenhum", "#9b59b6");
        lblMateriaisMaisUsados = (Label) ((VBox) card4.getChildren().get(1)).getChildren().get(0);

        grid.add(card1, 0, 0);
        grid.add(card2, 1, 0);
        grid.add(card3, 2, 0);
        grid.add(card4, 3, 0);

        return grid;
    }

    private VBox criarCardResumo(String titulo, String valorInicial, String cor) {
        VBox card = new VBox(10);
        card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 16px; " +
                        "-fx-padding: 20px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        card.setPrefWidth(200);

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        VBox valorBox = new VBox();
        valorBox.setAlignment(Pos.CENTER);

        Label lblValor = new Label(valorInicial);
        lblValor.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + cor + ";");

        valorBox.getChildren().add(lblValor);

        card.getChildren().addAll(lblTitulo, valorBox);
        return card;
    }

    private HBox criarFiltros() {
        HBox filtros = new HBox(15);
        filtros.setAlignment(Pos.CENTER_LEFT);
        filtros.setPadding(new Insets(10, 0, 10, 0));

        Label lblPeriodo = new Label("Período:");
        lblPeriodo.setStyle("-fx-font-weight: bold;");

        cbPeriodo = new ComboBox<>();
        cbPeriodo.getItems().addAll("Todos", "Este Mês", "Últimos 30 Dias", "Este Ano");
        cbPeriodo.setValue("Todos");
        cbPeriodo.setPrefWidth(150);
        cbPeriodo.setOnAction(e -> atualizarDashboard());

        Label lblTipo = new Label("Tipo de Gráfico:");
        lblTipo.setStyle("-fx-font-weight: bold;");

        cbTipoGrafico = new ComboBox<>();
        cbTipoGrafico.getItems().addAll("Barras", "Pizza", "Linha");
        cbTipoGrafico.setValue("Barras");
        cbTipoGrafico.setPrefWidth(150);
        cbTipoGrafico.setOnAction(e -> atualizarGraficos());

        Button btnAtualizar = new Button("🔄 Atualizar");
        btnAtualizar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAtualizar.setOnAction(e -> {
            carregarOrcamentos();
            atualizarDashboard();
        });

        filtros.getChildren().addAll(lblPeriodo, cbPeriodo, lblTipo, cbTipoGrafico, btnAtualizar);
        return filtros;
    }

    private TabPane criarGraficos() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setPrefHeight(500);

        // Aba: Faturamento por Mês
        Tab tabFaturamento = new Tab("💰 Faturamento Mensal");
        tabFaturamento.setClosable(false);

        // Criar gráfico de barras
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Valor (R$)");
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Faturamento por Mês");
        barChart.setAnimated(true);

        tabFaturamento.setContent(barChart);

        // Aba: Materiais Mais Vendidos
        Tab tabMateriais = new Tab("📦 Materiais Mais Vendidos");
        tabMateriais.setClosable(false);

        pieChart = new PieChart();
        pieChart.setTitle("Materiais Mais Vendidos");
        pieChart.setAnimated(true);
        pieChart.setLabelLineLength(10);
        pieChart.setLabelsVisible(true);

        tabMateriais.setContent(pieChart);

        // Aba: Evolução Temporal
        Tab tabEvolucao = new Tab("📈 Evolução Temporal");
        tabEvolucao.setClosable(false);

        CategoryAxis xAxisLine = new CategoryAxis();
        NumberAxis yAxisLine = new NumberAxis();
        yAxisLine.setLabel("Valor (R$)");
        lineChart = new LineChart<>(xAxisLine, yAxisLine);
        lineChart.setTitle("Evolução do Faturamento");
        lineChart.setAnimated(true);

        tabEvolucao.setContent(lineChart);

        tabPane.getTabs().addAll(tabFaturamento, tabMateriais, tabEvolucao);

        return tabPane;
    }

    private void atualizarDashboard() {
        // Filtrar orçamentos por período
        List<Orcamento> orcamentosFiltrados = filtrarPorPeriodo(orcamentos);

        // Atualizar cards
        atualizarCardsResumo(orcamentosFiltrados);

        // Atualizar gráficos
        atualizarGraficos();
    }

    private List<Orcamento> filtrarPorPeriodo(List<Orcamento> lista) {
        String periodo = cbPeriodo.getValue();
        LocalDateTime agora = LocalDateTime.now();

        return lista.stream()
                .filter(o -> {
                    if (periodo.equals("Todos")) return true;
                    if (periodo.equals("Este Mês")) {
                        return o.getDataCriacao().getMonth() == agora.getMonth() &&
                                o.getDataCriacao().getYear() == agora.getYear();
                    }
                    if (periodo.equals("Últimos 30 Dias")) {
                        return o.getDataCriacao().isAfter(agora.minusDays(30));
                    }
                    if (periodo.equals("Este Ano")) {
                        return o.getDataCriacao().getYear() == agora.getYear();
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    private void atualizarCardsResumo(List<Orcamento> orcamentosFiltrados) {
        // Total de orçamentos
        int totalOrcamentos = orcamentosFiltrados.size();
        lblTotalOrcamentos.setText(String.valueOf(totalOrcamentos));

        // Faturamento total
        double faturamentoTotal = orcamentosFiltrados.stream()
                .mapToDouble(Orcamento::getValorComLucro)
                .sum();
        lblTotalFaturamento.setText(String.format("R$ %.2f", faturamentoTotal));

        // Ticket médio
        double ticketMedio = totalOrcamentos > 0 ? faturamentoTotal / totalOrcamentos : 0;
        lblMediaOrcamento.setText(String.format("R$ %.2f", ticketMedio));

        // Material mais usado
        Map<String, Integer> contagemMateriais = new HashMap<>();
        for (Orcamento o : orcamentosFiltrados) {
            for (ItemOrcamento item : o.getItens()) {
                String nome = item.getMaterial().getNome();
                contagemMateriais.put(nome, contagemMateriais.getOrDefault(nome, 0) + item.getQuantidade());
            }
        }

        String materialMaisUsado = contagemMateriais.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Nenhum");

        lblMateriaisMaisUsados.setText(materialMaisUsado);
    }

    private void atualizarGraficos() {
        String tipo = cbTipoGrafico.getValue();

        if (tipo.equals("Barras")) {
            atualizarGraficoBarras();
        } else if (tipo.equals("Pizza")) {
            atualizarGraficoPizza();
        } else if (tipo.equals("Linha")) {
            atualizarGraficoLinha();
        }
    }

    private void atualizarGraficoBarras() {
        // Agrupar faturamento por mês
        Map<String, Double> faturamentoPorMes = new LinkedHashMap<>();

        for (Orcamento o : orcamentos) {
            String mes = o.getDataCriacao().format(DateTimeFormatter.ofPattern("MMM/yyyy"));
            faturamentoPorMes.put(mes, faturamentoPorMes.getOrDefault(mes, 0.0) + o.getValorComLucro());
        }

        // Ordenar por data
        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(faturamentoPorMes.entrySet());
        sortedEntries.sort(Map.Entry.comparingByKey());

        // Criar série de dados
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Faturamento");

        for (Map.Entry<String, Double> entry : sortedEntries) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().clear();
        barChart.getData().add(series);
    }

    private void atualizarGraficoPizza() {
        // Agrupar materiais mais vendidos
        Map<String, Integer> contagemMateriais = new HashMap<>();

        for (Orcamento o : orcamentos) {
            for (ItemOrcamento item : o.getItens()) {
                String nome = item.getMaterial().getNome();
                contagemMateriais.put(nome, contagemMateriais.getOrDefault(nome, 0) + item.getQuantidade());
            }
        }

        // Ordenar e pegar top 5 - VERSÃO JAVA 11
        List<Map.Entry<String, Integer>> topMateriais = new ArrayList<>(contagemMateriais.entrySet());
        topMateriais.sort((a, b) -> b.getValue().compareTo(a.getValue())); // Ordena decrescente

        // Pegar apenas os 5 primeiros
        if (topMateriais.size() > 5) {
            topMateriais = topMateriais.subList(0, 5);
        }

        // Criar dados do gráfico
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : topMateriais) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        if (topMateriais.isEmpty()) {
            pieChartData.add(new PieChart.Data("Nenhum dado", 1));
        }

        pieChart.setData(pieChartData);
    }

    private void atualizarGraficoLinha() {
        // Agrupar faturamento por mês
        Map<String, Double> faturamentoPorMes = new LinkedHashMap<>();

        for (Orcamento o : orcamentos) {
            String mes = o.getDataCriacao().format(DateTimeFormatter.ofPattern("MMM/yyyy"));
            faturamentoPorMes.put(mes, faturamentoPorMes.getOrDefault(mes, 0.0) + o.getValorComLucro());
        }

        // Ordenar por data
        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(faturamentoPorMes.entrySet());
        sortedEntries.sort(Map.Entry.comparingByKey());

        // Criar série de dados
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Evolução do Faturamento");

        for (Map.Entry<String, Double> entry : sortedEntries) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        lineChart.getData().clear();
        lineChart.getData().add(series);
    }
}