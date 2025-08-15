package br.gov.pr.guaira.animalys.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.gov.pr.guaira.animalys.entity.Permissao;

public class Permissoes implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Permissao> permissoesCadastradas() {
		return this.manager.createQuery("from Permissao", Permissao.class)
				.getResultList();
	}
	
	public Permissao porId(Integer idPermissao) {
		return manager.find(Permissao.class, idPermissao);
	}
}
