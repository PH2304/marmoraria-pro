package br.com.marmoraria.view;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Material;
import br.com.marmoraria.model.Servico;
import br.com.marmoraria.service.CalculadoraService;
import br.com.marmoraria.service.MaterialService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class OrcamentoView extends BorderPane {

    // Componentes
    private TableView<ItemOrcamento> tabela = new TableView<>();
    private ObservableList<ItemOrcamento> itens = FXCollections.observableArrayList();
    private Label totalLabel = new Label("Total: R$ 0,00");

    // Services
    private final CalculadoraService calc = new CalculadoraService();
    private final MaterialService materialService = new MaterialService();

    public OrcamentoView() {
        setPadding(new Insets(20));

        // Painel esquerdo (entrada de dados)
        VBox painelEntrada = criarPainelEntrada();

        // Painel central (tabela)
        VBox painelTabela = criarPainelTabela();

        // Rodapé
        HBox rodape = criarRodape();

        setLeft(painelEntrada);
        setCenter(painelTabela);
        setBottom(rodape);
    }

    private VBox criarPainelEntrada() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setPrefWidth(280);
        box.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10;");

        Label titulo = new Label("📦 Dados da Peça");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ComboBox<Material> cbMaterial = new ComboBox<>();
        cbMaterial.setItems(FXCollections.observableArrayList(materialService.getTodosMateriais()));
        cbMaterial.setPromptText("Selecione o material");
        cbMaterial.setPrefWidth(250);

        ComboBox<Servico> cbServico = new ComboBox<>();
        cbServico.setItems(FXCollections.observableArrayList(materialService.getTodosServicos()));
        cbServico.setPromptText("Selecione o serviço (opcional)");
        cbServico.setPrefWidth(250);

        TextField txtLargura = new TextField();
        txtLargura.setPromptText("Largura (mm)");

        TextField txtComprimento = new TextField();
        txtComprimento.setPromptText("Comprimento (mm)");

        TextField txtQuantidade = new TextField();
        txtQuantidade.setPromptText("Quantidade");
        txtQuantidade.setText("1");

        Button btnAdicionar = new Button("➕ Adicionar Item");
        btnAdicionar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAdicionar.setPrefWidth(250);

        btnAdicionar.setOnAction(e -> {
            try {
                Material material = cbMaterial.getValue();
                if (material == null) {
                    mostrarAlerta("Selecione um material!");
                    return;
                }

                double largura = Double.parseDouble(txtLargura.getText());
                double comprimento = Double.parseDouble(txtComprimento.getText());
                int quantidade = Integer.parseInt(txtQuantidade.getText());

                double area = calc.calcularArea(largura, comprimento);
                double total = calc.calcularMaterial(area, quantidade, material.getPrecoPorMetroQuadrado());

                ItemOrcamento item = new ItemOrcamento(
                        material,
                        cbServico.getValue(),
                        largura,
                        comprimento,
                        quantidade,
                        area,
                        total
                );

                itens.add(item);
                atualizarTotal();

                // Limpar campos
                txtLargura.clear();
                txtComprimento.clear();
                txtQuantidade.setText("1");

            } catch (Exception ex) {
                mostrarAlerta("Erro ao adicionar item: " + ex.getMessage());
            }
        });

        box.getChildren().addAll(titulo, cbMaterial, cbServico, txtLargura, txtComprimento, txtQuantidade, btnAdicionar);
        return box;
    }

    private VBox criarPainelTabela() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(0, 0, 0, 20));

        Label titulo = new Label("📋 Itens do Orçamento");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Configurar colunas
        TableColumn<ItemOrcamento, String> colMaterial = new TableColumn<>("Material");
        colMaterial.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getMaterial().getNome())
        );
        colMaterial.setPrefWidth(150);

        TableColumn<ItemOrcamento, String> colServico = new TableColumn<>("Serviço");
        colServico.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getServico() != null ? c.getValue().getServico().getNome() : "-"
                )
        );
        colServico.setPrefWidth(120);

        TableColumn<ItemOrcamento, String> colDimensoes = new TableColumn<>("Dimensões (mm)");
        colDimensoes.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        String.format("%.0f x %.0f", c.getValue().getLargura(), c.getValue().getComprimento())
                )
        );
        colDimensoes.setPrefWidth(120);

        TableColumn<ItemOrcamento, Number> colQuantidade = new TableColumn<>("Qtd");
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colQuantidade.setPrefWidth(60);

        TableColumn<ItemOrcamento, Number> colArea = new TableColumn<>("Área (m²)");
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colArea.setPrefWidth(100);

        TableColumn<ItemOrcamento, Number> colTotal = new TableColumn<>("Total (R$)");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colTotal.setPrefWidth(120);

        // Coluna de ação (remover)
        TableColumn<ItemOrcamento, Void> colAcao = new TableColumn<>("Ação");
        colAcao.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("🗑️");
            {
                btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                btn.setOnAction(e -> {
                    ItemOrcamento item = getTableView().getItems().get(getIndex());
                    itens.remove(item);
                    atualizarTotal();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
        colAcao.setPrefWidth(60);

        tabela.getColumns().addAll(colMaterial, colServico, colDimensoes, colQuantidade, colArea, colTotal, colAcao);
        tabela.setItems(itens);
        tabela.setPrefHeight(400);

        box.getChildren().addAll(titulo, tabela);
        return box;
    }

    private HBox criarRodape() {
        HBox rodape = new HBox(15);
        rodape.setPadding(new Insets(15, 10, 10, 10));
        rodape.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10;");
        rodape.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        Button btnSalvar = new Button("💾 Salvar Orçamento");
        btnSalvar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSalvar.setOnAction(e -> salvarOrcamento());

        Button btnLimpar = new Button("🗑️ Limpar Tudo");
        btnLimpar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        btnLimpar.setOnAction(e -> limparOrcamento());

        Button btnPDF = new Button("📄 Exportar PDF");
        btnPDF.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold;");
        btnPDF.setOnAction(e -> mostrarAlerta("Funcionalidade em desenvolvimento!\n\nEm breve você poderá exportar orçamentos para PDF."));

        rodape.getChildren().addAll(totalLabel, btnSalvar, btnLimpar, btnPDF);
        return rodape;
    }

    private void atualizarTotal() {
        double total = 0;
        for (ItemOrcamento item : itens) {
            total += item.getTotal();
        }
        totalLabel.setText(String.format("Total: R$ %.2f", total));
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Salva o orçamento atual
     */
    private void salvarOrcamento() {
        if (itens.isEmpty()) {
            mostrarAlerta("Não há itens para salvar. Adicione pelo menos um item.");
            return;
        }

        // Criar objeto Orcamento
        br.com.marmoraria.model.Orcamento orcamento = new br.com.marmoraria.model.Orcamento();

        // Adicionar itens
        for (ItemOrcamento item : itens) {
            orcamento.adicionarItem(item);
        }

        // Calcular totais
        orcamento.calcularTotais();

        // Salvar
        boolean salvou = br.com.marmoraria.util.FileManager.salvarOrcamento(orcamento);

        if (salvou) {
            mostrarAlerta("✅ Orçamento salvo com sucesso!\n\nNúmero: " + orcamento.getNumeroOrcamento() +
                    "\nLocal: " + br.com.marmoraria.util.FileManager.getPastaOrcamentos());
        } else {
            mostrarAlerta("❌ Erro ao salvar orçamento.");
        }
    }

    /**
     * Carrega um orçamento existente para edição
     */
    public void carregarOrcamento(br.com.marmoraria.model.Orcamento orcamento) {
        // Limpar itens existentes
        itens.clear();

        // Adicionar os itens do orçamento carregado
        for (ItemOrcamento item : orcamento.getItens()) {
            itens.add(item);
        }

        // Atualizar o total
        atualizarTotal();

        // Mostrar mensagem de confirmação
        System.out.println("📂 Orçamento carregado: " + orcamento.getNumeroOrcamento());
        mostrarAlerta("✅ Orçamento carregado com sucesso!\n\nNúmero: " + orcamento.getNumeroOrcamento() +
                "\nData: " + orcamento.getDataFormatada() +
                "\nItens: " + orcamento.getItens().size());
    }

    /**
     * Limpa todos os itens do orçamento atual
     */
    private void limparOrcamento() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Limpar Orçamento");
        confirm.setHeaderText("Confirmar limpeza");
        confirm.setContentText("Tem certeza que deseja limpar todos os itens?\n\nEsta ação não pode ser desfeita.");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            itens.clear();
            atualizarTotal();
            mostrarAlerta("Orçamento limpo com sucesso!");
        }
    }
}