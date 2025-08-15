package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.TipoProduto;
import br.gov.pr.guaira.animalys.filter.TipoProdutoFilter;
import br.gov.pr.guaira.animalys.repository.TiposProdutos;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaTipoProdutoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public PesquisaTipoProdutoBean() {
		this.tipoProdutoFilter = new TipoProdutoFilter();
	}

	private TipoProdutoFilter tipoProdutoFilter;
	private TipoProduto tipoProdutoSelecionado;
	private List<TipoProduto> tiposProdutosFiltrados;
	@Inject
	private TiposProdutos tiposProdutos;

	public TipoProdutoFilter getTipoProdutoFilter() {
		return tipoProdutoFilter;
	}
	public void setTipoProdutoFilter(TipoProdutoFilter tipoProdutoFilter) {
		this.tipoProdutoFilter = tipoProdutoFilter;
	}
	public List<TipoProduto> getTiposProdutosFiltrados() {
		return tiposProdutosFiltrados;
	}
	public TipoProduto getTipoProdutoSelecionado() {
		return tipoProdutoSelecionado;
	}
	public void setTipoProdutoSelecionado(TipoProduto tipoProdutoSelecionado) {
		this.tipoProdutoSelecionado = tipoProdutoSelecionado;
	}
	public void pesquisar() {
		this.tiposProdutosFiltrados = this.tiposProdutos.filtrados(this.tipoProdutoFilter);
	}
	public void excluir() {
		try {
			this.tiposProdutos.remover(this.tipoProdutoSelecionado);
			this.tiposProdutosFiltrados.remove(this.tipoProdutoSelecionado);
			FacesUtil.addInfoMessage("Tipo Produto excluído com sucesso!");
		}catch (NegocioException e) {
				e.printStackTrace();
				FacesUtil.addErrorMessage("Este tipo de produto não pode ser excluído!");
		}
	}
	
}
