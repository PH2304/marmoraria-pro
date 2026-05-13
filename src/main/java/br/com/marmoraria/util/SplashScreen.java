package br.com.marmoraria.util;

import javafx.animation.*;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Splash screen profissional com animação de carregamento.
 * Compatível com Java 11+.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class SplashScreen extends Preloader {

    private Stage splashStage;
    private ProgressBar progressBar;
    private Label statusLabel;
    private Label titleLabel;

    @Override
    public void start(Stage stage) {
        this.splashStage = stage;
        stage.initStyle(StageStyle.TRANSPARENT);

        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setStyle(
                "-fx-background-color: linear-gradient(135deg, #1e3c72 0%, #2a5298 50%, #667eea 100%); " +
                        "-fx-background-radius: 20px; " +
                        "-fx-padding: 60px 80px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 10);"
        );

        // Ícone com animação de pulso
        Label iconLabel = new Label("🪨");
        iconLabel.setStyle("-fx-font-size: 64px;");

        ScaleTransition pulse = new ScaleTransition(Duration.millis(1500), iconLabel);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.1);
        pulse.setToY(1.1);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.setInterpolator(Interpolator.EASE_BOTH);
        pulse.play();

        // Título principal
        titleLabel = new Label("MARMORARIA PRO");
        titleLabel.setStyle(
                "-fx-font-size: 32px; " +
                        "-fx-font-weight: 900; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Segoe UI', sans-serif; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 3);"
        );

        // Subtítulo
        Label subtitleLabel = new Label("Sistema Profissional de Orçamentos");
        subtitleLabel.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-text-fill: rgba(255,255,255,0.8); " +
                        "-fx-font-family: 'Segoe UI', sans-serif;"
        );

        // Barra de progresso estilizada
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);
        progressBar.setPrefHeight(4);
        progressBar.setStyle(
                "-fx-accent: white; " +
                        "-fx-background-color: rgba(255,255,255,0.2); " +
                        "-fx-background-radius: 4px;"
        );

        // Status de carregamento
        statusLabel = new Label("Inicializando sistema...");
        statusLabel.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-text-fill: rgba(255,255,255,0.7); " +
                        "-fx-font-family: 'Segoe UI', sans-serif;"
        );

        // Versão
        Label versionLabel = new Label("v3.0.0");
        versionLabel.setStyle(
                "-fx-font-size: 10px; " +
                        "-fx-text-fill: rgba(255,255,255,0.5);"
        );

        // Loading dots animados
        Label dotsLabel = new Label("");
        dotsLabel.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-text-fill: rgba(255,255,255,0.6);"
        );

        // Animação dos pontinhos
        Timeline dotsAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, e -> dotsLabel.setText("")),
                new KeyFrame(Duration.millis(500), e -> dotsLabel.setText(".")),
                new KeyFrame(Duration.millis(1000), e -> dotsLabel.setText("..")),
                new KeyFrame(Duration.millis(1500), e -> dotsLabel.setText("..."))
        );
        dotsAnimation.setCycleCount(Timeline.INDEFINITE);
        dotsAnimation.play();

        root.getChildren().addAll(
                iconLabel,
                titleLabel,
                subtitleLabel,
                progressBar,
                statusLabel,
                dotsLabel,
                versionLabel
        );

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.show();

        // Simular progresso
        simularProgresso();
    }

    /**
     * Simula o progresso de carregamento
     */
    private void simularProgresso() {
        final double[] progress = {0.0};
        final String[] stages = {
                "Carregando módulos...",
                "Inicializando banco de dados...",
                "Carregando catálogo de materiais...",
                "Configurando interface...",
                "Preparando dashboard...",
                "Sistema pronto!"
        };
        final int[] stageIndex = {0};

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    progress[0] += 0.008;
                    progressBar.setProgress(Math.min(progress[0], 1.0));

                    // Atualizar mensagem baseado no progresso
                    int newIndex = (int) (progress[0] * stages.length);
                    if (newIndex > stageIndex[0] && newIndex < stages.length) {
                        stageIndex[0] = newIndex;
                        statusLabel.setText(stages[newIndex]);
                    }
                })
        );
        timeline.setCycleCount(125);
        timeline.setOnFinished(e -> {
            progressBar.setProgress(1.0);
            statusLabel.setText("Sistema pronto!");
        });
        timeline.play();
    }

    @Override
    public void handleProgressNotification(ProgressNotification info) {
        if (progressBar != null) {
            progressBar.setProgress(info.getProgress());
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            // Animação de saída suave
            FadeTransition fade = new FadeTransition(Duration.millis(500),
                    splashStage.getScene().getRoot());
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(e -> splashStage.hide());
            fade.play();
        }
    }

    /**
     * Define a mensagem de status
     */
    public void setStatus(String text) {
        if (statusLabel != null) {
            statusLabel.setText(text);
        }
    }
}