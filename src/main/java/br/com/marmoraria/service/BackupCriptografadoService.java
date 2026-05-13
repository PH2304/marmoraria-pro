package br.com.marmoraria.service;

import br.com.marmoraria.model.Orcamento;
import br.com.marmoraria.util.GerenciadorArquivos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.spec.KeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Serviço de backup criptografado para maior segurança.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class BackupCriptografadoService {

    private static final Logger LOGGER = Logger.getLogger(BackupCriptografadoService.class.getName());

    private static final String BACKUP_DIR = "backups_seguros";
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "MarmorariaPro@2026#Backup!Key";
    private static final byte[] SALT = "MarmorariaSalt@2026".getBytes();

    /**
     * Cria um backup criptografado
     */
    public static boolean criarBackupCriptografado(String senha) {
        try {
            // Garantir diretório
            Path backupDir = Paths.get(BACKUP_DIR);
            if (!Files.exists(backupDir)) {
                Files.createDirectories(backupDir);
            }

            // Nome do arquivo
            String timestamp = LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nomeArquivo = BACKUP_DIR + "/backup_cripto_" + timestamp + ".mpr";

            // Carregar todos os orçamentos
            List<Orcamento> orcamentos = GerenciadorArquivos.carregarTodosOrcamentos();

            // Serializar para JSON
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDateTime.class,
                            new br.com.marmoraria.util.LocalDateTimeAdapter())
                    .create();
            String json = gson.toJson(orcamentos);

            // Comprimir
            byte[] dadosComprimidos = comprimir(json);

            // Criptografar
            byte[] dadosCriptografados = criptografar(dadosComprimidos, senha);

            // Salvar arquivo
            Files.write(Paths.get(nomeArquivo), dadosCriptografados);

            LOGGER.info("Backup criptografado criado: " + nomeArquivo);
            LOGGER.info("Orçamentos salvos: " + orcamentos.size());
            LOGGER.info("Tamanho: " + (dadosCriptografados.length / 1024) + " KB");

            return true;

        } catch (Exception e) {
            LOGGER.severe("Erro ao criar backup criptografado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Restaura um backup criptografado
     */
    public static List<Orcamento> restaurarBackupCriptografado(String arquivo, String senha) {
        try {
            // Ler arquivo
            byte[] dadosCriptografados = Files.readAllBytes(Paths.get(arquivo));

            // Descriptografar
            byte[] dadosComprimidos = descriptografar(dadosCriptografados, senha);

            // Descomprimir
            String json = descomprimir(dadosComprimidos);

            // Deserializar
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class,
                            new br.com.marmoraria.util.LocalDateTimeAdapter())
                    .create();

            Orcamento[] array = gson.fromJson(json, Orcamento[].class);
            List<Orcamento> orcamentos = java.util.Arrays.asList(array);

            LOGGER.info("Backup restaurado: " + orcamentos.size() + " orçamentos");

            // Salvar os orçamentos restaurados
            for (Orcamento o : orcamentos) {
                GerenciadorArquivos.salvarOrcamento(o);
            }

            return orcamentos;

        } catch (Exception e) {
            LOGGER.severe("Erro ao restaurar backup: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    /**
     * Lista backups criptografados disponíveis
     */
    public static java.util.List<File> listarBackupsCriptografados() {
        java.util.List<File> backups = new java.util.ArrayList<>();
        File dir = new File(BACKUP_DIR);

        if (dir.exists()) {
            File[] arquivos = dir.listFiles((d, name) -> name.endsWith(".mpr"));
            if (arquivos != null) {
                java.util.Arrays.sort(arquivos, (a, b) ->
                        Long.compare(b.lastModified(), a.lastModified()));
                backups.addAll(java.util.Arrays.asList(arquivos));
            }
        }

        return backups;
    }

    /**
     * Comprime dados usando ZIP
     */
    private static byte[] comprimir(String dados) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            ZipEntry entry = new ZipEntry("dados.json");
            zos.putNextEntry(entry);
            zos.write(dados.getBytes("UTF-8"));
            zos.closeEntry();
        }
        return baos.toByteArray();
    }

    /**
     * Descomprime dados ZIP
     */
    private static String descomprimir(byte[] dados) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(dados);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(bais)) {
            zis.getNextEntry();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = zis.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
        }

        return new String(baos.toByteArray(), "UTF-8");
    }

    /**
     * Criptografa dados com AES
     */
    private static byte[] criptografar(byte[] dados, String senha) throws Exception {
        SecretKey key = gerarChave(senha);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(dados);
    }

    /**
     * Descriptografa dados com AES
     */
    private static byte[] descriptografar(byte[] dados, String senha) throws Exception {
        SecretKey key = gerarChave(senha);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(dados);
    }

    /**
     * Gera chave de criptografia a partir da senha
     */
    private static SecretKey gerarChave(String senha) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(senha.toCharArray(), SALT, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
    }

    /**
     * Verifica a integridade de um backup
     */
    public static boolean verificarIntegridade(String arquivo) {
        try {
            byte[] dados = Files.readAllBytes(Paths.get(arquivo));
            return dados.length > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtém informações de um backup
     */
    public static String getInfoBackup(String arquivo) {
        try {
            File file = new File(arquivo);
            return String.format(
                    "Arquivo: %s\nData: %s\nTamanho: %.2f KB",
                    file.getName(),
                    new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                            .format(new java.util.Date(file.lastModified())),
                    file.length() / 1024.0
            );
        } catch (Exception e) {
            return "Informações indisponíveis";
        }
    }
}