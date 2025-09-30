package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.RetiradaMedicamento;
import br.gov.pr.guaira.animalys.repository.RetiradasMedicamento;

@Named
@ViewScoped
public class PesquisaRetiradaMedicamentoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RetiradasMedicamento retiradas;

    private List<RetiradaMedicamento> retiradasFiltradas;
    private String cpfProprietario;
    private String nomeAnimal;
    private String nomeMedicamento;

    @PostConstruct
    public void inicializar() {
        this.cpfProprietario = "";
        this.nomeAnimal = "";
        this.nomeMedicamento = "";
        // Carrega todas as retiradas automaticamente ao abrir a página
        pesquisar();
    }

    public void pesquisar() {
        // Se todos os filtros estão vazios, busca todas as retiradas
        if (isAllFiltersEmpty()) {
            this.retiradasFiltradas = retiradas.todas();
        } else {
            // Aplica os filtros informados
            this.retiradasFiltradas = retiradas.filtrar(cpfProprietario, nomeAnimal, nomeMedicamento);
        }
    }

    /**
     * Verifica se todos os filtros estão vazios
     */
    private boolean isAllFiltersEmpty() {
        return (cpfProprietario == null || cpfProprietario.trim().isEmpty()) &&
               (nomeAnimal == null || nomeAnimal.trim().isEmpty()) &&
               (nomeMedicamento == null || nomeMedicamento.trim().isEmpty());
    }

    // Getters e Setters
    public List<RetiradaMedicamento> getRetiradasFiltradas() {
        return retiradasFiltradas;
    }

    public void setRetiradasFiltradas(List<RetiradaMedicamento> retiradasFiltradas) {
        this.retiradasFiltradas = retiradasFiltradas;
    }

    public String getCpfProprietario() {
        return cpfProprietario;
    }

    public void setCpfProprietario(String cpfProprietario) {
        this.cpfProprietario = cpfProprietario;
    }

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }
}