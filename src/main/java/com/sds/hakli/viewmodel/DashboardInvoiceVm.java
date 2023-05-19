package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.model.TinvoiceListModel;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;

public class DashboardInvoiceVm {
	
private TinvoiceListModel model;
	
	private int pageStartNumber;
	private int pageTotalSize;
	private String filter;
	
	private String invoicetype;
	private String invno;
	private String invstatus;
	private String vano;	
	private Date begindate;
	private Date enddate;
	
	private SimpleDateFormat datelocalFormatter = new SimpleDateFormat("dd-MM-yyyy");
	
	@Wire
	private Grid grid;
	@Wire
	private Paging paging;
	
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
		
		grid.setRowRenderer(new RowRenderer<Tinvoice>() {

			@Override
			public void render(Row row, Tinvoice data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf((AppUtils.PAGESIZE * pageStartNumber) + index + 1)));
				row.getChildren().add(new Label(AppData.getInvoiceType(data.getInvoicetype())));
				row.getChildren().add(new Label(data.getTanggota().getNama()));
				row.getChildren().add(new Label(data.getInvoiceno()));
				row.getChildren().add(new Label(datelocalFormatter.format(data.getInvoicedate())));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getInvoiceamount())));
				row.getChildren().add(new Label(datelocalFormatter.format(data.getInvoiceduedate())));
				row.getChildren().add(new Label(data.getVano()));
				row.getChildren().add(new Label(data.getInvoicedesc()));
				row.getChildren().add(new Label(data.getIspaid().equals("Y") ? "Sudah Dibayar" : "Belum dibayar"));
			}
		});
		
		doReset();
	}
	
	@NotifyChange("pageTotalSize")
	public void refreshModel(int activePage) {
		paging.setPageSize(AppUtils.PAGESIZE);
		model = new TinvoiceListModel(activePage, AppUtils.PAGESIZE, filter, "tinvoicepk desc");
		pageTotalSize = model.getTotalSize(filter);
		paging.setTotalSize(pageTotalSize);
		grid.setModel(model);
	}
	
	@Command
	@NotifyChange("*")
	public void doSearch() {
		filter = "0=0";
		if (vano != null  && vano.trim().length() > 0)
			filter += "vano = '" + vano.trim() + "'";
		if (invno != null  && invno.trim().length() > 0)
			filter += "invoiceno = '" + invno.trim() + "'";
		refreshModel(pageStartNumber);
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		doSearch();
	}

	public int getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(int pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

	public String getInvoicetype() {
		return invoicetype;
	}

	public void setInvoicetype(String invoicetype) {
		this.invoicetype = invoicetype;
	}

	public String getInvno() {
		return invno;
	}

	public void setInvno(String invno) {
		this.invno = invno;
	}

	public String getInvstatus() {
		return invstatus;
	}

	public void setInvstatus(String invstatus) {
		this.invstatus = invstatus;
	}

	public String getVano() {
		return vano;
	}

	public void setVano(String vano) {
		this.vano = vano;
	}

	public Date getBegindate() {
		return begindate;
	}

	public void setBegindate(Date begindate) {
		this.begindate = begindate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	
}
