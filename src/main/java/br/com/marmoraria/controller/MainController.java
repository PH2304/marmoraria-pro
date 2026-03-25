package br.com.marmoraria.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import br.com.marmoraria.view.OrcamentoView;

import java.io.IOException;

public class MainController {

    @FXML
    private void abrirCalculadora() {
        try {
            System.out.println("Abrindo calculadora...");

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/resources/br/com/marmoraria/view/CalculadoraView.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Calculadora - Marmoraria Pro");
            stage.setScene(new Scene(root, 900, 600));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (IOException e) {
            mostrarErro("Erro ao abrir calculadora", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirOrcamentos() {
        try {
            System.out.println("Abrindo orçamentos...");

            // Usar a OrcamentoView.java existente
            OrcamentoView orcamentoView = new OrcamentoView();
            Scene scene = new Scene(orcamentoView, 1100, 700);

            Stage stage = new Stage();
            stage.setTitle("Orçamentos - Marmoraria Pro");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (Exception e) {
            mostrarErro("Erro ao abrir orçamentos", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirMateriais() {
        mostrarInfo("Em Desenvolvimento", "Funcionalidade de catálogo em breve!");
    }

    @FXML
    private void mostrarAjuda() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajuda");
        alert.setHeaderText("Guia Rápido");
        alert.setContentText(
                "1. Calculadora: Adicione materiais e calcule orçamentos\n" +
                        "2. Orçamentos: Visualize e gerencie seus orçamentos\n" +
                        "3. Catálogo: Em breve\n\n" +
                        "Para mais informações, aguarde as próximas atualizações."
        );
        alert.showAndWait();
    }

    @FXML
    private void mostrarSobre() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre");
        alert.setHeaderText("Marmoraria Pro");
        alert.setContentText(
                "Versão: 2.0.0\n" +
                        "Sistema para cálculo de orçamentos de marmoraria\n\n" +
                        "© 2024 - Todos os direitos reservados"
        );
        alert.showAndWait();
    }

    @FXML
    private void sair() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sair");
        alert.setHeaderText("Confirmar saída");
        alert.setContentText("Deseja realmente sair do sistema?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
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

    private void mostrarInfo(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}