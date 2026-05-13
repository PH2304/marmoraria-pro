package br.com.marmoraria.util;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Central de notificações do sistema.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class SistemaNotificacoes {

    public static class Notificacao {
        private final String id;
        private final String titulo;
        private final String mensagem;
        private final Tipo tipo;
        private final LocalDateTime timestamp;
        private boolean lida;

        public enum Tipo {
            INFO("ℹ️", "#3B82F6"),
            SUCESSO("✅", "#10B981"),
            AVISO("⚠️", "#F59E0B"),
            ERRO("❌", "#EF4444"),
            BACKUP("💾", "#8B5CF6"),
            ORCAMENTO("📋", "#EC4899");

            final String icone;
            final String cor;

            Tipo(String icone, String cor) {
                this.icone = icone;
                this.cor = cor;
            }
        }

        public Notificacao(String titulo, String mensagem, Tipo tipo) {
            this.id = UUID.randomUUID().toString();
            this.titulo = titulo;
            this.mensagem = mensagem;
            this.tipo = tipo;
            this.timestamp = LocalDateTime.now();
            this.lida = false;
        }

        public String getId() { return id; }
        public String getTitulo() { return titulo; }
        public String getMensagem() { return mensagem; }
        public Tipo getTipo() { return tipo; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public boolean isLida() { return lida; }
        public void marcarLida() { this.lida = true; }

        public String getTempoAtras() {
            long minutos = java.time.Duration.between(timestamp, LocalDateTime.now()).toMinutes();
            if (minutos < 1) return "Agora";
            if (minutos < 60) return minutos + "min";
            long horas = minutos / 60;
            if (horas < 24) return horas + "h";
            return timestamp.format(DateTimeFormatter.ofPattern("dd/MM"));
        }
    }

    private static final Queue<Notificacao> fila = new ConcurrentLinkedQueue<>();
    private static final List<Notificacao> historico = new ArrayList<>();
    private static final int MAX_HISTORICO = 50;

    private static VBox containerNotificacoes;
    private static Label badgeNotificacoes;

    /**
     * Inicializa o sistema de notificações
     */
    public static void inicializar(VBox container, Label badge) {
        containerNotificacoes = container;
        badgeNotificacoes = badge;

        // Processar fila periodicamente
        Timeline processador = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> processarFila())
        );
        processador.setCycleCount(Timeline.INDEFINITE);
        processador.play();
    }

    /**
     * Adiciona uma notificação
     */
    public static void notificar(String titulo, String mensagem, Notificacao.Tipo tipo) {
        Notificacao notif = new Notificacao(titulo, mensagem, tipo);
        fila.offer(notif);

        // Adicionar ao histórico
        historico.add(0, notif);
        if (historico.size() > MAX_HISTORICO) {
            historico.remove(historico.size() - 1);
        }
    }

    /**
     * Notificação de sucesso
     */
    public static void sucesso(String titulo, String mensagem) {
        notificar(titulo, mensagem, Notificacao.Tipo.SUCESSO);
    }

    /**
     * Notificação de erro
     */
    public static void erro(String titulo, String mensagem) {
        notificar(titulo, mensagem, Notificacao.Tipo.ERRO);
    }

    /**
     * Notificação de aviso
     */
    public static void aviso(String titulo, String mensagem) {
        notificar(titulo, mensagem, Notificacao.Tipo.AVISO);
    }

    /**
     * Notificação de info
     */
    public static void info(String titulo, String mensagem) {
        notificar(titulo, mensagem, Notificacao.Tipo.INFO);
    }

    /**
     * Processa a fila de notificações
     */
    private static void processarFila() {
        if (fila.isEmpty() || containerNotificacoes == null) return;

        Platform.runLater(() -> {
            Notificacao notif = fila.poll();
            if (notif != null) {
                mostrarNotificacao(notif);
            }
            atualizarBadge();
        });
    }

    /**
     * Mostra uma notificação na interface
     */
    private static void mostrarNotificacao(Notificacao notif) {
        HBox toast = new HBox(10);
        toast.setAlignment(Pos.CENTER_LEFT);
        toast.setPadding(new Insets(12, 16, 12, 16));
        toast.setMaxWidth(380);
        toast.setStyle(
                "-fx-background-color: " + TemaManager.getSurface() + "; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-border-color: " + notif.getTipo().cor + "40; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-border-width: 1px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);"
        );

        // Ícone
        Label iconLabel = new Label(notif.getTipo().icone);
        iconLabel.setStyle("-fx-font-size: 16px;");

        // Conteúdo
        VBox conteudo = new VBox(2);

        Label tituloLabel = new Label(notif.getTitulo());
        tituloLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: " +
                TemaManager.getTextPrimary() + ";");

        Label msgLabel = new Label(notif.getMensagem());
        msgLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " +
                TemaManager.getTextSecondary() + ";");

        conteudo.getChildren().addAll(tituloLabel, msgLabel);

        // Botão fechar
        Button closeBtn = new Button("✕");
        closeBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " +
                        TemaManager.getTextTertiary() + "; -fx-cursor: hand; -fx-font-size: 12px;"
        );

        toast.getChildren().addAll(iconLabel, conteudo, closeBtn);

        // Adicionar ao container
        containerNotificacoes.getChildren().add(0, toast);

        // Animar entrada
        toast.setTranslateX(400);
        toast.setOpacity(0);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), toast);
        slideIn.setFromX(400);
        slideIn.setToX(0);
        slideIn.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toast);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ParallelTransition entrada = new ParallelTransition(slideIn, fadeIn);
        entrada.play();

        // Auto-remover após 5 segundos
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), toast);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> containerNotificacoes.getChildren().remove(toast));
            fadeOut.play();
        });
        delay.play();

        // Fechar ao clicar
        closeBtn.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), toast);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> containerNotificacoes.getChildren().remove(toast));
            fadeOut.play();
        });
    }

    /**
     * Atualiza o badge de notificações não lidas
     */
    private static void atualizarBadge() {
        if (badgeNotificacoes == null) return;

        long naoLidas = historico.stream().filter(n -> !n.isLida()).count();

        if (naoLidas > 0) {
            badgeNotificacoes.setText(String.valueOf(naoLidas));
            badgeNotificacoes.setVisible(true);
        } else {
            badgeNotificacoes.setVisible(false);
        }
    }

    /**
     * Retorna o histórico de notificações
     */
    public static List<Notificacao> getHistorico() {
        return new ArrayList<>(historico);
    }

    /**
     * Marca todas como lidas
     */
    public static void marcarTodasLidas() {
        historico.forEach(Notificacao::marcarLida);
        atualizarBadge();
    }

    /**
     * Limpa o histórico
     */
    public static void limparHistorico() {
        historico.clear();
        atualizarBadge();
    }
}