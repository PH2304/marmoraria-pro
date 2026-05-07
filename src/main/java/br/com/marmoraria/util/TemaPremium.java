package br.com.marmoraria.util;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.effect.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Sistema de design premium com glassmorphism, sombras avançadas e animações.
 * Compatível com Java 11+.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class TemaPremium {

    // ==================== PALETA DE CORES PREMIUM ====================

    public static final String AZUL_50 = "#EFF6FF";
    public static final String AZUL_100 = "#DBEAFE";
    public static final String AZUL_200 = "#BFDBFE";
    public static final String AZUL_300 = "#93C5FD";
    public static final String AZUL_400 = "#60A5FA";
    public static final String AZUL_500 = "#3B82F6";
    public static final String AZUL_600 = "#2563EB";
    public static final String AZUL_700 = "#1D4ED8";
    public static final String AZUL_800 = "#1E40AF";
    public static final String AZUL_900 = "#1E3A8A";

    public static final String SLATE_50 = "#F8FAFC";
    public static final String SLATE_100 = "#F1F5F9";
    public static final String SLATE_200 = "#E2E8F0";
    public static final String SLATE_300 = "#CBD5E1";
    public static final String SLATE_400 = "#94A3B8";
    public static final String SLATE_500 = "#64748B";
    public static final String SLATE_600 = "#475569";
    public static final String SLATE_700 = "#334155";
    public static final String SLATE_800 = "#1E293B";
    public static final String SLATE_900 = "#0F172A";

    public static final String ESMERALDA_500 = "#10B981";
    public static final String ESMERALDA_600 = "#059669";

    public static final String AMBAR_500 = "#F59E0B";
    public static final String AMBAR_600 = "#D97706";

    public static final String VERMELHO_500 = "#EF4444";
    public static final String VERMELHO_600 = "#DC2626";

    public static final String VIOLETA_500 = "#8B5CF6";
    public static final String VIOLETA_600 = "#7C3AED";

    public static final String CIANO_500 = "#06B6D4";
    public static final String CIANO_600 = "#0891B2";

    // ==================== GRADIENTES ====================

    public static final String GRADIENT_AZURE =
            "linear-gradient(135deg, #667eea 0%, #764ba2 100%)";

    public static final String GRADIENT_OCEAN =
            "linear-gradient(135deg, #2193b0 0%, #6dd5ed 100%)";

    public static final String GRADIENT_SUNSET =
            "linear-gradient(135deg, #f093fb 0%, #f5576c 100%)";

    public static final String GRADIENT_EMERALD =
            "linear-gradient(135deg, #11998e 0%, #38ef7d 100%)";

    public static final String GRADIENT_ROYAL =
            "linear-gradient(135deg, #1e3c72 0%, #2a5298 100%)";

    public static final String GRADIENT_PREMIUM =
            "linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%)";

    // ==================== EFEITOS GLASSMORPHISM ====================

    public static final String ESTILO_GLASS =
            "-fx-background-color: rgba(255,255,255,0.8); " +
                    "-fx-background-radius: 16px; " +
                    "-fx-border-color: rgba(255,255,255,0.3); " +
                    "-fx-border-radius: 16px; " +
                    "-fx-border-width: 1px; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 10);";

    public static final String ESTILO_GLASS_DARK =
            "-fx-background-color: rgba(255,255,255,0.15); " +
                    "-fx-background-radius: 16px; " +
                    "-fx-border-color: rgba(255,255,255,0.1); " +
                    "-fx-border-radius: 16px; " +
                    "-fx-border-width: 1px; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 10);";

    /**
     * Gera CSS completo para o tema premium
     */
    public static String gerarCSS() {
        StringBuilder css = new StringBuilder();

        css.append(".root {\n");
        css.append("    -fx-font-family: 'Segoe UI', sans-serif;\n");
        css.append("}\n\n");

        css.append(".scroll-bar:vertical {\n");
        css.append("    -fx-background-color: transparent;\n");
        css.append("    -fx-pref-width: 8px;\n");
        css.append("}\n\n");

        css.append(".scroll-bar:vertical .thumb {\n");
        css.append("    -fx-background-color: #CBD5E1;\n");
        css.append("    -fx-background-radius: 4px;\n");
        css.append("}\n\n");

        css.append(".tooltip {\n");
        css.append("    -fx-background-color: #1E293B;\n");
        css.append("    -fx-text-fill: white;\n");
        css.append("    -fx-font-size: 12px;\n");
        css.append("    -fx-padding: 10px 14px;\n");
        css.append("    -fx-background-radius: 8px;\n");
        css.append("}\n\n");

        css.append(".table-view {\n");
        css.append("    -fx-background-color: white;\n");
        css.append("    -fx-background-radius: 12px;\n");
        css.append("    -fx-border-color: #E2E8F0;\n");
        css.append("    -fx-border-radius: 12px;\n");
        css.append("}\n\n");

        css.append(".table-row-cell:hover {\n");
        css.append("    -fx-background-color: #F8FAFC;\n");
        css.append("}\n\n");

        css.append(".table-row-cell:selected {\n");
        css.append("    -fx-background-color: #EFF6FF;\n");
        css.append("}\n\n");

        css.append(".button-premium {\n");
        css.append("    -fx-background-color: #2563EB;\n");
        css.append("    -fx-text-fill: white;\n");
        css.append("    -fx-font-weight: bold;\n");
        css.append("    -fx-font-size: 13px;\n");
        css.append("    -fx-background-radius: 10px;\n");
        css.append("    -fx-padding: 12px 24px;\n");
        css.append("    -fx-cursor: hand;\n");
        css.append("}\n\n");

        css.append(".text-field-premium {\n");
        css.append("    -fx-background-color: white;\n");
        css.append("    -fx-background-radius: 10px;\n");
        css.append("    -fx-border-color: #CBD5E1;\n");
        css.append("    -fx-border-radius: 10px;\n");
        css.append("    -fx-padding: 12px 16px;\n");
        css.append("    -fx-font-size: 13px;\n");
        css.append("}\n\n");

        css.append(".card-premium {\n");
        css.append("    -fx-background-color: white;\n");
        css.append("    -fx-background-radius: 16px;\n");
        css.append("    -fx-border-color: #E2E8F0;\n");
        css.append("    -fx-border-radius: 16px;\n");
        css.append("    -fx-padding: 24px;\n");
        css.append("}\n\n");

        css.append(".badge-premium {\n");
        css.append("    -fx-padding: 4px 12px;\n");
        css.append("    -fx-background-radius: 20px;\n");
        css.append("    -fx-font-weight: bold;\n");
        css.append("    -fx-font-size: 10px;\n");
        css.append("}\n");

        return css.toString();
    }

    // ==================== ANIMAÇÕES ====================

    public static void animarEntradaSuave(Node node, double delaySegundos) {
        node.setOpacity(0);
        node.setScaleX(0.95);
        node.setScaleY(0.95);

        PauseTransition delay = new PauseTransition(Duration.seconds(delaySegundos));
        delay.setOnFinished(e -> {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(node.opacityProperty(), 0, Interpolator.EASE_OUT),
                            new KeyValue(node.scaleXProperty(), 0.95, Interpolator.EASE_OUT),
                            new KeyValue(node.scaleYProperty(), 0.95, Interpolator.EASE_OUT)
                    ),
                    new KeyFrame(Duration.millis(400),
                            new KeyValue(node.opacityProperty(), 1, Interpolator.EASE_OUT),
                            new KeyValue(node.scaleXProperty(), 1.02, Interpolator.EASE_OUT),
                            new KeyValue(node.scaleYProperty(), 1.02, Interpolator.EASE_OUT)
                    ),
                    new KeyFrame(Duration.millis(600),
                            new KeyValue(node.scaleXProperty(), 1.0, Interpolator.EASE_OUT),
                            new KeyValue(node.scaleYProperty(), 1.0, Interpolator.EASE_OUT)
                    )
            );
            timeline.play();
        });
        delay.play();
    }

    public static void animarPulso(Node node) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(800), node);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.03);
        scale.setToY(1.03);
        scale.setAutoReverse(true);
        scale.setCycleCount(Timeline.INDEFINITE);
        scale.setInterpolator(Interpolator.EASE_BOTH);
        scale.play();
    }

    public static void aplicarHover3D(Node node) {
        DropShadow shadowNormal = new DropShadow();
        shadowNormal.setColor(Color.rgb(0, 0, 0, 0.08));
        shadowNormal.setRadius(8);
        shadowNormal.setSpread(0.05);

        DropShadow shadowHover = new DropShadow();
        shadowHover.setColor(Color.rgb(0, 0, 0, 0.15));
        shadowHover.setRadius(20);
        shadowHover.setSpread(0.1);

        node.setEffect(shadowNormal);

        node.setOnMouseEntered(e -> {
            node.setEffect(shadowHover);
            ScaleTransition st = new ScaleTransition(Duration.millis(200), node);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
            TranslateTransition tt = new TranslateTransition(Duration.millis(200), node);
            tt.setToY(-4);
            tt.play();
        });

        node.setOnMouseExited(e -> {
            node.setEffect(shadowNormal);
            ScaleTransition st = new ScaleTransition(Duration.millis(200), node);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            TranslateTransition tt = new TranslateTransition(Duration.millis(200), node);
            tt.setToY(0);
            tt.play();
        });
    }

    public static Timeline criarShimmer(Node node) {
        node.setOpacity(0.6);
        Timeline shimmer = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.opacityProperty(), 0.6, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.millis(750),
                        new KeyValue(node.opacityProperty(), 1.0, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.millis(1500),
                        new KeyValue(node.opacityProperty(), 0.6, Interpolator.EASE_BOTH)
                )
        );
        shimmer.setCycleCount(Timeline.INDEFINITE);
        return shimmer;
    }

    public static void animarFadeInSequencial(Node[] nodes, double intervalo) {
        for (int i = 0; i < nodes.length; i++) {
            final Node node = nodes[i];
            node.setOpacity(0);
            node.setTranslateY(20);

            final double delay = i * intervalo;
            PauseTransition pause = new PauseTransition(Duration.seconds(delay));
            pause.setOnFinished(e -> {
                FadeTransition fade = new FadeTransition(Duration.millis(400), node);
                fade.setFromValue(0);
                fade.setToValue(1);

                TranslateTransition slide = new TranslateTransition(Duration.millis(400), node);
                slide.setFromY(20);
                slide.setToY(0);

                new ParallelTransition(fade, slide).play();
            });
            pause.play();
        }
    }
}
