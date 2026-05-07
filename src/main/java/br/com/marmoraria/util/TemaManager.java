package br.com.marmoraria.util;

import javafx.scene.Scene;
import javafx.scene.Parent;

/**
 * Gerenciador de temas com suporte a Light/Dark mode.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class TemaManager {

    // ==================== CORES LIGHT MODE ====================

    public static final String LIGHT_BG = "#F0F4F8";
    public static final String LIGHT_SURFACE = "#FFFFFF";
    public static final String LIGHT_SURFACE_HOVER = "#F8FAFC";
    public static final String LIGHT_BORDER = "#E5E7EB";
    public static final String LIGHT_BORDER_LIGHT = "#F3F4F6";
    public static final String LIGHT_TEXT_PRIMARY = "#111827";
    public static final String LIGHT_TEXT_SECONDARY = "#6B7280";
    public static final String LIGHT_TEXT_TERTIARY = "#9CA3AF";

    // Cores de acento (Light)
    public static final String LIGHT_PRIMARY = "#2563EB";
    public static final String LIGHT_PRIMARY_GHOST = "#EFF6FF";
    public static final String LIGHT_SUCCESS = "#059669";
    public static final String LIGHT_SUCCESS_GHOST = "#ECFDF5";
    public static final String LIGHT_WARNING = "#D97706";
    public static final String LIGHT_WARNING_GHOST = "#FFFBEB";
    public static final String LIGHT_ACCENT = "#7C3AED";
    public static final String LIGHT_ACCENT_GHOST = "#F5F3FF";
    public static final String LIGHT_INFO = "#0891B2";
    public static final String LIGHT_INFO_GHOST = "#ECFEFF";

    // ==================== CORES DARK MODE ====================

    public static final String DARK_BG = "#0F172A";
    public static final String DARK_SURFACE = "#1E293B";
    public static final String DARK_SURFACE_HOVER = "#273548";
    public static final String DARK_BORDER = "#334155";
    public static final String DARK_BORDER_LIGHT = "#273548";
    public static final String DARK_TEXT_PRIMARY = "#F1F5F9";
    public static final String DARK_TEXT_SECONDARY = "#94A3B8";
    public static final String DARK_TEXT_TERTIARY = "#64748B";

    // Cores de acento (Dark)
    public static final String DARK_PRIMARY = "#3B82F6";
    public static final String DARK_PRIMARY_GHOST = "#1E3A5F";
    public static final String DARK_SUCCESS = "#10B981";
    public static final String DARK_SUCCESS_GHOST = "#064E3B";
    public static final String DARK_WARNING = "#F59E0B";
    public static final String DARK_WARNING_GHOST = "#78350F";
    public static final String DARK_ACCENT = "#8B5CF6";
    public static final String DARK_ACCENT_GHOST = "#3B1F6E";
    public static final String DARK_INFO = "#22D3EE";
    public static final String DARK_INFO_GHOST = "#164E63";

    // ==================== ESTADO ATUAL ====================

    private static boolean darkMode = false;

    // ==================== GETTERS DINÂMICOS ====================

    public static String getBg() { return darkMode ? DARK_BG : LIGHT_BG; }
    public static String getSurface() { return darkMode ? DARK_SURFACE : LIGHT_SURFACE; }
    public static String getSurfaceHover() { return darkMode ? DARK_SURFACE_HOVER : LIGHT_SURFACE_HOVER; }
    public static String getBorder() { return darkMode ? DARK_BORDER : LIGHT_BORDER; }
    public static String getBorderLight() { return darkMode ? DARK_BORDER_LIGHT : LIGHT_BORDER_LIGHT; }
    public static String getTextPrimary() { return darkMode ? DARK_TEXT_PRIMARY : LIGHT_TEXT_PRIMARY; }
    public static String getTextSecondary() { return darkMode ? DARK_TEXT_SECONDARY : LIGHT_TEXT_SECONDARY; }
    public static String getTextTertiary() { return darkMode ? DARK_TEXT_TERTIARY : LIGHT_TEXT_TERTIARY; }

    public static String getPrimary() { return darkMode ? DARK_PRIMARY : LIGHT_PRIMARY; }
    public static String getPrimaryGhost() { return darkMode ? DARK_PRIMARY_GHOST : LIGHT_PRIMARY_GHOST; }
    public static String getSuccess() { return darkMode ? DARK_SUCCESS : LIGHT_SUCCESS; }
    public static String getSuccessGhost() { return darkMode ? DARK_SUCCESS_GHOST : LIGHT_SUCCESS_GHOST; }
    public static String getWarning() { return darkMode ? DARK_WARNING : LIGHT_WARNING; }
    public static String getWarningGhost() { return darkMode ? DARK_WARNING_GHOST : LIGHT_WARNING_GHOST; }
    public static String getAccent() { return darkMode ? DARK_ACCENT : LIGHT_ACCENT; }
    public static String getAccentGhost() { return darkMode ? DARK_ACCENT_GHOST : LIGHT_ACCENT_GHOST; }
    public static String getInfo() { return darkMode ? DARK_INFO : LIGHT_INFO; }
    public static String getInfoGhost() { return darkMode ? DARK_INFO_GHOST : LIGHT_INFO_GHOST; }

    public static boolean isDarkMode() { return darkMode; }

    /**
     * Alterna entre Light e Dark mode
     */
    public static void toggleDarkMode() {
        darkMode = !darkMode;
        System.out.println("🌓 Modo " + (darkMode ? "Escuro" : "Claro") + " ativado");
    }

    /**
     * Define o modo escuro
     */
    public static void setDarkMode(boolean dark) {
        darkMode = dark;
    }

    /**
     * Gera o CSS dinâmico baseado no tema atual
     */
    public static String gerarCSSDinamico() {
        StringBuilder css = new StringBuilder();

        String bg = getBg();
        String surface = getSurface();
        String border = getBorder();
        String textPrimary = getTextPrimary();
        String textSecondary = getTextSecondary();
        String primary = getPrimary();

        css.append(".root {\n");
        css.append("    -fx-background-color: ").append(bg).append(";\n");
        css.append("}\n\n");

        css.append(".scroll-bar:vertical .thumb {\n");
        css.append("    -fx-background-color: ").append(darkMode ? "#475569" : "#CBD5E1").append(";\n");
        css.append("    -fx-background-radius: 4px;\n");
        css.append("}\n\n");

        css.append(".tooltip {\n");
        css.append("    -fx-background-color: ").append(darkMode ? "#F1F5F9" : "#1E293B").append(";\n");
        css.append("    -fx-text-fill: ").append(darkMode ? "#0F172A" : "white").append(";\n");
        css.append("    -fx-font-size: 12px;\n");
        css.append("    -fx-padding: 10px 14px;\n");
        css.append("    -fx-background-radius: 8px;\n");
        css.append("}\n\n");

        css.append(".table-view {\n");
        css.append("    -fx-background-color: ").append(surface).append(";\n");
        css.append("    -fx-background-radius: 12px;\n");
        css.append("    -fx-border-color: ").append(border).append(";\n");
        css.append("    -fx-border-radius: 12px;\n");
        css.append("}\n\n");

        css.append(".table-view .column-header-background {\n");
        css.append("    -fx-background-color: ").append(darkMode ? "#273548" : "#F8FAFC").append(";\n");
        css.append("}\n\n");

        css.append(".table-row-cell {\n");
        css.append("    -fx-background-color: ").append(surface).append(";\n");
        css.append("    -fx-text-fill: ").append(textPrimary).append(";\n");
        css.append("}\n\n");

        css.append(".table-row-cell:hover {\n");
        css.append("    -fx-background-color: ").append(darkMode ? "#273548" : "#F8FAFC").append(";\n");
        css.append("}\n\n");

        css.append(".table-row-cell:selected {\n");
        css.append("    -fx-background-color: ").append(darkMode ? "#1E3A5F" : "#EFF6FF").append(";\n");
        css.append("}\n\n");

        css.append(".text-field, .text-area {\n");
        css.append("    -fx-background-color: ").append(surface).append(";\n");
        css.append("    -fx-text-fill: ").append(textPrimary).append(";\n");
        css.append("    -fx-border-color: ").append(border).append(";\n");
        css.append("    -fx-background-radius: 8px;\n");
        css.append("    -fx-border-radius: 8px;\n");
        css.append("}\n\n");

        css.append(".combo-box {\n");
        css.append("    -fx-background-color: ").append(surface).append(";\n");
        css.append("    -fx-text-fill: ").append(textPrimary).append(";\n");
        css.append("    -fx-border-color: ").append(border).append(";\n");
        css.append("    -fx-background-radius: 8px;\n");
        css.append("    -fx-border-radius: 8px;\n");
        css.append("}\n\n");

        css.append(".combo-box .list-cell {\n");
        css.append("    -fx-text-fill: ").append(textPrimary).append(";\n");
        css.append("    -fx-background-color: ").append(surface).append(";\n");
        css.append("}\n\n");

        css.append(".combo-box-popup .list-view {\n");
        css.append("    -fx-background-color: ").append(surface).append(";\n");
        css.append("    -fx-border-color: ").append(border).append(";\n");
        css.append("}\n\n");

        css.append(".combo-box-popup .list-cell {\n");
        css.append("    -fx-text-fill: ").append(textPrimary).append(";\n");
        css.append("    -fx-background-color: ").append(surface).append(";\n");
        css.append("}\n\n");

        css.append(".combo-box-popup .list-cell:hover {\n");
        css.append("    -fx-background-color: ").append(darkMode ? "#273548" : "#F8FAFC").append(";\n");
        css.append("}\n\n");

        css.append(".check-box {\n");
        css.append("    -fx-text-fill: ").append(textPrimary).append(";\n");
        css.append("}\n\n");

        css.append(".label {\n");
        css.append("    -fx-text-fill: ").append(textPrimary).append(";\n");
        css.append("}\n\n");

        return css.toString();
    }
}