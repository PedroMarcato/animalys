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
		
		if(isEditando()) {
			this.contato = this.fornecedor.getContato();
			this.endereco = this.fornecedor.getEndereco();
			System.out.println("teste");
		}
	}

	private Fornecedor fornecedor;
	private Endereco endereco;
	private Contato contato;
	@Inject
	private FornecedorService fornecedorService;
	@Inject
	private Cidades cidades;
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
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
		FacesUtil.addInfoMessage("Fornecedor cadastrado com sucesso!");
		limpar();
	}
	
	private void limpar() {
		this.fornecedor = new Fornecedor();
		this.contato = new Contato();
		this.endereco = new Endereco();
	}
	
	public boolean isEditando() {
		return this.fornecedor.getIdFornecedor() != null;
	}
	
	public List<Cidade> completarCidade(String descricao) {
		return this.cidades.porNome(descricao);
	}
}
