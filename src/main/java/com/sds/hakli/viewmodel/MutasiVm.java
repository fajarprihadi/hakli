package com.sds.hakli.viewmodel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.sds.hakli.dao.McabangDAO;
import com.sds.hakli.dao.MprovDAO;
import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.dao.TmutasiDAO;
import com.sds.hakli.domain.Mcabang;
import com.sds.hakli.domain.Mprov;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tmutasi;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class MutasiVm {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota anggota;

	private TmutasiDAO oDao = new TmutasiDAO();
	private TcounterengineDAO counterDao = new TcounterengineDAO();

	private Tmutasi obj;
	private Mprov mprov;
	private Mcabang mcabang;

	private String docfilename;
	private Media media;
	private Integer pageTotalSize;

	private List<Tmutasi> objList = new ArrayList<>();
	private List<Mcabang> mcabangmodel = new ArrayList<>();

	@Wire
	private Grid grid;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		anggota = (Tanggota) zkSession.getAttribute("anggota");

		doReset();

		grid.setRowRenderer(new RowRenderer<Tmutasi>() {

			@Override
			public void render(Row row, Tmutasi data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index + 1)));
				row.getChildren().add(new Label(data.getProvcurr()));
				row.getChildren().add(new Label(data.getCabangcurr()));
				row.getChildren().add(new Label(new SimpleDateFormat("dd-MM-yyyy").format(data.getCreatetime())));
				row.getChildren().add(new Label(data.getMcabang().getMprov().getProvname()));
				row.getChildren().add(new Label(data.getMcabang().getCabang()));
				
				A a = new A(data.getDocid());
				a.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, String> mapDocument = new HashMap<>();
						mapDocument.put("src", data.getDocpath());
						zkSession.setAttribute("mapDocument", mapDocument);
						Executions.getCurrent().sendRedirect("/view/docviewer.zul", "_blank");
					}
				});
				
				row.getChildren().add(a);
				row.getChildren().add(new Label(data.getMemo()));
				row.getChildren().add(new Label(AppUtils.getStatusLabel(data.getStatus())));
				row.getChildren().add(new Label(data.getChecktime() != null ? new SimpleDateFormat("dd-MM-yyyy").format(data.getChecktime()) : "-"));
			}
		});
	}
	
	@Command
	@NotifyChange("mcabangmodel")
	public void doBranchLoad(@BindingParam("item") Mprov item) {
		if (item != null) {
			try {
				mcabangmodel = new ListModelList<Mcabang>(
						new McabangDAO().listByFilter("mprovfk = " + mprov.getMprovpk(), "cabang"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Command
	@NotifyChange("*")
	public void doSave() {
		Session session = StoreHibernateUtil.openSession();
		Transaction trx = session.beginTransaction();
		try {
			obj.setMcabang(mcabang);
			obj.setTanggota(anggota);
			obj.setStatus(AppUtils.STATUS_WAITCONFIRM);
			obj.setProvcurr(anggota.getMcabang().getMprov().getProvname());
			obj.setCabangcurr(anggota.getMcabang().getCabang());
			obj.setCreatedby(anggota.getNama());
			obj.setCreatetime(new Date());

			if (media != null) {
				try {
					String docid = counterDao.getP2kbCounter("MTS");
					String folder = Executions.getCurrent().getDesktop().getWebApp().getRealPath(AppUtils.PATH_DOC);
					if (media.isBinary()) {
						Files.copy(new File(folder + "/" + docid + "/" + media.getFormat()), media.getStreamData());
					} else {
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(folder + "/" + docid + "/" + media.getFormat()));
						Files.copy(writer, media.getReaderData());
						writer.close();
					}
					obj.setDocid(docid);
					obj.setDocpath(AppUtils.PATH_DOC + "/" + docid + "/" + media.getFormat());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			oDao.save(session, obj);
			trx.commit();

			doReset();
			Clients.showNotification("Proses mutasi data berhasil", "info", null, "middle_center", 1500);
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
		} finally {
			session.close();
		}
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

	@NotifyChange("pageTotalSize")
	public void doRefresh() {
		try {
			objList = oDao.listByFilter("tanggotafk = " + anggota.getTanggotapk(), "tmutasipk");
			pageTotalSize = objList.size();
			grid.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@NotifyChange("*")
	public void doReset() {
		mprov = null;
		mcabang = null;
		docfilename = "";
		obj = new Tmutasi();
		doRefresh();
	}

	public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				if (mcabang == null)
					this.addInvalidMessage(ctx, "mcabang", Labels.getLabel("common.validator.empty"));

				String memo = (String) ctx.getProperties("memo")[0].getValue();
				if (memo == null || "".equals(memo.trim()))
					this.addInvalidMessage(ctx, "memo", Labels.getLabel("common.validator.empty"));

				if (media == null)
					this.addInvalidMessage(ctx, "media", "Silahkan upload surat pengantar");
			}
		};
	}

	public ListModelList<Mprov> getMprovmodel() {
		ListModelList<Mprov> lm = null;
		try {
			lm = new ListModelList<Mprov>(new MprovDAO().listAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lm;
	}

	public Tmutasi getObj() {
		return obj;
	}

	public void setObj(Tmutasi obj) {
		this.obj = obj;
	}

	public Tanggota getAnggota() {
		return anggota;
	}

	public void setAnggota(Tanggota anggota) {
		this.anggota = anggota;
	}

	public Mprov getMprov() {
		return mprov;
	}

	public void setMprov(Mprov mprov) {
		this.mprov = mprov;
	}

	public Mcabang getMcabang() {
		return mcabang;
	}

	public void setMcabang(Mcabang mcabang) {
		this.mcabang = mcabang;
	}

	public String getDocfilename() {
		return docfilename;
	}

	public void setDocfilename(String docfilename) {
		this.docfilename = docfilename;
	}

	public Integer getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(Integer pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

	public List<Mcabang> getMcabangmodel() {
		return mcabangmodel;
	}

	public void setMcabangmodel(List<Mcabang> mcabangmodel) {
		this.mcabangmodel = mcabangmodel;
	}

}
