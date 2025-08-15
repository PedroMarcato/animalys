package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Grupo;
import br.gov.pr.guaira.animalys.filter.GrupoFilter;
import br.gov.pr.guaira.animalys.repository.Grupos;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaGrupoBean implements Serializable{

	private static final long serialVersionUID = 1L;

	public PesquisaGrupoBean() {
		this.grupoFilter = new GrupoFilter();
		this.gruposFiltrados = new ArrayList<>();
	}
	
	@Inject
	private Grupos grupos;
	
	private GrupoFilter grupoFilter;
	
	private Grupo grupoSelecionado;
	
	private List<Grupo> gruposFiltrados;

	public GrupoFilter getGrupoFilter() {
		return grupoFilter;
	}

	public void setGrupoFilter(GrupoFilter grupoFilter) {
		this.grupoFilter = grupoFilter;
	}

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

	public List<Grupo> getGruposFiltrados() {
		return gruposFiltrados;
	}

	public void setGrupoFiltrados(List<Grupo> gruposFiltrados) {
		this.gruposFiltrados = gruposFiltrados;
	}

	
	public void excluir(){
		grupos.remover(grupoSelecionado);
		gruposFiltrados.remove(grupoSelecionado);
		
		FacesUtil.addInfoMessage("Grupo " + grupoSelecionado.getDescricao() + " excluído com sucesso!");
	}
	
	public void pesquisar(){
		gruposFiltrados = grupos.filtrados(grupoFilter);
	}
	
}
