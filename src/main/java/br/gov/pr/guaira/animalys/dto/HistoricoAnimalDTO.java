package br.gov.pr.guaira.animalys.dto;

import java.util.List;
import br.gov.pr.guaira.animalys.entity.ItemLoteAtendimento;

public class HistoricoAnimalDTO {

    // Construtor exato exigido pelo Hibernate/HQL
    public HistoricoAnimalDTO(int idAtendimento, String data, String procedimento, String responsavel, String nomeAnimal, String tipoAtendimento, String diagnostico, String modalidadeSolicitante, br.gov.pr.guaira.animalys.entity.Status status,
                              String idade, String sexo, String cor, String raca, String especie, String nomeProprietario, String cpfProprietario, String enderecoProprietario, String numeroEndereco, String bairroProprietario, String contatoProprietario, java.util.Calendar dataEntrada, String fotoAnimal,
                              String escoreCorporal, double peso, String fc, String fr, String temperatura, String tratamento) {
        this.idAtendimento = idAtendimento;
        this.data = data;
        this.procedimento = procedimento;
        this.responsavel = responsavel;
        this.nomeAnimal = nomeAnimal;
        this.tipoAtendimento = tipoAtendimento;
        this.diagnostico = diagnostico;
        this.modalidadeSolicitante = modalidadeSolicitante;
        this.status = (status != null) ? status.toString() : null;
        this.idade = idade;
        this.sexo = sexo;
        this.cor = cor;
        this.raca = raca;
        this.especie = especie;
        this.nomeProprietario = nomeProprietario;
        this.cpfProprietario = cpfProprietario;
        this.enderecoProprietario = enderecoProprietario;
        this.numeroEndereco = numeroEndereco;
        this.bairroProprietario = bairroProprietario;
        this.contatoProprietario = contatoProprietario;
        this.dataEntrada = dataEntrada;
        this.fotoAnimal = fotoAnimal;
        this.escoreCorporal = escoreCorporal;
        this.peso = peso;
        this.fc = fc;
        this.fr = fr;
        this.temperatura = temperatura;
        this.tratamento = tratamento;
    }
    private String nomeAnimal;
    private String data;
    private String procedimento;
    private String responsavel;
    private String tipoAtendimento;
    private String diagnostico;
    private String modalidadeSolicitante;
    private String status;
    private Integer idAtendimento;

    // Novos campos para ficha clínica
    private String idade;
    private String sexo;
    private String cor;
    private String raca;
    private String especie;
    private String nomeProprietario;
    private String cpfProprietario;
    private String enderecoProprietario;
    private String numeroEndereco;
    private String bairroProprietario;
    private String contatoProprietario;
    private java.util.Calendar dataEntrada;
    private String fotoAnimal;
    private String numeroMicrochip;
    private String escoreCorporal;
    private Double peso;
    private String fc;
    private String fr;
    private String temperatura;
    private String tratamento;
    
    // Lista de medicamentos utilizados no atendimento
    private List<ItemLoteAtendimento> medicamentos;

    // Construtor exigido pelo JPQL
    public HistoricoAnimalDTO(Integer idAtendimento, String data, String procedimento, String responsavel, String nomeAnimal, String tipoAtendimento, String diagnostico, String modalidadeSolicitante, String status,
                              String idade, String sexo, String cor, String raca, String especie, String nomeProprietario, String cpfProprietario, String enderecoProprietario, String numeroEndereco, String bairroProprietario, String contatoProprietario, java.util.Calendar dataEntrada, String fotoAnimal,
                              String numeroMicrochip, String escoreCorporal, Double peso, String fc, String fr, String temperatura, String tratamento) {
        this.idAtendimento = idAtendimento;
        this.data = (data != null) ? data : "Sem registros";
        this.procedimento = (procedimento != null) ? procedimento : "Consulta Clínica";
        this.responsavel = (responsavel != null) ? responsavel : "Sem registros";
        this.nomeAnimal = (nomeAnimal != null) ? nomeAnimal : "Sem registros";
        this.tipoAtendimento = (tipoAtendimento != null) ? tipoAtendimento : "Sem registros";
        this.diagnostico = (diagnostico != null) ? diagnostico : "Sem registros";
        this.modalidadeSolicitante = (modalidadeSolicitante != null) ? modalidadeSolicitante : "Sem registros";
        this.status = (status != null) ? status : "Sem registros";
        // Novos campos
        this.idade = idade;
        this.sexo = sexo;
        this.cor = cor;
        this.raca = raca;
        this.especie = especie;
        this.nomeProprietario = nomeProprietario;
        this.cpfProprietario = cpfProprietario;
        this.enderecoProprietario = enderecoProprietario;
        this.numeroEndereco = numeroEndereco;
        this.bairroProprietario = bairroProprietario;
        this.contatoProprietario = contatoProprietario;
    this.dataEntrada = dataEntrada;
    this.fotoAnimal = fotoAnimal;
    this.numeroMicrochip = numeroMicrochip;
    this.escoreCorporal = escoreCorporal;
    this.peso = peso;
    this.fc = fc;
    this.fr = fr;
    this.temperatura = temperatura;
    this.tratamento = tratamento;
    } 


    // Construtor anterior (opcional)
   /* public HistoricoAnimalDTO(Calendar data, String procedimento, String responsavel, String nomeAnimal) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.data = (data != null) ? sdf.format(data.getTime()) : "";
        this.procedimento = (procedimento != null) ? procedimento : "-";
        this.responsavel = (responsavel != null) ? responsavel : "-";
        this.nomeAnimal = (nomeAnimal != null) ? nomeAnimal : "-";
    } */

    // Getters
    public String getNomeAnimal() { return nomeAnimal; }
    public String getData() { return data; }
    public String getProcedimento() { return procedimento; }
    public String getResponsavel() { return responsavel; }
    public String getTipoAtendimento() { return tipoAtendimento; }
    public String getDiagnostico() { return diagnostico; }
    public String getTipoSolicitacao() { return modalidadeSolicitante; }
    public String getStatusSolicitacao() { return status; }
    public Integer getIdAtendimento() { return idAtendimento; }       

    // Getters para os novos campos
    public String getIdade() { return idade; }
    public String getSexo() { return sexo; }
    public String getCor() { return cor; }
    public String getRaca() { return raca; }
    public String getEspecie() { return especie; }
    public String getNomeProprietario() { return nomeProprietario; }
    public String getCpfProprietario() { return cpfProprietario; }
    public String getEnderecoProprietario() { return enderecoProprietario; }
    public String getNumeroEndereco() { return numeroEndereco; }
    public String getBairroProprietario() { return bairroProprietario; }
    public String getContatoProprietario() { return contatoProprietario; }
    public java.util.Calendar getDataEntrada() { return dataEntrada; }
    public String getFotoAnimal() { return fotoAnimal; }
    public String getNumeroMicrochip() { return numeroMicrochip; }
    public String getEscoreCorporal() { return escoreCorporal; }
    public Double getPeso() { return peso; }
    public String getFc() { return fc; }
    public String getFr() { return fr; }
    public String getTemperatura() { return temperatura; }
    public String getTratamento() { return tratamento; }
    
    public List<ItemLoteAtendimento> getMedicamentos() { return medicamentos; }
    public void setMedicamentos(List<ItemLoteAtendimento> medicamentos) { this.medicamentos = medicamentos; }
    
    // Métodos auxiliares para facilitar o acesso aos dados dos medicamentos na página
    public String getNomeProduto(ItemLoteAtendimento item) {
        return item != null && item.getLote() != null && item.getLote().getProduto() != null 
            ? item.getLote().getProduto().getNome() : "";
    }
    
    public String getNumeroLote(ItemLoteAtendimento item) {
        return item != null && item.getLote() != null 
            ? item.getLote().getNumeroLote() : "";
    }
    
    public String getDataValidadeFormatada(ItemLoteAtendimento item) {
        if (item != null && item.getLote() != null && item.getLote().getValidade() != null) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                return sdf.format(item.getLote().getValidade().getTime());
            } catch (Exception e) {
                return "Data inválida";
            }
        }
        return "";
    }
    
    public Integer getQuantidadeMedicamento(ItemLoteAtendimento item) {
        return item != null ? item.getQuantidade() : 0;
    }
}
