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
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.domain.BranchTop;
import com.sds.hakli.domain.Tanggota;

public class DashboardProvVm {

	private Session session = Sessions.getCurrent();
	private Tanggota oUser;

	private TanggotaDAO anggotaDao = new TanggotaDAO();
	private Tp2kbDAO timp2kbDao = new Tp2kbDAO();

	private long totalpop;
	private String totalanggota;
	private String totaltimp2kb;
	private String totalregver;
	private List<BranchTop> objList = new ArrayList<>();

	@Wire
	private Window winDashboardProv;
	@Wire
	private Grid grid;
	@Wire
	private Charts chart;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);

		oUser = (Tanggota) session.getAttribute("anggota");

		grid.setRowRenderer(new RowRenderer<BranchTop>() {

			@Override
			public void render(Row row, BranchTop data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index+1)));
				row.getChildren().add(new Label(data.getName()));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getTotal())));
				
				totalpop += data.getTotal();
				BindUtils.postNotifyChange(DashboardProvVm.this, "totalpop");
			}
		});
		
		doCount();
		doBranchTop();
		doChart();
	}

	@NotifyChange({"totalanggota", "totaltimp2kb", "totalregver"})
	public void doCount() {
		try {
			totalanggota = NumberFormat.getInstance().format(anggotaDao.pageCount("statusreg = '3' and mprovfk = " + oUser.getMcabang().getMprov().getMprovpk()));
			totaltimp2kb = NumberFormat.getInstance().format(timp2kbDao.getSumWaitTimP2KB("mprovfk = " + oUser.getMcabang().getMprov().getMprovpk()));
			totalregver = NumberFormat.getInstance().format(anggotaDao.pageCount("statusreg = '1' and mprovfk = " + oUser.getMcabang().getMprov().getMprovpk()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doBranchTop() {
		try {
			totalpop = 0;
			objList = anggotaDao.listBranchByProv(oUser.getMcabang().getMprov().getMprovpk());
			grid.setModel(new ListModelList<>(objList));
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
	        chart.setSubtitle("Provinsi " + oUser.getMcabang().getMprov().getProvname());
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
	public void doViewTimP2kb() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("mprovpk", oUser.getMcabang().getMprov().getMprovpk());
			map.put("provname", oUser.getMcabang().getMprov().getProvname());
			map.put("type", "tim");
			Window win = (Window) Executions
					.createComponents("/view/p2kb/p2kbsumwaittimkab.zul", null, map);
			win.setClosable(true);
			win.setWidth("80%");
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
