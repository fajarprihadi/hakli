package com.sds.hakli.viewmodel;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;

import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.extension.MailHandler;
import com.sds.utils.AppData;
import com.sds.utils.db.StoreHibernateUtil;
public class ForgotPassVm {

	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();

	private TanggotaDAO anggotaDao = new TanggotaDAO();

	private String userid;
	private String lblMessage;
	
	@Wire
	private Button btSubmit;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
	}

	@Command
	@NotifyChange("lblMessage")
	public void doSubmit() {
		if (userid != null && !userid.trim().equals("")) {
			try {
				try {
					Tanggota anggota = anggotaDao.findByFilter("upper(noanggota) = '" + userid.trim().toUpperCase() + "'");
					if (anggota != null) {
						if (anggota.getStatusreg().equals("3")) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								anggota.setPassword(AppData.passwordGenerator());
								anggotaDao.save(session, anggota);
								trx.commit();
								
								String bodymail_path = Executions.getCurrent().getDesktop().getWebApp()
										.getRealPath("/themes/mail/mailresetpass.html");
								new Thread(new MailHandler(anggota, bodymail_path)).start();
								
								lblMessage = "Reset password berhasil. Silahkan cek email Anda " + anggota.getEmail() + " untuk melihat password baru Anda.";
								btSubmit.setDisabled(true);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								session.close();
							}
						} else lblMessage = "User Id Anda belum aktif. Silahkan melakukan pembayaran atas tagihan pendaftaran yang sudah dikirim ke email Anda.";
					} else {
						lblMessage = "Data nomor anggota Anda tidak ditemukan";
					}
				} catch (Exception e) {
					lblMessage = "Authentication Failed : " + e.getMessage();
					e.printStackTrace();
				}

			} catch (Exception e) {
				lblMessage = "Authentication Failed : " + e.getMessage();
				e.printStackTrace();
			}
		}
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getLblMessage() {
		return lblMessage;
	}

	public void setLblMessage(String lblMessage) {
		this.lblMessage = lblMessage;
	}
}
