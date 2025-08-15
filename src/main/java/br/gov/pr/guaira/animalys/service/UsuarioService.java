package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.Usuario;
import br.gov.pr.guaira.animalys.repository.Usuarios;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class UsuarioService implements Serializable{

		private static final long serialVersionUID = 1L;

		@Inject
		private Usuarios usuarios;
		
		@Transactional
		public Usuario salvar(Usuario usuario){
			
			return usuarios.guardar(usuario);
		}
}
