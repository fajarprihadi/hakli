package com.sds.hakli.viewmodel;

import java.text.SimpleDateFormat;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

import com.sds.hakli.domain.Tanggota;

public class InfoPeriodeKtaVm {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota anggota;

	@Wire
	private Label periodekta;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
		anggota = (Tanggota) zkSession.getAttribute("anggota");
		periodekta.setValue(new SimpleDateFormat("dd MMMMM yyyy").format(anggota.getPeriodekta()));
	}

	public Tanggota getAnggota() {
		return anggota;
	}

	public void setAnggota(Tanggota anggota) {
		this.anggota = anggota;
	}
}
