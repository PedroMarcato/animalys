package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Especie;
import br.gov.pr.guaira.animalys.service.EspecieService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroEspecieBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public CadastroEspecieBean() {
		this.especie = new Especie();
	}

	private Especie especie;
	
	@Inject
	private EspecieService especieService;

	public Especie getEspecie() {
		return especie;
	}

	public void setEspecie(Especie especie) {
		this.especie = especie;
	}
	
	public void salvar() {
		this.especie = this.especieService.salvar(this.especie);
		FacesUtil.addInfoMessage("Espécie cadastrada com sucesso!");
		limpar();
	}
	
	public void limpar() {
		this.especie = new Especie();
	}
	
	public boolean isEditando() {
		return this.especie.getIdEspecie() != null;
	}
}
