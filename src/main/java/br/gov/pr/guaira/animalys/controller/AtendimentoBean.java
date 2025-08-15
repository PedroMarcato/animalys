package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Atendimento;
import br.gov.pr.guaira.animalys.entity.ItemLoteAtendimento;
import br.gov.pr.guaira.animalys.entity.Lote;
import br.gov.pr.guaira.animalys.entity.Procedimento;
import br.gov.pr.guaira.animalys.entity.Produto;
import br.gov.pr.guaira.animalys.entity.Solicitacao;
import br.gov.pr.guaira.animalys.entity.Status;
import br.gov.pr.guaira.animalys.entity.TipoAtendimento;
import br.gov.pr.guaira.animalys.repository.Animais;
import br.gov.pr.guaira.animalys.repository.Atendimentos;
import br.gov.pr.guaira.animalys.repository.ItensLotes;
import br.gov.pr.guaira.animalys.repository.Lotes;
import br.gov.pr.guaira.animalys.repository.Procedimentos;
import br.gov.pr.guaira.animalys.security.Seguranca;
import br.gov.pr.guaira.animalys.security.UsuarioLogado;
import br.gov.pr.guaira.animalys.service.AnimalService;
import br.gov.pr.guaira.animalys.service.AtendimentoService;
import br.gov.pr.guaira.animalys.service.LoteService;
import br.gov.pr.guaira.animalys.service.SolicitacaoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class AtendimentoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public AtendimentoBean() {
		this.atendimento = new Atendimento();
		this.itensLotes = new ArrayList<>();
		this.itemLoteSelecionado = new ItemLoteAtendimento();
		// this.loteSelecionado = new Lote();
		this.dataComecoDia = Calendar.getInstance();
		this.dataFimDia = Calendar.getInstance();
		this.usuarioLogado = new Seguranca();
	}

	public void inicializar() {

		this.dataAtendimento = Calendar.getInstance();
		this.dataAgendaCastracaoCalendar = Calendar.getInstance();
		this.eventModel = new DefaultScheduleModel();

		this.dataComecoDia.set(Calendar.HOUR, 00);
		this.dataComecoDia.set(Calendar.MINUTE, 01);
		this.dataComecoDia.set(Calendar.SECOND, 01);

		this.dataFimDia.set(Calendar.HOUR, 23);
		this.dataFimDia.set(Calendar.MINUTE, 59);
		this.dataFimDia.set(Calendar.SECOND, 59);

		this.buscarAnimais();

		this.buscaAnimaisAgendadosParaCastracao();

		for (Animal animal : this.animaisAgendadosCastracao) {

			this.eventModel.addEvent(new DefaultScheduleEvent(animal.getNome(),
					animal.getDataAgendaCastracao().getTime(), animal.getDataAgendaCastracao().getTime()));
		}

		try {
			this.atendimento = this.atendimentos.atendimentoPorAnimal(this.animaisDoProprietario.get(0), dataComecoDia,
					dataFimDia);
			System.out.println("TESTE" + this.dataComecoDia.getTime());
			this.dataAgendaCastracao = this.animaisDoProprietario.get(0).getDataAgendaCastracao().getTime();
			this.alterarAtendimento1 = true;
			this.itensLotes = this.itensLotesAtendimento.porAtendimento(this.atendimento);

			this.procedimentoSelecionado = this.procedimentos.porSku("003");

		} catch (NoResultException e) {
			e.printStackTrace();
		}

		this.buscaAtendimentosAnteriores1();

		if (this.animaisDoProprietario.size() > 1) {
			this.temDoisAnimais = true;

			this.atendimento2 = new Atendimento();
			this.itensLotes2 = new ArrayList<>();
			this.itemLoteSelecionado2 = new ItemLoteAtendimento();

			this.dataAtendimento2 = Calendar.getInstance();
			this.dataAgendaCastracaoCalendar2 = Calendar.getInstance();

			try {
				this.atendimento2 = this.atendimentos.atendimentoPorAnimal(this.animaisDoProprietario.get(1),
						dataComecoDia, dataFimDia);

				this.dataAgendaCastracao2 = this.animaisDoProprietario.get(1).getDataAgendaCastracao().getTime();
				this.alterarAtendimento2 = true;
				this.habilitarTab = false;
				this.itensLotes2 = this.itensLotesAtendimento.porAtendimento(this.atendimento2);
			} catch (NoResultException e) {
				e.printStackTrace();
			}

			this.buscaAtendimentosAnteriores2();
		}

	}

	private Atendimento atendimento;
	private Solicitacao solicitacao;
	private ItemLoteAtendimento itemLoteSelecionado;
	private Calendar dataAtendimento;
	private Calendar dataAgendaCastracaoCalendar;
	private Calendar dataComecoDia;
	private Calendar dataFimDia;
	private ScheduleModel eventModel;
	private boolean temDoisAnimais = false;
	private boolean habilitarTab = true;
	private boolean alterarAtendimento1 = false;
	private boolean temAtendimentoAnterior = false;
	private boolean temAtendimentoAnterior2 = false;
	private Date dataAgendaCastracao;
	private Animal animal;
	private Produto produtoSelecionado;
	private Procedimento procedimentoSelecionado;
	private Integer quantidade;
	private List<Lote> lotesCadastrados;
	private List<ItemLoteAtendimento> itensLotes;
	private List<Produto> produtosCadastrados;
	private List<Animal> animaisDoProprietario;
	private List<Animal> animaisAgendadosCastracao;
	private List<Atendimento> atendimentosAnteriores;
	private List<Atendimento> atendimentosAnteriores2;

	@Inject
	private Lotes lotes;
	@Inject
	private Procedimentos procedimentos;
	@Inject
	private AtendimentoService atendimentoService;
	@Inject
	private Animais animais;
	@Inject
	private AnimalService animalService;
	@Inject
	private Atendimentos atendimentos;
	@Inject
	private LoteService loteService;
	@Inject
	private ItensLotes itensLotesAtendimento;
	@Inject
	private SolicitacaoService solicitacaoService;
	@UsuarioLogado
	private Seguranca usuarioLogado;

	public Atendimento getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public List<Lote> getLotesCadastrados() {
		return lotesCadastrados;
	}

	public List<Lote> porNumeroLote() {
		return this.lotes.porProduto(this.produtoSelecionado);
	}

	public void setProcedimentoSelecionado(Procedimento procedimentoSelecionado) {
		this.procedimentoSelecionado = procedimentoSelecionado;
	}

	public Procedimento getProcedimentoSelecionado() {
		return procedimentoSelecionado;
	}

	public List<Produto> getProdutosCadastrados() {
		return produtosCadastrados;
	}

	public List<Animal> getAnimaisDoProprietario() {
		return animaisDoProprietario;
	}

	public List<Atendimento> getAtendimentosAnteriores() {
		return atendimentosAnteriores;
	}

	public void setAtendimentosAnteriores(List<Atendimento> atendimentosAnteriores) {
		this.atendimentosAnteriores = atendimentosAnteriores;
	}

	public void setAnimaisDoProprietario(List<Animal> animaisDoProprietario) {
		this.animaisDoProprietario = animaisDoProprietario;
	}

	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public Date getDataAgendaCastracao() {
		return dataAgendaCastracao;
	}

	public void setDataAgendaCastracao(Date dataAgendaCastracao) {
		this.dataAgendaCastracao = dataAgendaCastracao;
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

	public ItemLoteAtendimento getItemLoteSelecionado() {
		return itemLoteSelecionado;
	}

	public void setItemLoteSelecionado(ItemLoteAtendimento itemLoteSelecionado) {
		this.itemLoteSelecionado = itemLoteSelecionado;
	}

	public boolean isTemDoisAnimais() {
		return temDoisAnimais;
	}

	public void setTemDoisAnimais(boolean temDoisAnimais) {
		this.temDoisAnimais = temDoisAnimais;
	}

	public boolean isHabilitarTab() {
		return habilitarTab;
	}

	public void setHabilitarTab(boolean habilitarTab) {
		this.habilitarTab = habilitarTab;
	}

	public boolean isAlterarAtendimento1() {
		return alterarAtendimento1;
	}

	public void setAlterarAtendimento1(boolean alterarAtendimento1) {
		this.alterarAtendimento1 = alterarAtendimento1;
	}

	public boolean isTemAtendimentoAnterior() {
		return temAtendimentoAnterior;
	}

	public void setTemAtendimentoAnterior(boolean temAtendimentoAnterior) {
		this.temAtendimentoAnterior = temAtendimentoAnterior;
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public void adicionarLote() {

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

	public void salvarPrimeiroAnimal() {
		// Comum a qualquer cenário
		this.atendimento.setItemLoteAtendimento(this.itensLotes);
		this.atendimento.setData(this.dataAtendimento);
		this.atendimento.setTipoAtendimento(TipoAtendimento.CONSULTA);
		this.atendimento.setSolicitacao(this.solicitacao);
		this.atendimento.setAnimal(this.animaisDoProprietario.get(0));
		this.atendimento.setProfissional(this.usuarioLogado.getUsuarioLogado().getUsuario().getProfissional());

		if (this.dataAgendaCastracao != null) {
			// valida a data informada
			if (this.dataAgendaCastracao.before(this.dataAtendimento.getTime())) {
				FacesUtil.addErrorMessage("A data para castração não pode ser anterior à data atual!");
				return;
			}
			this.dataAgendaCastracaoCalendar.setTime(this.dataAgendaCastracao);
			this.animaisDoProprietario.get(0).setStatus(Status.AGENDADOCASTRACAO);
			this.animaisDoProprietario.get(0).setDataAgendaCastracao(this.dataAgendaCastracaoCalendar);

			// Mantém comportamento atual da solicitação quando houver agendamento
			this.solicitacao.setStatus(Status.AGENDADOCASTRACAO);
			this.solicitacao = this.solicitacaoService.salvar(this.solicitacao);
		} else {
			// Sem agendamento -> consulta finalizada
			this.animaisDoProprietario.get(0).setStatus(Status.FINALIZADO);
			this.animaisDoProprietario.get(0).setDataAgendaCastracao(null);

			// Atualiza e salva a solicitação também
			this.solicitacao.setStatus(Status.FINALIZADO);
			this.solicitacao = this.solicitacaoService.salvar(this.solicitacao);
		}

		// Garante persistência do status do animal na tabela animal.animal
		this.animalService.salvar(this.animaisDoProprietario.get(0));

		// Salva o atendimento
		this.atendimento = this.atendimentoService.salvar(this.atendimento);

		FacesUtil.addInfoMessage(
				"Atendimento do(a) " + this.animaisDoProprietario.get(0).getNome() + " realizado com sucesso!");
		this.habilitarTab = false;
		this.alterarAtendimento1 = true;
		this.buscaAnimaisAgendadosParaCastracao();
	}

	public void alterarAtendimento() {
		this.alterarAtendimento1 = false;
	}

	public void limpar() {
		this.itensLotes = new ArrayList<>();
		this.atendimento = new Atendimento();
		this.dataAgendaCastracao = null;

	}

	public void limparAposAdicionar() {
		this.itemLoteSelecionado = null;
		this.itemLoteSelecionado = new ItemLoteAtendimento();
	}

	private void buscarAnimais() {
		this.animaisDoProprietario = this.animais.animaisPorProprietario(this.solicitacao.getProprietario());
	}

	public List<Procedimento> completarProcedimento(String descricao) {
		return this.procedimentos.porNome(descricao);
	}

	public List<Lote> completarLote(String descricao) {
		return this.lotes.porNomeProduto(descricao, this.dataFimDia);
	}

	public void buscaAtendimentosAnteriores1() {
		try {
			this.atendimentosAnteriores = this.atendimentos.atendimentosPorAnimal(this.animaisDoProprietario.get(0));
			this.temAtendimentoAnterior = true;

		} catch (NoResultException e) {
			e.printStackTrace();
		}

	}

	private void buscaAnimaisAgendadosParaCastracao() {
		this.animaisAgendadosCastracao = this.animais.animaisAgendadoCastracao(Status.AGENDADOCASTRACAO);
	}

	/* AQUI COMEÇA A ABA DO SEGUNDO ANIMAL */

	private Atendimento atendimento2;
	private ItemLoteAtendimento itemLoteSelecionado2;
	private ScheduleModel eventModel2;
	private Calendar dataAtendimento2;
	private Calendar dataAgendaCastracaoCalendar2;
	private Date dataAgendaCastracao2;
	private Animal animal2;
	private Produto produtoSelecionado2;
	private Integer quantidade2;
	private boolean alterarAtendimento2;
	private List<Lote> lotesCadastrados2;
	private List<ItemLoteAtendimento> itensLotes2;
	private List<Produto> produtosCadastrados2;

	public Atendimento getAtendimento2() {
		return atendimento2;
	}

	public void setAtendimento2(Atendimento atendimento) {
		this.atendimento2 = atendimento;
	}

	public Animal getAnimal2() {
		return animal2;
	}

	public void setAnimal2(Animal animal) {
		this.animal2 = animal;
	}

	public Produto getProdutoSelecionado2() {
		return produtoSelecionado2;
	}

	public void setProdutoSelecionado2(Produto produtoSelecionado) {
		this.produtoSelecionado2 = produtoSelecionado;
	}

	public List<Lote> getLotesCadastrados2() {
		return lotesCadastrados2;
	}

	public List<Lote> porNumeroLote2() {
		return this.lotes.porProduto(this.produtoSelecionado2);
	}

	public List<Produto> getProdutosCadastrados2() {
		return produtosCadastrados2;
	}

	public Date getDataAgendaCastracao2() {
		return dataAgendaCastracao2;
	}

	public void setDataAgendaCastracao2(Date dataAgendaCastracao) {
		this.dataAgendaCastracao2 = dataAgendaCastracao;
	}

	public Integer getQuantidade2() {
		return quantidade2;
	}

	public void setQuantidade2(Integer quantidade) {
		this.quantidade2 = quantidade;
	}

	public List<ItemLoteAtendimento> getItensLotes2() {
		return itensLotes2;
	}

	public void setItensLotes2(List<ItemLoteAtendimento> itensLotes) {
		this.itensLotes2 = itensLotes;
	}

	public ItemLoteAtendimento getItemLoteSelecionado2() {
		return itemLoteSelecionado2;
	}

	public void setItemLoteSelecionado2(ItemLoteAtendimento itemLoteSelecionado) {
		this.itemLoteSelecionado2 = itemLoteSelecionado;
	}

	public boolean isAlterarAtendimento2() {
		return alterarAtendimento2;
	}

	public void setAlterarAtendimento2(boolean alterarAtendimento2) {
		this.alterarAtendimento2 = alterarAtendimento2;
	}

	public boolean isTemAtendimentoAnterior2() {
		return temAtendimentoAnterior2;
	}

	public void setTemAtendimentoAnterior2(boolean temAtendimentoAnterior2) {
		this.temAtendimentoAnterior2 = temAtendimentoAnterior2;
	}

	public List<Atendimento> getAtendimentosAnteriores2() {
		return atendimentosAnteriores2;
	}

	public void setAtendimentosAnteriores2(List<Atendimento> atendimentosAnteriores2) {
		this.atendimentosAnteriores2 = atendimentosAnteriores2;
	}

	public ScheduleModel getEventModel2() {
		return eventModel2;
	}

	public void setEventModel2(ScheduleModel eventModel2) {
		this.eventModel2 = eventModel2;
	}

	public void adicionarLote2() {

		if (this.quantidade2 <= this.itemLoteSelecionado2.getLote().getQuantidade()) {

			if (!this.itensLotes2.contains(this.itemLoteSelecionado2)) {
				this.itemLoteSelecionado2.setQuantidade(this.quantidade2);
				this.itemLoteSelecionado2.getLote()
						.setQuantidade(this.itemLoteSelecionado2.getLote().getQuantidade() - this.quantidade2);
				this.itemLoteSelecionado2.setAtendimento(this.atendimento2);
				this.itensLotes2.add(this.itemLoteSelecionado2);
				limparAposAdicionar2();
			} else {
				FacesUtil.addErrorMessage("Este produto já foi adicionado!");
			}
		} else {
			FacesUtil.addErrorMessage("A quantidade informada é maior que o estoque!");
		}
		this.quantidade2 = null;
	}

	public void removerProduto2() {

		if (this.atendimento2.getIdAtendimento() != null) {
			this.itemLoteSelecionado2.getLote().setQuantidade(
					this.itemLoteSelecionado2.getQuantidade() + this.itemLoteSelecionado2.getLote().getQuantidade());
			this.loteService.salvar(this.itemLoteSelecionado2.getLote());
		}

		this.itensLotes2.remove(this.itemLoteSelecionado2);
		this.itemLoteSelecionado2 = null;
		this.itemLoteSelecionado2 = new ItemLoteAtendimento();
	}

	public void salvarSegundoAnimal() {
		// Comum a qualquer cenário
		this.atendimento2.setItemLoteAtendimento(this.itensLotes2);
		this.atendimento2.setData(this.dataAtendimento2);
		this.atendimento2.setSolicitacao(this.solicitacao);
		this.atendimento2.setTipoAtendimento(TipoAtendimento.CONSULTA);
		this.atendimento2.setAnimal(this.animaisDoProprietario.get(1));
		this.atendimento2.setProfissional(this.usuarioLogado.getUsuarioLogado().getUsuario().getProfissional());

		if (this.dataAgendaCastracao2 != null && this.dataAgendaCastracao2.getTime() != 0) {
			// valida a data informada
			if (this.dataAgendaCastracao2.before(this.dataAtendimento2.getTime())) {
				FacesUtil.addErrorMessage("A data para castração não pode ser anterior à data atual!");
				return;
			}
			this.dataAgendaCastracaoCalendar2.setTime(this.dataAgendaCastracao2);
			this.animaisDoProprietario.get(1).setStatus(Status.AGENDADOCASTRACAO);
			this.animaisDoProprietario.get(1).setDataAgendaCastracao(this.dataAgendaCastracaoCalendar2);
		} else {
			// Sem agendamento -> consulta finalizada
			this.animaisDoProprietario.get(1).setStatus(Status.FINALIZADO);
			this.animaisDoProprietario.get(1).setDataAgendaCastracao(null);

			// Atualiza e salva a solicitação também
			this.solicitacao.setStatus(Status.FINALIZADO);
			this.solicitacao = this.solicitacaoService.salvar(this.solicitacao);
		}

		// Salva o animal com o status atualizado
		this.animalService.salvar(this.animaisDoProprietario.get(1));

		// Salva o atendimento
		this.atendimento2 = this.atendimentoService.salvar(this.atendimento2);

		FacesUtil.addInfoMessage(
				"Atendimento do(a) " + this.animaisDoProprietario.get(1).getNome() + " realizado com sucesso!");
		this.alterarAtendimento2 = true;
		this.buscaAnimaisAgendadosParaCastracao();
	}

	public void alterarBotao2() {
		this.alterarAtendimento2 = false;
	}

	public void limpar2() {
		this.itensLotes2 = new ArrayList<>();
		this.atendimento2 = new Atendimento();
		this.dataAgendaCastracao2 = null;

	}

	public void limparAposAdicionar2() {
		this.itemLoteSelecionado2 = null;
		this.itemLoteSelecionado2 = new ItemLoteAtendimento();
	}

	public List<Procedimento> completarProcedimento2(String descricao) {
		return this.procedimentos.porNome(descricao);
	}

	public List<Lote> completarLote2(String descricao) {
		return this.lotes.porNomeProduto(descricao, this.dataFimDia);
	}

	public void buscaAtendimentosAnteriores2() {
		try {
			this.atendimentosAnteriores2 = this.atendimentos.atendimentosPorAnimal(this.animaisDoProprietario.get(1));
			this.temAtendimentoAnterior2 = true;
		} catch (NoResultException e) {
			e.printStackTrace();
		}
	}

}
