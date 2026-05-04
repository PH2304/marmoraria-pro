package br.com.marmoraria.view;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Material;
import br.com.marmoraria.model.Orcamento;
import br.com.marmoraria.model.Servico;
import br.com.marmoraria.model.TipoTrabalho;
import br.com.marmoraria.service.CalculadoraService;
import br.com.marmoraria.service.MaterialService;
import br.com.marmoraria.util.FileManager;
import br.com.marmoraria.util.GeradorPDF;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class OrcamentoView extends BorderPane {

    private final TableView<ItemOrcamento> tabela = new TableView<>();
    private final ObservableList<ItemOrcamento> itens = FXCollections.observableArrayList();

    private final Label subtotalLabel = new Label("Subtotal itens: R$ 0,00");
    private final Label totalLabel = new Label("Total final: R$ 0,00");

    private TextField txtClienteNome;
    private TextField txtClienteTelefone;
    private TextField txtClienteEmail;
    private TextField txtEnderecoObra;
    private TextArea txtObservacoes;

    private ComboBox<TipoTrabalho> cbTipoTrabalho;
    private ComboBox<Material> cbMaterial;
    private ComboBox<Servico> cbServico;
    private ComboBox<String> cbComplexidade;
    private TextField txtLargura;
    private TextField txtComprimento;
    private TextField txtQuantidade;
    private CheckBox chkAcabamentoEspecial;

    private TextField txtSaiaLateralQtd;
    private TextField txtSaiaLateralAltura;
    private TextField txtSaiaFrontalQtd;
    private TextField txtSaiaFrontalAltura;
    private TextField txtFrontaoLateralQtd;
    private TextField txtFrontaoLateralAltura;
    private TextField txtFrontaoParedeQtd;
    private TextField txtFrontaoParedeAltura;
    private VBox secaoBancada;

    private TextField txtPeitorilSaiaQtd;
    private TextField txtPeitorilSaiaAltura;
    private VBox secaoPeitoril;

    private TextField txtNichoAltura;
    private TextField txtNichoProfundidade;
    private TextField txtNichoFundoQtd;
    private TextField txtNichoAlizarLateralQtd;
    private TextField txtNichoAlizarLateralLargura;
    private TextField txtNichoAlizarSupInfQtd;
    private TextField txtNichoAlizarSupInfLargura;
    private TextField txtNichoPrateleiraQtd;
    private VBox secaoNicho;

    private TextField txtSoleiraSaiaQtd;
    private TextField txtSoleiraSaiaAltura;
    private VBox secaoSoleira;

    private TextField txtEscadaSaiaQtd;
    private TextField txtEscadaSaiaAltura;
    private TextField txtEscadaEspelhoQtd;
    private TextField txtEscadaEspelhoAltura;
    private VBox secaoEscada;

    private TextField txtRequadroAltura;
    private TextField txtRequadroLateraisQtd;
    private TextField txtRequadroLateraisLargura;
    private TextField txtRequadroSupInfQtd;
    private TextField txtRequadroSupInfLargura;
    private VBox secaoRequadro;

    private TextField txtChapimSaiaQtd;
    private TextField txtChapimSaiaAltura;
    private VBox secaoChapim;

    private VBox secaoRodape;
    private VBox secaoPiso;
    private VBox secaoTentoBox;

    private TextField txtMesaSaiaLateralQtd;
    private TextField txtMesaSaiaLateralAltura;
    private TextField txtMesaSaiaFrontalQtd;
    private TextField txtMesaSaiaFrontalAltura;
    private CheckBox chkMesaSarim;
    private TextField txtMesaPeLargura;
    private TextField txtMesaPeComprimento;
    private TextField txtMesaPeQtd;
    private VBox secaoMesa;

    private TextField txtCubaComprimento;
    private TextField txtCubaProfundidade;
    private TextField txtCubaFundoFalsoQtd;
    private TextField txtCubaFundoFalsoLargura;
    private TextField txtCubaFundoFalsoComprimento;
    private TextField txtCubaMaoDeObra;
    private VBox secaoCubaEsculpida;

    private TextField txtFrete;
    private TextField txtCredito;
    private TextField txtMargem;
    private CheckBox chkMaoDeObra;
    private TextField txtPercentualMaoDeObra;
    private TextField txtMinimoMaoDeObra;

    private final CalculadoraService calc = new CalculadoraService();
    private final MaterialService materialService = new MaterialService();

    public OrcamentoView() {
        setPadding(new Insets(15));
        setStyle("-fx-background-color: #ecf0f1;");

        VBox cabecalho = criarCabecalho();
        VBox painelCliente = criarPainelCliente();
        VBox painelEntrada = criarPainelEntrada();
        VBox painelFinanceiro = criarPainelFinanceiro();
        VBox painelTabela = criarPainelTabela();
        HBox rodape = criarRodape();

        VBox painelEsquerdo = new VBox(15, painelCliente, painelEntrada, painelFinanceiro);
        painelEsquerdo.setPrefWidth(370);
        ScrollPane scrollPainelEsquerdo = new ScrollPane(painelEsquerdo);
        scrollPainelEsquerdo.setFitToWidth(true);
        scrollPainelEsquerdo.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPainelEsquerdo.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPainelEsquerdo.setPannable(true);
        scrollPainelEsquerdo.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPainelEsquerdo.setPrefWidth(390);

        setTop(cabecalho);
        setLeft(scrollPainelEsquerdo);
        setCenter(painelTabela);
        setBottom(rodape);

        atualizarCamposBancada();
        atualizarResumo();
    }

    private VBox criarCabecalho() {
        VBox box = new VBox(4);
        box.setPadding(new Insets(0, 0, 14, 0));

        Label titulo = new Label("Orcamento");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1f2937;");

        Label subtitulo = new Label("Monte a composicao da peca, revise o fechamento e acompanhe o total final em tempo real.");
        subtitulo.setStyle("-fx-font-size: 12px; -fx-text-fill: #6b7280;");
        subtitulo.setWrapText(true);

        box.getChildren().addAll(titulo, subtitulo);
        return box;
    }

    private VBox criarPainelCliente() {
        VBox box = criarCardBase(10);
        Label titulo = criarTituloSecao("Dados do cliente", "Identificacao e contexto da obra.");

        txtClienteNome = new TextField();
        txtClienteNome.setPromptText("Nome do cliente");

        txtClienteTelefone = new TextField();
        txtClienteTelefone.setPromptText("Telefone");

        txtClienteEmail = new TextField();
        txtClienteEmail.setPromptText("E-mail");

        txtEnderecoObra = new TextField();
        txtEnderecoObra.setPromptText("Endereco da obra");

        txtObservacoes = new TextArea();
        txtObservacoes.setPromptText("Observacoes");
        txtObservacoes.setPrefRowCount(3);
        txtObservacoes.setWrapText(true);

        box.getChildren().addAll(
                titulo,
                criarCampoComLegenda("Cliente", txtClienteNome),
                criarCampoComLegenda("Telefone", txtClienteTelefone),
                criarCampoComLegenda("E-mail", txtClienteEmail),
                criarCampoComLegenda("Endereco da obra", txtEnderecoObra),
                criarCampoComLegenda("Observacoes", txtObservacoes)
        );
        return box;
    }

    private VBox criarPainelEntrada() {
        VBox box = criarCardBase(10);
        Label titulo = criarTituloSecao("Composicao da peca", "Escolha o tipo de trabalho e informe as medidas.");

        cbTipoTrabalho = new ComboBox<>();
        cbTipoTrabalho.setItems(FXCollections.observableArrayList(TipoTrabalho.values()));
        cbTipoTrabalho.setValue(TipoTrabalho.BANCADA);
        cbTipoTrabalho.setOnAction(event -> atualizarCamposBancada());
        cbTipoTrabalho.setMaxWidth(Double.MAX_VALUE);

        cbMaterial = new ComboBox<>();
        cbMaterial.setItems(FXCollections.observableArrayList(materialService.getTodosMateriais()));
        cbMaterial.setPromptText("Material");
        cbMaterial.setMaxWidth(Double.MAX_VALUE);

        cbServico = new ComboBox<>();
        cbServico.setItems(FXCollections.observableArrayList(materialService.getTodosServicos()));
        cbServico.setPromptText("Servico opcional");
        cbServico.setMaxWidth(Double.MAX_VALUE);

        cbComplexidade = new ComboBox<>();
        cbComplexidade.setItems(FXCollections.observableArrayList("1.00", "1.10", "1.20", "1.30", "1.50"));
        cbComplexidade.setValue("1.20");
        cbComplexidade.setMaxWidth(Double.MAX_VALUE);

        txtLargura = campoNumerico("Largura (mm)", "1500");
        txtComprimento = campoNumerico("Comprimento (mm)", "500");
        txtQuantidade = campoNumerico("Quantidade", "1");

        chkAcabamentoEspecial = new CheckBox("Acabamento especial (111%)");
        chkAcabamentoEspecial.setStyle("-fx-text-fill: #374151;");

        GridPane medidasGrid = new GridPane();
        medidasGrid.setHgap(10);
        medidasGrid.setVgap(10);
        medidasGrid.getColumnConstraints().addAll(colunaFlex(), colunaFlex(), colunaFlex());
        medidasGrid.add(criarCampoComLegenda("Largura", txtLargura), 0, 0);
        medidasGrid.add(criarCampoComLegenda("Comprimento", txtComprimento), 1, 0);
        medidasGrid.add(criarCampoComLegenda("Quantidade", txtQuantidade), 2, 0);

        GridPane bancadaGrid = new GridPane();
        bancadaGrid.setHgap(8);
        bancadaGrid.setVgap(8);

        txtSaiaLateralQtd = campoNumerico("Qtd", "0");
        txtSaiaLateralAltura = campoNumerico("Altura mm", "0");
        txtSaiaFrontalQtd = campoNumerico("Qtd", "1");
        txtSaiaFrontalAltura = campoNumerico("Altura mm", "100");
        txtFrontaoLateralQtd = campoNumerico("Qtd", "0");
        txtFrontaoLateralAltura = campoNumerico("Altura mm", "0");
        txtFrontaoParedeQtd = campoNumerico("Qtd", "1");
        txtFrontaoParedeAltura = campoNumerico("Altura mm", "200");

        adicionarLinhaBancada(bancadaGrid, 0, "Saia lateral", txtSaiaLateralQtd, txtSaiaLateralAltura);
        adicionarLinhaBancada(bancadaGrid, 1, "Saia frontal", txtSaiaFrontalQtd, txtSaiaFrontalAltura);
        adicionarLinhaBancada(bancadaGrid, 2, "Frontao lateral", txtFrontaoLateralQtd, txtFrontaoLateralAltura);
        adicionarLinhaBancada(bancadaGrid, 3, "Frontao parede", txtFrontaoParedeQtd, txtFrontaoParedeAltura);
        secaoBancada = criarSecaoTipo("Bancada", bancadaGrid);

        GridPane peitorilGrid = new GridPane();
        peitorilGrid.setHgap(8);
        peitorilGrid.setVgap(8);
        txtPeitorilSaiaQtd = campoNumerico("Qtd", "0");
        txtPeitorilSaiaAltura = campoNumerico("Altura mm", "0");
        adicionarLinhaBancada(peitorilGrid, 0, "Saia", txtPeitorilSaiaQtd, txtPeitorilSaiaAltura);
        secaoPeitoril = criarSecaoTipo("Peitoril", peitorilGrid);

        GridPane nichoGrid = new GridPane();
        nichoGrid.setHgap(8);
        nichoGrid.setVgap(8);
        txtNichoAltura = campoNumerico("Altura (mm)", "600");
        txtNichoProfundidade = campoNumerico("Profundidade (mm)", "120");
        txtNichoFundoQtd = campoNumerico("Qtd de fundo", "1");
        txtNichoAlizarLateralQtd = campoNumerico("Qtd alizar lateral", "2");
        txtNichoAlizarLateralLargura = campoNumerico("Largura alizar lat. (mm)", "50");
        txtNichoAlizarSupInfQtd = campoNumerico("Qtd alizar sup/inf", "2");
        txtNichoAlizarSupInfLargura = campoNumerico("Largura alizar sup/inf (mm)", "50");
        txtNichoPrateleiraQtd = campoNumerico("Qtd prateleira", "0");

        nichoGrid.add(new Label("Altura"), 0, 0);
        nichoGrid.add(txtNichoAltura, 1, 0, 2, 1);
        nichoGrid.add(new Label("Profundidade"), 0, 1);
        nichoGrid.add(txtNichoProfundidade, 1, 1, 2, 1);
        nichoGrid.add(new Label("Fundo"), 0, 2);
        nichoGrid.add(txtNichoFundoQtd, 1, 2, 2, 1);
        nichoGrid.add(new Label("Alizar lateral"), 0, 3);
        nichoGrid.add(txtNichoAlizarLateralQtd, 1, 3);
        nichoGrid.add(txtNichoAlizarLateralLargura, 2, 3);
        nichoGrid.add(new Label("Alizar sup/inf"), 0, 4);
        nichoGrid.add(txtNichoAlizarSupInfQtd, 1, 4);
        nichoGrid.add(txtNichoAlizarSupInfLargura, 2, 4);
        nichoGrid.add(new Label("Prateleiras"), 0, 5);
        nichoGrid.add(txtNichoPrateleiraQtd, 1, 5, 2, 1);
        secaoNicho = criarSecaoTipo("Nicho", nichoGrid);

        GridPane soleiraGrid = new GridPane();
        soleiraGrid.setHgap(8);
        soleiraGrid.setVgap(8);
        txtSoleiraSaiaQtd = campoNumerico("Qtd", "0");
        txtSoleiraSaiaAltura = campoNumerico("Altura mm", "0");
        adicionarLinhaBancada(soleiraGrid, 0, "Saia", txtSoleiraSaiaQtd, txtSoleiraSaiaAltura);
        secaoSoleira = criarSecaoTipo("Soleira", soleiraGrid);

        GridPane escadaGrid = new GridPane();
        escadaGrid.setHgap(8);
        escadaGrid.setVgap(8);
        txtEscadaSaiaQtd = campoNumerico("Qtd saias", "1");
        txtEscadaSaiaAltura = campoNumerico("Altura saia mm", "150");
        txtEscadaEspelhoQtd = campoNumerico("Qtd espelhos", "1");
        txtEscadaEspelhoAltura = campoNumerico("Altura espelho mm", "180");
        adicionarLinhaBancada(escadaGrid, 0, "Saia", txtEscadaSaiaQtd, txtEscadaSaiaAltura);
        adicionarLinhaBancada(escadaGrid, 1, "Espelho", txtEscadaEspelhoQtd, txtEscadaEspelhoAltura);
        secaoEscada = criarSecaoTipo("Escada", escadaGrid);

        GridPane requadroGrid = new GridPane();
        requadroGrid.setHgap(8);
        requadroGrid.setVgap(8);
        txtRequadroAltura = campoNumerico("Altura (mm)", "1200");
        txtRequadroLateraisQtd = campoNumerico("Qtd laterais", "2");
        txtRequadroLateraisLargura = campoNumerico("Largura lateral mm", "100");
        txtRequadroSupInfQtd = campoNumerico("Qtd sup/inf", "2");
        txtRequadroSupInfLargura = campoNumerico("Largura sup/inf mm", "100");
        requadroGrid.add(new Label("Altura"), 0, 0);
        requadroGrid.add(txtRequadroAltura, 1, 0, 2, 1);
        requadroGrid.add(new Label("Laterais"), 0, 1);
        requadroGrid.add(txtRequadroLateraisQtd, 1, 1);
        requadroGrid.add(txtRequadroLateraisLargura, 2, 1);
        requadroGrid.add(new Label("Sup/Inf"), 0, 2);
        requadroGrid.add(txtRequadroSupInfQtd, 1, 2);
        requadroGrid.add(txtRequadroSupInfLargura, 2, 2);
        secaoRequadro = criarSecaoTipo("Requadro", requadroGrid);

        GridPane chapimGrid = new GridPane();
        chapimGrid.setHgap(8);
        chapimGrid.setVgap(8);
        txtChapimSaiaQtd = campoNumerico("Qtd", "0");
        txtChapimSaiaAltura = campoNumerico("Altura mm", "0");
        adicionarLinhaBancada(chapimGrid, 0, "Saia", txtChapimSaiaQtd, txtChapimSaiaAltura);
        secaoChapim = criarSecaoTipo("Chapim", chapimGrid);

        secaoRodape = criarSecaoTipo(
                "Rodape",
                criarGradeInformativa("Use largura como comprimento linear e comprimento como altura da faixa.")
        );
        secaoPiso = criarSecaoTipo(
                "Piso",
                criarGradeInformativa("Use largura e comprimento da placa ou modulo repetido no ambiente.")
        );
        secaoTentoBox = criarSecaoTipo(
                "Tento do Box",
                criarGradeInformativa("O calculo considera duas faixas por conjunto. Use largura e comprimento como altura da faixa.")
        );

        GridPane mesaGrid = new GridPane();
        mesaGrid.setHgap(8);
        mesaGrid.setVgap(8);
        txtMesaSaiaLateralQtd = campoNumerico("Qtd", "0");
        txtMesaSaiaLateralAltura = campoNumerico("Altura mm", "0");
        txtMesaSaiaFrontalQtd = campoNumerico("Qtd", "0");
        txtMesaSaiaFrontalAltura = campoNumerico("Altura mm", "0");
        chkMesaSarim = new CheckBox("Adicionar sarim");
        chkMesaSarim.setStyle("-fx-text-fill: #374151;");
        txtMesaPeLargura = campoNumerico("Largura pe mm", "0");
        txtMesaPeComprimento = campoNumerico("Comprimento pe mm", "0");
        txtMesaPeQtd = campoNumerico("Qtd pes", "0");
        adicionarLinhaBancada(mesaGrid, 0, "Saia lateral", txtMesaSaiaLateralQtd, txtMesaSaiaLateralAltura);
        adicionarLinhaBancada(mesaGrid, 1, "Saia frontal", txtMesaSaiaFrontalQtd, txtMesaSaiaFrontalAltura);
        mesaGrid.add(chkMesaSarim, 0, 2, 3, 1);
        mesaGrid.add(new Label("Pe"), 0, 3);
        mesaGrid.add(txtMesaPeLargura, 1, 3);
        mesaGrid.add(txtMesaPeComprimento, 2, 3);
        mesaGrid.add(new Label("Qtd pes"), 0, 4);
        mesaGrid.add(txtMesaPeQtd, 1, 4, 2, 1);
        secaoMesa = criarSecaoTipo("Mesa", mesaGrid);

        GridPane cubaGrid = new GridPane();
        cubaGrid.setHgap(8);
        cubaGrid.setVgap(8);
        txtCubaComprimento = campoNumerico("Comprimento mm", "350");
        txtCubaProfundidade = campoNumerico("Profundidade mm", "150");
        txtCubaFundoFalsoQtd = campoNumerico("Qtd fundos falsos", "0");
        txtCubaFundoFalsoLargura = campoNumerico("Largura fundo falso mm", "0");
        txtCubaFundoFalsoComprimento = campoNumerico("Comprimento fundo falso mm", "0");
        txtCubaMaoDeObra = campoNumerico("Mao de obra un.", "1000");
        cubaGrid.add(new Label("Comprimento"), 0, 0);
        cubaGrid.add(txtCubaComprimento, 1, 0, 2, 1);
        cubaGrid.add(new Label("Profundidade"), 0, 1);
        cubaGrid.add(txtCubaProfundidade, 1, 1, 2, 1);
        cubaGrid.add(new Label("Fundos falsos"), 0, 2);
        cubaGrid.add(txtCubaFundoFalsoQtd, 1, 2, 2, 1);
        cubaGrid.add(new Label("Fundo falso"), 0, 3);
        cubaGrid.add(txtCubaFundoFalsoLargura, 1, 3);
        cubaGrid.add(txtCubaFundoFalsoComprimento, 2, 3);
        cubaGrid.add(new Label("Mao de obra"), 0, 4);
        cubaGrid.add(txtCubaMaoDeObra, 1, 4, 2, 1);
        secaoCubaEsculpida = criarSecaoTipo("Cuba Esculpida", cubaGrid);

        Button btnAdicionar = new Button("Adicionar item");
        btnAdicionar.setStyle(estiloBotaoPrimario());
        btnAdicionar.setMaxWidth(Double.MAX_VALUE);
        btnAdicionar.setOnAction(event -> adicionarItem());

        box.getChildren().addAll(
                titulo,
                criarCampoComLegenda("Tipo de trabalho", cbTipoTrabalho),
                criarCampoComLegenda("Material", cbMaterial),
                criarCampoComLegenda("Servico", cbServico),
                criarCampoComLegenda("Fator de complexidade", cbComplexidade),
                medidasGrid,
                chkAcabamentoEspecial,
                secaoBancada,
                secaoPeitoril,
                secaoNicho,
                secaoSoleira,
                secaoEscada,
                secaoRequadro,
                secaoChapim,
                secaoRodape,
                secaoPiso,
                secaoTentoBox,
                secaoMesa,
                secaoCubaEsculpida,
                btnAdicionar
        );

        return box;
    }

    private VBox criarPainelFinanceiro() {
        VBox box = criarCardBase(10);
        Label titulo = criarTituloSecao("Fechamento comercial", "Ajuste margem, frete, credito e mao de obra.");

        txtFrete = campoNumerico("Frete", "0");
        txtCredito = campoNumerico("Credito", "0");
        txtMargem = campoNumerico("Margem de lucro (%)", "30");
        chkMaoDeObra = new CheckBox("Aplicar mao de obra");
        chkMaoDeObra.setStyle("-fx-text-fill: #374151;");
        txtPercentualMaoDeObra = campoNumerico("Percentual MDO (0.30 = 30%)", "0.30");
        txtMinimoMaoDeObra = campoNumerico("Minimo da MDO", "1200");

        GridPane financeiroGrid = new GridPane();
        financeiroGrid.setHgap(10);
        financeiroGrid.setVgap(10);
        financeiroGrid.getColumnConstraints().addAll(colunaFlex(), colunaFlex());
        financeiroGrid.add(criarCampoComLegenda("Frete", txtFrete), 0, 0);
        financeiroGrid.add(criarCampoComLegenda("Credito", txtCredito), 1, 0);
        financeiroGrid.add(criarCampoComLegenda("Margem de lucro (%)", txtMargem), 0, 1);
        financeiroGrid.add(criarCampoComLegenda("Percentual da MDO", txtPercentualMaoDeObra), 1, 1);
        financeiroGrid.add(criarCampoComLegenda("Minimo da MDO", txtMinimoMaoDeObra, 2), 0, 2);

        box.getChildren().addAll(
                titulo,
                financeiroGrid,
                chkMaoDeObra,
                criarResumoFinanceiro()
        );

        return box;
    }

    private VBox criarPainelTabela() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(0, 0, 0, 15));

        Label titulo = criarTituloSecao("Itens do orcamento", "Confira a composicao e remova itens quando necessario.");

        TableColumn<ItemOrcamento, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipoTrabalho().getDescricao()));
        colTipo.setPrefWidth(110);

        TableColumn<ItemOrcamento, String> colMaterial = new TableColumn<>("Material");
        colMaterial.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMaterial().getNome()));
        colMaterial.setPrefWidth(150);

        TableColumn<ItemOrcamento, String> colDescricao = new TableColumn<>("Descricao");
        colDescricao.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescricao()));
        colDescricao.setPrefWidth(260);

        TableColumn<ItemOrcamento, Number> colArea = new TableColumn<>("Area m2");
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colArea.setPrefWidth(85);

        TableColumn<ItemOrcamento, Number> colMaterialValor = new TableColumn<>("Material R$");
        colMaterialValor.setCellValueFactory(new PropertyValueFactory<>("custoMaterial"));
        colMaterialValor.setPrefWidth(95);

        TableColumn<ItemOrcamento, Number> colServicoValor = new TableColumn<>("Servico R$");
        colServicoValor.setCellValueFactory(new PropertyValueFactory<>("custoServico"));
        colServicoValor.setPrefWidth(90);

        TableColumn<ItemOrcamento, Number> colTotal = new TableColumn<>("Total R$");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colTotal.setPrefWidth(90);

        TableColumn<ItemOrcamento, Void> colAcao = new TableColumn<>("Acao");
        colAcao.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Excluir");
            {
                btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                btn.setOnAction(event -> {
                    ItemOrcamento item = getTableView().getItems().get(getIndex());
                    itens.remove(item);
                    atualizarResumo();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
        colAcao.setPrefWidth(80);

        tabela.getColumns().addAll(colTipo, colMaterial, colDescricao, colArea, colMaterialValor, colServicoValor, colTotal, colAcao);
        tabela.setItems(itens);
        tabela.setPrefHeight(560);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setPlaceholder(new Label("Nenhum item adicionado ainda."));
        tabela.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 10; -fx-background-radius: 10;");

        box.getChildren().addAll(titulo, tabela);
        VBox.setVgrow(tabela, Priority.ALWAYS);
        return box;
    }

    private HBox criarRodape() {
        HBox rodape = new HBox(12);
        rodape.setPadding(new Insets(15, 10, 10, 10));
        rodape.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10;");
        rodape.setAlignment(Pos.CENTER_RIGHT);

        VBox resumo = new VBox(4, subtotalLabel, totalLabel);
        resumo.setAlignment(Pos.CENTER_LEFT);
        subtotalLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #4b5563;");
        totalLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #166534;");

        Button btnSalvar = new Button("Salvar orcamento");
        btnSalvar.setStyle(estiloBotaoSucesso());
        btnSalvar.setOnAction(event -> salvarOrcamento());

        Button btnLimpar = new Button("Limpar");
        btnLimpar.setStyle(estiloBotaoPerigo());
        btnLimpar.setOnAction(event -> limparOrcamento());

        Button btnPDF = new Button("Exportar PDF");
        btnPDF.setStyle(estiloBotaoSecundario());
        btnPDF.setOnAction(event -> gerarPDF());

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        rodape.getChildren().addAll(resumo, espacador, btnPDF, btnLimpar, btnSalvar);
        return rodape;
    }

    private VBox criarCardBase(int spacing) {
        VBox box = new VBox(spacing);
        box.setPadding(new Insets(14));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-border-color: #e5e7eb; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(15,23,42,0.06), 8, 0, 0, 2);");
        return box;
    }

    private TextField campoNumerico(String prompt, String valorInicial) {
        TextField field = new TextField(valorInicial);
        field.setPromptText(prompt);
        field.setMaxWidth(Double.MAX_VALUE);
        field.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #d1d5db; -fx-padding: 8 10;");
        return field;
    }

    private void adicionarLinhaBancada(GridPane grid, int row, String label, TextField qtd, TextField altura) {
        grid.add(new Label(label), 0, row);
        grid.add(qtd, 1, row);
        grid.add(altura, 2, row);
    }

    private VBox criarSecaoTipo(String titulo, GridPane conteudo) {
        VBox box = new VBox(8);
        Label label = new Label(titulo);
        label.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1f2937;");
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8; -fx-border-color: #e5e7eb; -fx-border-radius: 8;");
        box.getChildren().addAll(label, conteudo);
        return box;
    }

    private Label criarTituloSecao(String titulo, String descricao) {
        Label label = new Label(titulo + "\n" + descricao);
        label.setContentDisplay(ContentDisplay.TEXT_ONLY);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #111827;");
        label.setWrapText(true);
        return label;
    }

    private VBox criarCampoComLegenda(String titulo, Region campo) {
        VBox box = new VBox(4);
        Label label = new Label(titulo);
        label.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #6b7280;");
        box.getChildren().addAll(label, campo);
        VBox.setVgrow(campo, Priority.NEVER);
        return box;
    }

    private VBox criarCampoComLegenda(String titulo, Region campo, int colSpan) {
        VBox box = criarCampoComLegenda(titulo, campo);
        GridPane.setColumnSpan(box, colSpan);
        return box;
    }

    private ColumnConstraints colunaFlex() {
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
        cc.setFillWidth(true);
        return cc;
    }

    private VBox criarResumoFinanceiro() {
        VBox resumo = new VBox(4);
        resumo.setPadding(new Insets(10));
        resumo.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8; -fx-border-color: #e5e7eb; -fx-border-radius: 8;");

        Label titulo = new Label("Resumo");
        titulo.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1f2937;");

        Label dica = new Label("Subtotal com margem e total final aparecem no rodape conforme os itens e ajustes.");
        dica.setWrapText(true);
        dica.setStyle("-fx-font-size: 11px; -fx-text-fill: #6b7280;");

        resumo.getChildren().addAll(titulo, dica);
        return resumo;
    }

    private GridPane criarGradeInformativa(String texto) {
        GridPane grid = new GridPane();
        Label label = new Label(texto);
        label.setWrapText(true);
        label.setStyle("-fx-font-size: 11px; -fx-text-fill: #6b7280;");
        grid.add(label, 0, 0);
        return grid;
    }

    private String estiloBotaoPrimario() {
        return "-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 14;";
    }

    private String estiloBotaoSecundario() {
        return "-fx-background-color: #ea580c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 14;";
    }

    private String estiloBotaoSucesso() {
        return "-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 14;";
    }

    private String estiloBotaoPerigo() {
        return "-fx-background-color: #dc2626; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 14;";
    }

    private void atualizarCamposBancada() {
        TipoTrabalho tipo = cbTipoTrabalho != null ? cbTipoTrabalho.getValue() : TipoTrabalho.GENERICO;
        boolean exibirBancada = tipo == TipoTrabalho.BANCADA;
        boolean exibirPeitoril = tipo == TipoTrabalho.PEITORIL;
        boolean exibirNicho = tipo == TipoTrabalho.NICHO;
        boolean exibirSoleira = tipo == TipoTrabalho.SOLEIRA;
        boolean exibirEscada = tipo == TipoTrabalho.ESCADA;
        boolean exibirRequadro = tipo == TipoTrabalho.REQUADRO;
        boolean exibirChapim = tipo == TipoTrabalho.CHAPIM;
        boolean exibirRodape = tipo == TipoTrabalho.RODAPE;
        boolean exibirPiso = tipo == TipoTrabalho.PISO;
        boolean exibirTentoBox = tipo == TipoTrabalho.TENTO_DO_BOX;
        boolean exibirMesa = tipo == TipoTrabalho.MESA;
        boolean exibirCubaEsculpida = tipo == TipoTrabalho.CUBA_ESCULPIDA;

        setSecaoVisivel(secaoBancada, exibirBancada);
        setSecaoVisivel(secaoPeitoril, exibirPeitoril);
        setSecaoVisivel(secaoNicho, exibirNicho);
        setSecaoVisivel(secaoSoleira, exibirSoleira);
        setSecaoVisivel(secaoEscada, exibirEscada);
        setSecaoVisivel(secaoRequadro, exibirRequadro);
        setSecaoVisivel(secaoChapim, exibirChapim);
        setSecaoVisivel(secaoRodape, exibirRodape);
        setSecaoVisivel(secaoPiso, exibirPiso);
        setSecaoVisivel(secaoTentoBox, exibirTentoBox);
        setSecaoVisivel(secaoMesa, exibirMesa);
        setSecaoVisivel(secaoCubaEsculpida, exibirCubaEsculpida);
        setBancadaFieldsDisabled(!exibirBancada);
    }

    private void setSecaoVisivel(VBox secao, boolean visivel) {
        secao.setVisible(visivel);
        secao.setManaged(visivel);
    }

    private void setBancadaFieldsDisabled(boolean disabled) {
        txtSaiaLateralQtd.setDisable(disabled);
        txtSaiaLateralAltura.setDisable(disabled);
        txtSaiaFrontalQtd.setDisable(disabled);
        txtSaiaFrontalAltura.setDisable(disabled);
        txtFrontaoLateralQtd.setDisable(disabled);
        txtFrontaoLateralAltura.setDisable(disabled);
        txtFrontaoParedeQtd.setDisable(disabled);
        txtFrontaoParedeAltura.setDisable(disabled);
    }

    private void adicionarItem() {
        try {
            Material material = cbMaterial.getValue();
            if (material == null) {
                mostrarAlerta("Selecione um material.");
                return;
            }

            Servico servico = cbServico.getValue();
            TipoTrabalho tipoTrabalho = cbTipoTrabalho.getValue();
            double largura = lerDouble(txtLargura, "largura");
            double comprimento = lerDouble(txtComprimento, "comprimento");
            int quantidade = lerInt(txtQuantidade, "quantidade");
            double fatorComplexidade = Double.parseDouble(cbComplexidade.getValue());
            boolean acabamentoEspecial = chkAcabamentoEspecial.isSelected();

            ItemOrcamento item;
            if (tipoTrabalho == TipoTrabalho.BANCADA) {
                item = calc.criarBancada(
                        material,
                        servico,
                        largura,
                        comprimento,
                        quantidade,
                        lerInt(txtSaiaLateralQtd, "saia lateral"),
                        lerDouble(txtSaiaLateralAltura, "altura da saia lateral"),
                        lerInt(txtSaiaFrontalQtd, "saia frontal"),
                        lerDouble(txtSaiaFrontalAltura, "altura da saia frontal"),
                        lerInt(txtFrontaoLateralQtd, "frontao lateral"),
                        lerDouble(txtFrontaoLateralAltura, "altura do frontao lateral"),
                        lerInt(txtFrontaoParedeQtd, "frontao parede"),
                        lerDouble(txtFrontaoParedeAltura, "altura do frontao parede"),
                        fatorComplexidade,
                        acabamentoEspecial
                );
            } else if (tipoTrabalho == TipoTrabalho.PEITORIL) {
                item = calc.criarPeitoril(
                        material,
                        servico,
                        largura,
                        comprimento,
                        quantidade,
                        lerInt(txtPeitorilSaiaQtd, "saia do peitoril"),
                        lerDouble(txtPeitorilSaiaAltura, "altura da saia do peitoril"),
                        fatorComplexidade,
                        acabamentoEspecial
                );
            } else if (tipoTrabalho == TipoTrabalho.NICHO) {
                item = calc.criarNicho(
                        material,
                        servico,
                        largura,
                        lerDouble(txtNichoAltura, "altura do nicho"),
                        lerDouble(txtNichoProfundidade, "profundidade do nicho"),
                        quantidade,
                        lerInt(txtNichoFundoQtd, "fundo do nicho"),
                        lerInt(txtNichoAlizarLateralQtd, "alizar lateral do nicho"),
                        lerDouble(txtNichoAlizarLateralLargura, "largura do alizar lateral"),
                        lerInt(txtNichoAlizarSupInfQtd, "alizar superior/inferior do nicho"),
                        lerDouble(txtNichoAlizarSupInfLargura, "largura do alizar superior/inferior"),
                        lerInt(txtNichoPrateleiraQtd, "prateleira do nicho"),
                        fatorComplexidade,
                        acabamentoEspecial
                );
            } else if (tipoTrabalho == TipoTrabalho.SOLEIRA) {
                item = calc.criarSoleira(
                        material,
                        servico,
                        largura,
                        comprimento,
                        quantidade,
                        lerInt(txtSoleiraSaiaQtd, "saia da soleira"),
                        lerDouble(txtSoleiraSaiaAltura, "altura da saia da soleira"),
                        fatorComplexidade,
                        acabamentoEspecial
                );
            } else if (tipoTrabalho == TipoTrabalho.ESCADA) {
                item = calc.criarEscada(
                        material,
                        servico,
                        largura,
                        comprimento,
                        quantidade,
                        lerInt(txtEscadaSaiaQtd, "saia da escada"),
                        lerDouble(txtEscadaSaiaAltura, "altura da saia da escada"),
                        lerInt(txtEscadaEspelhoQtd, "espelho da escada"),
                        lerDouble(txtEscadaEspelhoAltura, "altura do espelho da escada"),
                        fatorComplexidade,
                        acabamentoEspecial
                );
            } else if (tipoTrabalho == TipoTrabalho.REQUADRO) {
                item = calc.criarRequadro(
                        material,
                        servico,
                        largura,
                        lerDouble(txtRequadroAltura, "altura do requadro"),
                        quantidade,
                        lerInt(txtRequadroLateraisQtd, "laterais do requadro"),
                        lerDouble(txtRequadroLateraisLargura, "largura lateral do requadro"),
                        lerInt(txtRequadroSupInfQtd, "superior/inferior do requadro"),
                        lerDouble(txtRequadroSupInfLargura, "largura superior/inferior do requadro"),
                        fatorComplexidade,
                        acabamentoEspecial
                );
            } else if (tipoTrabalho == TipoTrabalho.CHAPIM) {
                item = calc.criarChapim(
                        material,
                        servico,
                        largura,
                        comprimento,
                        quantidade,
                        lerInt(txtChapimSaiaQtd, "saia do chapim"),
                        lerDouble(txtChapimSaiaAltura, "altura da saia do chapim"),
                        fatorComplexidade,
                        acabamentoEspecial
                );
            } else if (tipoTrabalho == TipoTrabalho.RODAPE) {
                item = calc.criarRodape(
                        material,
                        servico,
                        largura,
                        comprimento,
                        quantidade,
                        fatorComplexidade,
                        acabamentoEspecial
                );
            } else if (tipoTrabalho == TipoTrabalho.PISO) {
                item = calc.criarPiso(
                        material,
                        servico,
                        largura,
                        comprimento,
                        quantidade,
                        fatorComplexidade,
                        acabamentoEspecial
                );
            } else if (tipoTrabalho == TipoTrabalho.TENTO_DO_BOX) {
                item = calc.criarTentoDoBox(
                        material,
                        servico,
                        largura,
                        comprimento,
                        quantidade,
                        fatorComplexidade,
                        acabamentoEspecial
                );
            } else if (tipoTrabalho == TipoTrabalho.MESA) {
                item = calc.criarMesa(
                        material,
                        servico,
                        largura,
                        comprimento,
                        quantidade,
                        lerInt(txtMesaSaiaLateralQtd, "saia lateral da mesa"),
                        lerDouble(txtMesaSaiaLateralAltura, "altura da saia lateral da mesa"),
                        lerInt(txtMesaSaiaFrontalQtd, "saia frontal da mesa"),
                        lerDouble(txtMesaSaiaFrontalAltura, "altura da saia frontal da mesa"),
                        chkMesaSarim.isSelected(),
                        lerDoubleSemFalha(txtMesaPeLargura, 0.0),
                        lerDoubleSemFalha(txtMesaPeComprimento, 0.0),
                        lerInt(txtMesaPeQtd, "pes da mesa"),
                        fatorComplexidade,
                        acabamentoEspecial
                );
            } else if (tipoTrabalho == TipoTrabalho.CUBA_ESCULPIDA) {
                item = calc.criarCubaEsculpida(
                        material,
                        servico,
                        largura,
                        lerDouble(txtCubaComprimento, "comprimento da cuba"),
                        lerDouble(txtCubaProfundidade, "profundidade da cuba"),
                        quantidade,
                        lerInt(txtCubaFundoFalsoQtd, "fundos falsos da cuba"),
                        lerDoubleSemFalha(txtCubaFundoFalsoLargura, 0.0),
                        lerDoubleSemFalha(txtCubaFundoFalsoComprimento, 0.0),
                        lerDoubleSemFalha(txtCubaMaoDeObra, 1000.0),
                        fatorComplexidade,
                        acabamentoEspecial
                );
            } else {
                item = calc.criarItemGenerico(material, servico, largura, comprimento, quantidade, fatorComplexidade, acabamentoEspecial);
            }

            itens.add(item);
            atualizarResumo();
            limparCamposDeEntrada();
        } catch (IllegalArgumentException ex) {
            mostrarAlerta(ex.getMessage());
        } catch (Exception ex) {
            mostrarAlerta("Erro ao adicionar item: " + ex.getMessage());
        }
    }

    private void atualizarResumo() {
        Orcamento parcial = criarOrcamentoAtual();
        subtotalLabel.setText(String.format("Subtotal itens: R$ %.2f", parcial.getValorComLucro()));
        totalLabel.setText(String.format("Total final: R$ %.2f", parcial.getTotalFinal()));
    }

    private Orcamento criarOrcamentoAtual() {
        Orcamento orcamento = new Orcamento();
        orcamento.setClienteNome(textoLimpo(txtClienteNome.getText()));
        orcamento.setClienteTelefone(textoLimpo(txtClienteTelefone.getText()));
        orcamento.setClienteEmail(textoLimpo(txtClienteEmail.getText()));
        orcamento.setEnderecoObra(textoLimpo(txtEnderecoObra.getText()));
        orcamento.setObservacoes(textoLimpo(txtObservacoes.getText()));
        orcamento.setMargemLucro(lerDoubleSemFalha(txtMargem, 30.0));
        orcamento.setFrete(lerDoubleSemFalha(txtFrete, 0.0));
        orcamento.setCredito(lerDoubleSemFalha(txtCredito, 0.0));
        orcamento.setIncluirMaoDeObra(chkMaoDeObra.isSelected());
        orcamento.setPercentualMaoDeObra(lerDoubleSemFalha(txtPercentualMaoDeObra, 0.30));
        orcamento.setValorMinimoMaoDeObra(lerDoubleSemFalha(txtMinimoMaoDeObra, 1200.0));

        for (ItemOrcamento item : itens) {
            orcamento.adicionarItem(item);
        }
        orcamento.calcularTotais();
        return orcamento;
    }

    private void salvarOrcamento() {
        if (itens.isEmpty()) {
            mostrarAlerta("Nao ha itens para salvar.");
            return;
        }

        Orcamento orcamento = criarOrcamentoAtual();
        boolean salvou = FileManager.salvarOrcamento(orcamento);

        if (salvou) {
            mostrarAlerta(
                    "Orcamento salvo com sucesso.\n\nNumero: " + orcamento.getNumeroOrcamento() +
                            "\nCliente: " + (orcamento.getClienteNome().isBlank() ? "Nao informado" : orcamento.getClienteNome()) +
                            "\nTotal final: R$ " + String.format("%.2f", orcamento.getTotalFinal())
            );
        } else {
            mostrarAlerta("Erro ao salvar orcamento.");
        }
    }

    public void carregarOrcamento(Orcamento orcamento) {
        itens.clear();
        txtClienteNome.setText(orcamento.getClienteNome() != null ? orcamento.getClienteNome() : "");
        txtClienteTelefone.setText(orcamento.getClienteTelefone() != null ? orcamento.getClienteTelefone() : "");
        txtClienteEmail.setText(orcamento.getClienteEmail() != null ? orcamento.getClienteEmail() : "");
        txtEnderecoObra.setText(orcamento.getEnderecoObra() != null ? orcamento.getEnderecoObra() : "");
        txtObservacoes.setText(orcamento.getObservacoes() != null ? orcamento.getObservacoes() : "");
        txtMargem.setText(String.format("%.2f", orcamento.getMargemLucro()));
        txtFrete.setText(String.format("%.2f", orcamento.getFrete()));
        txtCredito.setText(String.format("%.2f", orcamento.getCredito()));
        chkMaoDeObra.setSelected(orcamento.isIncluirMaoDeObra());
        txtPercentualMaoDeObra.setText(String.format("%.2f", orcamento.getPercentualMaoDeObra()));
        txtMinimoMaoDeObra.setText(String.format("%.2f", orcamento.getValorMinimoMaoDeObra()));

        itens.addAll(orcamento.getItens());
        atualizarResumo();
    }

    private void limparOrcamento() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Limpar orcamento");
        confirm.setHeaderText("Confirmar limpeza");
        confirm.setContentText("Deseja limpar todos os itens e dados do formulario?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            itens.clear();
            txtClienteNome.clear();
            txtClienteTelefone.clear();
            txtClienteEmail.clear();
            txtEnderecoObra.clear();
            txtObservacoes.clear();
            txtFrete.setText("0");
            txtCredito.setText("0");
            txtMargem.setText("30");
            chkMaoDeObra.setSelected(false);
            txtPercentualMaoDeObra.setText("0.30");
            txtMinimoMaoDeObra.setText("1200");
            limparCamposDeEntrada();
            atualizarResumo();
        }
    }

    private void gerarPDF() {
        if (itens.isEmpty()) {
            mostrarAlerta("Nao ha itens para gerar PDF.");
            return;
        }

        Orcamento orcamento = criarOrcamentoAtual();
        boolean sucesso = GeradorPDF.gerarOrcamentoPDF(orcamento);

        if (sucesso) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("PDF gerado");
            confirm.setHeaderText("Orcamento gerado com sucesso");
            confirm.setContentText("Arquivo: " + orcamento.getNumeroOrcamento() + ".pdf\nDeseja abrir o PDF agora?");

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                GeradorPDF.abrirPDF(orcamento.getNumeroOrcamento());
            }
        } else {
            mostrarAlerta("Erro ao gerar PDF.");
        }
    }

    private void limparCamposDeEntrada() {
        txtLargura.setText("1500");
        txtComprimento.setText("500");
        txtQuantidade.setText("1");
        chkAcabamentoEspecial.setSelected(false);
        txtSaiaLateralQtd.setText("0");
        txtSaiaLateralAltura.setText("0");
        txtSaiaFrontalQtd.setText("1");
        txtSaiaFrontalAltura.setText("100");
        txtFrontaoLateralQtd.setText("0");
        txtFrontaoLateralAltura.setText("0");
        txtFrontaoParedeQtd.setText("1");
        txtFrontaoParedeAltura.setText("200");
        txtPeitorilSaiaQtd.setText("0");
        txtPeitorilSaiaAltura.setText("0");
        txtNichoAltura.setText("600");
        txtNichoProfundidade.setText("120");
        txtNichoFundoQtd.setText("1");
        txtNichoAlizarLateralQtd.setText("2");
        txtNichoAlizarLateralLargura.setText("50");
        txtNichoAlizarSupInfQtd.setText("2");
        txtNichoAlizarSupInfLargura.setText("50");
        txtNichoPrateleiraQtd.setText("0");
        txtSoleiraSaiaQtd.setText("0");
        txtSoleiraSaiaAltura.setText("0");
        txtEscadaSaiaQtd.setText("1");
        txtEscadaSaiaAltura.setText("150");
        txtEscadaEspelhoQtd.setText("1");
        txtEscadaEspelhoAltura.setText("180");
        txtRequadroAltura.setText("1200");
        txtRequadroLateraisQtd.setText("2");
        txtRequadroLateraisLargura.setText("100");
        txtRequadroSupInfQtd.setText("2");
        txtRequadroSupInfLargura.setText("100");
        txtChapimSaiaQtd.setText("0");
        txtChapimSaiaAltura.setText("0");
        txtMesaSaiaLateralQtd.setText("0");
        txtMesaSaiaLateralAltura.setText("0");
        txtMesaSaiaFrontalQtd.setText("0");
        txtMesaSaiaFrontalAltura.setText("0");
        chkMesaSarim.setSelected(false);
        txtMesaPeLargura.setText("0");
        txtMesaPeComprimento.setText("0");
        txtMesaPeQtd.setText("0");
        txtCubaComprimento.setText("350");
        txtCubaProfundidade.setText("150");
        txtCubaFundoFalsoQtd.setText("0");
        txtCubaFundoFalsoLargura.setText("0");
        txtCubaFundoFalsoComprimento.setText("0");
        txtCubaMaoDeObra.setText("1000");
    }

    private String textoLimpo(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private double lerDouble(TextField field, String nome) {
        try {
            return Double.parseDouble(field.getText().replace(',', '.'));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Valor invalido para " + nome + ".");
        }
    }

    private int lerInt(TextField field, String nome) {
        try {
            return Integer.parseInt(field.getText().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Valor invalido para " + nome + ".");
        }
    }

    private double lerDoubleSemFalha(TextField field, double padrao) {
        try {
            return Double.parseDouble(field.getText().replace(',', '.'));
        } catch (Exception ex) {
            return padrao;
        }
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacao");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
