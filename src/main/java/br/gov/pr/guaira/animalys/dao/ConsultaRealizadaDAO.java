package br.gov.pr.guaira.animalys.dao;

import br.gov.pr.guaira.animalys.dto.ConsultaRealizada;
import br.gov.pr.guaira.animalys.entity.Status;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class ConsultaRealizadaDAO {

    @PersistenceContext
    private EntityManager em;

    public List<ConsultaRealizada> listarConsultasFinalizadas() {
        String jpql = "SELECT new br.gov.pr.guaira.animalys.dto.ConsultaRealizada(" +
                "a.idanimal, a.nome, p.nome, p.endereco, p.contato, at.data) " +
                "FROM Atendimento at " +
                "JOIN at.animal a " +
                "JOIN a.proprietario p " +
                "JOIN Solicitacao s ON s.idSolicitacao = at.solicitacao.idSolicitacao " +
                "WHERE s.status = :status " +
                "ORDER BY at.data DESC";

        return em.createQuery(jpql, ConsultaRealizada.class)
                .setParameter("status", Status.FINALIZADO)
                .getResultList();

    }
}
