package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Status;
import br.gov.pr.guaira.animalys.filter.AnimalFilter;
import br.gov.pr.guaira.animalys.report.GerarCarteirinha;
import br.gov.pr.guaira.animalys.repository.Animais;

@Named
@ViewScoped
public class PesquisaCastracoesRealizadas implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public PesquisaCastracoesRealizadas() {
		this.animalFilter = new AnimalFilter();
	}
	
	private AnimalFilter animalFilter;
	private Animal animalSelecionado;
	private List<Animal> animaisFiltrados;
	
	@Inject
	private Animais animais;
	
	public void inicializar() {
		this.pesquisar();
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
	
	public void pesquisar() {
		this.animalFilter.setStatus(Status.CASTRADO);
		this.animaisFiltrados = this.animais.animaisCastrados(this.animalFilter);
	}
	
	public void gerarCarteirinha() {
		GerarCarteirinha gerar = new GerarCarteirinha();
		gerar.gerar(this.animalSelecionado);
	}
}
