package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Solicitacao;
import br.gov.pr.guaira.animalys.repository.Animais;

@Named
@ViewScoped
public class NewAtendimentoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public NewAtendimentoBean() {
		
	}

	private Solicitacao solicitacao;
	private Animal animalSelecionado;
	private List<Animal> animaisDoProprietario;

	@Inject
	private Animais animais;
	
	
	public void inicializar() {
		
		this.buscaAnimais();	
		
	}

	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public Animal getAnimalSelecionado() {
		return animalSelecionado;
	}

	public void setAnimalSelecionado(Animal animalSelecionado) {
		this.animalSelecionado = animalSelecionado;
	}

	public List<Animal> getAnimaisDoProprietario() {
		return animaisDoProprietario;
	}

	private void buscaAnimais() {
    this.animaisDoProprietario = this.animais.animaisDaSolicitacao(this.solicitacao.getIdSolicitacao());
	}	
}
