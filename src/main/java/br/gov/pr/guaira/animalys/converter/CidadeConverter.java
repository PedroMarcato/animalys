package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Cidade;
import br.gov.pr.guaira.animalys.repository.Cidades;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Cidade.class)
public class CidadeConverter implements Converter{

private Cidades cidades;
	
	public CidadeConverter(){
		cidades = CDIServiceLocator.getBean(Cidades.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Cidade retorno = null;
		
		try {
		if(value != null){
			int id = Integer.parseInt(value);
			retorno = cidades.porId(id);
		}
		return retorno;
		}catch (NumberFormatException e) {
			System.err.println(e);
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value != null){
			Cidade cidade = (Cidade) value;
			
			return cidade.getId() == null ? null : cidade.getId().toString();
		}
		return "";
	}
}
