package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.Atendimento;
import br.gov.pr.guaira.animalys.repository.Atendimentos;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class AtendimentoService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private Atendimentos atendimentos;

	@Transactional
	public Atendimento salvar(Atendimento atendimento) {
		return atendimentos.guardar(atendimento);
	}
}
