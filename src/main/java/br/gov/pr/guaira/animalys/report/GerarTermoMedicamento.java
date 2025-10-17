package br.gov.pr.guaira.animalys.report;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import br.gov.pr.guaira.animalys.entity.RetiradaMedicamento;
import br.gov.pr.guaira.animalys.model.TermoMedicamentoDTO;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class GerarTermoMedicamento implements Serializable {

	private static final long serialVersionUID = 1L;

	public void gerar(RetiradaMedicamento retirada) {
		try {
			// Criar DTO para o relatório
			TermoMedicamentoDTO dto = criarDTO(retirada);
			
			List<TermoMedicamentoDTO> lista = new ArrayList<>();
			lista.add(dto);
			
			System.out.println("Gerando relatório do termo de medicamento");
			
			HashMap<String, Object> parameters = new HashMap<>();
			
			// Caminho do relatório
			String caminho = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/arquivos/relatorios/termoMedicamentos.jrxml");
			
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
			response.setHeader("Content-disposition", "inline;filename=termo_medicamento.pdf");
			
			response.getOutputStream().write(pdfBytes);
			response.getCharacterEncoding();
			
			FacesContext.getCurrentInstance().responseComplete();
			
			System.out.println("Relatório do termo de medicamento gerado com sucesso");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Erro ao gerar relatório do termo de medicamento: " + ex.getMessage());
		}
	}
	
	private TermoMedicamentoDTO criarDTO(RetiradaMedicamento retirada) {
		TermoMedicamentoDTO dto = new TermoMedicamentoDTO();
		
		// Dados do proprietário
		if (retirada.getProprietario() != null) {
			dto.setNomeProprietario(retirada.getProprietario().getNome());
			dto.setCpfProprietario(retirada.getProprietario().getCpf());
			dto.setRgProprietario(retirada.getProprietario().getRg());
			
			if (retirada.getProprietario().getEndereco() != null) {
				dto.setEnderecoProprietario(retirada.getProprietario().getEndereco().getLogradouro());
				dto.setBairroProprietario(retirada.getProprietario().getEndereco().getBairro());
				dto.setCepProprietario(retirada.getProprietario().getEndereco().getCep());
				
				// Cidade do proprietário
				if (retirada.getProprietario().getEndereco().getCidade() != null) {
					dto.setCidadeProprietario(retirada.getProprietario().getEndereco().getCidade().getNome());
				}
			}
			
			if (retirada.getProprietario().getContato() != null) {
				dto.setTelefoneProprietario(retirada.getProprietario().getContato().getTelefone());
				dto.setCelularProprietario(retirada.getProprietario().getContato().getCelular());
				// Fallback para contatoProprietario (campo usado no relatório original)
				String contato = retirada.getProprietario().getContato().getTelefone();
				if (contato == null || contato.isEmpty()) {
					contato = retirada.getProprietario().getContato().getCelular();
				}
				dto.setContatoProprietario(contato);
			}
		}
		
		// Dados do animal
		if (retirada.getAnimal() != null) {
			dto.setNomeAnimal(retirada.getAnimal().getNome());
			dto.setCor(retirada.getAnimal().getCor());
			
			// Converter Sexo para String
			if (retirada.getAnimal().getSexo() != null) {
				dto.setSexo(retirada.getAnimal().getSexo().toString());
			}
			
			dto.setNumeroMicrochip(retirada.getAnimal().getNumeroMicrochip());
			// Se existir número do microchip, considerar castrado = Sim (regra de negócio requerida)
			String microchip = retirada.getAnimal().getNumeroMicrochip();
			if (microchip != null && !microchip.trim().isEmpty()) {
				dto.setCastradoStr("Sim");
			} else {
				dto.setCastradoStr(retirada.getAnimal().isCastrado() ? "Sim" : "Não");
			}
			
			// Espécie: preferir raca.especie.nome quando disponível (corrige casos de null)
			if (retirada.getAnimal().getRaca() != null && retirada.getAnimal().getRaca().getEspecie() != null) {
				dto.setEspecie(retirada.getAnimal().getRaca().getEspecie().getNome());
			} else if (retirada.getAnimal().getEspecie() != null) {
				dto.setEspecie(retirada.getAnimal().getEspecie().getNome());
			} else {
				dto.setEspecie("Não informado");
			}
			
			// Idade - usar campo idade do animal se disponível
			dto.setIdade(retirada.getAnimal().getIdade());
		}
		
		// Dados da retirada/medicamento
		dto.setNomeMedicamento(retirada.getNomeMedicamento());
		dto.setQuantidade1Mes(retirada.getQuantidade());
		dto.setObservacoes(retirada.getObservacoes());
		
		// Data da retirada
		if (retirada.getDataRetirada() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dto.setData1Mes(sdf.format(retirada.getDataRetirada().getTime()));
		}
		
		return dto;
	}
}
