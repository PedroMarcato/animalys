package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import br.gov.pr.guaira.animalys.entity.Tratamento;
import br.gov.pr.guaira.animalys.repository.Tratamentos;

@Named
public class TratamentoService implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private Tratamentos tratamentos;

    @Transactional
    public Tratamento salvar(Tratamento tratamento) {
        return tratamentos.salvar(tratamento);
    }

    @Transactional
    public void remover(Tratamento tratamento) {
        tratamentos.remover(tratamento);
    }

    public java.util.List<Tratamento> tratamentosCadastrados() {
        return tratamentos.tratamentosCadastrados();
    }

    public Tratamento porId(Integer id) {
        return tratamentos.porId(id);
    }
}
