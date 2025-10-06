package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.model.TermoConsulta;
import br.gov.pr.guaira.animalys.service.TermoConsultaService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaTermoConsultaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TermoConsultaService termoConsultaService;

	private List<TermoConsulta> termosConsulta;
	private String cpfFiltro;
	private String nomeAnimalFiltro;

	@javax.annotation.PostConstruct
	public void init() {
		// Carrega todos os termos automaticamente ao abrir a página
		pesquisar();
	}

	public void pesquisar() {
		try {
			// Se não há filtros, busca todos
			if ((cpfFiltro == null || cpfFiltro.trim().isEmpty()) && 
				(nomeAnimalFiltro == null || nomeAnimalFiltro.trim().isEmpty())) {
				termosConsulta = termoConsultaService.listarTodos();
			} else {
				termosConsulta = termoConsultaService.filtrarTermos(cpfFiltro, nomeAnimalFiltro);
			}
			
			if (termosConsulta.isEmpty()) {
				FacesUtil.addInfoMessage("Nenhum termo de consulta encontrado com os critérios informados.");
			}
		} catch (Exception e) {
			FacesUtil.addErrorMessage("Erro ao pesquisar termos de consulta: " + e.getMessage());
		}
	}

	public void limpar() {
		cpfFiltro = null;
		nomeAnimalFiltro = null;
		termosConsulta = null;
	}

	// Getters e Setters
	public List<TermoConsulta> getTermosConsulta() {
		return termosConsulta;
	}

	public void setTermosConsulta(List<TermoConsulta> termosConsulta) {
		this.termosConsulta = termosConsulta;
	}

	public String getCpfFiltro() {
		return cpfFiltro;
	}

	public void setCpfFiltro(String cpfFiltro) {
		this.cpfFiltro = cpfFiltro;
	}

	public String getNomeAnimalFiltro() {
		return nomeAnimalFiltro;
	}

	public void setNomeAnimalFiltro(String nomeAnimalFiltro) {
		this.nomeAnimalFiltro = nomeAnimalFiltro;
	}
}