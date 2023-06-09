package com.sds.hakli.viewmodel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.dao.McabangDAO;
import com.sds.hakli.dao.MjenjangDAO;
import com.sds.hakli.dao.MkabDAO;
import com.sds.hakli.dao.MkepegawaianDAO;
import com.sds.hakli.dao.MkepegawaiansubDAO;
import com.sds.hakli.dao.MnegaraDAO;
import com.sds.hakli.dao.MprovDAO;
import com.sds.hakli.dao.MrumpunDAO;
import com.sds.hakli.dao.MuniversitasDAO;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.dao.TeventregDAO;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.dao.TpekerjaanDAO;
import com.sds.hakli.dao.TpendidikanDAO;
import com.sds.hakli.domain.AnggotaReg;
import com.sds.hakli.domain.Mcabang;
import com.sds.hakli.domain.Mjenjang;
import com.sds.hakli.domain.Mkab;
import com.sds.hakli.domain.Mkepegawaian;
import com.sds.hakli.domain.Mkepegawaiansub;
import com.sds.hakli.domain.Mnegara;
import com.sds.hakli.domain.Mprov;
import com.sds.hakli.domain.Mrumpun;
import com.sds.hakli.domain.Muniversitas;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tevent;
import com.sds.hakli.domain.Teventreg;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.domain.Tpekerjaan;
import com.sds.hakli.domain.Tpendidikan;
import com.sds.hakli.extension.BriApiExt;
import com.sds.hakli.extension.MailHandler;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.BrivaCreateResp;
import com.sds.hakli.pojo.BrivaData;
import com.sds.hakli.utils.InvoiceGenerator;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.StringUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class EventRegVm {

	private TanggotaDAO anggotaDao = new TanggotaDAO();
	private TpekerjaanDAO pekerjaanDao = new TpekerjaanDAO();
	private TpendidikanDAO pendidikanDao = new TpendidikanDAO();
	private MprovDAO provDao = new MprovDAO();
	private MkabDAO kabDao = new MkabDAO();
	private TeventregDAO eventregDao = new TeventregDAO();

	private AnggotaReg objForm;
	private Mprov region;
	private Mprov provrumah;
	private Mkab kabrumah;
	private Mprov provkantor;
	private Mkab kabkantor;

	private ListModelList<Mnegara> negaraModel;
	private ListModelList<Mkab> kabrumahModel;
	private ListModelList<Mkab> kabkantorModel;
	private ListModelList<Mcabang> cabangModel;
	private ListModelList<Mkepegawaiansub> kepegawaiansubModel;

	public Boolean isInsert;
	private Media media;
	private Media mediaIjazah;
	private Date dob;
	private Boolean isView;
	private String ijazahfilename;
	
	private Tevent tevent;
	private String eventimg;

	private DateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");

	@Wire
	private Window winEventreg;
	@Wire
	private Div divForm;
	@Wire
	private Combobox cbNegara;
	@Wire
	private Combobox cbCabang;
	@Wire
	private Combobox cbProvrumah;
	@Wire
	private Combobox cbKabrumah;
	@Wire
	private Combobox cbProvkantor;
	@Wire
	private Combobox cbKabkantor;
	@Wire
	private Combobox cbRumpun;
	@Wire
	private Combobox cbStatusanggota;
	@Wire
	private Combobox cbKepegawaian;
	@Wire
	private Combobox cbKepegawaiansub;
	@Wire
	private Combobox cbUniversitas;
	@Wire
	private Combobox cbJenjang;
	@Wire
	private Image photo;
	@Wire
	private Button btDeletePhoto;
	@Wire
	private Button btDeleteIjazah;
	@Wire
	private Combobox cbDobDay;
	@Wire
	private Combobox cbDobMonth;
	@Wire
	private Combobox cbDobYear;
	@Wire
	private Combobox cbPendidikanBlAwal;
	@Wire
	private Combobox cbPendidikanBlAkhir;
	@Wire
	private Combobox cbRegion;
	@Wire
	private Row rowNegara;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, 
			@ExecutionArgParam("event") Tevent tevent, @ExecutionArgParam("obj") Tanggota obj) {
		Selectors.wireComponents(view, this, false);
		try {
			if (obj == null) {
				Executions.sendRedirect("/timeout.zul");
			} else {
				this.tevent = tevent;
				eventimg = AppUtils.PATH_EVENT + "/" + tevent.getEventimg();
				init();
				doReset();
				objForm = new AnggotaReg();
				objForm.setPribadi(obj);
				if (obj.getTanggotapk() != null)
					isInsert = false;
				else {
					isInsert = true;
					objForm.getPribadi().setStatusreg("9");
				}
				
				List<Tpekerjaan> pekerjaans = pekerjaanDao.listByFilter("tanggota.tanggotapk = " + obj.getTanggotapk(), "tpekerjaanpk desc");
				if (pekerjaans.size() > 0) {
					objForm.setPekerjaan(pekerjaans.get(0));
				} else objForm.setPekerjaan(new Tpekerjaan());
				
				List<Tpendidikan> pendidikans = pendidikanDao.listByFilter("tanggota.tanggotapk = " + obj.getTanggotapk(), "tpendidikanpk desc");
				if (pendidikans.size() > 0) {
					objForm.setPendidikan(pendidikans.get(0));
				} else objForm.setPendidikan(new Tpendidikan());
				
				if (obj.getTgllahir() != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(obj.getTgllahir());
					cbDobDay.setValue(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
					cbDobMonth.setSelectedIndex(cal.get(Calendar.MONTH));
					cbDobYear.setValue(String.valueOf(cal.get(Calendar.YEAR)));
				}
				
				provrumah = provDao.findByFilter("provcode = '" + obj.getProvcode() + "'");
				if (provrumah != null) {
					cbProvrumah.setValue(provrumah.getProvname());
					doLoadKab(provrumah);
				}
				kabrumah = kabDao.findByFilter("provcode = '" + obj.getProvcode() + "' and kabcode = '" + obj.getKabcode() + "'");
				cbKabrumah.setValue(obj.getKabname());
				
				provkantor = provDao.findByFilter("provcode = '" + objForm.getPekerjaan().getProvcode() + "'");
				if (provkantor != null) {
					cbProvkantor.setValue(provkantor.getProvname());
					doLoadKabPekerjaan(provkantor);
				}
				kabkantor = kabDao.findByFilter("provcode = '" + objForm.getPekerjaan().getProvcode() + "' and kabcode = '" + objForm.getPekerjaan().getKabcode() + "'");
				cbKabkantor.setValue(objForm.getPekerjaan().getKabname());
				
				if (objForm.getPekerjaan().getMrumpun() != null)
					cbRumpun.setValue(objForm.getPekerjaan().getMrumpun().getRumpun());
				if (objForm.getPekerjaan().getMkepegawaian() != null) {
					cbKepegawaian.setValue(objForm.getPekerjaan().getMkepegawaian().getKepegawaian());
					doLoadKepegawaiansub(objForm.getPekerjaan().getMkepegawaian());
				}
				if (objForm.getPekerjaan().getMkepegawaiansub() != null) {
					cbKepegawaiansub.setValue(objForm.getPekerjaan().getMkepegawaiansub().getKepegawaiansub());
				}
				
				if (objForm.getPendidikan().getMuniversitas() != null)
					cbUniversitas.setValue(objForm.getPendidikan().getMuniversitas().getUniversitas());
				if (objForm.getPendidikan().getMjenjang() != null)
					cbJenjang.setValue(objForm.getPendidikan().getMjenjang().getJenjang());
				if (objForm.getPendidikan().getPeriodeblawal() != null  && StringUtils.isNumeric(objForm.getPendidikan().getPeriodeblawal().trim()))
					cbPendidikanBlAwal.setSelectedIndex(Integer.parseInt(objForm.getPendidikan().getPeriodeblawal()) - 1);
				if (objForm.getPendidikan().getPeriodeblakhir() != null  && StringUtils.isNumeric(objForm.getPendidikan().getPeriodeblakhir().trim()))
					cbPendidikanBlAkhir.setSelectedIndex(Integer.parseInt(objForm.getPendidikan().getPeriodeblakhir()) - 1);
				
				if (obj.getMcabang() != null) {
					region = obj.getMcabang().getMprov();
					if (region != null) {
						cbRegion.setValue(obj.getMcabang().getMprov().getProvname());
						doLoadCabang(region);
					}
					cbCabang.setValue(obj.getMcabang().getCabang());
				}
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

				Comboitem itemPendidikanBlAwal = new Comboitem(StringUtils.getMonthLabel(i));
				cbPendidikanBlAwal.appendChild(itemPendidikanBlAwal);

				Comboitem itemPendidikanBlAkhir = new Comboitem(StringUtils.getMonthLabel(i));
				cbPendidikanBlAkhir.appendChild(itemPendidikanBlAkhir);
			}
			int yearend = Calendar.getInstance().get(Calendar.YEAR);
			for (int i = 1940; i <= yearend - 10; i++) {
				Comboitem item = new Comboitem(String.valueOf(i));
				cbDobYear.appendChild(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("negaraModel")
	public void doLoadNegara(@BindingParam("item") String item) {
		try {
			if (item != null && item.equals("WNA")) {
				if (negaraModel == null) {
					cbNegara.setValue(null);
					negaraModel = new ListModelList<>(new MnegaraDAO().listAll());
				}
				rowNegara.setVisible(true);
			} else
				rowNegara.setVisible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	@Command
	@NotifyChange("kabkantorModel")
	public void doLoadKabPekerjaan(@BindingParam("prov") Mprov prov) {
		try {
			if (prov != null) {
				cbKabkantor.setValue(null);
				kabkantorModel = new ListModelList<>(
						new MkabDAO().listByFilter("provcode = '" + prov.getProvcode() + "'", "kabname"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("kepegawaiansubModel")
	public void doLoadKepegawaiansub(@BindingParam("kepegawaian") Mkepegawaian kepegawaian) {
		try {
			if (kepegawaian != null) {
				cbKepegawaiansub.setValue(null);
				kepegawaiansubModel = new ListModelList<>(new MkepegawaiansubDAO().listByFilter(
						"mkepegawaian.mkepegawaianpk = " + kepegawaian.getMkepegawaianpk(), "kepegawaiansub"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("cabangModel")
	public void doLoadCabang(@BindingParam("prov") Mprov prov) {
		try {
			if (prov != null) {
				cbCabang.setValue(null);
				cabangModel = new ListModelList<>(
						new McabangDAO().listByFilter("mprov.mprovpk = '" + prov.getMprovpk() + "'", "cabang"));
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
		objForm.setPekerjaan(new Tpekerjaan());
		objForm.setPendidikan(new Tpendidikan());
		dob = null;
		media = null;
		photo.setVisible(false);
		if (isView == null || !isView) {
			cbDobDay.setValue(null);
			cbDobMonth.setValue(null);
			cbDobYear.setValue(null);

			btDeletePhoto.setVisible(false);
		}
	}

	@Command
	public void doPageList() {
		Component comp = winEventreg.getParent();
		comp.getChildren().clear();
		Executions.createComponents("/view/anggota.zul", comp, null);
	}

	@Command
	public void doUploadPhoto(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
		try {
			UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
			media = event.getMedia();
			if (media instanceof org.zkoss.image.Image) {
				photo.setContent((org.zkoss.image.Image) media);
				photo.setVisible(true);
				btDeletePhoto.setVisible(true);
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
	@NotifyChange("ijazahfilename")
	public void doUploadIjazah(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
		try {
			UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
			mediaIjazah = event.getMedia();
			ijazahfilename = mediaIjazah.getName();
			btDeleteIjazah.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	public void doDeletePhoto() {
		media = null;
		photo.setSrc(null);
		btDeletePhoto.setVisible(false);
	}
	
	@Command
	@NotifyChange("ijazahfilename")
	public void doDeleteIjazah() {
		mediaIjazah = null;
		ijazahfilename = null;
		btDeleteIjazah.setVisible(false);
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
							Teventreg eventreg = new Teventreg();
							try {
								if (media != null) {
									try {
										String folder = Executions.getCurrent().getDesktop().getWebApp()
												.getRealPath(AppUtils.PATH_PHOTO);
										if (media.isBinary()) {
											Files.copy(new File(folder + "/" + media.getName()), media.getStreamData());
										} else {
											BufferedWriter writer = new BufferedWriter(
													new FileWriter(folder + "/" + media.getName()));
											Files.copy(writer, media.getReaderData());
											writer.close();
										}

										objForm.getPribadi().setPhotolink(media.getName());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								
								if (mediaIjazah != null) {
									try {
										String ijazahlink = new TcounterengineDAO().getLastCounter("DOC" + new SimpleDateFormat("yyMM").format(new Date()), 5) + "." + mediaIjazah.getFormat();
										String folder = Executions.getCurrent().getDesktop().getWebApp()
												.getRealPath(AppUtils.PATH_IJAZAH);
										System.out.println("Ijazah Path : " + folder);
										if (media.isBinary()) {
											Files.copy(new File(folder + "/" + ijazahlink), mediaIjazah.getStreamData());
										} else {
											BufferedWriter writer = new BufferedWriter(
													new FileWriter(folder + "/" + ijazahlink));
											Files.copy(writer, mediaIjazah.getReaderData());
											writer.close();
										}

										objForm.getPendidikan().setIjazahlink(ijazahlink);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								
								trx = session.beginTransaction();
								
								objForm.getPribadi().setTgllahir(dob);
								objForm.getPribadi().setProvcode(provrumah.getProvcode());
								objForm.getPribadi().setProvname(provrumah.getProvname());
								objForm.getPribadi().setKabcode(kabrumah.getKabcode());
								objForm.getPribadi().setKabname(kabrumah.getKabname());
								objForm.getPribadi().setStatusreg(AppUtils.STATUS_ANGGOTA_REG);
								objForm.getPribadi().setCreatetime(new Date());
								anggotaDao.save(session, objForm.getPribadi());

								objForm.getPendidikan().setTanggota(objForm.getPribadi());
								objForm.getPendidikan().setPeriodeblawal(String.valueOf(cbPendidikanBlAwal.getSelectedIndex() + 1));
								objForm.getPendidikan().setPeriodeblakhir(String.valueOf(cbPendidikanBlAkhir.getSelectedIndex() + 1));
								pendidikanDao.save(session, objForm.getPendidikan());

								objForm.getPekerjaan().setTanggota(objForm.getPribadi());
								objForm.getPekerjaan().setProvcode(provkantor.getProvcode());
								objForm.getPekerjaan().setProvname(provkantor.getProvname());
								objForm.getPekerjaan().setKabcode(kabkantor.getKabcode());
								objForm.getPekerjaan().setKabname(kabkantor.getKabname());
								pekerjaanDao.save(session, objForm.getPekerjaan());
								
								eventreg.setTevent(tevent);
								eventreg.setTanggota(objForm.getPribadi());
								eventreg.setIspaid("N");
								
								boolean isVaCreate = false;
								boolean isVaUpdate = false;
								if (objForm.getPribadi().getVaevent() == null || objForm.getPribadi().getVaevent().trim().length() == 0) {
									isVaCreate = true;
									isVaUpdate = true;
								} else {
									if (objForm.getPribadi().getVaeventstatus() == 1) {
										isVaCreate = true;
										isVaUpdate = false;
									} else {
										isVaCreate = false;
									}
								}
								
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
									if (isVaCreate)
										briva.setCustCode(new TcounterengineDAO().getVaCounter(custcode));
									else briva.setCustCode(objForm.getPribadi().getVaevent().substring(5));
									briva.setKeterangan(tevent.getEventname().trim().length() > 40 ? tevent.getEventname().substring(0, 40) : tevent.getEventname());
									briva.setNama(objForm.getPribadi().getNama().trim().length() > 40 ? objForm.getPribadi().getNama().trim().substring(0, 40) : objForm.getPribadi().getNama().trim());
									
									Calendar cal = Calendar.getInstance();
									cal.add(Calendar.DAY_OF_MONTH, 10);
									vaexpdate = cal.getTime();
									briva.setExpiredDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vaexpdate));
									
									BrivaCreateResp brivaCreated = null;
									if (isVaCreate) {
										brivaCreated = briapi.createBriva(briapiToken.getAccess_token(), briva);
										if (brivaCreated != null && brivaCreated.getStatus() && isVaUpdate) {
											objForm.getPribadi().setVaevent(briva.getBrivaNo() + briva.getCustCode());
											objForm.getPribadi().setVaeventstatus(1);
											anggotaDao.save(session, objForm.getPribadi());
										}
									} else {
										brivaCreated = briapi.updateDataBriva(briapiToken.getAccess_token(), briva);
										if (brivaCreated != null && brivaCreated.getStatus()) {
											objForm.getPribadi().setVaeventstatus(1);
											anggotaDao.save(session, objForm.getPribadi());
										}
									}
									
									if (brivaCreated != null && brivaCreated.getStatus()) {
										eventreg.setVano(briva.getBrivaNo() + briva.getCustCode());
										eventreg.setVaamount(tevent.getEventprice());
										eventreg.setVacreatedat(new Date());
										eventreg.setVaexpdate(vaexpdate);
									}
									
								}
								eventregDao.save(session, eventreg);
								
								Tinvoice inv = new InvoiceGenerator().doInvoice(eventreg, eventreg.getVano(), AppUtils.INVOICETYPE_EVENT, tevent.getEventprice(), tevent.getEventname(), vaexpdate);
								inv.setTanggota(objForm.getPribadi());
								new TinvoiceDAO().save(session, inv);
								
								trx.commit();
								
								String bodymail_path = Executions.getCurrent().getDesktop().getWebApp()
										.getRealPath("/themes/mail/mailinv.html");
								new Thread(new MailHandler(inv, inv.getInvoicedesc().trim(), inv.getTanggota().getEmail(), bodymail_path)).start();
								
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
								Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
										Messagebox.ERROR);
							} finally {
								session.close();
							}
							
							Component comp = winEventreg.getParent();
							comp.getChildren().clear();
							Map<String, Object> map = new HashMap<>();
							map.put("obj", eventreg);
							Executions.createComponents("/view/event/eventregdone.zul", comp, map);
						}
					}

				});
	}

	public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				// Pribadi
				String nama = (String) ctx.getProperties("pribadi.nama")[0].getValue();
				if (nama == null || "".equals(nama.trim()))
					this.addInvalidMessage(ctx, "nama", Labels.getLabel("common.validator.empty"));
				String noktp = (String) ctx.getProperties("pribadi.noktp")[0].getValue();
				if (noktp == null || "".equals(noktp.trim()))
					this.addInvalidMessage(ctx, "noktp", Labels.getLabel("common.validator.empty"));
				String tempatlahir = (String) ctx.getProperties("pribadi.tempatlahir")[0].getValue();
				if (tempatlahir == null || "".equals(tempatlahir.trim()))
					this.addInvalidMessage(ctx, "tempatlahir", Labels.getLabel("common.validator.empty"));
				String gender = (String) ctx.getProperties("pribadi.gender")[0].getValue();
				if (gender == null || "".equals(gender.trim()))
					this.addInvalidMessage(ctx, "gender", Labels.getLabel("common.validator.empty"));
				String warganegara = (String) ctx.getProperties("pribadi.warganegara")[0].getValue();
				if (warganegara == null || "".equals(warganegara.trim()))
					this.addInvalidMessage(ctx, "warganegara", Labels.getLabel("common.validator.empty"));
				else if (warganegara.equals("WNA")) {
					Mnegara mnegara = (Mnegara) ctx.getProperties("pribadi.mnegara")[0].getValue();
					if (mnegara == null)
						this.addInvalidMessage(ctx, "negara", Labels.getLabel("common.validator.empty"));
				}
				String email = (String) ctx.getProperties("pribadi.email")[0].getValue();
				if (email == null || "".equals(email.trim()))
					this.addInvalidMessage(ctx, "email", Labels.getLabel("common.validator.empty"));
				else if (!StringUtils.emailValidator(email)) {
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
						e.printStackTrace();
					}
				} else {
					this.addInvalidMessage(ctx, "tgllahir", Labels.getLabel("common.validator.empty"));
				}

				// Alama Rumah
				if (provrumah == null)
					this.addInvalidMessage(ctx, "provrumah", Labels.getLabel("common.validator.empty"));
				if (kabrumah == null)
					this.addInvalidMessage(ctx, "kabrumah", Labels.getLabel("common.validator.empty"));
				String alamat = (String) ctx.getProperties("pribadi.alamat")[0].getValue();
				if (alamat == null || "".equals(alamat.trim()))
					this.addInvalidMessage(ctx, "alamat", Labels.getLabel("common.validator.empty"));
				String hp = (String) ctx.getProperties("pribadi.hp")[0].getValue();
				if (hp == null || "".equals(hp.trim()))
					this.addInvalidMessage(ctx, "hp", Labels.getLabel("common.validator.empty"));

				// Keanggotaan
				String statusanggota = (String) ctx.getProperties("pribadi.statusanggota")[0].getValue();
				if (statusanggota == null || "".equals(statusanggota.trim()))
					this.addInvalidMessage(ctx, "statusanggota", Labels.getLabel("common.validator.empty"));
				Mcabang mcabang = (Mcabang) ctx.getProperties("pribadi.mcabang")[0].getValue();
				if (mcabang == null)
					this.addInvalidMessage(ctx, "mcabang", Labels.getLabel("common.validator.empty"));

				// Pekerjaan
				Mrumpun mrumpun = (Mrumpun) ctx.getProperties("pekerjaan.mrumpun")[0].getValue();
				if (mrumpun == null)
					this.addInvalidMessage(ctx, "mrumpun", Labels.getLabel("common.validator.empty"));
				Mkepegawaian mkepegawaian = (Mkepegawaian) ctx.getProperties("pekerjaan.mkepegawaian")[0].getValue();
				if (mkepegawaian == null)
					this.addInvalidMessage(ctx, "mkepegawaian", Labels.getLabel("common.validator.empty"));
				Mkepegawaiansub mkepegawaiansub = (Mkepegawaiansub) ctx.getProperties("pekerjaan.mkepegawaiansub")[0]
						.getValue();
				if (mkepegawaiansub == null)
					this.addInvalidMessage(ctx, "mkepegawaiansub", Labels.getLabel("common.validator.empty"));
				if (provkantor == null)
					this.addInvalidMessage(ctx, "provkantor", Labels.getLabel("common.validator.empty"));
				if (kabkantor == null)
					this.addInvalidMessage(ctx, "kabkantor", Labels.getLabel("common.validator.empty"));
				String namakantor = (String) ctx.getProperties("pekerjaan.namakantor")[0].getValue();
				if (namakantor == null || "".equals(namakantor.trim()))
					this.addInvalidMessage(ctx, "namakantor", Labels.getLabel("common.validator.empty"));
				String jabatan = (String) ctx.getProperties("pekerjaan.jabatankantor")[0].getValue();
				if (jabatan == null || "".equals(jabatan.trim()))
					this.addInvalidMessage(ctx, "jabatan", Labels.getLabel("common.validator.empty"));
				Date tglmulai = (Date) ctx.getProperties("pekerjaan.tglmulai")[0].getValue();
				if (tglmulai == null)
					this.addInvalidMessage(ctx, "tglmulai", Labels.getLabel("common.validator.empty"));
				String nip = (String) ctx.getProperties("pekerjaan.nip")[0].getValue();
				if (nip == null || "".equals(nip.trim()))
					this.addInvalidMessage(ctx, "nip", Labels.getLabel("common.validator.empty"));

				// Pendidikan
				Muniversitas muniversitas = (Muniversitas) ctx.getProperties("pendidikan.muniversitas")[0].getValue();
				if (muniversitas == null)
					this.addInvalidMessage(ctx, "muniversitas", Labels.getLabel("common.validator.empty"));
				Mjenjang mjenjang = (Mjenjang) ctx.getProperties("pendidikan.mjenjang")[0].getValue();
				if (mjenjang == null)
					this.addInvalidMessage(ctx, "mjenjang", Labels.getLabel("common.validator.empty"));
				
				String periodethawal = (String) ctx.getProperties("pendidikan.periodethawal")[0].getValue();
				if (cbPendidikanBlAwal.getValue() != null && (periodethawal == null || periodethawal.trim().length() == 0))
					this.addInvalidMessage(ctx, "pendidikanawal", Labels.getLabel("common.validator.empty"));
				String periodethakhir = (String) ctx.getProperties("pendidikan.periodethakhir")[0].getValue();
				if (cbPendidikanBlAkhir.getValue() != null && (periodethakhir == null || periodethakhir.trim().length() == 0))
					this.addInvalidMessage(ctx, "pendidikanakhir", Labels.getLabel("common.validator.empty"));
					
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

	public ListModelList<Mprov> getProvkantorModel() {
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

	public ListModelList<Muniversitas> getUniversitasModel() {
		ListModelList<Muniversitas> oList = null;
		try {
			oList = new ListModelList<>(new MuniversitasDAO().listAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	public ListModelList<Mjenjang> getJenjangModel() {
		ListModelList<Mjenjang> oList = null;
		try {
			oList = new ListModelList<>(new MjenjangDAO().listAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	public ListModelList<Mrumpun> getRumpunModel() {
		ListModelList<Mrumpun> oList = null;
		try {
			oList = new ListModelList<>(new MrumpunDAO().listAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	public ListModelList<Mkepegawaian> getKepegawaianModel() {
		ListModelList<Mkepegawaian> oList = null;
		try {
			oList = new ListModelList<>(new MkepegawaianDAO().listAll());
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

	public Mprov getProvkantor() {
		return provkantor;
	}

	public void setProvkantor(Mprov provkantor) {
		this.provkantor = provkantor;
	}

	public Mkab getKabkantor() {
		return kabkantor;
	}

	public void setKabkantor(Mkab kabkantor) {
		this.kabkantor = kabkantor;
	}

	public ListModelList<Mnegara> getNegaraModel() {
		return negaraModel;
	}

	public void setNegaraModel(ListModelList<Mnegara> negaraModel) {
		this.negaraModel = negaraModel;
	}

	public ListModelList<Mkab> getKabrumahModel() {
		return kabrumahModel;
	}

	public void setKabrumahModel(ListModelList<Mkab> kabrumahModel) {
		this.kabrumahModel = kabrumahModel;
	}

	public ListModelList<Mkab> getKabkantorModel() {
		return kabkantorModel;
	}

	public void setKabkantorModel(ListModelList<Mkab> kabkantorModel) {
		this.kabkantorModel = kabkantorModel;
	}

	public ListModelList<Mcabang> getCabangModel() {
		return cabangModel;
	}

	public void setCabangModel(ListModelList<Mcabang> cabangModel) {
		this.cabangModel = cabangModel;
	}

	public ListModelList<Mkepegawaiansub> getKepegawaiansubModel() {
		return kepegawaiansubModel;
	}

	public void setKepegawaiansubModel(ListModelList<Mkepegawaiansub> kepegawaiansubModel) {
		this.kepegawaiansubModel = kepegawaiansubModel;
	}

	public Mprov getRegion() {
		return region;
	}

	public void setRegion(Mprov region) {
		this.region = region;
	}

	public String getIjazahfilename() {
		return ijazahfilename;
	}

	public void setIjazahfilename(String ijazahfilename) {
		this.ijazahfilename = ijazahfilename;
	}

	public String getEventimg() {
		return eventimg;
	}

	public void setEventimg(String eventimg) {
		this.eventimg = eventimg;
	}

}
