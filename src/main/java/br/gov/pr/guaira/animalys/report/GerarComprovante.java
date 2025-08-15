package br.gov.pr.guaira.animalys.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import br.gov.pr.guaira.animalys.entity.Solicitacao;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class GerarComprovante implements Serializable{

	private static final long serialVersionUID = 1L;

	public void gerar(Solicitacao solicitacao) {
	
		try {  
			
			List<Solicitacao> lista = new ArrayList<Solicitacao>();
			lista.add(solicitacao);
            System.out.println("entrou no visualizar relatorio"); 
            
            
            
            
            HashMap<String, Object> parameters = new HashMap<String, Object>();
            
            String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/arquivos/relatorios/comprovanteSolicitacao.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(caminho);
            String logo = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/arquivos/relatorios/logo.png");
            parameters.put("logo", logo);
			
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(lista));  
            byte[] b = JasperExportManager.exportReportToPdf(jasperPrint);  
            

//            JasperReport jasperReport = JasperCompileManager.compileReport("C:\\animalys\\relatorios\\comprovanteSolicitacao.jrxml");  
//          
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JRBeanCollectionDataSource(lista));  
//            byte[] b = JasperExportManager.exportReportToPdf(jasperPrint);   
  
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();  
            res.setContentType("application/pdf");  
           
            res.setHeader("Content-disposition", "inline;filename=arquivo.pdf");  
          
            res.getOutputStream().write(b);  
            res.getCharacterEncoding();  
              
            FacesContext.getCurrentInstance().responseComplete();  
                
            System.out.println("saiu do visualizar relatorio");  
            
           
            
            
            
            
            
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
	}
}
