package br.gov.pr.guaira.animalys.report;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class FichaClinicaReportBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private FichaClinicaReportService reportService;

    private Integer idAnimal;

    public void setIdAnimal(Integer idAnimal) {
        this.idAnimal = idAnimal;
    }

    public Integer getIdAnimal() {
        return idAnimal;
    }

    public void imprimirFichaClinica() {
        try {
            // Criar uma lista com o ID do animal (ou dados reais se disponível)
            java.util.List<Integer> parameters = new java.util.ArrayList<>();
            parameters.add(idAnimal);
            reportService.gerarRelatorio(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            javax.faces.context.FacesContext.getCurrentInstance().addMessage(null,
                new javax.faces.application.FacesMessage(javax.faces.application.FacesMessage.SEVERITY_ERROR,
                    "Erro", "Erro ao gerar relatório: " + e.getMessage()));
        }
    }
}