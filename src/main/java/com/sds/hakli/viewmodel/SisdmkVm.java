package com.sds.hakli.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

import com.sds.hakli.bean.SisdmkBean;
import com.sds.hakli.dao.MjenjangDAO;
import com.sds.hakli.dao.MkabDAO;
import com.sds.hakli.dao.MprovDAO;
import com.sds.hakli.dao.MuniversitasDAO;
import com.sds.hakli.domain.Mjenjang;
import com.sds.hakli.domain.Mkab;
import com.sds.hakli.domain.Mprov;
import com.sds.hakli.domain.Muniversitas;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tpekerjaan;
import com.sds.hakli.domain.Tpendidikan;
import com.sds.hakli.extension.SisdmkApiExt;
import com.sds.hakli.pojo.SisdmkData;
import com.sds.hakli.pojo.SisdmkPekerjaan;
import com.sds.hakli.pojo.SisdmkPendidikan;
import com.sds.hakli.pojo.SisdmkToken;
import com.sds.utils.AppData;

public class SisdmkVm {
	
	private Session zkSession = Sessions.getCurrent();
	private Tanggota anggota;
	
	private String processinfo;

	@Wire
	private Window winSisdmk;
	@Wire
	private Div divProcessinfo;
	@Wire
	private Div divForm;
	@Wire
	private Button btSubmit;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		anggota = (Tanggota) zkSession.getAttribute("anggota");
	}
	
	@Command
	@NotifyChange("*")
	public void doSubmit() {
		try {
			SisdmkBean bean = AppData.getSisdmkParam();
			SisdmkApiExt api = new SisdmkApiExt();
			SisdmkToken sisdmkToken = api.getToken(bean);
			if (sisdmkToken != null && sisdmkToken.getStatus().equals("success")) {
				SisdmkData data = api.getBiodata(bean, sisdmkToken.getToken(), anggota.getNoktp());
				if (data != null && data.getStatus() == 200) {
					processinfo = "Anda telah terdaftar di SISDMK. Berikut data SISDMK Anda :";
					divProcessinfo.setVisible(true);
					
					Tanggota anggota = new Tanggota();
					anggota.setGender(data.getData().getJenis_kelamin());
					anggota.setNama(data.getData().getNama());
					anggota.setNoktp(data.getData().getNik());
					anggota.setTgllahir(data.getData().getTanggal_lahir());
					anggota.setTempatlahir(data.getData().getTempat_lahir());
					
					List<Tpekerjaan> pekerjaans = new ArrayList<>();
					for (SisdmkPekerjaan sisdmkPekerjaan: data.getData().getPekerjaan()) {
						Tpekerjaan pekerjaan = new Tpekerjaan();
						pekerjaan.setAlamatkantor(sisdmkPekerjaan.getAlamat());
						pekerjaan.setNip(sisdmkPekerjaan.getNip());
						pekerjaan.setNamakantor(sisdmkPekerjaan.getUnit());
						anggota.setNostr(sisdmkPekerjaan.getStr());
						
						if (sisdmkPekerjaan.getProvinsi() != null) {
							Mprov provkantor = new MprovDAO().findByFilter("upper(provname) = '" + sisdmkPekerjaan.getProvinsi().trim().toUpperCase() + "'");
							if (provkantor != null) {
								pekerjaan.setProvcode(provkantor.getProvcode());
								pekerjaan.setProvname(provkantor.getProvname());
							}
							
							if (sisdmkPekerjaan.getKabkot() != null) {
								Mkab kabkantor = new MkabDAO().findByFilter("provcode = '" + provkantor.getProvcode() + "' and upper(kabname) = '" + sisdmkPekerjaan.getKabkot().trim().toUpperCase() + "'");
								if (kabkantor != null) {
									pekerjaan.setKabcode(kabkantor.getKabcode());
									pekerjaan.setKabname(kabkantor.getKabname());
								}
							}
						}
						
						pekerjaans.add(pekerjaan);
					}
					
					List<Tpendidikan> pendidikans = new ArrayList<>();
					for (SisdmkPendidikan sisdmkPendidikan: data.getData().getPendidikan()) {
						Tpendidikan pendidikan = new Tpendidikan();
						pendidikan.setPeminatan1(sisdmkPendidikan.getProdi());
						pendidikan.setPeriodethakhir(sisdmkPendidikan.getTahun_lulus());
						
						if (sisdmkPendidikan.getPerguruan_tinggi() != null) {
							Muniversitas universitas = new MuniversitasDAO().findByFilter("upper(universitas) = '" + sisdmkPendidikan.getPerguruan_tinggi().trim().toUpperCase() + "'");
							if (universitas != null) {
								pendidikan.setMuniversitas(universitas);
							}
						}
						if (sisdmkPendidikan.getJenjang() != null) {
							Mjenjang jenjang = new MjenjangDAO().findByFilter("upper(trim(jenjang)) = '" + sisdmkPendidikan.getJenjang().trim().toUpperCase() + "'");
							if (jenjang != null) {
								pendidikan.setMjenjang(jenjang);
							}
						}
						
						pendidikans.add(pendidikan);
					}
					
					Map<String, Object> map = new HashMap<>();
					map.put("obj", anggota);
					map.put("pekerjaans", pekerjaans);
					map.put("pendidikans", pendidikans);
					map.put("acttype", "view");
					Executions.createComponents("/view/anggota/anggotaedit.zul", divForm, map);
				} else {
					processinfo = "Anda belum terdaftar di SISDMK.";
					divProcessinfo.setVisible(true);
				}
				btSubmit.setDisabled(true);
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
