package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.Raca;
import br.gov.pr.guaira.animalys.repository.Racas;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class RacaService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private Racas racas;

	@Transactional
	public Raca salvar(Raca raca) {
		return racas.guardar(raca);
	}
}
