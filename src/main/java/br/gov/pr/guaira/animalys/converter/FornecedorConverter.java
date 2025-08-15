package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Fornecedor;
import br.gov.pr.guaira.animalys.repository.Fornecedores;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Fornecedor.class)
public class FornecedorConverter implements Converter{
	
	private Fornecedores fornecedores;

	public FornecedorConverter() {
		fornecedores = CDIServiceLocator.getBean(Fornecedores.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Fornecedor retorno = null;

		if (value != null) {
			int id = Integer.parseInt(value);
			retorno = fornecedores.porId(id);
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Fornecedor especie = (Fornecedor) value;

			return especie.getIdFornecedor() == null ? null : especie.getIdFornecedor().toString();
		}
		return "";
	}

}
