package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.model.TermoConsulta;
import br.gov.pr.guaira.animalys.service.TermoConsultaService;
import br.gov.pr.guaira.animalys.report.GerarTermoConsulta;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class VisualizarTermoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TermoConsultaService termoConsultaService;
	
	private Integer termoId;
	private TermoConsulta termo;

	@PostConstruct
	public void init() {
		// Inicialização básica - o termo será carregado via viewAction
	}

	public void inicializar() {
		if (termoId != null) {
			try {
				termo = termoConsultaService.buscarPorId(termoId);
				
				if (termo == null) {
					FacesUtil.addErrorMessage("Termo não encontrado.");
				}
			} catch (Exception e) {
				FacesUtil.addErrorMessage("Erro ao buscar termo: " + e.getMessage());
			}
		} else {
			FacesUtil.addErrorMessage("ID do termo não informado.");
		}
	}

	// Getters e Setters
	public Integer getTermoId() {
		return termoId;
	}

	public void setTermoId(Integer termoId) {
		this.termoId = termoId;
	}

	public TermoConsulta getTermo() {
		return termo;
	}

	public void setTermo(TermoConsulta termo) {
		this.termo = termo;
	}

	public void imprimirPdf() {
		if (this.termo == null) {
			FacesUtil.addErrorMessage("Nenhum termo carregado para impressão.");
			return;
		}

		try {
			GerarTermoConsulta gerador = new GerarTermoConsulta();
			gerador.gerar(this.termo);
		} catch (Exception e) {
			FacesUtil.addErrorMessage("Erro ao gerar PDF do termo: " + e.getMessage());
			e.printStackTrace();
		}
	}
}