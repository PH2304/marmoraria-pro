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
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DashboardView extends BorderPane {

    private static final Logger LOGGER = Logger.getLogger(DashboardView.class.getName());

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

        // Inicializar lista vazia
        orcamentos = new ArrayList<>();

        // Carregar dados
        carregarOrcamentos();

        VBox cabecalho = criarCabecalho();
        GridPane cardsResumo = criarCardsResumo();
        HBox filtros = criarFiltros();
        TabPane tabPane = criarGraficos();

        VBox mainContent = new VBox(20, cabecalho, cardsResumo, filtros, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        setCenter(mainContent);

        // Atualizar dashboard com dados carregados
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

    /**
     * Carrega orçamentos usando o GerenciadorArquivos unificado
     */
    private void carregarOrcamentos() {
        try {
            // Usar o método correto que retorna List<Orcamento>
            orcamentos = GerenciadorArquivos.carregarTodosOrcamentos();

            if (orcamentos == null) {
                orcamentos = new ArrayList<>();
                LOGGER.warning("carregarOrcamentos retornou null, inicializando lista vazia");
            }

            LOGGER.info("Dashboard carregou " + orcamentos.size() + " orçamentos");

        } catch (Exception e) {
            LOGGER.severe("Erro ao carregar orçamentos: " + e.getMessage());
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
        card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: #e5e7eb; " +
                        "-fx-border-radius: 10; " +
                        "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.06), 8, 0, 0, 2);"
        );

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
        filtros.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: #e5e7eb; " +
                        "-fx-border-radius: 10; " +
                        "-fx-padding: 14;"
        );

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
        btnAtualizar.setStyle(
                "-fx-background-color: #2563eb; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-padding: 10 14;"
        );
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
        tabPane.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: #e5e7eb; " +
                        "-fx-border-radius: 10;"
        );

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
        // CORREÇÃO: filtrarPorPeriodo agora recebe List<Orcamento>
        List<Orcamento> orcamentosFiltrados = filtrarPorPeriodo(orcamentos);
        atualizarCardsResumo(orcamentosFiltrados);
        atualizarGraficos();
    }

    /**
     * Filtra orçamentos por período
     * AGORA RECEBE List<Orcamento> em vez de List<String>
     */
    private List<Orcamento> filtrarPorPeriodo(List<Orcamento> lista) {
        if (lista == null || lista.isEmpty()) {
            return new ArrayList<>();
        }

        String periodo = cbPeriodo.getValue();
        if (periodo == null || "Todos".equals(periodo)) {
            return new ArrayList<>(lista);
        }

        LocalDateTime agora = LocalDateTime.now();

        return lista.stream()
                .filter(o -> {
                    if (o == null || o.getDataCriacao() == null) {
                        return false;
                    }

                    switch (periodo) {
                        case "Este Mes":
                            return o.getDataCriacao().getMonth() == agora.getMonth() &&
                                    o.getDataCriacao().getYear() == agora.getYear();

                        case "Ultimos 30 Dias":
                            return o.getDataCriacao().isAfter(agora.minusDays(30));

                        case "Este Ano":
                            return o.getDataCriacao().getYear() == agora.getYear();

                        default:
                            return true;
                    }
                })
                .collect(Collectors.toList());
    }

    private void atualizarCardsResumo(List<Orcamento> orcamentosFiltrados) {
        if (orcamentosFiltrados == null || orcamentosFiltrados.isEmpty()) {
            lblTotalOrcamentos.setText("0");
            lblTotalFaturamento.setText("R$ 0,00");
            lblMediaOrcamento.setText("R$ 0,00");
            lblMateriaisMaisUsados.setText("Nenhum");
            return;
        }

        int totalOrcamentos = orcamentosFiltrados.size();
        lblTotalOrcamentos.setText(String.valueOf(totalOrcamentos));

        double faturamentoTotal = orcamentosFiltrados.stream()
                .filter(o -> o != null)
                .mapToDouble(Orcamento::getTotalFinal)
                .sum();
        lblTotalFaturamento.setText(String.format("R$ %.2f", faturamentoTotal));

        double ticketMedio = totalOrcamentos > 0 ? faturamentoTotal / totalOrcamentos : 0;
        lblMediaOrcamento.setText(String.format("R$ %.2f", ticketMedio));

        // Encontrar material mais usado
        Map<String, Integer> contagemMateriais = new HashMap<>();
        for (Orcamento o : orcamentosFiltrados) {
            if (o != null && o.getItens() != null) {
                for (ItemOrcamento item : o.getItens()) {
                    if (item != null && item.getMaterial() != null) {
                        String nome = item.getMaterial().getNome();
                        contagemMateriais.merge(nome, item.getQuantidade(), Integer::sum);
                    }
                }
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
        if (tipo == null) return;

        switch (tipo) {
            case "Barras":
                atualizarGraficoBarras();
                break;
            case "Pizza":
                atualizarGraficoPizza();
                break;
            case "Linha":
                atualizarGraficoLinha();
                break;
        }
    }

    private void atualizarGraficoBarras() {
        if (orcamentos == null || orcamentos.isEmpty()) {
            barChart.getData().clear();
            return;
        }

        Map<String, Double> faturamentoPorMes = new LinkedHashMap<>();

        for (Orcamento o : orcamentos) {
            if (o != null && o.getDataCriacao() != null) {
                String mes = o.getDataCriacao().format(DateTimeFormatter.ofPattern("MMM/yyyy"));
                faturamentoPorMes.merge(mes, o.getTotalFinal(), Double::sum);
            }
        }

        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(faturamentoPorMes.entrySet());
        sortedEntries.sort(Map.Entry.comparingByKey());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Faturamento");

        for (Map.Entry<String, Double> entry : sortedEntries) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().clear();
        if (!series.getData().isEmpty()) {
            barChart.getData().add(series);
        }
    }

    private void atualizarGraficoPizza() {
        if (orcamentos == null || orcamentos.isEmpty()) {
            pieChart.setData(FXCollections.observableArrayList(
                    new PieChart.Data("Sem dados", 1)
            ));
            return;
        }

        Map<String, Integer> contagemMateriais = new HashMap<>();

        for (Orcamento o : orcamentos) {
            if (o != null && o.getItens() != null) {
                for (ItemOrcamento item : o.getItens()) {
                    if (item != null && item.getMaterial() != null) {
                        String nome = item.getMaterial().getNome();
                        contagemMateriais.merge(nome, item.getQuantidade(), Integer::sum);
                    }
                }
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

        if (pieChartData.isEmpty()) {
            pieChartData.add(new PieChart.Data("Nenhum dado", 1));
        }

        pieChart.setData(pieChartData);
    }

    private void atualizarGraficoLinha() {
        if (orcamentos == null || orcamentos.isEmpty()) {
            lineChart.getData().clear();
            return;
        }

        Map<String, Double> faturamentoPorMes = new LinkedHashMap<>();

        for (Orcamento o : orcamentos) {
            if (o != null && o.getDataCriacao() != null) {
                String mes = o.getDataCriacao().format(DateTimeFormatter.ofPattern("MMM/yyyy"));
                faturamentoPorMes.merge(mes, o.getTotalFinal(), Double::sum);
            }
        }

        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(faturamentoPorMes.entrySet());
        sortedEntries.sort(Map.Entry.comparingByKey());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Evolucao do faturamento");

        for (Map.Entry<String, Double> entry : sortedEntries) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        lineChart.getData().clear();
        if (!series.getData().isEmpty()) {
            lineChart.getData().add(series);
        }
    }
}