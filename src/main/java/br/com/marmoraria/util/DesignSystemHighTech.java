package br.com.marmoraria.util;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * Design System High-Tech para Marmoraria Pro
 * Estilo Wall Street / Bloomberg Terminal
 * Visual escuro premium com efeitos neon e glassmorphism
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class DesignSystemHighTech {

    // ==================== CORES NEON HIGH-TECH ====================

    // Cores Primárias - Tom azul elétrico / ciano
    public static final String CYAN_NEON = "#00E5FF";
    public static final String CYAN_NEON_GLOW = "#00E5FF";
    public static final String BLUE_NEON = "#0088FF";
    public static final String BLUE_DEEP = "#001B3D";
    public static final String BLUE_DARK = "#0A1628";
    public static final String BLUE_MID = "#0F2A4A";

    // Cores de Destaque - Verde / Vermelho estilo trading
    public static final String GREEN_MARKET = "#00FF88";
    public static final String RED_MARKET = "#FF3366";
    public static final String ORANGE_NEON = "#FF6B00";
    public static final String PURPLE_NEON = "#B900FF";

    // Cores Neutras - Escalas de cinza premium
    public static final String GRAY_900 = "#0D1117";
    public static final String GRAY_800 = "#161B22";
    public static final String GRAY_750 = "#1A1F2E";
    public static final String GRAY_700 = "#21262D";
    public static final String GRAY_600 = "#30363D";
    public static final String GRAY_500 = "#484F58";
    public static final String GRAY_400 = "#8B949E";
    public static final String GRAY_300 = "#C9D1D9";
    public static final String GRAY_200 = "#E6EDF3";
    public static final String GRAY_100 = "#F0F6FC";

    // Cores de Status
    public static final String STATUS_SUCCESS = "#00FF88";
    public static final String STATUS_WARNING = "#FFB800";
    public static final String STATUS_ERROR = "#FF3366";
    public static final String STATUS_INFO = "#00E5FF";

    // ==================== GRADIENTES HIGH-TECH ====================

    public static final String GRADIENT_PRIMARY =
            "linear-gradient(135deg, #0A1628 0%, #001B3D 50%, #003366 100%)";

    public static final String GRADIENT_NEON_BLUE =
            "linear-gradient(135deg, #0088FF 0%, #00E5FF 100%)";

    public static final String GRADIENT_NEON_GREEN =
            "linear-gradient(135deg, #00FF88 0%, #00B8FF 100%)";

    public static final String GRADIENT_NEON_PURPLE =
            "linear-gradient(135deg, #7C00FF 0%, #B900FF 100%)";

    public static final String GRADIENT_GLASS =
            "linear-gradient(135deg, rgba(255,255,255,0.05) 0%, rgba(255,255,255,0.02) 100%)";

    public static final String GRADIENT_CARD =
            "linear-gradient(135deg, rgba(22,27,34,0.95) 0%, rgba(13,17,23,0.98) 100%)";

    // ==================== EFEITOS GLASSMORPHISM ====================

    public static final String GLASS_EFFECT =
            "-fx-background-color: rgba(22, 27, 34, 0.7); " +
                    "-fx-backdrop-filter: blur(10px); " +
                    "-fx-background-radius: 12px; " +
                    "-fx-border-color: rgba(0, 229, 255, 0.2); " +
                    "-fx-border-radius: 12px; " +
                    "-fx-border-width: 1px;";

    public static final String GLASS_EFFECT_STRONG =
            "-fx-background-color: rgba(13, 17, 23, 0.85); " +
                    "-fx-backdrop-filter: blur(15px); " +
                    "-fx-background-radius: 16px; " +
                    "-fx-border-color: rgba(0, 229, 255, 0.3); " +
                    "-fx-border-radius: 16px; " +
                    "-fx-border-width: 1.5px;";

    // ==================== EFEITOS NEON ====================

    public static DropShadow getNeonGlow(String color) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web(color));
        glow.setRadius(15);
        glow.setSpread(0.5);
        return glow;
    }

    public static DropShadow getNeonGlowIntense(String color) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web(color));
        glow.setRadius(25);
        glow.setSpread(0.7);
        return glow;
    }

    public static Glow getGlowEffect(double level) {
        Glow glow = new Glow();
        glow.setLevel(level);
        return glow;
    }

    public static Bloom getBloomEffect() {
        Bloom bloom = new Bloom();
        bloom.setThreshold(0.3);
        return bloom;
    }

    // ==================== TIPOGRAFIA PREMIUM ====================

    public static final String FONT_PRIMARY = "'Inter', 'Segoe UI', -apple-system, BlinkMacSystemFont, sans-serif";
    public static final String FONT_MONO = "'JetBrains Mono', 'Courier New', monospace";

    // ==================== TODOS OS MÉTODOS DE ESTILO ====================

    public static String getStyleH1() {
        return String.format("-fx-font-family: %s; -fx-font-size: 32px; -fx-font-weight: 800; " +
                "-fx-text-fill: %s; -fx-letter-spacing: -0.5px;", FONT_PRIMARY, GRAY_100);
    }

    public static String getStyleH2() {
        return String.format("-fx-font-family: %s; -fx-font-size: 24px; -fx-font-weight: 700; " +
                "-fx-text-fill: %s; -fx-letter-spacing: -0.3px;", FONT_PRIMARY, GRAY_200);
    }

    public static String getStyleH3() {
        return String.format("-fx-font-family: %s; -fx-font-size: 18px; -fx-font-weight: 600; " +
                "-fx-text-fill: %s;", FONT_PRIMARY, GRAY_300);
    }

    // CORRIGIDO: Adicionado método getStyleH4()
    public static String getStyleH4() {
        return String.format("-fx-font-family: %s; -fx-font-size: 15px; -fx-font-weight: 600; " +
                "-fx-text-fill: %s;", FONT_PRIMARY, GRAY_300);
    }

    public static String getStyleBody() {
        return String.format("-fx-font-family: %s; -fx-font-size: 13px; -fx-font-weight: 400; " +
                "-fx-text-fill: %s;", FONT_PRIMARY, GRAY_400);
    }

    public static String getStyleBodyBold() {
        return String.format("-fx-font-family: %s; -fx-font-size: 13px; -fx-font-weight: 600; " +
                "-fx-text-fill: %s;", FONT_PRIMARY, GRAY_300);
    }

    public static String getStyleMono() {
        return String.format("-fx-font-family: %s; -fx-font-size: 12px; -fx-font-weight: 500; " +
                "-fx-text-fill: %s;", FONT_MONO, CYAN_NEON);
    }

    public static String getStyleCaption() {
        return String.format("-fx-font-family: %s; -fx-font-size: 10px; -fx-font-weight: 600; " +
                        "-fx-text-fill: %s; -fx-letter-spacing: 0.5px; -fx-uppercase: true;",
                FONT_PRIMARY, GRAY_500);
    }

    public static String getStyleCaptionNeon() {
        return String.format("-fx-font-family: %s; -fx-font-size: 10px; -fx-font-weight: 700; " +
                "-fx-text-fill: %s; -fx-letter-spacing: 1px;", FONT_PRIMARY, CYAN_NEON);
    }

    public static String getStyleSmall() {
        return String.format("-fx-font-family: %s; -fx-font-size: 11px; -fx-font-weight: 400; " +
                "-fx-text-fill: %s;", FONT_PRIMARY, GRAY_500);
    }

    // ==================== ESPAÇAMENTOS ====================

    public static final double SPACE_XXS = 4;
    public static final double SPACE_XS = 8;
    public static final double SPACE_SM = 12;
    public static final double SPACE_MD = 16;
    public static final double SPACE_LG = 24;
    public static final double SPACE_XL = 32;
    public static final double SPACE_XXL = 48;
    public static final double SPACE_XXXL = 64;

    public static final double RADIUS_SM = 6;
    public static final double RADIUS_MD = 10;
    public static final double RADIUS_LG = 14;
    public static final double RADIUS_XL = 20;
    public static final double RADIUS_XXL = 28;
    public static final double RADIUS_FULL = 999;

    // ==================== BACKGROUND PRINCIPAL ====================

    public static String getMainBackground() {
        return "-fx-background-color: " + GRAY_900 + ";";
    }

    public static String getCardBackground() {
        return String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-border-color: %s; -fx-border-radius: %dpx; -fx-border-width: 1px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 8, 0, 0, 2);",
                GRAY_800, (int)RADIUS_LG, GRAY_700, (int)RADIUS_LG
        );
    }

    public static String getNeonCardBackground(String neonColor) {
        return String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-border-color: %s; -fx-border-radius: %dpx; -fx-border-width: 1.5px; " +
                        "-fx-effect: dropshadow(gaussian, %s, 15, 0, 0, 3);",
                GRAY_800, (int)RADIUS_LG, neonColor, (int)RADIUS_LG, neonColor
        );
    }

    // ==================== EFEITOS DE ENTRADA ====================

    public static Timeline createEntryAnimation(javafx.scene.Node node) {
        node.setOpacity(0);
        node.setScaleX(0.95);
        node.setScaleY(0.95);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.opacityProperty(), 0, Interpolator.EASE_OUT),
                        new KeyValue(node.scaleXProperty(), 0.95, Interpolator.EASE_OUT),
                        new KeyValue(node.scaleYProperty(), 0.95, Interpolator.EASE_OUT)
                ),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(node.opacityProperty(), 1, Interpolator.EASE_OUT),
                        new KeyValue(node.scaleXProperty(), 1, Interpolator.EASE_OUT),
                        new KeyValue(node.scaleYProperty(), 1, Interpolator.EASE_OUT)
                )
        );

        return timeline;
    }

    public static SequentialTransition createStaggeredEntry(javafx.scene.Node[] nodes) {
        SequentialTransition sequence = new SequentialTransition();

        for (int i = 0; i < nodes.length; i++) {
            javafx.scene.Node node = nodes[i];
            node.setOpacity(0);
            node.setTranslateY(20);

            FadeTransition fade = new FadeTransition(Duration.millis(300), node);
            fade.setFromValue(0);
            fade.setToValue(1);

            TranslateTransition slide = new TranslateTransition(Duration.millis(400), node);
            slide.setFromY(20);
            slide.setToY(0);
            slide.setInterpolator(Interpolator.EASE_OUT);

            ParallelTransition parallel = new ParallelTransition(fade, slide);
            parallel.setDelay(Duration.millis(i * 80));

            sequence.getChildren().add(parallel);
        }

        return sequence;
    }

    public static Timeline createPulseAnimation(javafx.scene.Node node, String neonColor) {
        Timeline pulse = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.opacityProperty(), 0.7),
                        new KeyValue(node.effectProperty(), getNeonGlow(neonColor))
                ),
                new KeyFrame(Duration.millis(800),
                        new KeyValue(node.opacityProperty(), 1.0),
                        new KeyValue(node.effectProperty(), getNeonGlowIntense(neonColor))
                ),
                new KeyFrame(Duration.millis(1600),
                        new KeyValue(node.opacityProperty(), 0.7),
                        new KeyValue(node.effectProperty(), getNeonGlow(neonColor))
                )
        );
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.setAutoReverse(true);
        return pulse;
    }

    // ==================== MÉTODOS UTILITÁRIOS ====================

    public static String formatValue(double value) {
        if (value >= 1000000) {
            return String.format("R$ %.1fM", value / 1000000);
        } else if (value >= 1000) {
            return String.format("R$ %.1fK", value / 1000);
        }
        return String.format("R$ %.2f", value);
    }

    public static String formatPercentage(double value) {
        String symbol = value >= 0 ? "▲" : "▼";
        String color = value >= 0 ? GREEN_MARKET : RED_MARKET;
        return String.format("%s %.2f%%", symbol, Math.abs(value));
    }
}