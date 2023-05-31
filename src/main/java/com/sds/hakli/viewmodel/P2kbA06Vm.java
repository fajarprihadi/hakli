package com.sds.hakli.viewmodel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
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
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.MuniversitasDAO;
import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.dao.Tp2kbA06DAO;
import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.domain.Mp2kbkegiatan;
import com.sds.hakli.domain.Muniversitas;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tp2kb;
import com.sds.hakli.domain.Tp2kba06;
import com.sds.hakli.domain.Tp2kbbook;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class P2kbA06Vm {

	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota anggota;
	private Tp2kbA06DAO oDao = new Tp2kbA06DAO();
	private Tp2kbDAO p2kbDao = new Tp2kbDAO();
	private TcounterengineDAO counterDao = new TcounterengineDAO();

	private Mp2kbkegiatan p2kb;
	private Tp2kba06 objForm;
	private Tp2kbbook tpb;
	private BigDecimal nilaiskp_curr;

	private Media media;
	private String docfilename;
	private boolean isInsert;

	@Wire
	private Window winP2kba06;
	@Wire
	private Combobox cbInstitut;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("obj") Mp2kbkegiatan p2kb, @ExecutionArgParam("objForm") Tp2kba06 objForm,
			@ExecutionArgParam("book") Tp2kbbook tpb) {
		Selectors.wireComponents(view, this, false);
		anggota = (Tanggota) zkSession.getAttribute("anggota");
		this.p2kb = p2kb;

		if (tpb != null) {
			this.tpb = tpb;
		}

		if (objForm != null) {
			this.objForm = objForm;
			nilaiskp_curr = objForm.getNilaiskp();
			isInsert = false;
			cbInstitut.setValue(objForm.getMuniversitas().getUniversitas());
		} else
			doReset();
	}

	@NotifyChange("*")
	public void doReset() {
		isInsert = true;
		objForm = new Tp2kba06();
		objForm.setTanggota(anggota);
		objForm.setMp2kbkegiatan(p2kb);
	}

	@Command
	@NotifyChange("docfilename")
	public void doUpload(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
		try {
			UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
			media = event.getMedia();
			docfilename = media.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("*")
	public void doSave() {
		if (objForm.getTglmulai().compareTo(tpb.getTglmulai()) >= 0
				&& objForm.getTglakhir().compareTo(tpb.getTglakhir()) <= 0) {
			Session session = StoreHibernateUtil.openSession();
			Transaction trx = session.beginTransaction();
			try {
				objForm.setNilaiskp(getNilaiSkp(objForm));
				objForm.setCreatedby(anggota.getNoanggota());
				objForm.setCreatetime(new Date());
				objForm.setStatus("WC");

				if (media != null) {
					try {
						String docid = counterDao.getP2kbCounter(objForm.getMp2kbkegiatan().getIdkegiatan());
						String folder = Executions.getCurrent().getDesktop().getWebApp()
								.getRealPath(AppUtils.PATH_P2KB);
						if (media.isBinary()) {
							Files.copy(new File(folder + "/" + docid + "/" + media.getFormat()), media.getStreamData());
						} else {
							BufferedWriter writer = new BufferedWriter(
									new FileWriter(folder + "/" + docid + "/" + media.getFormat()));
							Files.copy(writer, media.getReaderData());
							writer.close();
						}
						objForm.setDocid(docid);
						objForm.setDocpath(AppUtils.PATH_P2KB + "/" + docid + "/" + media.getFormat());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				oDao.save(session, objForm);

				Tp2kb book = p2kbDao.findByFilter("tanggota.tanggotapk = " + anggota.getTanggotapk()
						+ " and mp2kbkegiatan.mp2kbkegiatanpk = " + objForm.getMp2kbkegiatan().getMp2kbkegiatanpk());
				if (book == null) {
					book = new Tp2kb();
					book.setTp2kbbook(tpb);
					book.setTanggota(anggota);
					book.setMp2kbkegiatan(objForm.getMp2kbkegiatan());
					book.setTotalkegiatan(0);
					book.setTotalskp(new BigDecimal(0));
					book.setTotalwaiting(0);
				}
				if (isInsert) {
					book.setTotalkegiatan(book.getTotalkegiatan() + 1);
					book.setTotalskp(book.getTotalskp().add(objForm.getNilaiskp()));
					book.setTotalwaiting(book.getTotalwaiting() + 1);
				} else {
					book.setTotalskp(book.getTotalskp().subtract(nilaiskp_curr));
					book.setTotalskp(book.getTotalskp().add(objForm.getNilaiskp()));
				}
				book.setLastupdated(new Date());
				p2kbDao.save(session, book);

				trx.commit();

				if (isInsert) {
					Clients.showNotification("Proses simpan data berhasil", "info", null, "middle_center", 1500);
				} else {
					Clients.showNotification("Proses pembaruan data berhasil", "info", null, "middle_center", 1500);
				}

				doClose();
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
			} finally {
				session.close();
			}
		} else {
			Messagebox.show("Tanggal kegiatan diluar periode log.");
		}
	}

	public BigDecimal getNilaiSkp(Tp2kba06 obj) throws Exception {
		BigDecimal skp = new BigDecimal(0);
		if (obj.getJenjang().equals("D3")) {
			skp = new BigDecimal(9.5);
		} else if (obj.getJenjang().equals("D4/S1-KL")) {
			skp = new BigDecimal(12.5);
		} else if (obj.getJenjang().equals("Profesi Kesling/MgT/Sp1")) {
			skp = new BigDecimal(22.5);
		} else if (obj.getJenjang().equals("DrTerapan/Sp2")) {
			skp = new BigDecimal(40);
		}
		return skp;
	}

	@Command()
	@NotifyChange("*")
	public void doClose() {
		Map<String, Object> map = new HashMap<>();
		map.put("kegiatan", objForm.getMp2kbkegiatan().getKegiatan());
		Event closeEvent = new Event("onClose", winP2kba06, map);
		Events.postEvent(closeEvent);
	}

	public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				Muniversitas muniversitas = (Muniversitas) ctx.getProperties("muniversitas")[0].getValue();
				if (muniversitas == null)
					this.addInvalidMessage(ctx, "muniversitas", Labels.getLabel("common.validator.empty"));
				String jenjang = (String) ctx.getProperties("jenjang")[0].getValue();
				if (jenjang == null || "".equals(jenjang.trim()))
					this.addInvalidMessage(ctx, "jenjang", Labels.getLabel("common.validator.empty"));
				String prodi = (String) ctx.getProperties("prodi")[0].getValue();
				if (prodi == null || "".equals(prodi.trim()))
					this.addInvalidMessage(ctx, "prodi", Labels.getLabel("common.validator.empty"));
				Date tglmulai = (Date) ctx.getProperties("tglmulai")[0].getValue();
				if (tglmulai == null)
					this.addInvalidMessage(ctx, "tglmulai", Labels.getLabel("common.validator.empty"));
				Date tglakhir = (Date) ctx.getProperties("tglakhir")[0].getValue();
				if (tglakhir == null)
					this.addInvalidMessage(ctx, "tglakhir", Labels.getLabel("common.validator.empty"));
				if (tglmulai != null && tglakhir != null) {
					if (tglmulai.compareTo(tglakhir) > 0)
						this.addInvalidMessage(ctx, "tglmulai",
								"Tanggal mulai harus lebih kecil dari tanggal selesai.");
				}
				if (isInsert && media == null)
					this.addInvalidMessage(ctx, "media", "Silahkan upload dokumen bukti kegiatan");
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

	public Tp2kba06 getObjForm() {
		return objForm;
	}

	public void setObjForm(Tp2kba06 objForm) {
		this.objForm = objForm;
	}

	public String getDocfilename() {
		return docfilename;
	}

	public void setDocfilename(String docfilename) {
		this.docfilename = docfilename;
	}

}
