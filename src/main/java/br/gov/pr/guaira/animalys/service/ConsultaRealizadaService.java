package br.gov.pr.guaira.animalys.service;

import br.gov.pr.guaira.animalys.dao.ConsultaRealizadaDAO;
import br.gov.pr.guaira.animalys.dto.ConsultaRealizada;
import br.gov.pr.guaira.animalys.entity.ConsultaFiltro;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class ConsultaRealizadaService {

    @Inject
    private ConsultaRealizadaDAO dao;

    /**
     * Lista todas as consultas realizadas sem filtro
     */
    public List<ConsultaRealizada> listar() {
        return dao.listarConsultasRealizadas();
    }

    /**
     * Busca consultas aplicando filtros opcionais (proprietário, nome animal, datas)
     */
    public List<ConsultaRealizada> buscarComFiltro(ConsultaFiltro filtro) {
        return dao.buscarComFiltro(filtro);
    }
}