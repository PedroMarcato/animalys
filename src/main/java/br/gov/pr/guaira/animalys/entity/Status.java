package br.gov.pr.guaira.animalys.entity;

public enum Status {
    CONSULTA_ELETIVA_AGENDADA("Consulta Eletiva Agendada"),
    AGENDADOCASTRACAO("Agendado para castração"),
    CASTRADO("Castrado"),
    CONSULTA_ELETIVA_REALIZADA("Consulta Eletiva Realizada"),
    SOLICITADO("Solicitado"),
    CANCELADO("Cancelado"),
    FINALIZADO("Finalizado"),
    CONSULTAFINALIZADA("Consulta Finalizada"); // novo status adicionado

    private Status(String descricao) {
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

