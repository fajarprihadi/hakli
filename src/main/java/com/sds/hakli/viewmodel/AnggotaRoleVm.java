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

public class AnggotaRoleVm {
	
	private TanggotaDAO oDao = new TanggotaDAO();
	private TanggotarolesDAO tanggotarolesDao = new TanggotarolesDAO();
	private MusergroupDAO usergroupDao = new MusergroupDAO();
	private Map<String, Musergroup> mapUsergroup = new HashMap<>();
	private Map<String, Tanggotaroles> mapRoles = new HashMap<>();
	
	private Tanggota objForm;
	
	@Wire
	private Window winRole;
	@Wire
	private Image photo;
	@Wire
	private Combobox cbRole;
	@Wire
	private Checkbox chkVerReg;
	@Wire
	private Checkbox chkTimP2kb;
	@Wire
	private Checkbox chkKomisiP2kb;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tanggota obj) {
		Selectors.wireComponents(view, this, false);
		objForm = obj;
		if (obj.getMusergroup() != null)
			cbRole.setValue(obj.getMusergroup().getUsergroupname());
		photo.setSrc(AppUtils.PATH_PHOTO + "/" + obj.getPhotolink());
		doChangeRole(objForm.getMusergroup().getUsergroupcode());
		
		try {
			for (Musergroup usergroup: usergroupDao.listByFilter("0=0", "usergroupcode")) {
				mapUsergroup.put(usergroup.getUsergroupcode(), usergroup);
			}
			
			for (Tanggotaroles roles: tanggotarolesDao.listByFilter("tanggota.tanggotapk = " + objForm.getTanggotapk(), "tanggotarolespk")) {
				mapRoles.put(roles.getMusergroup().getUsergroupcode(), roles);
				if (roles.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_VERIFICATORREG)) {
					chkVerReg.setChecked(true);
				} else if (roles.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_TIMP2KB)) {
					chkTimP2kb.setChecked(true);
				} else if (roles.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_KOMISIP2KB)) {
					chkKomisiP2kb.setChecked(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doChangeRole(@BindingParam("usergroup") String usergroup) {
		if (usergroup != null) {
			if (usergroup.equals(AppUtils.ANGGOTA_ROLE_ADMIN) || usergroup.equals(AppUtils.ANGGOTA_ROLE_PENGURUSPUSAT)) {
				chkVerReg.setDisabled(false);
				chkTimP2kb.setDisabled(false);
				chkKomisiP2kb.setDisabled(false);
			} else if (usergroup.equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN) || usergroup.equals(AppUtils.ANGGOTA_ROLE_PENGURUSPROVINSI)) {
				chkVerReg.setDisabled(false);
				chkTimP2kb.setDisabled(false);
				chkKomisiP2kb.setDisabled(true);
				chkKomisiP2kb.setChecked(false);
			} else if (usergroup.equals(AppUtils.ANGGOTA_ROLE_ANGGOTABIASA)) {
				chkVerReg.setDisabled(true);
				chkTimP2kb.setDisabled(true);
				chkKomisiP2kb.setDisabled(true);
				chkVerReg.setChecked(false);
				chkTimP2kb.setChecked(false);
				chkKomisiP2kb.setChecked(false);
			}
		}
	}
	
	@Command
	public void doSave() {
		Session session = StoreHibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		try {
			oDao.save(session, objForm);
			
			List<Tanggotaroles> addRoles = new ArrayList<>();
			List<Tanggotaroles> delRoles = new ArrayList<>();
			if (chkVerReg.isChecked()) {
				if (mapRoles.get(AppUtils.ANGGOTA_ROLE_VERIFICATORREG) == null) {
					Tanggotaroles role = new Tanggotaroles();
					role.setTanggota(objForm);
					role.setMusergroup(mapUsergroup.get(AppUtils.ANGGOTA_ROLE_VERIFICATORREG));
					addRoles.add(role);
				}
			} else {
				if (mapRoles.get(AppUtils.ANGGOTA_ROLE_VERIFICATORREG) != null) {
					delRoles.add(mapRoles.get(AppUtils.ANGGOTA_ROLE_VERIFICATORREG));
				}
			}
			
			if (chkTimP2kb.isChecked()) {
				if (mapRoles.get(AppUtils.ANGGOTA_ROLE_TIMP2KB) == null) {
					Tanggotaroles role = new Tanggotaroles();
					role.setTanggota(objForm);
					role.setMusergroup(mapUsergroup.get(AppUtils.ANGGOTA_ROLE_TIMP2KB));
					addRoles.add(role);
				}
			} else {
				if (mapRoles.get(AppUtils.ANGGOTA_ROLE_TIMP2KB) != null) {
					delRoles.add(mapRoles.get(AppUtils.ANGGOTA_ROLE_TIMP2KB));
				}
			}
			
			if (chkKomisiP2kb.isChecked()) {
				if (mapRoles.get(AppUtils.ANGGOTA_ROLE_KOMISIP2KB) == null) {
					Tanggotaroles role = new Tanggotaroles();
					role.setTanggota(objForm);
					role.setMusergroup(mapUsergroup.get(AppUtils.ANGGOTA_ROLE_KOMISIP2KB));
					addRoles.add(role);
				}
			} else {
				if (mapRoles.get(AppUtils.ANGGOTA_ROLE_KOMISIP2KB) != null) {
					delRoles.add(mapRoles.get(AppUtils.ANGGOTA_ROLE_KOMISIP2KB));
				}
			}
			
			for (Tanggotaroles role: delRoles) {
				tanggotarolesDao.delete(session, role);
			}
			
			for (Tanggotaroles role: addRoles) {
				tanggotarolesDao.save(session, role);
			}
			
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
