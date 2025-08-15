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
@Table(schema="estoque")
public class ItemEntrada implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idItemEntrada;
	@Column
	private Integer quantidade;
	@Column
	private Double subTotal;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "produto", referencedColumnName = "idProduto")
	private Produto produto;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "lote", referencedColumnName = "idLote")
	private Lote lote;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "entradaProduto", referencedColumnName = "idEntradaProduto")
	private EntradaProduto entradaProduto;
	@Column
	private Integer quantidadeAnterior;//qtd que o produto tinha antes de fazer a entrada;

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Lote getLote() {
		return lote;
	}

	public void setLote(Lote lote) {
		this.lote = lote;
	}

	public Integer getIdItemEntrada() {
		return idItemEntrada;
	}
	
	public EntradaProduto getEntradaProduto() {
		return entradaProduto;
	}

	public void setEntradaProduto(EntradaProduto entradaProduto) {
		this.entradaProduto = entradaProduto;
	}
	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
	
	public Integer getQuantidadeAnterior() {
		return quantidadeAnterior;
	}

	public void setQuantidadeAnterior(Integer quantidadeAnterior) {
		this.quantidadeAnterior = quantidadeAnterior;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idItemEntrada == null) ? 0 : idItemEntrada.hashCode());
		result = prime * result + ((lote == null) ? 0 : lote.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
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
		ItemEntrada other = (ItemEntrada) obj;
		if (idItemEntrada == null) {
			if (other.idItemEntrada != null)
				return false;
		} else if (!idItemEntrada.equals(other.idItemEntrada))
			return false;
		if (lote == null) {
			if (other.lote != null)
				return false;
		} else if (!lote.equals(other.lote))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		if (quantidade == null) {
			if (other.quantidade != null)
				return false;
		} else if (!quantidade.equals(other.quantidade))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ItemEntrada [idItemEntrada=" + idItemEntrada + ", quantidade=" + quantidade + ", produto=" + produto
				+ ", lote=" + lote + "]";
	}

}
