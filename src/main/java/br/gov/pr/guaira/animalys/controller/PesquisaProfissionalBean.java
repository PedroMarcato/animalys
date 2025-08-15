package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Profissional;
import br.gov.pr.guaira.animalys.filter.ProfissionalFilter;
import br.gov.pr.guaira.animalys.repository.Profissionais;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaProfissionalBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public PesquisaProfissionalBean() {
		this.profissionalFilter = new ProfissionalFilter();
	}
	

	private Profissional profissionalSelecionado;
	private ProfissionalFilter profissionalFilter;
	private List<Profissional> profissionaisFiltrados;
	@Inject
	private Profissionais profissionais;
	
	public Profissional getProfissionalSelecionado() {
		return profissionalSelecionado;
	}
	public void setProfissionalSelecionado(Profissional profissionalSelecionado) {
		this.profissionalSelecionado = profissionalSelecionado;
	}
	public ProfissionalFilter getProfissionalFilter() {
		return profissionalFilter;
	}
	public void setProfissionalFilter(ProfissionalFilter profissionalFilter) {
		this.profissionalFilter = profissionalFilter;
	}
	
	public List<Profissional> getProfissionaisFiltrados() {
		return profissionaisFiltrados;
	}
	public void excluir() {
		try {
			this.profissionais.remover(this.profissionalSelecionado);
			this.profissionaisFiltrados.remove(this.profissionalSelecionado);
			FacesUtil.addInfoMessage("Excluído com sucesso!");
		}catch (NegocioException e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Este profissional não pode ser excluído!");
		}	
	}
	public void pesquisar() {
		this.profissionaisFiltrados = this.profissionais.filtrados(this.profissionalFilter);
	}
}
