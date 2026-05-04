package br.com.marmoraria.view;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Orcamento;
import br.com.marmoraria.util.FileManager;
import br.com.marmoraria.util.GeradorPDF;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class GestaoOrcamentosView extends BorderPane {

    private ListView<String> listaOrcamentos;
    private TextArea detalhesArea;

    public GestaoOrcamentosView() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #eef2f7;");

        VBox cabecalho = criarCabecalho();
        VBox painelLista = criarPainelLista();
        VBox painelInfo = criarPainelInfo();
        HBox botoes = criarBotoes();

        HBox conteudo = new HBox(20, painelLista, painelInfo);
        HBox.setHgrow(painelInfo, Priority.ALWAYS);

        setTop(cabecalho);
        setCenter(conteudo);
        setBottom(botoes);

        carregarListaOrcamentos();
    }

    private VBox criarCabecalho() {
        VBox box = new VBox(4);
        box.setPadding(new Insets(0, 0, 16, 0));

        Label titulo = new Label("Gestao de orcamentos");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        Label subtitulo = new Label("Abra, revise, exporte e remova orcamentos salvos com uma leitura mais clara dos detalhes.");
        subtitulo.setStyle("-fx-font-size: 12px; -fx-text-fill: #6b7280;");
        subtitulo.setWrapText(true);

        box.getChildren().addAll(titulo, subtitulo);
        return box;
    }

    private VBox criarPainelLista() {
        VBox box = criarCard();
        box.setPrefWidth(360);

        Label label = criarTituloSecao("Orcamentos salvos", "Selecione um item para visualizar os detalhes.");

        listaOrcamentos = new ListView<>();
        listaOrcamentos.setPrefHeight(460);
        listaOrcamentos.setPlaceholder(new Label("Nenhum orcamento salvo."));
        listaOrcamentos.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #e5e7eb; -fx-border-radius: 8;");
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
        VBox box = criarCard();

        Label label = criarTituloSecao("Detalhes", "Resumo textual do orcamento selecionado.");

        detalhesArea = new TextArea();
        detalhesArea.setEditable(false);
        detalhesArea.setPrefHeight(460);
        detalhesArea.setWrapText(true);
        detalhesArea.setStyle("-fx-font-family: monospace; -fx-font-size: 12px; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #e5e7eb;");

        VBox.setVgrow(detalhesArea, Priority.ALWAYS);
        box.getChildren().addAll(label, detalhesArea);
        return box;
    }

    private HBox criarBotoes() {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(18, 0, 0, 0));

        Button btnAbrir = criarBotao("Abrir", "#2563eb");
        btnAbrir.setOnAction(e -> abrirOrcamentoSelecionado());

        Button btnGerarPDF = criarBotao("Gerar PDF", "#ea580c");
        btnGerarPDF.setOnAction(e -> gerarPDFDoOrcamentoSelecionado());

        Button btnDeletar = criarBotao("Deletar", "#dc2626");
        btnDeletar.setOnAction(e -> deletarOrcamentoSelecionado());

        Button btnAtualizar = criarBotao("Atualizar", "#475569");
        btnAtualizar.setOnAction(e -> carregarListaOrcamentos());

        Button btnFechar = criarBotao("Fechar", "#64748b");
        btnFechar.setOnAction(e -> ((Stage) getScene().getWindow()).close());

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        box.getChildren().addAll(btnAbrir, btnGerarPDF, btnDeletar, btnAtualizar, espacador, btnFechar);
        return box;
    }

    private void carregarListaOrcamentos() {
        listaOrcamentos.getItems().clear();
        List<Orcamento> orcamentos = FileManager.listarOrcamentos();

        if (orcamentos.isEmpty()) {
            detalhesArea.setText("Nenhum orcamento salvo ainda.");
            return;
        }

        for (Orcamento orc : orcamentos) {
            String info = String.format("%s | %s | R$ %.2f | %s",
                    orc.getNumeroOrcamento(),
                    orc.getDataFormatada(),
                    orc.getTotalFinal(),
                    orc.getStatus());
            listaOrcamentos.getItems().add(info);
        }
    }

    private void carregarDetalhesOrcamento(String itemSelecionado) {
        String numeroOrcamento = itemSelecionado.split(" \\| ")[0];
        Orcamento orcamento = FileManager.carregarOrcamento(numeroOrcamento);

        if (orcamento == null) {
            detalhesArea.setText("Erro ao carregar orcamento.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(56)).append("\n");
        sb.append("ORCAMENTO: ").append(orcamento.getNumeroOrcamento()).append('\n');
        sb.append("DATA: ").append(orcamento.getDataFormatada()).append('\n');
        sb.append("STATUS: ").append(orcamento.getStatus()).append('\n');
        sb.append("=".repeat(56)).append("\n\n");
        sb.append("ITENS\n");
        sb.append("-".repeat(56)).append('\n');

        for (int i = 0; i < orcamento.getItens().size(); i++) {
            ItemOrcamento item = orcamento.getItens().get(i);
            sb.append(i + 1).append(". ").append(item.getMaterial().getNome()).append('\n');
            sb.append("   Tipo: ").append(item.getTipoTrabalho().getDescricao()).append('\n');
            sb.append("   Descricao: ").append(item.getDescricao()).append('\n');
            sb.append(String.format("   Dimensoes: %.0f x %.0f mm%n", item.getLargura(), item.getComprimento()));
            sb.append(String.format("   Quantidade: %d%n", item.getQuantidade()));
            sb.append(String.format("   Area: %.2f m2%n", item.getArea()));
            sb.append(String.format("   Total: R$ %.2f%n%n", item.getTotal()));
        }

        sb.append("=".repeat(56)).append('\n');
        sb.append(String.format("SUBTOTAL ITENS:      R$ %.2f%n", orcamento.getValorTotal()));
        sb.append(String.format("SUBTOTAL COM MARGEM: R$ %.2f%n", orcamento.getValorComLucro()));
        sb.append(String.format("MAO DE OBRA:         R$ %.2f%n", orcamento.getValorMaoDeObra()));
        sb.append(String.format("FRETE:               R$ %.2f%n", orcamento.getFrete()));
        sb.append(String.format("CREDITO:             R$ %.2f%n", orcamento.getCredito()));
        sb.append(String.format("TOTAL FINAL:         R$ %.2f%n", orcamento.getTotalFinal()));
        sb.append("=".repeat(56)).append('\n');

        if (orcamento.getObservacoes() != null && !orcamento.getObservacoes().isEmpty()) {
            sb.append("\nOBSERVACOES\n");
            sb.append("-".repeat(56)).append('\n');
            sb.append(orcamento.getObservacoes()).append('\n');
        }

        detalhesArea.setText(sb.toString());
    }

    private void abrirOrcamentoSelecionado() {
        String selecionado = listaOrcamentos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um orcamento para abrir.");
            return;
        }

        String numeroOrcamento = selecionado.split(" \\| ")[0];
        Orcamento orcamento = FileManager.carregarOrcamento(numeroOrcamento);
        if (orcamento == null) {
            mostrarAlerta("Erro ao carregar orcamento.");
            return;
        }

        OrcamentoView orcamentoView = new OrcamentoView();
        orcamentoView.carregarOrcamento(orcamento);

        Scene scene = new Scene(orcamentoView, 1250, 760);
        Stage stage = new Stage();
        stage.setTitle("Orcamento: " + numeroOrcamento);
        stage.setScene(scene);
        stage.show();
    }

    private void gerarPDFDoOrcamentoSelecionado() {
        String selecionado = listaOrcamentos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um orcamento para gerar PDF.");
            return;
        }

        String numeroOrcamento = selecionado.split(" \\| ")[0];
        Orcamento orcamento = FileManager.carregarOrcamento(numeroOrcamento);

        if (orcamento != null && GeradorPDF.gerarOrcamentoPDF(orcamento)) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("PDF gerado");
            confirm.setHeaderText("Orcamento gerado com sucesso");
            confirm.setContentText("Deseja abrir o PDF agora?");

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                GeradorPDF.abrirPDF(numeroOrcamento);
            }
        } else {
            mostrarAlerta("Erro ao gerar PDF.");
        }
    }

    private void deletarOrcamentoSelecionado() {
        String selecionado = listaOrcamentos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um orcamento para deletar.");
            return;
        }

        String numeroOrcamento = selecionado.split(" \\| ")[0];
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar exclusao");
        confirm.setHeaderText("Deletar orcamento");
        confirm.setContentText("Tem certeza que deseja deletar o orcamento " + numeroOrcamento + "?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            boolean deletado = FileManager.deletarOrcamento(numeroOrcamento);
            if (deletado) {
                carregarListaOrcamentos();
                detalhesArea.clear();
                mostrarAlerta("Orcamento deletado com sucesso.");
            } else {
                mostrarAlerta("Erro ao deletar orcamento.");
            }
        }
    }

    private VBox criarCard() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(14));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(15,23,42,0.06), 8, 0, 0, 2);");
        return box;
    }

    private Label criarTituloSecao(String titulo, String descricao) {
        Label label = new Label(titulo + "\n" + descricao);
        label.setWrapText(true);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #111827;");
        return label;
    }

    private Button criarBotao(String texto, String cor) {
        Button btn = new Button(texto);
        btn.setStyle("-fx-background-color: " + cor + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 14;");
        return btn;
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacao");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
