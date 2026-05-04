package br.com.marmoraria.view;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Orcamento;
import br.com.marmoraria.util.GerenciadorArquivos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardView extends BorderPane {

    private List<Orcamento> orcamentos;

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
        setStyle("-fx-background-color: #eef2f7;");

        carregarOrcamentos();

        VBox cabecalho = criarCabecalho();
        GridPane cardsResumo = criarCardsResumo();
        HBox filtros = criarFiltros();
        TabPane tabPane = criarGraficos();

        VBox mainContent = new VBox(20, cabecalho, cardsResumo, filtros, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        setCenter(mainContent);
        atualizarDashboard();
    }

    private VBox criarCabecalho() {
        VBox box = new VBox(4);

        Label titulo = new Label("Dashboard");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        Label subtitulo = new Label("Acompanhe faturamento, volume de orcamentos e materiais mais recorrentes.");
        subtitulo.setStyle("-fx-font-size: 12px; -fx-text-fill: #6b7280;");
        subtitulo.setWrapText(true);

        box.getChildren().addAll(titulo, subtitulo);
        return box;
    }

    private void carregarOrcamentos() {
        orcamentos = GerenciadorArquivos.listarOrcamentos();
        if (orcamentos == null) {
            orcamentos = new ArrayList<>();
        }
    }

    private GridPane criarCardsResumo() {
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);

        VBox card1 = criarCardResumo("Total de orcamentos", "0", "#2563eb");
        lblTotalOrcamentos = (Label) ((VBox) card1.getChildren().get(1)).getChildren().get(0);

        VBox card2 = criarCardResumo("Faturamento total", "R$ 0,00", "#16a34a");
        lblTotalFaturamento = (Label) ((VBox) card2.getChildren().get(1)).getChildren().get(0);

        VBox card3 = criarCardResumo("Ticket medio", "R$ 0,00", "#ea580c");
        lblMediaOrcamento = (Label) ((VBox) card3.getChildren().get(1)).getChildren().get(0);

        VBox card4 = criarCardResumo("Material mais usado", "Nenhum", "#7c3aed");
        lblMateriaisMaisUsados = (Label) ((VBox) card4.getChildren().get(1)).getChildren().get(0);

        grid.add(card1, 0, 0);
        grid.add(card2, 1, 0);
        grid.add(card3, 2, 0);
        grid.add(card4, 3, 0);
        return grid;
    }

    private VBox criarCardResumo(String titulo, String valorInicial, String cor) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(16));
        card.setPrefWidth(220);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(15,23,42,0.06), 8, 0, 0, 2);");

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #6b7280;");
        lblTitulo.setWrapText(true);

        VBox valorBox = new VBox();
        valorBox.setAlignment(Pos.CENTER_LEFT);

        Label lblValor = new Label(valorInicial);
        lblValor.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + cor + ";");
        lblValor.setWrapText(true);

        valorBox.getChildren().add(lblValor);
        card.getChildren().addAll(lblTitulo, valorBox);
        return card;
    }

    private HBox criarFiltros() {
        HBox filtros = new HBox(12);
        filtros.setAlignment(Pos.CENTER_LEFT);
        filtros.setPadding(new Insets(4, 0, 4, 0));
        filtros.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10; -fx-padding: 14;");

        Label lblPeriodo = new Label("Periodo");
        lblPeriodo.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");

        cbPeriodo = new ComboBox<>();
        cbPeriodo.getItems().addAll("Todos", "Este Mes", "Ultimos 30 Dias", "Este Ano");
        cbPeriodo.setValue("Todos");
        cbPeriodo.setPrefWidth(150);
        cbPeriodo.setOnAction(e -> atualizarDashboard());

        Label lblTipo = new Label("Grafico em destaque");
        lblTipo.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");

        cbTipoGrafico = new ComboBox<>();
        cbTipoGrafico.getItems().addAll("Barras", "Pizza", "Linha");
        cbTipoGrafico.setValue("Barras");
        cbTipoGrafico.setPrefWidth(150);
        cbTipoGrafico.setOnAction(e -> atualizarGraficos());

        Button btnAtualizar = new Button("Atualizar");
        btnAtualizar.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 14;");
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
        tabPane.setPrefHeight(540);
        tabPane.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        Tab tabFaturamento = new Tab("Faturamento mensal");
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Valor (R$)");
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Faturamento por mes");
        tabFaturamento.setContent(criarContainerGrafico(barChart));

        Tab tabMateriais = new Tab("Materiais mais vendidos");
        pieChart = new PieChart();
        pieChart.setTitle("Participacao por material");
        pieChart.setLabelsVisible(true);
        tabMateriais.setContent(criarContainerGrafico(pieChart));

        Tab tabEvolucao = new Tab("Evolucao temporal");
        CategoryAxis xAxisLine = new CategoryAxis();
        NumberAxis yAxisLine = new NumberAxis();
        yAxisLine.setLabel("Valor (R$)");
        lineChart = new LineChart<>(xAxisLine, yAxisLine);
        lineChart.setTitle("Evolucao do faturamento");
        tabEvolucao.setContent(criarContainerGrafico(lineChart));

        tabPane.getTabs().addAll(tabFaturamento, tabMateriais, tabEvolucao);
        return tabPane;
    }

    private VBox criarContainerGrafico(javafx.scene.Node grafico) {
        VBox box = new VBox(grafico);
        box.setPadding(new Insets(14));
        VBox.setVgrow(grafico, Priority.ALWAYS);
        return box;
    }

    private void atualizarDashboard() {
        List<Orcamento> orcamentosFiltrados = filtrarPorPeriodo(orcamentos);
        atualizarCardsResumo(orcamentosFiltrados);
        atualizarGraficos();
    }

    private List<Orcamento> filtrarPorPeriodo(List<Orcamento> lista) {
        String periodo = cbPeriodo.getValue();
        LocalDateTime agora = LocalDateTime.now();

        return lista.stream()
                .filter(o -> {
                    if ("Todos".equals(periodo)) return true;
                    if ("Este Mes".equals(periodo)) {
                        return o.getDataCriacao().getMonth() == agora.getMonth() &&
                                o.getDataCriacao().getYear() == agora.getYear();
                    }
                    if ("Ultimos 30 Dias".equals(periodo)) {
                        return o.getDataCriacao().isAfter(agora.minusDays(30));
                    }
                    if ("Este Ano".equals(periodo)) {
                        return o.getDataCriacao().getYear() == agora.getYear();
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    private void atualizarCardsResumo(List<Orcamento> orcamentosFiltrados) {
        int totalOrcamentos = orcamentosFiltrados.size();
        lblTotalOrcamentos.setText(String.valueOf(totalOrcamentos));

        double faturamentoTotal = orcamentosFiltrados.stream()
                .mapToDouble(Orcamento::getTotalFinal)
                .sum();
        lblTotalFaturamento.setText(String.format("R$ %.2f", faturamentoTotal));

        double ticketMedio = totalOrcamentos > 0 ? faturamentoTotal / totalOrcamentos : 0;
        lblMediaOrcamento.setText(String.format("R$ %.2f", ticketMedio));

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

        if ("Barras".equals(tipo)) {
            atualizarGraficoBarras();
        } else if ("Pizza".equals(tipo)) {
            atualizarGraficoPizza();
        } else if ("Linha".equals(tipo)) {
            atualizarGraficoLinha();
        }
    }

    private void atualizarGraficoBarras() {
        Map<String, Double> faturamentoPorMes = new LinkedHashMap<>();

        for (Orcamento o : orcamentos) {
            String mes = o.getDataCriacao().format(DateTimeFormatter.ofPattern("MMM/yyyy"));
            faturamentoPorMes.put(mes, faturamentoPorMes.getOrDefault(mes, 0.0) + o.getTotalFinal());
        }

        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(faturamentoPorMes.entrySet());
        sortedEntries.sort(Map.Entry.comparingByKey());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Faturamento");

        for (Map.Entry<String, Double> entry : sortedEntries) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().clear();
        barChart.getData().add(series);
    }

    private void atualizarGraficoPizza() {
        Map<String, Integer> contagemMateriais = new HashMap<>();

        for (Orcamento o : orcamentos) {
            for (ItemOrcamento item : o.getItens()) {
                String nome = item.getMaterial().getNome();
                contagemMateriais.put(nome, contagemMateriais.getOrDefault(nome, 0) + item.getQuantidade());
            }
        }

        List<Map.Entry<String, Integer>> topMateriais = new ArrayList<>(contagemMateriais.entrySet());
        topMateriais.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        if (topMateriais.size() > 5) {
            topMateriais = topMateriais.subList(0, 5);
        }

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
        Map<String, Double> faturamentoPorMes = new LinkedHashMap<>();

        for (Orcamento o : orcamentos) {
            String mes = o.getDataCriacao().format(DateTimeFormatter.ofPattern("MMM/yyyy"));
            faturamentoPorMes.put(mes, faturamentoPorMes.getOrDefault(mes, 0.0) + o.getTotalFinal());
        }

        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(faturamentoPorMes.entrySet());
        sortedEntries.sort(Map.Entry.comparingByKey());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Evolucao do faturamento");

        for (Map.Entry<String, Double> entry : sortedEntries) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        lineChart.getData().clear();
        lineChart.getData().add(series);
    }
}
