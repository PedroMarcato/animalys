package br.gov.pr.guaira.animalys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema="estoque")
public class TipoAcerto implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idTipoAcerto;
	@Column
	private String descricao;
	@Column
	private String operacao;
	@Column
	private boolean iniciaEstoque;
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getOperacao() {
		return operacao;
	}
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	public Integer getIdTipoAcerto() {
		return idTipoAcerto;
	}
	public boolean isIniciaEstoque() {
		return iniciaEstoque;
	}
	public void setIniciaEstoque(boolean iniciaEstoque) {
		this.iniciaEstoque = iniciaEstoque;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idTipoAcerto == null) ? 0 : idTipoAcerto.hashCode());
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
		TipoAcerto other = (TipoAcerto) obj;
		if (idTipoAcerto == null) {
			if (other.idTipoAcerto != null)
				return false;
		} else if (!idTipoAcerto.equals(other.idTipoAcerto))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "TipoAcerto [idTipoAcerto=" + idTipoAcerto + ", descricao=" + descricao + ", operacao=" + operacao + "]";
	}
	
	
}
