package br.com.marmoraria.util;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Random;

/**
 * Efeitos visuais de celebração para ações importantes.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class CelebrationEffect {

    private static final Random random = new Random();

    // Cores vibrantes para confetes
    private static final String[] CONFETTI_COLORS = {
            "#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4",
            "#FFEAA7", "#DDA0DD", "#FF8C00", "#00CED1",
            "#FF69B4", "#7B68EE", "#00FA9A", "#FFD700"
    };

    /**
     * Efeito de confetes ao redor de um nó
     */
    public static void confetti(Pane parent, double x, double y) {
        for (int i = 0; i < 30; i++) {
            Circle confetti = new Circle(4 + random.nextDouble() * 6);
            confetti.setFill(Color.web(CONFETTI_COLORS[random.nextInt(CONFETTI_COLORS.length)]));
            confetti.setOpacity(0.9);
            confetti.setMouseTransparent(true);

            // Posição inicial
            confetti.setCenterX(x);
            confetti.setCenterY(y);

            parent.getChildren().add(confetti);

            // Animação de queda com curva
            double angle = random.nextDouble() * 360;
            double distance = 100 + random.nextDouble() * 200;
            double targetX = x + Math.cos(Math.toRadians(angle)) * distance;
            double targetY = y + Math.sin(Math.toRadians(angle)) * distance + 100;

            Timeline animation = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(confetti.centerXProperty(), x, Interpolator.EASE_OUT),
                            new KeyValue(confetti.centerYProperty(), y, Interpolator.EASE_OUT),
                            new KeyValue(confetti.opacityProperty(), 0.9, Interpolator.EASE_OUT),
                            new KeyValue(confetti.rotateProperty(), 0, Interpolator.EASE_OUT)
                    ),
                    new KeyFrame(Duration.millis(800 + random.nextInt(400)),
                            new KeyValue(confetti.centerXProperty(), targetX, Interpolator.EASE_IN),
                            new KeyValue(confetti.centerYProperty(), targetY, Interpolator.EASE_IN),
                            new KeyValue(confetti.opacityProperty(), 0, Interpolator.EASE_IN),
                            new KeyValue(confetti.rotateProperty(), random.nextInt(360), Interpolator.EASE_IN)
                    )
            );
            animation.setDelay(Duration.millis(random.nextInt(200)));
            animation.setOnFinished(e -> parent.getChildren().remove(confetti));
            animation.play();
        }
    }

    /**
     * Efeito de brilho pulsante
     */
    public static void pulseGlow(Node node) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.GOLD);
        glow.setRadius(10);
        glow.setSpread(0.5);

        node.setEffect(glow);

        Timeline pulse = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(glow.radiusProperty(), 10),
                        new KeyValue(glow.spreadProperty(), 0.5)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(glow.radiusProperty(), 30),
                        new KeyValue(glow.spreadProperty(), 0.8)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(glow.radiusProperty(), 10),
                        new KeyValue(glow.spreadProperty(), 0.5)
                )
        );
        pulse.setCycleCount(3);
        pulse.setOnFinished(e -> node.setEffect(null));
        pulse.play();
    }

    /**
     * Efeito de check animado (✓)
     */
    public static void animatedCheck(Pane parent, double x, double y) {
        Label check = new Label("✓");
        check.setStyle(
                "-fx-font-size: 48px; " +
                        "-fx-text-fill: #10B981; " +
                        "-fx-font-weight: bold; " +
                        "-fx-effect: dropshadow(gaussian, rgba(16,185,129,0.5), 10, 0, 0, 5);"
        );
        check.setMouseTransparent(true);
        check.setLayoutX(x - 24);
        check.setLayoutY(y - 32);

        parent.getChildren().add(check);

        // Animação de escala e fade
        check.setScaleX(0);
        check.setScaleY(0);
        check.setOpacity(0);

        Timeline animation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(check.scaleXProperty(), 0, Interpolator.EASE_OUT),
                        new KeyValue(check.scaleYProperty(), 0, Interpolator.EASE_OUT),
                        new KeyValue(check.opacityProperty(), 0, Interpolator.EASE_OUT)
                ),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(check.scaleXProperty(), 1.5, Interpolator.EASE_OUT),
                        new KeyValue(check.scaleYProperty(), 1.5, Interpolator.EASE_OUT),
                        new KeyValue(check.opacityProperty(), 1, Interpolator.EASE_OUT)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(check.scaleXProperty(), 1.0, Interpolator.EASE_OUT),
                        new KeyValue(check.scaleYProperty(), 1.0, Interpolator.EASE_OUT)
                )
        );
        animation.setOnFinished(e -> {
            PauseTransition delay = new PauseTransition(Duration.millis(1000));
            delay.setOnFinished(ev -> {
                FadeTransition fade = new FadeTransition(Duration.millis(300), check);
                fade.setFromValue(1);
                fade.setToValue(0);
                fade.setOnFinished(ev2 -> parent.getChildren().remove(check));
                fade.play();
            });
            delay.play();
        });
        animation.play();
    }

    /**
     * Efeito de raios a partir de um ponto
     */
    public static void rays(Pane parent, double x, double y) {
        for (int i = 0; i < 12; i++) {
            double angle = i * 30;
            double length = 30 + random.nextDouble() * 20;

            javafx.scene.shape.Line ray = new javafx.scene.shape.Line(x, y, x, y);
            ray.setStroke(Color.GOLD);
            ray.setStrokeWidth(2);
            ray.setOpacity(0.8);
            ray.setMouseTransparent(true);

            parent.getChildren().add(ray);

            double endX = x + Math.cos(Math.toRadians(angle)) * length;
            double endY = y + Math.sin(Math.toRadians(angle)) * length;

            Timeline animation = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(ray.endXProperty(), x),
                            new KeyValue(ray.endYProperty(), y),
                            new KeyValue(ray.opacityProperty(), 0.8)
                    ),
                    new KeyFrame(Duration.millis(400),
                            new KeyValue(ray.endXProperty(), endX, Interpolator.EASE_OUT),
                            new KeyValue(ray.endYProperty(), endY, Interpolator.EASE_OUT)
                    ),
                    new KeyFrame(Duration.millis(800),
                            new KeyValue(ray.opacityProperty(), 0, Interpolator.EASE_IN)
                    )
            );
            animation.setOnFinished(e -> parent.getChildren().remove(ray));
            animation.play();
        }
    }
}
