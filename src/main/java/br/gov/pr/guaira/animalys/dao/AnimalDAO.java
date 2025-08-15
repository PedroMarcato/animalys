package br.gov.pr.guaira.animalys.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.gov.pr.guaira.animalys.entity.Animal;

@SuppressWarnings("serial")
public class AnimalDAO implements Serializable { 

    @Inject
    private EntityManager em;

    public List<Animal> buscarPorProprietario(Integer idProprietario) {
        return em.createQuery(
                "SELECT a FROM Animal a WHERE a.proprietario.idProprietario = :id", Animal.class)
                 .setParameter("id", idProprietario)
                 .getResultList();
    }
    
    @Transactional
    public Animal guardar(Animal animal) {
        return em.merge(animal);
    }

}
