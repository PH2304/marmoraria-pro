package br.com.marmoraria.service;

import br.com.marmoraria.model.Orcamento;
import br.com.marmoraria.util.GeradorPDF;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Serviço de pré-visualização de PDF antes da geração.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class PDFPreviewService {

    /**
     * Mostra pré-visualização do orçamento antes de gerar PDF
     */
    public static void mostrarPreview(Orcamento orcamento) {
        Stage previewStage = new Stage();
        previewStage.setTitle("Pré-visualização do PDF - " + orcamento.getNumeroOrcamento());

        VBox root = new VBox(15);
        root.setPadding(new javafx.geometry.Insets(20));
        root.setStyle("-fx-background-color: #F8FAFC;");

        // Título
        javafx.scene.control.Label title = new javafx.scene.control.Label(
                "📄 Pré-visualização do Orçamento");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1F2937;");

        // Área de preview
        TextArea previewArea = new TextArea();
        previewArea.setEditable(false);
        previewArea.setStyle(
                "-fx-font-family: 'Courier New', monospace; " +
                        "-fx-font-size: 12px; " +
                        "-fx-background-color: white; " +
                        "-fx-border-color: #E5E7EB; " +
                        "-fx-border-radius: 8px;"
        );
        previewArea.setPrefHeight(500);
        previewArea.setWrapText(false);

        // Gerar preview
        String preview = gerarPreviewTexto(orcamento);
        previewArea.setText(preview);

        // Botões
        javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(10);
        buttons.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        javafx.scene.control.Button btnGerarPDF = new javafx.scene.control.Button("📥 Gerar PDF");
        btnGerarPDF.setStyle(
                "-fx-background-color: #2563EB; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-padding: 10px 20px; " +
                        "-fx-background-radius: 8px; -fx-cursor: hand;"
        );
        btnGerarPDF.setOnAction(e -> {
            boolean sucesso = GeradorPDF.gerarOrcamentoPDF(orcamento);
            if (sucesso) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("PDF Gerado");
                alert.setHeaderText("✅ Sucesso!");
                alert.setContentText("PDF gerado com sucesso!\n\nArquivo: " +
                        orcamento.getNumeroOrcamento() + ".pdf");
                alert.showAndWait();
                previewStage.close();
            }
        });

        javafx.scene.control.Button btnFechar = new javafx.scene.control.Button("Fechar");
        btnFechar.setStyle(
                "-fx-background-color: #6B7280; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-padding: 10px 20px; " +
                        "-fx-background-radius: 8px; -fx-cursor: hand;"
        );
        btnFechar.setOnAction(e -> previewStage.close());

        buttons.getChildren().addAll(btnGerarPDF, btnFechar);

        root.getChildren().addAll(title, previewArea, buttons);

        Scene scene = new Scene(root, 700, 650);
        previewStage.setScene(scene);
        previewStage.show();
    }

    /**
     * Gera texto de preview formatado
     */
    private static String gerarPreviewTexto(Orcamento orcamento) {
        StringBuilder sb = new StringBuilder();

        sb.append("═".repeat(60)).append("\n");
        sb.append("                    MARMORARIA PRO\n");
        sb.append("              ORÇAMENTO PROFISSIONAL\n");
        sb.append("═".repeat(60)).append("\n\n");

        // Dados do orçamento
        sb.append(String.format("Nº Orçamento: %s\n", orcamento.getNumeroOrcamento()));
        sb.append(String.format("Data: %s\n", orcamento.getDataFormatada()));
        sb.append(String.format("Status: %s\n\n", orcamento.getStatus()));

        // Dados do cliente
        if (orcamento.getClienteNome() != null && !orcamento.getClienteNome().isEmpty()) {
            sb.append("DADOS DO CLIENTE\n");
            sb.append("-".repeat(60)).append("\n");
            sb.append(String.format("Cliente: %s\n", orcamento.getClienteNome()));
            if (orcamento.getClienteTelefone() != null) {
                sb.append(String.format("Telefone: %s\n", orcamento.getClienteTelefone()));
            }
            if (orcamento.getEnderecoObra() != null) {
                sb.append(String.format("Obra: %s\n", orcamento.getEnderecoObra()));
            }
            sb.append("\n");
        }

        // Itens
        sb.append("ITENS DO ORÇAMENTO\n");
        sb.append("-".repeat(60)).append("\n");

        double totalItens = 0;
        for (int i = 0; i < orcamento.getItens().size(); i++) {
            var item = orcamento.getItens().get(i);
            sb.append(String.format("%d. %s\n", i + 1, item.getMaterial().getNome()));
            sb.append(String.format("   Tipo: %s | Qtd: %d | Área: %.2f m²\n",
                    item.getTipoTrabalho().getDescricao(),
                    item.getQuantidade(),
                    item.getArea()));
            sb.append(String.format("   Valor: R$ %.2f\n\n", item.getTotal()));
            totalItens += item.getTotal();
        }

        // Totais
        sb.append("═".repeat(60)).append("\n");
        sb.append("TOTAIS\n");
        sb.append("-".repeat(60)).append("\n");
        sb.append(String.format("Subtotal Itens:      R$ %10.2f\n", orcamento.getValorTotal()));
        sb.append(String.format("Margem (%.0f%%):       R$ %10.2f\n",
                orcamento.getMargemLucro(), orcamento.getValorLucro()));
        sb.append(String.format("Subtotal c/ Margem:  R$ %10.2f\n", orcamento.getValorComLucro()));

        if (orcamento.isIncluirMaoDeObra()) {
            sb.append(String.format("Mão de Obra:         R$ %10.2f\n", orcamento.getValorMaoDeObra()));
        }

        sb.append(String.format("Frete:               R$ %10.2f\n", orcamento.getFrete()));
        sb.append(String.format("Crédito:             R$ %10.2f\n", orcamento.getCredito()));
        sb.append("═".repeat(60)).append("\n");
        sb.append(String.format("TOTAL FINAL:         R$ %10.2f\n", orcamento.getTotalFinal()));
        sb.append("═".repeat(60)).append("\n");

        // Observações
        if (orcamento.getObservacoes() != null && !orcamento.getObservacoes().isEmpty()) {
            sb.append("\nOBSERVAÇÕES:\n");
            sb.append("-".repeat(60)).append("\n");
            sb.append(orcamento.getObservacoes()).append("\n");
        }

        return sb.toString();
    }
}