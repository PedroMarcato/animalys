package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;
import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.TermoItraconazol;
import br.gov.pr.guaira.animalys.repository.TermosItraconazol;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

@Named
public class TermoItraconazolService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TermosItraconazol termosItraconazol;

    @Transactional
    public TermoItraconazol salvar(TermoItraconazol termo) throws Exception {
        // Definir data de retirada se não foi definida
        if (termo.getDataRetirada() == null) {
            termo.setDataRetirada(Calendar.getInstance());
        }

        return termosItraconazol.salvar(termo);
    }

    @Transactional
    public void remover(TermoItraconazol termo) {
        termosItraconazol.remover(termo);
    }

    public boolean podeRemover(TermoItraconazol termo) {
        // Implementar lógica de validação se necessário
        // Por exemplo, não permitir remoção se já foi assinado algum mês
        return termo.getData1Mes() == null && termo.getData2Mes() == null && 
               termo.getData3Mes() == null && termo.getData4Mes() == null && 
               termo.getData5Mes() == null && termo.getData6Mes() == null && 
               termo.getData7Mes() == null;
    }

    public String validarTermo(TermoItraconazol termo) {
        if (termo.getAnimal() == null) {
            return "Animal deve ser selecionado!";
        }

        if (termo.getQuantidadeRetirada() == null || termo.getQuantidadeRetirada() <= 0) {
            return "Quantidade deve ser informada e maior que zero!";
        }

        if (termo.getPorteAnimal() == null || termo.getPorteAnimal().trim().isEmpty()) {
            return "Porte do animal deve ser selecionado!";
        }

        return null; // Sem erros
    }
    
    @Transactional
    public void excluir(TermoItraconazol termo) throws Exception {
        if (termo == null || termo.getIdTermoItraconazol() == null) {
            throw new Exception("Termo inválido para exclusão!");
        }
        
        // Validar se pode excluir (opcional - se quiser manter a validação)
        if (!podeRemover(termo)) {
            throw new Exception("Não é possível excluir este termo pois já possui acompanhamento registrado!");
        }
        
        termosItraconazol.remover(termo);
    }
}