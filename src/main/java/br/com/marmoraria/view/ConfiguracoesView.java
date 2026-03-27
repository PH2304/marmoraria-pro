package br.com.marmoraria.view;

import br.com.marmoraria.model.Configuracoes;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ConfiguracoesView extends BorderPane {

    private Configuracoes config;

    // Abas
    private TabPane tabPane;

    // Componentes
    private TextField txtMargemLucro;
    private TextField txtDesconto;
    private TextField txtPrazoValidade;

    private CheckBox chkNotificarBackup;
    private CheckBox chkNotificarOrcamento;
    private TextField txtDiasAviso;

    private ComboBox<String> cbTema;
    private ComboBox<String> cbFonte;
    private Slider slTamanhoFonte;
    private Label lblTamanhoFontePreview;

    private TextField txtNomeEmpresa;
    private TextField txtTelefoneEmpresa;
    private TextField txtEmailEmpresa;
    private TextField txtEnderecoEmpresa;
    private TextField txtCnpj;

    private CheckBox chkBackupAutomatico;
    private TextField txtIntervaloBackup;
    private TextField txtMaxBackups;

    public ConfiguracoesView() {
        // Carregar configurações atuais
        config = Configuracoes.carregar();

        setPadding(new Insets(20));
        setStyle("-fx-background-color: #ecf0f1;");

        // Título
        Label titulo = new Label("⚙️ Configurações Avançadas");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Criar abas
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabOrcamento = criarTabOrcamento();
        Tab tabNotificacoes = criarTabNotificacoes();
        Tab tabAparencia = criarTabAparencia();
        Tab tabEmpresa = criarTabEmpresa();
        Tab tabBackup = criarTabBackup();

        tabPane.getTabs().addAll(tabOrcamento, tabNotificacoes, tabAparencia, tabEmpresa, tabBackup);

        // Botões
        HBox botoes = criarBotoes();

        setTop(titulo);
        setCenter(tabPane);
        setBottom(botoes);

        // Carregar valores
        carregarValores();
    }

    private Tab criarTabOrcamento() {
        Tab tab = new Tab("💰 Orçamento");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        int row = 0;

        grid.add(new Label("Margem de Lucro Padrão (%):"), 0, row);
        txtMargemLucro = new TextField();
        txtMargemLucro.setPrefWidth(200);
        grid.add(txtMargemLucro, 1, row);
        grid.add(new Label("Aplicada automaticamente aos orçamentos"), 2, row);
        row++;

        grid.add(new Label("Desconto Padrão (%):"), 0, row);
        txtDesconto = new TextField();
        grid.add(txtDesconto, 1, row);
        grid.add(new Label("Desconto padrão para novos orçamentos"), 2, row);
        row++;

        grid.add(new Label("Prazo de Validade (dias):"), 0, row);
        txtPrazoValidade = new TextField();
        grid.add(txtPrazoValidade, 1, row);
        grid.add(new Label("Dias de validade do orçamento"), 2, row);

        tab.setContent(grid);
        return tab;
    }

    private Tab criarTabNotificacoes() {
        Tab tab = new Tab("🔔 Notificações");
        tab.setClosable(false);

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        chkNotificarBackup = new CheckBox("Notificar quando backup for realizado");
        chkNotificarBackup.setStyle("-fx-font-size: 13px;");

        chkNotificarOrcamento = new CheckBox("Notificar quando orçamento estiver próximo do vencimento");
        chkNotificarOrcamento.setStyle("-fx-font-size: 13px;");

        HBox avisoBox = new HBox(10);
        avisoBox.setAlignment(Pos.CENTER_LEFT);
        avisoBox.getChildren().addAll(
                new Label("Avisar com antecedência de:"),
                txtDiasAviso = new TextField(),
                new Label("dias")
        );
        txtDiasAviso.setPrefWidth(60);

        vbox.getChildren().addAll(chkNotificarBackup, chkNotificarOrcamento, avisoBox);

        tab.setContent(vbox);
        return tab;
    }

    private Tab criarTabAparencia() {
        Tab tab = new Tab("🎨 Aparência");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        int row = 0;

        grid.add(new Label("Tema:"), 0, row);
        cbTema = new ComboBox<>();
        cbTema.getItems().addAll("claro", "escuro");
        grid.add(cbTema, 1, row);
        row++;

        grid.add(new Label("Fonte:"), 0, row);
        cbFonte = new ComboBox<>();
        cbFonte.getItems().addAll("Segoe UI", "Arial", "System", "Verdana");
        grid.add(cbFonte, 1, row);
        row++;

        grid.add(new Label("Tamanho da Fonte:"), 0, row);
        slTamanhoFonte = new Slider(10, 20, 13);
        slTamanhoFonte.setShowTickLabels(true);
        slTamanhoFonte.setShowTickMarks(true);
        slTamanhoFonte.setMajorTickUnit(2);
        slTamanhoFonte.setPrefWidth(300);
        grid.add(slTamanhoFonte, 1, row);
        row++;

        grid.add(new Label("Preview:"), 0, row);
        lblTamanhoFontePreview = new Label("Texto de exemplo");
        lblTamanhoFontePreview.setStyle("-fx-font-size: 13px;");
        grid.add(lblTamanhoFontePreview, 1, row);

        slTamanhoFonte.valueProperty().addListener((obs, oldVal, newVal) -> {
            lblTamanhoFontePreview.setStyle("-fx-font-size: " + newVal.intValue() + "px;");
        });

        tab.setContent(grid);
        return tab;
    }

    private Tab criarTabEmpresa() {
        Tab tab = new Tab("🏢 Empresa");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        int row = 0;

        grid.add(new Label("Nome da Empresa:"), 0, row);
        txtNomeEmpresa = new TextField();
        txtNomeEmpresa.setPrefWidth(400);
        grid.add(txtNomeEmpresa, 1, row);
        row++;

        grid.add(new Label("Telefone:"), 0, row);
        txtTelefoneEmpresa = new TextField();
        grid.add(txtTelefoneEmpresa, 1, row);
        row++;

        grid.add(new Label("E-mail:"), 0, row);
        txtEmailEmpresa = new TextField();
        grid.add(txtEmailEmpresa, 1, row);
        row++;

        grid.add(new Label("Endereço:"), 0, row);
        txtEnderecoEmpresa = new TextField();
        grid.add(txtEnderecoEmpresa, 1, row);
        row++;

        grid.add(new Label("CNPJ:"), 0, row);
        txtCnpj = new TextField();
        grid.add(txtCnpj, 1, row);

        tab.setContent(grid);
        return tab;
    }

    private Tab criarTabBackup() {
        Tab tab = new Tab("💾 Backup");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        int row = 0;

        chkBackupAutomatico = new CheckBox("Ativar backup automático");
        grid.add(chkBackupAutomatico, 0, row, 2, 1);
        row++;

        grid.add(new Label("Intervalo entre backups (horas):"), 0, row);
        txtIntervaloBackup = new TextField();
        txtIntervaloBackup.setPrefWidth(100);
        grid.add(txtIntervaloBackup, 1, row);
        row++;

        grid.add(new Label("Máximo de backups:"), 0, row);
        txtMaxBackups = new TextField();
        txtMaxBackups.setPrefWidth(100);
        grid.add(txtMaxBackups, 1, row);

        tab.setContent(grid);
        return tab;
    }

    private HBox criarBotoes() {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20, 0, 0, 0));

        Button btnSalvar = new Button("💾 Salvar Configurações");
        btnSalvar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-weight: bold;");
        btnSalvar.setOnAction(e -> salvarConfiguracoes());

        Button btnRestaurar = new Button("🔄 Restaurar Padrão");
        btnRestaurar.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-weight: bold;");
        btnRestaurar.setOnAction(e -> restaurarPadrao());

        Button btnCancelar = new Button("❌ Cancelar");
        btnCancelar.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-padding: 10 30;");
        btnCancelar.setOnAction(e -> ((Stage) getScene().getWindow()).close());

        box.getChildren().addAll(btnSalvar, btnRestaurar, btnCancelar);
        return box;
    }

    private void carregarValores() {
        // Orçamento
        txtMargemLucro.setText(String.valueOf(config.getMargemLucroPadrao()));
        txtDesconto.setText(String.valueOf(config.getDescontoPadrao()));
        txtPrazoValidade.setText(String.valueOf(config.getPrazoValidadeDias()));

        // Notificações
        chkNotificarBackup.setSelected(config.isNotificarBackup());
        chkNotificarOrcamento.setSelected(config.isNotificarOrcamentoVencendo());
        txtDiasAviso.setText(String.valueOf(config.getDiasAvisoAntecedencia()));

        // Aparência
        cbTema.setValue(config.getTema());
        cbFonte.setValue(config.getFontePadrao());
        slTamanhoFonte.setValue(config.getTamanhoFonte());
        lblTamanhoFontePreview.setStyle("-fx-font-size: " + config.getTamanhoFonte() + "px;");

        // Empresa
        txtNomeEmpresa.setText(config.getNomeEmpresa());
        txtTelefoneEmpresa.setText(config.getTelefoneEmpresa());
        txtEmailEmpresa.setText(config.getEmailEmpresa());
        txtEnderecoEmpresa.setText(config.getEnderecoEmpresa());
        txtCnpj.setText(config.getCnpj());

        // Backup
        chkBackupAutomatico.setSelected(config.isBackupAutomatico());
        txtIntervaloBackup.setText(String.valueOf(config.getIntervaloBackupHoras()));
        txtMaxBackups.setText(String.valueOf(config.getMaxBackups()));
    }

    private void salvarConfiguracoes() {
        try {
            // Orçamento
            config.setMargemLucroPadrao(Double.parseDouble(txtMargemLucro.getText()));
            config.setDescontoPadrao(Double.parseDouble(txtDesconto.getText()));
            config.setPrazoValidadeDias(Integer.parseInt(txtPrazoValidade.getText()));

            // Notificações
            config.setNotificarBackup(chkNotificarBackup.isSelected());
            config.setNotificarOrcamentoVencendo(chkNotificarOrcamento.isSelected());
            config.setDiasAvisoAntecedencia(Integer.parseInt(txtDiasAviso.getText()));

            // Aparência
            config.setTema(cbTema.getValue());
            config.setFontePadrao(cbFonte.getValue());
            config.setTamanhoFonte((int) slTamanhoFonte.getValue());

            // Empresa
            config.setNomeEmpresa(txtNomeEmpresa.getText());
            config.setTelefoneEmpresa(txtTelefoneEmpresa.getText());
            config.setEmailEmpresa(txtEmailEmpresa.getText());
            config.setEnderecoEmpresa(txtEnderecoEmpresa.getText());
            config.setCnpj(txtCnpj.getText());

            // Backup
            config.setBackupAutomatico(chkBackupAutomatico.isSelected());
            config.setIntervaloBackupHoras(Integer.parseInt(txtIntervaloBackup.getText()));
            config.setMaxBackups(Integer.parseInt(txtMaxBackups.getText()));

            // Salvar
            if (config.salvar()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText("✅ Configurações salvas!");
                alert.setContentText("As alterações serão aplicadas após reiniciar o sistema.");
                alert.showAndWait();

                ((Stage) getScene().getWindow()).close();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("❌ Valores inválidos");
            alert.setContentText("Verifique se todos os campos numéricos estão preenchidos corretamente.");
            alert.showAndWait();
        }
    }

    private void restaurarPadrao() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Restaurar Padrão");
        confirm.setHeaderText("Confirmar restauração");
        confirm.setContentText("Tem certeza que deseja restaurar todas as configurações para o padrão?\n\nEsta ação não pode ser desfeita.");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            config = new Configuracoes();
            carregarValores();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("✅ Configurações restauradas!");
            alert.setContentText("Os valores padrão foram carregados. Clique em 'Salvar' para aplicar.");
            alert.showAndWait();
        }
    }
}