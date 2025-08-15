package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.TipoProduto;
import br.gov.pr.guaira.animalys.repository.TiposProdutos;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class TipoProdutoService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private TiposProdutos tipos;

	@Transactional
	public TipoProduto salvar(TipoProduto tipoProduto) {
		return tipos.guardar(tipoProduto);
	}
}
