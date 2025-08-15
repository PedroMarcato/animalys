package br.gov.pr.guaira.animalys.filter;

import java.io.Serializable;
import java.util.Calendar;

import br.gov.pr.guaira.animalys.entity.Fornecedor;

public class LoteFilter implements Serializable{

	private static final long serialVersionUID = 1L;

	private Fornecedor fornecedor;
	private String numero;
	private Calendar dataValidadeInicial;
	private Calendar dataValidadeFinal;
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Calendar getDataValidadeInicial() {
		return dataValidadeInicial;
	}
	public void setDataValidadeInicial(Calendar dataValidadeInicial) {
		this.dataValidadeInicial = dataValidadeInicial;
	}
	public Calendar getDataValidadeFinal() {
		return dataValidadeFinal;
	}
	public void setDataValidadeFinal(Calendar dataValidadeFinal) {
		this.dataValidadeFinal = dataValidadeFinal;
	}
	
	
}
