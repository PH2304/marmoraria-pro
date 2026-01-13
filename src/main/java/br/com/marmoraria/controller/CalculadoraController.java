package br.com.marmoraria.controller;

import br.com.marmoraria.model.Material;
import br.com.marmoraria.model.Servico;
import br.com.marmoraria.service.CalculadoraService;
import br.com.marmoraria.service.MaterialService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import java.text.DecimalFormat;

public class CalculadoraController {

    @FXML private ComboBox<String> cbTipoMaterial;
    @FXML private ComboBox<Material> cbMaterial;
    @FXML private ComboBox<String> cbCategoriaServico;
    @FXML private ComboBox<Servico> cbServico;

    @FXML private TextField tfLargura;
    @FXML private TextField tfComprimento;
    @FXML private TextField tfQuantidade;
    @FXML private TextField tfMetragemLinear;
    @FXML private TextField tfPerimetro;

    @FXML private Label lblAreaResultado;
    @FXML private Label lblCustoMaterialResultado;
    @FXML private Label lblCustoServicoResultado;
    @FXML private Label lblTotalResultado;

    @FXML private TextArea taObservacoes;

    private MaterialService materialService;
    private DecimalFormat df;

    public void initialize() {
        materialService = new MaterialService();
        df = new DecimalFormat("#,##0.00");

        // Configurar ComboBox de tipos de material
        cbTipoMaterial.setItems(FXCollections.observableArrayList(
                materialService.getTiposMateriais()
        ));

        cbTipoMaterial.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        atualizarMateriais(newValue);
                    }
                }
        );

        // Configurar ComboBox de categorias de serviço
        cbCategoriaServico.setItems(FXCollections.observableArrayList(
                materialService.getCategoriasServicos()
        ));

        cbCategoriaServico.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        atualizarServicos(newValue);
                    }
                }
        );

        // Configurar campos numéricos
        configurarCampoNumerico(tfLargura);
        configurarCampoNumerico(tfComprimento);
        configurarCampoNumerico(tfQuantidade);

        // Configurar listeners para cálculo automático
        tfLargura.textProperty().addListener((obs, oldVal, newVal) -> calcular());
        tfComprimento.textProperty().addListener((obs, oldVal, newVal) -> calcular());
        tfQuantidade.textProperty().addListener((obs, oldVal, newVal) -> calcular());
        cbMaterial.valueProperty().addListener((obs, oldVal, newVal) -> calcular());
        cbServico.valueProperty().addListener((obs, oldVal, newVal) -> calcular());

        // Selecionar primeiros valores
        if (!materialService.getTiposMateriais().isEmpty()) {
            cbTipoMaterial.getSelectionModel().selectFirst();
        }
        if (!materialService.getCategoriasServicos().isEmpty()) {
            cbCategoriaServico.getSelectionModel().selectFirst();
        }
    }

    private void atualizarMateriais(String tipo) {
        cbMaterial.setItems(FXCollections.observableArrayList(
                materialService.getMateriaisPorTipo(tipo)
        ));
        if (!cbMaterial.getItems().isEmpty()) {
            cbMaterial.getSelectionModel().selectFirst();
        }
    }

    private void atualizarServicos(String categoria) {
        cbServico.setItems(FXCollections.observableArrayList(
                materialService.getServicosPorCategoria(categoria)
        ));
        if (!cbServico.getItems().isEmpty()) {
            cbServico.getSelectionModel().selectFirst();
        }
    }

    private void configurarCampoNumerico(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                textField.setText(oldValue);
            }
        });
    }

    @FXML
    private void calcular() {
        try {
            double largura = Double.parseDouble(tfLargura.getText());
            double comprimento = Double.parseDouble(tfComprimento.getText());
            int quantidade = Integer.parseInt(tfQuantidade.getText());

            Material material = cbMaterial.getValue();
            Servico servico = cbServico.getValue();

            // Calcular área
            double area = CalculadoraService.calcularArea(largura, comprimento, quantidade);
            lblAreaResultado.setText(df.format(area) + " m²");

            // Calcular metragem linear
            double metragemLinear = CalculadoraService.calcularMetragemLinear(comprimento, quantidade);
            tfMetragemLinear.setText(df.format(metragemLinear) + " m");

            // Calcular perímetro
            double perimetro = CalculadoraService.calcularPerimetro(largura, comprimento, quantidade);
            tfPerimetro.setText(df.format(perimetro) + " m");

            // Calcular custos
            double custoMaterial = CalculadoraService.calcularCustoMaterial(material, area);
            double custoServico = CalculadoraService.calcularCustoServico(servico, quantidade);
            double total = custoMaterial + custoServico;

            lblCustoMaterialResultado.setText("R$ " + df.format(custoMaterial));
            lblCustoServicoResultado.setText("R$ " + df.format(custoServico));
            lblTotalResultado.setText("R$ " + df.format(total));

        } catch (NumberFormatException e) {
            // Campos não preenchidos ainda, apenas ignore
        }
    }

    @FXML
    private void limpar() {
        tfLargura.clear();
        tfComprimento.clear();
        tfQuantidade.clear();
        tfMetragemLinear.clear();
        tfPerimetro.clear();

        lblAreaResultado.setText("0.00 m²");
        lblCustoMaterialResultado.setText("R$ 0.00");
        lblCustoServicoResultado.setText("R$ 0.00");
        lblTotalResultado.setText("R$ 0.00");

        taObservacoes.clear();

        if (!cbMaterial.getItems().isEmpty()) {
            cbMaterial.getSelectionModel().selectFirst();
        }
        if (!cbServico.getItems().isEmpty()) {
            cbServico.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void adicionarAoOrcamento() {
        try {
            Material material = cbMaterial.getValue();
            Servico servico = cbServico.getValue();

            if (material == null) {
                mostrarAlerta("Selecione um material", "Por favor, selecione um material para continuar.");
                return;
            }

            double largura = Double.parseDouble(tfLargura.getText());
            double comprimento = Double.parseDouble(tfComprimento.getText());
            int quantidade = Integer.parseInt(tfQuantidade.getText());
            String observacoes = taObservacoes.getText();

            // Aqui você implementaria a lógica para adicionar ao orçamento atual
            // Por enquanto, apenas mostra uma mensagem
            mostrarInformacao("Item adicionado",
                    "Item adicionado ao orçamento:\n" +
                            material.getNome() + "\n" +
                            "Dimensões: " + largura + " x " + comprimento + " mm\n" +
                            "Quantidade: " + quantidade + "\n" +
                            "Valor: R$ " + lblTotalResultado.getText());

        } catch (NumberFormatException e) {
            mostrarAlerta("Dados inválidos", "Preencha todos os campos numéricos corretamente.");
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarInformacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}