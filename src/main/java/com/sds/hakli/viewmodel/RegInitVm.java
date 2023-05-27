package com.sds.hakli.viewmodel;

import java.util.Date;
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
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tinvoice;
import com.sds.utils.AppUtils;
import com.sds.utils.StringUtils;

public class RegInitVm {
	
	private TanggotaDAO oDao = new TanggotaDAO();
	
	private String email;
	private String nik;
	
	@Wire
	private Window winRegInit;
	@Wire
	private Checkbox chkSisdmk;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
	}
	
	@Command
	public void doSubmit(@BindingParam("type") String type) {
		if (nik == null || nik.trim().length() == 0) {
			Messagebox.show("Silahkan masukkan NIK Anda", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.EXCLAMATION);
		} else if (!StringUtils.digitKtpValidator(nik)) { 
			Messagebox.show("Data NIK tidak sesuai. Periksa kembali data input NIK Anda", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.EXCLAMATION);
		} else {
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("isCheckSisdmk", chkSisdmk.isChecked());
				String page = "/view/anggota/anggotaadd.zul";
				Tanggota obj = null;
				List<Tanggota> objList = oDao.listByFilter("noktp = '" + nik.trim() + "'", "tanggotapk desc");
				if (objList.size() > 0) {
					obj = objList.get(0);
					if (obj.getStatusreg() != null && obj.getStatusreg().trim().length() > 0 && !obj.getStatusreg().equals(AppUtils.STATUS_ANGGOTA_REG_DECLINE)) {
						page = "/regdone.zul";
						if (obj.getStatusreg().equals("1")) {
							map.put("regstatus", "waitver");
						} else if (obj.getStatusreg().equals("2")) {
							Tinvoice inv = new TinvoiceDAO().findByFilter("vano = '" + obj.getVareg() + "'");
							if (inv != null && inv.getInvoicetype().equals(AppUtils.INVOICETYPE_REG) && inv.getInvoiceduedate().compareTo(new Date()) < 0) {
								map.put("regstatus", "invexp");
								map.put("inv", inv);
							} else {
								map.put("regstatus", "waitpay");
							}
						} else if (obj.getStatusreg().equals("3")) {
							map.put("regstatus", "aktif");
						}
					} else {
						map.put("obj", obj);
					}
				} else {
					obj = new Tanggota();
					obj.setNoktp(nik);
					map.put("obj", obj);
				}
				
				winRegInit.getChildren().clear();
				Executions.createComponents(page, winRegInit, map);
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
