package br.com.marmoraria.view;

import br.com.marmoraria.model.Material;
import br.com.marmoraria.service.CatalogoPrecosService;
import br.com.marmoraria.service.MaterialService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CatalogoOnlineView extends BorderPane {

    private MaterialService materialService;
    private ListView<Material> listaMateriais;
    private TextArea detalhesArea;
    private ComboBox<String> cbFiltroTipo;
    private ComboBox<String> cbOrdenacao;
    private TextField txtBusca;
    private Label lblStatus;
    private ProgressIndicator progressIndicator;

    // Controle para evitar atualização automática indesejada
    private boolean atualizacaoManual = false;

    public CatalogoOnlineView() {
        materialService = new MaterialService();

        setPadding(new Insets(20));
        setStyle("-fx-background-color: " +
                (br.com.marmoraria.util.TemaManager.isDarkMode() ? "#0F172A" : "#F0F4F8") + ";");

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

        // NÃO iniciar atualização automática mais
        // iniciarAtualizacaoAutomatica(); ← REMOVIDO
    }

    private HBox criarCabecalho() {
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        Label titulo = new Label("📚 Catálogo de Materiais");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " +
                (br.com.marmoraria.util.TemaManager.isDarkMode() ? "#F1F5F9" : "#1F2937") + ";");

        // Botão Atualizar
        Button btnAtualizar = new Button("🔄 Atualizar Catálogo");
        btnAtualizar.setStyle(
                "-fx-background-color: #2563EB; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-background-radius: 8px; " +
                        "-fx-padding: 10px 16px; -fx-cursor: hand;"
        );
        btnAtualizar.setOnAction(e -> {
            atualizacaoManual = true; // ← Marcar como manual
            atualizarMateriais();
        });

        // Botão Adicionar Material
        Button btnAdicionar = new Button("➕ Novo Material");
        btnAdicionar.setStyle(
                "-fx-background-color: #10B981; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-background-radius: 8px; " +
                        "-fx-padding: 10px 16px; -fx-cursor: hand;"
        );
        btnAdicionar.setOnAction(e -> abrirDialogoNovoMaterial());

        // Botão Importar CSV
        Button btnImportar = new Button("📥 Importar CSV");
        btnImportar.setStyle(
                "-fx-background-color: #7C3AED; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-background-radius: 8px; " +
                        "-fx-padding: 10px 16px; -fx-cursor: hand;"
        );
        btnImportar.setOnAction(e -> importarCSV());

        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(30, 30);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(titulo, spacer, btnImportar, btnAdicionar, btnAtualizar, progressIndicator);
        return header;
    }

    private HBox criarFiltros() {
        HBox filtros = new HBox(15);
        filtros.setAlignment(Pos.CENTER_LEFT);
        filtros.setPadding(new Insets(10));
        filtros.setStyle(
                "-fx-background-color: " +
                        (br.com.marmoraria.util.TemaManager.isDarkMode() ? "#1E293B" : "white") + "; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-border-color: " +
                        (br.com.marmoraria.util.TemaManager.isDarkMode() ? "#334155" : "#E5E7EB") + "; " +
                        "-fx-border-radius: 10px;"
        );

        // Filtro por tipo
        Label lblFiltro = new Label("Tipo:");
        lblFiltro.setStyle("-fx-font-weight: bold;");

        cbFiltroTipo = new ComboBox<>();
        cbFiltroTipo.getItems().add("Todos");
        cbFiltroTipo.getItems().addAll(materialService.getTiposMateriais());
        cbFiltroTipo.setValue("Todos");
        cbFiltroTipo.setPrefWidth(150);
        cbFiltroTipo.setOnAction(e -> filtrarMateriais());

        // Busca por nome
        Label lblBusca = new Label("Buscar:");
        lblBusca.setStyle("-fx-font-weight: bold;");

        txtBusca = new TextField();
        txtBusca.setPromptText("Digite o nome do material...");
        txtBusca.setPrefWidth(250);
        txtBusca.textProperty().addListener((obs, oldVal, newVal) -> filtrarMateriais());

        // NOVO: Ordenação por preço
        Label lblOrdenacao = new Label("Ordenar:");
        lblOrdenacao.setStyle("-fx-font-weight: bold;");

        cbOrdenacao = new ComboBox<>();
        cbOrdenacao.getItems().addAll(
                "Nome (A-Z)",
                "Nome (Z-A)",
                "Preço (Menor → Maior)",
                "Preço (Maior → Menor)",
                "Tipo"
        );
        cbOrdenacao.setValue("Nome (A-Z)");
        cbOrdenacao.setPrefWidth(180);
        cbOrdenacao.setOnAction(e -> filtrarMateriais());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        filtros.getChildren().addAll(lblFiltro, cbFiltroTipo, lblBusca, txtBusca,
                spacer, lblOrdenacao, cbOrdenacao);
        return filtros;
    }

    private HBox criarConteudo() {
        HBox conteudo = new HBox(20);

        // Lista de materiais
        VBox painelLista = new VBox(10);
        painelLista.setPrefWidth(450);

        Label lblLista = new Label("📦 Materiais (" + materialService.getTodosMateriais().size() + ")");
        lblLista.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        listaMateriais = new ListView<>();
        listaMateriais.setPrefHeight(450);
        listaMateriais.setStyle(
                "-fx-background-color: " +
                        (br.com.marmoraria.util.TemaManager.isDarkMode() ? "#1E293B" : "white") + "; " +
                        "-fx-background-radius: 8px;"
        );
        listaMateriais.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mostrarDetalhesMaterial(newVal);
            }
        });

        // Personalizar células da lista
        listaMateriais.setCellFactory(lv -> new ListCell<Material>() {
            @Override
            protected void updateItem(Material item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%-35s %-15s R$ %8.2f/m²",
                            item.getNome(), item.getTipo(), item.getPrecoPorMetroQuadrado()));
                    setStyle("-fx-font-family: 'Courier New', monospace; -fx-font-size: 12px;");
                }
            }
        });

        painelLista.getChildren().addAll(lblLista, listaMateriais);

        // Detalhes do material
        VBox painelDetalhes = new VBox(10);
        painelDetalhes.setPrefWidth(500);

        Label lblDetalhes = new Label("📋 Detalhes do Material");
        lblDetalhes.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        detalhesArea = new TextArea();
        detalhesArea.setEditable(false);
        detalhesArea.setPrefHeight(450);
        detalhesArea.setStyle(
                "-fx-font-family: 'Courier New', monospace; -fx-font-size: 12px; " +
                        "-fx-background-color: " +
                        (br.com.marmoraria.util.TemaManager.isDarkMode() ? "#1E293B" : "white") + ";"
        );
        detalhesArea.setWrapText(true);

        // Botões de ação no painel de detalhes
        HBox botoesDetalhes = new HBox(10);

        Button btnEditar = new Button("✏️ Editar Preço");
        btnEditar.setStyle(
                "-fx-background-color: #F59E0B; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-background-radius: 6px; " +
                        "-fx-padding: 8px 14px; -fx-cursor: hand;"
        );
        btnEditar.setOnAction(e -> editarPrecoMaterial());

        Button btnExcluir = new Button("🗑️ Remover");
        btnExcluir.setStyle(
                "-fx-background-color: #EF4444; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-background-radius: 6px; " +
                        "-fx-padding: 8px 14px; -fx-cursor: hand;"
        );
        btnExcluir.setOnAction(e -> removerMaterial());

        botoesDetalhes.getChildren().addAll(btnEditar, btnExcluir);
        botoesDetalhes.setAlignment(Pos.CENTER_RIGHT);

        painelDetalhes.getChildren().addAll(lblDetalhes, detalhesArea, botoesDetalhes);

        conteudo.getChildren().addAll(painelLista, painelDetalhes);
        return conteudo;
    }

    private HBox criarRodape() {
        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10, 0, 0, 0));

        lblStatus = new Label("Pronto");
        lblStatus.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 11px;");

        atualizarStatus();

        footer.getChildren().add(lblStatus);
        return footer;
    }

    private void carregarMateriais() {
        List<Material> materiais = materialService.getTodosMateriais();
        listaMateriais.getItems().clear();
        listaMateriais.getItems().addAll(materiais);

        // Aplicar ordenação padrão
        ordenarMateriais();
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

        // Aplicar ordenação
        ordenarMateriais();

        if (filtrados.isEmpty()) {
            detalhesArea.setText("Nenhum material encontrado com os filtros selecionados.");
        }

        atualizarStatus();
    }

    /**
     * NOVO: Ordena materiais conforme seleção
     */
    private void ordenarMateriais() {
        String ordenacao = cbOrdenacao.getValue();
        if (ordenacao == null) return;

        List<Material> materiais = new ArrayList<>(listaMateriais.getItems());

        switch (ordenacao) {
            case "Nome (A-Z)":
                materiais.sort(Comparator.comparing(Material::getNome));
                break;
            case "Nome (Z-A)":
                materiais.sort(Comparator.comparing(Material::getNome).reversed());
                break;
            case "Preço (Menor → Maior)":
                materiais.sort(Comparator.comparingDouble(Material::getPrecoPorMetroQuadrado));
                break;
            case "Preço (Maior → Menor)":
                materiais.sort(Comparator.comparingDouble(Material::getPrecoPorMetroQuadrado).reversed());
                break;
            case "Tipo":
                materiais.sort(Comparator.comparing(Material::getTipo)
                        .thenComparing(Material::getNome));
                break;
        }

        listaMateriais.getItems().clear();
        listaMateriais.getItems().addAll(materiais);
    }

    private void mostrarDetalhesMaterial(Material material) {
        StringBuilder sb = new StringBuilder();
        sb.append("═".repeat(55)).append("\n");
        sb.append(String.format("📦 %s\n", material.getNome()));
        sb.append("═".repeat(55)).append("\n\n");

        sb.append(String.format("🔖 Código: %s\n", material.getId()));
        sb.append(String.format("🏷️  Tipo: %s\n", material.getTipo()));
        sb.append(String.format("💰 Preço: R$ %.2f / m²\n", material.getPrecoPorMetroQuadrado()));
        sb.append(String.format("📏 Espessura: %.0f mm\n", material.getEspessura()));
        sb.append(String.format("🌍 Origem: %s\n", material.getOrigem()));
        sb.append("\n");
        sb.append(String.format("📝 Descrição:\n%s\n", material.getDescricao()));
        sb.append("\n");
        sb.append("═".repeat(55)).append("\n");

        // Cálculos rápidos
        sb.append("\n💡 CÁLCULOS RÁPIDOS:\n");
        sb.append(String.format("• 1 m² = R$ %.2f\n", material.getPrecoPorMetroQuadrado()));
        sb.append(String.format("• 2 m² = R$ %.2f\n", material.getPrecoPorMetroQuadrado() * 2));
        sb.append(String.format("• 5 m² = R$ %.2f\n", material.getPrecoPorMetroQuadrado() * 5));
        sb.append(String.format("• 10 m² = R$ %.2f\n", material.getPrecoPorMetroQuadrado() * 10));

        // Dica de aplicação
        sb.append("\n💡 DICA DE APLICAÇÃO:\n");
        if (material.getTipo().equals("Granito")) {
            sb.append("• Ideal para bancadas de cozinha\n");
            sb.append("• Alta resistência a impactos\n");
            sb.append("• Fácil manutenção\n");
        } else if (material.getTipo().equals("Mármore")) {
            sb.append("• Ideal para revestimentos e pisos\n");
            sb.append("• Sofisticação e elegância\n");
            sb.append("• Requer selagem periódica\n");
        } else if (material.getTipo().equals("Quartzo") || material.getTipo().equals("Silestone")) {
            sb.append("• Ideal para bancadas de cozinha\n");
            sb.append("• Alta resistência a manchas\n");
            sb.append("• Não requer selagem\n");
        }

        detalhesArea.setText(sb.toString());
    }

    // ==================== CORRIGIDO: Atualização ====================

    private void atualizarMateriais() {
        progressIndicator.setVisible(true);
        lblStatus.setText("🔄 Atualizando catálogo...");

        new Thread(() -> {
            try {
                Thread.sleep(500);
                materialService.atualizarMateriais();

                Platform.runLater(() -> {
                    carregarMateriais();
                    filtrarMateriais();
                    progressIndicator.setVisible(false);
                    atualizarStatus();

                    // SÓ mostra pop-up se foi atualização MANUAL
                    if (atualizacaoManual) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Catálogo Atualizado");
                        alert.setHeaderText("✅ Sucesso!");
                        alert.setContentText(
                                "Catálogo atualizado com sucesso!\n\n" +
                                        "Total de materiais: " + materialService.getTodosMateriais().size()
                        );
                        alert.showAndWait();
                        atualizacaoManual = false; // Resetar flag
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    atualizacaoManual = false;

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText("❌ Falha na atualização");
                    alert.setContentText("Erro: " + e.getMessage());
                    alert.showAndWait();
                });
            }
        }).start();
    }

    // ==================== NOVO: Adicionar Material Manualmente ====================

    private void abrirDialogoNovoMaterial() {
        Stage dialog = new Stage();
        dialog.setTitle("➕ Adicionar Novo Material");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");

        Label titulo = new Label("Cadastrar Novo Material");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Campos
        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do material");

        ComboBox<String> cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll("Granito", "Mármore", "Quartzo", "Silestone",
                "Dekton", "Ultracompact", "Especial", "Outro");
        cbTipo.setPromptText("Tipo");
        cbTipo.setMaxWidth(Double.MAX_VALUE);

        TextField txtPreco = new TextField();
        txtPreco.setPromptText("Preço por m² (R$)");

        TextField txtOrigem = new TextField();
        txtOrigem.setPromptText("Origem (ex: Brasil, Itália)");
        txtOrigem.setText("Brasil");

        TextField txtEspessura = new TextField();
        txtEspessura.setPromptText("Espessura (mm)");
        txtEspessura.setText("20");

        TextArea txtDescricao = new TextArea();
        txtDescricao.setPromptText("Descrição do material...");
        txtDescricao.setPrefRowCount(3);

        // Botões
        HBox botoes = new HBox(10);
        botoes.setAlignment(Pos.CENTER_RIGHT);

        Button btnSalvar = new Button("💾 Salvar");
        btnSalvar.setStyle(
                "-fx-background-color: #10B981; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-padding: 10px 20px; " +
                        "-fx-background-radius: 8px; -fx-cursor: hand;"
        );

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle(
                "-fx-background-color: #6B7280; -fx-text-fill: white; " +
                        "-fx-padding: 10px 20px; -fx-background-radius: 8px; -fx-cursor: hand;"
        );

        botoes.getChildren().addAll(btnCancelar, btnSalvar);

        root.getChildren().addAll(titulo,
                new Label("Nome:"), txtNome,
                new Label("Tipo:"), cbTipo,
                new Label("Preço por m² (R$):"), txtPreco,
                new Label("Origem:"), txtOrigem,
                new Label("Espessura (mm):"), txtEspessura,
                new Label("Descrição:"), txtDescricao,
                botoes
        );

        // Ações
        btnCancelar.setOnAction(e -> dialog.close());

        btnSalvar.setOnAction(e -> {
            try {
                String nome = txtNome.getText().trim();
                String tipo = cbTipo.getValue();
                double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
                String origem = txtOrigem.getText().trim();
                double espessura = Double.parseDouble(txtEspessura.getText());
                String descricao = txtDescricao.getText().trim();

                if (nome.isEmpty() || tipo == null) {
                    throw new IllegalArgumentException("Nome e tipo são obrigatórios");
                }

                // Salvar no arquivo de preços locais
                salvarMaterialNoArquivo(nome, tipo, preco, origem, espessura, descricao);

                // Recarregar catálogo
                atualizacaoManual = true;
                atualizarMateriais();

                dialog.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText("✅ Material adicionado!");
                alert.setContentText(nome + " foi adicionado ao catálogo.");
                alert.showAndWait();

            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("❌ Dados inválidos");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });

        Scene scene = new Scene(root, 450, 500);
        dialog.setScene(scene);
        dialog.show();
    }

    /**
     * Salva material no arquivo CSV local
     */
    private void salvarMaterialNoArquivo(String nome, String tipo, double preco,
                                         String origem, double espessura, String descricao) {
        try {
            File arquivo = new File("precos_locais.csv");
            boolean arquivoExiste = arquivo.exists();

            try (FileWriter fw = new FileWriter(arquivo, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {

                // Cabeçalho se for novo
                if (!arquivoExiste) {
                    out.println("nome,tipo,preco,origem,espessura,descricao");
                }

                // Adicionar material
                out.printf("\"%s\",\"%s\",%.2f,\"%s\",%.0f,\"%s\"%n",
                        nome, tipo, preco, origem, espessura, descricao);
            }

            System.out.println("✅ Material salvo no arquivo: " + nome);

        } catch (IOException e) {
            System.err.println("Erro ao salvar material: " + e.getMessage());
        }
    }

    /**
     * Importa materiais de arquivo CSV
     */
    private void importarCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar Materiais CSV");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Arquivos CSV", "*.csv")
        );

        File arquivo = fileChooser.showOpenDialog(getScene().getWindow());
        if (arquivo == null) return;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            int importados = 0;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                // Pular cabeçalho
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                // Formato: nome,tipo,preco,origem,espessura,descricao
                String[] partes = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (partes.length >= 4) {
                    String nome = partes[0].replace("\"", "").trim();
                    String tipo = partes[1].replace("\"", "").trim();
                    double preco = Double.parseDouble(partes[2].replace("\"", "").replace(",", ".").trim());
                    String origem = partes.length > 3 ? partes[3].replace("\"", "").trim() : "Brasil";
                    double espessura = partes.length > 4 ?
                            Double.parseDouble(partes[4].replace("\"", "").trim()) : 20;
                    String descricao = partes.length > 5 ? partes[5].replace("\"", "").trim() : "";

                    salvarMaterialNoArquivo(nome, tipo, preco, origem, espessura, descricao);
                    importados++;
                }
            }

            atualizacaoManual = true;
            atualizarMateriais();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Importação Concluída");
            alert.setHeaderText("✅ Sucesso!");
            alert.setContentText(importados + " materiais importados com sucesso!");
            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("❌ Erro na importação");
            alert.setContentText("Erro: " + e.getMessage() +
                    "\n\nFormato esperado: nome,tipo,preco,origem,espessura,descricao");
            alert.showAndWait();
        }
    }

    /**
     * Edita o preço de um material selecionado
     */
    private void editarPrecoMaterial() {
        Material selecionado = listaMateriais.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Selecione um material");
            alert.setContentText("Selecione um material na lista para editar.");
            alert.showAndWait();
            return;
        }

        TextInputDialog dialog = new TextInputDialog(
                String.format("%.2f", selecionado.getPrecoPorMetroQuadrado()));
        dialog.setTitle("Editar Preço");
        dialog.setHeaderText("Editar preço de: " + selecionado.getNome());
        dialog.setContentText("Novo preço por m² (R$):");

        dialog.showAndWait().ifPresent(valor -> {
            try {
                double novoPreco = Double.parseDouble(valor.replace(",", "."));
                selecionado.setPrecoPorMetroQuadrado(novoPreco);

                // Salvar no arquivo
                salvarMaterialNoArquivo(
                        selecionado.getNome(),
                        selecionado.getTipo(),
                        novoPreco,
                        selecionado.getOrigem(),
                        selecionado.getEspessura(),
                        selecionado.getDescricao()
                );

                listaMateriais.refresh();
                mostrarDetalhesMaterial(selecionado);

            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setContentText("Valor inválido!");
                alert.showAndWait();
            }
        });
    }

    /**
     * Remove um material da lista
     */
    private void removerMaterial() {
        Material selecionado = listaMateriais.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Selecione um material");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar Remoção");
        confirm.setHeaderText("Remover material?");
        confirm.setContentText("Deseja remover \"" + selecionado.getNome() + "\" do catálogo?");

        confirm.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                listaMateriais.getItems().remove(selecionado);
                detalhesArea.clear();
                atualizarStatus();
            }
        });
    }

    private void atualizarStatus() {
        int total = materialService.getTodosMateriais().size();
        int exibidos = listaMateriais.getItems().size();
        lblStatus.setText(String.format(
                "📊 Exibindo %d de %d materiais | Clique em '➕ Novo Material' para adicionar",
                exibidos, total
        ));
    }
}