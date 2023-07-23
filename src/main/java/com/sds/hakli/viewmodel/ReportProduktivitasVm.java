package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.sds.hakli.dao.McabangDAO;
import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.domain.Mcabang;
import com.sds.hakli.domain.Vreportproduktivitas;

public class ReportProduktivitasVm {
	
	private List<Vreportproduktivitas> objList = new ArrayList<>();
	
	private String filter;
	private String orderby;

	private String nama;
	private Mcabang mcabang;
	
	private Integer pageTotalSize;
	
	@Wire
	private Grid grid;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
		doReset();
		
		grid.setRowRenderer(new RowRenderer<Vreportproduktivitas>() {

			@Override
			public void render(Row row, Vreportproduktivitas data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index + 1)));
				row.getChildren().add(new Label(data.getCabang()));
				row.getChildren().add(new Label(data.getCheckedbyid()));
				row.getChildren().add(new Label(data.getCheckedby()));
				row.getChildren().add(new Label(data.getUsergroupname()));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getTotal())));
			}
		});
	}
	
	@Command
	@NotifyChange("*")
	public void doSearch() {
		try {
			filter = "0 = 0 ";
			orderby = "CHECKEDBY";

			if (nama != null && nama.trim().length() > 0)
				filter += " and upper(CHECKEDBY) like '%" + nama.trim().toUpperCase() + "%'";

			if (mcabang != null) 
				filter += " and mcabangpk = " + mcabang.getMcabangpk();

			objList = new Tp2kbDAO().reportProduktivitas(filter, orderby);
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
		mcabang = null;
		pageTotalSize = 0;

		doSearch();
	}
	
	public ListModelList<Mcabang> getMcabangmodel() {
		ListModelList<Mcabang> lm = null;
		try {
			lm = new ListModelList<Mcabang>(new McabangDAO().listByFilter("0=0", "cabang"));
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

	public Mcabang getMcabang() {
		return mcabang;
	}

	public void setMcabang(Mcabang mcabang) {
		this.mcabang = mcabang;
	}

	public Integer getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(Integer pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

}
