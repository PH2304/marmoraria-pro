package br.com.marmoraria;

import br.com.marmoraria.view.*;
import br.com.marmoraria.util.*;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.logging.Logger;

public class Main extends Application {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private boolean isDarkMode = false;
    private Scene mainScene;
    private VBox rootContainer;

    @Override
    public void start(Stage stage) {
        rootContainer = new VBox(0);

        construirInterface();

        mainScene = new Scene(rootContainer, 1200, 800);
        mainScene.setFill(Color.web(TemaManager.getBg()));

        stage.setTitle("Marmoraria Pro");
        stage.setScene(mainScene);
        stage.setMinWidth(950);
        stage.setMinHeight(680);
        stage.show();

        GerenciadorBackup.iniciarBackupAutomatico();
        LOGGER.info("🚀 Marmoraria Pro iniciado!");
    }

    private void construirInterface() {
        rootContainer.getChildren().clear();
        rootContainer.setStyle("-fx-background-color: " + TemaManager.getBg() + ";");

        VBox header = criarHeader();
        ScrollPane scrollPane = criarScrollPane();
        VBox content = criarConteudo();
        scrollPane.setContent(content);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        HBox footer = criarFooter();

        rootContainer.getChildren().addAll(header, scrollPane, footer);

        // Animar cards
        animarEntradaCards(content);
    }

    private VBox criarHeader() {
        VBox header = new VBox(0);
        header.setStyle(
                "-fx-background-color: " +
                        (TemaManager.isDarkMode() ? "rgba(30,41,59,0.95)" : "rgba(255,255,255,0.9)") + "; " +
                        "-fx-background-radius: 0 0 24px 24px; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-width: 0 0 1px 0; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0," +
                        TemaManager.getShadowOpacity() + "), 8, 0, 0, 3);"
        );

        HBox headerContent = new HBox(0);
        headerContent.setPadding(new Insets(20, 32, 20, 32));
        headerContent.setAlignment(Pos.CENTER_LEFT);

        // Logo
        HBox logoBox = criarLogo();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Métricas
        HBox metrics = criarMetricas();

        Region spacer2 = new Region();
        spacer2.setPrefWidth(16);

        // Toggle Dark Mode
        HBox toggleDM = criarToggleDarkMode();

        headerContent.getChildren().addAll(logoBox, spacer, metrics, spacer2, toggleDM);
        header.getChildren().add(headerContent);

        return header;
    }

    private HBox criarLogo() {
        HBox logoBox = new HBox(14);
        logoBox.setAlignment(Pos.CENTER_LEFT);

        StackPane iconContainer = new StackPane();
        iconContainer.setStyle(
                "-fx-background-color: linear-gradient(135deg, " +
                        TemaManager.getPrimary() + " 0%, " + TemaManager.getAccent() + " 100%); " +
                        "-fx-background-radius: 14px; " +
                        "-fx-min-width: 44px; -fx-min-height: 44px; " +
                        "-fx-max-width: 44px; -fx-max-height: 44px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(37,99,235,0.4), 10, 0, 0, 5);"
        );

        Label iconLabel = new Label("🪨");
        iconLabel.setStyle("-fx-font-size: 20px;");
        iconContainer.getChildren().add(iconLabel);

        VBox logoTexos = new VBox(2);

        Label titulo = new Label("Marmoraria Pro");
        titulo.setStyle(
                "-fx-font-size: 20px; -fx-font-weight: 800; " +
                        "-fx-text-fill: " + TemaManager.getTextPrimary() + "; " +
                        "-fx-font-family: 'Segoe UI', sans-serif;"
        );

        Label subtitulo = new Label("Sistema profissional de orçamentos");
        subtitulo.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-text-fill: " + TemaManager.getTextSecondary() + ";"
        );

        logoTexos.getChildren().addAll(titulo, subtitulo);
        logoBox.getChildren().addAll(iconContainer, logoTexos);

        return logoBox;
    }

    private HBox criarMetricas() {
        HBox metrics = new HBox(4);
        metrics.setAlignment(Pos.CENTER);
        metrics.setStyle(
                "-fx-background-color: " + TemaManager.getBg() + "; " +
                        "-fx-background-radius: 16px; -fx-padding: 6px; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-radius: 16px; -fx-border-width: 1px;"
        );

        metrics.getChildren().add(criarPill("📋",
                String.valueOf(GerenciadorArquivos.getTotalOrcamentos()),
                "Orçamentos", TemaManager.getPrimary()));

        metrics.getChildren().add(criarDivisorV());

        metrics.getChildren().add(criarPill("💾",
                String.valueOf(GerenciadorBackup.listarBackups().size()),
                "Backups", TemaManager.getSuccess()));

        metrics.getChildren().add(criarDivisorV());

        metrics.getChildren().add(criarPill("📦", "98", "Materiais", TemaManager.getWarning()));

        return metrics;
    }

    private HBox criarPill(String icone, String valor, String label, String cor) {
        HBox pill = new HBox(8);
        pill.setAlignment(Pos.CENTER_LEFT);
        pill.setPadding(new Insets(8, 14, 8, 14));

        Label lblIcone = new Label(icone);
        lblIcone.setStyle("-fx-font-size: 13px;");

        VBox textos = new VBox(1);

        Label lblValor = new Label(valor);
        lblValor.setStyle(
                "-fx-font-size: 15px; -fx-font-weight: 700; " +
                        "-fx-text-fill: " + TemaManager.getTextPrimary() + ";"
        );

        Label lblLabel = new Label(label);
        lblLabel.setStyle(
                "-fx-font-size: 10px; " +
                        "-fx-text-fill: " + TemaManager.getTextTertiary() + "; " +
                        "-fx-font-weight: 500;"
        );

        textos.getChildren().addAll(lblValor, lblLabel);
        pill.getChildren().addAll(lblIcone, textos);

        return pill;
    }

    private Separator criarDivisorV() {
        Separator sep = new Separator();
        sep.setOrientation(javafx.geometry.Orientation.VERTICAL);
        sep.setStyle("-fx-background-color: " + TemaManager.getBorderLight() + "; -fx-padding: 0 0.5px;");
        return sep;
    }

    private HBox criarToggleDarkMode() {
        HBox container = new HBox(8);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(6, 10, 6, 10));
        container.setStyle(
                "-fx-background-color: " + TemaManager.getBg() + "; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-radius: 20px; -fx-border-width: 1px;"
        );

        // Sol
        Label sunIcon = new Label("☀️");
        sunIcon.setStyle("-fx-font-size: 14px; -fx-opacity: " + (isDarkMode ? "0.4" : "1.0") + ";");

        // Track
        StackPane track = new StackPane();
        String trackColor = isDarkMode ? TemaManager.getPrimary() : "#CBD5E1";
        track.setStyle(
                "-fx-background-color: " + trackColor + "; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-min-width: 48px; -fx-min-height: 26px; " +
                        "-fx-max-width: 48px; -fx-max-height: 26px; " +
                        "-fx-cursor: hand;"
        );

        // Thumb
        Circle thumb = new Circle(10);
        thumb.setFill(Color.WHITE);
        thumb.setEffect(new DropShadow(2, Color.rgb(0, 0, 0, 0.2)));

        if (isDarkMode) {
            StackPane.setAlignment(thumb, Pos.CENTER_RIGHT);
            StackPane.setMargin(thumb, new Insets(0, 4, 0, 0));
        } else {
            StackPane.setAlignment(thumb, Pos.CENTER_LEFT);
            StackPane.setMargin(thumb, new Insets(0, 0, 0, 4));
        }

        track.getChildren().add(thumb);

        // Lua
        Label moonIcon = new Label("🌙");
        moonIcon.setStyle("-fx-font-size: 14px; -fx-opacity: " + (isDarkMode ? "1.0" : "0.4") + ";");

        container.getChildren().addAll(sunIcon, track, moonIcon);

        // Clique
        track.setOnMouseClicked(e -> {
            isDarkMode = !isDarkMode;
            TemaManager.setDarkMode(isDarkMode);

            // Animar thumb
            TranslateTransition tt = new TranslateTransition(Duration.millis(250), thumb);
            tt.setToX(isDarkMode ? 11 : -11);
            tt.setInterpolator(Interpolator.EASE_OUT);
            tt.play();

            // Atualizar cor do track
            track.setStyle(
                    "-fx-background-color: " + (isDarkMode ? TemaManager.getPrimary() : "#CBD5E1") + "; " +
                            "-fx-background-radius: 20px; " +
                            "-fx-min-width: 48px; -fx-min-height: 26px; " +
                            "-fx-max-width: 48px; -fx-max-height: 26px; " +
                            "-fx-cursor: hand;"
            );

            // Atualizar ícones
            sunIcon.setStyle("-fx-font-size: 14px; -fx-opacity: " + (isDarkMode ? "0.4" : "1.0") + ";");
            moonIcon.setStyle("-fx-font-size: 14px; -fx-opacity: " + (isDarkMode ? "1.0" : "0.4") + ";");

            // Reconstruir interface com delay para animação
            PauseTransition delay = new PauseTransition(Duration.millis(300));
            delay.setOnFinished(ev -> reconstruirInterface());
            delay.play();
        });

        return container;
    }

    private void reconstruirInterface() {
        // Guardar referência da janela
        Stage stage = (Stage) rootContainer.getScene().getWindow();
        double width = stage.getWidth();
        double height = stage.getHeight();

        // Reconstruir
        construirInterface();

        // Atualizar cena
        mainScene.setRoot(rootContainer);
        mainScene.setFill(Color.web(TemaManager.getBg()));
    }

    private ScrollPane criarScrollPane() {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setPannable(true);
        scroll.setStyle(
                "-fx-background: transparent; " +
                        "-fx-background-color: transparent; " +
                        "-fx-padding: 0; -fx-border: none;"
        );
        return scroll;
    }

    private VBox criarConteudo() {
        VBox content = new VBox(0);
        content.setPadding(new Insets(32, 32, 32, 32));
        content.setStyle("-fx-background-color: transparent;");

        VBox sectionHeader = new VBox(6);
        sectionHeader.setPadding(new Insets(0, 0, 24, 0));

        Label title = new Label("Acesso rápido");
        title.setStyle(
                "-fx-font-size: 20px; -fx-font-weight: 700; " +
                        "-fx-text-fill: " + TemaManager.getTextPrimary() + ";"
        );

        Label desc = new Label("Selecione uma das opções abaixo para começar");
        desc.setStyle("-fx-font-size: 13px; -fx-text-fill: " + TemaManager.getTextSecondary() + ";");

        sectionHeader.getChildren().addAll(title, desc);

        FlowPane grid = criarGridCards();
        content.getChildren().addAll(sectionHeader, grid);
        return content;
    }

    private FlowPane criarGridCards() {
        FlowPane grid = new FlowPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setStyle("-fx-background-color: transparent;");

        grid.getChildren().add(criarCard("🧮", "Calculadora",
                "Calcule áreas, materiais e custos com precisão. 23 tipos de trabalho disponíveis.",
                TemaManager.getPrimary(), TemaManager.getPrimaryGhost()));
        grid.getChildren().add(criarCard("📋", "Novo Orçamento",
                "Crie orçamentos completos com dados do cliente e configurações financeiras.",
                TemaManager.getSuccess(), TemaManager.getSuccessGhost()));
        grid.getChildren().add(criarCard("📚", "Catálogo",
                "98 materiais com preços atualizados. Granitos, mármores, quartzos e mais.",
                TemaManager.getWarning(), TemaManager.getWarningGhost()));
        grid.getChildren().add(criarCard("💾", "Orçamentos Salvos",
                "Gerencie todos os orçamentos. Visualize, exporte PDF e remova.",
                TemaManager.getAccent(), TemaManager.getAccentGhost()));
        grid.getChildren().add(criarCard("📊", "Dashboard",
                "Acompanhe métricas, faturamento e materiais mais usados com gráficos.",
                TemaManager.getInfo(), TemaManager.getInfoGhost()));
        grid.getChildren().add(criarCard("🔄", "Backups",
                "Backup automático diário. Restaure dados com total segurança.",
                "#0EA5E9", "#164E63"));
        grid.getChildren().add(criarCard("⚙️", "Configurações",
                "Personalize margens, prazos de entrega, notificações e dados.",
                "#64748B", "#1E293B"));
        grid.getChildren().add(criarCard("❓", "Ajuda",
                "Guia rápido de uso, dicas e contato de suporte.",
                "#475569", "#1E293B"));
        grid.getChildren().add(criarCard("ℹ️", "Sobre",
                "Marmoraria Pro v3.0. Tecnologias e equipe de desenvolvimento.",
                "#94A3B8", "#1E293B"));

        return grid;
    }

    private VBox criarCard(String icone, String titulo, String descricao, String cor, String bgCor) {
        VBox card = new VBox(0);
        card.setStyle(
                "-fx-background-color: " + TemaManager.getSurface() + "; " +
                        "-fx-background-radius: 16px; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-radius: 16px; -fx-border-width: 1px; " +
                        "-fx-padding: 0; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0," +
                        TemaManager.getShadowOpacity() + "), 4, 0, 0, 2);"
        );
        card.setPrefWidth(340);
        card.setMinHeight(200);
        card.setCursor(Cursor.HAND);

        // Top area
        StackPane topArea = new StackPane();
        topArea.setStyle(
                "-fx-background-color: " + bgCor + "; " +
                        "-fx-background-radius: 16px 16px 0 0; " +
                        "-fx-min-height: 80px;"
        );
        topArea.setAlignment(Pos.CENTER);

        StackPane iconCircle = new StackPane();
        iconCircle.setStyle(
                "-fx-background-color: " + TemaManager.getSurface() + "; " +
                        "-fx-background-radius: 50%; " +
                        "-fx-min-width: 52px; -fx-min-height: 52px; " +
                        "-fx-max-width: 52px; -fx-max-height: 52px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0, 0, 3);"
        );

        Label iconLabel = new Label(icone);
        iconLabel.setStyle("-fx-font-size: 22px;");
        iconCircle.getChildren().add(iconLabel);
        topArea.getChildren().add(iconCircle);

        // Bottom area
        VBox bottomArea = new VBox(10);
        bottomArea.setPadding(new Insets(16, 20, 20, 20));

        Label titleLabel = new Label(titulo);
        titleLabel.setStyle(
                "-fx-font-size: 15px; -fx-font-weight: 700; " +
                        "-fx-text-fill: " + TemaManager.getTextPrimary() + ";"
        );

        Label descLabel = new Label(descricao);
        descLabel.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-text-fill: " + TemaManager.getTextSecondary() + "; " +
                        "-fx-line-spacing: 4px;"
        );
        descLabel.setWrapText(true);

        HBox actionRow = new HBox(0);
        actionRow.setAlignment(Pos.CENTER_LEFT);
        actionRow.setPadding(new Insets(4, 0, 0, 0));

        Label acaoLabel = new Label("Acessar →");
        acaoLabel.setStyle(
                "-fx-font-size: 12px; -fx-font-weight: 600; " +
                        "-fx-text-fill: " + cor + ";"
        );

        actionRow.getChildren().add(acaoLabel);
        bottomArea.getChildren().addAll(titleLabel, descLabel, actionRow);
        card.getChildren().addAll(topArea, bottomArea);

        // Hover
        card.setOnMouseEntered(e -> {
            card.setStyle(
                    "-fx-background-color: " + TemaManager.getSurfaceHover() + "; " +
                            "-fx-background-radius: 16px; " +
                            "-fx-border-color: " + cor + "; " +
                            "-fx-border-radius: 16px; -fx-border-width: 1px; " +
                            "-fx-padding: 0; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0," +
                            TemaManager.getShadowHoverOpacity() + "), 12, 0, 0, 8); " +
                            "-fx-translate-y: -4px;"
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(200), iconCircle);
            st.setToX(1.08); st.setToY(1.08); st.play();
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                    "-fx-background-color: " + TemaManager.getSurface() + "; " +
                            "-fx-background-radius: 16px; " +
                            "-fx-border-color: " + TemaManager.getBorder() + "; " +
                            "-fx-border-radius: 16px; -fx-border-width: 1px; " +
                            "-fx-padding: 0; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0," +
                            TemaManager.getShadowOpacity() + "), 4, 0, 0, 2);"
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(200), iconCircle);
            st.setToX(1.0); st.setToY(1.0); st.play();
        });

        card.setOnMouseClicked(e -> navegar(titulo));

        return card;
    }

    private void navegar(String titulo) {
        try {
            Region view = null;
            int w = 1050, h = 680;

            switch (titulo) {
                case "Calculadora": view = new OrcamentoView(); w = 1150; break;
                case "Novo Orçamento": view = new OrcamentoView(); w = 1150; break;
                case "Catálogo": view = new CatalogoOnlineView(); break;
                case "Orçamentos Salvos": view = new GestaoOrcamentosView(); break;
                case "Dashboard": view = new DashboardView(); w = 1150; h = 720; break;
                case "Backups": view = new GestaoBackupView(); w = 950; h = 620; break;
                case "Configurações": view = new ConfiguracoesView(); w = 950; h = 620; break;
                case "Ajuda": mostrarAjuda(); return;
                case "Sobre": mostrarSobre(); return;
            }

            if (view != null) {
                Scene scene = new Scene(view, w, h);
                Stage stage = new Stage();
                stage.setTitle(titulo + " - Marmoraria Pro");
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            LOGGER.severe("Erro: " + e.getMessage());
        }
    }

    private HBox criarFooter() {
        HBox footer = new HBox(0);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(14, 32, 14, 32));
        footer.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-width: 1px 0 0 0;"
        );

        Label text = new Label("© 2026 Marmoraria Pro  •  v3.0.0  •  Desenvolvido com JavaFX");
        text.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TemaManager.getTextTertiary() + ";");

        footer.getChildren().add(text);
        return footer;
    }

    private void animarEntradaCards(VBox content) {
        if (content.getChildren().size() < 2) return;

        javafx.scene.Node node = content.getChildren().get(1);
        if (node instanceof FlowPane) {
            FlowPane grid = (FlowPane) node;
            for (int i = 0; i < grid.getChildren().size(); i++) {
                final javafx.scene.Node card = grid.getChildren().get(i);
                card.setOpacity(0);
                card.setTranslateY(30);

                final int index = i;
                PauseTransition delay = new PauseTransition(Duration.millis(80 * index));
                delay.setOnFinished(e -> {
                    FadeTransition fade = new FadeTransition(Duration.millis(500), card);
                    fade.setFromValue(0); fade.setToValue(1);

                    TranslateTransition slide = new TranslateTransition(Duration.millis(500), card);
                    slide.setFromY(30); slide.setToY(0);

                    new ParallelTransition(fade, slide).play();
                });
                delay.play();
            }
        }
    }

    private void mostrarAjuda() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajuda");
        alert.setHeaderText("📚 Central de Ajuda");
        alert.setContentText("🧮 CALCULADORA\n• 23 tipos de trabalho\n• 98 materiais\n\n📋 ORÇAMENTOS\n• Salve em JSON\n• Exporte PDF\n\n🌓 MODO ESCURO\n• Clique ☀️/🌙 no header");
        alert.showAndWait();
    }

    private void mostrarSobre() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre");
        alert.setHeaderText("🪨 Marmoraria Pro v3.0");
        alert.setContentText("Sistema Profissional para Marmorarias\n\n✨ 98 materiais • 23 tipos\n🌓 Modo Escuro\n\nJava 11+ • JavaFX • Maven\n© 2026");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        GerenciadorArquivos.inicializar();
        launch(args);
    }
}