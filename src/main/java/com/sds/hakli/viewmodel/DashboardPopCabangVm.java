package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Color;
import org.zkoss.chart.Legend;
import org.zkoss.chart.Point;
import org.zkoss.chart.Series;
import org.zkoss.chart.YAxis;
import org.zkoss.chart.model.CategoryModel;
import org.zkoss.chart.model.DefaultCategoryModel;
import org.zkoss.chart.plotOptions.PiePlotOptions;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.domain.BranchTop;
import com.sds.hakli.domain.Muser;

public class DashboardPopCabangVm {

	private Session session = Sessions.getCurrent();
	private Muser oUser;

	private TanggotaDAO anggotaDao = new TanggotaDAO();

	private long totalpop;
	private BranchTop obj;
	private List<BranchTop> objList = new ArrayList<>();
	
	@Wire
	private Grid grid;
	@Wire
	private Charts chart;
	@Wire
	private Window winPopCabang;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") BranchTop obj) {
		Selectors.wireComponents(view, this, false);
		
		oUser = (Muser) session.getAttribute("oUser");
		this.obj = obj;
		
		grid.setRowRenderer(new RowRenderer<BranchTop>() {

			@Override
			public void render(Row row, BranchTop data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index+1)));
				row.getChildren().add(new Label(data.getName()));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getTotal())));
				
				totalpop += data.getTotal();
				BindUtils.postNotifyChange(DashboardPopCabangVm.this, "totalpop");
			}
		});

		doBranchTop();
		doChart();
	}

	public void doBranchTop() {
		try {
			totalpop = 0;
			objList = anggotaDao.listBranchByProv(obj.getId());
			grid.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doChart() {
		try {
			CategoryModel model = new DefaultCategoryModel();
			for (BranchTop obj : objList) {
				 model.setValue(obj.getName(), obj.getName(), obj.getTotal());
			}
	        chart.setSubtitle("Provinsi " + obj.getName());
			chart.setModel(model);
	        chart.getXAxis().setTitle("");
	        
	        YAxis yAxis = chart.getYAxis();
	        yAxis.setMin(0);
	        yAxis.setTitle("Populasi");
	        yAxis.getTitle().setAlign("high");
	        yAxis.getLabels().setOverflow("justify");
	        
	        //chart.getTooltip().setValueSuffix(" millions");
	        
	        chart.getPlotOptions().getBar().getDataLabels().setEnabled(true);
	        
//	        Legend legend = chart.getLegend();
//	        legend.setLayout("vertical");
//	        legend.setAlign("right");
//	        legend.setVerticalAlign("top");
//	        legend.setX(-40);
//	        legend.setY(80);
//	        legend.setFloating(true);
//	        legend.setBorderWidth(1);
//	        legend.setBackgroundColor("#FFFFFF");
//	        legend.setShadow(true);
	        
	        chart.getCredits().setEnabled(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doBack() {
		Component comp = winPopCabang.getParent();
		comp.getChildren().clear();
		Executions.createComponents("/view/dashboard.zul", comp, null);
	}

	public long getTotalpop() {
		return totalpop;
	}

	public void setTotalpop(long totalpop) {
		this.totalpop = totalpop;
	}

}
