package br.gov.pr.guaira.animalys.util.cdi.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("animalysPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
