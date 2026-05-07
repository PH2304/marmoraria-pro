package br.com.marmoraria.util;

import br.com.marmoraria.model.Orcamento;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Gera o contrato e observações baseado na planilha Marmoraria Helomar.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class GeradorContrato {

    // Constantes financeiras
    public static final double VALOR_MINIMO_COLOCACAO = 1200.00;
    public static final double VALOR_MINIMO_CUBA_ESCULPIDA = 1000.00;
    public static final double VALOR_VISITA_TECNICA = 350.00;
    public static final int PRAZO_VALIDADE_NACIONAL = 20;
    public static final int PRAZO_VALIDADE_IMPORTADO = 2;
    public static final int PRAZO_ENTREGA_PEITORIS = 8;
    public static final int PRAZO_ENTREGA_BANCADAS = 20;
    public static final int PRAZO_NANOGLASS = 30;
    public static final int PRAZO_CUBA_ESCULPIDA = 35;
    public static final double TAXA_CANCELAMENTO = 30.0;

    /**
     * Gera a data por extenso (estilo planilha)
     * Ex: "Rio de Janeiro, 06 de maio de 2026"
     */
    public static String getDataPorExtenso() {
        LocalDate hoje = LocalDate.now();
        DateTimeFormatter diaFormat = DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter mesFormat = DateTimeFormatter.ofPattern("MMMM", new Locale("pt", "BR"));
        DateTimeFormatter anoFormat = DateTimeFormatter.ofPattern("yyyy");

        String dia = hoje.format(diaFormat);
        String mes = hoje.format(mesFormat).toLowerCase();
        String ano = hoje.format(anoFormat);

        return "Rio de Janeiro, " + dia + " de " + mes + " de " + ano;
    }

    /**
     * Gera o cabeçalho do contrato
     */
    public static String gerarCabecalhoContrato(Orcamento orcamento) {
        StringBuilder sb = new StringBuilder();

        sb.append(getDataPorExtenso());
        sb.append("\n\n");
        sb.append("Ao\n");
        sb.append("Prezado(a) Cliente,\n\n");
        sb.append("Conforme sua solicitação, apresentamos a seguir proposta ");
        sb.append("para apreciação de V.S.ª, quanto ao fornecimento de granito ");
        sb.append("para sua obra. Informamos ainda, prazo de entrega e ");
        sb.append("condições de pagamento.\n\n");

        return sb.toString();
    }

    /**
     * Gera as observações completas do contrato
     */
    public static String gerarObservacoesCompletas(Orcamento orcamento) {
        StringBuilder sb = new StringBuilder();

        sb.append("=================================================================\n");
        sb.append("                    OBSERVAÇÕES IMPORTANTES\n");
        sb.append("=================================================================\n\n");

        // 1. Prazos
        sb.append("1. PRAZOS:\n\n");
        sb.append("   a) Prazo de entrega: A combinar.\n");
        sb.append("   b) Condições de pagamento: A combinar.\n");
        sb.append("   c) Frete: R$ ");
        sb.append(String.format("%.2f", orcamento.getFrete()));
        sb.append(" por conta do cliente.\n");
        sb.append("   d) Validade da proposta: ");
        sb.append(PRAZO_VALIDADE_NACIONAL);
        sb.append(" dias para materiais nacionais.\n");
        sb.append("   e) Validade da proposta: ");
        sb.append(PRAZO_VALIDADE_IMPORTADO);
        sb.append(" dias para materiais importados ");
        sb.append("em função da variação do dólar/euro.\n");
        sb.append("   f) Este prazo muda se o material for nanoglass ou marmoglass ");
        sb.append("que passa a ser de até ");
        sb.append(PRAZO_NANOGLASS);
        sb.append(" dias úteis.\n");
        sb.append("   g) Se tiver cuba esculpida, então passa para até ");
        sb.append(PRAZO_CUBA_ESCULPIDA);
        sb.append(" dias úteis.\n\n");

        // 2. Materiais
        sb.append("2. MATERIAIS:\n\n");
        sb.append("   a) Não fornecemos cubas de louça, torneiras e acessórios.\n");
        sb.append("   b) Não fazemos furo de torneira em cubas de louça, somente em pedra.\n");
        sb.append("   c) Todo material necessário para colocação é de responsabilidade ");
        sb.append("do cliente.\n");
        sb.append("   d) O granito e o mármore são materiais provenientes da natureza, ");
        sb.append("portanto sujeitos a variações de cor, veios, desenhos e/ou manchas.\n");
        sb.append("   e) Materiais sintéticos também sofrem variação de cor como ");
        sb.append("porcelanatos, cerâmicas etc.\n\n");

        // 3. Financeiro
        sb.append("3. FINANCEIRO:\n\n");
        sb.append("   a) Todo o faturamento terá como base os valores de m² e ml, ");
        sb.append("podendo haver modificações após medição no local.\n");
        sb.append("   b) Valor mínimo cobrado para colocação e ou reparo é de R$ ");
        sb.append(String.format("%.2f", VALOR_MINIMO_COLOCACAO));
        sb.append(" (Hum mil e duzentos reais).\n");
        sb.append("   c) Toda cuba esculpida é trabalho artesanal, valor parte de R$ ");
        sb.append(String.format("%.2f", VALOR_MINIMO_CUBA_ESCULPIDA));
        sb.append(" (Hum mil reais).\n");
        sb.append("   d) Para depósito: Banco Itaú ag.9285 - conta 99559-4 - ");
        sb.append("FG MONTES ME.\n");
        sb.append("   e) PIX: marmorariahelomar@gmail.com\n");
        sb.append("   f) Se o cliente cancelar após recebermos o sinal, ");
        sb.append("será devolvido descontando-se ");
        sb.append(String.format("%.0f", TAXA_CANCELAMENTO));
        sb.append("% do total.\n\n");

        // 4. Colocação
        sb.append("4. COLOCAÇÃO:\n\n");
        sb.append("   a) É necessário que o colocador ou responsável esteja no local ");
        sb.append("para acompanhar a medição que será realizada em uma única etapa.\n");
        sb.append("   b) Caso haja necessidade de visita de nossos profissionais para ");
        sb.append("orientar o colocador, será cobrada R$ ");
        sb.append(String.format("%.2f", VALOR_VISITA_TECNICA));
        sb.append(" (Trezentos e cinquenta reais).\n");
        sb.append("   c) Todo material necessário para colocação (areia, saibro, ");
        sb.append("cimento, grampo, cola, tubos de PU, material para rejunte, ");
        sb.append("andaimes e demais) será fornecido pelo cliente.\n");
        sb.append("   d) A colocação de mármores e granitos não abrange troca de ");
        sb.append("azulejos, reparos de pinturas e rebocos, instalações hidráulicas ");
        sb.append("e demais reparos.\n\n");

        // 5. Entrega
        sb.append("5. ENTREGA:\n\n");
        sb.append("   a) A nossa entrega será avisada na véspera e é feita dentro do ");
        sb.append("horário comercial (08 as 18 horas).\n");
        sb.append("   b) Todo material deve ser conferido no ato da entrega, pois ");
        sb.append("não serão aceitas reclamações posteriores.\n");
        sb.append("   c) Não será feita a troca de peças após elas terem sido ");
        sb.append("colocadas e/ou quebradas.\n\n");

        // 6. Prazos específicos
        sb.append("6. PRAZOS DE ENTREGA APÓS MEDIÇÃO:\n\n");
        sb.append("   a) Peitoris, ilhargas, tentos, soleiras e filetes: até ");
        sb.append(PRAZO_ENTREGA_PEITORIS);
        sb.append(" dias úteis.\n");
        sb.append("   b) Bancadas, pisos, forrações e chapins de banheira: até ");
        sb.append(PRAZO_ENTREGA_BANCADAS);
        sb.append(" dias úteis.\n");
        sb.append("   c) Se houver armários sob as bancadas da cozinha e/ou banheiros, ");
        sb.append("mediremos após sua montagem.\n");
        sb.append("   d) O prazo passa a contar após todas as condições de pagamentos ");
        sb.append("cumpridas, o contrato assinado, os desenhos aprovados com as ");
        sb.append("medições feitas por nosso profissional no local.\n\n");

        // 7. Responsabilidades
        sb.append("7. RESPONSABILIDADES:\n\n");
        sb.append("   a) O cliente ou seu preposto deverá identificar-se devidamente ");
        sb.append("e assinar o canhoto do pedido.\n");
        sb.append("   b) O recebimento da mercadoria ou serviços implica no ");
        sb.append("reconhecimento, pelo destinatário, de que recebeu, examinou e ");
        sb.append("aprovou os mesmos.\n");
        sb.append("   c) Não nos responsabilizamos pelo mau uso dos materiais como ");
        sb.append("por exemplo: uso de produtos de limpeza agressivos, sabão em pó, ");
        sb.append("ácidos e etc.\n");
        sb.append("   d) Pedras devem ser limpas com água e sabão neutro.\n\n");

        sb.append("=================================================================\n");
        sb.append("Declaro que li e entendi as observações da marmoraria para o ");
        sb.append("início do meu trabalho.\n\n");
        sb.append("Aceite do cliente: ________________________________\n\n");
        sb.append("Data: " + getDataPorExtenso() + "\n");
        sb.append("CPF/CNPJ: ________________________________\n");
        sb.append("=================================================================\n");

        return sb.toString();
    }

    /**
     * Gera observações resumidas para o PDF
     */
    public static String gerarObservacoesResumidas(Orcamento orcamento) {
        StringBuilder sb = new StringBuilder();

        sb.append(". Prazo de entrega: A combinar.\n");
        sb.append(". Condições de pgto: A combinar.\n");
        sb.append(". Frete: R$ ");
        sb.append(String.format("%.2f", orcamento.getFrete()));
        sb.append(" por conta do cliente.\n");
        sb.append(". Não fornecemos cubas de louça, torneiras e acessórios.\n");
        sb.append(". Todo material necessário para colocação é de reponsabilidade do cliente.\n");
        sb.append(". Validade da proposta: ");
        sb.append(PRAZO_VALIDADE_NACIONAL);
        sb.append(" dias para materiais nacionais.\n");
        sb.append(". Validade da proposta: ");
        sb.append(PRAZO_VALIDADE_IMPORTADO);
        sb.append(" dias para materiais importados.\n");
        sb.append(". Todo o faturamento terá como base os valores de m² e ml.\n");
        sb.append(". Todo o material é de origem mineral, sujeito a veios e variações.\n");
        sb.append(". Valor mínimo para colocação: R$ ");
        sb.append(String.format("%.2f", VALOR_MINIMO_COLOCACAO));
        sb.append(".\n");
        sb.append(". Cuba esculpida: valor a partir de R$ ");
        sb.append(String.format("%.2f", VALOR_MINIMO_CUBA_ESCULPIDA));
        sb.append(".\n");

        return sb.toString();
    }

    /**
     * Gera termo de aceite
     */
    public static String gerarTermoAceite() {
        StringBuilder sb = new StringBuilder();

        sb.append("=================================================================\n");
        sb.append("                      TERMO DE ACEITE\n");
        sb.append("=================================================================\n\n");
        sb.append("Declaro que li e entendi as observações da marmoraria para o ");
        sb.append("início do meu trabalho.\n\n");
        sb.append("Aceite do cliente: ________________________________\n\n");
        sb.append("Data: " + getDataPorExtenso() + "\n");
        sb.append("CPF/CNPJ: ________________________________\n");
        sb.append("=================================================================\n");

        return sb.toString();
    }
}