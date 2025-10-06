package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.RetiradaMedicamento;
import br.gov.pr.guaira.animalys.repository.RetiradasMedicamento;

import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class RetiradaMedicamentoService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RetiradasMedicamento retiradas;

    @Transactional
    public RetiradaMedicamento salvar(RetiradaMedicamento retirada) throws NegocioException {
        // Validações
        validarRetirada(retirada);
        
        // Salvar retirada (sem controle de estoque)
        return retiradas.guardar(retirada);
    }

    private void validarRetirada(RetiradaMedicamento retirada) throws NegocioException {
        if (retirada.getProprietario() == null) {
            throw new NegocioException("Proprietário é obrigatório.");
        }

        if (retirada.getAnimal() == null) {
            throw new NegocioException("Animal é obrigatório.");
        }

        if (retirada.getNomeMedicamento() == null || retirada.getNomeMedicamento().trim().isEmpty()) {
            throw new NegocioException("Nome do medicamento é obrigatório.");
        }

        if (retirada.getQuantidade() == null || retirada.getQuantidade() <= 0) {
            throw new NegocioException("Quantidade deve ser maior que zero.");
        }

        if (retirada.getDataRetirada() == null) {
            throw new NegocioException("Data da retirada é obrigatória.");
        }

        // Verificar se o animal pertence ao proprietário (usando query para evitar LazyInitializationException)
        boolean animalPertenceProprietario = retiradas.verificarAnimalPertenceProprietario(
                retirada.getAnimal().getIdAnimal(), 
                retirada.getProprietario().getIdProprietario());

        if (!animalPertenceProprietario) {
            throw new NegocioException("O animal selecionado não pertence ao proprietário informado.");
        }
    }
    
    @Transactional
    public void excluir(RetiradaMedicamento retirada) throws NegocioException {
        retiradas.remover(retirada);
    }
}