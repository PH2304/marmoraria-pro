package com.marmoraria;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("🚀 Iniciando Marmoraria Pro (Workaround)...");

            // Criar layout principal
            VBox root = new VBox(20);
            root.setPadding(new Insets(30));
            root.setAlignment(Pos.TOP_CENTER);
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef);");

            // Cabeçalho
            HBox header = new HBox(20);
            header.setAlignment(Pos.CENTER_LEFT);

            Label icon = new Label("💎");
            icon.setStyle("-fx-font-size: 48px; -fx-padding: 0 20 0 0;");

            VBox titleBox = new VBox(5);
            Label title = new Label("MARMORARIA PRO");
            title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

            Label subtitle = new Label("Sistema Profissional de Cálculo de Orçamentos");
            subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");

            titleBox.getChildren().addAll(title, subtitle);
            header.getChildren().addAll(icon, titleBox);

            Separator separator = new Separator();

            // Grid de funcionalidades
            GridPane grid = new GridPane();
            grid.setHgap(20);
            grid.setVgap(20);
            grid.setPadding(new Insets(30));
            grid.setAlignment(Pos.CENTER);

            // Card 1: Calculadora
            VBox cardCalculadora = criarCard("🧮", "Calculadora",
                    "Cálculo preciso de áreas e materiais", "#3498db");
            Button btnCalculadora = criarBotaoCard("Abrir Calculadora", "#3498db");
            btnCalculadora.setOnAction(e -> mostrarCalculadora());
            cardCalculadora.getChildren().add(btnCalculadora);

            // Card 2: Orçamentos
            VBox cardOrcamentos = criarCard("💰", "Orçamentos",
                    "Gestão completa de orçamentos", "#2ecc71");
            Button btnOrcamentos = criarBotaoCard("Abrir Orçamentos", "#2ecc71");
            btnOrcamentos.setOnAction(e -> mostrarOrcamentos());
            cardOrcamentos.getChildren().add(btnOrcamentos);

            // Card 3: Catálogo
            VBox cardCatalogo = criarCard("💎", "Catálogo",
                    "Materiais e serviços disponíveis", "#e74c3c");
            Button btnCatalogo = criarBotaoCard("Abrir Catálogo", "#e74c3c");
            btnCatalogo.setOnAction(e -> mostrarCatalogo());
            cardCatalogo.getChildren().add(btnCatalogo);

            // Card 4: Relatórios
            VBox cardRelatorios = criarCard("📊", "Relatórios",
                    "Relatórios e exportação", "#9b59b6");
            Button btnRelatorios = criarBotaoCard("Gerar Relatórios", "#9b59b6");
            btnRelatorios.setOnAction(e -> mostrarRelatorios());
            cardRelatorios.getChildren().add(btnRelatorios);

            // Adicionar cards ao grid
            grid.add(cardCalculadora, 0, 0);
            grid.add(cardOrcamentos, 1, 0);
            grid.add(cardCatalogo, 0, 1);
            grid.add(cardRelatorios, 1, 1);

            // Rodapé
            HBox footer = new HBox(20);
            footer.setAlignment(Pos.CENTER_RIGHT);
            footer.setPadding(new Insets(20, 0, 0, 0));

            Button btnSobre = new Button("❓ Sobre");
            btnSobre.setStyle("-fx-background-color: transparent; -fx-text-fill: #3498db; -fx-font-weight: bold;");
            btnSobre.setOnAction(e -> mostrarSobre());

            Button btnSair = new Button("🚪 Sair");
            btnSair.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;");
            btnSair.setOnAction(e -> primaryStage.close());

            footer.getChildren().addAll(btnSobre, new Separator(), btnSair);

            // Montar layout principal
            root.getChildren().addAll(header, separator, grid, footer);

            // Configurar cena
            Scene scene = new Scene(root, 1000, 650);

            // Configurar janela
            primaryStage.setTitle("Marmoraria Pro - Sistema de Orçamentos");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(900);
            primaryStage.setMinHeight(600);

            primaryStage.show();

            System.out.println("✅ Aplicação JavaFX funcionando SEM FXML!");
            System.out.println("🎉 Interface manual carregada com sucesso!");

        } catch (Exception e) {
            System.err.println("❌ ERRO ao criar interface manual:");
            e.printStackTrace();
            mostrarErroDialog("Erro", "Não foi possível criar a interface: " + e.getMessage());
        }
    }

    private VBox criarCard(String emoji, String titulo, String descricao, String cor) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle(String.format(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0); " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-color: %s; " +
                        "-fx-border-width: 2;",
                cor
        ));
        card.setPrefSize(350, 200);

        HBox headerCard = new HBox(10);
        headerCard.setAlignment(Pos.CENTER_LEFT);

        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 24px;");

        Label tituloLabel = new Label(titulo);
        tituloLabel.setStyle(String.format(
                "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: %s;",
                cor
        ));

        headerCard.getChildren().addAll(emojiLabel, tituloLabel);

        Label descLabel = new Label(descricao);
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        VBox lista = new VBox(8);
        lista.setPadding(new Insets(10, 0, 0, 0));

        if (titulo.equals("Calculadora")) {
            lista.getChildren().addAll(
                    new Label("• Cálculo de áreas (m²)"),
                    new Label("• Conversão mm → metros"),
                    new Label("• Custo por material"),
                    new Label("• Serviços de polimento")
            );
        } else if (titulo.equals("Orçamentos")) {
            lista.getChildren().addAll(
                    new Label("• Cadastro de clientes"),
                    new Label("• Criação de orçamentos"),
                    new Label("• Cálculo de margens"),
                    new Label("• Histórico de vendas")
            );
        } else if (titulo.equals("Catálogo")) {
            lista.getChildren().addAll(
                    new Label("• Mármores diversos"),
                    new Label("• Granitos nacionais"),
                    new Label("• Quartzos e pedras"),
                    new Label("• Serviços especializados")
            );
        } else {
            lista.getChildren().addAll(
                    new Label("• Exportação para texto"),
                    new Label("• Relatórios por período"),
                    new Label("• Análise de lucratividade"),
                    new Label("• Estatísticas detalhadas")
            );
        }

        card.getChildren().addAll(headerCard, descLabel, lista);
        return card;
    }

    private Button criarBotaoCard(String texto, String cor) {
        Button btn = new Button(texto);
        btn.setStyle(String.format(
                "-fx-background-color: %s; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10 25; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;",
                cor
        ));
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private void mostrarCalculadora() {
        Stage calcStage = new Stage();
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f8f9fa;");

        Label titulo = new Label("🧮 Calculadora de Mármore");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Campos de entrada
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TextField tfLargura = criarCampoNumerico("mm");
        TextField tfAltura = criarCampoNumerico("mm");
        TextField tfQuantidade = criarCampoNumerico("un");
        tfQuantidade.setText("1");
        TextField tfPreco = criarCampoNumerico("R$/m²");
        tfPreco.setText("350.00");

        grid.add(new Label("Largura:"), 0, 0);
        grid.add(tfLargura, 1, 0);
        grid.add(new Label("Altura:"), 0, 1);
        grid.add(tfAltura, 1, 1);
        grid.add(new Label("Quantidade:"), 0, 2);
        grid.add(tfQuantidade, 1, 2);
        grid.add(new Label("Preço do material:"), 0, 3);
        grid.add(tfPreco, 1, 3);

        // Botões
        HBox botoes = new HBox(10);
        Button btnCalcular = criarBotao("📊 Calcular", "#3498db");
        Button btnLimpar = criarBotao("🔄 Limpar", "#95a5a6");

        // Resultados
        VBox resultados = new VBox(10);
        resultados.setPadding(new Insets(15));
        resultados.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #3498db;");

        Label lblArea = new Label("Área por peça: 0.000 m²");
        Label lblAreaTotal = new Label("Área total: 0.000 m²");
        Label lblCusto = new Label("💰 Custo total: R$ 0,00");
        lblCusto.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #27ae60;");

        resultados.getChildren().addAll(
                new Label("📈 Resultados:"),
                new Separator(),
                lblArea,
                lblAreaTotal,
                lblCusto
        );

        // Ações
        btnCalcular.setOnAction(e -> {
            try {
                double largura = Double.parseDouble(tfLargura.getText());
                double altura = Double.parseDouble(tfAltura.getText());
                double quantidade = Double.parseDouble(tfQuantidade.getText());
                double preco = Double.parseDouble(tfPreco.getText());

                double areaUnit = (largura * altura) / 1000000;
                double areaTotal = areaUnit * quantidade;
                double custoTotal = areaTotal * preco;

                lblArea.setText(String.format("Área por peça: %.3f m²", areaUnit));
                lblAreaTotal.setText(String.format("Área total: %.3f m²", areaTotal));
                lblCusto.setText(String.format("💰 Custo total: R$ %.2f", custoTotal));

            } catch (NumberFormatException ex) {
                mostrarAlerta("Erro", "Por favor, insira apenas números válidos.");
            }
        });

        btnLimpar.setOnAction(e -> {
            tfLargura.clear();
            tfAltura.clear();
            tfQuantidade.setText("1");
            tfPreco.setText("350.00");
            lblArea.setText("Área por peça: 0.000 m²");
            lblAreaTotal.setText("Área total: 0.000 m²");
            lblCusto.setText("💰 Custo total: R$ 0,00");
        });

        botoes.getChildren().addAll(btnCalcular, btnLimpar);
        root.getChildren().addAll(titulo, grid, botoes, resultados);

        Scene scene = new Scene(root, 400, 500);
        calcStage.setScene(scene);
        calcStage.setTitle("Calculadora de Mármore");
        calcStage.show();
    }

    private TextField criarCampoNumerico(String placeholder) {
        TextField tf = new TextField();
        tf.setPromptText(placeholder);
        tf.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                tf.setText(oldVal);
            }
        });
        return tf;
    }

    private Button criarBotao(String texto, String cor) {
        Button btn = new Button(texto);
        btn.setStyle(String.format(
                "-fx-background-color: %s; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 20; " +
                        "-fx-background-radius: 5;",
                cor
        ));
        return btn;
    }

    private void mostrarOrcamentos() {
        mostrarAlerta("💰 Orçamentos", "Módulo em desenvolvimento!");
    }

    private void mostrarCatalogo() {
        mostrarAlerta("💎 Catálogo",
                "Materiais disponíveis:\n\n" +
                        "1. Mármore Carrara - R$ 450,00/m²\n" +
                        "2. Granito Preto - R$ 380,00/m²\n" +
                        "3. Quartzo Branco - R$ 650,00/m²\n" +
                        "4. Granito Verde - R$ 420,00/m²");
    }

    private void mostrarRelatorios() {
        mostrarAlerta("📊 Relatórios", "Módulo em desenvolvimento!");
    }

    private void mostrarSobre() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre o Sistema");
        alert.setHeaderText("Marmoraria Pro v1.0");
        alert.setContentText(
                "✅ Sistema JavaFX funcionando!\n\n" +
                        "Tecnologias:\n" +
                        "• Java " + System.getProperty("java.version") + "\n" +
                        "• JavaFX (Interface manual)\n" +
                        "• Maven\n\n" +
                        "🎯 Sistema de cálculo de orçamentos para marmoraria\n\n" +
                        "© 2024 - Interface criada programaticamente"
        );
        alert.showAndWait();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarErroDialog(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText("Erro Crítico");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Marmoraria Pro...");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        launch(args);
    }
}