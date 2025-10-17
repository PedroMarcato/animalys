package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.RetiradaRacao;
import br.gov.pr.guaira.animalys.report.GerarTermoRacao;
import br.gov.pr.guaira.animalys.repository.RetiradasRacao;
import br.gov.pr.guaira.animalys.service.NegocioException;
import br.gov.pr.guaira.animalys.service.RetiradaRacaoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaRetiradaRacaoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RetiradasRacao retiradas;

    @Inject
    private RetiradaRacaoService retiradaService;

    private List<RetiradaRacao> retiradasFiltradas;
    private RetiradaRacao retiradaSelecionada;
    
    // Filtros
    private String cpfProprietario;
    private String nomeAnimal;
    private Integer mesReferencia;
    private Integer anoReferencia;

    @PostConstruct
    public void inicializar() {
        this.retiradasFiltradas = new ArrayList<>();
        pesquisar();
    }

    public void pesquisar() {
        try {
            if (cpfProprietario != null && !cpfProprietario.trim().isEmpty()) {
                // Usar busca por CPF
                this.retiradasFiltradas = retiradas.filtradasPorCpf(cpfProprietario, nomeAnimal, mesReferencia, anoReferencia);
            } else {
                // Usar busca por nome (sem filtros)
                this.retiradasFiltradas = retiradas.filtradas(null, nomeAnimal, mesReferencia, anoReferencia);
            }
            
            if (retiradasFiltradas.isEmpty()) {
                FacesUtil.addInfoMessage("Nenhuma retirada de ração encontrada com os filtros informados.");
            } else {
                FacesUtil.addInfoMessage("Encontradas " + retiradasFiltradas.size() + " retirada(s) de ração.");
            }
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro ao pesquisar retiradas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void limparFiltros() {
        this.cpfProprietario = null;
        this.nomeAnimal = null;
        this.mesReferencia = null;
        this.anoReferencia = null;
        pesquisar();
    }
    
    public void prepararExclusao(RetiradaRacao retirada) {
        this.retiradaSelecionada = retirada;
    }
    
    public void confirmarExclusao() {
        try {
            retiradaService.excluir(retiradaSelecionada);
            retiradasFiltradas.remove(retiradaSelecionada);
            FacesUtil.addInfoMessage("Retirada de ração excluída com sucesso!");
        } catch (NegocioException ne) {
            FacesUtil.addErrorMessage(ne.getMessage());
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro inesperado ao excluir retirada: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void excluir() {
        try {
            retiradaService.excluir(retiradaSelecionada);
            retiradasFiltradas.remove(retiradaSelecionada);
            FacesUtil.addInfoMessage("Retirada de ração excluída com sucesso!");
        } catch (NegocioException ne) {
            FacesUtil.addErrorMessage(ne.getMessage());
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro inesperado ao excluir retirada: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void visualizarTermo(RetiradaRacao retirada) {
        try {
            // Recarrega a retirada com todos os dados necessários usando fetch joins
            RetiradaRacao retiradaCompleta = retiradas.porId(retirada.getIdRetiradaRacao());
            
            if (retiradaCompleta != null) {
                // Gera o PDF do termo
                GerarTermoRacao gerador = new GerarTermoRacao();
                gerador.gerar(retiradaCompleta);
            } else {
                FacesUtil.addErrorMessage("Retirada de ração não encontrada!");
            }
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro ao gerar termo de ração: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Lista de meses para filtro
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
    public List<RetiradaRacao> getRetiradasFiltradas() {
        return retiradasFiltradas;
    }

    public void setRetiradasFiltradas(List<RetiradaRacao> retiradasFiltradas) {
        this.retiradasFiltradas = retiradasFiltradas;
    }

    public RetiradaRacao getRetiradaSelecionada() {
        return retiradaSelecionada;
    }

    public void setRetiradaSelecionada(RetiradaRacao retiradaSelecionada) {
        this.retiradaSelecionada = retiradaSelecionada;
    }

    public String getCpfProprietario() {
        return cpfProprietario;
    }

    public void setCpfProprietario(String cpfProprietario) {
        this.cpfProprietario = cpfProprietario;
    }

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public Integer getMesReferencia() {
        return mesReferencia;
    }

    public void setMesReferencia(Integer mesReferencia) {
        this.mesReferencia = mesReferencia;
    }

    public Integer getAnoReferencia() {
        return anoReferencia;
    }

    public void setAnoReferencia(Integer anoReferencia) {
        this.anoReferencia = anoReferencia;
    }

    // Lista de anos para filtro (últimos 10 anos + próximos 2 anos)
    public List<Integer> getAnosReferencia() {
        List<Integer> anos = new ArrayList<>();
        int anoAtual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        
        // Adiciona os últimos 10 anos até os próximos 2 anos
        for (int i = anoAtual - 10; i <= anoAtual + 2; i++) {
            anos.add(i);
        }
        
        return anos;
    }
}