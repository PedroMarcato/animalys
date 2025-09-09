package br.gov.pr.guaira.animalys.dao;

import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import br.gov.pr.guaira.animalys.dto.HistoricoAnimalDTO;
import br.gov.pr.guaira.animalys.entity.Atendimento;
import br.gov.pr.guaira.animalys.entity.ItemLoteAtendimento;

public class HistoricoAnimalDAO implements Serializable {
  
  private static final long serialVersionUID = 1L;

  @Inject
  private EntityManager em;

  public List<HistoricoAnimalDTO> buscarHistoricoPorAnimal(Integer idAnimal) {
    String jpql = "SELECT a FROM br.gov.pr.guaira.animalys.entity.Atendimento a " +
      "JOIN FETCH a.animal an " +
      "LEFT JOIN FETCH a.tratamento t " +
      "LEFT JOIN FETCH a.profissional pr " +
      "LEFT JOIN FETCH a.solicitacao s " +
      "LEFT JOIN FETCH an.raca r " +
      "LEFT JOIN FETCH r.especie e " +
      "LEFT JOIN FETCH an.proprietario p " +
      "LEFT JOIN FETCH p.contato c " +
      // Removido JOIN FETCH a.procedimentos proc para evitar duplicidade
      "WHERE an.idAnimal = :idAnimal " +
      "ORDER BY a.data DESC";

  TypedQuery<br.gov.pr.guaira.animalys.entity.Atendimento> queryAt = em.createQuery(jpql, br.gov.pr.guaira.animalys.entity.Atendimento.class);
  queryAt.setParameter("idAnimal", idAnimal);
  List<br.gov.pr.guaira.animalys.entity.Atendimento> atendimentos = queryAt.getResultList();

    List<HistoricoAnimalDTO> dtos = new java.util.ArrayList<>();
    for (br.gov.pr.guaira.animalys.entity.Atendimento a : atendimentos) {
      String procedimentosStr = "";
      if (a.getProcedimentos() != null && !a.getProcedimentos().isEmpty()) {
        procedimentosStr = a.getProcedimentos().stream()
          .filter(p -> p != null && p.getNome() != null)
          .map(p -> p.getNome())
          .distinct()
          .sorted()
          .reduce((p1, p2) -> p1 + ", " + p2).orElse("");
      }
      HistoricoAnimalDTO dto = new HistoricoAnimalDTO(
        a.getIdAtendimento(),
        a.getData() != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(a.getData().getTime()) : "",
        procedimentosStr,
        a.getProfissional() != null ? a.getProfissional().getNome() : "",
        a.getAnimal() != null ? a.getAnimal().getNome() : "",
        a.getTipoAtendimento() != null ? a.getTipoAtendimento().toString() : "",
        a.getDiagnostico(),
        "",
        a.getSolicitacao() != null && a.getSolicitacao().getStatus() != null ? a.getSolicitacao().getStatus().toString() : "",
        a.getAnimal() != null ? a.getAnimal().getIdade() : "",
        a.getAnimal() != null && a.getAnimal().getSexo() != null ? a.getAnimal().getSexo().toString() : "",
        a.getAnimal() != null ? a.getAnimal().getCor() : "",
        a.getAnimal() != null && a.getAnimal().getRaca() != null ? a.getAnimal().getRaca().getNome() : "",
        a.getAnimal() != null && a.getAnimal().getRaca() != null && a.getAnimal().getRaca().getEspecie() != null ? a.getAnimal().getRaca().getEspecie().getNome() : "",
        a.getAnimal() != null && a.getAnimal().getProprietario() != null ? a.getAnimal().getProprietario().getNome() : "",
        a.getAnimal() != null && a.getAnimal().getProprietario() != null ? a.getAnimal().getProprietario().getCpf() : "",
        a.getAnimal() != null && a.getAnimal().getProprietario() != null && a.getAnimal().getProprietario().getEndereco() != null ? a.getAnimal().getProprietario().getEndereco().getLogradouro() : "",
        a.getAnimal() != null && a.getAnimal().getProprietario() != null && a.getAnimal().getProprietario().getEndereco() != null ? a.getAnimal().getProprietario().getEndereco().getNumero() : "",
        a.getAnimal() != null && a.getAnimal().getProprietario() != null && a.getAnimal().getProprietario().getEndereco() != null ? a.getAnimal().getProprietario().getEndereco().getBairro() : "",
        a.getAnimal() != null && a.getAnimal().getProprietario() != null && a.getAnimal().getProprietario().getContato() != null ? a.getAnimal().getProprietario().getContato().getCelular() : "",
        a.getAnimal() != null ? a.getAnimal().getDataEntrada() : null,
        a.getAnimal() != null ? a.getAnimal().getFoto() : "",
        a.getAnimal() != null ? a.getAnimal().getNumeroMicrochip() : "",
        a.getEscoreCorporal() != null ? a.getEscoreCorporal().toString() : "",
        a.getPeso(),
        a.getFc(),
        a.getFr(),
        a.getTemperatura() != null ? a.getTemperatura().toString() : "",
        a.getTratamento() != null ? a.getTratamento().getNome() : "",
        a.getAnimal() != null ? a.getAnimal().getIdAnimal() : null
      );
  // Preencher o campo castrado do DTO
    // Preencher castrado com base no status do animal
    boolean isCastrado = false;
    if (a.getAnimal() != null && a.getAnimal().getStatus() != null) {
      isCastrado = br.gov.pr.guaira.animalys.entity.Status.CASTRADO.equals(a.getAnimal().getStatus());
    }
    dto.setCastrado(isCastrado);
      
      // Buscar medicamentos do atendimento
      String jpqlMedicamentos = "SELECT il FROM ItemLoteAtendimento il " +
        "JOIN FETCH il.lote l " +
        "JOIN FETCH l.produto p " +
        "WHERE il.atendimento.idAtendimento = :idAtendimento";
      
      TypedQuery<ItemLoteAtendimento> queryMed = em.createQuery(jpqlMedicamentos, ItemLoteAtendimento.class);
      queryMed.setParameter("idAtendimento", a.getIdAtendimento());
      List<ItemLoteAtendimento> medicamentos = queryMed.getResultList();
      
      dto.setMedicamentos(medicamentos);
      
      dtos.add(dto);
    }
    return dtos;

  // Código antigo removido, pois agora o DTO é montado manualmente
  }

  public void removerAtendimento(Integer idAtendimento) {
    Atendimento atendimento = em.find(Atendimento.class, idAtendimento);
    if (atendimento != null) {
      em.remove(atendimento);
      em.flush();
    }
  }
}
