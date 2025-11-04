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
import javax.faces.context.FacesContext;
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.gov.pr.guaira.animalys.repository.*;
//import br.gov.pr.guaira.animalys.service.AnimalService;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Contato;
import br.gov.pr.guaira.animalys.entity.DocumentosPessoais;
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
		this.cardUnicoFile = null;
		this.documentoComFotoFile = null;
		this.comprovanteEnderecoFile = null;
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

	// Lista para armazenar os nomes dos arquivos enviados
	private String cardUnicoFile;
	private String documentoComFotoFile;
	private String comprovanteEnderecoFile;

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
	@Inject
	private EntityManager manager;

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

	// Método para logar os nomes dos arquivos enviados
	public void logArquivosDocumentos() {
		System.out.println("[LOG] Card Único: " + (cardUnicoFile != null ? cardUnicoFile : "NÃO ENVIADO"));
		System.out.println("[LOG] Comprovante de Endereço: " + (comprovanteEnderecoFile != null ? comprovanteEnderecoFile : "NÃO ENVIADO"));
		System.out.println("[LOG] Documento com Foto: " + (documentoComFotoFile != null ? documentoComFotoFile : "NÃO ENVIADO"));
	}

	public String getCardUnicoFile() {
		return cardUnicoFile;
	}

	public void setCardUnicoFile(String cardUnicoFile) {
		this.cardUnicoFile = cardUnicoFile;
	}

	public String getDocumentoComFotoFile() {
		return documentoComFotoFile;
	}

	public void setDocumentoComFotoFile(String documentoComFotoFile) {
		this.documentoComFotoFile = documentoComFotoFile;
	}

	public String getComprovanteEnderecoFile() {
		return comprovanteEnderecoFile;
	}

	public void setComprovanteEnderecoFile(String comprovanteEnderecoFile) {
		this.comprovanteEnderecoFile = comprovanteEnderecoFile;
	}

	// Métodos para processar upload de cada documento
	public void handleCardUnicoUpload(org.primefaces.event.FileUploadEvent event) {
		System.out.println("[LOG] handleCardUnicoUpload chamado");
		String fileName = salvarArquivoUpload(event, "Card Único");
		if (fileName != null) {
			this.cardUnicoFile = fileName;
			System.out.println("[DEBUG] cardUnicoFile setado: " + this.cardUnicoFile);
		} else {
			System.out.println("[DEBUG] cardUnicoFile NÃO setado (null)");
		}
	}

	public void handleDocumentoComFotoUpload(org.primefaces.event.FileUploadEvent event) {
		System.out.println("[LOG] handleDocumentoComFotoUpload chamado");
		String fileName = salvarArquivoUpload(event, "Documento com Foto");
		if (fileName != null) {
			this.documentoComFotoFile = fileName;
			System.out.println("[DEBUG] documentoComFotoFile setado: " + this.documentoComFotoFile);
		} else {
			System.out.println("[DEBUG] documentoComFotoFile NÃO setado (null)");
		}
	}

	public void handleComprovanteEnderecoUpload(org.primefaces.event.FileUploadEvent event) {
		System.out.println("[LOG] handleComprovanteEnderecoUpload chamado");
		String fileName = salvarArquivoUpload(event, "Comprovante de Endereço");
		if (fileName != null) {
			this.comprovanteEnderecoFile = fileName;
			System.out.println("[DEBUG] comprovanteEnderecoFile setado: " + this.comprovanteEnderecoFile);
		} else {
			System.out.println("[DEBUG] comprovanteEnderecoFile NÃO setado (null)");
		}
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
		System.out.println("[LOG] ========== INICIANDO MÉTODO SALVAR ==========");
		System.out.println("[LOG] Modalidade da solicitação: " + (solicitacao != null && solicitacao.getModalidade() != null ? solicitacao.getModalidade() : "NULL"));
		System.out.println("[LOG] Quantidade de animais adicionados: " + (animaisAdicionados != null ? animaisAdicionados.size() : 0));
		
		Proprietario propAtual;
		// Busca o proprietário pelo CPF, se existir
		if (cpf != null && !cpf.trim().isEmpty()) {
			try {
				propAtual = proprietarios.proprietarioPorCPF(cpf);
				System.out.println("[LOG] Proprietario encontrado: id=" + propAtual.getIdProprietario());
			} catch (Exception e) {
				System.out.println("[LOG] Proprietario não encontrado, usando objeto do bean");
				propAtual = proprietario;
			}
		} else {
			System.out.println("[LOG] CPF vazio, usando objeto do bean");
			propAtual = proprietario;
		}

		try {
			manager.getTransaction().begin();

			// Associar contato e endereço do formulário ao proprietário atual (para novos e existentes)
			propAtual.setContato(this.contato);
			propAtual.setEndereco(this.endereco);

			// Se o contato está vazio, remove do proprietário para evitar erro de referência transiente
			if (propAtual.getContato() != null) {
				boolean contatoVazio = true;
				if (propAtual.getContato().getEmail() != null && !propAtual.getContato().getEmail().trim().isEmpty())
					contatoVazio = false;
				if (propAtual.getContato().getTelefone() != null && !propAtual.getContato().getTelefone().trim().isEmpty())
					contatoVazio = false;
				if (propAtual.getContato().getCelular() != null && !propAtual.getContato().getCelular().trim().isEmpty())
					contatoVazio = false;
				if (!contatoVazio && propAtual.getContato().getIdContato() == null) {
					manager.persist(propAtual.getContato());
					manager.flush();
					System.out.println("[LOG] Contato persistido: id=" + propAtual.getContato().getIdContato() + ", Email: " + propAtual.getContato().getEmail() + ", Telefone: " + propAtual.getContato().getTelefone() + ", Celular: " + propAtual.getContato().getCelular());
					Contato contatoGerenciado = manager.find(Contato.class, propAtual.getContato().getIdContato());
					propAtual.setContato(contatoGerenciado);
				} else if (contatoVazio) {
					propAtual.setContato(null);
					System.out.println("[LOG] Contato vazio removido do proprietário.");
				}
			}
			// Persistir endereço antes do proprietário se novo
			if (propAtual.getEndereco() != null && propAtual.getEndereco().getIdEndereco() == null) {
				// Validação básica para endereço (campos obrigatórios)
				if (propAtual.getEndereco().getLogradouro() == null || propAtual.getEndereco().getLogradouro().trim().isEmpty()) {
					throw new NegocioException("O logradouro é obrigatório.");
				}
				if (propAtual.getEndereco().getNumero() == null || propAtual.getEndereco().getNumero().trim().isEmpty()) {
					throw new NegocioException("O número do endereço é obrigatório.");
				}
				if (propAtual.getEndereco().getBairro() == null || propAtual.getEndereco().getBairro().trim().isEmpty()) {
					throw new NegocioException("O bairro é obrigatório.");
				}
				manager.persist(propAtual.getEndereco());
				manager.flush();
				System.out.println("[LOG] Endereço persistido: id=" + propAtual.getEndereco().getIdEndereco() + ", Logradouro: " + propAtual.getEndereco().getLogradouro() + ", Numero: " + propAtual.getEndereco().getNumero() + ", Bairro: " + propAtual.getEndereco().getBairro());
				Endereco enderecoGerenciado = manager.find(Endereco.class, propAtual.getEndereco().getIdEndereco());
				propAtual.setEndereco(enderecoGerenciado);
			}

			// Set dataNascimento for both new and existing owners
			if (dataNascimento != null) {
				dataNascimentoCalendar.setTime(dataNascimento);
				propAtual.setDataNascimento(dataNascimentoCalendar);
			}

			// Set modalidade from solicitacao
			if (solicitacao != null && solicitacao.getModalidade() != null) {
				propAtual.setModalidade(solicitacao.getModalidade());
				System.out.println("[LOG] Modalidade definida no proprietário: " + solicitacao.getModalidade());
			}

			// Persistir ou atualizar proprietário (antes dos animais)
			boolean isNovoProprietario = (propAtual.getIdProprietario() == null);
			if (isNovoProprietario) {
				// Validações manuais para novos proprietários para evitar campos nulos no banco
				if (propAtual.getNome() == null || propAtual.getNome().trim().isEmpty()) {
					throw new NegocioException("O nome do proprietário é obrigatório.");
				}
				if (propAtual.getRg() == null || propAtual.getRg().trim().isEmpty()) {
					throw new NegocioException("O RG do proprietário é obrigatório.");
				}
				if (dataNascimento == null) {
					throw new NegocioException("A data de nascimento é obrigatória.");
				}
				// NIS é opcional, não validar
				System.out.println("[LOG] Validações para novo proprietário passadas. Nome: " + propAtual.getNome() + ", RG: " + propAtual.getRg() + ", DataNascimento: " + propAtual.getDataNascimento());
				manager.persist(propAtual);
				manager.flush();
				System.out.println("[LOG] Proprietário persistido. id=" + propAtual.getIdProprietario());
			} else {
				// Para proprietário existente, atualiza campos do formulário (embora desabilitados, para consistência)
				propAtual.setNome(this.proprietario.getNome());
				propAtual.setRg(this.proprietario.getRg());
				propAtual.setNis(this.proprietario.getNis());
				propAtual = manager.merge(propAtual);
				manager.flush();
				System.out.println("[LOG] Proprietário existente atualizado. id=" + propAtual.getIdProprietario());
			}

			// Garantir que todos os animais adicionados referenciem o proprietário gerenciado
			List<Animal> animaisGerenciados = new ArrayList<>();
			for (Animal a : animaisAdicionados) {
				// Set proprietário gerenciado
				a.setProprietario(propAtual);
				Animal animalGerenciado;
				if (a.getIdAnimal() != null) {
					animalGerenciado = manager.merge(a);  // Merge se já tem ID
				} else {
					manager.persist(a);
					animalGerenciado = a;
				}
				manager.flush();
				animaisGerenciados.add(animalGerenciado);
				System.out.println("[LOG] Animal gerenciado: id=" + animalGerenciado.getIdAnimal());
			}

			if (!verificaSolicitacoesEmAberto()) {
				System.out.println("[LOG] Nenhuma solicitação em aberto");
				propAtual.setCpf(cpf);
				propAtual.setAnimais(animaisGerenciados);
				System.out.println("[LOG] Proprietario pronto para salvar: id=" + propAtual.getIdProprietario());
				solicitacao.setProprietario(propAtual);
				solicitacao.setData(dataAtual);
				solicitacao.setAnimais(animaisGerenciados);
				solicitacao.setStatus(Status.SOLICITADO);
				endereco.setCidade(cidades.porId(2888));
				endereco.setCep("85980-000");
				System.out.println("[LOG] Salvando solicitação...");
				solicitacao = solicitacaoService.salvar(solicitacao);
				protocolo = solicitacao.getIdSolicitacao();
				System.out.println("[LOG] Solicitação salva: id=" + protocolo);

				// Validação e persistência de documentos apenas para novos proprietários ou sem docs existentes
				if (isNovoProprietario || propAtual.getDocumentos() == null) {
					// Validação: Requer todos os documentos para novos proprietários
					if (isNovoProprietario && (cardUnicoFile == null || documentoComFotoFile == null || comprovanteEnderecoFile == null)) {
						throw new NegocioException("Todos os documentos pessoais (Card Único, Documento com Foto e Comprovante de Endereço) são obrigatórios para novos proprietários.");
					}

					// Logs detalhados dos arquivos enviados
					logArquivosDocumentos();

					System.out.println("[LOG] Criando objeto DocumentosPessoais...");
					DocumentosPessoais documentos = new DocumentosPessoais();
					documentos.setCardUnico(cardUnicoFile);
					documentos.setComprovanteEndereco(comprovanteEnderecoFile);
					documentos.setDocumentoComFoto(documentoComFotoFile);
					System.out.println("[LOG] Salvando DocumentosPessoais...");
					manager.persist(documentos);
					manager.flush();
					System.out.println("[LOG] DocumentosPessoais persistido: id=" + documentos.getId());
					propAtual.setDocumentos(documentos);
					System.out.println("[LOG] Documentos pessoais associados ao proprietário.");
					manager.merge(propAtual);
					System.out.println("[LOG] Proprietário atualizado com documentos pessoais.");

					// Limpar campos de documentos após sucesso
					cardUnicoFile = null;
					documentoComFotoFile = null;
					comprovanteEnderecoFile = null;
				} else {
					System.out.println("[LOG] Proprietário existente já possui documentos; pulando criação.");
				}

				FacesUtil.addInfoMessage("Solicitação enviada com sucesso! Protocolo: " + protocolo);
				chamaDialogComprovante();
				if (usuarioLogado.getUsuarioLogado() == null) {
					System.out.println("[LOG] Gerando comprovante...");
					geraComprovante();
				}
				limpar();
				cpf = "";
				habilitaCampos = true;
			} else {
				System.out.println("[LOG] Solicitação em aberto detectada, não irá salvar");
				habilitaCampos = true;
				animaisAdicionados.clear();
				FacesUtil.addErrorMessage("Este CPF já possui uma solicitação em aberto!");
				FacesUtil.addErrorMessage("É permitido uma solicitação por CPF!");
			}

			manager.getTransaction().commit();
			System.out.println("[LOG] Transação commitada com sucesso");
		} catch (Exception e) {
			System.out.println("[LOG] Erro ao salvar: " + e.getMessage());
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
				System.out.println("[LOG] Rollback executado");
				// Opcional: Limpar arquivos salvos em caso de erro (manual cleanup recomendado)
				if (cardUnicoFile != null) {
					new java.io.File("C:\\animalys\\documentos\\" + cardUnicoFile).delete();
				}
				if (documentoComFotoFile != null) {
					new java.io.File("C:\\animalys\\documentos\\" + documentoComFotoFile).delete();
				}
				if (comprovanteEnderecoFile != null) {
					new java.io.File("C:\\animalys\\documentos\\" + comprovanteEnderecoFile).delete();
				}
			}
			FacesUtil.addErrorMessage("Erro ao salvar: " + e.getMessage());
		}
	}

	// Função utilitária para salvar o arquivo
	private String salvarArquivoUpload(org.primefaces.event.FileUploadEvent event, String tipo) {
		try {
			// Use absolute path as specified
			String basePath = "C:\\animalys\\documentos";
			java.io.File dir = new java.io.File(basePath);
			if (!dir.exists()) {
				boolean created = dir.mkdirs();
				if (!created) {
					System.out.println("[LOG] Falha ao criar diretório: " + basePath);
					return null;
				}
			}
			String fileName = System.currentTimeMillis() + "_" + event.getFile().getFileName();
			java.io.File dest = new java.io.File(dir, fileName);
			try (java.io.InputStream in = event.getFile().getInputstream();
					java.io.FileOutputStream out = new java.io.FileOutputStream(dest)) {
				byte[] buffer = new byte[1024];
				int len;
				while ((len = in.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				System.out.println("[LOG] " + tipo + " salvo em: " + dest.getAbsolutePath());
				return fileName;
			}
		} catch (Exception e) {
			System.out.println("[LOG] Erro ao salvar " + tipo + ": " + e.getMessage());
			e.printStackTrace(); // For better debugging
			return null;
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
		this.cardUnicoFile = null;
		this.documentoComFotoFile = null;
		this.comprovanteEnderecoFile = null;
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

			System.out.println("METODO: adicionarAnimal() DIZ: - FotoUpload: "
					+ (fotoUpload != null ? fotoUpload.getFileName() : "null"));
			System.out.println("METODO: adicionarAnimal() DIZ: - Modalidade atual: " + 
					(this.solicitacao.getModalidade() != null ? this.solicitacao.getModalidade() : "NULL"));

			this.animal.setProprietario(this.proprietario);
			this.animal.setStatus(Status.SOLICITADO);
			this.animal.setDataEntrada(this.dataAtual);

			if (this.fotoUpload != null && this.fotoUpload.getSize() > 0) {

				System.out.println("METODO: adicionarAnimal() DIZ: BLOCO DE UPLOAD DE FOTO CHAMADO");
				System.out.println("METODO: adicionarAnimal() DIZ: Upload recebido: "
						+ (fotoUpload != null ? fotoUpload.getFileName() : "null"));

				// Chama o serviço para salvar a foto fisicamente
				System.out.println("METODO: adicionarAnimal() DIZ: SERVICO SALVAR SALVAR FOTO FISICAMENTE CHAMADO");
				FotoAnimal foto = fotoService.salvarFoto(this.fotoUpload, this.animal);
				this.animal.adicionarFoto(foto); // Adiciona a foto à lista de fotos do animal
				this.animal.setFoto(foto.getNomeArquivo()); // Define o nome do arquivo da foto principal no campo

			} else {
				System.out.println("METODO: adicionarAnimal() DIZ: FOTO NAO SELECIONADA");
			}

			// Adiciona à lista (persistência completa em salvar())
			System.out.println("METODO: adicionarAnimal() DIZ: NOME DA FOTO ANTES DE ADICIONAR: " + this.animal.getFoto());
			
			// Adiciona o animal à lista independente da modalidade
			if (!this.animaisAdicionados.contains(this.animal)) {
				this.animaisAdicionados.add(this.animal);
				System.out.println("METODO: adicionarAnimal() DIZ: ANIMAL ADICIONADO A LISTA - Modalidade: " + this.solicitacao.getModalidade());
			} else {
				System.out.println("METODO: adicionarAnimal() DIZ: ANIMAL JA EXISTE NA LISTA");
			}

			System.out.println("METODO: adicionarAnimal() DIZ: CHAMANDO METODO: limparCamposFoto()");
			this.limparCamposFoto();
			this.limparAposAdicionar();
			
			FacesUtil.addInfoMessage("Animal adicionado com sucesso!");

		} catch (NegocioException | IOException e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("METODO: adicionarAnimal() DIZ: Erro ao processar animal/foto: " + e.getMessage());
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
			// Preservar modalidade atual antes de carregar dados do proprietário
			ModalidadeSolicitante modalidadeAtual = this.solicitacao.getModalidade();

			try {
				this.proprietario = this.proprietarios.proprietarioPorCPF(this.cpf);
				
				// Se chegou aqui, o proprietário foi encontrado
				System.out
						.println("[LOG] Proprietario buscado: idProprietario=" + this.proprietario.getIdProprietario());
				System.out.println("[LOG] Documentos do proprietario: " + (this.proprietario.getDocumentos() != null ? "carregados com ID " + this.proprietario.getDocumentos().getId() : "NULL"));
				this.contato = this.proprietario.getContato() != null ? this.proprietario.getContato() : new Contato();
				this.endereco = this.proprietario.getEndereco() != null ? this.proprietario.getEndereco() : new Endereco();
				this.animaisAtendidos = this.proprietario.getAnimais();
				this.dataNascimento = this.proprietario.getDataNascimento().getTime();
				// Não carregar animais já cadastrados - esta é uma tela de cadastro de novos animais
				this.habilitaCampos = false; // Desabilitar campos para proprietário existente (pré-preenchidos)
				// Restaurar modalidade preservada
				this.solicitacao.setModalidade(modalidadeAtual);
				FacesUtil.addInfoMessage("Proprietário encontrado.");
			} catch (NoResultException e) {
				e.printStackTrace();
				System.out.println("[LOG] CPF não encontrado!");
				limpar();
				this.proprietario.setCpf(this.cpf);
				this.habilitaCampos = true; // Habilitar campos para novo cadastro
				// Restaurar modalidade também para novo proprietário
				this.solicitacao.setModalidade(modalidadeAtual);
				FacesUtil.addInfoMessage("CPF não encontrado. Faça o Cadastro.");
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

	// Métodos para remoção de documentos individuais
	public void removerCardUnico() {
		System.out.println("DEBUG: Executando removerCardUnico()");
		setCardUnicoFile(null);
		System.out.println("DEBUG: Card Único removido - cardUnicoFile = " + cardUnicoFile);
	}

	public void removerDocumentoComFoto() {
		System.out.println("DEBUG: Executando removerDocumentoComFoto()");
		setDocumentoComFotoFile(null);
		System.out.println("DEBUG: Documento com Foto removido - documentoComFotoFile = " + documentoComFotoFile);
	}

	public void removerComprovanteEndereco() {
		System.out.println("DEBUG: Executando removerComprovanteEndereco()");
		setComprovanteEnderecoFile(null);
		System.out.println("DEBUG: Comprovante de Endereço removido - comprovanteEnderecoFile = " + comprovanteEnderecoFile);
	}

	// Método para limpar todos os documentos
	public void limparTodosDocumentos() {
		setCardUnicoFile(null);
		setDocumentoComFotoFile(null);
		setComprovanteEnderecoFile(null);
	}
	
	// Método para verificar se o proprietário atual tem documentos preenchidos
	public boolean proprietarioTemDocumentos() {
		// Verifica se o proprietário está carregado
		if (proprietario == null || proprietario.getIdProprietario() == null) {
			return false;
		}
		
		// Verifica se o proprietário tem documentos
		if (proprietario.getDocumentos() == null) {
			// Tentar recarregar o proprietário com documentos
			try {
				Proprietario recarregado = proprietarios.porId(proprietario.getIdProprietario());
				if (recarregado != null && recarregado.getDocumentos() != null) {
					this.proprietario = recarregado;
				} else {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
		
		DocumentosPessoais docs = proprietario.getDocumentos();
		if (docs == null) {
			return false;
		}
		
		// Verifica se pelo menos um documento está preenchido
		boolean temCardUnico = docs.getCardUnico() != null && !docs.getCardUnico().trim().isEmpty();
		boolean temDocComFoto = docs.getDocumentoComFoto() != null && !docs.getDocumentoComFoto().trim().isEmpty();
		boolean temComprovante = docs.getComprovanteEndereco() != null && !docs.getComprovanteEndereco().trim().isEmpty();
		
		return temCardUnico || temDocComFoto || temComprovante;
	}
	
	// Método para verificar se há um proprietário carregado
	public boolean temProprietarioCarregado() {
		return proprietario != null && proprietario.getIdProprietario() != null;
	}
	
	// Método para forçar refresh do estado dos documentos (para debug)
	public void refreshDocumentos() {
		if (proprietario != null && proprietario.getIdProprietario() != null) {
			try {
				Proprietario recarregado = proprietarios.porId(proprietario.getIdProprietario());
				if (recarregado != null) {
					this.proprietario = recarregado;
					System.out.println("[REFRESH] Proprietário recarregado. Tem documentos: " + proprietarioTemDocumentos());
				}
			} catch (Exception e) {
				System.out.println("[REFRESH] Erro ao recarregar: " + e.getMessage());
			}
		}
	}

}
