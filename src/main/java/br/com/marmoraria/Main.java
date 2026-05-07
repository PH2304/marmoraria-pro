package br.com.marmoraria;

import br.com.marmoraria.view.*;
import br.com.marmoraria.util.GerenciadorBackup;
import br.com.marmoraria.util.TemaManager;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    // Referência para a cena principal (para atualizar o CSS)
    private Scene mainScene;
    private StackPane toggleTrack;
    private Circle toggleThumb;
    private boolean isDarkMode = false;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: " + TemaManager.getBg() + ";");

        // Header
        VBox header = criarHeaderPremium();

        // Conteúdo
        ScrollPane scrollPane = criarScrollPane();
        VBox content = criarConteudoPremium();
        scrollPane.setContent(content);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Footer
        HBox footer = criarFooterMinimal();

        root.getChildren().addAll(header, scrollPane, footer);

        mainScene = new Scene(root, 1200, 800);
        mainScene.setFill(Color.web(TemaManager.getBg()));

        stage.setTitle("Marmoraria Pro");
        stage.setScene(mainScene);
        stage.setMinWidth(950);
        stage.setMinHeight(680);
        stage.show();

        animarEntradaCards(content);
        GerenciadorBackup.iniciarBackupAutomatico();
    }

    // ═══════════════════════════════════════════
    // HEADER PREMIUM COM TOGGLE DARK MODE
    // ═══════════════════════════════════════════

    private VBox criarHeaderPremium() {
        VBox header = new VBox(0);
        atualizarEstiloHeader(header);

        HBox headerContent = new HBox(0);
        headerContent.setPadding(new Insets(20, 32, 20, 32));
        headerContent.setAlignment(Pos.CENTER_LEFT);

        // Logo
        HBox logoBox = criarLogo();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Métricas
        HBox metrics = criarMetricasHeader();

        // Espaçador entre métricas e toggle
        Region spacer2 = new Region();
        spacer2.setPrefWidth(16);

        // Toggle Dark Mode
        HBox toggleDarkMode = criarToggleDarkMode();

        headerContent.getChildren().addAll(logoBox, spacer, metrics, spacer2, toggleDarkMode);
        header.getChildren().add(headerContent);

        return header;
    }

    private void atualizarEstiloHeader(VBox header) {
        header.setStyle(
                "-fx-background-color: " +
                        (isDarkMode ? "rgba(30,41,59,0.9)" : "rgba(255,255,255,0.85)") + "; " +
                        "-fx-background-radius: 0 0 24px 24px; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-width: 0 0 1px 0; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0," +
                        (isDarkMode ? "0.2" : "0.04") + "), 8, 0, 0, 3);"
        );
    }

    private HBox criarLogo() {
        HBox logoBox = new HBox(14);
        logoBox.setAlignment(Pos.CENTER_LEFT);

        StackPane iconContainer = new StackPane();
        iconContainer.setStyle(
                "-fx-background-color: linear-gradient(135deg, " +
                        TemaManager.getPrimary() + " 0%, " + TemaManager.getAccent() + " 100%); " +
                        "-fx-background-radius: 14px; " +
                        "-fx-min-width: 44px; " +
                        "-fx-min-height: 44px; " +
                        "-fx-max-width: 44px; " +
                        "-fx-max-height: 44px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(37,99,235,0.4), 10, 0, 0, 5);"
        );

        Label iconLabel = new Label("🪨");
        iconLabel.setStyle("-fx-font-size: 20px;");
        iconContainer.getChildren().add(iconLabel);

        VBox logoTexos = new VBox(2);

        Label titulo = new Label("Marmoraria Pro");
        titulo.setStyle(
                "-fx-font-size: 20px; " +
                        "-fx-font-weight: 800; " +
                        "-fx-text-fill: " + TemaManager.getTextPrimary() + "; " +
                        "-fx-font-family: 'Segoe UI', system-ui, sans-serif; " +
                        "-fx-letter-spacing: -0.5px;"
        );

        Label subtitulo = new Label("Sistema profissional de orçamentos para marmorarias");
        subtitulo.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-text-fill: " + TemaManager.getTextSecondary() + "; " +
                        "-fx-font-family: 'Segoe UI', sans-serif;"
        );

        logoTexos.getChildren().addAll(titulo, subtitulo);
        logoBox.getChildren().addAll(iconContainer, logoTexos);

        return logoBox;
    }

    private HBox criarMetricasHeader() {
        HBox metrics = new HBox(4);
        metrics.setAlignment(Pos.CENTER);
        metrics.setStyle(
                "-fx-background-color: " + TemaManager.getBg() + "; " +
                        "-fx-background-radius: 16px; " +
                        "-fx-padding: 6px; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-radius: 16px; " +
                        "-fx-border-width: 1px;"
        );

        metrics.getChildren().add(criarPillMetrica("📋",
                String.valueOf(br.com.marmoraria.util.GerenciadorArquivos.getTotalOrcamentos()),
                "Orçamentos", TemaManager.getPrimary()));

        metrics.getChildren().add(criarDivisorVertical());

        metrics.getChildren().add(criarPillMetrica("💾",
                String.valueOf(br.com.marmoraria.util.GerenciadorBackup.listarBackups().size()),
                "Backups", TemaManager.getSuccess()));

        metrics.getChildren().add(criarDivisorVertical());

        metrics.getChildren().add(criarPillMetrica("📦", "98", "Materiais", TemaManager.getWarning()));

        return metrics;
    }

    private HBox criarPillMetrica(String icone, String valor, String label, String cor) {
        HBox pill = new HBox(8);
        pill.setAlignment(Pos.CENTER_LEFT);
        pill.setPadding(new Insets(8, 14, 8, 14));

        Label lblIcone = new Label(icone);
        lblIcone.setStyle("-fx-font-size: 13px;");

        VBox textos = new VBox(1);

        Label lblValor = new Label(valor);
        lblValor.setStyle(
                "-fx-font-size: 15px; " +
                        "-fx-font-weight: 700; " +
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

    private Separator criarDivisorVertical() {
        Separator sep = new Separator();
        sep.setOrientation(javafx.geometry.Orientation.VERTICAL);
        sep.setStyle(
                "-fx-background-color: " + TemaManager.getBorderLight() + "; " +
                        "-fx-padding: 0 0.5px;"
        );
        return sep;
    }

    /**
     * Cria o toggle de Dark Mode estilizado
     */
    private HBox criarToggleDarkMode() {
        HBox toggleContainer = new HBox(8);
        toggleContainer.setAlignment(Pos.CENTER_LEFT);
        toggleContainer.setPadding(new Insets(6, 10, 6, 10));
        toggleContainer.setStyle(
                "-fx-background-color: " + TemaManager.getBg() + "; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-border-width: 1px;"
        );

        // Ícone do sol
        Label sunIcon = new Label("☀️");
        sunIcon.setStyle("-fx-font-size: 14px; -fx-opacity: " + (isDarkMode ? "0.4" : "1.0") + ";");

        // Track do toggle
        toggleTrack = new StackPane();
        toggleTrack.setStyle(
                "-fx-background-color: " + (isDarkMode ? TemaManager.getPrimary() : "#CBD5E1") + "; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-min-width: 48px; " +
                        "-fx-min-height: 26px; " +
                        "-fx-max-width: 48px; " +
                        "-fx-max-height: 26px; " +
                        "-fx-cursor: hand; " +
                        "-fx-transition: all 0.3s ease;"
        );

        // Thumb (bolinha)
        toggleThumb = new Circle(10);
        toggleThumb.setFill(Color.WHITE);
        toggleThumb.setEffect(new javafx.scene.effect.DropShadow(2, Color.rgb(0, 0, 0, 0.2)));

        if (isDarkMode) {
            StackPane.setAlignment(toggleThumb, Pos.CENTER_RIGHT);
            StackPane.setMargin(toggleThumb, new Insets(0, 4, 0, 0));
        } else {
            StackPane.setAlignment(toggleThumb, Pos.CENTER_LEFT);
            StackPane.setMargin(toggleThumb, new Insets(0, 0, 0, 4));
        }

        toggleTrack.getChildren().add(toggleThumb);

        // Ícone da lua
        Label moonIcon = new Label("🌙");
        moonIcon.setStyle("-fx-font-size: 14px; -fx-opacity: " + (isDarkMode ? "1.0" : "0.4") + ";");

        toggleContainer.getChildren().addAll(sunIcon, toggleTrack, moonIcon);

        // Ação de clique
        toggleTrack.setOnMouseClicked(e -> alternarModoEscuro(toggleContainer, sunIcon, moonIcon));

        return toggleContainer;
    }

    /**
     * Alterna entre modo claro e escuro
     */
    private void alternarModoEscuro(HBox toggleContainer, Label sunIcon, Label moonIcon) {
        isDarkMode = !isDarkMode;
        TemaManager.setDarkMode(isDarkMode);

        // Animar o thumb
        TranslateTransition tt = new TranslateTransition(Duration.millis(250), toggleThumb);
        tt.setToX(isDarkMode ? 11 : -11);
        tt.setInterpolator(Interpolator.EASE_OUT);
        tt.play();

        // Atualizar cor do track
        toggleTrack.setStyle(
                "-fx-background-color: " + (isDarkMode ? TemaManager.getPrimary() : "#CBD5E1") + "; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-min-width: 48px; " +
                        "-fx-min-height: 26px; " +
                        "-fx-max-width: 48px; " +
                        "-fx-max-height: 26px; " +
                        "-fx-cursor: hand;"
        );

        // Atualizar ícones
        sunIcon.setStyle("-fx-font-size: 14px; -fx-opacity: " + (isDarkMode ? "0.4" : "1.0") + ";");
        moonIcon.setStyle("-fx-font-size: 14px; -fx-opacity: " + (isDarkMode ? "1.0" : "0.4") + ";");

        // Recriar a cena inteira com o novo tema
        reconstruirInterface(toggleContainer);
    }

    /**
     * Reconstrói a interface com o novo tema
     */
    private void reconstruirInterface(HBox toggleContainer) {
        Stage stage = (Stage) toggleContainer.getScene().getWindow();

        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: " + TemaManager.getBg() + ";");

        VBox header = criarHeaderPremium();
        ScrollPane scrollPane = criarScrollPane();
        VBox content = criarConteudoPremium();
        scrollPane.setContent(content);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        HBox footer = criarFooterMinimal();

        root.getChildren().addAll(header, scrollPane, footer);

        mainScene = new Scene(root, stage.getWidth(), stage.getHeight());
        mainScene.setFill(Color.web(TemaManager.getBg()));

        // Aplicar CSS dinâmico
        aplicarCSSDinamico(mainScene);

        stage.setScene(mainScene);

        // Reanimar cards
        animarEntradaCards(content);
    }

    /**
     * Aplica CSS dinâmico baseado no tema
     */
    private void aplicarCSSDinamico(Scene scene) {
        String css = TemaManager.gerarCSSDinamico();

        // Aplicar como estilo inline no root
        javafx.scene.Parent root = scene.getRoot();
        if (root instanceof Region) {
            ((Region) root).setStyle(
                    ((Region) root).getStyle() +
                            "-fx-background-color: " + TemaManager.getBg() + ";"
            );
        }
    }

    // ═══════════════════════════════════════════
    // SCROLL PANE
    // ═══════════════════════════════════════════

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
                        "-fx-padding: 0; " +
                        "-fx-border: none;"
        );
        return scroll;
    }

    // ═══════════════════════════════════════════
    // CONTEÚDO PRINCIPAL
    // ═══════════════════════════════════════════

    private VBox criarConteudoPremium() {
        VBox content = new VBox(0);
        content.setPadding(new Insets(32, 32, 32, 32));
        content.setStyle("-fx-background-color: transparent;");

        VBox sectionHeader = new VBox(4);
        sectionHeader.setPadding(new Insets(0, 0, 24, 0));

        Label sectionTitle = new Label("Acesso rápido");
        sectionTitle.setStyle(
                "-fx-font-size: 18px; " +
                        "-fx-font-weight: 700; " +
                        "-fx-text-fill: " + TemaManager.getTextPrimary() + "; " +
                        "-fx-letter-spacing: -0.3px;"
        );

        Label sectionDesc = new Label("Selecione uma das opções abaixo para começar");
        sectionDesc.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-text-fill: " + TemaManager.getTextSecondary() + ";"
        );

        sectionHeader.getChildren().addAll(sectionTitle, sectionDesc);

        FlowPane cardsGrid = criarGridCardsRefinado();
        content.getChildren().addAll(sectionHeader, cardsGrid);
        return content;
    }

    // ═══════════════════════════════════════════
    // GRID DE CARDS
    // ═══════════════════════════════════════════

    private FlowPane criarGridCardsRefinado() {
        FlowPane grid = new FlowPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setStyle("-fx-background-color: transparent;");

        grid.getChildren().add(criarCardRefinado(
                "🧮", "Calculadora",
                "Calcule áreas, materiais e custos com precisão. 23 tipos de trabalho disponíveis.",
                TemaManager.getPrimary(), TemaManager.getPrimaryGhost(), "Calcular agora"
        ));

        grid.getChildren().add(criarCardRefinado(
                "📋", "Novo Orçamento",
                "Crie orçamentos completos com dados do cliente e configurações financeiras.",
                TemaManager.getSuccess(), TemaManager.getSuccessGhost(), "Criar orçamento"
        ));

        grid.getChildren().add(criarCardRefinado(
                "📚", "Catálogo",
                "98 materiais com preços atualizados. Granitos, mármores, quartzos e mais.",
                TemaManager.getWarning(), TemaManager.getWarningGhost(), "Ver catálogo"
        ));

        grid.getChildren().add(criarCardRefinado(
                "💾", "Orçamentos Salvos",
                "Gerencie todos os orçamentos. Visualize, exporte PDF e remova quando necessário.",
                TemaManager.getAccent(), TemaManager.getAccentGhost(), "Gerenciar"
        ));

        grid.getChildren().add(criarCardRefinado(
                "📊", "Dashboard",
                "Acompanhe métricas, faturamento e materiais mais usados com gráficos interativos.",
                TemaManager.getInfo(), TemaManager.getInfoGhost(), "Ver dashboard"
        ));

        grid.getChildren().add(criarCardRefinado(
                "🔄", "Backups",
                "Backup automático diário. Restaure dados anteriores com total segurança.",
                "#0EA5E9", isDarkMode ? "#164E63" : "#F0F9FF", "Gerenciar backups"
        ));

        grid.getChildren().add(criarCardRefinado(
                "⚙️", "Configurações",
                "Personalize margens, prazos de entrega, notificações e dados da sua empresa.",
                "#64748B", isDarkMode ? "#1E293B" : "#F8FAFC", "Configurar"
        ));

        grid.getChildren().add(criarCardRefinado(
                "❓", "Ajuda",
                "Guia rápido de uso, dicas para aproveitar melhor o sistema e contato de suporte.",
                "#475569", isDarkMode ? "#1E293B" : "#F1F5F9", "Obter ajuda"
        ));

        grid.getChildren().add(criarCardRefinado(
                "ℹ️", "Sobre",
                "Marmoraria Pro v3.0. Conheça as tecnologias e a equipe de desenvolvimento.",
                "#94A3B8", isDarkMode ? "#1E293B" : "#F8FAFC", "Saiba mais"
        ));

        return grid;
    }

    // ═══════════════════════════════════════════
    // CARD REFINADO
    // ═══════════════════════════════════════════

    private VBox criarCardRefinado(String icone, String titulo, String descricao,
                                   String cor, String corFundo, String acao) {
        VBox card = new VBox(0);
        card.setStyle(
                "-fx-background-color: " + TemaManager.getSurface() + "; " +
                        "-fx-background-radius: 16px; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-radius: 16px; " +
                        "-fx-border-width: 1px; " +
                        "-fx-padding: 0; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0," +
                        (isDarkMode ? "0.2" : "0.03") + "), 4, 0, 0, 2);"
        );
        card.setPrefWidth(340);
        card.setMinHeight(200);
        card.setCursor(Cursor.HAND);

        // Área superior com ícone
        StackPane topArea = new StackPane();
        topArea.setStyle(
                "-fx-background-color: " + corFundo + "; " +
                        "-fx-background-radius: 16px 16px 0 0; " +
                        "-fx-min-height: 80px;"
        );
        topArea.setAlignment(Pos.CENTER);

        StackPane iconCircle = new StackPane();
        iconCircle.setStyle(
                "-fx-background-color: " + TemaManager.getSurface() + "; " +
                        "-fx-background-radius: 50%; " +
                        "-fx-min-width: 52px; " +
                        "-fx-min-height: 52px; " +
                        "-fx-max-width: 52px; " +
                        "-fx-max-height: 52px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0," +
                        (isDarkMode ? "0.3" : "0.08") + "), 6, 0, 0, 3);"
        );

        Label iconLabel = new Label(icone);
        iconLabel.setStyle("-fx-font-size: 22px;");
        iconCircle.getChildren().add(iconLabel);
        topArea.getChildren().add(iconCircle);

        // Área inferior
        VBox bottomArea = new VBox(10);
        bottomArea.setPadding(new Insets(16, 20, 20, 20));

        Label titleLabel = new Label(titulo);
        titleLabel.setStyle(
                "-fx-font-size: 15px; " +
                        "-fx-font-weight: 700; " +
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

        Label acaoLabel = new Label(acao);
        acaoLabel.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-text-fill: " + cor + ";"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label arrow = new Label("→");
        arrow.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-text-fill: " + cor + "; " +
                        "-fx-font-weight: 700;"
        );

        actionRow.getChildren().addAll(acaoLabel, spacer, arrow);
        bottomArea.getChildren().addAll(titleLabel, descLabel, actionRow);

        card.getChildren().addAll(topArea, bottomArea);

        // Hover
        card.setOnMouseEntered(e -> {
            card.setStyle(
                    "-fx-background-color: " + TemaManager.getSurfaceHover() + "; " +
                            "-fx-background-radius: 16px; " +
                            "-fx-border-color: " + cor + "40; " +
                            "-fx-border-radius: 16px; " +
                            "-fx-border-width: 1px; " +
                            "-fx-padding: 0; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0," +
                            (isDarkMode ? "0.4" : "0.1") + "), 12, 0, 0, 6); " +
                            "-fx-translate-y: -4px;"
            );

            ScaleTransition st = new ScaleTransition(Duration.millis(200), iconCircle);
            st.setToX(1.08);
            st.setToY(1.08);
            st.play();

            TranslateTransition tt = new TranslateTransition(Duration.millis(200), arrow);
            tt.setToX(5);
            tt.play();
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                    "-fx-background-color: " + TemaManager.getSurface() + "; " +
                            "-fx-background-radius: 16px; " +
                            "-fx-border-color: " + TemaManager.getBorder() + "; " +
                            "-fx-border-radius: 16px; " +
                            "-fx-border-width: 1px; " +
                            "-fx-padding: 0; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0," +
                            (isDarkMode ? "0.2" : "0.03") + "), 4, 0, 0, 2);"
            );

            ScaleTransition st = new ScaleTransition(Duration.millis(200), iconCircle);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            TranslateTransition tt = new TranslateTransition(Duration.millis(200), arrow);
            tt.setToX(0);
            tt.play();
        });

        // Clique
        card.setOnMouseClicked(e -> {
            switch (titulo) {
                case "Calculadora": abrirCalculadora(); break;
                case "Novo Orçamento": abrirOrcamentos(); break;
                case "Catálogo": abrirCatalogo(); break;
                case "Orçamentos Salvos": abrirGestaoOrcamentos(); break;
                case "Dashboard": abrirDashboard(); break;
                case "Backups": abrirGestaoBackup(); break;
                case "Configurações": abrirConfiguracoes(); break;
                case "Ajuda": mostrarAjuda(); break;
                case "Sobre": mostrarSobre(); break;
            }
        });

        return card;
    }

    // ═══════════════════════════════════════════
    // FOOTER
    // ═══════════════════════════════════════════

    private HBox criarFooterMinimal() {
        HBox footer = new HBox(0);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(14, 32, 14, 32));
        footer.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-width: 1px 0 0 0;"
        );

        Label text = new Label("© 2026 Marmoraria Pro  •  v3.0.0  •  Desenvolvido com JavaFX");
        text.setStyle(
                "-fx-font-size: 11px; " +
                        "-fx-text-fill: " + TemaManager.getTextTertiary() + "; " +
                        "-fx-font-weight: 400;"
        );

        footer.getChildren().add(text);
        return footer;
    }

    // ═══════════════════════════════════════════
    // ANIMAÇÕES
    // ═══════════════════════════════════════════

    private void animarEntradaCards(VBox content) {
        javafx.scene.Node flowPane = content.getChildren().get(1);

        if (flowPane instanceof FlowPane) {
            FlowPane grid = (FlowPane) flowPane;
            javafx.scene.Node[] cards = grid.getChildren().toArray(new javafx.scene.Node[0]);

            for (int i = 0; i < cards.length; i++) {
                final javafx.scene.Node card = cards[i];
                card.setOpacity(0);
                card.setTranslateY(30);

                final int index = i;
                PauseTransition delay = new PauseTransition(Duration.millis(80 * index));
                delay.setOnFinished(e -> {
                    FadeTransition fade = new FadeTransition(Duration.millis(500), card);
                    fade.setFromValue(0);
                    fade.setToValue(1);
                    fade.setInterpolator(Interpolator.EASE_OUT);

                    TranslateTransition slide = new TranslateTransition(Duration.millis(500), card);
                    slide.setFromY(30);
                    slide.setToY(0);
                    slide.setInterpolator(Interpolator.EASE_OUT);

                    new ParallelTransition(fade, slide).play();
                });
                delay.play();
            }
        }
    }

    // ═══════════════════════════════════════════
    // NAVEGAÇÃO
    // ═══════════════════════════════════════════

    private void abrirCalculadora() { abrirJanela("Calculadora", new OrcamentoView(), 1150, 680); }
    private void abrirOrcamentos() { abrirJanela("Novo Orçamento", new OrcamentoView(), 1150, 680); }
    private void abrirCatalogo() { abrirJanela("Catálogo", new CatalogoOnlineView(), 1050, 680); }
    private void abrirGestaoOrcamentos() { abrirJanela("Orçamentos Salvos", new GestaoOrcamentosView(), 1050, 680); }
    private void abrirDashboard() { abrirJanela("Dashboard", new DashboardView(), 1150, 720); }
    private void abrirGestaoBackup() { abrirJanela("Backups", new GestaoBackupView(), 950, 620); }
    private void abrirConfiguracoes() { abrirJanela("Configurações", new ConfiguracoesView(), 950, 620); }

    private void abrirJanela(String titulo, Region view, int width, int height) {
        try {
            Scene scene = new Scene(view, width, height);
            Stage stage = new Stage();
            stage.setTitle(titulo + " - Marmoraria Pro");
            stage.setScene(scene);
            stage.setMinWidth(850);
            stage.setMinHeight(580);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAjuda() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajuda");
        alert.setHeaderText("📚 Central de Ajuda");
        alert.setContentText(
                "🧮 CALCULADORA\n• 23 tipos de trabalho\n• 98 materiais com preços reais\n\n" +
                        "📋 ORÇAMENTOS\n• Salve em JSON\n• Exporte PDFs profissionais\n\n" +
                        "📊 DASHBOARD\n• Gráficos interativos\n• Métricas em tempo real\n\n" +
                        "🔄 BACKUP\n• Automático a cada 24h\n• Restauração segura\n\n" +
                        "🌓 MODO ESCURO\n• Clique no toggle ☀️/🌙 no header"
        );
        alert.getDialogPane().setPrefWidth(480);
        alert.showAndWait();
    }

    private void mostrarSobre() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre");
        alert.setHeaderText("🪨 Marmoraria Pro v3.0");
        alert.setContentText(
                "Sistema Profissional para Marmorarias\n\n" +
                        "✨ Recursos\n• 98 materiais • 23 tipos\n• Dashboard • Backup\n• Modo Escuro 🌓\n\n" +
                        "🛠 Java 11+ • JavaFX • Maven\n\n" +
                        "© 2026 Todos os direitos reservados"
        );
        alert.getDialogPane().setPrefWidth(420);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        br.com.marmoraria.util.GerenciadorArquivos.inicializar();
        launch(args);
    }
}