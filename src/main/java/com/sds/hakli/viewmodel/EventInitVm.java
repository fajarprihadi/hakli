package com.sds.hakli.viewmodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TeventDAO;
import com.sds.hakli.dao.TeventregDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tevent;
import com.sds.hakli.domain.Teventreg;
import com.sds.utils.AppUtils;

public class EventInitVm {
	
	private TanggotaDAO oDao = new TanggotaDAO();
	private TeventDAO eventDao = new TeventDAO();
	private TeventregDAO eventregDao = new TeventregDAO();
	
	private Tevent obj;
	private String eventimg;
	private String email;
	
	@Wire
	private Window winInit;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			Execution exec = Executions.getCurrent();
		    String id = exec.getParameter("id");
		    if (id != null) {
		    	obj = eventDao.findByFilter("eventid = '" + id + "'");
		    	if (obj != null)
		    		eventimg = AppUtils.PATH_EVENT + "/" + obj.getEventimg();
		    	else Executions.sendRedirect("/timeout.zul");
		    } else {
		    	Executions.sendRedirect("/timeout.zul");
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doSubmit() {
		if (email == null || email.trim().length() == 0) {
			Messagebox.show("Silahkan masukkan alamat e-mail Anda", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.INFORMATION);
		} else {
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("event", obj);
				//String page = "/view/event/eventreg.zul";
				String page = "/view/anggota/anggotaadd.zul";
				Tanggota obj = null;
				List<Tanggota> objList = oDao.listByFilter("email = '" + email.trim() + "'", "tanggotapk desc");
				if (objList.size() > 0) {
					obj = objList.get(0);
					
					Teventreg eventreg = eventregDao.findByFilter("tevent.teventpk = " + this.obj.getTeventpk() + " and tanggota.tanggotapk = " + obj.getTanggotapk());
					if (eventreg != null) {
						map.put("obj", eventreg);
						page = "/view/event/eventregdone.zul";
					} else map.put("obj", obj);
				} else {
					obj = new Tanggota();
					obj.setEmail(email);
					map.put("obj", obj);
				}
				winInit.getChildren().clear();
				Executions.createComponents(page, winInit, map);
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

	public String getEventimg() {
		return eventimg;
	}

	public void setEventimg(String eventimg) {
		this.eventimg = eventimg;
	}
}
