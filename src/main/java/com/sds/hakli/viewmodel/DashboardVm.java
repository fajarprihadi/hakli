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
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.chart.Charts;
import org.zkoss.chart.YAxis;
import org.zkoss.chart.model.CategoryModel;
import org.zkoss.chart.model.DefaultCategoryModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
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
import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.domain.BranchTop;

public class DashboardVm {

	private TanggotaDAO anggotaDao = new TanggotaDAO();
	private Tp2kbDAO p2kbDao = new Tp2kbDAO();

	private long totalpop;
	private String totalanggota;
	private String totalkomisip2kb;
	private String totaltimp2kb;
	private String totalregver;
	private List<BranchTop> objList = new ArrayList<>();
	private List<BranchTop> objListTopTen = new ArrayList<>();

	@Wire
	private Window winDashboard;
	@Wire
	private Grid grid;
	@Wire
	private Grid gridPop;
	@Wire
	private Charts chart;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);

		grid.setRowRenderer(new RowRenderer<BranchTop>() {

			@Override
			public void render(Row row, BranchTop data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index+1)));
				row.getChildren().add(new Label(data.getName()));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getTotal())));
			}
		});
		
		gridPop.setRowRenderer(new RowRenderer<BranchTop>() {

			@Override
			public void render(Row row, BranchTop data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index+1)));
				row.getChildren().add(new Label(data.getName()));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getTotal())));
				Button btDetail = new Button();
				btDetail.setIconSclass("z-icon-eye");
				btDetail.setSclass("btn btn-primary btn-sm");
				btDetail.setAutodisable("self");
				btDetail.setTooltiptext("Lihat Populasi Cabang");
				btDetail.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						Component comParent = winDashboard.getParent();
						comParent.getChildren().clear();
						Executions.createComponents("/view/dashboardpopcabang.zul", comParent, map);
					}
				});
				row.getChildren().add(btDetail);
				
				totalpop += data.getTotal();
				BindUtils.postNotifyChange(DashboardVm.this, "totalpop");
			}
		});

		doCount();
		doBranchTop();
		doChart();
	}

	@NotifyChange({"totalanggota", "totaltimp2kb", "totalkomisip2kb", "totalregver"})
	public void doCount() {
		try {
			totalanggota = NumberFormat.getInstance().format(anggotaDao.pageCount("statusreg = '3'"));
			totalkomisip2kb = NumberFormat.getInstance().format(p2kbDao.getSumWaitKomisiP2KB("0=0"));
			totaltimp2kb = NumberFormat.getInstance().format(p2kbDao.getSumWaitTimP2KB("0=0"));
			totalregver = NumberFormat.getInstance().format(anggotaDao.pageCount("statusreg = '1'"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doBranchTop() {
		try {
			totalpop = 0;
			objList = anggotaDao.listBranchTop();

			objListTopTen = anggotaDao.listTop10();
			grid.setModel(new ListModelList<>(objListTopTen));
			gridPop.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void doChart() {
		try {
			CategoryModel model = new DefaultCategoryModel();
			for (BranchTop obj : objList) {
				 model.setValue(obj.getName(), obj.getName(), obj.getTotal());
			}
	        
			chart.setModel(model);
	        chart.getXAxis().setTitle("");
	        
	        YAxis yAxis = chart.getYAxis();
	        yAxis.setMin(0);
	        yAxis.setTitle("Populasi");
	        yAxis.getTitle().setAlign("high");
	        yAxis.getLabels().setOverflow("justify");
	        
	        chart.getPlotOptions().getBar().getDataLabels().setEnabled(true);
	        
	        chart.getCredits().setEnabled(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doViewKomisiP2kb() {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("type", "komisi");
			Window win = (Window) Executions
					.createComponents("/view/p2kb/p2kbsumwaittim.zul", null, map);
			win.setClosable(true);
			win.setWidth("85%");
			win.doModal();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doViewTimP2kb() {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("type", "tim");
			Window win = (Window) Executions
					.createComponents("/view/p2kb/p2kbsumwaittim.zul", null, map);
			win.setClosable(true);
			win.setWidth("85%");
			win.doModal();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doViewPendingReg() {
		try {
			Window win = (Window) Executions
					.createComponents("/view/sumpendingreg.zul", null, null);
			win.setClosable(true);
			win.setWidth("85%");
			win.doModal();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getTotalanggota() {
		return totalanggota;
	}

	public void setTotalanggota(String totalanggota) {
		this.totalanggota = totalanggota;
	}

	public long getTotalpop() {
		return totalpop;
	}

	public void setTotalpop(long totalpop) {
		this.totalpop = totalpop;
	}

	public String getTotalkomisip2kb() {
		return totalkomisip2kb;
	}

	public void setTotalkomisip2kb(String totalkomisip2kb) {
		this.totalkomisip2kb = totalkomisip2kb;
	}

	public String getTotaltimp2kb() {
		return totaltimp2kb;
	}

	public void setTotaltimp2kb(String totaltimp2kb) {
		this.totaltimp2kb = totaltimp2kb;
	}

	public String getTotalregver() {
		return totalregver;
	}

	public void setTotalregver(String totalregver) {
		this.totalregver = totalregver;
	}

}
