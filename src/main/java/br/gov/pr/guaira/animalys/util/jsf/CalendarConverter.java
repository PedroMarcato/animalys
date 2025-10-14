package br.gov.pr.guaira.animalys.util.jsf;

import java.util.Calendar;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("calendarConverter")
public class CalendarConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        // O PrimeFaces já converte para Date, então isso não será chamado normalmente
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        
        if (value instanceof Calendar) {
            Calendar calendar = (Calendar) value;
            // Retorna formatado para o PrimeFaces exibir
            return String.format("%1$td/%1$tm/%1$tY", calendar);
        }
        
        return value.toString();
    }
}
