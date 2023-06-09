package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Transaction;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.Tp2kbbookDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tp2kbbook;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class BukuLogRequestVm {
	private Session session = Sessions.getCurrent();
	private Tanggota obj;

	List<Tp2kbbook> objList = new ArrayList<>();

	private String filter;
	private String orderby;

	private Integer pageTotalSize;

	private SimpleDateFormat dateLocalFormatter = new SimpleDateFormat("dd MMMM yyyy");

	@Wire
	private Grid grid;
	@Wire
	private Window winBookLogReq;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		obj = (Tanggota) session.getAttribute("anggota");

		doReset();
		grid.setRowRenderer(new RowRenderer<Tp2kbbook>() {

			@Override
			public void render(Row row, Tp2kbbook data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index + 1)));
				row.getChildren().add(new Label(data.getNostr()));
				row.getChildren().add(new Label(dateLocalFormatter.format(data.getTglmulai())));
				row.getChildren().add(new Label(dateLocalFormatter.format(data.getTglakhir())));
				row.getChildren().add(new Label(AppUtils.getStatusLogLabel(data.getStatus())));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getTotalskp())));

				Div div = new Div();
				Hlayout hlayout = new Hlayout();

				Button btEdit = new Button("Edit");
				btEdit.setIconSclass("z-icon-edit");
				btEdit.setSclass("btn btn-primary btn-sm");
				btEdit.setAutodisable("self");
				btEdit.setTooltiptext("Edit");
				btEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<>();
						Window win = new Window();
						map.put("obj", obj);
						map.put("objForm", data);
						win = (Window) Executions.createComponents("/view/p2kb/bukulogform.zul", null, map);
						win.setWidth("70%");
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
							@Override
							public void onEvent(Event event) throws Exception {
								doReset();
							}

						});
					}
				});

				Button btLog = new Button("Buku Log");
				btLog.setIconSclass("z-icon-book");
				btLog.setSclass("btn btn-success btn-sm");
				btLog.setAutodisable("self");
				btLog.setTooltiptext("Buku Log");
				btLog.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<>();
						Window win = new Window();
						map.put("obj", obj);
						map.put("book", data);

						SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

						if (obj.getPeriodekta() != null) {
							Date d1 = sdformat.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
							Date d2 = sdformat.parse(new SimpleDateFormat("yyyy-MM-dd").format(obj.getPeriodekta()));

							System.out.println(d1 + ", " + d2);
							if (d1.compareTo(d2) < 0) {
//							win = (Window) Executions.createComponents("/view/p2kb/bukulog.zul", null, map);
//							win.setWidth("90%");
//							win.setClosable(true);
//							win.doModal();
//							win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
//								@Override
//								public void onEvent(Event event) throws Exception {
//									doReset();
//								}
//
//							});

								Component comp = winBookLogReq.getParent();
								comp.getChildren().clear();
								// Map<String, Object> map = new HashMap<>();
								// map.put("obj", obj);
								Executions.createComponents("/view/p2kb/bukulog.zul", comp, map);
							} else {
								win = (Window) Executions.createComponents("/view/infoperiodekta.zul", null, map);
								win.setWidth("50%");
								win.setClosable(true);
								win.doModal();
								win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
									@Override
									public void onEvent(Event event) throws Exception {
										winBookLogReq.getChildren().clear();
										winBookLogReq.setBorder(false);
										Executions.createComponents("/view/payment/payment.zul", winBookLogReq, null);
									}

								});
							}
						} else {
							win = (Window) Executions.createComponents("/view/infoperiodekta.zul", null, map);
							win.setWidth("50%");
							win.setClosable(true);
							win.doModal();
							win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
								@Override
								public void onEvent(Event event) throws Exception {
									winBookLogReq.getChildren().clear();
									winBookLogReq.setBorder(false);
									Executions.createComponents("/view/payment/payment.zul", winBookLogReq, null);
								}

							});
						}

					}
				});

				Button btDelete = new Button("Delete");
				btDelete.setIconSclass("z-icon-trash");
				btDelete.setSclass("btn btn-danger btn-sm");
				btDelete.setAutodisable("self");
				btDelete.setTooltiptext("Delete");
				btDelete.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Messagebox.show("Apakah anda yakin ingin menghapus data ini?", "Confirm Dialog",
								Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
									@Override
									public void onEvent(Event event) throws Exception {
										if (event.getName().equals("onOK")) {
											try {
												org.hibernate.Session session = StoreHibernateUtil.openSession();
												Transaction transaction = session.beginTransaction();

												new Tp2kbbookDAO().delete(session, data);
												transaction.commit();
												session.close();

												Clients.showNotification("Data berhasil dihapus.", "info", null,
														"middle_center", 1500);

												objList = new Tp2kbbookDAO().listByFilter(filter, orderby);
												pageTotalSize = objList.size();
												grid.setModel(new ListModelList<>(objList));
												doReset();
												BindUtils.postNotifyChange(null, null, BukuLogRequestVm.this, "*");
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								});
					}
				});

				hlayout.appendChild(btLog);
				hlayout.appendChild(btEdit);
				hlayout.appendChild(btDelete);
				div.appendChild(hlayout);

				row.getChildren().add(div);
			}
		});
	}

	@Command
	@NotifyChange("*")
	public void doAdd() {
		try {
			Map<String, Object> map = new HashMap<>();
			Window win = new Window();
			map.put("obj", obj);
			win = (Window) Executions.createComponents("/view/p2kb/bukulogform.zul", null, map);
			win.setWidth("70%");
			win.setClosable(true);
			win.doModal();
			win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					doReset();
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Command
	@NotifyChange("*")
	public void doSearch() {
		try {
			filter = "tanggotafk = " + obj.getTanggotapk();
			orderby = "tglmulai";

			objList = new Tp2kbbookDAO().listByFilter(filter, orderby);
			pageTotalSize = objList.size();
			grid.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("*")
	public void doReset() {
		pageTotalSize = 0;

		doSearch();
	}

	public Integer getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(Integer pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}
}
