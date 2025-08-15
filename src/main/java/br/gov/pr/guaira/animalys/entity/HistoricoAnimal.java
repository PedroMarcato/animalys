package br.gov.pr.guaira.animalys.entity;

import javax.persistence.*;

@Entity
@Table(schema = "animal", name = "historico_animal")
public class HistoricoAnimal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String data;
    private String procedimento;
    private String responsavel;

    @ManyToOne
    @JoinColumn(name = "animal_id", referencedColumnName = "idAnimal")
    private Animal animal;

    // Getters e Setters
    public Long getId() { return id; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getProcedimento() { return procedimento; }
    public void setProcedimento(String procedimento) { this.procedimento = procedimento; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }
}
