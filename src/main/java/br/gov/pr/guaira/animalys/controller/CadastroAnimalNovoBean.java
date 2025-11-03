package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Especie;
import br.gov.pr.guaira.animalys.entity.FotoAnimal;
import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.entity.Raca;
import br.gov.pr.guaira.animalys.entity.Sexo;
import br.gov.pr.guaira.animalys.entity.Status;
import br.gov.pr.guaira.animalys.filter.ProprietarioFilter;
import br.gov.pr.guaira.animalys.repository.Animais;
import br.gov.pr.guaira.animalys.repository.Especies;
import br.gov.pr.guaira.animalys.repository.Proprietarios;
import br.gov.pr.guaira.animalys.repository.Racas;
import br.gov.pr.guaira.animalys.service.FotoService;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroAnimalNovoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Animais animais;
	
	@Inject
	private Proprietarios proprietarios;
	
	@Inject
	private Especies especies;
	
	@Inject
	private Racas racas;
	
	@Inject
	private FotoService fotoService;

	private Animal animal;
	private List<Proprietario> proprietariosCadastrados;
	private List<Especie> especiesCadastradas;
	private List<Raca> racasCadastradas;
	private Especie especieSelecionada;
	private Date dataEntrada;
	private Date dataSaida;
	private Date dataCastracao;
	private Date dataAgendaCastracao;
	private boolean edicao = false;
	private String fotoBase64Preview;
	private Boolean animalDeRua = false;
	private boolean proprietarioDesabilitado = false;
	
	// Para pesquisa de proprietário
	private String nomeProprietarioFiltro;
	private String cpfProprietarioFiltro;
	private List<Proprietario> proprietariosFiltrados;

	@PostConstruct
	public void inicializar() {
		limpar();
		carregarListas();
		this.animalDeRua = false;
		this.proprietarioDesabilitado = false;
		
		// Verificar se está editando
		String idParam = FacesContext.getCurrentInstance()
			.getExternalContext().getRequestParameterMap().get("animal");
		if (idParam != null && !idParam.isEmpty()) {
			try {
				Integer id = Integer.valueOf(idParam);
				this.animal = animais.porId(id);
				
				// Converter datas para Date
				if (animal.getDataEntrada() != null) {
					this.dataEntrada = animal.getDataEntrada().getTime();
				}
				if (animal.getDataSaida() != null) {
					this.dataSaida = animal.getDataSaida().getTime();
				}
				if (animal.getDataCastracao() != null) {
					this.dataCastracao = animal.getDataCastracao().getTime();
				}
				if (animal.getDataAgendaCastracao() != null) {
					this.dataAgendaCastracao = animal.getDataAgendaCastracao().getTime();
				}
				
				// Carregar raças da espécie do animal
				if (animal.getRaca() != null && animal.getRaca().getEspecie() != null) {
					this.especieSelecionada = animal.getRaca().getEspecie();
					carregaRacasPorEspecie();
				}
				
				// Verificar se é animal de rua (ID 2219 ou se tem campos de animal de rua preenchidos)
				if (animal.getProprietario() != null && animal.getProprietario().getIdProprietario() == 2219) {
					this.animalDeRua = true;
					this.proprietarioDesabilitado = true;
				} else if (animal.getResponsavelEntrega() != null && !animal.getResponsavelEntrega().trim().isEmpty()) {
					// Se tem responsável pela entrega preenchido, também é animal de rua
					this.animalDeRua = true;
					this.proprietarioDesabilitado = true;
				}
				
				this.edicao = true;
			} catch (NumberFormatException e) {
				FacesUtil.addErrorMessage("ID do animal inválido.");
				limpar();
			} catch (Exception e) {
				FacesUtil.addErrorMessage("Animal não encontrado.");
				limpar();
			}
		}
	}

	public void limpar() {
		this.animal = new Animal();
		this.especieSelecionada = null;
		this.dataEntrada = null;
		this.dataSaida = null;
		this.dataCastracao = null;
		this.dataAgendaCastracao = null;
		this.edicao = false;
		this.racasCadastradas = null;
		this.fotoBase64Preview = null;
		this.animalDeRua = false;
		this.proprietarioDesabilitado = false;
		
		// Limpar campos específicos de animal de rua
		this.animal.setResponsavelEntrega(null);
		this.animal.setEnderecoEntrega(null);
		this.animal.setCelularEntrega(null);
	}
	// Listener para mudança do campo Animal de Rua
	public void onAnimalDeRuaChange() {
		if (Boolean.TRUE.equals(animalDeRua)) {
			Proprietario proprietarioRua = proprietarios.porId(2219);
			if (proprietarioRua != null) {
				animal.setProprietario(proprietarioRua);
				proprietarioDesabilitado = true;
			}
		} else {
			proprietarioDesabilitado = false;
			animal.setProprietario(null);
			
			// Limpar campos específicos de animal de rua quando desmarcar
			animal.setResponsavelEntrega(null);
			animal.setEnderecoEntrega(null);
			animal.setCelularEntrega(null);
		}
	}

	public void carregarListas() {
		this.proprietariosCadastrados = proprietarios.todos();
		this.especiesCadastradas = especies.especiesCadastradas();
	}

	public void carregaRacasPorEspecie() {
		if (especieSelecionada != null) {
			this.racasCadastradas = racas.racasPorEspecie(especieSelecionada);
		} else {
			this.racasCadastradas = null;
		}
		// Limpar raça selecionada quando espécie mudar
		if (animal.getRaca() != null && racasCadastradas != null && !racasCadastradas.contains(animal.getRaca())) {
			animal.setRaca(null);
		}
	}

	public void especieAlterada() {
		// Quando a espécie do animal for alterada, atualizar a espécie selecionada
		this.especieSelecionada = animal.getEspecie();
		carregaRacasPorEspecie();
	}

	public void pesquisarProprietarios() {
		ProprietarioFilter filtro = new ProprietarioFilter();
		filtro.setNome(nomeProprietarioFiltro);
		filtro.setCpf(cpfProprietarioFiltro);
		this.proprietariosFiltrados = proprietarios.filtrados(filtro);
	}

	public void selecionarProprietario(Proprietario proprietario) {
 		if (!Boolean.TRUE.equals(animalDeRua)) {
 			this.animal.setProprietario(proprietario);
 		}
	}

	public void limparFiltroProprietario() {
		this.nomeProprietarioFiltro = null;
		this.cpfProprietarioFiltro = null;
		this.proprietariosFiltrados = null;
	}

	public void handleFotoUpload(FileUploadEvent event) {
		try {
			System.out.println("Iniciando upload de foto: " + event.getFile().getFileName());
			
			// Remover foto existente se houver
			if (!animal.getFotos().isEmpty()) {
				animal.getFotos().clear();
			}
			
			FotoAnimal fotoAnimal = fotoService.salvarFoto(event.getFile(), animal);
			animal.setFoto(fotoAnimal.getNomeArquivo());
			
			// Adicionar a foto à lista de fotos do animal
			animal.adicionarFoto(fotoAnimal);
			
			// Gerar preview em base64
			byte[] fileContent = event.getFile().getContents();
			String base64String = java.util.Base64.getEncoder().encodeToString(fileContent);
			this.fotoBase64Preview = "data:" + event.getFile().getContentType() + ";base64," + base64String;
			
			System.out.println("Preview base64 gerado. Tamanho: " + (fotoBase64Preview != null ? fotoBase64Preview.length() : 0));
			
			FacesUtil.addInfoMessage("Foto enviada com sucesso!");
		} catch (Exception e) {
			System.err.println("Erro no upload: " + e.getMessage());
			e.printStackTrace();
			FacesUtil.addErrorMessage("Erro ao enviar foto: " + e.getMessage());
		}
	}

	// Alias para o método de upload
	public void upload(FileUploadEvent event) {
		handleFotoUpload(event);
	}

	public void removerFoto(FotoAnimal foto) {
		try {
			animal.getFotos().remove(foto);
			FacesUtil.addInfoMessage("Foto removida com sucesso!");
		} catch (Exception e) {
			FacesUtil.addErrorMessage("Erro ao remover foto: " + e.getMessage());
		}
	}

	@Transactional
	public void salvar() {
		try {
			// Converter datas para Calendar
			if (dataEntrada != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(dataEntrada);
				animal.setDataEntrada(cal);
			}
			
			if (dataSaida != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(dataSaida);
				animal.setDataSaida(cal);
			}
			
			if (dataCastracao != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(dataCastracao);
				animal.setDataCastracao(cal);
			}
			
			if (dataAgendaCastracao != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(dataAgendaCastracao);
				animal.setDataAgendaCastracao(cal);
			}

			// Se não tem status, definir como SOLICITADO
			if (animal.getStatus() == null) {
				animal.setStatus(Status.SOLICITADO);
			}

			// Salvar animal
			this.animal = animais.guardar(animal);

			if (edicao) {
				FacesUtil.addInfoMessage("Animal atualizado com sucesso!");
			} else {
				FacesUtil.addInfoMessage("Animal cadastrado com sucesso!");
			}
			
			limpar();
			carregarListas();
			
		} catch (NegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErrorMessage("Erro ao salvar animal: " + e.getMessage());
		}
	}

	// Métodos auxiliares para enums
	public Sexo[] getSexos() {
		return Sexo.values();
	}

	public Status[] getStatusList() {
		return Status.values();
	}

	// Getters e Setters
	public Animal getAnimal() {
		return animal;
	}

	public Boolean getAnimalDeRua() {
		return animalDeRua;
	}

	public void setAnimalDeRua(Boolean animalDeRua) {
		this.animalDeRua = animalDeRua;
	}

	public boolean isProprietarioDesabilitado() {
		return proprietarioDesabilitado;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public List<Proprietario> getProprietariosCadastrados() {
		return proprietariosCadastrados;
	}

	public void setProprietariosCadastrados(List<Proprietario> proprietariosCadastrados) {
		this.proprietariosCadastrados = proprietariosCadastrados;
	}

	public List<Especie> getEspeciesCadastradas() {
		return especiesCadastradas;
	}

	public void setEspeciesCadastradas(List<Especie> especiesCadastradas) {
		this.especiesCadastradas = especiesCadastradas;
	}

	public List<Raca> getRacasCadastradas() {
		return racasCadastradas;
	}

	public void setRacasCadastradas(List<Raca> racasCadastradas) {
		this.racasCadastradas = racasCadastradas;
	}

	public Especie getEspecieSelecionada() {
		return especieSelecionada;
	}

	public void setEspecieSelecionada(Especie especieSelecionada) {
		this.especieSelecionada = especieSelecionada;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}

	public Date getDataCastracao() {
		return dataCastracao;
	}

	public void setDataCastracao(Date dataCastracao) {
		this.dataCastracao = dataCastracao;
	}

	public Date getDataAgendaCastracao() {
		return dataAgendaCastracao;
	}

	public void setDataAgendaCastracao(Date dataAgendaCastracao) {
		this.dataAgendaCastracao = dataAgendaCastracao;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}

	public String getFotoBase64Preview() {
		return fotoBase64Preview;
	}

	public void setFotoBase64Preview(String fotoBase64Preview) {
		this.fotoBase64Preview = fotoBase64Preview;
	}

	public String getNomeProprietarioFiltro() {
		return nomeProprietarioFiltro;
	}

	public void setNomeProprietarioFiltro(String nomeProprietarioFiltro) {
		this.nomeProprietarioFiltro = nomeProprietarioFiltro;
	}

	public String getCpfProprietarioFiltro() {
		return cpfProprietarioFiltro;
	}

	public void setCpfProprietarioFiltro(String cpfProprietarioFiltro) {
		this.cpfProprietarioFiltro = cpfProprietarioFiltro;
	}

	public List<Proprietario> getProprietariosFiltrados() {
		return proprietariosFiltrados;
	}

	public void setProprietariosFiltrados(List<Proprietario> proprietariosFiltrados) {
		this.proprietariosFiltrados = proprietariosFiltrados;
	}
}
