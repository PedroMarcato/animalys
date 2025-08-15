package br.gov.pr.guaira.animalys.entity;

import java.io.Serializable;

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

@Entity
@Table(schema="animal")
public class Raca implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idRaca;
	@Column
	private String nome;
	@ManyToOne(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinColumn(name="especie", referencedColumnName="idEspecie")
	private Especie especie;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}
	public Especie getEspecie() {
		return especie;
	}
	public void setEspecie(Especie especie) {
		this.especie = especie;
	}
	public Integer getIdRaca() {
		return idRaca;
	}
	
	@Override
	public int hashCode() {
	    return (idRaca != null) ? idRaca.hashCode() : 0;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null || getClass() != obj.getClass())
	        return false;
	    Raca other = (Raca) obj;
	    return idRaca != null && idRaca.equals(other.idRaca);
	}

	@Override
	public String toString() {
		return "Raca [idRaca=" + idRaca + ", nome=" + nome + ", especie=" + especie + "]";
	}
	
	
	
}
