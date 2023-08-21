package com.sds.hakli.viewmodel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.zkoss.lang.Threads;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.dao.TeventDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tevent;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class EventFormVm {
	
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	
	private Tanggota anggota;
	private TeventDAO dao = new TeventDAO();
	private Tevent objForm;
	private boolean isInsert;
	
	private String title;
	private Media media;
	private Media mediaCert;

	@Wire
	private Window winEventform;
	@Wire
	private Image photo;
	@Wire
	private Image imgCert;
	@Wire
	private Grid gridSharefee;
	@Wire
	private Checkbox chkSharefee;
	@Wire
	private Decimalbox dbPrice;
	@Wire
	private Intbox iboxSkp;
	@Wire
	private Row rowCert;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tevent obj) {
		Selectors.wireComponents(view, this, false);
		
		anggota = (Tanggota) zkSession.getAttribute("anggota");
		doReset();
		if (obj != null) {
			title = "Edit Event";
			isInsert = false;
			objForm = obj;
			doFree(objForm.getIsfree());
			doCert(objForm.getIscert());
			if (objForm.getEventimg() != null) {
				File file = new File(Executions.getCurrent().getDesktop().getWebApp()
						.getRealPath(AppUtils.PATH_EVENT) + "/" + objForm.getEventimg());
				if (file.exists()) {
					photo.setSrc(AppUtils.PATH_EVENT + "/" + objForm.getEventimg());
				}
			}
			if (objForm.getCertpath() != null && objForm.getCertpath().length() > 0) {
				File file = new File(Executions.getCurrent().getDesktop().getWebApp()
						.getRealPath("/") + objForm.getCertpath());
				if (file.exists()) {
					imgCert.setSrc(objForm.getCertpath());
				}
			}
			chkSharefee.setChecked(objForm.getIssharefee() != null && objForm.getIssharefee().equals("Y") ? true : false);
		} else title = "Buat Event";
		doCheckSharefee(objForm.getIssharefee() != null && objForm.getIssharefee().equals("Y") ? true : false);
	}
	
	public void doReset() {
		isInsert = true;
		objForm = new Tevent();
		objForm.setIsfree("N");
		objForm.setIsmember("N");
	}
	
	@Command
	public void doFree(@BindingParam("val") String val) {
		if (val != null) {
			if (val.equals("Y")) {
				dbPrice.setValue(new BigDecimal(0));
				dbPrice.setDisabled(true);
				chkSharefee.setChecked(false);
				chkSharefee.setDisabled(true);
				doCheckSharefee(false);
			} else {
				dbPrice.setDisabled(false);
				chkSharefee.setDisabled(false);
			}
		}
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
	public void doSkp(@BindingParam("val") String val) {
		if (val != null) {
			if (val.equals("Y")) {				
				iboxSkp.setDisabled(false);
			} else {
				iboxSkp.setValue(0);
				iboxSkp.setDisabled(true);
			}
		}
	}
	
	@Command
	public void doCert(@BindingParam("val") String val) {
		if (val != null) {
			if (val.equals("Y")) {
				rowCert.setVisible(true);
			} else {
				rowCert.setVisible(false);
			}
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
				Messagebox.show("Not an image: " + media.getName(), WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.ERROR);
				media = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doUploadCert(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
		try {
			UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
			mediaCert = event.getMedia();
			if (mediaCert instanceof org.zkoss.image.Image) {
				imgCert.setContent((org.zkoss.image.Image) mediaCert);
				imgCert.setVisible(true);
			} else {
				Messagebox.show("Not an image: " + mediaCert.getName(), WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.ERROR);
				mediaCert = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doPreview() {
		try {
			boolean isValid = true;
			String folder = Executions.getCurrent().getDesktop().getWebApp()
					.getRealPath("/");
			String certFilename = "";
			if (mediaCert != null) {
				certFilename = AppUtils.PATH_EVENT + "Preview_" + anggota.getNoanggota() + "." + mediaCert.getFormat();
				if (mediaCert.isBinary()) {
					Files.copy(new File(folder + "/" + certFilename), mediaCert.getStreamData());
				} else {
					BufferedWriter writer = new BufferedWriter(
							new FileWriter(folder + "/" + certFilename));
					Files.copy(writer, mediaCert.getReaderData());
					writer.close();
				}
			} else {
				if (isInsert) {
					isValid = false;
					Messagebox.show("Silakan upload template sertifikat", WebApps.getCurrent().getAppName(), Messagebox.OK,
							Messagebox.EXCLAMATION);
				} else {
					File file = new File(folder + objForm.getCertpath());
					if (file.exists()) {
						certFilename = objForm.getCertpath();
					} else {
						isValid = false;
						Messagebox.show("Silakan upload template sertifikat", WebApps.getCurrent().getAppName(), Messagebox.OK,
								Messagebox.EXCLAMATION);
					}
				}
			}
			
			if (isValid) {
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("NAMA", "Fajar Prihadi, ST");
				parameters.put("TEMPLATEPATH", folder + "/" + certFilename);
				
				List<Tanggota> objList = new ArrayList<>();
				objList.add(new Tanggota());
				zkSession.setAttribute("objList", objList);
				zkSession.setAttribute("parameters", parameters);
				zkSession.setAttribute("reportPath", Executions.getCurrent().getDesktop().getWebApp()
						.getRealPath(AppUtils.PATH_JASPER + "/cert.jasper"));
				Executions.getCurrent().sendRedirect("/view/jasperviewer.zul", "_blank");
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
			String folder = Executions.getCurrent().getDesktop().getWebApp()
					.getRealPath(AppUtils.PATH_EVENT);
			String eventid = "";
			if (isInsert)
				eventid = new TcounterengineDAO().getLastCounter("EVT" + new SimpleDateFormat("yyMM").format(new Date()), 3);
			else eventid = objForm.getEventid();
			if (media != null) {
				try {
					String imgid = eventid + "." + media.getFormat();
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
			
			if (mediaCert != null) {
				try {
					if (objForm.getIscert().equals("Y")) {
						String certFilename = mediaCert.getName();
						if (certFilename.length() > 80)
							certFilename = certFilename.substring(certFilename.length()-80, certFilename.length());
						certFilename = eventid + new SimpleDateFormat("HHmmss").format(new Date()) + "_" + certFilename;
						String certpath = AppUtils.PATH_EVENT + "/" + certFilename;
						if (mediaCert.isBinary()) {
							Files.copy(new File(folder + "/" + certFilename), mediaCert.getStreamData());
						} else {
							BufferedWriter writer = new BufferedWriter(
									new FileWriter(folder + "/" + certFilename));
							Files.copy(writer, mediaCert.getReaderData());
							writer.close();
						}
						objForm.setCertpath(certpath);
					}
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
				String isfree = (String) ctx.getProperties("isfree")[0].getValue();
				BigDecimal eventprice = (BigDecimal) ctx.getProperties("eventprice")[0].getValue();
				if (eventprice == null && isfree.equals("N"))
					this.addInvalidMessage(ctx, "eventprice", Labels.getLabel("common.validator.empty"));
				Date closedate = (Date) ctx.getProperties("closedate")[0].getValue();
				if (closedate == null)
					this.addInvalidMessage(ctx, "closedate", Labels.getLabel("common.validator.empty"));
				String isskp = (String) ctx.getProperties("isskp")[0].getValue();
				if (isskp == null || "".equals(isskp.trim()))
					this.addInvalidMessage(ctx, "isskp", Labels.getLabel("common.validator.empty"));
				else {
					if (isskp.equals("Y")) {
						Integer skp = (Integer) ctx.getProperties("skp")[0].getValue();
						if (skp == null || skp <= 0)
							this.addInvalidMessage(ctx, "skp", Labels.getLabel("common.validator.empty"));
					}
				}
				String iscert = (String) ctx.getProperties("iscert")[0].getValue();
				if (iscert == null || "".equals(iscert.trim()))
					this.addInvalidMessage(ctx, "iscert", Labels.getLabel("common.validator.empty"));
				else {
					if (isInsert && iscert.equals("Y") && mediaCert == null)
						this.addInvalidMessage(ctx, "imgCert", Labels.getLabel("common.validator.empty"));
				}
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
