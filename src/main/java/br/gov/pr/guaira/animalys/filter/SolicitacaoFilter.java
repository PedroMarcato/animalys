package br.gov.pr.guaira.animalys.filter;

import java.io.Serializable;
import java.util.Date;

import br.gov.pr.guaira.animalys.entity.ModalidadeSolicitante;
import br.gov.pr.guaira.animalys.entity.Status;

public class SolicitacaoFilter implements Serializable{

	private static final long serialVersionUID = 1L;

	private String proprietario;
	private Status status;
	private Date dataSolicitacao;
	private Date dataAgendaVisitaInicial;
	private Date dataAgendaVisitaFinal;
	private ModalidadeSolicitante modalidadeSolicitante;
	
	public String getProprietario() {
		return proprietario;
	}
	public void setProprietario(String proprietario) {
		this.proprietario = proprietario;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}
	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}
	public Date getDataAgendaVisitaInicial() {
		return dataAgendaVisitaInicial;
	}
	public void setDataAgendaVisitaInicial(Date dataAgendaVisitaInicial) {
		this.dataAgendaVisitaInicial = dataAgendaVisitaInicial;
	}
	public Date getDataAgendaVisitaFinal() {
		return dataAgendaVisitaFinal;
	}
	public void setDataAgendaVisitaFinal(Date dataAgendaVisitaFinal) {
		this.dataAgendaVisitaFinal = dataAgendaVisitaFinal;
	}
	public ModalidadeSolicitante getModalidadeSolicitante() {
		return modalidadeSolicitante;
	}
	public void setModalidadeSolicitante(ModalidadeSolicitante modalidadeSolicitante) {
		this.modalidadeSolicitante = modalidadeSolicitante;
	}
	
}
