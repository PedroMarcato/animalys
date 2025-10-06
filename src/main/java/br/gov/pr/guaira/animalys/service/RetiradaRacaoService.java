package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.RetiradaRacao;
import br.gov.pr.guaira.animalys.repository.RetiradasRacao;

import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class RetiradaRacaoService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RetiradasRacao retiradas;

    @Transactional
    public RetiradaRacao salvar(RetiradaRacao retirada) throws NegocioException {
        // Validações
        validarRetirada(retirada);
        
        // Salvar retirada
        return retiradas.guardar(retirada);
    }

    private void validarRetirada(RetiradaRacao retirada) throws NegocioException {
        if (retirada.getProprietario() == null) {
            throw new NegocioException("Proprietário é obrigatório.");
        }

        if (retirada.getAnimal() == null) {
            throw new NegocioException("Animal é obrigatório.");
        }

        if (retirada.getQuantidadeKg() == null || 
            retirada.getQuantidadeKg().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegocioException("Quantidade em quilogramas deve ser maior que zero.");
        }

        if (retirada.getQuantidadeKg().compareTo(new BigDecimal("999.999")) > 0) {
            throw new NegocioException("Quantidade não pode ser superior a 999,999 kg.");
        }

        if (retirada.getDataRetirada() == null) {
            throw new NegocioException("Data da retirada é obrigatória.");
        }

        if (retirada.getMesReferencia() == null || 
            retirada.getMesReferencia() < 1 || retirada.getMesReferencia() > 12) {
            throw new NegocioException("Mês de referência deve estar entre 1 (Janeiro) e 12 (Dezembro).");
        }

        // Validar se o animal pertence ao proprietário (comparar pelos IDs)
        if (retirada.getAnimal().getProprietario() == null || 
            !retirada.getAnimal().getProprietario().getIdProprietario().equals(retirada.getProprietario().getIdProprietario())) {
            throw new NegocioException("O animal selecionado não pertence ao proprietário informado.");
        }
    }

    @Transactional
    public void excluir(RetiradaRacao retirada) throws NegocioException {
        retiradas.remover(retirada);
    }
}