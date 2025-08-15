package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Grupo;
import br.gov.pr.guaira.animalys.repository.Grupos;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter("grupoConverter")
public class GrupoConverter implements Converter{

	private Grupos grupos;
	
	public GrupoConverter(){
		grupos = CDIServiceLocator.getBean(Grupos.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Grupo retorno = null;
		
		if(value != null){
			int id = Integer.parseInt(value);
			retorno = grupos.porId(id);
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value != null){
			Grupo grupo = (Grupo) value;
			
			return grupo.getIdGrupo() == null ? null : grupo.getIdGrupo().toString();
		}
		return "";
	}

}
