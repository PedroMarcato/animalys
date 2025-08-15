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
@Table(schema="atendimento")
public class ItemLoteAtendimento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idItemLoteAtendimento;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "lote", referencedColumnName = "idLote")
	private Lote lote;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "atendimento", referencedColumnName = "idAtendimento")
	private Atendimento atendimento;
	@Column
	private Integer quantidade;

	public Lote getLote() {
		return lote;
	}

	public void setLote(Lote lote) {
		this.lote = lote;
	}

	public Atendimento getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Integer getIdItemLoteAtendimento() {
		return idItemLoteAtendimento;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idItemLoteAtendimento == null) ? 0 : idItemLoteAtendimento.hashCode());
		result = prime * result + ((lote == null) ? 0 : lote.hashCode());
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
		ItemLoteAtendimento other = (ItemLoteAtendimento) obj;
		if (idItemLoteAtendimento == null) {
			if (other.idItemLoteAtendimento != null)
				return false;
		} else if (!idItemLoteAtendimento.equals(other.idItemLoteAtendimento))
			return false;
		if (lote == null) {
			if (other.lote != null)
				return false;
		} else if (!lote.equals(other.lote))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ItemLoteAtendimento [idItemLoteAtendimento=" + idItemLoteAtendimento + ", lote=" + lote
				+ ", atendimento=" + atendimento + ", quantidade=" + quantidade + "]";
	}

}
