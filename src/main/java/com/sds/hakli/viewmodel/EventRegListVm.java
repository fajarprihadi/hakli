package com.sds.hakli.viewmodel;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TeventDAO;
import com.sds.hakli.dao.TeventregDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tevent;
import com.sds.hakli.domain.Teventreg;
import com.sds.hakli.domain.Veventreg;
import com.sds.hakli.handler.NaskahHandler;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;

public class EventRegListVm {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota obj;

	private List<Veventreg> objList = new ArrayList<>();

	private String filter;
	private String orderby;

	private String eventname;

	private Integer pageTotalSize;
	private SimpleDateFormat dateLocalFormatted = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat localDateFormatted = new SimpleDateFormat("dd-MM-yyyy HH:mm");

	@Wire
	private Window winEventRegList;
	@Wire
	private Grid grid;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			obj = (Tanggota) zkSession.getAttribute("anggota");
			obj = new TanggotaDAO().findByPk(obj.getTanggotapk());

			grid.setRowRenderer(new RowRenderer<Veventreg>() {

				@Override
				public void render(Row row, Veventreg data, int index) throws Exception {
					row.getChildren().add(new Label(String.valueOf(index + 1)));
					row.getChildren().add(new Label(data.getEventname()));
					row.getChildren().add(new Label(AppData.getEventType(data.getEventtype())));
					row.getChildren().add(new Label(dateLocalFormatted.format(data.getEventdate())));
					row.getChildren().add(new Label(localDateFormatted.format(data.getClosedate())));
					row.getChildren().add(new Label(data.getEventprice() != null ? DecimalFormat.getInstance().format(data.getEventprice()) : "Gratis"));
					if (data.getTeventregpk() != null) {
						row.getChildren().add(new Label("Terdaftar Tanggal " + dateLocalFormatted.format(data.getVacreatedat())));
					} else {
						if (data.getClosedate().compareTo(new Date()) < 0 ) {
							row.getChildren().add(new Label("Telah Berakhir"));
						} else {
							Button btReg = new Button("Daftar");
							btReg.setSclass("btn btn-success btn-sm");
							btReg.setAutodisable("self");
							btReg.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

								@Override
								public void onEvent(Event event) throws Exception {
									try {
										if (data.getPeriodekta() != null) {
											boolean isValid = true;
											if (obj.getPeriodeborang() == null)
												obj.setPeriodeborang(obj.getPeriodekta());
											if (data.getPeriodekta() != null && obj.getPeriodekta().compareTo(data.getPeriodekta()) >= 0) {
												isValid = true;
											} else if (data.getPeriodeborang() != null && obj.getPeriodeborang().compareTo(data.getPeriodeborang()) >= 0) {
												isValid = true;
											} else {
												isValid = false;
											}
											
											if (!isValid) {
												Window win = (Window) Executions.createComponents("/view/infoperiodekta.zul", null, null);
												win.setWidth("50%");
												win.setClosable(true);
												win.doModal();
												win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
													@Override
													public void onEvent(Event event) throws Exception {
														winEventRegList.getChildren().clear();
														winEventRegList.setBorder(false);
														Executions.createComponents("/view/payment/payment.zul", winEventRegList,
																null);
													}

												});
											} else {
												Tevent tevent = new TeventDAO().findByPk(data.getTeventpk());
												Map<String, Object> map = new HashMap<String, Object>();
												map.put("obj", tevent);
												map.put("anggota", obj);
												Window win = (Window) Executions
														.createComponents("/view/event/eventreginfo.zul", null, map);
												win.setClosable(true);
												win.setWidth("95%");
												win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

													@Override
													public void onEvent(Event event) throws Exception {
														doReset();
													}
												});
												win.doModal();
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							row.getChildren().add(btReg);
						}
					}
					
					if (data.getTeventregpk() != null) {
						Button btView = new Button();
						btView.setIconSclass("z-icon-eye");
						btView.setSclass("btn btn-primary btn-sm");
						btView.setAutodisable("self");
						btView.setTooltiptext("Detail");
						btView.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								try {
									Tevent tevent = new TeventDAO().findByPk(data.getTeventpk());
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("obj", tevent);
									map.put("anggota", obj);
									map.put("dateregister", new SimpleDateFormat("dd MMMMM yyyy").format(data.getVacreatedat()));
									Window win = (Window) Executions
											.createComponents("/view/event/eventreginfo.zul", null, map);
									win.setClosable(true);
									win.setWidth("98%");
									win.doModal();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						row.getChildren().add(btView);
					} else row.getChildren().add(new Label());
					
				}
			});
			
			doReset();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void downloadNaskah(Teventreg data, String arg) {
		try {
			Map<String, Object> parameters = new HashMap<>();
			List<Tanggota> objList = new ArrayList<>();
			objList.add(obj);
			zkSession.setAttribute("objList", objList);
			String currentdate = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id", "ID")).format(data.getPaidat());
			String localdate = new SimpleDateFormat("EEEE, dd MMMMM yyyy", new Locale("id", "ID")).format(new Date());
			String tgllahir = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id", "ID")).format(obj.getTgllahir());

			String nosurat = "";
			String filepath = "naskahsumpah.jasper";
			String filepath2 = "naskahsumpah2.jasper";
			if (arg.equals("sumpah")) {
				nosurat = data.getSpno();
				//System.out.println("'" + obj.getAgama() + "'");
				if (obj.getAgama().toUpperCase().equals("ISLAM")) {
					parameters.put("SALAMNASKAH", "Semoga Allah Subhanahu wa ta’ala memberikan kekuatan kepada saya");
					parameters.put("SUMPAHAGAMA", "Demi Allah saya bersumpah");
					parameters.put("AGAMA", "Islam");
				} else if (obj.getAgama().toUpperCase().equals("KHATOLIK")) {
					parameters.put("SALAMNASKAH", "Kiranya Tuhan menolong saya");
					parameters.put("SUMPAHAGAMA", "Demi Tuhan saya berjanji");
					parameters.put("AGAMA", "Khatolik");
				} else if (obj.getAgama().toUpperCase().equals("PROTESTAN")) {
					parameters.put("SALAMNASKAH", "Kiranya Tuhan menolong saya");
					parameters.put("SUMPAHAGAMA", "Demi Tuhan saya berjanji");
					parameters.put("AGAMA", "Protestan");
				} else if (obj.getAgama().toUpperCase().equals("HINDU")) {
					parameters.put("SALAMNASKAH", "Om Santi Santi Santi Om");
					parameters.put("SUMPAHAGAMA", "Om Attah Paramawisesa saya bersumpah");
					parameters.put("AGAMA", "Hindu");
				} else if (obj.getAgama().toUpperCase().equals("BUDHA")) {
					parameters.put("SALAMNASKAH", "Om Santi Santi Santi Om");
					parameters.put("SUMPAHAGAMA", "Demi yang Tiratana saya berjanji");
					parameters.put("AGAMA", "Budha");
				}
			} else if (arg.equals("etik")) {
				nosurat = data.getKeno();
				filepath = "naskahetika.jasper";
				filepath2 = "naskahetika2.jasper";
			} else if (arg.equals("sertifikasi")) {
				filepath = "naskahsertifikasi.jasper";
				filepath2 = "";
			}

			String photoname = "";

			File file = new File(AppUtils.PATH_PHOTO + "/" + obj.getPhotolink());
			if (file.exists()) {
				//System.out.println("ADA FOTO");
				photoname = obj.getPhotolink();
			} else {
				//System.out.println("TIDAK ADA FOTO");
				photoname = "default.png";
			}
			
			String location = data.getTevent().getEventlocation();

			String gelardepan = "";
			String gelarbelakang = "";

			if (obj.getGelarbelakang() != null)
				gelarbelakang = " " + obj.getGelarbelakang();

			if (obj.getGelardepan() != null)
				gelardepan = obj.getGelardepan() + ". ";

			parameters.put("GELARBELAKANG", gelarbelakang);
			parameters.put("GELARDEPAN", gelardepan);
			parameters.put("NOSURAT", nosurat);
			parameters.put("LOCATION", location);
			parameters.put("CURRENTDATE", currentdate);
			parameters.put("LOCALDATE", localdate);
			parameters.put("TGLLAHIR", tgllahir);
			parameters.put("FOTO", Executions.getCurrent().getDesktop().getWebApp()
					.getRealPath(AppUtils.PATH_PHOTO + "/" + photoname));
			parameters.put("TTD_KETUAUMUM",
					Executions.getCurrent().getDesktop().getWebApp().getRealPath("images/ttd_ketum.png"));
			parameters.put("TTD_SAKSI",
					Executions.getCurrent().getDesktop().getWebApp().getRealPath("images/ttd_saksi.jpg"));
			parameters.put("LOGO", Executions.getCurrent().getDesktop().getWebApp().getRealPath("img/hakli.png"));

			zkSession.setAttribute("parameters", parameters);
			zkSession.setAttribute("reportPath", Executions.getCurrent().getDesktop().getWebApp()
					.getRealPath(AppUtils.PATH_JASPER + "/" + filepath));

			if (filepath2.trim().length() > 0) {
				zkSession.setAttribute("reportPath2", Executions.getCurrent().getDesktop().getWebApp()
						.getRealPath(AppUtils.PATH_JASPER + "/" + filepath2));
			}

			Executions.getCurrent().sendRedirect("/view/jasperviewer.zul", "_blank");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	public void doSearch() {
		try {
			filter = "0=0";

			if (eventname != null && eventname.trim().length() > 0)
				filter += " and upper(tevent.eventname) like '%" + eventname.trim().toUpperCase() + "%'";

			objList = new TeventregDAO().listEventReg(obj.getTanggotapk(), filter);
			grid.setModel(new ListModelList<>(objList));

			pageTotalSize = objList.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@NotifyChange("*")
	@Command
	public void doReset() {
		eventname = null;
		doSearch();
	}

	public Integer getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(Integer pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

	public String getEventname() {
		return eventname;
	}

	public void setEventname(String eventname) {
		this.eventname = eventname;
	}
}
