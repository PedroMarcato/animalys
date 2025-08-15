package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Profissional;
import br.gov.pr.guaira.animalys.repository.Profissionais;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Profissional.class)
public class ProssionalConverter implements Converter{
	
	private Profissionais profissionais;

	public ProssionalConverter() {
		profissionais = CDIServiceLocator.getBean(Profissionais.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Profissional retorno = null;

		if (value != null) {
			int id = Integer.parseInt(value);
			retorno = profissionais.porId(id);
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Profissional profissional = (Profissional) value;

			return profissional.getIdProfissional() == null ? null : profissional.getIdProfissional().toString();
		}
		return "";
	}

}
