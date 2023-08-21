package com.sds.hakli.viewmodel;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

import com.sds.hakli.bean.ChangePass;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.utils.db.StoreHibernateUtil;

public class AnggotaChangePassVm {

	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota oUser;
	
	private TanggotaDAO oDao = new TanggotaDAO();
	
	private ChangePass objForm;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
		try {
			oUser = (Tanggota) zkSession.getAttribute("anggota");
			objForm = new ChangePass();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doSubmit() {
		Session session = StoreHibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		try {
			Tanggota obj = oDao.findByPk(oUser.getTanggotapk());
			obj.setPassword(objForm.getPassnew());
			oDao.save(session, obj);
			tx.commit();
			
			objForm = new ChangePass();
			
			Clients.showNotification("Proses perubahan password berhasil", "info", null, "middle_center", 1500);
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.ERROR);
		} finally {
			session.close();
		}
	}
	
	public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String passold = (String) ctx.getProperties("passold")[0].getValue();
				if (passold == null || "".equals(passold.trim()))
					this.addInvalidMessage(ctx, "passold", Labels.getLabel("common.validator.empty"));
				else {
					try {
						Tanggota obj = oDao.findByPk(oUser.getTanggotapk());
						if (obj != null && !obj.getPassword().equals(passold.trim())) {
							this.addInvalidMessage(ctx, "passold", "Password tidak sesuai");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				String passnew = (String) ctx.getProperties("passnew")[0].getValue();
				String passnewconfirm = (String) ctx.getProperties("passnewconfirm")[0].getValue();
				if (passnewconfirm == null || "".equals(passnewconfirm.trim()))
					this.addInvalidMessage(ctx, "passnewconfirm", Labels.getLabel("common.validator.empty"));
				if (passnew == null || "".equals(passnew.trim()))
					this.addInvalidMessage(ctx, "passnew", Labels.getLabel("common.validator.empty"));
				if (passnew != null && passnewconfirm != null) {
					if (passnew.length() < 8)
						this.addInvalidMessage(ctx, "passnew", "Minimal 8 karakter");
					else if (passnewconfirm == null || "".equals(passnewconfirm.trim()))
						this.addInvalidMessage(ctx, "passnewconfirm", Labels.getLabel("common.validator.empty"));
					else {
						if (!passnew.trim().equals(passnewconfirm.trim()))
							this.addInvalidMessage(ctx, "passnewconfirm", "Konfirmasi password baru tidak sesuai");
					}
				}
			}
		};
	}

	public ChangePass getObjForm() {
		return objForm;
	}

	public void setObjForm(ChangePass objForm) {
		this.objForm = objForm;
	}
}
