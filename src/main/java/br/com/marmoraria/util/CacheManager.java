package br.com.marmoraria.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Sistema de cache inteligente com TTL e invalidação automática.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class CacheManager {

    private static final Logger LOGGER = Logger.getLogger(CacheManager.class.getName());

    // Caches separados para diferentes tipos de dados
    private static final Map<String, CacheEntry<?>> cache = new ConcurrentHashMap<>();

    // Tempos de expiração padrão
    private static final long TTL_MATERIAIS = 30 * 60 * 1000;      // 30 minutos
    private static final long TTL_ORCAMENTOS = 5 * 60 * 1000;       // 5 minutos
    private static final long TTL_DASHBOARD = 2 * 60 * 1000;        // 2 minutos
    private static final long TTL_CONFIGURACOES = 60 * 60 * 1000;   // 1 hora

    // Contador de hits/misses para estatísticas
    private static long hits = 0;
    private static long misses = 0;

    /**
     * Armazena um valor no cache
     */
    public static <T> void put(String key, T value, long ttlMillis) {
        cache.put(key, new CacheEntry<>(value, System.currentTimeMillis() + ttlMillis));
        LOGGER.fine("Cache PUT: " + key);
    }

    /**
     * Armazena com TTL padrão baseado no prefixo da chave
     */
    public static <T> void put(String key, T value) {
        long ttl = getDefaultTTL(key);
        put(key, value, ttl);
    }

    /**
     * Recupera um valor do cache
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> get(String key) {
        CacheEntry<?> entry = cache.get(key);

        if (entry == null) {
            misses++;
            return Optional.empty();
        }

        // Verificar expiração
        if (entry.isExpired()) {
            cache.remove(key);
            misses++;
            LOGGER.fine("Cache EXPIRED: " + key);
            return Optional.empty();
        }

        hits++;
        LOGGER.fine("Cache HIT: " + key);
        return Optional.of((T) entry.value);
    }

    /**
     * Recupera ou computa um valor (compute-if-absent)
     */
    @SuppressWarnings("unchecked")
    public static <T> T getOrCompute(String key, java.util.function.Supplier<T> supplier) {
        return getOrCompute(key, supplier, getDefaultTTL(key));
    }

    /**
     * Recupera ou computa com TTL personalizado
     */
    @SuppressWarnings("unchecked")
    public static <T> T getOrCompute(String key, java.util.function.Supplier<T> supplier, long ttlMillis) {
        Optional<T> cached = get(key);
        if (cached.isPresent()) {
            return cached.get();
        }

        T value = supplier.get();
        put(key, value, ttlMillis);
        return value;
    }

    /**
     * Invalida uma chave específica
     */
    public static void invalidate(String key) {
        cache.remove(key);
        LOGGER.fine("Cache INVALIDATE: " + key);
    }

    /**
     * Invalida todas as chaves com determinado prefixo
     */
    public static void invalidateByPrefix(String prefix) {
        cache.keySet().removeIf(key -> key.startsWith(prefix));
        LOGGER.info("Cache INVALIDATE prefix: " + prefix);
    }

    /**
     * Limpa todo o cache
     */
    public static void clear() {
        cache.clear();
        hits = 0;
        misses = 0;
        LOGGER.info("Cache CLEAR completo");
    }

    /**
     * Remove todas as entradas expiradas
     */
    public static void cleanExpired() {
        int before = cache.size();
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        int removed = before - cache.size();
        if (removed > 0) {
            LOGGER.fine("Cache CLEAN: " + removed + " entradas expiradas removidas");
        }
    }

    /**
     * Retorna estatísticas do cache
     */
    public static String getStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CACHE STATS ===\n");
        sb.append("Entradas ativas: ").append(cache.size()).append("\n");
        sb.append("Hits: ").append(hits).append("\n");
        sb.append("Misses: ").append(misses).append("\n");

        double hitRate = (hits + misses) > 0 ?
                (double) hits / (hits + misses) * 100 : 0;
        sb.append(String.format("Hit Rate: %.1f%%\n", hitRate));

        // Top 5 chaves mais acessadas
        sb.append("\nEntradas ativas:\n");
        cache.forEach((key, entry) -> {
            long remaining = entry.expiresAt - System.currentTimeMillis();
            sb.append(String.format("  • %s (expira em %ds)\n",
                    key, Math.max(0, remaining / 1000)));
        });

        return sb.toString();
    }

    /**
     * Determina o TTL padrão baseado no prefixo da chave
     */
    private static long getDefaultTTL(String key) {
        if (key.startsWith("materiais")) return TTL_MATERIAIS;
        if (key.startsWith("orcamentos")) return TTL_ORCAMENTOS;
        if (key.startsWith("dashboard")) return TTL_DASHBOARD;
        if (key.startsWith("config")) return TTL_CONFIGURACOES;
        return 5 * 60 * 1000; // 5 minutos padrão
    }

    /**
     * Entrada do cache com timestamp de expiração
     */
    private static class CacheEntry<T> {
        final T value;
        final long expiresAt;

        CacheEntry(T value, long expiresAt) {
            this.value = value;
            this.expiresAt = expiresAt;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiresAt;
        }
    }
}