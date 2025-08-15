package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Marca;
import br.gov.pr.guaira.animalys.entity.Produto;
import br.gov.pr.guaira.animalys.entity.TipoApresentacao;
import br.gov.pr.guaira.animalys.entity.TipoProduto;
import br.gov.pr.guaira.animalys.repository.Marcas;
import br.gov.pr.guaira.animalys.repository.TiposProdutos;
import br.gov.pr.guaira.animalys.service.MarcaService;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.service.ProdutoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroProdutoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public CadastroProdutoBean() {
		this.produto = new Produto();
		this.novaMarca = new Marca();
	}

	public void inicializar() {
		this.tiposProdutosCadastrados = this.tiposProdutos.tiposCadastrados();
		this.marcasCadastradas = this.marcas.marcasCadastradas();
	}

	private Produto produto;
	private Marca novaMarca;
	private List<Marca> marcasCadastradas;
	private List<TipoProduto> tiposProdutosCadastrados;
	@Inject
	private ProdutoService produtoService;
	@Inject
	private Marcas marcas;
	@Inject
	private TiposProdutos tiposProdutos;
	@Inject
	private MarcaService marcaService;

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<Marca> getMarcasCadastradas() {
		return marcasCadastradas;
	}

	public List<TipoProduto> getTiposProdutosCadastrados() {
		return tiposProdutosCadastrados;
	}

	public TipoApresentacao[] getTiposApresentacao() {
		return TipoApresentacao.values();
	}

	public Marca getNovaMarca() {
		return novaMarca;
	}

	public void setNovaMarca(Marca novaMarca) {
		this.novaMarca = novaMarca;
	}
	
	public List<Marca> completarMarca(String descricao) {
		return this.marcas.porNome(descricao);
	}

	public void salvar() {
		this.produtoService.salvar(this.produto);
		FacesUtil.addInfoMessage("Produto cadastrado com sucesso!");
		limpar();
	}

	private void limpar() {
		this.produto = new Produto();
	}
	
	private void limparMarca() {
		this.novaMarca = new Marca();
	}

	public boolean isEditando() {
		return this.produto.getIdProduto() != null;
	}

	public void salvarMarca() {
		try {
			this.marcaService.salvar(this.novaMarca);
			//this.produto.setMarca(this.novaMarca);
			FacesUtil.addInfoMessage("Marca cadastrada com sucesso!");
			limparMarca();
		} catch (NegocioException e) {
			e.printStackTrace();
		}

	}
}
