package br.gov.pr.guaira.animalys.filter;

import java.io.Serializable;

public class TipoProdutoFilter implements Serializable{

	private static final long serialVersionUID = 1L;

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
