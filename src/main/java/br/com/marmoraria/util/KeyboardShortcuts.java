package br.com.marmoraria.util;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * Sistema de atalhos de teclado para produtividade.
 * Compatível com Java 11+.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class KeyboardShortcuts {

    /**
     * Registra atalhos globais na cena
     */
    public static void registrarAtalhos(Scene scene, ShortcutHandler handler) {

        // Ctrl+N - Novo Orçamento
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN),
                handler::onNovoOrcamento
        );

        // Ctrl+S - Salvar
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
                handler::onSalvar
        );

        // Ctrl+P - Gerar PDF
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN),
                handler::onGerarPDF
        );

        // Ctrl+F - Buscar
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN),
                handler::onBuscar
        );

        // Ctrl+D - Dashboard
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN),
                handler::onDashboard
        );

        // Ctrl+B - Backup
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN),
                handler::onBackup
        );

        // Ctrl+, - Configurações
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.COMMA, KeyCombination.CONTROL_DOWN),
                handler::onConfiguracoes
        );

        // F1 - Ajuda
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F1),
                handler::onAjuda
        );

        // F5 - Atualizar
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F5),
                handler::onAtualizar
        );

        // F11 - Tela Cheia
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F11),
                handler::onTelaCheia
        );

        // Escape - Fechar janela
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.ESCAPE),
                handler::onFechar
        );

        // Ctrl+Shift+D - Dark Mode
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN),
                handler::onDarkMode
        );
    }

    /**
     * Interface para handlers de atalhos
     */
    public interface ShortcutHandler {
        default void onNovoOrcamento() { System.out.println("Ctrl+N: Novo Orçamento"); }
        default void onSalvar() { System.out.println("Ctrl+S: Salvar"); }
        default void onGerarPDF() { System.out.println("Ctrl+P: Gerar PDF"); }
        default void onBuscar() { System.out.println("Ctrl+F: Buscar"); }
        default void onDashboard() { System.out.println("Ctrl+D: Dashboard"); }
        default void onBackup() { System.out.println("Ctrl+B: Backup"); }
        default void onConfiguracoes() { System.out.println("Ctrl+,: Configurações"); }
        default void onAjuda() { System.out.println("F1: Ajuda"); }
        default void onAtualizar() { System.out.println("F5: Atualizar"); }
        default void onTelaCheia() { System.out.println("F11: Tela Cheia"); }
        default void onFechar() { System.out.println("Escape: Fechar"); }
        default void onDarkMode() { System.out.println("Ctrl+Shift+D: Dark Mode"); }
    }

    /**
     * Retorna uma string com todos os atalhos disponíveis
     */
    public static String getTodosAtalhos() {
        StringBuilder sb = new StringBuilder();
        sb.append("⌨️ ATALHOS DE TECLADO\n\n");
        sb.append("Ctrl+N  → Novo Orçamento\n");
        sb.append("Ctrl+S  → Salvar\n");
        sb.append("Ctrl+P  → Gerar PDF\n");
        sb.append("Ctrl+F  → Buscar\n");
        sb.append("Ctrl+D  → Dashboard\n");
        sb.append("Ctrl+B  → Backup\n");
        sb.append("Ctrl+,  → Configurações\n");
        sb.append("F1      → Ajuda\n");
        sb.append("F5      → Atualizar\n");
        sb.append("F11     → Tela Cheia\n");
        sb.append("Esc     → Fechar\n");
        sb.append("Ctrl+Shift+D → Dark Mode\n");
        return sb.toString();
    }
}
