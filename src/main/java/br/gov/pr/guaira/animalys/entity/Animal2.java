package br.gov.pr.guaira.animalys.entity;

import javax.persistence.*;

@Entity
@Table(name = "animal2") // nome da tabela no banco
public class Animal2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente dono;

    // Construtor padrão (necessário para JPA)
    public Animal2() {
    }

    public Animal2(Long id, String nome, Cliente dono) {
        this.id = id;
        this.nome = nome;
        this.dono = dono;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public Cliente getDono() { return dono; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDono(Cliente dono) { this.dono = dono; }
}
