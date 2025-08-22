package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
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
import br.gov.pr.guaira.animalys.entity.Solicitacao;
import br.gov.pr.guaira.animalys.entity.Status;
import br.gov.pr.guaira.animalys.filter.SolicitacaoFilter;
import br.gov.pr.guaira.animalys.report.GerarFichaCadastral;
import br.gov.pr.guaira.animalys.repository.Animais;
import br.gov.pr.guaira.animalys.repository.Solicitacoes;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.service.SolicitacaoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaVisitasAgendadas implements Serializable {

	private static final long serialVersionUID = 1L;

	public PesquisaVisitasAgendadas() {
		this.solicitacaoFilter = new SolicitacaoFilter();
		this.dataAgendaFinalCalendar = Calendar.getInstance();
		this.dataAgendaInicialCalendar = Calendar.getInstance();
		this.dataSelecionada = Calendar.getInstance();
		this.dataAgendaCalendar = Calendar.getInstance();
		this.dataAtual = Calendar.getInstance();
		this.event = new DefaultScheduleEvent();
	}

	private List<Solicitacao> solicitacoesCadastradas;
	private List<Solicitacao> solicitacoesAgendadas;
	private List<Animal> animaisCarregados;
	private SolicitacaoFilter solicitacaoFilter;
	private Solicitacao solicitacaoSelecionada;
	private Date dataAgendaInicial;
	private Calendar dataAgendaInicialCalendar;
	private Date dataAgendaFinal;
	private Calendar dataAgendaFinalCalendar;
	private String motivoCancelamento;
	private ScheduleModel eventModel;
	private ScheduleEvent event;
	private Calendar dataSelecionada;
	private Calendar dataAgendaCalendar;
	private Calendar dataAtual;

	public void inicializar() {
		this.pesquisar();
	
		this.buscaAgendasRealizadas();
	}

	@Inject
	private Solicitacoes solicitacoes;
	@Inject
	private Animais animais;
	@Inject
	private SolicitacaoService solicitacaoService;

	public SolicitacaoFilter getSolicitacaoFilter() {
		return solicitacaoFilter;
	}

	public void setSolicitacaoFilter(SolicitacaoFilter solicitacaoFilter) {
		this.solicitacaoFilter = solicitacaoFilter;
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
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

	public Status[] situacoes() {
		return Status.values();
	}

	public List<Animal> getAnimaisCarregados() {
		return animaisCarregados;
	}

	public void setAnimaisCarregados(List<Animal> animaisCarregados) {
		this.animaisCarregados = animaisCarregados;
	}

	public Date getDataAgendaInicial() {
		return dataAgendaInicial;
	}

	public void setDataAgendaInicial(Date dataAgendaInicial) {
		this.dataAgendaInicial = dataAgendaInicial;
	}

	public Calendar getDataAgendaInicialCalendar() {
		return dataAgendaInicialCalendar;
	}

	public void setDataAgendaInicialCalendar(Calendar dataAgendaInicialCalendar) {
		this.dataAgendaInicialCalendar = dataAgendaInicialCalendar;
	}

	public Date getDataAgendaFinal() {
		return dataAgendaFinal;
	}

	public void setDataAgendaFinal(Date dataAgendaFinal) {
		this.dataAgendaFinal = dataAgendaFinal;
	}

	public Calendar getDataAgendaFinalCalendar() {
		return dataAgendaFinalCalendar;
	}

	public void setDataAgendaFinalCalendar(Calendar dataAgendaFinalCalendar) {
		this.dataAgendaFinalCalendar = dataAgendaFinalCalendar;
	}

	public String getMotivoCancelamento() {
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	public Calendar getDataSelecionada() {
		return dataSelecionada;
	}

	public void setDataSelecionada(Calendar dataSelecionada) {
		this.dataSelecionada = dataSelecionada;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public void onEventSelect(SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();
		this.dataSelecionada.setTime(event.getStartDate());
		this.solicitacaoSelecionada = this.solicitacoes.porDataAgendada(this.dataSelecionada);
	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
	}

	public void pesquisar() {

		this.solicitacaoFilter.setStatus(Status.CONSULTA_ELETIVA_AGENDADA);
		this.solicitacoesCadastradas = this.solicitacoes.filtrados(this.solicitacaoFilter);
	}

	public void salvarDataVisita() {

		System.out.println("DATA TESTE " + this.event.getStartDate());
		this.dataAgendaCalendar.setTime(this.event.getStartDate());

		if (!confereData()) {

			try {

				this.solicitacaoSelecionada.setDataAgendaVisita(this.dataAgendaCalendar);

				this.solicitacaoSelecionada = this.solicitacaoService.salvar(this.solicitacaoSelecionada);
				FacesUtil.addInfoMessage(
						"Solicitação n°: " + this.solicitacaoSelecionada.getIdSolicitacao() + " agendada com sucesso!");
				limpar();
				this.inicializar();
			} catch (NegocioException e) {
				e.printStackTrace();
			}
		} else {
			FacesUtil.addErrorMessage("A data informada é inválida!");
		}
	}

	public void carregarAnimais() {
		this.animaisCarregados = this.animais.animaisDaSolicitacao(this.solicitacaoSelecionada.getIdSolicitacao());
	}

	public void cancelar() {
		try {
			this.solicitacoesCadastradas.remove(this.solicitacaoSelecionada);

			this.solicitacaoSelecionada.setStatus(Status.CANCELADO);
			this.solicitacaoSelecionada.setMotivoCancelamento(this.motivoCancelamento);
			this.solicitacaoService.salvar(this.solicitacaoSelecionada);

			FacesUtil.addInfoMessage(
					"Solicitação " + this.solicitacaoSelecionada.getIdSolicitacao() + " cancelada com sucesso!");
			this.pesquisar();
			limpar();
		} catch (NegocioException e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Esta solicitação não pode ser excluída!");
		}
	}

	public void gerarFichaCadastral() {
		GerarFichaCadastral fichaCadastral = new GerarFichaCadastral();

		List<Animal> animaisSolicitacao = this.animais
				.animaisPorProprietario(this.solicitacaoSelecionada.getProprietario());

		this.solicitacaoSelecionada.setAnimais(animaisSolicitacao);
		fichaCadastral.gerar(this.solicitacaoSelecionada);
	}

	private void limpar() {
		this.motivoCancelamento = "";
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

		} catch (Exception e) {
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
}
