package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.repository.Proprietarios;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class ProprietarioService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private Proprietarios proprietarios;

	@Transactional
	public Proprietario salvar(Proprietario proprietario) {
		return proprietarios.guardar(proprietario);
	}
}
