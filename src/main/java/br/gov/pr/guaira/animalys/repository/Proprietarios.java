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

import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.filter.ProprietarioFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class Proprietarios implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Transactional
	public Proprietario guardar(Proprietario proprietario) {
		return manager.merge(proprietario);
	}

	@Transactional
	public void remover(Proprietario proprietario) throws NegocioException {
		try {
			proprietario = porId(proprietario.getIdProprietario());
			manager.remove(proprietario);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Este Proprietario não pode ser excluído!");
		}

	}

	public List<Proprietario> filtrados(ProprietarioFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Proprietario> criteriaQuery = builder.createQuery(Proprietario.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Proprietario> proprietarioRoot = criteriaQuery.from(Proprietario.class);
		
		// Fazer eager loading das entidades relacionadas
		proprietarioRoot.fetch("contato", javax.persistence.criteria.JoinType.LEFT);
		proprietarioRoot.fetch("endereco", javax.persistence.criteria.JoinType.LEFT);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.lower(proprietarioRoot.get("nome")),
					"%" + filtro.getNome().toLowerCase() + "%"));
		}

		if (StringUtils.isNotBlank(filtro.getCpf())) {
			predicates.add(builder.like(builder.lower(proprietarioRoot.get("cpf")),
					"%" + filtro.getCpf().toLowerCase() + "%"));
		}

		criteriaQuery.select(proprietarioRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(proprietarioRoot.get("nome")));

		TypedQuery<Proprietario> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public Proprietario proprietarioPorCPF(String cpf) {
		// Primeiro tenta buscar com o CPF exatamente como veio
		List<Proprietario> resultados = this.manager.createQuery("from Proprietario p left join fetch p.contato left join fetch p.endereco left join fetch p.documentos where p.cpf =:cpf order by p.nome asc ", Proprietario.class)
				.setParameter("cpf", cpf).getResultList();
		
		if (!resultados.isEmpty()) {
			return resultados.get(0);
		}
		
		// Se não encontrou, tenta com formatação (se o CPF veio apenas com números)
		if (cpf != null && cpf.matches("\\d{11}")) {
			String cpfFormatado = cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
			resultados = this.manager.createQuery("from Proprietario p left join fetch p.contato left join fetch p.endereco left join fetch p.documentos where p.cpf =:cpf order by p.nome asc ", Proprietario.class)
					.setParameter("cpf", cpfFormatado).getResultList();
			
			if (!resultados.isEmpty()) {
				return resultados.get(0);
			}
		}
		
		// Se o CPF veio formatado, tenta apenas com números
		if (cpf != null && cpf.contains(".")) {
			String cpfSemFormatacao = cpf.replaceAll("[^0-9]", "");
			resultados = this.manager.createQuery("from Proprietario p left join fetch p.contato left join fetch p.endereco left join fetch p.documentos where p.cpf =:cpf order by p.nome asc ", Proprietario.class)
					.setParameter("cpf", cpfSemFormatacao).getResultList();
			
			if (!resultados.isEmpty()) {
				return resultados.get(0);
			}
		}
		
		// Se chegou até aqui, não encontrou o CPF - lança exception para manter compatibilidade
		throw new javax.persistence.NoResultException("CPF não encontrado: " + cpf);
	}

	public Proprietario porId(Integer idProprietario) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Proprietario> criteriaQuery = builder.createQuery(Proprietario.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Proprietario> proprietarioRoot = criteriaQuery.from(Proprietario.class);
		
		// Fazer eager loading das entidades relacionadas
		proprietarioRoot.fetch("contato", javax.persistence.criteria.JoinType.LEFT);
		proprietarioRoot.fetch("endereco", javax.persistence.criteria.JoinType.LEFT);
		proprietarioRoot.fetch("documentos", javax.persistence.criteria.JoinType.LEFT);

		if (idProprietario != null) {
			predicates.add(builder.equal(proprietarioRoot.get("idProprietario"), idProprietario));
		}

		criteriaQuery.select(proprietarioRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(proprietarioRoot.get("idProprietario")));

		TypedQuery<Proprietario> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}
	
	public List<Proprietario> todos() {
		return this.manager.createQuery("from Proprietario p left join fetch p.contato left join fetch p.endereco order by p.nome asc", Proprietario.class)
				.getResultList();
	}
}
