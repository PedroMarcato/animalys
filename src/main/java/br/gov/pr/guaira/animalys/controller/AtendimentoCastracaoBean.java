package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Atendimento;
import br.gov.pr.guaira.animalys.entity.ItemLoteAtendimento;
import br.gov.pr.guaira.animalys.entity.Lote;
import br.gov.pr.guaira.animalys.entity.Procedimento;
import br.gov.pr.guaira.animalys.entity.Solicitacao;
import br.gov.pr.guaira.animalys.entity.Status;
import br.gov.pr.guaira.animalys.entity.TipoAtendimento;
import br.gov.pr.guaira.animalys.repository.Atendimentos;
import br.gov.pr.guaira.animalys.repository.Lotes;
import br.gov.pr.guaira.animalys.repository.Procedimentos;
import br.gov.pr.guaira.animalys.repository.Solicitacoes;
import br.gov.pr.guaira.animalys.security.Seguranca;
import br.gov.pr.guaira.animalys.security.UsuarioLogado;
import br.gov.pr.guaira.animalys.service.AnimalService;
import br.gov.pr.guaira.animalys.service.AtendimentoService;
import br.gov.pr.guaira.animalys.service.LoteService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;
import br.gov.pr.guaira.animalys.dto.ProfissionalSelectDTO;
@Named
@ViewScoped
public class AtendimentoCastracaoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public AtendimentoCastracaoBean() {
		this.atendimento = new Atendimento();
		this.itemLoteSelecionado = new ItemLoteAtendimento();
		this.procedimentosSelecionados = new ArrayList<>();
		this.itensLotes = new ArrayList<>();
		this.usuarioLogado = new Seguranca();
		this.solicitacao = new Solicitacao();
	}

	public void inicializar() {
		this.alterarAtendimento1 = false;
		this.dataAtendimento = Calendar.getInstance();
		this.dataAtual = Calendar.getInstance();
		this.dataAtual.set(Calendar.HOUR, 23);
		this.dataAtual.set(Calendar.MINUTE, 59);
		this.dataAtual.set(Calendar.SECOND, 59);
		
		this.procedimentosCadastrados = this.procedimentos.procedimentosCadastrados();

		// Carrega profissionais para o selectOneMenu usando DTOs
		this.profissionaisSelect = profissionaisRepo.profissionaisParaSelect();

		this.buscaSolicitacao();
		this.buscaAtendimentosAnteriores1();
	}

	private Animal animal;
	private Atendimento atendimento;
	private Solicitacao solicitacao;
	private Procedimento procedimentoSelecionado;
	private ItemLoteAtendimento itemLoteSelecionado;
	private Calendar dataAtendimento;
	private Calendar dataAtual;
	private boolean alterarAtendimento1;
	private boolean temAtendimentoAnterior;
	private Integer quantidade;
	private List<Procedimento> procedimentosSelecionados;
	private List<Procedimento> procedimentosCadastrados;
	private List<ItemLoteAtendimento> itensLotes;
	private List<Atendimento> atendimentosAnteriores;
	private List<ProfissionalSelectDTO> profissionaisSelect;
	private ProfissionalSelectDTO profissionalSelecionado;

	@Inject
	private AtendimentoService atendimentoService;
	@Inject
	private Procedimentos procedimentos;
	@Inject
	private Lotes lotes;
	@Inject
	private LoteService loteService;
	@Inject
	private Atendimentos atendimentos;
	@Inject
	private Solicitacoes solicitacoes;
	@UsuarioLogado
	private Seguranca usuarioLogado;
	@Inject
	private AnimalService animalService;
	@Inject
	private br.gov.pr.guaira.animalys.repository.Profissionais profissionaisRepo;


	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public Atendimento getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
	}

	public boolean isAlterarAtendimento1() {
		return alterarAtendimento1;
	}

	public void setAlterarAtendimento1(boolean alterarAtendimento1) {
		this.alterarAtendimento1 = alterarAtendimento1;
	}

	public List<Procedimento> getProcedimentosSelecionados() {
		return procedimentosSelecionados;
	}

	public void setProcedimentosSelecionados(List<Procedimento> procedimentosSelecionados) {
		this.procedimentosSelecionados = procedimentosSelecionados;
	}

	public List<Procedimento> getProcedimentosCadastrados() {
		return procedimentosCadastrados;
	}

	public Procedimento getProcedimentoSelecionado() {
		return procedimentoSelecionado;
	}

	public void setProcedimentoSelecionado(Procedimento procedimentoSelecionado) {
		this.procedimentoSelecionado = procedimentoSelecionado;
	}

	public ItemLoteAtendimento getItemLoteSelecionado() {
		return itemLoteSelecionado;
	}

	public void setItemLoteSelecionado(ItemLoteAtendimento itemLoteSelecionado) {
		this.itemLoteSelecionado = itemLoteSelecionado;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public List<ItemLoteAtendimento> getItensLotes() {
		return itensLotes;
	}

	public void setItensLotes(List<ItemLoteAtendimento> itensLotes) {
		this.itensLotes = itensLotes;
	}

	public boolean isTemAtendimentoAnterior() {
		return temAtendimentoAnterior;
	}

	public void setTemAtendimentoAnterior(boolean temAtendimentoAnterior) {
		this.temAtendimentoAnterior = temAtendimentoAnterior;
	}

	public List<Atendimento> getAtendimentosAnteriores() {
		return atendimentosAnteriores;
	}

	public List<ProfissionalSelectDTO> getProfissionaisSelect() {
		return profissionaisSelect;
	}

	public ProfissionalSelectDTO getProfissionalSelecionado() {
		return profissionalSelecionado;
	}

	public void setProfissionalSelecionado(ProfissionalSelectDTO profissionalSelecionado) {
		this.profissionalSelecionado = profissionalSelecionado;
	}

	public List<Procedimento> completarProcedimento(String descricao) {
		return this.procedimentos.porNomeAndEspecie(descricao, this.animal.getRaca().getEspecie());
	}

	public void adicionarProcedimento() {

		if (!this.procedimentosSelecionados.contains(this.procedimentoSelecionado)) {
			this.procedimentosSelecionados.add(this.procedimentoSelecionado);
		} else {
			FacesUtil.addErrorMessage("Este procedimento j� foi adicionado!");
		}
	}

	public void removerProcedimento() {
		this.procedimentosSelecionados.remove(this.procedimentoSelecionado);
	}

	public void adicionarLote() {
		
		try {

		if (this.quantidade <= this.itemLoteSelecionado.getLote().getQuantidade()) {

			if (!this.itensLotes.contains(this.itemLoteSelecionado)) {
				this.itemLoteSelecionado.setQuantidade(this.quantidade);
				this.itemLoteSelecionado.getLote()
						.setQuantidade(this.itemLoteSelecionado.getLote().getQuantidade() - this.quantidade);
				this.itemLoteSelecionado.setAtendimento(this.atendimento);
				this.itensLotes.add(this.itemLoteSelecionado);
				limparAposAdicionar();
			} else {
				FacesUtil.addErrorMessage("Este produto já foi adicionado!");
			}
		} else {
			FacesUtil.addErrorMessage("A quantidade informada é maior que o estoque!");
		}
		}catch (NullPointerException e) {
				FacesUtil.addErrorMessage("Seleciona o medicamento antes de adicionar!");
		}
		this.quantidade = null;
	}

	public void removerProduto() {
		if (this.atendimento.getIdAtendimento() != null) {
			this.itemLoteSelecionado.getLote().setQuantidade(
					this.itemLoteSelecionado.getQuantidade() + this.itemLoteSelecionado.getLote().getQuantidade());
			this.loteService.salvar(this.itemLoteSelecionado.getLote());
		}
		this.itensLotes.remove(this.itemLoteSelecionado);
		this.itemLoteSelecionado = null;
		this.itemLoteSelecionado = new ItemLoteAtendimento();
	}

	public List<Lote> completarLote(String descricao) {
		return this.lotes.porNomeProduto(descricao, this.dataAtual);
	}

	public void limparAposAdicionar() {
		this.itemLoteSelecionado = null;
		this.itemLoteSelecionado = new ItemLoteAtendimento();
	}

	public void buscaAtendimentosAnteriores1() {
		try {
			this.atendimentosAnteriores = this.atendimentos.atendimentosPorAnimal(this.animal);
			this.temAtendimentoAnterior = true;

		} catch (NoResultException e) {
			e.printStackTrace();
		}

	}

	private void buscaSolicitacao() {
		try {
			
			for(Solicitacao solicitacao : this.solicitacoes.porCPFList(this.animal.getProprietario().getCpf(),
					Status.AGENDADOCASTRACAO)) {
				
				boolean tem = false;
				
				for(Animal animal : solicitacao.getAnimais()) {
					if(animal.equals(this.animal)) {
						this.solicitacao = solicitacao;
						tem = true;
						break;
					}
					
				}
				
				if(tem) {
					break;
				}
				
			}

		} catch (NoResultException e) {
			e.printStackTrace();
		}
	}

	   public void salvar() {
		   // Validação do profissional selecionado
		   if (this.profissionalSelecionado == null || this.profissionalSelecionado.getIdProfissional() == null) {
			   FacesUtil.addErrorMessage("Selecione um profissional antes de salvar a castração!");
			   return;
		   }
		   
		   if (!this.procedimentosSelecionados.isEmpty()) {
			   System.out.println("[DEBUG] Status do animal ANTES de salvar: " + this.animal.getStatus());
			   // Buscar atendimento existente para este animal e solicitação
			   Atendimento atendimentoExistente = null;
			   if (this.animal != null && this.solicitacao != null) {
				   List<Atendimento> atendimentosDoAnimal = this.atendimentos.atendimentosPorAnimal(this.animal);
				   for (Atendimento at : atendimentosDoAnimal) {
					   if (at.getSolicitacao() != null && at.getSolicitacao().equals(this.solicitacao)
						   && TipoAtendimento.CASTRACAO.equals(at.getTipoAtendimento())) {
						   atendimentoExistente = at;
						   break;
					   }
				   }
			   }

			   Atendimento atendimentoParaSalvar = atendimentoExistente != null ? atendimentoExistente : this.atendimento;

			   // 1. Atualiza o status do animal e salva primeiro
			   this.animal.setStatus(Status.CASTRADO);
			   this.animal.setDataCastracao(this.dataAtendimento);
			   this.animalService.salvar(this.animal);
			   System.out.println("[DEBUG] Status do animal DEPOIS de salvar: " + this.animal.getStatus());

			   // 2. Só depois finaliza a solicitação
			   this.solicitacao.setStatus(Status.FINALIZADO);

			   // 3. Preenche e salva o atendimento
			   atendimentoParaSalvar.setItemLoteAtendimento(this.itensLotes);
			   atendimentoParaSalvar.setProcedimentos(this.procedimentosSelecionados);
			   atendimentoParaSalvar.setData(this.dataAtendimento);
			   atendimentoParaSalvar.setSolicitacao(this.solicitacao);
			   atendimentoParaSalvar.setAnimal(this.animal);
			   atendimentoParaSalvar.setTipoAtendimento(TipoAtendimento.CASTRACAO);
			   
			   // Se um profissional foi selecionado no dropdown, busca o objeto completo
			   if (this.profissionalSelecionado != null && this.profissionalSelecionado.getIdProfissional() != null) {
				   br.gov.pr.guaira.animalys.entity.Profissional profissional = 
					   profissionaisRepo.porId(this.profissionalSelecionado.getIdProfissional());
				   atendimentoParaSalvar.setProfissional(profissional);
			   } else {
				   // Fallback para o usuário logado
				   atendimentoParaSalvar.setProfissional(this.usuarioLogado.getUsuarioLogado().getUsuario().getProfissional());
			   }
			   
			   this.atendimento = this.atendimentoService.salvar(atendimentoParaSalvar);

			   // 4. Força o update do animal novamente após tudo
			   System.out.println("[DEBUG] Status do animal ANTES do salvar FINAL: " + this.animal.getStatus());
			   this.animalService.salvar(this.animal);
			   System.out.println("[DEBUG] Status do animal DEPOIS do salvar FINAL: " + this.animal.getStatus());

			   FacesUtil.addInfoMessage("Atendimento do(a) " + this.animal.getNome() + " realizado com sucesso!");
			   this.alterarAtendimento1 = true;
			   // limpar();
		   } else {
			   FacesUtil.addErrorMessage("Adicione ao menos um procedimento para finalizar o atendimento!");
		   }
	   }
}
