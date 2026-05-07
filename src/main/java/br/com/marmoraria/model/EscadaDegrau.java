package br.com.marmoraria.model;

/**
 * Representa um degrau de escada com todas as opções da planilha.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class EscadaDegrau {

    public enum TipoDegrau {
        NORMAL("Normal"),
        SAIA("Saia"),
        ESPELHO("Espelho"),
        DEGRAU_ESPECIAL("Degrau Especial"),
        SAIA_ESPECIAL("Saia Especial"),
        ESPELHO_ESPECIAL("Espelho Especial");

        private final String descricao;

        TipoDegrau(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() { return descricao; }

        @Override
        public String toString() { return descricao; }
    }

    private double largura;          // Largura do degrau em mm
    private double comprimento;      // Comprimento/profundidade em mm
    private double alturaEspelho;    // Altura do espelho em mm
    private int quantidade;
    private TipoDegrau tipo;
    private boolean temSaia;
    private double alturaSaia;
    private boolean temEspelho;
    private boolean especial;        // Degrau com formato irregular
    private double larguraEspecial;  // Para degraus especiais
    private double alturaEspecial;   // Para degraus especiais

    public EscadaDegrau() {
        this.tipo = TipoDegrau.NORMAL;
        this.quantidade = 1;
        this.temSaia = false;
        this.temEspelho = false;
        this.especial = false;
    }

    public EscadaDegrau(double largura, double comprimento, int quantidade) {
        this();
        this.largura = largura;
        this.comprimento = comprimento;
        this.quantidade = quantidade;
    }

    /**
     * Calcula a área do degrau
     */
    public double calcularArea() {
        double areaBase = (largura * comprimento) / 1000000.0;
        return areaBase * quantidade;
    }

    /**
     * Calcula a área da saia (se existir)
     */
    public double calcularAreaSaia() {
        if (!temSaia || alturaSaia <= 0) {
            return 0.0;
        }
        return (comprimento * alturaSaia) / 1000000.0 * quantidade;
    }

    /**
     * Calcula a área do espelho (se existir)
     */
    public double calcularAreaEspelho() {
        if (!temEspelho || alturaEspelho <= 0) {
            return 0.0;
        }
        return (largura * alturaEspelho) / 1000000.0 * quantidade;
    }

    /**
     * Calcula a área total (degrau + saia + espelho)
     */
    public double calcularAreaTotal() {
        return calcularArea() + calcularAreaSaia() + calcularAreaEspelho();
    }

    /**
     * Gera descrição do degrau
     */
    public String getDescricao() {
        StringBuilder sb = new StringBuilder();

        if (quantidade > 1) {
            sb.append(quantidade).append(" Degraus de ");
        } else {
            sb.append("Degrau ");
        }

        sb.append(String.format("%.0f x %.0f mm", largura, comprimento));

        if (temSaia) {
            sb.append(String.format(" + Saia %.0f mm", alturaSaia));
        }

        if (temEspelho) {
            sb.append(String.format(" + Espelho %.0f mm", alturaEspelho));
        }

        if (especial) {
            sb.append(" (Especial)");
        }

        return sb.toString();
    }

    // Getters e Setters
    public double getLargura() { return largura; }
    public void setLargura(double largura) { this.largura = largura; }

    public double getComprimento() { return comprimento; }
    public void setComprimento(double comprimento) { this.comprimento = comprimento; }

    public double getAlturaEspelho() { return alturaEspelho; }
    public void setAlturaEspelho(double alturaEspelho) { this.alturaEspelho = alturaEspelho; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public TipoDegrau getTipo() { return tipo; }
    public void setTipo(TipoDegrau tipo) { this.tipo = tipo; }

    public boolean isTemSaia() { return temSaia; }
    public void setTemSaia(boolean temSaia) { this.temSaia = temSaia; }

    public double getAlturaSaia() { return alturaSaia; }
    public void setAlturaSaia(double alturaSaia) { this.alturaSaia = alturaSaia; }

    public boolean isTemEspelho() { return temEspelho; }
    public void setTemEspelho(boolean temEspelho) { this.temEspelho = temEspelho; }

    public boolean isEspecial() { return especial; }
    public void setEspecial(boolean especial) { this.especial = especial; }

    public double getLarguraEspecial() { return larguraEspecial; }
    public void setLarguraEspecial(double larguraEspecial) { this.larguraEspecial = larguraEspecial; }

    public double getAlturaEspecial() { return alturaEspecial; }
    public void setAlturaEspecial(double alturaEspecial) { this.alturaEspecial = alturaEspecial; }

    @Override
    public String toString() {
        return getDescricao();
    }
}