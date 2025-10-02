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

import org.apache.commons.lang3.StringUtils;

import br.gov.pr.guaira.animalys.entity.Lote;
import br.gov.pr.guaira.animalys.entity.Produto;
import br.gov.pr.guaira.animalys.filter.LoteFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class Lotes implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Lote guardar(Lote lote) {
		return manager.merge(lote);
	}

	@Transactional
	public void remover(Lote lote) throws NegocioException {
		try {
			lote = porId(lote.getIdLote());
			manager.remove(lote);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Este Lote n�o pode ser exclu�do!");
		}
	}

	public List<Lote> filtrados(LoteFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lote> criteriaQuery = builder.createQuery(Lote.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Lote> loteRoot = criteriaQuery.from(Lote.class);
		loteRoot.fetch("produto", JoinType.INNER);

		if (StringUtils.isNotBlank(filtro.getNumero())) {
			predicates.add(builder.equal(builder.lower(loteRoot.get("numero")), filtro.getNumero().toLowerCase()));
		}

		if (filtro.getFornecedor() != null) {
			predicates.add(builder.equal(loteRoot.get("fornecedor"), filtro.getFornecedor()));
		}

		if (filtro.getDataValidadeInicial() != null && filtro.getDataValidadeFinal() != null) {

			predicates.add(builder.between(loteRoot.get("validade"), filtro.getDataValidadeInicial(),
					filtro.getDataValidadeFinal()));
		}

		criteriaQuery.select(loteRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(loteRoot.get("idLote")));

		TypedQuery<Lote> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public List<Lote> porNumero(String numero, Produto produto) {
		return this.manager
				.createQuery("select l from Lote l inner join fetch l.produto p "
						+ "where numero like :numero and p =:produto", Lote.class)
				.setParameter("numero", numero.toUpperCase() + "%").setParameter("produto", produto).getResultList();
	}

	public List<Lote> porProduto(Produto produto) {
		Calendar dataAtual = Calendar.getInstance();
		return this.manager
				.createQuery("select l from Lote l inner join fetch l.produto p "
						+ "where p =:produto and l.validade >= :dataAtual order by l.validade desc", Lote.class)
				.setParameter("produto", produto).setParameter("dataAtual", dataAtual).getResultList();
	}
	
	//USADO NO ACERTO DE ESTOQUE, ASSIM TR�S TODOS OS ESTOQUES POSITIVOS DO BANCO
	public List<Lote> porProdutoAcerto(Produto produto) {
		return this.manager
				.createQuery("select l from Lote l inner join fetch l.produto p "
						+ "where p =:produto and l.quantidade > 0 order by l.validade desc", Lote.class)
				.setParameter("produto", produto).getResultList();
	}

	public List<Lote> porProdutoEntrada(Produto produto) {
		Calendar dataAtual = Calendar.getInstance();
		return this.manager
				.createQuery("select l from Lote l inner join fetch l.produto p "
						+ "where p =:produto and l.validade >= :dataAtual order by l.validade desc", Lote.class)
				.setParameter("produto", produto).setParameter("dataAtual", dataAtual).getResultList();
	}

	public List<Lote> porNomeProduto(String nome, Calendar dataAtual) {
		return this.manager
				.createQuery("select l from Lote l inner join fetch l.produto p "
						+ "where p.nome like :nome and l.quantidade > 0 and validade > :dataAtual order by l.validade asc", Lote.class)
				.setParameter("nome", "%" + nome + "%").setParameter("dataAtual", dataAtual).getResultList();
	}

	public Lote porId(Integer idLote) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lote> criteriaQuery = builder.createQuery(Lote.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Lote> loteRoot = criteriaQuery.from(Lote.class);
		loteRoot.fetch("produto", JoinType.INNER);

		if (idLote != null) {
			predicates.add(builder.equal(loteRoot.get("idLote"), idLote));
		}

		criteriaQuery.select(loteRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(loteRoot.get("idLote")));

		TypedQuery<Lote> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	public List<Lote> lotesComEstoque() {
		Calendar dataAtual = Calendar.getInstance();
		return this.manager
				.createQuery("SELECT l FROM Lote l INNER JOIN FETCH l.produto p " +
						"WHERE l.quantidade > 0 AND l.validade > :dataAtual " +
						"ORDER BY p.nome, l.validade ASC", Lote.class)
				.setParameter("dataAtual", dataAtual)
				.getResultList();
	}

	public List<Lote> porNomeProdutoComEstoque(String nome) {
		Calendar dataAtual = Calendar.getInstance();
		return this.manager
				.createQuery("SELECT l FROM Lote l INNER JOIN FETCH l.produto p " +
						"WHERE p.nome LIKE :nome AND l.quantidade > 0 AND l.validade > :dataAtual " +
						"ORDER BY p.nome, l.validade ASC", Lote.class)
				.setParameter("nome", "%" + nome.toUpperCase() + "%")
				.setParameter("dataAtual", dataAtual)
				.getResultList();
	}

	public List<Lote> lotesValidosPorProduto(String nomeProduto) {
		Calendar dataAtual = Calendar.getInstance();
		return this.manager
				.createQuery("SELECT l FROM Lote l INNER JOIN FETCH l.produto p " +
						"WHERE LOWER(p.nome) LIKE :nome AND l.quantidade > 0 AND l.validade > :dataAtual " +
						"ORDER BY p.nome, l.validade ASC", Lote.class)
				.setParameter("nome", "%" + nomeProduto.toLowerCase() + "%")
				.setParameter("dataAtual", dataAtual)
				.getResultList();
	}

	public Integer quantidadeRetiradaDoLote(Integer idLote) {
		// Soma as quantidades retiradas através de RetiradaMedicamento
		Long quantidadeRetiradaMedicamento = this.manager
				.createQuery("SELECT COALESCE(SUM(rm.quantidade), 0) FROM RetiradaMedicamento rm WHERE rm.lote.idLote = :idLote", Long.class)
				.setParameter("idLote", idLote)
				.getSingleResult();

		// Soma as quantidades retiradas através de TermoItraconazol
		Long quantidadeRetiradaTermo = this.manager
				.createQuery("SELECT COALESCE(SUM(ti.quantidadeRetirada), 0) FROM TermoItraconazol ti WHERE ti.lote.idLote = :idLote", Long.class)
				.setParameter("idLote", idLote)
				.getSingleResult();

		return Integer.valueOf((int)(quantidadeRetiradaMedicamento + quantidadeRetiradaTermo));
	}
}
