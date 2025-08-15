package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Procedimento;
import br.gov.pr.guaira.animalys.repository.Procedimentos;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Procedimento.class)
public class ProcedimentoConverter implements Converter{
	
	private Procedimentos procedimentos;

	public ProcedimentoConverter() {
		procedimentos = CDIServiceLocator.getBean(Procedimentos.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Procedimento retorno = null;

		if (value != null) {
			int id = Integer.parseInt(value);
			retorno = procedimentos.porId(id);
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Procedimento procedimento = (Procedimento) value;

			return procedimento.getIdProcedimento() == null ? null : procedimento.getIdProcedimento().toString();
		}
		return "";
	}

}
