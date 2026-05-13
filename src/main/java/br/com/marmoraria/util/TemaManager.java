package br.com.marmoraria.util;

/**
 * Gerenciador de temas com suporte a Light/Dark mode.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class TemaManager {

    private static boolean darkMode = false;

    // ==================== LIGHT MODE ====================

    private static final String LIGHT_BG = "#F0F4F8";
    private static final String LIGHT_SURFACE = "#FFFFFF";
    private static final String LIGHT_SURFACE_HOVER = "#F8FAFC";
    private static final String LIGHT_BORDER = "#E5E7EB";
    private static final String LIGHT_BORDER_LIGHT = "#F3F4F6";
    private static final String LIGHT_TEXT_PRIMARY = "#111827";
    private static final String LIGHT_TEXT_SECONDARY = "#6B7280";
    private static final String LIGHT_TEXT_TERTIARY = "#9CA3AF";
    private static final String LIGHT_PRIMARY = "#2563EB";
    private static final String LIGHT_PRIMARY_GHOST = "#EFF6FF";
    private static final String LIGHT_SUCCESS = "#059669";
    private static final String LIGHT_SUCCESS_GHOST = "#ECFDF5";
    private static final String LIGHT_WARNING = "#D97706";
    private static final String LIGHT_WARNING_GHOST = "#FFFBEB";
    private static final String LIGHT_ACCENT = "#7C3AED";
    private static final String LIGHT_ACCENT_GHOST = "#F5F3FF";
    private static final String LIGHT_INFO = "#0891B2";
    private static final String LIGHT_INFO_GHOST = "#ECFEFF";

    // ==================== DARK MODE ====================

    private static final String DARK_BG = "#0F172A";
    private static final String DARK_SURFACE = "#1E293B";
    private static final String DARK_SURFACE_HOVER = "#273548";
    private static final String DARK_BORDER = "#334155";
    private static final String DARK_BORDER_LIGHT = "#273548";
    private static final String DARK_TEXT_PRIMARY = "#F1F5F9";
    private static final String DARK_TEXT_SECONDARY = "#94A3B8";
    private static final String DARK_TEXT_TERTIARY = "#64748B";
    private static final String DARK_PRIMARY = "#3B82F6";
    private static final String DARK_PRIMARY_GHOST = "#1E3A5F";
    private static final String DARK_SUCCESS = "#10B981";
    private static final String DARK_SUCCESS_GHOST = "#064E3B";
    private static final String DARK_WARNING = "#F59E0B";
    private static final String DARK_WARNING_GHOST = "#78350F";
    private static final String DARK_ACCENT = "#8B5CF6";
    private static final String DARK_ACCENT_GHOST = "#3B1F6E";
    private static final String DARK_INFO = "#22D3EE";
    private static final String DARK_INFO_GHOST = "#164E63";

    // ==================== GETTERS ====================

    public static boolean isDarkMode() { return darkMode; }

    public static void setDarkMode(boolean dark) {
        darkMode = dark;
        System.out.println("🌓 Modo " + (darkMode ? "Escuro" : "Claro") + " ativado");
    }

    public static void toggleDarkMode() {
        setDarkMode(!darkMode);
    }

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

    /**
     * Retorna a opacidade da sombra baseado no tema
     */
    public static double getShadowOpacity() {
        return darkMode ? 0.35 : 0.06;
    }

    /**
     * Retorna a opacidade da sombra hover
     */
    public static double getShadowHoverOpacity() {
        return darkMode ? 0.45 : 0.12;
    }
}