package br.com.marmoraria.util;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.function.Consumer;

/**
 * Componentes UI Premium com animações e micro-interações
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class PremiumComponents {

    // ==================== CARDS ANIMADOS ====================

    /**
     * Card com ícone animado e hover 3D
     */
    public static VBox createAnimatedCard(String icon, String title, String description,
                                          String gradient, Runnable onClick) {
        VBox card = new VBox(DesignSystem.SPACING_MD);
        card.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-border-color: %s; -fx-border-radius: %dpx; -fx-border-width: 1px; " +
                        "-fx-padding: %dpx; -fx-cursor: hand;",
                TemaManager.isDarkMode() ? DesignSystem.NEUTRAL_800 : "white",
                (int)DesignSystem.RADIUS_XL,
                TemaManager.isDarkMode() ? DesignSystem.NEUTRAL_700 : DesignSystem.NEUTRAL_200,
                (int)DesignSystem.RADIUS_XL,
                (int)DesignSystem.SPACING_LG
        ));

        // Ícone com gradiente
        StackPane iconContainer = new StackPane();
        iconContainer.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-min-width: 56px; -fx-min-height: 56px; -fx-max-width: 56px; -fx-max-height: 56px;",
                gradient, (int)DesignSystem.RADIUS_LG
        ));

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 28px;");
        iconContainer.getChildren().add(iconLabel);

        // Título
        Label titleLabel = new Label(title);
        titleLabel.setStyle(DesignSystem.getH3Style());

        // Descrição
        Label descLabel = new Label(description);
        descLabel.setStyle(DesignSystem.getBodyStyle());
        descLabel.setWrapText(true);

        // Indicador de ação
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_RIGHT);

        Label actionLabel = new Label("Acessar →");
        actionLabel.setStyle(String.format(
                "-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: %s;",
                DesignSystem.PRIMARY_500
        ));
        footer.getChildren().add(actionLabel);

        card.getChildren().addAll(iconContainer, titleLabel, descLabel, footer);

        // Animações
        card.setOnMouseEntered(e -> {
            card.setStyle(card.getStyle().replace(
                    TemaManager.isDarkMode() ? DesignSystem.NEUTRAL_800 : "white",
                    TemaManager.isDarkMode() ? DesignSystem.NEUTRAL_700 : DesignSystem.NEUTRAL_50
            ));

            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.02);
            st.setToY(1.02);
            st.setInterpolator(Interpolator.EASE_OUT);
            st.play();

            TranslateTransition tt = new TranslateTransition(Duration.millis(200), card);
            tt.setToY(-4);
            tt.play();

            card.setEffect(DesignSystem.getShadowLg());
        });

        card.setOnMouseExited(e -> {
            card.setStyle(card.getStyle().replace(
                    TemaManager.isDarkMode() ? DesignSystem.NEUTRAL_700 : DesignSystem.NEUTRAL_50,
                    TemaManager.isDarkMode() ? DesignSystem.NEUTRAL_800 : "white"
            ));

            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            TranslateTransition tt = new TranslateTransition(Duration.millis(200), card);
            tt.setToY(0);
            tt.play();

            card.setEffect(DesignSystem.getShadowMd());
        });

        card.setOnMouseClicked(e -> {
            if (onClick != null) onClick.run();
            animateClick(card);
        });

        card.setEffect(DesignSystem.getShadowMd());

        return card;
    }

    /**
     * Card estatístico moderno
     */
    public static HBox createStatCard(String icon, String value, String label, String color) {
        HBox card = new HBox(DesignSystem.SPACING_MD);
        card.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-border-color: %s; -fx-border-radius: %dpx; -fx-border-width: 1px; " +
                        "-fx-padding: %dpx;",
                TemaManager.isDarkMode() ? DesignSystem.NEUTRAL_800 : "white",
                (int)DesignSystem.RADIUS_LG,
                TemaManager.isDarkMode() ? DesignSystem.NEUTRAL_700 : DesignSystem.NEUTRAL_200,
                (int)DesignSystem.RADIUS_LG,
                (int)DesignSystem.SPACING_MD
        ));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPrefWidth(200);

        // Container do ícone
        StackPane iconContainer = new StackPane();
        iconContainer.setStyle(String.format(
                "-fx-background-color: %s20; -fx-background-radius: %dpx; " +
                        "-fx-min-width: 48px; -fx-min-height: 48px; -fx-max-width: 48px; -fx-max-height: 48px;",
                color, (int)DesignSystem.RADIUS_LG
        ));

        Label iconLabel = new Label(icon);
        iconLabel.setStyle(String.format("-fx-font-size: 22px;"));
        iconContainer.getChildren().add(iconLabel);

        // Textos
        VBox textBox = new VBox(4);

        Label valueLabel = new Label(value);
        valueLabel.setStyle(String.format(
                "-fx-font-size: 24px; -fx-font-weight: 800; -fx-text-fill: %s;",
                color
        ));

        Label labelLabel = new Label(label);
        labelLabel.setStyle(DesignSystem.getSmallStyle());

        textBox.getChildren().addAll(valueLabel, labelLabel);

        card.getChildren().addAll(iconContainer, textBox);

        return card;
    }

    /**
     * Card de progresso com barra animada
     */
    public static VBox createProgressCard(String title, double progress, String color) {
        VBox card = createAnimatedCard("📊", title,
                String.format("%.1f%% concluído", progress * 100),
                DesignSystem.GRADIENT_PRIMARY, null);

        // Barra de progresso customizada
        StackPane progressContainer = new StackPane();
        progressContainer.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-min-height: 8px; -fx-max-height: 8px;",
                TemaManager.isDarkMode() ? DesignSystem.NEUTRAL_700 : DesignSystem.NEUTRAL_200,
                (int)DesignSystem.RADIUS_FULL
        ));

        Rectangle progressBar = new Rectangle(0, 8);
        progressBar.setFill(Color.web(color));
        progressBar.setArcWidth(DesignSystem.RADIUS_FULL);
        progressBar.setArcHeight(DesignSystem.RADIUS_FULL);

        // Usar variável final para a lambda
        final double finalProgress = progress;

        // Animação da barra
        Timeline animation = new Timeline(
                new KeyFrame(Duration.millis(800),
                        new KeyValue(progressBar.widthProperty(),
                                progressContainer.widthProperty().multiply(finalProgress).getValue(),
                                Interpolator.EASE_OUT)
                )
        );

        progressContainer.widthProperty().addListener((obs, old, newVal) -> {
            progressBar.setWidth(newVal.doubleValue() * finalProgress);
        });

        progressContainer.getChildren().add(progressBar);

        card.getChildren().add(progressContainer);

        return card;
    }

    // ==================== BOTÕES PREMIUM ====================

    /**
     * Botão com efeito ripple e loading state
     */
    public static Button createRippleButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 14px; -fx-background-radius: %dpx; -fx-padding: 12px 24px; " +
                        "-fx-cursor: hand;",
                color, (int)DesignSystem.RADIUS_MD
        ));

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

    /**
     * Botão de ícone circular com tooltip
     */
    public static Button createIconButton(String icon, String tooltipText, String color) {
        Button btn = new Button(icon);
        btn.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: 16px; " +
                        "-fx-background-radius: %dpx; -fx-min-width: 36px; -fx-min-height: 36px; " +
                        "-fx-max-width: 36px; -fx-max-height: 36px; -fx-cursor: hand; -fx-padding: 0;",
                color, (int)DesignSystem.RADIUS_LG
        ));

        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: 11px; " +
                        "-fx-padding: 6px 12px; -fx-background-radius: %dpx;",
                DesignSystem.NEUTRAL_800, (int)DesignSystem.RADIUS_SM
        ));
        btn.setTooltip(tooltip);

        return btn;
    }

    /**
     * Toggle switch premium - CORRIGIDO
     */
    public static HBox createToggleSwitch(String label, boolean initialState, Consumer<Boolean> onChange) {
        HBox container = new HBox(DesignSystem.SPACING_SM);
        container.setAlignment(Pos.CENTER_LEFT);

        Label labelText = new Label(label);
        labelText.setStyle(DesignSystem.getBodyStyle());

        StackPane track = new StackPane();
        track.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-min-width: 48px; -fx-min-height: 24px; -fx-max-width: 48px; -fx-max-height: 24px; " +
                        "-fx-cursor: hand;",
                initialState ? DesignSystem.PRIMARY_500 : DesignSystem.NEUTRAL_400,
                (int)DesignSystem.RADIUS_FULL
        ));

        Circle thumb = new Circle(10);
        thumb.setFill(Color.WHITE);

        if (initialState) {
            StackPane.setAlignment(thumb, Pos.CENTER_RIGHT);
            StackPane.setMargin(thumb, new Insets(0, 3, 0, 0));
        } else {
            StackPane.setAlignment(thumb, Pos.CENTER_LEFT);
            StackPane.setMargin(thumb, new Insets(0, 0, 0, 3));
        }

        track.getChildren().add(thumb);

        // CORREÇÃO: Usar array para estado mutável dentro da lambda
        final boolean[] currentState = {initialState};

        track.setOnMouseClicked(e -> {
            boolean newState = !currentState[0];
            currentState[0] = newState;

            TranslateTransition tt = new TranslateTransition(Duration.millis(200), thumb);
            tt.setToX(newState ? 10 : -10);
            tt.play();

            String newColor = newState ? DesignSystem.PRIMARY_500 : DesignSystem.NEUTRAL_400;
            track.setStyle(track.getStyle().replace(
                    newState ? DesignSystem.NEUTRAL_400 : DesignSystem.PRIMARY_500,
                    newColor
            ));

            if (onChange != null) onChange.accept(newState);
        });

        container.getChildren().addAll(labelText, track);
        return container;
    }

    // ==================== CAMPOS DE ENTRADA ====================

    /**
     * Campo de busca com ícone e botão limpar - CORRIGIDO
     */
    public static HBox createSearchField(String placeholder, Consumer<String> onSearch) {
        HBox container = new HBox(DesignSystem.SPACING_XS);
        container.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-border-color: %s; -fx-border-radius: %dpx; -fx-border-width: 1px; " +
                        "-fx-padding: 10px 14px;",
                TemaManager.isDarkMode() ? DesignSystem.NEUTRAL_800 : "white",
                (int)DesignSystem.RADIUS_MD,
                TemaManager.isDarkMode() ? DesignSystem.NEUTRAL_700 : DesignSystem.NEUTRAL_300,
                (int)DesignSystem.RADIUS_MD
        ));
        container.setAlignment(Pos.CENTER_LEFT);

        Label searchIcon = new Label("🔍");
        searchIcon.setStyle("-fx-font-size: 14px; -fx-opacity: 0.5;");

        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setStyle(
                "-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 0; " +
                        "-fx-font-size: 13px;"
        );
        HBox.setHgrow(field, Priority.ALWAYS);

        Button clearBtn = new Button("✕");
        clearBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + DesignSystem.NEUTRAL_400 + "; " +
                        "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 2px;"
        );
        clearBtn.setVisible(false);

        // CORREÇÃO: Usar variável final para o callback
        final Consumer<String> searchCallback = onSearch;

        field.textProperty().addListener((obs, old, val) -> {
            clearBtn.setVisible(val != null && !val.isEmpty());
            if (searchCallback != null) searchCallback.accept(val);
        });

        clearBtn.setOnAction(e -> {
            field.clear();
            clearBtn.setVisible(false);
        });

        container.getChildren().addAll(searchIcon, field, clearBtn);
        return container;
    }

    /**
     * Campo de número com botões + e - - CORRIGIDO
     */
    public static HBox createNumberField(int initialValue, int min, int max, Consumer<Integer> onChange) {
        HBox container = new HBox(0);
        container.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-border-color: %s; -fx-border-radius: %dpx; -fx-border-width: 1px;",
                TemaManager.isDarkMode() ? DesignSystem.NEUTRAL_800 : "white",
                (int)DesignSystem.RADIUS_MD,
                TemaManager.isDarkMode() ? DesignSystem.NEUTRAL_700 : DesignSystem.NEUTRAL_300,
                (int)DesignSystem.RADIUS_MD
        ));

        Button minusBtn = new Button("−");
        minusBtn.setStyle(String.format(
                "-fx-background-color: transparent; -fx-text-fill: %s; -fx-font-weight: bold; " +
                        "-fx-font-size: 16px; -fx-min-width: 32px; -fx-min-height: 32px; -fx-cursor: hand;",
                DesignSystem.PRIMARY_500
        ));

        TextField field = new TextField(String.valueOf(initialValue));
        field.setStyle(
                "-fx-background-color: transparent; -fx-border-width: 0; -fx-text-alignment: center; " +
                        "-fx-font-size: 14px; -fx-min-width: 50px;"
        );
        field.setAlignment(Pos.CENTER);

        Button plusBtn = new Button("+");
        plusBtn.setStyle(String.format(
                "-fx-background-color: transparent; -fx-text-fill: %s; -fx-font-weight: bold; " +
                        "-fx-font-size: 16px; -fx-min-width: 32px; -fx-min-height: 32px; -fx-cursor: hand;",
                DesignSystem.PRIMARY_500
        ));

        // CORREÇÃO: Usar variáveis finais para os limites
        final int minValue = min;
        final int maxValue = max;
        final Consumer<Integer> changeCallback = onChange;

        minusBtn.setOnAction(e -> {
            int currentVal;
            try {
                currentVal = Integer.parseInt(field.getText());
            } catch (NumberFormatException ex) {
                currentVal = initialValue;
            }
            int newVal = currentVal - 1;
            if (newVal >= minValue) {
                field.setText(String.valueOf(newVal));
                if (changeCallback != null) changeCallback.accept(newVal);
            }
        });

        plusBtn.setOnAction(e -> {
            int currentVal;
            try {
                currentVal = Integer.parseInt(field.getText());
            } catch (NumberFormatException ex) {
                currentVal = initialValue;
            }
            int newVal = currentVal + 1;
            if (newVal <= maxValue) {
                field.setText(String.valueOf(newVal));
                if (changeCallback != null) changeCallback.accept(newVal);
            }
        });

        container.getChildren().addAll(minusBtn, field, plusBtn);
        return container;
    }

    // ==================== MENSAGENS ====================

    /**
     * Toast notification premium
     */
    public static void showToast(Pane parent, String message, String type) {
        HBox toast = new HBox(DesignSystem.SPACING_SM);
        toast.setAlignment(Pos.CENTER);
        toast.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-padding: %dpx %dpx; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 16, 0, 0, 4);",
                DesignSystem.NEUTRAL_800, (int)DesignSystem.RADIUS_LG,
                (int)DesignSystem.SPACING_SM, (int)DesignSystem.SPACING_LG
        ));

        String icon = type.equals("success") ? "✓" : type.equals("error") ? "✕" : "ℹ";
        String color = type.equals("success") ? DesignSystem.SUCCESS :
                type.equals("error") ? DesignSystem.ERROR : DesignSystem.INFO;

        Label iconLabel = new Label(icon);
        iconLabel.setStyle(String.format(
                "-fx-text-fill: %s; -fx-font-weight: bold; -fx-font-size: 14px;",
                color
        ));

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");

        toast.getChildren().addAll(iconLabel, messageLabel);

        // Posicionar no centro superior
        toast.setLayoutX((parent.getWidth() - 300) / 2);
        toast.setLayoutY(20);
        toast.setOpacity(0);

        parent.getChildren().add(toast);

        // Animações
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toast);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition wait = new PauseTransition(Duration.seconds(2.5));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), toast);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> parent.getChildren().remove(toast));

        fadeIn.play();
        wait.setOnFinished(e -> fadeOut.play());
        wait.play();
    }

    // ==================== ANIMAÇÕES ====================

    private static void animateClick(Node node) {
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
    }

    /**
     * Anima entrada de componentes em cascata
     */
    public static void animateStaggered(Node[] nodes, double delayBetween) {
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            node.setOpacity(0);
            node.setTranslateY(20);

            final int index = i;
            final double delay = index * delayBetween;

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(delay), e -> {
                        FadeTransition fade = new FadeTransition(Duration.millis(400), node);
                        fade.setFromValue(0);
                        fade.setToValue(1);

                        TranslateTransition slide = new TranslateTransition(Duration.millis(400), node);
                        slide.setFromY(20);
                        slide.setToY(0);

                        new ParallelTransition(fade, slide).play();
                    })
            );
            timeline.play();
        }
    }
}