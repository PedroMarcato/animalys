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

        // Validar se pelo menos um dos meses tem quantidade e data preenchidas
        boolean temPeloMenosUmaRetirada = false;
        
        if (validarMes(retirada.getData1Mes(), retirada.getQuantidade1Mes())) temPeloMenosUmaRetirada = true;
        if (validarMes(retirada.getData2Mes(), retirada.getQuantidade2Mes())) temPeloMenosUmaRetirada = true;
        if (validarMes(retirada.getData3Mes(), retirada.getQuantidade3Mes())) temPeloMenosUmaRetirada = true;
        if (validarMes(retirada.getData4Mes(), retirada.getQuantidade4Mes())) temPeloMenosUmaRetirada = true;
        if (validarMes(retirada.getData5Mes(), retirada.getQuantidade5Mes())) temPeloMenosUmaRetirada = true;
        if (validarMes(retirada.getData6Mes(), retirada.getQuantidade6Mes())) temPeloMenosUmaRetirada = true;
        if (validarMes(retirada.getData7Mes(), retirada.getQuantidade7Mes())) temPeloMenosUmaRetirada = true;
        if (validarMes(retirada.getData8Mes(), retirada.getQuantidade8Mes())) temPeloMenosUmaRetirada = true;
        if (validarMes(retirada.getData9Mes(), retirada.getQuantidade9Mes())) temPeloMenosUmaRetirada = true;
        if (validarMes(retirada.getData10Mes(), retirada.getQuantidade10Mes())) temPeloMenosUmaRetirada = true;
        if (validarMes(retirada.getData11Mes(), retirada.getQuantidade11Mes())) temPeloMenosUmaRetirada = true;
        if (validarMes(retirada.getData12Mes(), retirada.getQuantidade12Mes())) temPeloMenosUmaRetirada = true;
        
        if (!temPeloMenosUmaRetirada) {
            throw new NegocioException("É necessário preencher pelo menos uma data e quantidade de retirada.");
        }

        // Validar se todas as quantidades preenchidas são maiores que zero
        validarQuantidadeMes(retirada.getData1Mes(), retirada.getQuantidade1Mes(), "1º mês");
        validarQuantidadeMes(retirada.getData2Mes(), retirada.getQuantidade2Mes(), "2º mês");
        validarQuantidadeMes(retirada.getData3Mes(), retirada.getQuantidade3Mes(), "3º mês");
        validarQuantidadeMes(retirada.getData4Mes(), retirada.getQuantidade4Mes(), "4º mês");
        validarQuantidadeMes(retirada.getData5Mes(), retirada.getQuantidade5Mes(), "5º mês");
        validarQuantidadeMes(retirada.getData6Mes(), retirada.getQuantidade6Mes(), "6º mês");
        validarQuantidadeMes(retirada.getData7Mes(), retirada.getQuantidade7Mes(), "7º mês");
        validarQuantidadeMes(retirada.getData8Mes(), retirada.getQuantidade8Mes(), "8º mês");
        validarQuantidadeMes(retirada.getData9Mes(), retirada.getQuantidade9Mes(), "9º mês");
        validarQuantidadeMes(retirada.getData10Mes(), retirada.getQuantidade10Mes(), "10º mês");
        validarQuantidadeMes(retirada.getData11Mes(), retirada.getQuantidade11Mes(), "11º mês");
        validarQuantidadeMes(retirada.getData12Mes(), retirada.getQuantidade12Mes(), "12º mês");

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
    
    /**
     * Valida se um mês tem data e quantidade válidas
     */
    private boolean validarMes(java.util.Date data, BigDecimal quantidade) {
        if (data != null && quantidade != null && quantidade.compareTo(BigDecimal.ZERO) > 0) {
            return true;
        }
        return false;
    }
    
    /**
     * Valida quantidade de um mês específico
     */
    private void validarQuantidadeMes(java.util.Date data, BigDecimal quantidade, String nomeMes) throws NegocioException {
        // Se a data foi preenchida, a quantidade também deve ser preenchida
        if (data != null) {
            if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
                throw new NegocioException("A quantidade do " + nomeMes + " deve ser maior que zero quando a data for preenchida.");
            }
            if (quantidade.compareTo(new BigDecimal("999.999")) > 0) {
                throw new NegocioException("A quantidade do " + nomeMes + " não pode ser superior a 999,999 kg.");
            }
        }
        
        // Se a quantidade foi preenchida, a data também deve ser preenchida
        if (quantidade != null && quantidade.compareTo(BigDecimal.ZERO) > 0 && data == null) {
            throw new NegocioException("A data do " + nomeMes + " é obrigatória quando a quantidade for preenchida.");
        }
    }

    @Transactional
    public void excluir(RetiradaRacao retirada) throws NegocioException {
        retiradas.remover(retirada);
    }
}