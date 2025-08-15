package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.TipoAcerto;
import br.gov.pr.guaira.animalys.service.TipoAcertoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroTipoAcertoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public CadastroTipoAcertoBean() {
		this.tipoAcerto = new TipoAcerto();
	}

	private TipoAcerto tipoAcerto;
	@Inject
	private TipoAcertoService tipoService;

	public TipoAcerto getTipoAcerto() {
		return tipoAcerto;
	}
	public void setTipoAcerto(TipoAcerto tipoAcerto) {
		this.tipoAcerto = tipoAcerto;
	}
	
	public void salvar() {
		this.tipoService.salvar(this.tipoAcerto);
		FacesUtil.addInfoMessage("Tipo de Acerto cadastrado com sucesso!");
		limpar();
	}
	
	private void limpar() {
		this.tipoAcerto = new TipoAcerto();
	}
	
	public boolean isEditando() {
		return this.tipoAcerto.getIdTipoAcerto() != null;
	}
}
