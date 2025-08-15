package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.Grupo;
import br.gov.pr.guaira.animalys.repository.Grupos;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class GrupoService implements Serializable{

		private static final long serialVersionUID = 1L;

		@Inject
		private Grupos grupos;
		
		@Transactional
		public Grupo salvar(Grupo grupo){
			
			return grupos.guardar(grupo);
		}
}
