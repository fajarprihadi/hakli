package com.sds.hakli.viewmodel;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.extension.BriApiExt;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.FundRcReq;
import com.sds.hakli.pojo.FundRcResp;
import com.sds.utils.AppData;

public class FtStatusVm {
	
	private FundRcResp obj;
	private String noreferral;
	private Date trxdate;
	private Tinvoice objInvoice;
	
	private BriapiBean bean;
	
	@Wire
	private Groupbox gbResult;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			bean = AppData.getBriapibean();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void doSubmit() {
		if (noreferral != null && noreferral.trim().length() > 0) {
			try {
				BriApiExt briapi = new BriApiExt(bean);
				BriApiToken briapiToken = briapi.getTokenFundTransfer();
				if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
					
					FundRcReq rcReq = new FundRcReq();
					rcReq.setNoReferral(noreferral);
					rcReq.setTransactionDate(new SimpleDateFormat("dd-MM-yyyy").format(trxdate));
					obj = briapi.fundRcStatus(briapiToken.getAccess_token(), rcReq);
					if (obj != null) {
						if (obj.getResponseCode().equals("0300")) 
							gbResult.setVisible(true);
						else Messagebox.show(obj.getResponseDescription(), WebApps.getCurrent().getAppName(), Messagebox.OK,
								Messagebox.INFORMATION);
					} else 
						Messagebox.show("Data tidak ditemukan", WebApps.getCurrent().getAppName(), Messagebox.OK,
							Messagebox.INFORMATION);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.ERROR);
			}
		} else {
			Messagebox.show("Silakan masukkan nomor referral dan tanggal transaksi yang ingin anda lakukan pengecekan", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		gbResult.setVisible(false);
		noreferral = null;
		trxdate = null;
	}

	public String getNoreferral() {
		return noreferral;
	}

	public void setNoreferral(String noreferral) {
		this.noreferral = noreferral;
	}

	public Date getTrxdate() {
		return trxdate;
	}

	public void setTrxdate(Date trxdate) {
		this.trxdate = trxdate;
	}
	
	public FundRcResp getObj() {
		return obj;
	}

	public void setObj(FundRcResp obj) {
		this.obj = obj;
	}

	public Tinvoice getObjInvoice() {
		return objInvoice;
	}

	public void setObjInvoice(Tinvoice objInvoice) {
		this.objInvoice = objInvoice;
	}
}
