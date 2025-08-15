package br.gov.pr.guaira.animalys.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(schema="estoque")
public class EntradaProduto implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idEntradaProduto;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar data;
	@ManyToOne(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinColumn(name="fornecedor", referencedColumnName="idFornecedor")
	private Fornecedor fornecedor;
	@Column
	private Long numeroNota;
	@Column
	private Double valorTotal;
	@OneToMany(cascade=CascadeType.MERGE, fetch=FetchType.LAZY, mappedBy="entradaProduto")
	private List<ItemEntrada> itens;
	
	public Calendar getData() {
		return data;
	}
	public void setData(Calendar data) {
		this.data = data;
	}
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	public Integer getIdEntradaProduto() {
		return idEntradaProduto;
	}
	public Long getNumeroNota() {
		return numeroNota;
	}
	public void setNumeroNota(Long numeroNota) {
		this.numeroNota = numeroNota;
	}
	public Double getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}
	public List<ItemEntrada> getItens() {
		return itens;
	}
	public void setItens(List<ItemEntrada> itens) {
		this.itens = itens;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((fornecedor == null) ? 0 : fornecedor.hashCode());
		result = prime * result + ((idEntradaProduto == null) ? 0 : idEntradaProduto.hashCode());
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
		EntradaProduto other = (EntradaProduto) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (fornecedor == null) {
			if (other.fornecedor != null)
				return false;
		} else if (!fornecedor.equals(other.fornecedor))
			return false;
		if (idEntradaProduto == null) {
			if (other.idEntradaProduto != null)
				return false;
		} else if (!idEntradaProduto.equals(other.idEntradaProduto))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "EntradaProduto [idEntradaProduto=" + idEntradaProduto + ", data=" + data + ", fornecedor=" + fornecedor
				+ "]";
	}

}
