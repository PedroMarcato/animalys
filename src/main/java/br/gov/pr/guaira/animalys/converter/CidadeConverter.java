package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Cidade;
import br.gov.pr.guaira.animalys.repository.Cidades;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Cidade.class)
public class CidadeConverter implements Converter{

private Cidades cidades;
	
	public CidadeConverter(){
		cidades = CDIServiceLocator.getBean(Cidades.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		System.out.println("=== CIDADE CONVERTER getAsObject ===");
		System.out.println("Value recebido: " + value);
		Cidade retorno = null;
		
		try {
		if(value != null){
			int id = Integer.parseInt(value);
			System.out.println("ID convertido: " + id);
			retorno = cidades.porId(id);
			System.out.println("Cidade encontrada: " + (retorno != null ? retorno.getNome() + " - " + 
				(retorno.getEstado() != null ? retorno.getEstado().getUf() : "SEM_ESTADO") : "NULL"));
			System.out.println("Converter finalizando - retornando: " + (retorno != null ? "CIDADE_VALIDA" : "NULL"));
		} else {
			System.out.println("Value é null - retornando null");
		}
		return retorno;
		}catch (NumberFormatException e) {
			System.err.println("Erro NumberFormat: " + e);
		} catch (Exception e) {
			System.err.println("Erro geral: " + e);
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value != null){
			Cidade cidade = (Cidade) value;
			
			return cidade.getId() == null ? null : cidade.getId().toString();
		}
		return "";
	}
}
