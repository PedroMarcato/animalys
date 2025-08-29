package br.gov.pr.guaira.animalys.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "tratamento", schema = "atendimento")
public class Tratamento implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tratamento")
    private Integer idTratamento;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 20)
    private String sigla;

    // Getters e Setters
    public Integer getIdTratamento() {
        return idTratamento;
    }
    public void setIdTratamento(Integer idTratamento) {
        this.idTratamento = idTratamento;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getSigla() {
        return sigla;
    }
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
