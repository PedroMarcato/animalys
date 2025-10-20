package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.entity.TermoItraconazol;
import br.gov.pr.guaira.animalys.repository.Animais;
import br.gov.pr.guaira.animalys.repository.Proprietarios;
import br.gov.pr.guaira.animalys.service.TermoItraconazolService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;
import br.gov.pr.guaira.animalys.util.FileUploadUtil;

import org.primefaces.model.UploadedFile;
import org.primefaces.event.FileUploadEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Named
@ViewScoped
public class TermoItraconazolBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TermoItraconazolService termoItraconazolService;
    
    @Inject
    private Proprietarios proprietarios;
    
    @Inject
    private Animais animais;

    private TermoItraconazol termo;
    private String cpfProprietario;
    private List<Animal> animaisProprietario;
    private Proprietario proprietarioSelecionado;
    private UploadedFile arquivoUpload;
    private String nomeArquivoTemp;
    // campo de visualização pré-calculado para evitar navegar proxies lazy em renderizações parciais
    private String especieNomeView;
    private static final Log logger = LogFactory.getLog(TermoItraconazolBean.class);

    @PostConstruct
    public void init() {
        limpar();
    }

    public void inicializar() {
        if (this.termo == null) {
            limpar();
        } else if (this.termo.getIdTermoItraconazol() != null) {
            // Modo edição - carregar dados do termo existente
            carregarDadosEdicao();
        } else {
            // Garantir que o termo sempre tenha uma instância válida
            this.termo = new TermoItraconazol();
        }
    }
    
    private void carregarDadosEdicao() {
        if (this.termo != null && this.termo.getAnimal() != null) {
            // Carregar proprietário e CPF
            Proprietario proprietario = this.termo.getAnimal().getProprietario();
            if (proprietario != null) {
                this.proprietarioSelecionado = proprietario;
                this.cpfProprietario = proprietario.getCpf();
                
                // Carregar lista de animais do proprietário
                try {
                    this.animaisProprietario = animais.animaisPorProprietario(proprietario);
                } catch (Exception e) {
                    FacesUtil.addErrorMessage("Erro ao carregar animais do proprietário: " + e.getMessage());
                }
                // Precalcula nome da espécie para evitar LazyInitializationException em partial render
                try {
                    if (this.termo.getAnimal() != null && this.termo.getAnimal().getRaca() != null && this.termo.getAnimal().getRaca().getEspecie() != null) {
                        this.especieNomeView = this.termo.getAnimal().getRaca().getEspecie().getNome();
                    } else {
                        this.especieNomeView = null;
                    }
                } catch (Exception ex) {
                    this.especieNomeView = null;
                }
            }
        }
    }

    public void handleFileUpload() {
        if (arquivoUpload != null) {
            try {
                System.out.println("[server-debug][TermoItraconazol] handleFileUpload: início - file=" + arquivoUpload.getFileName() + " size=" + arquivoUpload.getSize());
                logger.debug("handleFileUpload: início");
                String nomeArquivo = FileUploadUtil.salvarArquivo(arquivoUpload);

                // Remover arquivo antigo se existir
                if (termo.getNomeArquivo() != null && !termo.getNomeArquivo().isEmpty()) {
                    FileUploadUtil.removerArquivo(termo.getNomeArquivo());
                }

                termo.setNomeArquivo(nomeArquivo);
                this.nomeArquivoTemp = nomeArquivo;
                System.out.println("[server-debug][TermoItraconazol] handleFileUpload: arquivo salvo temporariamente '" + nomeArquivo + "'");
                logger.info("handleFileUpload: arquivo salvo temporariamente '" + nomeArquivo + "'");
                FacesUtil.addInfoMessage("Arquivo anexado com sucesso!");
            } catch (IllegalArgumentException e) {
                System.out.println("[server-debug][TermoItraconazol] handleFileUpload: validação falhou: " + e.getMessage());
                logger.warn("handleFileUpload: validação falhou: " + e.getMessage());
                FacesUtil.addErrorMessage(e.getMessage());
            } catch (Exception e) {
                System.out.println("[server-debug][TermoItraconazol] handleFileUpload: erro inesperado: " + e.getMessage());
                logger.error("handleFileUpload: erro inesperado: " + e.getMessage(), e);
                FacesUtil.addErrorMessage("Erro ao anexar arquivo: " + e.getMessage());
            }
        }
    }

    public void uploadListener(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        if (file != null) {
            try {
                System.out.println("[server-debug][TermoItraconazol] uploadListener: recebendo arquivo '" + file.getFileName() + "' tamanho=" + file.getSize() + " contentType=" + file.getContentType());
                logger.debug("uploadListener: recebendo arquivo '" + file.getFileName() + "' tamanho=" + file.getSize());
                String nomeArquivo = FileUploadUtil.salvarArquivo(file);
                if (this.nomeArquivoTemp != null && !this.nomeArquivoTemp.equals(termo.getNomeArquivo())) {
                    FileUploadUtil.removerArquivo(this.nomeArquivoTemp);
                }
                this.nomeArquivoTemp = nomeArquivo;
                termo.setNomeArquivo(nomeArquivo);
                logger.info("uploadListener: arquivo enviado e salvo temporariamente como '" + nomeArquivo + "'");
                System.out.println("[server-debug][TermoItraconazol] uploadListener: arquivo salvo temporariamente '" + nomeArquivo + "'");
                FacesUtil.addInfoMessage("Arquivo enviado ao servidor com sucesso.");
            } catch (IllegalArgumentException e) {
                System.out.println("[server-debug][TermoItraconazol] uploadListener: validação falhou: " + e.getMessage());
                logger.warn("uploadListener: validação falhou: " + e.getMessage());
                FacesUtil.addErrorMessage(e.getMessage());
            } catch (Exception e) {
                System.out.println("[server-debug][TermoItraconazol] uploadListener: erro ao salvar arquivo no servidor: " + e.getMessage());
                logger.error("uploadListener: erro ao salvar arquivo no servidor: " + e.getMessage(), e);
                FacesUtil.addErrorMessage("Erro ao salvar arquivo no servidor: " + e.getMessage());
            }
        }
    }
    
    public void removerArquivoAnexado() {
        if (termo.getNomeArquivo() != null && !termo.getNomeArquivo().isEmpty()) {
            try {
                System.out.println("[server-debug][TermoItraconazol] removerArquivoAnexado: removendo arquivo '" + termo.getNomeArquivo() + "'");
                FileUploadUtil.removerArquivo(termo.getNomeArquivo());
                termo.setNomeArquivo(null);
                this.nomeArquivoTemp = null;
                logger.info("removerArquivoAnexado: arquivo removido com sucesso");
                FacesUtil.addInfoMessage("Arquivo removido com sucesso!");
            } catch (Exception e) {
                System.out.println("[server-debug][TermoItraconazol] removerArquivoAnexado: erro ao remover arquivo: " + e.getMessage());
                logger.error("removerArquivoAnexado: erro ao remover arquivo: " + e.getMessage(), e);
                FacesUtil.addErrorMessage("Erro ao remover arquivo: " + e.getMessage());
            }
        }
    }

    public void salvar() {
        try {
            if (this.nomeArquivoTemp != null) {
                termo.setNomeArquivo(this.nomeArquivoTemp);
            }
            
            // Validação específica do termo (campos obrigatórios gerais)
            String mensagemErro = termoItraconazolService.validarTermo(this.termo);
            if (mensagemErro != null) {
                FacesUtil.addErrorMessage(mensagemErro);
                return;
            }

            // Validação específica dos pares dataNMes / quantidadeNMes
            String mensagemMeses = validarMeses();
            if (mensagemMeses != null) {
                FacesUtil.addErrorMessage(mensagemMeses);
                return;
            }

            logger.debug("salvar: salvando termo id=" + (termo.getIdTermoItraconazol()!=null?termo.getIdTermoItraconazol():"(novo)"));
            this.termo = termoItraconazolService.salvar(this.termo);
            limpar();

            logger.info("salvar: termo salvo com sucesso id=" + (termo.getIdTermoItraconazol()!=null?termo.getIdTermoItraconazol():"(novo)"));
            FacesUtil.addInfoMessage("Termo de Itraconazol salvo com sucesso!");
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro ao salvar termo: " + e.getMessage());
        }
    }

    public void limpar() {
        this.termo = new TermoItraconazol();
        this.cpfProprietario = null;
        this.animaisProprietario = null;
        this.proprietarioSelecionado = null;
        this.especieNomeView = null;
    }

    public void buscarProprietarioPorCpf() {
        if (cpfProprietario != null && !cpfProprietario.trim().isEmpty()) {
            try {
                this.proprietarioSelecionado = proprietarios.proprietarioPorCPF(cpfProprietario);
                
                if (proprietarioSelecionado != null) {
                    // Buscar animais do proprietário
                    this.animaisProprietario = animais.animaisPorProprietario(proprietarioSelecionado);
                    
                    if (animaisProprietario == null || animaisProprietario.isEmpty()) {
                        FacesUtil.addErrorMessage("O proprietário não possui animais cadastrados.");
                        this.proprietarioSelecionado = null;
                    }
                } else {
                    FacesUtil.addErrorMessage("Proprietário não encontrado com o CPF informado.");
                    this.animaisProprietario = null;
                    this.proprietarioSelecionado = null;
                }
            } catch (Exception e) {
                FacesUtil.addErrorMessage("Erro ao buscar proprietário: " + e.getMessage());
                this.animaisProprietario = null;
                this.proprietarioSelecionado = null;
            }
        } else {
            this.animaisProprietario = null;
            this.proprietarioSelecionado = null;
        }
    }

    public void onAnimalChange() {
        // Método para lidar com a mudança do animal selecionado
        // Garantir que o termo existe
        if (this.termo == null) {
            this.termo = new TermoItraconazol();
        }
        // Atualizar campo de exibição quando o animal mudar (por seleção no formulário)
        try {
            if (this.termo.getAnimal() != null && this.termo.getAnimal().getRaca() != null && this.termo.getAnimal().getRaca().getEspecie() != null) {
                this.especieNomeView = this.termo.getAnimal().getRaca().getEspecie().getNome();
            } else {
                this.especieNomeView = null;
            }
        } catch (Exception ex) {
            this.especieNomeView = null;
        }
    }

    public List<String> completarNomeAnimal(String query) {
        // Este método poderia buscar nomes de animais para autocompletar
        // Por simplicidade, retorno lista vazia - será usado o seletor de animal
        return Arrays.asList();
    }

    public boolean isEditando() {
        return this.termo.getIdTermoItraconazol() != null;
    }

    // Getters e Setters
    public TermoItraconazol getTermo() {
        if (this.termo == null) {
            this.termo = new TermoItraconazol();
        }
        return termo;
    }

    public void setTermo(TermoItraconazol termo) {
        if (termo == null) {
            this.termo = new TermoItraconazol();
        } else {
            this.termo = termo;
        }
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

    public Proprietario getProprietarioSelecionado() {
        return proprietarioSelecionado;
    }

    public void setProprietarioSelecionado(Proprietario proprietarioSelecionado) {
        this.proprietarioSelecionado = proprietarioSelecionado;
    }

    public boolean isMostrarDadosAnimal() {
        return this.termo != null && this.termo.getAnimal() != null;
    }

    /**
     * Valida os pares dataNMes / quantidadeNMes: exige que ambos sejam nulos ou ambos preenchidos.
     * Retorna mensagem de erro (String) para exibir ou null quando válido.
     */
    private String validarMeses() {
        if (this.termo == null) {
            return null;
        }

        Date[] datas = new Date[] {
            this.termo.getData1Mes(), this.termo.getData2Mes(), this.termo.getData3Mes(),
            this.termo.getData4Mes(), this.termo.getData5Mes(), this.termo.getData6Mes(),
            this.termo.getData7Mes()
        };

        Integer[] qts = new Integer[] {
            this.termo.getQuantidade1Mes(), this.termo.getQuantidade2Mes(), this.termo.getQuantidade3Mes(),
            this.termo.getQuantidade4Mes(), this.termo.getQuantidade5Mes(), this.termo.getQuantidade6Mes(),
            this.termo.getQuantidade7Mes()
        };

        for (int i = 0; i < 7; i++) {
            boolean temData = datas[i] != null;
            boolean temQt = qts[i] != null && qts[i] > 0;
            if (temData && !temQt) {
                return String.format("Informar quantidade para o %dº mês ou remover a data.", i + 1);
            }
            if (!temData && temQt) {
                return String.format("Informar data para o %dº mês ou remover a quantidade.", i + 1);
            }
        }

        return null;
    }
    
    public UploadedFile getArquivoUpload() {
        return arquivoUpload;
    }
    
    public void setArquivoUpload(UploadedFile arquivoUpload) {
        this.arquivoUpload = arquivoUpload;
    }
    
    public String getUrlArquivo() {
        if (termo != null && termo.getNomeArquivo() != null) {
            return FileUploadUtil.obterUrlArquivo(termo.getNomeArquivo());
        }
        return null;
    }
    
    public boolean isArquivoImagem() {
        if (termo != null && termo.getNomeArquivo() != null) {
            return FileUploadUtil.isImagem(termo.getNomeArquivo());
        }
        return false;
    }
    
    public boolean isArquivoPdf() {
        if (termo != null && termo.getNomeArquivo() != null) {
            return FileUploadUtil.isPdf(termo.getNomeArquivo());
        }
        return false;
    }

    /**
     * URL usada pelo preview: retorna o arquivo associado quando presente,
     * ou o placeholder 'no_doc.jpg' caso não exista anexo.
     */
    public String getUrlArquivoPreview() {
        if (termo != null && termo.getNomeArquivo() != null && !termo.getNomeArquivo().isEmpty()) {
            return FileUploadUtil.obterUrlArquivo(termo.getNomeArquivo());
        }
        // fallback para imagem padrão no mesmo diretório de uploads
        return FileUploadUtil.obterUrlArquivo("no_doc.jpg");
    }

    /**
     * Indica se devemos exibir um preview em <img>. Retorna true quando o
     * anexo existente é uma imagem ou quando não há anexo (exibir placeholder).
     */
    public boolean isArquivoImagemPreview() {
        if (termo != null && termo.getNomeArquivo() != null && !termo.getNomeArquivo().isEmpty()) {
            return FileUploadUtil.isImagem(termo.getNomeArquivo());
        }
        // Quando não houver arquivo, mostramos o placeholder (imagem)
        return true;
    }

    public boolean isArquivoPdfPreview() {
        if (termo != null && termo.getNomeArquivo() != null && !termo.getNomeArquivo().isEmpty()) {
            return FileUploadUtil.isPdf(termo.getNomeArquivo());
        }
        return false;
    }

    /**
     * Indica se há algum arquivo anexado (temporário ou persistido).
     */
    public boolean hasArquivoAnexado() {
        boolean temp = this.nomeArquivoTemp != null && !this.nomeArquivoTemp.isEmpty();
        boolean persist = this.termo != null && this.termo.getNomeArquivo() != null && !this.termo.getNomeArquivo().isEmpty();
        return temp || persist;
    }

    public String getEspecieNomeView() {
        return especieNomeView;
    }

    public void setEspecieNomeView(String especieNomeView) {
        this.especieNomeView = especieNomeView;
    }
}