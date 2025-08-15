package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.ModalidadeSolicitante;
import br.gov.pr.guaira.animalys.entity.Solicitacao;
import br.gov.pr.guaira.animalys.entity.Status;
import br.gov.pr.guaira.animalys.filter.SolicitacaoFilter;
import br.gov.pr.guaira.animalys.repository.Animais;
import br.gov.pr.guaira.animalys.repository.Solicitacoes;
import br.gov.pr.guaira.animalys.service.AnimalService;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.service.SolicitacaoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaSolicitacoesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public PesquisaSolicitacoesBean() {
		this.solicitacaoFilter = new SolicitacaoFilter();
		this.dataAgendaCalendar = Calendar.getInstance();
		this.solicitacoesCadastradas = new ArrayList<>();
		this.event = new DefaultScheduleEvent();
		this.dataSelecionada = Calendar.getInstance();
		this.dataAtual = Calendar.getInstance();

	}

	private List<Solicitacao> solicitacoesCadastradas;
	private List<Solicitacao> solicitacoesAgendadas;
	private List<Animal> animaisCarregados;
	private SolicitacaoFilter solicitacaoFilter;
	private Solicitacao solicitacaoSelecionada;
	private Calendar dataSelecionada;
	private Date dataAgenda;
	private Calendar dataAgendaCalendar;
	private Calendar dataAtual;
	private ScheduleModel eventModel;
	private ScheduleEvent event;
	private String fotoSelecionada;

	@Inject
	private Solicitacoes solicitacoes;
	@Inject
	private SolicitacaoService solicitacaoService;
	@Inject
	private Animais animais;
	@Inject
	private AnimalService animalService;

	public void inicializar() {
		
		this.pesquisar();
		this.buscaAgendasRealizadas();
	
	}

	public SolicitacaoFilter getSolicitacaoFilter() {
		return solicitacaoFilter;
	}

	public void setSolicitacaoFilter(SolicitacaoFilter solicitacaoFilter) {
		this.solicitacaoFilter = solicitacaoFilter;
	}

	public Solicitacao getSolicitacaoSelecionada() {
		return solicitacaoSelecionada;
	}

	public void setSolicitacaoSelecionada(Solicitacao solicitacaoSelecionada) {
		this.solicitacaoSelecionada = solicitacaoSelecionada;
	}

	public List<Solicitacao> getSolicitacoesCadastradas() {
		return solicitacoesCadastradas;
	}

	public Date getDataAgenda() {
		return dataAgenda;
	}

	public void setDataAgenda(Date dataAgenda) {
		this.dataAgenda = dataAgenda;
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public Status[] situacoes() {
		return Status.values();
	}

	public List<Animal> getAnimaisCarregados() {
		return animaisCarregados;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public void setAnimaisCarregados(List<Animal> animaisCarregados) {
		this.animaisCarregados = animaisCarregados;
	}
	
	public ModalidadeSolicitante[] getModalidades() {
		return ModalidadeSolicitante.values();
	}

	public void pesquisar() {
		this.solicitacaoFilter.setStatus(Status.SOLICITADO);
		this.solicitacoesCadastradas = this.solicitacoes.filtrados(this.solicitacaoFilter);
	}

	public void carregarAnimais() {
		this.animaisCarregados = this.animais.animaisPorSolicitacao(this.solicitacaoSelecionada.getIdSolicitacao());
	}

	public void onEventSelect(SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();
		this.dataSelecionada.setTime(event.getStartDate());
		this.solicitacaoSelecionada = this.solicitacoes.porDataAgendada(this.dataSelecionada);
	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
	}

	public void excluir() {
		try {
			this.solicitacoesCadastradas.remove(this.solicitacaoSelecionada);
			this.solicitacoes.remover(this.solicitacaoSelecionada);
		} catch (NegocioException e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Esta solicitação não pode ser excluída!");
		}
	}

	public void salvarDataVisita() {

		System.out.println("DATA TESTE " + this.event.getStartDate());
		this.dataAgendaCalendar.setTime(this.event.getStartDate());

		if (!confereData()) {

			try {

				this.solicitacaoSelecionada.setDataAgendaVisita(this.dataAgendaCalendar);
				this.solicitacaoSelecionada.setStatus(Status.CONSULTA_ELETIVA_AGENDADA);
				this.solicitacaoSelecionada = this.solicitacaoService.salvar(this.solicitacaoSelecionada);
				this.atualizaStatusAnimais();
				FacesUtil.addInfoMessage(
						"Solicitação n°: " + this.solicitacaoSelecionada.getIdSolicitacao() + " agendada com sucesso!");
				limpar();
				this.pesquisar();
				this.buscaAgendasRealizadas();
			} catch (NegocioException e) {
				e.printStackTrace();
			}
		} else {
			FacesUtil.addErrorMessage("A data informada é inválida!");
		}
	}

	private void limpar() {
		this.dataAgenda = null;
		this.solicitacoesCadastradas.clear();
	}

	public void atualizaStatusAnimais() {
		this.animaisCarregados = new ArrayList<>();
		this.carregarAnimais();
		for (Animal animal : this.animaisCarregados) {
			animal.setStatus(Status.CONSULTA_ELETIVA_AGENDADA);
			this.animalService.salvar(animal);
		}
	}

	private boolean confereData() {
		if (this.dataAgendaCalendar.before(this.dataAtual)) {

			return true;
		} else if (verificaDataEscolhida()) {

			return true;
		} else {
			
			return false;
		}
	}
	
	private boolean verificaDataEscolhida() {
		try {
			this.solicitacoes.porDataAgendada(this.dataAgendaCalendar);
			
			return true;
			
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void buscaAgendasRealizadas() {
		
		this.eventModel = new DefaultScheduleModel();
		this.solicitacoesAgendadas = this.solicitacoes.visitasAgendadas(Status.CONSULTA_ELETIVA_AGENDADA);

		for (Solicitacao soli : this.solicitacoesAgendadas) {

			this.eventModel.addEvent(new DefaultScheduleEvent(soli.getProprietario().getNome(),
					soli.getDataAgendaVisita().getTime(), soli.getDataAgendaVisita().getTime()));
		}
	}

	public String getFotoSelecionada() {
		return fotoSelecionada;
	}

	public void setFotoSelecionada(String fotoSelecionada) {
		this.fotoSelecionada = fotoSelecionada;
	}
	
	private String imagemSelecionada;

	public void selecionarImagem(String fotoPath) {
	    this.imagemSelecionada = fotoPath;
	}

	public String getImagemSelecionada() {
	    return imagemSelecionada;
	}

	public void setImagemSelecionada(String imagemSelecionada) {
	    this.imagemSelecionada = imagemSelecionada;
	}

}
