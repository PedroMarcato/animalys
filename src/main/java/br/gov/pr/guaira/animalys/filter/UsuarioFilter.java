package br.gov.pr.guaira.animalys.filter;

import java.io.Serializable;

public class UsuarioFilter implements Serializable{

	private static final long serialVersionUID = 1L;

	private String nome;
	
	private String nomeUsuario;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	
}
