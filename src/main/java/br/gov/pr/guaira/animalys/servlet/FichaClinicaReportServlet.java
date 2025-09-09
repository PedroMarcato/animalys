package br.gov.pr.guaira.animalys.servlet;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.gov.pr.guaira.animalys.dto.HistoricoAnimalDTO;
import br.gov.pr.guaira.animalys.report.FichaClinicaReportService;

@WebServlet("/relatorio/ficha-clinica")
public class FichaClinicaReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Inject
    private FichaClinicaReportService reportService;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            HttpSession session = request.getSession();
            @SuppressWarnings("unchecked")
            List<HistoricoAnimalDTO> dadosRelatorio = (List<HistoricoAnimalDTO>) session.getAttribute("dadosRelatorio");
            
            if (dadosRelatorio != null && !dadosRelatorio.isEmpty()) {
                reportService.gerarRelatorioServlet(dadosRelatorio, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dados do relatório não encontrados na sessão");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao gerar relatório: " + e.getMessage());
        }
    }
}
