package br.gov.pr.guaira.animalys.entity;

public enum TipoApresentacao {

	AMP("AMPOLA"), CPR("COMPRIMIDO"),  CPS("C�PSULA"), CX("CAIXA"), FRASCO("FRASCO"), ML("MILILITRO"), MG("MILIGRAMA"), M3("METRO C�BICO");
	
	private TipoApresentacao(String descricao) {
		this.descricao = descricao;
	}
	
	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
