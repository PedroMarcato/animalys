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

import br.gov.pr.guaira.animalys.entity.Profissional;
import br.gov.pr.guaira.animalys.filter.ProfissionalFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;
import br.gov.pr.guaira.animalys.dto.ProfissionalSelectDTO;

public class Profissionais implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Profissional guardar(Profissional profissional) {
		return manager.merge(profissional);
	}

	@Transactional
	public void remover(Profissional servico) throws NegocioException {
		try {
			servico = porId(servico.getIdProfissional());
			manager.remove(servico);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Esta Profissional não pode ser excluída!");
		}

	}

	public List<Profissional> filtrados(ProfissionalFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Profissional> criteriaQuery = builder.createQuery(Profissional.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Profissional> profissionalRoot = criteriaQuery.from(Profissional.class);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.lower(profissionalRoot.get("nome")),
					"%" + filtro.getNome().toLowerCase() + "%"));
		}

		criteriaQuery.select(profissionalRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(profissionalRoot.get("nome")));

		TypedQuery<Profissional> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public Profissional porId(Integer idProfissional) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Profissional> criteriaQuery = builder.createQuery(Profissional.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Profissional> profissionalRoot = criteriaQuery.from(Profissional.class);
		profissionalRoot.fetch("endereco", JoinType.INNER);
		profissionalRoot.fetch("contato", JoinType.INNER);
		profissionalRoot.fetch("profissao", JoinType.INNER);
		if (idProfissional != null) {
			predicates.add(builder.equal(profissionalRoot.get("idProfissional"), idProfissional));
		}

		criteriaQuery.select(profissionalRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(profissionalRoot.get("idProfissional")));

		TypedQuery<Profissional> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	public List<Profissional> porNome(String nome) {
		return this.manager
				.createQuery("from Profissional where upper(nome) like :nome order by nome asc", Profissional.class)
				.setParameter("nome", "%" + nome.toUpperCase() + "%").getResultList();
	}

	public List<Profissional> profissionaisCadastrados() {
		TypedQuery<Profissional> query = this.manager.createQuery(
			"SELECT DISTINCT p FROM Profissional p " +
			"LEFT JOIN FETCH p.endereco " +
			"LEFT JOIN FETCH p.contato " +
			"LEFT JOIN FETCH p.profissao " +
			"ORDER BY p.nome ASC", Profissional.class);
		
		List<Profissional> profissionais = query.getResultList();
		
		// Força a inicialização de todos os relacionamentos lazy para evitar LazyInitializationException
		for (Profissional prof : profissionais) {
			if (prof.getEndereco() != null) {
				prof.getEndereco().getLogradouro(); // Força inicialização
			}
			if (prof.getContato() != null) {
				prof.getContato().getTelefone(); // Força inicialização
			}
			if (prof.getProfissao() != null) {
				prof.getProfissao().getNome(); // Força inicialização
			}
		}
		
		return profissionais;
	}

	public List<ProfissionalSelectDTO> profissionaisParaSelect() {
		TypedQuery<Object[]> query = this.manager.createQuery(
			"SELECT p.idProfissional, p.nome FROM Profissional p ORDER BY p.nome ASC", Object[].class);
		
		List<Object[]> results = query.getResultList();
		List<ProfissionalSelectDTO> dtos = new ArrayList<>();
		
		for (Object[] result : results) {
			Integer id = (Integer) result[0];
			String nome = (String) result[1];
			dtos.add(new ProfissionalSelectDTO(id, nome));
		}
		
		return dtos;
	}
}
