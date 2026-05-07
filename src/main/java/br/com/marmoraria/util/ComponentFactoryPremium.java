package br.com.marmoraria.util;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * Fábrica de componentes UI ultra premium para Marmoraria Pro.
 * Design sofisticado com animações fluidas e micro-interações.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class ComponentFactoryPremium {

    // ==================== CARDS ====================

    /**
     * Cria um card premium com ícone animado e gradiente
     */
    public static VBox criarCardPremium(String icone, String titulo, String descricao,
                                        String cor, String gradient) {
        VBox card = new VBox(14);
        card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 18px; " +
                        "-fx-border-color: " + TemaPremium.SLATE_200 + "; " +
                        "-fx-border-radius: 18px; " +
                        "-fx-border-width: 1px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 5); " +
                        "-fx-padding: 24px;"
        );
        card.setMinWidth(280);
        card.setPrefWidth(340);
        card.setMinHeight(180);
        card.setCursor(Cursor.HAND);

        // Container do ícone com gradiente
        StackPane iconeContainer = new StackPane();
        iconeContainer.setStyle(
                "-fx-background-color: " + gradient + "; " +
                        "-fx-background-radius: 16px; " +
                        "-fx-min-width: 56px; " +
                        "-fx-min-height: 56px; " +
                        "-fx-max-width: 56px; " +
                        "-fx-max-height: 56px; " +
                        "-fx-effect: dropshadow(gaussian, " + cor + "40, 10, 0, 0, 5);"
        );

        Label iconeLabel = new Label(icone);
        iconeLabel.setStyle("-fx-font-size: 24px;");
        iconeContainer.getChildren().add(iconeLabel);
        StackPane.setAlignment(iconeLabel, Pos.CENTER);

        // Indicador de status (bolinha pulsante)
        Circle statusDot = new Circle(4);
        statusDot.setFill(Color.web(cor));
        statusDot.setOpacity(0.8);

        StackPane.setAlignment(statusDot, Pos.TOP_RIGHT);
        StackPane.setMargin(statusDot, new Insets(6, 6, 0, 0));
        iconeContainer.getChildren().add(statusDot);

        // Animação de pulso no status dot
        Timeline pulseDot = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(statusDot.opacityProperty(), 0.8),
                        new KeyValue(statusDot.radiusProperty(), 4)
                ),
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(statusDot.opacityProperty(), 0.3),
                        new KeyValue(statusDot.radiusProperty(), 5)
                ),
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(statusDot.opacityProperty(), 0.8),
                        new KeyValue(statusDot.radiusProperty(), 4)
                )
        );
        pulseDot.setCycleCount(Timeline.INDEFINITE);
        pulseDot.play();

        // Título
        Label tituloLabel = new Label(titulo);
        tituloLabel.setStyle(
                "-fx-font-size: 15px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: " + TemaPremium.SLATE_800 + "; " +
                        "-fx-font-family: 'Segoe UI', sans-serif;"
        );

        // Descrição
        Label descLabel = new Label(descricao);
        descLabel.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-text-fill: " + TemaPremium.SLATE_500 + "; " +
                        "-fx-font-family: 'Segoe UI', sans-serif; " +
                        "-fx-line-spacing: 5px;"
        );
        descLabel.setWrapText(true);

        // Separador sutil
        Separator sep = new Separator();
        sep.setStyle(
                "-fx-background-color: " + TemaPremium.SLATE_100 + "; " +
                        "-fx-padding: 4px 0;"
        );

        // Rodapé do card com indicador
        HBox footer = new HBox(0);
        footer.setAlignment(Pos.CENTER_LEFT);

        Label labelAcao = new Label("Acessar");
        labelAcao.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-text-fill: " + cor + ";"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Seta animada
        Label seta = new Label("→");
        seta.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-text-fill: " + cor + "; " +
                        "-fx-font-weight: bold;"
        );

        footer.getChildren().addAll(labelAcao, spacer, seta);

        card.getChildren().addAll(iconeContainer, tituloLabel, descLabel, sep, footer);

        // Hover effects premium
        card.setOnMouseEntered(e -> {
            card.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-background-radius: 18px; " +
                            "-fx-border-color: " + cor + "; " +
                            "-fx-border-radius: 18px; " +
                            "-fx-border-width: 1.5px; " +
                            "-fx-effect: dropshadow(gaussian, " + cor + "25, 20, 0, 0, 8); " +
                            "-fx-padding: 24px; " +
                            "-fx-translate-y: -6px;"
            );

            // Animação suave do ícone
            ScaleTransition stIcon = new ScaleTransition(Duration.millis(200), iconeContainer);
            stIcon.setToX(1.08);
            stIcon.setToY(1.08);
            stIcon.setInterpolator(Interpolator.EASE_OUT);
            stIcon.play();

            // Animação da seta
            TranslateTransition ttSeta = new TranslateTransition(Duration.millis(200), seta);
            ttSeta.setToX(5);
            ttSeta.setInterpolator(Interpolator.EASE_OUT);
            ttSeta.play();
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-background-radius: 18px; " +
                            "-fx-border-color: " + TemaPremium.SLATE_200 + "; " +
                            "-fx-border-radius: 18px; " +
                            "-fx-border-width: 1px; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 5); " +
                            "-fx-padding: 24px;"
            );

            ScaleTransition stIcon = new ScaleTransition(Duration.millis(200), iconeContainer);
            stIcon.setToX(1.0);
            stIcon.setToY(1.0);
            stIcon.setInterpolator(Interpolator.EASE_OUT);
            stIcon.play();

            TranslateTransition ttSeta = new TranslateTransition(Duration.millis(200), seta);
            ttSeta.setToX(0);
            ttSeta.setInterpolator(Interpolator.EASE_OUT);
            ttSeta.play();
        });

        return card;
    }

    /**
     * Cria um card estatístico pequeno
     */
    public static VBox criarCardEstatistica(String icone, String valor, String label, String cor) {
        VBox card = new VBox(8);
        card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 14px; " +
                        "-fx-border-color: " + TemaPremium.SLATE_200 + "; " +
                        "-fx-border-radius: 14px; " +
                        "-fx-border-width: 1px; " +
                        "-fx-padding: 18px;"
        );
        card.setMinWidth(180);
        card.setPrefWidth(200);

        // Ícone
        Label iconeLabel = new Label(icone);
        iconeLabel.setStyle("-fx-font-size: 20px; -fx-opacity: 0.8;");

        // Valor
        Label valorLabel = new Label(valor);
        valorLabel.setStyle(
                "-fx-font-size: 26px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: " + cor + ";"
        );

        // Label
        Label descLabel = new Label(label);
        descLabel.setStyle(
                "-fx-font-size: 11px; " +
                        "-fx-text-fill: " + TemaPremium.SLATE_500 + ";"
        );

        card.getChildren().addAll(iconeLabel, valorLabel, descLabel);

        return card;
    }

    // ==================== BOTÕES ====================

    /**
     * Cria um botão premium com gradiente
     */
    public static Button criarBotaoPremium(String texto, String gradient) {
        Button btn = new Button(texto);
        btn.setStyle(
                "-fx-background-color: " + gradient + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 13px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-padding: 12px 24px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 4);"
        );
        btn.setCursor(Cursor.HAND);

        btn.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.03);
            st.setToY(1.03);
            st.setInterpolator(Interpolator.EASE_OUT);
            st.play();
        });

        btn.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.setInterpolator(Interpolator.EASE_OUT);
            st.play();
        });

        btn.setOnMousePressed(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(80), btn);
            st.setToX(0.96);
            st.setToY(0.96);
            st.setInterpolator(Interpolator.EASE_IN);
            st.play();
        });

        btn.setOnMouseReleased(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(120), btn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.setInterpolator(Interpolator.EASE_OUT);
            st.play();
        });

        return btn;
    }

    /**
     * Cria um botão outline premium
     */
    public static Button criarBotaoOutlinePremium(String texto, String cor) {
        Button btn = new Button(texto);
        btn.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: " + cor + "; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 13px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-border-color: " + cor + "; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-border-width: 1.5px; " +
                        "-fx-padding: 10px 22px; " +
                        "-fx-cursor: hand;"
        );
        btn.setCursor(Cursor.HAND);

        btn.setOnMouseEntered(e -> {
            btn.setStyle(
                    "-fx-background-color: " + cor + "10; " +
                            "-fx-text-fill: " + cor + "; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 13px; " +
                            "-fx-background-radius: 10px; " +
                            "-fx-border-color: " + cor + "; " +
                            "-fx-border-radius: 10px; " +
                            "-fx-border-width: 2px; " +
                            "-fx-padding: 10px 22px; " +
                            "-fx-cursor: hand;"
            );
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-text-fill: " + cor + "; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 13px; " +
                            "-fx-background-radius: 10px; " +
                            "-fx-border-color: " + cor + "; " +
                            "-fx-border-radius: 10px; " +
                            "-fx-border-width: 1.5px; " +
                            "-fx-padding: 10px 22px; " +
                            "-fx-cursor: hand;"
            );
        });

        return btn;
    }

    /**
     * Cria um botão de ícone circular
     */
    public static Button criarBotaoIconePremium(String icone, String cor, String tooltip) {
        Button btn = new Button(icone);
        btn.setStyle(
                "-fx-background-color: " + cor + "15; " +
                        "-fx-text-fill: " + cor + "; " +
                        "-fx-font-size: 18px; " +
                        "-fx-background-radius: 50%; " +
                        "-fx-min-width: 42px; " +
                        "-fx-min-height: 42px; " +
                        "-fx-max-width: 42px; " +
                        "-fx-max-height: 42px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 0;"
        );
        btn.setCursor(Cursor.HAND);

        if (tooltip != null) {
            Tooltip tip = new Tooltip(tooltip);
            tip.setStyle(
                    "-fx-background-color: " + TemaPremium.SLATE_800 + "; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 11px; " +
                            "-fx-padding: 8px 12px; " +
                            "-fx-background-radius: 6px;"
            );
            btn.setTooltip(tip);
        }

        btn.setOnMouseEntered(e -> {
            btn.setStyle(
                    "-fx-background-color: " + cor + "; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 18px; " +
                            "-fx-background-radius: 50%; " +
                            "-fx-min-width: 42px; " +
                            "-fx-min-height: 42px; " +
                            "-fx-max-width: 42px; " +
                            "-fx-max-height: 42px; " +
                            "-fx-cursor: hand; " +
                            "-fx-padding: 0; " +
                            "-fx-effect: dropshadow(gaussian, " + cor + "40, 8, 0, 0, 4);"
            );
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(
                    "-fx-background-color: " + cor + "15; " +
                            "-fx-text-fill: " + cor + "; " +
                            "-fx-font-size: 18px; " +
                            "-fx-background-radius: 50%; " +
                            "-fx-min-width: 42px; " +
                            "-fx-min-height: 42px; " +
                            "-fx-max-width: 42px; " +
                            "-fx-max-height: 42px; " +
                            "-fx-cursor: hand; " +
                            "-fx-padding: 0;"
            );
        });

        return btn;
    }

    // ==================== CHIPS E BADGES ====================

    /**
     * Cria um chip/badge premium
     */
    public static Label criarChipPremium(String texto, String corFundo, String corTexto) {
        Label chip = new Label(texto);
        chip.setStyle(
                "-fx-background-color: " + corFundo + "; " +
                        "-fx-text-fill: " + corTexto + "; " +
                        "-fx-font-weight: 600; " +
                        "-fx-font-size: 10px; " +
                        "-fx-padding: 5px 12px; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-border-color: " + corTexto + "20; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-border-width: 1px;"
        );
        return chip;
    }

    /**
     * Cria um badge de status com ponto
     */
    public static HBox criarBadgeStatus(String texto, String cor, boolean ativo) {
        HBox badge = new HBox(8);
        badge.setAlignment(Pos.CENTER_LEFT);
        badge.setStyle(
                "-fx-background-color: " + cor + "15; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-padding: 6px 14px; " +
                        "-fx-border-color: " + cor + "30; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-border-width: 1px;"
        );

        // Ponto indicador
        Circle dot = new Circle(4);
        dot.setFill(ativo ? Color.web(cor) : Color.web(TemaPremium.SLATE_400));

        if (ativo) {
            // Animação de pulso no ponto
            Timeline pulse = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(dot.opacityProperty(), 1.0)),
                    new KeyFrame(Duration.seconds(1), new KeyValue(dot.opacityProperty(), 0.4)),
                    new KeyFrame(Duration.seconds(2), new KeyValue(dot.opacityProperty(), 1.0))
            );
            pulse.setCycleCount(Timeline.INDEFINITE);
            pulse.play();
        }

        Label textoLabel = new Label(texto);
        textoLabel.setStyle(
                "-fx-font-size: 11px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-text-fill: " + cor + ";"
        );

        badge.getChildren().addAll(dot, textoLabel);
        return badge;
    }

    // ==================== AVATARES ====================

    /**
     * Cria um avatar com iniciais
     */
    public static StackPane criarAvatar(String iniciais, String cor) {
        StackPane avatar = new StackPane();
        avatar.setStyle(
                "-fx-background-color: " + cor + "; " +
                        "-fx-background-radius: 50%; " +
                        "-fx-min-width: 40px; " +
                        "-fx-min-height: 40px; " +
                        "-fx-max-width: 40px; " +
                        "-fx-max-height: 40px; " +
                        "-fx-effect: dropshadow(gaussian, " + cor + "40, 6, 0, 0, 3);"
        );

        Label label = new Label(iniciais.toUpperCase().substring(0, Math.min(2, iniciais.length())));
        label.setStyle(
                "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px;"
        );

        avatar.getChildren().add(label);
        StackPane.setAlignment(label, Pos.CENTER);

        return avatar;
    }

    /**
     * Cria um avatar com ícone
     */
    public static StackPane criarAvatarIcone(String icone, String cor) {
        StackPane avatar = new StackPane();
        avatar.setStyle(
                "-fx-background-color: " + cor + "15; " +
                        "-fx-background-radius: 50%; " +
                        "-fx-min-width: 44px; " +
                        "-fx-min-height: 44px; " +
                        "-fx-max-width: 44px; " +
                        "-fx-max-height: 44px; " +
                        "-fx-border-color: " + cor + "30; " +
                        "-fx-border-radius: 50%; " +
                        "-fx-border-width: 1px;"
        );

        Label label = new Label(icone);
        label.setStyle("-fx-font-size: 18px;");

        avatar.getChildren().add(label);
        StackPane.setAlignment(label, Pos.CENTER);

        return avatar;
    }

    // ==================== CAMPOS DE ENTRADA ====================

    /**
     * Cria um campo de busca premium
     */
    public static HBox criarCampoBuscaPremium(String placeholder) {
        HBox searchBox = new HBox(10);
        searchBox.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12px; " +
                        "-fx-border-color: " + TemaPremium.SLATE_300 + "; " +
                        "-fx-border-radius: 12px; " +
                        "-fx-border-width: 1.5px; " +
                        "-fx-padding: 10px 16px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 4, 0, 0, 2);"
        );
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setMaxWidth(400);

        Label iconeBusca = new Label("🔍");
        iconeBusca.setStyle("-fx-font-size: 14px; -fx-opacity: 0.5;");

        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-width: 0; " +
                        "-fx-font-size: 13px; " +
                        "-fx-padding: 0; " +
                        "-fx-text-fill: " + TemaPremium.SLATE_800 + ";"
        );
        field.setPrefWidth(300);
        HBox.setHgrow(field, Priority.ALWAYS);

        // Botão limpar (aparece quando tem texto)
        Button btnLimpar = new Button("✕");
        btnLimpar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: " + TemaPremium.SLATE_400 + "; " +
                        "-fx-font-size: 12px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 2px 4px;"
        );
        btnLimpar.setVisible(false);
        btnLimpar.setOnAction(e -> {
            field.clear();
            btnLimpar.setVisible(false);
        });

        field.textProperty().addListener((obs, oldVal, newVal) -> {
            btnLimpar.setVisible(newVal != null && !newVal.isEmpty());
        });

        searchBox.getChildren().addAll(iconeBusca, field, btnLimpar);

        // Efeito de foco
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                searchBox.setStyle(
                        "-fx-background-color: white; " +
                                "-fx-background-radius: 12px; " +
                                "-fx-border-color: " + TemaPremium.AZUL_500 + "; " +
                                "-fx-border-radius: 12px; " +
                                "-fx-border-width: 2px; " +
                                "-fx-padding: 10px 16px; " +
                                "-fx-effect: dropshadow(gaussian, rgba(59,130,246,0.12), 6, 0, 0, 3);"
                );
            } else {
                searchBox.setStyle(
                        "-fx-background-color: white; " +
                                "-fx-background-radius: 12px; " +
                                "-fx-border-color: " + TemaPremium.SLATE_300 + "; " +
                                "-fx-border-radius: 12px; " +
                                "-fx-border-width: 1.5px; " +
                                "-fx-padding: 10px 16px; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 4, 0, 0, 2);"
                );
            }
        });

        return searchBox;
    }

    // ==================== INDICADORES ====================

    /**
     * Cria uma barra de progresso premium
     */
    public static ProgressBar criarProgressBarPremium(String cor) {
        ProgressBar pb = new ProgressBar();
        pb.setStyle(
                "-fx-accent: " + cor + "; " +
                        "-fx-pref-height: 6px; " +
                        "-fx-min-height: 6px; " +
                        "-fx-max-height: 6px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-background-color: " + TemaPremium.SLATE_100 + ";"
        );
        pb.setPrefWidth(200);
        pb.setProgress(0);
        return pb;
    }

    /**
     * Cria uma barra de progresso com label
     */
    public static VBox criarProgressBarComLabel(String label, String cor) {
        VBox box = new VBox(6);

        HBox header = new HBox(0);
        header.setAlignment(Pos.CENTER_LEFT);

        Label lbl = new Label(label);
        lbl.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-text-fill: " + TemaPremium.SLATE_600 + ";"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label percentLabel = new Label("0%");
        percentLabel.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: " + cor + ";"
        );

        header.getChildren().addAll(lbl, spacer, percentLabel);

        ProgressBar pb = criarProgressBarPremium(cor);
        pb.progressProperty().addListener((obs, oldVal, newVal) -> {
            percentLabel.setText(String.format("%.0f%%", newVal.doubleValue() * 100));
        });

        box.getChildren().addAll(header, pb);
        return box;
    }

    // ==================== TOGGLES ====================

    /**
     * Cria um toggle switch premium
     */
    public static HBox criarTogglePremium(String label, boolean initialState) {
        HBox toggle = new HBox(12);
        toggle.setAlignment(Pos.CENTER_LEFT);

        Label lbl = new Label(label);
        lbl.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-text-fill: " + TemaPremium.SLATE_700 + ";"
        );

        StackPane switchTrack = new StackPane();
        switchTrack.setStyle(
                "-fx-background-color: " + (initialState ? TemaPremium.AZUL_500 : TemaPremium.SLATE_300) + "; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-min-width: 44px; " +
                        "-fx-min-height: 24px; " +
                        "-fx-max-width: 44px; " +
                        "-fx-max-height: 24px; " +
                        "-fx-cursor: hand;"
        );

        Circle thumb = new Circle(10);
        thumb.setFill(Color.WHITE);
        thumb.setEffect(new DropShadow(2, Color.rgb(0, 0, 0, 0.2)));

        // Posição inicial
        if (initialState) {
            StackPane.setAlignment(thumb, Pos.CENTER_RIGHT);
            StackPane.setMargin(thumb, new Insets(0, 3, 0, 0));
        } else {
            StackPane.setAlignment(thumb, Pos.CENTER_LEFT);
            StackPane.setMargin(thumb, new Insets(0, 0, 0, 3));
        }

        switchTrack.getChildren().add(thumb);

        // Estado mutável
        final boolean[] state = {initialState};

        switchTrack.setOnMouseClicked(e -> {
            state[0] = !state[0];

            // Animação do thumb
            TranslateTransition tt = new TranslateTransition(Duration.millis(200), thumb);
            tt.setToX(state[0] ? 10 : -10);
            tt.setInterpolator(Interpolator.EASE_OUT);
            tt.play();

            // Mudar cor do track
            String novaCor = state[0] ? TemaPremium.AZUL_500 : TemaPremium.SLATE_300;
            switchTrack.setStyle(
                    "-fx-background-color: " + novaCor + "; " +
                            "-fx-background-radius: 20px; " +
                            "-fx-min-width: 44px; " +
                            "-fx-min-height: 24px; " +
                            "-fx-max-width: 44px; " +
                            "-fx-max-height: 24px; " +
                            "-fx-cursor: hand;"
            );
        });

        toggle.getChildren().addAll(lbl, switchTrack);
        return toggle;
    }

    // ==================== SEPARADORES ====================

    /**
     * Cria um divisor premium com label
     */
    public static HBox criarDivisorComLabel(String texto) {
        HBox divisor = new HBox(10);
        divisor.setAlignment(Pos.CENTER);
        divisor.setPadding(new Insets(10, 0, 10, 0));

        Region linhaEsquerda = new Region();
        linhaEsquerda.setStyle(
                "-fx-background-color: " + TemaPremium.SLATE_200 + "; " +
                        "-fx-min-height: 1px; " +
                        "-fx-pref-height: 1px;"
        );
        HBox.setHgrow(linhaEsquerda, Priority.ALWAYS);

        Label lbl = new Label(texto);
        lbl.setStyle(
                "-fx-font-size: 11px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-text-fill: " + TemaPremium.SLATE_400 + "; " +
                        "-fx-padding: 0 8px;"
        );

        Region linhaDireita = new Region();
        linhaDireita.setStyle(
                "-fx-background-color: " + TemaPremium.SLATE_200 + "; " +
                        "-fx-min-height: 1px; " +
                        "-fx-pref-height: 1px;"
        );
        HBox.setHgrow(linhaDireita, Priority.ALWAYS);

        divisor.getChildren().addAll(linhaEsquerda, lbl, linhaDireita);
        return divisor;
    }

    // ==================== GRUPOS DE FORMULÁRIO ====================

    /**
     * Cria um grupo de campo com label estilizado
     */
    public static VBox criarGrupoFormulario(String label, javafx.scene.Node campo) {
        VBox grupo = new VBox(6);

        Label lbl = new Label(label);
        lbl.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-text-fill: " + TemaPremium.SLATE_600 + ";"
        );

        grupo.getChildren().addAll(lbl, campo);
        return grupo;
    }

    /**
     * Cria um container de seção com título
     */
    public static VBox criarSecao(String titulo, String descricao, javafx.scene.Node conteudo) {
        VBox secao = new VBox(12);
        secao.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 14px; " +
                        "-fx-border-color: " + TemaPremium.SLATE_200 + "; " +
                        "-fx-border-radius: 14px; " +
                        "-fx-border-width: 1px; " +
                        "-fx-padding: 20px;"
        );

        VBox header = new VBox(2);
        Label tituloLabel = new Label(titulo);
        tituloLabel.setStyle(
                "-fx-font-size: 15px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: " + TemaPremium.SLATE_800 + ";"
        );

        Label descLabel = new Label(descricao);
        descLabel.setStyle(
                "-fx-font-size: 11px; " +
                        "-fx-text-fill: " + TemaPremium.SLATE_500 + "; " +
                        "-fx-wrap-text: true;"
        );
        descLabel.setWrapText(true);

        header.getChildren().addAll(tituloLabel, descLabel);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: " + TemaPremium.SLATE_100 + "; -fx-padding: 4px 0;");

        secao.getChildren().addAll(header, sep, conteudo);
        return secao;
    }

    // ==================== UTILITÁRIOS DE ANIMAÇÃO ====================

    /**
     * Anima a entrada de uma lista de nós sequencialmente
     */
    public static void animarEntradaSequencial(javafx.scene.Node[] nodes, double intervalo) {
        TemaPremium.animarFadeInSequencial(nodes, intervalo);
    }

    /**
     * Aplica efeito hover 3D em um nó
     */
    public static void aplicarHover3D(javafx.scene.Node node) {
        TemaPremium.aplicarHover3D(node);
    }
}