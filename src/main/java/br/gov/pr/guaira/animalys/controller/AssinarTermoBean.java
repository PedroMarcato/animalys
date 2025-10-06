package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.model.TermoConsulta;
import br.gov.pr.guaira.animalys.repository.Animais;
import br.gov.pr.guaira.animalys.repository.Proprietarios;
import br.gov.pr.guaira.animalys.service.TermoConsultaService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class AssinarTermoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Proprietarios proprietarios;

	@Inject
	private Animais animais;

	@Inject
	private TermoConsultaService termoConsultaService;

	private String cpf;
	private Proprietario proprietario;
	private List<Animal> animaisDoProprietario = new ArrayList<>();
	private Animal animalSelecionado;
	private TermoConsulta termo;
	private boolean proprietarioEncontrado = false;
	private boolean termoPreparado = false;

	public void buscarProprietarioPorCPF() {
		try {
			if (cpf == null || cpf.trim().isEmpty()) {
				FacesUtil.addErrorMessage("Por favor, informe o CPF do proprietário.");
				limparDados();
				return;
			}

			// Remove formatação do CPF
			String cpfLimpo = cpf.replaceAll("[^0-9]", "");
			
			proprietario = proprietarios.proprietarioPorCPF(cpfLimpo);
			
			if (proprietario != null) {
				proprietarioEncontrado = true;
				carregarAnimaisDoProprietario();
				FacesUtil.addInfoMessage("Proprietário encontrado: " + proprietario.getNome());
			} else {
				FacesUtil.addErrorMessage("Nenhum proprietário encontrado com o CPF informado.");
				limparDados();
			}
		} catch (Exception e) {
			FacesUtil.addErrorMessage("Erro ao buscar proprietário: " + e.getMessage());
			limparDados();
		}
	}

	private void carregarAnimaisDoProprietario() {
		if (proprietario != null) {
			animaisDoProprietario = animais.animaisPorProprietario(proprietario);
			if (animaisDoProprietario.isEmpty()) {
				FacesUtil.addInfoMessage("Este proprietário não possui animais cadastrados.");
			}
		}
	}

	public void selecionarAnimal() {
		if (animalSelecionado != null) {
			// Verifica se já existe termo para este animal
			if (termoConsultaService.existeTermoPara(proprietario.getIdProprietario(), animalSelecionado.getIdAnimal())) {
				FacesUtil.addInfoMessage("Já existe um termo de consulta assinado para este animal.");
				return;
			}

			// Prepara o termo com os dados
			termo = termoConsultaService.preparar(proprietario, animalSelecionado);
			termoPreparado = true;
			FacesUtil.addInfoMessage("Termo preparado para o animal: " + animalSelecionado.getNome());
		} else {
			FacesUtil.addErrorMessage("Por favor, selecione um animal.");
		}
	}

	public void assinarTermo() {
		try {
			if (termo == null) {
				FacesUtil.addErrorMessage("Termo não preparado.");
				return;
			}

			// Salva o termo
			termo = termoConsultaService.salvar(termo);
			
			FacesUtil.addInfoMessage("Termo de consulta assinado com sucesso!");
			
			// Limpa os dados após assinar
			limparDados();
			
		} catch (Exception e) {
			FacesUtil.addErrorMessage("Erro ao assinar termo: " + e.getMessage());
		}
	}

	public void novoTermo() {
		limparDados();
	}

	private void limparDados() {
		cpf = null;
		proprietario = null;
		animaisDoProprietario.clear();
		animalSelecionado = null;
		termo = null;
		proprietarioEncontrado = false;
		termoPreparado = false;
	}

	public boolean isProprietarioEncontrado() {
		return proprietarioEncontrado;
	}

	public boolean isTemAnimais() {
		return animaisDoProprietario != null && !animaisDoProprietario.isEmpty();
	}

	public boolean isTemAnimalSelecionado() {
		return animalSelecionado != null;
	}

	public boolean isTermoPreparado() {
		return termoPreparado && termo != null;
	}

	// Getters e Setters
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Proprietario getProprietario() {
		return proprietario;
	}

	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}

	public List<Animal> getAnimaisDoProprietario() {
		return animaisDoProprietario;
	}

	public void setAnimaisDoProprietario(List<Animal> animaisDoProprietario) {
		this.animaisDoProprietario = animaisDoProprietario;
	}

	public Animal getAnimalSelecionado() {
		return animalSelecionado;
	}

	public void setAnimalSelecionado(Animal animalSelecionado) {
		this.animalSelecionado = animalSelecionado;
	}

	public TermoConsulta getTermo() {
		return termo;
	}

	public void setTermo(TermoConsulta termo) {
		this.termo = termo;
	}
}