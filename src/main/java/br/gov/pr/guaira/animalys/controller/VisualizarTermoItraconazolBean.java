package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.TermoItraconazol;
import br.gov.pr.guaira.animalys.report.GerarTermoItraconazol;
import br.gov.pr.guaira.animalys.repository.TermosItraconazol;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class VisualizarTermoItraconazolBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TermosItraconazol termosItraconazol;

	private TermoItraconazol termo;
	private Long termoId;

	@PostConstruct
	public void inicializar() {
		if (termoId != null) {
			carregarTermo();
		}
	}

	private void carregarTermo() {
		try {
			// Converte Long para Integer para o método porId
			Integer id = termoId != null ? termoId.intValue() : null;
			termo = termosItraconazol.porId(id);
			
			if (termo == null) {
				FacesUtil.addErrorMessage("Termo não encontrado.");
			}
		} catch (Exception e) {
			FacesUtil.addErrorMessage("Erro ao carregar o termo: " + e.getMessage());
		}
	}

	public TermoItraconazol getTermo() {
		return termo;
	}

	public void setTermo(TermoItraconazol termo) {
		this.termo = termo;
	}

	public Long getTermoId() {
		return termoId;
	}

	public void setTermoId(Long termoId) {
		this.termoId = termoId;
	}
	
	public void imprimirPDF() {
		try {
			if (termo == null) {
				FacesUtil.addErrorMessage("Nenhum termo foi carregado.");
				return;
			}
			
			GerarTermoItraconazol relatorio = new GerarTermoItraconazol();
			relatorio.gerar(termo);
			
		} catch (Exception e) {
			FacesUtil.addErrorMessage("Erro ao gerar PDF: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
