package br.com.marmoraria.service;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Catálogo de preços oficial baseado na planilha Marmoraria Helomar.
 * Contém 98 materiais com preços atualizados.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class CatalogoPrecosService {

    private static final Logger LOGGER = Logger.getLogger(CatalogoPrecosService.class.getName());

    // Mapa de preços
    private static final Map<String, MaterialInfo> CATALOGO_PRECOS = new LinkedHashMap<>();

    static {
        LOGGER.info("Carregando catálogo de preços...");

        // ==================== GRANITOS ====================
        adicionarMaterial("ALBA PEITA CINZA", "Granito", 2000.00);
        adicionarMaterial("AMARELO FLORENÇA", "Granito", 500.00);
        adicionarMaterial("AMARELO ICARAI", "Granito", 500.00);
        adicionarMaterial("AMARELO MARACUJA", "Granito", 600.00);
        adicionarMaterial("AMARELO ORNAMENTAL", "Granito", 500.00);
        adicionarMaterial("AMÊNDOA", "Granito", 500.00);
        adicionarMaterial("ARDOSIA", "Granito", 600.00);
        adicionarMaterial("AS DE PAUS", "Granito", 800.00);
        adicionarMaterial("AZUL MACAÚBAS", "Granito", 2500.00);

        // Brancos
        adicionarMaterial("BRANCO CACHOEIRO", "Granito", 450.00);
        adicionarMaterial("BRANCO CARRARINHA", "Granito", 800.00);
        adicionarMaterial("BRANCO CINTILANTE", "Granito", 1800.00);
        adicionarMaterial("BRANCO COMUM", "Granito", 400.00);
        adicionarMaterial("BRANCO DALLAS", "Granito", 500.00);
        adicionarMaterial("BRANCO ITAUNAS FLAMEADO", "Granito", 830.00);
        adicionarMaterial("BCO ITAUNAS", "Granito", 800.00);
        adicionarMaterial("BRANCO LEBLON", "Granito", 600.00);
        adicionarMaterial("BRANCO PARANÁ", "Granito", 1700.00);
        adicionarMaterial("BRANCO PARIS", "Granito", 500.00);
        adicionarMaterial("BRANCO PIGHÊS", "Granito", 2300.00);
        adicionarMaterial("BRANCO PITAYA", "Granito", 1200.00);
        adicionarMaterial("BRANCO SIENA ANTICATO", "Granito", 750.00);

        adicionarMaterial("BRANCO CEARÁ", "Granito", 850.00);
        adicionarMaterial("BCO CEARÁ ESCOVADO", "Granito", 950.00);
        adicionarMaterial("BCO CHOCOLATE", "Granito", 650.00);
        adicionarMaterial("BCO EXTRA", "Granito", 1200.00);
        adicionarMaterial("BCO INTER", "Granito", 900.00);
        adicionarMaterial("BCO PINTA CINZA", "Granito", 600.00);
        adicionarMaterial("BCO PINTA VERDE", "Granito", 500.00);
        adicionarMaterial("BCO PREMIER", "Granito", 800.00);
        adicionarMaterial("BCO SIENA ESCOVADO", "Granito", 900.00);
        adicionarMaterial("BEGE BAHIA", "Granito", 650.00);
        adicionarMaterial("BEGE PREMIER", "Granito", 900.00);

        // Cinzas
        adicionarMaterial("CINZA ANDORINHA", "Granito", 500.00);
        adicionarMaterial("CINZA ANDORINHA ESCOVADO", "Granito", 650.00);
        adicionarMaterial("CINZA CASTELO", "Granito", 500.00);
        adicionarMaterial("CINZA CORUMBA", "Granito", 500.00);
        adicionarMaterial("CINZA PREMIER", "Granito", 900.00);
        adicionarMaterial("CINZA ABSOLUTO GRANITO", "Granito", 850.00);
        adicionarMaterial("GRANITO CINZA ABSOLUTO ESCOVADO", "Granito", 850.00);
        adicionarMaterial("GRANITO OURO BRANCO", "Granito", 600.00);
        adicionarMaterial("ITAUNAS ESCOVADO", "Granito", 850.00);

        // Pretos
        adicionarMaterial("PRETO ABSOLUTO", "Granito", 1500.00);
        adicionarMaterial("PRETO BASALTO", "Granito", 1600.00);
        adicionarMaterial("PRETO ESCOVADO / APICOADO", "Granito", 1100.00);
        adicionarMaterial("PRETO INDIANO", "Granito", 700.00);
        adicionarMaterial("PRETO S. GABRIEL ESCOVADO", "Granito", 900.00);
        adicionarMaterial("PRETO S.GABRIEL", "Granito", 850.00);
        adicionarMaterial("PRETO SEMI ABSOLUTO", "Granito", 950.00);

        // Cores especiais
        adicionarMaterial("OURO BRANCO", "Granito", 550.00);
        adicionarMaterial("OURO BRASIL", "Granito", 500.00);
        adicionarMaterial("VERDE CANDEIAS", "Granito", 500.00);
        adicionarMaterial("VERDE GUATEMALA", "Granito", 1500.00);
        adicionarMaterial("VERDE JADE", "Granito", 800.00);
        adicionarMaterial("VERDE ESCOVADO UBATUBA", "Granito", 500.00);
        adicionarMaterial("VERDE UBATUBA", "Granito", 450.00);
        adicionarMaterial("VRD UBATUBA FLAMEADO", "Granito", 400.00);
        adicionarMaterial("VERMELHO BRASILIA 0,03", "Granito", 1050.00);
        adicionarMaterial("VERMELHO BRASILIA 0,02", "Granito", 750.00);
        adicionarMaterial("VIA LÁCTEA", "Granito", 850.00);
        adicionarMaterial("SÃO FRANCISCO", "Granito", 500.00);
        adicionarMaterial("SÃO TOMÉ", "Granito", 600.00);
        adicionarMaterial("SIENA BRANCO", "Granito", 850.00);
        adicionarMaterial("OCRE ITABIRA", "Granito", 500.00);
        adicionarMaterial("KALAHARI", "Granito", 1500.00);

        // Marrons
        adicionarMaterial("MARROM ABSOLUTO", "Granito", 950.00);
        adicionarMaterial("MARROM CAFÉ", "Granito", 900.00);
        adicionarMaterial("MARROM TABACO", "Granito", 800.00);

        // ==================== MÁRMORES ====================
        adicionarMaterial("CARRARA", "Mármore", 1600.00);
        adicionarMaterial("CALACATA GOLD ALTA POTENCIA", "Mármore", 5300.00);
        adicionarMaterial("CALACATA ORO RAVELO", "Mármore", 1600.00);
        adicionarMaterial("CREMA MARFIL", "Mármore", 1700.00);
        adicionarMaterial("CREMA MOKKA", "Mármore", 1200.00);
        adicionarMaterial("MARROM IMPERIAL", "Mármore", 1900.00);
        adicionarMaterial("NERO MARQUINA", "Mármore", 2000.00);
        adicionarMaterial("TRAVERTINO MATTE", "Mármore", 1700.00);
        adicionarMaterial("TRAVERTINO NAVONA BRUTO", "Mármore", 1800.00);
        adicionarMaterial("TRAVERTINO NAVONA ESTUCADO", "Mármore", 2000.00);
        adicionarMaterial("TRAVERTINO ROMANO ESTUCADO", "Mármore", 1600.00);
        adicionarMaterial("BOTTICINO", "Mármore", 2450.00);
        adicionarMaterial("SIVEC", "Mármore", 4000.00);
        adicionarMaterial("TAJ MAHAL", "Mármore", 2100.00);
        adicionarMaterial("BLUE MIST", "Mármore", 2200.00);

        // ==================== QUARTZOS ====================
        adicionarMaterial("QUARTZO BRANCO", "Quartzo", 1300.00);
        adicionarMaterial("QUARTZO CINZA", "Quartzo", 1500.00);
        adicionarMaterial("QUARTZO GRAIN MINERVA", "Quartzo", 1800.00);
        adicionarMaterial("QUARTZO MARROM ESTELAR", "Quartzo", 1550.00);
        adicionarMaterial("QUARTZO MARROM CAFÉ", "Quartzo", 1900.00);
        adicionarMaterial("QUARTZO PERLA SANTANA", "Quartzo", 1800.00);
        adicionarMaterial("QTZO CALACATA", "Quartzo", 1700.00);
        adicionarMaterial("QUARTZO BCO STELAR", "Quartzo", 2800.00);
        adicionarMaterial("NANO PRIME", "Quartzo", 1000.00);
        adicionarMaterial("PRIME CALACATA", "Quartzo", 1300.00);
        adicionarMaterial("PRIME ONIX", "Quartzo", 2050.00);
        adicionarMaterial("SUPER NANO", "Quartzo", 1900.00);

        // ==================== SILESTONE ====================
        adicionarMaterial("SILESTONE BLANCO NORTE", "Silestone", 2000.00);
        adicionarMaterial("SILESTONE BRANCO ZEUS", "Silestone", 4500.00);
        adicionarMaterial("SILESTONE CINZA KENSHO", "Silestone", 3000.00);
        adicionarMaterial("SILESTONE MARENGO", "Silestone", 2400.00);

        // ==================== DEKTON ====================
        adicionarMaterial("DEKTON ENTZO NATURAL", "Dekton", 7075.00);
        adicionarMaterial("DEKTON KELYA (4MM)", "Dekton", 2300.00);
        adicionarMaterial("DEKTON RIO BRANCO (1,2MM)", "Dekton", 3500.00);

        // ==================== ULTRACOMPACT ====================
        adicionarMaterial("ULTRACOMPCT BRANCO ACETINADO", "Ultracompact", 1300.00);
        adicionarMaterial("ULTRACOMPCT BRANCO LUSTRADO", "Ultracompact", 1300.00);
        adicionarMaterial("ULTACOMPACT B.CARRARA STATURIETTO 6MM", "Ultracompact", 2800.00);
        adicionarMaterial("ULTRA COMP BIANCO ABSOLUTE LUCIDATO", "Ultracompact", 2500.00);
        adicionarMaterial("ULTRACOMPACT CALCE TORTORA", "Ultracompact", 2400.00);
        adicionarMaterial("ULTRACOMPACT CREAM DIAMOND", "Ultracompact", 2850.00);
        adicionarMaterial("ULTRACOMPACT CINZA", "Ultracompact", 1500.00);
        adicionarMaterial("ULTRACOMPACT PIETRA GREY LUCIDATO", "Ultracompact", 3200.00);
        adicionarMaterial("ULTRACOMPACT TAJ MAHAL", "Ultracompact", 1300.00);
        adicionarMaterial("ULTRACOMPACT TRAVERTINO NAVONA", "Ultracompact", 1500.00);

        // ==================== ESPECIAIS ====================
        adicionarMaterial("CERÂMICA CALACATA", "Especial", 2000.00);
        adicionarMaterial("ONIX WHITE", "Especial", 6000.00);
        adicionarMaterial("PORCELANATO CLIENTE", "Especial", 1500.00);

        LOGGER.info("✅ Catálogo carregado com " + CATALOGO_PRECOS.size() + " materiais");
    }

    private static void adicionarMaterial(String nome, String tipo, double preco) {
        CATALOGO_PRECOS.put(nome.toUpperCase(),
                new MaterialInfo(nome, tipo, preco));
    }

    /**
     * Classe interna com informações do material
     */
    public static class MaterialInfo {
        private final String nome;
        private final String tipo;
        private final double precoPorMetroQuadrado;

        public MaterialInfo(String nome, String tipo, double preco) {
            this.nome = nome;
            this.tipo = tipo;
            this.precoPorMetroQuadrado = preco;
        }

        public String getNome() { return nome; }
        public String getTipo() { return tipo; }
        public double getPreco() { return precoPorMetroQuadrado; }

        public String getPrecoFormatado() {
            return String.format("R$ %.2f /m²", precoPorMetroQuadrado);
        }

        @Override
        public String toString() {
            return String.format("%s (%s) - %s", nome, tipo, getPrecoFormatado());
        }
    }

    // ==================== MÉTODOS DE BUSCA ====================

    /**
     * Busca material pelo nome exato
     */
    public static Optional<MaterialInfo> buscarPorNome(String nome) {
        return Optional.ofNullable(CATALOGO_PRECOS.get(nome.toUpperCase()));
    }

    /**
     * Busca materiais por parte do nome
     */
    public static List<MaterialInfo> buscarPorNomeParcial(String texto) {
        String busca = texto.toUpperCase();
        return CATALOGO_PRECOS.values().stream()
                .filter(m -> m.getNome().toUpperCase().contains(busca))
                .collect(Collectors.toList());
    }

    /**
     * Busca todos os materiais de um tipo
     */
    public static List<MaterialInfo> buscarPorTipo(String tipo) {
        return CATALOGO_PRECOS.values().stream()
                .filter(m -> m.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    /**
     * Retorna todos os nomes de materiais
     */
    public static List<String> getTodosNomes() {
        return new ArrayList<>(CATALOGO_PRECOS.keySet());
    }

    /**
     * Retorna todos os tipos disponíveis
     */
    public static Set<String> getTiposDisponiveis() {
        return CATALOGO_PRECOS.values().stream()
                .map(MaterialInfo::getTipo)
                .collect(Collectors.toSet());
    }

    /**
     * Retorna o catálogo completo (imutável)
     */
    public static Map<String, MaterialInfo> getCatalogoCompleto() {
        return Collections.unmodifiableMap(CATALOGO_PRECOS);
    }

    /**
     * Retorna estatísticas do catálogo
     */
    public static String getEstatisticas() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CATÁLOGO DE PREÇOS ===\n");
        sb.append("Total de materiais: ").append(CATALOGO_PRECOS.size()).append("\n\n");

        Map<String, Long> porTipo = CATALOGO_PRECOS.values().stream()
                .collect(Collectors.groupingBy(MaterialInfo::getTipo, Collectors.counting()));

        sb.append("Por tipo:\n");
        porTipo.forEach((tipo, qtd) ->
                sb.append(String.format("  • %s: %d materiais\n", tipo, qtd)));

        DoubleSummaryStatistics stats = CATALOGO_PRECOS.values().stream()
                .mapToDouble(MaterialInfo::getPreco)
                .summaryStatistics();

        sb.append("\nEstatísticas de preços:\n");
        sb.append(String.format("  • Menor preço: R$ %.2f\n", stats.getMin()));
        sb.append(String.format("  • Maior preço: R$ %.2f\n", stats.getMax()));
        sb.append(String.format("  • Preço médio: R$ %.2f\n", stats.getAverage()));

        return sb.toString();
    }
}