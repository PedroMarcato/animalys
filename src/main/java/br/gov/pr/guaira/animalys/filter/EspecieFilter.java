package br.gov.pr.guaira.animalys.filter;

import java.io.Serializable;

public class EspecieFilter implements Serializable{

	private static final long serialVersionUID = 1L;
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}
