package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.gov.pr.guaira.animalys.model.TermoConsulta;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class TermosConsulta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public TermoConsulta guardar(TermoConsulta termo) {
		return manager.merge(termo);
	}

	@Transactional
	public void remover(TermoConsulta termo) {
		termo = porId(termo.getId());
		manager.remove(termo);
	}

	public TermoConsulta porId(Long id) {
		List<TermoConsulta> resultado = manager.createQuery(
				"SELECT t FROM TermoConsulta t " +
				"LEFT JOIN FETCH t.proprietario p " +
				"LEFT JOIN FETCH p.endereco e " +
				"LEFT JOIN FETCH e.cidade " +
				"LEFT JOIN FETCH p.contato " +
				"LEFT JOIN FETCH t.animal a " +
				"LEFT JOIN FETCH a.raca ra " +
				"LEFT JOIN FETCH ra.especie " +
				"WHERE t.id = :id", TermoConsulta.class)
				.setParameter("id", id)
				.getResultList();

		return resultado.isEmpty() ? null : resultado.get(0);
	}

	public List<TermoConsulta> porProprietario(Integer proprietarioId) {
		TypedQuery<TermoConsulta> query = manager
				.createQuery("from TermoConsulta t where t.proprietario.id = :proprietarioId order by t.dataCadastro desc", TermoConsulta.class);
		query.setParameter("proprietarioId", proprietarioId);
		return query.getResultList();
	}

	public List<TermoConsulta> porAnimal(Integer animalId) {
		TypedQuery<TermoConsulta> query = manager
				.createQuery("from TermoConsulta t where t.animal.id = :animalId order by t.dataCadastro desc", TermoConsulta.class);
		query.setParameter("animalId", animalId);
		return query.getResultList();
	}

	public TermoConsulta porProprietarioEAnimal(Integer proprietarioId, Integer animalId) {
		try {
			TypedQuery<TermoConsulta> query = manager
					.createQuery("from TermoConsulta t where t.proprietario.id = :proprietarioId and t.animal.id = :animalId", TermoConsulta.class);
			query.setParameter("proprietarioId", proprietarioId);
			query.setParameter("animalId", animalId);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public TermoConsulta buscarTermoPara(Integer proprietarioId, Integer animalId) {
		try {
			TypedQuery<TermoConsulta> query = manager
					.createQuery("from TermoConsulta t where t.proprietario.id = :proprietarioId and t.animal.id = :animalId", TermoConsulta.class);
			query.setParameter("proprietarioId", proprietarioId);
			query.setParameter("animalId", animalId);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public boolean existeTermoPara(Integer proprietarioId, Integer animalId) {
		TypedQuery<Long> query = manager
				.createQuery("select count(t) from TermoConsulta t where t.proprietario.id = :proprietarioId and t.animal.id = :animalId", Long.class);
		query.setParameter("proprietarioId", proprietarioId);
		query.setParameter("animalId", animalId);
		return query.getSingleResult() > 0;
	}

	public List<TermoConsulta> todos() {
		return manager.createQuery("from TermoConsulta t order by t.dataCadastro desc", TermoConsulta.class)
				.getResultList();
	}

	public List<TermoConsulta> filtrarPorCpfEAnimal(String cpf, String nomeAnimal) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("from TermoConsulta t where 1=1 ");

		if (cpf != null && !cpf.trim().isEmpty()) {
			// Busca tanto com formatação quanto sem formatação
			jpql.append("and (t.cpfProprietario like :cpf or replace(replace(t.cpfProprietario, '.', ''), '-', '') like :cpfSemFormatacao) ");
		}

		if (nomeAnimal != null && !nomeAnimal.trim().isEmpty()) {
			jpql.append("and lower(t.nomeAnimal) like lower(:nomeAnimal) ");
		}

		jpql.append("order by t.dataCadastro desc");

		TypedQuery<TermoConsulta> query = manager.createQuery(jpql.toString(), TermoConsulta.class);

		if (cpf != null && !cpf.trim().isEmpty()) {
			// Parâmetro com formatação original (como digitado)
			query.setParameter("cpf", "%" + cpf + "%");
			// Parâmetro sem formatação (apenas números)
			query.setParameter("cpfSemFormatacao", "%" + cpf.replace(".", "").replace("-", "") + "%");
		}

		if (nomeAnimal != null && !nomeAnimal.trim().isEmpty()) {
			query.setParameter("nomeAnimal", "%" + nomeAnimal + "%");
		}

		return query.getResultList();
	}
}