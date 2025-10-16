package br.gov.pr.guaira.animalys.model;

import java.io.Serializable;

public class TermoItraconazolDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Dados do proprietário
	private String nomeProprietario;
	private String cpfProprietario;
	private String rgProprietario;
	private String enderecoProprietario;
	private String numeroEndereco;
	private String bairroProprietario;
	private String cepProprietario;
	private String cidadeProprietario;
	private String telefoneProprietario;
	private String celularProprietario;
	private String contatoProprietario; // Campo genérico para fallback

	// Dados do animal
	private String nomeAnimal;
	private String especie;
	private String raca;
	private String sexo;
	private String cor;
	private String numeroMicrochip;
	private String castradoStr;
	private String idade;
	private String porteAnimal;

	// Dados do termo
	private String dataRetirada;
	private Integer quantidadeRetirada;
	private String observacoes;

	// Datas dos meses seguintes
	private String data1Mes;
	private String data2Mes;
	private String data3Mes;
	private String data4Mes;
	private String data5Mes;
	private String data6Mes;
	private String data7Mes;

	// Campos adicionais do relatório JRXML
	private String data; // Campo genérico "data" usado no JRXML
	private String procedimento;
	private String responsavel;
	private String tipoAtendimento;
	private String diagnostico;
	private String statusSolicitacao;
	private java.util.Calendar dataEntrada;
	private String escoreCorporal;
	private String peso;
	private String fc;
	private String fr;
	private String temperatura;
	private String tratamento;
	private Integer idAnimal;

	// Getters e Setters
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

	public String getRgProprietario() {
		return rgProprietario;
	}

	public void setRgProprietario(String rgProprietario) {
		this.rgProprietario = rgProprietario;
	}

	public String getEnderecoProprietario() {
		return enderecoProprietario;
	}

	public void setEnderecoProprietario(String enderecoProprietario) {
		this.enderecoProprietario = enderecoProprietario;
	}

	public String getNumeroEndereco() {
		return numeroEndereco;
	}

	public void setNumeroEndereco(String numeroEndereco) {
		this.numeroEndereco = numeroEndereco;
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

	public String getContatoProprietario() {
		return contatoProprietario;
	}

	public void setContatoProprietario(String contatoProprietario) {
		this.contatoProprietario = contatoProprietario;
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

	public String getRaca() {
		return raca;
	}

	public void setRaca(String raca) {
		this.raca = raca;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getNumeroMicrochip() {
		return numeroMicrochip;
	}

	public void setNumeroMicrochip(String numeroMicrochip) {
		this.numeroMicrochip = numeroMicrochip;
	}

	public String getCastradoStr() {
		return castradoStr;
	}

	public void setCastradoStr(String castradoStr) {
		this.castradoStr = castradoStr;
	}

	public String getIdade() {
		return idade;
	}

	public void setIdade(String idade) {
		this.idade = idade;
	}

	public String getPorteAnimal() {
		return porteAnimal;
	}

	public void setPorteAnimal(String porteAnimal) {
		this.porteAnimal = porteAnimal;
	}

	public String getDataRetirada() {
		return dataRetirada;
	}

	public void setDataRetirada(String dataRetirada) {
		this.dataRetirada = dataRetirada;
	}

	public Integer getQuantidadeRetirada() {
		return quantidadeRetirada;
	}

	public void setQuantidadeRetirada(Integer quantidadeRetirada) {
		this.quantidadeRetirada = quantidadeRetirada;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
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

	// Getters e Setters para campos adicionais do JRXML
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getTipoAtendimento() {
		return tipoAtendimento;
	}

	public void setTipoAtendimento(String tipoAtendimento) {
		this.tipoAtendimento = tipoAtendimento;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getStatusSolicitacao() {
		return statusSolicitacao;
	}

	public void setStatusSolicitacao(String statusSolicitacao) {
		this.statusSolicitacao = statusSolicitacao;
	}

	public java.util.Calendar getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(java.util.Calendar dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public String getEscoreCorporal() {
		return escoreCorporal;
	}

	public void setEscoreCorporal(String escoreCorporal) {
		this.escoreCorporal = escoreCorporal;
	}

	public String getPeso() {
		return peso;
	}

	public void setPeso(String peso) {
		this.peso = peso;
	}

	public String getFc() {
		return fc;
	}

	public void setFc(String fc) {
		this.fc = fc;
	}

	public String getFr() {
		return fr;
	}

	public void setFr(String fr) {
		this.fr = fr;
	}

	public String getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(String temperatura) {
		this.temperatura = temperatura;
	}

	public String getTratamento() {
		return tratamento;
	}

	public void setTratamento(String tratamento) {
		this.tratamento = tratamento;
	}

	public Integer getIdAnimal() {
		return idAnimal;
	}

	public void setIdAnimal(Integer idAnimal) {
		this.idAnimal = idAnimal;
	}
}
