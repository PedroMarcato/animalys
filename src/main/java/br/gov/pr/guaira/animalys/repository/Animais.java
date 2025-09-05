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
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.entity.Raca;
import br.gov.pr.guaira.animalys.entity.Sexo;
import br.gov.pr.guaira.animalys.entity.Status;
import br.gov.pr.guaira.animalys.filter.AnimalFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class Animais implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Transactional
	public Animal guardar(Animal animal) {
	System.out.println("[DEBUG] REPOSITÓRIO: Status recebido para salvar: " + animal.getStatus());
	Animal a = manager.merge(animal);
	manager.flush();
	System.out.println("[DEBUG] REPOSITÓRIO: Status após merge e flush: " + a.getStatus());
	return a;
	}

	public List<Animal> porSolicitacao(Integer idSolicitacao) {
		String jpql = "SELECT DISTINCT a " +
				"FROM Atendimento at " +
				"JOIN at.animal a " +
				"WHERE at.solicitacao.idSolicitacao = :id";
		return manager.createQuery(jpql, Animal.class)
				.setParameter("id", idSolicitacao)
				.getResultList();
	}

	@Transactional
	public void remover(Animal animal) throws NegocioException {
		try {
			animal = porId(animal.getIdAnimal());
			manager.remove(animal);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new NegocioException("Este Animal não pode ser excluído!");
		}

	}

	public List<Animal> filtrados(AnimalFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Animal> criteriaQuery = builder.createQuery(Animal.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Animal> animalRoot = criteriaQuery.from(Animal.class);
		Fetch<Animal, Proprietario> proprietarioRoot = animalRoot.fetch("proprietario", JoinType.INNER);
		proprietarioRoot.fetch("contato", JoinType.INNER);
		proprietarioRoot.fetch("endereco", JoinType.INNER);

		if (StringUtils.isNotBlank(filtro.getNomeProprietario())) {
			predicates.add(builder.like(builder.lower(animalRoot.get("proprietario").get("nome")),
					"%" + filtro.getNomeProprietario().toLowerCase() + "%"));
		}

		if (StringUtils.isNotBlank(filtro.getCpfProprietario())) {
			predicates.add(builder.like(builder.lower(animalRoot.get("proprietario").get("cpf")),
					"%" + filtro.getCpfProprietario().toLowerCase() + "%"));
		}

		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(
					builder.like(builder.lower(animalRoot.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
		}

		if (filtro.getDataInicioCalendar() != null) {

			Calendar data1 = Calendar.getInstance();

			filtro.getDataInicioCalendar().set(Calendar.HOUR, 00);
			filtro.getDataInicioCalendar().set(Calendar.MINUTE, 01);
			filtro.getDataInicioCalendar().set(Calendar.SECOND, 01);

			data1.set(Calendar.HOUR, 23);
			data1.set(Calendar.HOUR, 59);
			data1.set(Calendar.HOUR, 59);

			predicates
					.add(builder.between(animalRoot.get("dataAgendaCastracao"), filtro.getDataInicioCalendar(), data1));
		}

		if (filtro.getDataInicioCalendar() != null && filtro.getDataFimCalendar() != null) {
			filtro.getDataInicioCalendar().set(Calendar.HOUR, 00);
			filtro.getDataInicioCalendar().set(Calendar.MINUTE, 01);
			filtro.getDataInicioCalendar().set(Calendar.SECOND, 01);

			filtro.getDataFimCalendar().set(Calendar.HOUR, 23);
			filtro.getDataFimCalendar().set(Calendar.MINUTE, 59);
			filtro.getDataFimCalendar().set(Calendar.SECOND, 59);

			predicates.add(builder.between(animalRoot.get("dataAgendaCastracao"), filtro.getDataInicioCalendar(),
					filtro.getDataFimCalendar()));
		}

		if (filtro.getDataInicioCastracaoCalendar() != null) {

			Calendar data1 = Calendar.getInstance();

			filtro.getDataInicioCastracaoCalendar().set(Calendar.HOUR, 00);
			filtro.getDataInicioCastracaoCalendar().set(Calendar.MINUTE, 01);
			filtro.getDataInicioCastracaoCalendar().set(Calendar.SECOND, 01);

			data1.set(Calendar.HOUR, 23);
			data1.set(Calendar.HOUR, 59);
			data1.set(Calendar.HOUR, 59);

			predicates
					.add(builder.between(animalRoot.get("dataAgendaCastracao"), filtro.getDataInicioCastracaoCalendar(),
							data1));
		}

		if (filtro.getDataInicioCastracaoCalendar() != null && filtro.getDataFimCastracaoCalendar() != null) {
			filtro.getDataInicioCastracaoCalendar().set(Calendar.HOUR, 00);
			filtro.getDataInicioCastracaoCalendar().set(Calendar.MINUTE, 01);
			filtro.getDataInicioCastracaoCalendar().set(Calendar.SECOND, 01);

			filtro.getDataFimCastracaoCalendar().set(Calendar.HOUR, 23);
			filtro.getDataFimCastracaoCalendar().set(Calendar.MINUTE, 59);
			filtro.getDataFimCastracaoCalendar().set(Calendar.SECOND, 59);
			predicates
					.add(builder.between(animalRoot.get("dataAgendaCastracao"), filtro.getDataInicioCastracaoCalendar(),
							filtro.getDataFimCastracaoCalendar()));
		}

		if (filtro.getStatus() != null) {
			predicates.add(builder.equal(animalRoot.get("status"), filtro.getStatus()));
		}

		criteriaQuery.select(animalRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(animalRoot.get("dataAgendaCastracao")));

		TypedQuery<Animal> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public List<Animal> animaisCastrados(AnimalFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Animal> criteriaQuery = builder.createQuery(Animal.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Animal> animalRoot = criteriaQuery.from(Animal.class);
		Fetch<Animal, Raca> racaRoot = animalRoot.fetch("raca", JoinType.INNER);
		racaRoot.fetch("especie", JoinType.INNER);
		Fetch<Animal, Proprietario> proprietarioRoot = animalRoot.fetch("proprietario", JoinType.INNER);
		proprietarioRoot.fetch("contato", JoinType.INNER);
		proprietarioRoot.fetch("endereco", JoinType.INNER);

		if (StringUtils.isNotBlank(filtro.getNomeProprietario())) {
			predicates.add(builder.like(builder.lower(animalRoot.get("proprietario").get("nome")),
					"%" + filtro.getNomeProprietario().toLowerCase() + "%"));
		}

		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(
					builder.like(builder.lower(animalRoot.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
		}

		if (filtro.getDataInicioCalendar() != null) {

			Calendar data1 = Calendar.getInstance();

			filtro.getDataInicioCalendar().set(Calendar.HOUR, 00);
			filtro.getDataInicioCalendar().set(Calendar.MINUTE, 01);
			filtro.getDataInicioCalendar().set(Calendar.SECOND, 01);

			data1.set(Calendar.HOUR, 23);
			data1.set(Calendar.HOUR, 59);
			data1.set(Calendar.HOUR, 59);

			predicates
					.add(builder.between(animalRoot.get("dataCastracao"), filtro.getDataInicioCalendar(), data1));
		}

		if (filtro.getDataInicioCalendar() != null && filtro.getDataFimCalendar() != null) {
			predicates.add(builder.between(animalRoot.get("dataCastracao"), filtro.getDataInicioCalendar(),
					filtro.getDataFimCalendar()));
		}

		if (filtro.getStatus() != null) {
			predicates.add(builder.equal(animalRoot.get("status"), filtro.getStatus()));
		}

		criteriaQuery.select(animalRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.desc(animalRoot.get("dataCastracao")));

		TypedQuery<Animal> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public List<Animal> animaisCadastrados() {
		return this.manager.createQuery("from Animal order by nome asc ", Animal.class).getResultList();
	}

	public List<Animal> animaisPorMes(String mes, Sexo sexo) {
		return this.manager
				.createQuery("SELECT a FROM Animal AS a WHERE to_char(a.dataCastracao, 'MM') = :mes AND sexo = :sexo",
						Animal.class)
				.setParameter("mes", mes).setParameter("sexo", sexo).getResultList();
	}

	public List<Animal> animaisPorProprietario(Proprietario proprietario) {
		return this.manager
				.createQuery("Select a from Animal a inner join fetch a.raca r inner join fetch "
						+ "r.especie e where a.proprietario =:proprietario order by a.nome asc ", Animal.class)
				.setParameter("proprietario", proprietario).getResultList();
	}

	public List<Animal> animaisPorSolicitacao(Integer solicitacao) {
		String jpql = "SELECT DISTINCT a FROM Atendimento at " +
				"JOIN at.animal a " +
				"LEFT JOIN FETCH a.raca r " +
				"LEFT JOIN FETCH r.especie e " +
				"LEFT JOIN FETCH a.proprietario p " +
				"WHERE at.solicitacao.idSolicitacao = :solicitacao";

		return this.manager.createQuery(jpql, Animal.class)
				.setParameter("solicitacao", solicitacao)
				.getResultList();
	}

	public List<Animal> animaisPorProprietario(Proprietario proprietario, Status status) {
		return this.manager.createQuery(
				"Select a from Animal a inner join fetch a.raca r inner join fetch "
						+ "r.especie e where a.proprietario =:proprietario and a.status =:status order by a.nome asc ",
				Animal.class).setParameter("proprietario", proprietario).setParameter("status", status).getResultList();
	}

	public List<Animal> animaisAgendadoCastracao(Status status) {
		return this.manager
				.createQuery("Select a from Animal a inner join fetch a.raca r inner join fetch "
						+ "r.especie e where a.status =:status order by a.dataAgendaCastracao asc ", Animal.class)
				.setParameter("status", status).getResultList();
	}

	public Animal porDataAgendaCastracao(Calendar data) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Animal> criteriaQuery = builder.createQuery(Animal.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Animal> animalRoot = criteriaQuery.from(Animal.class);
		Fetch<Animal, Proprietario> proprietarioRoot = animalRoot.fetch("proprietario", JoinType.INNER);
		proprietarioRoot.fetch("endereco", JoinType.INNER);
		proprietarioRoot.fetch("contato", JoinType.INNER);
		Fetch<Animal, Raca> racaRoot = animalRoot.fetch("raca", JoinType.INNER);
		racaRoot.fetch("especie", JoinType.INNER);

		if (data != null) {
			predicates.add(builder.equal(animalRoot.get("dataAgendaCastracao"), data));
		}

		criteriaQuery.select(animalRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(animalRoot.get("idAnimal")));

		TypedQuery<Animal> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	public Animal porId(Integer idAnimal) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Animal> criteriaQuery = builder.createQuery(Animal.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Root<Animal> animalRoot = criteriaQuery.from(Animal.class);
		Fetch<Animal, Proprietario> proprietarioRoot = animalRoot.fetch("proprietario", JoinType.INNER);
		proprietarioRoot.fetch("endereco", JoinType.INNER);
		proprietarioRoot.fetch("contato", JoinType.INNER);
		Fetch<Animal, Raca> racaRoot = animalRoot.fetch("raca", JoinType.INNER);
		racaRoot.fetch("especie", JoinType.INNER);

		if (idAnimal != null) {
			predicates.add(builder.equal(animalRoot.get("idAnimal"), idAnimal));
		}

		criteriaQuery.select(animalRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(animalRoot.get("idAnimal")));

		TypedQuery<Animal> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	public List<Animal> animaisDaSolicitacao(Integer idSolicitacao) {
		String jpql = "SELECT DISTINCT a FROM Solicitacao s " +
				"JOIN s.animais a " +
				"LEFT JOIN FETCH a.raca r " +
				"LEFT JOIN FETCH r.especie e " +
				"WHERE s.idSolicitacao = :idSolicitacao";
		return manager.createQuery(jpql, Animal.class)
				.setParameter("idSolicitacao", idSolicitacao)
				.getResultList();
	}

}
