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

import br.gov.pr.guaira.animalys.entity.AcertoEstoque;
import br.gov.pr.guaira.animalys.filter.AcertoEstoqueFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class AcertosEstoques implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public AcertoEstoque guardar(AcertoEstoque acerto) {
		return manager.merge(acerto);
	}

	@Transactional
	public void remover(AcertoEstoque servico) throws NegocioException {
		try {
			servico = porId(servico.getIdAcertoEstoque());
			manager.remove(servico);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Esta Entrada de Produto não pode ser excluída!");
		}

	}
	

	public List<AcertoEstoque> filtrados(AcertoEstoqueFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<AcertoEstoque> criteriaQuery = builder.createQuery(AcertoEstoque.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<AcertoEstoque> entradaRoot = criteriaQuery.from(AcertoEstoque.class);
		entradaRoot.fetch("tipoAcerto", JoinType.INNER);
		
		if (filtro.getTipoAcerto() != null) {
			predicates.add(
					builder.equal(entradaRoot.get("tipoAcerto"), filtro.getTipoAcerto()));
		}

		if (filtro.getDataInicial() != null && filtro.getDataFinal() == null) {
			Calendar data1 = Calendar.getInstance();
			data1.setTime(filtro.getDataInicial());
			data1.set(Calendar.HOUR, 00);
			data1.set(Calendar.MINUTE, 01);
			data1.set(Calendar.SECOND, 01);
			
			Calendar data2 = Calendar.getInstance();
			data2.set(Calendar.HOUR, 23);
			data2.set(Calendar.MINUTE, 59);
			data2.set(Calendar.SECOND, 59);

			predicates.add(builder.between(entradaRoot.get("data"), data1,
					data2));
		}
		
		if (filtro.getDataInicial() != null && filtro.getDataFinal() != null) {
			Calendar data1 = Calendar.getInstance();
			data1.setTime(filtro.getDataInicial());
			data1.set(Calendar.HOUR, 00);
			data1.set(Calendar.MINUTE, 01);
			data1.set(Calendar.SECOND, 01);
			
			Calendar data2 = Calendar.getInstance();
			data2.setTime(filtro.getDataFinal());
			data2.set(Calendar.HOUR, 23);
			data2.set(Calendar.MINUTE, 59);
			data2.set(Calendar.SECOND, 59);

			predicates.add(builder.between(entradaRoot.get("data"), data1,
					data2));
		}

		criteriaQuery.select(entradaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(entradaRoot.get("idAcertoEstoque")));

		TypedQuery<AcertoEstoque> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}


	public AcertoEstoque porId(Integer idAcertoEstoque) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<AcertoEstoque> criteriaQuery = builder.createQuery(AcertoEstoque.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<AcertoEstoque> entradaRoot = criteriaQuery.from(AcertoEstoque.class);
		entradaRoot.fetch("lote", JoinType.INNER);
		
		if (idAcertoEstoque != null) {
			predicates.add(
					builder.equal(entradaRoot.get("idAcertoEstoque"), idAcertoEstoque));
		}

		criteriaQuery.select(entradaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(entradaRoot.get("idAcertoEstoque")));

		TypedQuery<AcertoEstoque> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}
}
