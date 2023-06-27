package com.sds.hakli.viewmodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TmutasiDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tmutasi;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class MutasiApprovalVm {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota anggota;

	private TmutasiDAO oDao = new TmutasiDAO();
	private Integer pageTotalSize;
	private String action;
	private String decisionmemo;

	private List<Tmutasi> objList = new ArrayList<>();
	private Map<Integer, Tmutasi> mapData = new HashMap<>();

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
				Checkbox check = new Checkbox();
				check.setAttribute("obj", data);
				check.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Checkbox checked = (Checkbox) event.getTarget();
						Tmutasi obj = (Tmutasi) checked.getAttribute("obj");
						if (checked.isChecked()) {
							mapData.put(obj.getTmutasipk(), obj);
						} else {
							mapData.remove(obj.getTmutasipk());
						}
					}
				});
				if (mapData.get(data.getTmutasipk()) != null)
					check.setChecked(true);
				
				row.getChildren().add(check);
				row.getChildren().add(new Label(data.getTanggota().getNoanggota()));
				row.getChildren().add(new Label(data.getTanggota().getNama()));
				row.getChildren().add(new Label(data.getProvcurr()));
				row.getChildren().add(new Label(data.getCabangcurr()));
				row.getChildren().add(new Label(new SimpleDateFormat("dd-MM-yyyy").format(data.getCreatetime())));
				row.getChildren().add(new Label(data.getMcabang().getMprov().getProvname()));
				row.getChildren().add(new Label(data.getMcabang().getCabang()));
			}
		});
	}
	
	@Command
	public void doCheckedall(@BindingParam("checked") Boolean checked) {
		try {
			List<Row> components = grid.getRows().getChildren();
			for (Row comp : components) {
				Checkbox chk = (Checkbox) comp.getChildren().get(1);
				Tmutasi obj = (Tmutasi) chk.getAttribute("obj");
				if (checked) {
					if (!chk.isChecked()) {
						chk.setChecked(true);
						mapData.put(obj.getTmutasipk(), obj);
					}
				} else {
					if (chk.isChecked()) {
						chk.setChecked(false);
						mapData.remove(obj.getTmutasipk());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("*")
	public void doSave() {
		if (decisionmemo != null && decisionmemo.trim().length() > 0) {
			if (mapData.size() > 0) {
				Session session = StoreHibernateUtil.openSession();
				Transaction trx = session.beginTransaction();
				try {
					for (Entry<Integer, Tmutasi> entry : mapData.entrySet()) {
						Tmutasi obj = entry.getValue();
						obj.setStatus(action);
						obj.setDecisionmemo(decisionmemo);
						obj.setCheckedby(anggota.getNama());
						obj.setChecktime(new Date());
						oDao.save(session, obj);

						if (action.equals("A")) {
							Tanggota objAnggota = obj.getTanggota();
							objAnggota.setMcabang(obj.getMcabang());
							new TanggotaDAO().save(session, objAnggota);
						}
					}
					trx.commit();
					doReset();
					
					Clients.showNotification("Proses verifikasi data berhasil", "info", null, "middle_center", 1500);
				} catch (Exception e) {
					e.printStackTrace();
					Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
				} finally {
					session.close();
				}
			} else {
				Messagebox.show("Tidak ada data yang dipilih.");
			}
		} else {
			Messagebox.show("Silahkan isi catatan terlebih dahulu.");
		}
	}

	@NotifyChange("pageTotalSize")
	public void doRefresh() {
		try {
			String filter ="status = '" + AppUtils.STATUS_WAITCONFIRM + "' and mcabangfk = " + anggota.getMcabang().getMcabangpk();
			if (anggota.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_ADMIN))
				filter ="status = '" + AppUtils.STATUS_WAITCONFIRM + "'";
			objList = oDao.listByFilter(filter, "tmutasipk");
			pageTotalSize = objList.size();
			grid.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@NotifyChange("*")
	public void doReset() {
		action = "A";
		decisionmemo = "";
		doRefresh();
	}

	public Integer getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(Integer pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDecisionmemo() {
		return decisionmemo;
	}

	public void setDecisionmemo(String decisionmemo) {
		this.decisionmemo = decisionmemo;
	}
}
