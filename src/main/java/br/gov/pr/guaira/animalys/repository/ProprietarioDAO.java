package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.gov.pr.guaira.animalys.entity.Proprietario;

public class ProprietarioDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    public List<Proprietario> buscarTodos() {
        return em.createQuery("FROM Proprietario", Proprietario.class).getResultList();
    }

    public Proprietario porId(Integer id) {
        return em.find(Proprietario.class, id);
    }
    
    public List<Proprietario> buscarPorNome(String nome) {
        return em.createQuery("FROM Proprietario p WHERE UPPER(p.nome) LIKE :nome ORDER BY p.nome", Proprietario.class)
                 .setParameter("nome", "%" + nome.toUpperCase() + "%")
                 .getResultList();
    }

}
