package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Especie;
import br.gov.pr.guaira.animalys.entity.Raca;
import br.gov.pr.guaira.animalys.filter.RacaFilter;
import br.gov.pr.guaira.animalys.repository.Especies;
import br.gov.pr.guaira.animalys.repository.Racas;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaRacaBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public PesquisaRacaBean() {
		this.racaFilter = new RacaFilter();
	}
	
	public void inicializar() {
		this.especiesCadastradas = this.especies.especiesCadastradas();
	}

	private Raca racaSelecionada;
	private RacaFilter racaFilter;
	private List<Raca> racasFiltradas;
	private List<Especie> especiesCadastradas;
	@Inject
	private Racas racas;
	@Inject
	private Especies especies;

	public Raca getRacaSelecionada() {
		return racaSelecionada;
	}

	public void setRacaSelecionada(Raca racaSelecionada) {
		this.racaSelecionada = racaSelecionada;
	}

	public RacaFilter getRacaFilter() {
		return racaFilter;
	}

	public void setRacaFilter(RacaFilter racaFilter) {
		this.racaFilter = racaFilter;
	}

	public List<Raca> getRacasFiltradas() {
		return racasFiltradas;
	}

	public List<Especie> getEspeciesCadastradas() {
		return especiesCadastradas;
	}
	
	public void pesquisar() {
		this.racasFiltradas = this.racas.filtrados(this.racaFilter);
	}
	
	public void excluir() {
		try {
			this.racas.remover(this.racaSelecionada);
			this.racasFiltradas.remove(this.racaSelecionada);
			FacesUtil.addInfoMessage("Excluído com sucesso!");
		}catch (NegocioException e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Esta raça não pode ser excluída!");
		}
		
	
	}
}
