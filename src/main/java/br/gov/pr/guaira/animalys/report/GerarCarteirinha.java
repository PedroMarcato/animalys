
package br.gov.pr.guaira.animalys.report;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.awt.image.BufferedImage;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.InputStream;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import br.gov.pr.guaira.animalys.entity.Animal;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class GerarCarteirinha implements Serializable{

	private static final long serialVersionUID = 1L;

	public void gerar(Animal solicitacao) {
	
		try {  
			
            System.out.println("[DEBUG] Entrou no método gerar do GerarCarteirinha");
            List<Animal> lista = new ArrayList<>();
            lista.add(solicitacao);
            System.out.println("[DEBUG] Animal recebido: " + solicitacao);
            System.out.println("[DEBUG] Lista de animais para o relatório: " + lista);


            HashMap<String, Object> parameters = new HashMap<>();
            // Geração do QRCode usando ZXing
            try {
                String conteudoQRCode = solicitacao.getNumeroMicrochip(); // ou qualquer informação desejada
                if (conteudoQRCode != null && !conteudoQRCode.isEmpty()) {
                    QRCodeWriter qrCodeWriter = new QRCodeWriter();
                    BitMatrix bitMatrix = qrCodeWriter.encode(conteudoQRCode, BarcodeFormat.QR_CODE, 120, 120);
                    BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                    parameters.put("qrCodeImage", qrImage);
                    System.out.println("[DEBUG] QRCode gerado e adicionado aos parâmetros.");
                }
            } catch (Exception e) {
                System.out.println("[ERRO] Falha ao gerar QRCode: " + e.getMessage());
                e.printStackTrace();
            }
            System.out.println("[DEBUG] Parâmetros do relatório: " + parameters);

            // Usar classpath para garantir compatibilidade com JasperReports 7.x
            InputStream reportStream = getClass().getResourceAsStream("/arquivos/relatorios/carteirinhaNova.jrxml");
            System.out.println("[DEBUG] Tentando carregar o arquivo do relatório do classpath: /arquivos/relatorios/carteirinhaNova.jrxml");
            System.out.println("[DEBUG] reportStream é nulo? " + (reportStream == null));
            if (reportStream == null) {
                throw new RuntimeException("Arquivo do relatório não encontrado no classpath: /arquivos/relatorios/carteirinhaNova.jrxml");
            }
            JasperReport jasperReport = null;
            try {
                jasperReport = JasperCompileManager.compileReport(reportStream);
                System.out.println("[DEBUG] Compilação do relatório .jrxml realizada com sucesso.");
            } catch (Exception e) {
                System.out.println("[ERRO] Falha ao compilar o relatório .jrxml: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
            // Se precisar de logo:
            // InputStream logoStream = getClass().getResourceAsStream("/arquivos/relatorios/logo.png");
            // parameters.put("logo", logoStream);

            JasperPrint jasperPrint = null;
            try {
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(lista));
                System.out.println("[DEBUG] Preenchimento do relatório realizado com sucesso.");
            } catch (Exception e) {
                System.out.println("[ERRO] Falha ao preencher o relatório: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
            byte[] b = null;
            try {
                b = JasperExportManager.exportReportToPdf(jasperPrint);
                System.out.println("[DEBUG] Exportação do relatório para PDF realizada com sucesso.");
            } catch (Exception e) {
                System.out.println("[ERRO] Falha ao exportar o relatório para PDF: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
            
            
            
//            JasperReport jasperReport = JasperCompileManager.compileReport("C:\\animalys\\relatorios\\carteirinhaNova.jrxml");              
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JRBeanCollectionDataSource(lista));  
//            byte[] b = JasperExportManager.exportReportToPdf(jasperPrint);   
  
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();  
            res.setContentType("application/pdf");  
            //C�digo abaixo gerar o relat�rio e disponibiliza diretamente na p�gina   
            res.setHeader("Content-disposition", "inline;filename=arquivo.pdf");  
            //C�digo abaixo gerar o relat�rio e disponibiliza para o cliente baixar ou salvar   
            //res.setHeader("Content-disposition", "attachment;filename=arquivo.pdf");  
            res.getOutputStream().write(b);  
            res.getCharacterEncoding();  
              
            FacesContext.getCurrentInstance().responseComplete();  
                
            System.out.println("saiu do visualizar relatorio");  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
	}
}
