package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Estado;
import br.gov.pr.guaira.animalys.repository.Estados;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Estado.class)
public class EstadoConverter implements Converter{

private Estados estados;
	
	public EstadoConverter(){
		estados = CDIServiceLocator.getBean(Estados.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Estado retorno = null;
		
		if(value != null){
			int id = Integer.parseInt(value);
			retorno = estados.porId(id);
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value != null){
			Estado estado = (Estado) value;
			
			return estado.getIdEstado() == null ? null : estado.getIdEstado().toString();
		}
		return "";
	}
	
}
