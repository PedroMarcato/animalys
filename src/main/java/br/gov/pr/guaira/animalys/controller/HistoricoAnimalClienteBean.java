package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.dao.AnimalDAO;
import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.util.FacesUtil;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class HistoricoAnimalClienteBean implements Serializable {

    private List<Animal> animais;

    @Inject
    private AnimalDAO animalDAO;

    private Integer clienteId;

    @PostConstruct
    public void init() {
        clienteId = FacesUtil.getParameterAsInteger("clientId");

        if (clienteId != null) {
            animais = animalDAO.buscarPorProprietario(clienteId);
        }
    }

    public List<Animal> getAnimais() {
        return animais;
    }
}
