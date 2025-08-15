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

import org.apache.commons.lang3.StringUtils;

import br.gov.pr.guaira.animalys.entity.Produto;
import br.gov.pr.guaira.animalys.filter.ProdutoFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class Produtos implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Produto guardar(Produto produto) {
		return manager.merge(produto);
	}

	@Transactional
	public void remover(Produto servico) throws NegocioException {
		try {
			servico = porId(servico.getIdProduto());
			manager.remove(servico);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Este Produto não pode ser excluído!");
		}

	}
	

	public List<Produto> filtrados(ProdutoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Produto> criteriaQuery = builder.createQuery(Produto.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Produto> produtoRoot = criteriaQuery.from(Produto.class);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(
					builder.like(builder.lower(produtoRoot.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
		}
		
		if (filtro.getMarca() != null) {
			predicates.add(
					builder.equal(produtoRoot.get("marca"), filtro.getMarca()));
		}
		
		if (filtro.getTipoProduto() != null) {
			predicates.add(
					builder.equal(produtoRoot.get("tipoProduto"), filtro.getTipoProduto()));
		}

		criteriaQuery.select(produtoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(produtoRoot.get("nome")));

		TypedQuery<Produto> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public List<Produto> produtosCadastrados() {
		return this.manager.createQuery("from Produto order by nome asc ", Produto.class)
				.getResultList();
	}
	
	

	public Produto porId(Integer idProduto) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Produto> criteriaQuery = builder.createQuery(Produto.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Produto> produtoRoot = criteriaQuery.from(Produto.class);
		produtoRoot.fetch("tipoProduto", JoinType.INNER);
		produtoRoot.fetch("marca", JoinType.INNER);
		
		if (idProduto != null) {
			predicates.add(
					builder.equal(produtoRoot.get("idProduto"), idProduto));
		}

		criteriaQuery.select(produtoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(produtoRoot.get("idProduto")));

		TypedQuery<Produto> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	public List<Produto> porNome(String nome) {
		return this.manager.createQuery("select p from Produto p inner join fetch p.marca m inner join fetch p.tipoProduto tp " +
				"where upper(p.nome) like :nome ", Produto.class)
				.setParameter("nome", nome.toUpperCase() + "%")
				.getResultList();
	}
}
