package br.com.marmoraria.util;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Sistema de carregamento lazy para views pesadas.
 * Carrega a view em background thread e mostra skeleton enquanto isso.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class LazyViewLoader {

    private static final Logger LOGGER = Logger.getLogger(LazyViewLoader.class.getName());
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    /**
     * Carrega uma view de forma assíncrona com skeleton de loading
     */
    public static <T extends Node> void loadAsync(StackPane container, Supplier<T> viewSupplier) {
        loadAsync(container, viewSupplier, null);
    }

    /**
     * Carrega uma view de forma assíncrona com callback
     */
    public static <T extends Node> void loadAsync(StackPane container,
                                                  Supplier<T> viewSupplier,
                                                  LazyLoadCallback<T> callback) {
        // Mostrar skeleton
        Node skeleton = criarSkeletonLoading();
        Platform.runLater(() -> {
            container.getChildren().clear();
            container.getChildren().add(skeleton);
        });

        // Carregar view em background
        CompletableFuture.supplyAsync(() -> {
            LOGGER.fine("Carregando view em background...");
            long start = System.currentTimeMillis();
            T view = viewSupplier.get();
            long time = System.currentTimeMillis() - start;
            LOGGER.fine("View carregada em " + time + "ms");
            return view;
        }, executor).thenAccept(view -> {
            Platform.runLater(() -> {
                // Animação de transição
                skeleton.setOpacity(1);

                javafx.animation.FadeTransition fadeOut =
                        new javafx.animation.FadeTransition(javafx.util.Duration.millis(200), skeleton);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(e -> {
                    container.getChildren().clear();
                    container.getChildren().add(view);

                    // Fade in da view
                    view.setOpacity(0);
                    javafx.animation.FadeTransition fadeIn =
                            new javafx.animation.FadeTransition(javafx.util.Duration.millis(300), view);
                    fadeIn.setFromValue(0);
                    fadeIn.setToValue(1);
                    fadeIn.play();

                    if (callback != null) {
                        callback.onLoaded(view);
                    }
                });
                fadeOut.play();
            });
        }).exceptionally(throwable -> {
            Platform.runLater(() -> {
                container.getChildren().clear();
                Label errorLabel = new Label("Erro ao carregar: " + throwable.getMessage());
                errorLabel.setStyle("-fx-text-fill: #EF4444; -fx-font-size: 14px;");
                container.getChildren().add(errorLabel);
                LOGGER.severe("Erro ao carregar view: " + throwable.getMessage());
            });
            return null;
        });
    }

    /**
     * Cria um skeleton de loading animado
     */
    private static Node criarSkeletonLoading() {
        VBox skeleton = new VBox(20);
        skeleton.setAlignment(javafx.geometry.Pos.CENTER);
        skeleton.setPadding(new javafx.geometry.Insets(40));
        skeleton.setStyle(
                "-fx-background-color: " + TemaManager.getSurface() + "; " +
                        "-fx-background-radius: 16px;"
        );

        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setStyle("-fx-progress-color: " + TemaManager.getPrimary() + ";");
        spinner.setMaxSize(40, 40);

        Label loadingLabel = new Label("Carregando...");
        loadingLabel.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-text-fill: " + TemaManager.getTextSecondary() + ";"
        );

        skeleton.getChildren().addAll(spinner, loadingLabel);
        return skeleton;
    }

    /**
     * Interface de callback para quando a view é carregada
     */
    public interface LazyLoadCallback<T> {
        void onLoaded(T view);
    }

    /**
     * Encerra o executor service
     */
    public static void shutdown() {
        executor.shutdown();
        LOGGER.info("LazyViewLoader encerrado");
    }
}