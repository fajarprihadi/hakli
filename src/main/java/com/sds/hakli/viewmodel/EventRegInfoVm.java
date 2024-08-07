package com.sds.hakli.viewmodel;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zhtml.H2;
import org.zkoss.zhtml.H4;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.TeventregDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tevent;
import com.sds.hakli.domain.Teventreg;
import com.sds.hakli.extension.MailHandler;
import com.sds.hakli.handler.NaskahHandler;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class EventRegInfoVm {
	
	private TeventregDAO eventregDao = new TeventregDAO();
	
	private Tevent obj;
	private Tanggota anggota;
	private String eventimg;
	private String eventtype;
	private String email;
	private String nik;
	private String ismember;
	private String regcode;
	private String dateregister;
	private Teventreg eventreg;
	
	@Wire
	private Window winEventRegInfo;
	@Wire
	private H2 title;
	@Wire
	private H4 subtitle;
	@Wire
	private Div divReg;
	@Wire
	private Div divMember;
	@Wire
	private Div divRegistered;
	@Wire
	private Button btReg;
	@Wire
	private Div divRegcode;
	@Wire
	private Button btEtik;
	@Wire
	private Button btCert;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tevent obj, 
			@ExecutionArgParam("anggota") Tanggota anggota, @ExecutionArgParam("dateregister") String dateregister) {
		Selectors.wireComponents(view, this, false);
		try {
			this.obj = obj;
			this.anggota = anggota;
			this.dateregister = dateregister;
			eventimg = AppUtils.PATH_EVENT + "/" + obj.getEventimg();
			eventtype = AppData.getEventType(obj.getEventtype());
			if (dateregister != null) {
				divRegistered.setVisible(true);
				btReg.setVisible(false);
				divRegcode.setVisible(false);
				
				eventreg = eventregDao.findByFilter("tevent.teventpk = " + obj.getTeventpk() + " and tanggota.tanggotapk = " + anggota.getTanggotapk());
				
				if (((eventreg.getTevent().getIsfree() != null && eventreg.getTevent().getIsfree().equals("Y")) || eventreg.getIspaid().equals("Y")) && eventreg.getTevent().getDocactivedate().compareTo(new Date()) <= 0) {
					if (eventreg.getTevent().getEventtype().equals(AppUtils.EVENTTYPE_SUMPAHPROFESI)) {
						btEtik.setVisible(true);
					}
					
					if (eventreg.getTevent().getIscert() != null && eventreg.getTevent().getIscert().equals("Y")) {
						btCert.setVisible(true);
					}
				} 
				
			} else {
				divRegistered.setVisible(false);
				btReg.setVisible(true);
				if (obj.getRegcode() != null && !obj.getRegcode().equals(""))
					divRegcode.setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doSubmit() {
		Messagebox.show("Apakah Anda ingin mendaftar dan data Anda sudah benar? Jika sudah benar silahkan pilih OK untuk mengirim data registrasi dan Anda akan menerima e-mail informasi pendaftaran", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals("onOK")) {
							boolean isVerified = true;
							if (obj.getRegcode() != null && !obj.getRegcode().equals(""))
								isVerified = false;
							if (isVerified || (regcode != null && regcode.equals(obj.getRegcode()))) {
								Session session = StoreHibernateUtil.openSession();
								Transaction trx = session.beginTransaction();
								try {
									Teventreg eventreg = new Teventreg();
									eventreg.setTevent(obj);
									eventreg.setTanggota(anggota);
									eventreg.setVacreatedat(new Date());
									eventreg.setIspaid("N");
									eventregDao.save(session, eventreg);
									trx.commit();
									
									if (obj.getEventprice() == null || obj.getEventprice().compareTo(new BigDecimal(0))  == 0) {
										String bodymail = obj.getBodymail();
										new Thread(new MailHandler(eventreg, obj.getEventname(), anggota.getEmail(), null, bodymail)).start();
									}
									
									Clients.showNotification("Submit data pendaftaran berhasil. Silakan cek e-mail Anda untuk mendapatkan informasi pendaftaran Anda.", "info", null, "middle_center", 3000);
									doClose();
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									session.close();
								}
							} else {
								Messagebox.show("Invalid Registration Code", WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.EXCLAMATION);
							}
							
						}
					}
		});
		
	}
	
	@Command
	public void doEtika() {
		try {
			if (eventreg != null) {
				new NaskahHandler().downloadNaskah(eventreg, "etik");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
		}		
	}
	
	@Command
	public void doCert() {
		try {			
			if (eventreg != null) {
				new NaskahHandler().downloadSertifikat(eventreg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
		}	
	}
	
	public void doClose() {
		Event closeEvent = new Event("onClose", winEventRegInfo, null);
		Events.postEvent(closeEvent);
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

	public String getNik() {
		return nik;
	}

	public void setNik(String nik) {
		this.nik = nik;
	}

	public String getIsmember() {
		return ismember;
	}

	public void setIsmember(String ismember) {
		this.ismember = ismember;
	}

	public Tevent getObj() {
		return obj;
	}

	public void setObj(Tevent obj) {
		this.obj = obj;
	}

	public Tanggota getAnggota() {
		return anggota;
	}

	public void setAnggota(Tanggota anggota) {
		this.anggota = anggota;
	}

	public String getEventtype() {
		return eventtype;
	}

	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}

	public String getDateregister() {
		return dateregister;
	}

	public void setDateregister(String dateregister) {
		this.dateregister = dateregister;
	}

	public String getRegcode() {
		return regcode;
	}

	public void setRegcode(String regcode) {
		this.regcode = regcode;
	}
}
