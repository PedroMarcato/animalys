package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.filter.AnimalFilter;
import br.gov.pr.guaira.animalys.repository.Animais;

@Named
@ViewScoped
public class PesquisaAnimalBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public PesquisaAnimalBean() {
		this.animalFilter = new AnimalFilter();
	}

	private AnimalFilter animalFilter;
	private List<Animal> animaisFiltrados;
	
	@Inject
	private Animais animais;
		
	public AnimalFilter getAnimalFilter() {
		return animalFilter;
	}
	
	public void setAnimalFilter(AnimalFilter animalFilter) {
		this.animalFilter = animalFilter;
	}
	
	public List<Animal> getAnimaisFiltrados() {
		return animaisFiltrados;
	}
	
	public void pesquisar() {
		this.animaisFiltrados = this.animais.filtrados(this.animalFilter);
	}
}
