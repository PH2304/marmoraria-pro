package br.com.marmoraria.service;

import br.com.marmoraria.model.Material;
import br.com.marmoraria.model.Servico;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MaterialService {

    private final List<Material> materiais = new ArrayList<>();
    private final List<Servico> servicos = new ArrayList<>();

    public MaterialService() {
        carregarMateriais();
        carregarServicos();
    }

    // ================= MATERIAIS =================

    private void carregarMateriais() {

        materiais.add(new Material(
                "M01",
                "Granito Preto Absoluto",
                "Granito",
                350.00,
                20,
                "Brasil",
                "Granito nacional de alta resistência"
        ));

        materiais.add(new Material(
                "M02",
                "Granito Verde Ubatuba",
                "Granito",
                280.00,
                20,
                "Brasil",
                "Granito de tom esverdeado"
        ));

        materiais.add(new Material(
                "M03",
                "Mármore Carrara",
                "Mármore",
                520.00,
                20,
                "Itália",
                "Mármore importado clássico"
        ));

        materiais.add(new Material(
                "M04",
                "Quartzo Branco",
                "Quartzo",
                650.00,
                20,
                "Industrializado",
                "Quartzo branco premium"
        ));
    }

    public List<Material> getTodosMateriais() {
        return materiais;
    }

    public Set<String> getTiposMateriais() {
        return materiais.stream()
                .map(Material::getTipo)
                .collect(Collectors.toSet());
    }

    public List<Material> getMateriaisPorTipo(String tipo) {
        return materiais.stream()
                .filter(m -> m.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    // ================= SERVIÇOS =================

    private void carregarServicos() {

        servicos.add(new Servico(
                "S01",
                "Corte reto",
                40.00,
                "m",
                "Corte"
        ));

        servicos.add(new Servico(
                "S02",
                "Polimento simples",
                60.00,
                "m²",
                "Polimento"
        ));

        servicos.add(new Servico(
                "S03",
                "Instalação padrão",
                250.00,
                "un",
                "Instalação"
        ));
    }

    public List<Servico> getTodosServicos() {
        return servicos;
    }

    public Set<String> getCategoriasServicos() {
        return servicos.stream()
                .map(Servico::getCategoria)
                .collect(Collectors.toSet());
    }

    public List<Servico> getServicosPorCategoria(String categoria) {
        return servicos.stream()
                .filter(s -> s.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }
}

