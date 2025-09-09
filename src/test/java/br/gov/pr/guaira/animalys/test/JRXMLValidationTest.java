package br.gov.pr.guaira.animalys.test;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import java.io.InputStream;

public class JRXMLValidationTest {
    public static void main(String[] args) {
        try {
            // Carregar o modelo de relatório (.jrxml)
            InputStream reportStream = JRXMLValidationTest.class.getResourceAsStream("/reports/historico_animal.jrxml");
            if (reportStream == null) {
                System.out.println("ERRO: Template de relatório não encontrado");
                return;
            }
            
            // Compilar o relatório .jrxml para .jasper
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            
            System.out.println("SUCESSO: JRXML compilado com sucesso!");
            System.out.println("Nome do relatório: " + jasperReport.getName());
            
        } catch (Exception e) {
            System.out.println("ERRO ao compilar JRXML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
