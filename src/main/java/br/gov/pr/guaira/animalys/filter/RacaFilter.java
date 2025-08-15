package br.gov.pr.guaira.animalys.filter;

import java.io.Serializable;

import br.gov.pr.guaira.animalys.entity.Especie;

public class RacaFilter implements Serializable{

	private static final long serialVersionUID = 1L;

	private String nome;
	
	private Especie especie;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Especie getEspecie() {
		return especie;
	}

	public void setEspecie(Especie especie) {
		this.especie = especie;
	}
	
	
}
