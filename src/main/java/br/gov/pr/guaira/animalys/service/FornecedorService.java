package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.Fornecedor;
import br.gov.pr.guaira.animalys.repository.Fornecedores;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class FornecedorService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private Fornecedores fornecedores;

	@Transactional
	public Fornecedor salvar(Fornecedor fornecedor) {
		return fornecedores.guardar(fornecedor);
	}
}
