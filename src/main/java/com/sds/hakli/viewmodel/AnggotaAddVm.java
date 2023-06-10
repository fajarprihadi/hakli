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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.bean.SisdmkBean;
import com.sds.hakli.dao.McabangDAO;
import com.sds.hakli.dao.MjenjangDAO;
import com.sds.hakli.dao.MkabDAO;
import com.sds.hakli.dao.MkepegawaianDAO;
import com.sds.hakli.dao.MkepegawaiansubDAO;
import com.sds.hakli.dao.MnegaraDAO;
import com.sds.hakli.dao.MprovDAO;
import com.sds.hakli.dao.MrumpunDAO;
import com.sds.hakli.dao.MuniversitasDAO;
import com.sds.hakli.dao.MusergroupDAO;
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
import com.sds.hakli.extension.SisdmkApiExt;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.BrivaCreateResp;
import com.sds.hakli.pojo.BrivaData;
import com.sds.hakli.pojo.SisdmkData;
import com.sds.hakli.pojo.SisdmkPekerjaan;
import com.sds.hakli.pojo.SisdmkPendidikan;
import com.sds.hakli.pojo.SisdmkToken;
import com.sds.hakli.utils.InvoiceGenerator;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.StringUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class AnggotaAddVm {

	private TanggotaDAO anggotaDao = new TanggotaDAO();
	private TpekerjaanDAO pekerjaanDao = new TpekerjaanDAO();
	private TpendidikanDAO pendidikanDao = new TpendidikanDAO();
	private MprovDAO provDao = new MprovDAO();
	private MkabDAO kabDao = new MkabDAO();
	private TeventregDAO eventregDao = new TeventregDAO();

	private AnggotaReg objForm;
	private Tevent tevent;
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
	
	private DateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");

	@Wire
	private Window winAnggotaAdd;
	@Wire
	private Div divForm;
	@Wire
	private Image imgEvent;
	@Wire
	private Combobox cbNegara;
	@Wire
	private Combobox cbRegion;
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
	private Combobox cbStatusanggota;
	@Wire
	private Combobox cbRumpun;
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
	private Combobox cbDobDay;
	@Wire
	private Combobox cbDobMonth;
	@Wire
	private Combobox cbDobYear;
	@Wire
	private Combobox cbPendidikanThAwal;
	@Wire
	private Combobox cbPendidikanBlAwal;
	@Wire
	private Combobox cbPendidikanThAkhir;
	@Wire
	private Combobox cbPendidikanBlAkhir;
	@Wire
	private Row rowNegara;

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
				
				List<Tpekerjaan> pekerjaans = pekerjaanDao.listByFilter("tanggota.tanggotapk = " + obj.getTanggotapk(), "tpekerjaanpk desc");
				if (pekerjaans.size() > 0)
					objForm.setPekerjaan(pekerjaans.get(0));
				List<Tpendidikan> pendidikans = pendidikanDao.listByFilter("tanggota.tanggotapk = " + obj.getTanggotapk(), "tpendidikanpk desc");
				if (pendidikans.size() > 0)
					objForm.setPendidikan(pendidikans.get(0));
				
				if (obj.getTanggotapk() == null && isCheckSisdmk != null && isCheckSisdmk)
					doSisdmk();
				
				if (obj.getPhotolink() != null) {
					photo.setSrc(AppUtils.PATH_PHOTO + "/" + obj.getPhotolink());
					photo.setVisible(true);
				}
				
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
				
				doLoadNegara(obj.getWarganegara());
				
				if (objForm.getPekerjaan().getTpekerjaanpk() != null) {
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
					if (objForm.getPekerjaan().getMkepegawaiansub() != null)
						cbKepegawaiansub.setValue(objForm.getPekerjaan().getMkepegawaiansub().getKepegawaiansub());
				}
				
				
				if (objForm.getPendidikan().getTpendidikanpk() != null) {
					if (objForm.getPendidikan().getMuniversitas() != null) {
						cbUniversitas.setValue(objForm.getPendidikan().getMuniversitas().getUniversitas());
					}
					if (objForm.getPendidikan().getMjenjang() != null) {
						cbJenjang.setValue(objForm.getPendidikan().getMjenjang().getJenjang());
					}
					
					cbPendidikanThAwal.setValue(objForm.getPendidikan().getPeriodethawal());
					cbPendidikanThAkhir.setValue(objForm.getPendidikan().getPeriodethakhir());
					if (objForm.getPendidikan().getPeriodeblawal() != null && objForm.getPendidikan().getPeriodeblawal().trim().length() > 0)
						cbPendidikanBlAwal.setSelectedIndex(Integer.parseInt(objForm.getPendidikan().getPeriodeblawal()) - 1);
					if (objForm.getPendidikan().getPeriodeblakhir() != null && objForm.getPendidikan().getPeriodeblakhir().trim().length() > 0)
						cbPendidikanBlAkhir.setSelectedIndex(Integer.parseInt(objForm.getPendidikan().getPeriodeblakhir()) - 1);
					
					ijazahfilename = objForm.getPendidikan().getIjazahfilename();
				}
				
				if (obj.getMcabang() != null) {
					region = obj.getMcabang().getMprov();
					if (region != null) {
						cbRegion.setValue(region.getProvname());
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
			for (int i = 1940; i <= yearend; i++) {
				Comboitem item = new Comboitem(String.valueOf(i));
				cbDobYear.appendChild(item);

				Comboitem itemPendidikanThAwal = new Comboitem(String.valueOf(i));
				cbPendidikanThAwal.appendChild(itemPendidikanThAwal);

				Comboitem itemPendidikanThAkhir = new Comboitem(String.valueOf(i));
				cbPendidikanThAkhir.appendChild(itemPendidikanThAkhir);
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
					
					if (data.getData().getPekerjaan() != null && data.getData().getPekerjaan().size() > 0) {
						SisdmkPekerjaan sisdmkPekerjaan = data.getData().getPekerjaan().get(0);
						Tpekerjaan pekerjaan = new Tpekerjaan();
						pekerjaan.setAlamatkantor(sisdmkPekerjaan.getAlamat());
						pekerjaan.setNip(sisdmkPekerjaan.getNip());
						pekerjaan.setNamakantor(sisdmkPekerjaan.getUnit());
						anggota.setNostr(sisdmkPekerjaan.getStr());
						
						if (sisdmkPekerjaan.getProvinsi() != null) {
							provkantor = provDao.findByFilter("upper(provname) = '" + sisdmkPekerjaan.getProvinsi().trim().toUpperCase() + "'");
							if (provkantor != null) {
								cbProvkantor.setValue(provkantor.getProvname());
								doLoadKabPekerjaan(provkantor);
							}
							
							if (sisdmkPekerjaan.getKabkot() != null) {
								kabkantor = kabDao.findByFilter("provcode = '" + provkantor.getProvcode() + "' and upper(kabname) = '" + sisdmkPekerjaan.getKabkot().trim().toUpperCase() + "'");
								if (kabkantor != null) {
									cbKabkantor.setValue(kabkantor.getKabname());
								}
							}
						}
						
						objForm.setPekerjaan(pekerjaan);
					}
					
					if (data.getData().getPendidikan() != null && data.getData().getPendidikan().size() > 0) {
						SisdmkPendidikan sisdmkPendidikan = data.getData().getPendidikan().get(0);
						Tpendidikan pendidikan = new Tpendidikan();
						pendidikan.setPeminatan1(sisdmkPendidikan.getProdi());
						pendidikan.setPeriodethakhir(sisdmkPendidikan.getTahun_lulus());
						if (sisdmkPendidikan.getTahun_lulus() != null)
							cbPendidikanThAkhir.setValue(sisdmkPendidikan.getTahun_lulus());
						
						if (sisdmkPendidikan.getPerguruan_tinggi() != null) {
							Muniversitas universitas = new MuniversitasDAO().findByFilter("upper(universitas) = '" + sisdmkPendidikan.getPerguruan_tinggi().trim().toUpperCase() + "'");
							if (universitas != null) {
								pendidikan.setMuniversitas(universitas);
								cbUniversitas.setValue(universitas.getUniversitas());
							}
						}
						if (sisdmkPendidikan.getJenjang() != null) {
							Mjenjang jenjang = new MjenjangDAO().findByFilter("upper(trim(jenjang)) = '" + sisdmkPendidikan.getJenjang().trim().toUpperCase() + "'");
							if (jenjang != null) {
								pendidikan.setMjenjang(jenjang);
								cbJenjang.setValue(jenjang.getJenjang());
							}
						}
						
						objForm.setPendidikan(pendidikan);
					}
					
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
						new McabangDAO().listByFilter("mprov.mprovpk = " + prov.getMprovpk(), "cabang"));
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
		mediaIjazah = null;
		ijazahfilename = null;
		photo.setVisible(false);
		if (isView == null || !isView) {
			cbDobDay.setValue(null);
			cbDobMonth.setValue(null);
			cbDobYear.setValue(null);
		}
	}

	@Command
	public void doPageList() {
		Component comp = winAnggotaAdd.getParent();
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
			} else {
				media = null;
				Messagebox.show("File bukan berupa gambar", WebApps.getCurrent().getAppName(), Messagebox.OK,
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
		} catch (Exception e) {
			e.printStackTrace();
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
							try {
								if (media != null) {
									try {
										String photoId = new TcounterengineDAO().getLastCounter("PHOTO" + new SimpleDateFormat("yyMM").format(new Date()), 5) + "." + media.getFormat();
										String folder = Executions.getCurrent().getDesktop().getWebApp()
												.getRealPath(AppUtils.PATH_PHOTO);
										if (media.isBinary()) {
											Files.copy(new File(folder + "/" + photoId), media.getStreamData());
										} else {
											BufferedWriter writer = new BufferedWriter(
													new FileWriter(folder + "/" + photoId));
											Files.copy(writer, media.getReaderData());
											writer.close();
										}

										objForm.getPribadi().setPhotolink(photoId);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								
								if (mediaIjazah != null) {
									try {
										String ijazahId = new TcounterengineDAO().getLastCounter("DOC" + new SimpleDateFormat("yyMM").format(new Date()), 5) + "." + mediaIjazah.getFormat();
										String folder = Executions.getCurrent().getDesktop().getWebApp()
												.getRealPath(AppUtils.PATH_IJAZAH);
										if (mediaIjazah.isBinary()) {
											Files.copy(new File(folder + "/" + ijazahId), mediaIjazah.getStreamData());
										} else {
											BufferedWriter writer = new BufferedWriter(
													new FileWriter(folder + "/" + ijazahId));
											Files.copy(writer, mediaIjazah.getReaderData());
											writer.close();
										}

										objForm.getPendidikan().setIjazahlink(ijazahId);
										objForm.getPendidikan().setIjazahfilename(ijazahfilename);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
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

								// pendidikan
								objForm.getPendidikan().setTanggota(objForm.getPribadi());
								if (cbPendidikanBlAwal.getValue() != null)
									objForm.getPendidikan().setPeriodeblawal(String.valueOf(cbPendidikanBlAwal.getSelectedIndex() + 1));
								if (cbPendidikanBlAkhir.getValue() != null)
									objForm.getPendidikan().setPeriodeblakhir(String.valueOf(cbPendidikanBlAkhir.getSelectedIndex() + 1));
								objForm.getPendidikan().setPeriodethawal(cbPendidikanThAwal.getValue());
								objForm.getPendidikan().setPeriodethakhir(cbPendidikanThAkhir.getValue());
								pendidikanDao.save(session, objForm.getPendidikan());
								
								// pekerjaan
								objForm.getPekerjaan().setTanggota(objForm.getPribadi());
								objForm.getPekerjaan().setProvcode(provkantor.getProvcode());
								objForm.getPekerjaan().setProvname(provkantor.getProvname());
								objForm.getPekerjaan().setKabcode(kabkantor.getKabcode());
								objForm.getPekerjaan().setKabname(kabkantor.getKabname());
								pekerjaanDao.save(session, objForm.getPekerjaan());
								
								if (tevent != null) {
									inv = doSaveEvent(session);
								} else {
									objForm.getPribadi().setStatusreg(AppUtils.STATUS_ANGGOTA_REG);
								}
								
								trx.commit();
								
								if (inv != null) {
									String bodymail_path = Executions.getCurrent().getDesktop().getWebApp()
											.getRealPath("/themes/mail/mailinv.html");
									new Thread(new MailHandler(inv, inv.getInvoicedesc().trim(), inv.getTanggota().getEmail(), bodymail_path)).start();
								}
								
								if (tevent != null) {
									Component comp = winAnggotaAdd.getParent();
									comp.getChildren().clear();
									divForm.getChildren().clear();
									Map<String, Object> map = new HashMap<>();
									map.put("obj", inv.getTeventreg());
									Executions.createComponents("/view/event/eventregdone.zul", comp, map);
								} else {
									divForm.getChildren().clear();
									Map<String, Object> map = new HashMap<>();
									map.put("obj", objForm);
									map.put("regstatus", "reg");
									Executions.createComponents("/regdone.zul", divForm, map);
								}
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
								Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
										Messagebox.ERROR);
							} finally {
								session.close();
							}
						}
					}

				});
	}
	
	private Tinvoice doSaveEvent(Session session) throws Exception {
		Tinvoice inv = null;
		try {
			Teventreg eventreg = new Teventreg();
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
				vaexpdate = tevent.getEventdate();
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
				
				eventregDao.save(session, eventreg);
				
				inv = new InvoiceGenerator().doInvoice(eventreg, eventreg.getVano(), AppUtils.INVOICETYPE_EVENT, tevent.getEventprice(), tevent.getEventname(), vaexpdate);
				inv.setTanggota(objForm.getPribadi());
				new TinvoiceDAO().save(session, inv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inv;
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
				String tempatlahir = (String) ctx.getProperties("pribadi.tempatlahir")[0].getValue();
				if (tempatlahir == null || "".equals(tempatlahir.trim())) {
					this.addInvalidMessage(ctx, "tempatlahir", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				String gender = (String) ctx.getProperties("pribadi.gender")[0].getValue();
				if (gender == null || "".equals(gender.trim())) {
					this.addInvalidMessage(ctx, "gender", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				String warganegara = (String) ctx.getProperties("pribadi.warganegara")[0].getValue();
				if (warganegara == null || "".equals(warganegara.trim())) {
					this.addInvalidMessage(ctx, "warganegara", Labels.getLabel("common.validator.empty"));
					isValid = false;
				} else if (warganegara.equals("WNA")) {
					Mnegara mnegara = (Mnegara) ctx.getProperties("pribadi.mnegara")[0].getValue();
					if (mnegara == null) {
						this.addInvalidMessage(ctx, "negara", Labels.getLabel("common.validator.empty"));
						isValid = false;
					}
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
//				if (noktp != null && provrumah != null && kabrumah != null && dob != null) {
//					if (!StringUtils.ktpValidator(noktp, kabrumah.getKabcode(), new SimpleDateFormat("ddMMyy").format(dob), gender)) {
//						this.addInvalidMessage(ctx, "noktp", "No KTP tidak sesuai dengan data diri Anda");
//					}
//				}

				// Keanggotaan
				String statusanggota = (String) ctx.getProperties("pribadi.statusanggota")[0].getValue();
				if (statusanggota == null || "".equals(statusanggota.trim())) {
					this.addInvalidMessage(ctx, "statusanggota", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				Mcabang mcabang = (Mcabang) ctx.getProperties("pribadi.mcabang")[0].getValue();
				if (mcabang == null) {
					this.addInvalidMessage(ctx, "mcabang", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				// Pekerjaan
				Mrumpun mrumpun = (Mrumpun) ctx.getProperties("pekerjaan.mrumpun")[0].getValue();
				if (mrumpun == null) {
					this.addInvalidMessage(ctx, "mrumpun", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				Mkepegawaian mkepegawaian = (Mkepegawaian) ctx.getProperties("pekerjaan.mkepegawaian")[0].getValue();
				if (mkepegawaian == null) {
					this.addInvalidMessage(ctx, "mkepegawaian", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				Mkepegawaiansub mkepegawaiansub = (Mkepegawaiansub) ctx.getProperties("pekerjaan.mkepegawaiansub")[0]
						.getValue();
				if (mkepegawaiansub == null) {
					this.addInvalidMessage(ctx, "mkepegawaiansub", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				if (provkantor == null) {
					this.addInvalidMessage(ctx, "provkantor", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				if (kabkantor == null) {
					this.addInvalidMessage(ctx, "kabkantor", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				String namakantor = (String) ctx.getProperties("pekerjaan.namakantor")[0].getValue();
				if (namakantor == null || "".equals(namakantor.trim())) {
					this.addInvalidMessage(ctx, "namakantor", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				String alamatkantor = (String) ctx.getProperties("pekerjaan.alamatkantor")[0].getValue();
				if (alamatkantor == null || "".equals(alamatkantor.trim())) {
					this.addInvalidMessage(ctx, "alamatkantor", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				Date tglmulai = (Date) ctx.getProperties("pekerjaan.tglmulai")[0].getValue();
				if (tglmulai == null) {
					this.addInvalidMessage(ctx, "tglmulai", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				// Pendidikan
				Muniversitas muniversitas = (Muniversitas) ctx.getProperties("pendidikan.muniversitas")[0].getValue();
				if (muniversitas == null) {
					this.addInvalidMessage(ctx, "muniversitas", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				Mjenjang mjenjang = (Mjenjang) ctx.getProperties("pendidikan.mjenjang")[0].getValue();
				if (mjenjang == null) {
					this.addInvalidMessage(ctx, "mjenjang", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				// Kontak Darurat
				String namadarurat = (String) ctx.getProperties("pribadi.namadarurat")[0].getValue();
				if (namadarurat == null || "".equals(namadarurat.trim())) {
					this.addInvalidMessage(ctx, "namadarurat", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				String hubungan = (String) ctx.getProperties("pribadi.hubungan")[0].getValue();
				if (hubungan == null || "".equals(hubungan.trim())) {
					this.addInvalidMessage(ctx, "hubungan", Labels.getLabel("common.validator.empty"));
					isValid = false;
				}
				String nohpdarurat = (String) ctx.getProperties("pribadi.nohpdarurat")[0].getValue();
				if (nohpdarurat == null || "".equals(nohpdarurat.trim())) {
					this.addInvalidMessage(ctx, "nohpdarurat", Labels.getLabel("common.validator.empty"));
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

}
