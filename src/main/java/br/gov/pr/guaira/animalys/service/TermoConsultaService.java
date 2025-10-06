package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.model.TermoConsulta;
import br.gov.pr.guaira.animalys.repository.TermosConsulta;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class TermoConsultaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TermosConsulta termosConsulta;

	@Transactional
	public TermoConsulta salvar(TermoConsulta termo) {
		preencherDadosDoTermo(termo);
		return termosConsulta.guardar(termo);
	}

	@Transactional
	public void excluir(TermoConsulta termo) {
		termosConsulta.remover(termo);
	}

	public List<TermoConsulta> buscarPorProprietario(Integer proprietarioId) {
		return termosConsulta.porProprietario(proprietarioId);
	}

	public List<TermoConsulta> buscarPorAnimal(Integer animalId) {
		return termosConsulta.porAnimal(animalId);
	}

	public TermoConsulta buscarPorProprietarioEAnimal(Integer proprietarioId, Integer animalId) {
		return termosConsulta.porProprietarioEAnimal(proprietarioId, animalId);
	}

	public boolean existeTermoPara(Integer proprietarioId, Integer animalId) {
		return termosConsulta.existeTermoPara(proprietarioId, animalId);
	}

	public List<TermoConsulta> listarTodos() {
		return termosConsulta.todos();
	}

	public TermoConsulta buscarPorId(Integer id) {
		return termosConsulta.porId(id.longValue());
	}

	public List<TermoConsulta> filtrarTermos(String cpf, String nomeAnimal) {
		return termosConsulta.filtrarPorCpfEAnimal(cpf, nomeAnimal);
	}

	public TermoConsulta preparar(Proprietario proprietario, Animal animal) {
		TermoConsulta termo = new TermoConsulta();
		termo.setProprietario(proprietario);
		termo.setAnimal(animal);
		preencherDadosDoTermo(termo);
		return termo;
	}

	private void preencherDadosDoTermo(TermoConsulta termo) {
		if (termo.getProprietario() != null) {
			Proprietario prop = termo.getProprietario();
			termo.setNomeProprietario(prop.getNome());
			termo.setRgProprietario(prop.getRg());
			termo.setCpfProprietario(prop.getCpf());
			
			// Monta o endereço completo
			StringBuilder endereco = new StringBuilder();
			if (prop.getEndereco() != null) {
				endereco.append(prop.getEndereco().getLogradouro() != null ? prop.getEndereco().getLogradouro() : "");
				endereco.append(", ");
				endereco.append(prop.getEndereco().getNumero() != null ? prop.getEndereco().getNumero() : "");
				if (prop.getEndereco().getComplemento() != null && !prop.getEndereco().getComplemento().trim().isEmpty()) {
					endereco.append(", ").append(prop.getEndereco().getComplemento());
				}
				endereco.append(" - ");
				endereco.append(prop.getEndereco().getBairro() != null ? prop.getEndereco().getBairro() : "");
				endereco.append(", Guaíra-PR");
			}
			termo.setEnderecoProprietario(endereco.toString());
		}

		if (termo.getAnimal() != null) {
			Animal animal = termo.getAnimal();
			termo.setNomeAnimal(animal.getNome());
			termo.setCorAnimal(animal.getCor());
			termo.setFichaControle(animal.getIdAnimal() != null ? animal.getIdAnimal().toString() : "");
			
			if (animal.getRaca() != null && animal.getRaca().getEspecie() != null) {
				termo.setEspecieAnimal(animal.getRaca().getEspecie().getNome());
			}
			
			if (animal.getSexo() != null) {
				termo.setSexoAnimal(animal.getSexo().getDescricao());
			}
		}
	}
}