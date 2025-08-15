package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.TipoAcerto;
import br.gov.pr.guaira.animalys.repository.TipoAcertos;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class TipoAcertoService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private TipoAcertos tipos;

	@Transactional
	public TipoAcerto salvar(TipoAcerto tipo) {
		return tipos.guardar(tipo);
	}
}
