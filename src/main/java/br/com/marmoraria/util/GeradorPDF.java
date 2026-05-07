package br.com.marmoraria.util;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Orcamento;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.IOException;

public class GeradorPDF {

    private static final String PASTA_PDFS = "pdfs";

    private static final Color COR_PRIMARIA = new DeviceRgb(52, 152, 219);
    private static final Color COR_SECUNDARIA = new DeviceRgb(46, 204, 113);
    private static final Color COR_CABECALHO = new DeviceRgb(44, 62, 80);
    private static final Color COR_TEXTO = new DeviceRgb(52, 73, 94);

    public static void garantirPastaPDFs() {
        File pasta = new File(PASTA_PDFS);
        if (!pasta.exists()) {
            pasta.mkdirs();
            System.out.println("Pasta de PDFs criada: " + pasta.getAbsolutePath());
        }
    }

    public static boolean gerarOrcamentoPDF(Orcamento orcamento) {
        try {
            garantirPastaPDFs();

            String nomeArquivo = orcamento.getNumeroOrcamento() + ".pdf";
            String caminhoCompleto = PASTA_PDFS + File.separator + nomeArquivo;

            PdfWriter writer = new PdfWriter(caminhoCompleto);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            document.setMargins(50, 50, 50, 50);

            document.add(new Paragraph("MARMORARIA PRO")
                    .setFontSize(24)
                    .setFontColor(COR_PRIMARIA)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Orcamento Profissional")
                    .setFontSize(14)
                    .setFontColor(COR_TEXTO)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("__________________________________________________").setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(" "));

            Paragraph infoOrcamento = new Paragraph()
                    .add(new Text("Numero: ").setBold())
                    .add(new Text(orcamento.getNumeroOrcamento()))
                    .add(new Text("\nData: ").setBold())
                    .add(new Text(orcamento.getDataFormatada()))
                    .add(new Text("\nStatus: ").setBold())
                    .add(new Text(orcamento.getStatus()))
                    .setFontSize(11);
            document.add(infoOrcamento);

            if (orcamento.getClienteNome() != null && !orcamento.getClienteNome().isEmpty()) {
                document.add(new Paragraph(" "));
                document.add(new Paragraph("DADOS DO CLIENTE").setBold().setFontSize(12).setFontColor(COR_CABECALHO));

                Paragraph cliente = new Paragraph()
                        .add(new Text("Cliente: ").setBold()).add(new Text(orcamento.getClienteNome()))
                        .add(new Text("\nTelefone: ").setBold()).add(new Text(valorOuTraco(orcamento.getClienteTelefone())))
                        .add(new Text("\nEmail: ").setBold()).add(new Text(valorOuTraco(orcamento.getClienteEmail())))
                        .add(new Text("\nEndereco da Obra: ").setBold()).add(new Text(valorOuTraco(orcamento.getEnderecoObra())))
                        .setFontSize(11);
                document.add(cliente);
            }

            document.add(new Paragraph(" "));
            document.add(new Paragraph("ITENS DO ORCAMENTO").setBold().setFontSize(12).setFontColor(COR_CABECALHO));

            float[] larguras = {14, 19, 29, 8, 12, 18};
            Table table = new Table(UnitValue.createPercentArray(larguras));
            table.setWidth(UnitValue.createPercentValue(100));

            table.addCell(criarCelulaCabecalho("Tipo"));
            table.addCell(criarCelulaCabecalho("Material"));
            table.addCell(criarCelulaCabecalho("Descricao"));
            table.addCell(criarCelulaCabecalho("Qtd"));
            table.addCell(criarCelulaCabecalho("Area"));
            table.addCell(criarCelulaCabecalho("Total"));

            for (ItemOrcamento item : orcamento.getItens()) {
                table.addCell(criarCelulaDado(item.getTipoTrabalho().getDescricao()));
                table.addCell(criarCelulaDado(item.getMaterial().getNome()));
                table.addCell(criarCelulaDado(item.getDescricao()));
                table.addCell(criarCelulaDado(String.valueOf(item.getQuantidade())));
                table.addCell(criarCelulaDado(String.format("%.2f m2", item.getArea())));
                table.addCell(criarCelulaDado(String.format("R$ %.2f", item.getTotal())));
            }

            document.add(table);
            document.add(new Paragraph(" "));

            document.add(criarLinhaResumo("Subtotal dos itens", orcamento.getValorTotal()));
            document.add(new Paragraph()
                    .add(new Text("Margem de lucro: ").setBold())
                    .add(new Text(String.format("%.1f%%", orcamento.getMargemLucro())))
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(criarLinhaResumo("Subtotal com margem", orcamento.getValorComLucro()));
            document.add(criarLinhaResumo("Mao de obra", orcamento.getValorMaoDeObra()));
            document.add(criarLinhaResumo("Frete", orcamento.getFrete()));
            document.add(criarLinhaResumo("Credito", orcamento.getCredito()));

            document.add(new Paragraph("__________________________________________________").setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph()
                    .add(new Text("TOTAL FINAL: ").setBold().setFontSize(16))
                    .add(new Text(String.format("R$ %.2f", orcamento.getTotalFinal()))
                            .setBold()
                            .setFontSize(16)
                            .setFontColor(COR_SECUNDARIA))
                    .setTextAlignment(TextAlignment.RIGHT));

            if (orcamento.getObservacoes() != null && !orcamento.getObservacoes().isEmpty()) {
                document.add(new Paragraph(" "));
                document.add(new Paragraph("OBSERVACOES").setBold().setFontSize(12).setFontColor(COR_CABECALHO));
                document.add(new Paragraph(orcamento.getObservacoes()).setFontSize(10).setFontColor(COR_TEXTO));
            }

            document.add(new Paragraph(" "));
            document.add(new Paragraph("__________________________________________________").setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Este orcamento e valido por 30 dias.\nPara mais informacoes, entre em contato conosco.\n\nMarmoraria Pro - Qualidade e Profissionalismo")
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(COR_TEXTO));

            document.close();
            System.out.println("PDF gerado: " + caminhoCompleto);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao gerar PDF: " + e.getMessage());
            return false;
        }
    }

    private static Paragraph criarLinhaResumo(String titulo, double valor) {
        return new Paragraph()
                .add(new Text(titulo + ": ").setBold())
                .add(new Text(String.format("R$ %.2f", valor)))
                .setTextAlignment(TextAlignment.RIGHT);
    }

    private static String valorOuTraco(String valor) {
        return valor == null || valor.isBlank() ? "-" : valor;
    }

    private static Cell criarCelulaCabecalho(String texto) {
        return new Cell()
                .add(new Paragraph(texto).setBold())
                .setBackgroundColor(COR_CABECALHO)
                .setFontColor(DeviceRgb.WHITE)
                .setPadding(8);
    }

    private static Cell criarCelulaDado(String texto) {
        return new Cell()
                .add(new Paragraph(texto))
                .setPadding(6);
    }

    public static String getPastaPDFs() {
        return new File(PASTA_PDFS).getAbsolutePath();
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
}
