package br.gov.pr.guaira.animalys.dto;

import java.io.Serializable;

public class ProfissionalSelectDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer idProfissional;
    private String nome;
    
    public ProfissionalSelectDTO() {
    }
    
    public ProfissionalSelectDTO(Integer idProfissional, String nome) {
        this.idProfissional = idProfissional;
        this.nome = nome;
    }
    
    public Integer getIdProfissional() {
        return idProfissional;
    }
    
    public void setIdProfissional(Integer idProfissional) {
        this.idProfissional = idProfissional;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ProfissionalSelectDTO that = (ProfissionalSelectDTO) obj;
        return idProfissional != null ? idProfissional.equals(that.idProfissional) : that.idProfissional == null;
    }
    
    @Override
    public int hashCode() {
        return idProfissional != null ? idProfissional.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return nome;
    }
}
