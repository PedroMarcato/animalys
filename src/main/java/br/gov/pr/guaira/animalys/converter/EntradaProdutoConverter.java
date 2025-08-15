package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.EntradaProduto;
import br.gov.pr.guaira.animalys.repository.EntradasProdutos;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = EntradaProduto.class)
public class EntradaProdutoConverter implements Converter{
	
	private EntradasProdutos entradas;

	public EntradaProdutoConverter() {
		entradas = CDIServiceLocator.getBean(EntradasProdutos.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		EntradaProduto retorno = null;

		if (value != null) {
			int id = Integer.parseInt(value);
			retorno = entradas.porId(id);
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			EntradaProduto entradaProduto = (EntradaProduto) value;

			return entradaProduto.getIdEntradaProduto() == null ? null : entradaProduto.getIdEntradaProduto().toString();
		}
		return "";
	}

}
