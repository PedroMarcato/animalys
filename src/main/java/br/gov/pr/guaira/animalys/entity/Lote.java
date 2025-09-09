package br.gov.pr.guaira.animalys.entity;

import java.io.Serializable;
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
import javax.persistence.Transient;

import java.text.SimpleDateFormat;

@Entity
@Table(schema="estoque")
public class Lote implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idLote;
	@Column
	private Double preco;
	@Column
	private Integer quantidade;
	@Column
	private String numeroLote;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar validade;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar fabricacao;
	@ManyToOne(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	@JoinColumn(name="produto", referencedColumnName="idProduto")
	private Produto produto;
	@Transient
	private String validadeFormatada;
	@Transient
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public Double getPreco() {
		return preco;
	}
	public void setPreco(Double preco) {
		System.out.println("---------------------------------------------------------------------------------------------------------preco"+preco);
		this.preco = preco;
	}
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	public Integer getIdLote() {
		return idLote;
	}
	
	public String getNumeroLote() {
		return numeroLote;
	}
	public void setNumeroLote(String numeroLote) {
		this.numeroLote = numeroLote;
	}
	public Calendar getValidade() {
		return validade;
	}
	public void setValidade(Calendar validade) {
		this.validade = validade;
	}
	public Calendar getFabricacao() {
		return fabricacao;
	}
	public void setFabricacao(Calendar fabricacao) {
		this.fabricacao = fabricacao;
	}	
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public String getValidadeFormatada() {
		return sdf.format(this.validade.getTime());
	}
	public void setValidadeFormatada(String validadeFormatada) {
		this.validadeFormatada = validadeFormatada;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idLote == null) ? 0 : idLote.hashCode());
		result = prime * result + ((numeroLote == null) ? 0 : numeroLote.hashCode());
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
		Lote other = (Lote) obj;
		if (idLote == null) {
			if (other.idLote != null)
				return false;
		} else if (!idLote.equals(other.idLote))
			return false;
		if (numeroLote == null) {
			if (other.numeroLote != null)
				return false;
		} else if (!numeroLote.equals(other.numeroLote))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return this.produto.getNome()+" L: "+ this.numeroLote + " V: " + this.getValidadeFormatada();
	}

}
