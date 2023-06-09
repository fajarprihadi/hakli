package com.sds.hakli.viewmodel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.dao.TeventDAO;
import com.sds.hakli.domain.Tevent;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class EventFormVm {
	
	private TeventDAO dao = new TeventDAO();
	private Tevent objForm;
	private boolean isInsert;
	
	private Media media;

	@Wire
	private Window winEventform;
	@Wire
	private Image photo;
	@Wire
	private Grid gridSharefee;
	@Wire
	private Checkbox chkSharefee;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tevent obj) {
		Selectors.wireComponents(view, this, false);
		doReset();
		if (obj != null) {
			isInsert = false;
			objForm = obj;
			photo.setSrc(AppUtils.PATH_EVENT + "/" + objForm.getEventimg());
			chkSharefee.setChecked(objForm.getIssharefee() != null && objForm.getIssharefee().equals("Y") ? true : false);
		}
		doCheckSharefee(objForm.getIssharefee() != null && objForm.getIssharefee().equals("Y") ? true : false);
	}
	
	public void doReset() {
		isInsert = true;
		objForm = new Tevent();
	}
	
	@Command
	public void doCheckSharefee(@BindingParam("ischeck") Boolean ischeck) {
		if (ischeck) {
			gridSharefee.setVisible(true);
		} else {
			gridSharefee.setVisible(false);
		}
	}
	
	@Command
	public void doUploadPhoto(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
		try {
			UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
			media = event.getMedia();
			if (media instanceof org.zkoss.image.Image) {
				photo.setContent((org.zkoss.image.Image) media);
				photo.setVisible(true);
			} else {
				media = null;
				Messagebox.show("Not an image: " + media, WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doSave() {
		Session session = StoreHibernateUtil.openSession();
		Transaction trx = session.beginTransaction();
		try {
			if (media != null) {
				try {
					String eventid = new TcounterengineDAO().getLastCounter("EVT" + new SimpleDateFormat("yyMM").format(new Date()), 3);
					String imgid = eventid + "." + media.getFormat();
					String folder = Executions.getCurrent().getDesktop().getWebApp()
							.getRealPath(AppUtils.PATH_EVENT);
					if (media.isBinary()) {
						Files.copy(new File(folder + "/" + imgid), media.getStreamData());
					} else {
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(folder + "/" + imgid));
						Files.copy(writer, media.getReaderData());
						writer.close();
					}
					objForm.setEventid(eventid);
					objForm.setEventimg(imgid);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			objForm.setIssharefee(chkSharefee.isChecked() == true ? "Y" : "N");
			if (!chkSharefee.isChecked()) {
				objForm.setFeepusat(null);
				objForm.setFeeprov(null);
				objForm.setFeekab(null);
			}
			dao.save(session, objForm);
			trx.commit();
			
			if (isInsert)
				Clients.showNotification("Pembuatan data event berhasil", "info", null, "middle_center", 1500);
			else Clients.showNotification("Pembaruan data event berhasil", "info", null, "middle_center", 1500);
			doClose();
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.ERROR);
		} finally {
			session.close();
		}
	}
	
	@Command()
	@NotifyChange("*")
	public void doClose() {
		Event closeEvent = new Event("onClose", winEventform, null);
		Events.postEvent(closeEvent);
	}
	
	public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String eventname = (String) ctx.getProperties("eventname")[0].getValue();
				if (eventname == null || "".equals(eventname.trim()))
					this.addInvalidMessage(ctx, "eventname", Labels.getLabel("common.validator.empty"));
				String eventtype = (String) ctx.getProperties("eventtype")[0].getValue();
				if (eventtype == null || "".equals(eventtype.trim()))
					this.addInvalidMessage(ctx, "eventtype", Labels.getLabel("common.validator.empty"));
				String eventdesc = (String) ctx.getProperties("eventdesc")[0].getValue();
				if (eventdesc == null || "".equals(eventdesc.trim()))
					this.addInvalidMessage(ctx, "eventdesc", Labels.getLabel("common.validator.empty"));
				Date eventdate = (Date) ctx.getProperties("eventdate")[0].getValue();
				if (eventdate == null)
					this.addInvalidMessage(ctx, "eventdate", Labels.getLabel("common.validator.empty"));
				String eventlocation = (String) ctx.getProperties("eventlocation")[0].getValue();
				if (eventlocation == null || "".equals(eventlocation.trim()))
					this.addInvalidMessage(ctx, "eventlocation", Labels.getLabel("common.validator.empty"));
				String eventcity = (String) ctx.getProperties("eventcity")[0].getValue();
				if (eventcity == null || "".equals(eventcity.trim()))
					this.addInvalidMessage(ctx, "eventcity", Labels.getLabel("common.validator.empty"));
				BigDecimal eventprice = (BigDecimal) ctx.getProperties("eventprice")[0].getValue();
				if (eventprice == null)
					this.addInvalidMessage(ctx, "eventprice", Labels.getLabel("common.validator.empty"));
				Date closedate = (Date) ctx.getProperties("closedate")[0].getValue();
				if (closedate == null)
					this.addInvalidMessage(ctx, "closedate", Labels.getLabel("common.validator.empty"));
				if (isInsert && media == null)
					this.addInvalidMessage(ctx, "eventimg", Labels.getLabel("common.validator.empty"));
				
				if (chkSharefee.isChecked()) {
					BigDecimal feepusat = (BigDecimal) ctx.getProperties("feepusat")[0].getValue();
					if (feepusat == null)
						this.addInvalidMessage(ctx, "sharefee", Labels.getLabel("common.validator.empty"));
					BigDecimal feeprov = (BigDecimal) ctx.getProperties("feeprov")[0].getValue();
					if (feeprov == null)
						this.addInvalidMessage(ctx, "sharefee", Labels.getLabel("common.validator.empty"));
					BigDecimal feekab = (BigDecimal) ctx.getProperties("feekab")[0].getValue();
					if (feekab == null)
						this.addInvalidMessage(ctx, "sharefee", Labels.getLabel("common.validator.empty"));
					
					if (eventprice != null && feepusat != null && feeprov != null && feekab != null) {
						if (eventprice.compareTo(feepusat.add(feeprov).add(feekab)) != 0) {
							this.addInvalidMessage(ctx, "sharefee", "Jumlah Sharing Fee tidak sama dengan Jumlah Biaya Pendaftaran");
						}
					}
				}
				
			}
		};
	}

	public Tevent getObjForm() {
		return objForm;
	}

	public void setObjForm(Tevent objForm) {
		this.objForm = objForm;
	}
	
	
}
