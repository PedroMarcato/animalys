package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Produto;
import br.gov.pr.guaira.animalys.repository.Produtos;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Produto.class)
public class ProdutoConverter implements Converter{
	
	private Produtos produtos;

	public ProdutoConverter() {
		produtos = CDIServiceLocator.getBean(Produtos.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Produto retorno = null;

		if (value != null) {
			int id = Integer.parseInt(value);
			retorno = produtos.porId(id);
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Produto produto = (Produto) value;

			return produto.getIdProduto() == null ? null : produto.getIdProduto().toString();
		}
		return "";
	}

}
