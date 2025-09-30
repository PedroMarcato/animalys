package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.entity.RetiradaMedicamento;
import br.gov.pr.guaira.animalys.service.NegocioException;

public class RetiradasMedicamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager manager;

    @Transactional
    public RetiradaMedicamento guardar(RetiradaMedicamento retirada) {
        return manager.merge(retirada);
    }

    @Transactional
    public void remover(RetiradaMedicamento retirada) throws NegocioException {
        try {
            retirada = porId(retirada.getIdRetiradaMedicamento());
            manager.remove(retirada);
            manager.flush();
        } catch (PersistenceException e) {
            e.printStackTrace();
            throw new NegocioException("Esta retirada de medicamento não pode ser excluída!");
        }
    }

    public RetiradaMedicamento porId(Integer id) {
        return manager.find(RetiradaMedicamento.class, id);
    }

    public List<RetiradaMedicamento> porProprietario(Proprietario proprietario) {
        return manager.createQuery(
                "SELECT r FROM RetiradaMedicamento r " +
                "LEFT JOIN FETCH r.proprietario " +
                "LEFT JOIN FETCH r.animal " +
                "WHERE r.proprietario = :proprietario " +
                "ORDER BY r.dataRetirada DESC", RetiradaMedicamento.class)
                .setParameter("proprietario", proprietario)
                .getResultList();
    }

    public List<RetiradaMedicamento> porAnimal(Animal animal) {
        return manager.createQuery(
                "SELECT r FROM RetiradaMedicamento r " +
                "LEFT JOIN FETCH r.proprietario " +
                "LEFT JOIN FETCH r.animal " +
                "WHERE r.animal = :animal " +
                "ORDER BY r.dataRetirada DESC", RetiradaMedicamento.class)
                .setParameter("animal", animal)
                .getResultList();
    }

    public List<RetiradaMedicamento> todas() {
        return manager.createQuery(
                "SELECT r FROM RetiradaMedicamento r " +
                "LEFT JOIN FETCH r.proprietario " +
                "LEFT JOIN FETCH r.animal " +
                "ORDER BY r.dataRetirada DESC", RetiradaMedicamento.class)
                .getResultList();
    }

    public List<RetiradaMedicamento> filtrar(String cpfProprietario, String nomeAnimal, String nomeMedicamento) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT r FROM RetiradaMedicamento r ");
        jpql.append("LEFT JOIN FETCH r.proprietario p ");
        jpql.append("LEFT JOIN FETCH r.animal a ");
        jpql.append("WHERE 1=1 ");

        // Adiciona filtros condicionais
        if (cpfProprietario != null && !cpfProprietario.trim().isEmpty()) {
            jpql.append("AND (p.cpf LIKE :cpfProprietario OR REPLACE(REPLACE(REPLACE(p.cpf, '.', ''), '-', ''), '/', '') LIKE :cpfProprietarioLimpo) ");
        }
        if (nomeAnimal != null && !nomeAnimal.trim().isEmpty()) {
            jpql.append("AND UPPER(a.nome) LIKE UPPER(:nomeAnimal) ");
        }
        if (nomeMedicamento != null && !nomeMedicamento.trim().isEmpty()) {
            jpql.append("AND UPPER(r.nomeMedicamento) LIKE UPPER(:nomeMedicamento) ");
        }
        
        jpql.append("ORDER BY r.dataRetirada DESC");

        javax.persistence.TypedQuery<RetiradaMedicamento> query = manager.createQuery(jpql.toString(), RetiradaMedicamento.class);

        // Define parâmetros se necessário
        if (cpfProprietario != null && !cpfProprietario.trim().isEmpty()) {
            // Busca tanto no CPF formatado quanto no CPF limpo
            String cpfFormatado = cpfProprietario.trim();
            String cpfLimpo = cpfProprietario.replaceAll("[^0-9]", "");
            query.setParameter("cpfProprietario", "%" + cpfFormatado + "%");
            query.setParameter("cpfProprietarioLimpo", "%" + cpfLimpo + "%");
        }
        if (nomeAnimal != null && !nomeAnimal.trim().isEmpty()) {
            query.setParameter("nomeAnimal", "%" + nomeAnimal.trim() + "%");
        }
        if (nomeMedicamento != null && !nomeMedicamento.trim().isEmpty()) {
            query.setParameter("nomeMedicamento", "%" + nomeMedicamento.trim() + "%");
        }

        return query.getResultList();
    }

    /**
     * Verifica se um animal pertence a um proprietário específico
     * usando query para evitar LazyInitializationException
     */
    public boolean verificarAnimalPertenceProprietario(Integer idAnimal, Integer idProprietario) {
        Long count = manager.createQuery(
                "SELECT COUNT(a) FROM Animal a " +
                "WHERE a.idAnimal = :idAnimal " +
                "AND a.proprietario.idProprietario = :idProprietario", Long.class)
                .setParameter("idAnimal", idAnimal)
                .setParameter("idProprietario", idProprietario)
                .getSingleResult();
        
        return count > 0;
    }
}