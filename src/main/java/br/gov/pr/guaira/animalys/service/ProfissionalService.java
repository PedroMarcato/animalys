package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.Profissional;
import br.gov.pr.guaira.animalys.repository.Profissionais;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class ProfissionalService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private Profissionais profissionais;

	@Transactional
	public Profissional salvar(Profissional profissional) {
		return profissionais.guardar(profissional);
	}
}
