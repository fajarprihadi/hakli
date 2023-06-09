package com.sds.hakli.viewmodel;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.bean.CallbackBean;
import com.sds.hakli.bean.CallbackResp;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.extension.BriApiExt;
import com.sds.hakli.extension.CallbackExt;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.BrivaInquiryResp;
import com.sds.utils.AppData;

public class VaStatusVm {
	
	private BrivaInquiryResp obj;
	private String vano;
	private Tinvoice objInvoice;
	
	private TinvoiceDAO invDao = new TinvoiceDAO();
	
	private BriapiBean bean;
	
	@Wire
	private Groupbox gbResult;
	@Wire
	private Button btCheckInvoice;
	@Wire
	private Groupbox gbInvoice;
	@Wire
	private Button btFlagInvoice;

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
		if (vano != null && vano.trim().length() > 5) {
			try {
				BriApiExt briapi = new BriApiExt(bean);
				BriApiToken briapiToken = briapi.getToken();
				if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
					String custcode = vano.substring(5);
					obj = briapi.getBriva(briapiToken.getAccess_token(), custcode);
					if (obj != null && obj.getStatus() != null && obj.getStatus()) {
						gbResult.setVisible(true);
						if (obj.getData().getStatusBayar().equals("Y"))
							btCheckInvoice.setVisible(true);
					} else 
						Messagebox.show("Nomor virtual account tidak dikenal", WebApps.getCurrent().getAppName(), Messagebox.OK,
							Messagebox.INFORMATION);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.ERROR);
			}
		} else {
			Messagebox.show("Silakan masukkan nomor virtual account yang ingin anda lakukan pengecekan", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
		
	}
	
	@Command
	@NotifyChange("objInvoice")
	public void doCheckInvoice() {
		try {
			//vano = "77777327600015";
			List<Tinvoice> objList = invDao.listByFilter("vano = '" + vano.trim() + "' and ispaid = 'N'", "tinvoicepk desc");
			if (objList.size() > 0) {
				objInvoice = objList.get(0);
				gbInvoice.setVisible(true);
			} else {
				Messagebox.show("Tidak ada data tagihan yang belum terupdate", WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.INFORMATION);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doFlagInvoice() {
		try {
			CallbackBean data = new CallbackBean();
			data.setBrivaNo(obj.getData().getBrivaNo());
			data.setBillAmount(String.valueOf(obj.getData().getAmount()));
			data.setTransactionDateTime("");
			data.setJournalSeq("");
			data.setTerminalId("1");
			CallbackResp objResp = new CallbackExt().notifPayment(data);
			if (objResp != null) {
				if (objResp.getResponseCode().equals("0000")) {
					Messagebox.show("Proses update status pembayaran berhasil", WebApps.getCurrent().getAppName(), Messagebox.OK,
							Messagebox.EXCLAMATION);
					
					btFlagInvoice.setDisabled(true);
				} else {
					Messagebox.show("Proses update status pembayaran gagal : " + objResp.getResponseDescription(), WebApps.getCurrent().getAppName(), Messagebox.OK,
							Messagebox.EXCLAMATION);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		gbResult.setVisible(false);
		vano = null;
		gbInvoice.setVisible(false);
		btCheckInvoice.setVisible(false);
		objInvoice = null;
	}

	public String getVano() {
		return vano;
	}

	public void setVano(String vano) {
		this.vano = vano;
	}

	public BrivaInquiryResp getObj() {
		return obj;
	}

	public void setObj(BrivaInquiryResp obj) {
		this.obj = obj;
	}

	public Tinvoice getObjInvoice() {
		return objInvoice;
	}

	public void setObjInvoice(Tinvoice objInvoice) {
		this.objInvoice = objInvoice;
	}
}
