package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.entity.Solicitacao;
import br.gov.pr.guaira.animalys.entity.Status;
import br.gov.pr.guaira.animalys.filter.SolicitacaoFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class Solicitacoes implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Solicitacao guardar(Solicitacao solicitacao) {
		return manager.merge(solicitacao);
	}

	@Transactional
	public void remover(Solicitacao solicitacao) throws NegocioException {
		try {
			solicitacao = porId(solicitacao.getIdSolicitacao());
			manager.remove(solicitacao);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Esta Solicitacao não pode ser excluída!");
		}

	}

	public List<Solicitacao> filtrados(SolicitacaoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Solicitacao> criteriaQuery = builder.createQuery(Solicitacao.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Solicitacao> solicitacaoRoot = criteriaQuery.from(Solicitacao.class);
		Fetch<Solicitacao, Proprietario> proprietarioRoot =  solicitacaoRoot.fetch("proprietario", JoinType.INNER);
		proprietarioRoot.fetch("endereco", JoinType.INNER);
		proprietarioRoot.fetch("contato", JoinType.INNER);

		if (StringUtils.isNotBlank(filtro.getProprietario())) {
			predicates.add(builder.like(builder.lower(solicitacaoRoot.get("proprietario").get("nome")),
					"%" + filtro.getProprietario().toLowerCase() + "%"));
		}

		if (filtro.getStatus() != null) {
			predicates.add(builder.equal(solicitacaoRoot.get("status"), filtro.getStatus()));
		}
		
		if (filtro.getModalidadeSolicitante() != null) {
			predicates.add(builder.equal(solicitacaoRoot.get("modalidade"), filtro.getModalidadeSolicitante()));
		}

		if (filtro.getDataSolicitacao() != null) {
			Calendar data1 = Calendar.getInstance();
			Calendar data2 = Calendar.getInstance();

			data1.setTime(filtro.getDataSolicitacao());
			data1.set(Calendar.HOUR, 00);
			data1.set(Calendar.MINUTE, 01);
			data1.set(Calendar.SECOND, 01);

			data2.setTime(filtro.getDataSolicitacao());
			data2.set(Calendar.HOUR, 23);
			data2.set(Calendar.MINUTE, 59);
			data2.set(Calendar.SECOND, 59);

			predicates.add(builder.between(solicitacaoRoot.get("data"), data1, data2));
		}

		if (filtro.getDataAgendaVisitaInicial() != null) {
			Calendar data1 = Calendar.getInstance();
			Calendar data2 = Calendar.getInstance();

			data1.setTime(filtro.getDataAgendaVisitaInicial());
			data1.set(Calendar.HOUR, 00);
			data1.set(Calendar.MINUTE, 01);
			data1.set(Calendar.SECOND, 01);

			data2.setTime(filtro.getDataAgendaVisitaInicial());
			data2.set(Calendar.HOUR, 23);
			data2.set(Calendar.MINUTE, 59);
			data2.set(Calendar.SECOND, 59);

			predicates.add(builder.between(solicitacaoRoot.get("dataAgendaVisita"), data1, data2));
		}

		if (filtro.getDataAgendaVisitaInicial() != null && filtro.getDataAgendaVisitaFinal() != null) {
			Calendar data1 = Calendar.getInstance();
			Calendar data2 = Calendar.getInstance();

			data1.setTime(filtro.getDataAgendaVisitaInicial());
			data1.set(Calendar.HOUR, 00);
			data1.set(Calendar.MINUTE, 01);
			data1.set(Calendar.SECOND, 01);

			data2.setTime(filtro.getDataAgendaVisitaFinal());
			data2.set(Calendar.HOUR, 23);
			data2.set(Calendar.MINUTE, 59);
			data2.set(Calendar.SECOND, 59);

			predicates.add(builder.between(solicitacaoRoot.get("dataAgendaVisita"), data1, data2));
		}

		criteriaQuery.select(solicitacaoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(solicitacaoRoot.get("dataAgendaVisita")));

		TypedQuery<Solicitacao> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public List<Solicitacao> visitasAgendadas(Status status) {
		return this.manager.createQuery(
				"Select a from Solicitacao a inner join fetch a.proprietario p " + "where a.status =:status",
				Solicitacao.class).setParameter("status", status).getResultList();
	}

	public Long porStatus(Status status) {
		return this.manager
				.createQuery("Select COUNT(idSolicitacao) from Solicitacao " + "where status =:status", Long.class)
				.setParameter("status", status).getSingleResult();
	}

	public List<Solicitacao> porCpf(Status status, String cpf) {
		return this.manager
				.createQuery("Select s from Solicitacao s inner join fetch s.proprietario p "
						+ "inner join fetch s.animais ani where s.status =:status and p.cpf = :cpf and ani < 2", Solicitacao.class)
				.setParameter("status", status).setParameter("cpf", cpf).getResultList();
	}


	public Solicitacao porDataAgendada(Calendar data) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Solicitacao> criteriaQuery = builder.createQuery(Solicitacao.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Solicitacao> solicitacaoRoot = criteriaQuery.from(Solicitacao.class);
		solicitacaoRoot.fetch("proprietario", JoinType.INNER);

		if (data != null) {
			predicates.add(builder.equal(solicitacaoRoot.get("dataAgendaVisita"), data));
		}

		criteriaQuery.select(solicitacaoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(solicitacaoRoot.get("idSolicitacao")));

		TypedQuery<Solicitacao> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	public Solicitacao porDataAgendadaCastracao(Calendar data) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Solicitacao> criteriaQuery = builder.createQuery(Solicitacao.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Solicitacao> solicitacaoRoot = criteriaQuery.from(Solicitacao.class);
		solicitacaoRoot.fetch("proprietario", JoinType.INNER);

		if (data != null) {
			predicates.add(builder.equal(solicitacaoRoot.get("dataAgendaCastracao"), data));
		}

		criteriaQuery.select(solicitacaoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(solicitacaoRoot.get("idSolicitacao")));

		TypedQuery<Solicitacao> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	public Solicitacao porId(Integer idSolicitacao) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Solicitacao> criteriaQuery = builder.createQuery(Solicitacao.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Solicitacao> solicitacaoRoot = criteriaQuery.from(Solicitacao.class);
		solicitacaoRoot.fetch("proprietario", JoinType.INNER);

		if (idSolicitacao != null) {
			predicates.add(builder.equal(solicitacaoRoot.get("idSolicitacao"), idSolicitacao));
		}

		criteriaQuery.select(solicitacaoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(solicitacaoRoot.get("idSolicitacao")));

		TypedQuery<Solicitacao> query = manager.createQuery(criteriaQuery);
		return query.setMaxResults(1).getSingleResult();
	}

	public Solicitacao porCPF(String cpf, Status status) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Solicitacao> criteriaQuery = builder.createQuery(Solicitacao.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Solicitacao> solicitacaoRoot = criteriaQuery.from(Solicitacao.class);

		if (cpf != null) {
			predicates.add(builder.equal(solicitacaoRoot.get("proprietario").get("cpf"), cpf));
		}

		if (status != null) {
			predicates.add(builder.equal(solicitacaoRoot.get("status"), status));
		}

		criteriaQuery.select(solicitacaoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(solicitacaoRoot.get("idSolicitacao")));

		TypedQuery<Solicitacao> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}
	
	public List<Solicitacao> porCPFList(String cpf, Status status) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Solicitacao> criteriaQuery = builder.createQuery(Solicitacao.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Solicitacao> solicitacaoRoot = criteriaQuery.from(Solicitacao.class);

		if (cpf != null) {
			predicates.add(builder.equal(solicitacaoRoot.get("proprietario").get("cpf"), cpf));
		}

		if (status != null) {
			predicates.add(builder.equal(solicitacaoRoot.get("status"), status));
		}

		criteriaQuery.select(solicitacaoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(solicitacaoRoot.get("idSolicitacao")));

		TypedQuery<Solicitacao> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public Solicitacao porCPF(String cpf) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Solicitacao> criteriaQuery = builder.createQuery(Solicitacao.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Solicitacao> solicitacaoRoot = criteriaQuery.from(Solicitacao.class);

		if (cpf != null) {
			predicates.add(builder.equal(solicitacaoRoot.get("proprietario").get("cpf"), cpf));
		}

		criteriaQuery.select(solicitacaoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(solicitacaoRoot.get("idSolicitacao")));

		TypedQuery<Solicitacao> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

}
