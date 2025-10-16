package br.gov.pr.guaira.animalys.report;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import br.gov.pr.guaira.animalys.entity.TermoItraconazol;
import br.gov.pr.guaira.animalys.model.TermoItraconazolDTO;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class GerarTermoItraconazol implements Serializable {

	private static final long serialVersionUID = 1L;

	public void gerar(TermoItraconazol termo) {
		try {
			// Criar DTO para o relatório
			TermoItraconazolDTO dto = criarDTO(termo);
			
			List<TermoItraconazolDTO> lista = new ArrayList<>();
			lista.add(dto);
			
			System.out.println("Gerando relatório do termo de itraconazol");
			
			HashMap<String, Object> parameters = new HashMap<>();
			
			// Caminho do relatório
			String caminho = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/arquivos/relatorios/termoItraconazol.jrxml");
			
			// Caminho da pasta de recursos (para imagens)
			String reportDirectory = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/arquivos/relatorios/");
			parameters.put("REPORT_DIRECTORY", reportDirectory);
			
			JasperReport jasperReport = JasperCompileManager.compileReport(caminho);
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					new JRBeanCollectionDataSource(lista));
			byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
			
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
					.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline;filename=termo_itraconazol.pdf");
			
			response.getOutputStream().write(pdfBytes);
			response.getCharacterEncoding();
			
			FacesContext.getCurrentInstance().responseComplete();
			
			System.out.println("Relatório do termo de itraconazol gerado com sucesso");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Erro ao gerar relatório do termo de itraconazol: " + ex.getMessage());
		}
	}
	
	private TermoItraconazolDTO criarDTO(TermoItraconazol termo) {
		TermoItraconazolDTO dto = new TermoItraconazolDTO();
		
		// Dados do proprietário
		if (termo.getAnimal() != null && termo.getAnimal().getProprietario() != null) {
			dto.setNomeProprietario(termo.getAnimal().getProprietario().getNome());
			dto.setCpfProprietario(termo.getAnimal().getProprietario().getCpf());
			dto.setRgProprietario(termo.getAnimal().getProprietario().getRg());
			
			if (termo.getAnimal().getProprietario().getEndereco() != null) {
				dto.setEnderecoProprietario(termo.getAnimal().getProprietario().getEndereco().getLogradouro());
				dto.setNumeroEndereco(termo.getAnimal().getProprietario().getEndereco().getNumero());
				dto.setBairroProprietario(termo.getAnimal().getProprietario().getEndereco().getBairro());
				dto.setCepProprietario(termo.getAnimal().getProprietario().getEndereco().getCep());
				
				// Cidade do proprietário
				if (termo.getAnimal().getProprietario().getEndereco().getCidade() != null) {
					dto.setCidadeProprietario(termo.getAnimal().getProprietario().getEndereco().getCidade().getNome());
				}
			}
			
			if (termo.getAnimal().getProprietario().getContato() != null) {
				dto.setTelefoneProprietario(termo.getAnimal().getProprietario().getContato().getTelefone());
				dto.setCelularProprietario(termo.getAnimal().getProprietario().getContato().getCelular());
				// Fallback para contatoProprietario (campo usado no relatório original)
				String contato = termo.getAnimal().getProprietario().getContato().getTelefone();
				if (contato == null || contato.isEmpty()) {
					contato = termo.getAnimal().getProprietario().getContato().getCelular();
				}
				dto.setContatoProprietario(contato);
			}
		}
		
		// Dados do animal
		if (termo.getAnimal() != null) {
			dto.setNomeAnimal(termo.getAnimal().getNome());
			dto.setCor(termo.getAnimal().getCor());
			
			// Converter Sexo para String
			if (termo.getAnimal().getSexo() != null) {
				dto.setSexo(termo.getAnimal().getSexo().toString());
			}
			
			dto.setNumeroMicrochip(termo.getAnimal().getNumeroMicrochip());
			// Se existir número do microchip, considerar castrado = Sim (regra de negócio requerida)
			String microchip = termo.getAnimal().getNumeroMicrochip();
			if (microchip != null && !microchip.trim().isEmpty()) {
				dto.setCastradoStr("Sim");
			} else {
				dto.setCastradoStr(termo.getAnimal().isCastrado() ? "Sim" : "Não");
			}
			
			if (termo.getAnimal().getEspecie() != null) {
				dto.setEspecie(termo.getAnimal().getEspecie().getNome());
			}
			
			if (termo.getAnimal().getRaca() != null) {
				dto.setRaca(termo.getAnimal().getRaca().getNome());
			}
			
			// Idade - usar campo idade do animal se disponível
			dto.setIdade(termo.getAnimal().getIdade());
		}
		
		// Dados específicos do termo
		dto.setPorteAnimal(termo.getPorteAnimal());
		dto.setObservacoes(termo.getObservacoes());
		dto.setQuantidadeRetirada(termo.getQuantidadeRetirada());
		
		// Data da primeira retirada
		if (termo.getDataRetirada() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dto.setDataRetirada(sdf.format(termo.getDataRetirada().getTime()));
		}
		
		// Datas dos meses seguintes
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		dto.setData1Mes(termo.getData1Mes() != null ? sdf.format(termo.getData1Mes()) : "");
		dto.setData2Mes(termo.getData2Mes() != null ? sdf.format(termo.getData2Mes()) : "");
		dto.setData3Mes(termo.getData3Mes() != null ? sdf.format(termo.getData3Mes()) : "");
		dto.setData4Mes(termo.getData4Mes() != null ? sdf.format(termo.getData4Mes()) : "");
		dto.setData5Mes(termo.getData5Mes() != null ? sdf.format(termo.getData5Mes()) : "");
		dto.setData6Mes(termo.getData6Mes() != null ? sdf.format(termo.getData6Mes()) : "");
		dto.setData7Mes(termo.getData7Mes() != null ? sdf.format(termo.getData7Mes()) : "");
		
		return dto;
	}
}
