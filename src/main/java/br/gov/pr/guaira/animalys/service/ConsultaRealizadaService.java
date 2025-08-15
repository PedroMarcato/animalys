package br.gov.pr.guaira.animalys.service;

import br.gov.pr.guaira.animalys.dao.ConsultaRealizadaDAO;
import br.gov.pr.guaira.animalys.dto.ConsultaRealizada;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ConsultaRealizadaService {

    @Inject
    private ConsultaRealizadaDAO dao;

    public List<ConsultaRealizada> buscarFinalizadas() {
        return dao.listarConsultasFinalizadas();
    }
}
