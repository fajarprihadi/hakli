package com.sds.hakli.viewmodel;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.extension.BriapiFundTransfer;

public class FundTransferVm {
	
	private Integer count;
	private Integer limit;
	private String trftype;
	private String trftoname;
	private String invoicetype;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("count")
	public void doCount() {
		if (trftype != null && trftoname != null && invoicetype != null) {
			try {
				if (trftype.equals("PROV")) {
					count = new TinvoiceDAO().countPaidPendingDisburseProv(invoicetype, trftoname.trim());
				} else if (trftype.equals("KAB")) {
					count = new TinvoiceDAO().countPaidPendingDisburseKab(invoicetype, trftoname.trim());
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.ERROR);
			}
		} else Messagebox.show("Please complete fields", WebApps.getCurrent().getAppName(), Messagebox.OK,
				Messagebox.EXCLAMATION);
	}
	
	@Command
	@NotifyChange("*")
	public void doSubmit() {
		if (limit != null && trftype != null && trftoname != null && invoicetype != null) {
			try {
				BriapiFundTransfer fundTransfer = new BriapiFundTransfer(invoicetype, trftype, trftoname.trim(), limit);
				fundTransfer.init();
				Messagebox.show("Fund Transfer Finished", WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.INFORMATION);
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.ERROR);
			}
		} else Messagebox.show("Please complete fields", WebApps.getCurrent().getAppName(), Messagebox.OK,
				Messagebox.EXCLAMATION);
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		trftype = null;
		limit = 0;
		trftoname = null;
		invoicetype = null;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getTrftype() {
		return trftype;
	}

	public void setTrftype(String trftype) {
		this.trftype = trftype;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getTrftoname() {
		return trftoname;
	}

	public void setTrftoname(String trftoname) {
		this.trftoname = trftoname;
	}

	public String getInvoicetype() {
		return invoicetype;
	}

	public void setInvoicetype(String invoicetype) {
		this.invoicetype = invoicetype;
	}

}
