package br.com.marmoraria.service;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Orcamento;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrcamentoService {

    /**
     * Gera um relatório em formato texto (sem PDF por enquanto)
     */
    public void gerarRelatorio(Orcamento orcamento, String caminhoArquivo) {
        try {
            String conteudo = exportarParaTextoFormatado(orcamento);

            // Salva como arquivo .txt
            FileWriter writer = new FileWriter(caminhoArquivo);
            writer.write(conteudo);
            writer.close();

            System.out.println("✅ Relatório salvo em: " + caminhoArquivo);

        } catch (IOException e) {
            System.err.println("❌ Erro ao salvar relatório: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Imprime o orçamento no console
     */
    public void imprimirNoConsole(Orcamento orcamento) {
        String conteudo = exportarParaTextoFormatado(orcamento);
        System.out.println(conteudo);
    }

    /**
     * Gera um arquivo HTML do orçamento (alternativa a PDF)
     */
    public void gerarHTML(Orcamento orcamento, String caminhoArquivo) {
        try {
            String html = gerarHTMLFormatado(orcamento);
            Files.write(Paths.get(caminhoArquivo), html.getBytes());
            System.out.println("✅ HTML gerado em: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("❌ Erro ao gerar HTML: " + e.getMessage());
        }
    }

    /**
     * Exporta para texto formatado (para exibição)
     */
    public String exportarParaTextoFormatado(Orcamento orcamento) {
        StringBuilder sb = new StringBuilder();

        // Cabeçalho
        sb.append("╔══════════════════════════════════════════════════════════════╗\n");
        sb.append("║                 ORÇAMENTO DE MARMORARIA PROFISSIONAL         ║\n");
        sb.append("╚══════════════════════════════════════════════════════════════╝\n\n");

        // Informações do orçamento
        sb.append("┌──────────────────────────────────────────────────────────────┐\n");
        sb.append("│ INFORMACÕES DO ORÇAMENTO                                    │\n");
        sb.append("├──────────────────────────────────────────────────────────────┤\n");
        sb.append(String.format("│ Número: %-55s │\n", orcamento.getNumeroOrcamento()));
        sb.append(String.format("│ Data: %-57s │\n", orcamento.getDataFormatada()));
        sb.append(String.format("│ Responsável: %-50s │\n",
                orcamento.getResponsavel() != null ? orcamento.getResponsavel() : "Não informado"));
        sb.append("└──────────────────────────────────────────────────────────────┘\n\n");

        // Cliente
        sb.append("┌──────────────────────────────────────────────────────────────┐\n");
        sb.append("│ 👤 DADOS DO CLIENTE                                         │\n");
        sb.append("├──────────────────────────────────────────────────────────────┤\n");
        sb.append(String.format("│ Nome: %-56s │\n",
                orcamento.getClienteNome() != null ? orcamento.getClienteNome() : "Não informado"));
        sb.append(String.format("│ Telefone: %-53s │\n",
                orcamento.getClienteTelefone() != null ? orcamento.getClienteTelefone() : "Não informado"));
        sb.append(String.format("│ Email: %-56s │\n",
                orcamento.getClienteEmail() != null ? orcamento.getClienteEmail() : "Não informado"));
        sb.append(String.format("│ Endereço: %-53s │\n",
                orcamento.getEnderecoObra() != null ? orcamento.getEnderecoObra() : "Não informado"));
        sb.append("└──────────────────────────────────────────────────────────────┘\n\n");

        // Itens do orçamento
        if (orcamento.getItens() != null && !orcamento.getItens().isEmpty()) {
            sb.append("┌──────────────────────────────────────────────────────────────┐\n");
            sb.append("│ 📋 ITENS DO ORÇAMENTO                                       │\n");
            sb.append("├──────┬──────────────────────┬──────────┬────────┬───────────┤\n");
            sb.append("│ Item │ Material             │ Dimensões│ Qtd    │ Valor     │\n");
            sb.append("├──────┼──────────────────────┼──────────┼────────┼───────────┤\n");

            int itemNum = 1;
            for (ItemOrcamento item : orcamento.getItens()) {
                String materialNome = item.getMaterial() != null ?
                        formatarTexto(item.getMaterial().getNome(), 20) : "Sem material";
                String dimensoes = String.format("%.0fx%.0f",
                        item.getLargura(), item.getComprimento());

                sb.append(String.format("│ %-4d │ %-20s │ %-8s │ %-6.0f │ R$ %-6.2f │\n",
                        itemNum++,
                        materialNome,
                        dimensoes,
                        item.getQuantidade(),
                        item.getValorTotal()
                ));
            }

            sb.append("└──────┴──────────────────────┴──────────┴────────┴───────────┘\n\n");
        } else {
            sb.append("⚠️  Nenhum item adicionado ao orçamento\n\n");
        }

        // Resumo financeiro
        sb.append("┌──────────────────────────────────────────────────────────────┐\n");
        sb.append("│ 💰 RESUMO FINANCEIRO                                        │\n");
        sb.append("├──────────────────────────────────────────────────────────────┤\n");
        sb.append(String.format("│ Valor dos itens: %48.2f │\n", orcamento.getValorTotal()));

        if (orcamento.getMargemLucro() > 0) {
            double margem = orcamento.getValorLucro();
            sb.append(String.format("│ Margem de lucro (%.1f%%): %40.2f │\n",
                    orcamento.getMargemLucro(), margem));
            sb.append("├──────────────────────────────────────────────────────────────┤\n");
            sb.append(String.format("│ VALOR TOTAL: %50.2f │\n", orcamento.getValorComLucro()));
        } else {
            sb.append("├──────────────────────────────────────────────────────────────┤\n");
            sb.append(String.format("│ VALOR TOTAL: %50.2f │\n", orcamento.getValorTotal()));
        }
        sb.append("└──────────────────────────────────────────────────────────────┘\n\n");

        // Observações
        if (orcamento.getObservacoes() != null && !orcamento.getObservacoes().isEmpty()) {
            sb.append("┌──────────────────────────────────────────────────────────────┐\n");
            sb.append("│ 📝 OBSERVAÇÕES                                             │\n");
            sb.append("├──────────────────────────────────────────────────────────────┤\n");
            String[] observacoes = quebrarTexto(orcamento.getObservacoes(), 60);
            for (String linha : observacoes) {
                sb.append(String.format("│ %-60s │\n", linha));
            }
            sb.append("└──────────────────────────────────────────────────────────────┘\n\n");
        }

        // Rodapé
        sb.append("══════════════════════════════════════════════════════════════════\n");
        sb.append("📅 Validade: 30 dias a partir da data de emissão\n");
        sb.append("💳 Forma de pagamento: 50% na aprovação, 50% na entrega\n");
        sb.append("🚚 Prazo de entrega: A combinar após aprovação do orçamento\n");
        sb.append("📞 Contato: (11) 99999-9999 | contato@marmorariapro.com.br\n");
        sb.append("══════════════════════════════════════════════════════════════════\n");

        return sb.toString();
    }

    /**
     * Gera HTML formatado para o orçamento
     */
    private String gerarHTMLFormatado(Orcamento orcamento) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("    <meta charset='UTF-8'>\n");
        html.append("    <title>Orçamento ").append(orcamento.getNumeroOrcamento()).append("</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: Arial, sans-serif; margin: 40px; }\n");
        html.append("        .header { text-align: center; border-bottom: 2px solid #3498db; padding-bottom: 20px; }\n");
        html.append("        .section { margin: 20px 0; border: 1px solid #ddd; padding: 15px; border-radius: 5px; }\n");
        html.append("        table { width: 100%; border-collapse: collapse; margin: 10px 0; }\n");
        html.append("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
        html.append("        th { background-color: #3498db; color: white; }\n");
        html.append("        .total { background-color: #f8f9fa; font-weight: bold; padding: 10px; }\n");
        html.append("        .footer { margin-top: 30px; font-size: 12px; color: #666; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");

        // Cabeçalho
        html.append("    <div class='header'>\n");
        html.append("        <h1>ORÇAMENTO DE MARMORARIA PROFISSIONAL</h1>\n");
        html.append("        <h2>").append(orcamento.getNumeroOrcamento()).append("</h2>\n");
        html.append("        <p>Data: ").append(orcamento.getDataFormatada()).append("</p>\n");
        html.append("    </div>\n");

        // Informações
        html.append("    <div class='section'>\n");
        html.append("        <h3>📋 Informações do Orçamento</h3>\n");
        html.append("        <p><strong>Responsável:</strong> ").append(orcamento.getResponsavel()).append("</p>\n");
        html.append("    </div>\n");

        // Cliente
        html.append("    <div class='section'>\n");
        html.append("        <h3>👤 Dados do Cliente</h3>\n");
        html.append("        <p><strong>Nome:</strong> ").append(orcamento.getClienteNome()).append("</p>\n");
        html.append("        <p><strong>Telefone:</strong> ").append(orcamento.getClienteTelefone()).append("</p>\n");
        html.append("        <p><strong>Email:</strong> ").append(orcamento.getClienteEmail()).append("</p>\n");
        html.append("        <p><strong>Endereço:</strong> ").append(orcamento.getEnderecoObra()).append("</p>\n");
        html.append("    </div>\n");

        // Itens
        if (orcamento.getItens() != null && !orcamento.getItens().isEmpty()) {
            html.append("    <div class='section'>\n");
            html.append("        <h3>📦 Itens do Orçamento</h3>\n");
            html.append("        <table>\n");
            html.append("            <tr>\n");
            html.append("                <th>Item</th>\n");
            html.append("                <th>Material</th>\n");
            html.append("                <th>Dimensões (mm)</th>\n");
            html.append("                <th>Quantidade</th>\n");
            html.append("                <th>Área (m²)</th>\n");
            html.append("                <th>Valor (R$)</th>\n");
            html.append("            </tr>\n");

            int itemNum = 1;
            for (ItemOrcamento item : orcamento.getItens()) {
                html.append("            <tr>\n");
                html.append("                <td>").append(itemNum++).append("</td>\n");
                html.append("                <td>").append(item.getMaterial().getNome()).append("</td>\n");
                html.append("                <td>").append(item.getLargura()).append(" x ").append(item.getComprimento()).append("</td>\n");
                html.append("                <td>").append(item.getQuantidade()).append("</td>\n");
                html.append("                <td>").append(String.format("%.3f", item.getArea())).append("</td>\n");
                html.append("                <td>").append(String.format("R$ %.2f", item.getValorTotal())).append("</td>\n");
                html.append("            </tr>\n");
            }

            html.append("        </table>\n");
            html.append("    </div>\n");
        }

        // Resumo
        html.append("    <div class='section total'>\n");
        html.append("        <h3>💰 Resumo Financeiro</h3>\n");
        html.append("        <p><strong>Valor dos itens:</strong> R$ ").append(String.format("%.2f", orcamento.getValorTotal())).append("</p>\n");

        if (orcamento.getMargemLucro() > 0) {
            html.append("        <p><strong>Margem de lucro (").append(orcamento.getMargemLucro()).append("%):</strong> R$ ")
                    .append(String.format("%.2f", orcamento.getValorLucro())).append("</p>\n");
            html.append("        <h2>VALOR TOTAL: R$ ").append(String.format("%.2f", orcamento.getValorComLucro())).append("</h2>\n");
        } else {
            html.append("        <h2>VALOR TOTAL: R$ ").append(String.format("%.2f", orcamento.getValorTotal())).append("</h2>\n");
        }
        html.append("    </div>\n");

        // Observações
        if (orcamento.getObservacoes() != null && !orcamento.getObservacoes().isEmpty()) {
            html.append("    <div class='section'>\n");
            html.append("        <h3>📝 Observações</h3>\n");
            html.append("        <p>").append(orcamento.getObservacoes().replace("\n", "<br>")).append("</p>\n");
            html.append("    </div>\n");
        }

        // Rodapé
        html.append("    <div class='footer'>\n");
        html.append("        <hr>\n");
        html.append("        <p><strong>Validade:</strong> 30 dias a partir da data de emissão</p>\n");
        html.append("        <p><strong>Forma de pagamento:</strong> 50% na aprovação, 50% na entrega</p>\n");
        html.append("        <p><strong>Prazo de entrega:</strong> A combinar após aprovação</p>\n");
        html.append("        <p><strong>Contato:</strong> (11) 99999-9999 | contato@marmorariapro.com.br</p>\n");
        html.append("    </div>\n");

        html.append("</body>\n");
        html.append("</html>\n");

        return html.toString();
    }

    /**
     * Método auxiliar para formatar texto (limita tamanho)
     */
    private String formatarTexto(String texto, int tamanhoMaximo) {
        if (texto == null) return "";
        if (texto.length() <= tamanhoMaximo) return texto;
        return texto.substring(0, tamanhoMaximo - 3) + "...";
    }

    /**
     * Método auxiliar para quebrar texto em linhas
     */
    private String[] quebrarTexto(String texto, int tamanhoLinha) {
        if (texto == null) return new String[0];

        StringBuilder sb = new StringBuilder(texto);
        int i = 0;
        while ((i = sb.indexOf(" ", i + tamanhoLinha)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        return sb.toString().split("\n");
    }

    /**
     * Método SIMPLES para testes rápidos
     */
    public String gerarOrcamentoSimples(Orcamento orcamento) {
        return String.format(
                "Orçamento: %s\n" +
                        "Cliente: %s\n" +
                        "Itens: %d\n" +
                        "Total: R$ %.2f\n" +
                        "Com %2.0f%% de margem: R$ %.2f",
                orcamento.getNumeroOrcamento(),
                orcamento.getClienteNome(),
                orcamento.getItens().size(),
                orcamento.getValorTotal(),
                orcamento.getMargemLucro(),
                orcamento.getValorComLucro()
        );
    }
}