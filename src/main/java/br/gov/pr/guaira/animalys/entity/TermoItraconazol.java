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

    // Quantidades correspondentes aos meses (compatibilidade com a view)
    @Column(name = "quantidade_1_mes")
    private Integer quantidade1Mes;

    @Column(name = "quantidade_2_mes")
    private Integer quantidade2Mes;

    @Column(name = "quantidade_3_mes")
    private Integer quantidade3Mes;

    @Column(name = "quantidade_4_mes")
    private Integer quantidade4Mes;

    @Column(name = "quantidade_5_mes")
    private Integer quantidade5Mes;

    @Column(name = "quantidade_6_mes")
    private Integer quantidade6Mes;

    @Column(name = "quantidade_7_mes")
    private Integer quantidade7Mes;

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

    public Integer getQuantidade1Mes() {
        return quantidade1Mes;
    }

    public void setQuantidade1Mes(Integer quantidade1Mes) {
        this.quantidade1Mes = quantidade1Mes;
    }

    public Integer getQuantidade2Mes() {
        return quantidade2Mes;
    }

    public void setQuantidade2Mes(Integer quantidade2Mes) {
        this.quantidade2Mes = quantidade2Mes;
    }

    public Integer getQuantidade3Mes() {
        return quantidade3Mes;
    }

    public void setQuantidade3Mes(Integer quantidade3Mes) {
        this.quantidade3Mes = quantidade3Mes;
    }

    public Integer getQuantidade4Mes() {
        return quantidade4Mes;
    }

    public void setQuantidade4Mes(Integer quantidade4Mes) {
        this.quantidade4Mes = quantidade4Mes;
    }

    public Integer getQuantidade5Mes() {
        return quantidade5Mes;
    }

    public void setQuantidade5Mes(Integer quantidade5Mes) {
        this.quantidade5Mes = quantidade5Mes;
    }

    public Integer getQuantidade6Mes() {
        return quantidade6Mes;
    }

    public void setQuantidade6Mes(Integer quantidade6Mes) {
        this.quantidade6Mes = quantidade6Mes;
    }

    public Integer getQuantidade7Mes() {
        return quantidade7Mes;
    }

    public void setQuantidade7Mes(Integer quantidade7Mes) {
        this.quantidade7Mes = quantidade7Mes;
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

    /**
     * Retorna a data da primeira retirada (menor data entre data1Mes..data7Mes).
     * Se não houver nenhuma data mensal preenchida, retorna a dataRetirada (quando o termo foi criado) se houver.
     */
    public Date getPrimeiraRetirada() {
        Date[] datas = new Date[] { data1Mes, data2Mes, data3Mes, data4Mes, data5Mes, data6Mes, data7Mes };
        Date primeira = null;
        for (Date d : datas) {
            if (d != null) {
                if (primeira == null || d.before(primeira)) {
                    primeira = d;
                }
            }
        }
        if (primeira != null) {
            return primeira;
        }
        if (dataRetirada != null) {
            return dataRetirada.getTime();
        }
        return null;
    }

    /**
     * Retorna a quantidade da última retirada (a quantidade relacionada à maior data entre data1Mes..data7Mes).
     * Se não houver datas mensais, retorna a quantidadeRetirada (campo legado) como fallback.
     */
    public Integer getQuantidadeUltimaRetirada() {
        Date[] datas = new Date[] { data1Mes, data2Mes, data3Mes, data4Mes, data5Mes, data6Mes, data7Mes };
        Integer[] qts = new Integer[] { quantidade1Mes, quantidade2Mes, quantidade3Mes, quantidade4Mes, quantidade5Mes, quantidade6Mes, quantidade7Mes };

        Date ultima = null;
        Integer qtUltima = null;
        for (int i = 0; i < datas.length; i++) {
            Date d = datas[i];
            if (d != null) {
                if (ultima == null || d.after(ultima)) {
                    ultima = d;
                    qtUltima = qts[i];
                }
            }
        }
        if (qtUltima != null) {
            return qtUltima;
        }
        // fallback para campo legado
        return quantidadeRetirada;
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