package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.gov.pr.guaira.animalys.entity.Usuario;
import br.gov.pr.guaira.animalys.filter.UsuarioFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class Usuarios implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Usuario guardar(Usuario usuario) {
		return manager.merge(usuario);
	}

	@Transactional
	public void remover(Usuario usuario) throws NegocioException {
		try {
			usuario = porId(usuario.getIdUsuario());
			manager.remove(usuario);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Usuário não pode ser excluído!");
		}

	}

	public List<Usuario> filtrados(UsuarioFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = builder.createQuery(Usuario.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Usuario> logradouroRoot = criteriaQuery.from(Usuario.class);
		// Fetch<Logradouro, Cidade> cidadeJoin = logradouroRoot.fetch("cidade",
		// JoinType.INNER);
		// cidadeJoin.fetch("estado", JoinType.INNER);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.lower(logradouroRoot.get("nomeUsuario")),
					"%" + filtro.getNome().toLowerCase() + "%"));
		}

		criteriaQuery.select(logradouroRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(logradouroRoot.get("nomeUsuario")));

		TypedQuery<Usuario> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public Usuario porId(Integer idUsuario) {
		return manager.find(Usuario.class, idUsuario);
	}

	public Usuario porNomeUsuario(String nomeUsuario) {
		Usuario usuario = null;
		try {
			usuario = this.manager
					.createQuery("from Usuario where upper(nomeUsuario) like :nomeUsuario", Usuario.class)
						.setParameter("nomeUsuario", "%" + nomeUsuario.toUpperCase() + "%").getSingleResult();
		}catch (NoResultException e) {
			
		}
		return usuario;
	}
}
