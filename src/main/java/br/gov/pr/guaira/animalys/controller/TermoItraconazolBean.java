package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.Arrays;
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
            }
        }
    }

    public void salvar() {
        try {
            // Validação específica
            String mensagemErro = termoItraconazolService.validarTermo(this.termo);
            if (mensagemErro != null) {
                FacesUtil.addErrorMessage(mensagemErro);
                return;
            }

            this.termo = termoItraconazolService.salvar(this.termo);
            limpar();

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
}