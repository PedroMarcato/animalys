package br.gov.pr.guaira.animalys.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(schema = "atendimento")
public class Atendimento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idAtendimento;
	@Lob
	private String diagnostico;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar data;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "animal", referencedColumnName = "idAnimal")
	private Animal animal;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "solicitacao", referencedColumnName = "idSolicitacao")
	private Solicitacao solicitacao;
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name = "atendimento_procedimento", joinColumns = @JoinColumn(name = "atendimento"), inverseJoinColumns = @JoinColumn(name = "procedimento"), schema = "atendimento")
	private List<Procedimento> procedimentos;
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "atendimento")
	private List<ItemLoteAtendimento> itemLoteAtendimento;
	@Transient
	private String dataAtendimentoFormatada;
	@Transient
	private SimpleDateFormat sdf;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "profissional", referencedColumnName = "idProfissional")
	private Profissional profissional;
	@Enumerated(EnumType.STRING)
	private TipoAtendimento tipoAtendimento;

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public List<Procedimento> getProcedimentos() {
		return procedimentos;
	}

	public void setProcedimentos(List<Procedimento> procedimentos) {
		this.procedimentos = procedimentos;
	}

	public List<ItemLoteAtendimento> getItemLoteAtendimento() {
		return itemLoteAtendimento;
	}

	public void setItemLoteAtendimento(List<ItemLoteAtendimento> itemLoteAtendimento) {
		this.itemLoteAtendimento = itemLoteAtendimento;
	}

	public Integer getIdAtendimento() {
		return idAtendimento;
	}

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public TipoAtendimento getTipoAtendimento() {
		return tipoAtendimento;
	}

	public void setTipoAtendimento(TipoAtendimento tipoAtendimento) {
		this.tipoAtendimento = tipoAtendimento;
	}

	public String getDataAtendimentoFormatada() {
		sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return sdf.format(this.data.getTime());
	}

	public void setDataAtendimentoFormatada(String dataAtendimentoFormatada) {
		this.dataAtendimentoFormatada = dataAtendimentoFormatada;
	}

	@Override
	public String toString() {
		return "Atendimento [idAtendimento=" + idAtendimento + ", diagnostico=" + diagnostico + ", data=" + data
				+ ", animal=" + animal + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((animal == null) ? 0 : animal.hashCode());
		result = prime * result + ((idAtendimento == null) ? 0 : idAtendimento.hashCode());
		result = prime * result + ((solicitacao == null) ? 0 : solicitacao.hashCode());
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
		Atendimento other = (Atendimento) obj;
		if (animal == null) {
			if (other.animal != null)
				return false;
		} else if (!animal.equals(other.animal))
			return false;
		if (idAtendimento == null) {
			if (other.idAtendimento != null)
				return false;
		} else if (!idAtendimento.equals(other.idAtendimento))
			return false;
		if (solicitacao == null) {
			if (other.solicitacao != null)
				return false;
		} else if (!solicitacao.equals(other.solicitacao))
			return false;
		return true;
	}

}
