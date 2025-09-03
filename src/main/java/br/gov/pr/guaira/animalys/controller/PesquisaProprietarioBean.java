package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.filter.ProprietarioFilter;
import br.gov.pr.guaira.animalys.repository.Proprietarios;

@Named
@ViewScoped
public class PesquisaProprietarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public PesquisaProprietarioBean() {
		this.proprietarioFilter = new ProprietarioFilter();
	}

	private ProprietarioFilter proprietarioFilter;
	private List<Proprietario> proprietariosFiltrados;
	
	@Inject
	private Proprietarios proprietarios;
		
	public ProprietarioFilter getProprietarioFilter() {
		return proprietarioFilter;
	}
	
	public void setProprietarioFilter(ProprietarioFilter proprietarioFilter) {
		this.proprietarioFilter = proprietarioFilter;
	}
	
	public List<Proprietario> getProprietariosFiltrados() {
		return proprietariosFiltrados;
	}
	
	public void pesquisar() {
		this.proprietariosFiltrados = this.proprietarios.filtrados(this.proprietarioFilter);
	}
}
