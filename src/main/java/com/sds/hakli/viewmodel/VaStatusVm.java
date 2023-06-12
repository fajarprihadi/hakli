package com.sds.hakli.viewmodel;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.bean.CallbackBean;
import com.sds.hakli.bean.CallbackResp;
import com.sds.hakli.dao.MsysparamDAO;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.extension.BriApiExt;
import com.sds.hakli.extension.CallbackExt;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.BrivaCreateResp;
import com.sds.hakli.pojo.BrivaData;
import com.sds.hakli.pojo.BrivaInquiryResp;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;

public class VaStatusVm {
	
	private BrivaInquiryResp obj;
	private String vano;
	private Tinvoice objInvoice;
	
	private TinvoiceDAO invDao = new TinvoiceDAO();
	private MsysparamDAO sysparamDao = new MsysparamDAO();
	
	private BriapiBean bean;
	
	@Wire
	private Groupbox gbResult;
	@Wire
	private Button btCheckInvoice;
	@Wire
	private Groupbox gbInvoice;
	@Wire
	private Button btFlagInvoice;
	@Wire
	private Button btUpdateVA;

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
						btCheckInvoice.setVisible(true);
						if (obj.getData().getStatusBayar().equals("Y")) {
							btFlagInvoice.setDisabled(false);
							btUpdateVA.setDisabled(true);
						} else {
							btFlagInvoice.setDisabled(true);
							btUpdateVA.setDisabled(false);
						}
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
		Messagebox.show("Anda ingin melakukan update status tagihan menjadi terbayar dan sistem akan mengirim notifikasi pembayaran via e-mail ke anggota yang bersangkutan?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals("onOK")) {
							try {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("objInvoice", objInvoice);
								Window win = (Window) Executions
										.createComponents("/view/va/invoiceflag.zul", null, map);
								win.setClosable(true);
								win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

									@Override
									public void onEvent(Event event) throws Exception {
										if (event.getData() != null) {
											Boolean isUpdate = (Boolean) event.getData();
											if (isUpdate) {
												doReset();
												BindUtils.postNotifyChange(VaStatusVm.this, "*");
											}
										}
									}
								});
								win.doModal();
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
		});
	}
	
	@Command
	@NotifyChange("*")
	public void doUpdateVA() {
		Messagebox.show("Anda ingin melakukan pembaruan data virtual account sesuai dengan data tagihan yang tertera?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals("onOK")) {
							try {
								BriApiExt briapi = new BriApiExt(bean);
								BriApiToken briapiToken = briapi.getToken();
								BrivaData briva = new BrivaData();
								if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
									briva.setAmount(objInvoice.getInvoiceamount().toString());
									briva.setInstitutionCode(bean.getBriva_institutioncode());
									briva.setBrivaNo(bean.getBriva_cid());
									briva.setCustCode(vano.substring(5));
									briva.setKeterangan(objInvoice.getInvoicedesc().trim().length() > 40 ? objInvoice.getInvoicedesc().substring(0, 40) : objInvoice.getInvoicedesc().trim());
									briva.setNama(objInvoice.getTanggota().getNama().trim().length() > 40 ? objInvoice.getTanggota().getNama().trim().substring(0, 40) : objInvoice.getTanggota().getNama().trim());
									briva.setExpiredDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objInvoice.getInvoiceduedate()));
									
									BrivaCreateResp brivaCreated = briapi.updateDataBriva(briapiToken.getAccess_token(), briva);
									if (brivaCreated != null && brivaCreated.getStatus()) {
										Messagebox.show("Update data virtual account berhasil", WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.INFORMATION);
										doReset();
										BindUtils.postNotifyChange(VaStatusVm.this, "*");
									} else {
										btUpdateVA.setDisabled(true);
										Messagebox.show("Update data virtual account gagal : " + brivaCreated.getErrDesc(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
									}	
								}
							} catch (Exception e) {	
								Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
								e.printStackTrace();
							} 
						}
					}
		});
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
