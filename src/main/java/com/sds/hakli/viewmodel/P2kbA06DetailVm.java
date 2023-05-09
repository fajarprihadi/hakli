package com.sds.hakli.viewmodel;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.Tp2kbA06DAO;
import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tp2kb;
import com.sds.hakli.domain.Tp2kba06;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class P2kbA06DetailVm {
	
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota anggota;
	private Tp2kbA06DAO oDao = new Tp2kbA06DAO();
	private Tp2kbDAO p2kbDao = new Tp2kbDAO();
	
	private Tp2kb p2kb;
	private BigDecimal totalskp;
	
	private Map<Integer, Tp2kba06> mapData = new HashMap<>();
	private Integer totalselected = 0;

	private String action;
	private boolean isApprove = false;

	@Wire
	private Column colCheck, colAksi;
	@Wire
	private Div divApprove;
	@Wire
	private Window winP2kba06Detail;
	@Wire
	private Grid grid;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tp2kb p2kb,
			@ExecutionArgParam("isApprove") String isApprove) {
		Selectors.wireComponents(view, this, false);
		if (isApprove != null && isApprove.equals("Y")) {
			colCheck.setVisible(true);
			colAksi.setVisible(false);
			divApprove.setVisible(true);
			this.isApprove = true;
		}
		
		anggota = (Tanggota) zkSession.getAttribute("anggota");
		this.p2kb = p2kb;
		
		grid.setRowRenderer(new RowRenderer<Tp2kba06>() {

			@Override
			public void render(Row row, Tp2kba06 data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index+1)));
				Checkbox check = new Checkbox();
				check.setAttribute("obj", data);
				check.addEventListener(Events.ON_CHECK, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						Checkbox checked = (Checkbox) event.getTarget();
						if (checked.isChecked()) {
							mapData.put(data.getTp2kba06pk(), data);
						} else {
							mapData.remove(data.getTp2kba06pk());
						}
						totalselected = mapData.size();
					}
				});
				if (mapData.get(data.getTp2kba06pk()) != null)
					check.setChecked(true);

				row.getChildren().add(check);
				
				Vlayout vlayoutKet = new Vlayout();
				
				Div divKet0 = new Div();
				divKet0.setSclass("note note-light");
				Label lblStatus = new Label("Status Pemeriksaan :");
				lblStatus.setStyle("font-weight: bold");
				divKet0.appendChild(lblStatus);
				Label lblStatusVal = new Label(AppUtils.getStatusLabel(data.getStatus()));
				divKet0.appendChild(lblStatusVal);
				vlayoutKet.appendChild(divKet0);
				
				Div divKet1 = new Div();
				divKet1.setSclass("note note-light");
				Label lblCheckdate = new Label("Tanggal Pemeriksaan :");
				lblCheckdate.setStyle("font-weight: bold");
				divKet1.appendChild(lblCheckdate);
				Label lblCheckdateVal = new Label(data.getChecktime() != null ? new SimpleDateFormat("dd MMM yyyy").format(data.getChecktime()) : "");
				divKet1.appendChild(lblCheckdateVal);
				vlayoutKet.appendChild(divKet1);
				
				Div divKet2 = new Div();
				divKet2.setSclass("note note-light");
				Label lblCheckedby = new Label("Pemeriksa :");
				lblCheckedby.setStyle("font-weight: bold");
				divKet2.appendChild(lblCheckedby);
				Label lblCheckedbyVal = new Label(data.getCheckedby());
				divKet2.appendChild(lblCheckedbyVal);
				vlayoutKet.appendChild(divKet2);
				
				Div divKet3 = new Div();
				divKet3.setSclass("note note-light");
				Label lblMemoTim = new Label("Catatan Tim P2KB :");
				lblMemoTim.setStyle("font-weight: bold");
				divKet3.appendChild(lblMemoTim);
				Label lblMemoTimVal = new Label(data.getMemo());
				divKet3.appendChild(lblMemoTimVal);
				vlayoutKet.appendChild(divKet3);
				
				Div divKet4 = new Div();
				divKet4.setSclass("note note-light");
				Label lblMemoKomisi = new Label("Catatan Komisi P2KB :");
				lblMemoKomisi.setStyle("font-weight: bold");
				divKet4.appendChild(lblMemoKomisi);
				Label lblMemoKomisiVal = new Label();
				divKet4.appendChild(lblMemoKomisiVal);
				vlayoutKet.appendChild(divKet4);
				
				row.getChildren().add(vlayoutKet);
				
				Vlayout vlayoutKegiatan = new Vlayout();
				
				Div divKegiatan1 = new Div();
				divKegiatan1.setSclass("note note-light");
				Label lbl1 = new Label("Nama Institusi Pendidikan");
				lbl1.setStyle("font-weight: bold");
				divKegiatan1.appendChild(lbl1);
				Label lbl2 = new Label(": " + data.getMuniversitas().getUniversitas());
				divKegiatan1.appendChild(lbl2);
				vlayoutKegiatan.appendChild(divKegiatan1);
				
				Div divKegiatan2 = new Div();
				divKegiatan2.setSclass("note note-light");
				Label lbl3 = new Label("Peminatan");
				lbl3.setStyle("font-weight: bold");
				divKegiatan2.appendChild(lbl3);
				Label lbl4 = new Label(": " + data.getProdi());
				divKegiatan2.appendChild(lbl4);
				vlayoutKegiatan.appendChild(divKegiatan2);
				
				Div divKegiatan3 = new Div();
				divKegiatan3.setSclass("note note-light");
				Label lbl5 = new Label("Jenjang");
				lbl5.setStyle("font-weight: bold");
				divKegiatan3.appendChild(lbl5);
				Label lbl6 = new Label(": " + data.getJenjang());
				divKegiatan3.appendChild(lbl6);
				vlayoutKegiatan.appendChild(divKegiatan3);
				
				Div divKegiatan4 = new Div();
				divKegiatan4.setSclass("note note-light");
				Label lbl7 = new Label("Dokumen Bukti Kegiatan");
				lbl7.setStyle("font-weight: bold");
				divKegiatan4.appendChild(lbl7);
				
				File file = new File(Executions.getCurrent().getDesktop().getWebApp()
							.getRealPath(data.getDocpath()));
				if (file.exists()) {
					divKegiatan4.appendChild(new Separator());
					
					Vlayout vlaydoc = new Vlayout();
					Iframe iframe = new Iframe(data.getDocpath());
					iframe.setWidth("100%");
					iframe.setStyle("border: 1px solid gray");
					vlaydoc.appendChild(iframe);
					
					Div divExpand = new Div();
					divExpand.setAlign("right");
					Button btView = new Button("Full Screen");
					btView.setIconSclass("z-icon-eye");
					btView.setSclass("btn btn-success btn-sm");
					btView.setAutodisable("self");
					btView.setTooltiptext("Full Screen");
					btView.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							Map<String, String> mapDocument = new HashMap<>();
							mapDocument.put("src", data.getDocpath());
							zkSession.setAttribute("mapDocument", mapDocument);
							Executions.getCurrent().sendRedirect("/view/docviewer.zul", "_blank");
						}
					});
					divExpand.appendChild(btView);
					vlaydoc.appendChild(divExpand);
					
					divKegiatan4.appendChild(vlaydoc);
				} else {
					Label lblempty = new Label(": Tidak ada dokumen kegiatan");
					divKegiatan4.appendChild(lblempty);
				}
				
				vlayoutKegiatan.appendChild(divKegiatan4);
				
				Div divKegiatan5 = new Div();
				divKegiatan5.setSclass("note note-light");
				Label lbl9 = new Label("Tanggal Mulai");
				lbl9.setStyle("font-weight: bold");
				divKegiatan5.appendChild(lbl9);
				Label lbl10 = new Label(": " + new SimpleDateFormat("dd MMM yyyy").format(data.getTglmulai()));
				divKegiatan5.appendChild(lbl10);
				vlayoutKegiatan.appendChild(divKegiatan5);
				
				Div divKegiatan51 = new Div();
				divKegiatan51.setSclass("note note-light");
				Label lbl91 = new Label("Tanggal Selesai");
				lbl91.setStyle("font-weight: bold");
				divKegiatan51.appendChild(lbl91);
				Label lbl101 = new Label(": " + new SimpleDateFormat("dd MMM yyyy").format(data.getTglakhir()));
				divKegiatan51.appendChild(lbl101);
				vlayoutKegiatan.appendChild(divKegiatan51);
				
				Div divKegiatan6 = new Div();
				divKegiatan6.setSclass("note note-light");
				Label lbl11 = new Label("Nilai SKP");
				lbl11.setStyle("font-weight: bold");
				divKegiatan6.appendChild(lbl11);
				Label lbl12 = new Label(": " + String.valueOf(data.getNilaiskp()) + " SKP");
				divKegiatan6.appendChild(lbl12);
				vlayoutKegiatan.appendChild(divKegiatan6);
				
				row.getChildren().add(vlayoutKegiatan);
				
				Div divAction = new Div();
				Button btEdit = new Button();
				btEdit.setIconSclass("z-icon-edit");
				btEdit.setSclass("btn btn-primary btn-sm");
				btEdit.setAutodisable("self");
				btEdit.setTooltiptext("Edit");
				btEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						doEdit(data);
					}
				});
				
				Button btDel = new Button();
				btDel.setIconSclass("z-icon-trash");
				btDel.setSclass("btn btn-danger btn-sm");
				btDel.setAutodisable("self");
				btDel.setTooltiptext("Hapus");
				btDel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						doDelete(data);
					}
				});
				
				divAction.appendChild(btEdit);
				divAction.appendChild(new Separator("vertical"));
				divAction.appendChild(btDel);
				row.getChildren().add(divAction);
				
				totalskp = totalskp.add(data.getNilaiskp());
				BindUtils.postNotifyChange(P2kbA06DetailVm.this, "totalskp");
			}
		});
		
		doRefresh();
	}
	
	@Command()
	@NotifyChange("*")
	public void doSubmit() {
		if (mapData.size() > 0) {
			if (action != null && action.trim().length() >0) {
			Messagebox.show("Apakah anda yakin submit data ini?", "Confirm Dialog", Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							if (event.getName().equals("onOK")) {
								try {
									Session session = StoreHibernateUtil.openSession();
									Transaction trx = session.beginTransaction();

									p2kb.setTotalwaiting(p2kb.getTotalwaiting() - totalselected);
									new Tp2kbDAO().save(session, p2kb);

									for (Entry<Integer, Tp2kba06> entry : mapData.entrySet()) {
										Tp2kba06 obj = entry.getValue();
										obj.setStatus(action);
										new Tp2kbA06DAO().save(session, obj);
									}

									trx.commit();
									session.close();

									Clients.showNotification(AppData.getLabel(action) + " data berhasil", "info", null, "middle_center", 1500);
									Event closeEvent = new Event("onClose", winP2kba06Detail, null);
									Events.postEvent(closeEvent);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					});
			} else {
				Messagebox.show("Silahkan status terlebih dahulu.");
			}
		} else {
			Messagebox.show("Silahkan pilih data terlebih dahulu.");
		}

	}
	
	@NotifyChange("totalskp")
	public void doRefresh() {
		try {
			totalskp = new BigDecimal(0);
			String filter = "mp2kbkegiatan.mp2kbkegiatanpk = " + p2kb.getMp2kbkegiatan().getMp2kbkegiatanpk()
					+ " and tanggota.tanggotapk = " + p2kb.getTanggota().getTanggotapk();

			if (isApprove)
				filter += " and status = 'WC'";
			List<Tp2kba06> objList = oDao.listByFilter(filter, "tp2kba06pk desc");
			grid.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command()
	@NotifyChange("*")
	public void doEdit(Tp2kba06 obj) {
		Map<String, Object> map = new HashMap<>();
		map.put("action", "edit");
		map.put("page", "p2kba06.zul");
		map.put("p2kb", obj);
		map.put("p2kbkegiatan", obj.getMp2kbkegiatan());
		Event closeEvent = new Event("onClose", winP2kba06Detail, map);
		Events.postEvent(closeEvent);
	}
	
	@Command()
	@NotifyChange("*")
	public void doDelete(Tp2kba06 obj) {
		Messagebox.show("Anda ingin menghapus data ini?", "Confirm Dialog", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

			@Override
			public void onEvent(Event event)
					throws Exception {
				if (event.getName().equals("onOK")) {
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						oDao.delete(session, obj);
						
						Tp2kb book = p2kbDao.findByFilter("tanggota.tanggotapk = " + anggota.getTanggotapk() + " and mp2kbkegiatan.mp2kbkegiatanpk = " + obj.getMp2kbkegiatan().getMp2kbkegiatanpk());
						if (book != null) {
							if (book.getTotalkegiatan() > 1) {
								book.setTotalkegiatan(book.getTotalkegiatan()-1);
								book.setTotalskp(book.getTotalskp().subtract(obj.getNilaiskp()));
								book.setLastupdated(new Date());
								p2kbDao.save(session, book);
							} else {
								p2kbDao.delete(session, book);
							}
						}
						
						trx.commit();
						Clients.showNotification("Proses hapus data berhasil", "info", null, "middle_center", 1500);
						doRefresh();
						BindUtils.postNotifyChange(P2kbA06DetailVm.this, "*");
					} catch (Exception e) {
						Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
						e.printStackTrace();
					} finally {
						session.close();
					}																									
				} 									
			}
			
		});
	}

	public BigDecimal getTotalskp() {
		return totalskp;
	}

	public void setTotalskp(BigDecimal totalskp) {
		this.totalskp = totalskp;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	
}
