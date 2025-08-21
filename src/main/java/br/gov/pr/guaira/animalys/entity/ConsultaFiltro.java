package br.gov.pr.guaira.animalys.entity;

import java.util.Calendar; // 1. Mude o import de java.util.Date para java.util.Calendar

public class ConsultaFiltro {

    private String nomeAnimal;
    private String nomeProprietario;
    private String status;
    private Calendar dataInicio; // 2. Mude o tipo do atributo
    private Calendar dataFim;    // 3. Mude o tipo do atributo

    // 4. Ajuste os getters e setters para usarem Calendar
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

    // O resto dos getters e setters permanecem os mesmos
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
