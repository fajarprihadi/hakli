package com.sds.hakli.viewmodel;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.MusergroupDAO;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.domain.Musergroup;
import com.sds.hakli.domain.Tanggota;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class AnggotaRoleVm {
	
	private Tanggota objForm;
	
	@Wire
	private Window winRole;
	@Wire
	private Image photo;
	@Wire
	private Combobox cbRole;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tanggota obj) {
		Selectors.wireComponents(view, this, false);
		objForm = obj;
		if (obj.getMusergroup() != null)
			cbRole.setValue(obj.getMusergroup().getUsergroupname());
		photo.setSrc(AppUtils.PATH_PHOTO + "/" + obj.getPhotolink());
	}
	
	@Command
	public void doSave() {
		Session session = StoreHibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		try {
			new TanggotaDAO().save(session, objForm);
			tx.commit();
			Clients.showNotification("Set Kewenangan Anggota Berhasil", "info", null, "middle_center", 1500);
			doClose();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void doClose() {
		Event closeEvent = new Event("onClose", winRole, null);
		Events.postEvent(closeEvent);
	}
	
	public ListModelList<Musergroup> getUsergroup() {
		ListModelList<Musergroup> oList = null;
		try {
			oList = new ListModelList<>(new MusergroupDAO().listByFilter("0=0", "usergroupname"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	public Tanggota getObjForm() {
		return objForm;
	}

	public void setObjForm(Tanggota objForm) {
		this.objForm = objForm;
	}

}
