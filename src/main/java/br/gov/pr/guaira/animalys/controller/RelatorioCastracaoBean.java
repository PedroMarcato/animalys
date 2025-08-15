package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Status;
import br.gov.pr.guaira.animalys.filter.AnimalFilter;
import br.gov.pr.guaira.animalys.report.GerarRelatorioCastracoes;
import br.gov.pr.guaira.animalys.repository.Animais;

@Named
@ViewScoped
public class RelatorioCastracaoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public RelatorioCastracaoBean() {
		this.animalFilter = new AnimalFilter();
	}

	private AnimalFilter animalFilter;
	private GerarRelatorioCastracoes gerarRelatorioCastracoes;
	
	@Inject
	private Animais animais;

	public AnimalFilter getAnimalFilter() {
		return animalFilter;
	}

	public void setAnimalFilter(AnimalFilter animalFilter) {
		this.animalFilter = animalFilter;
	}
	
	
	
	public void gerarRelatorio() {
		this.gerarRelatorioCastracoes = new GerarRelatorioCastracoes();
		this.animalFilter.setStatus(Status.CASTRADO);
		this.gerarRelatorioCastracoes.gerar(this.animais.filtrados(animalFilter));

	}
	
}
