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

import br.gov.pr.guaira.animalys.entity.TipoProduto;
import br.gov.pr.guaira.animalys.filter.TipoProdutoFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class TiposProdutos implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public TipoProduto guardar(TipoProduto tipoProduto) {
		return manager.merge(tipoProduto);
	}

	@Transactional
	public void remover(TipoProduto servico) throws NegocioException {
		try {
			servico = porId(servico.getIdTipoProduto());
			manager.remove(servico);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Este Tipo Produto não pode ser excluído!");
		}

	}
	

	public List<TipoProduto> filtrados(TipoProdutoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<TipoProduto> criteriaQuery = builder.createQuery(TipoProduto.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<TipoProduto> tipoRoot = criteriaQuery.from(TipoProduto.class);

		if (StringUtils.isNotBlank(filtro.getDescricao())) {
			predicates.add(
					builder.like(builder.lower(tipoRoot.get("descricao")), "%" + filtro.getDescricao().toLowerCase() + "%"));
		}

		criteriaQuery.select(tipoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(tipoRoot.get("descricao")));

		TypedQuery<TipoProduto> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public List<TipoProduto> tiposCadastrados() {
		return this.manager.createQuery("from TipoProduto order by descricao asc ", TipoProduto.class)
				.getResultList();
	}
	
	

	public TipoProduto porId(Integer idProduto) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<TipoProduto> criteriaQuery = builder.createQuery(TipoProduto.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<TipoProduto> servicoRoot = criteriaQuery.from(TipoProduto.class);
		
		if (idProduto != null) {
			predicates.add(
					builder.equal(servicoRoot.get("idTipoProduto"), idProduto));
		}

		criteriaQuery.select(servicoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(servicoRoot.get("idTipoProduto")));

		TypedQuery<TipoProduto> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}
}
