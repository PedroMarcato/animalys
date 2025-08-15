package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.gov.pr.guaira.animalys.entity.Animal;

public class AnimalDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    public List<Animal> buscarPorProprietario(Integer idProprietario) {
        return em.createQuery("FROM Animal a WHERE a.proprietario.idProprietario = :id", Animal.class)
                 .setParameter("id", idProprietario)
                 .getResultList();
    }

    public Animal porId(Integer id) {
        return em.find(Animal.class, id);
    }

    // métodos
    @Transactional
    public Animal guardar(Animal animal) {
        return em.merge(animal);
    }

}
