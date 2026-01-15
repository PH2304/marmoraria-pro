// Arquivo: src/main/java/com/marmoraria/config/Configuracao.java
package br.com.marmoraria.config;

import java.io.*;
import java.util.Properties;

public class Configuracao {
    private static final String CONFIG_FILE = "marmoraria_config.properties";
    private Properties props;

    public Configuracao() {
        props = new Properties();
        carregarConfig();
    }

    private void carregarConfig() {
        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                FileInputStream fis = new FileInputStream(configFile);
                props.load(fis);
                fis.close();
                System.out.println("✅ Configuração carregada: " + CONFIG_FILE);
            } else {
                criarConfigPadrao();
            }
        } catch (IOException e) {
            System.err.println("❌ Erro ao carregar configuração: " + e.getMessage());
            criarConfigPadrao();
        }
    }

    private void criarConfigPadrao() {
        // Valores padrão
        props.setProperty("fonte_precos", "LOCAL"); // LOCAL, API, HIBRIDO
        props.setProperty("api_habilitada", "false");
        props.setProperty("atualizar_auto", "true");
        props.setProperty("intervalo_atualizacao", "24"); // horas
        props.setProperty("margem_padrao", "30.0");
        props.setProperty("arquivo_precos", "precos_locais.csv");
        props.setProperty("usar_cache", "true");

        salvarConfig();
    }

    public void salvarConfig() {
        try {
            FileOutputStream fos = new FileOutputStream(CONFIG_FILE);
            props.store(fos, "Configuração Marmoraria Pro");
            fos.close();
            System.out.println("💾 Configuração salva");
        } catch (IOException e) {
            System.err.println("❌ Erro ao salvar configuração: " + e.getMessage());
        }
    }

    // Getters
    public String getFontePrecos() { return props.getProperty("fonte_precos", "LOCAL"); }
    public boolean isApiHabilitada() { return Boolean.parseBoolean(props.getProperty("api_habilitada", "false")); }
    public boolean isAtualizarAuto() { return Boolean.parseBoolean(props.getProperty("atualizar_auto", "true")); }
    public int getIntervaloAtualizacao() { return Integer.parseInt(props.getProperty("intervalo_atualizacao", "24")); }
    public double getMargemPadrao() { return Double.parseDouble(props.getProperty("margem_padrao", "30.0")); }
    public String getArquivoPrecos() { return props.getProperty("arquivo_precos", "precos_locais.csv"); }
    public boolean isUsarCache() { return Boolean.parseBoolean(props.getProperty("usar_cache", "true")); }

    // Setters
    public void setFontePrecos(String fonte) { props.setProperty("fonte_precos", fonte); salvarConfig(); }
    public void setApiHabilitada(boolean habilitada) { props.setProperty("api_habilitada", String.valueOf(habilitada)); salvarConfig(); }
    public void setArquivoPrecos(String arquivo) { props.setProperty("arquivo_precos", arquivo); salvarConfig(); }
}