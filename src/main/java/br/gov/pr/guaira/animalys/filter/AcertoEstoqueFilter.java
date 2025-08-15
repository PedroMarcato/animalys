package br.gov.pr.guaira.animalys.filter;

import java.util.Date;

import br.gov.pr.guaira.animalys.entity.TipoAcerto;

public class AcertoEstoqueFilter {

	private Date dataInicial;
	private Date dataFinal;
	private TipoAcerto tipoAcerto;
	
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
	public TipoAcerto getTipoAcerto() {
		return tipoAcerto;
	}
	public void setTipoAcerto(TipoAcerto tipoAcerto) {
		this.tipoAcerto = tipoAcerto;
	}
	
	
}
