package br.com.marmoraria.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Sistema de Design Unificado para Marmoraria Pro
 * Centraliza cores, tipografia, espaçamentos e efeitos visuais
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class DesignSystem {

    // ==================== CORES PREMIUM ====================

    // Cores Primárias
    public static final String PRIMARY_50 = "#EFF6FF";
    public static final String PRIMARY_100 = "#DBEAFE";
    public static final String PRIMARY_200 = "#BFDBFE";
    public static final String PRIMARY_300 = "#93C5FD";
    public static final String PRIMARY_400 = "#60A5FA";
    public static final String PRIMARY_500 = "#3B82F6";
    public static final String PRIMARY_600 = "#2563EB";
    public static final String PRIMARY_700 = "#1D4ED8";
    public static final String PRIMARY_800 = "#1E40AF";
    public static final String PRIMARY_900 = "#1E3A8A";

    // Cores Neutras
    public static final String NEUTRAL_50 = "#F9FAFB";
    public static final String NEUTRAL_100 = "#F3F4F6";
    public static final String NEUTRAL_200 = "#E5E7EB";
    public static final String NEUTRAL_300 = "#D1D5DB";
    public static final String NEUTRAL_400 = "#9CA3AF";
    public static final String NEUTRAL_500 = "#6B7280";
    public static final String NEUTRAL_600 = "#4B5563";
    public static final String NEUTRAL_700 = "#374151";
    public static final String NEUTRAL_800 = "#1F2937";
    public static final String NEUTRAL_900 = "#111827";

    // Cores Semânticas
    public static final String SUCCESS = "#10B981";
    public static final String SUCCESS_LIGHT = "#D1FAE5";
    public static final String ERROR = "#EF4444";
    public static final String ERROR_LIGHT = "#FEE2E2";
    public static final String WARNING = "#F59E0B";
    public static final String WARNING_LIGHT = "#FEF3C7";
    public static final String INFO = "#06B6D4";
    public static final String INFO_LIGHT = "#CFFAFE";

    // Cores de Destaque
    public static final String ACCENT_PURPLE = "#8B5CF6";
    public static final String ACCENT_PINK = "#EC4899";
    public static final String ACCENT_ORANGE = "#F97316";
    public static final String ACCENT_EMERALD = "#059669";

    // Gradientes
    public static final String GRADIENT_PRIMARY =
            "linear-gradient(135deg, " + PRIMARY_600 + " 0%, " + PRIMARY_800 + " 100%)";
    public static final String GRADIENT_SUCCESS =
            "linear-gradient(135deg, " + SUCCESS + " 0%, #047857 100%)";
    public static final String GRADIENT_ACCENT =
            "linear-gradient(135deg, " + ACCENT_PURPLE + " 0%, " + ACCENT_PINK + " 100%)";
    public static final String GRADIENT_DARK =
            "linear-gradient(135deg, " + NEUTRAL_800 + " 0%, " + NEUTRAL_900 + " 100%)";

    // ==================== TIPOGRAFIA ====================

    public static final String FONT_FAMILY = "'Segoe UI', 'Inter', system-ui, -apple-system, sans-serif";

    public static String getFontStyle(int size, String weight, String color) {
        return String.format(
                "-fx-font-family: %s; -fx-font-size: %dpx; -fx-font-weight: %s; -fx-text-fill: %s;",
                FONT_FAMILY, size, weight, color
        );
    }

    // Títulos
    public static String getH1Style() { return getFontStyle(28, "800", NEUTRAL_900); }
    public static String getH2Style() { return getFontStyle(22, "700", NEUTRAL_800); }
    public static String getH3Style() { return getFontStyle(18, "600", NEUTRAL_800); }
    public static String getH4Style() { return getFontStyle(16, "600", NEUTRAL_700); }
    public static String getBodyStyle() { return getFontStyle(13, "400", NEUTRAL_600); }
    public static String getSmallStyle() { return getFontStyle(11, "400", NEUTRAL_500); }
    public static String getCaptionStyle() { return getFontStyle(10, "500", NEUTRAL_400); }

    // ==================== ESPAÇAMENTOS ====================

    public static final double SPACING_XXS = 4;
    public static final double SPACING_XS = 8;
    public static final double SPACING_SM = 12;
    public static final double SPACING_MD = 16;
    public static final double SPACING_LG = 24;
    public static final double SPACING_XL = 32;
    public static final double SPACING_XXL = 48;

    public static final double RADIUS_SM = 6;
    public static final double RADIUS_MD = 10;
    public static final double RADIUS_LG = 14;
    public static final double RADIUS_XL = 20;
    public static final double RADIUS_FULL = 999;

    // ==================== SOMBRAS ====================

    public static DropShadow getShadowSm() {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.05));
        shadow.setRadius(4);
        shadow.setOffsetY(1);
        return shadow;
    }

    public static DropShadow getShadowMd() {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.08));
        shadow.setRadius(8);
        shadow.setOffsetY(2);
        return shadow;
    }

    public static DropShadow getShadowLg() {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.12));
        shadow.setRadius(16);
        shadow.setOffsetY(4);
        return shadow;
    }

    public static DropShadow getShadowXl() {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.15));
        shadow.setRadius(24);
        shadow.setOffsetY(8);
        return shadow;
    }

    public static DropShadow getShadowGlow(String color) {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web(color + "40"));
        shadow.setRadius(12);
        shadow.setSpread(0.2);
        return shadow;
    }

    // ==================== ESTILOS CSS ====================

    public static String getBaseStyle() {
        return String.format(
                "-fx-background-color: %s; -fx-font-family: %s;",
                TemaManager.isDarkMode() ? NEUTRAL_900 : NEUTRAL_50, FONT_FAMILY
        );
    }

    public static String getCardStyle() {
        String bg = TemaManager.isDarkMode() ? NEUTRAL_800 : "white";
        String border = TemaManager.isDarkMode() ? NEUTRAL_700 : NEUTRAL_200;
        return String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-border-color: %s; -fx-border-radius: %dpx; -fx-border-width: 1px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);",
                bg, (int)RADIUS_LG, border, (int)RADIUS_LG
        );
    }

    // ==================== COMPONENTES ====================

    /**
     * Cria um card premium
     */
    public static VBox createCard(Node... children) {
        VBox card = new VBox(SPACING_MD);
        card.setStyle(getCardStyle());
        card.setPadding(new Insets(SPACING_LG));
        card.getChildren().addAll(children);
        card.setEffect(getShadowSm());
        return card;
    }

    /**
     * Cria um título estilizado
     */
    public static Label createTitle(String text) {
        Label title = new Label(text);
        title.setStyle(getH2Style());
        return title;
    }

    /**
     * Cria um subtítulo estilizado
     */
    public static Label createSubtitle(String text) {
        Label subtitle = new Label(text);
        subtitle.setStyle(getBodyStyle());
        subtitle.setWrapText(true);
        return subtitle;
    }

    /**
     * Cria um botão primário premium
     */
    public static Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 13px; -fx-background-radius: %dpx; -fx-padding: 10px 20px; " +
                        "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(37,99,235,0.3), 6, 0, 0, 3);",
                PRIMARY_600, (int)RADIUS_MD
        ));

        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle().replace(PRIMARY_600, PRIMARY_700)));
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replace(PRIMARY_700, PRIMARY_600)));

        return btn;
    }

    /**
     * Cria um botão secundário
     */
    public static Button createSecondaryButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(String.format(
                "-fx-background-color: transparent; -fx-text-fill: %s; -fx-font-weight: 600; " +
                        "-fx-font-size: 13px; -fx-background-radius: %dpx; -fx-padding: 10px 20px; " +
                        "-fx-border-color: %s; -fx-border-radius: %dpx; -fx-border-width: 1px; -fx-cursor: hand;",
                PRIMARY_600, (int)RADIUS_MD, PRIMARY_300, (int)RADIUS_MD
        ));

        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle().replace(
                "transparent", PRIMARY_50 + "20")));
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replace(
                PRIMARY_50 + "20", "transparent")));

        return btn;
    }

    /**
     * Cria um campo de texto estilizado
     */
    public static TextField createTextField(String placeholder) {
        TextField field = new TextField();
        String bg = TemaManager.isDarkMode() ? NEUTRAL_700 : "white";
        String border = TemaManager.isDarkMode() ? NEUTRAL_600 : NEUTRAL_300;

        field.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: %dpx; " +
                        "-fx-border-color: %s; -fx-border-radius: %dpx; -fx-border-width: 1px; " +
                        "-fx-padding: 12px 14px; -fx-font-size: 13px; -fx-text-fill: %s; " +
                        "-fx-prompt-text-fill: %s;",
                bg, (int)RADIUS_MD, border, (int)RADIUS_MD,
                TemaManager.isDarkMode() ? NEUTRAL_100 : NEUTRAL_800,
                NEUTRAL_400
        ));

        field.setPromptText(placeholder);
        return field;
    }

    /**
     * Cria um badge de status
     */
    public static Label createBadge(String text, String color) {
        Label badge = new Label(text);
        badge.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-font-size: 11px; -fx-padding: 4px 12px; -fx-background-radius: %dpx;",
                color, (int)RADIUS_FULL
        ));
        return badge;
    }

    /**
     * Cria um separador elegante
     */
    public static Separator createSeparator() {
        Separator sep = new Separator();
        sep.setStyle(String.format(
                "-fx-background-color: %s;",
                TemaManager.isDarkMode() ? NEUTRAL_700 : NEUTRAL_200
        ));
        return sep;
    }
}