package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Marca;
import br.gov.pr.guaira.animalys.repository.Marcas;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Marca.class)
public class MarcaConverter implements Converter{
	
	private Marcas marcas;

	public MarcaConverter() {
		marcas = CDIServiceLocator.getBean(Marcas.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Marca retorno = null;

		if (value != null) {
			int id = Integer.parseInt(value);
			retorno = marcas.porId(id);
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Marca marca = (Marca) value;

			return marca.getIdMarca() == null ? null : marca.getIdMarca().toString();
		}
		return "";
	}

}
