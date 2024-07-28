package com.sds.hakli.viewmodel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlNativeComponent;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TeventDAO;
import com.sds.hakli.dao.TeventregDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tevent;
import com.sds.hakli.domain.Teventreg;
import com.sds.hakli.extension.MailHandler;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.StringUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class EventRegInfoVm {
	
	private TanggotaDAO oDao = new TanggotaDAO();
	private TeventDAO eventDao = new TeventDAO();
	private TeventregDAO eventregDao = new TeventregDAO();
	
	private Tevent obj;
	private Tanggota anggota;
	private String eventimg;
	private String eventtype;
	private String email;
	private String nik;
	private String ismember;
	private String dateregister;
	
	private DateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
	
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
			} else {
				divRegistered.setVisible(false);
				btReg.setVisible(true);
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
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								Teventreg eventreg = new Teventreg();
								eventreg.setTevent(obj);
								eventreg.setTanggota(anggota);
								eventreg.setVacreatedat(new Date());
								new TeventregDAO().save(session, eventreg);
								trx.commit();
								
								String bodymail = obj.getBodymail();
								new Thread(new MailHandler(eventreg, obj.getEventname(), anggota.getEmail(), null, bodymail)).start();
								
								Clients.showNotification("Submit data pendaftaran berhasil. Silakan cek e-mail Anda untuk mendapatkan informasi pendaftaran Anda.", "info", null, "middle_center", 3000);
								
								doClose();
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
		});
		
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
}
