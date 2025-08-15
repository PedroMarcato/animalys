package br.gov.pr.guaira.animalys.filter;

import java.io.Serializable;

public class FornecedorFilter implements Serializable{


	private static final long serialVersionUID = 1L;

	private String nomeFantasia;
	private String razaoSocial;
	
	private String cnpj;

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
		
}
