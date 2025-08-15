package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.EntradaProduto;
import br.gov.pr.guaira.animalys.entity.Fornecedor;
import br.gov.pr.guaira.animalys.entity.ItemEntrada;
import br.gov.pr.guaira.animalys.filter.EntradaProdutoFilter;
import br.gov.pr.guaira.animalys.repository.EntradasProdutos;
import br.gov.pr.guaira.animalys.repository.Fornecedores;
import br.gov.pr.guaira.animalys.repository.ItensEntrada;
import br.gov.pr.guaira.animalys.service.LoteService;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaEntradasBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public PesquisaEntradasBean() {
		this.entradaFilter = new EntradaProdutoFilter();
	}

	private List<EntradaProduto> entradasProtudos;
	private List<ItemEntrada> itensEntrada;
	private EntradaProduto entradaProdutoSelecionada;
	private EntradaProdutoFilter entradaFilter;
	
	@Inject
	private EntradasProdutos entradas;
	@Inject
	private Fornecedores fornecedores;
	@Inject
	private LoteService loteService;
	@Inject
	private ItensEntrada itens;
	
	
	public EntradaProduto getEntradaProdutoSelecionada() {
		return entradaProdutoSelecionada;
	}

	public void setEntradaProdutoSelecionada(EntradaProduto entradaProdutoSelecionada) {
		this.entradaProdutoSelecionada = entradaProdutoSelecionada;
	}

	public EntradaProdutoFilter getEntradaFilter() {
		return entradaFilter;
	}

	public void setEntradaFilter(EntradaProdutoFilter entradaFilter) {
		this.entradaFilter = entradaFilter;
	}

	public List<EntradaProduto> getEntradasProtudos() {
		return entradasProtudos;
	}

	public void pesquisar() {
		this.entradasProtudos = this.entradas.filtrados(entradaFilter);
	}
	
	public List<Fornecedor> completarFornecedor(String descricao) {
		return this.fornecedores.porNome(descricao);
	}
	
	private void excluir() {
	
		try {
			
			this.entradasProtudos.remove(this.entradaProdutoSelecionada);
			this.entradas.remover(this.entradaProdutoSelecionada);
			
			FacesUtil.addInfoMessage("Entrada n° " + this.entradaProdutoSelecionada.getIdEntradaProduto()
					+ " excluída com sucesso!");
			
		}catch (NegocioException e) {
			e.printStackTrace();
		}
	}
	
	public void voltaEstoque() {
		//se for diferente a entrada da quantidade do lote não deixar excluir
		
		try {
			
			
			itensEntrada = this.itens.porEntrada(this.entradaProdutoSelecionada);
			
			if(confereDivergencias()==false) {
				
				for(ItemEntrada ie : this.itensEntrada) {
					Integer qtd = 0;
					qtd = ie.getLote().getQuantidade() - ie.getQuantidadeAnterior();
					
					if(qtd.equals(ie.getQuantidade())) {					
						ie.getLote().setQuantidade(ie.getQuantidadeAnterior());
						this.loteService.salvar(ie.getLote());					
					}				
				}
				
				excluir();
				
			}else {
				FacesUtil.addErrorMessage("Um dos itens desta entrada já foi utilizado!");
			}
		}catch (NegocioException e) {
				e.printStackTrace();
		}

	}
	
	private boolean confereDivergencias(){
		
		boolean retorno = false;
		
		for(ItemEntrada ie : this.itensEntrada) {
			Integer qtd = 0;
			qtd = ie.getLote().getQuantidade() - ie.getQuantidadeAnterior();
			
			if(qtd.equals(ie.getQuantidade())) {								
				retorno = false;
			}else {
				retorno = true;
				break;
			}
			
		}
		
		return retorno;
	}
}
