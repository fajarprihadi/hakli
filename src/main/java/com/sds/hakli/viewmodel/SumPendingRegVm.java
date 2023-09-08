package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
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

import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.domain.BranchTop;
import com.sds.hakli.domain.Tanggota;
import com.sds.utils.AppUtils;

public class SumPendingRegVm {
	
	private Session session = Sessions.getCurrent();
	private Tanggota oUser;
	
	private TanggotaDAO oDao = new TanggotaDAO();
	
	private String filter;
	private List<BranchTop> objList;
	private long totaldata;
	
	@Wire
	private Grid grid;

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
						Window win = (Window) Executions
								.createComponents("/view/sumpendingregkab.zul", null, map);
						win.setClosable(true);
						win.setWidth("80%");
						win.doModal();
					}
				});
				row.getChildren().add(btDetail);
				
				totaldata += data.getTotal();
				BindUtils.postNotifyChange(SumPendingRegVm.this, "totaldata");
			}
		});
		refreshModel();
	}
	
	public void refreshModel() {
		try {
			filter = "0=0";
			if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN))
				filter = "MCABANGFK = " + oUser.getMcabang().getMcabangpk();
			else if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSPROVINSI))
				filter = "MPROVFK = " + oUser.getMcabang().getMprov().getMprovpk();
			
			objList = oDao.listSumPendingReg(filter);
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

}
