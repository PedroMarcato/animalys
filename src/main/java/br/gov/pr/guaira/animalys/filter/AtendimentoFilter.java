package br.gov.pr.guaira.animalys.filter;

import java.util.Calendar;

import br.gov.pr.guaira.animalys.entity.Profissional;

public class AtendimentoFilter {

	private Profissional profissional;
	private Calendar dataInicial;
	private Calendar dataFinal;
	
	public Profissional getProfissional() {
		return profissional;
	}
	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}
	public Calendar getDataInicial() {
		return dataInicial;
	}
	public void setDataInicial(Calendar dataInicial) {
		this.dataInicial = dataInicial;
	}
	public Calendar getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(Calendar dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	
}
