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

import br.gov.pr.guaira.animalys.entity.Estado;
import br.gov.pr.guaira.animalys.filter.EstadoFilter;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class Estados implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Estado guardar(Estado estado) {
		return manager.merge(estado);
	}
	@Transactional
	public void remover(Estado estado){
		try{
			estado = porId(estado.getIdEstado());
			manager.remove(estado);
			manager.flush();
		}catch(PersistenceException e){
			e.printStackTrace();
			throw new NegocioException("Estado não pode ser excluído!");
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<Estado> filtrados(EstadoFilter filtro) {

		Session session = (Session) manager;
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(Estado.class);
		// se quiser colocar mais filtros para busca coloca aqui, usando o
		// criteria. Aula 12.8
		// where nome like '%joao'
		
		if(StringUtils.isNotBlank(filtro.getUf())){
			criteria.add(Restrictions.eq("uf", filtro.getUf()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(),
					MatchMode.ANYWHERE));
		}

		return criteria.addOrder(Order.asc("nome")).list();
	}
	
	public List<Estado> estadosCadastrados() {
		
		return this.manager.createQuery("from Estado", Estado.class)
				.getResultList();
	}

	public Estado porId(Integer idEstado) {
		return manager.find(Estado.class, idEstado);
	}
	
}
