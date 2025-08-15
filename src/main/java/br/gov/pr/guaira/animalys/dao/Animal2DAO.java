package br.gov.pr.guaira.animalys.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.gov.pr.guaira.animalys.entity.Animal2;
import br.gov.pr.guaira.animalys.util.cdi.jpa.JPAUtil;

public class Animal2DAO {

    public List<Animal2> buscarPorClienteId(Long clienteId) {
        EntityManager em = JPAUtil.getEntityManager();
        String jpql = "SELECT a FROM Animal2 a WHERE a.dono.id = :clienteId";
        TypedQuery<Animal2> query = em.createQuery(jpql, Animal2.class);
        query.setParameter("clienteId", clienteId);
        return query.getResultList();
    }
}
