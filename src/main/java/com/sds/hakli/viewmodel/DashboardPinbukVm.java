package com.sds.hakli.viewmodel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.dao.TtransferDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Ttransfer;
import com.sds.hakli.model.TtransferListModel;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;

public class DashboardPinbukVm {
	
	private Session session = Sessions.getCurrent();
	private Tanggota oUser;
	
	private TtransferListModel model;
	
	private TtransferDAO oDao = new TtransferDAO();
	
	private int pageStartNumber;
	private int pageTotalSize;
	private String filter;
	
	private BigDecimal totalpinbuk;
	private BigDecimal totalpinbukprov;
	private BigDecimal totalpinbukkab;
	
	private String invoicetype;
	private String invno;
	private String status;
	private String benefacc;	
	private Date begindate;
	private Date enddate;
	private String period;
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat datelocalFormatter = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat datetimelocalFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	
	@Wire
	private Groupbox gbSearch;
	@Wire
	private Grid grid;
	@Wire
	private Paging paging;
	@Wire
	private Div divHead;
	@Wire
	private Textbox tbBenefAcc;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		oUser = (Tanggota) session.getAttribute("anggota");
		
		if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_ADMIN) || 
				oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSPUSAT)) {
			divHead.setVisible(true);
		} else {
			divHead.setVisible(false);
			tbBenefAcc.setReadonly(true);
		}
		
		paging.addEventListener("onPaging", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				PagingEvent pe = (PagingEvent) event;
				pageStartNumber = pe.getActivePage();
				refreshModel(pageStartNumber);
			}
		});
		
		grid.setRowRenderer(new RowRenderer<Ttransfer>() {

			@Override
			public void render(Row row, Ttransfer data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf((AppUtils.PAGESIZE * pageStartNumber) + index + 1)));
				row.getChildren().add(new Label(AppData.getInvoiceType(data.getTinvoice().getInvoicetype())));
				A aNama = new A(data.getTinvoice().getTanggota().getNama());
				aNama.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data.getTinvoice().getTanggota());
						map.put("acttype", "view");
						Window win = (Window) Executions
								.createComponents("/view/anggota/anggotaedit.zul", null, map);
						win.setWidth("98%");
						win.setClosable(true);
						win.doModal();
					}
				});
				row.getChildren().add(aNama);
				row.getChildren().add(new Label(data.getTinvoice().getInvoiceno()));
				row.getChildren().add(new Label(datetimelocalFormatter.format(data.getTinvoice().getPaidtime())));
				row.getChildren().add(new Label(data.getNoreferral()));
				row.getChildren().add(new Label(data.getBenefacc()));
				row.getChildren().add(new Label(data.getBenefname()));
				row.getChildren().add(new Label(data.getBenefbankcode()));
				row.getChildren().add(new Label(data.getTrfto()));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getAmount())));
				row.getChildren().add(new Label(datetimelocalFormatter.format(data.getTrxtime())));
				row.getChildren().add(new Label(data.getResponsedesc()));
			}
		
		});
		
		doReset();
	}
	
	@NotifyChange("pageTotalSize")
	public void refreshModel(int activePage) {
		paging.setPageSize(AppUtils.PAGESIZE);
		model = new TtransferListModel(activePage, AppUtils.PAGESIZE, filter, "ttransferpk desc");
		pageTotalSize = model.getTotalSize(filter);
		paging.setTotalSize(pageTotalSize);
		grid.setModel(model);
		
		doListSummary();
	}
	
	@Command
	@NotifyChange("*")
	public void doSearch() {
		filter = "0=0";
		
		if (begindate == null || enddate == null) {
			Calendar cal = Calendar.getInstance();
			enddate = cal.getTime();
			cal.add(Calendar.MONTH, -1);
			begindate = cal.getTime();	 
		}
		
		if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSPROVINSI)) {
			benefacc = oUser.getMcabang().getMprov().getAccno();
		} else if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN)) {
			benefacc = oUser.getMcabang().getAccno();
		}
		
		if (invoicetype != null)
			filter += " and invoicetype = '" + invoicetype + "'";
		if (benefacc != null  && benefacc.trim().length() > 0)
			filter += " and benefacc = '" + benefacc.trim() + "'";
		if (invno != null  && invno.trim().length() > 0)
			filter += " and invoiceno = '" + invno.trim() + "'";
		if (status != null  && status.trim().length() > 0) {
			if (status.equals("Y"))
				filter += " and responsecode = '0200'";
			else filter += " and responsecode != '0200'";
		}
		if (begindate != null && enddate != null) {
			filter += " and date(trxtime) between '" + dateFormatter.format(begindate) + "' and '" + dateFormatter.format(enddate) + "'";
		}
		period = "Periode Pemindahbukuan " + datelocalFormatter.format(begindate) + " s/d " + datelocalFormatter.format(enddate);
		
//		if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSPROVINSI)) {
//			filter += " and benefacc = '" + oUser.getMcabang().getMprov().getAccno() + "'";
//		} else if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN)) {
//			filter += " and benefacc = '" + oUser.getMcabang().getAccno() + "'";
//		}
		
		refreshModel(pageStartNumber);
	}
	
	
	@Command
	@NotifyChange("*")
	public void doListSummary() {
		try {
			String filterperiod = "date(trxtime) between '" + dateFormatter.format(begindate) + "' and '" + dateFormatter.format(enddate) + "'";
			totalpinbuk = oDao.sumAmount(filterperiod);
			totalpinbukprov = oDao.sumAmount(filterperiod + " and trfto = 'PROV'");
			totalpinbukkab = oDao.sumAmount(filterperiod + " and trfto = 'KAB'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		
		invoicetype = null;
		invno = null;
		benefacc = null;
		status = null;
		
//		Calendar cal = Calendar.getInstance();
//		enddate = cal.getTime();
//		cal.add(Calendar.MONTH, -1);
//		begindate = cal.getTime();
		
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

	public BigDecimal getTotalpinbuk() {
		return totalpinbuk;
	}

	public void setTotalpinbuk(BigDecimal totalpinbuk) {
		this.totalpinbuk = totalpinbuk;
	}

	public BigDecimal getTotalpinbukprov() {
		return totalpinbukprov;
	}

	public void setTotalpinbukprov(BigDecimal totalpinbukprov) {
		this.totalpinbukprov = totalpinbukprov;
	}

	public BigDecimal getTotalpinbukkab() {
		return totalpinbukkab;
	}

	public void setTotalpinbukkab(BigDecimal totalpinbukkab) {
		this.totalpinbukkab = totalpinbukkab;
	}

	public String getBenefacc() {
		return benefacc;
	}

	public void setBenefacc(String benefacc) {
		this.benefacc = benefacc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	
}
