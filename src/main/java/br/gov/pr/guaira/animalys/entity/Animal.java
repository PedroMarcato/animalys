

package br.gov.pr.guaira.animalys.entity;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(schema = "animal")
public class Animal implements Serializable {
  // ...existing code...


  public String getNome() {
    return nome;
  }

  public String getCor() {
    return cor;
  }

  public void setCor(String cor) {
    this.cor = cor;
  }
  @Column(name = "cor")
  private String cor;

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer idAnimal;

  @Column
  private String nome;

  @Column
  private String numeroMicrochip;


  @Column
  private String idade;

  @Temporal(TemporalType.TIMESTAMP)
  private Calendar dataEntrada;

  @Temporal(TemporalType.TIMESTAMP)
  private Calendar dataSaida;

  @Column
  private boolean castrado;

  @Column
  private boolean vacinado;

  @Column(length = 5000)
  private String qualVacina;

  @Column
  private boolean desmifurgado;

  @Column
  private boolean visitado;

  @Enumerated(EnumType.STRING)
  private Sexo sexo;

  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  @JoinColumn(name = "proprietario", referencedColumnName = "idProprietario")
  private Proprietario proprietario;

  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  @JoinColumn(name = "raca", referencedColumnName = "idRaca")
  private Raca raca;

  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  @JoinColumn(name = "especie", referencedColumnName = "idEspecie") // Assumindo que a chave primária em Especie é 'idEspecie'
  private Especie especie;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Temporal(TemporalType.TIMESTAMP)
  private Calendar dataCastracao;

  @Temporal(TemporalType.TIMESTAMP)
  private Calendar dataAgendaCastracao;

  @Column
  private String motivoCancelamentoCastracao;

  @Column(name = "foto")
  private String foto;

  
  @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<FotoAnimal> fotos = new ArrayList<>();
  
  // Getters e Setters


  
  public Integer getIdAnimal() {
    return idAnimal;
  }

  public String getIdade() {
    return idade;
  }

  public void setIdade(String idade) {
    this.idade = idade;
  }

  public Calendar getDataEntrada() {
    return dataEntrada;
  }

  public void setDataEntrada(Calendar dataEntrada) {
    this.dataEntrada = dataEntrada;
  }

  public Calendar getDataSaida() {
    return dataSaida;
  }

  public void setDataSaida(Calendar dataSaida) {
    this.dataSaida = dataSaida;
  }

  public boolean isCastrado() {
    return castrado;
  }

  public void setCastrado(boolean castrado) {
    this.castrado = castrado;
  }

  public boolean isVacinado() {
    return vacinado;
  }

  public void setVacinado(boolean vacinado) {
    this.vacinado = vacinado;
  }

  public String getQualVacina() {
    return qualVacina;
  }

  public void setQualVacina(String qualVacina) {
    this.qualVacina = qualVacina;
  }

  public boolean isDesmifurgado() {
    return desmifurgado;
  }

  public void setDesmifurgado(boolean desmifurgado) {
    this.desmifurgado = desmifurgado;
  }

  public boolean isVisitado() {
    return visitado;
  }

  public void setVisitado(boolean visitado) {
    this.visitado = visitado;
  }

  public Sexo getSexo() {
    return sexo;
  }

  public void setSexo(Sexo sexo) {
    this.sexo = sexo;
  }

  public Proprietario getProprietario() {
    return proprietario;
  }

  public void setProprietario(Proprietario proprietario) {
    this.proprietario = proprietario;
  }

  public Raca getRaca() {
    return raca;
  }

  public void setRaca(Raca raca) {
    this.raca = raca;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Calendar getDataCastracao() {
    return dataCastracao;
  }

  public void setDataCastracao(Calendar dataCastracao) {
    this.dataCastracao = dataCastracao;
  }

  public Calendar getDataAgendaCastracao() {
    return dataAgendaCastracao;
  }

  public void setDataAgendaCastracao(Calendar dataAgendaCastracao) {
    this.dataAgendaCastracao = dataAgendaCastracao;
  }

  public String getMotivoCancelamentoCastracao() {
    return motivoCancelamentoCastracao;
  }

  public void setMotivoCancelamentoCastracao(String motivoCancelamentoCastracao) {
    this.motivoCancelamentoCastracao = motivoCancelamentoCastracao;
  }

  public String getFoto() {
    return foto;
  }
  public void setFoto(String foto) {
    this.foto = foto;
  }



  
  public List<FotoAnimal> getFotos() {
	    return fotos;
	  }

	  public void setFotos(List<FotoAnimal> fotos) {
	    this.fotos = fotos;
	  }

	  // Métodos utilitários para fotos
	  public FotoAnimal getFotoPrincipal() {
	    return fotos.stream()
	        .filter(FotoAnimal::isPrincipal)
	        .findFirst()
	        .orElse(fotos.isEmpty() ? null : fotos.get(0));
	  }

      @Transient
      public boolean getTemFoto() {
        return !fotos.isEmpty();
      }

	  public void adicionarFoto(FotoAnimal foto) {
	    foto.setAnimal(this);
	    this.fotos.add(foto);
	  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((idAnimal == null) ? 0 : idAnimal.hashCode());
    result = prime * result + ((nome == null) ? 0 : nome.hashCode());
    result = prime * result + ((numeroMicrochip == null) ? 0 : numeroMicrochip.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Animal other = (Animal) obj;
    if (idAnimal == null) {
      if (other.idAnimal != null) return false;
    } else if (!idAnimal.equals(other.idAnimal)) return false;
    if (nome == null) {
      if (other.nome != null) return false;
    } else if (!nome.equals(other.nome)) return false;
    if (numeroMicrochip == null) {
      if (other.numeroMicrochip != null) return false;
    } else if (!numeroMicrochip.equals(other.numeroMicrochip)) return false;
    return true;
  }

  @Override
  public String toString() {
    return "Animal [idAnimal=" + idAnimal + ", nome=" + nome + ", numeroMicrochip=" + numeroMicrochip
  + ", dataEntrada=" + dataEntrada + ", dataSaida=" + dataSaida
        + ", castrado=" + castrado + ", vacinado=" + vacinado + ", desmifurgado=" + desmifurgado
        + ", visitado=" + visitado + "]";
  }
}