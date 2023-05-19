package com.sds.hakli.viewmodel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.model.TinvoiceListModel;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;

public class DashboardInvoiceVm {
	
	private TinvoiceListModel model;
	
	private TinvoiceDAO oDao = new TinvoiceDAO();
	
	private int pageStartNumber;
	private int pageTotalSize;
	private String filter;
	
	private Integer totaldue;
	private BigDecimal totaldueamount;
	private Integer totalunpaid;
	private BigDecimal totalunpaidamount;
	
	private BigDecimal invamount;
	private BigDecimal paidamount;
	private BigDecimal unpaidamount;
	
	private String invoicetype;
	private String invno;
	private String invstatus;
	private String vano;	
	private Date begindate;
	private Date enddate;
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat datelocalFormatter = new SimpleDateFormat("dd-MM-yyyy");
	
	@Wire
	private Groupbox gbSearch;
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
				row.getChildren().add(new Label(data.getIspaid().equals("Y") ? "Sudah Dibayar" : "Belum Dibayar"));
				row.getChildren().add(new Label(data.getPaidtime() != null ? datelocalFormatter.format(data.getPaidtime()) : ""));
				row.getChildren().add(new Label(data.getPaidrefno() != null ? data.getPaidrefno() : ""));
			}
		
		});
		
		doSummary();
		doReset();
	}
	
	@NotifyChange("*")
	public void doSummary() {
		try {
			totaldue = oDao.countDueDate();
			totaldueamount = oDao.sumAmount("CURRENT_DATE - INVOICEDUEDATE BETWEEN 0 AND 7");
			totalunpaid = oDao.pageCount("ispaid = 'N'");
			totalunpaidamount = oDao.sumAmount("ispaid = 'N'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@NotifyChange("pageTotalSize")
	public void refreshModel(int activePage) {
		paging.setPageSize(AppUtils.PAGESIZE);
		model = new TinvoiceListModel(activePage, AppUtils.PAGESIZE, filter, "tinvoicepk desc");
		pageTotalSize = model.getTotalSize(filter);
		paging.setTotalSize(pageTotalSize);
		grid.setModel(model);
		
		doListSummary();
	}
	
	@Command
	@NotifyChange("*")
	public void doSearchDue() {
		doReset();
		gbSearch.setOpen(false);
		filter = "CURRENT_DATE - INVOICEDUEDATE BETWEEN 0 AND 7";
		refreshModel(pageStartNumber);
	}
	
	@Command
	@NotifyChange("*")
	public void doSearchUnpaid() {
		doReset();
		gbSearch.setOpen(false);
		filter = "ispaid = 'N'";
		refreshModel(pageStartNumber);
	}
	
	@Command
	@NotifyChange("*")
	public void doSearch() {
		filter = "0=0";
		if (invoicetype != null)
			filter += " and invoicetype = '" + invoicetype + "'";
		if (vano != null  && vano.trim().length() > 0)
			filter += " and vano = '" + vano.trim() + "'";
		if (invno != null  && invno.trim().length() > 0)
			filter += " and invoiceno = '" + invno.trim() + "'";
		if (invstatus != null  && invstatus.trim().length() > 0)
			filter += " and ispaid = '" + invstatus.trim() + "'";
		if (begindate != null && enddate != null) {
			filter += " and invoicedate between '" + dateFormatter.format(begindate) + "' and '" + dateFormatter.format(enddate) + "'";
		}
		refreshModel(pageStartNumber);
	}
	
	
	@Command
	@NotifyChange("*")
	public void doListSummary() {
		try {
			invamount = oDao.sumAmount(filter);
			paidamount = oDao.sumAmount(filter + " and ispaid = 'Y'");
			unpaidamount = oDao.sumAmount(filter + " and ispaid = 'N'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		
		invoicetype = null;
		invno = null;
		vano = null;
		invstatus = null;
		
		Calendar cal = Calendar.getInstance();
		enddate = cal.getTime();
		cal.add(Calendar.MONTH, -1);
		begindate = cal.getTime();
		
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

	public Integer getTotalunpaid() {
		return totalunpaid;
	}

	public void setTotalunpaid(Integer totalunpaid) {
		this.totalunpaid = totalunpaid;
	}

	public BigDecimal getTotalunpaidamount() {
		return totalunpaidamount;
	}

	public void setTotalunpaidamount(BigDecimal totalunpaidamount) {
		this.totalunpaidamount = totalunpaidamount;
	}

	public Integer getTotaldue() {
		return totaldue;
	}

	public void setTotaldue(Integer totaldue) {
		this.totaldue = totaldue;
	}

	public BigDecimal getTotaldueamount() {
		return totaldueamount;
	}

	public void setTotaldueamount(BigDecimal totaldueamount) {
		this.totaldueamount = totaldueamount;
	}

	public BigDecimal getInvamount() {
		return invamount;
	}

	public void setInvamount(BigDecimal invamount) {
		this.invamount = invamount;
	}

	public BigDecimal getPaidamount() {
		return paidamount;
	}

	public void setPaidamount(BigDecimal paidamount) {
		this.paidamount = paidamount;
	}

	public BigDecimal getUnpaidamount() {
		return unpaidamount;
	}

	public void setUnpaidamount(BigDecimal unpaidamount) {
		this.unpaidamount = unpaidamount;
	}
	
}
