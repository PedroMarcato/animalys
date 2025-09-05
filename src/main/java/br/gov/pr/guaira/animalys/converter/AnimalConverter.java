package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.repository.Animais;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(value = "animalConverter", forClass = Animal.class)
public class AnimalConverter implements Converter{
	
	private Animais animais;

	public AnimalConverter() {
		animais = CDIServiceLocator.getBean(Animais.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Animal retorno = null;

		if (value != null && !value.trim().isEmpty()) {
			try {
				int id = Integer.parseInt(value);
				retorno = animais.porId(id);
			} catch (NumberFormatException e) {
				// Retorna null se não conseguir converter
				return null;
			}
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Animal animal = (Animal) value;

			return animal.getIdAnimal() == null ? null : animal.getIdAnimal().toString();
		}
		return "";
	}

}
