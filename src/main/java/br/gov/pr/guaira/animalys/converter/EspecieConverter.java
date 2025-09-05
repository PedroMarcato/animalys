package br.gov.pr.guaira.animalys.converter;

//import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Especie;
import br.gov.pr.guaira.animalys.repository.Especies;
//import br.gov.pr.guaira.animalys.util.cdi.CDIServiceLocator;


@Named("especieConverter")
@FacesConverter(value = "especieConverter", forClass = Especie.class)
@RequestScoped
public class EspecieConverter implements Converter {

    @Inject
    private Especies especies;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !"".equals(value.trim())) {
            try {
                return especies.porId(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null && value instanceof Especie) {
            Especie especie = (Especie) value;
            return especie.getIdEspecie() != null ? especie.getIdEspecie().toString() : "";
        }
        return "";
    }
}

