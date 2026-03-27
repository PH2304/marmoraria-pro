package br.com.marmoraria.view;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Material;
import br.com.marmoraria.model.Servico;
import br.com.marmoraria.service.CalculadoraService;
import br.com.marmoraria.service.MaterialService;
import br.com.marmoraria.util.GeradorPDF;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class OrcamentoView extends BorderPane {

    // Componentes
    private TableView<ItemOrcamento> tabela = new TableView<>();
    private ObservableList<ItemOrcamento> itens = FXCollections.observableArrayList();
    private Label totalLabel = new Label("Total: R$ 0,00");

    // Componentes de dados do cliente
    private TextField txtClienteNome;
    private TextField txtClienteTelefone;
    private TextField txtClienteEmail;
    private TextField txtEnderecoObra;
    private TextArea txtObservacoes;

    // Services
    private final CalculadoraService calc = new CalculadoraService();
    private final MaterialService materialService = new MaterialService();

    public OrcamentoView() {
        setPadding(new Insets(15));
        setStyle("-fx-background-color: #ecf0f1;");

        // Painel de dados do cliente
        VBox painelCliente = criarPainelCliente();

        // Painel esquerdo (entrada de dados)
        VBox painelEntrada = criarPainelEntrada();

        // Painel central (tabela)
        VBox painelTabela = criarPainelTabela();

        // Rodapé
        HBox rodape = criarRodape();

        // Layout: Cliente + Entrada na esquerda
        VBox painelEsquerdo = new VBox(15);
        painelEsquerdo.getChildren().addAll(painelCliente, painelEntrada);

        setLeft(painelEsquerdo);
        setCenter(painelTabela);
        setBottom(rodape);
    }

    /**
     * Cria o painel de dados do cliente
     */
    private VBox criarPainelCliente() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setPrefWidth(280);
        box.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label titulo = new Label("👤 DADOS DO CLIENTE");
        titulo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        txtClienteNome = new TextField();
        txtClienteNome.setPromptText("Nome do Cliente");
        txtClienteNome.setPrefWidth(250);

        txtClienteTelefone = new TextField();
        txtClienteTelefone.setPromptText("Telefone");
        txtClienteTelefone.setPrefWidth(250);

        txtClienteEmail = new TextField();
        txtClienteEmail.setPromptText("E-mail");
        txtClienteEmail.setPrefWidth(250);

        txtEnderecoObra = new TextField();
        txtEnderecoObra.setPromptText("Endereço da Obra");
        txtEnderecoObra.setPrefWidth(250);

        txtObservacoes = new TextArea();
        txtObservacoes.setPromptText("Observações (opcional)");
        txtObservacoes.setPrefHeight(80);
        txtObservacoes.setWrapText(true);

        box.getChildren().addAll(titulo, txtClienteNome, txtClienteTelefone,
                txtClienteEmail, txtEnderecoObra, txtObservacoes);
        return box;
    }

    private VBox criarPainelEntrada() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setPrefWidth(280);
        box.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label titulo = new Label("📦 DADOS DA PEÇA");
        titulo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

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
        box.setPadding(new Insets(0, 0, 0, 15));

        Label titulo = new Label("📋 ITENS DO ORÇAMENTO");
        titulo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

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
                btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
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
        tabela.setPrefHeight(450);
        tabela.setStyle("-fx-background-color: white; -fx-background-radius: 8px;");

        box.getChildren().addAll(titulo, tabela);
        return box;
    }

    private HBox criarRodape() {
        HBox rodape = new HBox(15);
        rodape.setPadding(new Insets(15, 10, 10, 10));
        rodape.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10;");
        rodape.setAlignment(Pos.CENTER_RIGHT);

        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        Button btnSalvar = new Button("💾 Salvar Orçamento");
        btnSalvar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSalvar.setOnAction(e -> salvarOrcamento());

        Button btnLimpar = new Button("🗑️ Limpar Tudo");
        btnLimpar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        btnLimpar.setOnAction(e -> limparOrcamento());

        Button btnPDF = new Button("📄 Exportar PDF");
        btnPDF.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold;");
        btnPDF.setOnAction(e -> gerarPDF());

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

        // Adicionar dados do cliente
        orcamento.setClienteNome(txtClienteNome.getText().trim());
        orcamento.setClienteTelefone(txtClienteTelefone.getText().trim());
        orcamento.setClienteEmail(txtClienteEmail.getText().trim());
        orcamento.setEnderecoObra(txtEnderecoObra.getText().trim());
        orcamento.setObservacoes(txtObservacoes.getText().trim());

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
                    "\nCliente: " + (txtClienteNome.getText().isEmpty() ? "Não informado" : txtClienteNome.getText()) +
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

        // Carregar dados do cliente
        txtClienteNome.setText(orcamento.getClienteNome() != null ? orcamento.getClienteNome() : "");
        txtClienteTelefone.setText(orcamento.getClienteTelefone() != null ? orcamento.getClienteTelefone() : "");
        txtClienteEmail.setText(orcamento.getClienteEmail() != null ? orcamento.getClienteEmail() : "");
        txtEnderecoObra.setText(orcamento.getEnderecoObra() != null ? orcamento.getEnderecoObra() : "");
        txtObservacoes.setText(orcamento.getObservacoes() != null ? orcamento.getObservacoes() : "");

        // Adicionar os itens do orçamento carregado
        for (ItemOrcamento item : orcamento.getItens()) {
            itens.add(item);
        }

        // Atualizar o total
        atualizarTotal();

        // Mostrar mensagem de confirmação
        System.out.println("📂 Orçamento carregado: " + orcamento.getNumeroOrcamento());
        mostrarAlerta("✅ Orçamento carregado com sucesso!\n\nNúmero: " + orcamento.getNumeroOrcamento() +
                "\nCliente: " + (orcamento.getClienteNome() != null ? orcamento.getClienteNome() : "Não informado") +
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
        confirm.setContentText("Tem certeza que deseja limpar todos os itens e dados do cliente?\n\nEsta ação não pode ser desfeita.");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            itens.clear();
            atualizarTotal();

            // Limpar campos do cliente
            txtClienteNome.clear();
            txtClienteTelefone.clear();
            txtClienteEmail.clear();
            txtEnderecoObra.clear();
            txtObservacoes.clear();

            mostrarAlerta("✅ Orçamento limpo com sucesso!");
        }
    }

    /**
     * Gera PDF do orçamento atual
     */
    private void gerarPDF() {
        if (itens.isEmpty()) {
            mostrarAlerta("Não há itens para gerar PDF. Adicione pelo menos um item.");
            return;
        }

        // Criar objeto Orcamento
        br.com.marmoraria.model.Orcamento orcamento = new br.com.marmoraria.model.Orcamento();

        // Adicionar dados do cliente
        orcamento.setClienteNome(txtClienteNome.getText().trim());
        orcamento.setClienteTelefone(txtClienteTelefone.getText().trim());
        orcamento.setClienteEmail(txtClienteEmail.getText().trim());
        orcamento.setEnderecoObra(txtEnderecoObra.getText().trim());
        orcamento.setObservacoes(txtObservacoes.getText().trim());

        // Adicionar itens
        for (ItemOrcamento item : itens) {
            orcamento.adicionarItem(item);
        }

        // Calcular totais
        orcamento.calcularTotais();

        // Gerar PDF
        boolean sucesso = GeradorPDF.gerarOrcamentoPDF(orcamento);

        if (sucesso) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("PDF Gerado");
            confirm.setHeaderText("✅ Orçamento gerado com sucesso!");
            confirm.setContentText(
                    "Arquivo: " + orcamento.getNumeroOrcamento() + ".pdf\n" +
                            "Cliente: " + (txtClienteNome.getText().isEmpty() ? "Não informado" : txtClienteNome.getText()) +
                            "\nLocal: " + GeradorPDF.getPastaPDFs() + "\n\n" +
                            "Deseja abrir o PDF agora?"
            );

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                GeradorPDF.abrirPDF(orcamento.getNumeroOrcamento());
            }
        } else {
            mostrarAlerta("❌ Erro ao gerar PDF.\n\nVerifique se a pasta 'pdfs' tem permissão de escrita.");
        }
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}