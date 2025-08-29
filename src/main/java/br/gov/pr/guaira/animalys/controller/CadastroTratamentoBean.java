package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Tratamento;
import br.gov.pr.guaira.animalys.service.TratamentoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroTratamentoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idTratamento; // Recebe o parâmetro da URL
    private Tratamento tratamento;

    @Inject
    private TratamentoService tratamentoService;

    public CadastroTratamentoBean() {
        this.tratamento = new Tratamento();
    }

    public void inicializar() {
        if (idTratamento != null && idTratamento > 0) {
            this.tratamento = tratamentoService.porId(idTratamento);
            if (this.tratamento == null) {
                this.tratamento = new Tratamento();
            }
        }
    }

    public Integer getIdTratamento() {
        return idTratamento;
    }
    public void setIdTratamento(Integer idTratamento) {
        this.idTratamento = idTratamento;
    }

    public Tratamento getTratamento() {
        return tratamento;
    }
    public void setTratamento(Tratamento tratamento) {
        this.tratamento = tratamento;
    }

    public void salvar() {
        tratamentoService.salvar(tratamento);
        FacesUtil.addInfoMessage("Tratamento salvo com sucesso!");
        limpar();
    }

    public void limpar() {
        this.tratamento = new Tratamento();
    }
}
