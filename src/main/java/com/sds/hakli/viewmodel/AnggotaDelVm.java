package com.sds.hakli.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
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
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.MusergroupDAO;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TanggotarolesDAO;
import com.sds.hakli.domain.Musergroup;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tanggotaroles;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class AnggotaDelVm {
	
	private TanggotaDAO oDao = new TanggotaDAO();
	
	private Tanggota objForm;
	
	@Wire
	private Window winAnggotaDel;
	@Wire
	private Image photo;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tanggota obj) {
		Selectors.wireComponents(view, this, false);
		objForm = obj;
		photo.setSrc(AppUtils.PATH_PHOTO + "/" + obj.getPhotolink());			
	}
	
	@Command
	public void doSave() {
		Session session = StoreHibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		try {
			objForm.setStatusreg(AppUtils.STATUS_ANGGOTA_DELETE);
			oDao.save(session, objForm);
			
			tx.commit();
			Clients.showNotification("Hapus Data Anggota Berhasil", "info", null, "middle_center", 1500);
			doClose();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void doClose() {
		Event closeEvent = new Event("onClose", winAnggotaDel, null);
		Events.postEvent(closeEvent);
	}
	
	public ListModelList<Musergroup> getUsergroup() {
		ListModelList<Musergroup> oList = null;
		try {
			oList = new ListModelList<>(new MusergroupDAO().listByFilter("isextra = 'N'", "usergroupname"));
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
