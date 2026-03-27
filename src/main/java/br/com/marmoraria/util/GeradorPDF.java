package br.com.marmoraria.util;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Orcamento;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.IOException;

public class GeradorPDF {

    private static final String PASTA_PDFS = "pdfs";

    // Cores
    private static final Color COR_PRIMARIA = new DeviceRgb(52, 152, 219);  // #3498db
    private static final Color COR_SECUNDARIA = new DeviceRgb(46, 204, 113); // #2ecc71
    private static final Color COR_CABECALHO = new DeviceRgb(44, 62, 80);    // #2c3e50
    private static final Color COR_TEXTO = new DeviceRgb(52, 73, 94);         // #34495e

    /**
     * Garante que a pasta de PDFs existe
     */
    public static void garantirPastaPDFs() {
        File pasta = new File(PASTA_PDFS);
        if (!pasta.exists()) {
            pasta.mkdirs();
            System.out.println("📁 Pasta de PDFs criada: " + pasta.getAbsolutePath());
        }
    }

    /**
     * Gera o PDF de um orçamento
     */
    public static boolean gerarOrcamentoPDF(Orcamento orcamento) {
        try {
            garantirPastaPDFs();

            String nomeArquivo = orcamento.getNumeroOrcamento() + ".pdf";
            String caminhoCompleto = PASTA_PDFS + File.separator + nomeArquivo;

            // Criar o documento PDF
            PdfWriter writer = new PdfWriter(caminhoCompleto);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Configurar margens
            document.setMargins(50, 50, 50, 50);

            // ========== CABEÇALHO ==========
            // Título principal
            Paragraph titulo = new Paragraph("MARMORARIA PRO")
                    .setFontSize(24)
                    .setFontColor(COR_PRIMARIA)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(titulo);

            // Subtítulo
            Paragraph subtitulo = new Paragraph("Orçamento Profissional")
                    .setFontSize(14)
                    .setFontColor(COR_TEXTO)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(subtitulo);

            document.add(new Paragraph(" ")); // Espaço

            // Linha separadora - CORRIGIDO: usar um Paragraph com linha
            Paragraph linhaSeparadora = new Paragraph("__________________________________________________")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(linhaSeparadora);

            document.add(new Paragraph(" "));

            // ========== INFORMAÇÕES DO ORÇAMENTO ==========
            Paragraph infoOrcamento = new Paragraph()
                    .add(new Text("Nº: ").setBold())
                    .add(new Text(orcamento.getNumeroOrcamento()))
                    .add(new Text("\nData: ").setBold())
                    .add(new Text(orcamento.getDataFormatada()))
                    .add(new Text("\nStatus: ").setBold())
                    .add(new Text(orcamento.getStatus()))
                    .setFontSize(11);
            document.add(infoOrcamento);

            document.add(new Paragraph(" "));

            // ========== DADOS DO CLIENTE (se houver) ==========
            if (orcamento.getClienteNome() != null && !orcamento.getClienteNome().isEmpty()) {
                Paragraph clienteTitulo = new Paragraph("DADOS DO CLIENTE")
                        .setFontSize(12)
                        .setBold()
                        .setFontColor(COR_CABECALHO);
                document.add(clienteTitulo);

                Paragraph clienteInfo = new Paragraph()
                        .add(new Text("Cliente: ").setBold())
                        .add(new Text(orcamento.getClienteNome()))
                        .add(new Text("\nTelefone: ").setBold())
                        .add(new Text(orcamento.getClienteTelefone() != null ? orcamento.getClienteTelefone() : "-"))
                        .add(new Text("\nEmail: ").setBold())
                        .add(new Text(orcamento.getClienteEmail() != null ? orcamento.getClienteEmail() : "-"))
                        .add(new Text("\nEndereço da Obra: ").setBold())
                        .add(new Text(orcamento.getEnderecoObra() != null ? orcamento.getEnderecoObra() : "-"))
                        .setFontSize(11);
                document.add(clienteInfo);
                document.add(new Paragraph(" "));
            }

            // ========== TABELA DE ITENS ==========
            Paragraph itensTitulo = new Paragraph("ITENS DO ORÇAMENTO")
                    .setFontSize(12)
                    .setBold()
                    .setFontColor(COR_CABECALHO);
            document.add(itensTitulo);

            // Criar tabela com 5 colunas
            float[] columnWidths = {35, 15, 10, 15, 25};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Cabeçalho da tabela
            table.addCell(criarCelulaCabecalho("Material"));
            table.addCell(criarCelulaCabecalho("Dimensões (mm)"));
            table.addCell(criarCelulaCabecalho("Qtd"));
            table.addCell(criarCelulaCabecalho("Área (m²)"));
            table.addCell(criarCelulaCabecalho("Total (R$)"));

            // Dados da tabela
            for (ItemOrcamento item : orcamento.getItens()) {
                table.addCell(criarCelulaDado(item.getMaterial().getNome()));
                table.addCell(criarCelulaDado(String.format("%.0f x %.0f", item.getLargura(), item.getComprimento())));
                table.addCell(criarCelulaDado(String.valueOf(item.getQuantidade())));
                table.addCell(criarCelulaDado(String.format("%.2f", item.getArea())));
                table.addCell(criarCelulaDado(String.format("R$ %.2f", item.getTotal())));
            }

            document.add(table);
            document.add(new Paragraph(" "));

            // ========== TOTAIS ==========
            // Subtotal
            Paragraph subtotal = new Paragraph()
                    .add(new Text("Subtotal: ").setBold())
                    .add(new Text(String.format("R$ %.2f", orcamento.getValorTotal())))
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(subtotal);

            // Margem de lucro
            Paragraph margem = new Paragraph()
                    .add(new Text("Margem de Lucro: ").setBold())
                    .add(new Text(String.format("%.1f%%", orcamento.getMargemLucro())))
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(margem);

            // Valor do lucro
            Paragraph valorLucro = new Paragraph()
                    .add(new Text("Valor do Lucro: ").setBold())
                    .add(new Text(String.format("R$ %.2f", orcamento.getValorLucro())))
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(valorLucro);

            // Linha separadora
            Paragraph linhaFinal = new Paragraph("__________________________________________________")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(linhaFinal);

            // Total final
            Paragraph totalFinal = new Paragraph()
                    .add(new Text("TOTAL FINAL: ").setBold().setFontSize(16))
                    .add(new Text(String.format("R$ %.2f", orcamento.getValorComLucro())).setBold().setFontSize(16).setFontColor(COR_SECUNDARIA))
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(totalFinal);

            document.add(new Paragraph(" "));

            // ========== OBSERVAÇÕES ==========
            if (orcamento.getObservacoes() != null && !orcamento.getObservacoes().isEmpty()) {
                Paragraph obsTitulo = new Paragraph("OBSERVAÇÕES")
                        .setFontSize(12)
                        .setBold()
                        .setFontColor(COR_CABECALHO);
                document.add(obsTitulo);

                Paragraph observacoes = new Paragraph(orcamento.getObservacoes())
                        .setFontSize(10)
                        .setFontColor(COR_TEXTO);
                document.add(observacoes);
                document.add(new Paragraph(" "));
            }

            // ========== RODAPÉ ==========
            Paragraph linhaRodape = new Paragraph("__________________________________________________")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(linhaRodape);

            Paragraph rodape = new Paragraph()
                    .add("Este orçamento é válido por 30 dias.\n")
                    .add("Para mais informações, entre em contato conosco.\n\n")
                    .add("Marmoraria Pro - Qualidade e Profissionalismo")
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(COR_TEXTO);
            document.add(rodape);

            // Fechar documento
            document.close();

            System.out.println("✅ PDF gerado: " + caminhoCompleto);
            return true;

        } catch (IOException e) {
            System.err.println("❌ Erro ao gerar PDF: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cria uma célula de cabeçalho para a tabela
     */
    private static Cell criarCelulaCabecalho(String texto) {
        return new Cell()
                .add(new Paragraph(texto).setBold())
                .setBackgroundColor(COR_CABECALHO)
                .setFontColor(DeviceRgb.WHITE)
                .setPadding(8);
    }

    /**
     * Cria uma célula de dado para a tabela
     */
    private static Cell criarCelulaDado(String texto) {
        return new Cell()
                .add(new Paragraph(texto))
                .setPadding(6);
    }

    /**
     * Obtém o caminho da pasta de PDFs
     */
    public static String getPastaPDFs() {
        return new File(PASTA_PDFS).getAbsolutePath();
    }

    /**
     * Abre o PDF gerado
     */
    public static void abrirPDF(String numeroOrcamento) {
        try {
            File pdfFile = new File(PASTA_PDFS, numeroOrcamento + ".pdf");
            if (pdfFile.exists()) {
                java.awt.Desktop.getDesktop().open(pdfFile);
                System.out.println("📄 PDF aberto: " + pdfFile.getAbsolutePath());
            } else {
                System.out.println("❌ Arquivo PDF não encontrado: " + pdfFile.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao abrir PDF: " + e.getMessage());
        }
    }
}