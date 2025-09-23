package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.entity.ModalidadeSolicitante;

@FacesConverter(value = "modalidadeSolicitanteConverter", forClass = ModalidadeSolicitante.class)
public class ModalidadeSolicitanteConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        for (ModalidadeSolicitante modalidade : ModalidadeSolicitante.values()) {
            if (modalidade.name().equals(value)) {
                return modalidade;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof ModalidadeSolicitante) {
            return ((ModalidadeSolicitante) value).name();
        }
        return value.toString();
    }
}
