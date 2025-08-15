package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Especie;
import br.gov.pr.guaira.animalys.entity.Procedimento;
import br.gov.pr.guaira.animalys.repository.Especies;
import br.gov.pr.guaira.animalys.service.ProcedimentoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroProcedimentoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public CadastroProcedimentoBean() {
		this.procedimento = new Procedimento();
	}
	
	public void inicializar() {
		this.carregaEspecies();
	}

	private Procedimento procedimento;
	private List<Especie> especiesCadastradas;
	
	@Inject
	private ProcedimentoService procedientoService;
	@Inject
	private Especies especies;
	
	public Procedimento getProcedimento() {
		return procedimento;
	}
	public void setProcedimento(Procedimento procedimento) {
		this.procedimento = procedimento;
	}
		
	public List<Especie> getEspeciesCadastradas() {
		return especiesCadastradas;
	}

	public void setEspeciesCadastradas(List<Especie> especiesCadastradas) {
		this.especiesCadastradas = especiesCadastradas;
	}

	private void carregaEspecies() {
		this.especiesCadastradas = this.especies.especiesCadastradas();
	}
	public void salvar() {
		this.procedientoService.salvar(this.procedimento);
		FacesUtil.addInfoMessage("Procedimento salvo com sucesso!");
		limpar();
	}
	private void limpar() {
		this.procedimento = new Procedimento();
	}
	public boolean isEditando() {
		return this.procedimento.getIdProcedimento() != null;
	}
}
