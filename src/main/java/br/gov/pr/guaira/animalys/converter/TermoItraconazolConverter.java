package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.TermoItraconazol;
import br.gov.pr.guaira.animalys.repository.TermosItraconazol;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(value = "termoItraconazolConverter", forClass = TermoItraconazol.class)
public class TermoItraconazolConverter implements Converter {
	
	private TermosItraconazol termosRepository;

	public TermoItraconazolConverter() {
		termosRepository = CDIServiceLocator.getBean(TermosItraconazol.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		TermoItraconazol retorno = null;

		if (value != null && !value.trim().isEmpty() && !value.equals("null")) {
			try {
				int id = Integer.parseInt(value);
				retorno = termosRepository.porId(id);
			} catch (NumberFormatException e) {
				return null;
			} catch (Exception e) {
				return null;
			}
		}

		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			TermoItraconazol termo = (TermoItraconazol) value;
			if (termo.getIdTermoItraconazol() != null) {
				return termo.getIdTermoItraconazol().toString();
			}
		}
		return "";
	}
}