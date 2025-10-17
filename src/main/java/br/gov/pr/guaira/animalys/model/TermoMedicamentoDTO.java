package br.gov.pr.guaira.animalys.model;

import java.io.Serializable;

public class TermoMedicamentoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Dados do proprietário
	private String nomeProprietario;
	private String rgProprietario;
	private String cpfProprietario;
	private String enderecoProprietario;
	private String bairroProprietario;
	private String cepProprietario;
	private String cidadeProprietario;
	private String contatoProprietario;
	private String telefoneProprietario;
	private String celularProprietario;

	// Dados do animal
	private String nomeAnimal;
	private String especie;
	private String sexo;
	private String numeroMicrochip;
	private String cor; // pelagem
	private String porteAnimal;
	private String idade;
	private String castradoStr;

	// Dados do medicamento/retirada
	private String nomeMedicamento; // medicamento recebido
	private String data1Mes; // data/retirada
	private Integer quantidade1Mes; // quantidade unitária
	private String observacoes;

	// Construtor vazio
	public TermoMedicamentoDTO() {
	}

	// Getters e Setters
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

	public String getTelefoneProprietario() {
		return telefoneProprietario;
	}

	public void setTelefoneProprietario(String telefoneProprietario) {
		this.telefoneProprietario = telefoneProprietario;
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

	public String getNumeroMicrochip() {
		return numeroMicrochip;
	}

	public void setNumeroMicrochip(String numeroMicrochip) {
		this.numeroMicrochip = numeroMicrochip;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
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

	public String getNomeMedicamento() {
		return nomeMedicamento;
	}

	public void setNomeMedicamento(String nomeMedicamento) {
		this.nomeMedicamento = nomeMedicamento;
	}

	public String getData1Mes() {
		return data1Mes;
	}

	public void setData1Mes(String data1Mes) {
		this.data1Mes = data1Mes;
	}

	public Integer getQuantidade1Mes() {
		return quantidade1Mes;
	}

	public void setQuantidade1Mes(Integer quantidade1Mes) {
		this.quantidade1Mes = quantidade1Mes;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
}
