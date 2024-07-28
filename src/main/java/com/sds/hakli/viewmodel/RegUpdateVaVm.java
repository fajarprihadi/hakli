package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlNativeComponent;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;
import org.zkoss.zul.Messagebox;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.extension.BriApiExt;
import com.sds.hakli.extension.MailHandler;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.BrivaCreateResp;
import com.sds.hakli.pojo.BrivaData;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class RegUpdateVaVm {
	
	private TanggotaDAO oDao = new TanggotaDAO();
	private TinvoiceDAO invDao = new TinvoiceDAO();
	
	private String nik;
	
	@Wire
	private Button btSubmit;
	@Wire
	private Div divInfo;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
	}
	
	@Command
	public void doSubmit() {
		if (nik != null && nik.trim().length() > 0) {
			try {
				divInfo.getChildren().clear();
				List<Tanggota> listAnggota = oDao.listByFilter("noktp = '" + nik.trim() + "'", "tanggotapk desc");
				if (listAnggota.size() > 0) {
					Tanggota obj = listAnggota.get(0);
					if (obj.getStatusreg().equals(AppUtils.STATUS_ANGGOTA_REG_PAYMENT)) {
						List<Tinvoice> listInv = invDao.listByFilter("vano = '" + obj.getVareg() + "'", "tinvoicepk desc");
						if (listInv.size() > 0) {
							Tinvoice inv = listInv.get(0);
							if (inv.getIspaid().equals("Y")) {
								doInfo(inv, null);
								Messagebox.show("Tagihan Anda sudah terbayar", WebApps.getCurrent().getAppName(), Messagebox.OK,
										Messagebox.INFORMATION);
							} else {
								if (inv.getInvoiceduedate().compareTo(new Date()) >= 0) {
									doInfo(inv, null);
									Messagebox.show("Tagihan Anda belum kadaluarsa. Batas akhir tagihan Anda tanggal " + new SimpleDateFormat("dd-MM-yyyy").format(inv.getInvoicedate()) + ". Segera lakukan pembayaran sebelum tagihan Anda kadaluarsa.", WebApps.getCurrent().getAppName(), Messagebox.OK,
											Messagebox.INFORMATION);
								} else {
									doUpdateVA(inv);
								}
							}
						} else {
							Messagebox.show("Data tagihan tidak ditemukan. Silakan hubungi pengurus.", WebApps.getCurrent().getAppName(), Messagebox.OK,
									Messagebox.INFORMATION);
						}
					} else if (obj.getStatusreg().equals(AppUtils.STATUS_ANGGOTA_REG_ACTIVE)) {
						Messagebox.show("Anda sudah terdaftar dalam Aplikasi Manajemen Keanggotaan HAKLI. Silakan Anda login menggunakan User Id dan Password Anda.", WebApps.getCurrent().getAppName(), Messagebox.OK,
								Messagebox.INFORMATION);
					} else if (obj.getStatusreg().equals(AppUtils.STATUS_ANGGOTA_REG)) {
						Messagebox.show("Anda sudah melakukan pendaftaran. Status pendaftaran Anda masih dalam proses verifikasi oleh pengurus.", WebApps.getCurrent().getAppName(), Messagebox.OK,
								Messagebox.INFORMATION);
					} else if (obj.getStatusreg().equals(AppUtils.STATUS_ANGGOTA_REG_DECLINE)) {
						Messagebox.show("Maaf, pendaftaran Anda ditolak dengan alasan " + obj.getRegmemo(), WebApps.getCurrent().getAppName(), Messagebox.OK,
								Messagebox.INFORMATION);
					}
				} else {
					Messagebox.show("NIK tidak terdaftar. Silakan Anda melakukan pendaftaran", WebApps.getCurrent().getAppName(), Messagebox.OK,
							Messagebox.EXCLAMATION);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Messagebox.show("Silakan input NIK Anda", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void doUpdateVA(Tinvoice objInvoice) {
		try {
			BriapiBean bean = AppData.getBriapibean();
			BriApiExt briapi = new BriApiExt(bean);
			BriApiToken briapiToken = briapi.getToken();
			BrivaData briva = new BrivaData();
			if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
				briva.setAmount(objInvoice.getInvoiceamount().toString());
				briva.setInstitutionCode(bean.getBriva_institutioncode());
				briva.setBrivaNo(bean.getBriva_cid());
				briva.setCustCode(objInvoice.getVano().substring(5));
				briva.setKeterangan(objInvoice.getInvoicedesc().trim().length() > 40 ? objInvoice.getInvoicedesc().substring(0, 40) : objInvoice.getInvoicedesc().trim());
				briva.setNama(objInvoice.getTanggota().getNama().trim().length() > 40 ? objInvoice.getTanggota().getNama().trim().substring(0, 40) : objInvoice.getTanggota().getNama().trim());
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, 14);
				Date vaexpdate = cal.getTime();
				
				briva.setExpiredDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vaexpdate));
				
				BrivaCreateResp brivaCreated = briapi.updateDataBriva(briapiToken.getAccess_token(), briva);
				if (brivaCreated != null && brivaCreated.getStatus()) {
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						objInvoice.setInvoiceduedate(vaexpdate);
						invDao.save(session, objInvoice);
						trx.commit();
						
						String bodymail_path = Executions.getCurrent().getDesktop().getWebApp()
								.getRealPath("/themes/mail/mailinv.html");
						new Thread(new MailHandler(objInvoice, objInvoice.getInvoicedesc().trim(), objInvoice.getTanggota().getEmail(), bodymail_path, null)).start();
						
						//Messagebox.show("Update data virtual account berhasil", WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.INFORMATION);
						btSubmit.setDisabled(true);
						
						doInfo(objInvoice, "Update perpanjangan tagihan berhasil");
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
					} finally {
						session.close();
					}
					
				} else {
					Messagebox.show("Update data virtual account gagal : " + brivaCreated.getErrDesc(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
				}	
			}
		} catch (Exception e) {	
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		} 
	}
	
	private void doInfo(Tinvoice obj, String info) {
		btSubmit.setDisabled(true);
		
		if (info != null) {
			HtmlNativeComponent h5 = new HtmlNativeComponent("h5");
			h5.appendChild(new Html(info));
			divInfo.appendChild(h5);
		}
		
		HtmlNativeComponent table = new HtmlNativeComponent("table");
		table.setClientAttribute("class", "table table-sm table-striped mb-0");
		HtmlNativeComponent tr = new HtmlNativeComponent("tr");
		HtmlNativeComponent td1 = new HtmlNativeComponent("td");
		td1.setClientAttribute("width", "50%");
		td1.setClientAttribute("align", "right");
		td1.appendChild(new Html("Nama :"));
		HtmlNativeComponent td2 = new HtmlNativeComponent("td");
		td2.appendChild(new Html(obj.getTanggota().getNama()));
		td2.setClientAttribute("align", "left");
		tr.appendChild(td1);
		tr.appendChild(td2);
		table.appendChild(tr);
		
		tr = new HtmlNativeComponent("tr");
		td1 = new HtmlNativeComponent("td");
		td1.appendChild(new Html("E-Mail :"));
		td1.setClientAttribute("align", "right");
		td2 = new HtmlNativeComponent("td");
		td2.setClientAttribute("align", "left");
		td2.appendChild(new Html(obj.getTanggota().getEmail()));
		tr.appendChild(td1);
		tr.appendChild(td2);
		table.appendChild(tr);
		
		tr = new HtmlNativeComponent("tr");
		td1 = new HtmlNativeComponent("td");
		td1.appendChild(new Html("Nomor Virtual Account :"));
		td1.setClientAttribute("align", "right");
		td2 = new HtmlNativeComponent("td");
		td2.setClientAttribute("align", "left");
		td2.appendChild(new Html(obj.getVano()));
		tr.appendChild(td1);
		tr.appendChild(td2);
		table.appendChild(tr);
		
		tr = new HtmlNativeComponent("tr");
		td1 = new HtmlNativeComponent("td");
		td1.appendChild(new Html("Nominal Tagihan :"));
		td1.setClientAttribute("align", "right");
		td2 = new HtmlNativeComponent("td");
		td2.setClientAttribute("align", "left");
		td2.appendChild(new Html("Rp. " + NumberFormat.getInstance().format(obj.getInvoiceamount())));
		tr.appendChild(td1);
		tr.appendChild(td2);
		table.appendChild(tr);
		
		tr = new HtmlNativeComponent("tr");
		td1 = new HtmlNativeComponent("td");
		td1.appendChild(new Html("Tanggal Jatuh Tempo :"));
		td1.setClientAttribute("align", "right");
		td2 = new HtmlNativeComponent("td");
		td2.setClientAttribute("align", "left");
		td2.appendChild(new Html(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(obj.getInvoiceduedate())));
		tr.appendChild(td1);
		tr.appendChild(td2);
		table.appendChild(tr);
		
		tr = new HtmlNativeComponent("tr");
		td1 = new HtmlNativeComponent("td");
		td1.appendChild(new Html("Status Pembayaran :"));
		td1.setClientAttribute("align", "right");
		td2 = new HtmlNativeComponent("td");
		td2.setClientAttribute("align", "left");
		td2.appendChild(new Html(obj.getIspaid().equals("Y") ? "Sudah dibayar Tanggal " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(obj.getPaidtime()) : "Belum dibayar"));
		tr.appendChild(td1);
		tr.appendChild(td2);
		table.appendChild(tr);
		
		divInfo.appendChild(table);
	}
	
	public String getNik() {
		return nik;
	}

	public void setNik(String nik) {
		this.nik = nik;
	}
}
