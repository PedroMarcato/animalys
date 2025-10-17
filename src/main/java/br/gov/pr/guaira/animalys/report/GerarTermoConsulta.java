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

import br.gov.pr.guaira.animalys.model.TermoConsulta;
import br.gov.pr.guaira.animalys.relatorios.dto.TermoConsultaDTO;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class GerarTermoConsulta {

    public void gerar(TermoConsulta termo) throws Exception {
        // criar DTO
        TermoConsultaDTO dto = criarDTO(termo);

        List<TermoConsultaDTO> lista = new ArrayList<>();
        lista.add(dto);

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lista);

        // compilar JRXML
        InputStream jrxml = this.getClass().getResourceAsStream("/arquivos/relatorios/termoConsulta.jrxml");
        JasperReport report = JasperCompileManager.compileReport(jrxml);

        Map<String, Object> parametros = new HashMap<>();
        String reportDir = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/arquivos/relatorios/");
        parametros.put("REPORT_DIRECTORY", reportDir + "/");

        JasperPrint print = JasperFillManager.fillReport(report, parametros, ds);

        // escrever PDF na resposta HTTP
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletResponse response = (HttpServletResponse) ec.getResponse();
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename=termoConsulta.pdf");

        ServletOutputStream output = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(print, output);
        output.flush();
        output.close();

        FacesContext.getCurrentInstance().responseComplete();
    }

    private TermoConsultaDTO criarDTO(TermoConsulta termo) {
        TermoConsultaDTO dto = new TermoConsultaDTO();

        if (termo.getProprietario() != null) {
            dto.setNomeProprietario(termo.getProprietario().getNome());
            dto.setRgProprietario(termo.getProprietario().getRg());
            dto.setCpfProprietario(termo.getProprietario().getCpf());
            dto.setEnderecoProprietario(termo.getProprietario().getEndereco() != null ? termo.getProprietario().getEndereco().toString() : "");
            dto.setBairroProprietario(termo.getProprietario().getEndereco() != null ? termo.getProprietario().getEndereco().getBairro() : "");
            dto.setCepProprietario(termo.getProprietario().getEndereco() != null ? termo.getProprietario().getEndereco().getCep() : "");
            dto.setCidadeProprietario(termo.getProprietario().getEndereco() != null && termo.getProprietario().getEndereco().getCidade() != null ? termo.getProprietario().getEndereco().getCidade().getNome() : "");
            dto.setContatoProprietario(termo.getProprietario().getContato() != null ? termo.getProprietario().getContato().getEmail() : "");
            dto.setCelularProprietario(termo.getProprietario().getContato() != null ? termo.getProprietario().getContato().getCelular() : "");
        }

        if (termo.getAnimal() != null) {
            // Alguns dados já são copiados para o próprio TermoConsulta no service
            dto.setNomeAnimal(termo.getNomeAnimal());
            dto.setFichaControle(termo.getFichaControle());
            dto.setCor(termo.getCorAnimal() != null ? termo.getCorAnimal() : (termo.getAnimal().getCor()));
            // espécie: primeiro usa campo em TermoConsulta, senão raca->especie
            if (termo.getEspecieAnimal() != null && !termo.getEspecieAnimal().isEmpty()) {
                dto.setEspecie(termo.getEspecieAnimal());
            } else if (termo.getAnimal().getRaca() != null && termo.getAnimal().getRaca().getEspecie() != null) {
                dto.setEspecie(termo.getAnimal().getRaca().getEspecie().getNome());
            } else {
                dto.setEspecie("Não informado");
            }
            dto.setSexo(termo.getSexoAnimal() != null ? termo.getSexoAnimal() : (termo.getAnimal().getSexo() != null ? termo.getAnimal().getSexo().toString() : null));
            dto.setIdade(termo.getAnimal().getIdade());
        }

    // TermoConsulta provides getDataAssinaturaAsDate() helper that returns java.util.Date
    dto.setDataAssinaturaAsDate(termo.getDataAssinaturaAsDate());

        return dto;
    }
}
