package br.gov.pr.guaira.animalys.dto;

import java.util.List;
import br.gov.pr.guaira.animalys.entity.ItemLoteAtendimento;

public class HistoricoAnimalDTO {

    // Construtor exato exigido pelo Hibernate/HQL
    public HistoricoAnimalDTO(int idAtendimento, String data, String procedimento, String responsavel, String nomeAnimal, String tipoAtendimento, String diagnostico, String modalidadeSolicitante, br.gov.pr.guaira.animalys.entity.Status status,
                              String idade, String sexo, String cor, String raca, String especie, String nomeProprietario, String cpfProprietario, String enderecoProprietario, String numeroEndereco, String bairroProprietario, String contatoProprietario, java.util.Calendar dataEntrada, String fotoAnimal,
                              String escoreCorporal, double peso, String fc, String fr, String temperatura, String tratamento, Integer idAnimal) {
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
        this.idAnimal = idAnimal;
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
    private Integer idAnimal; // ID do animal para o relatório

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

    // Novo campo: indica se o animal é castrado
    private Boolean castrado;

    // Lista de medicamentos utilizados no atendimento
    private List<ItemLoteAtendimento> medicamentos;
    // Getter para exibir "Sim" ou "Não" para castrado
    public String getCastradoStr() {
        if (castrado == null) return "";
        return castrado ? "Sim" : "Não";
    }

    public Boolean getCastrado() {
        return castrado;
    }

    public void setCastrado(Boolean castrado) {
        this.castrado = castrado;
    }

    // Construtor exigido pelo JPQL
    public HistoricoAnimalDTO(Integer idAtendimento, String data, String procedimento, String responsavel, String nomeAnimal, String tipoAtendimento, String diagnostico, String modalidadeSolicitante, String status,
                              String idade, String sexo, String cor, String raca, String especie, String nomeProprietario, String cpfProprietario, String enderecoProprietario, String numeroEndereco, String bairroProprietario, String contatoProprietario, java.util.Calendar dataEntrada, String fotoAnimal,
                              String numeroMicrochip, String escoreCorporal, Double peso, String fc, String fr, String temperatura, String tratamento, Integer idAnimal) {
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
    this.idAnimal = idAnimal;
    } 


    // Construtor anterior (opcional)
   /* public HistoricoAnimalDTO(Calendar data, String procedimento, String responsavel, String nomeAnimal) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.data = (data != null) ? sdf.format(data.getTime()) : "";
        this.procedimento = (procedimento != null) ? procedimento : "-";
        this.responsavel = (responsavel != null) ? responsavel : "-";
        this.nomeAnimal = (nomeAnimal != null) ? nomeAnimal : "-";
    } */

    // Getters com tratamento de valores nulos
    public String getNomeAnimal() { return nomeAnimal != null ? nomeAnimal : ""; }
    public String getData() { return data != null ? data : "Data não informada"; }
    public String getProcedimento() { return procedimento != null && !procedimento.trim().isEmpty() ? procedimento : ""; }
    public String getResponsavel() { return responsavel != null ? responsavel : ""; }
    public String getTipoAtendimento() { return tipoAtendimento != null && !tipoAtendimento.trim().isEmpty() ? tipoAtendimento : "Não informado"; }
    public String getDiagnostico() { return diagnostico != null && !diagnostico.trim().isEmpty() ? diagnostico : "Não informado"; }
    public String getTipoSolicitacao() { return modalidadeSolicitante != null ? modalidadeSolicitante : ""; }
    public String getStatusSolicitacao() { return status != null ? status : ""; }
    public Integer getIdAtendimento() { return idAtendimento; }       

    // Getters para os novos campos com tratamento de nulos
    public String getIdade() { return idade != null ? idade : ""; }
    public String getSexo() { return sexo != null ? sexo : ""; }
    public String getCor() { return cor != null && !cor.trim().isEmpty() ? cor : ""; }
    public String getRaca() { return raca != null ? raca : ""; }
    public String getEspecie() { return especie != null ? especie : ""; }
    public String getNomeProprietario() { return nomeProprietario != null ? nomeProprietario : ""; }
    public String getCpfProprietario() { return cpfProprietario != null ? cpfProprietario : ""; }
    public String getEnderecoProprietario() { return enderecoProprietario != null ? enderecoProprietario : ""; }
    public String getNumeroEndereco() { return numeroEndereco != null ? numeroEndereco : ""; }
    public String getBairroProprietario() { return bairroProprietario != null ? bairroProprietario : ""; }
    public String getContatoProprietario() { return contatoProprietario != null ? contatoProprietario : ""; }
    public java.util.Calendar getDataEntrada() { return dataEntrada; }
    public String getFotoAnimal() { return fotoAnimal; }
    public String getNumeroMicrochip() { return numeroMicrochip != null ? numeroMicrochip : ""; }
    public String getEscoreCorporal() { return escoreCorporal != null ? escoreCorporal : "0.0"; }
    public Double getPeso() { return peso != null ? peso : 0.0; }
    public String getFc() { return fc != null && !fc.trim().isEmpty() ? fc + " bpm" : "0"; }
    public String getFr() { return fr != null && !fr.trim().isEmpty() ? fr + " irpm" : "0"; }
    public String getTemperatura() { return temperatura != null && !temperatura.trim().isEmpty() ? temperatura + " °C" : "0"; }
    public String getTratamento() { return tratamento != null ? tratamento : ""; }
    
    // Getters específicos para o JRXML com tratamento e formatação
    public String getDataEntradaFormatada() {
        if (dataEntrada != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(dataEntrada.getTime());
        }
        return "";
    }
    
    
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

    // Getter e Setter para idAnimal
    public Integer getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(Integer idAnimal) {
        this.idAnimal = idAnimal;
    }
}
