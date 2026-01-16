package br.com.marmoraria.service;

import br.com.marmoraria.model.Material;
import br.com.marmoraria.model.Servico;

import java.util.List;

public class MaterialService {

    public List<Material> listarMateriais() {
        return List.of(
                new Material("1", "Granito Preto", "Granito", 250, 20, "Brasil", ""),
                new Material("2", "Mármore Carrara", "Mármore", 380, 20, "Itália", "")
        );
    }

    public List<Servico> listarServicos() {
        return List.of(
                new Servico("1", "Corte", 50, "un", "Corte"),
                new Servico("2", "Instalação", 120, "un", "Instalação")
        );
    }
}
