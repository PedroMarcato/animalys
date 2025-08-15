package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.AcertoEstoque;
import br.gov.pr.guaira.animalys.entity.Lote;
import br.gov.pr.guaira.animalys.entity.Produto;
import br.gov.pr.guaira.animalys.entity.TipoAcerto;
import br.gov.pr.guaira.animalys.repository.Lotes;
import br.gov.pr.guaira.animalys.repository.Produtos;
import br.gov.pr.guaira.animalys.repository.TipoAcertos;
import br.gov.pr.guaira.animalys.service.AcertoEstoqueService;
import br.gov.pr.guaira.animalys.service.LoteService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class AcertoEstoqueBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public AcertoEstoqueBean() {
		this.acertoEstoque = new AcertoEstoque();
	}

	private AcertoEstoque acertoEstoque;
	private Produto produtoSelecionado;
	private Lote loteSelecionado;
	private Integer novaQuantidade;
	private Calendar dataAtual;
	private Date dataVencimento;
	private Date dataFabricacao;
	private List<Produto> produtosCadastrados;
	private List<Lote> lotesCarregados;
	private List<TipoAcerto> tiposAcertosCarregados;

	@Inject
	private AcertoEstoqueService acertoEstoqueService;
	@Inject
	private Produtos produtos;
	@Inject
	private Lotes lotes;
	@Inject
	private TipoAcertos tipos;
	@Inject
	private LoteService loteService;

	public void inicializar() {
		this.produtosCadastrados = this.produtos.produtosCadastrados();
		this.tiposAcertosCarregados = this.tipos.tiposCadastrados();
		this.dataAtual = Calendar.getInstance();
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public Lote getLoteSelecionado() {
		return loteSelecionado;
	}

	public void setLoteSelecionado(Lote loteSelecionado) {
		this.loteSelecionado = loteSelecionado;
	}

	public Produtos getProdutos() {
		return produtos;
	}

	public List<Produto> getProdutosCadastrados() {
		return produtosCadastrados;
	}

	public List<Lote> getLotesCarregados() {
		return lotesCarregados;
	}

	public AcertoEstoque getAcertoEstoque() {
		return acertoEstoque;
	}

	public void setAcertoEstoque(AcertoEstoque acertoEstoque) {
		this.acertoEstoque = acertoEstoque;
	}

	public List<TipoAcerto> getTiposAcertosCarregados() {
		return tiposAcertosCarregados;
	}

	public Integer getNovaQuantidade() {
		return novaQuantidade;
	}

	public void setNovaQuantidade(Integer novaQuantidade) {
		this.novaQuantidade = novaQuantidade;
	}
	
	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataFabricacao() {
		return dataFabricacao;
	}

	public void setDataFabricacao(Date dataFabricacao) {
		this.dataFabricacao = dataFabricacao;
	}

	public void carregarLotes() {
		this.lotesCarregados = this.lotes.porProdutoAcerto(this.produtoSelecionado);
	}

	public void salvar() {

		this.acertoEstoque.setQtdAnterior(this.loteSelecionado.getQuantidade());
		this.acertoEstoque.setQtdNova(this.novaQuantidade);
		this.acertoEstoque.setData(this.dataAtual);
		this.acertoEstoque.setLote(this.loteSelecionado);
		atualizarEstoque();
		this.acertoEstoqueService.salvar(this.acertoEstoque);
		FacesUtil.addInfoMessage("Acerto de estoque realizado com sucesso!");
		limpar();
	}
	
	public void salvarLote() {		
		this.loteSelecionado = this.loteService.salvar(this.loteSelecionado);
		FacesUtil.addInfoMessage("Lote salvo com sucesso!");
	}
	
	public void novoLote() {
		this.loteSelecionado = new Lote();
	}

	public void limpar() {
		this.acertoEstoque = new AcertoEstoque();
		this.produtoSelecionado = null;
		this.novaQuantidade = null;
		this.loteSelecionado = null;
	}

	private void atualizarEstoque() {

		if (this.acertoEstoque.getTipoAcerto().getOperacao().equals("+")) {
			this.loteSelecionado.setQuantidade(this.loteSelecionado.getQuantidade() + this.novaQuantidade);
			this.loteService.salvar(this.loteSelecionado);
		} else {
			this.loteSelecionado.setQuantidade(this.loteSelecionado.getQuantidade() - this.novaQuantidade);
			this.loteService.salvar(this.loteSelecionado);
		}

	}

}
