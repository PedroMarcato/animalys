package br.gov.pr.guaira.animalys.util;

import javax.faces.context.FacesContext;

public class FacesUtil {

    public static Integer getParameterAsInteger(String name) {
        String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
        if (value == null) return null;
        return Integer.valueOf(value);
    }

    public static Long getParameterAsLong(String name) {
        String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
        if (value == null) return null;
        return Long.valueOf(value);
    }

    public static String getParameter(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }
}
