package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DualListModel;

import br.gov.pr.guaira.animalys.entity.Grupo;
import br.gov.pr.guaira.animalys.entity.Permissao;
import br.gov.pr.guaira.animalys.repository.Permissoes;
import br.gov.pr.guaira.animalys.service.GrupoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class GrupoBean implements Serializable{

	private static final long serialVersionUID = 1L;

	public GrupoBean(){
		
	}
	
	public void inicializar() {
		if (this.grupo == null) {
			limpar();
		} else {
			List<Permissao> lista = permissoes.permissoesCadastradas();
			lista.removeAll(grupo.getPermissoes());

			listaPermissoes = new DualListModel<>(lista, new ArrayList<>(this.grupo.getPermissoes()));
			
		}
	}
	
	private Grupo grupo;
	private DualListModel<Permissao> listaPermissoes;
	
	@Inject
	private GrupoService grupoService;
	@Inject
	private Permissoes permissoes;

	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
		
	public DualListModel<Permissao> getListaPermissoes() {
		return listaPermissoes;
	}
	
	public void setListaPermissoes(DualListModel<Permissao> listaPermissoes) {
		this.listaPermissoes = listaPermissoes;
	}

	public void salvar(){
		this.grupo.setPermissoes(this.listaPermissoes.getTarget());
		this.grupo = this.grupoService.salvar(this.grupo);
		limpar();
		FacesUtil.addInfoMessage("Grupo salvo com Sucesso!");
	}
	
	public void limpar(){
		this.grupo = new Grupo();
		listaPermissoes = new DualListModel<>(this.permissoes.permissoesCadastradas(), new ArrayList<>());
	}
	
	public boolean isEditando(){
		return this.grupo.getIdGrupo() != null;
	}
}
