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
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.gov.pr.guaira.animalys.entity.EntradaProduto;
import br.gov.pr.guaira.animalys.entity.ItemEntrada;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class ItensEntrada implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;


	public List<ItemEntrada> porEntrada(EntradaProduto entrada) {
		return this.manager.createQuery("select ie from ItemEntrada ie inner join fetch ie.entradaProduto ep " +
				"where ep =:entrada", ItemEntrada.class)
				.setParameter("entrada", entrada).getResultList();
	}
	
	@Transactional
	public void remover(ItemEntrada servico) throws NegocioException {
		try {
			servico = porId(servico.getIdItemEntrada());
			manager.remove(servico);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Este Item não pode ser excluído!");
		}

	}
	
	public ItemEntrada porId(Integer idItemEntrada) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ItemEntrada> criteriaQuery = builder.createQuery(ItemEntrada.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<ItemEntrada> itemRoot = criteriaQuery.from(ItemEntrada.class);
		itemRoot.fetch("produto", JoinType.INNER);
		itemRoot.fetch("entradaProduto", JoinType.INNER);
				
		if (idItemEntrada != null) {
			predicates.add(
					builder.equal(itemRoot.get("idItemEntrada"), idItemEntrada));
		}

		criteriaQuery.select(itemRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(itemRoot.get("idItemEntrada")));

		TypedQuery<ItemEntrada> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}
}
