package br.gov.pr.guaira.animalys.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

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

    // Campos para controle de acompanhamento mensal - DATAS
    @Temporal(TemporalType.DATE)
    private Date data1Mes;

    @Temporal(TemporalType.DATE)
    private Date data2Mes;

    @Temporal(TemporalType.DATE)
    private Date data3Mes;

    @Temporal(TemporalType.DATE)
    private Date data4Mes;

    @Temporal(TemporalType.DATE)
    private Date data5Mes;

    @Temporal(TemporalType.DATE)
    private Date data6Mes;

    @Temporal(TemporalType.DATE)
    private Date data7Mes;

    @Temporal(TemporalType.DATE)
    private Date data8Mes;

    @Temporal(TemporalType.DATE)
    private Date data9Mes;

    @Temporal(TemporalType.DATE)
    private Date data10Mes;

    @Temporal(TemporalType.DATE)
    private Date data11Mes;

    @Temporal(TemporalType.DATE)
    private Date data12Mes;

    // Campos para controle de acompanhamento mensal - QUANTIDADES (Kg)
    @Column(precision = 10, scale = 3)
    private BigDecimal quantidade1Mes;

    @Column(precision = 10, scale = 3)
    private BigDecimal quantidade2Mes;

    @Column(precision = 10, scale = 3)
    private BigDecimal quantidade3Mes;

    @Column(precision = 10, scale = 3)
    private BigDecimal quantidade4Mes;

    @Column(precision = 10, scale = 3)
    private BigDecimal quantidade5Mes;

    @Column(precision = 10, scale = 3)
    private BigDecimal quantidade6Mes;

    @Column(precision = 10, scale = 3)
    private BigDecimal quantidade7Mes;

    @Column(precision = 10, scale = 3)
    private BigDecimal quantidade8Mes;

    @Column(precision = 10, scale = 3)
    private BigDecimal quantidade9Mes;

    @Column(precision = 10, scale = 3)
    private BigDecimal quantidade10Mes;

    @Column(precision = 10, scale = 3)
    private BigDecimal quantidade11Mes;

    @Column(precision = 10, scale = 3)
    private BigDecimal quantidade12Mes;

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

    public Date getData1Mes() {
        return data1Mes;
    }

    public void setData1Mes(Date data1Mes) {
        this.data1Mes = data1Mes;
    }

    public Date getData2Mes() {
        return data2Mes;
    }

    public void setData2Mes(Date data2Mes) {
        this.data2Mes = data2Mes;
    }

    public Date getData3Mes() {
        return data3Mes;
    }

    public void setData3Mes(Date data3Mes) {
        this.data3Mes = data3Mes;
    }

    public Date getData4Mes() {
        return data4Mes;
    }

    public void setData4Mes(Date data4Mes) {
        this.data4Mes = data4Mes;
    }

    public Date getData5Mes() {
        return data5Mes;
    }

    public void setData5Mes(Date data5Mes) {
        this.data5Mes = data5Mes;
    }

    public Date getData6Mes() {
        return data6Mes;
    }

    public void setData6Mes(Date data6Mes) {
        this.data6Mes = data6Mes;
    }

    public Date getData7Mes() {
        return data7Mes;
    }

    public void setData7Mes(Date data7Mes) {
        this.data7Mes = data7Mes;
    }

    public Date getData8Mes() {
        return data8Mes;
    }

    public void setData8Mes(Date data8Mes) {
        this.data8Mes = data8Mes;
    }

    public Date getData9Mes() {
        return data9Mes;
    }

    public void setData9Mes(Date data9Mes) {
        this.data9Mes = data9Mes;
    }

    public Date getData10Mes() {
        return data10Mes;
    }

    public void setData10Mes(Date data10Mes) {
        this.data10Mes = data10Mes;
    }

    public Date getData11Mes() {
        return data11Mes;
    }

    public void setData11Mes(Date data11Mes) {
        this.data11Mes = data11Mes;
    }

    public Date getData12Mes() {
        return data12Mes;
    }

    public void setData12Mes(Date data12Mes) {
        this.data12Mes = data12Mes;
    }

    public BigDecimal getQuantidade1Mes() {
        return quantidade1Mes;
    }

    public void setQuantidade1Mes(BigDecimal quantidade1Mes) {
        this.quantidade1Mes = quantidade1Mes;
    }

    public BigDecimal getQuantidade2Mes() {
        return quantidade2Mes;
    }

    public void setQuantidade2Mes(BigDecimal quantidade2Mes) {
        this.quantidade2Mes = quantidade2Mes;
    }

    public BigDecimal getQuantidade3Mes() {
        return quantidade3Mes;
    }

    public void setQuantidade3Mes(BigDecimal quantidade3Mes) {
        this.quantidade3Mes = quantidade3Mes;
    }

    public BigDecimal getQuantidade4Mes() {
        return quantidade4Mes;
    }

    public void setQuantidade4Mes(BigDecimal quantidade4Mes) {
        this.quantidade4Mes = quantidade4Mes;
    }

    public BigDecimal getQuantidade5Mes() {
        return quantidade5Mes;
    }

    public void setQuantidade5Mes(BigDecimal quantidade5Mes) {
        this.quantidade5Mes = quantidade5Mes;
    }

    public BigDecimal getQuantidade6Mes() {
        return quantidade6Mes;
    }

    public void setQuantidade6Mes(BigDecimal quantidade6Mes) {
        this.quantidade6Mes = quantidade6Mes;
    }

    public BigDecimal getQuantidade7Mes() {
        return quantidade7Mes;
    }

    public void setQuantidade7Mes(BigDecimal quantidade7Mes) {
        this.quantidade7Mes = quantidade7Mes;
    }

    public BigDecimal getQuantidade8Mes() {
        return quantidade8Mes;
    }

    public void setQuantidade8Mes(BigDecimal quantidade8Mes) {
        this.quantidade8Mes = quantidade8Mes;
    }

    public BigDecimal getQuantidade9Mes() {
        return quantidade9Mes;
    }

    public void setQuantidade9Mes(BigDecimal quantidade9Mes) {
        this.quantidade9Mes = quantidade9Mes;
    }

    public BigDecimal getQuantidade10Mes() {
        return quantidade10Mes;
    }

    public void setQuantidade10Mes(BigDecimal quantidade10Mes) {
        this.quantidade10Mes = quantidade10Mes;
    }

    public BigDecimal getQuantidade11Mes() {
        return quantidade11Mes;
    }

    public void setQuantidade11Mes(BigDecimal quantidade11Mes) {
        this.quantidade11Mes = quantidade11Mes;
    }

    public BigDecimal getQuantidade12Mes() {
        return quantidade12Mes;
    }

    public void setQuantidade12Mes(BigDecimal quantidade12Mes) {
        this.quantidade12Mes = quantidade12Mes;
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