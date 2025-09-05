package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.gov.pr.guaira.animalys.entity.Especie;
import br.gov.pr.guaira.animalys.entity.Raca;
import br.gov.pr.guaira.animalys.filter.RacaFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

@Named
@RequestScoped
public class Racas implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Raca guardar(Raca raca) {
		return manager.merge(raca);
	}

	@Transactional
	public void remover(Raca raca) throws NegocioException {
		try {
			raca = porId(raca.getIdRaca());
			manager.remove(raca);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Esta Raca não pode ser excluída!");
		}

	}
	

	public List<Raca> filtrados(RacaFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Raca> criteriaQuery = builder.createQuery(Raca.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Raca> racaRoot = criteriaQuery.from(Raca.class);
		racaRoot.fetch("especie", JoinType.INNER);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(
					builder.like(builder.lower(racaRoot.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
		}
		
		if(filtro.getEspecie() != null) {
			predicates.add(builder.equal(racaRoot.get("especie"), filtro.getEspecie()));
		}

		criteriaQuery.select(racaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(racaRoot.get("nome")));

		TypedQuery<Raca> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public List<Raca> racasCadastradas() {
		return this.manager.createQuery("from Raca order by nome asc ", Raca.class)
				.getResultList();
	}
	
	

	public Raca porId(Integer idRaca) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Raca> criteriaQuery = builder.createQuery(Raca.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Raca> servicoRoot = criteriaQuery.from(Raca.class);
		servicoRoot.fetch("especie", JoinType.INNER);
		
		if (idRaca != null) {
			predicates.add(
					builder.equal(servicoRoot.get("idRaca"), idRaca));
		}

		criteriaQuery.select(servicoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(servicoRoot.get("idRaca")));

		TypedQuery<Raca> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	public List<Raca> racasPorEspecie(Especie especieSelecionada) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Raca> criteriaQuery = builder.createQuery(Raca.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Raca> servicoRoot = criteriaQuery.from(Raca.class);
		servicoRoot.fetch("especie", JoinType.INNER);
		
		if (especieSelecionada != null) {
			predicates.add(
					builder.equal(servicoRoot.get("especie"), especieSelecionada));
		}

		criteriaQuery.select(servicoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(servicoRoot.get("idRaca")));

		TypedQuery<Raca> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
}
