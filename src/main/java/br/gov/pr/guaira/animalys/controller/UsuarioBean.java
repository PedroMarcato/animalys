package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DualListModel;

import br.gov.pr.guaira.animalys.entity.CriarHashSenha;
import br.gov.pr.guaira.animalys.entity.Grupo;
import br.gov.pr.guaira.animalys.entity.Profissional;
import br.gov.pr.guaira.animalys.entity.Usuario;
import br.gov.pr.guaira.animalys.repository.Grupos;
import br.gov.pr.guaira.animalys.repository.Profissionais;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.service.UsuarioService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public UsuarioBean() {
		this.criarHashSenha = new CriarHashSenha();
	}

	private Usuario usuario;
	private String senha;
	String senhaComHash;
	private CriarHashSenha criarHashSenha;
	private DualListModel<Grupo> listaGrupos;

	@Inject
	private UsuarioService cadastroUsuarioService;

	@Inject
	private Grupos grupos;
	@Inject
	private Profissionais profissionais;

	public void inicializar() {
		if (usuario == null) {
			limpar();
		} else {
			List<Grupo> lista = grupos.gruposCadastrados();
			lista.removeAll(usuario.getGrupos());

			listaGrupos = new DualListModel<>(lista, new ArrayList<>(usuario.getGrupos()));

		}
	}

	public void limpar() {
		this.usuario = new Usuario();
		this.senha = null;
		listaGrupos = new DualListModel<>(grupos.gruposCadastrados(), new ArrayList<>());
	}

	public void salvar() {
		try {

			if (!this.senha.isEmpty()) {
				if (this.senha.length() > 5) {

					this.senhaComHash = this.criarHashSenha.adicionaCriptografado(this.senha);
					this.usuario.setSenha(senhaComHash);
					salva();

				} else {
					FacesUtil.addErrorMessage("A senha deve ter no mínimo 6 caracteres!");
				}
			} else if (isEditando()) {
				salva();
			} else {
				FacesUtil.addErrorMessage("Informe a senha!");
			}
		} catch (NegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	private void salva() {
		usuario.setGrupos(listaGrupos.getTarget());
		cadastroUsuarioService.salvar(this.usuario);
		limpar();
		FacesUtil.addInfoMessage("Usuário salvo com sucesso!");
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isEditando() {
		return usuario.getIdUsuario() != null;
	}

	public DualListModel<Grupo> getListaGrupos() {
		return listaGrupos;
	}

	public void setListaGrupos(DualListModel<Grupo> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Profissional> completarProfissional(String descricao) {
		return this.profissionais.porNome(descricao);
	}

}
