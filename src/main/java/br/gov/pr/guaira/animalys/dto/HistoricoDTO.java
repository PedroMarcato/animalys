package br.gov.pr.guaira.animalys.dto;

public class HistoricoDTO {
  private String nomeAnimal;
  private String data;
  private String procedimento;
  private String responsavel;

  public HistoricoDTO(String nomeAnimal, String data, String procedimento, String responsavel) {
    this.nomeAnimal = nomeAnimal;
    this.data = data;
    this.procedimento = procedimento;
    this.responsavel = responsavel;
  }

  // Getters e Setters
  public String getNomeAnimal() { return nomeAnimal; }
  public void setNomeAnimal(String nomeAnimal) { this.nomeAnimal = nomeAnimal; }

  public String getData() { return data; }
  public void setData(String data) { this.data = data; }

  public String getProcedimento() { return procedimento; }
  public void setProcedimento(String procedimento) { this.procedimento = procedimento; }

  public String getResponsavel() { return responsavel; }
  public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
}
