package br.gov.pr.guaira.animalys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema="animal")
public class Especie implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idEspecie;
	@Column
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public Integer getIdEspecie() {
		return idEspecie;
	}

	@Override
	public int hashCode() {
	    return (idEspecie != null) ? idEspecie.hashCode() : 0;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null || getClass() != obj.getClass())
	        return false;
	    Especie other = (Especie) obj;
	    return idEspecie != null && idEspecie.equals(other.idEspecie);
	}


	@Override
	public String toString() {
		return "Especie [idEspecie=" + idEspecie + ", nome=" + nome + "]";
	}
	
	
}
