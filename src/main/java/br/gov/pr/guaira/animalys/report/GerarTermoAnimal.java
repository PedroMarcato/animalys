package br.gov.pr.guaira.animalys.report;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.ModalidadeSolicitante;
import br.gov.pr.guaira.animalys.relatorios.dto.TermoAnimalDTO;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class GerarTermoAnimal {

    public void gerar(Animal animal) throws Exception {
        // Identificar qual template usar baseado na modalidade do proprietário
        String nomeTemplate = identificarTemplate(animal);
        
        // Criar DTO
        TermoAnimalDTO dto = criarDTO(animal);

        List<TermoAnimalDTO> lista = new ArrayList<>();
        lista.add(dto);

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lista);

        // Compilar JRXML
        InputStream jrxml = this.getClass().getResourceAsStream("/arquivos/relatorios/" + nomeTemplate);
        JasperReport report = JasperCompileManager.compileReport(jrxml);

        Map<String, Object> parametros = new HashMap<>();
        String reportDir = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/arquivos/relatorios/");
        parametros.put("REPORT_DIRECTORY", reportDir + "/");

        JasperPrint print = JasperFillManager.fillReport(report, parametros, ds);

        // Escrever PDF na resposta HTTP
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletResponse response = (HttpServletResponse) ec.getResponse();
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename=termo_" + nomeTemplate.replace(".jrxml", ".pdf"));

        ServletOutputStream output = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(print, output);
        output.flush();
        output.close();

        FacesContext.getCurrentInstance().responseComplete();
    }

    /**
     * Identifica qual template usar baseado na modalidade do proprietário
     */
    private String identificarTemplate(Animal animal) {
        if (animal.getProprietario() == null || animal.getProprietario().getModalidade() == null) {
            // Default: protetor individual
            return "animalIndividual.jrxml";
        }

        ModalidadeSolicitante modalidade = animal.getProprietario().getModalidade();
        
        switch (modalidade) {
            case CAO_COMUNITARIO:
                return "animalComunitario.jrxml";
            case TUTOR_VULNERABILIDADE_SOCIAL:
                return "animalVulneravel.jrxml";
            case PROTETOR_INDIVIUAL_ANIMAIS:
                return "animalIndividual.jrxml";
            case PESSOA_FISICA:
            default:
                return "animalIndividual.jrxml";
        }
    }

    private TermoAnimalDTO criarDTO(Animal animal) {
        TermoAnimalDTO dto = new TermoAnimalDTO();

        // Dados do Proprietário
        if (animal.getProprietario() != null) {
            dto.setNomeProprietario(animal.getProprietario().getNome());
            dto.setRgProprietario(animal.getProprietario().getRg());
            dto.setCpfProprietario(animal.getProprietario().getCpf());
            
            if (animal.getProprietario().getEndereco() != null) {
                dto.setEnderecoProprietario(animal.getProprietario().getEndereco().toString());
                dto.setBairroProprietario(animal.getProprietario().getEndereco().getBairro());
                dto.setCepProprietario(animal.getProprietario().getEndereco().getCep());
                
                if (animal.getProprietario().getEndereco().getCidade() != null) {
                    dto.setCidadeProprietario(animal.getProprietario().getEndereco().getCidade().getNome());
                }
            }
            
            if (animal.getProprietario().getContato() != null) {
                dto.setContatoProprietario(animal.getProprietario().getContato().getTelefone());
                dto.setCelularProprietario(animal.getProprietario().getContato().getCelular());
            }
        }

        // Dados do Animal
        dto.setNomeAnimal(animal.getNome());
        dto.setFichaControle(animal.getIdAnimal() != null ? animal.getIdAnimal().toString() : "");
        dto.setCor(animal.getCor());
        
        if (animal.getEspecie() != null) {
            dto.setEspecie(animal.getEspecie().getNome());
        }
        
        if (animal.getSexo() != null) {
            dto.setSexo(animal.getSexo().getDescricao());
        }
        
        dto.setIdade(animal.getIdade());
        
        // Porte do animal (pode ser derivado da raça ou outro campo)
        if (animal.getRaca() != null) {
            dto.setPorteAnimal(animal.getRaca().getNome());
        }
        
        // Status de castração
        if (animal.getStatus() != null) {
            dto.setCastradoStr(animal.getStatus().name().equals("CASTRADO") ? "Sim" : "Não");
        }
        
        // Data de assinatura (pode usar data de entrada ou data atual)
        if (animal.getDataEntrada() != null) {
            dto.setDataAssinaturaAsDate(animal.getDataEntrada().getTime());
        } else {
            dto.setDataAssinaturaAsDate(new java.util.Date());
        }

        return dto;
    }
}
