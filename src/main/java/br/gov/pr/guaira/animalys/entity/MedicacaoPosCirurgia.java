package br.gov.pr.guaira.animalys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MedicacaoPosCirurgia implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idMedicacaoPosCirurgia;
	@Column(length=2000)
	private String posologia;
	@Column
	private Integer quantidadeProduto;
	@Column
	private boolean entregue;
	
	public String getPosologia() {
		return posologia;
	}
	public void setPosologia(String posologia) {
		this.posologia = posologia;
	}
	public Integer getQuantidadeProduto() {
		return quantidadeProduto;
	}
	public void setQuantidadeProduto(Integer quantidadeProduto) {
		this.quantidadeProduto = quantidadeProduto;
	}
	public boolean isEntregue() {
		return entregue;
	}
	public void setEntregue(boolean entregue) {
		this.entregue = entregue;
	}
	public Integer getIdMedicacaoPosCirurgia() {
		return idMedicacaoPosCirurgia;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (entregue ? 1231 : 1237);
		result = prime * result + ((idMedicacaoPosCirurgia == null) ? 0 : idMedicacaoPosCirurgia.hashCode());
		result = prime * result + ((posologia == null) ? 0 : posologia.hashCode());
		result = prime * result + ((quantidadeProduto == null) ? 0 : quantidadeProduto.hashCode());
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
		MedicacaoPosCirurgia other = (MedicacaoPosCirurgia) obj;
		if (entregue != other.entregue)
			return false;
		if (idMedicacaoPosCirurgia == null) {
			if (other.idMedicacaoPosCirurgia != null)
				return false;
		} else if (!idMedicacaoPosCirurgia.equals(other.idMedicacaoPosCirurgia))
			return false;
		if (posologia == null) {
			if (other.posologia != null)
				return false;
		} else if (!posologia.equals(other.posologia))
			return false;
		if (quantidadeProduto == null) {
			if (other.quantidadeProduto != null)
				return false;
		} else if (!quantidadeProduto.equals(other.quantidadeProduto))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "MedicacaoPosCirurgia [idMedicacaoPosCirurgia=" + idMedicacaoPosCirurgia + ", posologia=" + posologia
				+ ", quantidadeProduto=" + quantidadeProduto + ", entregue=" + entregue + "]";
	}
	
	
	
}
