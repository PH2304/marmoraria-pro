package br.com.marmoraria.util;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UIHelper {

    /**
     * Tipos de notificação Toast
     */
    public enum ToastType {
        SUCCESS, ERROR, INFO, WARNING
    }

    /**
     * Adiciona efeito de shimmer em um nó (para loading)
     */
    public static void addShimmerEffect(Node node) {
        Glow glow = new Glow();
        glow.setLevel(0.5);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0.5)),
                new KeyFrame(Duration.millis(500), new KeyValue(glow.levelProperty(), 1.0)),
                new KeyFrame(Duration.millis(1000), new KeyValue(glow.levelProperty(), 0.5))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);

        node.setEffect(glow);
        timeline.play();
    }

    /**
     * Remove efeito de shimmer
     */
    public static void removeShimmerEffect(Node node) {
        node.setEffect(null);
    }

    /**
     * Valida campo e aplica estilo de erro
     */
    public static boolean validarCampo(TextField campo, String mensagemErro) {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            aplicarEstiloErro(campo);
            mostrarTooltipErro(campo, mensagemErro);
            return false;
        }

        try {
            Double.parseDouble(texto);
            removerEstiloErro(campo);
            return true;
        } catch (NumberFormatException e) {
            aplicarEstiloErro(campo);
            mostrarTooltipErro(campo, "Valor numérico inválido");
            return false;
        }
    }

    /**
     * Valida campo inteiro
     */
    public static boolean validarCampoInteiro(TextField campo, String mensagemErro) {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            aplicarEstiloErro(campo);
            mostrarTooltipErro(campo, mensagemErro);
            return false;
        }

        try {
            Integer.parseInt(texto);
            removerEstiloErro(campo);
            return true;
        } catch (NumberFormatException e) {
            aplicarEstiloErro(campo);
            mostrarTooltipErro(campo, "Valor inteiro inválido");
            return false;
        }
    }

    /**
     * Aplica estilo de erro no campo
     */
    private static void aplicarEstiloErro(TextField campo) {
        campo.getStyleClass().add("error");

        // Efeito de shake
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), campo);
        shake.setByX(5);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.play();
    }

    /**
     * Remove estilo de erro
     */
    private static void removerEstiloErro(TextField campo) {
        campo.getStyleClass().remove("error");
    }

    /**
     * Mostra tooltip com erro
     */
    private static void mostrarTooltipErro(TextField campo, String mensagem) {
        Tooltip tooltip = new Tooltip(mensagem);
        tooltip.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 12px;");
        campo.setTooltip(tooltip);

        // Remove tooltip após 3 segundos
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> campo.setTooltip(null));
        pause.play();
    }

    /**
     * Mostra notificação toast (versão simplificada)
     */
    public static void mostrarToast(String mensagem, Node parent, ToastType tipo) {
        if (parent == null || parent.getScene() == null) {
            // Fallback: mostrar alert simples
            mostrarAlertSimples(mensagem, tipo);
            return;
        }

        Label toast = new Label(mensagem);

        String cor;
        String icone;
        switch (tipo) {
            case SUCCESS:
                cor = "#27ae60";
                icone = "✓ ";
                break;
            case ERROR:
                cor = "#e74c3c";
                icone = "✗ ";
                break;
            case WARNING:
                cor = "#f39c12";
                icone = "⚠ ";
                break;
            default:
                cor = "#3498db";
                icone = "ℹ ";
                break;
        }

        toast.setText(icone + mensagem);
        toast.setStyle(String.format(
                "-fx-background-color: %s; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10 20; " +
                        "-fx-background-radius: 8; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);",
                cor
        ));

        StackPane root = (StackPane) parent.getScene().getRoot();
        toast.setTranslateX(root.getWidth() / 2 - 100);
        toast.setTranslateY(root.getHeight() - 80);
        toast.setOpacity(0);

        root.getChildren().add(toast);

        // Animação fade in/out
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toast);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition wait = new PauseTransition(Duration.seconds(2));
        wait.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), toast);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> root.getChildren().remove(toast));
            fadeOut.play();
        });

        fadeIn.play();
        wait.play();
    }

    /**
     * Alert simples como fallback
     */
    private static void mostrarAlertSimples(String mensagem, ToastType tipo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tipo.toString());
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Mostra diálogo de confirmação
     */
    public static boolean mostrarConfirmacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);

        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    /**
     * Mostra diálogo de erro
     */
    public static void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Mostra diálogo de informação
     */
    public static void mostrarInfo(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Mostra diálogo de aviso
     */
    public static void mostrarAviso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Formata valor monetário
     */
    public static String formatarMoeda(double valor) {
        return String.format("R$ %.2f", valor);
    }

    /**
     * Formata área em m²
     */
    public static String formatarArea(double area) {
        return String.format("%.2f m²", area);
    }

    /**
     * Valida se é um número positivo
     */
    public static boolean isNumeroPositivo(String texto) {
        try {
            double valor = Double.parseDouble(texto);
            return valor > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Extrai double de um TextField com validação
     */
    public static Double getDoubleFromTextField(TextField campo, String nomeCampo) throws NumberFormatException {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            throw new NumberFormatException(nomeCampo + " não pode estar vazio");
        }

        try {
            return Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(nomeCampo + " deve ser um número válido");
        }
    }

    /**
     * Extrai int de um TextField com validação
     */
    public static Integer getIntFromTextField(TextField campo, String nomeCampo) throws NumberFormatException {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            throw new NumberFormatException(nomeCampo + " não pode estar vazio");
        }

        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(nomeCampo + " deve ser um número inteiro válido");
        }
    }
}