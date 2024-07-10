package com.sds.hakli.viewmodel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.dao.McabangDAO;
import com.sds.hakli.dao.MprovDAO;
import com.sds.hakli.dao.Tp2kbbookDAO;
import com.sds.hakli.domain.Mcabang;
import com.sds.hakli.domain.Mprov;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tp2kbbook;
import com.sds.hakli.model.Tp2kbbookListModel;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class BukuLogRequestVm {
	private Session session = Sessions.getCurrent();
	private Tanggota obj;

	private Tp2kbbookListModel model;

	private int pageStartNumber;
	private int pageTotalSize;
	private boolean needsPageUpdate;
	private String orderby;
	private String filter;
	private String arg;
	private String status;

	private String nama;
	private Mcabang mcabang;
	private Mprov mprov;

	private List<Mcabang> mcabangmodel = new ArrayList<>();

	private SimpleDateFormat dateLocalFormatter = new SimpleDateFormat("dd MMMM yyyy");

	@Wire
	private Grid grid;
	@Wire
	private Window winBookLogReq;
	@Wire
	private Paging paging;
	@Wire
	private Column colAction;
	@Wire
	private Div divAdd, divProv, divCab;
	@Wire
	private Groupbox gbSearch;
	@Wire
	private Combobox cbProv, cbCabang;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("arg") String arg) {
		Selectors.wireComponents(view, this, false);
		obj = (Tanggota) session.getAttribute("anggota");
		this.arg = arg;
		
		try {
			SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
			if (obj.getPeriodekta() != null) {
				Date d1 = sdformat.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				Date d2 = sdformat
						.parse(new SimpleDateFormat("yyyy-MM-dd").format(obj.getPeriodekta()));
				
				Date d3 = obj.getPeriodeborang() != null ? sdformat
						.parse(new SimpleDateFormat("yyyy-MM-dd").format(obj.getPeriodeborang())) : null;

				if (d1.compareTo(d2) < 0 || (d3 != null && d1.compareTo(d3) < 0)) {
					
				} else {
					Window win = (Window) Executions.createComponents("/view/infoperiodekta.zul", null, null);
					win.setWidth("50%");
					win.setClosable(true);
					win.doModal();
					win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
						@Override
						public void onEvent(Event event) throws Exception {
							winBookLogReq.getChildren().clear();
							winBookLogReq.setBorder(false);
							Executions.createComponents("/view/payment/payment.zul", winBookLogReq,
									null);
						}

					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (arg != null && arg.equals("list")) {
			doLoadCabang();

			divAdd.setVisible(false);
			gbSearch.setVisible(true);

			if (obj.getMusergroup().getUsergroupcode().equals("PPR")) {
				divProv.setVisible(false);
			} else if (obj.getMusergroup().getUsergroupcode().equals("PKA")) {
				divProv.setVisible(false);
				divCab.setVisible(false);
			}

		}

		paging.addEventListener("onPaging", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				PagingEvent pe = (PagingEvent) event;
				pageStartNumber = pe.getActivePage();
				refreshModel(pageStartNumber);
			}
		});

		doReset();
		grid.setRowRenderer(new RowRenderer<Tp2kbbook>() {

			@Override
			public void render(Row row, Tp2kbbook data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index + 1)));
				row.getChildren().add(new Label(data.getNostr()));
				row.getChildren().add(new Label(data.getTanggota().getNama()));
				row.getChildren().add(new Label(data.getTanggota().getMcabang().getCabang()));
				row.getChildren().add(new Label(dateLocalFormatter.format(data.getTglmulai())));
				row.getChildren().add(new Label(dateLocalFormatter.format(data.getTglakhir())));
				row.getChildren().add(new Label(AppUtils.getStatusLogLabel(data.getStatus())));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getTotalskp())));

				String status = "";
				if (data.getStatus().equals("O"))
					status = "-";
				else if (data.getStatus().equals("R") && data.getIspaid() != null && data.getIspaid().equals("N"))
					status = "BELUM BAYAR";
				else if (data.getIspaid() != null && data.getIspaid().equals("Y"))
					status = "LUNAS";
				else
					status = "-";

				row.getChildren().add(new Label(status));

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
						map.put("arg", arg);
						if (arg != null && arg.equals("req")) {

							SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
							if (obj.getPeriodekta() != null) {
								Date d1 = sdformat.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
								Date d2 = sdformat
										.parse(new SimpleDateFormat("yyyy-MM-dd").format(obj.getPeriodekta()));
								
								Date d3 = obj.getPeriodeborang() != null ? sdformat
										.parse(new SimpleDateFormat("yyyy-MM-dd").format(obj.getPeriodeborang())) : null;

								if (d1.compareTo(d2) < 0 || (d3 != null && d1.compareTo(d3) < 0)) {
									Component comp = winBookLogReq.getParent();
									comp.getChildren().clear();
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
											Executions.createComponents("/view/payment/payment.zul", winBookLogReq,
													null);
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
						} else {
							map.put("isDetail", "Y");
							Component comp = winBookLogReq.getParent();
							comp.getChildren().clear();
							Executions.createComponents("/view/p2kb/bukulog.zul", comp, map);
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

				Button btLetter = new Button("Download");
				btLetter.setIconSclass("z-icon-download");
				btLetter.setSclass("btn btn-primary btn-sm");
				btLetter.setAutodisable("self");
				btLetter.setTooltiptext("Download surat rekomendasi");
				btLetter.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> parameters = new HashMap<>();
						List<Tp2kbbook> dataList = new ArrayList<>();
						String currentdate = "";

//						currentdate = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id", "ID"))
//								.format(data.getReviewtime());
						
						currentdate = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id", "ID"))
								.format(new Date());

						String nosurat = data.getLetterno();
						dataList.add(data);
						session.setAttribute("objList", dataList);

						parameters.put("NOSURAT", nosurat);
						parameters.put("CURRENTDATE", currentdate);
						parameters.put("TTD_KETUAUMUM",
								Executions.getCurrent().getDesktop().getWebApp().getRealPath("img/ttd_kolegium.jpg"));
						parameters.put("LOGO",
								Executions.getCurrent().getDesktop().getWebApp().getRealPath("img/kolegium.jpg"));

						session.setAttribute("parameters", parameters);
						if (data.getTotalskp().compareTo(new BigDecimal(50)) >= 0)
							session.setAttribute("reportPath", Executions.getCurrent().getDesktop().getWebApp()
								.getRealPath(AppUtils.PATH_JASPER + "/suratrekomendasi.jasper"));
						else 
							session.setAttribute("reportPath", Executions.getCurrent().getDesktop().getWebApp()
									.getRealPath(AppUtils.PATH_JASPER + "/suratrekomendasi_belumcukup.jasper"));
						Executions.getCurrent().sendRedirect("/view/jasperviewer.zul", "_blank");
					}
				});

				hlayout.appendChild(btLog);

				if (arg != null && arg.equals("req")) {
					hlayout.appendChild(btEdit);
					hlayout.appendChild(btDelete);
					if (data.getIspaid() != null && data.getIspaid().equals("Y") && data.getStatus().equals("C"))
						hlayout.appendChild(btLetter);
				}
				div.appendChild(hlayout);

				row.getChildren().add(div);
			}
		});
	}

	@Command
	@NotifyChange("mcabangmodel")
	public void doLoadCabang() {
		try {
			if (mprov != null) {
				mcabangmodel = new McabangDAO().listByFilter("mprovfk = " + mprov.getMprovpk(), "cabang");
			} else if (obj.getMusergroup().getUsergroupcode().equals("PPR")) {
				mcabangmodel = new McabangDAO().listByFilter("mprovfk = " + obj.getMcabang().getMprov().getMprovpk(),
						"cabang");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			if (arg != null && arg.equals("list")) {
				if (obj.getMusergroup().getUsergroupcode().equals("PPR"))
					filter = "mprovfk = " + obj.getMcabang().getMprov().getMprovpk();
				else if (obj.getMusergroup().getUsergroupcode().equals("PKA"))
					filter = "mcabangpk = " + obj.getMcabang().getMcabangpk();
				else
					filter = "0=0";
			} else {
				filter = "tanggotafk = " + obj.getTanggotapk();
			}

			if (nama != null && nama.trim().length() > 0)
				filter += " and UPPER(nama) like '%" + nama.trim().toUpperCase() + "%'";
			if (mprov != null)
				filter += " and mprovfk = " + mprov.getMprovpk();
			if (mcabang != null)
				filter += " and mcabangpk = " + mcabang.getMcabangpk();

			needsPageUpdate = true;
			paging.setActivePage(0);
			pageStartNumber = 0;
			refreshModel(pageStartNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@NotifyChange("pageTotalSize")
	public void refreshModel(int activePage) {
		orderby = "tglmulai";
		paging.setPageSize(AppUtils.PAGESIZE);
		model = new Tp2kbbookListModel(activePage, AppUtils.PAGESIZE, filter, orderby);
		if (needsPageUpdate) {
			pageTotalSize = model.getTotalSize(filter);
			needsPageUpdate = false;
		}
		paging.setTotalSize(pageTotalSize);
		grid.setModel(model);
	}

	@Command
	@NotifyChange("*")
	public void doReset() {
		cbCabang.setValue(null);
		cbProv.setValue(null);
		nama = null;
		mcabang = null;
		mprov = null;
		doSearch();
	}

	public ListModelList<Mprov> getMprovmodel() {
		ListModelList<Mprov> lm = null;
		try {
			lm = new ListModelList<Mprov>(new MprovDAO().listByFilter("0=0", "provname"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lm;
	}

	public Integer getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(Integer pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Mcabang getMcabang() {
		return mcabang;
	}

	public void setMcabang(Mcabang mcabang) {
		this.mcabang = mcabang;
	}

	public Mprov getMprov() {
		return mprov;
	}

	public void setMprov(Mprov mprov) {
		this.mprov = mprov;
	}

	public List<Mcabang> getMcabangmodel() {
		return mcabangmodel;
	}

	public void setMcabangmodel(List<Mcabang> mcabangmodel) {
		this.mcabangmodel = mcabangmodel;
	}
}
