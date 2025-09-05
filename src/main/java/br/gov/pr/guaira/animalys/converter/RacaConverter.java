package br.gov.pr.guaira.animalys.converter;

//import javax.enterprise.context.ApplicationScoped;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Raca;
import br.gov.pr.guaira.animalys.repository.Racas;
//import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;


@Named("racaConverter")
@FacesConverter(value = "racaConverter", forClass = Raca.class)
@RequestScoped
public class RacaConverter implements Converter {

    @Inject
    private Racas racas;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !"".equals(value.trim())) {
            try {
                return racas.porId(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null && value instanceof Raca) {
            Raca raca = (Raca) value;
            return raca.getIdRaca() != null ? raca.getIdRaca().toString() : "";
        }
        return "";
    }
}

