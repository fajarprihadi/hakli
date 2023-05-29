package com.sds.hakli.viewmodel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
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
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.Mp2kbkegiatanDAO;
import com.sds.hakli.dao.Mp2kbranahDAO;
import com.sds.hakli.dao.Tp2kbbookDAO;
import com.sds.hakli.domain.Mp2kbkegiatan;
import com.sds.hakli.domain.Mp2kbranah;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tp2kbbook;

public class BorangPengisianVm {

	private org.zkoss.zk.ui.Session session = Sessions.getCurrent();
	private Tanggota obj;

	private Mp2kbkegiatanDAO kegiatanDao = new Mp2kbkegiatanDAO();

	private String filter;
	private String kegiatan;
	private Mp2kbranah ranah;
	private Tp2kbbook tpb;
	private String startdate;
	private String enddate;

	@Wire
	private Window winBorang;
	@Wire
	private Grid grid;
	@Wire
	private Combobox cbRanah;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			obj = (Tanggota) session.getAttribute("anggota");

			grid.setRowRenderer(new RowRenderer<Mp2kbkegiatan>() {

				@Override
				public void render(Row row, Mp2kbkegiatan data, int index) throws Exception {
					row.getChildren().add(new Label(String.valueOf(index + 1)));
					row.getChildren().add(new Label(data.getKegiatan()));
					row.getChildren().add(new Label(data.getMp2kbranah().getRanah()));
					Button btEdit = new Button("Input");
					btEdit.setIconSclass("z-icon-edit");
					btEdit.setSclass("btn btn-primary btn-sm");
					btEdit.setAutodisable("self");
					btEdit.setTooltiptext("Edit");
					btEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							doLoadPage(data);
						}
					});

					row.getChildren().add(btEdit);
				}
			});

			doReset();

			tpb = new Tp2kbbookDAO().findByFilter("tanggotafk = " + obj.getTanggotapk() + " and '"
					+ new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "' BETWEEN TGLMULAI AND TGLAKHIR");
			if (tpb == null) {
				tpb = new Tp2kbbook();
				Map<String, Object> map = new HashMap<>();
				Window win = new Window();
				map.put("type", "permohonan");
				win = (Window) Executions.createComponents("/view/infoperiodekta.zul", null, map);
				win.setWidth("50%");
				win.setClosable(true);
				win.doModal();
				win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						winBorang.getChildren().clear();
						winBorang.setBorder(false);
						Executions.createComponents("/view/p2kb/bukulogrequest.zul", winBorang, null);
					}

				});
			} else {
				startdate = new SimpleDateFormat("dd MMMMM yyyy").format(tpb.getTglmulai());
				enddate = new SimpleDateFormat("dd MMMMM yyyy").format(tpb.getTglakhir());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refreshModel() {
		try {
			grid.setModel(new ListModelList<>(kegiatanDao.listByFilter(filter, "idkegiatan")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	public void doSearch() {
		filter = "0=0";
		if (kegiatan != null && kegiatan.trim().length() > 0) {
			filter += " and upper(kegiatan) like '%" + kegiatan.trim().toUpperCase() + "%'";
		}
		if (ranah != null) {
			filter += " and mp2kbranah.mp2kbranahpk = " + ranah.getMp2kbranahpk();
		}
		refreshModel();
	}

	@Command
	@NotifyChange("*")
	public void doReset() {
		kegiatan = null;
		ranah = null;
		cbRanah.setValue(null);
		doSearch();
	}

	public void doLoadPage(Mp2kbkegiatan obj) {
		try {
			String page = "";
			switch (obj.getIdkegiatan()) {
			case "A01":
				page = "p2kba01.zul";
				break;
			case "A02":
				page = "p2kba02.zul";
				break;
			case "A03":
				page = "p2kba03.zul";
				break;
			case "A04":
				page = "p2kba04.zul";
				break;
			case "A05":
				page = "p2kba05.zul";
				break;
			case "A06":
				page = "p2kba06.zul";
				break;
			case "B01":
				page = "p2kbb01.zul";
				break;
			case "B02":
				page = "p2kbb02.zul";
				break;
			case "B03":
				page = "p2kbb03.zul";
				break;
			case "B04":
				page = "p2kbb04.zul";
				break;
			case "B05":
				page = "p2kbb05.zul";
				break;
			case "B06":
				page = "p2kbb06.zul";
				break;
			case "B07":
				page = "p2kbb07.zul";
				break;
			case "B08":
				page = "p2kbb08.zul";
				break;
			case "B09":
				page = "p2kbb09.zul";
				break;
			case "B10":
				page = "p2kbb10.zul";
				break;
			case "B11":
				page = "p2kbb11.zul";
				break;
			case "B12":
				page = "p2kbb12.zul";
				break;
			case "B13":
				page = "p2kbb13.zul";
				break;
			case "B14":
				page = "p2kbb14.zul";
				break;
			case "B15":
				page = "p2kbb15.zul";
				break;
			case "B16":
				page = "p2kbb16.zul";
				break;
			case "B17":
				page = "p2kbb17.zul";
				break;
			case "B18":
				page = "p2kbb18.zul";
				break;
			case "B19":
				page = "p2kbb19.zul";
				break;
			case "B20":
				page = "p2kbb20.zul";
				break;
			case "C01":
				page = "p2kbc01.zul";
				break;
			case "C02":
				page = "p2kbc02.zul";
				break;
			case "D01":
				page = "p2kbd01.zul";
				break;
			case "D02":
				page = "p2kbd02.zul";
				break;
			case "E01":
				page = "p2kbe01.zul";
				break;
			case "E02":
				page = "p2kbe02.zul";
				break;
			case "E03":
				page = "p2kbe03.zul";
				break;
			case "E04":
				page = "p2kbe04.zul";
				break;
			case "E05":
				page = "p2kbe05.zul";
				break;
			case "E06":
				page = "p2kbe06.zul";
				break;
			case "E07":
				page = "p2kbe07.zul";
				break;
			case "E08":
				page = "p2kbe08.zul";
				break;
			case "E09":
				page = "p2kbe09.zul";
				break;
			case "E10":
				page = "p2kbe10.zul";
				break;
			case "E11":
				page = "p2kbe11.zul";
				break;
			}

			if (page.equals("")) {
				Messagebox.show("Page not available. Please contact the administrator",
						WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.INFORMATION);
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("obj", obj);
				map.put("book", tpb);
				Window win = (Window) Executions.createComponents("/view/p2kb/" + page, null, map);
				win.setClosable(true);
				win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Event closeEvent = new Event("onClose", winBorang, map);
						Events.postEvent(closeEvent);
					}
				});
				win.doModal();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ListModelList<Mp2kbranah> getRanahModel() {
		ListModelList<Mp2kbranah> oList = null;
		try {
			oList = new ListModelList<>(new Mp2kbranahDAO().listByFilter("0=0", "ranah"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	public String getKegiatan() {
		return kegiatan;
	}

	public void setKegiatan(String kegiatan) {
		this.kegiatan = kegiatan;
	}

	public Mp2kbranah getRanah() {
		return ranah;
	}

	public void setRanah(Mp2kbranah ranah) {
		this.ranah = ranah;
	}

	public Tp2kbbook getTpb() {
		return tpb;
	}

	public void setTpb(Tp2kbbook tpb) {
		this.tpb = tpb;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

}
