package br.com.marmoraria.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class MainController {

    @FXML
    private void abrirCalculadora() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/marmoraria/view/CalculadoraView.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Calculadora de Materiais - Marmoraria");
            stage.setScene(new Scene(root, 900, 600));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            mostrarErro("Erro ao abrir calculadora", e.getMessage());
        }
    }

    @FXML
    private void abrirOrcamentos() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/marmoraria/view/OrcamentoView.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Gestão de Orçamentos - Marmoraria");
            stage.setScene(new Scene(root, 1100, 700));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            mostrarErro("Erro ao abrir orçamentos", e.getMessage());
        }
    }

    @FXML
    private void abrirMateriais() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/marmoraria/view/MaterialView.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Catálogo de Materiais e Serviços - Marmoraria");
            stage.setScene(new Scene(root, 800, 600));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            mostrarErro("Erro ao abrir materiais", e.getMessage());
        }
    }

    @FXML
    private void mostrarSobre() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre o Sistema");
        alert.setHeaderText("Calculadora de Orçamentos - Marmoraria Profissional");
        alert.setContentText(
                "Versão: 1.0.0\n" +
                        "Desenvolvido para marmorarias e indústrias de pedras\n" +
                        "\n" +
                        "Funcionalidades:\n" +
                        "• Cálculo preciso de áreas e custos\n" +
                        "• Catálogo de materiais (mármore, granito, quartzo)\n" +
                        "• Gestão completa de orçamentos\n" +
                        "• Geração de PDF e relatórios\n" +
                        "• Cálculo de margens de lucro\n" +
                        "\n" +
                        "© 2024 - Todos os direitos reservados"
        );
        alert.showAndWait();
    }

    @FXML
    private void sair() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sair");
        alert.setHeaderText("Deseja realmente sair do sistema?");
        alert.setContentText("Todos os dados não salvos serão perdidos.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // Obter a janela atual através do botão ou outro nó
            // Mas como alternativa simples, podemos fechar a aplicação
            System.exit(0);
        }
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}