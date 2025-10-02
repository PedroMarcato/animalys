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

import br.gov.pr.guaira.animalys.entity.Cidade;
import br.gov.pr.guaira.animalys.entity.Contato;
import br.gov.pr.guaira.animalys.entity.Endereco;
import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.repository.Cidades;
import br.gov.pr.guaira.animalys.repository.Proprietarios;
import br.gov.pr.guaira.animalys.service.NegocioException;
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

			// Salvar proprietário
			this.proprietario = proprietarios.guardar(proprietario);

			if (edicao) {
				FacesUtil.addInfoMessage("Proprietário atualizado com sucesso!");
			} else {
				FacesUtil.addInfoMessage("Proprietário cadastrado com sucesso!");
			}
			
			limpar();
			
		} catch (NegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErrorMessage("Erro ao salvar proprietário: " + e.getMessage());
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
}
