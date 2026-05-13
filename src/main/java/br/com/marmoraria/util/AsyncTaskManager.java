package br.com.marmoraria.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Gerenciador de tarefas assíncronas com feedback de UI.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class AsyncTaskManager {

    private static final Logger LOGGER = Logger.getLogger(AsyncTaskManager.class.getName());

    private static final ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    /**
     * Executa uma tarefa em background com callback de sucesso
     */
    public static <T> void runAsync(Supplier<T> task, Consumer<T> onSuccess) {
        runAsync(task, onSuccess, null, null);
    }

    /**
     * Executa com callback de sucesso e erro
     */
    public static <T> void runAsync(Supplier<T> task,
                                    Consumer<T> onSuccess,
                                    Consumer<Throwable> onError) {
        runAsync(task, onSuccess, onError, null);
    }

    /**
     * Executa com todos os callbacks
     */
    public static <T> void runAsync(Supplier<T> task,
                                    Consumer<T> onSuccess,
                                    Consumer<Throwable> onError,
                                    Runnable onFinally) {
        CompletableFuture.supplyAsync(task, executor)
                .thenAccept(result -> {
                    if (onSuccess != null) {
                        Platform.runLater(() -> onSuccess.accept(result));
                    }
                })
                .exceptionally(throwable -> {
                    LOGGER.severe("Erro em tarefa assíncrona: " + throwable.getMessage());
                    if (onError != null) {
                        Platform.runLater(() -> onError.accept(throwable));
                    } else {
                        Platform.runLater(() -> mostrarErroPadrao(throwable));
                    }
                    return null;
                })
                .thenRun(() -> {
                    if (onFinally != null) {
                        Platform.runLater(onFinally);
                    }
                });
    }

    /**
     * Executa uma tarefa simples sem retorno
     */
    public static void runAsync(Runnable task) {
        CompletableFuture.runAsync(task, executor)
                .exceptionally(throwable -> {
                    LOGGER.severe("Erro: " + throwable.getMessage());
                    Platform.runLater(() -> mostrarErroPadrao(throwable));
                    return null;
                });
    }

    /**
     * Executa com delay
     */
    public static void runDelayed(Runnable task, long delayMs) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            Platform.runLater(task);
            scheduler.shutdown();
        }, delayMs, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    /**
     * Executa periodicamente
     */
    public static ScheduledFuture<?> runPeriodic(Runnable task, long intervalMs) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        return scheduler.scheduleAtFixedRate(
                () -> Platform.runLater(task),
                intervalMs,
                intervalMs,
                java.util.concurrent.TimeUnit.MILLISECONDS
        );
    }

    /**
     * Mostra diálogo de erro padrão
     */
    private static void mostrarErroPadrao(Throwable throwable) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Ocorreu um erro inesperado");
        alert.setContentText(throwable.getMessage() != null ?
                throwable.getMessage() : "Erro desconhecido");
        alert.showAndWait();
    }

    /**
     * Encerra o executor
     */
    public static void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        LOGGER.info("AsyncTaskManager encerrado");
    }
}