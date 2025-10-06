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

@Entity
@Table(schema = "estoque")
public class RetiradaRacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRetiradaRacao;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietario", referencedColumnName = "idProprietario")
    private Proprietario proprietario;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "animal", referencedColumnName = "idAnimal")
    private Animal animal;

    @Column(precision = 10, scale = 3)
    private BigDecimal quantidadeKg;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dataRetirada;

    @Column
    private Integer mesReferencia; // 1 a 12 representando os meses

    @Column(length = 500)
    private String observacoes;

    // Construtores
    public RetiradaRacao() {
        this.quantidadeKg = BigDecimal.ZERO;
    }

    public RetiradaRacao(Proprietario proprietario, Animal animal, BigDecimal quantidadeKg, 
                        Calendar dataRetirada, Integer mesReferencia) {
        this.proprietario = proprietario;
        this.animal = animal;
        this.quantidadeKg = quantidadeKg;
        this.dataRetirada = dataRetirada;
        this.mesReferencia = mesReferencia;
    }

    // Getters e Setters
    public Integer getIdRetiradaRacao() {
        return idRetiradaRacao;
    }

    public void setIdRetiradaRacao(Integer idRetiradaRacao) {
        this.idRetiradaRacao = idRetiradaRacao;
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

    public BigDecimal getQuantidadeKg() {
        return quantidadeKg;
    }

    public void setQuantidadeKg(BigDecimal quantidadeKg) {
        this.quantidadeKg = quantidadeKg;
    }

    public Calendar getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(Calendar dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public Integer getMesReferencia() {
        return mesReferencia;
    }

    public void setMesReferencia(Integer mesReferencia) {
        this.mesReferencia = mesReferencia;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getMesReferenciaFormatado() {
        if (mesReferencia == null) {
            return "";
        }
        
        String[] meses = {
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        };
        
        if (mesReferencia >= 1 && mesReferencia <= 12) {
            return meses[mesReferencia - 1];
        }
        
        return "";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idRetiradaRacao == null) ? 0 : idRetiradaRacao.hashCode());
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
        RetiradaRacao other = (RetiradaRacao) obj;
        if (idRetiradaRacao == null) {
            if (other.idRetiradaRacao != null)
                return false;
        } else if (!idRetiradaRacao.equals(other.idRetiradaRacao))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RetiradaRacao [idRetiradaRacao=" + idRetiradaRacao + ", proprietario=" 
                + (proprietario != null ? proprietario.getNome() : "null") + ", animal=" 
                + (animal != null ? animal.getNome() : "null") + ", quantidadeKg=" + quantidadeKg 
                + ", mesReferencia=" + getMesReferenciaFormatado() + "]";
    }
}