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
	private Integer animalIdParaAtender;
	private boolean mostrarConfirmacaoDuplicada = false;

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

	public void solicitarAtendimento(Integer idAnimal) {
		this.animalIdParaAtender = idAnimal;
		// Verifica se já existe atendimento para o animal no dia
		try {
			java.util.Calendar hoje = java.util.Calendar.getInstance();
			hoje.set(java.util.Calendar.HOUR_OF_DAY, 0);
			hoje.set(java.util.Calendar.MINUTE, 0);
			hoje.set(java.util.Calendar.SECOND, 0);
			hoje.set(java.util.Calendar.MILLISECOND, 0);
			java.util.Calendar fim = (java.util.Calendar) hoje.clone();
			fim.set(java.util.Calendar.HOUR_OF_DAY, 23);
			fim.set(java.util.Calendar.MINUTE, 59);
			fim.set(java.util.Calendar.SECOND, 59);
			fim.set(java.util.Calendar.MILLISECOND, 999);
			br.gov.pr.guaira.animalys.entity.Animal animal = this.animais.porId(idAnimal);
			br.gov.pr.guaira.animalys.repository.Atendimentos atendimentosRepo = br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator.getBean(br.gov.pr.guaira.animalys.repository.Atendimentos.class);
			br.gov.pr.guaira.animalys.entity.Atendimento atendimentoExistente = atendimentosRepo.atendimentoPorAnimal(animal, hoje, fim);
			if (atendimentoExistente != null) {
				this.mostrarConfirmacaoDuplicada = true;
				org.primefaces.PrimeFaces.current().executeScript("abrirDialogDuplicado();");
				return;
			}
		} catch (javax.persistence.NoResultException e) {
			// Não existe atendimento, pode seguir normalmente
		}
		// Redireciona direto se não houver duplicidade
		redirecionarParaConsulta();
	}

	public void confirmarAtendimentoDuplicado() {
		this.mostrarConfirmacaoDuplicada = false;
		redirecionarParaConsulta();
	}

	public void cancelarAtendimentoDuplicado() {
		this.mostrarConfirmacaoDuplicada = false;
		this.animalIdParaAtender = null;
	}

	private void redirecionarParaConsulta() {
		try {
			String url = "consultaAnimal.xhtml?animal=" + this.animalIdParaAtender + "&solicitacao=" + this.solicitacao.getIdSolicitacao();
			if (this.mostrarConfirmacaoDuplicada == false) {
				url += "&confirmadoDuplicado=1";
			}
			javax.faces.context.FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isMostrarConfirmacaoDuplicada() {
		return mostrarConfirmacaoDuplicada;
	}
}
