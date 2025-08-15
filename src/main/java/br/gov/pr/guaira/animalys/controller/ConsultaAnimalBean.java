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
import javax.persistence.NonUniqueResultException;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import br.gov.pr.guaira.animalys.entity.Animal;
import br.gov.pr.guaira.animalys.entity.Atendimento;
import br.gov.pr.guaira.animalys.entity.ItemLoteAtendimento;
import br.gov.pr.guaira.animalys.entity.Lote;
import br.gov.pr.guaira.animalys.entity.Produto;
import br.gov.pr.guaira.animalys.entity.Solicitacao;
import br.gov.pr.guaira.animalys.entity.Status;
import br.gov.pr.guaira.animalys.entity.TipoAtendimento;
import br.gov.pr.guaira.animalys.repository.Animais;
import br.gov.pr.guaira.animalys.repository.Atendimentos;
import br.gov.pr.guaira.animalys.repository.ItensLotes;
import br.gov.pr.guaira.animalys.repository.Lotes;
import br.gov.pr.guaira.animalys.security.Seguranca;
import br.gov.pr.guaira.animalys.security.UsuarioLogado;
import br.gov.pr.guaira.animalys.service.AtendimentoService;
import br.gov.pr.guaira.animalys.service.LoteService;
import br.gov.pr.guaira.animalys.service.SolicitacaoService;
import br.gov.pr.guaira.animalys.util.jsf.FacesUtil;

@Named
@ViewScoped
public class ConsultaAnimalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public ConsultaAnimalBean() {
		this.atendimento = new Atendimento();
		this.itemLoteSelecionado = new ItemLoteAtendimento();
		this.itensLotes = new ArrayList<>();
		this.dataAgendaCastracaoCalendar = Calendar.getInstance();
		this.dataComecoDia = Calendar.getInstance();
		this.dataFimDia = Calendar.getInstance();
		this.event = new DefaultScheduleEvent();
		this.usuarioLogado = new Seguranca();
	}

	private Atendimento atendimento;
	private Animal animalSelecionado;
	private Animal animalSelecionadoSchedule; // Animal selecionado no schedule
	private Solicitacao solicitacao;
	private ItemLoteAtendimento itemLoteSelecionado;
	private Produto produtoSelecionado;
	private Calendar dataAtendimento;
	private Calendar dataAgendaCastracaoCalendar;
	private Calendar dataComecoDia;
	private Calendar dataFimDia;
	private Date dataAgendaCastracao;
	private ScheduleModel eventModel;
	private ScheduleEvent event;
	private Integer quantidade;
	private boolean temAtendimentoAnterior;
	private boolean atendido;
	private List<Animal> animaisAgendadosCastracao;
	private List<Lote> lotesCadastrados;
	private List<Produto> produtosCadastrados;
	private List<ItemLoteAtendimento> itensLotes;
	private List<Atendimento> atendimentosAnteriores;
	@Inject
	private AtendimentoService atendimentoService;
	@Inject
	private LoteService loteService;
	@Inject
	private Animais animais;
	@Inject
	private Lotes lotes;
	@Inject
	private SolicitacaoService solicitacaoService;
	@Inject
	private Atendimentos atendimentos;
	@Inject
	private ItensLotes itensLotesAtendimento;
	@UsuarioLogado
	private Seguranca usuarioLogado;

	public void inicializar() {
		this.dataAtendimento = Calendar.getInstance();
		this.eventModel = new DefaultScheduleModel();
		this.temAtendimentoAnterior = false;
		this.atendido = false;
		this.buscaAnimaisAgendadosParaCastracao();

		this.buscaAtendimentoNoDia();
		this.buscaAtendimentosAnteriores();
	}

	public Atendimento getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
	}

	public Animal getAnimalSelecionado() {
		return animalSelecionado;
	}

	public void setAnimalSelecionado(Animal animalSelecionado) {
		this.animalSelecionado = animalSelecionado;
	}

	public ItemLoteAtendimento getItemLoteSelecionado() {
		return itemLoteSelecionado;
	}

	public void setItemLoteSelecionado(ItemLoteAtendimento itemLoteSelecionado) {
		this.itemLoteSelecionado = itemLoteSelecionado;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
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

	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public boolean isTemAtendimentoAnterior() {
		return temAtendimentoAnterior;
	}

	public void setTemAtendimentoAnterior(boolean temAtendimentoAnterior) {
		this.temAtendimentoAnterior = temAtendimentoAnterior;
	}

	public List<Animal> getAnimaisAgendadosCastracao() {
		return animaisAgendadosCastracao;
	}

	public List<Lote> getLotesCadastrados() {
		return lotesCadastrados;
	}

	public List<Atendimento> getAtendimentosAnteriores() {
		return atendimentosAnteriores;
	}

	public List<Produto> getProdutosCadastrados() {
		return produtosCadastrados;
	}

	public List<Lote> completarLote(String descricao) {
		return this.lotes.porNomeProduto(descricao, this.dataFimDia);
	}

	public List<ItemLoteAtendimento> getItensLotes() {
		return itensLotes;
	}

	public void setItensLotes(List<ItemLoteAtendimento> itensLotes) {
		this.itensLotes = itensLotes;
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public Animal getAnimalSelecionadoSchedule() {
		return animalSelecionadoSchedule;
	}

	public void setAnimalSelecionadoSchedule(Animal animalSelecionadoSchedule) {
		this.animalSelecionadoSchedule = animalSelecionadoSchedule;
	}

	public boolean isAtendido() {
		return atendido;
	}

	public void setAtendido(boolean atendido) {
		this.atendido = atendido;
	}

	public void onEventSelect(SelectEvent selectEvent) {

		event = (ScheduleEvent) selectEvent.getObject();

		try {
			Calendar dataCastracaoSelecionada = Calendar.getInstance();
			dataCastracaoSelecionada.setTime(this.event.getStartDate());
			this.animalSelecionadoSchedule = this.animais.porDataAgendaCastracao(dataCastracaoSelecionada);
		} catch (NoResultException e) {
			e.printStackTrace();
			this.animalSelecionadoSchedule = this.animalSelecionado;
		}
	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent(this.animalSelecionado.getNome(), (Date) selectEvent.getObject(),
				(Date) selectEvent.getObject());

	}

	public void adicionarLote() {

		if (this.quantidade <= this.itemLoteSelecionado.getLote().getQuantidade()) {

			if (!this.confereItens()) {
				this.itemLoteSelecionado.setQuantidade(this.quantidade);
				this.itemLoteSelecionado.getLote()
						.setQuantidade(this.itemLoteSelecionado.getLote().getQuantidade() - this.quantidade);
				this.itemLoteSelecionado.setAtendimento(this.atendimento);
				this.itensLotes.add(this.itemLoteSelecionado);
				this.limparAposAdicionar();
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

	public void limparAposAdicionar() {
		this.itemLoteSelecionado = null;
		this.itemLoteSelecionado = new ItemLoteAtendimento();
	}

	public void salvar() {
		this.atendimento.setItemLoteAtendimento(this.itensLotes);
		this.atendimento.setTipoAtendimento(TipoAtendimento.CONSULTA);
		this.atendimento.setData(this.dataAtendimento);

		// Se houver data de castração, atualiza status
		if (this.animalSelecionado.getDataAgendaCastracao() != null) {
			this.animalSelecionado.setStatus(Status.AGENDADOCASTRACAO);
		} else {
			this.animalSelecionado.setStatus(Status.CONSULTA_ELETIVA_REALIZADA);
			this.animalSelecionado.setDataAgendaCastracao(null);
		}

		this.atendimento.setAnimal(this.animalSelecionado);
		this.atendimento.setSolicitacao(this.solicitacao);
		this.atendimento.setProfissional(this.usuarioLogado.getUsuarioLogado().getUsuario().getProfissional());
		this.atendimentoService.salvar(this.atendimento);

		this.atendido = true;
		this.confereAnimaisAtendidos();
		FacesUtil.addInfoMessage("Atendimento Realizado com Sucesso!");
	}

	private void buscaAnimaisAgendadosParaCastracao() {
		this.animaisAgendadosCastracao = this.animais.animaisAgendadoCastracao(Status.AGENDADOCASTRACAO);

		for (Animal animal : this.animaisAgendadosCastracao) {

			this.eventModel.addEvent(new DefaultScheduleEvent(animal.getNome(),
					animal.getDataAgendaCastracao().getTime(), animal.getDataAgendaCastracao().getTime()));
		}
	}

	private void confereAnimaisAtendidos() {
		List<Animal> animaisDaSolicitacao = this.animais
				.animaisPorProprietario(this.animalSelecionado.getProprietario(), Status.CONSULTA_ELETIVA_AGENDADA);

		if (animaisDaSolicitacao.size() < 1) {
			if (this.animalSelecionado.getDataAgendaCastracao() != null) {
				this.solicitacao.setStatus(Status.AGENDADOCASTRACAO);
			} else {
				this.solicitacao.setStatus(Status.FINALIZADO);
			}
			this.solicitacao = this.solicitacaoService.salvar(this.solicitacao);
		}
	}

	private void buscaAtendimentoNoDia() {
		this.dataComecoDia.set(Calendar.HOUR_OF_DAY, 0);
		this.dataComecoDia.set(Calendar.MINUTE, 1);
		this.dataComecoDia.set(Calendar.SECOND, 1);

		this.dataFimDia.set(Calendar.HOUR_OF_DAY, 23);
		this.dataFimDia.set(Calendar.MINUTE, 59);
		this.dataFimDia.set(Calendar.SECOND, 59);

		try {
			this.atendimento = this.atendimentos.atendimentoPorAnimal(this.animalSelecionado, dataComecoDia,
					dataFimDia);

			// Proteção caso a data de castração seja null
			if (this.animalSelecionado.getDataAgendaCastracao() != null) {
				this.dataAgendaCastracao = this.animalSelecionado.getDataAgendaCastracao().getTime();
			} else {
				this.dataAgendaCastracao = null;
			}

			this.itensLotes = this.itensLotesAtendimento.porAtendimento(this.atendimento);
		} catch (NoResultException e) {
			e.printStackTrace();
		} catch (NonUniqueResultException e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Este animal já foi atendido!");
			this.atendido = true;
		}
	}

	private void buscaAtendimentosAnteriores() {
		try {
			this.atendimentosAnteriores = this.atendimentos.atendimentosPorAnimal(this.animalSelecionado);
			this.temAtendimentoAnterior = true;
		} catch (NoResultException e) {
			e.printStackTrace();
		}
	}

	public void adicionaCastracao() {

		if (this.animalSelecionado.getDataAgendaCastracao() == null) {

			this.eventModel.addEvent(event);

			this.dataAgendaCastracaoCalendar.setTime(this.event.getStartDate());
			this.animalSelecionado.setDataAgendaCastracao(this.dataAgendaCastracaoCalendar);

		} else {
			FacesUtil.addErrorMessage("A data de castração já foi informada para este animal!");
		}
	}

	private boolean confereItens() {

		boolean retorno = false;

		for (ItemLoteAtendimento item : this.itensLotes) {

			if (item.getLote().equals(this.itemLoteSelecionado.getLote())) {
				return true;
			} else {
				return false;
			}

		}

		return retorno;

	}

}
