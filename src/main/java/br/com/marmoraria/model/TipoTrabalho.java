package br.com.marmoraria.model;

/**
 * Enum com todos os tipos de trabalho da planilha Marmoraria Helomar.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public enum TipoTrabalho {

    // Tipos existentes
    GENERICO("Genérico"),
    BANCADA("Bancada", true, true),        // Tem saias, frontões, frontes
    PEITORIL("Peitoril", true),             // Tem saia
    NICHO("Nicho", true),                   // Tem fundo, alizares, prateleiras
    SOLEIRA("Soleira", true),               // Tem saia
    ESCADA("Escada", true),                 // Tem saia, espelho, degraus especiais
    REQUADRO("Requadro Porta", true),       // Tem laterais, superior, piso
    CHAPIM("Chapim", true),                 // Tem saia
    RODAPE("Rodapé"),
    PISO("Piso"),
    TENTO_DO_BOX("Tento do Box"),
    MESA("Mesa", true, true),              // Tem saias, sarim, pés
    CUBA_ESCULPIDA("Cuba Esculpida", true), // Tem fundo falso, mão de obra

    // NOVOS TIPOS DA PLANILHA
    ILHARGA("Ilharga", true),              // Tem saia, opção dupla, 2 faces polidas
    FILETE("Filete", true),                // Tem saia
    CUBA("Cuba", true),                    // Tipo 1 Rasa, Tipo 1 Funda, Tipo 2 Rasa, Tipo 2 Funda
    BALCAO_SECO("Balcão Seco", true),      // Tem saias, frontes
    SOCO("Soco"),                          // Rodapé especial
    FORRACAO("Forração"),                  // Revestimento de parede
    FORRACAO_BANHEIRA("Forração Banheira"), // Específico para banheira
    ALIZAR("Alizar"),                      // Moldura ao redor da porta/box
    REQUADRO_JANELA("Requadro Janela", true); // Similar ao requadro porta

    private final String descricao;
    private final boolean temAcessorios;    // Tem saias, frontões, etc.
    private final boolean temMultiplasPecas; // Várias peças (ex: bancada com saias)

    TipoTrabalho(String descricao) {
        this(descricao, false, false);
    }

    TipoTrabalho(String descricao, boolean temAcessorios) {
        this(descricao, temAcessorios, temAcessorios);
    }

    TipoTrabalho(String descricao, boolean temAcessorios, boolean temMultiplasPecas) {
        this.descricao = descricao;
        this.temAcessorios = temAcessorios;
        this.temMultiplasPecas = temMultiplasPecas;
    }

    public String getDescricao() { return descricao; }
    public boolean temAcessorios() { return temAcessorios; }
    public boolean temMultiplasPecas() { return temMultiplasPecas; }

    @Override
    public String toString() {
        return descricao;
    }
}