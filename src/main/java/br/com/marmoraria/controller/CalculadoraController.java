package br.com.marmoraria.controller;

import br.com.marmoraria.model.*;
import br.com.marmoraria.service.MaterialService;
import br.com.marmoraria.service.OrcamentoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.DecimalFormat;

public class CalculadoraController {

    // ===== FXML =====
    @FXML private ComboBox<String> cbTipoMaterial;
    @FXML private ComboBox<Material> cbMaterial;
    @FXML private ComboBox<String> cbCategoriaServico;
    @FXML private ComboBox<Servico> cbServico;

    @FXML private TextField txtLargura;
    @FXML private TextField txtComprimento;
    @FXML private TextField txtQuantidade;

    @FXML private TableView<ItemOrcamento> tabelaOrcamento;
    @FXML private TableColumn<ItemOrcamento, String> colMaterial;
    @FXML private TableColumn<ItemOrcamento, String> colServico;
    @FXML private TableColumn<ItemOrcamento, String> colDimensoes;
    @FXML private TableColumn<ItemOrcamento, Integer> colQuantidade;
    @FXML private TableColumn<ItemOrcamento, Double> colArea;
    @FXML private TableColumn<ItemOrcamento, Double> colTotal;

    @FXML private Label lblTotalOrcamento;

    // ===== NEGÓCIO =====
    private final MaterialService materialService = new MaterialService();
    private final OrcamentoService orcamentoService = new OrcamentoService();
    private final Orcamento orcamentoAtual = new Orcamento(); // construtor vazio
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    // ===== INIT =====
    @FXML
    public void initialize() {
        cbMaterial.setCellFactory(cb -> new ListCell<Material>() {
            @Override
            protected void updateItem(Material item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(
                            item.getNome() +
                                    " | R$ " +
                                    String.format("%.2f", item.getPrecoPorMetroQuadrado()) +
                                    " / m²"
                    );
                }
            }
        });

// texto exibido quando o ComboBox está fechado
        cbMaterial.setButtonCell(cbMaterial.getCellFactory().call(null));

        configurarCombos();
        configurarCampos();
        configurarTabela();
        atualizarTotal();
    }

    // ===== COMBOS =====
    private void configurarCombos() {

        cbTipoMaterial.setItems(
                FXCollections.observableArrayList(materialService.getTiposMateriais())
        );
        cbTipoMaterial.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        cbMaterial.setItems(
                                FXCollections.observableArrayList(
                                        materialService.getMateriaisPorTipo(newVal)
                                )
                        );
                    }
                }
        );

        cbCategoriaServico.setItems(
                FXCollections.observableArrayList(materialService.getCategoriasServicos())
        );
        cbCategoriaServico.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        cbServico.setItems(
                                FXCollections.observableArrayList(
                                        materialService.getServicosPorCategoria(newVal)
                                )
                        );
                    }
                }
        );

        if (!cbTipoMaterial.getItems().isEmpty()) {
            cbTipoMaterial.getSelectionModel().selectFirst();
        }

        if (!cbCategoriaServico.getItems().isEmpty()) {
            cbCategoriaServico.getSelectionModel().selectFirst();
        }
    }

    // ===== VALIDADORES =====
    private void configurarCampos() {
        permitirNumeros(txtLargura);
        permitirNumeros(txtComprimento);
        permitirInteiro(txtQuantidade);
    }

    private void permitirNumeros(TextField tf) {
        tf.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*(\\.\\d*)?")) {
                tf.setText(oldV);
            }
        });
    }

    private void permitirInteiro(TextField tf) {
        tf.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*")) {
                tf.setText(oldV);
            }
        });
    }

    // ===== TABELA =====
    private void configurarTabela() {

        colMaterial.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getMaterial().getNome())
        );

        colServico.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getServico().getDescricao())
        );

        colDimensoes.setCellValueFactory(c ->
                new SimpleStringProperty(
                        String.format("%.0f x %.0f",
                                c.getValue().getLarguraMm(),
                                c.getValue().getComprimentoMm())
                )
        );

        colQuantidade.setCellValueFactory(
                new PropertyValueFactory<>("quantidade")
        );
        colArea.setCellValueFactory(
                new PropertyValueFactory<>("areaM2")
        );
        colTotal.setCellValueFactory(
                new PropertyValueFactory<>("valorTotal")
        );

        tabelaOrcamento.setItems(
                FXCollections.observableList(orcamentoAtual.getItens())
        );
    }

    // ===== AÇÃO =====
    @FXML
    private void adicionarItem() {
        try {
            ItemOrcamento item = new ItemOrcamento(
                    cbMaterial.getValue(),
                    cbServico.getValue(),
                    Integer.parseInt(txtQuantidade.getText()),
                    Double.parseDouble(txtLargura.getText()),
                    Double.parseDouble(txtComprimento.getText()),
                    ""
            );

            orcamentoService.adicionarItem(orcamentoAtual, item);
            tabelaOrcamento.refresh();
            atualizarTotal();
            limparCampos();

        } catch (Exception e) {
            mostrarAlerta("Erro", "Preencha todos os campos corretamente.");
        }
    }

    // ===== TOTAL =====
    private void atualizarTotal() {
        lblTotalOrcamento.setText(
                "R$ " + df.format(
                        orcamentoService.calcularValorTotal(orcamentoAtual)
                )
        );
    }

    private void limparCampos() {
        txtLargura.clear();
        txtComprimento.clear();
        txtQuantidade.clear();
    }

    // ===== ALERTA =====
    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(titulo);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
