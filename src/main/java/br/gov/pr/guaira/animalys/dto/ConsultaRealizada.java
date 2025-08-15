package br.gov.pr.guaira.animalys.dto;

import java.util.Date;

public class ConsultaRealizada {

    private Integer idAnimal;
    private String nomeAnimal;
    private String nomeProprietario;
    private String enderecoProprietario;
    private String contatoProprietario;
    private Date dataConsulta;

    public ConsultaRealizada(Integer idAnimal, String nomeAnimal, String nomeProprietario, String enderecoProprietario, String contatoProprietario, Date dataConsulta) {
        this.idAnimal = idAnimal;
        this.nomeAnimal = nomeAnimal;
        this.nomeProprietario = nomeProprietario;
        this.enderecoProprietario = enderecoProprietario;
        this.contatoProprietario = contatoProprietario;
        this.dataConsulta = dataConsulta;
    }

    public Integer getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(Integer idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public String getNomeProprietario() {
        return nomeProprietario;
    }

    public void setNomeProprietario(String nomeProprietario) {
        this.nomeProprietario = nomeProprietario;
    }

    public String getEnderecoProprietario() {
        return enderecoProprietario;
    }

    public void setEnderecoProprietario(String enderecoProprietario) {
        this.enderecoProprietario = enderecoProprietario;
    }

    public String getContatoProprietario() {
        return contatoProprietario;
    }

    public void setContatoProprietario(String contatoProprietario) {
        this.contatoProprietario = contatoProprietario;
    }

    public Date getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(Date dataConsulta) {
        this.dataConsulta = dataConsulta;
    }
}
