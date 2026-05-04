package br.com.marmoraria.view;

import br.com.marmoraria.model.Material;
import br.com.marmoraria.service.MaterialAPI;
import br.com.marmoraria.service.MaterialService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CatalogoOnlineView extends BorderPane {

    private MaterialService materialService;
    private ListView<Material> listaMateriais;
    private TextArea detalhesArea;
    private ComboBox<String> cbFiltroTipo;
    private TextField txtBusca;
    private Label lblStatus;
    private ProgressIndicator progressIndicator;

    public CatalogoOnlineView() {
        materialService = new MaterialService();

        setPadding(new Insets(20));
        setStyle("-fx-background-color: #ecf0f1;");

        // Título
        HBox header = criarCabecalho();

        // Painel de filtros
        HBox filtros = criarFiltros();

        // Conteúdo principal
        HBox conteudo = criarConteudo();

        // Rodapé com status
        HBox footer = criarRodape();

        VBox mainContent = new VBox(15);
        mainContent.getChildren().addAll(header, filtros, conteudo, footer);

        setCenter(mainContent);

        // Carregar materiais
        carregarMateriais();

        // Iniciar thread para atualização periódica
        iniciarAtualizacaoAutomatica();
    }

    private HBox criarCabecalho() {
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        Label titulo = new Label("🌐 Catálogo Online de Materiais");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button btnAtualizar = new Button("🔄 Atualizar Agora");
        btnAtualizar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAtualizar.setOnAction(e -> atualizarMateriais());

        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(30, 30);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(titulo, spacer, btnAtualizar, progressIndicator);
        return header;
    }

    private HBox criarFiltros() {
        HBox filtros = new HBox(15);
        filtros.setAlignment(Pos.CENTER_LEFT);
        filtros.setPadding(new Insets(10, 0, 10, 0));

        Label lblFiltro = new Label("Filtrar por tipo:");
        lblFiltro.setStyle("-fx-font-weight: bold;");

        cbFiltroTipo = new ComboBox<>();
        cbFiltroTipo.getItems().add("Todos");
        cbFiltroTipo.getItems().addAll(MaterialAPI.getTiposDisponiveis());
        cbFiltroTipo.setValue("Todos");
        cbFiltroTipo.setPrefWidth(150);
        cbFiltroTipo.setOnAction(e -> filtrarMateriais());

        Label lblBusca = new Label("Buscar:");
        lblBusca.setStyle("-fx-font-weight: bold;");

        txtBusca = new TextField();
        txtBusca.setPromptText("Digite o nome do material...");
        txtBusca.setPrefWidth(250);
        txtBusca.textProperty().addListener((obs, oldVal, newVal) -> filtrarMateriais());

        filtros.getChildren().addAll(lblFiltro, cbFiltroTipo, lblBusca, txtBusca);
        return filtros;
    }

    private HBox criarConteudo() {
        HBox conteudo = new HBox(20);

        // Lista de materiais
        VBox painelLista = new VBox(10);
        painelLista.setPrefWidth(400);

        Label lblLista = new Label("📦 Materiais Disponíveis");
        lblLista.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        listaMateriais = new ListView<>();
        listaMateriais.setPrefHeight(450);
        listaMateriais.setStyle("-fx-background-color: white; -fx-background-radius: 8px;");
        listaMateriais.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mostrarDetalhesMaterial(newVal);
            }
        });

        painelLista.getChildren().addAll(lblLista, listaMateriais);

        // Detalhes do material
        VBox painelDetalhes = new VBox(10);
        painelDetalhes.setPrefWidth(450);

        Label lblDetalhes = new Label("📋 Detalhes do Material");
        lblDetalhes.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        detalhesArea = new TextArea();
        detalhesArea.setEditable(false);
        detalhesArea.setPrefHeight(450);
        detalhesArea.setStyle("-fx-font-family: monospace; -fx-font-size: 12px;");
        detalhesArea.setWrapText(true);

        painelDetalhes.getChildren().addAll(lblDetalhes, detalhesArea);

        conteudo.getChildren().addAll(painelLista, painelDetalhes);
        return conteudo;
    }

    private HBox criarRodape() {
        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(15, 0, 0, 0));

        lblStatus = new Label();
        lblStatus.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");

        atualizarStatus();

        footer.getChildren().add(lblStatus);
        return footer;
    }

    private void carregarMateriais() {
        List<Material> materiais = materialService.getTodosMateriais();
        listaMateriais.getItems().clear();
        listaMateriais.getItems().addAll(materiais);
        atualizarStatus();
    }

    private void filtrarMateriais() {
        String tipoSelecionado = cbFiltroTipo.getValue();
        String busca = txtBusca.getText().toLowerCase();

        List<Material> todos = materialService.getTodosMateriais();
        List<Material> filtrados = new ArrayList<>();

        for (Material m : todos) {
            boolean matchTipo = tipoSelecionado.equals("Todos") || m.getTipo().equals(tipoSelecionado);
            boolean matchBusca = busca.isEmpty() || m.getNome().toLowerCase().contains(busca);
            if (matchTipo && matchBusca) {
                filtrados.add(m);
            }
        }

        listaMateriais.getItems().clear();
        listaMateriais.getItems().addAll(filtrados);

        if (filtrados.isEmpty()) {
            detalhesArea.setText("Nenhum material encontrado com os filtros selecionados.");
        }
    }
    private void mostrarDetalhesMaterial(Material material) {
        StringBuilder sb = new StringBuilder();
        sb.append("═".repeat(50)).append("\n");
        sb.append(String.format("📦 %s\n", material.getNome()));
        sb.append("═".repeat(50)).append("\n\n");

        sb.append(String.format("🔖 Código: %s\n", material.getId()));
        sb.append(String.format("🏷️ Tipo: %s\n", material.getTipo()));
        sb.append(String.format("💰 Preço: R$ %.2f / m²\n", material.getPrecoPorMetroQuadrado()));
        sb.append(String.format("📏 Espessura: %.0f mm\n", material.getEspessura()));
        sb.append(String.format("🌍 Origem: %s\n", material.getOrigem()));
        sb.append("\n");
        sb.append(String.format("📝 Descrição:\n%s\n", material.getDescricao()));
        sb.append("\n");
        sb.append("═".repeat(50)).append("\n");

        // Informações adicionais
        sb.append("\n💡 DICA DE APLICAÇÃO:\n");
        if (material.getTipo().equals("Granito")) {
            sb.append("• Ideal para bancadas de cozinha\n");
            sb.append("• Alta resistência a impactos\n");
            sb.append("• Fácil manutenção\n");
        } else if (material.getTipo().equals("Mármore")) {
            sb.append("• Ideal para revestimentos e pisos\n");
            sb.append("• Sofisticação e elegância\n");
            sb.append("• Requer selagem periódica\n");
        } else if (material.getTipo().equals("Quartzo")) {
            sb.append("• Ideal para bancadas de cozinha\n");
            sb.append("• Alta resistência a manchas\n");
            sb.append("• Não requer selagem\n");
        }

        detalhesArea.setText(sb.toString());
    }

    private void atualizarMateriais() {
        // Mostrar progresso
        progressIndicator.setVisible(true);
        lblStatus.setText("🔄 Atualizando catálogo...");

        // Executar em thread separada
        new Thread(() -> {
            try {
                // Simular tempo de download
                Thread.sleep(1000);

                // Atualizar materiais
                materialService.atualizarMateriais();

                // Atualizar UI na thread principal
                Platform.runLater(() -> {
                    carregarMateriais();
                    filtrarMateriais();
                    progressIndicator.setVisible(false);
                    atualizarStatus();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Atualização Concluída");
                    alert.setHeaderText("✅ Catálogo atualizado!");
                    alert.setContentText("Os preços e materiais foram atualizados com sucesso.");
                    alert.showAndWait();
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText("❌ Falha na atualização");
                    alert.setContentText("Erro ao atualizar catálogo: " + e.getMessage());
                    alert.showAndWait();
                });
            }
        }).start();
    }

    private void atualizarStatus() {
        int total = materialService.getTodosMateriais().size();
        String ultimaAtualizacao = materialService.getInfoUltimaAtualizacao();
        lblStatus.setText(String.format("📊 Total de materiais: %d | Última atualização: %s", total, ultimaAtualizacao));
    }

    private void iniciarAtualizacaoAutomatica() {
        // Atualizar a cada 24 horas (simulado a cada 30 segundos para teste)
        new Thread(() -> {
            while (true) {
                try {
                    // Para teste: atualizar a cada 30 segundos
                    // Em produção: usar 24 horas = 24 * 60 * 60 * 1000
                    Thread.sleep(30 * 1000); // 30 segundos para teste

                    Platform.runLater(() -> {
                        System.out.println("🔄 Atualização automática do catálogo...");
                        atualizarMateriais();
                    });
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }
}
