package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import br.gov.pr.guaira.animalys.dao.HistoricoAnimalDAO;
import br.gov.pr.guaira.animalys.dto.HistoricoAnimalDTO;

@Named
@ViewScoped
public class HistoricoAnimalBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private HistoricoAnimalDAO historicoAnimalDAO;

    private Integer idAnimal;
    private List<HistoricoAnimalDTO> lista;
    private String nomeAnimal; // Novo campo

    @PostConstruct
    public void init() {
        // Obtem o parâmetro da URL
        String parametro = javax.faces.context.FacesContext.getCurrentInstance()
                             .getExternalContext().getRequestParameterMap().get("animalId");
        if (parametro != null && !parametro.isEmpty()) {
            idAnimal = Integer.valueOf(parametro);
            lista = historicoAnimalDAO.buscarHistoricoPorAnimal(idAnimal);

            // Pega o nome do animal do primeiro item da lista
            if (lista != null && !lista.isEmpty()) {
                nomeAnimal = lista.get(0).getNomeAnimal();
            } else {
                nomeAnimal = "Animal não identificado";
            }
        }
    }

    // Getters e Setters

    public List<HistoricoAnimalDTO> getLista() {
        return lista;
    }

    public void setLista(List<HistoricoAnimalDTO> lista) {
        this.lista = lista;
    }

    public Integer getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(Integer idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    // Adicione getters para os dados da ficha clínica do primeiro item
    public HistoricoAnimalDTO getFichaClinica() {
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }

    public void excluir(Integer idAtendimento) {
        historicoAnimalDAO.removerAtendimento(idAtendimento);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage("Atendimento excluído com sucesso."));
        lista = historicoAnimalDAO.buscarHistoricoPorAnimal(idAnimal);
    }
    // Getter para foto do animal
    public String getFotoAnimal() {
        HistoricoAnimalDTO dto = getFichaClinica();
        return (dto != null) ? dto.getFotoAnimal() : null;
    }
}
