package br.gov.pr.guaira.animalys.report;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import br.gov.pr.guaira.animalys.entity.RetiradaRacao;
import br.gov.pr.guaira.animalys.model.TermoRacaoDTO;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class GerarTermoRacao implements Serializable {

	private static final long serialVersionUID = 1L;

	public void gerar(RetiradaRacao retirada) {
		try {
			// Criar DTO para o relatório
			TermoRacaoDTO dto = criarDTO(retirada);
			
			List<TermoRacaoDTO> lista = new ArrayList<>();
			lista.add(dto);
			
			System.out.println("Gerando relatório do termo de ração");
			
			HashMap<String, Object> parameters = new HashMap<>();
			
			// Caminho do relatório
			String caminho = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/arquivos/relatorios/termoRacao.jrxml");
			
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
			response.setHeader("Content-disposition", "inline;filename=termo_racao.pdf");
			
			response.getOutputStream().write(pdfBytes);
			response.getCharacterEncoding();
			
			FacesContext.getCurrentInstance().responseComplete();
			
			System.out.println("Relatório do termo de ração gerado com sucesso");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Erro ao gerar relatório do termo de ração: " + ex.getMessage());
		}
	}
	
	private TermoRacaoDTO criarDTO(RetiradaRacao retirada) {
		TermoRacaoDTO dto = new TermoRacaoDTO();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
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
		
		// Dados das retiradas mensais - DATAS (converter Date para String formatado)
		dto.setData1Mes(retirada.getData1Mes() != null ? sdf.format(retirada.getData1Mes()) : "");
		dto.setData2Mes(retirada.getData2Mes() != null ? sdf.format(retirada.getData2Mes()) : "");
		dto.setData3Mes(retirada.getData3Mes() != null ? sdf.format(retirada.getData3Mes()) : "");
		dto.setData4Mes(retirada.getData4Mes() != null ? sdf.format(retirada.getData4Mes()) : "");
		dto.setData5Mes(retirada.getData5Mes() != null ? sdf.format(retirada.getData5Mes()) : "");
		dto.setData6Mes(retirada.getData6Mes() != null ? sdf.format(retirada.getData6Mes()) : "");
		dto.setData7Mes(retirada.getData7Mes() != null ? sdf.format(retirada.getData7Mes()) : "");
		dto.setData8Mes(retirada.getData8Mes() != null ? sdf.format(retirada.getData8Mes()) : "");
		dto.setData9Mes(retirada.getData9Mes() != null ? sdf.format(retirada.getData9Mes()) : "");
		dto.setData10Mes(retirada.getData10Mes() != null ? sdf.format(retirada.getData10Mes()) : "");
		dto.setData11Mes(retirada.getData11Mes() != null ? sdf.format(retirada.getData11Mes()) : "");
		dto.setData12Mes(retirada.getData12Mes() != null ? sdf.format(retirada.getData12Mes()) : "");
		
		// Dados das retiradas mensais - QUANTIDADES (BigDecimal - passar direto)
		dto.setQuantidade1Mes(retirada.getQuantidade1Mes());
		dto.setQuantidade2Mes(retirada.getQuantidade2Mes());
		dto.setQuantidade3Mes(retirada.getQuantidade3Mes());
		dto.setQuantidade4Mes(retirada.getQuantidade4Mes());
		dto.setQuantidade5Mes(retirada.getQuantidade5Mes());
		dto.setQuantidade6Mes(retirada.getQuantidade6Mes());
		dto.setQuantidade7Mes(retirada.getQuantidade7Mes());
		dto.setQuantidade8Mes(retirada.getQuantidade8Mes());
		dto.setQuantidade9Mes(retirada.getQuantidade9Mes());
		dto.setQuantidade10Mes(retirada.getQuantidade10Mes());
		dto.setQuantidade11Mes(retirada.getQuantidade11Mes());
		dto.setQuantidade12Mes(retirada.getQuantidade12Mes());
		
		// Observações e mês de referência
		dto.setObservacoes(retirada.getObservacoes());
		dto.setMesReferenciaFormatado(retirada.getMesReferenciaFormatado());
		
		return dto;
	}
}
