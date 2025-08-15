package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.Procedimento;
import br.gov.pr.guaira.animalys.repository.Procedimentos;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class ProcedimentoService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private Procedimentos procedimentos;

	@Transactional
	public Procedimento salvar(Procedimento procedimento) {
		return procedimentos.guardar(procedimento);
	}
}
