package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.Lote;
import br.gov.pr.guaira.animalys.repository.Lotes;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class LoteService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private Lotes lotes;

	@Transactional
	public Lote salvar(Lote lote) {
		return lotes.guardar(lote);
	}
}
