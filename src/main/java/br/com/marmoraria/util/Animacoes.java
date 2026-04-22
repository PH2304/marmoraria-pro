package br.com.marmoraria.util;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class Animacoes {

    /**
     * Aplica animação de fade in
     */
    public static void fadeIn(Node node, Duration duracao) {
        node.setOpacity(0);
        FadeTransition fade = new FadeTransition(duracao, node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    /**
     * Aplica animação de fade out
     */
    public static void fadeOut(Node node, Duration duracao, Runnable callback) {
        FadeTransition fade = new FadeTransition(duracao, node);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> {
            if (callback != null) callback.run();
        });
        fade.play();
    }

    /**
     * Aplica animação de slide in (entrada lateral)
     */
    public static void slideIn(Node node, Direction direcao, Duration duracao) {
        double startX = 0, startY = 0;
        switch (direcao) {
            case LEFT: startX = -node.getBoundsInParent().getWidth(); break;
            case RIGHT: startX = node.getBoundsInParent().getWidth(); break;
            case TOP: startY = -node.getBoundsInParent().getHeight(); break;
            case BOTTOM: startY = node.getBoundsInParent().getHeight(); break;
        }

        node.setTranslateX(startX);
        node.setTranslateY(startY);

        TranslateTransition slide = new TranslateTransition(duracao, node);
        slide.setToX(0);
        slide.setToY(0);
        slide.play();
    }

    /**
     * Aplica efeito de pulsação em um botão
     */
    public static void pulsar(Node node) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), node);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(1.05);
        scale.setToY(1.05);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }

    /**
     * Aplica efeito de shake (para erros)
     */
    public static void shake(Node node) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), node);
        shake.setByX(8);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }

    public enum Direction {
        LEFT, RIGHT, TOP, BOTTOM
    }
}