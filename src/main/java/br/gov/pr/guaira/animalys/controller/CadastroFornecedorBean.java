package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Cidade;
import br.gov.pr.guaira.animalys.entity.Contato;
import br.gov.pr.guaira.animalys.entity.Endereco;
import br.gov.pr.guaira.animalys.entity.Fornecedor;
import br.gov.pr.guaira.animalys.repository.Cidades;
import br.gov.pr.guaira.animalys.repository.Fornecedores;
import br.gov.pr.guaira.animalys.service.FornecedorService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroFornecedorBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public CadastroFornecedorBean() {
		this.fornecedor = new Fornecedor();
		this.contato = new Contato();
		this.endereco = new Endereco();
	}
	
	public void inicializar() {
		if (idFornecedor != null) {
			try {
				this.fornecedor = fornecedores.porId(idFornecedor);
				this.contato = this.fornecedor.getContato();
				this.endereco = this.fornecedor.getEndereco();
			} catch (Exception e) {
				FacesUtil.addErrorMessage("Fornecedor não encontrado.");
				limpar();
			}
		}
	}

	private Fornecedor fornecedor;
	private Integer idFornecedor; // ID do fornecedor para edição
	private Endereco endereco;
	private Contato contato;
	@Inject
	private FornecedorService fornecedorService;
	@Inject
	private Fornecedores fornecedores;
	@Inject
	private Cidades cidades;
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public Integer getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Integer idFornecedor) {
		this.idFornecedor = idFornecedor;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public void salvar() {
		this.fornecedor.setContato(this.contato);
		this.fornecedor.setEndereco(this.endereco);
		this.fornecedorService.salvar(this.fornecedor);
		if (idFornecedor == null) {
			// Só limpa se for um novo cadastro
			FacesUtil.addInfoMessage("Fornecedor cadastrado com sucesso!");
			limpar();
		} else {
			// Se é edição, apenas mostra mensagem
			FacesUtil.addInfoMessage("Fornecedor atualizado com sucesso!");
		}
	}
	
	private void limpar() {
		this.fornecedor = new Fornecedor();
		this.contato = new Contato();
		this.endereco = new Endereco();
		this.idFornecedor = null;
	}
	
	public boolean isEditando() {
		return this.fornecedor.getIdFornecedor() != null;
	}
	
	public List<Cidade> completarCidade(String descricao) {
		return this.cidades.porNome(descricao);
	}
}
