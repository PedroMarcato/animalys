package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.Produto;
import br.gov.pr.guaira.animalys.repository.Produtos;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class ProdutoService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private Produtos produtos;

	@Transactional
	public Produto salvar(Produto produto) {
		return produtos.guardar(produto);
	}
}
