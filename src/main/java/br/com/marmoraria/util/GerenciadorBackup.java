package br.com.marmoraria.util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorBackup {

    private static final String PASTA_BACKUP = "backups";
    private static final String PASTA_ORCAMENTOS = "orcamentos";
    private static final int MAX_BACKUPS = 30; // Máximo de backups mantidos
    private static final int DIAS_PARA_BACKUP = 1; // Faz backup a cada 1 dia

    /**
     * Garante que a pasta de backup existe
     */
    public static void garantirPastaBackup() {
        File pasta = new File(PASTA_BACKUP);
        if (!pasta.exists()) {
            pasta.mkdirs();
            System.out.println("📁 Pasta de backup criada: " + pasta.getAbsolutePath());
        }
    }

    /**
     * Verifica se precisa fazer backup (baseado na última data)
     */
    public static boolean precisaBackup() {
        try {
            File pasta = new File(PASTA_BACKUP);
            if (!pasta.exists()) {
                return true;
            }

            // Encontrar o backup mais recente
            File[] backups = pasta.listFiles((dir, name) -> name.endsWith(".zip"));
            if (backups == null || backups.length == 0) {
                return true;
            }

            // Encontrar o mais recente
            File ultimoBackup = null;
            long ultimaModificacao = 0;
            for (File backup : backups) {
                if (backup.lastModified() > ultimaModificacao) {
                    ultimaModificacao = backup.lastModified();
                    ultimoBackup = backup;
                }
            }

            if (ultimoBackup == null) {
                return true;
            }

            // Calcular dias desde o último backup
            long diasDesdeUltimo = (System.currentTimeMillis() - ultimoBackup.lastModified()) / (1000 * 60 * 60 * 24);
            return diasDesdeUltimo >= DIAS_PARA_BACKUP;

        } catch (Exception e) {
            System.err.println("❌ Erro ao verificar necessidade de backup: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cria um backup completo da pasta de orçamentos
     */
    public static boolean criarBackup() {
        try {
            garantirPastaBackup();

            String nomeBackup = "backup_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".zip";
            File arquivoBackup = new File(PASTA_BACKUP, nomeBackup);

            // Criar arquivo ZIP
            try (java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(new FileOutputStream(arquivoBackup))) {

                File pastaOrcamentos = new File(PASTA_ORCAMENTOS);
                if (pastaOrcamentos.exists()) {
                    adicionarPastaAoZip(pastaOrcamentos, pastaOrcamentos.getName(), zos);
                }
            }

            System.out.println("💾 Backup criado: " + arquivoBackup.getAbsolutePath());

            // Limpar backups antigos
            limparBackupsAntigos();

            return true;

        } catch (Exception e) {
            System.err.println("❌ Erro ao criar backup: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adiciona uma pasta e seu conteúdo ao arquivo ZIP
     */
    private static void adicionarPastaAoZip(File pasta, String nomeBase, java.util.zip.ZipOutputStream zos) throws IOException {
        File[] arquivos = pasta.listFiles();
        if (arquivos == null) return;

        for (File arquivo : arquivos) {
            if (arquivo.isDirectory()) {
                adicionarPastaAoZip(arquivo, nomeBase + "/" + arquivo.getName(), zos);
            } else {
                String nomeZip = nomeBase + "/" + arquivo.getName();
                java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(nomeZip);
                zos.putNextEntry(zipEntry);

                try (FileInputStream fis = new FileInputStream(arquivo)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                }

                zos.closeEntry();
            }
        }
    }

    /**
     * Remove backups antigos mantendo apenas os MAX_BACKUPS mais recentes
     */
    private static void limparBackupsAntigos() {
        try {
            File pasta = new File(PASTA_BACKUP);
            File[] backups = pasta.listFiles((dir, name) -> name.endsWith(".zip"));

            if (backups != null && backups.length > MAX_BACKUPS) {
                // Ordenar por data de modificação (mais antigos primeiro)
                java.util.Arrays.sort(backups, (a, b) -> Long.compare(a.lastModified(), b.lastModified()));

                int remover = backups.length - MAX_BACKUPS;
                for (int i = 0; i < remover; i++) {
                    boolean deletado = backups[i].delete();
                    if (deletado) {
                        System.out.println("🗑️ Backup antigo removido: " + backups[i].getName());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao limpar backups antigos: " + e.getMessage());
        }
    }

    /**
     * Lista todos os backups disponíveis
     */
    public static List<File> listarBackups() {
        List<File> backups = new ArrayList<>();

        try {
            File pasta = new File(PASTA_BACKUP);
            if (pasta.exists()) {
                File[] arquivos = pasta.listFiles((dir, name) -> name.endsWith(".zip"));
                if (arquivos != null) {
                    for (File arquivo : arquivos) {
                        backups.add(arquivo);
                    }
                    // Ordenar por data (mais recentes primeiro)
                    backups.sort((a, b) -> Long.compare(b.lastModified(), a.lastModified()));
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao listar backups: " + e.getMessage());
        }

        return backups;
    }

    /**
     * Restaura um backup específico
     */
    public static boolean restaurarBackup(File arquivoBackup) {
        try {
            if (!arquivoBackup.exists()) {
                System.err.println("❌ Arquivo de backup não encontrado: " + arquivoBackup.getAbsolutePath());
                return false;
            }

            // Criar backup dos dados atuais antes de restaurar
            criarBackup();

            // Remover pasta de orçamentos atual
            File pastaOrcamentos = new File(PASTA_ORCAMENTOS);
            if (pastaOrcamentos.exists()) {
                deletarPasta(pastaOrcamentos);
            }

            // Recriar pasta
            pastaOrcamentos.mkdirs();

            // Extrair ZIP
            try (java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(new FileInputStream(arquivoBackup))) {
                java.util.zip.ZipEntry entry;
                byte[] buffer = new byte[1024];

                while ((entry = zis.getNextEntry()) != null) {
                    File novoArquivo = new File(entry.getName());

                    if (entry.isDirectory()) {
                        novoArquivo.mkdirs();
                    } else {
                        // Criar diretórios pai se necessário
                        new File(novoArquivo.getParent()).mkdirs();

                        try (FileOutputStream fos = new FileOutputStream(novoArquivo)) {
                            int length;
                            while ((length = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, length);
                            }
                        }
                    }
                    zis.closeEntry();
                }
            }

            System.out.println("✅ Backup restaurado com sucesso: " + arquivoBackup.getName());
            return true;

        } catch (Exception e) {
            System.err.println("❌ Erro ao restaurar backup: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deleta uma pasta e todo seu conteúdo
     */
    private static void deletarPasta(File pasta) {
        File[] arquivos = pasta.listFiles();
        if (arquivos != null) {
            for (File arquivo : arquivos) {
                if (arquivo.isDirectory()) {
                    deletarPasta(arquivo);
                } else {
                    arquivo.delete();
                }
            }
        }
        pasta.delete();
    }

    /**
     * Agenda backups automáticos (será chamado ao iniciar o programa)
     */
    public static void iniciarBackupAutomatico() {
        // Criar um thread para verificar backups periodicamente
        Thread backupThread = new Thread(() -> {
            while (true) {
                try {
                    // Verificar a cada 1 hora se precisa de backup
                    Thread.sleep(60 * 60 * 1000); // 1 hora

                    if (precisaBackup()) {
                        System.out.println("🔄 Iniciando backup automático...");
                        criarBackup();
                    }
                } catch (InterruptedException e) {
                    System.out.println("⏹️ Backup automático interrompido");
                    break;
                } catch (Exception e) {
                    System.err.println("❌ Erro no backup automático: " + e.getMessage());
                }
            }
        });
        backupThread.setDaemon(true);
        backupThread.start();
        System.out.println("🔄 Backup automático iniciado (verifica a cada 1 hora)");
    }

    /**
     * Obtém informações sobre o último backup
     */
    public static String getInfoUltimoBackup() {
        List<File> backups = listarBackups();
        if (backups.isEmpty()) {
            return "Nenhum backup encontrado";
        }

        File ultimo = backups.get(0);
        long dias = (System.currentTimeMillis() - ultimo.lastModified()) / (1000 * 60 * 60 * 24);

        return String.format("Último backup: %s (%d dias atrás)\nTamanho: %.2f KB",
                ultimo.getName(),
                dias,
                ultimo.length() / 1024.0
        );
    }
}