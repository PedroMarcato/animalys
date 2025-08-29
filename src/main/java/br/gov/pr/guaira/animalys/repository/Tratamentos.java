package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import br.gov.pr.guaira.animalys.entity.Tratamento;

public class Tratamentos implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    public List<Tratamento> tratamentosCadastrados() {
        TypedQuery<Tratamento> query = em.createQuery("SELECT t FROM Tratamento t ORDER BY t.nome", Tratamento.class);
        return query.getResultList();
    }
}
