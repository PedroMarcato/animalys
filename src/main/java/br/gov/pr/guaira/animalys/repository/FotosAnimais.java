package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.FotoAnimal;

public class FotosAnimais implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager manager;

    public FotoAnimal guardar(FotoAnimal fotoAnimal) {
        return manager.merge(fotoAnimal);
    }

    public void remover(FotoAnimal fotoAnimal) {
        fotoAnimal = porId(fotoAnimal.getId());
        manager.remove(fotoAnimal);
    }

    public FotoAnimal porId(Long id) {
        return manager.find(FotoAnimal.class, id);
    }

    public List<FotoAnimal> porAnimal(Animal animal) {
        TypedQuery<FotoAnimal> query = manager.createQuery(
            "SELECT f FROM FotoAnimal f WHERE f.animal = :animal ORDER BY f.principal DESC, f.dataUpload ASC", 
            FotoAnimal.class);
        query.setParameter("animal", animal);
        return query.getResultList();
    }

    public FotoAnimal fotoPrincipalDoAnimal(Animal animal) {
        try {
            TypedQuery<FotoAnimal> query = manager.createQuery(
                "SELECT f FROM FotoAnimal f WHERE f.animal = :animal AND f.principal = true", 
                FotoAnimal.class);
            query.setParameter("animal", animal);
            return query.getSingleResult();
        } catch (NoResultException e) {
            // Se não há foto principal, retorna a primeira foto do animal
            try {
                TypedQuery<FotoAnimal> query = manager.createQuery(
                    "SELECT f FROM FotoAnimal f WHERE f.animal = :animal ORDER BY f.dataUpload ASC", 
                    FotoAnimal.class);
                query.setParameter("animal", animal);
                query.setMaxResults(1);
                return query.getSingleResult();
            } catch (NoResultException ex) {
                return null;
            }
        }
    }

    public void definirFotoPrincipal(FotoAnimal novaFotoPrincipal) {
        // Remove o status de principal de todas as fotos do animal
        manager.createQuery(
            "UPDATE FotoAnimal f SET f.principal = false WHERE f.animal = :animal")
            .setParameter("animal", novaFotoPrincipal.getAnimal())
            .executeUpdate();
        
        // Define a nova foto como principal
        novaFotoPrincipal.setPrincipal(true);
        guardar(novaFotoPrincipal);
    }

    public long contarFotosDoAnimal(Animal animal) {
        TypedQuery<Long> query = manager.createQuery(
            "SELECT COUNT(f) FROM FotoAnimal f WHERE f.animal = :animal", 
            Long.class);
        query.setParameter("animal", animal);
        return query.getSingleResult();
    }
}
