package br.com.marmoraria.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

/**
 * Configuração de logging profissional com rotação de arquivos.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class LoggerConfig {

    private static final String LOG_DIR = "logs";
    private static final int MAX_LOG_FILES = 10;
    private static final long MAX_LOG_SIZE = 5 * 1024 * 1024; // 5MB

    /**
     * Inicializa o sistema de logging
     */
    public static void inicializar() {
        try {
            // Criar diretório de logs
            Path logDir = Paths.get(LOG_DIR);
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }

            // Configurar logger raiz
            Logger rootLogger = Logger.getLogger("");

            // Remover handlers padrão
            for (Handler handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
            }

            // Handler para console
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new LogFormatter());
            rootLogger.addHandler(consoleHandler);

            // Handler para arquivo
            String logFile = LOG_DIR + "/marmoraria-" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".log";
            FileHandler fileHandler = new FileHandler(logFile, MAX_LOG_SIZE, MAX_LOG_FILES, true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new LogFormatter());
            rootLogger.addHandler(fileHandler);

            // Definir nível padrão
            rootLogger.setLevel(Level.INFO);

            // Log de inicialização
            Logger logger = Logger.getLogger(LoggerConfig.class.getName());
            logger.info("═══════════════════════════════════");
            logger.info("Sistema de logging inicializado");
            logger.info("Pasta de logs: " + logDir.toAbsolutePath());
            logger.info("═══════════════════════════════════");

            // Limpar logs antigos
            limparLogsAntigos(logDir);

        } catch (IOException e) {
            System.err.println("Erro ao configurar logging: " + e.getMessage());
        }
    }

    /**
     * Formatter personalizado para logs
     */
    private static class LogFormatter extends Formatter {

        private static final DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        // Cores ANSI para console
        private static final String RESET = "\u001B[0m";
        private static final String RED = "\u001B[31m";
        private static final String YELLOW = "\u001B[33m";
        private static final String CYAN = "\u001B[36m";
        private static final String GREEN = "\u001B[32m";

        @Override
        public String format(LogRecord record) {
            StringBuilder sb = new StringBuilder();

            // Timestamp
            sb.append(CYAN);
            sb.append(dtf.format(LocalDateTime.now()));
            sb.append(RESET);
            sb.append(" ");

            // Nível
            String level = record.getLevel().getName();
            switch (level) {
                case "SEVERE":
                    sb.append(RED).append("ERROR").append(RESET);
                    break;
                case "WARNING":
                    sb.append(YELLOW).append("WARN ").append(RESET);
                    break;
                case "INFO":
                    sb.append(GREEN).append("INFO ").append(RESET);
                    break;
                default:
                    sb.append("DEBUG");
            }
            sb.append(" ");

            // Logger name (abreviado)
            String loggerName = record.getLoggerName();
            if (loggerName != null) {
                String[] parts = loggerName.split("\\.");
                String shortName = parts[parts.length - 1];
                sb.append("[").append(shortName).append("] ");
            }

            // Mensagem
            sb.append(record.getMessage());
            sb.append("\n");

            // Exceção se houver
            if (record.getThrown() != null) {
                sb.append(RED);
                sb.append("    Causa: ").append(record.getThrown().getMessage());
                sb.append("\n");
                for (StackTraceElement element : record.getThrown().getStackTrace()) {
                    if (element.getClassName().contains("marmoraria")) {
                        sb.append("        at ").append(element.toString()).append("\n");
                    }
                }
                sb.append(RESET);
            }

            return sb.toString();
        }
    }

    /**
     * Remove logs antigos
     */
    private static void limparLogsAntigos(Path logDir) {
        try {
            Files.list(logDir)
                    .filter(Files::isRegularFile)
                    .filter(f -> f.toString().endsWith(".log"))
                    .sorted((a, b) -> {
                        try {
                            return Files.getLastModifiedTime(b).compareTo(Files.getLastModifiedTime(a));
                        } catch (IOException e) {
                            return 0;
                        }
                    })
                    .skip(MAX_LOG_FILES)
                    .forEach(f -> {
                        try {
                            Files.delete(f);
                        } catch (IOException e) {
                            // Ignorar erros de deleção
                        }
                    });
        } catch (IOException e) {
            // Ignorar
        }
    }
}