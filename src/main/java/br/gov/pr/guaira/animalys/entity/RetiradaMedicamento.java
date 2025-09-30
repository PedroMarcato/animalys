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

@Entity
@Table(schema = "estoque")
public class RetiradaMedicamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRetiradaMedicamento;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietario", referencedColumnName = "idProprietario")
    private Proprietario proprietario;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "animal", referencedColumnName = "idAnimal")
    private Animal animal;

    @Column(length = 200)
    private String nomeMedicamento;

    @Column
    private Integer quantidade;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dataRetirada;

    @Column(length = 500)
    private String observacoes;

    // Construtores
    public RetiradaMedicamento() {
        this.dataRetirada = Calendar.getInstance();
    }

    // Getters e Setters
    public Integer getIdRetiradaMedicamento() {
        return idRetiradaMedicamento;
    }

    public void setIdRetiradaMedicamento(Integer idRetiradaMedicamento) {
        this.idRetiradaMedicamento = idRetiradaMedicamento;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Proprietario proprietario) {
        this.proprietario = proprietario;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Calendar getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(Calendar dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idRetiradaMedicamento == null) ? 0 : idRetiradaMedicamento.hashCode());
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
        RetiradaMedicamento other = (RetiradaMedicamento) obj;
        if (idRetiradaMedicamento == null) {
            if (other.idRetiradaMedicamento != null)
                return false;
        } else if (!idRetiradaMedicamento.equals(other.idRetiradaMedicamento))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RetiradaMedicamento [idRetiradaMedicamento=" + idRetiradaMedicamento + 
               ", proprietario=" + (proprietario != null ? proprietario.getNome() : null) + 
               ", animal=" + (animal != null ? animal.getNome() : null) + 
               ", quantidade=" + quantidade + "]";
    }
}