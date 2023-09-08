package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
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

import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.dao.Tp2kbbookDAO;
import com.sds.hakli.domain.BranchTop;

public class P2kbSumWaitTimVm {
	
	private Tp2kbDAO oDao = new Tp2kbDAO();
	
	private List<BranchTop> objList;
	private long totaldata;
	private String type;
	private String title;
	
	@Wire
	private Grid grid;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("type") String type) {
		Selectors.wireComponents(view, this, false);
		this.type = type;
		if (type.equals("komisi"))
			title = "Daftar Pending Verifikasi Komisi P2KB";
		else if (type.equals("tim"))
			title = "Daftar Pending Verifikasi Tim P2KB";
		grid.setRowRenderer(new RowRenderer<BranchTop>() {

			@Override
			public void render(Row row, BranchTop data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index+1)));
				row.getChildren().add(new Label(data.getName()));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getTotal())));
				Button btDetail = new Button();
				btDetail.setIconSclass("z-icon-eye");
				btDetail.setSclass("btn btn-primary btn-sm");
				btDetail.setAutodisable("self");
				btDetail.setTooltiptext("Lihat Pending Cabang");
				btDetail.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("mprovpk", data.getId());
						map.put("provname", data.getName());
						map.put("type", type);
						Window win = (Window) Executions
								.createComponents("/view/p2kb/p2kbsumwaittimkab.zul", null, map);
						win.setClosable(true);
						win.setWidth("80%");
						win.doModal();
					}
				});
				row.getChildren().add(btDetail);
				
				totaldata += data.getTotal();
				BindUtils.postNotifyChange(P2kbSumWaitTimVm.this, "totaldata");
			}
		});
		refreshModel();
	}
	
	public void refreshModel() {
		try {
			if (type.equals("komisi")) {
				objList = new Tp2kbbookDAO().listSumWaitKomisiP2KB("0=0");
			} else if (type.equals("tim")) {
				objList = oDao.listSumWaitTimP2KB("totalkegiatanwv > 0");
			}
			
			grid.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public long getTotaldata() {
		return totaldata;
	}

	public void setTotaldata(long totaldata) {
		this.totaldata = totaldata;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
