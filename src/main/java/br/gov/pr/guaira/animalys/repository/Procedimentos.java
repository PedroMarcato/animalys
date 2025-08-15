package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.gov.pr.guaira.animalys.entity.Especie;
import br.gov.pr.guaira.animalys.entity.Procedimento;
import br.gov.pr.guaira.animalys.filter.ProcedimentoFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class Procedimentos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Procedimento guardar(Procedimento procedimento) {
		return manager.merge(procedimento);
	}

	@Transactional
	public void remover(Procedimento procedimento) throws NegocioException {
		try {
			procedimento = porId(procedimento.getIdProcedimento());
			manager.remove(procedimento);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Este Animal não pode ser excluído!");
		}
	}

	public List<Procedimento> filtrados(ProcedimentoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Procedimento> criteriaQuery = builder.createQuery(Procedimento.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Procedimento> animalRoot = criteriaQuery.from(Procedimento.class);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(
					builder.like(builder.lower(animalRoot.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
		}

		criteriaQuery.select(animalRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(animalRoot.get("nome")));

		TypedQuery<Procedimento> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public List<Procedimento> procedimentosCadastrados() {
		return this.manager.createQuery("from Procedimento order by nome asc ", Procedimento.class).getResultList();
	}

	public List<Procedimento> porNome(String nome) {
		return this.manager
				.createQuery("from Procedimento where upper(nome) like :nome order by nome asc", Procedimento.class)
				.setParameter("nome", "%" + nome.toUpperCase() + "%").getResultList();
	}
	
	public List<Procedimento> porNomeAndEspecie(String nome, Especie especie) {
		return this.manager
				.createQuery("SELECT p FROM Procedimento p INNER JOIN FETCH p.especie e WHERE UPPER(p.nome) like :nome AND e = :especie order by p.nome asc", Procedimento.class)
				.setParameter("nome", "%" + nome.toUpperCase() + "%").setParameter("especie", especie).getResultList();
	}

	public Procedimento porId(Integer idProcedimento) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Procedimento> criteriaQuery = builder.createQuery(Procedimento.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Procedimento> procedimentoRoot = criteriaQuery.from(Procedimento.class);

		if (idProcedimento != null) {
			predicates.add(builder.equal(procedimentoRoot.get("idProcedimento"), idProcedimento));
		}

		criteriaQuery.select(procedimentoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(procedimentoRoot.get("idProcedimento")));

		TypedQuery<Procedimento> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}
	public Procedimento porSku(String sku) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Procedimento> criteriaQuery = builder.createQuery(Procedimento.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Procedimento> procedimentoRoot = criteriaQuery.from(Procedimento.class);

		if (sku != null) {
			predicates.add(builder.equal(procedimentoRoot.get("idProcedimento"), sku));
		}

		criteriaQuery.select(procedimentoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(procedimentoRoot.get("idProcedimento")));

		TypedQuery<Procedimento> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}
}
