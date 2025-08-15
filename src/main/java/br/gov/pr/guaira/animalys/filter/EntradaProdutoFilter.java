package br.gov.pr.guaira.animalys.filter;

import java.io.Serializable;
import java.util.Date;

import br.gov.pr.guaira.animalys.entity.Fornecedor;
import br.gov.pr.guaira.animalys.entity.Lote;

public class EntradaProdutoFilter implements Serializable{

	private static final long serialVersionUID = 2020602285907469480L;

	private Fornecedor fornecedor;
	private Lote lote;
	private Date dataInicial;
	private Date dataFinal;

	
	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Lote getLote() {
		return lote;
	}

	public void setLote(Lote lote) {
		this.lote = lote;
	}	
}
