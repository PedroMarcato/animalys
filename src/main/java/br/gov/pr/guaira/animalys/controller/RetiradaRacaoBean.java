package br.gov.pr.guaira.animalys.controller;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.entity.RetiradaRacao;
import br.gov.pr.guaira.animalys.repository.AnimalDAO;
import br.gov.pr.guaira.animalys.repository.Proprietarios;
import br.gov.pr.guaira.animalys.repository.RetiradasRacao;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.service.RetiradaRacaoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class RetiradaRacaoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RetiradaRacaoService retiradaService;

    @Inject
    private Proprietarios proprietarios;

    @Inject
    private AnimalDAO animalDAO;
    
    @Inject
    private RetiradasRacao retiradasRacao;

    private RetiradaRacao retirada;
    private String cpfProprietario;
    private List<Animal> animaisProprietario;
    private Date dataRetirada;
    private Integer animalSelecionadoId;

    @PostConstruct
    public void init() {
        // Método init sem lógica, a inicialização real acontece no inicializar()
    }
    
    public void inicializar() {
        // Verificar se há parâmetro de edição
        String retiradaIdParam = FacesContext.getCurrentInstance()
            .getExternalContext().getRequestParameterMap().get("retirada");
            
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
            RetiradaRacao retiradaExistente = retiradasRacao.porId(retiradaId);
            if (retiradaExistente != null) {
                this.retirada = retiradaExistente;
                
                // Carregar dados do proprietário
                if (retirada.getProprietario() != null) {
                    this.cpfProprietario = retirada.getProprietario().getCpf();
                    
                    // Carregar lista de animais do proprietário
                    this.animaisProprietario = animalDAO.buscarPorProprietario(
                        retirada.getProprietario().getIdProprietario());
                    
                    // Debug: Verificar se o animal da retirada está na lista
                    if (retirada.getAnimal() != null) {
                        System.out.println("DEBUG: Animal da retirada: " + retirada.getAnimal().getNome() + " (ID: " + retirada.getAnimal().getIdAnimal() + ")");
                        System.out.println("DEBUG: Lista de animais carregada: " + animaisProprietario.size() + " animais");
                        for (Animal a : animaisProprietario) {
                            System.out.println("DEBUG: - Animal: " + a.getNome() + " (ID: " + a.getIdAnimal() + ")");
                        }
                        
                        // Definir o ID do animal selecionado para o selectOneMenu
                        this.animalSelecionadoId = retirada.getAnimal().getIdAnimal();
                        System.out.println("DEBUG: Animal ID definido: " + this.animalSelecionadoId);
                    }
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
        this.retirada = new RetiradaRacao();
        this.cpfProprietario = "";
        this.animaisProprietario = new ArrayList<>();
        this.dataRetirada = new Date();
        this.animalSelecionadoId = null;
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

    public void salvar() {
        try {
            // Calcular automaticamente o mês de referência baseado na última data preenchida
            calcularMesReferencia();
            
            // Debug antes de salvar
            System.out.println("DEBUG SALVAR - Proprietário ID: " + (retirada.getProprietario() != null ? retirada.getProprietario().getIdProprietario() : "null"));
            System.out.println("DEBUG SALVAR - Animal ID: " + (retirada.getAnimal() != null ? retirada.getAnimal().getIdAnimal() : "null"));
            System.out.println("DEBUG SALVAR - Animal Proprietário ID: " + (retirada.getAnimal() != null && retirada.getAnimal().getProprietario() != null ? retirada.getAnimal().getProprietario().getIdProprietario() : "null"));
            System.out.println("DEBUG SALVAR - Mês de Referência: " + retirada.getMesReferencia());
            
            boolean isEdicao = (retirada.getIdRetiradaRacao() != null);
            this.retirada = retiradaService.salvar(this.retirada);
            
            if (isEdicao) {
                FacesUtil.addInfoMessage("Retirada de ração atualizada com sucesso!");
            } else {
                FacesUtil.addInfoMessage("Retirada de ração salva com sucesso!");
                limpar();
            }
        } catch (NegocioException ne) {
            FacesUtil.addErrorMessage(ne.getMessage());
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro inesperado ao salvar retirada: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Calcula automaticamente o mês de referência baseado na última data de retirada preenchida
     */
    private void calcularMesReferencia() {
        Date ultimaData = null;
        
        // Verificar as datas do 12º ao 1º mês para encontrar a última preenchida
        if (retirada.getData12Mes() != null) {
            ultimaData = retirada.getData12Mes();
        } else if (retirada.getData11Mes() != null) {
            ultimaData = retirada.getData11Mes();
        } else if (retirada.getData10Mes() != null) {
            ultimaData = retirada.getData10Mes();
        } else if (retirada.getData9Mes() != null) {
            ultimaData = retirada.getData9Mes();
        } else if (retirada.getData8Mes() != null) {
            ultimaData = retirada.getData8Mes();
        } else if (retirada.getData7Mes() != null) {
            ultimaData = retirada.getData7Mes();
        } else if (retirada.getData6Mes() != null) {
            ultimaData = retirada.getData6Mes();
        } else if (retirada.getData5Mes() != null) {
            ultimaData = retirada.getData5Mes();
        } else if (retirada.getData4Mes() != null) {
            ultimaData = retirada.getData4Mes();
        } else if (retirada.getData3Mes() != null) {
            ultimaData = retirada.getData3Mes();
        } else if (retirada.getData2Mes() != null) {
            ultimaData = retirada.getData2Mes();
        } else if (retirada.getData1Mes() != null) {
            ultimaData = retirada.getData1Mes();
        }
        
        // Se encontrou uma data, extrair o mês
        if (ultimaData != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(ultimaData);
            int mes = cal.get(Calendar.MONTH) + 1; // Calendar.MONTH é 0-based
            retirada.setMesReferencia(mes);
        } else {
            // Se não há nenhuma data preenchida, não define mês de referência
            retirada.setMesReferencia(null);
        }
    }
    
    /**
     * Retorna o mês de referência formatado para exibição
     */
    public String getMesReferenciaFormatado() {
        if (retirada.getMesReferencia() == null) {
            return "Não definido";
        }
        
        String[] nomesMeses = {
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        };
        
        int mes = retirada.getMesReferencia();
        if (mes >= 1 && mes <= 12) {
            return nomesMeses[mes - 1];
        }
        
        return "Não definido";
    }

    public String getClassificacaoIdade() {
        if (retirada.getAnimal() == null || retirada.getAnimal().getIdade() == null) {
            return "";
        }

        // Retorna a idade já cadastrada no animal
        return retirada.getAnimal().getIdade();
    }

    // Lista de meses para o selectOneMenu
    public List<MesReferencia> getMesesReferencia() {
        List<MesReferencia> meses = new ArrayList<>();
        String[] nomesMeses = {
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        };
        
        for (int i = 0; i < nomesMeses.length; i++) {
            meses.add(new MesReferencia(i + 1, nomesMeses[i]));
        }
        
        return meses;
    }

    // Classe interna para representar mês de referência
    public static class MesReferencia {
        private Integer numero;
        private String nome;

        public MesReferencia(Integer numero, String nome) {
            this.numero = numero;
            this.nome = nome;
        }

        public Integer getNumero() {
            return numero;
        }

        public void setNumero(Integer numero) {
            this.numero = numero;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        @Override
        public String toString() {
            return nome;
        }
    }

    // Getters e Setters
    public RetiradaRacao getRetirada() {
        return retirada;
    }

    public void setRetirada(RetiradaRacao retirada) {
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

    public Date getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(Date dataRetirada) {
        this.dataRetirada = dataRetirada;
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
                    System.out.println("DEBUG: Animal sincronizado - " + animal.getNome() + 
                                     " (ID: " + animal.getIdAnimal() + 
                                     ", Proprietário ID: " + (animal.getProprietario() != null ? animal.getProprietario().getIdProprietario() : "null") + ")");
                    break;
                }
            }
        } else {
            this.retirada.setAnimal(null);
            System.out.println("DEBUG: Animal limpo");
        }
    }
}