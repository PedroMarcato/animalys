package br.gov.pr.guaira.animalys.entity;

public enum Sexo {

	MACHO("Macho"), FEMEA("Fêmea");
	
	private Sexo(String descricao) {
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
