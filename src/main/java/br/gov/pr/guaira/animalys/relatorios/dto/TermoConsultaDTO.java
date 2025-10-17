package br.gov.pr.guaira.animalys.relatorios.dto;

import java.util.Date;

public class TermoConsultaDTO {

    // Dados do proprietário
    private String nomeProprietario;
    private String rgProprietario;
    private String cpfProprietario;
    private String enderecoProprietario;
    private String bairroProprietario;
    private String cepProprietario;
    private String cidadeProprietario;
    private String contatoProprietario;
    private String celularProprietario;

    // Dados do animal
    private String nomeAnimal;
    private String fichaControle;
    private String cor;
    private String especie;
    private String sexo;
    private String porteAnimal;
    private String idade;
    private String castradoStr;

    // Dados do termo
    private Date dataAssinaturaAsDate;

    // getters / setters
    public String getNomeProprietario() {
        return nomeProprietario;
    }

    public void setNomeProprietario(String nomeProprietario) {
        this.nomeProprietario = nomeProprietario;
    }

    public String getRgProprietario() {
        return rgProprietario;
    }

    public void setRgProprietario(String rgProprietario) {
        this.rgProprietario = rgProprietario;
    }

    public String getCpfProprietario() {
        return cpfProprietario;
    }

    public void setCpfProprietario(String cpfProprietario) {
        this.cpfProprietario = cpfProprietario;
    }

    public String getEnderecoProprietario() {
        return enderecoProprietario;
    }

    public void setEnderecoProprietario(String enderecoProprietario) {
        this.enderecoProprietario = enderecoProprietario;
    }

    public String getBairroProprietario() {
        return bairroProprietario;
    }

    public void setBairroProprietario(String bairroProprietario) {
        this.bairroProprietario = bairroProprietario;
    }

    public String getCepProprietario() {
        return cepProprietario;
    }

    public void setCepProprietario(String cepProprietario) {
        this.cepProprietario = cepProprietario;
    }

    public String getCidadeProprietario() {
        return cidadeProprietario;
    }

    public void setCidadeProprietario(String cidadeProprietario) {
        this.cidadeProprietario = cidadeProprietario;
    }

    public String getContatoProprietario() {
        return contatoProprietario;
    }

    public void setContatoProprietario(String contatoProprietario) {
        this.contatoProprietario = contatoProprietario;
    }

    public String getCelularProprietario() {
        return celularProprietario;
    }

    public void setCelularProprietario(String celularProprietario) {
        this.celularProprietario = celularProprietario;
    }

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public String getFichaControle() {
        return fichaControle;
    }

    public void setFichaControle(String fichaControle) {
        this.fichaControle = fichaControle;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getPorteAnimal() {
        return porteAnimal;
    }

    public void setPorteAnimal(String porteAnimal) {
        this.porteAnimal = porteAnimal;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getCastradoStr() {
        return castradoStr;
    }

    public void setCastradoStr(String castradoStr) {
        this.castradoStr = castradoStr;
    }

    public Date getDataAssinaturaAsDate() {
        return dataAssinaturaAsDate;
    }

    public void setDataAssinaturaAsDate(Date dataAssinaturaAsDate) {
        this.dataAssinaturaAsDate = dataAssinaturaAsDate;
    }
}
