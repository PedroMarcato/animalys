package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.event.FileUploadEvent;

import br.gov.pr.guaira.animalys.entity.Cidade;
import br.gov.pr.guaira.animalys.entity.Contato;
import br.gov.pr.guaira.animalys.entity.DocumentosPessoais;
import br.gov.pr.guaira.animalys.entity.Endereco;
import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.repository.Cidades;
import br.gov.pr.guaira.animalys.repository.Proprietarios;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroProprietarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Proprietarios proprietarios;
	
	@Inject
	private Cidades cidades;
	
	@Inject
	private EntityManager manager;
	
	private Proprietario proprietario;
	private Endereco endereco;
	private Contato contato;
	private String cpf;
	private Date dataNascimento;
	private boolean edicao = false;
	
	// Propriedades para upload de documentos
	private String cardUnicoFile;
	private String documentoComFotoFile;
	private String comprovanteEnderecoFile;

	@PostConstruct
	public void inicializar() {
		limpar();
		
		// Verificar se está editando
		String idParam = FacesContext.getCurrentInstance()
			.getExternalContext().getRequestParameterMap().get("proprietario");
		if (idParam != null && !idParam.isEmpty()) {
			try {
				Integer id = Integer.valueOf(idParam);
				this.proprietario = proprietarios.porId(id);
				this.endereco = proprietario.getEndereco();
				this.contato = proprietario.getContato();
				this.cpf = proprietario.getCpf();
				if (proprietario.getDataNascimento() != null) {
					this.dataNascimento = proprietario.getDataNascimento().getTime();
				}
				this.edicao = true;
			} catch (NumberFormatException e) {
				FacesUtil.addErrorMessage("ID do proprietário inválido.");
				limpar();
			} catch (Exception e) {
				FacesUtil.addErrorMessage("Proprietário não encontrado.");
				limpar();
			}
		}
	}

	public void limpar() {
		this.proprietario = new Proprietario();
		this.endereco = new Endereco();
		this.endereco.setCidade(null); // Garantir que a cidade inicie como null
		this.contato = new Contato();
		this.cpf = "";
		this.dataNascimento = null;
		this.edicao = false;
		this.cardUnicoFile = null;
		this.documentoComFotoFile = null;
		this.comprovanteEnderecoFile = null;
	}

	public void buscaCadastroPeloCPF() {
		if (cpf != null && !cpf.trim().isEmpty()) {
			try {
				Proprietario proprietarioExistente = proprietarios.proprietarioPorCPF(cpf);
				if (proprietarioExistente != null) {
					this.proprietario = proprietarioExistente;
					this.endereco = proprietarioExistente.getEndereco() != null ? 
						proprietarioExistente.getEndereco() : new Endereco();
					this.contato = proprietarioExistente.getContato() != null ? 
						proprietarioExistente.getContato() : new Contato();
					if (proprietarioExistente.getDataNascimento() != null) {
						this.dataNascimento = proprietarioExistente.getDataNascimento().getTime();
					}
					this.edicao = true;
					FacesUtil.addInfoMessage("Proprietário encontrado! Dados carregados.");
				}
			} catch (NoResultException e) {
				// CPF não encontrado, continua com cadastro novo
			} catch (Exception e) {
				FacesUtil.addErrorMessage("Erro ao buscar proprietário: " + e.getMessage());
			}
		}
	}

	@Transactional
	public void salvar() {
		try {
			// Setar CPF no proprietário (removendo máscara se necessário)
			if (cpf != null && !cpf.trim().isEmpty()) {
				String cpfLimpo = cpf.replaceAll("[^0-9]", ""); // Remove caracteres não numéricos
				proprietario.setCpf(cpfLimpo);
			}
			
			// Converter data de nascimento
			if (dataNascimento != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(dataNascimento);
				proprietario.setDataNascimento(cal);
			}

			// Fazer merge da cidade se ela existir
			if (endereco.getCidade() != null) {
				endereco.setCidade(manager.merge(endereco.getCidade()));
			}

			// Salvar endereço usando manager
			this.endereco = manager.merge(endereco);
			proprietario.setEndereco(endereco);

			// Salvar contato usando manager
			this.contato = manager.merge(contato);
			proprietario.setContato(contato);
			
			// Processar documentos pessoais se houver uploads
			boolean isNovoProprietario = (proprietario.getIdProprietario() == null);
			if ((cardUnicoFile != null || documentoComFotoFile != null || comprovanteEnderecoFile != null)) {
				// Se é novo proprietário ou não tem documentos ainda
				if (isNovoProprietario || proprietario.getDocumentos() == null) {
					System.out.println("[LOG] Criando objeto DocumentosPessoais...");
					DocumentosPessoais documentos = new DocumentosPessoais();
					documentos.setCardUnico(cardUnicoFile);
					documentos.setComprovanteEndereco(comprovanteEnderecoFile);
					documentos.setDocumentoComFoto(documentoComFotoFile);
					System.out.println("[LOG] Salvando DocumentosPessoais...");
					manager.persist(documentos);
					manager.flush();
					System.out.println("[LOG] DocumentosPessoais persistido: id=" + documentos.getId());
					proprietario.setDocumentos(documentos);
					System.out.println("[LOG] Documentos pessoais associados ao proprietário.");
				} else {
					// Atualizar documentos existentes
					System.out.println("[LOG] Atualizando DocumentosPessoais existentes...");
					DocumentosPessoais docs = proprietario.getDocumentos();
					if (cardUnicoFile != null) {
						docs.setCardUnico(cardUnicoFile);
					}
					if (documentoComFotoFile != null) {
						docs.setDocumentoComFoto(documentoComFotoFile);
					}
					if (comprovanteEnderecoFile != null) {
						docs.setComprovanteEndereco(comprovanteEnderecoFile);
					}
					manager.merge(docs);
					System.out.println("[LOG] Documentos pessoais atualizados.");
				}
			}

			// Salvar proprietário (guardar já tem @Transactional, mas está dentro desta transação maior)
			this.proprietario = proprietarios.guardar(proprietario);

			System.out.println("[LOG] Proprietário salvo com sucesso");

			if (edicao) {
				FacesUtil.addInfoMessage("Proprietário atualizado com sucesso!");
			} else {
				FacesUtil.addInfoMessage("Proprietário cadastrado com sucesso!");
			}
			
			limpar();
			
		} catch (NegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			FacesUtil.addErrorMessage("Erro ao salvar proprietário: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Getters e Setters
	public Proprietario getProprietario() {
		return proprietario;
	}

	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}
	
	public List<Cidade> completarCidade(String descricao) {
		System.out.println("=== COMPLETAR CIDADE ===");
		System.out.println("Descrição buscada: " + descricao);
		
		List<Cidade> resultado = this.cidades.porNome(descricao);
		System.out.println("Quantidade de cidades encontradas: " + (resultado != null ? resultado.size() : 0));
		
		if (resultado != null && !resultado.isEmpty()) {
			for (int i = 0; i < Math.min(5, resultado.size()); i++) {
				Cidade cidade = resultado.get(i);
				System.out.println("Cidade " + (i+1) + ": " + cidade.getNome() + 
					" - Estado: " + (cidade.getEstado() != null ? cidade.getEstado().getUf() : "NULL") +
					" - ID: " + cidade.getId());
			}
		}
		
		return resultado;
	}
	
	public void onCidadeSelecionada() {
		System.out.println("=== CIDADE SELECIONADA ===");
		System.out.println("Método chamado!");
		System.out.println("Endereco é null? " + (this.endereco == null));
		
		if (this.endereco != null) {
			System.out.println("Endereco.cidade é null? " + (this.endereco.getCidade() == null));
			
			if (this.endereco.getCidade() != null) {
				Cidade cidade = this.endereco.getCidade();
				System.out.println("Cidade selecionada: " + cidade.getNome());
				
				if (cidade.getEstado() != null) {
					System.out.println("UF da cidade: " + cidade.getEstado().getUf());
					System.out.println("Seleção realizada com sucesso!");
				} else {
					System.out.println("Estado da cidade é NULL");
				}
			} else {
				System.out.println("A cidade no endereço está NULL - tentando recovery");
				
				// Força um pequeno delay para permitir que o JSF processe
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				
				// Verifica novamente após o delay
				if (this.endereco.getCidade() != null) {
					System.out.println("Cidade encontrada após delay: " + this.endereco.getCidade().getNome());
				} else {
					System.out.println("Cidade ainda é null após delay - verificando componente");
					
					// Tenta recuperar a cidade do contexto JSF
					try {
						FacesContext context = FacesContext.getCurrentInstance();
						if (context != null) {
							UIComponent component = context.getViewRoot().findComponent("formProprietario:cidade");
							if (component instanceof AutoComplete) {
								AutoComplete autoComplete = (AutoComplete) component;
								Object value = autoComplete.getValue();
								System.out.println("Valor do componente: " + value);
								if (value instanceof Cidade) {
									this.endereco.setCidade((Cidade) value);
									Cidade cidade = (Cidade) value;
									System.out.println("Cidade recuperada do componente: " + cidade.getNome());
									
									if (cidade.getEstado() != null) {
										System.out.println("UF da cidade recuperada: " + cidade.getEstado().getUf());
									}
								}
							}
						}
					} catch (Exception e) {
						System.out.println("Erro ao recuperar cidade do componente: " + e.getMessage());
					}
				}
			}
		} else {
			System.out.println("O endereço está NULL");
		}
		System.out.println("=== FIM CIDADE SELECIONADA ===");
	}
	
	// ========== MÉTODOS PARA UPLOAD E GERENCIAMENTO DE DOCUMENTOS ==========
	
	// Getters e Setters para os arquivos de documentos
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
	public void handleCardUnicoUpload(FileUploadEvent event) {
		System.out.println("[LOG] handleCardUnicoUpload chamado");
		String fileName = salvarArquivoUpload(event, "Card Único");
		if (fileName != null) {
			this.cardUnicoFile = fileName;
			System.out.println("[DEBUG] cardUnicoFile setado: " + this.cardUnicoFile);
			FacesUtil.addInfoMessage("Card Único enviado com sucesso!");
		} else {
			System.out.println("[DEBUG] cardUnicoFile NÃO setado (null)");
		}
	}

	public void handleDocumentoComFotoUpload(FileUploadEvent event) {
		System.out.println("[LOG] handleDocumentoComFotoUpload chamado");
		String fileName = salvarArquivoUpload(event, "Documento com Foto");
		if (fileName != null) {
			this.documentoComFotoFile = fileName;
			System.out.println("[DEBUG] documentoComFotoFile setado: " + this.documentoComFotoFile);
			FacesUtil.addInfoMessage("Documento com Foto enviado com sucesso!");
		} else {
			System.out.println("[DEBUG] documentoComFotoFile NÃO setado (null)");
		}
	}

	public void handleComprovanteEnderecoUpload(FileUploadEvent event) {
		System.out.println("[LOG] handleComprovanteEnderecoUpload chamado");
		String fileName = salvarArquivoUpload(event, "Comprovante de Endereço");
		if (fileName != null) {
			this.comprovanteEnderecoFile = fileName;
			System.out.println("[DEBUG] comprovanteEnderecoFile setado: " + this.comprovanteEnderecoFile);
			FacesUtil.addInfoMessage("Comprovante de Endereço enviado com sucesso!");
		} else {
			System.out.println("[DEBUG] comprovanteEnderecoFile NÃO setado (null)");
		}
	}
	
	// Função utilitária para salvar o arquivo
	private String salvarArquivoUpload(FileUploadEvent event, String tipo) {
		try {
			String basePath = "C:\\animalys\\documentos";
			java.io.File dir = new java.io.File(basePath);
			if (!dir.exists()) {
				boolean created = dir.mkdirs();
				if (!created) {
					System.out.println("[LOG] Falha ao criar diretório: " + basePath);
					FacesUtil.addErrorMessage("Erro ao criar diretório de documentos");
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
			e.printStackTrace();
			FacesUtil.addErrorMessage("Erro ao salvar " + tipo);
			return null;
		}
	}
	
	// Métodos para remoção de documentos individuais
	public void removerCardUnico() {
		System.out.println("DEBUG: Executando removerCardUnico()");
		setCardUnicoFile(null);
		System.out.println("DEBUG: Card Único removido - cardUnicoFile = " + cardUnicoFile);
		FacesUtil.addInfoMessage("Card Único removido com sucesso!");
	}

	public void removerDocumentoComFoto() {
		System.out.println("DEBUG: Executando removerDocumentoComFoto()");
		setDocumentoComFotoFile(null);
		System.out.println("DEBUG: Documento com Foto removido - documentoComFotoFile = " + documentoComFotoFile);
		FacesUtil.addInfoMessage("Documento com Foto removido com sucesso!");
	}

	public void removerComprovanteEndereco() {
		System.out.println("DEBUG: Executando removerComprovanteEndereco()");
		setComprovanteEnderecoFile(null);
		System.out.println("DEBUG: Comprovante de Endereço removido - comprovanteEnderecoFile = " + comprovanteEnderecoFile);
		FacesUtil.addInfoMessage("Comprovante de Endereço removido com sucesso!");
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
	
	// Método para forçar refresh do estado dos documentos
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
