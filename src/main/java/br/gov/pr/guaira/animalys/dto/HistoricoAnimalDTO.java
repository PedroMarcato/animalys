package br.gov.pr.guaira.animalys.dto;


public class HistoricoAnimalDTO {

    private String nomeAnimal;
    private String data;
    private String procedimento;
    private String responsavel;
    private String tipoAtendimento;
    private String diagnostico;
    private String modalidadeSolicitante;
    private String status;
    private Integer idAtendimento;

    // Construtor exigido pelo JPQL
    public HistoricoAnimalDTO(Integer idAtendimento, String data, String procedimento, String responsavel, String nomeAnimal, String tipoAtendimento, String diagnostico, String modalidadeSolicitante, String status) {
    	this.idAtendimento = idAtendimento;
    	this.data = (data != null) ? data : "Sem registros";
        this.procedimento = (procedimento != null) ? procedimento : "CONSULTA NORMAL";
        this.responsavel = (responsavel != null) ? responsavel : "Sem registros";
        this.nomeAnimal = (nomeAnimal != null) ? nomeAnimal : "Sem registros";
        this.tipoAtendimento = (tipoAtendimento != null) ? tipoAtendimento : "Sem registros";
        this.diagnostico = (diagnostico != null) ? diagnostico : "Sem registros";
        this.modalidadeSolicitante = (modalidadeSolicitante != null) ? modalidadeSolicitante : "Sem registros";
        this.status = (status != null) ? status : "Sem registros";
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
}
