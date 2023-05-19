package com.sds.hakli.viewmodel;

import java.text.SimpleDateFormat;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.sds.hakli.domain.Tanggota;

public class InfoPeriodeKtaVm {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota anggota;

	@Wire
	private Window winKtaDue;
	@Wire
	private Label periodekta;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
		anggota = (Tanggota) zkSession.getAttribute("anggota");
		periodekta.setValue(new SimpleDateFormat("dd MMMMM yyyy").format(anggota.getPeriodekta()));
	}
	
	@Command
	public void doClose() {
		Event closeEvent = new Event("onClose", winKtaDue, null);
		Events.postEvent(closeEvent);
	}

	public Tanggota getAnggota() {
		return anggota;
	}

	public void setAnggota(Tanggota anggota) {
		this.anggota = anggota;
	}
}
