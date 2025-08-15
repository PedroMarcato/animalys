package br.gov.pr.guaira.animalys.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

import com.ibm.icu.text.SimpleDateFormat;

import br.gov.pr.guaira.animalys.entity.Sexo;
import br.gov.pr.guaira.animalys.entity.Status;
import br.gov.pr.guaira.animalys.entity.TipoProduto;
import br.gov.pr.guaira.animalys.repository.Animais;
import br.gov.pr.guaira.animalys.repository.ItensLotes;
import br.gov.pr.guaira.animalys.repository.Solicitacoes;
import br.gov.pr.guaira.animalys.repository.TiposProdutos;

@Named
@ViewScoped
public class DashBoardBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public DashBoardBean() {
		this.sdf = new SimpleDateFormat("MM");
		this.mesPorExtenso = new SimpleDateFormat("MMMM", local);
		this.local = new Locale("pt", "BR");
	}

	private BarChartModel barModel2;
	private DonutChartModel donutModel;
	private PieChartModel pieModel;
	private Calendar dataAtualMacho;
	private Calendar dataAtualFemea;
	private SimpleDateFormat sdf;
	private SimpleDateFormat mesPorExtenso;
	private Locale local;

	@Inject
	private Animais animais;
	@Inject
	private Solicitacoes solicitacoes;
	@Inject
	private TiposProdutos tiposProdutos;
	@Inject
	private ItensLotes itensLotes;

	public void inicializar() {
		this.dataAtualMacho = Calendar.getInstance();
		this.dataAtualFemea = Calendar.getInstance();
		this.createBarModel2();
		this.createDonutModel();
		this.createPieModel();
	}

	public BarChartModel getBarModel2() {
		return barModel2;
	}

	public void setBarModel2(BarChartModel barModel2) {
		this.barModel2 = barModel2;
	}
	
	

	public DonutChartModel getDonutModel() {
		return donutModel;
	}

	public void setDonutModel(DonutChartModel donutModel) {
		this.donutModel = donutModel;
	}
	
	public PieChartModel getPieModel() {
		return pieModel;
	}

	public void setPieModel(PieChartModel pieModel) {
		this.pieModel = pieModel;
	}

	public void createBarModel2() {
		barModel2 = new BarChartModel();
		ChartData data = new ChartData();
		List<String> labels = new ArrayList<>();

		BarChartDataSet barDataSet = new BarChartDataSet();
		barDataSet.setLabel("Macho");
		barDataSet.setBackgroundColor("rgba(69, 157, 231, 0.9)");
		barDataSet.setBorderColor("rgb(65,105,225)");
		barDataSet.setBorderWidth(1);
		List<Number> values = new ArrayList<>();

		this.dataAtualMacho.add(Calendar.MONTH, -6);

		for (int i = 1; i <= 6; i++) {
			this.dataAtualMacho.add(Calendar.MONTH, 1);

			values.add(this.animais.animaisPorMes(sdf.format(this.dataAtualMacho.getTime()), Sexo.MACHO).size());

			if (!labels.contains(this.colocaMes(this.dataAtualMacho))) {
				labels.add(this.colocaMes(this.dataAtualMacho));
			}
		}

		barDataSet.setData(values);

		BarChartDataSet barDataSet2 = new BarChartDataSet();
		barDataSet2.setLabel("Fêmea");
		barDataSet2.setBackgroundColor("rgba(255, 99, 132, 0.2)");
		barDataSet2.setBorderColor("rgb(255, 159, 64)");
		barDataSet2.setBorderWidth(1);
		List<Number> values2 = new ArrayList<>();

		this.dataAtualFemea.add(Calendar.MONTH, -6);

		for (int i = 1; i <= 6; i++) {
			this.dataAtualFemea.add(Calendar.MONTH, 1);
			values2.add(this.animais.animaisPorMes(sdf.format(this.dataAtualFemea.getTime()), Sexo.FEMEA).size());

			if (!labels.contains(this.colocaMes(this.dataAtualFemea))) {
				labels.add(this.colocaMes(this.dataAtualFemea));
			}
		}

		barDataSet2.setData(values2);

		data.addChartDataSet(barDataSet);
		data.addChartDataSet(barDataSet2);
		data.setLabels(labels);

		barModel2.setData(data);
	}
	
	  public void createDonutModel() {
	        donutModel = new DonutChartModel();
	        ChartData data = new ChartData();
	         
	        DonutChartDataSet dataSet = new DonutChartDataSet();
	        List<Number> values = new ArrayList<>();
	        
	        values.add(this.solicitacoes.porStatus(Status.SOLICITADO));
	        values.add(this.solicitacoes.porStatus(Status.CONSULTA_ELETIVA_AGENDADA));
	        values.add(this.animais.animaisAgendadoCastracao(Status.AGENDADOCASTRACAO).size());	        
	        values.add(this.solicitacoes.porStatus(Status.CANCELADO));
	        
	        dataSet.setData(values);
	         
	        List<String> bgColors = new ArrayList<>();
	        bgColors.add(gerarCorAleatoriamente());
	        bgColors.add(gerarCorAleatoriamente());
	        bgColors.add(gerarCorAleatoriamente());
	        bgColors.add(gerarCorAleatoriamente());
	        dataSet.setBackgroundColor(bgColors);
	         
	        data.addChartDataSet(dataSet);
	        List<String> labels = new ArrayList<>();
	        labels.add(Status.SOLICITADO.getDescricao());
	        labels.add(Status.CONSULTA_ELETIVA_AGENDADA.getDescricao());
	        labels.add(Status.AGENDADOCASTRACAO.getDescricao());
	        labels.add(Status.CANCELADO.getDescricao());
	        data.setLabels(labels);
	         
	        donutModel.setData(data);
	    }
	  
	  private void createPieModel() {
	        pieModel = new PieChartModel();
	        ChartData data = new ChartData();
	         
	        PieChartDataSet dataSet = new PieChartDataSet();
	        List<Number> values = new ArrayList<>();
              
	        List<String> bgColors = new ArrayList<>();     	        
	        List<String> labels = new ArrayList<>();
        
	        for(TipoProduto tp : this.tiposProdutos.tiposCadastrados()) {      	
	        	labels.add(tp.getDescricao());
	        	bgColors.add(gerarCorAleatoriamente());
	        	values.add(this.itensLotes.porTipoProduto(tp).size());	        	
	        }
	        
	        dataSet.setBackgroundColor(bgColors);
	        dataSet.setData(values);
	        data.addChartDataSet(dataSet);
	        data.setLabels(labels);
	         
	        pieModel.setData(data);
	    }
	     

	private String colocaMes(Calendar data) {

		return this.mesPorExtenso.format(data.getTime()).toUpperCase();
	}
	
	private String gerarCorAleatoriamente() {
		Random randColor = new Random();
		int r = randColor.nextInt(256);
		int g = randColor.nextInt(256);
		int b = randColor.nextInt(256);

		return "rgb(" + r + ", " + g + ", " + b + ")";
	}

}
