package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.Especie;
import br.gov.pr.guaira.animalys.repository.Especies;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class EspecieService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private Especies especies;

	@Transactional
	public Especie salvar(Especie especie) {
		return especies.guardar(especie);
	}
}
