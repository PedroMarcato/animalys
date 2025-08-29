package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import br.gov.pr.guaira.animalys.entity.Tratamento;

@Named
public class Tratamentos implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    public List<Tratamento> tratamentosCadastrados() {
        TypedQuery<Tratamento> query = em.createQuery("SELECT t FROM Tratamento t ORDER BY t.nome", Tratamento.class);
        return query.getResultList();
    }

    public Tratamento salvar(Tratamento tratamento) {
        System.out.println("DEBUG: EntityManager em = " + em);
        if (em == null) {
            throw new IllegalStateException("EntityManager está nulo!");
        }
        javax.persistence.EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (tratamento.getIdTratamento() == null) {
                em.persist(tratamento);
            } else {
                tratamento = em.merge(tratamento);
            }
            em.flush();
            em.clear();
            tx.commit();
            return tratamento;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void remover(Tratamento tratamento) {
        javax.persistence.EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Tratamento t = em.find(Tratamento.class, tratamento.getIdTratamento());
            if (t != null) {
                em.remove(t);
            }
            em.flush();
            em.clear();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }
    public Tratamento porId(Integer id) {
        return em.find(Tratamento.class, id);
    }
}
