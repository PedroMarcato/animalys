package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.EntradaProduto;
import br.gov.pr.guaira.animalys.entity.Fornecedor;
import br.gov.pr.guaira.animalys.entity.ItemEntrada;
import br.gov.pr.guaira.animalys.entity.Lote;
import br.gov.pr.guaira.animalys.entity.Produto;
import br.gov.pr.guaira.animalys.repository.Fornecedores;
import br.gov.pr.guaira.animalys.repository.ItensEntrada;
import br.gov.pr.guaira.animalys.repository.Lotes;
import br.gov.pr.guaira.animalys.repository.Produtos;
import br.gov.pr.guaira.animalys.service.EntradaProdutoService;
import br.gov.pr.guaira.animalys.service.LoteService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class EntradaProdutoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public EntradaProdutoBean() {
		this.entradaProduto = new EntradaProduto();
		this.itemEntrada = new ItemEntrada();
		this.itensAdicionados = new ArrayList<>();
		this.habilitaFornecedor = false;
		this.readOnly = true;
		
		}
	
	public void inicializar() {
		this.fornecedoresCadastrados = this.fornecedores.fornecedoresCadastrados();
		this.produtosCadastrados = this.produtos.produtosCadastrados();
		this.dataAtual = Calendar.getInstance();
		
		if(isEditando()){
			this.itensAdicionados = this.itens.porEntrada(this.entradaProduto);
			this.atualizaValorTotal();
		}
		
	}

	private EntradaProduto entradaProduto;
	private ItemEntrada itemEntrada;
	private Lote novoLote;
	private Lote lote;
	private Integer quantidade;
	private Date dataFabricacao;
	private Date dataValidade;
	private Calendar dataAtual;
	private boolean habilitaFornecedor;
	private boolean readOnly;
	private Double valorTotal;
	private List<Fornecedor> fornecedoresCadastrados;
	private List<Produto> produtosCadastrados;
	private List<Lote> lotesCadastrados;
	private List<ItemEntrada> itensAdicionados;
	@Inject
	private EntradaProdutoService entradaProdutoService;
	@Inject
	private Fornecedores fornecedores;
	@Inject
	private Produtos produtos;
	@Inject
	private Lotes lotes;
	@Inject
	private LoteService loteService;
	@Inject
	private ItensEntrada itens;

	public EntradaProduto getEntradaProduto() {
		return entradaProduto;
	}

	public void setEntradaProduto(EntradaProduto entradaProduto) {
		this.entradaProduto = entradaProduto;
	}

	public ItemEntrada getItemEntrada() {
		return itemEntrada;
	}

	public void setItemEntrada(ItemEntrada itemEntrada) {
		this.itemEntrada = itemEntrada;
	}

	public List<Fornecedor> getFornecedoresCadastrados() {
		return fornecedoresCadastrados;
	}

	public List<Produto> getProdutosCadastrados() {
		return produtosCadastrados;
	}

	public List<Lote> getLotesCadastrados() {
		return lotesCadastrados;
	}

	public List<ItemEntrada> getItensAdicionados() {
		return itensAdicionados;
	}

	public Date getDataFabricacao() {
		return dataFabricacao;
	}

	public void setDataFabricacao(Date dataFabricacao) {
		this.dataFabricacao = dataFabricacao;
	}

	public Date getDataValidade() {
		return dataValidade;
	}

	public void setDataValidade(Date dataValidade) {
		this.dataValidade = dataValidade;
	}

	public Lote getNovoLote() {
		return novoLote;
	}

	public void setNovoLote(Lote novoLote) {
		this.novoLote = novoLote;
	}
	
	public Lote getLote() {
		return lote;
	}

	public void setLote(Lote lote) {
		this.lote = lote;
	}
	
	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public List<Produto> completarProduto(String nome) {
		return this.produtos.porNome(nome);
	}

	public List<Fornecedor> completarFornecedor(String nome) {
		return this.fornecedores.porNome(nome);
	}

	public List<Lote> completarLote(String nome) {
		return this.lotes.porNumero(nome, this.itemEntrada.getProduto());
	}
	public boolean isHabilitaFornecedor() {
		return habilitaFornecedor;
	}

	public void setHabilitaFornecedor(boolean habilitaFornecedor) {
		this.habilitaFornecedor = habilitaFornecedor;
	}

	public void removerItem() {
		
		if(this.itemEntrada.getIdItemEntrada() != null) {
			this.itens.remover(this.itemEntrada);
			this.itensAdicionados.remove(this.itemEntrada);
			this.atualizaValorTotal();
			this.atualizaEstoqueNoRemover();
			this.limparAposAdicionar();
		}else {
			
			this.itensAdicionados.remove(this.itemEntrada);
			this.atualizaValorTotal();
			this.limparAposAdicionar();
		}

	}
	
	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	private void limparAposAdicionar() {
		this.itemEntrada = null;
		this.itemEntrada = new ItemEntrada();
		this.lote = new Lote();
		this.habilitaFornecedor = false;
	}

	public void adicionarItem() {
		
		if(this.itemEntrada.getQuantidade()!= null && this.itemEntrada.getQuantidade() > 0) {
			
			if (!this.itensAdicionados.contains(this.itemEntrada)) {
				this.itemEntrada.setEntradaProduto(this.entradaProduto);
				this.itemEntrada.setLote(this.lote);
				this.itemEntrada.setQuantidadeAnterior(this.lote.getQuantidade());
				this.itemEntrada.setSubTotal(this.itemEntrada.getQuantidade() * this.lote.getPreco());
				this.itensAdicionados.add(this.itemEntrada);
				this.habilitaFornecedor = true;
				atualizaValorTotal();
				limparAposAdicionar();
			} else {
				FacesUtil.addErrorMessage("Este item já foi adicionado!");
			}
		}else {
				FacesUtil.addErrorMessage("Verifique se todos os campos estão preenchidos corretamente!");
		}	
	}

	public void novoLote() {
		this.novoLote = new Lote();	
	}
	
	public void carregaLotes() {
		this.lotesCadastrados = this.lotes.porProdutoEntrada(this.itemEntrada.getProduto());
	}
	
	public void carregaDadosLote() {
		this.lote = this.itemEntrada.getLote();
	}
	
	public void salvarLote() {
		
		if(this.itemEntrada.getProduto() != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(dataFabricacao);
			this.novoLote.setFabricacao(c);
			
			Calendar d = Calendar.getInstance();
			d.setTime(dataValidade);
			d.set(Calendar.HOUR, 23);
			d.set(Calendar.MINUTE, 59);
			d.set(Calendar.SECOND, 59);
			this.novoLote.setValidade(d);
			
			this.novoLote.setProduto(this.itemEntrada.getProduto());
			this.novoLote.setQuantidade(0);
			this.lote = this.loteService.salvar(this.novoLote);
			this.itemEntrada.setLote(this.lote);
			novoLote();
		}else {
			FacesUtil.addErrorMessage("Informe um produto antes de criar um novo lote!");
		}
		
		
	}
	
	public void salvar() {
		
		if(!this.itensAdicionados.isEmpty()) {
			atualizaValorTotal();
			this.entradaProduto.setData(this.dataAtual);
			this.entradaProduto.setItens(this.itensAdicionados);
			this.entradaProdutoService.salvar(this.entradaProduto);
			atualizaEstoque();
			limpar();
			FacesUtil.addInfoMessage("Entrada de produtos cadastrada com sucesso!");
		}else {
			FacesUtil.addErrorMessage("Informe ao menos um item!");
		}	
	}

	public void limpar() {
		this.itensAdicionados.clear();
		this.itensAdicionados = new ArrayList<>();
		this.entradaProduto = new EntradaProduto();
	}
	
	private void atualizaEstoque() {
		
		for(ItemEntrada ie : this.itensAdicionados) {
			
			Lote lot = this.lotes.porId(ie.getLote().getIdLote());
			
			lot.setQuantidade(lot.getQuantidade() + ie.getQuantidade());
			
			this.loteService.salvar(lot);
		}	
	}
	
	private void atualizaValorTotal() {
		this.valorTotal = 0.0;
		
		for(ItemEntrada ie : this.itensAdicionados) {
			valorTotal += ie.getSubTotal();
		}
		this.entradaProduto.setValorTotal(valorTotal);
	}
	
	private void atualizaEstoqueNoRemover() {
		Lote lot = this.lotes.porId(this.itemEntrada.getIdItemEntrada());
		
		lot.setQuantidade(lot.getQuantidade() - itemEntrada.getQuantidade());
		
		this.loteService.salvar(lot);
	}
	
	private boolean isEditando() {
		return this.entradaProduto.getIdEntradaProduto() != null;
	}
	
}
