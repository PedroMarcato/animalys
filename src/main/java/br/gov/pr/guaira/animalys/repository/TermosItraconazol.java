package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.TermoItraconazol;
import br.gov.pr.guaira.animalys.repository.filter.TermoItraconazolFilter;

public class TermosItraconazol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager manager;

    public TermoItraconazol salvar(TermoItraconazol termo) {
        TermoItraconazol resultado = manager.merge(termo);
        manager.flush(); // Força o flush para garantir que seja persistido
        return resultado;
    }

    public void remover(TermoItraconazol termo) {
        termo = porId(termo.getIdTermoItraconazol());
        manager.remove(termo);
    }

    public TermoItraconazol porId(Integer idTermo) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<TermoItraconazol> criteriaQuery = builder.createQuery(TermoItraconazol.class);
        
        Root<TermoItraconazol> termoRoot = criteriaQuery.from(TermoItraconazol.class);
        
        // Fetch joins para evitar LazyInitializationException
        Fetch<TermoItraconazol, Animal> animalFetch = termoRoot.fetch("animal", JoinType.INNER);
        animalFetch.fetch("proprietario", JoinType.INNER);
        animalFetch.fetch("raca", JoinType.LEFT);
        
        criteriaQuery.select(termoRoot);
        criteriaQuery.where(builder.equal(termoRoot.get("idTermoItraconazol"), idTermo));
        
        return manager.createQuery(criteriaQuery).getSingleResult();
    }

    public List<TermoItraconazol> filtrados(TermoItraconazolFilter filtro) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<TermoItraconazol> criteriaQuery = builder.createQuery(TermoItraconazol.class);
        List<Predicate> predicates = new ArrayList<Predicate>();

        Root<TermoItraconazol> termoRoot = criteriaQuery.from(TermoItraconazol.class);
        
        // Fetch joins para evitar LazyInitializationException
        Fetch<TermoItraconazol, Animal> animalFetch = termoRoot.fetch("animal", JoinType.INNER);
        animalFetch.fetch("proprietario", JoinType.INNER);
        animalFetch.fetch("raca", JoinType.LEFT);

        // Filtro por nome do animal
        if (filtro.getNomeAnimal() != null && !filtro.getNomeAnimal().trim().isEmpty()) {
            predicates.add(builder.like(builder.lower(termoRoot.get("animal").get("nome")), 
                "%" + filtro.getNomeAnimal().toLowerCase() + "%"));
        }

        // Filtro por nome do proprietário
        if (filtro.getNomeProprietario() != null && !filtro.getNomeProprietario().trim().isEmpty()) {
            predicates.add(builder.like(builder.lower(termoRoot.get("animal").get("proprietario").get("nome")), 
                "%" + filtro.getNomeProprietario().toLowerCase() + "%"));
        }

        // Filtro por CPF do proprietário
        if (filtro.getCpfProprietario() != null && !filtro.getCpfProprietario().trim().isEmpty()) {
            predicates.add(builder.like(builder.lower(termoRoot.get("animal").get("proprietario").get("cpf")), 
                "%" + filtro.getCpfProprietario().toLowerCase() + "%"));
        }

        // Filtro por período de data
        if (filtro.getDataInicio() != null) {
            predicates.add(builder.greaterThanOrEqualTo(termoRoot.get("dataRetirada"), filtro.getDataInicio()));
        }

        if (filtro.getDataFim() != null) {
            Calendar dataFimAjustada = Calendar.getInstance();
            dataFimAjustada.setTime(filtro.getDataFim().getTime());
            dataFimAjustada.set(Calendar.HOUR_OF_DAY, 23);
            dataFimAjustada.set(Calendar.MINUTE, 59);
            dataFimAjustada.set(Calendar.SECOND, 59);
            predicates.add(builder.lessThanOrEqualTo(termoRoot.get("dataRetirada"), dataFimAjustada));
        }

        criteriaQuery.select(termoRoot);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.orderBy(builder.desc(termoRoot.get("dataRetirada")));

        TypedQuery<TermoItraconazol> query = manager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public List<TermoItraconazol> todos() {
        return manager.createQuery(
            "SELECT t FROM TermoItraconazol t " +
            "JOIN FETCH t.animal a " +
            "JOIN FETCH a.proprietario p " +
            "LEFT JOIN FETCH a.raca r " +
            "ORDER BY t.dataRetirada DESC", 
            TermoItraconazol.class)
            .getResultList();
    }

    public List<TermoItraconazol> termosPorAnimal(Animal animal) {
        return manager.createQuery(
            "SELECT t FROM TermoItraconazol t " +
            "JOIN FETCH t.animal a " +
            "JOIN FETCH a.proprietario p " +
            "LEFT JOIN FETCH a.raca r " +
            "WHERE t.animal = :animal " +
            "ORDER BY t.dataRetirada DESC", 
            TermoItraconazol.class)
            .setParameter("animal", animal)
            .getResultList();
    }

    public List<TermoItraconazol> filtrar(String cpfProprietario, String nomeAnimal, String nomeMedicamento) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT t FROM TermoItraconazol t ");
        jpql.append("JOIN FETCH t.animal a ");
        jpql.append("JOIN FETCH a.proprietario p ");
        jpql.append("LEFT JOIN FETCH a.raca r ");
        jpql.append("WHERE 1=1 ");

        // Adiciona filtros conforme necessário
        if (cpfProprietario != null && !cpfProprietario.trim().isEmpty()) {
            jpql.append("AND LOWER(p.cpf) LIKE LOWER(:cpf) ");
        }

        if (nomeAnimal != null && !nomeAnimal.trim().isEmpty()) {
            jpql.append("AND LOWER(a.nome) LIKE LOWER(:nomeAnimal) ");
        }

        // nomeMedicamento sempre será "Itraconazol", então não precisa filtrar

        jpql.append("ORDER BY t.dataRetirada DESC");

        TypedQuery<TermoItraconazol> query = manager.createQuery(jpql.toString(), TermoItraconazol.class);

        // Define parâmetros
        if (cpfProprietario != null && !cpfProprietario.trim().isEmpty()) {
            query.setParameter("cpf", "%" + cpfProprietario + "%");
        }

        if (nomeAnimal != null && !nomeAnimal.trim().isEmpty()) {
            query.setParameter("nomeAnimal", "%" + nomeAnimal + "%");
        }

        return query.getResultList();
    }

}