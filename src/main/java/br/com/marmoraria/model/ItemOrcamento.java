package br.com.marmoraria.model;

public class ItemOrcamento {

    private TipoTrabalho tipoTrabalho;
    private Material material;
    private Servico servico;
    private String descricao;

    private double largura;
    private double comprimento;

    private int quantidade;

    private double areaBase;
    private double areaAdicional;
    private double area;
    private double custoMaterial;
    private double custoServico;
    private double total;
    private double fatorComplexidade;
    private double fatorAcabamento;

    public ItemOrcamento(Material material,
                         Servico servico,
                         double largura,
                         double comprimento,
                         int quantidade,
                         double area,
                         double total) {
        this(TipoTrabalho.GENERICO, material, servico, "Item generico", largura, comprimento,
                quantidade, area, 0.0, total, 0.0, 1.0, 1.0);
    }

    public ItemOrcamento(TipoTrabalho tipoTrabalho,
                         Material material,
                         Servico servico,
                         String descricao,
                         double largura,
                         double comprimento,
                         int quantidade,
                         double areaBase,
                         double areaAdicional,
                         double custoMaterial,
                         double custoServico,
                         double fatorComplexidade,
                         double fatorAcabamento) {
        this.material = material;
        this.servico = servico;
        this.tipoTrabalho = tipoTrabalho;
        this.descricao = descricao;
        this.largura = largura;
        this.comprimento = comprimento;
        this.quantidade = quantidade;
        this.areaBase = areaBase;
        this.areaAdicional = areaAdicional;
        this.area = areaBase + areaAdicional;
        this.custoMaterial = custoMaterial;
        this.custoServico = custoServico;
        this.total = custoMaterial + custoServico;
        this.fatorComplexidade = fatorComplexidade;
        this.fatorAcabamento = fatorAcabamento;
    }

    // ================= GETTERS =================

    public TipoTrabalho getTipoTrabalho() {
        return tipoTrabalho == null ? TipoTrabalho.GENERICO : tipoTrabalho;
    }

    public Material getMaterial() {
        return material;
    }

    public Servico getServico() {
        return servico;
    }

    public double getLargura() {
        return largura;
    }

    public double getComprimento() {
        return comprimento;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getDescricao() {
        return descricao == null || descricao.isBlank() ? getTipoTrabalho().getDescricao() : descricao;
    }

    public double getAreaBase() {
        return areaBase;
    }

    public double getAreaAdicional() {
        return areaAdicional;
    }

    public double getArea() {
        return area;
    }

    public double getAreaM2() {
        return area;
    }

    public double getTotal() {
        return total;
    }

    public double getCustoMaterial() {
        return custoMaterial;
    }

    public double getCustoServico() {
        return custoServico;
    }

    public double getFatorComplexidade() {
        return fatorComplexidade <= 0 ? 1.0 : fatorComplexidade;
    }

    public double getFatorAcabamento() {
        return fatorAcabamento <= 0 ? 1.0 : fatorAcabamento;
    }

    public double getValorTotal() {
        return total;
    }
}
