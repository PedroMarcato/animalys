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
import br.gov.pr.guaira.animalys.filter.EspecieFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class Especies implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Especie guardar(Especie especie) {
		return manager.merge(especie);
	}

	@Transactional
	public void remover(Especie servico) throws NegocioException {
		try {
			servico = porId(servico.getIdEspecie());
			manager.remove(servico);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Esta Espécie não pode ser excluída!");
		}

	}
	

	public List<Especie> filtrados(EspecieFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Especie> criteriaQuery = builder.createQuery(Especie.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Especie> servicoRoot = criteriaQuery.from(Especie.class);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(
					builder.like(builder.lower(servicoRoot.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
		}

		criteriaQuery.select(servicoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(servicoRoot.get("nome")));

		TypedQuery<Especie> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public List<Especie> especiesCadastradas() {
		return this.manager.createQuery("from Especie order by nome asc ", Especie.class)
				.getResultList();
	}
	
	

	public Especie porId(Integer idEspecie) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Especie> criteriaQuery = builder.createQuery(Especie.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Especie> servicoRoot = criteriaQuery.from(Especie.class);
		
		if (idEspecie != null) {
			predicates.add(
					builder.equal(servicoRoot.get("idEspecie"), idEspecie));
		}

		criteriaQuery.select(servicoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(servicoRoot.get("idEspecie")));

		TypedQuery<Especie> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}
}
