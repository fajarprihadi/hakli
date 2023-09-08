package com.sds.hakli.viewmodel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
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
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.bean.SisdmkBean;
import com.sds.hakli.dao.MkabDAO;
import com.sds.hakli.dao.MprovDAO;
import com.sds.hakli.dao.MusergroupDAO;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.dao.TeventregDAO;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.domain.AnggotaReg;
import com.sds.hakli.domain.Mkab;
import com.sds.hakli.domain.Mprov;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tevent;
import com.sds.hakli.domain.Teventreg;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.extension.BriApiExt;
import com.sds.hakli.extension.MailHandler;
import com.sds.hakli.extension.SisdmkApiExt;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.BrivaCreateResp;
import com.sds.hakli.pojo.BrivaData;
import com.sds.hakli.pojo.SisdmkData;
import com.sds.hakli.pojo.SisdmkToken;
import com.sds.hakli.utils.InvoiceGenerator;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.StringUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class EventPubRegVm {

	private TanggotaDAO anggotaDao = new TanggotaDAO();
	private MprovDAO provDao = new MprovDAO();
	private MkabDAO kabDao = new MkabDAO();
	private TeventregDAO eventregDao = new TeventregDAO();

	private AnggotaReg objForm;
	private Tevent tevent;
	private Mprov region;
	private Mprov provrumah;
	private Mkab kabrumah;
	private ListModelList<Mkab> kabrumahModel;
	
	public Boolean isInsert;
	private Date dob;
	private Boolean isView;
	
	private DateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");

	@Wire
	private Window winAnggotaAdd;
	@Wire
	private Div divForm;
	@Wire
	private Image imgEvent;
	@Wire
	private Combobox cbProvrumah;
	@Wire
	private Combobox cbKabrumah;
	@Wire
	private Combobox cbDobDay;
	@Wire
	private Combobox cbDobMonth;
	@Wire
	private Combobox cbDobYear;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tanggota obj, 
			@ExecutionArgParam("event") Tevent tevent, @ExecutionArgParam("isCheckSisdmk") Boolean isCheckSisdmk) {
		Selectors.wireComponents(view, this, false);
		try {
			if (obj == null) {
				Executions.sendRedirect("/timeout.zul");
			} else {
				init();
				doReset();
				objForm.setPribadi(obj);
				
				if (tevent != null) {
					this.tevent = tevent;
					imgEvent.setSrc(AppUtils.PATH_EVENT + "/" + tevent.getEventimg());
					imgEvent.setVisible(true);
				}
				
				if (obj.getTanggotapk() == null && isCheckSisdmk != null && isCheckSisdmk)
					doSisdmk();
				
				dob = objForm.getPribadi().getTgllahir();
				if (dob != null) {
					cbDobDay.setValue(new SimpleDateFormat("dd").format(dob));
					cbDobMonth.setSelectedIndex(Integer.parseInt(new SimpleDateFormat("MM").format(dob)) - 1);
					cbDobYear.setValue(new SimpleDateFormat("yyyy").format(dob));
				}
				
				provrumah = provDao.findByFilter("provcode = '" + obj.getProvcode() + "'");
				if (provrumah != null) {
					cbProvrumah.setValue(provrumah.getProvname());
					doLoadKab(provrumah);
				}
				kabrumah = kabDao.findByFilter("provcode = '" + obj.getProvcode() + "' and kabcode = '" + obj.getKabcode() + "'");
				cbKabrumah.setValue(obj.getKabname());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() {
		try {
			for (int i = 1; i <= 31; i++) {
				Comboitem item = new Comboitem(String.valueOf(i));
				cbDobDay.appendChild(item);
			}
			for (int i = 1; i <= 12; i++) {
				Comboitem item = new Comboitem(StringUtils.getMonthLabel(i));
				cbDobMonth.appendChild(item);
			}
			int yearend = Calendar.getInstance().get(Calendar.YEAR);
			for (int i = 1940; i <= yearend; i++) {
				Comboitem item = new Comboitem(String.valueOf(i));
				cbDobYear.appendChild(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean doSisdmk() throws Exception {
		boolean isValid = false;
		try {
			SisdmkBean bean = AppData.getSisdmkParam();
			SisdmkApiExt api = new SisdmkApiExt();
			SisdmkToken sisdmkToken = api.getToken(bean);
			if (sisdmkToken != null && sisdmkToken.getStatus().equals("success")) {
				SisdmkData data = api.getBiodata(bean, sisdmkToken.getToken(), objForm.getPribadi().getNoktp());
				if (data != null && data.getStatus() == 200) {
					Tanggota anggota = new Tanggota();
					anggota.setGender(data.getData().getJenis_kelamin());
					anggota.setNama(data.getData().getNama());
					anggota.setNoktp(data.getData().getNik());
					anggota.setTgllahir(data.getData().getTanggal_lahir());
					anggota.setTempatlahir(data.getData().getTempat_lahir());
					
					objForm.setPribadi(anggota);
					
					isValid = true;
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isValid;
	}

	@Command
	@NotifyChange("kabrumahModel")
	public void doLoadKab(@BindingParam("prov") Mprov prov) {
		try {
			if (prov != null) {
				cbKabrumah.setValue(null);
				kabrumahModel = new ListModelList<>(
						new MkabDAO().listByFilter("provcode = '" + prov.getProvcode() + "'", "kabname"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@NotifyChange("*")
	public void doReset() {
		isInsert = true;
		objForm = new AnggotaReg();
		objForm.setPribadi(new Tanggota());
		dob = null;
		if (isView == null || !isView) {
			cbDobDay.setValue(null);
			cbDobMonth.setValue(null);
			cbDobYear.setValue(null);
		}
	}

	@Command
	@NotifyChange("*")
	public void doSave() {
		Messagebox.show("Apakah data yang anda isi sudah benar? Jika sudah benar silahkan pilih OK untuk mengirim data registrasi anda", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals("onOK")) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = null;
							Tinvoice inv = null;
							Teventreg eventreg = null;
							try {
								trx = session.beginTransaction();
								
								// anggota/pribadi
								if (objForm.getPribadi().getMusergroup() == null)
									objForm.getPribadi().setMusergroup(new MusergroupDAO().findByFilter("usergroupcode = '" + AppUtils.ANGGOTA_ROLE_ANGGOTABIASA + "'"));
								objForm.getPribadi().setTgllahir(dob);
								objForm.getPribadi().setProvcode(provrumah.getProvcode());
								objForm.getPribadi().setProvname(provrumah.getProvname());
								objForm.getPribadi().setKabcode(kabrumah.getKabcode());
								objForm.getPribadi().setKabname(kabrumah.getKabname());
								objForm.getPribadi().setCreatetime(new Date());
								anggotaDao.save(session, objForm.getPribadi());

								if (tevent != null) {
									Object objEvent = doSaveEvent(session);
									if (objEvent instanceof Tinvoice)
										inv = (Tinvoice) objEvent;
									else if (objEvent instanceof Teventreg)
										eventreg = (Teventreg) objEvent;
								} else {
									objForm.getPribadi().setStatusreg(AppUtils.STATUS_ANGGOTA_REG);
								}
								
								trx.commit();
								
								if (inv != null) {
									String bodymail_path = Executions.getCurrent().getDesktop().getWebApp()
											.getRealPath("/themes/mail/mailinv.html");
									new Thread(new MailHandler(inv, inv.getInvoicedesc().trim(), inv.getTanggota().getEmail(), bodymail_path)).start();
								} else {
									if (tevent != null && tevent.getIsfree().equals("Y")) {
										new Thread(new MailHandler(eventreg, tevent.getEventname(), objForm.getPribadi().getEmail(), null)).start();
									}
								}
								
								if (tevent != null) {
									Component comp = winAnggotaAdd.getParent();
									comp.getChildren().clear();
									divForm.getChildren().clear();
									Map<String, Object> map = new HashMap<>();
									map.put("obj", inv != null ? inv.getTeventreg() : eventreg);
									Executions.createComponents("/view/event/eventregdone.zul", comp, map);
								} else {
									divForm.getChildren().clear();
									Map<String, Object> map = new HashMap<>();
									map.put("obj", objForm);
									map.put("regstatus", "reg");
									Executions.createComponents("/regdone.zul", divForm, map);
								}
							} catch (Exception e) {								
								e.printStackTrace();
								trx.rollback();
								Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
										Messagebox.ERROR);
							} finally {
								session.close();
							}
						}
					}

				});
	}
	
	private Object doSaveEvent(Session session) throws Exception {
		Object objResp = null;
		Tinvoice inv = null;
		Teventreg eventreg = new Teventreg();
		try {
			eventreg.setTevent(tevent);
			eventreg.setTanggota(objForm.getPribadi());
			eventreg.setIspaid("N");
			
			if (tevent.getIsfree().equals("N")) {
				BriapiBean bean = AppData.getBriapibean();
				BriApiExt briapi = new BriApiExt(bean);
				BriApiToken briapiToken = briapi.getToken();
				
				Date vaexpdate = null;
				BrivaData briva = new BrivaData();
				if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
					briva.setAmount(tevent.getEventprice().toString());
					briva.setInstitutionCode(bean.getBriva_institutioncode());
					briva.setBrivaNo(bean.getBriva_cid());
					
					String custcode_cabang = "0000" + objForm.getPribadi().getMcabang().getKodecabang();
					String custcode = custcode_cabang.substring(custcode_cabang.length()-4, custcode_cabang.length());
					briva.setCustCode(new TcounterengineDAO().getVaCounter());
					briva.setKeterangan(tevent.getEventname().trim().length() > 40 ? tevent.getEventname().substring(0, 40) : tevent.getEventname());
					briva.setNama(objForm.getPribadi().getNama().trim().length() > 40 ? objForm.getPribadi().getNama().trim().substring(0, 40) : objForm.getPribadi().getNama().trim());
					Calendar cal = Calendar.getInstance();
					cal.setTime(tevent.getEventdate());
					cal.add(Calendar.DAY_OF_MONTH, 14);
					
					vaexpdate = cal.getTime();
					briva.setExpiredDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vaexpdate));
					
					BrivaCreateResp brivaCreated = null;
					brivaCreated = briapi.createBriva(briapiToken.getAccess_token(), briva);
					if (brivaCreated != null && brivaCreated.getStatus()) {
						eventreg.setVano(briva.getBrivaNo() + briva.getCustCode());
						eventreg.setVaamount(tevent.getEventprice());
						eventreg.setVacreatedat(new Date());
						eventreg.setVaexpdate(vaexpdate);
						
						eventregDao.save(session, eventreg);
						
						inv = new InvoiceGenerator().doInvoice(eventreg, eventreg.getVano(), AppUtils.INVOICETYPE_EVENT, tevent.getEventprice(), tevent.getEventname(), vaexpdate);
						inv.setTanggota(objForm.getPribadi());
						new TinvoiceDAO().save(session, inv);
						objResp = inv;
					}
				}
			} else {
				eventregDao.save(session, eventreg);
				objResp = eventreg;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objResp;
	}

	public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				boolean isValid = true;
				// Pribadi
				String nama = (String) ctx.getProperties("pribadi.nama")[0].getValue();
				if (nama == null || "".equals(nama.trim())) {
					this.addInvalidMessage(ctx, "nama", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				String noktp = (String) ctx.getProperties("pribadi.noktp")[0].getValue();
				if (noktp == null || "".equals(noktp.trim())) {
					this.addInvalidMessage(ctx, "noktp", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				String gender = (String) ctx.getProperties("pribadi.gender")[0].getValue();
				if (gender == null || "".equals(gender.trim())) {
					this.addInvalidMessage(ctx, "gender", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				String email = (String) ctx.getProperties("pribadi.email")[0].getValue();
				if (email == null || "".equals(email.trim())) {
					this.addInvalidMessage(ctx, "email", Labels.getLabel("common.validator.empty"));
					isValid = false;
				} else if (!StringUtils.emailValidator(email)) {
					this.addInvalidMessage(ctx, "email", "Format e-Mail tidak sesuai");
				}
				if (cbDobDay.getValue().trim().length() > 0 && cbDobMonth.getValue().trim().length() > 0
						&& cbDobYear.getValue().trim().length() > 0) {
					String strDob = cbDobYear.getValue().toString() + "/" + (cbDobMonth.getSelectedIndex() + 1) + "/"
							+ cbDobDay.getValue().toString();
					try {
						dob = dateFormatter.parse(strDob);
					} catch (ParseException e) {
						this.addInvalidMessage(ctx, "tgllahir", "Data tanggal lahir tidak sesuai");
						isValid = false;
						e.printStackTrace();
					}
				} else this.addInvalidMessage(ctx, "tgllahir", Labels.getLabel("common.validator.empty"));

				if (provrumah == null) {
					this.addInvalidMessage(ctx, "provrumah", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				if (kabrumah == null) {
					this.addInvalidMessage(ctx, "kabrumah", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				String alamat = (String) ctx.getProperties("pribadi.alamat")[0].getValue();
				if (alamat == null || "".equals(alamat.trim())) {
					this.addInvalidMessage(ctx, "alamat", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				String hp = (String) ctx.getProperties("pribadi.hp")[0].getValue();
				if (hp == null || "".equals(hp.trim())) {
					this.addInvalidMessage(ctx, "hp", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				String instansi = (String) ctx.getProperties("pribadi.instansi")[0].getValue();
				if (instansi == null || "".equals(instansi.trim())) {
					this.addInvalidMessage(ctx, "instansi", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				
				if (!isValid) {
					Messagebox.show("Harap lengkapi data Anda", WebApps.getCurrent().getAppName(), Messagebox.OK,
							Messagebox.EXCLAMATION);
				}
			}
		};
	}

	public ListModelList<Mprov> getProvrumahModel() {
		ListModelList<Mprov> oList = null;
		try {
			oList = new ListModelList<>(new MprovDAO().listAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	public ListModelList<Mprov> getRegionModel() {
		ListModelList<Mprov> oList = null;
		try {
			oList = new ListModelList<>(new MprovDAO().listAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	public AnggotaReg getObjForm() {
		return objForm;
	}

	public void setObjForm(AnggotaReg objForm) {
		this.objForm = objForm;
	}

	public Mprov getProvrumah() {
		return provrumah;
	}

	public void setProvrumah(Mprov provrumah) {
		this.provrumah = provrumah;
	}

	public Mkab getKabrumah() {
		return kabrumah;
	}

	public void setKabrumah(Mkab kabrumah) {
		this.kabrumah = kabrumah;
	}

	public ListModelList<Mkab> getKabrumahModel() {
		return kabrumahModel;
	}

	public void setKabrumahModel(ListModelList<Mkab> kabrumahModel) {
		this.kabrumahModel = kabrumahModel;
	}

	public Mprov getRegion() {
		return region;
	}

	public void setRegion(Mprov region) {
		this.region = region;
	}

}
