package br.com.marmoraria;

import br.com.marmoraria.view.GestaoBackupView;
import br.com.marmoraria.view.GestaoOrcamentosView;
import br.com.marmoraria.view.OrcamentoView;
import br.com.marmoraria.util.GerenciadorBackup;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // Criar a interface principal em código Java
        VBox root = new VBox(15);
        root.setStyle("-fx-padding: 30; -fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");
        root.setAlignment(Pos.CENTER);

        // Título
        Label titulo = new Label("MARMORARIA PRO");
        titulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);");

        Label subtitulo = new Label("Sistema Profissional de Orçamentos");
        subtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(255,255,255,0.9);");

        // Cards container
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        // ==================== CARDS ====================

        // Card Calculadora
        VBox cardCalculadora = criarCard(
                "🧮",
                "Calculadora",
                "Calcule áreas, materiais e custos com precisão profissional.",
                "#3498db"
        );
        cardCalculadora.setOnMouseClicked(e -> abrirCalculadora());

        // Card Orçamentos
        VBox cardOrcamentos = criarCard(
                "📋",
                "Orçamentos",
                "Crie e gerencie orçamentos completos para seus clientes.",
                "#27ae60"
        );
        cardOrcamentos.setOnMouseClicked(e -> abrirOrcamentos());

        // Card Catálogo
        VBox cardCatalogo = criarCard(
                "📚",
                "Catálogo",
                "Consulte preços e especificações de todos os materiais.",
                "#e74c3c"
        );
        cardCatalogo.setOnMouseClicked(e -> abrirCatalogo());

        // Card Orçamentos Salvos
        VBox cardOrcamentosSalvos = criarCard(
                "💾",
                "Orçamentos Salvos",
                "Gerencie e visualize todos os orçamentos salvos.",
                "#9b59b6"
        );
        cardOrcamentosSalvos.setOnMouseClicked(e -> abrirGestaoOrcamentos());

        // Card Backup
        VBox cardBackup = criarCard(
                "🔄",
                "Backup",
                "Gerencie backups automáticos e restaure dados.",
                "#16a085"
        );
        cardBackup.setOnMouseClicked(e -> abrirGestaoBackup());

        // Card Estatísticas
        VBox cardEstatisticas = criarCard(
                "📊",
                "Estatísticas",
                "Acompanhe métricas e indicadores do seu negócio.",
                "#f39c12"
        );
        cardEstatisticas.setOnMouseClicked(e -> mostrarInfo("Estatísticas", "Funcionalidade em desenvolvimento!\n\nEm breve você terá gráficos e métricas detalhadas."));

        // Card Relatórios
        VBox cardRelatorios = criarCard(
                "📄",
                "Relatórios",
                "Exporte relatórios em PDF e acompanhe seus resultados.",
                "#9b59b6"
        );
        cardRelatorios.setOnMouseClicked(e -> mostrarInfo("Relatórios", "Funcionalidade em desenvolvimento!\n\nEm breve você poderá gerar relatórios completos em PDF."));

        // Card Ajuda
        VBox cardAjuda = criarCard(
                "❓",
                "Ajuda",
                "Tire dúvidas e aprenda a usar o sistema.",
                "#3498db"
        );
        cardAjuda.setOnMouseClicked(e -> mostrarAjuda());

        // Card Sobre
        VBox cardSobre = criarCard(
                "ℹ️",
                "Sobre",
                "Informações sobre o sistema.",
                "#95a5a6"
        );
        cardSobre.setOnMouseClicked(e -> mostrarSobre());

        // Card Sair
        VBox cardSair = criarCard(
                "🚪",
                "Sair",
                "Encerrar o sistema.",
                "#e74c3c"
        );
        cardSair.setOnMouseClicked(e -> sair());

        // ==================== ADICIONAR CARDS AO GRID ====================
        // Linha 1
        grid.add(cardCalculadora, 0, 0);
        grid.add(cardOrcamentos, 1, 0);
        grid.add(cardCatalogo, 2, 0);

        // Linha 2
        grid.add(cardOrcamentosSalvos, 0, 1);
        grid.add(cardBackup, 1, 1);
        grid.add(cardEstatisticas, 2, 1);

        // Linha 3
        grid.add(cardRelatorios, 0, 2);
        grid.add(cardAjuda, 1, 2);
        grid.add(cardSobre, 2, 2);

        // Linha 4 - Sair sozinho
        grid.add(cardSair, 1, 3);

        // Rodapé
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(15, 0, 0, 0));

        Label versao = new Label("Versão 2.0.0 | © 2024 Marmoraria Pro");
        versao.setStyle("-fx-text-fill: rgba(255,255,255,0.7); -fx-font-size: 10px;");
        footer.getChildren().add(versao);

        root.getChildren().addAll(titulo, subtitulo, grid, footer);

        // Configurar cena
        Scene scene = new Scene(root, 950, 750);
        stage.setTitle("Marmoraria Pro - Sistema de Orçamentos");
        stage.setScene(scene);
        stage.setMinWidth(850);
        stage.setMinHeight(650);
        stage.show();

        // Iniciar backup automático em segundo plano
        GerenciadorBackup.iniciarBackupAutomatico();
        System.out.println("🚀 Sistema iniciado com sucesso!");
    }

    /**
     * Cria um card estilizado
     */
    private VBox criarCard(String icone, String titulo, String descricao, String cor) {
        VBox card = new VBox(8);
        card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 16px; " +
                        "-fx-padding: 15px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );
        card.setPrefWidth(240);
        card.setPrefHeight(180);

        // Ícone
        Label iconeLabel = new Label(icone);
        iconeLabel.setStyle("-fx-font-size: 38px;");

        // Título
        Label tituloLabel = new Label(titulo);
        tituloLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + cor + ";");

        // Descrição
        Label descLabel = new Label(descricao);
        descLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");
        descLabel.setWrapText(true);

        // Setinha
        Label seta = new Label("→");
        seta.setStyle("-fx-font-size: 16px; -fx-text-fill: " + cor + "; -fx-font-weight: bold;");

        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.getChildren().add(seta);

        card.getChildren().addAll(iconeLabel, tituloLabel, descLabel, footer);

        // Efeito hover
        card.setOnMouseEntered(e -> {
            card.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-background-radius: 16px; " +
                            "-fx-padding: 15px; " +
                            "-fx-cursor: hand; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 20, 0, 0, 10);"
            );
            card.setTranslateY(-3);
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-background-radius: 16px; " +
                            "-fx-padding: 15px; " +
                            "-fx-cursor: hand; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"
            );
            card.setTranslateY(0);
        });

        return card;
    }

    /**
     * Abre a calculadora
     */
    private void abrirCalculadora() {
        try {
            OrcamentoView orcamentoView = new OrcamentoView();

            Scene scene = new Scene(orcamentoView, 950, 580);

            // Tentar adicionar CSS se existir
            try {
                String cssPath = getClass().getResource("/css/styles.css").toExternalForm();
                scene.getStylesheets().add(cssPath);
            } catch (Exception e) {
                // CSS não encontrado, continuar sem
            }

            Stage stage = new Stage();
            stage.setTitle("Calculadora - Marmoraria Pro");
            stage.setScene(scene);
            stage.setMinWidth(850);
            stage.setMinHeight(520);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao abrir calculadora: " + e.getMessage());
        }
    }

    /**
     * Abre a tela de orçamentos
     */
    private void abrirOrcamentos() {
        try {
            OrcamentoView orcamentoView = new OrcamentoView();

            Scene scene = new Scene(orcamentoView, 950, 580);

            // Tentar adicionar CSS se existir
            try {
                String cssPath = getClass().getResource("/css/styles.css").toExternalForm();
                scene.getStylesheets().add(cssPath);
            } catch (Exception e) {
                // CSS não encontrado, continuar sem
            }

            Stage stage = new Stage();
            stage.setTitle("Orçamentos - Marmoraria Pro");
            stage.setScene(scene);
            stage.setMinWidth(850);
            stage.setMinHeight(520);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao abrir orçamentos: " + e.getMessage());
        }
    }

    /**
     * Abre o catálogo de materiais
     */
    private void abrirCatalogo() {
        try {
            VBox catalogoView = new VBox(12);
            catalogoView.setStyle("-fx-padding: 25; -fx-background-color: #ecf0f1;");
            catalogoView.setAlignment(Pos.TOP_CENTER);

            Label titulo = new Label("📚 Catálogo de Materiais");
            titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

            Label mensagem = new Label(
                    "📦 MATERIAIS DISPONÍVEIS:\n\n" +
                            "• Granito Preto Absoluto\n" +
                            "  Preço: R$ 350,00/m²\n" +
                            "  Espessura: 20mm\n\n" +

                            "• Granito Verde Ubatuba\n" +
                            "  Preço: R$ 280,00/m²\n" +
                            "  Espessura: 20mm\n\n" +

                            "• Mármore Carrara\n" +
                            "  Preço: R$ 520,00/m²\n" +
                            "  Espessura: 20mm\n\n" +

                            "• Quartzo Branco\n" +
                            "  Preço: R$ 650,00/m²\n" +
                            "  Espessura: 20mm\n\n" +

                            "🔧 SERVIÇOS DISPONÍVEIS:\n\n" +
                            "• Corte reto - R$ 40,00/m\n" +
                            "• Polimento simples - R$ 60,00/m²\n" +
                            "• Instalação padrão - R$ 250,00/un\n\n" +

                            "✨ Mais materiais em breve!"
            );
            mensagem.setStyle("-fx-font-size: 13px; -fx-text-fill: #2c3e50;");
            mensagem.setWrapText(true);

            Button fechar = new Button("Fechar");
            fechar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 8 20; -fx-font-weight: bold;");
            fechar.setOnAction(e -> ((Stage) fechar.getScene().getWindow()).close());

            catalogoView.getChildren().addAll(titulo, mensagem, fechar);

            Scene scene = new Scene(catalogoView, 650, 500);
            Stage stage = new Stage();
            stage.setTitle("Catálogo - Marmoraria Pro");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao abrir catálogo: " + e.getMessage());
        }
    }

    /**
     * Abre a tela de gestão de orçamentos salvos
     */
    private void abrirGestaoOrcamentos() {
        try {
            GestaoOrcamentosView gestaoView = new GestaoOrcamentosView();
            Scene scene = new Scene(gestaoView, 900, 650);
            Stage stage = new Stage();
            stage.setTitle("Gestão de Orçamentos Salvos");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao abrir gestão de orçamentos: " + e.getMessage());
        }
    }

    /**
     * Abre a tela de gerenciamento de backups
     */
    private void abrirGestaoBackup() {
        try {
            GestaoBackupView backupView = new GestaoBackupView();
            Scene scene = new Scene(backupView, 900, 600);
            Stage stage = new Stage();
            stage.setTitle("Gerenciamento de Backups");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao abrir gerenciamento de backups: " + e.getMessage());
        }
    }

    /**
     * Mostra a tela de ajuda
     */
    private void mostrarAjuda() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajuda - Marmoraria Pro");
        alert.setHeaderText("📚 Guia Rápido do Sistema");
        alert.setContentText(
                "═══════════════════════════════════════\n" +
                        "       MARMORARIA PRO - AJUDA\n" +
                        "═══════════════════════════════════════\n\n" +

                        "📌 CALCULADORA\n" +
                        "• Selecione um material no catálogo\n" +
                        "• Informe as dimensões (largura x comprimento em mm)\n" +
                        "• Informe a quantidade de peças\n" +
                        "• Clique em 'Adicionar Item'\n" +
                        "• O sistema calcula automaticamente:\n" +
                        "  - Área em m²\n" +
                        "  - Custo total do material\n" +
                        "  - Total geral do orçamento\n\n" +

                        "💾 SALVAR ORÇAMENTO\n" +
                        "• Após adicionar os itens, clique em 'Salvar Orçamento'\n" +
                        "• Os orçamentos são salvos na pasta 'orcamentos/'\n" +
                        "• Use a tela 'Orçamentos Salvos' para gerenciar\n\n" +

                        "🔄 BACKUP\n" +
                        "• Backups automáticos são feitos a cada 24 horas\n" +
                        "• Mantém os últimos 30 backups\n" +
                        "• Você pode criar backups manuais a qualquer momento\n" +
                        "• Restaurar backups é simples e seguro\n\n" +

                        "💡 DICAS:\n" +
                        "• Sempre confira as medidas antes de adicionar\n" +
                        "• Use o botão 'Excluir' para remover itens\n" +
                        "• O total é atualizado automaticamente\n\n" +

                        "Suporte: suporte@marmorariapro.com.br"
        );
        alert.getDialogPane().setPrefWidth(500);
        alert.showAndWait();
    }

    /**
     * Mostra informações sobre o sistema
     */
    private void mostrarSobre() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre o Sistema");
        alert.setHeaderText("Marmoraria Pro");
        alert.setContentText(
                "═══════════════════════════════════════\n" +
                        "       MARMORARIA PRO v2.0.0\n" +
                        "═══════════════════════════════════════\n\n" +

                        "🏢 Sistema Profissional para Marmorarias\n\n" +

                        "✨ FUNCIONALIDADES:\n" +
                        "• Cálculo preciso de áreas e custos\n" +
                        "• Catálogo de materiais (mármore, granito, quartzo)\n" +
                        "• Gestão completa de orçamentos\n" +
                        "• Salvar e carregar orçamentos\n" +
                        "• Backup automático dos dados\n" +
                        "• Interface moderna e intuitiva\n\n" +

                        "👨‍💻 DESENVOLVIDO COM:\n" +
                        "• Java 11+\n" +
                        "• JavaFX\n" +
                        "• Maven\n" +
                        "• Gson para JSON\n\n" +

                        "📞 SUPORTE:\n" +
                        "• Email: suporte@marmorariapro.com.br\n" +
                        "• Telefone: (11) 99999-9999\n\n" +

                        "© 2024 - Todos os direitos reservados\n" +
                        "═══════════════════════════════════════"
        );
        alert.getDialogPane().setPrefWidth(450);
        alert.showAndWait();
    }

    /**
     * Mostra uma mensagem informativa
     */
    private void mostrarInfo(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Mostra uma mensagem de erro
     */
    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Ops! Algo deu errado");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Confirma e sai do sistema
     */
    private void sair() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sair");
        alert.setHeaderText("Confirmar saída");
        alert.setContentText("Deseja realmente sair do sistema?\n\nTodos os dados não salvos serão perdidos.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            System.out.println("👋 Sistema encerrado!");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}