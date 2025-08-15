package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.repository.ProprietarioDAO;

@Named
@ViewScoped
public class HistoricoClienteBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Proprietario clienteSelecionado;

    @Inject
    private ProprietarioDAO proprietarioDAO;

    private String termoBusca;
    public void pesquisar() {
        if (termoBusca != null && !termoBusca.trim().isEmpty()) {
            proprietarioDAO.buscarPorNome(termoBusca.trim());
        } else {
            proprietarioDAO.buscarTodos();
        }
    }

    public List<Proprietario> getClientes() {
        if (termoBusca != null && !termoBusca.trim().isEmpty()) {
            return proprietarioDAO.buscarPorNome(termoBusca);
        }
        return proprietarioDAO.buscarTodos();
    }

    public Proprietario getClienteSelecionado() {
        return clienteSelecionado;
    }

    public void setClienteSelecionado(Proprietario clienteSelecionado) {
        this.clienteSelecionado = clienteSelecionado;
    }

    public String selecionarCliente() {
        return "historicoAnimaisCliente.xhtml?faces-redirect=true&clienteId=" + clienteSelecionado.getIdProprietario();
    }

    public String getTermoBusca() {
        return termoBusca;
    }

    public void setTermoBusca(String termoBusca) {
        this.termoBusca = termoBusca;
    }
}

