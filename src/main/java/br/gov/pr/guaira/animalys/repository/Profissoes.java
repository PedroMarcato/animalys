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

import br.gov.pr.guaira.animalys.entity.Profissao;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class Profissoes implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Profissao guardar(Profissao profissao) {
		return manager.merge(profissao);
	}

	@Transactional
	public void remover(Profissao servico) throws NegocioException {
		try {
			servico = porId(servico.getIdProfissao());
			manager.remove(servico);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Esta Profissao não pode ser excluída!");
		}

	}
	

	public List<Profissao> porNome(String nome) {
		return this.manager
				.createQuery("from Profissao where upper(nome) like :nome order by nome asc", Profissao.class)
					.setParameter("nome", "%" + nome.toUpperCase() + "%").getResultList();
	}

	public Profissao porId(Integer idProfissional) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Profissao> criteriaQuery = builder.createQuery(Profissao.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Profissao> profissaoRoot = criteriaQuery.from(Profissao.class);
		
		if (idProfissional != null) {
			predicates.add(
					builder.equal(profissaoRoot.get("idProfissao"), idProfissional));
		}

		criteriaQuery.select(profissaoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(profissaoRoot.get("idProfissao")));

		TypedQuery<Profissao> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}
}
