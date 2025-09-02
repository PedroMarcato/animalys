package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.List;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.borders.Border;

import br.gov.pr.guaira.animalys.dao.HistoricoAnimalDAO;
import br.gov.pr.guaira.animalys.dto.HistoricoAnimalDTO;

@Named
@ViewScoped
public class HistoricoAnimalBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private HistoricoAnimalDAO historicoAnimalDAO;

    private Integer idAnimal;
    private List<HistoricoAnimalDTO> lista;
    private String nomeAnimal; // Novo campo

    @PostConstruct
    public void init() {
        // Obtem o parâmetro da URL
        String parametro = javax.faces.context.FacesContext.getCurrentInstance()
                             .getExternalContext().getRequestParameterMap().get("animalId");
        if (parametro != null && !parametro.isEmpty()) {
            idAnimal = Integer.valueOf(parametro);
            lista = historicoAnimalDAO.buscarHistoricoPorAnimal(idAnimal);

            // Pega o nome do animal do primeiro item da lista
            if (lista != null && !lista.isEmpty()) {
                nomeAnimal = lista.get(0).getNomeAnimal();
            } else {
                nomeAnimal = "Animal não identificado";
            }
        }
    }

    // Getters e Setters

    public List<HistoricoAnimalDTO> getLista() {
        return lista;
    }

    public void setLista(List<HistoricoAnimalDTO> lista) {
        this.lista = lista;
    }

    public Integer getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(Integer idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    // Adicione getters para os dados da ficha clínica do primeiro item
    public HistoricoAnimalDTO getFichaClinica() {
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }

    public void excluir(Integer idAtendimento) {
        historicoAnimalDAO.removerAtendimento(idAtendimento);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage("Atendimento excluído com sucesso."));
        lista = historicoAnimalDAO.buscarHistoricoPorAnimal(idAnimal);
    }
    // Getter para foto do animal
    public String getFotoAnimal() {
        HistoricoAnimalDTO dto = getFichaClinica();
        return (dto != null) ? dto.getFotoAnimal() : null;
    }

    // impressão

    public void imprimirFichaClinica() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
            
            // Configurar resposta HTTP para PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"ficha_clinica_" + nomeAnimal + ".pdf\"");
            
            // Criar documento PDF
            PdfWriter writer = new PdfWriter(response.getOutputStream());
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            
            // Configurar encoding UTF-8
            writer.setCloseStream(false);
            
            // Fontes
            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            
            // Cores personalizadas
            DeviceRgb azulTitulo = new DeviceRgb(41, 128, 185);        // Azul para títulos
            DeviceRgb verdeCabecalho = new DeviceRgb(39, 174, 96);     // Verde para cabeçalhos
            DeviceRgb cinzaFundo = new DeviceRgb(236, 240, 241);       // Cinza claro para fundos
            DeviceRgb azulSecundario = new DeviceRgb(52, 152, 219);    // Azul secundário
            DeviceRgb vermelhoAnimal = new DeviceRgb(231, 76, 60);     // Vermelho para nome do animal
            

            // Cabeçalho com logos e textos institucionais
            Table headerTable = new Table(UnitValue.createPercentArray(new float[]{20, 60, 20})).useAllAvailableWidth();
            headerTable.setMarginBottom(0);
            // Logo do centro de controle (esquerda)
            Cell cellLogoCentro = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setPadding(0);
            try {
                String logoCentroPath = facesContext.getExternalContext().getRealPath("/resources/images/centroDeControle.png");
                java.io.File logoCentroFile = new java.io.File(logoCentroPath);
                if (logoCentroFile.exists()) {
                    com.itextpdf.layout.element.Image logoCentro = new com.itextpdf.layout.element.Image(
                        com.itextpdf.io.image.ImageDataFactory.create(logoCentroPath));
                    logoCentro.setHeight(44);
                    logoCentro.setAutoScale(false);
                    cellLogoCentro.add(logoCentro);
                } else {
                    cellLogoCentro.add(new Paragraph("[Logo Centro]").setFont(normalFont).setFontSize(8));
                }
            } catch (Exception e) {
                cellLogoCentro.add(new Paragraph("Erro logo centro").setFont(normalFont).setFontSize(8));
            }
            // Texto verde abaixo do logo
            cellLogoCentro.add(new Paragraph("Centro de Controle\nAnimal")
                .setFont(boldFont)
                .setFontSize(9)
                .setFontColor(verdeCabecalho)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(0));
            headerTable.addCell(cellLogoCentro);

            // Textos institucionais (centro)
            Cell cellCentro = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setPadding(0);
            cellCentro.add(new Paragraph("MUNICIPIO DE GUAIRA").setFont(boldFont).setFontSize(11).setMargin(0));
            cellCentro.add(new Paragraph("SEMAM - DIRETORIA DE MEIO AMBIENTE").setFont(normalFont).setFontSize(9).setMargin(0));
            cellCentro.add(new Paragraph("CENTRO DE CONTROLE ANIMAL").setFont(boldFont).setFontSize(10).setMargin(0));
            headerTable.addCell(cellCentro);

            // Logo do município (direita)
            Cell cellLogoMunicipio = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setPadding(0);
            try {
                String logoMunicipioPath = facesContext.getExternalContext().getRealPath("/resources/images/municipio.png");
                java.io.File logoMunicipioFile = new java.io.File(logoMunicipioPath);
                if (logoMunicipioFile.exists()) {
                    com.itextpdf.layout.element.Image logoMunicipio = new com.itextpdf.layout.element.Image(
                        com.itextpdf.io.image.ImageDataFactory.create(logoMunicipioPath));
                    logoMunicipio.setHeight(44);
                    logoMunicipio.setAutoScale(false);
                    cellLogoMunicipio.add(logoMunicipio);
                } else {
                    cellLogoMunicipio.add(new Paragraph("[Logo Município]").setFont(normalFont).setFontSize(8));
                }
            } catch (Exception e) {
                cellLogoMunicipio.add(new Paragraph("Erro logo município").setFont(normalFont).setFontSize(8));
            }
            headerTable.addCell(cellLogoMunicipio);

            document.add(headerTable);

            // Título "FICHA DE ATENDIMENTO" em preto, colado ao cabeçalho
            Paragraph titulo = new Paragraph("FICHA DE ATENDIMENTO")
                .setFont(boldFont)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(DeviceRgb.BLACK)
                .setMarginTop(0)
                .setMarginBottom(6);
            document.add(titulo);

           
            
            // Nome do animal em destaque - "Animal:" em preto e nome em vermelho
            Paragraph nomeAnimalPdf = new Paragraph()
                .setFont(boldFont)
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
            
            // Adicionar "Animal:" em preto
            nomeAnimalPdf.add(new com.itextpdf.layout.element.Text("Animal: ").setFontColor(ColorConstants.BLACK));
            // Adicionar nome do animal em vermelho
            nomeAnimalPdf.add(new com.itextpdf.layout.element.Text(nomeAnimal).setFontColor(vermelhoAnimal));
            
            document.add(nomeAnimalPdf);
            
            HistoricoAnimalDTO ficha = getFichaClinica();
            if (ficha != null) {
                // Tabela única ocupando toda a largura da folha
                Table tabelaDados = new Table(UnitValue.createPercentArray(new float[]{2, 3})).useAllAvailableWidth();
                
                // DADOS DO PROPRIETARIO (título ocupando duas colunas)
                Cell cellTituloProprietario = new Cell(1,2)
                    .add(new Paragraph("DADOS DO PROPRIETARIO").setFont(boldFont).setFontSize(14).setFontColor(verdeCabecalho))
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setPaddingTop(8f)
                    .setPaddingBottom(4f);
                tabelaDados.addCell(cellTituloProprietario);
                
                adicionarLinhaTabelaColorida(tabelaDados, "Nome:", 
                    ficha.getNomeProprietario() != null && !ficha.getNomeProprietario().trim().isEmpty() ? ficha.getNomeProprietario() : "Nao informado", 
                    boldFont, normalFont, cinzaFundo, azulTitulo);
                adicionarLinhaTabelaColorida(tabelaDados, "CPF:", 
                    ficha.getCpfProprietario() != null && !ficha.getCpfProprietario().trim().isEmpty() ? ficha.getCpfProprietario() : "Nao informado", 
                    boldFont, normalFont, cinzaFundo, azulTitulo);
                
                String enderecoCompleto = "Nao informado";
                if (ficha.getEnderecoProprietario() != null && !ficha.getEnderecoProprietario().trim().isEmpty()) {
                    enderecoCompleto = ficha.getEnderecoProprietario();
                    if (ficha.getNumeroEndereco() != null && !ficha.getNumeroEndereco().trim().isEmpty()) {
                        enderecoCompleto += ", " + ficha.getNumeroEndereco();
                    }
                }
                adicionarLinhaTabelaColorida(tabelaDados, "Endereco:", enderecoCompleto, boldFont, normalFont, cinzaFundo, azulTitulo);
                adicionarLinhaTabelaColorida(tabelaDados, "Bairro:", 
                    ficha.getBairroProprietario() != null && !ficha.getBairroProprietario().trim().isEmpty() ? ficha.getBairroProprietario() : "Nao informado", 
                    boldFont, normalFont, cinzaFundo, azulTitulo);
                adicionarLinhaTabelaColorida(tabelaDados, "Contato:", 
                    ficha.getContatoProprietario() != null && !ficha.getContatoProprietario().trim().isEmpty() ? ficha.getContatoProprietario() : "Nao informado", 
                    boldFont, normalFont, cinzaFundo, azulTitulo);
                
                // DADOS DO ANIMAL (título ocupando duas colunas)
                Cell cellTituloAnimal = new Cell(1,2)
                    .add(new Paragraph("DADOS DO ANIMAL").setFont(boldFont).setFontSize(14).setFontColor(verdeCabecalho))
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setPaddingTop(15f)
                    .setPaddingBottom(4f);
                tabelaDados.addCell(cellTituloAnimal);
                
                adicionarLinhaTabelaColorida(tabelaDados, "Especie:", 
                    ficha.getEspecie() != null ? ficha.getEspecie() : "Nao informado", boldFont, normalFont, cinzaFundo, azulTitulo);
                adicionarLinhaTabelaColorida(tabelaDados, "Raca:", 
                    ficha.getRaca() != null ? ficha.getRaca() : "Nao informado", boldFont, normalFont, cinzaFundo, azulTitulo);
                adicionarLinhaTabelaColorida(tabelaDados, "Idade:", 
                    ficha.getIdade() != null ? ficha.getIdade() : "Nao informado", boldFont, normalFont, cinzaFundo, azulTitulo);
                adicionarLinhaTabelaColorida(tabelaDados, "Sexo:", 
                    ficha.getSexo() != null ? ficha.getSexo() : "Nao informado", boldFont, normalFont, cinzaFundo, azulTitulo);
                adicionarLinhaTabelaColorida(tabelaDados, "Cor:", 
                    ficha.getCor() != null && !ficha.getCor().trim().isEmpty() ? ficha.getCor() : "Nao informado", boldFont, normalFont, cinzaFundo, azulTitulo);
                
                if (ficha.getDataEntrada() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    adicionarLinhaTabelaColorida(tabelaDados, "Data de Entrada:", sdf.format(ficha.getDataEntrada().getTime()), boldFont, normalFont, cinzaFundo, azulTitulo);
                }
                
                document.add(tabelaDados);
                
                document.add(new Paragraph("\n"));
                
                // Linha decorativa colorida
                document.add(new Paragraph("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(normalFont)
                    .setFontColor(verdeCabecalho)
                    .setFontSize(10));
                
                // Histórico de atendimentos
                if (lista != null && !lista.isEmpty()) {
                    Paragraph historicoTitulo = new Paragraph("▼ HISTORICO DE ATENDIMENTOS")
                        .setFont(boldFont)
                        .setFontSize(14)
                        .setFontColor(verdeCabecalho)
                        .setMarginBottom(15)
                        .setMarginTop(10);
                    document.add(historicoTitulo);
                    
                    int contador = 1;
                    for (HistoricoAnimalDTO historico : lista) {
                        // Título do atendimento com fundo colorido
                        Paragraph atendimentoTitulo = new Paragraph("Atendimento " + contador + " - " + historico.getData())
                            .setFont(boldFont)
                            .setFontSize(12)
                            .setFontColor(ColorConstants.WHITE)
                            .setBackgroundColor(azulSecundario)
                            .setPadding(5)
                            .setMarginBottom(8)
                            .setMarginTop(10);
                        document.add(atendimentoTitulo);
                        
                        Table tabelaAtendimento = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
                        // Remove bordas da tabela de atendimento
                        
                        DeviceRgb fundoAtendimento = new DeviceRgb(245, 245, 245); // Cinza muito claro
                        
                        if (historico.getTipoAtendimento() != null && !historico.getTipoAtendimento().isEmpty()) {
                            adicionarLinhaTabelaColorida(tabelaAtendimento, "Tipo de Atendimento:", historico.getTipoAtendimento(), boldFont, normalFont, fundoAtendimento, azulTitulo);
                        }
                        if (historico.getProcedimento() != null && !historico.getProcedimento().isEmpty()) {
                            adicionarLinhaTabelaColorida(tabelaAtendimento, "Procedimento:", historico.getProcedimento(), boldFont, normalFont, fundoAtendimento, azulTitulo);
                        }
                        if (historico.getDiagnostico() != null && !historico.getDiagnostico().isEmpty()) {
                            // Remover tags HTML do diagnóstico
                            String diagnosticoLimpo = historico.getDiagnostico().replaceAll("<[^>]+>", "");
                            adicionarLinhaTabelaColorida(tabelaAtendimento, "Diagnostico:", diagnosticoLimpo, boldFont, normalFont, fundoAtendimento, azulTitulo);
                        }
                        if (historico.getEscoreCorporal() != null && !historico.getEscoreCorporal().isEmpty()) {
                            adicionarLinhaTabelaColorida(tabelaAtendimento, "Escore Corporal:", historico.getEscoreCorporal(), boldFont, normalFont, fundoAtendimento, azulTitulo);
                        }
                        if (historico.getPeso() != null) {
                            adicionarLinhaTabelaColorida(tabelaAtendimento, "Peso:", historico.getPeso() + " kg", boldFont, normalFont, fundoAtendimento, azulTitulo);
                        }
                        if (historico.getFc() != null && !historico.getFc().isEmpty()) {
                            adicionarLinhaTabelaColorida(tabelaAtendimento, "FC:", historico.getFc() + " bpm", boldFont, normalFont, fundoAtendimento, azulTitulo);
                        }
                        if (historico.getFr() != null && !historico.getFr().isEmpty()) {
                            adicionarLinhaTabelaColorida(tabelaAtendimento, "FR:", historico.getFr() + " irpm", boldFont, normalFont, fundoAtendimento, azulTitulo);
                        }
                        if (historico.getTemperatura() != null && !historico.getTemperatura().isEmpty()) {
                            adicionarLinhaTabelaColorida(tabelaAtendimento, "Temperatura:", historico.getTemperatura() + " graus C", boldFont, normalFont, fundoAtendimento, azulTitulo);
                        }
                        if (historico.getTratamento() != null && !historico.getTratamento().isEmpty()) {
                            adicionarLinhaTabelaColorida(tabelaAtendimento, "Tratamento:", historico.getTratamento(), boldFont, normalFont, fundoAtendimento, azulTitulo);
                        }
                        adicionarLinhaTabelaColorida(tabelaAtendimento, "Responsavel:", 
                            historico.getResponsavel() != null ? historico.getResponsavel() : "", boldFont, normalFont, fundoAtendimento, azulTitulo);
                        adicionarLinhaTabelaColorida(tabelaAtendimento, "Situacao:", 
                            historico.getStatusSolicitacao() != null ? historico.getStatusSolicitacao() : "", boldFont, normalFont, fundoAtendimento, azulTitulo);
                        
                        document.add(tabelaAtendimento);
                        contador++;
                    }
                }
                
                // Rodapé com local para assinatura - linha divisória preta
                document.add(new Paragraph("\n\n"));
                document.add(new Paragraph("═══════════════════════════════════════════════")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(boldFont)
                    .setFontColor(ColorConstants.BLACK)
                    .setFontSize(12));
                
                // Assinatura do responsável no topo (sem cores, centralizada)
                document.add(new Paragraph("\n\n_________________________________")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(normalFont)
                    .setFontColor(ColorConstants.BLACK));
                document.add(new Paragraph("Assinatura do Responsavel")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(boldFont)
                    .setFontColor(ColorConstants.BLACK)
                    .setFontSize(12)
                    .setMarginBottom(20));
                
                // Data de emissão
                SimpleDateFormat sdfEmissao = new SimpleDateFormat("dd/MM/yyyy 'as' HH:mm");
                document.add(new Paragraph("\nEmitido em: " + sdfEmissao.format(new java.util.Date()))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(normalFont)
                    .setFontColor(azulTitulo)
                    .setFontSize(8)
                    .setMarginTop(20));
            }
            
            document.close();
            facesContext.responseComplete();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao gerar PDF", e.getMessage()));
        }
    }
    
    private void adicionarLinhaTabelaColorida(Table tabela, String rotulo, String valor, PdfFont boldFont, PdfFont normalFont, DeviceRgb corFundo, DeviceRgb corTextoRotulo) {
        if (valor == null) valor = "";
        
        Cell celulaRotulo = new Cell()
            .add(new Paragraph(rotulo).setFont(boldFont).setFontColor(corTextoRotulo).setFontSize(10))
            .setBackgroundColor(corFundo)
            .setBorder(new SolidBorder(corTextoRotulo, 0.5f));
        
        Cell celulaValor = new Cell()
            .add(new Paragraph(valor).setFont(normalFont).setFontSize(10))
            .setBorder(new SolidBorder(corTextoRotulo, 0.5f));
        
        tabela.addCell(celulaRotulo);
        tabela.addCell(celulaValor);
    }
}
