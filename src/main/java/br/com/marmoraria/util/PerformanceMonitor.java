package br.com.marmoraria.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Monitor de performance para identificar gargalos.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class PerformanceMonitor {

    private static final Logger LOGGER = Logger.getLogger(PerformanceMonitor.class.getName());

    private static final Map<String, Long> timers = new ConcurrentHashMap<>();
    private static final Map<String, Integer> counters = new ConcurrentHashMap<>();
    private static final Map<String, Long> totalTimes = new ConcurrentHashMap<>();

    /**
     * Inicia um timer
     */
    public static void start(String operation) {
        timers.put(operation, System.currentTimeMillis());
    }

    /**
     * Finaliza um timer e registra o tempo
     */
    public static long end(String operation) {
        Long startTime = timers.remove(operation);
        if (startTime == null) {
            return -1;
        }

        long duration = System.currentTimeMillis() - startTime;

        // Atualizar contadores
        counters.merge(operation, 1, Integer::sum);
        totalTimes.merge(operation, duration, Long::sum);

        if (duration > 1000) {
            LOGGER.warning("⚠️ Operação lenta: " + operation + " - " + duration + "ms");
        } else {
            LOGGER.fine("⏱️ " + operation + ": " + duration + "ms");
        }

        return duration;
    }

    /**
     * Incrementa um contador
     */
    public static void increment(String counter) {
        counters.merge(counter, 1, Integer::sum);
    }

    /**
     * Retorna estatísticas de performance
     */
    public static String getStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== PERFORMANCE STATS ===\n\n");

        if (totalTimes.isEmpty()) {
            sb.append("Nenhuma operação registrada ainda.\n");
            return sb.toString();
        }

        // Ordenar por tempo total (mais pesadas primeiro)
        totalTimes.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry -> {
                    String op = entry.getKey();
                    long total = entry.getValue();
                    int count = counters.getOrDefault(op, 0);
                    double avg = count > 0 ? (double) total / count : 0;

                    sb.append(String.format("%s:\n", op));
                    sb.append(String.format("  Total: %dms | Chamadas: %d | Média: %.1fms\n\n",
                            total, count, avg));
                });

        return sb.toString();
    }

    /**
     * Retorna um resumo rápido
     */
    public static String getQuickSummary() {
        int totalOps = counters.values().stream().mapToInt(Integer::intValue).sum();
        long totalTime = totalTimes.values().stream().mapToLong(Long::longValue).sum();

        return String.format("📊 %d operações | ⏱️ %dms total | 📈 %.1fms média",
                totalOps, totalTime, totalOps > 0 ? (double) totalTime / totalOps : 0);
    }

    /**
     * Limpa todas as estatísticas
     */
    public static void clear() {
        timers.clear();
        counters.clear();
        totalTimes.clear();
    }
}