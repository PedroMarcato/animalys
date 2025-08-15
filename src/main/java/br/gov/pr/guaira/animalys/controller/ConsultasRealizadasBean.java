package br.gov.pr.guaira.animalys.controller;

import br.gov.pr.guaira.animalys.dto.ConsultaRealizada;
import br.gov.pr.guaira.animalys.service.ConsultaRealizadaService;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ConsultasRealizadasBean implements Serializable {

    @Inject
    private ConsultaRealizadaService service;

    private List<ConsultaRealizada> consultas;

    @PostConstruct
    public void init() {
        consultas = service.buscarFinalizadas();
    }

    public List<ConsultaRealizada> getConsultas() {
        return consultas;
    }

    public String novaConsulta() {
        return "/atendimento/newatendimento.xhtml?faces-redirect=true";
    }
}
