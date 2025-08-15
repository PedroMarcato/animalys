package br.gov.pr.guaira.animalys.filter;

import java.io.Serializable;

public class EstadoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String uf;
	private String nome;

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
