package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.filter.AnimalFilter;
import br.gov.pr.guaira.animalys.repository.Animais;
import br.gov.pr.guaira.animalys.service.AnimalService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaAnimalBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public PesquisaAnimalBean() {
		this.animalFilter = new AnimalFilter();
	}

	private AnimalFilter animalFilter;
	private List<Animal> animaisFiltrados;
	private Animal animalParaExcluir;
	
	@Inject
	private Animais animais;
	
	@Inject
	private AnimalService animalService;
		
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
	
	public void prepararExclusao(Animal animal) {
		this.animalParaExcluir = animal;
	}
	
	public void confirmarExclusao() {
		if (animalParaExcluir != null) {
			try {
				animalService.excluir(animalParaExcluir);
				// Atualiza a lista após exclusão
				pesquisar();
				FacesUtil.addInfoMessage("Animal excluído com sucesso!");
			} catch (Exception e) {
				FacesUtil.addErrorMessage("Erro ao excluir animal: " + e.getMessage());
			} finally {
				this.animalParaExcluir = null;
			}
		}
	}
}
