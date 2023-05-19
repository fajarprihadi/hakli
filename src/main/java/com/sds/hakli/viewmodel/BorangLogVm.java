package com.sds.hakli.viewmodel;

import java.text.SimpleDateFormat;
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

import com.sds.hakli.dao.Tp2kbbookDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tp2kbbook;

public class BorangLogVm {
	private Session session = Sessions.getCurrent();
	private Tanggota obj;
	
	List<Tp2kbbook> objList = new ArrayList<>();
	
	private String filter;
	private String orderby;
	
	private Integer pageTotalSize;
	
	private SimpleDateFormat dateLocalFormatter = new SimpleDateFormat("dd MMMM yyyy");
	
	@Wire
	private Grid grid;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		obj = (Tanggota) session.getAttribute("anggota");
		
		doReset();
		grid.setRowRenderer(new RowRenderer<Tp2kbbook>() {

			@Override
			public void render(Row row, Tp2kbbook data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index + 1)));
				row.getChildren().add(new Label(data.getNostr()));
				row.getChildren().add(new Label(dateLocalFormatter.format(data.getTglmulai())));
				row.getChildren().add(new Label(dateLocalFormatter.format(data.getTglakhir())));
				row.getChildren().add(new Label(data.getStatus().equals("Y") ? "Yes" : "No"));
				
				Button btBorang = new Button();
				btBorang.setIconSclass("z-icon-edit");
				btBorang.setSclass("btn btn-primary btn-sm");
				btBorang.setAutodisable("self");
				btBorang.setTooltiptext("Form Pengisian");
				btBorang.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<>();
						Window win = new Window();
						map.put("obj", obj);
						map.put("objForm", data);
						win = (Window) Executions.createComponents("/view/p2kb/borang.zul", null, map);
						win.setWidth("90%");
						win.setClosable(true);
						win.doModal();
					}
				});
				
				row.getChildren().add(btBorang);
			}
		});
	}
	
	@Command
	@NotifyChange("*")
	public void doSearch() {
		try {
			filter = "tanggotafk = " + obj.getTanggotapk() + " and status = 'Y'";
			orderby = "tglmulai";
			
			objList = new Tp2kbbookDAO().listByFilter(filter, orderby);
			pageTotalSize = objList.size();
			grid.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		pageTotalSize = 0;

		doSearch();
	}

	public Integer getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(Integer pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}
}
