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

import br.gov.pr.guaira.animalys.entity.Marca;
import br.gov.pr.guaira.animalys.filter.MarcaFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class Marcas implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Marca guardar(Marca marca) {
		return manager.merge(marca);
	}

	@Transactional
	public void remover(Marca servico) throws NegocioException {
		try {
			servico = porId(servico.getIdMarca());
			manager.remove(servico);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Esta Marca não pode ser excluída!");
		}

	}
	

	public List<Marca> porNome(String nome) {
		return this.manager
				.createQuery("from Marca where upper(nome) like :nome order by nome asc", Marca.class)
					.setParameter("nome", "%" + nome.toUpperCase() + "%").getResultList();
	}
	
	public List<Marca> marcasCadastradas() {
		return this.manager.createQuery("from Marca order by nome asc ", Marca.class)
				.getResultList();
	}
	
	public List<Marca> filtrados(MarcaFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Marca> criteriaQuery = builder.createQuery(Marca.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Marca> marcaRoot = criteriaQuery.from(Marca.class);
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(
					builder.like(builder.lower(marcaRoot.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
		}

		criteriaQuery.select(marcaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(marcaRoot.get("idMarca")));

		TypedQuery<Marca> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public Marca porId(Integer idProfissional) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Marca> criteriaQuery = builder.createQuery(Marca.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Marca> profissaoRoot = criteriaQuery.from(Marca.class);
		
		if (idProfissional != null) {
			predicates.add(
					builder.equal(profissaoRoot.get("idMarca"), idProfissional));
		}

		criteriaQuery.select(profissaoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(profissaoRoot.get("idMarca")));

		TypedQuery<Marca> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}
}
