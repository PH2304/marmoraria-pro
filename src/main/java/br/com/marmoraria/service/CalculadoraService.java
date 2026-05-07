package br.com.marmoraria.service;

import br.com.marmoraria.model.ItemOrcamento;
import br.com.marmoraria.model.Material;
import br.com.marmoraria.model.Servico;
import br.com.marmoraria.model.TipoTrabalho;

public class CalculadoraService {

    public double calcularArea(double largura, double altura) {
        return (largura * altura) / 1000000;
    }

    public double calcularMaterial(double area, int qtd, double preco) {
        return area * qtd * preco;
    }

    public double calcularPerimetro(double largura, double altura) {
        return 2 * ((largura + altura) / 1000);
    }

    public double calcularCustoServico(Servico servico, double areaTotal, double largura, double comprimento, int quantidade) {
        if (servico == null) {
            return 0.0;
        }

        String unidade = servico.getUnidade();
        if ("m2".equalsIgnoreCase(unidade) || "m²".equalsIgnoreCase(unidade)) {
            return areaTotal * servico.getPreco();
        }
        if ("m".equalsIgnoreCase(unidade)) {
            return calcularPerimetro(largura, comprimento) * quantidade * servico.getPreco();
        }
        return quantidade * servico.getPreco();
    }

    public ItemOrcamento criarItemGenerico(Material material,
                                           Servico servico,
                                           double largura,
                                           double comprimento,
                                           int quantidade,
                                           double fatorComplexidade,
                                           boolean acabamentoEspecial) {
        double areaBase = calcularArea(largura, comprimento) * quantidade;
        double fatorAcabamento = acabamentoEspecial ? 1.11 : 1.0;
        double custoMaterial = areaBase * material.getPrecoPorMetroQuadrado() * fatorComplexidade * fatorAcabamento;
        double custoServico = calcularCustoServico(servico, areaBase, largura, comprimento, quantidade);

        return new ItemOrcamento(
                TipoTrabalho.GENERICO,
                material,
                servico,
                "Item generico",
                largura,
                comprimento,
                quantidade,
                areaBase,
                0.0,
                custoMaterial,
                custoServico,
                fatorComplexidade,
                fatorAcabamento
        );
    }

    public ItemOrcamento criarBancada(Material material,
                                      Servico servico,
                                      double largura,
                                      double comprimento,
                                      int quantidade,
                                      int qtdSaiaLateral,
                                      double alturaSaiaLateral,
                                      int qtdSaiaFrontal,
                                      double alturaSaiaFrontal,
                                      int qtdFrontaoLateral,
                                      double alturaFrontaoLateral,
                                      int qtdFrontaoParede,
                                      double alturaFrontaoParede,
                                      double fatorComplexidade,
                                      boolean acabamentoEspecial) {
        double areaBase = calcularArea(largura, comprimento) * quantidade;
        double areaAdicional =
                calcularArea(comprimento, alturaSaiaFrontal) * qtdSaiaFrontal * quantidade +
                calcularArea(largura, alturaSaiaLateral) * qtdSaiaLateral * quantidade +
                calcularArea(largura, alturaFrontaoParede) * qtdFrontaoParede * quantidade +
                calcularArea(comprimento, alturaFrontaoLateral) * qtdFrontaoLateral * quantidade;

        double fatorAcabamento = acabamentoEspecial ? 1.11 : 1.0;
        double areaTotal = areaBase + areaAdicional;
        double custoMaterial = areaTotal * material.getPrecoPorMetroQuadrado() * fatorComplexidade * fatorAcabamento;
        double custoServico = calcularCustoServico(servico, areaTotal, largura, comprimento, quantidade);

        String descricao = String.format(
                "Bancada %dx %.0f x %.0f mm | saias lat: %d | saias front: %d | frontoes lat: %d | frontoes parede: %d",
                quantidade,
                largura,
                comprimento,
                qtdSaiaLateral,
                qtdSaiaFrontal,
                qtdFrontaoLateral,
                qtdFrontaoParede
        );

        return new ItemOrcamento(
                TipoTrabalho.BANCADA,
                material,
                servico,
                descricao,
                largura,
                comprimento,
                quantidade,
                areaBase,
                areaAdicional,
                custoMaterial,
                custoServico,
                fatorComplexidade,
                fatorAcabamento
        );
    }

    public ItemOrcamento criarPeitoril(Material material,
                                       Servico servico,
                                       double largura,
                                       double profundidade,
                                       int quantidade,
                                       int qtdSaia,
                                       double alturaSaia,
                                       double fatorComplexidade,
                                       boolean acabamentoEspecial) {
        double areaBase = calcularArea(largura, profundidade) * quantidade;
        double areaAdicional = calcularArea(largura, alturaSaia) * qtdSaia * quantidade;
        double fatorAcabamento = acabamentoEspecial ? 1.11 : 1.0;
        double areaTotal = areaBase + areaAdicional;
        double custoMaterial = areaTotal * material.getPrecoPorMetroQuadrado() * fatorComplexidade * fatorAcabamento;
        double custoServico = calcularCustoServico(servico, areaTotal, largura, profundidade, quantidade);

        String descricao = String.format(
                "Peitoril %dx %.0f x %.0f mm | saias: %d",
                quantidade,
                largura,
                profundidade,
                qtdSaia
        );

        return new ItemOrcamento(
                TipoTrabalho.PEITORIL,
                material,
                servico,
                descricao,
                largura,
                profundidade,
                quantidade,
                areaBase,
                areaAdicional,
                custoMaterial,
                custoServico,
                fatorComplexidade,
                fatorAcabamento
        );
    }

    public ItemOrcamento criarNicho(Material material,
                                    Servico servico,
                                    double largura,
                                    double altura,
                                    double profundidade,
                                    int quantidade,
                                    int qtdFundo,
                                    int qtdAlizarLateral,
                                    double larguraAlizarLateral,
                                    int qtdAlizarSuperiorInferior,
                                    double larguraAlizarSuperiorInferior,
                                    int qtdPrateleira,
                                    double fatorComplexidade,
                                    boolean acabamentoEspecial) {
        double areaEstrutural = quantidade * (
                (2 * calcularArea(largura, profundidade)) +
                (2 * calcularArea(altura, profundidade))
        );
        double areaFundo = calcularArea(largura, altura) * qtdFundo * quantidade;
        double areaAlizarLateral = calcularArea(profundidade, larguraAlizarLateral) * qtdAlizarLateral * quantidade;
        double areaAlizarSuperiorInferior = calcularArea(profundidade, larguraAlizarSuperiorInferior)
                * qtdAlizarSuperiorInferior * quantidade;
        double areaPrateleira = calcularArea(largura, profundidade) * qtdPrateleira * quantidade;

        double areaAdicional = areaFundo + areaAlizarLateral + areaAlizarSuperiorInferior + areaPrateleira;
        double fatorAcabamento = acabamentoEspecial ? 1.11 : 1.0;
        double areaTotal = areaEstrutural + areaAdicional;
        double custoMaterial = areaTotal * material.getPrecoPorMetroQuadrado() * fatorComplexidade * fatorAcabamento;
        double custoServico = calcularCustoServico(servico, areaTotal, largura, profundidade, quantidade);

        String descricao = String.format(
                "Nicho %dx %.0f x %.0f x %.0f mm | fundo: %d | alizares lat: %d | alizares sup/inf: %d | prateleiras: %d",
                quantidade,
                largura,
                altura,
                profundidade,
                qtdFundo,
                qtdAlizarLateral,
                qtdAlizarSuperiorInferior,
                qtdPrateleira
        );

        return new ItemOrcamento(
                TipoTrabalho.NICHO,
                material,
                servico,
                descricao,
                largura,
                profundidade,
                quantidade,
                areaEstrutural,
                areaAdicional,
                custoMaterial,
                custoServico,
                fatorComplexidade,
                fatorAcabamento
        );
    }

    public ItemOrcamento criarSoleira(Material material,
                                      Servico servico,
                                      double largura,
                                      double profundidade,
                                      int quantidade,
                                      int qtdSaia,
                                      double alturaSaia,
                                      double fatorComplexidade,
                                      boolean acabamentoEspecial) {
        double areaBase = calcularArea(largura, profundidade) * quantidade;
        double areaAdicional = calcularArea(largura, alturaSaia) * qtdSaia * quantidade;
        double fatorAcabamento = acabamentoEspecial ? 1.11 : 1.0;
        double areaTotal = areaBase + areaAdicional;
        double custoMaterial = areaTotal * material.getPrecoPorMetroQuadrado() * fatorComplexidade * fatorAcabamento;
        double custoServico = calcularCustoServico(servico, areaTotal, largura, profundidade, quantidade);

        String descricao = String.format(
                "Soleira %dx %.0f x %.0f mm | saias: %d",
                quantidade,
                largura,
                profundidade,
                qtdSaia
        );

        return new ItemOrcamento(
                TipoTrabalho.SOLEIRA,
                material,
                servico,
                descricao,
                largura,
                profundidade,
                quantidade,
                areaBase,
                areaAdicional,
                custoMaterial,
                custoServico,
                fatorComplexidade,
                fatorAcabamento
        );
    }

    public ItemOrcamento criarEscada(Material material,
                                     Servico servico,
                                     double larguraDegrau,
                                     double profundidadeDegrau,
                                     int quantidadeDegraus,
                                     int qtdSaias,
                                     double alturaSaia,
                                     int qtdEspelhos,
                                     double alturaEspelho,
                                     double fatorComplexidade,
                                     boolean acabamentoEspecial) {
        double areaDegraus = calcularArea(larguraDegrau, profundidadeDegrau) * quantidadeDegraus;
        double areaSaias = calcularArea(profundidadeDegrau, alturaSaia) * qtdSaias * quantidadeDegraus;
        double areaEspelhos = calcularArea(larguraDegrau, alturaEspelho) * qtdEspelhos * quantidadeDegraus;
        double fatorAcabamento = acabamentoEspecial ? 1.11 : 1.0;
        double areaTotal = areaDegraus + areaSaias + areaEspelhos;
        double custoMaterial = areaTotal * material.getPrecoPorMetroQuadrado() * fatorComplexidade * fatorAcabamento;
        double custoServico = calcularCustoServico(servico, areaTotal, larguraDegrau, profundidadeDegrau, quantidadeDegraus);

        String descricao = String.format(
                "Escada %d degrau(s) %.0f x %.0f mm | saias: %d | espelhos: %d",
                quantidadeDegraus,
                larguraDegrau,
                profundidadeDegrau,
                qtdSaias,
                qtdEspelhos
        );

        return new ItemOrcamento(
                TipoTrabalho.ESCADA,
                material,
                servico,
                descricao,
                larguraDegrau,
                profundidadeDegrau,
                quantidadeDegraus,
                areaDegraus,
                areaSaias + areaEspelhos,
                custoMaterial,
                custoServico,
                fatorComplexidade,
                fatorAcabamento
        );
    }

    public ItemOrcamento criarRequadro(Material material,
                                       Servico servico,
                                       double largura,
                                       double altura,
                                       int quantidade,
                                       int qtdLaterais,
                                       double larguraLateral,
                                       int qtdSuperiorInferior,
                                       double larguraSuperiorInferior,
                                       double fatorComplexidade,
                                       boolean acabamentoEspecial) {
        double areaLaterais = calcularArea(altura, larguraLateral) * qtdLaterais * quantidade;
        double areaSuperiorInferior = calcularArea(largura, larguraSuperiorInferior) * qtdSuperiorInferior * quantidade;
        double fatorAcabamento = acabamentoEspecial ? 1.11 : 1.0;
        double areaTotal = areaLaterais + areaSuperiorInferior;
        double custoMaterial = areaTotal * material.getPrecoPorMetroQuadrado() * fatorComplexidade * fatorAcabamento;
        double custoServico = calcularCustoServico(servico, areaTotal, largura, altura, quantidade);

        String descricao = String.format(
                "Requadro %dx %.0f x %.0f mm | laterais: %d | sup/inf: %d",
                quantidade,
                largura,
                altura,
                qtdLaterais,
                qtdSuperiorInferior
        );

        return new ItemOrcamento(
                TipoTrabalho.REQUADRO,
                material,
                servico,
                descricao,
                largura,
                altura,
                quantidade,
                areaLaterais,
                areaSuperiorInferior,
                custoMaterial,
                custoServico,
                fatorComplexidade,
                fatorAcabamento
        );
    }

    public ItemOrcamento criarChapim(Material material,
                                     Servico servico,
                                     double largura,
                                     double profundidade,
                                     int quantidade,
                                     int qtdSaia,
                                     double alturaSaia,
                                     double fatorComplexidade,
                                     boolean acabamentoEspecial) {
        double areaBase = calcularArea(largura, profundidade) * quantidade;
        double areaAdicional = calcularArea(largura, alturaSaia) * qtdSaia * quantidade;
        double fatorAcabamento = acabamentoEspecial ? 1.11 : 1.0;
        double areaTotal = areaBase + areaAdicional;
        double custoMaterial = areaTotal * material.getPrecoPorMetroQuadrado() * fatorComplexidade * fatorAcabamento;
        double custoServico = calcularCustoServico(servico, areaTotal, largura, profundidade, quantidade);

        String descricao = String.format(
                "Chapim %dx %.0f x %.0f mm | saias: %d",
                quantidade,
                largura,
                profundidade,
                qtdSaia
        );

        return new ItemOrcamento(
                TipoTrabalho.CHAPIM,
                material,
                servico,
                descricao,
                largura,
                profundidade,
                quantidade,
                areaBase,
                areaAdicional,
                custoMaterial,
                custoServico,
                fatorComplexidade,
                fatorAcabamento
        );
    }

    public ItemOrcamento criarRodape(Material material,
                                     Servico servico,
                                     double comprimento,
                                     double altura,
                                     int quantidade,
                                     double fatorComplexidade,
                                     boolean acabamentoEspecial) {
        double areaBase = calcularArea(comprimento, altura) * quantidade;
        double fatorAcabamento = acabamentoEspecial ? 1.11 : 1.0;
        double custoMaterial = areaBase * material.getPrecoPorMetroQuadrado() * fatorComplexidade * fatorAcabamento;
        double custoServico = calcularCustoServico(servico, areaBase, comprimento, altura, quantidade);

        String descricao = String.format(
                "Rodape %dx %.0f x %.0f mm",
                quantidade,
                comprimento,
                altura
        );

        return new ItemOrcamento(
                TipoTrabalho.RODAPE,
                material,
                servico,
                descricao,
                comprimento,
                altura,
                quantidade,
                areaBase,
                0.0,
                custoMaterial,
                custoServico,
                fatorComplexidade,
                fatorAcabamento
        );
    }

    public ItemOrcamento criarPiso(Material material,
                                   Servico servico,
                                   double largura,
                                   double comprimento,
                                   int quantidade,
                                   double fatorComplexidade,
                                   boolean acabamentoEspecial) {
        double areaBase = calcularArea(largura, comprimento) * quantidade;
        double fatorAcabamento = acabamentoEspecial ? 1.11 : 1.0;
        double custoMaterial = areaBase * material.getPrecoPorMetroQuadrado() * fatorComplexidade * fatorAcabamento;
        double custoServico = calcularCustoServico(servico, areaBase, largura, comprimento, quantidade);

        String descricao = String.format(
                "Piso %dx %.0f x %.0f mm",
                quantidade,
                largura,
                comprimento
        );

        return new ItemOrcamento(
                TipoTrabalho.PISO,
                material,
                servico,
                descricao,
                largura,
                comprimento,
                quantidade,
                areaBase,
                0.0,
                custoMaterial,
                custoServico,
                fatorComplexidade,
                fatorAcabamento
        );
    }

    public ItemOrcamento criarTentoDoBox(Material material,
                                         Servico servico,
                                         double largura,
                                         double altura,
                                         int quantidade,
                                         double fatorComplexidade,
                                         boolean acabamentoEspecial) {
        double areaBase = calcularArea(largura, altura) * 2 * quantidade;
        double fatorAcabamento = acabamentoEspecial ? 1.11 : 1.0;
        double custoMaterial = areaBase * material.getPrecoPorMetroQuadrado() * fatorComplexidade * fatorAcabamento;
        double custoServico = calcularCustoServico(servico, areaBase, largura, altura, quantidade);

        String descricao = String.format(
                "Tento do Box %dx 2 faixa(s) %.0f x %.0f mm",
                quantidade,
                largura,
                altura
        );

        return new ItemOrcamento(
                TipoTrabalho.TENTO_DO_BOX,
                material,
                servico,
                descricao,
                largura,
                altura,
                quantidade,
                areaBase,
                0.0,
                custoMaterial,
                custoServico,
                fatorComplexidade,
                fatorAcabamento
        );
    }

    public ItemOrcamento criarMesa(Material material,
                                   Servico servico,
                                   double larguraTampo,
                                   double comprimentoTampo,
                                   int quantidade,
                                   int qtdSaiaLateral,
                                   double alturaSaiaLateral,
                                   int qtdSaiaFrontal,
                                   double alturaSaiaFrontal,
                                   boolean possuiSarim,
                                   double larguraPe,
                                   double comprimentoPe,
                                   int qtdPes,
                                   double fatorComplexidade,
                                   boolean acabamentoEspecial) {
        double areaTampo = calcularArea(larguraTampo, comprimentoTampo) * quantidade;
        double areaSaias =
                calcularArea(comprimentoTampo, alturaSaiaFrontal) * qtdSaiaFrontal * quantidade +
                calcularArea(larguraTampo, alturaSaiaLateral) * qtdSaiaLateral * quantidade;
        double areaPes = calcularArea(larguraPe, comprimentoPe) * qtdPes * quantidade;
        double adicionalSarim = possuiSarim ? areaTampo * 0.15 : 0.0;

        double fatorAcabamento = acabamentoEspecial ? 1.11 : 1.0;
        double areaTotal = areaTampo + areaSaias + areaPes + adicionalSarim;
        double custoMaterial = areaTotal * material.getPrecoPorMetroQuadrado() * fatorComplexidade * fatorAcabamento;
        double custoServico = calcularCustoServico(servico, areaTotal, larguraTampo, comprimentoTampo, quantidade);

        String descricao = String.format(
                "Mesa %dx %.0f x %.0f mm | saias lat: %d | saias front: %d | pes: %d%s",
                quantidade,
                larguraTampo,
                comprimentoTampo,
                qtdSaiaLateral,
                qtdSaiaFrontal,
                qtdPes,
                possuiSarim ? " | c/ sarim" : ""
        );

        return new ItemOrcamento(
                TipoTrabalho.MESA,
                material,
                servico,
                descricao,
                larguraTampo,
                comprimentoTampo,
                quantidade,
                areaTampo,
                areaSaias + areaPes + adicionalSarim,
                custoMaterial,
                custoServico,
                fatorComplexidade,
                fatorAcabamento
        );
    }

    public ItemOrcamento criarCubaEsculpida(Material material,
                                            Servico servico,
                                            double larguraCuba,
                                            double comprimentoCuba,
                                            double profundidadeCuba,
                                            int quantidade,
                                            int qtdFundosFalsos,
                                            double larguraFundoFalso,
                                            double comprimentoFundoFalso,
                                            double adicionalMaoDeObraPorUnidade,
                                            double fatorComplexidade,
                                            boolean acabamentoEspecial) {
        double areaEstrutural = quantidade * (
                2 * calcularArea(larguraCuba, profundidadeCuba) +
                2 * calcularArea(comprimentoCuba, profundidadeCuba) +
                calcularArea(larguraCuba, comprimentoCuba)
        );
        double areaFundosFalsos = calcularArea(larguraFundoFalso, comprimentoFundoFalso) * qtdFundosFalsos * quantidade;
        double fatorAcabamento = acabamentoEspecial ? 1.11 : 1.0;
        double areaTotal = areaEstrutural + areaFundosFalsos;
        double custoMaterial = areaTotal * material.getPrecoPorMetroQuadrado() * fatorComplexidade * fatorAcabamento;
        double custoServico = calcularCustoServico(servico, areaTotal, larguraCuba, comprimentoCuba, quantidade)
                + (adicionalMaoDeObraPorUnidade * quantidade);

        String descricao = String.format(
                "Cuba Esculpida %dx %.0f x %.0f x %.0f mm | fundos falsos: %d",
                quantidade,
                larguraCuba,
                comprimentoCuba,
                profundidadeCuba,
                qtdFundosFalsos
        );

        return new ItemOrcamento(
                TipoTrabalho.CUBA_ESCULPIDA,
                material,
                servico,
                descricao,
                larguraCuba,
                comprimentoCuba,
                quantidade,
                areaEstrutural,
                areaFundosFalsos,
                custoMaterial,
                custoServico,
                fatorComplexidade,
                fatorAcabamento
        );
    }
}
