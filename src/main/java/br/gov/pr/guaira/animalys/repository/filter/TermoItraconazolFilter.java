package br.gov.pr.guaira.animalys.repository.filter;

import java.io.Serializable;
import java.util.Calendar;

public class TermoItraconazolFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nomeAnimal;
    private String nomeProprietario;
    private String cpfProprietario;
    private Calendar dataInicio;
    private Calendar dataFim;

    public TermoItraconazolFilter() {
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

    public String getCpfProprietario() {
        return cpfProprietario;
    }

    public void setCpfProprietario(String cpfProprietario) {
        this.cpfProprietario = cpfProprietario;
    }

    public Calendar getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Calendar dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Calendar getDataFim() {
        return dataFim;
    }

    public void setDataFim(Calendar dataFim) {
        this.dataFim = dataFim;
    }
}