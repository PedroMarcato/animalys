package br.gov.pr.guaira.animalys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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

@SuppressWarnings("unused")
@Entity
@Table(schema="estoque")
public class AcertoEstoque implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idAcertoEstoque;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar data;
	@ManyToOne(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinColumn(name="lote", referencedColumnName="idLote")
	private Lote lote;
	@ManyToOne(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinColumn(name="tipoAcerto", referencedColumnName="idTipoAcerto")
	private TipoAcerto tipoAcerto;
	@Column
	private Integer qtdAnterior;
	@Column
	private Integer qtdNova;

	
	public Calendar getData() {
		return data;
	}
	public void setData(Calendar data) {
		this.data = data;
	}
	public Lote getLote() {
		return lote;
	}
	public void setLote(Lote lote) {
		this.lote = lote;
	}
	public Integer getQtdAnterior() {
		return qtdAnterior;
	}
	public void setQtdAnterior(Integer qtdAnterior) {
		this.qtdAnterior = qtdAnterior;
	}
	public Integer getQtdNova() {
		return qtdNova;
	}
	public void setQtdNova(Integer qtdNova) {
		this.qtdNova = qtdNova;
	}
	public Integer getIdAcertoEstoque() {
		return idAcertoEstoque;
	}
	public TipoAcerto getTipoAcerto() {
		return tipoAcerto;
	}
	public void setTipoAcerto(TipoAcerto tipoAcerto) {
		this.tipoAcerto = tipoAcerto;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idAcertoEstoque == null) ? 0 : idAcertoEstoque.hashCode());
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
		AcertoEstoque other = (AcertoEstoque) obj;
		if (idAcertoEstoque == null) {
			if (other.idAcertoEstoque != null)
				return false;
		} else if (!idAcertoEstoque.equals(other.idAcertoEstoque))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AcertoEstoque [idAcertoEstoque=" + idAcertoEstoque + "]";
	}
}
