package br.gov.pr.guaira.animalys.dto;

import java.util.Calendar;
import java.util.Date;

import br.gov.pr.guaira.animalys.entity.Status;

public class ConsultaRealizada {

    private int idAnimal;
    private String nomeAnimal;
    private String nomeProprietario;
    private Calendar dataConsulta;
    private String enderecoProprietario;
    private String contatoProprietario;
    private Status status;

    public ConsultaRealizada(int idAnimal, String nomeAnimal, String nomeProprietario, Calendar dataConsulta, String enderecoProprietario, String contatoProprietario, Status status) {
        this.idAnimal = idAnimal;
        this.nomeAnimal = nomeAnimal;
        this.nomeProprietario = nomeProprietario;
        this.dataConsulta = dataConsulta;
        this.enderecoProprietario = enderecoProprietario;
        this.contatoProprietario = (contatoProprietario != null) ? contatoProprietario : "Sem Registro";
        this.status = status;
    }

    public int getIdAnimal() {
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

    public Calendar getDataConsulta() {
        return dataConsulta;
    }

    public Date getDataConsultaDate() {
        return dataConsulta != null ? dataConsulta.getTime() : null;
    }


    public void setDataConsulta(Calendar dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
