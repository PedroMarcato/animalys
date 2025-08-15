package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.repository.Animais;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;

public class AnimalService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private Animais animais;

	@Transactional
	public Animal salvar(Animal animal) {
		return animais.guardar(animal);
	}
}
