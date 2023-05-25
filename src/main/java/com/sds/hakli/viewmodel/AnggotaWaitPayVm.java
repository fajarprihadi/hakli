package com.sds.hakli.viewmodel;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.dao.McabangDAO;
import com.sds.hakli.dao.MprovDAO;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.domain.Mcabang;
import com.sds.hakli.domain.Mprov;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.model.TanggotaListModel;
import com.sds.utils.AppUtils;

public class AnggotaWaitPayVm {
	
	private TanggotaListModel model;
	private ListModelList<Mcabang> cabangModel;
	
	private int pageStartNumber;
	private int pageTotalSize;
	private boolean needsPageUpdate;
	private String filter;
	
	private String nama;
	private Mprov region;
	private Mcabang cabang;
	
	private SimpleDateFormat datetimeFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	
	@Wire
	private Combobox cbRegion;
	@Wire
	private Combobox cbCabang;
	@Wire
	private Paging paging;
	@Wire
	private Grid grid;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
		paging.addEventListener("onPaging", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				PagingEvent pe = (PagingEvent) event;
				pageStartNumber = pe.getActivePage();
				refreshModel(pageStartNumber);
			}
		});
		
		grid.setRowRenderer(new RowRenderer<Tanggota>() {

			@Override
			public void render(Row row, Tanggota data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf((AppUtils.PAGESIZE * pageStartNumber) + index + 1)));
				row.getChildren().add(new Label(data.getNama()));
				row.getChildren().add(new Label(data.getNoktp()));
				row.getChildren().add(new Label(data.getMcabang().getCabang()));
				row.getChildren().add(new Label(datetimeFormatter.format(data.getCreatetime())));
				row.getChildren().add(new Label(data.getVareg()));
				Button btProcess = new Button();
				btProcess.setIconSclass("z-icon-eye");
				btProcess.setSclass("btn btn-primary btn-sm");
				btProcess.setAutodisable("self");
				btProcess.setTooltiptext("Detail");
				btProcess.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						map.put("acttype", "decided");
						Window win = (Window) Executions
								.createComponents("/view/anggota/anggotaedit.zul", null, map);
						win.setClosable(true);
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								doSearch();
								BindUtils.postNotifyChange(AnggotaWaitPayVm.this, "pageTotalSize");
							}
						});
						win.setWidth("98%");
						win.doModal();
					}
				});
				row.getChildren().add(btProcess);
			}
		});
		
		doReset();
	}
	
	@NotifyChange("pageTotalSize")
	public void refreshModel(int activePage) {
		paging.setPageSize(AppUtils.PAGESIZE);
		model = new TanggotaListModel(activePage, AppUtils.PAGESIZE, filter, "tanggotapk");
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
		filter = "statusreg = '" + AppUtils.STATUS_ANGGOTA_REG_PAYMENT + "'";
		
		if (nama != null && nama.trim().length() > 0)
			filter += " and upper(nama) like '%" + nama.trim().toUpperCase() + "%'" ;
		if (region != null) {
			if (cabang != null)
				filter += " and mcabangfk = " + cabang.getMcabangpk();
			else filter += " and mcabang.provcode = '" + region.getProvcode() + "'";
		}
			

		needsPageUpdate = true;
		pageStartNumber = 0;
		refreshModel(pageStartNumber);
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		nama = null;
		region = null;
		cabang = null;
		cbRegion.setValue(null);
		cbCabang.setValue(null);
		doSearch();
	}
	
	@Command
	@NotifyChange("cabangModel")
	public void doLoadCabang(@BindingParam("prov") Mprov prov) {
		try {
			if (prov != null) {
				cbCabang.setValue(null);
				cabangModel = new ListModelList<>(
						new McabangDAO().listByFilter("mprov.mprovpk = " + prov.getMprovpk(), "cabang"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ListModelList<Mprov> getRegionModel() {
		ListModelList<Mprov> oList = null;
		try {
			oList = new ListModelList<>(new MprovDAO().listAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}
	
	public int getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(int pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

	public ListModelList<Mcabang> getCabangModel() {
		return cabangModel;
	}

	public void setCabangModel(ListModelList<Mcabang> cabangModel) {
		this.cabangModel = cabangModel;
	}

	public Mprov getRegion() {
		return region;
	}

	public void setRegion(Mprov region) {
		this.region = region;
	}

	public Mcabang getCabang() {
		return cabang;
	}

	public void setCabang(Mcabang cabang) {
		this.cabang = cabang;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}
	
}
