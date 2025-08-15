package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Marca;
import br.gov.pr.guaira.animalys.filter.MarcaFilter;
import br.gov.pr.guaira.animalys.repository.Marcas;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaMarcaBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public PesquisaMarcaBean() {
		this.marcaFilter = new MarcaFilter();
	}

	private MarcaFilter marcaFilter;
	private Marca marcaSelecionada;
	private List<Marca> marcasFiltradas;
	@Inject
	private Marcas marcas;
	
	public Marca getMarcaSelecionada() {
		return marcaSelecionada;
	}

	public void setMarcaSelecionada(Marca marcaSelecionada) {
		this.marcaSelecionada = marcaSelecionada;
	}
	
	public List<Marca> getMarcasFiltradas() {
		return marcasFiltradas;
	}
	
	public MarcaFilter getMarcaFilter() {
		return marcaFilter;
	}

	public void setMarcaFilter(MarcaFilter marcaFilter) {
		this.marcaFilter = marcaFilter;
	}

	public void pesquisar() {
		this.marcasFiltradas = this.marcas.filtrados(this.marcaFilter);
	}
	
	public void excluir() {
		try {
			this.marcas.remover(this.marcaSelecionada);
			this.marcasFiltradas.remove(this.marcaSelecionada);
			FacesUtil.addInfoMessage("Excluído com sucesso!");
		}catch (NegocioException e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Esta marca não pode ser excluída!");
		}
	}
}
