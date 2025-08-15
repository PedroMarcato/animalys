package br.gov.pr.guaira.animalys.entity;

import java.io.Serializable;
import java.util.Calendar;

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
@Table(name = "foto_animal", schema = "animal")
public class FotoAnimal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "caminho_arquivo", nullable = false, length = 500)
    private String nomeArquivo;

    @Column(name = "nome_original", nullable = false, length = 255)
    private String nomeOriginal;

    @Column(name = "tipo_mime", nullable = false, length = 100)
    private String tipoMime;

    @Column(nullable = false)
    private Long tamanho;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_upload")
    private Calendar dataUpload;

    @Column(nullable = false)
    private boolean principal = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    // Construtores
    public FotoAnimal() {
        this.dataUpload = Calendar.getInstance();
    }

    public FotoAnimal(String nomeArquivo, String nomeOriginal, String tipoMime, Long tamanho, Animal animal) {
        this();
        this.nomeArquivo = nomeArquivo;
        this.nomeOriginal = nomeOriginal;
        this.tipoMime = tipoMime;
        this.tamanho = tamanho;
        this.animal = animal;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getNomeOriginal() {
        return nomeOriginal;
    }

    public void setNomeOriginal(String nomeOriginal) {
        this.nomeOriginal = nomeOriginal;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }

    public Calendar getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(Calendar dataUpload) {
        this.dataUpload = dataUpload;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    // Métodos utilitários
    public String getTamanhoFormatado() {
        if (tamanho == null) return "0 KB";
        
        double bytes = tamanho.doubleValue();
        if (bytes < 1024) return String.format("%.0f B", bytes);
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024);
        return String.format("%.1f MB", bytes / (1024 * 1024));
    }

    // Método utilitário para retornar apenas o nome do arquivo (sem o caminho)
    public String getNomeArquivoSimples() {
        if (nomeArquivo == null) return null;
        int lastSlashIndex = nomeArquivo.lastIndexOf("/");
        if (lastSlashIndex == -1) {
            return nomeArquivo; // Sem barra, retorna o nome completo
        } else {
            return nomeArquivo.substring(lastSlashIndex + 1);
        }
    }

    // hashCode, equals e toString
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        FotoAnimal other = (FotoAnimal) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "FotoAnimal [id=" + id + ", nomeOriginal=" + nomeOriginal + 
               ", tamanho=" + getTamanhoFormatado() + ", principal=" + principal + "]";
    }
}