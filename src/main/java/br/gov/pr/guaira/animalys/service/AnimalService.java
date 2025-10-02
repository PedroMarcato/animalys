package br.gov.pr.guaira.animalys.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.repository.Animais;
import br.gov.pr.guaira.animalys.util.cdi.jpa.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Named
public class AnimalService implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private Animais animais;
	
	@Inject
	private EntityManager entityManager;

	@Transactional
	public Animal salvar(Animal animal) {
		return animais.guardar(animal);
	}

	public List<Animal> buscarPorSolicitacao(Integer idSolicitacao) {
        //return animais.animaisPorSolicitacao(idSolicitacao);
		return animais.porSolicitacao(idSolicitacao);
    }
    
    @Transactional
    public void excluir(Animal animal) throws Exception {
        if (animal == null || animal.getIdAnimal() == null) {
            throw new Exception("Animal inválido para exclusão!");
        }
        
        try {
            Integer idAnimal = animal.getIdAnimal();
            
            // 1. Excluir Termos de Itraconazol relacionados
            Query queryTermosItraconazol = entityManager.createQuery(
                "DELETE FROM TermoItraconazol t WHERE t.animal.idAnimal = :idAnimal");
            queryTermosItraconazol.setParameter("idAnimal", idAnimal);
            queryTermosItraconazol.executeUpdate();
            
            // 2. Excluir Retiradas de Medicamento relacionadas
            Query queryRetiradaMedicamento = entityManager.createQuery(
                "DELETE FROM RetiradaMedicamento r WHERE r.animal.idAnimal = :idAnimal");
            queryRetiradaMedicamento.setParameter("idAnimal", idAnimal);
            queryRetiradaMedicamento.executeUpdate();
            
            // 3. Excluir Fotos do Animal
            Query queryFotos = entityManager.createQuery(
                "DELETE FROM FotoAnimal f WHERE f.animal.idAnimal = :idAnimal");
            queryFotos.setParameter("idAnimal", idAnimal);
            queryFotos.executeUpdate();
            
            // 4. Excluir Histórico do Animal
            Query queryHistorico = entityManager.createQuery(
                "DELETE FROM HistoricoAnimal h WHERE h.animal.idAnimal = :idAnimal");
            queryHistorico.setParameter("idAnimal", idAnimal);
            queryHistorico.executeUpdate();
            
            // 5. Excluir Atendimentos relacionados
            Query queryAtendimentos = entityManager.createQuery(
                "DELETE FROM Atendimento a WHERE a.animal.idAnimal = :idAnimal");
            queryAtendimentos.setParameter("idAnimal", idAnimal);
            queryAtendimentos.executeUpdate();
            
            // 6. Remover referências em Solicitações (tabela de junção solicitacao_animal)
            Query querySolicitacoes = entityManager.createNativeQuery(
                "DELETE FROM atendimento.solicitacao_animal WHERE animal = ?");
            querySolicitacoes.setParameter(1, idAnimal);
            querySolicitacoes.executeUpdate();
            
            // 7. Finalmente, excluir o animal
            entityManager.flush(); // Garante que todas as operações anteriores sejam executadas
            animais.remover(animal);
            
        } catch (Exception e) {
            throw new Exception("Erro ao excluir animal e registros relacionados: " + e.getMessage());
        }
    }
}
