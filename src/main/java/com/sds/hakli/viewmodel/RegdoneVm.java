package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
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
import org.zkoss.zul.Separator;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.domain.AnggotaReg;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.extension.BriApiExt;
import com.sds.hakli.extension.MailHandler;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.BrivaCreateResp;
import com.sds.hakli.pojo.BrivaData;
import com.sds.utils.AppData;
import com.sds.utils.db.StoreHibernateUtil;

public class RegdoneVm {
	
	private Tinvoice inv;
	private String processinfo;
	
	@Wire
	private Button btLogin;
	@Wire
	private Button btInv;
	@Wire
	private Div divProcessinfo;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, 
			@ExecutionArgParam("obj") AnggotaReg obj, @ExecutionArgParam("regstatus") String regstatus, 
			@ExecutionArgParam("inv") Tinvoice inv) {
		Selectors.wireComponents(view, this, false);
		this.inv = inv;
		try {
			if (regstatus == null) {
				Executions.sendRedirect("/reginit.zul");
			} else {
				if (regstatus.equals("reg")) {
					processinfo = obj.getPribadi().getNama() + ", Pendaftaran Anda telah diterima, "
							+ "tim pengurus cabang yang bersangkutan akan melakukan verifikasi data "
							+ "yang anda masukkan dan selanjutnya dikonfirmasi kembali melalui "
							+ "e-mail " + obj.getPribadi().getEmail();
				} else if (regstatus.equals("waitver")) {
					processinfo = "Data NIK Anda sudah melakukan pendaftaran pada sistem keanggotaan HAKLI. \nSaat ini data pendaftaran Anda sedang dalam proses verifikasi. \nMohon ditunggu.";
				} else if (regstatus.equals("waitpay")) {
					processinfo = "Data NIK Anda sudah melakukan pendaftaran pada sistem keanggotaan HAKLI. \nSilahkan menyelesaikan pendaftaran Anda dengan menyelesaikan pembayaran pendaftaran. \nCek e-Mail anda untuk melihat informasi tagihan.";
				} else if (regstatus.equals("invexp")) {
					processinfo = "Data NIK Anda sudah melakukan pendaftaran pada sistem keanggotaan HAKLI. \nTapi Anda tidak menyelesaikan pembayaran pendaftaran hingga melewati batas akhir pembayaran. \nKlik tombol Re-Generate tagihan untuk membuat tagihan baru.";
					btInv.setVisible(true);
				} else if (regstatus.equals("aktif")) {
					processinfo = "Data NIK sudah terdaftar pada sistem keanggotaan HAKLI. Silahkan login menggunakan user id dan password Anda.";
					btLogin.setVisible(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doLogin() {
		Executions.sendRedirect("/");
	}
	
	@Command
	public void doReInvoice() {
		try {
			BriapiBean bean = AppData.getBriapibean();
			BriApiExt briapi = new BriApiExt(bean);
			BriApiToken briapiToken = briapi.getToken();
			BrivaData briva = new BrivaData();
			if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
				briva.setAmount(inv.getInvoiceamount().toString());
				briva.setInstitutionCode(bean.getBriva_institutioncode());
				briva.setBrivaNo(bean.getBriva_cid());
				briva.setCustCode(inv.getVano().substring(5));
				briva.setKeterangan(inv.getInvoicedesc().trim().length() > 40 ? inv.getInvoicedesc().substring(0, 40) : inv.getInvoicedesc());
				briva.setNama(inv.getTanggota().getNama());
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, 10);
				Date vaexpdate = cal.getTime();
				briva.setExpiredDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vaexpdate));
				
				BrivaCreateResp brivaCreated = briapi.updateDataBriva(briapiToken.getAccess_token(), briva);
				if (brivaCreated != null && brivaCreated.getStatus()) {
					Session session = StoreHibernateUtil.openSession();
					Transaction tx = session.beginTransaction();
					try {
						inv.setInvoiceduedate(vaexpdate);
						new TinvoiceDAO().save(session, inv);
						tx.commit();
						
						String bodymail_path = Executions.getCurrent().getDesktop().getWebApp()
								.getRealPath("/themes/mail/mailinv.html");
						new Thread(new MailHandler(inv, bodymail_path)).start();
						
						HtmlNativeComponent table = new HtmlNativeComponent("table");
						table.setClientAttribute("class", "table table-sm table-striped mb-0");
						HtmlNativeComponent tr = new HtmlNativeComponent("tr");
						HtmlNativeComponent td1 = new HtmlNativeComponent("td");
						td1.setClientAttribute("width", "50%");
						td1.setClientAttribute("align", "right");
						td1.appendChild(new Html("Nama :"));
						HtmlNativeComponent td2 = new HtmlNativeComponent("td");
						td2.appendChild(new Html(inv.getTanggota().getNama()));
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
						td2.appendChild(new Html(inv.getTanggota().getEmail()));
						tr.appendChild(td1);
						tr.appendChild(td2);
						table.appendChild(tr);
						
						tr = new HtmlNativeComponent("tr");
						td1 = new HtmlNativeComponent("td");
						td1.appendChild(new Html("Nomor Virtual Account :"));
						td1.setClientAttribute("align", "right");
						td2 = new HtmlNativeComponent("td");
						td2.setClientAttribute("align", "left");
						td2.appendChild(new Html(inv.getVano()));
						tr.appendChild(td1);
						tr.appendChild(td2);
						table.appendChild(tr);
						
						tr = new HtmlNativeComponent("tr");
						td1 = new HtmlNativeComponent("td");
						td1.appendChild(new Html("Nominal Tagihan :"));
						td1.setClientAttribute("align", "right");
						td2 = new HtmlNativeComponent("td");
						td2.setClientAttribute("align", "left");
						td2.appendChild(new Html("Rp. " + NumberFormat.getInstance().format(inv.getInvoiceamount())));
						tr.appendChild(td1);
						tr.appendChild(td2);
						table.appendChild(tr);
						
						tr = new HtmlNativeComponent("tr");
						td1 = new HtmlNativeComponent("td");
						td1.appendChild(new Html("Tanggal Jatuh Tempo :"));
						td1.setClientAttribute("align", "right");
						td2 = new HtmlNativeComponent("td");
						td2.setClientAttribute("align", "left");
						td2.appendChild(new Html(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(vaexpdate)));
						tr.appendChild(td1);
						tr.appendChild(td2);
						table.appendChild(tr);
						divProcessinfo.getChildren().clear();
						divProcessinfo.appendChild(new Html("Proses Re-generate Tagihan Berhasil. Silakan Anda melakukan pembayaran dengan data sebagai berikut :"));
						divProcessinfo.appendChild(new Separator());
						divProcessinfo.appendChild(table);
						btInv.setDisabled(true);
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
								Messagebox.ERROR);
					} finally {
						session.close();
					}
					
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getProcessinfo() {
		return processinfo;
	}

	public void setProcessinfo(String processinfo) {
		this.processinfo = processinfo;
	}
	
	

}
