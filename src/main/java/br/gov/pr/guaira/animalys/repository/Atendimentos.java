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
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Atendimento;
import br.gov.pr.guaira.animalys.filter.AtendimentoFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class Atendimentos implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Atendimento guardar(Atendimento atendimento) {
	    if (atendimento.getAnimal() != null) {
	        manager.merge(atendimento.getAnimal());
	    }
	    if (atendimento.getSolicitacao() != null) {
	        manager.merge(atendimento.getSolicitacao());
	    }
	    return manager.merge(atendimento);
	}

	@Transactional
	public void remover(Atendimento servico) throws NegocioException {
		try {
			servico = porId(servico.getIdAtendimento());
			manager.remove(servico);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Este Atendimento de Produto n�o pode ser exclu�do!");
		}

	}
	

	public List<Atendimento> filtrados(AtendimentoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Atendimento> criteriaQuery = builder.createQuery(Atendimento.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Atendimento> entradaRoot = criteriaQuery.from(Atendimento.class);
		entradaRoot.fetch("profissional", JoinType.INNER);
		
		if (filtro.getProfissional() != null) {
			predicates.add(
					builder.equal(entradaRoot.get("profissional"), filtro.getProfissional()));
		}
		
		if (filtro.getDataInicial() != null && filtro.getDataFinal() != null) {

			predicates.add(builder.between(entradaRoot.get("data"), filtro.getDataInicial(),
					filtro.getDataFinal()));
		}

		criteriaQuery.select(entradaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(entradaRoot.get("idAtendimento")));

		TypedQuery<Atendimento> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public Atendimento atendimentoPorAnimal(Animal animal, Calendar dataComeco, Calendar dataFim) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Atendimento> criteriaQuery = builder.createQuery(Atendimento.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Atendimento> atendimentoRoot = criteriaQuery.from(Atendimento.class);
		atendimentoRoot.fetch("animal", JoinType.INNER);
		//atendimentoRoot.fetch("solicitacao", JoinType.INNER);

		if (animal != null) {
			predicates.add(
					builder.equal(atendimentoRoot.get("animal"), animal));
		}

		if (dataComeco != null && dataFim != null) {
			predicates.add(builder.between(atendimentoRoot.get("data"), dataComeco,
					dataFim));
		}

		criteriaQuery.select(atendimentoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(atendimentoRoot.get("animal").get("nome")));

		TypedQuery<Atendimento> query = manager.createQuery(criteriaQuery);
		query.setMaxResults(1);
		List<Atendimento> resultados = query.getResultList();
		return resultados.isEmpty() ? null : resultados.get(0);
	}
	
	public List<Atendimento> atendimentosPorAnimal(Animal animal) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Atendimento> criteriaQuery = builder.createQuery(Atendimento.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Atendimento> atendimentoRoot = criteriaQuery.from(Atendimento.class);
		atendimentoRoot.fetch("animal", JoinType.INNER);
		//atendimentoRoot.fetch("solicitacao", JoinType.INNER);
		
		if (animal != null) {
			predicates.add(
					builder.equal(atendimentoRoot.get("animal"), animal));
		}
		
		criteriaQuery.select(atendimentoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.desc(atendimentoRoot.get("data")));

		TypedQuery<Atendimento> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}


	public Atendimento porId(Integer idAtendimento) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Atendimento> criteriaQuery = builder.createQuery(Atendimento.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Atendimento> atendimentoRoot = criteriaQuery.from(Atendimento.class);
		atendimentoRoot.fetch("profissional", JoinType.INNER);
		
		if (idAtendimento != null) {
			predicates.add(
					builder.equal(atendimentoRoot.get("idAtendimento"), idAtendimento));
		}

		criteriaQuery.select(atendimentoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(atendimentoRoot.get("idAtendimento")));

		TypedQuery<Atendimento> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}
}
