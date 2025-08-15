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

import br.gov.pr.guaira.animalys.entity.EntradaProduto;
import br.gov.pr.guaira.animalys.filter.EntradaProdutoFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class EntradasProdutos implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public EntradaProduto guardar(EntradaProduto entradaProduto) {
		return manager.merge(entradaProduto);
	}

	@Transactional
	public void remover(EntradaProduto servico) throws NegocioException {
		try {
			servico = porId(servico.getIdEntradaProduto());
			manager.remove(servico);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Esta Entrada de Produto não pode ser excluída!");
		}

	}
	

	public List<EntradaProduto> filtrados(EntradaProdutoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<EntradaProduto> criteriaQuery = builder.createQuery(EntradaProduto.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<EntradaProduto> entradaRoot = criteriaQuery.from(EntradaProduto.class);

		if (filtro.getLote() != null) {
			predicates.add(
					builder.equal(entradaRoot.get("lote"), filtro.getLote()));
		}
		if (filtro.getFornecedor() != null) {
			predicates.add(
					builder.equal(entradaRoot.get("fornecedor"), filtro.getFornecedor()));
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
		criteriaQuery.orderBy(builder.asc(entradaRoot.get("idEntradaProduto")));

		TypedQuery<EntradaProduto> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}


	public EntradaProduto porId(Integer idEspecie) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<EntradaProduto> criteriaQuery = builder.createQuery(EntradaProduto.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<EntradaProduto> entradaRoot = criteriaQuery.from(EntradaProduto.class);
		entradaRoot.fetch("fornecedor", JoinType.INNER);
		
		if (idEspecie != null) {
			predicates.add(
					builder.equal(entradaRoot.get("idEntradaProduto"), idEspecie));
		}

		criteriaQuery.select(entradaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(entradaRoot.get("idEntradaProduto")));

		TypedQuery<EntradaProduto> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}
}
