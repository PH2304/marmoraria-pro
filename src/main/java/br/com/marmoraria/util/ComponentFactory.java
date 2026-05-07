package br.com.marmoraria.util;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Fábrica de componentes UI profissionais para Marmoraria Pro.
 * Compatível com Light/Dark mode via TemaManager.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class ComponentFactory {

    /**
     * Cria um card estilizado com hover effect
     */
    public static VBox criarCard(String icone, String titulo, String descricao, String cor) {
        VBox card = new VBox(10);
        card.setStyle(
                "-fx-background-color: " + TemaManager.getSurface() + "; " +
                        "-fx-background-radius: 12px; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-radius: 12px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0," +
                        (TemaManager.isDarkMode() ? "0.2" : "0.06") + "), 8, 0, 0, 2); " +
                        "-fx-padding: 20px;"
        );
        card.setMinWidth(220);
        card.setPrefWidth(260);
        card.setMaxWidth(280);
        card.setMinHeight(160);
        card.setCursor(Cursor.HAND);

        // Container do ícone
        StackPane iconeContainer = new StackPane();
        iconeContainer.setStyle(
                "-fx-background-color: " + cor + "15; " +
                        "-fx-background-radius: 50%; " +
                        "-fx-min-width: 56px; " +
                        "-fx-min-height: 56px; " +
                        "-fx-max-width: 56px; " +
                        "-fx-max-height: 56px;"
        );

        Label iconeLabel = new Label(icone);
        iconeLabel.setStyle("-fx-font-size: 24px;");
        iconeContainer.getChildren().add(iconeLabel);
        iconeContainer.setAlignment(Pos.CENTER);

        // Título
        Label tituloLabel = new Label(titulo);
        tituloLabel.setStyle(
                "-fx-font-size: 15px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: " + TemaManager.getTextPrimary() + "; " +
                        "-fx-font-family: 'Segoe UI', sans-serif;"
        );

        // Descrição
        Label descLabel = new Label(descricao);
        descLabel.setStyle(
                "-fx-font-size: 11px; " +
                        "-fx-text-fill: " + TemaManager.getTextSecondary() + "; " +
                        "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        descLabel.setWrapText(true);

        // Indicador
        Label indicador = new Label("→");
        indicador.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-text-fill: " + cor + "; " +
                        "-fx-font-weight: bold;"
        );

        HBox rowIndicador = new HBox();
        rowIndicador.setAlignment(Pos.CENTER_RIGHT);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        rowIndicador.getChildren().addAll(spacer2, indicador);

        card.getChildren().addAll(iconeContainer, tituloLabel, descLabel, rowIndicador);

        // Hover effect
        card.setOnMouseEntered(e -> {
            card.setStyle(
                    "-fx-background-color: " + TemaManager.getSurfaceHover() + "; " +
                            "-fx-background-radius: 12px; " +
                            "-fx-border-color: " + cor + "; " +
                            "-fx-border-radius: 12px; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0," +
                            (TemaManager.isDarkMode() ? "0.3" : "0.12") + "), 12, 0, 0, 6); " +
                            "-fx-padding: 20px; " +
                            "-fx-translate-y: -4px;"
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setToX(1.02);
            st.setToY(1.02);
            st.setInterpolator(Interpolator.EASE_OUT);
            st.play();
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                    "-fx-background-color: " + TemaManager.getSurface() + "; " +
                            "-fx-background-radius: 12px; " +
                            "-fx-border-color: " + TemaManager.getBorder() + "; " +
                            "-fx-border-radius: 12px; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0," +
                            (TemaManager.isDarkMode() ? "0.2" : "0.06") + "), 8, 0, 0, 2); " +
                            "-fx-padding: 20px;"
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.setInterpolator(Interpolator.EASE_OUT);
            st.play();
        });

        return card;
    }

    /**
     * Cria um botão primário estilizado
     */
    public static Button criarBotaoPrimario(String texto) {
        Button btn = new Button(texto);
        btn.setStyle(
                "-fx-background-color: " + TemaManager.getPrimary() + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 13px; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(37,99,235,0.3), 6, 0, 0, 3);"
        );
        btn.setCursor(Cursor.HAND);

        btn.setOnMouseEntered(e -> {
            btn.setStyle(
                    "-fx-background-color: " + (TemaManager.isDarkMode() ? "#2563EB" : "#1D4ED8") + "; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 13px; " +
                            "-fx-background-radius: 8px; " +
                            "-fx-padding: 10px 20px; " +
                            "-fx-cursor: hand; " +
                            "-fx-effect: dropshadow(gaussian, rgba(29,78,216,0.4), 8, 0, 0, 4);"
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(100), btn);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(
                    "-fx-background-color: " + TemaManager.getPrimary() + "; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 13px; " +
                            "-fx-background-radius: 8px; " +
                            "-fx-padding: 10px 20px; " +
                            "-fx-cursor: hand; " +
                            "-fx-effect: dropshadow(gaussian, rgba(37,99,235,0.3), 6, 0, 0, 3);"
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(100), btn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        return btn;
    }

    /**
     * Cria um botão de sucesso
     */
    public static Button criarBotaoSucesso(String texto) {
        Button btn = new Button(texto);
        btn.setStyle(
                "-fx-background-color: " + TemaManager.getSuccess() + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 13px; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(34,197,94,0.3), 6, 0, 0, 3);"
        );
        btn.setCursor(Cursor.HAND);
        return btn;
    }

    /**
     * Cria um botão de perigo
     */
    public static Button criarBotaoPerigo(String texto) {
        Button btn = new Button(texto);
        btn.setStyle(
                "-fx-background-color: #DC2626; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 13px; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(220,38,38,0.3), 6, 0, 0, 3);"
        );
        btn.setCursor(Cursor.HAND);
        return btn;
    }

    /**
     * Cria um campo de texto estilizado
     */
    public static TextField criarTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle(
                "-fx-background-color: " + TemaManager.getSurface() + "; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-radius: 8px; " +
                        "-fx-border-width: 1px; " +
                        "-fx-padding: 10px 14px; " +
                        "-fx-font-size: 13px; " +
                        "-fx-text-fill: " + TemaManager.getTextPrimary() + "; " +
                        "-fx-prompt-text-fill: " + TemaManager.getTextTertiary() + ";"
        );
        field.setPrefHeight(40);

        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                        "-fx-background-color: " + TemaManager.getSurface() + "; " +
                                "-fx-background-radius: 8px; " +
                                "-fx-border-color: " + TemaManager.getPrimary() + "; " +
                                "-fx-border-radius: 8px; " +
                                "-fx-border-width: 2px; " +
                                "-fx-padding: 10px 14px; " +
                                "-fx-font-size: 13px; " +
                                "-fx-text-fill: " + TemaManager.getTextPrimary() + "; " +
                                "-fx-effect: dropshadow(gaussian, rgba(59,130,246,0.15), 4, 0, 0, 2);"
                );
            } else {
                field.setStyle(
                        "-fx-background-color: " + TemaManager.getSurface() + "; " +
                                "-fx-background-radius: 8px; " +
                                "-fx-border-color: " + TemaManager.getBorder() + "; " +
                                "-fx-border-radius: 8px; " +
                                "-fx-border-width: 1px; " +
                                "-fx-padding: 10px 14px; " +
                                "-fx-font-size: 13px; " +
                                "-fx-text-fill: " + TemaManager.getTextPrimary() + ";"
                );
            }
        });

        return field;
    }

    /**
     * Cria um ComboBox estilizado
     */
    public static <T> ComboBox<T> criarComboBox(String prompt) {
        ComboBox<T> combo = new ComboBox<>();
        combo.setPromptText(prompt);
        combo.setStyle(
                "-fx-background-color: " + TemaManager.getSurface() + "; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-radius: 8px; " +
                        "-fx-border-width: 1px; " +
                        "-fx-padding: 8px; " +
                        "-fx-font-size: 13px;"
        );
        combo.setPrefHeight(40);
        combo.setMaxWidth(Double.MAX_VALUE);
        return combo;
    }

    /**
     * Cria um badge de status
     */
    public static Label criarBadge(String texto, String corFundo, String corTexto) {
        Label badge = new Label(texto);
        badge.setStyle(
                "-fx-background-color: " + corFundo + "; " +
                        "-fx-text-fill: " + corTexto + "; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 10px; " +
                        "-fx-padding: 4px 10px; " +
                        "-fx-background-radius: 20px;"
        );
        return badge;
    }

    /**
     * Cria um badge de sucesso
     */
    public static Label criarBadgeSucesso(String texto) {
        return criarBadge(texto, TemaManager.getSuccessGhost(), TemaManager.getSuccess());
    }

    /**
     * Cria um badge de erro
     */
    public static Label criarBadgeErro(String texto) {
        return criarBadge(texto, "#FEE2E2", "#DC2626");
    }

    /**
     * Cria um badge de aviso
     */
    public static Label criarBadgeAviso(String texto) {
        return criarBadge(texto, TemaManager.getWarningGhost(), TemaManager.getWarning());
    }

    /**
     * Cria um badge de informação
     */
    public static Label criarBadgeInfo(String texto) {
        return criarBadge(texto, TemaManager.getPrimaryGhost(), TemaManager.getPrimary());
    }

    /**
     * Cria um grupo de campo com label
     */
    public static VBox criarGrupoCampo(String label, Region campo) {
        VBox grupo = new VBox(6);

        Label lbl = new Label(label);
        lbl.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-font-weight: 600; " +
                        "-fx-text-fill: " + TemaManager.getTextSecondary() + ";"
        );

        grupo.getChildren().addAll(lbl, campo);
        return grupo;
    }
}