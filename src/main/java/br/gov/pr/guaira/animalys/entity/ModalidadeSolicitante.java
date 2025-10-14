package br.gov.pr.guaira.animalys.entity;

public enum ModalidadeSolicitante {

	PESSOA_FISICA("Pessoa Física"),
	PROTETOR_INDIVIUAL_ANIMAIS("Protetor Individual de Animais"),
	CAO_COMUNITARIO("Cão comunitário"),
	TUTOR_VULNERABILIDADE_SOCIAL("Tutor em situação de vulnerabilidade social");
	
	private ModalidadeSolicitante(String descricao) {
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
