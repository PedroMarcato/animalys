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
import br.gov.pr.guaira.animalys.entity.Status;
import br.gov.pr.guaira.animalys.filter.AnimalFilter;
import br.gov.pr.guaira.animalys.repository.Animais;
import br.gov.pr.guaira.animalys.service.AnimalService;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaCastracoesAgendadasBean implements Serializable{

	private static final long serialVersionUID = 1L;

	public PesquisaCastracoesAgendadasBean() {
		this.animalFilter = new AnimalFilter();
		this.dataCastracaoCalendar = Calendar.getInstance();
		this.dataAtual = Calendar.getInstance();
		this.dataSelecionada = Calendar.getInstance();
		this.event = new DefaultScheduleEvent();
		this.animalSelecionado = new Animal();
	}
	
	private AnimalFilter animalFilter;
	private Animal animalSelecionado;
	private Date dataCastracao;
	private Calendar dataCastracaoCalendar;
	private Calendar dataAtual;
	private Calendar dataSelecionada;
	private List<Animal> animaisFiltrados;
	private ScheduleModel eventModel;
	private ScheduleEvent event;
	
	@Inject
	private Animais animais;
	@Inject
	private AnimalService animalService;
	
	public void inicializar() {
		this.buscar();
		this.buscaAgendasRealizadas();
	}

	public AnimalFilter getAnimalFilter() {
		return animalFilter;
	}

	public void setAnimalFilter(AnimalFilter animalFilter) {
		this.animalFilter = animalFilter;
	}

	public Animal getAnimalSelecionado() {
		return animalSelecionado;
	}

	public void setAnimalSelecionado(Animal animalSelecionado) {
		this.animalSelecionado = animalSelecionado;
	}

	public List<Animal> getAnimaisFiltrados() {
		return animaisFiltrados;
	}
	
	public Date getDataCastracao() {
		return dataCastracao;
	}

	public void setDataCastracao(Date dataCastracao) {
		this.dataCastracao = dataCastracao;
	}
	
	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
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
		this.animalSelecionado = this.animais.porDataAgendaCastracao(this.dataSelecionada);
	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
	}
	

	public void buscar() {
		this.animalFilter.setStatus(Status.AGENDADOCASTRACAO);
		this.animaisFiltrados = this.animais.filtrados(animalFilter);
	}
	
	public void salvarDataAgendaCastracao() {
		
		this.dataCastracaoCalendar.setTime(this.event.getStartDate());

		if (!confereData()) {

			try {

				this.animalSelecionado.setDataAgendaCastracao(this.dataCastracaoCalendar);;

				this.animalSelecionado = this.animalService.salvar(this.animalSelecionado);
				FacesUtil.addInfoMessage(
						"O " + this.animalSelecionado.getNome() + " foi agendado com sucesso!");

				this.inicializar();
			} catch (NegocioException e) {
				e.printStackTrace();
			}
		} else {
			FacesUtil.addErrorMessage("A data informada é inválida!");
		}
		
	}
	
	public void cancelar() {
		this.animaisFiltrados.remove(this.animalSelecionado);
		
		this.animalSelecionado.setStatus(Status.CANCELADO);
		this.animalService.salvar(this.animalSelecionado);

		FacesUtil.addInfoMessage("Castracao cancelada com sucesso!");
	}
	
	private boolean confereData() {
		if (this.dataCastracaoCalendar.before(this.dataAtual)) {

			return true;
		} else if (verificaDataEscolhida()) {

			return true;
		} else {

			return false;
		}
	}

	private boolean verificaDataEscolhida() {
		try {
			this.animais.porDataAgendaCastracao(this.dataCastracaoCalendar);
			
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void buscaAgendasRealizadas() {
		
		this.eventModel = new DefaultScheduleModel();
				
		this.animaisFiltrados = this.animais.animaisAgendadoCastracao(Status.AGENDADOCASTRACAO);

		for (Animal animal : this.animaisFiltrados) {

			this.eventModel.addEvent(new DefaultScheduleEvent(animal.getNome(),
					animal.getDataAgendaCastracao().getTime(), animal.getDataAgendaCastracao().getTime()));
		}
	}
}
