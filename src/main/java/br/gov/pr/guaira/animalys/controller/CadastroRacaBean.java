package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Especie;
import br.gov.pr.guaira.animalys.entity.Raca;
import br.gov.pr.guaira.animalys.repository.Especies;
import br.gov.pr.guaira.animalys.repository.Racas;
import br.gov.pr.guaira.animalys.service.RacaService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroRacaBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer idRaca;

	public CadastroRacaBean() {
		this.raca = new Raca();
	}
	
	public void inicializar() {
		this.especiesCadastradas = this.especies.especiesCadastradas();
		
		if (idRaca != null) {
			this.raca = racas.porId(idRaca);
		}
	}
	
	private List<Especie> especiesCadastradas;
	private Raca raca;
	@Inject
	private RacaService racaService;
	@Inject
	private Especies especies;
	@Inject
	private Racas racas;

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
		this.raca = this.racaService.salvar(this.raca);
		
		if (idRaca == null) {
			FacesUtil.addInfoMessage("Raça cadastrada com sucesso!");
			limpar();
		} else {
			FacesUtil.addInfoMessage("Raça atualizada com sucesso!");
		}
	}
	
	public void limpar() {
		this.raca = new Raca();
		this.idRaca = null;
	}
	
	public Integer getIdRaca() {
		return idRaca;
	}
	
	public void setIdRaca(Integer idRaca) {
		this.idRaca = idRaca;
	}
}
