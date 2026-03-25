package br.com.marmoraria.view;

import br.com.marmoraria.model.Orcamento;
import br.com.marmoraria.util.FileManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GestaoOrcamentosView extends BorderPane {

    private ListView<String> listaOrcamentos;
    private ObservableList<String> orcamentosList;
    private TextArea detalhesArea;

    public GestaoOrcamentosView() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #ecf0f1;");

        // Título
        Label titulo = new Label("📋 Gestão de Orçamentos Salvos");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Painel esquerdo - Lista de orçamentos
        VBox painelLista = criarPainelLista();

        // Painel direito - Detalhes
        VBox painelDetalhes = criarPainelDetalhes();

        // Botões
        HBox botoes = criarBotoes();

        // Layout
        HBox conteudo = new HBox(20);
        conteudo.getChildren().addAll(painelLista, painelDetalhes);

        setTop(titulo);
        setCenter(conteudo);
        setBottom(botoes);

        // Carregar lista de orçamentos
        carregarListaOrcamentos();
    }

    private VBox criarPainelLista() {
        VBox box = new VBox(10);
        box.setPrefWidth(300);

        Label label = new Label("Orçamentos Salvos");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        orcamentosList = FXCollections.observableArrayList();
        listaOrcamentos = new ListView<>(orcamentosList);
        listaOrcamentos.setPrefHeight(400);
        listaOrcamentos.setStyle("-fx-background-color: white; -fx-background-radius: 8px;");

        // Evento de seleção
        listaOrcamentos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                carregarDetalhesOrcamento(newVal);
            }
        });

        box.getChildren().addAll(label, listaOrcamentos);
        return box;
    }

    private VBox criarPainelDetalhes() {
        VBox box = new VBox(10);
        box.setPrefWidth(500);

        Label label = new Label("Detalhes do Orçamento");
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
        btnAbrir.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 10 20;");
        btnAbrir.setOnAction(e -> abrirOrcamentoSelecionado());

        Button btnDeletar = new Button("🗑️ Deletar");
        btnDeletar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10 20;");
        btnDeletar.setOnAction(e -> deletarOrcamentoSelecionado());

        Button btnAtualizar = new Button("🔄 Atualizar");
        btnAtualizar.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-padding: 10 20;");
        btnAtualizar.setOnAction(e -> carregarListaOrcamentos());

        Button btnFechar = new Button("❌ Fechar");
        btnFechar.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-padding: 10 20;");
        btnFechar.setOnAction(e -> ((Stage) getScene().getWindow()).close());

        box.getChildren().addAll(btnAbrir, btnDeletar, btnAtualizar, btnFechar);
        return box;
    }

    private void carregarListaOrcamentos() {
        orcamentosList.clear();
        orcamentosList.addAll(FileManager.listarNomesOrcamentos());

        if (orcamentosList.isEmpty()) {
            detalhesArea.setText("Nenhum orçamento salvo ainda.\n\nUse a calculadora para criar e salvar orçamentos.");
        }
    }

    private void carregarDetalhesOrcamento(String numero) {
        Orcamento orcamento = FileManager.carregarOrcamento(numero);

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
        if (selecionado != null) {
            Orcamento orcamento = FileManager.carregarOrcamento(selecionado);
            if (orcamento != null) {
                // Abrir a calculadora com o orçamento carregado
                OrcamentoView orcamentoView = new OrcamentoView();
                orcamentoView.carregarOrcamento(orcamento);

                Scene scene = new Scene(orcamentoView, 950, 580);
                Stage stage = new Stage();
                stage.setTitle("Orçamento: " + selecionado);
                stage.setScene(scene);
                stage.show();
            }
        } else {
            mostrarAlerta("Selecione um orçamento para abrir.");
        }
    }

    private void deletarOrcamentoSelecionado() {
        String selecionado = listaOrcamentos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar exclusão");
            confirm.setHeaderText("Deletar orçamento");
            confirm.setContentText("Tem certeza que deseja deletar o orçamento " + selecionado + "?");

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                boolean deletado = FileManager.deletarOrcamento(selecionado);
                if (deletado) {
                    carregarListaOrcamentos();
                    detalhesArea.clear();
                    mostrarAlerta("Orçamento deletado com sucesso!");
                } else {
                    mostrarAlerta("Erro ao deletar orçamento.");
                }
            }
        } else {
            mostrarAlerta("Selecione um orçamento para deletar.");
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