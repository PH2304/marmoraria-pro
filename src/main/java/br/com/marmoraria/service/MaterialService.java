package br.com.marmoraria.service;

import br.com.marmoraria.model.Material;
import br.com.marmoraria.model.Servico;
import br.com.marmoraria.service.CatalogoPrecosService.MaterialInfo;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Serviço de materiais atualizado com catálogo da planilha Marmoraria Helomar.
 * Agora utiliza CatalogoPrecosService com 98 materiais reais.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class MaterialService {

    private static final Logger LOGGER = Logger.getLogger(MaterialService.class.getName());

    private List<Material> materiais;
    private final List<Servico> servicos = new ArrayList<>();
    private final CatalogoPrecosService catalogoPrecos;

    // Cache de materiais por tipo
    private final Map<String, List<Material>> cachePorTipo = new HashMap<>();

    public MaterialService() {
        this.catalogoPrecos = new CatalogoPrecosService();
        carregarServicos();
        carregarMateriais();
        LOGGER.info("MaterialService inicializado com " + materiais.size() + " materiais");
    }

    // ==================== MATERIAIS ====================

    /**
     * Carrega materiais do catálogo de preços da planilha
     */
    private void carregarMateriais() {
        materiais = new ArrayList<>();

        Map<String, MaterialInfo> catalogo = CatalogoPrecosService.getCatalogoCompleto();

        int contador = 1;
        for (MaterialInfo info : catalogo.values()) {
            String id = gerarId(info.getTipo(), contador);
            Material material = new Material(
                    id,
                    info.getNome(),
                    info.getTipo(),
                    info.getPreco(),
                    20, // espessura padrão 20mm
                    "Brasil", // origem padrão
                    gerarDescricao(info)
            );
            materiais.add(material);
            contador++;
        }

        // Ordenar por tipo e nome
        materiais.sort(Comparator
                .comparing(Material::getTipo)
                .thenComparing(Material::getNome));

        // Atualizar cache
        atualizarCache();

        LOGGER.info("Catálogo carregado: " + materiais.size() + " materiais em " +
                getTiposMateriais().size() + " categorias");
    }

    /**
     * Gera ID único para o material baseado no tipo
     */
    private String gerarId(String tipo, int sequencial) {
        String prefixo;
        switch (tipo.toUpperCase()) {
            case "GRANITO":
                prefixo = "GRAN";
                break;
            case "MÁRMORE":
                prefixo = "MAR";
                break;
            case "QUARTZO":
                prefixo = "QUART";
                break;
            case "SILESTONE":
                prefixo = "SILE";
                break;
            case "DEKTON":
                prefixo = "DEKT";
                break;
            case "ULTRACOMPACT":
                prefixo = "ULTR";
                break;
            default:
                prefixo = "ESP";
                break;
        }
        return String.format("%s%03d", prefixo, sequencial);
    }

    /**
     * Gera descrição para o material baseado no tipo
     */
    private String gerarDescricao(MaterialInfo info) {
        String tipo = info.getTipo();
        String nome = info.getNome();

        if (tipo.equals("Granito")) {
            return nome + " - Granito nacional de alta qualidade. " +
                    "Ideal para bancadas, pisos e revestimentos.";
        } else if (tipo.equals("Mármore")) {
            return nome + " - Mármore importado de alta qualidade. " +
                    "Sofisticação e elegância para seu ambiente.";
        } else if (tipo.equals("Quartzo") || tipo.equals("Silestone")) {
            return nome + " - Superfície de quartzo de alta tecnologia. " +
                    "Resistente a manchas e não requer selagem.";
        } else if (tipo.equals("Dekton") || tipo.equals("Ultracompact")) {
            return nome + " - Pedra ultracompacta de alta resistência. " +
                    "Resistente a altas temperaturas e riscos.";
        } else {
            return nome + " - Material especial para marmoraria.";
        }
    }

    /**
     * Atualiza o cache de materiais por tipo
     */
    private void atualizarCache() {
        cachePorTipo.clear();
        for (Material m : materiais) {
            cachePorTipo
                    .computeIfAbsent(m.getTipo(), k -> new ArrayList<>())
                    .add(m);
        }
    }

    /**
     * Força atualização dos materiais (recarrega do catálogo)
     */
    public void atualizarMateriais() {
        LOGGER.info("Atualizando catálogo de materiais...");
        carregarMateriais();
        LOGGER.info("Catálogo atualizado com sucesso!");
    }

    /**
     * Retorna todos os materiais
     */
    public List<Material> getTodosMateriais() {
        return new ArrayList<>(materiais);
    }

    /**
     * Retorna todos os tipos de materiais disponíveis
     */
    public Set<String> getTiposMateriais() {
        return materiais.stream()
                .map(Material::getTipo)
                .collect(Collectors.toSet());
    }

    /**
     * Retorna materiais filtrados por tipo
     */
    public List<Material> getMateriaisPorTipo(String tipo) {
        // Usar cache para melhor performance
        List<Material> cached = cachePorTipo.get(tipo);
        if (cached != null) {
            return new ArrayList<>(cached);
        }

        // Fallback: buscar da lista
        return materiais.stream()
                .filter(m -> m.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    /**
     * Busca materiais por nome (parcial)
     */
    public List<Material> buscarPorNome(String nome) {
        String busca = nome.toLowerCase();
        return materiais.stream()
                .filter(m -> m.getNome().toLowerCase().contains(busca))
                .collect(Collectors.toList());
    }

    /**
     * Busca material por nome exato
     */
    public Optional<Material> buscarPorNomeExato(String nome) {
        return materiais.stream()
                .filter(m -> m.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }

    /**
     * Retorna materiais ordenados por preço
     */
    public List<Material> getMateriaisPorPreco(boolean crescente) {
        return materiais.stream()
                .sorted((m1, m2) -> crescente ?
                        Double.compare(m1.getPrecoPorMetroQuadrado(), m2.getPrecoPorMetroQuadrado()) :
                        Double.compare(m2.getPrecoPorMetroQuadrado(), m1.getPrecoPorMetroQuadrado()))
                .collect(Collectors.toList());
    }

    /**
     * Retorna os materiais mais caros
     */
    public List<Material> getTopMateriaisCaros(int limite) {
        return getMateriaisPorPreco(false).stream()
                .limit(limite)
                .collect(Collectors.toList());
    }

    /**
     * Retorna os materiais mais baratos
     */
    public List<Material> getTopMateriaisBaratos(int limite) {
        return getMateriaisPorPreco(true).stream()
                .limit(limite)
                .collect(Collectors.toList());
    }

    /**
     * Retorna faixa de preços dos materiais
     */
    public String getFaixaPrecos() {
        DoubleSummaryStatistics stats = materiais.stream()
                .mapToDouble(Material::getPrecoPorMetroQuadrado)
                .summaryStatistics();

        return String.format("Preços de R$ %.2f a R$ %.2f (média: R$ %.2f)",
                stats.getMin(), stats.getMax(), stats.getAverage());
    }

    /**
     * Retorna estatísticas do catálogo
     */
    public String getEstatisticasCatalogo() {
        StringBuilder sb = new StringBuilder();

        sb.append("=== CATÁLOGO DE MATERIAIS ===\n");
        sb.append("Total de materiais: ").append(materiais.size()).append("\n");
        sb.append("Tipos disponíveis: ").append(getTiposMateriais().size()).append("\n\n");

        sb.append("Por tipo:\n");
        for (String tipo : getTiposMateriais()) {
            List<Material> porTipo = getMateriaisPorTipo(tipo);
            double precoMedio = porTipo.stream()
                    .mapToDouble(Material::getPrecoPorMetroQuadrado)
                    .average()
                    .orElse(0);
            sb.append(String.format("  • %s: %d materiais (média R$ %.2f/m²)\n",
                    tipo, porTipo.size(), precoMedio));
        }

        sb.append("\n").append(getFaixaPrecos());

        // Top 5 mais caros
        sb.append("\n\nTop 5 mais caros:\n");
        getTopMateriaisCaros(5).forEach(m ->
                sb.append(String.format("  • %s - R$ %.2f/m²\n",
                        m.getNome(), m.getPrecoPorMetroQuadrado())));

        return sb.toString();
    }

    /**
     * Retorna informações da última atualização
     */
    public String getInfoUltimaAtualizacao() {
        return "Catálogo local com " + materiais.size() + " materiais | " +
                "Baseado na planilha Marmoraria Helomar";
    }

    // ==================== SERVIÇOS ====================

    /**
     * Carrega lista de serviços
     */
    private void carregarServicos() {
        servicos.add(new Servico("S01", "Corte reto", 40.00, "m", "Corte"));
        servicos.add(new Servico("S02", "Corte em ângulo", 60.00, "m", "Corte"));
        servicos.add(new Servico("S03", "Polimento simples", 60.00, "m²", "Polimento"));
        servicos.add(new Servico("S04", "Polimento especial", 90.00, "m²", "Polimento"));
        servicos.add(new Servico("S05", "Instalação padrão", 250.00, "un", "Instalação"));
        servicos.add(new Servico("S06", "Instalação premium", 400.00, "un", "Instalação"));
        servicos.add(new Servico("S07", "Furação para torneira", 50.00, "un", "Instalação"));
        servicos.add(new Servico("S08", "Furação para cooktop", 80.00, "un", "Instalação"));
        servicos.add(new Servico("S09", "Acabamento especial", 120.00, "m²", "Acabamento"));
        servicos.add(new Servico("S10", "Visita técnica", 350.00, "un", "Visita"));

        LOGGER.info("Serviços carregados: " + servicos.size());
    }

    /**
     * Retorna todos os serviços
     */
    public List<Servico> getTodosServicos() {
        return new ArrayList<>(servicos);
    }

    /**
     * Retorna categorias de serviços
     */
    public Set<String> getCategoriasServicos() {
        return servicos.stream()
                .map(Servico::getCategoria)
                .collect(Collectors.toSet());
    }

    /**
     * Retorna serviços por categoria
     */
    public List<Servico> getServicosPorCategoria(String categoria) {
        return servicos.stream()
                .filter(s -> s.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    /**
     * Retorna serviços ordenados por preço
     */
    public List<Servico> getServicosPorPreco() {
        return servicos.stream()
                .sorted(Comparator.comparingDouble(Servico::getPreco))
                .collect(Collectors.toList());
    }

    // ==================== UTILITÁRIOS ====================

    /**
     * Converte Material para MaterialInfo do catálogo
     */
    public Optional<MaterialInfo> converterParaMaterialInfo(Material material) {
        return CatalogoPrecosService.buscarPorNome(material.getNome());
    }

    /**
     * Verifica se um material existe no catálogo
     */
    public boolean existeMaterial(String nome) {
        return CatalogoPrecosService.buscarPorNome(nome).isPresent();
    }

    /**
     * Retorna o preço de um material específico
     */
    public Optional<Double> getPrecoMaterial(String nome) {
        return CatalogoPrecosService.buscarPorNome(nome)
                .map(MaterialInfo::getPreco);
    }

    /**
     * Compara preços entre dois materiais
     */
    public String compararPrecos(String nome1, String nome2) {
        Optional<Double> preco1 = getPrecoMaterial(nome1);
        Optional<Double> preco2 = getPrecoMaterial(nome2);

        if (!preco1.isPresent() || !preco2.isPresent()) {
            return "Um ou ambos materiais não encontrados";
        }

        double p1 = preco1.get();
        double p2 = preco2.get();
        double diferenca = Math.abs(p1 - p2);
        double percentual = (diferenca / Math.max(p1, p2)) * 100;

        return String.format("%s: R$ %.2f/m² | %s: R$ %.2f/m² | " +
                        "Diferença: R$ %.2f (%.1f%%)",
                nome1, p1, nome2, p2, diferenca, percentual);
    }

    /**
     * Sugere materiais similares mais baratos
     */
    public List<Material> sugerirAlternativasMaisBaratas(Material material, int quantidade) {
        return materiais.stream()
                .filter(m -> m.getTipo().equals(material.getTipo()))
                .filter(m -> m.getPrecoPorMetroQuadrado() < material.getPrecoPorMetroQuadrado())
                .sorted(Comparator.comparingDouble(Material::getPrecoPorMetroQuadrado))
                .limit(quantidade)
                .collect(Collectors.toList());
    }

    /**
     * Sugere materiais similares baseado no tipo
     */
    public List<Material> sugerirSimilares(String tipo, double precoMaximo) {
        return materiais.stream()
                .filter(m -> m.getTipo().equalsIgnoreCase(tipo))
                .filter(m -> m.getPrecoPorMetroQuadrado() <= precoMaximo)
                .sorted(Comparator.comparingDouble(Material::getPrecoPorMetroQuadrado))
                .collect(Collectors.toList());
    }
}