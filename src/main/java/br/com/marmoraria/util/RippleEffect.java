package br.com.marmoraria.util;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Efeito ripple material design para botões e cards.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class RippleEffect {

    private static final Color RIPPLE_COLOR = Color.rgb(255, 255, 255, 0.3);
    private static final Color RIPPLE_COLOR_DARK = Color.rgb(255, 255, 255, 0.1);

    /**
     * Aplica efeito ripple a um nó
     */
    public static void applyTo(Node node) {
        node.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            createRipple(node, event.getX(), event.getY());
        });
    }

    /**
     * Cria o ripple no ponto clicado
     */
    private static void createRipple(Node node, double x, double y) {
        // Calcular o tamanho máximo do ripple
        double width = node.getBoundsInLocal().getWidth();
        double height = node.getBoundsInLocal().getHeight();
        double maxRadius = Math.max(width, height) * 0.8;

        Circle ripple = new Circle(x, y, 0);
        ripple.setFill(TemaManager.isDarkMode() ? RIPPLE_COLOR_DARK : RIPPLE_COLOR);
        ripple.setMouseTransparent(true);
        ripple.setOpacity(1);

        // Adicionar ao pai
        if (node instanceof Pane) {
            ((Pane) node).getChildren().add(ripple);
        } else if (node.getParent() instanceof Pane) {
            // Converter coordenadas para o pai
            javafx.geometry.Point2D pointInParent = node.localToParent(x, y);
            ripple.setCenterX(pointInParent.getX());
            ripple.setCenterY(pointInParent.getY());
            ((Pane) node.getParent()).getChildren().add(ripple);
        } else {
            return;
        }

        // Animação do ripple
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(ripple.radiusProperty(), 0, Interpolator.EASE_OUT),
                        new KeyValue(ripple.opacityProperty(), 1, Interpolator.EASE_OUT)
                ),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(ripple.radiusProperty(), maxRadius, Interpolator.EASE_OUT),
                        new KeyValue(ripple.opacityProperty(), 0, Interpolator.EASE_OUT)
                )
        );

        timeline.setOnFinished(e -> {
            if (ripple.getParent() != null) {
                ((Pane) ripple.getParent()).getChildren().remove(ripple);
            }
        });

        timeline.play();
    }

    /**
     * Aplica ripple a todos os botões em um container
     */
    public static void applyToAllButtons(Pane container) {
        for (Node child : container.getChildren()) {
            if (child instanceof javafx.scene.control.Button) {
                applyTo(child);
            }
            if (child instanceof Pane) {
                applyToAllButtons((Pane) child);
            }
        }
    }
}