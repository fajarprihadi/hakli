package com.sds.hakli.viewmodel;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.dao.McabangDAO;
import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.domain.Mcabang;
import com.sds.hakli.domain.Vreportproduktivitas;
import com.sds.hakli.model.Tp2kbbookListModel;
import com.sds.hakli.model.VreportproduktivitasListModel;
import com.sds.utils.AppUtils;

public class ReportProduktivitasVm {
	
	private VreportproduktivitasListModel model;
	private List<Vreportproduktivitas> objList = new ArrayList<>();
	
	private String filter;
	private String orderby;

	private String nama;
	private Mcabang mcabang;
	
	private Integer year;
	private Integer month;
	
	private int pageStartNumber;
	private int pageTotalSize;
	private boolean needsPageUpdate;
	
	@Wire
	private Grid grid;
	@Wire
	private Paging paging;
	@Wire
	private Combobox cbMonth;
	
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
		
		String[] months = new DateFormatSymbols().getMonths();
		for (int i = 0; i < months.length; i++) {
			Comboitem item = new Comboitem();
			item.setLabel(months[i]);
			item.setValue(i + 1);
			cbMonth.appendChild(item);
		}
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
			
			if (month != null && year != null)
				filter += " and extract(year from checktime) = " + year + " and "
						+ "extract(month from checktime) = " + month;

			needsPageUpdate = true;
			paging.setActivePage(0);
			pageStartNumber = 0;
			refreshModel(pageStartNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@NotifyChange("pageTotalSize")
	public void refreshModel(int activePage) {
		paging.setPageSize(AppUtils.PAGESIZE);
		model = new VreportproduktivitasListModel(activePage, AppUtils.PAGESIZE, filter, orderby);
		if (needsPageUpdate) {
			pageTotalSize = model.getTotalSize(filter);
			needsPageUpdate = false;
		}
		paging.setTotalSize(pageTotalSize);
		grid.setModel(model);
	}

	@Command
	@NotifyChange("*")
	public void doReset() {
		nama = "";
		mcabang = null;
		pageTotalSize = 0;
		year = Calendar.getInstance().get(Calendar.YEAR);
		month = Calendar.getInstance().get(Calendar.MONTH) + 1;

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

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}


}
