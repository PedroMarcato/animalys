package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.AcertoEstoque;
import br.gov.pr.guaira.animalys.repository.AcertosEstoques;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class AcertoEstoqueService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private AcertosEstoques estoques;

	@Transactional
	public AcertoEstoque salvar(AcertoEstoque acertoEstoque) {
		return estoques.guardar(acertoEstoque);
	}
}
