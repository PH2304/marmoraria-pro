package br.com.marmoraria.controller;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Material;
import br.com.marmoraria.model.Servico;
import br.com.marmoraria.service.MaterialService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CalculadoraController {

    @FXML private ComboBox<Material> cbMaterial;
    @FXML private ComboBox<Servico> cbServico;

    @FXML private TextField txtLargura;
    @FXML private TextField txtComprimento;
    @FXML private TextField txtQuantidade;

    @FXML private TableView<ItemOrcamento> tabela;
    @FXML private TableColumn<ItemOrcamento, Double> colArea;
    @FXML private TableColumn<ItemOrcamento, Double> colTotal;

    private final MaterialService materialService = new MaterialService();

    @FXML
    public void initialize() {

        cbMaterial.getItems().addAll(materialService.listarMateriais());
        cbServico.getItems().addAll(materialService.listarServicos());

        colArea.setCellValueFactory(
                new PropertyValueFactory<>("areaM2")
        );
        colTotal.setCellValueFactory(
                new PropertyValueFactory<>("valorTotal")
        );
    }

    @FXML
    private void adicionarItem() {

        ItemOrcamento item = new ItemOrcamento();
        item.setMaterial(cbMaterial.getValue());
        item.setServico(cbServico.getValue());
        item.setLarguraMm(Double.parseDouble(txtLargura.getText()));
        item.setComprimentoMm(Double.parseDouble(txtComprimento.getText()));
        item.setQuantidade(Integer.parseInt(txtQuantidade.getText()));

        item.calcular();
        tabela.getItems().add(item);
    }
}
