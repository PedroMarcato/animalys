package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.TipoAcerto;
import br.gov.pr.guaira.animalys.repository.TipoAcertos;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = TipoAcerto.class)
public class TipoAcertoConverter implements Converter {

	private TipoAcertos tiposProdutos;

	public TipoAcertoConverter() {
		tiposProdutos = CDIServiceLocator.getBean(TipoAcertos.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		TipoAcerto retorno = null;

		if (value != null) {
			int id = Integer.parseInt(value);
			retorno = tiposProdutos.porId(id);
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			TipoAcerto tipo = (TipoAcerto) value;

			return tipo.getIdTipoAcerto() == null ? null : tipo.getIdTipoAcerto().toString();
		}
		return "";
	}

}
