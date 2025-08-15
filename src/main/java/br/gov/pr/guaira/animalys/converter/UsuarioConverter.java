package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Usuario;
import br.gov.pr.guaira.animalys.repository.Usuarios;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Usuario.class)
public class UsuarioConverter implements Converter{

	private Usuarios usuarios;
	
	public UsuarioConverter(){
		usuarios = CDIServiceLocator.getBean(Usuarios.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Usuario retorno = null;
		
		if(value != null){
			int id = Integer.parseInt(value);
			retorno = usuarios.porId(id);
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value != null){
			Usuario usuario = (Usuario) value;
			
			return usuario.getIdUsuario() == null ? null : usuario.getIdUsuario().toString();
		}
		return "";
	}

}
