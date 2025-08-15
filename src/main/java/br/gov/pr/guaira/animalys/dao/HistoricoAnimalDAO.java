package br.gov.pr.guaira.animalys.dao;

import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import br.gov.pr.guaira.animalys.dto.HistoricoAnimalDTO;
import br.gov.pr.guaira.animalys.entity.Atendimento;

public class HistoricoAnimalDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;


  @Inject
  private EntityManager em;

  public List<HistoricoAnimalDTO> buscarHistoricoPorAnimal(Integer idAnimal) {
	  
	  String jpql = "SELECT new br.gov.pr.guaira.animalys.dto.HistoricoAnimalDTO(" +
			    "a.idAtendimento, " +
			    "FUNCTION('TO_CHAR', a.data, 'DD/MM/YYYY HH24:MI:SS'), " +
			    "p.nome, pr.nome, an.nome, " +
			    "CAST(a.tipoAtendimento AS string), " +
			    "a.diagnostico, " +
			    "CAST(s.modalidade AS string), " +              // ← CORRIGIDO
			    "CAST(s.status AS string)) " +                  // ← CORRIGIDO
			    "FROM Atendimento a " +
			    "JOIN a.animal an " +
			    "LEFT JOIN a.procedimentos p " +
			    "LEFT JOIN a.profissional pr " +
			    "LEFT JOIN a.solicitacao s " +
			    "WHERE an.idAnimal = :idAnimal " +
			    "ORDER BY a.data DESC";

	  
	 /* String jpql = "SELECT new br.gov.pr.guaira.animalys.dto.HistoricoAnimalDTO(" +
              "FUNCTION('TO_CHAR', a.data, 'DD/MM/YYYY'), p.nome, pr.nome, an.nome, CAST(a.tipoAtendimento AS string)) " +
              "FROM Atendimento a " +
              "JOIN a.animal an " +
              "LEFT JOIN a.procedimentos p " +
              "LEFT JOIN a.profissional pr " +
              "WHERE an.idAnimal = :idAnimal " +
              "ORDER BY a.data DESC"; */

    TypedQuery<HistoricoAnimalDTO> query = em.createQuery(jpql, HistoricoAnimalDTO.class);
    query.setParameter("idAnimal", idAnimal);
    return query.getResultList();
  }
  
  public void removerAtendimento(Integer idAtendimento) {
	    Atendimento atendimento = em.find(Atendimento.class, idAtendimento);
	    if (atendimento != null) {
	        em.remove(atendimento);
	        em.flush();
	    }
	}
}
