package br.com.marmoraria.service;

import br.com.marmoraria.model.Material;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MaterialAPI {

    private static final String API_URL = "https://api.exemplo.com/materiais"; // Substituir por API real
    private static final String API_KEY = "sua-api-key-aqui"; // Substituir pela chave real

    private static List<Material> cacheMateriais = new ArrayList<>();
    private static LocalDateTime ultimaAtualizacao = null;
    private static final long CACHE_VALIDADE_HORAS = 24;

    /**
     * Busca materiais da API (simulado)
     */
    public static List<Material> buscarMateriaisOnline() {
        // Por enquanto, dados simulados
        // No futuro, substituir por chamada HTTP real
        return buscarMateriaisSimulados();
    }

    /**
     * Busca materiais simulados (para demonstração)
     */
    private static List<Material> buscarMateriaisSimulados() {
        List<Material> materiais = new ArrayList<>();

        // Granitos
        materiais.add(new Material("GRAN001", "Granito Preto Absoluto", "Granito", 350.00, 20, "Brasil",
                "Granito nacional de alta resistência, ideal para bancadas e pisos"));
        materiais.add(new Material("GRAN002", "Granito Verde Ubatuba", "Granito", 280.00, 20, "Brasil",
                "Granito de tom esverdeado, muito utilizado em cozinhas"));
        materiais.add(new Material("GRAN003", "Granito Branco Itaúnas", "Granito", 320.00, 20, "Brasil",
                "Granito branco com veios escuros, elegante e sofisticado"));
        materiais.add(new Material("GRAN004", "Granito Amarelo Icaraí", "Granito", 290.00, 20, "Brasil",
                "Granito amarelo claro, traz luminosidade ao ambiente"));

        // Mármores
        materiais.add(new Material("MAR001", "Mármore Carrara", "Mármore", 520.00, 20, "Itália",
                "Mármore importado clássico, branco com veios cinzas"));
        materiais.add(new Material("MAR002", "Mármore Travertino", "Mármore", 480.00, 20, "Itália",
                "Mármore bege com textura porosa, aspecto rústico"));
        materiais.add(new Material("MAR003", "Mármore Branco Puro", "Mármore", 450.00, 20, "Brasil",
                "Mármore branco nacional, mais acessível"));
        materiais.add(new Material("MAR004", "Mármore Negro", "Mármore", 680.00, 20, "Espanha",
                "Mármore preto intenso, sofisticação máxima"));

        // Quartzos
        materiais.add(new Material("QUART001", "Quartzo Branco", "Quartzo", 650.00, 20, "Industrializado",
                "Quartzo branco premium, alta resistência a manchas"));
        materiais.add(new Material("QUART002", "Quartzo Cinza", "Quartzo", 620.00, 20, "Industrializado",
                "Quartzo cinza moderno, ideal para cozinhas contemporâneas"));
        materiais.add(new Material("QUART003", "Quartzo Bege", "Quartzo", 600.00, 20, "Industrializado",
                "Quartzo bege neutro, combina com diversos ambientes"));

        // Pedras Especiais
        materiais.add(new Material("ESP001", "Silestone", "Pedra Especial", 780.00, 20, "Espanha",
                "Superfície de quartzo de alta tecnologia"));
        materiais.add(new Material("ESP002", "Dekton", "Pedra Especial", 890.00, 20, "Espanha",
                "Pedra ultracompacta, resistente a altas temperaturas"));

        return materiais;
    }

    /**
     * Busca materiais com cache
     */
    public static List<Material> getMateriais() {
        // Verificar se cache é válido
        if (cacheMateriais.isEmpty() || precisaAtualizarCache()) {
            System.out.println("🔄 Atualizando catálogo de materiais...");
            cacheMateriais = buscarMateriaisOnline();
            ultimaAtualizacao = LocalDateTime.now();
            System.out.println("✅ Catálogo atualizado! " + cacheMateriais.size() + " materiais disponíveis.");
        }
        return cacheMateriais;
    }

    /**
     * Força atualização do cache
     */
    public static void forcarAtualizacao() {
        cacheMateriais.clear();
        getMateriais();
    }

    /**
     * Verifica se precisa atualizar o cache
     */
    private static boolean precisaAtualizarCache() {
        if (ultimaAtualizacao == null) return true;
        return LocalDateTime.now().isAfter(ultimaAtualizacao.plusHours(CACHE_VALIDADE_HORAS));
    }

    /**
     * Obtém informações sobre o último update
     */
    public static String getInfoUltimaAtualizacao() {
        if (ultimaAtualizacao == null) {
            return "Nunca atualizado";
        }
        return ultimaAtualizacao.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    /**
     * Busca materiais por tipo
     */
    public static List<Material> getMateriaisPorTipo(String tipo) {
        List<Material> resultado = new ArrayList<>();
        for (Material m : getMateriais()) {
            if (m.getTipo().equalsIgnoreCase(tipo)) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    /**
     * Busca material por nome (parcial)
     */
    public static List<Material> buscarPorNome(String nome) {
        List<Material> resultado = new ArrayList<>();
        for (Material m : getMateriais()) {
            if (m.getNome().toLowerCase().contains(nome.toLowerCase())) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    /**
     * Obtém tipos de materiais disponíveis
     */
    public static List<String> getTiposDisponiveis() {
        List<String> tipos = new ArrayList<>();
        for (Material m : getMateriais()) {
            if (!tipos.contains(m.getTipo())) {
                tipos.add(m.getTipo());
            }
        }
        return tipos;
    }
}