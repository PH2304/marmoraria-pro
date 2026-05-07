package br.com.marmoraria.model;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Configuracoes implements Serializable {

    private static final String ARQUIVO_CONFIG = "config.dat";

    // Configurações de orçamento
    private double margemLucroPadrao;
    private double descontoPadrao;
    private int prazoValidadeDias;

    // Configurações de notificação
    private boolean notificarBackup;
    private boolean notificarOrcamentoVencendo;
    private int diasAvisoAntecedencia;

    // Configurações de aparência
    private String tema; // "claro" ou "escuro"
    private String fontePadrao;
    private int tamanhoFonte;

    // Configurações de empresa
    private String nomeEmpresa;
    private String telefoneEmpresa;
    private String emailEmpresa;
    private String enderecoEmpresa;
    private String cnpj;
    private String logoPath;

    // Configurações de backup
    private boolean backupAutomatico;
    private int intervaloBackupHoras;
    private int maxBackups;

    // Configurações de impressão
    private boolean imprimirAutomatico;
    private String pastaPDF;

    public Configuracoes() {
        // Valores padrão
        this.margemLucroPadrao = 30.0;
        this.descontoPadrao = 0.0;
        this.prazoValidadeDias = 30;

        this.notificarBackup = true;
        this.notificarOrcamentoVencendo = true;
        this.diasAvisoAntecedencia = 5;

        this.tema = "claro";
        this.fontePadrao = "Segoe UI";
        this.tamanhoFonte = 13;

        this.nomeEmpresa = "Marmoraria Pro";
        this.telefoneEmpresa = "(11) 99999-9999";
        this.emailEmpresa = "contato@marmorariapro.com.br";
        this.enderecoEmpresa = "Av. Principal, 1000 - São Paulo, SP";
        this.cnpj = "00.000.000/0001-00";
        this.logoPath = "";

        this.backupAutomatico = true;
        this.intervaloBackupHoras = 24;
        this.maxBackups = 30;

        this.imprimirAutomatico = false;
        this.pastaPDF = "pdfs";
    }

    /**
     * Salva as configurações em arquivo
     */
    public boolean salvar() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_CONFIG))) {
            oos.writeObject(this);
            System.out.println("⚙️ Configurações salvas com sucesso!");
            return true;
        } catch (IOException e) {
            System.err.println("❌ Erro ao salvar configurações: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carrega as configurações do arquivo
     */
    public static Configuracoes carregar() {
        File arquivo = new File(ARQUIVO_CONFIG);
        if (!arquivo.exists()) {
            System.out.println("📁 Arquivo de configuração não encontrado. Criando padrão...");
            Configuracoes config = new Configuracoes();
            config.salvar();
            return config;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            Configuracoes config = (Configuracoes) ois.readObject();
            System.out.println("⚙️ Configurações carregadas com sucesso!");
            return config;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Erro ao carregar configurações: " + e.getMessage());
            return new Configuracoes();
        }
    }

    // Getters e Setters
    public double getMargemLucroPadrao() { return margemLucroPadrao; }
    public void setMargemLucroPadrao(double margemLucroPadrao) { this.margemLucroPadrao = margemLucroPadrao; }

    public double getDescontoPadrao() { return descontoPadrao; }
    public void setDescontoPadrao(double descontoPadrao) { this.descontoPadrao = descontoPadrao; }

    public int getPrazoValidadeDias() { return prazoValidadeDias; }
    public void setPrazoValidadeDias(int prazoValidadeDias) { this.prazoValidadeDias = prazoValidadeDias; }

    public boolean isNotificarBackup() { return notificarBackup; }
    public void setNotificarBackup(boolean notificarBackup) { this.notificarBackup = notificarBackup; }

    public boolean isNotificarOrcamentoVencendo() { return notificarOrcamentoVencendo; }
    public void setNotificarOrcamentoVencendo(boolean notificarOrcamentoVencendo) { this.notificarOrcamentoVencendo = notificarOrcamentoVencendo; }

    public int getDiasAvisoAntecedencia() { return diasAvisoAntecedencia; }
    public void setDiasAvisoAntecedencia(int diasAvisoAntecedencia) { this.diasAvisoAntecedencia = diasAvisoAntecedencia; }

    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }

    public String getFontePadrao() { return fontePadrao; }
    public void setFontePadrao(String fontePadrao) { this.fontePadrao = fontePadrao; }

    public int getTamanhoFonte() { return tamanhoFonte; }
    public void setTamanhoFonte(int tamanhoFonte) { this.tamanhoFonte = tamanhoFonte; }

    public String getNomeEmpresa() { return nomeEmpresa; }
    public void setNomeEmpresa(String nomeEmpresa) { this.nomeEmpresa = nomeEmpresa; }

    public String getTelefoneEmpresa() { return telefoneEmpresa; }
    public void setTelefoneEmpresa(String telefoneEmpresa) { this.telefoneEmpresa = telefoneEmpresa; }

    public String getEmailEmpresa() { return emailEmpresa; }
    public void setEmailEmpresa(String emailEmpresa) { this.emailEmpresa = emailEmpresa; }

    public String getEnderecoEmpresa() { return enderecoEmpresa; }
    public void setEnderecoEmpresa(String enderecoEmpresa) { this.enderecoEmpresa = enderecoEmpresa; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getLogoPath() { return logoPath; }
    public void setLogoPath(String logoPath) { this.logoPath = logoPath; }

    public boolean isBackupAutomatico() { return backupAutomatico; }
    public void setBackupAutomatico(boolean backupAutomatico) { this.backupAutomatico = backupAutomatico; }

    public int getIntervaloBackupHoras() { return intervaloBackupHoras; }
    public void setIntervaloBackupHoras(int intervaloBackupHoras) { this.intervaloBackupHoras = intervaloBackupHoras; }

    public int getMaxBackups() { return maxBackups; }
    public void setMaxBackups(int maxBackups) { this.maxBackups = maxBackups; }

    public boolean isImprimirAutomatico() { return imprimirAutomatico; }
    public void setImprimirAutomatico(boolean imprimirAutomatico) { this.imprimirAutomatico = imprimirAutomatico; }

    public String getPastaPDF() { return pastaPDF; }
    public void setPastaPDF(String pastaPDF) { this.pastaPDF = pastaPDF; }

    public String getDataHoraUltimaAtualizacao() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}