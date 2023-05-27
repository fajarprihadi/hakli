package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.chart.AxisTitle;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Color;
import org.zkoss.chart.Legend;
import org.zkoss.chart.Point;
import org.zkoss.chart.Series;
import org.zkoss.chart.Tooltip;
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
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.domain.BranchTop;
import com.sds.hakli.domain.Mcabang;
import com.sds.hakli.domain.Muser;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Veventamount;
import com.sds.hakli.model.TanggotaListModel;
import com.sds.utils.AppUtils;

public class DashboardKabVm {

	private Session session = Sessions.getCurrent();
	private Tanggota oUser;

	private TanggotaDAO anggotaDao = new TanggotaDAO();
	
	private TanggotaListModel modelAktif;
	private int pageStartNumberAktif;
	private int pageTotalSizeAktif;
	private boolean needsPageUpdateAktif;
	private String filterAktif;
	
	private TanggotaListModel modelNonAktif;
	private int pageStartNumberNonAktif;
	private int pageTotalSizeNonAktif;
	private boolean needsPageUpdateNonAktif;
	private String filterNonAktif;

	private long totalpop;
	private String totalanggota;
	private String totalaktif;
	private String totalnonaktif;
	private List<BranchTop> objList = new ArrayList<>();
	
	private String namaaktif;
	private String namanonaktif;
	
	private SimpleDateFormat datelocalFormatter = new SimpleDateFormat("dd-MM-yyyy");

	@Wire
	private Window winDashboardProv;
	@Wire
	private Grid gridAktif;
	@Wire
	private Grid gridNonAktif;
	@Wire
	private Charts chart;
	@Wire
	private Paging pagingAktif;
	@Wire
	private Paging pagingNonAktif;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);

		oUser = (Tanggota) session.getAttribute("anggota");
		
		pagingAktif.addEventListener("onPaging", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				PagingEvent pe = (PagingEvent) event;
				pageStartNumberAktif = pe.getActivePage();
				refreshModelAktif(pageStartNumberAktif);
			}
		});
		
		pagingNonAktif.addEventListener("onPaging", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				PagingEvent pe = (PagingEvent) event;
				pageStartNumberNonAktif = pe.getActivePage();
				refreshModelNonAktif(pageStartNumberNonAktif);
			}
		});
		
		gridAktif.setRowRenderer(new RowRenderer<Tanggota>() {

			@Override
			public void render(Row row, Tanggota data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf((AppUtils.PAGESIZE * pageStartNumberAktif) + index + 1)));
				row.getChildren().add(new Label(data.getNama()));
				row.getChildren().add(new Label(data.getNoanggota()));
				row.getChildren().add(new Label(data.getNostr()));
				row.getChildren().add(new Label(data.getPeriodekta() != null ? datelocalFormatter.format(data.getPeriodekta()) : ""));
				Button btView = new Button();
				btView.setIconSclass("z-icon-eye");
				btView.setSclass("btn btn-primary btn-sm");
				btView.setAutodisable("self");
				btView.setTooltiptext("Detail");
				btView.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						map.put("acttype", "view");
						Window win = (Window) Executions
								.createComponents("/view/anggota/anggotaedit.zul", null, map);
						win.setClosable(true);
						win.setWidth("98%");
						win.doModal();
					}
				});
				
				row.getChildren().add(btView);
			}
		});
		
		gridNonAktif.setRowRenderer(new RowRenderer<Tanggota>() {

			@Override
			public void render(Row row, Tanggota data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf((AppUtils.PAGESIZE * pageStartNumberNonAktif) + index + 1)));
				row.getChildren().add(new Label(data.getNama()));
				row.getChildren().add(new Label(data.getNoanggota()));
				row.getChildren().add(new Label(data.getNostr()));
				row.getChildren().add(new Label(data.getPeriodekta() != null ? datelocalFormatter.format(data.getPeriodekta()) : ""));
				Button btView = new Button();
				btView.setIconSclass("z-icon-eye");
				btView.setSclass("btn btn-primary btn-sm");
				btView.setAutodisable("self");
				btView.setTooltiptext("Detail");
				btView.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						map.put("acttype", "view");
						Window win = (Window) Executions
								.createComponents("/view/anggota/anggotaedit.zul", null, map);
						win.setClosable(true);
						win.setWidth("98%");
						win.doModal();
					}
				});
				
				row.getChildren().add(btView);
			}
		});
		
		doSearchAktif();
		doSearchNonAktif();
		doCount();
		doChart();
	}
	
	@NotifyChange("pageTotalSizeAktif")
	public void refreshModelAktif(int activePage) {
		pagingAktif.setPageSize(AppUtils.PAGESIZE);
		modelAktif = new TanggotaListModel(activePage, AppUtils.PAGESIZE, filterAktif, "nama");
		if (needsPageUpdateAktif) {
			pageTotalSizeAktif = modelAktif.getTotalSize(filterAktif);
			needsPageUpdateAktif = false;
		}
		pagingAktif.setTotalSize(pageTotalSizeAktif);
		gridAktif.setModel(modelAktif);
	}
	
	@NotifyChange("pageTotalSizeNonAktif")
	public void refreshModelNonAktif(int activePage) {
		pagingNonAktif.setPageSize(AppUtils.PAGESIZE);
		modelNonAktif = new TanggotaListModel(activePage, AppUtils.PAGESIZE, filterNonAktif, "nama");
		if (needsPageUpdateNonAktif) {
			pageTotalSizeNonAktif = modelNonAktif.getTotalSize(filterNonAktif);
			needsPageUpdateNonAktif = false;
		}
		pagingNonAktif.setTotalSize(pageTotalSizeNonAktif);
		gridNonAktif.setModel(modelNonAktif);
	}

	@NotifyChange("*")
	public void doCount() {
		try {
			totalanggota = NumberFormat.getInstance().format((pageTotalSizeAktif + pageTotalSizeNonAktif));
			totalaktif = NumberFormat.getInstance().format(pageTotalSizeAktif);
			totalnonaktif = NumberFormat.getInstance().format(pageTotalSizeNonAktif);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("pageTotalSizeAktif")
	public void doSearchAktif() {
		filterAktif = "statusreg = '" + AppUtils.STATUS_ANGGOTA_REG_ACTIVE + "' and mcabangfk = " + oUser.getMcabang().getMcabangpk() + " and periodekta >= date(now())";
		
		if (namaaktif != null && namaaktif.trim().length() > 0)
			filterAktif += " and upper(nama) like '%" + namaaktif.trim().toUpperCase() + "%'" ;
		
		needsPageUpdateAktif = true;
		pageStartNumberAktif = 0;
		refreshModelAktif(pageStartNumberAktif);
	}
	
	@Command
	@NotifyChange("pageTotalSizeNonAktif")
	public void doSearchNonAktif() {
		filterNonAktif = "statusreg = '" + AppUtils.STATUS_ANGGOTA_REG_ACTIVE + "' and mcabangfk = " + oUser.getMcabang().getMcabangpk() + " and periodekta < date(now())";
		
		if (namanonaktif != null && namanonaktif.trim().length() > 0)
			filterNonAktif += " and upper(nama) like '%" + namanonaktif.trim().toUpperCase() + "%'" ;
		
		needsPageUpdateNonAktif = true;
		pageStartNumberNonAktif = 0;
		refreshModelNonAktif(pageStartNumberNonAktif);
	}

	public void doChart() {
		try {
			chart.setSubtitle("Cabang " + oUser.getMcabang().getCabang());
			
			CategoryModel model = new DefaultCategoryModel();
			model.setValue("Anggota Aktif", "Anggota", Integer.parseInt(totalaktif));
	        model.setValue("Anggota Non Aktif", "Anggota", Integer.parseInt(totalnonaktif));
			
	        chart.setModel(model);
	        
	        chart.getXAxis().setCrosshair(true);
	        
	        AxisTitle title = chart.getYAxis().getTitle();
	        title.setUseHTML(true);
	        title.setText("Jumlah Anggota");
	        
	        Tooltip tooltip = chart.getTooltip();
	        tooltip.setHeaderFormat("<span style=\"font-size:10px\">{point.key}</span><table>");
	        tooltip.setPointFormat("<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>"
	            + "<td style=\"padding:0\"><b>{point.y:.1f}</b></td></tr>");
	        tooltip.setFooterFormat("</table>");
	        tooltip.setShared(true);
	        tooltip.setUseHTML(true);
	        
	        chart.getPlotOptions().getColumn().setPointPadding(0.2);
	        chart.getPlotOptions().getColumn().setBorderWidth(0);
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

	public int getPageTotalSizeAktif() {
		return pageTotalSizeAktif;
	}

	public void setPageTotalSizeAktif(int pageTotalSizeAktif) {
		this.pageTotalSizeAktif = pageTotalSizeAktif;
	}

	public int getPageTotalSizeNonAktif() {
		return pageTotalSizeNonAktif;
	}

	public void setPageTotalSizeNonAktif(int pageTotalSizeNonAktif) {
		this.pageTotalSizeNonAktif = pageTotalSizeNonAktif;
	}

	public String getTotalaktif() {
		return totalaktif;
	}

	public void setTotalaktif(String totalaktif) {
		this.totalaktif = totalaktif;
	}

	public String getTotalnonaktif() {
		return totalnonaktif;
	}

	public void setTotalnonaktif(String totalnonaktif) {
		this.totalnonaktif = totalnonaktif;
	}

	public String getNamaaktif() {
		return namaaktif;
	}

	public void setNamaaktif(String namaaktif) {
		this.namaaktif = namaaktif;
	}

	public String getNamanonaktif() {
		return namanonaktif;
	}

	public void setNamanonaktif(String namanonaktif) {
		this.namanonaktif = namanonaktif;
	}

}
