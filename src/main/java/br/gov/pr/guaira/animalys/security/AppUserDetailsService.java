package br.gov.pr.guaira.animalys.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.gov.pr.guaira.animalys.entity.Grupo;
import br.gov.pr.guaira.animalys.entity.Permissao;
import br.gov.pr.guaira.animalys.entity.Usuario;
import br.gov.pr.guaira.animalys.repository.Usuarios;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String nomeUsuario) throws UsernameNotFoundException {
		Usuarios usuarios = CDIServiceLocator.getBean(Usuarios.class);
		Usuario usuario = usuarios.porNomeUsuario(nomeUsuario);
		User user = null;

		if (usuario != null) {
			user = new UsuarioSistema(usuario, getGrupos(usuario));
		} else {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}

		return user;
	}

	private Collection<? extends GrantedAuthority> getGrupos(Usuario usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Grupo grupo : usuario.getGrupos()) {

			authorities.add(new SimpleGrantedAuthority("ROLE_" + grupo.getNome().toUpperCase()));

			for (Permissao permissao : grupo.getPermissoes()) {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + permissao.getNome().toUpperCase()));
			}
		}

		return authorities;
	}

}
