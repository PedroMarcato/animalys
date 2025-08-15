package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Marca;
import br.gov.pr.guaira.animalys.entity.Produto;
import br.gov.pr.guaira.animalys.entity.TipoProduto;
import br.gov.pr.guaira.animalys.filter.ProdutoFilter;
import br.gov.pr.guaira.animalys.repository.Marcas;
import br.gov.pr.guaira.animalys.repository.Produtos;
import br.gov.pr.guaira.animalys.repository.TiposProdutos;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaProdutoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public PesquisaProdutoBean() {
		this.produtoFilter = new ProdutoFilter();
	}
	
	public void inicializar() {
		this.marcasCadastradas = this.marcas.marcasCadastradas();
		this.tiposProdutosCadastrados = this.tiposProdutos.tiposCadastrados();
	}

	private ProdutoFilter produtoFilter;
	private Produto produtoSelecionado;
	private List<Produto> produtosFiltrados;
	private List<Marca> marcasCadastradas;
	private List<TipoProduto> tiposProdutosCadastrados;
	
	@Inject
	private Produtos produtos;
	@Inject
	private Marcas marcas;
	@Inject
	private TiposProdutos tiposProdutos;

	public ProdutoFilter getProdutoFilter() {
		return produtoFilter;
	}

	public void setProdutoFilter(ProdutoFilter produtoFilter) {
		this.produtoFilter = produtoFilter;
	}
	
	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public List<Produto> getProdutosFiltrados() {
		return produtosFiltrados;
	}

	public List<Marca> getMarcasCadastradas() {
		return marcasCadastradas;
	}

	public List<TipoProduto> getTiposProdutosCadastrados() {
		return tiposProdutosCadastrados;
	}
	
	public void pesquisar() {
		this.produtosFiltrados = this.produtos.filtrados(this.produtoFilter);
	}
	public void excluir() {
		try {
			this.produtos.remover(this.produtoSelecionado);
			this.produtosFiltrados.remove(this.produtoSelecionado);
			FacesUtil.addInfoMessage("Produto excluído com sucesso!");
		}catch (NegocioException e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Este produto não pode ser excluído!");
		}
		
	}
}
