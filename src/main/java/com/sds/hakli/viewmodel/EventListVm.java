package com.sds.hakli.viewmodel;

import java.text.SimpleDateFormat;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.sds.hakli.domain.Tevent;
import com.sds.utils.AppData;

public class EventListVm {
	
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
			}
		});
	}

}
