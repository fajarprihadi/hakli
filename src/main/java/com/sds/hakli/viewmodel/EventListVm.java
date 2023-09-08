package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
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

import com.sds.hakli.dao.TeventDAO;
import com.sds.hakli.domain.Tevent;
import com.sds.utils.AppData;

public class EventListVm {
	
	private List<Tevent> objList;
	
	private SimpleDateFormat datelocalFormatter = new SimpleDateFormat("dd-MM-yyyy");
	
	@Wire
	private Grid grid;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
		grid.setRowRenderer(new RowRenderer<Tevent>() {

			@Override
			public void render(Row row, Tevent data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index + 1)));
				row.getChildren().add(new Label(data.getEventname()));
				row.getChildren().add(new Label(AppData.getEventType(data.getEventtype())));
				row.getChildren().add(new Label(datelocalFormatter.format(data.getEventdate())));
				row.getChildren().add(new Label(datelocalFormatter.format(data.getClosedate())));
				row.getChildren().add(new Label(data.getEventlocation()));
				row.getChildren().add(new Label(data.getIsfree().equals("Y") ? "Free" : NumberFormat.getInstance().format(data.getEventprice())));
				Button btReg = new Button("Daftar");
				btReg.setIconSclass("z-icon-user-circle-o");
				btReg.setSclass("btn btn-primary btn-sm");
				btReg.setAutodisable("self");
				btReg.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Executions.getCurrent().sendRedirect("https://portofolio.pphakli.org/view/event/eventinit.zul?id=" + data.getEventid(), "_blank");
					}
				});
				row.getChildren().add(btReg);
				
			}
		});
		
		refreshModel();
	}
	
	private void refreshModel() {
		try {
			objList = new TeventDAO().listByFilter("closedate >= date(now()) and isprivate = 'N'", "eventdate");
			grid.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
