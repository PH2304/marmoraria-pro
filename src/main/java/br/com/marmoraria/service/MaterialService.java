package br.com.marmoraria.service;

import br.com.marmoraria.model.Material;
import br.com.marmoraria.model.Servico;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaterialService {

    private List<Material> materiais;
    private List<Servico> servicos;

    public MaterialService() {
        inicializarMateriais();
        inicializarServicos();
    }

    private void inicializarMateriais() {
        materiais = new ArrayList<>();

        // Mármores
        materiais.add(new Material("M001", "Mármore Branco Carrara", "Mármore", 350.00, 20, "Itália",
                "Mármore clássico italiano com veios cinzas sutis"));
        materiais.add(new Material("M002", "Mármore Crema Marfil", "Mármore", 420.00, 20, "Espanha",
                "Mármore bege claro de alta qualidade"));
        materiais.add(new Material("M003", "Mármore Nero Marquina", "Mármore", 480.00, 20, "Espanha",
                "Mármore preto com veios brancos"));

        // Granitos
        materiais.add(new Material("G001", "Granito Preto Absoluto", "Granito", 280.00, 30, "Brasil",
                "Granito preto brilhante de alta resistência"));
        materiais.add(new Material("G002", "Granito Branco Itaúnas", "Granito", 320.00, 30, "Brasil",
                "Granito branco com pontuações cinzas"));
        materiais.add(new Material("G003", "Granito Verde Ubatuba", "Granito", 310.00, 30, "Brasil",
                "Granito verde escuro com brilho natural"));

        // Quartzos
        materiais.add(new Material("Q001", "Quartzo Branco Neve", "Quartzo", 650.00, 20, "Brasil",
                "Superfície de quartzo branco puro"));
        materiais.add(new Material("Q002", "Quartzo Cinza Concreto", "Quartzo", 680.00, 20, "Brasil",
                "Quartzo cinza com aspecto industrial"));
        materiais.add(new Material("Q003", "Quartzo Calacatta", "Quartzo", 720.00, 20, "Brasil",
                "Quartzo com aparência de mármore Calacatta"));
    }

    private void inicializarServicos() {
        servicos = new ArrayList<>();

        // Corte
        servicos.add(new Servico("S001", "Corte reto", 25.00, "metro linear", "Corte"));
        servicos.add(new Servico("S002", "Corte curvo", 45.00, "metro linear", "Corte"));
        servicos.add(new Servico("S003", "Corte especial (45º)", 35.00, "metro linear", "Corte"));

        // Polimento
        servicos.add(new Servico("S004", "Polimento de borda reta", 40.00, "metro linear", "Polimento"));
        servicos.add(new Servico("S005", "Polimento de borda arredondada", 55.00, "metro linear", "Polimento"));
        servicos.add(new Servico("S006", "Polimento de borda bisotada", 65.00, "metro linear", "Polimento"));
        servicos.add(new Servico("S007", "Polimento de superfície", 80.00, "metro quadrado", "Polimento"));

        // Instalação
        servicos.add(new Servico("S008", "Instalação de bancada", 150.00, "unidade", "Instalação"));
        servicos.add(new Servico("S009", "Instalação de pia", 120.00, "unidade", "Instalação"));
        servicos.add(new Servico("S010", "Instalação de soleira", 35.00, "metro linear", "Instalação"));

        // Outros
        servicos.add(new Servico("S011", "Furação para torneira", 30.00, "unidade", "Outros"));
        servicos.add(new Servico("S012", "Furação para sifão", 25.00, "unidade", "Outros"));
        servicos.add(new Servico("S013", "Selagem", 15.00, "metro linear", "Outros"));
    }

    public List<Material> getMateriais() {
        return new ArrayList<>(materiais);
    }

    public List<Material> getMateriaisPorTipo(String tipo) {
        List<Material> filtrados = new ArrayList<>();
        for (Material material : materiais) {
            if (material.getTipo().equalsIgnoreCase(tipo)) {
                filtrados.add(material);
            }
        }
        return filtrados;
    }

    public List<Servico> getServicos() {
        return new ArrayList<>(servicos);
    }

    public List<Servico> getServicosPorCategoria(String categoria) {
        List<Servico> filtrados = new ArrayList<>();
        for (Servico servico : servicos) {
            if (servico.getCategoria().equalsIgnoreCase(categoria)) {
                filtrados.add(servico);
            }
        }
        return filtrados;
    }

    public void adicionarMaterial(Material material) {
        materiais.add(material);
    }

    public void adicionarServico(Servico servico) {
        servicos.add(servico);
    }

    public List<String> getTiposMateriais() {
        return Arrays.asList("Mármore", "Granito", "Quartzo", "Outros");
    }

    public List<String> getCategoriasServicos() {
        return Arrays.asList("Corte", "Polimento", "Instalação", "Outros");
    }
}
