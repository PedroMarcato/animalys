package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Permissao;
import br.gov.pr.guaira.animalys.repository.Permissoes;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter("permissaoConverter")
public class PermissaoConverter implements Converter {

	private Permissoes permissoes;

	public PermissaoConverter() {
		permissoes = CDIServiceLocator.getBean(Permissoes.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Permissao retorno = null;

		if (value != null) {
			int id = Integer.parseInt(value);
			retorno = permissoes.porId(id);
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Permissao permissao = (Permissao) value;

			return permissao.getIdPermissao() == null ? null : permissao.getIdPermissao().toString();
		}
		return "";
	}

}
