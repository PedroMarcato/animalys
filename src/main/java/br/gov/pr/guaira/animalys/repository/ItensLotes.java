package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.gov.pr.guaira.animalys.entity.Atendimento;
import br.gov.pr.guaira.animalys.entity.ItemLoteAtendimento;
import br.gov.pr.guaira.animalys.entity.TipoProduto;

public class ItensLotes implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;


	public List<ItemLoteAtendimento> porAtendimento(Atendimento atendimento) {
		return this.manager.createQuery("select il from ItemLoteAtendimento il inner join fetch il.atendimento a " +
				"where a =:atendimento", ItemLoteAtendimento.class)
				.setParameter("atendimento", atendimento).getResultList();
	}
	
	public List<ItemLoteAtendimento> porTipoProduto(TipoProduto tipo) {
		return this.manager.createQuery("select il from ItemLoteAtendimento il inner join fetch il.lote l " +
				" inner join fetch l.produto p where p.tipoProduto =:tipo", ItemLoteAtendimento.class)
				.setParameter("tipo", tipo).getResultList();
	}
}
