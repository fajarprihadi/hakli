package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
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
import org.zkoss.zul.A;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.MprovinsiDAO;
import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.domain.Mprovinsi;
import com.sds.hakli.domain.Vp2kb;

public class VerifikasiP2kbVm {
	
	private List<Vp2kb> objList = new ArrayList<>();

	private String filter;
	private String orderby;

	private String nama;
	private Mprovinsi region;
	
	private Integer pageTotalSize;
	
	@Wire
	private Grid grid;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);

		doReset();
		grid.setRowRenderer(new RowRenderer<Vp2kb>() {

			@Override
			public void render(Row row, Vp2kb data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index + 1)));
				row.getChildren().add(new Label(data.getCabang()));
				row.getChildren().add(new Label(data.getNoanggota()));
				A a = new A(data.getNama());
				a.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<>();
						Window win = new Window();
						map.put("obj", data);
						win = (Window) Executions.createComponents("/view/timp2kb/verifikasip2kbdata.zul", null, map);
						win.setWidth("70%");
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
							@Override
							public void onEvent(Event event) throws Exception {
								doReset();
							}

						});
					}
				});
				row.getChildren().add(a);
				row.getChildren().add(new Label(data.getAlamat()));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getTotalwaiting())));
			}
		});
	}
	
	@Command
	@NotifyChange("*")
	public void doSearch() {
		try {
			filter = "0=0";
			orderby = "nama";
			
			if(nama != null && nama.trim().length() > 0)
				filter += " and nama like '%" + nama.trim().toUpperCase() + "'";
			
			if(region != null)
				filter += " ";
			
			objList = new Tp2kbDAO().listVerifikasi(filter, orderby);
			pageTotalSize = objList.size();
			grid.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		nama = "";
		region = null;
		pageTotalSize = 0;

		doSearch();
	}
	
	public ListModelList<Mprovinsi> getRegionmodel(){
		ListModelList<Mprovinsi> lm = null;
		try {
			lm = new ListModelList<Mprovinsi>(new MprovinsiDAO().listAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lm;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Mprovinsi getRegion() {
		return region;
	}

	public void setRegion(Mprovinsi region) {
		this.region = region;
	}

	public Integer getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(Integer pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

}
