package com.sds.hakli.viewmodel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.MuniversitasDAO;
import com.sds.hakli.dao.Tp2kbbookDAO;
import com.sds.hakli.domain.Muniversitas;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tp2kbbook;
import com.sds.utils.db.StoreHibernateUtil;

public class BukuLogFormVm {

	private Tanggota obj;
	private Tp2kbbook objForm;

	private String tgllahir;
	private String gender;

	private boolean isInsert;

	@Wire
	private Window winLogForm;
	@Wire
	private Combobox cbUniversitas;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tanggota obj,
			@ExecutionArgParam("objForm") Tp2kbbook objForm) {
		Selectors.wireComponents(view, this, false);

		if (obj != null) {
			this.obj = obj;
			tgllahir = obj.getTempatlahir() + " / " + new SimpleDateFormat("dd MMMMM yyyy").format(obj.getTgllahir());
			gender = obj.getGender().trim().equals("P") ? "Wanita" : "Pria";
		}
		doReset();

		if (objForm != null) {
			this.objForm = objForm;
			cbUniversitas.setValue(objForm.getMuniversitas().getUniversitas());
			isInsert = false;
		}

	}

	@Command
	@NotifyChange("*")
	public void doSave() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(objForm.getTglakhir());
		cal.add(Calendar.YEAR, -5);

		try {
			List<Tp2kbbook> tpb = new Tp2kbbookDAO().listByFilter("TANGGOTAFK = " + obj.getTanggotapk() + " AND (('"
					+ new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime())
					+ "' BETWEEN TGLMULAI AND TGLAKHIR) OR ('"
					+ new SimpleDateFormat("yyyy-MM-dd").format(objForm.getTglakhir())
					+ "' BETWEEN TGLMULAI AND TGLAKHIR))", "tp2kbbookpk");

			if (tpb.size() > 0) {
				Messagebox.show("Tidak bisa menambah permohonan baru, karena terdapat permohonan yang masih aktif.");
			} else {
				Session session = StoreHibernateUtil.openSession();
				Transaction trx = session.beginTransaction();
				
				objForm.setTanggota(obj);
				objForm.setStatus("O");

				objForm.setTglmulai(cal.getTime());
				if (isInsert) {
					objForm.setCreatetime(new Date());
					objForm.setCreatedby(obj.getNoanggota());
				} else {
					objForm.setLastupdated(new Date());
					objForm.setUpdatedby(obj.getNoanggota());
				}

				new Tp2kbbookDAO().save(session, objForm);
				trx.commit();
				session.close();
				
				if (isInsert) {
					Clients.showNotification("Proses simpan data berhasil", "info", null, "middle_center", 1500);
				} else {
					Clients.showNotification("Proses pembaruan data berhasil", "info", null, "middle_center", 1500);
				}

				doClose();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
		}
	}

	public void doReset() {
		objForm = new Tp2kbbook();
		isInsert = true;
	}

	public void doClose() {
		Event closeEvent = new Event("onClose", winLogForm, null);
		Events.postEvent(closeEvent);
	}

	public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String nostr = (String) ctx.getProperties("nostr")[0].getValue();
				if (nostr == null || "".equals(nostr.trim()))
					this.addInvalidMessage(ctx, "nostr", Labels.getLabel("common.validator.empty"));

				Date tglakhir = (Date) ctx.getProperties("tglakhir")[0].getValue();
				if (tglakhir == null)
					this.addInvalidMessage(ctx, "tglakhir", Labels.getLabel("common.validator.empty"));
				
				Date tgllulus = (Date) ctx.getProperties("tgllulus")[0].getValue();
				if (tgllulus == null)
					this.addInvalidMessage(ctx, "tgllulus", Labels.getLabel("common.validator.empty"));
				
				Muniversitas muniversitas = (Muniversitas) ctx.getProperties("muniversitas")[0].getValue();
				if (muniversitas == null)
					this.addInvalidMessage(ctx, "muniversitas", Labels.getLabel("common.validator.empty"));
				
			}
		};
	}
	
	public ListModelList<Muniversitas> getUniversitasModel() {
		ListModelList<Muniversitas> oList = null;
		try {
			oList = new ListModelList<>(new MuniversitasDAO().listAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	public Tanggota getObj() {
		return obj;
	}

	public void setObj(Tanggota obj) {
		this.obj = obj;
	}

	public Tp2kbbook getObjForm() {
		return objForm;
	}

	public void setObjForm(Tp2kbbook objForm) {
		this.objForm = objForm;
	}

	public String getTgllahir() {
		return tgllahir;
	}

	public void setTgllahir(String tgllahir) {
		this.tgllahir = tgllahir;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
}
