package br.gov.pr.guaira.animalys.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(schema="atendimento")
public class Solicitacao implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idSolicitacao;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar data;
	@ManyToMany(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinTable(name = "solicitacao_animal", joinColumns = @JoinColumn(name = "solicitacao"), 
	inverseJoinColumns = @JoinColumn(name = "animal"), schema="atendimento")
	private List<Animal> animais;
	@ManyToOne(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinColumn(name="proprietario", referencedColumnName="idProprietario")
	private Proprietario proprietario;
	@Enumerated(EnumType.STRING)
	private Status status;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataAgendaVisita;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataAgendaCastracao;
	@Enumerated(EnumType.STRING)
	private ModalidadeSolicitante modalidade;
	@Column
	private String motivoCancelamento;
	
	public Calendar getData() {
		return data;
	}
	public void setData(Calendar data) {
		this.data = data;
	}
	public Proprietario getProprietario() {
		return proprietario;
	}
	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}
	public Integer getIdSolicitacao() {
		return idSolicitacao;
	}
	public List<Animal> getAnimais() {
		return animais;
	}
	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Calendar getDataAgendaVisita() {
		return dataAgendaVisita;
	}
	public void setDataAgendaVisita(Calendar dataAgendaVisita) {
		this.dataAgendaVisita = dataAgendaVisita;
	}
	
	public Calendar getDataAgendaCastracao() {
		return dataAgendaCastracao;
	}
	public void setDataAgendaCastracao(Calendar dataAgendaCastracao) {
		this.dataAgendaCastracao = dataAgendaCastracao;
	}
	
	public ModalidadeSolicitante getModalidade() {
		return modalidade;
	}
	public void setModalidade(ModalidadeSolicitante modalidade) {
		this.modalidade = modalidade;
	}
	
	public String getMotivoCancelamento() {
		return motivoCancelamento;
	}
	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((animais == null) ? 0 : animais.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((idSolicitacao == null) ? 0 : idSolicitacao.hashCode());
		result = prime * result + ((proprietario == null) ? 0 : proprietario.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Solicitacao other = (Solicitacao) obj;
		if (animais == null) {
			if (other.animais != null)
				return false;
		} else if (!animais.equals(other.animais))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (idSolicitacao == null) {
			if (other.idSolicitacao != null)
				return false;
		} else if (!idSolicitacao.equals(other.idSolicitacao))
			return false;
		if (proprietario == null) {
			if (other.proprietario != null)
				return false;
		} else if (!proprietario.equals(other.proprietario))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Solicitacao [idSolicitacao=" + idSolicitacao + ", data=" + data + ", animais=" + animais
				+ ", proprietario=" + proprietario + "]";
	}

	
	
	
}
