package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Cidade;
import br.gov.pr.guaira.animalys.entity.Contato;
import br.gov.pr.guaira.animalys.entity.Endereco;
import br.gov.pr.guaira.animalys.entity.Profissao;
import br.gov.pr.guaira.animalys.entity.Profissional;
import br.gov.pr.guaira.animalys.repository.Cidades;
import br.gov.pr.guaira.animalys.repository.Profissoes;
import br.gov.pr.guaira.animalys.repository.Profissionais;
import br.gov.pr.guaira.animalys.service.ProfissionalService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroProfissionalBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer idProfissional;
	
	public CadastroProfissionalBean() {
		this.profissional = new Profissional();
		this.contato = new Contato();
		this.endereco = new Endereco();
		this.dataNascimentoCalendar = Calendar.getInstance();
	}
	
	public void inicializar() {
		if (idProfissional != null) {
			this.profissional = profissionais.porId(idProfissional);
			if (this.profissional != null) {
				this.contato = this.profissional.getContato();
				this.endereco = this.profissional.getEndereco();
				this.dataNascimento = this.profissional.getDataNascimento().getTime();
				this.cpfFormatado = this.profissional.getCpfFormatado();
			}
		}
	}

	private Profissional profissional;
	private Endereco endereco;
	private Contato contato;
	private Date dataNascimento;
	private Calendar dataNascimentoCalendar;
	private String cpfFormatado;
	@Inject
	private ProfissionalService profissionalService;
	@Inject
	private Profissionais profissionais;
	@Inject
	private Profissoes profissoes;
	@Inject
	private Cidades cidades;
	
	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
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
	
	public List<Profissao> completarProfissao(String descricao) {
		return this.profissoes.porNome(descricao);
	}
	
	public List<Cidade> completarCidade(String descricao) {
		return this.cidades.porNome(descricao);
	}
	
	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public void salvar() {
		this.dataNascimentoCalendar.setTime(this.dataNascimento);
		this.profissional.setDataNascimento(this.dataNascimentoCalendar);
		this.profissional.setContato(this.contato);
		this.profissional.setEndereco(this.endereco);
		
		// Atualiza o CPF a partir do campo formatado
		System.out.println("[DEBUG BEAN] CPF formatado: " + this.cpfFormatado);
		if (this.cpfFormatado != null) {
			this.profissional.setCpf(this.cpfFormatado);
		}
		System.out.println("[DEBUG BEAN] CPF no profissional antes de salvar: " + this.profissional.getCpf());
		
		this.profissional = this.profissionalService.salvar(this.profissional);
		
		if (idProfissional == null) {
			FacesUtil.addInfoMessage("Profissional cadastrado com sucesso!");
			limpar();
		} else {
			FacesUtil.addInfoMessage("Profissional atualizado com sucesso!");
		}
	}
	
	public void limpar() {
		this.profissional = new Profissional();
		this.endereco = new Endereco();
		this.contato = new Contato();
		this.dataNascimento = null;
		this.idProfissional = null;
		this.cpfFormatado = null;
	}
	
	public boolean isEditando() {
		return this.profissional.getIdProfissional() != null;
	}
	
	public Integer getIdProfissional() {
		return idProfissional;
	}
	
	public void setIdProfissional(Integer idProfissional) {
		this.idProfissional = idProfissional;
	}
	
	public String getCpfFormatado() {
		return cpfFormatado;
	}
	
	public void setCpfFormatado(String cpfFormatado) {
		this.cpfFormatado = cpfFormatado;
	}
}
