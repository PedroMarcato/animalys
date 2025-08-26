package br.gov.pr.guaira.animalys.controller;

import br.gov.pr.guaira.animalys.dto.ConsultaRealizada;
import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.ConsultaFiltro;
import br.gov.pr.guaira.animalys.entity.Solicitacao;
import br.gov.pr.guaira.animalys.entity.Status;
import br.gov.pr.guaira.animalys.service.AnimalService;
import br.gov.pr.guaira.animalys.service.ConsultaRealizadaService;
import br.gov.pr.guaira.animalys.service.SolicitacaoService;
import br.gov.pr.guaira.animalys.repository.Animais;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.view.ViewScoped;

@Named
@ViewScoped // <-- Troque de @RequestScoped para @ViewScoped
public class ConsultasRealizadasBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ConsultaFiltro filtro = new ConsultaFiltro();

    @Inject
    private ConsultaRealizadaService service;

    @Inject
    private AnimalService servicoAnimal;

    @Inject
    private SolicitacaoService solicitacaoService;

    @Inject
    private Animais animaisRepo;

    private List<ConsultaRealizada> consultas;
    private ConsultaRealizada consultaSelecionada;
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

    public ConsultaRealizada getConsultaSelecionada() {
        return consultaSelecionada;
    }

    public void setConsultaSelecionada(ConsultaRealizada consultaSelecionada) {
        this.consultaSelecionada = consultaSelecionada;
    }

    public List<Animal> getAnimaisCarregados() {
        return animaisCarregados;
    }

    public void carregarAnimais() {
        if (consultaSelecionada != null && consultaSelecionada.getIdSolicitacao() != null) {
            this.animaisCarregados = servicoAnimal.buscarPorSolicitacao(consultaSelecionada.getIdSolicitacao());
        } else {
            this.animaisCarregados = Collections.emptyList();
        }
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

    public void agendarNovaConsulta() {
        try {
            if (consultaSelecionada == null) {
                FacesUtil.addErrorMessage("Selecione uma consulta para agendar.");
                return;
            }

            // Busca o animal pelo id
            Animal animal = animaisRepo.porId(consultaSelecionada.getIdAnimal());
            if (animal == null) {
                FacesUtil.addErrorMessage("Animal não encontrado.");
                return;
            }

            // Cria nova solicitação
            Solicitacao novaSolicitacao = new Solicitacao();
            novaSolicitacao.setData(Calendar.getInstance());
            novaSolicitacao.setStatus(Status.CONSULTA_ELETIVA_AGENDADA);
            novaSolicitacao.setProprietario(animal.getProprietario());
            novaSolicitacao.setAnimais(Arrays.asList(animal));

            // Salva a solicitação
            solicitacaoService.salvar(novaSolicitacao);

            FacesUtil.addInfoMessage("Nova consulta agendada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.addErrorMessage("Erro ao agendar nova consulta: " + e.getMessage());
        }
    }

    private Date dataNovaConsulta;

    public Date getDataNovaConsulta() {
        return dataNovaConsulta;
    }

    public void setDataNovaConsulta(Date dataNovaConsulta) {
        this.dataNovaConsulta = dataNovaConsulta;
    }

    public void salvarNovaConsulta() {
        try {
            if (consultaSelecionada == null || dataNovaConsulta == null) {
                FacesUtil.addErrorMessage("Selecione a consulta e a data.");
                return;
            }

            Animal animal = animaisRepo.porId(consultaSelecionada.getIdAnimal());
            if (animal == null) {
                FacesUtil.addErrorMessage("Animal não encontrado.");
                return;
            }

            Solicitacao novaSolicitacao = new Solicitacao();
            Calendar dataAgenda = Calendar.getInstance();
            dataAgenda.setTime(dataNovaConsulta);

            novaSolicitacao.setData(Calendar.getInstance());
            novaSolicitacao.setStatus(Status.CONSULTA_ELETIVA_AGENDADA);
            novaSolicitacao.setProprietario(animal.getProprietario());
            novaSolicitacao.setAnimais(Arrays.asList(animal));
            novaSolicitacao.setDataAgendaVisita(dataAgenda);

            solicitacaoService.salvar(novaSolicitacao);

            FacesUtil.addInfoMessage("Nova consulta agendada com sucesso!");
            this.dataNovaConsulta = null;
            this.consultaSelecionada = null;
            this.init(); // Atualiza a lista
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.addErrorMessage("Erro ao agendar nova consulta: " + e.getMessage());
        }
    }
}