package br.gov.pr.guaira.animalys.dao;

import br.gov.pr.guaira.animalys.entity.Cliente;
import br.gov.pr.guaira.animalys.util.cdi.jpa.JPAUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class ClienteDAO {
    public List<Cliente> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }
}