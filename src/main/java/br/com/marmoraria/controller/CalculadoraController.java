package br.com.marmoraria.controller;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Material;
import br.com.marmoraria.model.Servico;
import br.com.marmoraria.service.MaterialService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CalculadoraController {

    // ================= FXML =================

    @FXML
    private ComboBox<Material> cbMaterial;

    @FXML
    private ComboBox<Servico> cbServico;

    @FXML
    private TextField txtLargura;

    @FXML
    private TextField txtComprimento;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private TableView<ItemOrcamento> tabela;

    @FXML
    private TableColumn<ItemOrcamento, Double> colArea;

    @FXML
    private TableColumn<ItemOrcamento, Double> colTotal;

    @FXML
    private TableColumn<ItemOrcamento, Void> colAcao;

    @FXML
    private Label lblTotalGeral;

    // ================= SERVICES =================

    private final MaterialService materialService = new MaterialService();

    // ================= DADOS =================

    private final ObservableList<ItemOrcamento> itens = FXCollections.observableArrayList();

    private ItemOrcamento itemEmEdicao = null;

    // ================= INITIALIZE =================

    @FXML
    public void initialize() {

        // ComboBox
        cbMaterial.setItems(FXCollections.observableArrayList(
                materialService.getTodosMateriais()
        ));

        cbServico.setItems(FXCollections.observableArrayList(
                materialService.getTodosServicos()
        ));

        // Tabela
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tabela.setItems(itens);

        configurarBotaoRemover();
        configurarEdicaoPorDuploClique();

        atualizarTotalGeral();
    }

    // ================= ADICIONAR / EDITAR =================

    @FXML
    private void adicionarItem() {

        try {

            Material material = cbMaterial.getValue();
            Servico servico = cbServico.getValue();

            if (material == null) {
                mostrarErro("Selecione um material.");
                return;
            }

            double largura = Double.parseDouble(txtLargura.getText());
            double comprimento = Double.parseDouble(txtComprimento.getText());
            int quantidade = Integer.parseInt(txtQuantidade.getText());

            double area = (largura * comprimento) / 1_000_000.0;
            double total = area * material.getPrecoPorMetroQuadrado() * quantidade;

            ItemOrcamento novoItem = new ItemOrcamento(
                    material,
                    servico,
                    largura,
                    comprimento,
                    quantidade,
                    area,
                    total
            );

            if (itemEmEdicao != null) {

                int index = itens.indexOf(itemEmEdicao);
                itens.set(index, novoItem);
                itemEmEdicao = null;

            } else {

                itens.add(novoItem);
            }

            atualizarTotalGeral();
            limparCampos();

        } catch (Exception e) {
            mostrarErro("Erro ao adicionar/editar item.");
        }
    }

    // ================= REMOVER =================

    private void configurarBotaoRemover() {

        colAcao.setCellFactory(param -> new TableCell<>() {

            private final Button btn = new Button("Excluir");

            {
                btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                btn.setOnAction(event -> {
                    ItemOrcamento item = getTableView().getItems().get(getIndex());

                    itens.remove(item);
                    atualizarTotalGeral();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    // ================= EDIÇÃO =================

    private void configurarEdicaoPorDuploClique() {

        tabela.setRowFactory(tv -> {
            TableRow<ItemOrcamento> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {

                    ItemOrcamento item = row.getItem();
                    carregarItemParaEdicao(item);
                }
            });

            return row;
        });
    }

    private void carregarItemParaEdicao(ItemOrcamento item) {

        itemEmEdicao = item;

        cbMaterial.setValue(item.getMaterial());
        cbServico.setValue(item.getServico());

        txtLargura.setText(String.valueOf(item.getLargura()));
        txtComprimento.setText(String.valueOf(item.getComprimento()));
        txtQuantidade.setText(String.valueOf(item.getQuantidade()));
    }

    // ================= TOTAL =================

    private void atualizarTotalGeral() {

        double total = 0;

        for (ItemOrcamento item : itens) {
            total += item.getTotal();
        }

        lblTotalGeral.setText(String.format("TOTAL: R$ %.2f", total));
    }

    // ================= UTIL =================

    private void limparCampos() {
        txtLargura.clear();
        txtComprimento.clear();
        txtQuantidade.clear();
        itemEmEdicao = null;
    }

    private void mostrarErro(String mensagem) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);

        alert.showAndWait();
    }
}