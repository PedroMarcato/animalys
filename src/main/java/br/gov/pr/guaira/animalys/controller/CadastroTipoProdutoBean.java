package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.TipoProduto;
import br.gov.pr.guaira.animalys.service.TipoProdutoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroTipoProdutoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public CadastroTipoProdutoBean() {
		this.tipoProduto = new TipoProduto();
	}

	private TipoProduto tipoProduto;
	@Inject
	private TipoProdutoService tipoProdutoService;

	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}
	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	
	public void salvar() {
		this.tipoProdutoService.salvar(this.tipoProduto);
		FacesUtil.addInfoMessage("Produto cadastrado com sucesso!");
		
		limpar();
	}
	
	private void limpar() {
		this.tipoProduto = new TipoProduto();
	}
	
	public boolean isEditando() {
		return this.tipoProduto.getIdTipoProduto() != null;
	}
}
