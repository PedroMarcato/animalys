package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.Marca;
import br.gov.pr.guaira.animalys.repository.Marcas;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class MarcaService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private Marcas marcas;

	@Transactional
	public Marca salvar(Marca marca) {
		return marcas.guardar(marca);
	}
}
