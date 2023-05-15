package com.sds.hakli.viewmodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tevent;

public class RegInitVm {
	
	private TanggotaDAO oDao = new TanggotaDAO();
	
	private Tevent obj;
	private String email;
	private String nik;
	
	@Wire
	private Window winRegInit;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
	}
	
	@Command
	public void doSubmit(@BindingParam("type") String type) {
		if (nik == null || nik.trim().length() == 0) {
			Messagebox.show("Silahkan masukkan NIK Anda", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.INFORMATION);
		} else {
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("event", obj);
				String page = "/view/anggota/anggotaadd.zul";
				Tanggota obj = null;
				List<Tanggota> objList = oDao.listByFilter("noktp = '" + nik.trim() + "'", "tanggotapk desc");
				if (objList.size() > 0) {
					obj = objList.get(0);
					if (obj.getStatusreg() != null && obj.getStatusreg().trim().length() > 0) {
						if (obj.getStatusreg().equals("1")) {
							Messagebox.show("Data NIK Anda sudah melakukan pendaftaran pada sistem keanggotaan HAKLI. \nSaat ini data pendaftaran Anda sedang dalam proses verifikasi. \nMohon ditunggu.", WebApps.getCurrent().getAppName(), Messagebox.OK,
									Messagebox.INFORMATION);
						} else if (obj.getStatusreg().equals("2")) {
							Messagebox.show("Data NIK Anda sudah melakukan pendaftaran pada sistem keanggotaan HAKLI. \nSilahkan menyelesaikan pendaftaran Anda dengan menyelesaikan pembayaran pendaftaran. \nCek e-Mail anda " + obj.getEmail() + " untuk melihat informasi tagihan.", WebApps.getCurrent().getAppName(), Messagebox.OK,
									Messagebox.INFORMATION);
						} else if (obj.getStatusreg().equals("3")) {
							Messagebox.show("Data NIK sudah terdaftar pada sistem keanggotaan HAKLI. Silahkan login menggunakan user id dan password Anda", WebApps.getCurrent().getAppName(), Messagebox.OK,
									Messagebox.INFORMATION);
						}
					} else {
						map.put("obj", obj);
						map.put("regtype", "event");
						winRegInit.getChildren().clear();
						Executions.createComponents(page, winRegInit, map);
					}
				} else {
					obj = new Tanggota();
					obj.setNoktp(nik);
					map.put("obj", obj);
					winRegInit.getChildren().clear();
					Executions.createComponents(page, winRegInit, map);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNik() {
		return nik;
	}

	public void setNik(String nik) {
		this.nik = nik;
	}

}
