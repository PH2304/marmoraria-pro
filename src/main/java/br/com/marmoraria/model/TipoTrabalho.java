package br.com.marmoraria.model;

public enum TipoTrabalho {
    GENERICO("Generico"),
    BANCADA("Bancada"),
    PEITORIL("Peitoril"),
    NICHO("Nicho"),
    SOLEIRA("Soleira"),
    ESCADA("Escada"),
    REQUADRO("Requadro"),
    CHAPIM("Chapim"),
    RODAPE("Rodape"),
    PISO("Piso"),
    TENTO_DO_BOX("Tento do Box"),
    MESA("Mesa"),
    CUBA_ESCULPIDA("Cuba Esculpida");

    private final String descricao;

    TipoTrabalho(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
