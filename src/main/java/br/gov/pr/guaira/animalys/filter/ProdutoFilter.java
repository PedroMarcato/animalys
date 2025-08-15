package br.gov.pr.guaira.animalys.filter;

import java.io.Serializable;

import br.gov.pr.guaira.animalys.entity.Marca;
import br.gov.pr.guaira.animalys.entity.TipoProduto;

public class ProdutoFilter implements Serializable{

	private static final long serialVersionUID = 1L;

	private String nome;
	private Marca marca;
	private TipoProduto tipoProduto;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Marca getMarca() {
		return marca;
	}
	public void setMarca(Marca marca) {
		this.marca = marca;
	}
	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}
	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	
	
}
