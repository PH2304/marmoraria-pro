package br.com.marmoraria.view;

import br.com.marmoraria.model.Orcamento;
import br.com.marmoraria.util.FileManager;
import br.com.marmoraria.util.GeradorPDF;
import br.com.marmoraria.util.FileManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GestaoOrcamentosView extends BorderPane {

    private ListView<String> listaOrcamentos;
    private TextArea detalhesArea;

    public GestaoOrcamentosView() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #ecf0f1;");

        // Título
        Label titulo = new Label("📋 Gestão de Orçamentos Salvos");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Painel esquerdo - Lista de orçamentos
        VBox painelLista = criarPainelLista();

        // Painel direito - Informações
        VBox painelInfo = criarPainelInfo();

        // Botões
        HBox botoes = criarBotoes();

        // Layout
        HBox conteudo = new HBox(20);
        conteudo.getChildren().addAll(painelLista, painelInfo);

        setTop(titulo);
        setCenter(conteudo);
        setBottom(botoes);

        // Carregar lista de orçamentos
        carregarListaOrcamentos();
    }

    private VBox criarPainelLista() {
        VBox box = new VBox(10);
        box.setPrefWidth(350);

        Label label = new Label("📋 Orçamentos Salvos");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        listaOrcamentos = new ListView<>();
        listaOrcamentos.setPrefHeight(400);
        listaOrcamentos.setStyle("-fx-background-color: white; -fx-background-radius: 8px;");

        // Evento de seleção
        listaOrcamentos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                carregarDetalhesOrcamento(newVal);
            }
        });

        VBox.setVgrow(listaOrcamentos, Priority.ALWAYS);
        box.getChildren().addAll(label, listaOrcamentos);
        return box;
    }

    private VBox criarPainelInfo() {
        VBox box = new VBox(10);
        box.setPrefWidth(450);

        Label label = new Label("ℹ️ Detalhes do Orçamento");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        detalhesArea = new TextArea();
        detalhesArea.setEditable(false);
        detalhesArea.setPrefHeight(400);
        detalhesArea.setStyle("-fx-font-family: monospace; -fx-font-size: 12px;");
        detalhesArea.setWrapText(true);

        box.getChildren().addAll(label, detalhesArea);
        return box;
    }

    private HBox criarBotoes() {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20, 0, 0, 0));

        Button btnAbrir = new Button("📂 Abrir Orçamento");
        btnAbrir.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-weight: bold;");
        btnAbrir.setOnAction(e -> abrirOrcamentoSelecionado());

        Button btnGerarPDF = new Button("📄 Gerar PDF");
        btnGerarPDF.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-weight: bold;");
        btnGerarPDF.setOnAction(e -> gerarPDFDoOrcamentoSelecionado());

        Button btnDeletar = new Button("🗑️ Deletar");
        btnDeletar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-weight: bold;");
        btnDeletar.setOnAction(e -> deletarOrcamentoSelecionado());

        Button btnAtualizar = new Button("🔄 Atualizar");
        btnAtualizar.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-padding: 10 20;");
        btnAtualizar.setOnAction(e -> carregarListaOrcamentos());

        Button btnFechar = new Button("❌ Fechar");
        btnFechar.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-padding: 10 20;");
        btnFechar.setOnAction(e -> ((Stage) getScene().getWindow()).close());

        box.getChildren().addAll(btnAbrir, btnGerarPDF, btnDeletar, btnAtualizar, btnFechar);
        return box;
    }

    private void carregarListaOrcamentos() {
        listaOrcamentos.getItems().clear();
        List<Orcamento> orcamentos = FileManager.listarOrcamentos();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        if (orcamentos.isEmpty()) {
            listaOrcamentos.getItems().add("Nenhum orçamento encontrado");
            detalhesArea.setText("Nenhum orçamento salvo ainda.\n\nUse a calculadora para criar e salvar orçamentos.");
        } else {
            for (Orcamento orc : orcamentos) {
                String data = sdf.format(new Date(orc.getDataCriacao().toEpochSecond(null) * 1000));
                String info = String.format("%s | %s | R$ %.2f | %s",
                        orc.getNumeroOrcamento(),
                        data,
                        orc.getValorComLucro(),
                        orc.getStatus());
                listaOrcamentos.getItems().add(info);
            }
        }
    }

    private void carregarDetalhesOrcamento(String itemSelecionado) {
        if (itemSelecionado.equals("Nenhum orçamento encontrado")) {
            return;
        }

        String numeroOrcamento = itemSelecionado.split(" \\| ")[0];
        Orcamento orcamento = FileManager.carregarOrcamento(numeroOrcamento);

        if (orcamento != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("═".repeat(50)).append("\n");
            sb.append(String.format("ORÇAMENTO: %s\n", orcamento.getNumeroOrcamento()));
            sb.append(String.format("DATA: %s\n", orcamento.getDataFormatada()));
            sb.append(String.format("STATUS: %s\n", orcamento.getStatus()));
            sb.append("═".repeat(50)).append("\n\n");

            sb.append("📋 ITENS:\n");
            sb.append("─".repeat(40)).append("\n");

            for (int i = 0; i < orcamento.getItens().size(); i++) {
                var item = orcamento.getItens().get(i);
                sb.append(String.format("%d. %s\n", i + 1, item.getMaterial().getNome()));
                sb.append(String.format("   Dimensões: %.0f x %.0f mm\n", item.getLargura(), item.getComprimento()));
                sb.append(String.format("   Quantidade: %d\n", item.getQuantidade()));
                sb.append(String.format("   Área: %.2f m²\n", item.getArea()));
                sb.append(String.format("   Total: R$ %.2f\n", item.getTotal()));
                sb.append("\n");
            }

            sb.append("═".repeat(50)).append("\n");
            sb.append(String.format("💰 VALOR TOTAL: R$ %.2f\n", orcamento.getValorTotal()));
            sb.append(String.format("📈 MARGEM DE LUCRO: %.1f%%\n", orcamento.getMargemLucro()));
            sb.append(String.format("💵 TOTAL COM LUCRO: R$ %.2f\n", orcamento.getValorComLucro()));
            sb.append("═".repeat(50)).append("\n");

            if (orcamento.getObservacoes() != null && !orcamento.getObservacoes().isEmpty()) {
                sb.append("\n📝 OBSERVAÇÕES:\n");
                sb.append(orcamento.getObservacoes()).append("\n");
            }

            detalhesArea.setText(sb.toString());
        } else {
            detalhesArea.setText("Erro ao carregar orçamento.");
        }
    }

    private void abrirOrcamentoSelecionado() {
        String selecionado = listaOrcamentos.getSelectionModel().getSelectedItem();
        if (selecionado == null || selecionado.equals("Nenhum orçamento encontrado")) {
            mostrarAlerta("Selecione um orçamento para abrir.");
            return;
        }

        String numeroOrcamento = selecionado.split(" \\| ")[0];
        Orcamento orcamento = FileManager.carregarOrcamento(numeroOrcamento);

        if (orcamento != null) {
            OrcamentoView orcamentoView = new OrcamentoView();
            orcamentoView.carregarOrcamento(orcamento);

            Scene scene = new Scene(orcamentoView, 950, 580);
            Stage stage = new Stage();
            stage.setTitle("Orçamento: " + numeroOrcamento);
            stage.setScene(scene);
            stage.show();
        } else {
            mostrarAlerta("Erro ao carregar orçamento.");
        }
    }

    /**
     * Gera PDF do orçamento selecionado
     */
    private void gerarPDFDoOrcamentoSelecionado() {
        String selecionado = listaOrcamentos.getSelectionModel().getSelectedItem();
        if (selecionado == null || selecionado.equals("Nenhum orçamento encontrado")) {
            mostrarAlerta("Selecione um orçamento para gerar PDF.");
            return;
        }

        // Extrair o número do orçamento
        String numeroOrcamento = selecionado.split(" \\| ")[0];

        Orcamento orcamento = FileManager.carregarOrcamento(numeroOrcamento);

        if (orcamento != null) {
            boolean sucesso = GeradorPDF.gerarOrcamentoPDF(orcamento);

            if (sucesso) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("PDF Gerado");
                confirm.setHeaderText("Orçamento gerado com sucesso!");
                confirm.setContentText(
                        "Arquivo: " + numeroOrcamento + ".pdf\n" +
                                "Local: " + GeradorPDF.getPastaPDFs() + "\n\n" +
                                "Deseja abrir o PDF agora?"
                );

                if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    GeradorPDF.abrirPDF(numeroOrcamento);
                }
            } else {
                mostrarAlerta("❌ Erro ao gerar PDF.\n\nVerifique se a pasta 'pdfs' tem permissão de escrita.");
            }
        } else {
            mostrarAlerta("Erro ao carregar orçamento.");
        }
    }

    private void deletarOrcamentoSelecionado() {
        String selecionado = listaOrcamentos.getSelectionModel().getSelectedItem();
        if (selecionado == null || selecionado.equals("Nenhum orçamento encontrado")) {
            mostrarAlerta("Selecione um orçamento para deletar.");
            return;
        }

        String numeroOrcamento = selecionado.split(" \\| ")[0];

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar exclusão");
        confirm.setHeaderText("Deletar orçamento");
        confirm.setContentText("Tem certeza que deseja deletar o orçamento " + numeroOrcamento + "?\n\nEsta ação não pode ser desfeita.");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            boolean deletado = FileManager.deletarOrcamento(numeroOrcamento);
            if (deletado) {
                carregarListaOrcamentos();
                detalhesArea.clear();
                mostrarAlerta("✅ Orçamento deletado com sucesso!");
            } else {
                mostrarAlerta("❌ Erro ao deletar orçamento.");
            }
        }
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}