package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Atendimento;
import br.gov.pr.guaira.animalys.entity.ItemLoteAtendimento;
import br.gov.pr.guaira.animalys.entity.Lote;
import br.gov.pr.guaira.animalys.entity.Procedimento;
import br.gov.pr.guaira.animalys.entity.Profissional;
import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.entity.Solicitacao;
import br.gov.pr.guaira.animalys.entity.Status;
import br.gov.pr.guaira.animalys.entity.TipoAtendimento;
import br.gov.pr.guaira.animalys.entity.Tratamento;
import br.gov.pr.guaira.animalys.repository.Animais;
import br.gov.pr.guaira.animalys.repository.Atendimentos;
import br.gov.pr.guaira.animalys.repository.Lotes;
import br.gov.pr.guaira.animalys.repository.Procedimentos;
import br.gov.pr.guaira.animalys.repository.Profissionais;
import br.gov.pr.guaira.animalys.repository.Proprietarios;
import br.gov.pr.guaira.animalys.repository.Tratamentos;
import br.gov.pr.guaira.animalys.security.Seguranca;
import br.gov.pr.guaira.animalys.security.UsuarioLogado;
import br.gov.pr.guaira.animalys.service.AnimalService;
import br.gov.pr.guaira.animalys.service.AtendimentoService;
import br.gov.pr.guaira.animalys.service.LoteService;
import br.gov.pr.guaira.animalys.service.SolicitacaoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroFichaClinicaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // Controle de etapas
    private String cpfPesquisa;
    private Proprietario proprietarioSelecionado;
    private Animal animalSelecionado;
    private List<Animal> animaisDoProprietario;
    private List<Atendimento> atendimentosDoAnimal;
    
        // Animal de Rua
        private Boolean animalDeRua = false;
    // Formulário de atendimento
    private Atendimento novoAtendimento;
    private boolean mostrarFormularioAtendimento = false;
    private String tipoAtendimentoSelecionado = "CONSULTA"; // CONSULTA ou CASTRACAO
    
    // Dados específicos do atendimento
    private Profissional profissionalSelecionado;
    private List<Profissional> profissionaisSelect;
    
    // Procedimentos
    private Procedimento procedimentoSelecionado;
    private List<Procedimento> procedimentosSelecionados;
    
    // Medicamentos
    private ItemLoteAtendimento itemLoteSelecionado;
    private List<ItemLoteAtendimento> itensLotes;
    private Integer quantidade;
    
    // Tratamentos
    private List<Tratamento> tratamentos;
    
    // Microchip
    private String numeroMicrochip;
    
    // Injeções
    @Inject
    private Proprietarios proprietarios;
    
    @Inject
    private Animais animais;
    
    @Inject
    private Atendimentos atendimentos;
    
    @Inject
    private Profissionais profissionaisRepo;
    
    @Inject
    private Procedimentos procedimentos;
    
    @Inject
    private Lotes lotes;
    
    @Inject
    private Tratamentos tratamentosRepo;
    
    @Inject
    private AtendimentoService atendimentoService;
    
    @Inject
    private AnimalService animalService;
    
    @Inject
    private SolicitacaoService solicitacaoService;
    
    @Inject
    private LoteService loteService;
    
    @UsuarioLogado
    private Seguranca usuarioLogado;

    @PostConstruct
    public void init() {
        this.novoAtendimento = new Atendimento();
        this.itemLoteSelecionado = new ItemLoteAtendimento();
        this.procedimentosSelecionados = new ArrayList<>();
        this.itensLotes = new ArrayList<>();
        this.animaisDoProprietario = new ArrayList<>();
        this.atendimentosDoAnimal = new ArrayList<>();
        this.profissionaisSelect = profissionaisRepo.profissionaisCadastrados();
        this.tratamentos = tratamentosRepo.tratamentosCadastrados();
    }

    public void pesquisarProprietarioPorCpf() {
        try {
            if (cpfPesquisa == null || cpfPesquisa.trim().isEmpty()) {
                FacesUtil.addErrorMessage("Informe o CPF do proprietário!");
                return;
            }
            
            proprietarioSelecionado = proprietarios.proprietarioPorCPF(cpfPesquisa.trim());
            animaisDoProprietario = animais.animaisPorProprietario(proprietarioSelecionado);
            
            // Limpa seleções anteriores
            animalSelecionado = null;
            atendimentosDoAnimal.clear();
            mostrarFormularioAtendimento = false;
            
            FacesUtil.addInfoMessage("Proprietário encontrado: " + proprietarioSelecionado.getNome());
                        // Se animalDeRua estiver marcado, força proprietário 2219
                        if (Boolean.TRUE.equals(animalDeRua)) {
                            Proprietario proprietarioRua = proprietarios.porId(2219);
                            if (proprietarioRua != null) {
                                proprietarioSelecionado = proprietarioRua;
                                animaisDoProprietario = animais.animaisPorProprietario(proprietarioRua);
                            }
                        }
            
        } catch (NoResultException e) {
            FacesUtil.addErrorMessage("Proprietário não encontrado com o CPF informado!");
            proprietarioSelecionado = null;
            animaisDoProprietario.clear();
        }
    }

    public void selecionarAnimal() {
        if (animalSelecionado != null) {
            atendimentosDoAnimal = atendimentos.atendimentosPorAnimal(animalSelecionado);
            mostrarFormularioAtendimento = false;
            FacesUtil.addInfoMessage("Animal selecionado: " + animalSelecionado.getNome());
        }
    }

    public void novoAtendimento() {
        if (animalSelecionado == null) {
            FacesUtil.addErrorMessage("Selecione um animal primeiro!");
            return;
        }
        
        // Inicializa novo atendimento
        novoAtendimento = new Atendimento();
        novoAtendimento.setData(Calendar.getInstance());
        
        // Limpa dados do formulário
        procedimentosSelecionados.clear();
        itensLotes.clear();
        profissionalSelecionado = null;
        quantidade = null;
        numeroMicrochip = animalSelecionado.getNumeroMicrochip();
        
        mostrarFormularioAtendimento = true;
    }

    public void salvarAtendimento() {
        try {
            if (animalSelecionado == null) {
                FacesUtil.addErrorMessage("Animal não selecionado!");
                return;
            }
            
            if (profissionalSelecionado == null) {
                FacesUtil.addErrorMessage("Selecione o profissional responsável!");
                return;
            }
            
            if (novoAtendimento.getDiagnostico() == null || novoAtendimento.getDiagnostico().trim().isEmpty()) {
                FacesUtil.addErrorMessage("O diagnóstico/parecer é obrigatório!");
                return;
            }
            
            // Define o tipo de atendimento
            if ("CASTRACAO".equals(tipoAtendimentoSelecionado)) {
                novoAtendimento.setTipoAtendimento(TipoAtendimento.CASTRACAO);
                animalSelecionado.setStatus(Status.CASTRADO);
                // Salva a data da castração no animal
                animalSelecionado.setDataCastracao(novoAtendimento.getData());
            } else {
                novoAtendimento.setTipoAtendimento(TipoAtendimento.CONSULTA);
            }
            
            // Cria solicitação fictícia para manter a integridade
            Solicitacao solicitacao = new Solicitacao();
            solicitacao.setStatus(Status.FINALIZADO);
            solicitacao.setData(Calendar.getInstance());
            solicitacao = solicitacaoService.salvar(solicitacao);
            
            // Preenche o atendimento
            novoAtendimento.setAnimal(animalSelecionado);
            novoAtendimento.setSolicitacao(solicitacao);
            novoAtendimento.setProfissional(profissionalSelecionado);
            novoAtendimento.setProcedimentos(new ArrayList<>(procedimentosSelecionados));
            
            // Atualiza microchip se informado
            if (numeroMicrochip != null && !numeroMicrochip.trim().isEmpty()) {
                animalSelecionado.setNumeroMicrochip(numeroMicrochip.trim());
            }
            
            // Salva animal e atendimento PRIMEIRO para gerar o ID
            animalService.salvar(animalSelecionado);
            novoAtendimento = atendimentoService.salvar(novoAtendimento);
            
            // AGORA associa os medicamentos ao atendimento que já tem ID
            if (!itensLotes.isEmpty()) {
                for (ItemLoteAtendimento item : itensLotes) {
                    item.setAtendimento(novoAtendimento);
                }
                novoAtendimento.setItemLoteAtendimento(new ArrayList<>(itensLotes));
                atendimentoService.salvar(novoAtendimento); // Salva novamente com os medicamentos
            }
            
            // Atualiza a lista de atendimentos
            atendimentosDoAnimal = atendimentos.atendimentosPorAnimal(animalSelecionado);
            
            String tipoAtendimentoTexto = "CASTRACAO".equals(tipoAtendimentoSelecionado) ? "Castração" : "Consulta Clínica";
            FacesUtil.addInfoMessage("Atendimento de " + tipoAtendimentoTexto + " cadastrado com sucesso para " + animalSelecionado.getNome() + "!");
            mostrarFormularioAtendimento = false;
            
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.addErrorMessage("Erro ao salvar atendimento: " + e.getMessage());
        }
    }

    public void cancelarAtendimento() {
        mostrarFormularioAtendimento = false;
        novoAtendimento = new Atendimento();
        procedimentosSelecionados.clear();
        itensLotes.clear();
    }

    // Métodos para procedimentos
    public List<Procedimento> completarProcedimento(String descricao) {
        return procedimentos.porNome(descricao);
    }

    public void adicionarProcedimento() {
        if (procedimentoSelecionado != null && !procedimentosSelecionados.contains(procedimentoSelecionado)) {
            procedimentosSelecionados.add(procedimentoSelecionado);
            procedimentoSelecionado = null;
        }
    }

    public void removerProcedimento() {
        if (procedimentoSelecionado != null) {
            procedimentosSelecionados.remove(procedimentoSelecionado);
        }
    }

    public void removerProcedimento(Procedimento procedimento) {
        if (procedimento != null) {
            procedimentosSelecionados.remove(procedimento);
        }
    }

    // Métodos para medicamentos
    public List<Lote> completarLote(String descricao) {
        Calendar dataAtual = Calendar.getInstance();
        return lotes.porNomeProduto(descricao, dataAtual);
    }

    public void adicionarLote() {
        try {
            if (itemLoteSelecionado.getLote() == null) {
                FacesUtil.addErrorMessage("Selecione o medicamento!");
                return;
            }
            
            if (quantidade == null || quantidade <= 0) {
                FacesUtil.addErrorMessage("Informe a quantidade!");
                return;
            }
            
            if (itemLoteSelecionado.getLote().getQuantidade() < quantidade) {
                FacesUtil.addErrorMessage("Quantidade em estoque insuficiente!");
                return;
            }
            
            // Cria novo item
            ItemLoteAtendimento novoItem = new ItemLoteAtendimento();
            novoItem.setLote(itemLoteSelecionado.getLote());
            novoItem.setQuantidade(quantidade);
            
            // Adiciona à lista
            itensLotes.add(novoItem);
            
            // Atualiza estoque
            Lote lote = itemLoteSelecionado.getLote();
            lote.setQuantidade(lote.getQuantidade() - quantidade);
            loteService.salvar(lote);
            
            // Limpa seleção
            itemLoteSelecionado = new ItemLoteAtendimento();
            quantidade = null;
            
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.addErrorMessage("Erro ao adicionar medicamento!");
        }
    }

    public void removerProduto() {
        if (itemLoteSelecionado != null) {
            // Retorna ao estoque
            Lote lote = itemLoteSelecionado.getLote();
            lote.setQuantidade(lote.getQuantidade() + itemLoteSelecionado.getQuantidade());
            loteService.salvar(lote);
            
            // Remove da lista
            itensLotes.remove(itemLoteSelecionado);
        }
    }

    // Getters e Setters
    // Listener para mudança do campo Animal de Rua
    public void onAnimalDeRuaChange() {
        if (Boolean.TRUE.equals(animalDeRua)) {
            Proprietario proprietarioRua = proprietarios.porId(2219);
            if (proprietarioRua != null) {
                proprietarioSelecionado = proprietarioRua;
                animaisDoProprietario = animais.animaisPorProprietario(proprietarioRua);
            }
        } else {
            proprietarioSelecionado = null;
            animaisDoProprietario = new ArrayList<>();
        }
        animalSelecionado = null;
        atendimentosDoAnimal = new ArrayList<>();
        mostrarFormularioAtendimento = false;
    }
    
    public Boolean getAnimalDeRua() {
        return animalDeRua;
    }

    public void setAnimalDeRua(Boolean animalDeRua) {
        this.animalDeRua = animalDeRua;
    }
    public String getCpfPesquisa() {
        return cpfPesquisa;
    }

    public void setCpfPesquisa(String cpfPesquisa) {
        this.cpfPesquisa = cpfPesquisa;
    }

    public Proprietario getProprietarioSelecionado() {
        return proprietarioSelecionado;
    }

    public void setProprietarioSelecionado(Proprietario proprietarioSelecionado) {
        this.proprietarioSelecionado = proprietarioSelecionado;
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

    public void setAnimaisDoProprietario(List<Animal> animaisDoProprietario) {
        this.animaisDoProprietario = animaisDoProprietario;
    }

    public List<Atendimento> getAtendimentosDoAnimal() {
        return atendimentosDoAnimal;
    }

    public void setAtendimentosDoAnimal(List<Atendimento> atendimentosDoAnimal) {
        this.atendimentosDoAnimal = atendimentosDoAnimal;
    }

    public Atendimento getNovoAtendimento() {
        return novoAtendimento;
    }

    public void setNovoAtendimento(Atendimento novoAtendimento) {
        this.novoAtendimento = novoAtendimento;
    }

    public boolean isMostrarFormularioAtendimento() {
        return mostrarFormularioAtendimento;
    }

    public void setMostrarFormularioAtendimento(boolean mostrarFormularioAtendimento) {
        this.mostrarFormularioAtendimento = mostrarFormularioAtendimento;
    }

    public String getTipoAtendimentoSelecionado() {
        return tipoAtendimentoSelecionado;
    }

    public void setTipoAtendimentoSelecionado(String tipoAtendimentoSelecionado) {
        this.tipoAtendimentoSelecionado = tipoAtendimentoSelecionado;
    }

    public Profissional getProfissionalSelecionado() {
        return profissionalSelecionado;
    }

    public void setProfissionalSelecionado(Profissional profissionalSelecionado) {
        this.profissionalSelecionado = profissionalSelecionado;
    }

    public List<Profissional> getProfissionaisSelect() {
        return profissionaisSelect;
    }

    public void setProfissionaisSelect(List<Profissional> profissionaisSelect) {
        this.profissionaisSelect = profissionaisSelect;
    }

    public Procedimento getProcedimentoSelecionado() {
        return procedimentoSelecionado;
    }

    public void setProcedimentoSelecionado(Procedimento procedimentoSelecionado) {
        this.procedimentoSelecionado = procedimentoSelecionado;
    }

    public List<Procedimento> getProcedimentosSelecionados() {
        return procedimentosSelecionados;
    }

    public void setProcedimentosSelecionados(List<Procedimento> procedimentosSelecionados) {
        this.procedimentosSelecionados = procedimentosSelecionados;
    }

    public ItemLoteAtendimento getItemLoteSelecionado() {
        return itemLoteSelecionado;
    }

    public void setItemLoteSelecionado(ItemLoteAtendimento itemLoteSelecionado) {
        this.itemLoteSelecionado = itemLoteSelecionado;
    }

    public List<ItemLoteAtendimento> getItensLotes() {
        return itensLotes;
    }

    public void setItensLotes(List<ItemLoteAtendimento> itensLotes) {
        this.itensLotes = itensLotes;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public List<Tratamento> getTratamentos() {
        return tratamentos;
    }

    public void setTratamentos(List<Tratamento> tratamentos) {
        this.tratamentos = tratamentos;
    }

    public String getNumeroMicrochip() {
        return numeroMicrochip;
    }

    public void setNumeroMicrochip(String numeroMicrochip) {
        this.numeroMicrochip = numeroMicrochip;
    }
}
