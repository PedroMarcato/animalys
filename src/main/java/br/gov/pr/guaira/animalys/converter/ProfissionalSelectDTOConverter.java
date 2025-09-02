package br.gov.pr.guaira.animalys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pr.guaira.animalys.dto.ProfissionalSelectDTO;

@FacesConverter(forClass = ProfissionalSelectDTO.class)
public class ProfissionalSelectDTOConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        try {
            String[] parts = value.split(":");
            if (parts.length == 2) {
                Integer id = Integer.valueOf(parts[0]);
                String nome = parts[1];
                return new ProfissionalSelectDTO(id, nome);
            }
        } catch (NumberFormatException e) {
            // Log error if needed
        }
        
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        
        if (value instanceof ProfissionalSelectDTO) {
            ProfissionalSelectDTO dto = (ProfissionalSelectDTO) value;
            return dto.getIdProfissional() + ":" + dto.getNome();
        }
        
        return "";
    }
}
