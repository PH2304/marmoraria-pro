package br.com.marmoraria.util;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Componentes de loading skeleton para melhor UX durante carregamento.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class SkeletonLoader {

    private static final String SKELETON_COLOR = "#E5E7EB";
    private static final String SKELETON_COLOR_DARK = "#334155";
    private static final String SHIMMER_COLOR = "#F3F4F6";
    private static final String SHIMMER_COLOR_DARK = "#475569";

    /**
     * Cria um card skeleton (placeholder de carregamento)
     */
    public static VBox criarCardSkeleton() {
        boolean isDark = TemaManager.isDarkMode();
        String bgColor = isDark ? SKELETON_COLOR_DARK : SKELETON_COLOR;

        VBox card = new VBox(14);
        card.setStyle(
                "-fx-background-color: " + TemaManager.getSurface() + "; " +
                        "-fx-background-radius: 16px; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-radius: 16px; " +
                        "-fx-border-width: 1px; " +
                        "-fx-padding: 24px;"
        );
        card.setPrefWidth(340);
        card.setMinHeight(200);

        // Ícone skeleton
        Rectangle iconSkeleton = criarRetangulo(56, 56, 14, bgColor);

        // Título skeleton
        Rectangle titleSkeleton = criarRetangulo(200, 16, 8, bgColor);

        // Descrição skeleton (2 linhas)
        VBox descSkeletons = new VBox(6);
        Rectangle desc1 = criarRetangulo(280, 10, 5, bgColor);
        Rectangle desc2 = criarRetangulo(200, 10, 5, bgColor);
        descSkeletons.getChildren().addAll(desc1, desc2);

        // Botão skeleton
        Rectangle btnSkeleton = criarRetangulo(100, 14, 7, bgColor);

        card.getChildren().addAll(iconSkeleton, titleSkeleton, descSkeletons, btnSkeleton);

        // Animar shimmer
        animarShimmer(card);

        return card;
    }

    /**
     * Cria um grid de cards skeleton
     */
    public static FlowPane criarGridSkeleton(int quantidade) {
        FlowPane grid = new FlowPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setPadding(new Insets(0));

        for (int i = 0; i < quantidade; i++) {
            VBox cardSkeleton = criarCardSkeleton();

            // Delay na animação para efeito cascata
            PauseTransition delay = new PauseTransition(Duration.millis(i * 100));
            delay.setOnFinished(e -> grid.getChildren().add(cardSkeleton));
            delay.play();
        }

        return grid;
    }

    /**
     * Cria skeleton para tabela
     */
    public static VBox criarTabelaSkeleton(int linhas) {
        boolean isDark = TemaManager.isDarkMode();
        String bgColor = isDark ? SKELETON_COLOR_DARK : SKELETON_COLOR;

        VBox table = new VBox(0);
        table.setStyle(
                "-fx-background-color: " + TemaManager.getSurface() + "; " +
                        "-fx-background-radius: 12px; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-radius: 12px;"
        );
        table.setPadding(new Insets(0));

        // Header skeleton
        HBox header = new HBox(0);
        header.setPadding(new Insets(12, 16, 12, 16));
        header.setStyle("-fx-background-color: " + (isDark ? "#273548" : "#F8FAFC") + "; " +
                "-fx-background-radius: 12px 12px 0 0;");

        for (int i = 0; i < 4; i++) {
            Rectangle col = criarRetangulo(80 + i * 30, 12, 4, bgColor);
            HBox.setMargin(col, new Insets(0, 16, 0, 0));
            header.getChildren().add(col);
        }

        table.getChildren().add(header);

        // Linhas skeleton
        for (int i = 0; i < linhas; i++) {
            HBox row = new HBox(0);
            row.setPadding(new Insets(10, 16, 10, 16));
            row.setStyle("-fx-border-color: " + TemaManager.getBorderLight() + "; " +
                    "-fx-border-width: 0 0 1px 0;");

            for (int j = 0; j < 4; j++) {
                Rectangle cell = criarRetangulo(60 + j * 40, 10, 3, bgColor);
                HBox.setMargin(cell, new Insets(0, 16, 0, 0));
                row.getChildren().add(cell);
            }

            table.getChildren().add(row);
        }

        animarShimmer(table);
        return table;
    }

    /**
     * Cria skeleton para gráfico
     */
    public static VBox criarGraficoSkeleton() {
        boolean isDark = TemaManager.isDarkMode();
        String bgColor = isDark ? SKELETON_COLOR_DARK : SKELETON_COLOR;

        VBox chart = new VBox(10);
        chart.setStyle(
                "-fx-background-color: " + TemaManager.getSurface() + "; " +
                        "-fx-background-radius: 12px; " +
                        "-fx-border-color: " + TemaManager.getBorder() + "; " +
                        "-fx-border-radius: 12px; " +
                        "-fx-padding: 20px;"
        );
        chart.setPrefHeight(300);

        // Título
        Rectangle title = criarRetangulo(180, 16, 8, bgColor);

        // Barras simuladas
        HBox bars = new HBox(20);
        bars.setAlignment(Pos.BOTTOM_CENTER);
        bars.setPadding(new Insets(20, 0, 0, 0));

        for (int i = 0; i < 7; i++) {
            VBox bar = new VBox(6);
            bar.setAlignment(Pos.BOTTOM_CENTER);

            double height = 40 + Math.random() * 160;
            Rectangle barRect = criarRetangulo(30, (int)height, 4, bgColor);

            Rectangle label = criarRetangulo(30, 8, 2, bgColor);

            bar.getChildren().addAll(barRect, label);
            bars.getChildren().add(bar);
        }

        chart.getChildren().addAll(title, bars);
        animarShimmer(chart);

        return chart;
    }

    /**
     * Cria um retângulo arredondado para skeleton
     */
    private static Rectangle criarRetangulo(int width, int height, int radius, String color) {
        Rectangle rect = new Rectangle(width, height);
        rect.setArcWidth(radius * 2);
        rect.setArcHeight(radius * 2);
        rect.setFill(Color.web(color));
        return rect;
    }

    /**
     * Anima o efeito shimmer no skeleton
     */
    private static void animarShimmer(Region node) {
        boolean isDark = TemaManager.isDarkMode();
        String shimmerColor = isDark ? SHIMMER_COLOR_DARK : SHIMMER_COLOR;

        // Criar overlay para shimmer
        Rectangle shimmer = new Rectangle(node.getPrefWidth(), node.getPrefHeight());
        shimmer.setFill(Color.web(shimmerColor));
        shimmer.setOpacity(0);
        shimmer.setMouseTransparent(true);

        if (node instanceof Pane) {
            ((Pane) node).getChildren().add(shimmer);
        }

        Timeline animation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(shimmer.opacityProperty(), 0.0, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.millis(750),
                        new KeyValue(shimmer.opacityProperty(), 0.5, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.millis(1500),
                        new KeyValue(shimmer.opacityProperty(), 0.0, Interpolator.EASE_BOTH)
                )
        );
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }
}