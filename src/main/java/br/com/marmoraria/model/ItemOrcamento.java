package br.com.marmoraria.model;

public class ItemOrcamento {

    private Material material;
    private Servico servico;

    private double largura;
    private double comprimento;

    private int quantidade;

    private double area;
    private double total;

    public ItemOrcamento(Material material,
                         Servico servico,
                         double largura,
                         double comprimento,
                         int quantidade,
                         double area,
                         double total) {

        this.material = material;
        this.servico = servico;
        this.largura = largura;
        this.comprimento = comprimento;
        this.quantidade = quantidade;
        this.area = area;
        this.total = total;
    }

    // ================= GETTERS =================

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

    public double getArea() {
        return area;
    }

    public double getAreaM2() {
        return area;
    }

    public double getTotal() {
        return total;
    }

    public double getValorTotal() {
        return total;
    }
}