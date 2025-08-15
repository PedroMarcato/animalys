package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.Solicitacao;
import br.gov.pr.guaira.animalys.repository.Solicitacoes;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class SolicitacaoService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private Solicitacoes solicitacoes;

	@Transactional
	public Solicitacao salvar(Solicitacao solicitacao) {
		return solicitacoes.guardar(solicitacao);
	}
}
