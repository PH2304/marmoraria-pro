package br.com.marmoraria.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Verificador de atualizações do sistema.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class UpdateChecker {

    private static final Logger LOGGER = Logger.getLogger(UpdateChecker.class.getName());

    private static final String VERSAO_ATUAL = "3.0.0";
    private static final String UPDATE_URL = "https://api.github.com/repos/marmoraria-pro/releases/latest";

    private static boolean atualizacaoDisponivel = false;
    private static String ultimaVersao = "";
    private static String releaseNotes = "";

    /**
     * Verifica se há atualizações disponíveis
     */
    public static void verificarAtualizacoes() {
        new Thread(() -> {
            try {
                URL url = new URL(UPDATE_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestProperty("User-Agent", "MarmorariaPro/" + VERSAO_ATUAL);

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Extrair versão (simplificado)
                    String json = response.toString();
                    if (json.contains("\"tag_name\"")) {
                        int start = json.indexOf("\"tag_name\"") + 12;
                        int end = json.indexOf("\"", start);
                        ultimaVersao = json.substring(start, end).replace("v", "");

                        if (compararVersoes(ultimaVersao, VERSAO_ATUAL) > 0) {
                            atualizacaoDisponivel = true;

                            // Extrair release notes
                            if (json.contains("\"body\"")) {
                                start = json.indexOf("\"body\"") + 8;
                                end = json.indexOf("\"", start);
                                releaseNotes = json.substring(start, end)
                                        .replace("\\n", "\n")
                                        .replace("\\r", "");
                            }
                        }
                    }
                }
                conn.disconnect();
            } catch (Exception e) {
                LOGGER.fine("Verificação de atualização indisponível: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Mostra diálogo de atualização se disponível
     */
    public static void mostrarDialogoAtualizacao() {
        if (!atualizacaoDisponivel) return;

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Atualização Disponível");
            alert.setHeaderText("🚀 Nova versão disponível: v" + ultimaVersao);
            alert.setContentText(
                    "Sua versão: v" + VERSAO_ATUAL + "\n" +
                            "Nova versão: v" + ultimaVersao + "\n\n" +
                            "Novidades:\n" + releaseNotes + "\n\n" +
                            "Deseja baixar a atualização?"
            );

            ButtonType btnBaixar = new ButtonType("Baixar");
            ButtonType btnDepois = new ButtonType("Depois");
            alert.getButtonTypes().setAll(btnBaixar, btnDepois);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == btnBaixar) {
                abrirPaginaDownload();
            }
        });
    }

    /**
     * Compara duas versões semânticas
     */
    private static int compararVersoes(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");

        int length = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < length; i++) {
            int p1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int p2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            if (p1 != p2) return p1 - p2;
        }
        return 0;
    }

    /**
     * Abre página de download no navegador
     */
    private static void abrirPaginaDownload() {
        try {
            java.awt.Desktop.getDesktop().browse(
                    new java.net.URI("https://github.com/marmoraria-pro/releases/latest"));
        } catch (Exception e) {
            LOGGER.warning("Não foi possível abrir o navegador");
        }
    }

    // Getters
    public static String getVersaoAtual() { return VERSAO_ATUAL; }
    public static boolean isAtualizacaoDisponivel() { return atualizacaoDisponivel; }
    public static String getUltimaVersao() { return ultimaVersao; }
}