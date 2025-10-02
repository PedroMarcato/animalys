package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.TermoItraconazol;
import br.gov.pr.guaira.animalys.repository.TermosItraconazol;
import br.gov.pr.guaira.animalys.service.TermoItraconazolService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaTermoItraconazolBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TermosItraconazol termos;
    
    @Inject
    private TermoItraconazolService termoItraconazolService;

    private List<TermoItraconazol> termosFiltrados;
    private String cpfProprietario;
    private String nomeAnimal;
    private String nomeMedicamento;
    private TermoItraconazol termoParaExcluir;

    @PostConstruct
    public void inicializar() {
        this.cpfProprietario = "";
        this.nomeAnimal = "";
        this.nomeMedicamento = "Itraconazol"; // Sempre Itraconazol
        // Carrega todos os termos automaticamente ao abrir a página
        pesquisar();
    }

    public void pesquisar() {
        // Se todos os filtros estão vazios, busca todos os termos
        if (isAllFiltersEmpty()) {
            this.termosFiltrados = termos.todos();
        } else {
            // Aplica os filtros informados
            this.termosFiltrados = termos.filtrar(cpfProprietario, nomeAnimal, nomeMedicamento);
        }
    }

    /**
     * Verifica se todos os filtros estão vazios
     */
    private boolean isAllFiltersEmpty() {
        return (cpfProprietario == null || cpfProprietario.trim().isEmpty()) &&
               (nomeAnimal == null || nomeAnimal.trim().isEmpty());
               // nomeMedicamento não entra na verificação pois é sempre "Itraconazol"
    }

    // Getters e Setters
    public List<TermoItraconazol> getTermosFiltrados() {
        return termosFiltrados;
    }

    public void setTermosFiltrados(List<TermoItraconazol> termosFiltrados) {
        this.termosFiltrados = termosFiltrados;
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
    
    public void prepararExclusao(TermoItraconazol termo) {
        this.termoParaExcluir = termo;
    }
    
    public void confirmarExclusao() {
        if (termoParaExcluir != null) {
            try {
                termoItraconazolService.excluir(termoParaExcluir);
                // Atualiza a lista após exclusão
                pesquisar();
                FacesUtil.addInfoMessage("Registro de retirada de Itraconazol excluído com sucesso!");
            } catch (Exception e) {
                FacesUtil.addErrorMessage("Erro ao excluir registro de retirada: " + e.getMessage());
            } finally {
                this.termoParaExcluir = null;
            }
        }
    }
    
    public void excluir(TermoItraconazol termo) {
        try {
            termoItraconazolService.excluir(termo);
            // Atualiza a lista após exclusão
            pesquisar();
            FacesUtil.addInfoMessage("Registro de retirada de Itraconazol excluído com sucesso!");
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro ao excluir registro de retirada: " + e.getMessage());
        }
    }
}