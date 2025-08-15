package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.EntradaProduto;
import br.gov.pr.guaira.animalys.repository.EntradasProdutos;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class EntradaProdutoService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private EntradasProdutos entradas;

	@Transactional
	public EntradaProduto salvar(EntradaProduto entrada) {
		return entradas.guardar(entrada);
	}
}
