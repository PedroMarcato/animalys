package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Procedimento;
import br.gov.pr.guaira.animalys.filter.ProcedimentoFilter;
import br.gov.pr.guaira.animalys.repository.Procedimentos;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaProcedimentoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public PesquisaProcedimentoBean() {
		this.procedimentoFilter = new ProcedimentoFilter();
	}

	private ProcedimentoFilter procedimentoFilter;
	private Procedimento procedimentoSelecionado;
	private List<Procedimento> procedimentosFiltrados;
	@Inject
	private Procedimentos procedimentos;

	public ProcedimentoFilter getProcedimentoFilter() {
		return procedimentoFilter;
	}
	public void setProcedimentoFilter(ProcedimentoFilter procedimentoFilter) {
		this.procedimentoFilter = procedimentoFilter;
	}
	public List<Procedimento> getProcedimentosFiltrados() {
		return procedimentosFiltrados;
	}
	public void setProcedimentosFiltrados(List<Procedimento> procedimentosFiltrados) {
		this.procedimentosFiltrados = procedimentosFiltrados;
	}
	public Procedimento getProcedimentoSelecionado() {
		return procedimentoSelecionado;
	}
	public void setProcedimentoSelecionado(Procedimento procedimentoSelecionado) {
		this.procedimentoSelecionado = procedimentoSelecionado;
	}
	public void pesquisar() {
		this.procedimentosFiltrados = this.procedimentos.filtrados(this.procedimentoFilter);
	}
	public void excluir() {
		this.procedimentos.remover(this.procedimentoSelecionado);
		this.procedimentosFiltrados.remove(this.procedimentoSelecionado);
		FacesUtil.addInfoMessage("Procedimento excluído com sucesso!");
	}
}
