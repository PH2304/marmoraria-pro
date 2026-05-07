package br.com.marmoraria.util;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Utilitário de animações para UI.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class AnimacoesUI {

    /**
     * Animação de entrada com bounce
     */
    public static void entradaBounce(Node node, Duration duracao) {
        node.setScaleX(0);
        node.setScaleY(0);
        node.setOpacity(0);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.scaleXProperty(), 0, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                        new KeyValue(node.scaleYProperty(), 0, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                        new KeyValue(node.opacityProperty(), 0, Interpolator.EASE_IN)
                ),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(node.scaleXProperty(), 1.1, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                        new KeyValue(node.scaleYProperty(), 1.1, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                        new KeyValue(node.opacityProperty(), 1, Interpolator.EASE_OUT)
                ),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(node.scaleXProperty(), 1.0, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)),
                        new KeyValue(node.scaleYProperty(), 1.0, Interpolator.SPLINE(0.25, 0.1, 0.25, 1))
                )
        );

        timeline.play();
    }

    /**
     * Animação de entrada sequencial para lista de nós
     */
    public static void entradaSequencial(Node[] nodes, Duration delayEntre) {
        for (int i = 0; i < nodes.length; i++) {
            final Node node = nodes[i];
            node.setOpacity(0);
            node.setTranslateY(20);

            PauseTransition delay = new PauseTransition(Duration.millis(i * delayEntre.toMillis()));
            delay.setOnFinished(e -> {
                TranslateTransition slide = new TranslateTransition(Duration.millis(300), node);
                slide.setFromY(20);
                slide.setToY(0);
                slide.setInterpolator(Interpolator.EASE_OUT);

                FadeTransition fade = new FadeTransition(Duration.millis(300), node);
                fade.setFromValue(0);
                fade.setToValue(1);

                new ParallelTransition(slide, fade).play();
            });
            delay.play();
        }
    }

    /**
     * Efeito de brilho (shimmer) para loading
     */
    public static Timeline shimmer(Node node) {
        Glow glow = new Glow();
        glow.setLevel(0.3);
        node.setEffect(glow);

        return new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(glow.levelProperty(), 0.3)
                ),
                new KeyFrame(Duration.millis(750),
                        new KeyValue(glow.levelProperty(), 0.8)
                ),
                new KeyFrame(Duration.millis(1500),
                        new KeyValue(glow.levelProperty(), 0.3)
                )
        );
    }

    /**
     * Animação de shake para erro
     */
    public static void shake(Node node) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(40), node);
        shake.setByX(6);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.setInterpolator(Interpolator.EASE_BOTH);
        shake.play();
    }

    /**
     * Animação de notificação toast
     */
    public static void toastNotification(Node node) {
        node.setTranslateY(-50);
        node.setOpacity(0);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), node);
        slideIn.setFromY(-50);
        slideIn.setToY(0);
        slideIn.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), node);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition wait = new PauseTransition(Duration.seconds(2));

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), node);
        slideOut.setToY(-50);
        slideOut.setInterpolator(Interpolator.EASE_IN);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), node);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        new SequentialTransition(
                new ParallelTransition(slideIn, fadeIn),
                wait,
                new ParallelTransition(slideOut, fadeOut)
        ).play();
    }

    /**
     * Efeito hover 3D
     */
    public static void hover3D(Node node) {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setRadius(15);
        shadow.setSpread(0.1);

        node.setOnMouseEntered(e -> {
            shadow.setRadius(25);
            shadow.setColor(Color.rgb(0, 0, 0, 0.3));
            node.setEffect(shadow);

            ScaleTransition scale = new ScaleTransition(Duration.millis(200), node);
            scale.setToX(1.03);
            scale.setToY(1.03);
            scale.play();
        });

        node.setOnMouseExited(e -> {
            shadow.setRadius(10);
            shadow.setColor(Color.rgb(0, 0, 0, 0.1));
            node.setEffect(shadow);

            ScaleTransition scale = new ScaleTransition(Duration.millis(200), node);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
    }
}
