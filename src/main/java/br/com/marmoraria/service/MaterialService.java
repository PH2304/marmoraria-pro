package br.com.marmoraria.service;

import br.com.marmoraria.model.Material;
import br.com.marmoraria.model.Servico;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MaterialService {

    private List<Material> materiais;
    private final List<Servico> servicos = new ArrayList<>();

    public MaterialService() {
        carregarServicos();
        carregarMateriais();
    }

    // ================= MATERIAIS =================

    private void carregarMateriais() {
        // Usar a API para obter materiais
        materiais = MaterialAPI.getMateriais();
    }

    /**
     * Força atualização dos materiais da internet
     */
    public void atualizarMateriais() {
        MaterialAPI.forcarAtualizacao();
        materiais = MaterialAPI.getMateriais();
        System.out.println("🔄 Materiais atualizados via internet!");
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

    public String getInfoUltimaAtualizacao() {
        return MaterialAPI.getInfoUltimaAtualizacao();
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
                "Corte em ângulo",
                60.00,
                "m",
                "Corte"
        ));

        servicos.add(new Servico(
                "S03",
                "Polimento simples",
                60.00,
                "m²",
                "Polimento"
        ));

        servicos.add(new Servico(
                "S04",
                "Polimento especial",
                90.00,
                "m²",
                "Polimento"
        ));

        servicos.add(new Servico(
                "S05",
                "Instalação padrão",
                250.00,
                "un",
                "Instalação"
        ));

        servicos.add(new Servico(
                "S06",
                "Instalação premium",
                400.00,
                "un",
                "Instalação"
        ));

        servicos.add(new Servico(
                "S07",
                "Furação para torneira",
                50.00,
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