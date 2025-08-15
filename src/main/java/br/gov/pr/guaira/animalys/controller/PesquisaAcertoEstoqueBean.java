package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.AcertoEstoque;
import br.gov.pr.guaira.animalys.entity.Lote;
import br.gov.pr.guaira.animalys.entity.TipoAcerto;
import br.gov.pr.guaira.animalys.filter.AcertoEstoqueFilter;
import br.gov.pr.guaira.animalys.repository.AcertosEstoques;
import br.gov.pr.guaira.animalys.repository.TipoAcertos;
import br.gov.pr.guaira.animalys.service.LoteService;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaAcertoEstoqueBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public PesquisaAcertoEstoqueBean() {
		this.acertoFilter = new AcertoEstoqueFilter();
	}

	private AcertoEstoqueFilter acertoFilter;
	private AcertoEstoque acertoEstoqueSelecionado;
	private List<TipoAcerto> tiposAcertosCadastrados;
	private List<AcertoEstoque> acertosEstoquesFiltrados;
	@Inject
	private AcertosEstoques acertosEstoques;
	@Inject
	private TipoAcertos tiposAcertos;
	@Inject
	private LoteService loteService;

	public void inicializar() {
		this.tiposAcertosCadastrados = this.tiposAcertos.tiposCadastrados();
	}

	public AcertoEstoqueFilter getAcertoFilter() {
		return acertoFilter;
	}

	public void setAcertoFilter(AcertoEstoqueFilter acertoFilter) {
		this.acertoFilter = acertoFilter;
	}

	public AcertoEstoque getAcertoEstoqueSelecionado() {
		return acertoEstoqueSelecionado;
	}

	public void setAcertoEstoqueSelecionado(AcertoEstoque acertoEstoqueSelecionado) {
		this.acertoEstoqueSelecionado = acertoEstoqueSelecionado;
	}

	public List<TipoAcerto> getTiposAcertosCadastrados() {
		return tiposAcertosCadastrados;
	}

	public List<AcertoEstoque> getAcertosEstoquesFiltrados() {
		return acertosEstoquesFiltrados;
	}

	public void pesquisar() {
		this.acertosEstoquesFiltrados = this.acertosEstoques.filtrados(this.acertoFilter);
	}

	public void excluir() {
		try {
			this.acertosEstoquesFiltrados.remove(this.acertoEstoqueSelecionado);
			this.voltaEstoque();
			this.acertosEstoques.remover(this.acertoEstoqueSelecionado);
		} catch (NegocioException e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Este acerto não pode ser excluído!");
		}
	}
	
	private void voltaEstoque() {
		
		Lote lote = this.acertoEstoqueSelecionado.getLote();
		
		lote.setQuantidade(this.acertoEstoqueSelecionado.getQtdAnterior());
		
		this.loteService.salvar(lote);
		
	}
}
