package br.gov.pr.guaira.animalys.controller;

import java.io.IOException;
//import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
//import java.util.UUID;
//import java.nio.file.*;

import javax.annotation.PostConstruct;
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import br.gov.pr.guaira.animalys.repository.*;
//import br.gov.pr.guaira.animalys.service.AnimalService;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Contato;
import br.gov.pr.guaira.animalys.entity.Endereco;
import br.gov.pr.guaira.animalys.entity.Especie;
import br.gov.pr.guaira.animalys.entity.ModalidadeSolicitante;
import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.entity.Raca;
import br.gov.pr.guaira.animalys.entity.Sexo;
import br.gov.pr.guaira.animalys.entity.Solicitacao;
import br.gov.pr.guaira.animalys.entity.Status;
import br.gov.pr.guaira.animalys.report.GerarComprovante;
import br.gov.pr.guaira.animalys.security.Seguranca;
import br.gov.pr.guaira.animalys.service.SolicitacaoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;
import br.gov.pr.guaira.animalys.entity.FotoAnimal;
import br.gov.pr.guaira.animalys.service.FotoService;
import br.gov.pr.guaira.animalys.service.NegocioException;

@Named
@ViewScoped
public class CadastroAnimalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public CadastroAnimalBean() {
		this.animal = new Animal();
		this.proprietario = new Proprietario();
		this.contato = new Contato();
		this.endereco = new Endereco();
		this.animaisAdicionados = new ArrayList<>();
		this.dataNascimentoCalendar = Calendar.getInstance();
		this.cpf = "";
		this.habilitaCampos = true;
		this.solicitacao = new Solicitacao();
	}

	@PostConstruct
	public void inicializar() {
		this.dataAtual = Calendar.getInstance();
		this.carregaEspecies();
		// this.especiesCadastradas = especies.especiesCadastradas(); // CORRIGIDO
		// this.racasCadastradas = racas.racasCadastradas(); // CORRIGIDO
	}

	private Animal animal;
	private Animal animalSelecionado;
	private Solicitacao solicitacao;
	private Proprietario proprietario;
	private Contato contato;
	private Endereco endereco;
	private Especie especieSelecionada;
	private Date dataNascimento;
	private Calendar dataNascimentoCalendar;
	private Calendar dataAtual;
	private String cpf;
	private Integer protocolo;
	private boolean habilitaCampos;
	private List<Animal> animaisAdicionados;
	private List<Animal> animaisAtendidos;
	private List<Especie> especiesCadastradas;
	private List<Raca> racasCadastradas;
	private String fotoPreview;
	private UploadedFile arquivoFoto;
	private UploadedFile fotoUpload;

	@Inject
	private Racas racas;
	@Inject
	private Especies especies;
	@Inject
	private Proprietarios proprietarios;
	@Inject
	private Cidades cidades;
	@Inject
	private SolicitacaoService solicitacaoService;
	@Inject
	private Solicitacoes solicitacoes;
	@Inject
	private Seguranca usuarioLogado;
	@Inject
	private FotoService fotoService;
	@Inject
	private AnimalDAO animalDAO;

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public Proprietario getProprietario() {
		return proprietario;
	}

	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Racas getRacas() {
		return racas;
	}

	public Especies getEspecies() {
		return especies;
	}

	public Animal getAnimalSelecionado() {
		return animalSelecionado;
	}

	public void setAnimalSelecionado(Animal animalSelecionado) {
		this.animalSelecionado = animalSelecionado;
	}

	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public Sexo[] getSexos() {
		return Sexo.values();
	}

	public UploadedFile getArquivoFoto() {
		return arquivoFoto;
	}

	public void setArquivoFoto(UploadedFile arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}

	public List<Especie> getEspeciesCadastradas() {
		return especiesCadastradas;
	}

	public List<Raca> getRacasCadastradas() {
		return racasCadastradas;
	}

	public List<Animal> getAnimaisAdicionados() {
		return animaisAdicionados;
	}

	public List<Animal> getAnimaisAtendidos() {
		return animaisAtendidos;
	}

	public void setAnimaisAtendidos(List<Animal> animaisAtendidos) {
		this.animaisAtendidos = animaisAtendidos;
	}

	public Especie getEspecieSelecionada() {
		return especieSelecionada;
	}

	public void setEspecieSelecionada(Especie especieSelecionada) {
		this.especieSelecionada = especieSelecionada;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public boolean isHabilitaCampos() {
		return habilitaCampos;
	}

	public void setHabilitaCampos(boolean habilitaCampos) {
		this.habilitaCampos = habilitaCampos;
	}

	public Integer getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(Integer protocolo) {
		this.protocolo = protocolo;
	}

	public ModalidadeSolicitante[] getModalidadesSolicitante() {
		return ModalidadeSolicitante.values();
	}

	/*
	 * 
	 * public void handleFotoUpload(FileUploadEvent event) {
	 * try (InputStream is = event.getFile().getInputStream()) {
	 * byte[] bytes = is.readAllBytes();
	 * this.fotoBase64 = Base64.getEncoder().encodeToString(bytes);
	 * this.fotoPreview = "data:image/png;base64," + this.fotoBase64;
	 * } catch (IOException e) {
	 * FacesContext.getCurrentInstance().addMessage(null,
	 * new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar imagem",
	 * null));
	 * }
	 * }
	 * 
	 */

	// Método para processar upload de foto
	public void handleFotoUpload(FileUploadEvent event) {
		System.out.println("METODO: handleFotoUpload() EXECUTADO");
		this.fotoUpload = event.getFile();
		System.out.println("METODO: handleFotoUpload() DIZ: Foto recebida: " + fotoUpload.getFileName());
	}

	// Método para limpar campos de foto
	private void limparCamposFoto() {
		System.out.println("METODO: limparCamposFoto() EXECUTADO");
		this.fotoUpload = null;
		this.fotoPreview = null;
		System.out.println("METODO: limparCamposFoto() DIZ: CAMPO DE FOTO LIMPO");
	}

	// Método para obter URL da foto de um animal
	public String obterUrlFoto(Animal animal) {
		if (animal.getTemFoto()) {
			FotoAnimal fotoPrincipal = animal.getFotoPrincipal();
			return fotoService.obterUrlFoto(fotoPrincipal);
		}
		return null;
	}

	public String getFotoPreview() {
		return fotoPreview;
	}

	public void setFotoPreview(String fotoPreview) {
		this.fotoPreview = fotoPreview;
	}

	public UploadedFile getFotoUpload() {
		return fotoUpload;
	}

	public void setFotoUpload(UploadedFile fotoUpload) {
		this.fotoUpload = fotoUpload;
	}

	public void salvar() {

		if (!verificaSolicitacoesEmAberto()) {

			if (!this.animaisAdicionados.isEmpty()) {
				this.solicitacao.setProprietario(this.proprietario);
				this.solicitacao.setData(this.dataAtual);

				this.solicitacao.setAnimais(this.animaisAdicionados);
				this.solicitacao.setStatus(Status.SOLICITADO);

				this.endereco.setCidade(this.cidades.porId(2888));
				this.endereco.setCep("85980-000");

				this.proprietario.setCpf(this.cpf);
				this.proprietario.setEndereco(this.endereco);
				this.proprietario.setContato(this.contato);
				this.proprietario.setAnimais(this.animaisAdicionados);
				this.dataNascimentoCalendar.setTime(this.dataNascimento);
				this.proprietario.setDataNascimento(this.dataNascimentoCalendar);
				this.solicitacao = this.solicitacaoService.salvar(this.solicitacao);

				this.protocolo = this.solicitacao.getIdSolicitacao();
				FacesUtil.addInfoMessage("Animal cadastrado com sucesso!");

				this.chamaDialogComprovante();

				if (this.usuarioLogado.getUsuarioLogado() == null) {
					this.geraComprovante();
				}

				limpar();
				this.cpf = "";
				this.habilitaCampos = true;
			} else {
				FacesUtil.addErrorMessage("Adicione um animal!");
			}

		} else {

			this.habilitaCampos = true;
			this.animaisAdicionados.clear();
			FacesUtil.addErrorMessage("Este CPF já possui uma solicitação em aberto!");
			FacesUtil.addErrorMessage("É permitido uma solicitação por CPF!");
		}

	}

	public void limpar() {
		this.contato = new Contato();
		this.endereco = new Endereco();
		this.proprietario = new Proprietario();
		this.animal = new Animal();
		this.solicitacao = new Solicitacao();
		this.animaisAdicionados.clear();
		this.animaisAdicionados = new ArrayList<>();
		this.dataNascimento = null;
		this.dataNascimentoCalendar = Calendar.getInstance();

	}

	private void carregaEspecies() {
		this.especiesCadastradas = this.especies.especiesCadastradas();
	}

	public void carregaRacasPorEspecie() {
		this.racasCadastradas = this.racas.racasPorEspecie(this.especieSelecionada);
	}

	public boolean isEditando() {
		return this.animal.getIdAnimal() != null;
	}

	public void adicionarAnimal() {
		try {

			System.out.println("METODO: adicionarAnimal() DIZ: - FotoUpload: " + (fotoUpload != null ? fotoUpload.getFileName() : "null"));

			this.animal.setProprietario(this.proprietario);
			this.animal.setStatus(Status.SOLICITADO);
			this.animal.setDataEntrada(this.dataAtual);

			if (this.fotoUpload != null && this.fotoUpload.getSize() > 0) {

				System.out.println("METODO: adicionarAnimal() DIZ: BLOCO DE UPLOAD DE FOTO CHAMADO");
				System.out.println("METODO: adicionarAnimal() DIZ: Upload recebido: " + (fotoUpload != null ? fotoUpload.getFileName() : "null"));

				// Chama o serviço para salvar a foto fisicamente
				System.out.println("METODO: adicionarAnimal() DIZ: SERVICO SALVAR SALVAR FOTO FISICAMENTE CHAMADO");
				FotoAnimal foto = fotoService.salvarFoto(this.fotoUpload, this.animal);
				this.animal.adicionarFoto(foto); // Adiciona a foto à lista de fotos do animal
				this.animal.setFoto(foto.getNomeArquivo()); // Define o nome do arquivo da foto principal no campo
				
			} else {
				System.out.println("METODO: adicionarAnimal() DIZ: FOTO NAO SELECIONADA");
			}

			// Adiciona à lista antes de salvar
			if (this.solicitacao.getModalidade().equals(ModalidadeSolicitante.PESSOA_FISICA)) {
				System.out.println("METODO: adicionarAnimal() DIZ: ADCIONANDO A LISTA ANTES DE SALVAR");
				this.adicionaAnimaisPessoaFisica();
			} else if (this.solicitacao.getModalidade().equals(ModalidadeSolicitante.PROTETOR_INDIVIUAL_ANIMAIS)) {
				this.adicionaAnimaisProtetores();
			}

			// Agora salva o animal no banco
			System.out.println("METODO: adicionarAnimal() DIZ: NOME DA FOTO ANTES DE SALVAR: " + this.animal.getFoto());
			System.out.println("METODO: adicionarAnimal() DIZ: SALVANDO ANIMAL AO BANCO DE DADOS");
			this.animal = animalDAO.guardar(this.animal);

			System.out.println("METODO: adicionarAnimal() DIZ: CHAMANDO METODO: limparCamposFoto()");
			this.limparCamposFoto();

		} catch (NegocioException | IOException e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("METODO: adicionarAnimal() DIZ: Erro ao salvar animal/foto: " + e.getMessage());
		}
		System.out.println("METODO: adicionarAnimal() EXECUTADO");
	}

	public void removerAnimal() {
		this.animaisAdicionados.remove(this.animalSelecionado);
	}

	private void limparAposAdicionar() {
		this.animal = null;
		this.animal = new Animal();
	}

	public void buscaCadastroPeloCPF() {

		if (!this.cpf.equals("___.___.___-__")) {

			try {

				this.proprietario = this.proprietarios.proprietarioPorCPF(this.cpf);
				this.contato = this.proprietario.getContato();
				this.endereco = this.proprietario.getEndereco();
				this.animaisAtendidos = this.proprietario.getAnimais();
				this.dataNascimento = this.proprietario.getDataNascimento().getTime();
				this.habilitaCampos = false;

			} catch (NoResultException e) {
				e.printStackTrace();
				System.out.println("CPF não encontrado!");
				this.habilitaCampos = false;
				limpar();
			}

		} else {
			FacesUtil.addErrorMessage("Informe o CPF!");
		}
	}

	public void geraComprovante() {
		GerarComprovante gera = new GerarComprovante();
		gera.gerar(this.solicitacoes.porId(this.protocolo));
	}

	private void adicionaAnimaisPessoaFisica() {
		if (this.animaisAdicionados.size() < 2) {

			if (!this.animaisAdicionados.contains(this.animal)) {
				this.animal.setDataEntrada(this.dataAtual);
				this.animal.setProprietario(this.proprietario);
				this.animal.setStatus(Status.SOLICITADO);

				this.animaisAdicionados.add(this.animal);

				limparAposAdicionar();
			} else {
				FacesUtil.addErrorMessage("Este animal já está cadastrado!");
			}
		} else {
			FacesUtil.addErrorMessage("É permitido 2 (Dois) animais por solicitação!");
			FacesUtil.addErrorMessage("Após os animais serem atendidos, será liberado o cadastro de novos animais!");
		}
	}

	private void adicionaAnimaisProtetores() {

		if (this.animaisAdicionados.size() < 10) {

			if (!this.animaisAdicionados.contains(this.animal)) {
				this.animal.setDataEntrada(this.dataAtual);
				this.animal.setProprietario(this.proprietario);
				this.animal.setStatus(Status.SOLICITADO);

				this.animaisAdicionados.add(this.animal);

				limparAposAdicionar();
			} else {
				FacesUtil.addErrorMessage("Este animal já está cadastrado!");
			}
		} else {
			FacesUtil.addErrorMessage("É permitido 10 (Dez) animais por solicitação!");
			FacesUtil.addErrorMessage("Após os animais serem atendidos, será liberado o cadastro de novos animais!");
		}
	}

	private boolean verificaSolicitacoesEmAberto() {

		boolean retorno = false;

		if (this.proprietario.getIdProprietario() != null) {

			if (this.solicitacoes.porCpf(Status.SOLICITADO, this.proprietario.getCpf()).size() > 0) {
				retorno = true;
				return retorno;
			} else if (this.solicitacoes.porCpf(Status.CONSULTA_ELETIVA_AGENDADA, this.proprietario.getCpf())
					.size() > 0) {
				retorno = true;
				return retorno;
			} else if (this.solicitacoes.porCpf(Status.AGENDADOCASTRACAO, this.proprietario.getCpf()).size() > 0) {
				retorno = true;
				return retorno;
			}
		}
		return retorno;
	}

	private void chamaDialogComprovante() {

		PrimeFaces.current().executeScript("PF(\'dlgSucesso\').show()");
	}

}