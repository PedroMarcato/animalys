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

import br.gov.pr.guaira.animalys.entity.Grupo;
import br.gov.pr.guaira.animalys.filter.GrupoFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class Grupos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Grupo guardar(Grupo grupo) {
		return manager.merge(grupo);
	}

	@Transactional
	public void remover(Grupo grupo) throws NegocioException {
		try {
			grupo = porId(grupo.getIdGrupo());
			manager.remove(grupo);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Grupo não pode ser excluído!");
		}

	}

	public List<Grupo> filtrados(GrupoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Grupo> criteriaQuery = builder.createQuery(Grupo.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Grupo> logradouroRoot = criteriaQuery.from(Grupo.class);
		// Fetch<Logradouro, Cidade> cidadeJoin = logradouroRoot.fetch("cidade",
		// JoinType.INNER);
		// cidadeJoin.fetch("estado", JoinType.INNER);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.lower(logradouroRoot.get("nome")),
					"%" + filtro.getNome().toLowerCase() + "%"));
		}

		criteriaQuery.select(logradouroRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(logradouroRoot.get("nome")));

		TypedQuery<Grupo> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public Grupo porId(Integer idGrupo) {
		return manager.find(Grupo.class, idGrupo);
	}

	public List<Grupo> gruposCadastrados() {
		return this.manager.createQuery("from Grupo", Grupo.class)
				.getResultList();
	}
}
