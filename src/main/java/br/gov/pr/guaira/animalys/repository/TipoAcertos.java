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

import br.gov.pr.guaira.animalys.entity.TipoAcerto;
import br.gov.pr.guaira.animalys.filter.TipoAcertoFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class TipoAcertos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public TipoAcerto guardar(TipoAcerto acerto) {
		return manager.merge(acerto);
	}

	@Transactional
	public void remover(TipoAcerto servico) throws NegocioException {
		try {
			servico = porId(servico.getIdTipoAcerto());
			manager.remove(servico);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Esta Tipo de Acerto n�o pode ser exclu�da!");
		}
	}
	
	public List<TipoAcerto> tiposCadastrados() {
		return this.manager.createQuery("from TipoAcerto order by descricao asc ", TipoAcerto.class)
				.getResultList();
	}
	
	public List<TipoAcerto> filtrados(TipoAcertoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<TipoAcerto> criteriaQuery = builder.createQuery(TipoAcerto.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<TipoAcerto> tipoRoot = criteriaQuery.from(TipoAcerto.class);
		
		if (StringUtils.isNotBlank(filtro.getDescricao())) {
			predicates.add(
					builder.like(builder.lower(tipoRoot.get("descricao")), "%" + filtro.getDescricao().toLowerCase() + "%"));
		}

		criteriaQuery.select(tipoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(tipoRoot.get("descricao")));

		TypedQuery<TipoAcerto> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public TipoAcerto porId(Integer idTipoAcerto) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<TipoAcerto> criteriaQuery = builder.createQuery(TipoAcerto.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<TipoAcerto> tipoRoot = criteriaQuery.from(TipoAcerto.class);

		if (idTipoAcerto != null) {
			predicates.add(builder.equal(tipoRoot.get("idTipoAcerto"), idTipoAcerto));
		}

		criteriaQuery.select(tipoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(tipoRoot.get("idTipoAcerto")));

		TypedQuery<TipoAcerto> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}
}
