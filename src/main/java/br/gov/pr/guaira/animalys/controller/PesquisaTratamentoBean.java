package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Tratamento;
import br.gov.pr.guaira.animalys.service.TratamentoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaTratamentoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    public PesquisaTratamentoBean() {
        this.tratamentoFilter = new Tratamento();
    }

    private Tratamento tratamentoFilter;
    private Tratamento tratamentoSelecionado;
    private List<Tratamento> tratamentosFiltrados;
    @Inject
    private TratamentoService tratamentoService;

    public Tratamento getTratamentoFilter() {
        return tratamentoFilter;
    }
    public void setTratamentoFilter(Tratamento tratamentoFilter) {
        this.tratamentoFilter = tratamentoFilter;
    }
    public List<Tratamento> getTratamentosFiltrados() {
        return tratamentosFiltrados;
    }
    public void setTratamentosFiltrados(List<Tratamento> tratamentosFiltrados) {
        this.tratamentosFiltrados = tratamentosFiltrados;
    }
    public Tratamento getTratamentoSelecionado() {
        return tratamentoSelecionado;
    }
    public void setTratamentoSelecionado(Tratamento tratamentoSelecionado) {
        this.tratamentoSelecionado = tratamentoSelecionado;
    }
    public void pesquisar() {
        // Exemplo simples: filtra por nome e sigla se preenchidos
        this.tratamentosFiltrados = tratamentoService.tratamentosCadastrados();
        if(tratamentoFilter.getNome() != null && !tratamentoFilter.getNome().isEmpty()) {
            this.tratamentosFiltrados.removeIf(t -> !t.getNome().toLowerCase().contains(tratamentoFilter.getNome().toLowerCase()));
        }
        if(tratamentoFilter.getSigla() != null && !tratamentoFilter.getSigla().isEmpty()) {
            this.tratamentosFiltrados.removeIf(t -> !t.getSigla().toLowerCase().contains(tratamentoFilter.getSigla().toLowerCase()));
        }
    }
    public void excluir() {
        tratamentoService.remover(tratamentoSelecionado);
        this.tratamentosFiltrados.remove(tratamentoSelecionado);
        FacesUtil.addInfoMessage("Tratamento excluído com sucesso!");
    }
}
