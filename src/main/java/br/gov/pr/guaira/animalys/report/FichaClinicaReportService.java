package br.gov.pr.guaira.animalys.report;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JasperExportManager;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FichaClinicaReportService implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String REPORT_TEMPLATE = "/reports/historico_animal.jrxml";

    @PostConstruct
    public void init() {
        // Initialize any resources if needed
    }

    public void gerarRelatorio(List<?> dadosAnimal) {
        try {
            // Carregar o modelo de relatório (.jrxml)
            InputStream reportStream = getClass().getResourceAsStream(REPORT_TEMPLATE);
            if (reportStream == null) {
                throw new RuntimeException("Template de relatório não encontrado: " + REPORT_TEMPLATE);
            }
            
            // Compilar o relatório .jrxml para .jasper
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Criar uma fonte de dados para o relatório
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dadosAnimal);

            // Parâmetros do relatório
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_DIRECTORY", getClass().getResource("/reports/").getPath());

            // Preencher o relatório
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Exportar o relatório para PDF
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"ficha_clinica.pdf\"");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
            // Tratar exceções adequadamente
        }
    }
    
    public void gerarRelatorioServlet(List<?> dadosAnimal, HttpServletResponse response) {
        try {
            // Configurar o compilador para JasperReports 7.x
            System.setProperty("net.sf.jasperreports.compiler.class", "net.sf.jasperreports.compilers.JRGroovyCompiler");
            
            // Carregar o modelo de relatório (.jrxml)
            InputStream reportStream = getClass().getResourceAsStream(REPORT_TEMPLATE);
            if (reportStream == null) {
                throw new RuntimeException("Template de relatório não encontrado: " + REPORT_TEMPLATE);
            }
            
            // Compilar o relatório .jrxml para .jasper
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Criar uma fonte de dados para o relatório
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dadosAnimal);

            // Parâmetros do relatório
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_DIRECTORY", getClass().getResource("/reports/").getPath());

            // Preencher o relatório
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Exportar o relatório para PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"ficha_clinica.pdf\"");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
            // Tratar exceções adequadamente
        }
    }
}