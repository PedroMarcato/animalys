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
public class TipoProduto implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idTipoProduto;
	@Column
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao.toUpperCase();
	}
	public Integer getIdTipoProduto() {
		return idTipoProduto;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((idTipoProduto == null) ? 0 : idTipoProduto.hashCode());
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
		TipoProduto other = (TipoProduto) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (idTipoProduto == null) {
			if (other.idTipoProduto != null)
				return false;
		} else if (!idTipoProduto.equals(other.idTipoProduto))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "TipoProduto [idTipoProduto=" + idTipoProduto + ", descricao=" + descricao + "]";
	}
	
	
}
