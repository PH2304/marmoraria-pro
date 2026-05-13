package br.com.marmoraria.util;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.function.Consumer;

/**
 * Componentes High-Tech estilo Wall Street / Trading Terminal
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class HighTechComponents {

    // ==================== CARDS NEON PREMIUM ====================

    /**
     * Card com efeito neon e brilho pulsante
     */
    public static VBox createNeonCard(String icon, String title, String value, String subtitle,
                                      String neonColor, Runnable onClick) {
        VBox card = new VBox(DesignSystemHighTech.SPACE_MD);
        card.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-border-color: %s; -fx-border-radius: %dpx; -fx-border-width: 1px; " +
                        "-fx-padding: %dpx; -fx-cursor: hand;",
                DesignSystemHighTech.GRAY_800, (int)DesignSystemHighTech.RADIUS_LG,
                neonColor + "40", (int)DesignSystemHighTech.RADIUS_LG,
                (int)DesignSystemHighTech.SPACE_LG
        ));

        // Header com ícone e título
        HBox header = new HBox(DesignSystemHighTech.SPACE_SM);
        header.setAlignment(Pos.CENTER_LEFT);

        // Ícone container com brilho
        StackPane iconContainer = new StackPane();
        iconContainer.setStyle(String.format(
                "-fx-background-color: %s20; -fx-background-radius: %dpx; " +
                        "-fx-min-width: 44px; -fx-min-height: 44px;",
                neonColor, (int)DesignSystemHighTech.RADIUS_MD
        ));

        Label iconLabel = new Label(icon);
        iconLabel.setStyle(String.format("-fx-font-size: 20px;"));
        iconContainer.getChildren().add(iconLabel);

        VBox headerText = new VBox(2);
        Label titleLabel = new Label(title);
        titleLabel.setStyle(DesignSystemHighTech.getStyleCaption());
        titleLabel.setStyle(titleLabel.getStyle() + String.format("-fx-text-fill: %s;", neonColor));

        Label valueLabel = new Label(value);
        valueLabel.setStyle(DesignSystemHighTech.getStyleH2());

        headerText.getChildren().addAll(titleLabel, valueLabel);
        header.getChildren().addAll(iconContainer, headerText);

        // Separador neon
        Region separator = new Region();
        separator.setStyle(String.format(
                "-fx-background-color: %s; -fx-min-height: 1px; -fx-pref-height: 1px;",
                neonColor + "40"
        ));

        // Subtítulo
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle(DesignSystemHighTech.getStyleBody());
        subtitleLabel.setWrapText(true);

        // Botão de ação
        HBox actionBox = new HBox();
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        Label actionLabel = new Label("ACESSAR →");
        actionLabel.setStyle(String.format(
                "-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: %s; " +
                        "-fx-font-family: %s; -fx-letter-spacing: 1px;",
                neonColor, DesignSystemHighTech.FONT_PRIMARY
        ));
        actionBox.getChildren().add(actionLabel);

        card.getChildren().addAll(header, separator, subtitleLabel, actionBox);

        // Animação de glow ao hover
        final DropShadow[] glow = {null};

        card.setOnMouseEntered(e -> {
            card.setStyle(card.getStyle().replace(
                    neonColor + "40",
                    neonColor + "80"
            ));

            // Efeito de glow
            glow[0] = new DropShadow();
            glow[0].setColor(Color.web(neonColor));
            glow[0].setRadius(20);
            glow[0].setSpread(0.3);
            card.setEffect(glow[0]);

            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.02);
            st.setToY(1.02);
            st.setInterpolator(Interpolator.EASE_OUT);
            st.play();

            TranslateTransition tt = new TranslateTransition(Duration.millis(200), card);
            tt.setToY(-2);
            tt.play();
        });

        card.setOnMouseExited(e -> {
            card.setStyle(card.getStyle().replace(
                    neonColor + "80",
                    neonColor + "40"
            ));
            card.setEffect(null);

            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            TranslateTransition tt = new TranslateTransition(Duration.millis(200), card);
            tt.setToY(0);
            tt.play();
        });

        card.setOnMouseClicked(e -> {
            if (onClick != null) onClick.run();
            animateClick(card, neonColor);
        });

        return card;
    }

    /**
     * Card de métrica tipo terminal financeiro
     */
    public static VBox createMetricCard(String label, String value, String change, boolean isPositive) {
        VBox card = new VBox(DesignSystemHighTech.SPACE_XS);
        card.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-border-color: %s; -fx-border-radius: %dpx; -fx-border-width: 1px; " +
                        "-fx-padding: %dpx;",
                DesignSystemHighTech.GRAY_800, (int)DesignSystemHighTech.RADIUS_MD,
                DesignSystemHighTech.GRAY_700, (int)DesignSystemHighTech.RADIUS_MD,
                (int)DesignSystemHighTech.SPACE_MD
        ));
        card.setPrefWidth(180);

        Label labelLabel = new Label(label.toUpperCase());
        labelLabel.setStyle(DesignSystemHighTech.getStyleCaption());

        Label valueLabel = new Label(value);
        valueLabel.setStyle(DesignSystemHighTech.getStyleH3());

        HBox changeBox = new HBox(4);
        changeBox.setAlignment(Pos.CENTER_LEFT);

        String arrowIcon = isPositive ? "▲" : "▼";
        String changeColor = isPositive ? DesignSystemHighTech.GREEN_MARKET : DesignSystemHighTech.RED_MARKET;

        Label arrowLabel = new Label(arrowIcon);
        arrowLabel.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 12px;", changeColor));

        Label changeLabel = new Label(change);
        changeLabel.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 11px;", changeColor));

        changeBox.getChildren().addAll(arrowLabel, changeLabel);

        card.getChildren().addAll(labelLabel, valueLabel, changeBox);

        return card;
    }

    /**
     * Card de ativo (tipo ação da bolsa)
     */
    public static HBox createAssetCard(String name, String symbol, String price, String change, boolean isPositive) {
        HBox card = new HBox(DesignSystemHighTech.SPACE_MD);
        card.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-border-color: %s; -fx-border-radius: %dpx; -fx-border-width: 1px; " +
                        "-fx-padding: %dpx; -fx-cursor: hand;",
                DesignSystemHighTech.GRAY_800, (int)DesignSystemHighTech.RADIUS_MD,
                DesignSystemHighTech.GRAY_700, (int)DesignSystemHighTech.RADIUS_MD,
                (int)DesignSystemHighTech.SPACE_MD
        ));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPrefWidth(300);

        VBox leftBox = new VBox(2);
        Label nameLabel = new Label(name);
        nameLabel.setStyle(DesignSystemHighTech.getStyleBody());
        Label symbolLabel = new Label(symbol);
        symbolLabel.setStyle(DesignSystemHighTech.getStyleCaption());
        leftBox.getChildren().addAll(nameLabel, symbolLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox rightBox = new VBox(2);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        Label priceLabel = new Label(price);
        // CORRIGIDO: Agora getStyleH4() existe!
        priceLabel.setStyle(DesignSystemHighTech.getStyleH4());

        String changeColor = isPositive ? DesignSystemHighTech.GREEN_MARKET : DesignSystemHighTech.RED_MARKET;
        Label changeLabel = new Label((isPositive ? "+" : "") + change);
        changeLabel.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 11px;", changeColor));

        rightBox.getChildren().addAll(priceLabel, changeLabel);

        card.getChildren().addAll(leftBox, spacer, rightBox);

        card.setOnMouseEntered(e -> {
            card.setStyle(card.getStyle().replace(
                    DesignSystemHighTech.GRAY_800,
                    DesignSystemHighTech.GRAY_750
            ));
        });

        card.setOnMouseExited(e -> {
            card.setStyle(card.getStyle().replace(
                    DesignSystemHighTech.GRAY_750,
                    DesignSystemHighTech.GRAY_800
            ));
        });

        return card;
    }


    // ==================== BOTÕES HIGH-TECH ====================

    /**
     * Botão neon com efeito de pulsação
     */
    public static Button createNeonButton(String text, String neonColor) {
        Button btn = new Button(text.toUpperCase());
        btn.setStyle(String.format(
                "-fx-background-color: transparent; -fx-text-fill: %s; -fx-font-weight: bold; " +
                        "-fx-font-size: 12px; -fx-background-radius: %dpx; -fx-padding: 10px 20px; " +
                        "-fx-border-color: %s; -fx-border-radius: %dpx; -fx-border-width: 1.5px; " +
                        "-fx-cursor: hand; -fx-letter-spacing: 1px;",
                neonColor, (int)DesignSystemHighTech.RADIUS_MD,
                neonColor, (int)DesignSystemHighTech.RADIUS_MD
        ));

        final DropShadow[] glow = {null};

        btn.setOnMouseEntered(e -> {
            glow[0] = new DropShadow();
            glow[0].setColor(Color.web(neonColor));
            glow[0].setRadius(15);
            glow[0].setSpread(0.5);
            btn.setEffect(glow[0]);

            btn.setStyle(btn.getStyle().replace(
                    "transparent",
                    neonColor + "20"
            ));
        });

        btn.setOnMouseExited(e -> {
            btn.setEffect(null);
            btn.setStyle(btn.getStyle().replace(
                    neonColor + "20",
                    "transparent"
            ));
        });

        return btn;
    }

    /**
     * Botão de ação primária com gradiente neon
     */
    public static Button createPrimaryActionButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: %s; -fx-font-weight: bold; " +
                        "-fx-font-size: 13px; -fx-background-radius: %dpx; -fx-padding: 12px 24px; " +
                        "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,229,255,0.3), 10, 0, 0, 5);",
                DesignSystemHighTech.GRADIENT_NEON_BLUE,
                DesignSystemHighTech.GRAY_100,
                (int)DesignSystemHighTech.RADIUS_MD
        ));

        btn.setOnMouseEntered(e -> {
            btn.setStyle(btn.getStyle().replace(
                    "rgba(0,229,255,0.3), 10",
                    "rgba(0,229,255,0.5), 15"
            ));
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(btn.getStyle().replace(
                    "rgba(0,229,255,0.5), 15",
                    "rgba(0,229,255,0.3), 10"
            ));
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        btn.setOnMousePressed(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(80), btn);
            st.setToX(0.97);
            st.setToY(0.97);
            st.play();
        });

        btn.setOnMouseReleased(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(120), btn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        return btn;
    }

    // ==================== CAMPOS DE ENTRADA HIGH-TECH ====================

    /**
     * Campo de entrada com estilo terminal
     */
    public static TextField createTerminalTextField(String placeholder) {
        TextField field = new TextField();
        field.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-border-color: %s; -fx-border-radius: %dpx; -fx-border-width: 1px; " +
                        "-fx-padding: 12px 14px; -fx-font-family: %s; -fx-font-size: 13px; " +
                        "-fx-text-fill: %s; -fx-prompt-text-fill: %s;",
                DesignSystemHighTech.GRAY_800, (int)DesignSystemHighTech.RADIUS_MD,
                DesignSystemHighTech.GRAY_700, (int)DesignSystemHighTech.RADIUS_MD,
                DesignSystemHighTech.FONT_MONO,
                DesignSystemHighTech.CYAN_NEON,
                DesignSystemHighTech.GRAY_500
        ));
        field.setPromptText(placeholder);

        field.focusedProperty().addListener((obs, old, val) -> {
            if (val) {
                field.setStyle(field.getStyle().replace(
                        DesignSystemHighTech.GRAY_700,
                        DesignSystemHighTech.CYAN_NEON
                ));
            } else {
                field.setStyle(field.getStyle().replace(
                        DesignSystemHighTech.CYAN_NEON,
                        DesignSystemHighTech.GRAY_700
                ));
            }
        });

        return field;
    }

    /**
     * Campo de busca estilo terminal
     */
    public static HBox createTerminalSearchField(String placeholder, Consumer<String> onSearch) {
        HBox container = new HBox(DesignSystemHighTech.SPACE_XS);
        container.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-border-color: %s; -fx-border-radius: %dpx; -fx-border-width: 1px; " +
                        "-fx-padding: 10px 14px;",
                DesignSystemHighTech.GRAY_800, (int)DesignSystemHighTech.RADIUS_MD,
                DesignSystemHighTech.GRAY_700, (int)DesignSystemHighTech.RADIUS_MD
        ));
        container.setAlignment(Pos.CENTER_LEFT);

        Label searchIcon = new Label("🔍");
        searchIcon.setStyle(String.format("-fx-font-size: 12px; -fx-text-fill: %s;", DesignSystemHighTech.CYAN_NEON));

        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setStyle(
                "-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 0; " +
                        "-fx-font-family: " + DesignSystemHighTech.FONT_MONO + "; -fx-font-size: 12px; " +
                        "-fx-text-fill: " + DesignSystemHighTech.GRAY_300 + ";"
        );
        HBox.setHgrow(field, Priority.ALWAYS);

        // Indicador de digitação (blinking cursor effect)
        Circle cursorIndicator = new Circle(3);
        cursorIndicator.setFill(Color.web(DesignSystemHighTech.CYAN_NEON));
        cursorIndicator.setOpacity(0);

        Timeline blinkTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(cursorIndicator.opacityProperty(), 1)),
                new KeyFrame(Duration.millis(500), new KeyValue(cursorIndicator.opacityProperty(), 0)),
                new KeyFrame(Duration.millis(1000), new KeyValue(cursorIndicator.opacityProperty(), 1))
        );
        blinkTimeline.setCycleCount(Timeline.INDEFINITE);

        field.focusedProperty().addListener((obs, old, val) -> {
            if (val) {
                blinkTimeline.play();
                cursorIndicator.setOpacity(1);
            } else {
                blinkTimeline.stop();
                cursorIndicator.setOpacity(0);
            }
        });

        final Consumer<String> searchCallback = onSearch;
        field.textProperty().addListener((obs, old, val) -> {
            if (searchCallback != null) searchCallback.accept(val);
        });

        container.getChildren().addAll(searchIcon, field, cursorIndicator);
        return container;
    }

    // ==================== ANIMAÇÕES ====================

    private static void animateClick(Node node, String neonColor) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
        st.setToX(0.96);
        st.setToY(0.96);
        st.setOnFinished(e -> {
            ScaleTransition st2 = new ScaleTransition(Duration.millis(150), node);
            st2.setToX(1.0);
            st2.setToY(1.0);
            st2.play();
        });
        st.play();

        // Flash de luz
        final Glow glow = new Glow();
        glow.setLevel(0);
        node.setEffect(glow);

        Timeline flash = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0)),
                new KeyFrame(Duration.millis(80), new KeyValue(glow.levelProperty(), 0.8)),
                new KeyFrame(Duration.millis(200), new KeyValue(glow.levelProperty(), 0))
        );
        flash.setOnFinished(e -> node.setEffect(null));
        flash.play();
    }

    /**
     * Efeito de loading com pontos pulsantes estilo terminal
     */
    public static HBox createTerminalLoadingIndicator() {
        HBox indicator = new HBox(DesignSystemHighTech.SPACE_XS);
        indicator.setAlignment(Pos.CENTER);

        Circle[] dots = new Circle[3];
        for (int i = 0; i < 3; i++) {
            dots[i] = new Circle(4);
            dots[i].setFill(Color.web(DesignSystemHighTech.CYAN_NEON));
            dots[i].setOpacity(0.3);

            final int index = i;
            Timeline pulse = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(dots[index].opacityProperty(), 0.3)),
                    new KeyFrame(Duration.millis(400), new KeyValue(dots[index].opacityProperty(), 1)),
                    new KeyFrame(Duration.millis(800), new KeyValue(dots[index].opacityProperty(), 0.3))
            );
            pulse.setCycleCount(Timeline.INDEFINITE);
            pulse.setDelay(Duration.millis(i * 200));
            pulse.play();

            indicator.getChildren().add(dots[i]);
        }

        return indicator;
    }

    /**
     * Efeito de typing (simula digitação)
     */
    public static void animateTyping(TextArea area, String text, int delayMs) {
        final StringBuilder current = new StringBuilder();
        final int[] index = {0};

        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(delayMs), e -> {
            if (index[0] < text.length()) {
                current.append(text.charAt(index[0]));
                area.setText(current.toString());
                index[0]++;
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(text.length());
        timeline.play();
    }
}