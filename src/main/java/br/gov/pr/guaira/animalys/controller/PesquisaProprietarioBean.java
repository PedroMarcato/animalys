package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.filter.ProprietarioFilter;
import br.gov.pr.guaira.animalys.repository.Proprietarios;

@Named
@ViewScoped
public class PesquisaProprietarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public PesquisaProprietarioBean() {
		this.proprietarioFilter = new ProprietarioFilter();
	}

	private ProprietarioFilter proprietarioFilter;
	private List<Proprietario> proprietariosFiltrados;
	
	@Inject
	private Proprietarios proprietarios;
	
	private Proprietario proprietarioSelecionado;
		
	public ProprietarioFilter getProprietarioFilter() {
		return proprietarioFilter;
	}
	
	public void setProprietarioFilter(ProprietarioFilter proprietarioFilter) {
		this.proprietarioFilter = proprietarioFilter;
	}
	
	public List<Proprietario> getProprietariosFiltrados() {
		return proprietariosFiltrados;
	}
	
	public Proprietario getProprietarioSelecionado() {
		return proprietarioSelecionado;
	}
	
	public void setProprietarioSelecionado(Proprietario proprietarioSelecionado) {
		this.proprietarioSelecionado = proprietarioSelecionado;
	}
	
	public void pesquisar() {
		this.proprietariosFiltrados = this.proprietarios.filtrados(this.proprietarioFilter);
	}
	
	public boolean proprietarioTemDocumentos(Proprietario proprietario) {
		if (proprietario == null || proprietario.getIdProprietario() == null) {
			return false;
		}

		// Verifica se já tem documentos carregados
		if (proprietario.getDocumentos() != null) {
			br.gov.pr.guaira.animalys.entity.DocumentosPessoais docs = proprietario.getDocumentos();
			return (docs.getCardUnico() != null && !docs.getCardUnico().trim().isEmpty()) ||
				   (docs.getDocumentoComFoto() != null && !docs.getDocumentoComFoto().trim().isEmpty()) ||
				   (docs.getComprovanteEndereco() != null && !docs.getComprovanteEndereco().trim().isEmpty());
		}

		return false;
	}
	
	public void selecionarProprietario(Proprietario proprietario) {
		if (proprietario != null && proprietario.getIdProprietario() != null) {
			this.proprietarioSelecionado = proprietarios.porId(proprietario.getIdProprietario());
		}
	}
	
	public boolean proprietarioSelecionadoTemDocumentos() {
		return proprietarioTemDocumentos(this.proprietarioSelecionado);
	}
}
