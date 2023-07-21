package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.dao.Tp2kbbookDAO;
import com.sds.hakli.domain.BranchTop;

public class P2kbSumWaitTimKabVm {
	
	private Tp2kbDAO oDao = new Tp2kbDAO();
	
	private Integer mprovpk;
	private String provname;
	private List<BranchTop> objList;
	private long totaldata;
	private String type;
	private String title;
	
	@Wire
	private Grid grid;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, 
			@ExecutionArgParam("mprovpk") Integer mprovpk, 
			@ExecutionArgParam("provname") String provname, @ExecutionArgParam("type") String type) {
		Selectors.wireComponents(view, this, false);
		this.mprovpk = mprovpk;
		this.provname = provname;
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
				
				totaldata += data.getTotal();
				BindUtils.postNotifyChange(P2kbSumWaitTimKabVm.this, "totaldata");
			}
		});
		refreshModel();
	}
	
	public void refreshModel() {
		try {
			if (type.equals("komisi")) {
				objList = new Tp2kbbookDAO().listSumWaitKomisiP2KBKab("mprovfk = " + mprovpk);
			} else if (type.equals("tim")) {
				objList = oDao.listSumWaitTimP2KBKab("mprovfk = " + mprovpk);
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

	public Integer getMprovpk() {
		return mprovpk;
	}

	public void setMprovpk(Integer mprovpk) {
		this.mprovpk = mprovpk;
	}

	public String getProvname() {
		return provname;
	}

	public void setProvname(String provname) {
		this.provname = provname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
