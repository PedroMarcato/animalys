package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Especie;
import br.gov.pr.guaira.animalys.entity.Raca;
import br.gov.pr.guaira.animalys.repository.Especies;
import br.gov.pr.guaira.animalys.service.RacaService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroRacaBean implements Serializable{

	private static final long serialVersionUID = 1L;

	public CadastroRacaBean() {
		this.raca = new Raca();
	}
	
	public void inicializar() {
		this.especiesCadastradas = this.especies.especiesCadastradas();
	}
	
	private List<Especie> especiesCadastradas;
	private Raca raca;
	@Inject
	private RacaService racaService;
	@Inject
	private Especies especies;

	public Raca getRaca() {
		return raca;
	}

	public void setRaca(Raca raca) {
		this.raca = raca;
	}

	public List<Especie> getEspeciesCadastradas() {
		return especiesCadastradas;
	}
	
	public boolean isEditando() {
		return this.raca.getIdRaca() != null;
	}
	public void salvar() {
		this.racaService.salvar(this.raca);
		FacesUtil.addInfoMessage("Raça salva com sucesso!");
		limpar();
	}
	
	public void limpar() {
		this.raca = new Raca();
	}
	
}
