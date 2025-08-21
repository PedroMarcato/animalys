package br.gov.pr.guaira.animalys.dao;

import br.gov.pr.guaira.animalys.dto.ConsultaRealizada;
import br.gov.pr.guaira.animalys.entity.ConsultaFiltro;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import br.gov.pr.guaira.animalys.entity.Status;

@ApplicationScoped
public class ConsultaRealizadaDAO {

    @Inject
    private EntityManager em;

    public List<ConsultaRealizada> listarConsultasRealizadas() {
        String jpql = "SELECT new br.gov.pr.guaira.animalys.dto.ConsultaRealizada(" +
                "a.idAnimal, " +
                "a.nome, " +
                "p.nome, " +
                "at.data, " +
                "e.logradouro, " +
                "c.celular, " +
                "s.status, " +
                "at, " +
                "a.foto) " +
                "FROM Atendimento at " +
                "JOIN at.animal a " +
                "JOIN a.proprietario p " +
                "LEFT JOIN p.endereco e " +
                "LEFT JOIN p.contato c " +
                "JOIN at.solicitacao s " +
                "WHERE s.status IN (:statusList) " + // <-- alterado
                "AND at.procedimentos IS EMPTY " +
                "ORDER BY at.data DESC";

        return em.createQuery(jpql, ConsultaRealizada.class)
                .setParameter("statusList", Arrays.asList(Status.FINALIZADO, Status.AGENDADOCASTRACAO))
                .getResultList();
    }

    public List<ConsultaRealizada> buscarComFiltro(ConsultaFiltro filtro) {
        StringBuilder jpql = new StringBuilder(
                "SELECT new br.gov.pr.guaira.animalys.dto.ConsultaRealizada(" +
                        "a.idAnimal, " +
                        "a.nome, " +
                        "p.nome, " +
                        "at.data, " +
                        "e.logradouro, " +
                        "c.celular, " +
                        "s.status, " +
                        "at, " +
                        "a.foto) " +
                        "FROM Atendimento at " +
                        "JOIN at.animal a " +
                        "JOIN a.proprietario p " +
                        "LEFT JOIN p.endereco e " +
                        "LEFT JOIN p.contato c " +
                        "JOIN at.solicitacao s " +
                        "WHERE s.status IN (:statusList) " + // <-- alterado
                        "AND at.procedimentos IS EMPTY");

        if (filtro.getNomeProprietario() != null && !filtro.getNomeProprietario().trim().isEmpty()) {
            jpql.append(" AND LOWER(p.nome) LIKE LOWER(CONCAT('%', :nomeProprietario, '%'))");
        }

        if (filtro.getNomeAnimal() != null && !filtro.getNomeAnimal().trim().isEmpty()) {
            jpql.append(" AND LOWER(a.nome) LIKE LOWER(CONCAT('%', :nomeAnimal, '%'))");
        }

        if (filtro.getDataInicio() != null) {
            jpql.append(" AND at.data >= :dataInicio");
        }

        if (filtro.getDataFim() != null) {
            jpql.append(" AND at.data <= :dataFim");
        }

        jpql.append(" ORDER BY at.data DESC");

        TypedQuery<ConsultaRealizada> query = em.createQuery(jpql.toString(), ConsultaRealizada.class)
                .setParameter("statusList", Arrays.asList(Status.FINALIZADO, Status.AGENDADOCASTRACAO));

        if (filtro.getNomeProprietario() != null && !filtro.getNomeProprietario().trim().isEmpty()) {
            query.setParameter("nomeProprietario", filtro.getNomeProprietario());
        }

        if (filtro.getNomeAnimal() != null && !filtro.getNomeAnimal().trim().isEmpty()) {
            query.setParameter("nomeAnimal", filtro.getNomeAnimal());
        }

        if (filtro.getDataInicio() != null) {
            query.setParameter("dataInicio", filtro.getDataInicio());
        }

        if (filtro.getDataFim() != null) {
            // Ajuste para incluir o dia inteiro
            Date dataFim = new Date(filtro.getDataFim().getTime() + (1000 * 60 * 60 * 24) - 1);
            query.setParameter("dataFim", dataFim);
        }

        return query.getResultList();
    }
}
