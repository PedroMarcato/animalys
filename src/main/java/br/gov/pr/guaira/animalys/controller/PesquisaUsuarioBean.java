package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Usuario;
import br.gov.pr.guaira.animalys.filter.UsuarioFilter;
import br.gov.pr.guaira.animalys.repository.Usuarios;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaUsuarioBean implements Serializable{

	private static final long serialVersionUID = 1L;

	public PesquisaUsuarioBean() {
		this.usuarioFilter = new UsuarioFilter();
		this.usuariosFiltrados = new ArrayList<>();
	}
	
	@Inject
	private Usuarios usuarios;
	
	private UsuarioFilter usuarioFilter;
	private Usuario usuarioSelecionado;
	private List<Usuario> usuariosFiltrados;

	public UsuarioFilter getUsuarioFilter() {
		return usuarioFilter;
	}

	public void setUsuarioFilter(UsuarioFilter usuarioFilter) {
		this.usuarioFilter = usuarioFilter;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public List<Usuario> getUsuariosFiltrados() {
		return usuariosFiltrados;
	}

	public void setUsuariosFiltrados(List<Usuario> usuariosFiltrados) {
		this.usuariosFiltrados = usuariosFiltrados;
	}
	
	public void excluir(){
		usuarios.remover(usuarioSelecionado);
		usuariosFiltrados.remove(usuarioSelecionado);
		
		FacesUtil.addInfoMessage("Usuário " + usuarioSelecionado.getNome() + " excluído com sucesso!");
	}
	
	public void pesquisar(){
		usuariosFiltrados = usuarios.filtrados(usuarioFilter);
	}
	
}
