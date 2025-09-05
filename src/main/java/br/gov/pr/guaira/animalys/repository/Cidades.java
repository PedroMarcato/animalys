package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.pr.guaira.animalys.entity.Cidade;
import br.gov.pr.guaira.animalys.entity.Estado;
import br.gov.pr.guaira.animalys.filter.CidadeFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class Cidades implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager manager;

	public Cidade guardar(Cidade cidade) {
		return manager.merge(cidade);
	}
	@Transactional
	public void remover(Cidade cidade) throws NegocioException{
		try{
			cidade = porId(cidade.getId());
			manager.remove(cidade);
			manager.flush();
		}catch(PersistenceException e){
			e.printStackTrace();
			throw new NegocioException("Cidade não pode ser excluída!");
		}
		
	}
	@SuppressWarnings("unchecked")
	public List<Cidade> filtrados(CidadeFilter filtro) {

		Session session = (Session) manager;
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(Cidade.class);
		// se quiser colocar mais filtros para busca coloca aqui, usando o
		// criteria. Aula 12.8
		// where nome like '%joao'
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(),
					MatchMode.ANYWHERE));
		}

		return criteria.addOrder(Order.asc("nome")).list();
	}
	
	public Cidade porId(Integer idCidade) {
		return manager.find(Cidade.class, idCidade);
	}
	
	public List<Cidade> porNome(String nome) {
		return this.manager.createQuery("select c from Cidade c " +
				"join fetch c.estado e " +
				"where upper(c.nome) like :nome " +
				"order by c.nome", Cidade.class)
				.setParameter("nome", nome.toUpperCase() + "%")
				.getResultList();
	}

	public List<Cidade> cidadeByEstado(Estado estado){
		return manager.createQuery("from Cidade where estado = :estado", Cidade.class).
				setParameter("estado", estado).getResultList();
		
	}
	

}
