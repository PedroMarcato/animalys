package br.gov.pr.guaira.animalys.filter;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import br.gov.pr.guaira.animalys.entity.Raca;
import br.gov.pr.guaira.animalys.entity.Status;

public class AnimalFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String nomeProprietario;
	private String cpfProprietario;
	private Raca raca;
	private Date dataInicio;
	private Date datafim;
	private Calendar dataInicioCalendar;
	private Calendar dataFimCalendar;
	private Date dataInicioCastracao;
	private Date dataFimCastracao;
	private Calendar dataInicioCastracaoCalendar;
	private Calendar dataFimCastracaoCalendar;
	private boolean castrado;
	private boolean visitado;
	private Status status;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeProprietario() {
		return nomeProprietario;
	}

	public void setNomeProprietario(String nomeProprietario) {
		this.nomeProprietario = nomeProprietario;
	}

	public String getCpfProprietario() {
		return cpfProprietario;
	}

	public void setCpfProprietario(String cpfProprietario) {
		this.cpfProprietario = cpfProprietario;
	}

	public Raca getRaca() {
		return raca;
	}

	public void setRaca(Raca raca) {
		this.raca = raca;
	}

	public boolean isCastrado() {
		return castrado;
	}

	public void setCastrado(boolean castrado) {
		this.castrado = castrado;
	}

	public boolean isVisitado() {
		return visitado;
	}

	public void setVisitado(boolean visitado) {
		this.visitado = visitado;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDatafim() {
		return datafim;
	}

	public void setDatafim(Date datafim) {
		this.datafim = datafim;
	}

	public Calendar getDataInicioCalendar() {
		if (this.dataInicio != null) {
			this.dataInicioCalendar = Calendar.getInstance();
			this.dataInicioCalendar.setTime(this.dataInicio);
			return this.dataInicioCalendar;
		} else {
			return null;
		}
	}

	public Calendar getDataFimCalendar() {
		if (this.datafim != null) {
			this.dataFimCalendar = Calendar.getInstance();
			this.dataFimCalendar.setTime(this.datafim);
			return this.dataFimCalendar;
		} else {
			return null;
		}
	}
	
	

	public Date getDataInicioCastracao() {
		return dataInicioCastracao;
	}

	public void setDataInicioCastracao(Date dataInicioCastracao) {
		this.dataInicioCastracao = dataInicioCastracao;
	}

	public Date getDataFimCastracao() {
		return dataFimCastracao;
	}

	public void setDataFimCastracao(Date dataFimCastracao) {
		this.dataFimCastracao = dataFimCastracao;
	}

	public Calendar getDataInicioCastracaoCalendar() {
		if (this.dataInicioCastracao != null) {
			this.dataInicioCastracaoCalendar = Calendar.getInstance();
			this.dataInicioCastracaoCalendar.setTime(this.dataInicioCastracao);
			return this.dataInicioCastracaoCalendar;
		} else {
			return null;
		}
	}

	public Calendar getDataFimCastracaoCalendar() {
		if (this.dataFimCastracao != null) {
			this.dataFimCastracaoCalendar = Calendar.getInstance();
			this.dataFimCastracaoCalendar.setTime(this.dataFimCastracao);
			return this.dataFimCastracaoCalendar;
		} else {
			return null;
		}
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
