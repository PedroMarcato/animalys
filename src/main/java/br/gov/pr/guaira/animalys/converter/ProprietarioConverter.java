package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Proprietario;
import br.gov.pr.guaira.animalys.repository.Proprietarios;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(value = "proprietarioConverter", forClass = Proprietario.class)
public class ProprietarioConverter implements Converter{
	
	private Proprietarios proprietarios;

	public ProprietarioConverter() {
		proprietarios = CDIServiceLocator.getBean(Proprietarios.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Proprietario retorno = null;

		if (value != null && !value.trim().isEmpty()) {
			try {
				int id = Integer.parseInt(value);
				retorno = proprietarios.porId(id);
			} catch (NumberFormatException e) {
				// Retorna null se não conseguir converter
				return null;
			}
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Proprietario proprietario = (Proprietario) value;

			return proprietario.getIdProprietario() == null ? null : proprietario.getIdProprietario().toString();
		}
		return "";
	}

}
