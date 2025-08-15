package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Marca;
import br.gov.pr.guaira.animalys.service.MarcaService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroMarcaBean implements Serializable{

	private static final long serialVersionUID = 1L;

	public CadastroMarcaBean() {
		this.marca = new Marca();
	}
	private Marca marca;
	@Inject
	private MarcaService marcaService;
	
	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public void salvar() {
		this.marcaService.salvar(this.marca);
		FacesUtil.addInfoMessage("Marca cadastrada com sucesso!");
		limpar();
	}
	
	private void limpar(){
		this.marca = new Marca();
	}
	
	public boolean isEditando() {
		return this.marca.getIdMarca() != null;
	}
}
