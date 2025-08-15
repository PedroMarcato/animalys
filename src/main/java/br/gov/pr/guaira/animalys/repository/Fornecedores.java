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

import br.gov.pr.guaira.animalys.entity.Fornecedor;
import br.gov.pr.guaira.animalys.filter.FornecedorFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class Fornecedores implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Fornecedor guardar(Fornecedor fornecedor) {
		return manager.merge(fornecedor);
	}

	@Transactional
	public void remover(Fornecedor servico) throws NegocioException {
		try {
			servico = porId(servico.getIdFornecedor());
			manager.remove(servico);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Este Fornecedor não pode ser excluído!");
		}

	}
	

	public List<Fornecedor> filtrados(FornecedorFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Fornecedor> criteriaQuery = builder.createQuery(Fornecedor.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Fornecedor> fornecedorRoot = criteriaQuery.from(Fornecedor.class);

		if (StringUtils.isNotBlank(filtro.getNomeFantasia())) {
			predicates.add(
					builder.like(builder.lower(fornecedorRoot.get("nomeFantasia")), "%" + filtro.getNomeFantasia().toLowerCase() + "%"));
		}
		if (StringUtils.isNotBlank(filtro.getRazaoSocial())) {
			predicates.add(
					builder.like(builder.lower(fornecedorRoot.get("razaoSocial")), "%" + filtro.getRazaoSocial().toLowerCase() + "%"));
		}
		if (StringUtils.isNotBlank(filtro.getCnpj())) {
			predicates.add(
					builder.like(builder.lower(fornecedorRoot.get("cnpj")), "%" + filtro.getCnpj().toLowerCase() + "%"));
		}

		criteriaQuery.select(fornecedorRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(fornecedorRoot.get("nomeFantasia")));

		TypedQuery<Fornecedor> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public List<Fornecedor> fornecedoresCadastrados() {
		return this.manager.createQuery("from Fornecedor order by nomeFantasia asc ", Fornecedor.class)
				.getResultList();
	}
	
	

	public Fornecedor porId(Integer idFornecedor) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Fornecedor> criteriaQuery = builder.createQuery(Fornecedor.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Fornecedor> fornecedorRoot = criteriaQuery.from(Fornecedor.class);
		fornecedorRoot.fetch("contato", JoinType.INNER);
		fornecedorRoot.fetch("endereco", JoinType.INNER);
		
		if (idFornecedor != null) {
			predicates.add(
					builder.equal(fornecedorRoot.get("idFornecedor"), idFornecedor));
		}

		criteriaQuery.select(fornecedorRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(fornecedorRoot.get("idFornecedor")));

		TypedQuery<Fornecedor> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	public List<Fornecedor> porNome(String nomeFantasia) {
		return this.manager.createQuery("select f from Fornecedor f inner join fetch f.contato c inner join fetch "
				+ "f.endereco e " +
				"where upper(nomeFantasia) like :nomeFantasia ", Fornecedor.class)
				.setParameter("nomeFantasia", nomeFantasia.toUpperCase() + "%")
				.getResultList();
	}
}
