package br.gov.pr.guaira.animalys.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Proprietario;

@Entity
@Table(name = "termos_consulta", schema = "animal")
public class TermoConsulta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "proprietario_id", nullable = false)
	private Proprietario proprietario;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "animal_id", nullable = false)
	private Animal animal;

	@Column(name = "nome_proprietario", nullable = false, length = 200)
	private String nomeProprietario;

	@Column(name = "rg_proprietario", nullable = false, length = 20)
	private String rgProprietario;

	@Column(name = "cpf_proprietario", nullable = false, length = 14)
	private String cpfProprietario;

	@Column(name = "endereco_proprietario", nullable = false, length = 500)
	private String enderecoProprietario;

	@Column(name = "nome_animal", nullable = false, length = 100)
	private String nomeAnimal;

	@Column(name = "ficha_controle", nullable = false, length = 50)
	private String fichaControle;

	@Column(name = "especie_animal", nullable = false, length = 50)
	private String especieAnimal;

	@Column(name = "sexo_animal", nullable = false, length = 10)
	private String sexoAnimal;

	@Column(name = "cor_animal", length = 50)
	private String corAnimal;

	@NotNull
	@Column(name = "data_assinatura", nullable = false)
	private LocalDate dataAssinatura;

	@NotNull
	@Column(name = "data_cadastro", nullable = false)
	private LocalDateTime dataCadastro;

	@Column(name = "observacoes", length = 1000)
	private String observacoes;

	public TermoConsulta() {
		this.dataCadastro = LocalDateTime.now();
		this.dataAssinatura = LocalDate.now();
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Proprietario getProprietario() {
		return proprietario;
	}

	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

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

	public String getEspecieAnimal() {
		return especieAnimal;
	}

	public void setEspecieAnimal(String especieAnimal) {
		this.especieAnimal = especieAnimal;
	}

	public String getSexoAnimal() {
		return sexoAnimal;
	}

	public void setSexoAnimal(String sexoAnimal) {
		this.sexoAnimal = sexoAnimal;
	}

	public String getCorAnimal() {
		return corAnimal;
	}

	public void setCorAnimal(String corAnimal) {
		this.corAnimal = corAnimal;
	}

	public LocalDate getDataAssinatura() {
		return dataAssinatura;
	}

	public void setDataAssinatura(LocalDate dataAssinatura) {
		this.dataAssinatura = dataAssinatura;
	}

	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	// Métodos auxiliares para conversão de data para JSF
	public Date getDataAssinaturaAsDate() {
		if (dataAssinatura != null) {
			return Date.from(dataAssinatura.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		return null;
	}
	
	public Date getDataCadastroAsDate() {
		if (dataCadastro != null) {
			return Date.from(dataCadastro.atZone(ZoneId.systemDefault()).toInstant());
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		TermoConsulta other = (TermoConsulta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}