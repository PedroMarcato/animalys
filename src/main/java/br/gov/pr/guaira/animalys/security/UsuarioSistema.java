package br.gov.pr.guaira.animalys.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import br.gov.pr.guaira.animalys.entity.Usuario;

public class UsuarioSistema extends User {

	public UsuarioSistema(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getNomeUsuario(), usuario.getSenha(), authorities);
		this.usuario = usuario;
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
