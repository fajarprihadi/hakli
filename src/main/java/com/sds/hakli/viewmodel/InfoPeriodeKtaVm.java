package com.sds.hakli.viewmodel;

import java.text.SimpleDateFormat;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
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
	@Wire
	private Div divPeriodekta, divPermohonan, divPeriode;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("type") String type) {
		Selectors.wireComponents(view, this, false);

		if (type != null && type.equals("permohonan")) {
			divPermohonan.setVisible(true);
		} else {
			divPeriodekta.setVisible(true);
			anggota = (Tanggota) zkSession.getAttribute("anggota");
			if (anggota.getPeriodekta() != null) {
				periodekta.setValue(new SimpleDateFormat("dd MMMMM yyyy").format(anggota.getPeriodekta()));
			} else {
				divPeriode.setVisible(false);
			}
		}
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
