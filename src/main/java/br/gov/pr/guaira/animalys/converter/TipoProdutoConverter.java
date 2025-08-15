package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.TipoProduto;
import br.gov.pr.guaira.animalys.repository.TiposProdutos;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = TipoProduto.class)
public class TipoProdutoConverter implements Converter{
	
	private TiposProdutos tiposProdutos;

	public TipoProdutoConverter() {
		tiposProdutos = CDIServiceLocator.getBean(TiposProdutos.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		TipoProduto retorno = null;

		if (value != null) {
			int id = Integer.parseInt(value);
			retorno = tiposProdutos.porId(id);
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			TipoProduto tipo = (TipoProduto) value;

			return tipo.getIdTipoProduto() == null ? null : tipo.getIdTipoProduto().toString();
		}
		return "";
	}

}
