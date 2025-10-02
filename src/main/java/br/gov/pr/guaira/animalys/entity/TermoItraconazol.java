package br.gov.pr.guaira.animalys.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "termo_itraconazol", schema = "animal")
public class TermoItraconazol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_termo_itraconazol")
    private Integer idTermoItraconazol;

    @ManyToOne
    @JoinColumn(name = "id_animal", referencedColumnName = "idAnimal")
    private Animal animal;

    @Column(name = "quantidade_retirada")
    private Integer quantidadeRetirada;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_retirada")
    private Calendar dataRetirada;

    @Column(name = "observacoes", length = 1000)
    private String observacoes;

    // Campo específico do termo - porte do animal
    @Column(name = "porte_animal", length = 50)
    private String porteAnimal;

    // Campos para controle mensal
    @Temporal(TemporalType.DATE)
    @Column(name = "data_1_mes")
    private Date data1Mes;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_2_mes")
    private Date data2Mes;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_3_mes")
    private Date data3Mes;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_4_mes")
    private Date data4Mes;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_5_mes")
    private Date data5Mes;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_6_mes")
    private Date data6Mes;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_7_mes")
    private Date data7Mes;

    // Constructors
    public TermoItraconazol() {
        this.dataRetirada = Calendar.getInstance();
    }

    // Getters and Setters
    public Integer getIdTermoItraconazol() {
        return idTermoItraconazol;
    }

    public void setIdTermoItraconazol(Integer idTermoItraconazol) {
        this.idTermoItraconazol = idTermoItraconazol;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Integer getQuantidadeRetirada() {
        return quantidadeRetirada;
    }

    public void setQuantidadeRetirada(Integer quantidadeRetirada) {
        this.quantidadeRetirada = quantidadeRetirada;
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

    public String getPorteAnimal() {
        return porteAnimal;
    }

    public void setPorteAnimal(String porteAnimal) {
        this.porteAnimal = porteAnimal;
    }

    public String getDataRetiradaFormatada() {
        if (dataRetirada != null) {
            return String.format("%02d/%02d/%04d %02d:%02d", 
                dataRetirada.get(Calendar.DAY_OF_MONTH),
                dataRetirada.get(Calendar.MONTH) + 1,
                dataRetirada.get(Calendar.YEAR),
                dataRetirada.get(Calendar.HOUR_OF_DAY),
                dataRetirada.get(Calendar.MINUTE));
        }
        return "";
    }

    @Override
    public int hashCode() {
        return (idTermoItraconazol != null) ? idTermoItraconazol.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        TermoItraconazol other = (TermoItraconazol) obj;
        return idTermoItraconazol != null && idTermoItraconazol.equals(other.idTermoItraconazol);
    }

    @Override
    public String toString() {
        return "TermoItraconazol [idTermoItraconazol=" + idTermoItraconazol + 
               ", animal=" + (animal != null ? animal.getNome() : "null") + 
               ", quantidadeRetirada=" + quantidadeRetirada + 
               ", dataRetirada=" + getDataRetiradaFormatada() + "]";
    }
}