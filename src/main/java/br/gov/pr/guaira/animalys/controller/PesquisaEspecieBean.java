package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Especie;
import br.gov.pr.guaira.animalys.filter.EspecieFilter;
import br.gov.pr.guaira.animalys.repository.Especies;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaEspecieBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public PesquisaEspecieBean() {
		this.especieFilter = new EspecieFilter();
	}

	@Inject
	private Especies especies;
	
	private Especie especieSelecionada;
	private EspecieFilter especieFilter;
	private List<Especie> especiesFiltradas;
	
	
	public Especie getEspecieSelecionada() {
		return especieSelecionada;
	}

	public void setEspecieSelecionada(Especie especieSelecionada) {
		this.especieSelecionada = especieSelecionada;
	}

	public EspecieFilter getEspecieFilter() {
		return especieFilter;
	}

	public void setEspecieFilter(EspecieFilter especieFilter) {
		this.especieFilter = especieFilter;
	}

	public List<Especie> getEspeciesFiltradas() {
		return especiesFiltradas;
	}

	public void setEspeciesFiltradas(List<Especie> especiesFiltradas) {
		this.especiesFiltradas = especiesFiltradas;
	}

	public void excluir() {
		try {
			this.especies.remover(this.especieSelecionada);
			this.especiesFiltradas.remove(this.especieSelecionada);
			FacesUtil.addInfoMessage("Espécie excluída com sucesso!");
		}catch (NegocioException e) {
				e.printStackTrace();
				FacesUtil.addErrorMessage("Esta espécie está em uso no sistema!");
		}
		
	}
	public void pesquisar() {
		this.especiesFiltradas = this.especies.filtrados(this.especieFilter);
	}
}
