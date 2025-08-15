package br.gov.pr.guaira.animalys.entity;

public enum TipoApresentacao {

	AMP("AMPOLA"), CPR("COMPRIMIDO"),  CPS("CÁPSULA"), CX("CAIXA"), FRASCO("FRASCO"), ML("MILILITRO"), MG("MILIGRAMA"), M3("METRO CÚBICO");
	
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
