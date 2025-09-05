package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.Tratamento;
import br.gov.pr.guaira.animalys.repository.Tratamentos;
import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;

@FacesConverter(value = "tratamentoConverter", forClass = Tratamento.class)
public class TratamentoConverter implements Converter {

    private Tratamentos tratamentos;
    
    public TratamentoConverter() {
        tratamentos = CDIServiceLocator.getBean(Tratamentos.class);
    }
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Tratamento retorno = null;
        
        try {
            if (value != null && !value.trim().isEmpty()) {
                int id = Integer.parseInt(value);
                retorno = tratamentos.porId(id);
            }
            return retorno;
        } catch (NumberFormatException e) {
            System.err.println("Erro na conversão do Tratamento: " + e);
        }
        return retorno;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            Tratamento tratamento = (Tratamento) value;
            return tratamento.getIdTratamento() == null ? null : tratamento.getIdTratamento().toString();
        }
        return "";
    }
}
