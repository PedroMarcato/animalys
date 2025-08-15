package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.TipoAcerto;
import br.gov.pr.guaira.animalys.filter.TipoAcertoFilter;
import br.gov.pr.guaira.animalys.repository.TipoAcertos;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaTipoAcertoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public PesquisaTipoAcertoBean() {
		this.tipoAcertoFilter = new TipoAcertoFilter();
	}

	private TipoAcertoFilter tipoAcertoFilter;
	private TipoAcerto tipoAcertoSelecionado;
	private List<TipoAcerto> tiposAcertosFiltrados;
	@Inject
	private TipoAcertos tiposAcertos;

	public TipoAcertoFilter getTipoAcertoFilter() {
		return tipoAcertoFilter;
	}
	public void setTipoAcertoFilter(TipoAcertoFilter tipoAcertoFilter) {
		this.tipoAcertoFilter = tipoAcertoFilter;
	}
	public List<TipoAcerto> getTiposAcertosFiltrados() {
		return tiposAcertosFiltrados;
	}
		
	public TipoAcerto getTipoAcertoSelecionado() {
		return tipoAcertoSelecionado;
	}
	public void setTipoAcertoSelecionado(TipoAcerto tipoAcertoSelecionado) {
		this.tipoAcertoSelecionado = tipoAcertoSelecionado;
	}
	public void pesquisar() {
		this.tiposAcertosFiltrados = this.tiposAcertos.filtrados(this.tipoAcertoFilter);
	}
	
	public void excluir() {
		try {
			this.tiposAcertosFiltrados.remove(this.tipoAcertoSelecionado);
			this.tiposAcertos.remover(this.tipoAcertoSelecionado);
			FacesUtil.addInfoMessage("Tipo de Acerto excluído com sucesso!");
		}catch (NegocioException e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Este tipo de acerto não pode ser excluído!");
		}

	}
}
