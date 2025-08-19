package br.gov.pr.guaira.animalys.controller;

import br.gov.pr.guaira.animalys.dto.ConsultaRealizada;
import br.gov.pr.guaira.animalys.entity.ConsultaFiltro;
import br.gov.pr.guaira.animalys.service.ConsultaRealizadaService;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class ConsultasRealizadasBean {
    private ConsultaFiltro filtro = new ConsultaFiltro();

    @Inject
    private ConsultaRealizadaService service;

    private List<ConsultaRealizada> consultas;

    @PostConstruct
    public void init() {
        consultas = service.listar();
    }

    public List<ConsultaRealizada> getConsultas() {
        return consultas;
    }

    public void pesquisar() {
        consultas = service.buscarComFiltro(filtro);
    }

    public void setFiltro(ConsultaFiltro filtro) {
        this.filtro = filtro;
    }

    public ConsultaFiltro getFiltro() {
        return filtro;
    }

    public void limparFiltro() {
        filtro = new ConsultaFiltro();
        consultas = service.listar();
    }
}