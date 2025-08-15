package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Profissao;
import br.gov.pr.guaira.animalys.repository.Profissoes;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Profissao.class)
public class ProfissaoConverter implements Converter{
	
	private Profissoes profissoes;

	public ProfissaoConverter() {
		profissoes = CDIServiceLocator.getBean(Profissoes.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Profissao retorno = null;

		if (value != null) {
			int id = Integer.parseInt(value);
			retorno = profissoes.porId(id);
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Profissao animal = (Profissao) value;

			return animal.getIdProfissao() == null ? null : animal.getIdProfissao().toString();
		}
		return "";
	}

}
