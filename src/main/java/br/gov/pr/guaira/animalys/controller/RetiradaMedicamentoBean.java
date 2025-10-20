package br.gov.pr.guaira.animalys.controller;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.entity.RetiradaMedicamento;
import br.gov.pr.guaira.animalys.repository.AnimalDAO;
import br.gov.pr.guaira.animalys.repository.Proprietarios;
import br.gov.pr.guaira.animalys.repository.RetiradasMedicamento;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.service.RetiradaMedicamentoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;
import br.gov.pr.guaira.animalys.util.FileUploadUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;
import org.primefaces.event.FileUploadEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Named
@ViewScoped
public class RetiradaMedicamentoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RetiradaMedicamentoService retiradaService;

    @Inject
    private Proprietarios proprietarios;

    @Inject
    private AnimalDAO animalDAO;
    
    @Inject
    private RetiradasMedicamento retiradasMedicamento;

    private RetiradaMedicamento retirada;
    private String cpfProprietario;
    private List<Animal> animaisProprietario;
    private Date dataRetirada;
    private Integer animalSelecionadoId;
    private UploadedFile arquivoUpload;
    // nome temporário do arquivo salvo no disco antes do persist (ficará null se nenhum upload novo)
    private String nomeArquivoTemp;
    private static final Log logger = LogFactory.getLog(RetiradaMedicamentoBean.class);

    @PostConstruct
    public void init() {
        // Método init sem lógica, a inicialização real acontece no inicializar()
    }
    
    public void inicializar() {
        // Verificar se há parâmetro de edição
        javax.faces.context.FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        String retiradaIdParam = context.getExternalContext().getRequestParameterMap().get("retirada");
            
        if (retiradaIdParam != null && !retiradaIdParam.isEmpty()) {
            try {
                Integer retiradaId = Integer.valueOf(retiradaIdParam);
                carregarRetiradaParaEdicao(retiradaId);
            } catch (NumberFormatException e) {
                FacesUtil.addErrorMessage("ID de retirada inválido.");
                limpar();
            }
        } else {
            limpar();
        }
    }

    private void carregarRetiradaParaEdicao(Integer retiradaId) {
        try {
            RetiradaMedicamento retiradaExistente = retiradasMedicamento.porId(retiradaId);
            if (retiradaExistente != null) {
                this.retirada = retiradaExistente;
                
                // Carregar dados do proprietário
                if (retirada.getProprietario() != null) {
                    this.cpfProprietario = retirada.getProprietario().getCpf();
                    
                    // Carregar lista de animais do proprietário
                    this.animaisProprietario = animalDAO.buscarPorProprietario(
                        retirada.getProprietario().getIdProprietario());
                    
                    // Definir o ID do animal selecionado
                    if (retirada.getAnimal() != null) {
                        this.animalSelecionadoId = retirada.getAnimal().getIdAnimal();
                    }
                }
                
                // Converter data para o campo de interface
                if (retirada.getDataRetirada() != null) {
                    this.dataRetirada = retirada.getDataRetirada().getTime();
                }
                
                FacesUtil.addInfoMessage("Retirada carregada para edição.");
            } else {
                FacesUtil.addErrorMessage("Retirada não encontrada.");
                limpar();
            }
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro ao carregar retirada para edição: " + e.getMessage());
            limpar();
        }
    }

    public void limpar() {
        this.retirada = new RetiradaMedicamento();
        this.cpfProprietario = "";
        this.animaisProprietario = new ArrayList<>();
        this.dataRetirada = new Date();
        this.animalSelecionadoId = null;
        this.retirada.setDataRetirada(Calendar.getInstance());
    }

    public void buscarProprietarioPorCpf() {
        if (cpfProprietario != null && !cpfProprietario.trim().isEmpty()) {
            try {
                String cpfLimpo = cpfProprietario.replaceAll("[^0-9]", "");
                System.out.println("DEBUG: Buscando proprietário com CPF: '" + cpfLimpo + "'");
                
                Proprietario proprietario = proprietarios.proprietarioPorCPF(cpfLimpo);
                
                if (proprietario != null) {
                    this.retirada.setProprietario(proprietario);
                    carregarAnimaisProprietario();
                    FacesUtil.addInfoMessage("Proprietário encontrado: " + proprietario.getNome());
                    System.out.println("DEBUG: Proprietário encontrado: " + proprietario.getNome());
                } else {
                    FacesUtil.addErrorMessage("Proprietário não encontrado com o CPF informado.");
                    this.retirada.setProprietario(null);
                    this.animaisProprietario.clear();
                    System.out.println("DEBUG: Nenhum proprietário encontrado para CPF: " + cpfLimpo);
                }
                
                // Limpar animal selecionado quando trocar de proprietário
                this.retirada.setAnimal(null);
                
            } catch (Exception e) {
                FacesUtil.addErrorMessage("Erro ao buscar proprietário: " + e.getMessage());
                this.retirada.setProprietario(null);
                this.animaisProprietario.clear();
                this.retirada.setAnimal(null);
                e.printStackTrace();
            }
        } else {
            this.retirada.setProprietario(null);
            this.animaisProprietario.clear();
            this.retirada.setAnimal(null);
        }
    }

    public void carregarAnimaisProprietario() {
        if (this.retirada.getProprietario() != null) {
            this.animaisProprietario = animalDAO.buscarPorProprietario(
                this.retirada.getProprietario().getIdProprietario());
        } else {
            this.animaisProprietario.clear();
        }
    }

    // Método removido - não precisamos mais de autocomplete de lotes

    public void handleFileUpload() {
        // Mantido para compatibilidade; em modo simple é acionado ao submeter formulário
        if (arquivoUpload != null) {
            try {
                System.out.println("[server-debug][RetiradaMedicamento] handleFileUpload: início - file=" + arquivoUpload.getFileName() + " size=" + arquivoUpload.getSize());
                logger.debug("handleFileUpload: início");
                String nomeArquivo = FileUploadUtil.salvarArquivo(arquivoUpload);
                System.out.println("[server-debug][RetiradaMedicamento] handleFileUpload: arquivo salvo temporariamente '" + nomeArquivo + "'");
                this.nomeArquivoTemp = nomeArquivo;
                retirada.setNomeArquivo(nomeArquivo);
                logger.info("handleFileUpload: arquivo salvo temporariamente '" + nomeArquivo + "'");
                FacesUtil.addInfoMessage("Arquivo anexado com sucesso!");
            } catch (IllegalArgumentException e) {
                System.out.println("[server-debug][RetiradaMedicamento] handleFileUpload: validação falhou: " + e.getMessage());
                logger.warn("handleFileUpload: validação falhou: " + e.getMessage());
                FacesUtil.addErrorMessage(e.getMessage());
            } catch (Exception e) {
                System.out.println("[server-debug][RetiradaMedicamento] handleFileUpload: erro inesperado: " + e.getMessage());
                logger.error("handleFileUpload: erro inesperado: " + e.getMessage(), e);
                FacesUtil.addErrorMessage("Erro ao anexar arquivo: " + e.getMessage());
            }
        }
    }

    /**
     * Listener para upload automático (PrimeFaces advanced)
     */
    public void uploadListener(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        if (file != null) {
            try {
                System.out.println("[server-debug][RetiradaMedicamento] uploadListener: recebendo arquivo '" + file.getFileName() + "' tamanho=" + file.getSize() + " contentType=" + file.getContentType());
                logger.debug("uploadListener: recebendo arquivo '" + file.getFileName() + "' tamanho=" + file.getSize());
                String nomeArquivo = FileUploadUtil.salvarArquivo(file);
                System.out.println("[server-debug][RetiradaMedicamento] uploadListener: arquivo salvo temporariamente '" + nomeArquivo + "'");
                // se havia um arquivo temporário anterior, removemos
                if (this.nomeArquivoTemp != null && !this.nomeArquivoTemp.equals(retirada.getNomeArquivo())) {
                    FileUploadUtil.removerArquivo(this.nomeArquivoTemp);
                }
                this.nomeArquivoTemp = nomeArquivo;
                // mostrar preview/URL atualizando o objeto de tela (não persiste no DB ainda)
                retirada.setNomeArquivo(nomeArquivo);
                logger.info("uploadListener: arquivo enviado e salvo temporariamente como '" + nomeArquivo + "'");
                FacesUtil.addInfoMessage("Arquivo enviado ao servidor com sucesso.");
            } catch (IllegalArgumentException e) {
                System.out.println("[server-debug][RetiradaMedicamento] uploadListener: validação falhou: " + e.getMessage());
                logger.warn("uploadListener: validação falhou: " + e.getMessage());
                FacesUtil.addErrorMessage(e.getMessage());
            } catch (Exception e) {
                System.out.println("[server-debug][RetiradaMedicamento] uploadListener: erro ao salvar arquivo no servidor: " + e.getMessage());
                logger.error("uploadListener: erro ao salvar arquivo no servidor: " + e.getMessage(), e);
                FacesUtil.addErrorMessage("Erro ao salvar arquivo no servidor: " + e.getMessage());
            }
        }
    }
    
    public void removerArquivoAnexado() {
        if ((nomeArquivoTemp != null && !nomeArquivoTemp.isEmpty()) || (retirada.getNomeArquivo() != null && !retirada.getNomeArquivo().isEmpty())) {
            try {
                // Remover temporário primeiro (novo upload ainda não persistido)
                if (nomeArquivoTemp != null && !nomeArquivoTemp.isEmpty()) {
                    FileUploadUtil.removerArquivo(nomeArquivoTemp);
                    nomeArquivoTemp = null;
                }
                // Remover arquivo associado ao objeto (persistido)
                if (retirada.getNomeArquivo() != null && !retirada.getNomeArquivo().isEmpty()) {
                    FileUploadUtil.removerArquivo(retirada.getNomeArquivo());
                }
                retirada.setNomeArquivo(null);
                this.nomeArquivoTemp = null;
                logger.info("removerArquivoAnexado: arquivo removido com sucesso");
                FacesUtil.addInfoMessage("Arquivo removido com sucesso!");
            } catch (Exception e) {
                logger.error("removerArquivoAnexado: erro ao remover arquivo: " + e.getMessage(), e);
                FacesUtil.addErrorMessage("Erro ao remover arquivo: " + e.getMessage());
            }
        }
    }

    public void salvar() {
        try {
            // Se houve upload prévio, garantir que o nome temporário siga para persistência
            if (this.nomeArquivoTemp != null) {
                retirada.setNomeArquivo(this.nomeArquivoTemp);
            }
            
            // Converter Date para Calendar
            if (this.dataRetirada != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(this.dataRetirada);
                this.retirada.setDataRetirada(cal);
            }

            boolean isEdicao = (retirada.getIdRetiradaMedicamento() != null);
            logger.debug("salvar: salvando retirada id=" + (retirada.getIdRetiradaMedicamento()!=null?retirada.getIdRetiradaMedicamento():"(novo)"));
            this.retiradaService.salvar(this.retirada);
            logger.info("salvar: retirada salva com sucesso id=" + (retirada.getIdRetiradaMedicamento()!=null?retirada.getIdRetiradaMedicamento():"(novo)"));
            
            if (isEdicao) {
                FacesUtil.addInfoMessage("Retirada de medicamento atualizada com sucesso!");
            } else {
                FacesUtil.addInfoMessage("Retirada de medicamento registrada com sucesso!");
                limpar();
            }
            
        } catch (NegocioException e) {
            FacesUtil.addErrorMessage(e.getMessage());
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro inesperado: " + e.getMessage());
        }
    }

    // Getters e Setters
    public RetiradaMedicamento getRetirada() {
        return retirada;
    }

    public void setRetirada(RetiradaMedicamento retirada) {
        this.retirada = retirada;
    }

    public String getCpfProprietario() {
        return cpfProprietario;
    }

    public void setCpfProprietario(String cpfProprietario) {
        this.cpfProprietario = cpfProprietario;
    }

    public List<Animal> getAnimaisProprietario() {
        return animaisProprietario;
    }

    public void setAnimaisProprietario(List<Animal> animaisProprietario) {
        this.animaisProprietario = animaisProprietario;
    }

    // Removidos métodos de lotes - não são mais necessários

    public Date getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(Date dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    /**
     * Classifica a idade do animal selecionado como Adulto, Jovem ou Filhote
     */
    public String getClassificacaoIdade() {
        if (retirada == null || retirada.getAnimal() == null || retirada.getAnimal().getIdade() == null) {
            return "";
        }
        
        String idade = retirada.getAnimal().getIdade().toLowerCase().trim();
        
        // Classificação baseada em palavras-chave comuns
        if (idade.contains("filhote") || idade.contains("bebe") || idade.contains("bebê") || 
            idade.contains("recém") || idade.contains("nascido") || idade.contains("semana")) {
            return "Filhote";
        } else if (idade.contains("jovem") || idade.contains("adolescente")) {
            return "Jovem";
        }
        
        // Verificação por números e meses/anos usando lógica mais simples
        try {
            // Extrai números da string
            String[] palavras = idade.split("\\s+");
            for (int i = 0; i < palavras.length; i++) {
                String palavra = palavras[i];
                if (palavra.matches("\\d+")) {
                    int numero = Integer.parseInt(palavra);
                    
                    // Verifica se a próxima palavra indica unidade de tempo
                    if (i + 1 < palavras.length) {
                        String unidade = palavras[i + 1];
                        if (unidade.startsWith("mes") || unidade.startsWith("mês")) {
                            if (numero <= 2) {
                                return "Filhote";
                            } else if (numero <= 11) {
                                return "Jovem";
                            } else {
                                return "Adulto";
                            }
                        } else if (unidade.startsWith("ano")) {
                            if (numero == 1) {
                                return "Jovem";
                            } else {
                                return "Adulto";
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Em caso de erro na análise, assume adulto
        }
        
        // Default para adulto se não conseguir classificar
        return "Adulto";
    }
    
    public Integer getAnimalSelecionadoId() {
        return animalSelecionadoId;
    }
    
    public void setAnimalSelecionadoId(Integer animalSelecionadoId) {
        this.animalSelecionadoId = animalSelecionadoId;
        
        // Sincronizar com o objeto Animal na retirada
        if (animalSelecionadoId != null && animaisProprietario != null) {
            for (Animal animal : animaisProprietario) {
                if (animal.getIdAnimal().equals(animalSelecionadoId)) {
                    this.retirada.setAnimal(animal);
                    break;
                }
            }
        } else {
            this.retirada.setAnimal(null);
        }
    }
    
    public UploadedFile getArquivoUpload() {
        return arquivoUpload;
    }
    
    public void setArquivoUpload(UploadedFile arquivoUpload) {
        this.arquivoUpload = arquivoUpload;
    }
    
    public String getUrlArquivo() {
        if (retirada != null && retirada.getNomeArquivo() != null) {
            return FileUploadUtil.obterUrlArquivo(retirada.getNomeArquivo());
        }
        return null;
    }
    
    public boolean isArquivoImagem() {
        if (retirada != null && retirada.getNomeArquivo() != null) {
            return FileUploadUtil.isImagem(retirada.getNomeArquivo());
        }
        return false;
    }
    
    public boolean isArquivoPdf() {
        if (retirada != null && retirada.getNomeArquivo() != null) {
            return FileUploadUtil.isPdf(retirada.getNomeArquivo());
        }
        return false;
    }

    /**
     * URL usada pelo preview: retorna o arquivo associado quando presente,
     * ou o placeholder 'no_doc.jpg' caso não exista anexo.
     */
    public String getUrlArquivoPreview() {
        if (retirada != null && retirada.getNomeArquivo() != null && !retirada.getNomeArquivo().isEmpty()) {
            return FileUploadUtil.obterUrlArquivo(retirada.getNomeArquivo());
        }
        return FileUploadUtil.obterUrlArquivo("no_doc.jpg");
    }

    /**
     * Indica se devemos exibir um preview em <img>. Retorna true quando o
     * anexo existente é uma imagem ou quando não há anexo (exibir placeholder).
     */
    public boolean isArquivoImagemPreview() {
        if (retirada != null && retirada.getNomeArquivo() != null && !retirada.getNomeArquivo().isEmpty()) {
            return FileUploadUtil.isImagem(retirada.getNomeArquivo());
        }
        // Sem arquivo -> exibimos placeholder (imagem)
        return true;
    }

    public boolean isArquivoPdfPreview() {
        if (retirada != null && retirada.getNomeArquivo() != null && !retirada.getNomeArquivo().isEmpty()) {
            return FileUploadUtil.isPdf(retirada.getNomeArquivo());
        }
        return false;
    }

    /**
     * Indica se há algum arquivo anexado (temporário ou persistido).
     */
    public boolean hasArquivoAnexado() {
        boolean temp = this.nomeArquivoTemp != null && !this.nomeArquivoTemp.isEmpty();
        boolean persist = this.retirada != null && this.retirada.getNomeArquivo() != null && !this.retirada.getNomeArquivo().isEmpty();
        return temp || persist;
    }
}