package com.sds.hakli.viewmodel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zhtml.H2;
import org.zkoss.zhtml.H4;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlNativeComponent;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
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
import com.sds.utils.AppUtils;
import com.sds.utils.StringUtils;

public class EventInitVm {
	
	private TanggotaDAO oDao = new TanggotaDAO();
	private TeventDAO eventDao = new TeventDAO();
	private TeventregDAO eventregDao = new TeventregDAO();
	
	private Tevent obj;
	private String eventimg;
	private String email;
	private String nik;
	private String ismember;
	
	private DateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
	
	@Wire
	private Window winInit;
	@Wire
	private H2 title;
	@Wire
	private H4 subtitle;
	//@Wire
	//private Checkbox chkSisdmk;
	@Wire
	private Div divReg;
	@Wire
	private Div divMember;
	@Wire
	private Combobox cbDobDay;
	@Wire
	private Combobox cbDobMonth;
	@Wire
	private Combobox cbDobYear;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			Execution exec = Executions.getCurrent();
		    String id = exec.getParameter("id");
		    if (id != null) {
		    	obj = eventDao.findByFilter("eventid = '" + id + "'");
		    	if (obj != null) {
		    		eventimg = AppUtils.PATH_EVENT + "/" + obj.getEventimg();
		    		title.appendChild(new Html(obj.getEventname()));
		    		subtitle.appendChild(new Html(obj.getEventdesc()));
		    		dateFiller();
		    		if (obj.getClosedate().compareTo(new Date()) == -1) {
		    			divReg.getChildren().clear();
		    			HtmlNativeComponent h3 = new HtmlNativeComponent("h3");
		    			h3.appendChild(new Html("Maaf, Pendaftaran sudah ditutup per tanggal " + 
		    					new SimpleDateFormat("dd-MM-yyyy HH:mm").format(obj.getClosedate()) + " WIB"));
		    			divReg.appendChild(h3);
		    		} else {
		    			if (obj.getIsmember().equals("N"))
		    				divMember.setVisible(true);
		    			else divMember.setVisible(false);
		    		}
		    		
		    	} else Executions.sendRedirect("/timeout.zul");
		    } else {
		    	Executions.sendRedirect("/timeout.zul");
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void dateFiller() {
		try {
			for (int i = 1; i <= 31; i++) {
				Comboitem item = new Comboitem(String.valueOf(i));
				cbDobDay.appendChild(item);
			}
			for (int i = 1; i <= 12; i++) {
				Comboitem item = new Comboitem(StringUtils.getMonthLabel(i));
				cbDobMonth.appendChild(item);
			}
			Calendar calYear = Calendar.getInstance();
			calYear.add(Calendar.YEAR, -15);
			int yearend = calYear.get(Calendar.YEAR);
			for (int i = 1940; i <= yearend; i++) {
				Comboitem item = new Comboitem(String.valueOf(i));
				cbDobYear.appendChild(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doSubmit() {
		Date dob = null;
		if (cbDobDay.getValue().trim().length() > 0 && cbDobMonth.getValue().trim().length() > 0
				&& cbDobYear.getValue().trim().length() > 0) {
			String strDob = cbDobYear.getValue().toString() + "/" + (cbDobMonth.getSelectedIndex() + 1) + "/"
					+ cbDobDay.getValue().toString();
			try {
				dob = dateFormatter.parse(strDob);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if (nik == null || nik.trim().length() == 0) {
			Messagebox.show("Silakan masukkan NIK Anda", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.INFORMATION);
		} else if (!StringUtils.digitKtpValidator(nik)) { 
			Messagebox.show("Data NIK tidak sesuai. Periksa kembali data input NIK Anda", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.EXCLAMATION);
		} else if (dob == null) { 
			Messagebox.show("Silakan lengkapi data tanggal lahir Anda", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.EXCLAMATION);
		} else if (divMember.isVisible() && ismember == null) {
			Messagebox.show("Silakan pilih status kenaggotaan Hakli", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.EXCLAMATION);
		} else {
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("event", obj);
				//map.put("isCheckSisdmk", chkSisdmk.isChecked());
				String page = "/view/anggota/anggotaadd.zul";
				Tanggota obj = null;
				List<Tanggota> objList = oDao.listByFilter("noktp = '" + nik.trim() + "'", "tanggotapk desc");
				
				boolean isValid = true;
				if (this.obj.getIsmember() != null && this.obj.getIsmember().equals("Y")) {
					if (objList.size() == 0)
						isValid = false;
					else {
						obj = objList.get(0);
						if (obj.getStatusreg() == null | !obj.getStatusreg().equals("3"))
							isValid = false;
					}
				} else if (divMember.isVisible()) {
					if (ismember.equals("Y")) {
						if (objList.size() == 0)
							isValid = false;
						else {
							obj = objList.get(0);
							if (obj.getStatusreg() == null | !obj.getStatusreg().equals("3"))
								isValid = false;
						}
					} else if (ismember.equals("N")) 
						page = "/view/event/eventpubreg.zul";
				}
				
				if (!isValid) {
					Messagebox.show("Data Anda tidak terdaftar pada keanggotaan HAKLI", WebApps.getCurrent().getAppName(), Messagebox.OK,
							Messagebox.EXCLAMATION);
				} else {					
					if (objList.size() > 0) {
						obj = objList.get(0);
						
						if (objList.get(0).getTgllahir().compareTo(dob) == 0) {
							Teventreg eventreg = eventregDao.findByFilter("tevent.teventpk = " + this.obj.getTeventpk() + " and tanggota.tanggotapk = " + obj.getTanggotapk());
							if (eventreg != null) {
								map.put("obj", eventreg);
								page = "/view/event/eventregdone.zul";
							} else map.put("obj", obj);
						} else {
							isValid = false;
							Messagebox.show("Data yang Anda input tidak sesuai dengan data yang telah tersimpan pada sistem HAKLI", WebApps.getCurrent().getAppName(), Messagebox.OK,
									Messagebox.EXCLAMATION);
						}
					} else {
						obj = new Tanggota();
						obj.setNoktp(nik);
						obj.setTgllahir(dob);
						map.put("obj", obj);
					}
					if (isValid) {
						winInit.getChildren().clear();
						Executions.createComponents(page, winInit, map);
					}					
				}
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
}
