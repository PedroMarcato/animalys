package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Lote;
import br.gov.pr.guaira.animalys.repository.Lotes;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Lote.class)
public class LoteConverter implements Converter{
	
	private Lotes lotes;

	public LoteConverter() {
		lotes = CDIServiceLocator.getBean(Lotes.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Lote retorno = null;

		if (value != null) {
			Integer id = Integer.parseInt(value);
			retorno = lotes.porId(id);
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Lote lote = (Lote) value;

			return lote.getIdLote() == null ? null : lote.getIdLote().toString();
		}
		return "";
	}

}
