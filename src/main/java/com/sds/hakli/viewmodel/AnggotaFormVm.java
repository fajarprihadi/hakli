package com.sds.hakli.viewmodel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
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
import org.zkoss.zhtml.Li;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.dao.McabangDAO;
import com.sds.hakli.dao.MchargeDAO;
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
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.dao.TpekerjaanDAO;
import com.sds.hakli.dao.TpendidikanDAO;
import com.sds.hakli.domain.Mcabang;
import com.sds.hakli.domain.Mcharge;
import com.sds.hakli.domain.Mjenjang;
import com.sds.hakli.domain.Mkab;
import com.sds.hakli.domain.Mkepegawaian;
import com.sds.hakli.domain.Mkepegawaiansub;
import com.sds.hakli.domain.Mnegara;
import com.sds.hakli.domain.Mprov;
import com.sds.hakli.domain.Mrumpun;
import com.sds.hakli.domain.Muniversitas;
import com.sds.hakli.domain.Tanggota;
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
import com.sds.utils.CompUtils;
import com.sds.utils.StringUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class AnggotaFormVm {

	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();

	private String acttype;

	private TanggotaDAO oDao = new TanggotaDAO();
	private TpendidikanDAO pendidikanDao = new TpendidikanDAO();
	private TpekerjaanDAO pekerjaanDao = new TpekerjaanDAO();
	private MprovDAO provDao = new MprovDAO();
	private MkabDAO kabDao = new MkabDAO();

	private Tanggota pribadi;
	private Tpendidikan pendidikan;
	private Tpekerjaan pekerjaan;

	private Date dob;
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

	private List<Tpendidikan> pendidikans;
	private List<Tpekerjaan> pekerjaans;
	private String processinfo;
	private String ijazahfilename;

	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat datetimeFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");

	private Media media;
	private Media mediaIjazah;

	@Wire
	private Window winAnggotaForm;
	@Wire
	private Grid gridPendidikan;
	@Wire
	private Grid gridPekerjaan;
	@Wire
	private Button btSave;

	@Wire
	private Combobox cbDobDay;
	@Wire
	private Combobox cbDobMonth;
	@Wire
	private Combobox cbDobYear;
	@Wire
	private Combobox cbNegara;
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
	private Combobox cbKepegawaian;
	@Wire
	private Combobox cbKepegawaiansub;
	@Wire
	private Combobox cbUniversitas;
	@Wire
	private Combobox cbJenjang;
	@Wire
	private Combobox cbRegion;
	@Wire
	private Combobox cbCabang;
	@Wire
	private Combobox cbPendidikanThAwal;
	@Wire
	private Combobox cbPendidikanBlAwal;
	@Wire
	private Combobox cbPendidikanThAkhir;
	@Wire
	private Combobox cbPendidikanBlAkhir;

	@Wire
	private Image photo;

	@Wire
	private Button btAddPekerjaan;
	@Wire
	private Button btAddPendidikan;
	@Wire
	private Groupbox gbPekerjaan;
	@Wire
	private Groupbox gbPendidikan;
	@Wire
	private Button btSavePekerjaan;
	@Wire
	private Button btSavePendidikan;

	@Wire
	private Div divProcessinfo;

	@Wire
	private Vlayout vlayNation;

	@Wire
	private Li tabApproval;
	@Wire
	private Radiogroup rgDecision;
	@Wire
	private Radio rdVerDecYes;
	@Wire
	private Radio rdVerDecNo;
	@Wire
	private Textbox tbMemo;
	@Wire
	private Button btSaveApproval;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tanggota obj,
			@ExecutionArgParam("pekerjaans") List<Tpekerjaan> pekerjaans, 
			@ExecutionArgParam("pendidikans") List<Tpendidikan> pendidikans, @ExecutionArgParam("acttype") String acttype) {
		Selectors.wireComponents(view, this, false);
		try {
			this.acttype = acttype;
			Tanggota anggota = (Tanggota) zkSession.getAttribute("anggota");
			if (anggota == null)
				anggota = new Tanggota();

			if (obj != null)
				pribadi = obj;
			else {
				pribadi = oDao.findByPk(anggota.getTanggotapk());
				if (pribadi == null)
					pribadi = new Tanggota();
			}

			init();

			if (pribadi.getPhotolink() != null) {
				photo.setSrc(AppUtils.PATH_PHOTO + "/" + pribadi.getPhotolink());
				photo.setVisible(true);
			}

			dob = pribadi.getTgllahir();
			if (dob != null) {
				cbDobDay.setValue(new SimpleDateFormat("dd").format(dob));
				cbDobMonth.setSelectedIndex(Integer.parseInt(new SimpleDateFormat("MM").format(dob)) - 1);
				cbDobYear.setValue(new SimpleDateFormat("yyyy").format(dob));
			}

			provrumah = provDao.findByFilter("provcode = '" + pribadi.getProvcode() + "'");
			if (provrumah != null) {
				cbProvrumah.setValue(provrumah.getProvname());
				doLoadKabrumah(provrumah);
			}
			kabrumah = kabDao.findByFilter(
					"provcode = '" + pribadi.getProvcode() + "' and kabcode = '" + pribadi.getKabcode() + "'");
			cbKabrumah.setValue(pribadi.getKabname());

			doLoadNegara(pribadi.getWarganegara());

			if (pribadi.getMcabang() != null) {
				region = pribadi.getMcabang().getMprov();
				if (region != null) {
					cbRegion.setValue(region.getProvname());
					doLoadCabang(region);
				}
				cbCabang.setValue(pribadi.getMcabang().getCabang());
			}

			gridPendidikan.setRowRenderer(new RowRenderer<Tpendidikan>() {

				@Override
				public void render(Row row, Tpendidikan data, int index) throws Exception {
					row.getChildren().add(
							new Label(data.getMuniversitas() != null ? data.getMuniversitas().getUniversitas() : ""));
					row.getChildren().add(new Label(data.getMjenjang() != null ? data.getMjenjang().getJenjang() : ""));
					row.getChildren().add(new Label(data.getPeminatan1()));
					row.getChildren().add(new Label(data.getPeminatan2()));
					String periode = "";
					periode = (data.getPeriodeblawal() != null ? data.getPeriodeblawal() : "") + " " + (data.getPeriodethawal() != null ? data.getPeriodethawal() : "") + " s/d "
							+ (data.getPeriodeblakhir() != null ? data.getPeriodeblakhir() : "") + " " + (data.getPeriodethakhir() != null ? data.getPeriodethakhir() : "");
					row.getChildren().add(new Label(periode));
					
					Button btView = new Button();
					btView.setIconSclass("z-icon-eye");
					btView.setSclass("btn btn-primary btn-sm");
					btView.setAutodisable("self");
					btView.setTooltiptext("Lihat Ijazah");
					btView.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							File file = new File(Executions.getCurrent().getDesktop().getWebApp()
									.getRealPath(AppUtils.PATH_IJAZAH) + "/" + data.getIjazahlink());
							if (data.getIjazahlink() != null && data.getIjazahlink().trim().length() > 0 && file.exists()) {
								Map<String, String> mapDocument = new HashMap<>();
								mapDocument.put("src", AppUtils.PATH_IJAZAH + "/" + data.getIjazahlink());
								zkSession.setAttribute("mapDocument", mapDocument);
								Executions.getCurrent().sendRedirect("/view/docviewer.zul", "_blank");
							} else {
								Messagebox.show("Dokumen Ijazah Tidak Tersedia", WebApps.getCurrent().getAppName(), Messagebox.OK,
										Messagebox.INFORMATION);
							}
						}
					});
					
					Hlayout hlayout = new Hlayout();
					hlayout.appendChild(btView);
					if (data.getNoijazah() != null && data.getNoijazah().trim().length() > 0)
						hlayout.appendChild(new Label(data.getNoijazah()));
					row.getChildren().add(hlayout);
					
					
					if (acttype != null && acttype.equals("edit")) {
						Button btEdit = new Button();
						btEdit.setIconSclass("z-icon-edit");
						btEdit.setSclass("btn btn-primary btn-sm");
						btEdit.setAutodisable("self");
						btEdit.setTooltiptext("Edit");
						btEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								doAddPendidikan(data);
								BindUtils.postNotifyChange(AnggotaFormVm.this, "pendidikan");
							}
						});

						Button btDel = new Button();
						btDel.setIconSclass("z-icon-trash-o");
						btDel.setSclass("btn btn-danger btn-sm");
						btDel.setAutodisable("self");
						btDel.setTooltiptext("Hapus");

						btDel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								Messagebox.show("Anda ingin menghapus data ini?", "Confirm Dialog",
										Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

											@Override
											public void onEvent(Event event) throws Exception {
												if (event.getName().equals("onOK")) {
													try {

													} catch (Exception e) {
														Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(),
																Messagebox.OK, Messagebox.ERROR);
														e.printStackTrace();
													}
												}
											}

										});
							}
						});

						Div div = new Div();
						div.appendChild(btEdit);
						div.appendChild(new Separator("vertical"));
						div.appendChild(btDel);

						row.getChildren().add(div);
					} else {
						row.getChildren().add(new Label());
					}
				}
			});

			gridPekerjaan.setRowRenderer(new RowRenderer<Tpekerjaan>() {

				@Override
				public void render(Row row, Tpekerjaan data, int index) throws Exception {
					row.getChildren().add(new Label(data.getNamakantor()));
					row.getChildren().add(new Label(data.getProvname()));
					row.getChildren().add(new Label(data.getKabname()));
					row.getChildren().add(new Label(data.getAlamatkantor()));
					row.getChildren().add(new Label(data.getMrumpun() != null ? data.getMrumpun().getRumpun() : ""));
					row.getChildren().add(
							new Label(data.getMkepegawaian() != null ? data.getMkepegawaian().getKepegawaian() : ""));
					row.getChildren().add(new Label(data.getJabatankantor()));
					row.getChildren().add(new Label(data.getNip()));
					row.getChildren()
							.add(new Label(data.getTglmulai() != null ? dateFormatter.format(data.getTglmulai()) : ""));
					row.getChildren().add(new Label(data.getNoskkantor()));
					row.getChildren().add(new Label(data.getKeterangankerja()));
					row.getChildren().add(new Label(data.getTelpkantor()));
					row.getChildren().add(new Label(data.getFaxkantor()));
					
					if (acttype != null && acttype.equals("edit")) {
						Button btEdit = new Button();
						btEdit.setIconSclass("z-icon-edit");
						btEdit.setSclass("btn btn-primary btn-sm");
						btEdit.setAutodisable("self");
						btEdit.setTooltiptext("Edit");
						btEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								doAddPekerjaan(data);
								BindUtils.postNotifyChange(AnggotaFormVm.this, "pekerjaan");
							}
						});

						Button btDel = new Button();
						btDel.setIconSclass("z-icon-trash-o");
						btDel.setSclass("btn btn-danger btn-sm");
						btDel.setAutodisable("self");
						btDel.setTooltiptext("Hapus");

						btDel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								Messagebox.show("Anda ingin menghapus data ini?", "Confirm Dialog",
										Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

											@Override
											public void onEvent(Event event) throws Exception {
												if (event.getName().equals("onOK")) {
													try {

													} catch (Exception e) {
														Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(),
																Messagebox.OK, Messagebox.ERROR);
														e.printStackTrace();
													}
												}
											}

										});
							}
						});

						Div div = new Div();
						div.appendChild(btEdit);
						div.appendChild(new Separator("vertical"));
						div.appendChild(btDel);

						row.getChildren().add(div);
					} else {
						row.getChildren().add(new Label());
					}
				}
			});

			if (pekerjaans != null) {
				this.pekerjaans = pekerjaans;
			} else {
				this.pekerjaans = pekerjaanDao.listByFilter("tanggota.tanggotapk = " + pribadi.getTanggotapk(),
						"tpekerjaanpk desc");
			}
			gridPekerjaan.setModel(new ListModelList<>(this.pekerjaans));
			
			if (pendidikans != null) {
				this.pendidikans = pendidikans;
			} else {
				this.pendidikans = pendidikanDao.listByFilter("tanggota.tanggotapk = " + pribadi.getTanggotapk(),
						"tpendidikanpk desc");
			}
			gridPendidikan.setModel(new ListModelList<>(this.pendidikans));
			
			if (acttype != null && acttype.equals("approval")) {
				tabApproval.setVisible(true);
				List<Component> compExclude = new ArrayList<>();
				compExclude.add(rgDecision);
				compExclude.add(rdVerDecYes);
				compExclude.add(rdVerDecNo);
				compExclude.add(tbMemo);
				compExclude.add(btSaveApproval);
				CompUtils.doDisableComponent(winAnggotaForm, true, compExclude);
			} else if (acttype != null && acttype.equals("decided")) {
				CompUtils.doDisableComponent(winAnggotaForm, true, null);
			} else if (acttype != null && acttype.equals("view")) {
				tabApproval.setVisible(false);
				CompUtils.doDisableComponent(winAnggotaForm, true, null);
			} else if (acttype != null && acttype.equals("edit")) {
				tabApproval.setVisible(false);
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

				Comboitem itemPendidikanThAwal = new Comboitem(String.valueOf(i));
				cbPendidikanThAwal.appendChild(itemPendidikanThAwal);

				Comboitem itemPendidikanThAkhir = new Comboitem(String.valueOf(i));
				cbPendidikanThAkhir.appendChild(itemPendidikanThAkhir);
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
				vlayNation.setVisible(true);
			} else {
				vlayNation.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("kabrumahModel")
	public void doLoadKabrumah(@BindingParam("prov") Mprov prov) {
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
	public void doLoadKabkantor(@BindingParam("prov") Mprov prov) {
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
	public void doLoadKepegawaiansub(@BindingParam("mkepegawaian") Mkepegawaian kepegawaian) {
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

	@Command
	@NotifyChange({"pendidikan", "ijazahfilename"})
	public void doAddPendidikan(Tpendidikan obj) {
		try {
			if (obj != null) {
				pendidikan = obj;
				gbPendidikan.setVisible(true);
				btAddPendidikan.setLabel("Cancel");
				btAddPendidikan.setIconSclass("z-icon-reply");
				btSavePendidikan.setLabel("Perbarui");

				cbUniversitas.setValue(
						pendidikan.getMuniversitas() != null ? pendidikan.getMuniversitas().getUniversitas() : "");
				cbJenjang.setValue(pendidikan.getMjenjang() != null ? pendidikan.getMjenjang().getJenjang() : "");
				cbPendidikanBlAwal.setValue(pendidikan.getPeriodeblawal());
				cbPendidikanThAwal.setValue(pendidikan.getPeriodethawal());
				cbPendidikanBlAkhir.setValue(pendidikan.getPeriodeblakhir());
				cbPendidikanThAkhir.setValue(pendidikan.getPeriodethakhir());
				
				mediaIjazah = null;
				ijazahfilename = null;
				if (pendidikan.getIjazahfilename() != null)
					ijazahfilename = pendidikan.getIjazahfilename();
			} else if (btAddPendidikan.getLabel().equals("Tambah Pendidikan")) {
				pendidikan = new Tpendidikan();
				gbPendidikan.setVisible(true);
				cbUniversitas.setValue(null);
				cbJenjang.setValue(null);
				cbPendidikanBlAwal.setValue(null);
				cbPendidikanThAwal.setValue(null);
				cbPendidikanBlAkhir.setValue(null);
				cbPendidikanThAkhir.setValue(null);
				btAddPendidikan.setLabel("Cancel");
				btAddPendidikan.setIconSclass("z-icon-reply");
				btSavePekerjaan.setLabel("Submit");
				
				mediaIjazah = null;
				ijazahfilename = null;
			} else {
				pendidikan = null;
				gbPendidikan.setVisible(false);
				btAddPendidikan.setLabel("Tambah Pendidikan");
				btAddPendidikan.setIconSclass("z-icon-plus-square");
				
				mediaIjazah = null;
				ijazahfilename = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("pekerjaan")
	public void doAddPekerjaan(Tpekerjaan obj) {
		try {
			if (obj != null) {
				pekerjaan = obj;
				gbPekerjaan.setVisible(true);
				btAddPekerjaan.setLabel("Cancel");
				btAddPekerjaan.setIconSclass("z-icon-reply");
				btSavePekerjaan.setLabel("Perbarui");

				provkantor = provDao.findByFilter("provcode = '" + pekerjaan.getProvcode() + "'");
				kabkantor = kabDao.findByFilter(
						"provcode = '" + pekerjaan.getProvcode() + "' and kabcode = '" + pekerjaan.getKabcode() + "'");
				doLoadKabkantor(provkantor);
				cbProvkantor.setValue(pekerjaan.getProvname());
				cbKabkantor.setValue(pekerjaan.getKabname());
				cbRumpun.setValue(pekerjaan.getMrumpun() != null ? pekerjaan.getMrumpun().getRumpun() : "");
				cbKepegawaian.setValue(
						pekerjaan.getMkepegawaian() != null ? pekerjaan.getMkepegawaian().getKepegawaian() : "");
				cbKepegawaiansub.setValue(
						pekerjaan.getMkepegawaiansub() != null ? pekerjaan.getMkepegawaiansub().getKepegawaiansub()
								: "");

			} else if (btAddPekerjaan.getLabel().equals("Tambah Pekerjaan")) {
				pekerjaan = new Tpekerjaan();
				provkantor = null;
				kabkantor = null;
				gbPekerjaan.setVisible(true);
				cbProvkantor.setValue(null);
				cbKabkantor.setValue(null);
				cbRumpun.setValue(null);
				cbKepegawaian.setValue(null);
				cbKepegawaiansub.setValue(null);
				btAddPekerjaan.setLabel("Cancel");
				btAddPekerjaan.setIconSclass("z-icon-reply");
				btSavePekerjaan.setLabel("Submit");
			} else {
				pekerjaan = null;
				gbPekerjaan.setVisible(false);
				btAddPekerjaan.setLabel("Tambah Pekerjaan");
				btAddPekerjaan.setIconSclass("z-icon-plus-square");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		Session session = StoreHibernateUtil.openSession();
		Transaction trx = null;
		try {
			if (media != null) {
				try {
					String folder = Executions.getCurrent().getDesktop().getWebApp().getRealPath(AppUtils.PATH_PHOTO);
					if (media.isBinary()) {
						Files.copy(new File(folder + "/" + media.getName()), media.getStreamData());
					} else {
						BufferedWriter writer = new BufferedWriter(new FileWriter(folder + "/" + media.getName()));
						Files.copy(writer, media.getReaderData());
						writer.close();
					}

					pribadi.setPhotolink(media.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			trx = session.beginTransaction();
			pribadi.setLastupdated(new Date());
			pribadi.setUpdatedby(pribadi.getNoanggota());
			pribadi.setTgllahir(dob);
			pribadi.setProvcode(provrumah.getProvcode());
			pribadi.setProvname(provrumah.getProvname());
			pribadi.setKabcode(kabrumah.getKabcode());
			pribadi.setKabname(kabrumah.getKabname());
			oDao.save(session, pribadi);
			trx.commit();

			Clients.showNotification("Proses pembaruan data pribadi berhasil", "info", null, "middle_center", 1500);
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
		} finally {
			session.close();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doSavePendidikan() {
		Session session = StoreHibernateUtil.openSession();
		Transaction trx = null;
		try {
			boolean isInsert = false;
			if (pendidikan.getTpendidikanpk() == null) {
				isInsert = true;
				pendidikan.setTanggota(pribadi);
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

					pendidikan.setIjazahlink(ijazahId);
					pendidikan.setIjazahfilename(ijazahfilename);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			trx = session.beginTransaction();
			pendidikan.setLastupdated(new Date());
			pendidikan.setUpdatedby(pribadi.getNoanggota());
			pendidikan.setPeriodeblawal(cbPendidikanBlAwal.getValue());
			pendidikan.setPeriodethawal(cbPendidikanThAwal.getValue());
			pendidikan.setPeriodeblakhir(cbPendidikanBlAkhir.getValue());
			pendidikan.setPeriodethakhir(cbPendidikanThAkhir.getValue());
			pendidikanDao.save(session, pendidikan);
			trx.commit();

			if (isInsert) {
				Clients.showNotification("Proses tambah data pendidikan berhasil", "info", null, "middle_center", 1500);
				pendidikans = pendidikanDao.listByFilter("tanggota.tanggotapk = " + pribadi.getTanggotapk(),
						"tpendidikanpk desc");
				gridPendidikan.setModel(new ListModelList<>(pendidikans));
				pendidikan = null;
				gbPendidikan.setVisible(false);
				btAddPendidikan.setLabel("Tambah Pendidikan");
				btAddPendidikan.setIconSclass("z-icon-plus-square");
			} else {
				Clients.showNotification("Proses pembaruan data pendidikan berhasil", "info", null, "middle_center",
						1500);
				pendidikans = pendidikanDao.listByFilter("tanggota.tanggotapk = " + pribadi.getTanggotapk(),
						"tpendidikanpk desc");
				gridPendidikan.setModel(new ListModelList<>(pendidikans));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
		} finally {
			session.close();
		}
	}

	@Command
	@NotifyChange("*")
	public void doSavePekerjaan() {
		Session session = StoreHibernateUtil.openSession();
		Transaction trx = null;
		try {
			boolean isInsert = false;
			if (pekerjaan.getTpekerjaanpk() == null) {
				isInsert = true;
				pekerjaan.setTanggota(pribadi);
			}

			trx = session.beginTransaction();
			pekerjaan.setLastupdated(new Date());
			pekerjaan.setUpdatedby(pribadi.getNoanggota());
			pekerjaan.setProvcode(provkantor.getProvcode());
			pekerjaan.setProvname(provkantor.getProvname());
			pekerjaan.setKabcode(kabkantor.getKabcode());
			pekerjaan.setKabname(kabkantor.getKabname());
			pekerjaanDao.save(session, pekerjaan);
			trx.commit();

			if (isInsert) {
				Clients.showNotification("Proses tambah data pekerjaan berhasil", "info", null, "middle_center", 1500);
				pekerjaans = pekerjaanDao.listByFilter("tanggota.tanggotapk = " + pribadi.getTanggotapk(),
						"tpekerjaanpk desc");
				gridPekerjaan.setModel(new ListModelList<>(pekerjaans));
				pekerjaan = null;
				gbPekerjaan.setVisible(false);
				btAddPekerjaan.setLabel("Tambah Pekerjaan");
				btAddPekerjaan.setIconSclass("z-icon-plus-square");
			} else {
				Clients.showNotification("Proses pembaruan data pekerjaan berhasil", "info", null, "middle_center",
						1500);
				pekerjaans = pekerjaanDao.listByFilter("tanggota.tanggotapk = " + pribadi.getTanggotapk(),
						"tpekerjaanpk desc");
				gridPekerjaan.setModel(new ListModelList<>(pekerjaans));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
		} finally {
			session.close();
		}
	}

	@Command
	@NotifyChange("*")
	public void doSaveKontak() {
		Session session = StoreHibernateUtil.openSession();
		Transaction trx = null;
		try {
			trx = session.beginTransaction();
			pribadi.setLastupdated(new Date());
			pribadi.setUpdatedby(pribadi.getNoanggota());
			oDao.save(session, pribadi);
			trx.commit();

			Clients.showNotification("Proses pembaruan data kontak berhasil", "info", null, "middle_center", 1500);
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
		} finally {
			session.close();
		}
	}

	@Command
	@NotifyChange("*")
	public void doSaveApproval() {
		try {
			if (pribadi.getStatusreg().equals(AppUtils.STATUS_ANGGOTA_REG_PAYMENT)) {
				try {
					BigDecimal invamount = new BigDecimal(0);
					for (Mcharge charge : new MchargeDAO().listByFilter("chargetype = '" + AppUtils.CHARGETYPE_REG + "'",
							"isbase desc")) {
						invamount = invamount.add(charge.getChargeamount());
					}

					BriapiBean bean = AppData.getBriapibean();
					BriApiExt briapi = new BriApiExt(bean);
					BriApiToken briapiToken = briapi.getToken();

					Date vaexpdate = null;
					BrivaData briva = new BrivaData();
					if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
						briva.setAmount(invamount.toString());
						briva.setInstitutionCode(bean.getBriva_institutioncode());
						briva.setBrivaNo(bean.getBriva_cid());

						String custcode_cabang = "0000" + pribadi.getMcabang().getKodecabang();
						String custcode = custcode_cabang.substring(custcode_cabang.length()-4, custcode_cabang.length());
						briva.setCustCode(new TcounterengineDAO().getVaCounter(custcode));
						briva.setKeterangan("Pendaftaran Anggota HAKLI");
						briva.setNama(pribadi.getNama());
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.DAY_OF_MONTH, 10);
						vaexpdate = cal.getTime();
						briva.setExpiredDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vaexpdate));

						BrivaCreateResp brivaCreated = null;
						brivaCreated = briapi.createBriva(briapiToken.getAccess_token(), briva);
						if (brivaCreated != null && brivaCreated.getStatus()) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								pribadi.setVareg(briva.getBrivaNo() + briva.getCustCode());
								pribadi.setVaregstatus(1);
								String kodecabang = pribadi.getMcabang().getKodecabang();
								kodecabang = "0000" + kodecabang;
								kodecabang = kodecabang.substring(kodecabang.length() - 4, kodecabang.length());
								String noanggota = new TcounterengineDAO().generateNoAnggota(kodecabang);
								noanggota = noanggota + StringUtils.checkDigit(Integer.parseInt(noanggota));
								pribadi.setNoanggota(noanggota);
								
								Random rand = new Random();
								String password = String.valueOf(rand.nextInt(1000000)) + "000000";
								pribadi.setPassword(password.substring(0, 6));
								pribadi.setRegdecisiontime(new Date());
								//pribadi.setPeriodekta(new Date());
								oDao.save(session, pribadi);

								Tinvoice inv = new InvoiceGenerator().doInvoice(pribadi,
										briva.getBrivaNo() + briva.getCustCode(), AppUtils.INVOICETYPE_REG, invamount,
										"Pendaftararan Anggota HAKLI", vaexpdate);
								new TinvoiceDAO().save(session, inv);

								trx.commit();

								String bodymail_path = Executions.getCurrent().getDesktop().getWebApp()
										.getRealPath("/themes/mail/mailinv.html");
								new Thread(new MailHandler(inv, bodymail_path)).start();

								processinfo = "Proses persetujuan pendaftaran anggota berhasil. Informasi permintaan pembayaran sudah dikirim ke e-mail anggota dengan Nomor VA "
										+ pribadi.getVareg();
								divProcessinfo.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
								trx.rollback();
								Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
										Messagebox.ERROR);
							} finally {
								session.close();
							}

						} else {
							if (brivaCreated != null) {
								processinfo = "Proses generate virtual account gagal : " + brivaCreated.getErrDesc();
								divProcessinfo.setVisible(true);
							}
								
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
							Messagebox.ERROR);
				} 
			} else if (pribadi.getStatusreg().equals(AppUtils.STATUS_ANGGOTA_REG_DECLINE)) {
				Session session = StoreHibernateUtil.openSession();
				Transaction trx = session.beginTransaction();
				try {
					pribadi.setRegdecisiontime(new Date());
					oDao.save(session, pribadi);
					trx.commit();
					processinfo = "Proses penolakan pendaftaran anggota berhasil diproses.";
					divProcessinfo.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					trx.rollback();
					Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
							Messagebox.ERROR);
				} finally {
					session.close();
				}
			}
			btSaveApproval.setDisabled(true);
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
		}
	}

	public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String nama = (String) ctx.getProperties("nama")[0].getValue();
				if (nama == null || "".equals(nama.trim()))
					this.addInvalidMessage(ctx, "nama", Labels.getLabel("common.validator.empty"));
				String noktp = (String) ctx.getProperties("noktp")[0].getValue();
				if (noktp == null || "".equals(noktp.trim()))
					this.addInvalidMessage(ctx, "noktp", Labels.getLabel("common.validator.empty"));
				String tempatlahir = (String) ctx.getProperties("tempatlahir")[0].getValue();
				if (tempatlahir == null || "".equals(tempatlahir.trim()))
					this.addInvalidMessage(ctx, "tempatlahir", Labels.getLabel("common.validator.empty"));
				String gender = (String) ctx.getProperties("gender")[0].getValue();
				if (gender == null || "".equals(gender.trim()))
					this.addInvalidMessage(ctx, "gender", Labels.getLabel("common.validator.empty"));
				String warganegara = (String) ctx.getProperties("warganegara")[0].getValue();
				if (warganegara == null || "".equals(warganegara.trim()))
					this.addInvalidMessage(ctx, "warganegara", Labels.getLabel("common.validator.empty"));
				else if (warganegara.equals("WNA")) {
					Mnegara mnegara = (Mnegara) ctx.getProperties("mnegara")[0].getValue();
					if (mnegara == null)
						this.addInvalidMessage(ctx, "mnegara", Labels.getLabel("common.validator.empty"));
				}
				if (cbDobDay.getValue().trim().length() > 0 && cbDobMonth.getValue().trim().length() > 0
						&& cbDobYear.getValue().trim().length() > 0) {
					String strDob = cbDobYear.getValue().toString() + "-" + (cbDobMonth.getSelectedIndex() + 1) + "-"
							+ cbDobDay.getValue().toString();
					try {
						dob = dateFormatter.parse(strDob);
					} catch (ParseException e) {
						this.addInvalidMessage(ctx, "tgllahir", "Data tanggal lahir tidak sesuai");
						e.printStackTrace();
					}
				}

				if (provrumah == null)
					this.addInvalidMessage(ctx, "provrumah", Labels.getLabel("common.validator.empty"));
				if (kabrumah == null)
					this.addInvalidMessage(ctx, "kabrumah", Labels.getLabel("common.validator.empty"));
				String alamat = (String) ctx.getProperties("alamat")[0].getValue();
				if (alamat == null || "".equals(alamat.trim()))
					this.addInvalidMessage(ctx, "alamat", Labels.getLabel("common.validator.empty"));
			}
		};
	}

	public Validator getValidatorKeanggotaan() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {

			}
		};
	}

	public Validator getValidatorPendidikan() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				Muniversitas muniversitas = (Muniversitas) ctx.getProperties("muniversitas")[0].getValue();
				if (muniversitas == null)
					this.addInvalidMessage(ctx, "muniversitas", Labels.getLabel("common.validator.empty"));
				Mjenjang mjenjang = (Mjenjang) ctx.getProperties("mjenjang")[0].getValue();
				if (mjenjang == null)
					this.addInvalidMessage(ctx, "mjenjang", Labels.getLabel("common.validator.empty"));
			}
		};
	}

	public Validator getValidatorPekerjaan() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				Mrumpun mrumpun = (Mrumpun) ctx.getProperties("mrumpun")[0].getValue();
				if (mrumpun == null)
					this.addInvalidMessage(ctx, "mrumpun", Labels.getLabel("common.validator.empty"));
				Mkepegawaian mkepegawaian = (Mkepegawaian) ctx.getProperties("mkepegawaian")[0].getValue();
				if (mkepegawaian == null)
					this.addInvalidMessage(ctx, "mkepegawaian", Labels.getLabel("common.validator.empty"));
				Mkepegawaiansub mkepegawaiansub = (Mkepegawaiansub) ctx.getProperties("mkepegawaiansub")[0].getValue();
				if (mkepegawaiansub == null)
					this.addInvalidMessage(ctx, "mkepegawaiansub", Labels.getLabel("common.validator.empty"));
				if (provkantor == null)
					this.addInvalidMessage(ctx, "provkantor", Labels.getLabel("common.validator.empty"));
				if (kabkantor == null)
					this.addInvalidMessage(ctx, "kabkantor", Labels.getLabel("common.validator.empty"));
				String namakantor = (String) ctx.getProperties("namakantor")[0].getValue();
				if (namakantor == null || "".equals(namakantor.trim()))
					this.addInvalidMessage(ctx, "namakantor", Labels.getLabel("common.validator.empty"));
				String jabatan = (String) ctx.getProperties("jabatankantor")[0].getValue();
				if (jabatan == null || "".equals(jabatan.trim()))
					this.addInvalidMessage(ctx, "jabatan", Labels.getLabel("common.validator.empty"));
				Date tglmulai = (Date) ctx.getProperties("tglmulai")[0].getValue();
				if (tglmulai == null)
					this.addInvalidMessage(ctx, "tglmulai", Labels.getLabel("common.validator.empty"));
				String nip = (String) ctx.getProperties("nip")[0].getValue();
				if (nip == null || "".equals(nip.trim()))
					this.addInvalidMessage(ctx, "nip", Labels.getLabel("common.validator.empty"));
			}
		};
	}

	public Validator getValidatorKontak() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String hp = (String) ctx.getProperties("hp")[0].getValue();
				if (hp == null || "".equals(hp.trim()))
					this.addInvalidMessage(ctx, "hp", Labels.getLabel("common.validator.empty"));
				String email = (String) ctx.getProperties("email")[0].getValue();
				if (email == null || "".equals(email.trim()))
					this.addInvalidMessage(ctx, "email", Labels.getLabel("common.validator.empty"));
				else if (!StringUtils.emailValidator(email)) {
					this.addInvalidMessage(ctx, "email", "Format e-Mail tidak sesuai");
				}
				String namadarurat = (String) ctx.getProperties("namadarurat")[0].getValue();
				if (namadarurat == null || "".equals(namadarurat.trim()))
					this.addInvalidMessage(ctx, "namadarurat", Labels.getLabel("common.validator.empty"));
				String hubungan = (String) ctx.getProperties("hubungan")[0].getValue();
				if (hubungan == null || "".equals(hubungan.trim()))
					this.addInvalidMessage(ctx, "hubungan", Labels.getLabel("common.validator.empty"));
				String nohpdarurat = (String) ctx.getProperties("nohpdarurat")[0].getValue();
				if (nohpdarurat == null || "".equals(nohpdarurat.trim()))
					this.addInvalidMessage(ctx, "nohpdarurat", Labels.getLabel("common.validator.empty"));
			}
		};
	}
	
	public Validator getValidatorApproval() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String statusreg = (String) ctx.getProperties("statusreg")[0].getValue();
				if (statusreg == null || "".equals(statusreg.trim()))
					this.addInvalidMessage(ctx, "statusreg", Labels.getLabel("common.validator.empty"));
				else {
					if (!statusreg.equals(AppUtils.STATUS_ANGGOTA_REG_PAYMENT) && !statusreg.equals(AppUtils.STATUS_ANGGOTA_REG_DECLINE))
						this.addInvalidMessage(ctx, "statusreg", Labels.getLabel("common.validator.empty"));
					else if (statusreg.equals(AppUtils.STATUS_ANGGOTA_REG_DECLINE)) {
						String regmemo = (String) ctx.getProperties("regmemo")[0].getValue();
						if (regmemo == null || "".equals(regmemo.trim()))
							this.addInvalidMessage(ctx, "regmemo", Labels.getLabel("common.validator.empty"));
					}
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

	public Tanggota getPribadi() {
		return pribadi;
	}

	public void setPribadi(Tanggota pribadi) {
		this.pribadi = pribadi;
	}

	public String getProcessinfo() {
		return processinfo;
	}

	public void setProcessinfo(String processinfo) {
		this.processinfo = processinfo;
	}

	public Tpendidikan getPendidikan() {
		return pendidikan;
	}

	public void setPendidikan(Tpendidikan pendidikan) {
		this.pendidikan = pendidikan;
	}

	public Tpekerjaan getPekerjaan() {
		return pekerjaan;
	}

	public void setPekerjaan(Tpekerjaan pekerjaan) {
		this.pekerjaan = pekerjaan;
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

	public ListModelList<Mnegara> getNegaraModel() {
		return negaraModel;
	}

	public void setNegaraModel(ListModelList<Mnegara> negaraModel) {
		this.negaraModel = negaraModel;
	}

	public Mprov getRegion() {
		return region;
	}

	public void setRegion(Mprov region) {
		this.region = region;
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

	public String getIjazahfilename() {
		return ijazahfilename;
	}

	public void setIjazahfilename(String ijazahfilename) {
		this.ijazahfilename = ijazahfilename;
	}

}
