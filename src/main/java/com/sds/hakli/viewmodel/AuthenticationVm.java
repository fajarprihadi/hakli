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
import org.zkoss.zul.Window;

import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.utils.db.StoreHibernateUtil;

public class AuthenticationVm {

	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();

	private TanggotaDAO anggotaDao = new TanggotaDAO();

	private String userid;
	private String password;
	private String lblMessage;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			Window win = (Window) Executions.createComponents("info.zul", null, null);
			win.doModal();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("lblMessage")
	public void doLogin() {
		if (userid != null && !userid.trim().equals("") && password != null && !password.trim().equals("")) {
			try {
				try {
					Tanggota anggota = anggotaDao.findByFilter("upper(noanggota) = '" + userid.trim().toUpperCase() + "'");
					if (anggota != null) {
						if (anggota.getStatusreg().equals("3")) {
							if (anggota.getPassword().equals(password)) {
								Session session = StoreHibernateUtil.openSession();
								Transaction trx = session.beginTransaction();
								try {
									anggota.setLastlogin(new Date());
									anggotaDao.save(session, anggota);
									trx.commit();
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									session.close();
								}
								
								zkSession.setAttribute("anggota", anggota);
								Executions.sendRedirect("view/index.zul");
							} else {
								lblMessage = "Authentication Failed : Invalid Password";
							}
						} else lblMessage = "User Id Anda belum aktif. Silahkan melakukan pembayaran atas tagihan pendaftaran yang sudah dikirim ke email Anda.";
					} else {
						lblMessage = "Authentication Failed : Invalid User Id";
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLblMessage() {
		return lblMessage;
	}

	public void setLblMessage(String lblMessage) {
		this.lblMessage = lblMessage;
	}
}
