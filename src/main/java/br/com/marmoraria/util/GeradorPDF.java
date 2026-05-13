package br.com.marmoraria.util;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Orcamento;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class GeradorPDF {

    private static final String PASTA_PDFS = "pdfs";

    // Cores exatas da planilha Helomar
    private static final Color AZUL_MARINHO = new DeviceRgb(0, 51, 102);      // Cabecalho
    private static final Color PRETO = new DeviceRgb(0, 0, 0);
    private static final Color BRANCO = new DeviceRgb(255, 255, 255);
    private static final Color CINZA_CLARO = new DeviceRgb(230, 230, 230);    // Fundo alternado
    private static final Color CINZA_BORDA = new DeviceRgb(180, 180, 180);    // Bordas

    private static final double VALOR_MINIMO_COLOCACAO = 1200.00;
    private static final double VALOR_MINIMO_CUBA = 1000.00;
    private static final double PERCENTUAL_MDO = 0.30;

    public static boolean gerarOrcamentoPDF(Orcamento orcamento) {
        try {
            garantirPastaPDFs();

            String nomeArquivo = orcamento.getNumeroOrcamento() + ".pdf";
            String caminhoCompleto = PASTA_PDFS + File.separator + nomeArquivo;

            PdfWriter writer = new PdfWriter(caminhoCompleto);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc, PageSize.A4);
            doc.setMargins(30, 30, 30, 30);

            PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fontSmall = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // ═══════════════════════════════════════════════════════
            // CABECALHO HELOMAR (Logo + Subtitulo)
            // ═══════════════════════════════════════════════════════

            // Nome da empresa em destaque
            Paragraph logo = new Paragraph();
            logo.add(new Text("HELOMAR")
                    .setFont(fontBold)
                    .setFontSize(18)
                    .setFontColor(AZUL_MARINHO));
            logo.setTextAlignment(TextAlignment.CENTER);
            doc.add(logo);

            // Subtitulo
            Paragraph subtitulo = new Paragraph();
            subtitulo.add(new Text("Marmores | Granitos | Pedras Decorativas")
                    .setFont(fontSmall)
                    .setFontSize(8)
                    .setFontColor(AZUL_MARINHO));
            subtitulo.setTextAlignment(TextAlignment.CENTER);
            doc.add(subtitulo);

            // Linha separadora azul
            Paragraph linhaAzul = new Paragraph("_")
                    .setFont(fontSmall)
                    .setFontSize(1)
                    .setFontColor(AZUL_MARINHO)
                    .setFixedLeading(0);
            doc.add(linhaAzul);

            doc.add(espaco(8));

            // ═══════════════════════════════════════════════════════
            // DATA E DESTINATARIO
            // ═══════════════════════════════════════════════════════

            String dataExtenso = gerarDataPorExtenso();
            doc.add(new Paragraph(dataExtenso)
                    .setFont(fontNormal).setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT));

            doc.add(espaco(6));

            doc.add(new Paragraph("Ao").setFont(fontNormal).setFontSize(10));

            String cliente = orcamento.getClienteNome();
            if (cliente == null || cliente.trim().isEmpty()) {
                cliente = "________________________________";
            }
            doc.add(new Paragraph(cliente).setFont(fontNormal).setFontSize(10));

            String endereco = orcamento.getEnderecoObra();
            if (endereco != null && !endereco.trim().isEmpty()) {
                doc.add(new Paragraph(endereco).setFont(fontNormal).setFontSize(10));
            }

            doc.add(espaco(8));

            // Texto de apresentacao
            String textoApres = "Conforme sua solicitacao, apresentamos a seguir proposta " +
                    "para apreciacao de V.S.a, quanto ao fornecimento de granito para sua " +
                    "obra. Informamos ainda, prazo de entrega e condicoes de pagamento.";
            doc.add(new Paragraph(textoApres)
                    .setFont(fontNormal).setFontSize(9)
                    .setTextAlignment(TextAlignment.JUSTIFIED));

            doc.add(espaco(10));

            // ═══════════════════════════════════════════════════════
            // TABELA DE ITENS (DISCRIMINACAO | VALOR)
            // ═══════════════════════════════════════════════════════

            Table tabelaItens = new Table(new float[]{320, 100});
            tabelaItens.setWidth(UnitValue.createPercentValue(100));

            // Cabecalho azul marinho
            Cell hdrDesc = new Cell();
            hdrDesc.add(new Paragraph("DISCRIMINACAO")
                    .setFont(fontBold).setFontSize(8)
                    .setFontColor(BRANCO)
                    .setTextAlignment(TextAlignment.CENTER));
            hdrDesc.setBackgroundColor(AZUL_MARINHO);
            hdrDesc.setBorder(new SolidBorder(PRETO, 0.5f));
            hdrDesc.setPadding(5);
            tabelaItens.addCell(hdrDesc);

            Cell hdrValor = new Cell();
            hdrValor.add(new Paragraph("VALOR")
                    .setFont(fontBold).setFontSize(8)
                    .setFontColor(BRANCO)
                    .setTextAlignment(TextAlignment.CENTER));
            hdrValor.setBackgroundColor(AZUL_MARINHO);
            hdrValor.setBorder(new SolidBorder(PRETO, 0.5f));
            hdrValor.setPadding(5);
            tabelaItens.addCell(hdrValor);

            // Itens
            boolean zebra = false;
            for (ItemOrcamento item : orcamento.getItens()) {
                String desc = formatarDescricao(item);

                Cell cDesc = new Cell();
                cDesc.add(new Paragraph(desc)
                        .setFont(fontSmall).setFontSize(7.5f)
                        .setTextAlignment(TextAlignment.LEFT));
                cDesc.setBorder(new SolidBorder(CINZA_BORDA, 0.3f));
                cDesc.setPadding(4);
                if (zebra) cDesc.setBackgroundColor(CINZA_CLARO);
                tabelaItens.addCell(cDesc);

                Cell cValor = new Cell();
                cValor.add(new Paragraph(formatarMoeda(item.getTotal()))
                        .setFont(fontSmall).setFontSize(7.5f)
                        .setTextAlignment(TextAlignment.RIGHT));
                cValor.setBorder(new SolidBorder(CINZA_BORDA, 0.3f));
                cValor.setPadding(4);
                cValor.setVerticalAlignment(VerticalAlignment.MIDDLE);
                if (zebra) cValor.setBackgroundColor(CINZA_CLARO);
                tabelaItens.addCell(cValor);

                zebra = !zebra;
            }

            doc.add(tabelaItens);
            doc.add(espaco(4));

            // ═══════════════════════════════════════════════════════
            // TABELA DE TOTAIS (alinhada a direita)
            // ═══════════════════════════════════════════════════════

            Table tabelaTotais = new Table(new float[]{180, 90});
            tabelaTotais.setWidth(UnitValue.createPercentValue(55));
            tabelaTotais.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            tabelaTotais.setMarginTop(4);

            // TOTAL MATERIAL
            addLinhaTotal(tabelaTotais, "TOTAL MATERIAL", orcamento.getValorTotal(),
                    fontBold, fontBold, 8, true);

            // M.O COLOCACAO
            double mdo = 0;
            if (orcamento.isIncluirMaoDeObra()) {
                mdo = Math.max(orcamento.getValorComLucro() * PERCENTUAL_MDO,
                        VALOR_MINIMO_COLOCACAO);
            }
            addLinhaTotal(tabelaTotais, "M.O  COLOCACAO", mdo,
                    fontNormal, fontNormal, 8, false);

            // CREDITO
            if (orcamento.getCredito() > 0) {
                addLinhaTotal(tabelaTotais, "CREDITO", -orcamento.getCredito(),
                        fontNormal, fontNormal, 8, false);
            } else {
                addLinhaTotal(tabelaTotais, "CREDITO", 0,
                        fontNormal, fontNormal, 8, false);
            }

            // FRETE
            if (orcamento.getFrete() > 0) {
                addLinhaTotal(tabelaTotais, "FRETE", orcamento.getFrete(),
                        fontNormal, fontNormal, 8, false);
            } else {
                addLinhaTotal(tabelaTotais, "FRETE", 0,
                        fontNormal, fontNormal, 8, false);
            }

            // Linha em branco antes do total
            addLinhaVazia(tabelaTotais);

            // TOTAL FINAL
            double totalFinal = orcamento.getTotalFinal() + mdo + orcamento.getFrete()
                    - orcamento.getCredito();
            addLinhaTotal(tabelaTotais, "TOTAL", totalFinal,
                    fontBold, fontBold, 10, true);

            doc.add(tabelaTotais);
            doc.add(espaco(12));

            // ═══════════════════════════════════════════════════════
            // CONDICOES
            // ═══════════════════════════════════════════════════════

            String[] condicoes = {
                    ". Prazo de entrega: A combinar.",
                    ". Condicoes de pgto:  A combinar.",
                    ". Frete: R$ " + formatarMoeda(orcamento.getFrete()) +
                            " por conta do cliente.",
                    ". Nao fornecemos cubas de louca, torneiras e acessorios. " +
                            "Nao fazemos furo de torneira em cubas de louca, somente em pedra.",
                    ". Todo material necessario para colocacao e de responsabilidade " +
                            "do cliente.",
                    ". Para deposito:  Banco Itau ag.9285 - conta 99559-4 - FG MONTES ME " +
                            "- PIX :  marmorariahelomar@gmail.com",
                    ". (*) Validade da proposta: 20 dias para materiais nacionais.",
                    ". (**) Validade da proposta: 02 dias para materiais importados " +
                            "em funcao da variacao do dolar/euro.",
            };

            for (int i = 0; i < condicoes.length; i++) {
                String num = (i < 5) ? String.valueOf(i + 1) : "";
                String texto = num.isEmpty() ? condicoes[i] : condicoes[i];
                doc.add(new Paragraph(texto)
                        .setFont(fontSmall).setFontSize(7.5f)
                        .setMultipliedLeading(1.2f));
            }

            doc.add(espaco(8));

            // Linha separadora
            doc.add(new Paragraph(". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .")
                    .setFont(fontSmall).setFontSize(5)
                    .setFontColor(CINZA_BORDA)
                    .setTextAlignment(TextAlignment.CENTER));
            doc.add(espaco(6));

            // ═══════════════════════════════════════════════════════
            // OBSERVACOES
            // ═══════════════════════════════════════════════════════

            doc.add(new Paragraph("OBSERVACOES:")
                    .setFont(fontBold).setFontSize(8)
                    .setFontColor(AZUL_MARINHO)
                    .setUnderline());
            doc.add(espaco(4));

            String[] observacoes = gerarObservacoes();
            for (int i = 0; i < observacoes.length; i++) {
                doc.add(new Paragraph((i + 1) + ". " + observacoes[i])
                        .setFont(fontSmall).setFontSize(6.5f)
                        .setMultipliedLeading(1.15f)
                        .setTextAlignment(TextAlignment.JUSTIFIED));
            }

            doc.add(espaco(10));

            // ═══════════════════════════════════════════════════════
            // RODAPE COM ENDERECO
            // ═══════════════════════════════════════════════════════

            Paragraph enderecoRodape = new Paragraph();
            enderecoRodape.add(new Text(
                    "Rua Jacamim, n8 - Jardim Anhanga - Duque de Caxias - RJ - " +
                            "Tels: (021) 98371-0067 / 98090-0260")
                    .setFont(fontSmall).setFontSize(6.5f)
                    .setFontColor(CINZA_BORDA));
            enderecoRodape.setTextAlignment(TextAlignment.CENTER);
            doc.add(enderecoRodape);

            doc.add(espaco(16));

            // ═══════════════════════════════════════════════════════
            // ASSINATURAS
            // ═══════════════════════════════════════════════════════

            doc.add(new Paragraph("Atenciosamente,")
                    .setFont(fontNormal).setFontSize(9));
            doc.add(espaco(20));

            doc.add(new Paragraph("Marmoraria Helomar")
                    .setFont(fontBold).setFontSize(10)
                    .setFontColor(AZUL_MARINHO)
                    .setTextAlignment(TextAlignment.CENTER));
            doc.add(espaco(20));

            doc.add(new Paragraph(
                    "Declaro que li e entendi as observacoes da marmoraria " +
                            "para o inicio do meu trabalho.")
                    .setFont(fontSmall).setFontSize(8));
            doc.add(espaco(14));

            // Tabela de aceite e data
            Table tabAss = new Table(new float[]{250, 250});
            tabAss.setWidth(UnitValue.createPercentValue(100));

            Cell assEsq = new Cell();
            assEsq.add(new Paragraph(
                    "Aceite do cliente:\n________________________________")
                    .setFont(fontSmall).setFontSize(8));
            assEsq.setBorder(Border.NO_BORDER);
            tabAss.addCell(assEsq);

            String dataHoje = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Cell assDir = new Cell();
            assDir.add(new Paragraph(
                    "Data: " + dataHoje + "\nCPF/CNPJ: ________________________________")
                    .setFont(fontSmall).setFontSize(8)
                    .setTextAlignment(TextAlignment.RIGHT));
            assDir.setBorder(Border.NO_BORDER);
            tabAss.addCell(assDir);

            doc.add(tabAss);

            doc.close();
            System.out.println("PDF Helomar gerado: " + caminhoCompleto);
            return true;

        } catch (IOException e) {
            System.err.println("Erro ao gerar PDF: " + e.getMessage());
            return false;
        }
    }

    private static void addLinhaTotal(Table tabela, String desc, double valor,
                                      PdfFont fontDesc, PdfFont fontValor,
                                      int fontSize, boolean destaque) {
        Cell c1 = new Cell();
        c1.add(new Paragraph(desc)
                .setFont(fontDesc).setFontSize(fontSize)
                .setTextAlignment(TextAlignment.LEFT));
        c1.setBorder(new SolidBorder(CINZA_BORDA, 0.3f));
        c1.setPadding(3);
        if (destaque) c1.setBackgroundColor(CINZA_CLARO);
        tabela.addCell(c1);

        String valorStr = valor < 0 ?
                String.format("-%,.2f", Math.abs(valor)) :
                String.format("%,.2f", valor);

        Cell c2 = new Cell();
        c2.add(new Paragraph(valorStr)
                .setFont(fontValor).setFontSize(fontSize)
                .setTextAlignment(TextAlignment.RIGHT));
        c2.setBorder(new SolidBorder(CINZA_BORDA, 0.3f));
        c2.setPadding(3);
        if (destaque) c2.setBackgroundColor(CINZA_CLARO);
        tabela.addCell(c2);
    }

    private static void addLinhaVazia(Table tabela) {
        Cell c1 = new Cell();
        c1.add(new Paragraph("").setFontSize(2));
        c1.setBorder(Border.NO_BORDER);
        tabela.addCell(c1);
        Cell c2 = new Cell();
        c2.add(new Paragraph("").setFontSize(2));
        c2.setBorder(Border.NO_BORDER);
        tabela.addCell(c2);
    }

    private static String formatarDescricao(ItemOrcamento item) {
        double largM = item.getLargura() / 1000;
        double compM = item.getComprimento() / 1000;
        int qtd = item.getQuantidade();
        String material = item.getMaterial().getNome();
        String tipo = item.getTipoTrabalho().getDescricao();

        if (qtd > 1) {
            return String.format("%d %ss de %.2fx%.2fm2 (%s)",
                    qtd, tipo, largM, compM, material);
        }
        return String.format("%s: %.2fx%.2fm2 (%s)",
                tipo, largM, compM, material);
    }

    private static String gerarDataPorExtenso() {
        LocalDate hoje = LocalDate.now();
        String dia = hoje.format(DateTimeFormatter.ofPattern("dd"));
        String mes = hoje.format(DateTimeFormatter.ofPattern("MMMM",
                new Locale("pt", "BR"))).toLowerCase();
        String ano = hoje.format(DateTimeFormatter.ofPattern("yyyy"));
        return "Rio de Janeiro, " + dia + " de " + mes + " de " + ano;
    }

    private static String formatarMoeda(double valor) {
        return String.format("%,.2f", valor);
    }

    private static Paragraph espaco(float tamanho) {
        return new Paragraph("").setFontSize(tamanho);
    }

    private static String[] gerarObservacoes() {
        return new String[]{
                "Todo o faturamento tera como base os valores de m2 e ml, " +
                        "podendo haver modificacoes apos medicao no local.",
                "Todo o material aqui proposto e de origem mineral, portanto " +
                        "esta sujeito a veios e variacoes de tonalidades. " +
                        "(*) Este prazo muda se o material for nanoglass ou marmoglass " +
                        "que passa a ser de ate 30 dias uteis. Se tiver cuba esculpida, " +
                        "entao passa para ate 35 dias uteis.",
                "Valor minimo cobrado para colocacao e ou reparo e de " +
                        String.format("R$ %,.2f", VALOR_MINIMO_COLOCACAO) +
                        " (Hum mil e duzentos reais);",
                "Toda cuba esculpida e trabalho artesanal e sendo assim, o valor " +
                        "cobrado parte de " + String.format("R$ %,.2f", VALOR_MINIMO_CUBA) +
                        " (Hum mil reais);",
                "E necessario que o colocador ou responsavel pela colocacao " +
                        "esteja(m) no local para acompanhar a medicao que sera realizada " +
                        "em uma unica etapa e cada profissional tem uma maneira propria " +
                        "de trabalhar;",
                "Caso o cliente nao tenha nenhum destes profissionais contratado, " +
                        "ele acompanhara e dara o OK nas medicoes e acabamentos, para " +
                        "que o mesmo possa explicar a quem for fazer a colocacao;",
                "Caso haja necessidade de uma visita de nossos profissionais por " +
                        "ocasiao da colocacao para orientar o colocador do cliente, sera " +
                        "cobrada a visita no valor de R$ 350,00 (Trezentos e cinquenta reais);",
                "Todos os cortes para ajustes de colocacao (ex.: furos para tomada, " +
                        "ralos, meia esquadria de frontispicios, recortes de pedras para " +
                        "ajustes de esquadro de ambientes, etc.), serao feitos pelo " +
                        "colocador, salvo servicos combinados previamente e cobrados de " +
                        "acordo com a nossa tabela de servicos;",
                "Todo material necessario para colocacao (areia, saibro, cimento, " +
                        "grampo, cola, tubos de PU, material para rejunte, tubos de aco " +
                        "galvanizado, andaimes e demais) sera fornecido pelo cliente;",
                "A colocacao de marmores e granitos nao abrange troca de azulejos, " +
                        "reparos de pinturas e rebocos, instalacoes hidraulicas e demais " +
                        "reparos, restringindo-se apenas aos materiais adquiridos na marmoraria;",
                "A colocacao de cubas de sobrepor ou de apoio, furos de torneiras " +
                        "e cooktop sera feita por nossos profissionais depois das bancadas " +
                        "serem colocadas, com agendamento previo e sera necessaria a " +
                        "presenca do proprietario ou arquiteto responsavel pela obra. " +
                        "A nao observacao desta clausula implicara numa nova visita e esta " +
                        "sera cobrada o valor minimo de R$ 350,00 (Trezentos e cinquenta reais).",
                "A nossa entrega sera avisada na vespera e e feita dentro do horario " +
                        "comercial (08 as 18 horas), nao e possivel marcar horario.",
                "Quantidades fornecidas por V.Sa., e orcadas com medidas retiradas " +
                        "das plantas podendo variar depois da medicao no local. O orcamento " +
                        "sera refeito, prevalecendo sempre o valor do m2 utilizado para o " +
                        "calculo dos servicos.",
                "Os materiais que forem solicitados e entregues que estejam fora do " +
                        "deste orcamento, serao entregues acompanhados de boletos bancarios " +
                        "com vencimentos no maximo de 10 dias apos a entrega;",
                "Todo material deve ser conferido no ato da entrega, pois nao serao " +
                        "aceitas reclamacoes posteriores. Eventuais reclamacoes deverao " +
                        "ser descritas no verso do pedido;",
                "Nao sera feita a troca de pecas apos elas terem sido colocadas " +
                        "e/ou quebradas;",
                "Todas as mercadorias (pecas) a serem entregues em locais de dificil " +
                        "acesso (que nao possam ser transportadas pelo elevador), que " +
                        "necessite icar por meio de cordas ou equipamento especifico, " +
                        "sera de inteira responsabilidade do cliente contratante;",
                "O cliente ou seu preposto devera identificar-se devidamente e " +
                        "assinar o canhoto do pedido. O recebimento da mercadoria ou " +
                        "servicos implica no reconhecimento, pelo destinatario, de que " +
                        "recebeu, examinou e aprovou os mesmos;",
                "O prazo de entrega do pedido sera de ate 08 dias (uteis) " +
                        "considerados apos a medicao pela Helomar de todo o material " +
                        "para os peitoris, ilhargas, tentos, soleiras e filetes, e de " +
                        "20 dias (uteis) para as bancadas, pisos, forracoes e chapins " +
                        "de banheira. Se houver armarios sob as bancadas da cozinha " +
                        "e/ou banheiros, mediremos apos sua montagem.",
                "O prazo passa a contar apos todas as condicoes de pagamentos " +
                        "cumpridas, o contrato assinado, os desenhos aprovados com as " +
                        "medicoes feitas por nosso profissional no local, as cubas " +
                        "entregues e o material para corte aprovado pelo Cliente.",
                "Se o cliente cancelar o servico apos recebermos o sinal, sera " +
                        "devolvido o dinheiro descontando-se 30% do total do orcamento;",
                "Nao nos responsabilizamos pelo mau uso dos materiais como por " +
                        "exemplo: uso de produtos de limpeza agressivos, sabao em po, " +
                        "acidos e etc; que podem causar manchas nas pecas. Pedras devem " +
                        "ser limpas com agua e sabao neutro.",
                "O granito e o marmore sao materiais provenientes da natureza, " +
                        "portanto sujeitos a variacoes de cor, veios, desenhos e/ou " +
                        "manchas e e por isso que 02 pecas nunca serao exatamente " +
                        "iguais. Por estas formacoes naturais nao poderao ser recusados " +
                        "ou devolvidos;",
                "Materiais sinteticos tambem sofrem variacao de cor como " +
                        "porcelanatos, ceramicas etc que devem ser comprados todos no " +
                        "mesmo lote.",
        };
    }

    private static void garantirPastaPDFs() {
        File pasta = new File(PASTA_PDFS);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }
    }

    public static void abrirPDF(String numeroOrcamento) {
        try {
            File pdfFile = new File(PASTA_PDFS, numeroOrcamento + ".pdf");
            if (pdfFile.exists()) {
                java.awt.Desktop.getDesktop().open(pdfFile);
            }
        } catch (Exception e) {
            System.err.println("Erro ao abrir PDF: " + e.getMessage());
        }
    }

    public static String getPastaPDFs() {
        return new File(PASTA_PDFS).getAbsolutePath();
    }
}