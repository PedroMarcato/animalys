package br.gov.pr.guaira.animalys.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.br.CPF;

@Entity
@Table(schema="atendimento")
public class Profissional implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idProfissional;
	@Column
	private String nome;
	@Column
	@CPF
	private String cpf;
	@Column
	private String crmv;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataNascimento;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "contato", referencedColumnName = "idContato")
	private Contato contato;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "endereco", referencedColumnName = "idEndereco")
	private Endereco endereco;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "profissao", referencedColumnName = "idProfissao")
	private Profissao profissao;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Profissao getProfissao() {
		return profissao;
	}

	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}

	public Integer getIdProfissional() {
		return idProfissional;
	}

	public String getCpf() {
		return cpf;
	}
	
	public String getCpfFormatado() {
		if (cpf != null && cpf.length() == 11) {
			return cpf.substring(0, 3) + "." + 
				   cpf.substring(3, 6) + "." + 
				   cpf.substring(6, 9) + "-" + 
				   cpf.substring(9, 11);
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		// Remove formatação do CPF (pontos e hífen) para validação
		System.out.println("[DEBUG CPF] Valor recebido: " + cpf);
		this.cpf = cpf != null ? cpf.replaceAll("[^0-9]", "") : null;
		System.out.println("[DEBUG CPF] Valor armazenado: " + this.cpf);
	}

	public Calendar getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Calendar dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public String getCrmv() {
		return crmv;
	}
	public void setCrmv(String crmv) {
		this.crmv = crmv;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idProfissional == null) ? 0 : idProfissional.hashCode());
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
		Profissional other = (Profissional) obj;
		if (idProfissional == null) {
			if (other.idProfissional != null)
				return false;
		} else if (!idProfissional.equals(other.idProfissional))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Profissional [idProfissional=" + idProfissional + ", nome=" + nome + ", cpf=" + cpf + "]";
	}

}
