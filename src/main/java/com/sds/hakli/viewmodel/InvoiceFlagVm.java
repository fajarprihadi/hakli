package com.sds.hakli.viewmodel;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.bean.CallbackBean;
import com.sds.hakli.bean.CallbackResp;
import com.sds.hakli.dao.MsysparamDAO;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.extension.BriApiExt;
import com.sds.hakli.extension.CallbackExt;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.BrivaReport;
import com.sds.hakli.pojo.BrivaReportResp;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;

public class InvoiceFlagVm {
	
	private MsysparamDAO sysparamDao = new MsysparamDAO();
	
	private Tinvoice objInvoice;
	private Date trxdate;
	private Boolean isUpdated;
	
	private SimpleDateFormat dateParserFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
	
	@Wire
	private Window winInvoiceFlag;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("objInvoice") Tinvoice objInvoice) {
		Selectors.wireComponents(view, this, false);
		try {
			this.objInvoice = objInvoice;
			isUpdated = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doSubmit() {
		if (trxdate != null) {
			try {
				BriapiBean bean = AppData.getBriapibean();
				BriApiExt briapi = new BriApiExt(bean);
				BriApiToken briapiToken = briapi.getToken();
				if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
					String period = new SimpleDateFormat("yyyyMMdd").format(trxdate);
					BrivaReportResp obj = briapi.getBrivaReport(briapiToken.getAccess_token(), period, period);
					if (obj != null && obj.getStatus() != null && obj.getStatus()) {
						String callback_path = sysparamDao.getParamvalue(AppUtils.SYSPARAM_CALLBACK_PATH);
						String callback_url = sysparamDao.getParamvalue(AppUtils.SYSPARAM_CALLBACK_URL);
						String callback_token = sysparamDao.getParamvalue(AppUtils.SYSPARAM_BRIAPI_CONSUMER_KEY);
						String callback_secret = sysparamDao.getParamvalue(AppUtils.SYSPARAM_BRIAPI_CONSUMER_SECRET);
						
						for (BrivaReport report: obj.getData()) {
							if (objInvoice.getVano().equals(report.getBrivaNo() + report.getCustCode())) {
								try {
									Date trxdatetime = dateParserFormatter.parse(report.getPaymentDate());
									
									CallbackBean data = new CallbackBean();
									data.setBrivaNo(objInvoice.getVano());
									data.setBillAmount(String.valueOf(objInvoice.getInvoiceamount()));
									data.setTransactionDateTime(dateFormatter.format(trxdatetime));
									data.setJournalSeq(report.getKeterangan());
									data.setTerminalId("1");
									CallbackResp objResp = new CallbackExt().notifPayment(data, callback_path, callback_url, callback_token, callback_secret);
									if (objResp != null && objResp.getResponseCode().equals("0000")) {
										isUpdated = true;
										break;
									}
								} catch (Exception e) {
									e.printStackTrace();
									Messagebox.show("Update status tagihan gagal : " + e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
											Messagebox.ERROR);
								}
							}	
						}	
						
						if (isUpdated) {
							Clients.showNotification("Update Status Tagihan Berhasil", "info", null, "middle_center", 3000);
							doClose();
						} else {
							Messagebox.show("Tidak ditemukan transaksi pembayaran atas Nomor VA " + objInvoice.getVano() + " ditanggal " + new SimpleDateFormat("dd-MM-yyyy").format(trxdate), WebApps.getCurrent().getAppName(), Messagebox.OK,
									Messagebox.INFORMATION);
						}
					} else {
						Messagebox.show("Tidak ditemukan transaksi pembayaran atas Nomor VA " + objInvoice.getVano() + " ditanggal " + new SimpleDateFormat("dd-MM-yyyy").format(trxdate), WebApps.getCurrent().getAppName(), Messagebox.OK,
								Messagebox.INFORMATION);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Messagebox.show("Silakan input tanggal pembayaran", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
	}
	
	public void doClose() {
		Event closeEvent = new Event("onClose", winInvoiceFlag, isUpdated);
		Events.postEvent(closeEvent);
	}

	public Tinvoice getObjInvoice() {
		return objInvoice;
	}

	public void setObjInvoice(Tinvoice objInvoice) {
		this.objInvoice = objInvoice;
	}

	public Date getTrxdate() {
		return trxdate;
	}

	public void setTrxdate(Date trxdate) {
		this.trxdate = trxdate;
	}
	
	

}
