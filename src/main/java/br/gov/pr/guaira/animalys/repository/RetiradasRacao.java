package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.entity.RetiradaRacao;
import br.gov.pr.guaira.animalys.service.NegocioException;

public class RetiradasRacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager manager;

    @Transactional
    public RetiradaRacao guardar(RetiradaRacao retirada) {
        return manager.merge(retirada);
    }

    @Transactional
    public void remover(RetiradaRacao retirada) throws NegocioException {
        try {
            retirada = porId(retirada.getIdRetiradaRacao());
            manager.remove(retirada);
            manager.flush();
        } catch (PersistenceException e) {
            e.printStackTrace();
            throw new NegocioException("Esta retirada de ração não pode ser excluída!");
        }
    }

    public RetiradaRacao porId(Integer id) {
        return manager.find(RetiradaRacao.class, id);
    }

    public List<RetiradaRacao> porProprietario(Proprietario proprietario) {
        return manager.createQuery(
                "SELECT r FROM RetiradaRacao r " +
                "LEFT JOIN FETCH r.proprietario " +
                "LEFT JOIN FETCH r.animal " +
                "WHERE r.proprietario = :proprietario " +
                "ORDER BY r.dataRetirada DESC", RetiradaRacao.class)
                .setParameter("proprietario", proprietario)
                .getResultList();
    }

    public List<RetiradaRacao> porAnimal(Animal animal) {
        return manager.createQuery(
                "SELECT r FROM RetiradaRacao r " +
                "LEFT JOIN FETCH r.proprietario " +
                "LEFT JOIN FETCH r.animal " +
                "WHERE r.animal = :animal " +
                "ORDER BY r.dataRetirada DESC", RetiradaRacao.class)
                .setParameter("animal", animal)
                .getResultList();
    }

    public List<RetiradaRacao> porMesReferencia(Integer mesReferencia) {
        return manager.createQuery(
                "SELECT r FROM RetiradaRacao r " +
                "LEFT JOIN FETCH r.proprietario " +
                "LEFT JOIN FETCH r.animal " +
                "WHERE r.mesReferencia = :mesReferencia " +
                "ORDER BY r.dataRetirada DESC", RetiradaRacao.class)
                .setParameter("mesReferencia", mesReferencia)
                .getResultList();
    }

    public List<RetiradaRacao> todas() {
        return manager.createQuery(
                "SELECT r FROM RetiradaRacao r " +
                "LEFT JOIN FETCH r.proprietario " +
                "LEFT JOIN FETCH r.animal " +
                "ORDER BY r.dataRetirada DESC", RetiradaRacao.class)
                .getResultList();
    }

    public List<RetiradaRacao> filtradas(String nomeProprietario, String nomeAnimal, Integer mesReferencia, Integer anoReferencia) {
        StringBuilder jpql = new StringBuilder("SELECT r FROM RetiradaRacao r " +
                "LEFT JOIN FETCH r.proprietario p " +
                "LEFT JOIN FETCH r.animal a " +
                "WHERE 1=1 ");

        if (nomeProprietario != null && !nomeProprietario.trim().isEmpty()) {
            jpql.append("AND LOWER(p.nome) LIKE LOWER(:nomeProprietario) ");
        }

        if (nomeAnimal != null && !nomeAnimal.trim().isEmpty()) {
            jpql.append("AND LOWER(a.nome) LIKE LOWER(:nomeAnimal) ");
        }

        if (mesReferencia != null) {
            jpql.append("AND r.mesReferencia = :mesReferencia ");
        }

        if (anoReferencia != null) {
            jpql.append("AND YEAR(COALESCE(r.data12Mes, r.data11Mes, r.data10Mes, r.data9Mes, r.data8Mes, r.data7Mes, r.data6Mes, r.data5Mes, r.data4Mes, r.data3Mes, r.data2Mes, r.data1Mes)) = :anoReferencia ");
        }

        jpql.append("ORDER BY r.dataRetirada DESC");

        javax.persistence.TypedQuery<RetiradaRacao> query = manager.createQuery(jpql.toString(), RetiradaRacao.class);

        if (nomeProprietario != null && !nomeProprietario.trim().isEmpty()) {
            query.setParameter("nomeProprietario", "%" + nomeProprietario.trim() + "%");
        }

        if (nomeAnimal != null && !nomeAnimal.trim().isEmpty()) {
            query.setParameter("nomeAnimal", "%" + nomeAnimal.trim() + "%");
        }

        if (mesReferencia != null) {
            query.setParameter("mesReferencia", mesReferencia);
        }

        if (anoReferencia != null) {
            query.setParameter("anoReferencia", anoReferencia);
        }

        return query.getResultList();
    }

    public List<RetiradaRacao> filtradasPorCpf(String cpfProprietario, String nomeAnimal, Integer mesReferencia, Integer anoReferencia) {
        StringBuilder jpql = new StringBuilder("SELECT r FROM RetiradaRacao r " +
                "LEFT JOIN FETCH r.proprietario p " +
                "LEFT JOIN FETCH r.animal a " +
                "WHERE 1=1 ");

        if (cpfProprietario != null && !cpfProprietario.trim().isEmpty()) {
            jpql.append("AND (p.cpf LIKE :cpfProprietario OR REPLACE(REPLACE(REPLACE(p.cpf, '.', ''), '-', ''), '/', '') LIKE :cpfProprietarioLimpo) ");
        }

        if (nomeAnimal != null && !nomeAnimal.trim().isEmpty()) {
            jpql.append("AND LOWER(a.nome) LIKE LOWER(:nomeAnimal) ");
        }

        if (mesReferencia != null) {
            jpql.append("AND r.mesReferencia = :mesReferencia ");
        }

        if (anoReferencia != null) {
            jpql.append("AND YEAR(COALESCE(r.data12Mes, r.data11Mes, r.data10Mes, r.data9Mes, r.data8Mes, r.data7Mes, r.data6Mes, r.data5Mes, r.data4Mes, r.data3Mes, r.data2Mes, r.data1Mes)) = :anoReferencia ");
        }

        jpql.append("ORDER BY r.dataRetirada DESC");

        javax.persistence.TypedQuery<RetiradaRacao> query = manager.createQuery(jpql.toString(), RetiradaRacao.class);

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

        if (mesReferencia != null) {
            query.setParameter("mesReferencia", mesReferencia);
        }

        if (anoReferencia != null) {
            query.setParameter("anoReferencia", anoReferencia);
        }

        return query.getResultList();
    }
}