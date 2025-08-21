package br.gov.pr.guaira.animalys.controller;

import br.gov.pr.guaira.animalys.dto.ConsultaRealizada;
import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Atendimento;
import br.gov.pr.guaira.animalys.entity.ConsultaFiltro;
import br.gov.pr.guaira.animalys.service.AnimalService;
import br.gov.pr.guaira.animalys.service.ConsultaRealizadaService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named
@RequestScoped
public class ConsultasRealizadasBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ConsultaFiltro filtro = new ConsultaFiltro();

    @Inject
    private ConsultaRealizadaService service;

    @Inject
    private AnimalService servicoAnimal;

    private List<ConsultaRealizada> consultas;
    private Atendimento consultaSelecionada;
    private List<Animal> animaisCarregados;

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

    public ConsultaFiltro getFiltro() {
        return filtro;
    }

    public void setFiltro(ConsultaFiltro filtro) {
        this.filtro = filtro;
    }

    public void limparFiltro() {
        filtro = new ConsultaFiltro();
        consultas = service.listar();
    }

    public Atendimento getConsultaSelecionada() {
        return consultaSelecionada;
    }

    public void setConsultaSelecionada(Atendimento consultaSelecionada) {
        this.consultaSelecionada = consultaSelecionada;
    }

    public List<Animal> getAnimaisCarregados() {
        return animaisCarregados;
    }

    public void carregarAnimais() {

        System.out.println("--- Debug carregarAnimais ---");
        System.out.println("consultaSelecionada: " + consultaSelecionada);

        if (consultaSelecionada != null) {
            System.out.println("consultaSelecionada.getSolicitacao(): " + consultaSelecionada.getSolicitacao());
            if (consultaSelecionada.getSolicitacao() != null) {
                System.out.println("consultaSelecionada.getSolicitacao().getIdSolicitacao(): "
                        + consultaSelecionada.getSolicitacao().getIdSolicitacao());
            }
        }

        if (consultaSelecionada != null
                && consultaSelecionada.getSolicitacao() != null
                && consultaSelecionada.getSolicitacao().getIdSolicitacao() != null) {

            System.out.println("Solicitação: " + consultaSelecionada.getSolicitacao().getIdSolicitacao());

            this.animaisCarregados = servicoAnimal.buscarPorSolicitacao(
                    consultaSelecionada.getSolicitacao().getIdSolicitacao());
        } else {
            System.out.println("consultaSelecionada ou sua solicitação está nula");
            this.animaisCarregados = Collections.emptyList();
        }
        System.out.println("--- Fim Debug carregarAnimais ---");
    }

    private String imagemSelecionada;

    public void selecionarImagem(String fotoPath) {
        System.out.println("Caminho da foto recebido: " + fotoPath);
        this.imagemSelecionada = fotoPath;
    }

    public String getImagemSelecionada() {
        return imagemSelecionada;
    }

    public void setImagemSelecionada(String imagemSelecionada) {
        this.imagemSelecionada = imagemSelecionada;
    }

}