package br.gov.pr.guaira.animalys.controller;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.entity.RetiradaMedicamento;
import br.gov.pr.guaira.animalys.repository.AnimalDAO;
import br.gov.pr.guaira.animalys.repository.Proprietarios;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.service.RetiradaMedicamentoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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

    private RetiradaMedicamento retirada;
    private String cpfProprietario;
    private List<Animal> animaisProprietario;
    private Date dataRetirada;

    @PostConstruct
    public void inicializar() {
        limpar();
    }

    public void limpar() {
        this.retirada = new RetiradaMedicamento();
        this.cpfProprietario = "";
        this.animaisProprietario = new ArrayList<>();
        this.dataRetirada = new Date();
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

    public void salvar() {
        try {
            // Converter Date para Calendar
            if (this.dataRetirada != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(this.dataRetirada);
                this.retirada.setDataRetirada(cal);
            }

            this.retiradaService.salvar(this.retirada);
            FacesUtil.addInfoMessage("Retirada de medicamento registrada com sucesso!");
            limpar();
            
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
}