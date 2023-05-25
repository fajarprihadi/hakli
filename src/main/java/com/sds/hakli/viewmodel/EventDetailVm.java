package com.sds.hakli.viewmodel;

import java.text.SimpleDateFormat;
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
import org.zkoss.chart.AxisTitle;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Tooltip;
import org.zkoss.chart.model.CategoryModel;
import org.zkoss.chart.model.DefaultCategoryModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlNativeComponent;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TeventDAO;
import com.sds.hakli.dao.TeventregDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tevent;
import com.sds.hakli.domain.Teventreg;
import com.sds.hakli.domain.Veventamount;
import com.sds.hakli.model.TanggotaListModel;
import com.sds.hakli.model.TeventregListModel;
import com.sds.utils.AppUtils;

public class EventDetailVm {

	private TeventDAO oDao = new TeventDAO();
	private TeventregDAO eventregDao = new TeventregDAO();
	private TeventregListModel model;
	private Tevent obj;
	
	private int pageStartNumber;
	private int pageTotalSize;
	private boolean needsPageUpdate;
	private String filter;
	private String nama;
	private String email;
	private String vano;
	private String status;
	
	private SimpleDateFormat datetimeLocalFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	@Wire
	private Window winEventDetail;
	@Wire
	private Paging paging;
	@Wire
	private Grid grid;
	@Wire
	private Charts chart;
	@Wire
	private Combobox cbStatus;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tevent obj) {
		Selectors.wireComponents(view, this, false);
		try {
			this.obj = obj;
			
			if (paging != null) {
				paging.addEventListener("onPaging", new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						PagingEvent pe = (PagingEvent) event;
						pageStartNumber = pe.getActivePage();
						refreshModel(pageStartNumber);
					}
				});
			}
			
			grid.setRowRenderer(new RowRenderer<Teventreg>() {

				@Override
				public void render(Row row, Teventreg data, int index) throws Exception {
					row.getChildren().add(new Label(String.valueOf((AppUtils.PAGESIZE * pageStartNumber) + index + 1)));
					row.getChildren().add(new Label(data.getTanggota().getNama()));
					row.getChildren().add(new Label(data.getTanggota().getEmail()));
					row.getChildren().add(new Label(data.getVacreatedat() != null ? datetimeLocalFormatter.format(data.getVacreatedat()) : ""));
					row.getChildren().add(new Label(data.getVano()));
					row.getChildren().add(new Label(data.getVaexpdate() != null ? datetimeLocalFormatter.format(data.getVaexpdate()) : ""));
					row.getChildren().add(new Label(data.getIspaid().equals("Y") ? "Sudah Dibayar" : "Belum Dibayar"));
					row.getChildren().add(new Label(data.getPaidat() != null ? datetimeLocalFormatter.format(data.getPaidat()) : ""));
					Button btProcess = new Button();
					btProcess.setIconSclass("z-icon-eye");
					btProcess.setSclass("btn btn-primary btn-sm");
					btProcess.setAutodisable("self");
					btProcess.setTooltiptext("Detail");
					btProcess.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("obj", data.getTanggota());
							map.put("acttype", "view");
							Window win = (Window) Executions
									.createComponents("/view/anggota/anggotaedit.zul", null, map);
							win.setClosable(true);
							win.setWidth("98%");
							win.doModal();
						}
					});
					row.getChildren().add(btProcess);
				}
			});
			
			doReset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@NotifyChange("pageTotalSize")
	public void refreshModel(int activePage) {
		paging.setPageSize(AppUtils.PAGESIZE);
		model = new TeventregListModel(activePage, AppUtils.PAGESIZE, filter, "teventregpk desc");
		if (needsPageUpdate) {
			pageTotalSize = model.getTotalSize(filter);
			needsPageUpdate = false;
		}
		paging.setTotalSize(pageTotalSize);
		grid.setModel(model);
	}
	
	@Command
	@NotifyChange("pageTotalSize")
	public void doSearch() {
		filter = "teventfk = " + obj.getTeventpk();
		if (nama != null && nama.trim().length() > 0)
			filter += " and upper(nama) like '%" + nama.trim().toUpperCase() + "%'";
		if (email != null && email.trim().length() > 0)
			filter += " and upper(email) like '%" + email.trim().toUpperCase() + "%'";
		if (vano != null && vano.trim().length() > 0)
			filter += " and teventreg.vano like '%" + vano.trim() + "%'";
		if (status != null && status.trim().length() > 0)
			filter += " and ispaid = '" + status + "'";
		needsPageUpdate = true;
		pageStartNumber = 0;
		refreshModel(pageStartNumber);
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		nama = null;
		email = null;
		vano = null;
		status = null;
		cbStatus.setValue(null);
		doSearch();
		doChart();
	}
	
	public void doChart() {
		try {
			chart.setSubtitle("Potensi Pendapatan VS Realisasi Pendapatan");
			
			Veventamount vsum = eventregDao.sumAmount(obj.getTeventpk());
			
			CategoryModel model = new DefaultCategoryModel();
			model.setValue("Potensi", "Pendapatan", vsum.getInvamount());
	        model.setValue("Realisasi", "Pendapatan", vsum.getPaymentamount());
			
	        chart.setModel(model);
	        
	        chart.getXAxis().setCrosshair(true);
	        
	        AxisTitle title = chart.getYAxis().getTitle();
	        title.setUseHTML(true);
	        title.setText("Nominal Pendapatan");
	        
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
	
	@Command
	public void doBack() {
		try {
			Component comp = winEventDetail.getParent().getParent();
			comp.getChildren().clear();
			Executions.createComponents("/view/event/event.zul", comp, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(int pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

	public String getVano() {
		return vano;
	}

	public void setVano(String vano) {
		this.vano = vano;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
