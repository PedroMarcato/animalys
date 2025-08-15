package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Fornecedor;
import br.gov.pr.guaira.animalys.filter.FornecedorFilter;
import br.gov.pr.guaira.animalys.repository.Fornecedores;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaFornecedorBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public PesquisaFornecedorBean() {
		this.fornecedorFilter = new FornecedorFilter();
	}

	private Fornecedor fornecedorSelecionado;
	private FornecedorFilter fornecedorFilter;
	private List<Fornecedor> fornecedoresFiltrados;
	@Inject
	private Fornecedores fornecedores;
		
	public Fornecedor getFornecedorSelecionado() {
		return fornecedorSelecionado;
	}
	public void setFornecedorSelecionado(Fornecedor fornecedorSelecionado) {
		this.fornecedorSelecionado = fornecedorSelecionado;
	}
	public FornecedorFilter getFornecedorFilter() {
		return fornecedorFilter;
	}
	public void setFornecedorFilter(FornecedorFilter fornecedorFilter) {
		this.fornecedorFilter = fornecedorFilter;
	}
	public List<Fornecedor> getFornecedoresFiltrados() {
		return fornecedoresFiltrados;
	}
	public void pesquisar() {
		this.fornecedoresFiltrados = this.fornecedores.filtrados(this.fornecedorFilter);
	}
	public void excluir() {
		this.fornecedores.remover(this.fornecedorSelecionado);
		this.fornecedoresFiltrados.remove(this.fornecedorSelecionado);
		FacesUtil.addInfoMessage("Fornecedor excluído com sucesso!");
	}
}

