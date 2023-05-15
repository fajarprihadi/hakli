package com.sds.hakli.viewmodel;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.Tp2kbB04DAO;
import com.sds.hakli.dao.Tp2kbB05DAO;
import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tp2kb;
import com.sds.hakli.domain.Tp2kbb04;
import com.sds.hakli.domain.Tp2kbb05;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class P2kbB05DetailVm {
	
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota anggota;
	private Tp2kbB05DAO oDao = new Tp2kbB05DAO();
	private Tp2kbDAO p2kbDao = new Tp2kbDAO();
	
	private Tp2kb p2kb;
	private BigDecimal totalskp;
	
	private boolean isApproved = false;

	@Wire
	private Column colAksi;
	@Wire
	private Window winP2kbb05Detail;
	@Wire
	private Grid grid;

	private String approvetype;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tp2kb p2kb,
			@ExecutionArgParam("isApprove") String isApprove) {
		Selectors.wireComponents(view, this, false);

		if (isApprove != null && isApprove.length() > 0) {
			isApproved = true;
			colAksi.setVisible(false);
			approvetype = isApprove;
		}
		
		anggota = (Tanggota) zkSession.getAttribute("anggota");
		this.p2kb = p2kb;
		
		grid.setRowRenderer(new RowRenderer<Tp2kbb05>() {

			@Override
			public void render(Row row, Tp2kbb05 data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index+1)));
				
				Vlayout vlayoutKet = new Vlayout();

				Div divKet0 = new Div();
				Hlayout hlayout = new Hlayout();
				divKet0.setSclass("rows note note-light");
				Label lblStatus = new Label("Status Pemeriksaan :");
				lblStatus.setStyle("font-weight: bold");
				hlayout.appendChild(lblStatus);

				Separator separator = new Separator();
				hlayout.appendChild(separator);

				Combobox combobox = new Combobox();
				combobox.setReadonly(true);
				combobox.setCols(15);

				Comboitem cbItem = new Comboitem();
				cbItem.setLabel("Approve");
				cbItem.setValue("A");
				combobox.appendChild(cbItem);

				cbItem = new Comboitem();
				cbItem.setLabel("Reject");
				cbItem.setValue("R");
				combobox.appendChild(cbItem);

				Label lblStatusVal = new Label(AppUtils.getStatusLabel(data.getStatus()));

				if (isApproved)
					hlayout.appendChild(combobox);
				else
					hlayout.appendChild(lblStatusVal);

				divKet0.appendChild(hlayout);
				vlayoutKet.appendChild(divKet0);

				Div divKet1 = new Div();
				divKet1.setSclass("note note-light");
				Label lblCheckdate = new Label("Tanggal Pemeriksaan :");
				lblCheckdate.setStyle("font-weight: bold");
				divKet1.appendChild(lblCheckdate);
				Label lblCheckdateVal = new Label(
						data.getChecktime() != null ? new SimpleDateFormat("dd MMM yyyy").format(data.getChecktime())
								: "");
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
				hlayout = new Hlayout();

				divKet3.setSclass("note note-light");
				Label lblMemoTim = new Label("Catatan Tim P2KB :");
				lblMemoTim.setStyle("font-weight: bold");
				hlayout.appendChild(lblMemoTim);

				separator = new Separator();
				hlayout.appendChild(separator);

				Label lblMemoTimVal = new Label(data.getMemo());

				Textbox tb1 = new Textbox();
				tb1.setRows(2);
				tb1.setCols(30);

				if (approvetype.equals("K"))
					hlayout.appendChild(lblMemoTimVal);
				else
					hlayout.appendChild(tb1);

				divKet3.appendChild(hlayout);
				vlayoutKet.appendChild(divKet3);

				Div divKet4 = new Div();
				hlayout = new Hlayout();
				
				divKet4.setSclass("note note-light");
				Label lblMemoKomisi = new Label("Catatan Komisi P2KB :");
				lblMemoKomisi.setStyle("font-weight: bold");
				hlayout.appendChild(lblMemoKomisi);
				
				separator = new Separator();
				hlayout.appendChild(separator);
				
				Label lblMemoKomisiVal = new Label(data.getMemokomisi());
				
				if (approvetype.equals("T"))
					hlayout.appendChild(lblMemoKomisiVal);
				else
					hlayout.appendChild(tb1);
				
				divKet4.appendChild(hlayout);
				vlayoutKet.appendChild(divKet4);

				Button btApproved = new Button("Submit");
				btApproved.setIconSclass("z-icon-check");
				btApproved.setSclass("btn btn-primary btn-sm");
				btApproved.setAutodisable("self");
				btApproved.setTooltiptext("Submit");
				btApproved.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						if ((combobox.getSelectedItem().getLabel() != null)
								&& (tb1.getValue() != null && tb1.getValue().length() > 0)) {
							Messagebox.show("Apakah anda yakin submit data ini?", "Confirm Dialog",
									Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
										@Override
										public void onEvent(Event event) throws Exception {
											if (event.getName().equals("onOK")) {
												doSubmit(data, combobox.getSelectedItem().getValue(), tb1.getValue());
												BindUtils.postNotifyChange(P2kbB05DetailVm.this, "totalskp");
											}
										}
									});
						} else {
							Messagebox.show("Silahkan status dan catatan terlebih dahulu.");
						}
					}
				});

				
				separator = new Separator();
				vlayoutKet.appendChild(separator);

				Div divBtn = new Div();
				hlayout = new Hlayout();

				hlayout.appendChild(btApproved);
				separator = new Separator();
				hlayout.appendChild(separator);

				if (isApproved) {
					divBtn.appendChild(hlayout);
					vlayoutKet.appendChild(divBtn);
				}

				row.getChildren().add(vlayoutKet);
				
				Vlayout vlayoutKegiatan = new Vlayout();
				
				Div divKegiatan1 = new Div();
				divKegiatan1.setSclass("note note-light");
				Label lbl1 = new Label("Nama Acara");
				lbl1.setStyle("font-weight: bold");
				divKegiatan1.appendChild(lbl1);
				Label lbl2 = new Label(": " + data.getJudul());
				divKegiatan1.appendChild(lbl2);
				vlayoutKegiatan.appendChild(divKegiatan1);
				
				Div divKegiatan2 = new Div();
				divKegiatan2.setSclass("note note-light");
				Label lbl3 = new Label("Status Dalam Kegiatan");
				lbl3.setStyle("font-weight: bold");
				divKegiatan2.appendChild(lbl3);
				Label lbl4 = new Label(": " + data.getStatuspeserta());
				divKegiatan2.appendChild(lbl4);
				vlayoutKegiatan.appendChild(divKegiatan2);
				
				Div divKegiatan3 = new Div();
				divKegiatan3.setSclass("note note-light");
				Label lbl5 = new Label("Jenis Kegiatan");
				lbl5.setStyle("font-weight: bold");
				divKegiatan3.appendChild(lbl5);
				Label lbl6 = new Label(": " + data.getJeniskegiatan());
				divKegiatan3.appendChild(lbl6);
				vlayoutKegiatan.appendChild(divKegiatan3);
				
				Div divKegiatan31 = new Div();
				divKegiatan31.setSclass("note note-light");
				Label lbl51 = new Label("Jenis Kegiatan");
				lbl51.setStyle("font-weight: bold");
				divKegiatan31.appendChild(lbl51);
				Label lbl61 = new Label(": " + data.getJeniskegiatan());
				divKegiatan31.appendChild(lbl61);
				vlayoutKegiatan.appendChild(divKegiatan31);
				
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
				Label lbl9 = new Label("Tanggal Mulai Kegiatan");
				lbl9.setStyle("font-weight: bold");
				divKegiatan5.appendChild(lbl9);
				Label lbl10 = new Label(": " + new SimpleDateFormat("dd MMM yyyy").format(data.getTglmulai()));
				divKegiatan5.appendChild(lbl10);
				vlayoutKegiatan.appendChild(divKegiatan5);
				
				Div divKegiatan51 = new Div();
				divKegiatan51.setSclass("note note-light");
				Label lbl91 = new Label("Tanggal Selesai Kegiatan");
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
				BindUtils.postNotifyChange(P2kbB05DetailVm.this, "totalskp");
			}
		});
		
		doRefresh();
	}
	
	@NotifyChange("*")
	public void doSubmit(Tp2kbb05 obj, String action, String memotim) {
		try {
			Session session = StoreHibernateUtil.openSession();
			Transaction trx = session.beginTransaction();

			if(approvetype.equals("T")) {
				p2kb.setTotalwaiting(p2kb.getTotalwaiting() - 1);
				
				if(action.equals("A")) {
					p2kb.setTotaltimapprove(p2kb.getTotaltimapprove() + 1);
					obj.setStatus(AppUtils.STATUS_APPROVEDTIM);
				}else {
					obj.setStatus(AppUtils.STATUS_REJECTEDTIM);
				}
				
				obj.setMemo(memotim);
			} else {
				p2kb.setTotaltimapprove(p2kb.getTotaltimapprove() - 1);
				
				if(action.equals("A"))
					obj.setStatus(AppUtils.STATUS_APPROVEDKOMISI);
				else
					obj.setStatus(AppUtils.STATUS_REJECTEDKOMISI);
				
				obj.setMemokomisi(memotim);
			}
			new Tp2kbDAO().save(session, p2kb);
			
			obj.setCheckedby(anggota.getNama());
			obj.setChecktime(new Date());
			new Tp2kbB05DAO().save(session, obj);
			
			totalskp = totalskp.subtract(obj.getNilaiskp());

			trx.commit();
			session.close();

			doRefresh();
			Clients.showNotification(AppData.getLabel(action) + " data berhasil", "info", null, "middle_center", 1500);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@NotifyChange("totalskp")
	public void doRefresh() {
		try {
			totalskp = new BigDecimal(0);
			String filter = "mp2kbkegiatan.mp2kbkegiatanpk = " + p2kb.getMp2kbkegiatan().getMp2kbkegiatanpk()
					+ " and tanggota.tanggotapk = " + p2kb.getTanggota().getTanggotapk();

			if (isApproved)
				filter += " and status = 'WC'";
			List<Tp2kbb05> objList = oDao.listByFilter(filter, "tp2kbb05pk desc");
			grid.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command()
	@NotifyChange("*")
	public void doEdit(Tp2kbb05 obj) {
		Map<String, Object> map = new HashMap<>();
		map.put("action", "edit");
		map.put("page", "p2kbb05.zul");
		map.put("p2kb", obj);
		map.put("p2kbkegiatan", obj.getMp2kbkegiatan());
		Event closeEvent = new Event("onClose", winP2kbb05Detail, map);
		Events.postEvent(closeEvent);
	}
	
	@Command()
	@NotifyChange("*")
	public void doDelete(Tp2kbb05 obj) {
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
						BindUtils.postNotifyChange(P2kbB05DetailVm.this, "*");
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
	
	
}
