package br.com.marmoraria.util;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Sistema de notificações Toast não-intrusivas.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class ToastNotification {

    public enum Type {
        SUCCESS("✅", "#10B981", "#ECFDF5"),
        ERROR("❌", "#EF4444", "#FEF2F2"),
        WARNING("⚠️", "#F59E0B", "#FFFBEB"),
        INFO("ℹ️", "#3B82F6", "#EFF6FF"),
        SAVED("💾", "#8B5CF6", "#F5F3FF"),
        PDF("📄", "#0891B2", "#ECFEFF");

        final String icon;
        final String color;
        final String bgColor;

        Type(String icon, String color, String bgColor) {
            this.icon = icon;
            this.color = color;
            this.bgColor = bgColor;
        }
    }

    /**
     * Mostra uma notificação toast animada
     */
    public static void show(Stage stage, String message, Type type) {
        show(stage, message, type, 3000);
    }

    /**
     * Mostra uma notificação toast com duração personalizada
     */
    public static void show(Stage stage, String message, Type type, int durationMs) {
        // Container da notificação
        HBox toast = new HBox(12);
        toast.setAlignment(Pos.CENTER_LEFT);
        toast.setPadding(new Insets(14, 20, 14, 20));
        toast.setMaxWidth(420);
        toast.setStyle(
                "-fx-background-color: " + type.bgColor + "; " +
                        "-fx-background-radius: 12px; " +
                        "-fx-border-color: " + type.color + "30; " +
                        "-fx-border-radius: 12px; " +
                        "-fx-border-width: 1px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );

        // Ícone
        Label iconLabel = new Label(type.icon);
        iconLabel.setStyle("-fx-font-size: 18px;");

        // Mensagem
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-font-weight: 500; " +
                        "-fx-text-fill: #1F2937; " +
                        "-fx-wrap-text: true;"
        );
        messageLabel.setWrapText(true);

        // Botão fechar
        Label closeBtn = new Label("✕");
        closeBtn.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-text-fill: #9CA3AF; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 4px 6px; " +
                        "-fx-background-radius: 50%;"
        );
        closeBtn.setOnMouseClicked(e -> {
            animateOut(toast, null);
        });
        closeBtn.setOnMouseEntered(e ->
                closeBtn.setStyle(closeBtn.getStyle() + "-fx-background-color: rgba(0,0,0,0.05);"));
        closeBtn.setOnMouseExited(e ->
                closeBtn.setStyle(closeBtn.getStyle().replace("-fx-background-color: rgba(0,0,0,0.05);", "")));

        toast.getChildren().addAll(iconLabel, messageLabel, closeBtn);

        // Posicionar no topo direito
        StackPane root = (StackPane) stage.getScene().getRoot();
        if (!(root instanceof StackPane)) {
            // Se o root não for StackPane, usar o stage diretamente
            Pane glassPane = new Pane();
            glassPane.setMouseTransparent(true);
            glassPane.setStyle("-fx-background-color: transparent;");
            glassPane.getChildren().add(toast);

            // Adicionar ao root
            if (stage.getScene().getRoot() instanceof Pane) {
                ((Pane) stage.getScene().getRoot()).getChildren().add(glassPane);
            }
        } else {
            root.getChildren().add(toast);
            StackPane.setAlignment(toast, Pos.TOP_RIGHT);
            StackPane.setMargin(toast, new Insets(20, 20, 0, 0));
        }

        // Animar entrada
        toast.setTranslateY(-80);
        toast.setOpacity(0);

        Timeline enterAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(toast.translateYProperty(), -80, Interpolator.EASE_OUT),
                        new KeyValue(toast.opacityProperty(), 0, Interpolator.EASE_OUT)
                ),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(toast.translateYProperty(), 0, Interpolator.EASE_OUT),
                        new KeyValue(toast.opacityProperty(), 1, Interpolator.EASE_OUT)
                )
        );
        enterAnimation.play();

        // Auto-remover após duração
        PauseTransition delay = new PauseTransition(Duration.millis(durationMs));
        delay.setOnFinished(e -> animateOut(toast, () -> {
            if (toast.getParent() != null) {
                ((Pane) toast.getParent()).getChildren().remove(toast);
            }
        }));
        delay.play();
    }

    /**
     * Anima a saída do toast
     */
    private static void animateOut(HBox toast, Runnable onFinished) {
        Timeline exitAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(toast.translateYProperty(), 0, Interpolator.EASE_IN),
                        new KeyValue(toast.opacityProperty(), 1, Interpolator.EASE_IN)
                ),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(toast.translateYProperty(), -30, Interpolator.EASE_IN),
                        new KeyValue(toast.opacityProperty(), 0, Interpolator.EASE_IN)
                )
        );
        exitAnimation.setOnFinished(e -> {
            if (onFinished != null) onFinished.run();
        });
        exitAnimation.play();
    }
}