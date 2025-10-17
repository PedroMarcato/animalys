package br.gov.pr.guaira.animalys.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class TermoRacaoDTO implements Serializable {

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

	// Dados das 12 retiradas mensais - DATAS
	private String data1Mes;
	private String data2Mes;
	private String data3Mes;
	private String data4Mes;
	private String data5Mes;
	private String data6Mes;
	private String data7Mes;
	private String data8Mes;
	private String data9Mes;
	private String data10Mes;
	private String data11Mes;
	private String data12Mes;

	// Dados das 12 retiradas mensais - QUANTIDADES (Kg)
	private BigDecimal quantidade1Mes;
	private BigDecimal quantidade2Mes;
	private BigDecimal quantidade3Mes;
	private BigDecimal quantidade4Mes;
	private BigDecimal quantidade5Mes;
	private BigDecimal quantidade6Mes;
	private BigDecimal quantidade7Mes;
	private BigDecimal quantidade8Mes;
	private BigDecimal quantidade9Mes;
	private BigDecimal quantidade10Mes;
	private BigDecimal quantidade11Mes;
	private BigDecimal quantidade12Mes;

	private String observacoes;
	private String mesReferenciaFormatado;

	// Construtor vazio
	public TermoRacaoDTO() {
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

	public String getData1Mes() {
		return data1Mes;
	}

	public void setData1Mes(String data1Mes) {
		this.data1Mes = data1Mes;
	}

	public String getData2Mes() {
		return data2Mes;
	}

	public void setData2Mes(String data2Mes) {
		this.data2Mes = data2Mes;
	}

	public String getData3Mes() {
		return data3Mes;
	}

	public void setData3Mes(String data3Mes) {
		this.data3Mes = data3Mes;
	}

	public String getData4Mes() {
		return data4Mes;
	}

	public void setData4Mes(String data4Mes) {
		this.data4Mes = data4Mes;
	}

	public String getData5Mes() {
		return data5Mes;
	}

	public void setData5Mes(String data5Mes) {
		this.data5Mes = data5Mes;
	}

	public String getData6Mes() {
		return data6Mes;
	}

	public void setData6Mes(String data6Mes) {
		this.data6Mes = data6Mes;
	}

	public String getData7Mes() {
		return data7Mes;
	}

	public void setData7Mes(String data7Mes) {
		this.data7Mes = data7Mes;
	}

	public String getData8Mes() {
		return data8Mes;
	}

	public void setData8Mes(String data8Mes) {
		this.data8Mes = data8Mes;
	}

	public String getData9Mes() {
		return data9Mes;
	}

	public void setData9Mes(String data9Mes) {
		this.data9Mes = data9Mes;
	}

	public String getData10Mes() {
		return data10Mes;
	}

	public void setData10Mes(String data10Mes) {
		this.data10Mes = data10Mes;
	}

	public String getData11Mes() {
		return data11Mes;
	}

	public void setData11Mes(String data11Mes) {
		this.data11Mes = data11Mes;
	}

	public String getData12Mes() {
		return data12Mes;
	}

	public void setData12Mes(String data12Mes) {
		this.data12Mes = data12Mes;
	}

	public BigDecimal getQuantidade1Mes() {
		return quantidade1Mes;
	}

	public void setQuantidade1Mes(BigDecimal quantidade1Mes) {
		this.quantidade1Mes = quantidade1Mes;
	}

	public BigDecimal getQuantidade2Mes() {
		return quantidade2Mes;
	}

	public void setQuantidade2Mes(BigDecimal quantidade2Mes) {
		this.quantidade2Mes = quantidade2Mes;
	}

	public BigDecimal getQuantidade3Mes() {
		return quantidade3Mes;
	}

	public void setQuantidade3Mes(BigDecimal quantidade3Mes) {
		this.quantidade3Mes = quantidade3Mes;
	}

	public BigDecimal getQuantidade4Mes() {
		return quantidade4Mes;
	}

	public void setQuantidade4Mes(BigDecimal quantidade4Mes) {
		this.quantidade4Mes = quantidade4Mes;
	}

	public BigDecimal getQuantidade5Mes() {
		return quantidade5Mes;
	}

	public void setQuantidade5Mes(BigDecimal quantidade5Mes) {
		this.quantidade5Mes = quantidade5Mes;
	}

	public BigDecimal getQuantidade6Mes() {
		return quantidade6Mes;
	}

	public void setQuantidade6Mes(BigDecimal quantidade6Mes) {
		this.quantidade6Mes = quantidade6Mes;
	}

	public BigDecimal getQuantidade7Mes() {
		return quantidade7Mes;
	}

	public void setQuantidade7Mes(BigDecimal quantidade7Mes) {
		this.quantidade7Mes = quantidade7Mes;
	}

	public BigDecimal getQuantidade8Mes() {
		return quantidade8Mes;
	}

	public void setQuantidade8Mes(BigDecimal quantidade8Mes) {
		this.quantidade8Mes = quantidade8Mes;
	}

	public BigDecimal getQuantidade9Mes() {
		return quantidade9Mes;
	}

	public void setQuantidade9Mes(BigDecimal quantidade9Mes) {
		this.quantidade9Mes = quantidade9Mes;
	}

	public BigDecimal getQuantidade10Mes() {
		return quantidade10Mes;
	}

	public void setQuantidade10Mes(BigDecimal quantidade10Mes) {
		this.quantidade10Mes = quantidade10Mes;
	}

	public BigDecimal getQuantidade11Mes() {
		return quantidade11Mes;
	}

	public void setQuantidade11Mes(BigDecimal quantidade11Mes) {
		this.quantidade11Mes = quantidade11Mes;
	}

	public BigDecimal getQuantidade12Mes() {
		return quantidade12Mes;
	}

	public void setQuantidade12Mes(BigDecimal quantidade12Mes) {
		this.quantidade12Mes = quantidade12Mes;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public String getMesReferenciaFormatado() {
		return mesReferenciaFormatado;
	}

	public void setMesReferenciaFormatado(String mesReferenciaFormatado) {
		this.mesReferenciaFormatado = mesReferenciaFormatado;
	}
}
