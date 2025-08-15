package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Solicitacao;
import br.gov.pr.guaira.animalys.repository.Solicitacoes;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Solicitacao.class)
public class SolicitacaoConverter implements Converter{
	
	private Solicitacoes solicitacoes;

	public SolicitacaoConverter() {
		solicitacoes = CDIServiceLocator.getBean(Solicitacoes.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Solicitacao retorno = null;

		if (value != null) {
			int id = Integer.parseInt(value);
			retorno = solicitacoes.porId(id);
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Solicitacao solicitacao = (Solicitacao) value;

			return solicitacao.getIdSolicitacao() == null ? null : solicitacao.getIdSolicitacao().toString();
		}
		return "";
	}

}
