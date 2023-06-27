package com.sds.hakli.viewmodel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlNativeComponent;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.MprovDAO;
import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.domain.Mp2kbkegiatan;
import com.sds.hakli.domain.Mprov;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tp2kb;
import com.sds.hakli.domain.Tp2kbbook;

public class LogbookVm {

	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota anggota;

	private Tp2kbbook tpb;
	private Tp2kbDAO oDao = new Tp2kbDAO();

	private List<Tp2kb> objList = new ArrayList<>();

	private String filter;
	private Integer totalkegiatan;
	private BigDecimal totalskp;
	private BigDecimal totalwaitingskp;

	private String startdate;
	private String enddate;

	@Wire
	private Div divTitle;
	@Wire
	private Grid grid;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("book") Tp2kbbook tpb) {
		Selectors.wireComponents(view, this, false);

		anggota = (Tanggota) zkSession.getAttribute("anggota");
		
		if (tpb != null) {
			this.tpb = tpb;
			startdate = new SimpleDateFormat("dd MMMMM yyyy").format(tpb.getTglmulai());
			enddate = new SimpleDateFormat("dd MMMMM yyyy").format(tpb.getTglakhir());
			
			HtmlNativeComponent h4 = new HtmlNativeComponent("h4");
			h4.appendChild(new Html("Buku Log P2KB Periode " + startdate + " S/D " + enddate));
			divTitle.appendChild(h4);
		}

		grid.setRowRenderer(new RowRenderer<Tp2kb>() {

			@Override
			public void render(Row row, Tp2kb data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index + 1)));

				row.getChildren().add(new Label(data.getMp2kbkegiatan().getKegiatan()));
				row.getChildren().add(new Label(data.getMp2kbkegiatan().getMp2kbranah().getRanah()));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getTotalkegiatan())));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getTotalskp())));
				row.getChildren().add(new Label(DecimalFormat.getInstance().format(data.getTotalskpwaiting())));
				Button btProcess = new Button();
				btProcess.setIconSclass("z-icon-eye");
				btProcess.setSclass("btn btn-primary btn-sm");
				btProcess.setAutodisable("self");
				btProcess.setTooltiptext("Detail");
				btProcess.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						doLoadPage(data);
					}
				});
				row.getChildren().add(btProcess);

				totalkegiatan = totalkegiatan + data.getTotalkegiatan();
				totalskp = totalskp.add(data.getTotalskp());
				totalwaitingskp = totalwaitingskp.add(data.getTotalskpwaiting());
				BindUtils.postNotifyChange(LogbookVm.this, "*");
			}
		});

		doReset();
		
	}

	@Command
	@NotifyChange("*")
	public void doAdd() {
		Map<String, Object> map = new HashMap<>();
		Window win = new Window();
		map.put("book", tpb);
		win = (Window) Executions.createComponents("/view/p2kb/borang.zul", null, map);
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

	@NotifyChange("*")
	public void refreshModel() {
		try {
			totalkegiatan = 0;
			totalskp = new BigDecimal(0);
			totalwaitingskp = new BigDecimal(0);
			objList = oDao.listByFilter(filter, "mp2kbkegiatan.idkegiatan");
			grid.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doLoadPage(Tp2kb obj) {
		try {
			String page = "";
			switch (obj.getMp2kbkegiatan().getIdkegiatan()) {
			case "A01":
				page = "p2kba01detail.zul";
				break;
			case "A02":
				page = "p2kba02detail.zul";
				break;
			case "A03":
				page = "p2kba03detail.zul";
				break;
			case "A04":
				page = "p2kba04detail.zul";
				break;
			case "A05":
				page = "p2kba05detail.zul";
				break;
			case "A06":
				page = "p2kba06detail.zul";
				break;
			case "B01":
				page = "p2kbb01detail.zul";
				break;
			case "B02":
				page = "p2kbb02detail.zul";
				break;
			case "B03":
				page = "p2kbb03detail.zul";
				break;
			case "B04":
				page = "p2kbb04detail.zul";
				break;
			case "B05":
				page = "p2kbb05detail.zul";
				break;
			case "B06":
				page = "p2kbb06detail.zul";
				break;
			case "B07":
				page = "p2kbb07detail.zul";
				break;
			case "B08":
				page = "p2kbb08detail.zul";
				break;
			case "B09":
				page = "p2kbb09detail.zul";
				break;
			case "B10":
				page = "p2kbb10detail.zul";
				break;
			case "B11":
				page = "p2kbb11detail.zul";
				break;
			case "B12":
				page = "p2kbb12detail.zul";
				break;
			case "B13":
				page = "p2kbb13detail.zul";
				break;
			case "B14":
				page = "p2kbb14detail.zul";
				break;
			case "B15":
				page = "p2kbb15detail.zul";
				break;
			case "B16":
				page = "p2kbb16detail.zul";
				break;
			case "B17":
				page = "p2kbb17detail.zul";
				break;
			case "B18":
				page = "p2kbb18detail.zul";
				break;
			case "B19":
				page = "p2kbb19detail.zul";
				break;
			case "B20":
				page = "p2kbb20detail.zul";
				break;
			case "C01":
				page = "p2kbc01detail.zul";
				break;
			case "C02":
				page = "p2kbc02detail.zul";
				break;
			case "D01":
				page = "p2kbd01detail.zul";
				break;
			case "D02":
				page = "p2kbd02detail.zul";
				break;
			case "E01":
				page = "p2kbe01detail.zul";
				break;
			case "E02":
				page = "p2kbe02detail.zul";
				break;
			case "E03":
				page = "p2kbe03detail.zul";
				break;
			case "E04":
				page = "p2kbe04detail.zul";
				break;
			case "E05":
				page = "p2kbe05detail.zul";
				break;
			case "E06":
				page = "p2kbe06detail.zul";
				break;
			case "E07":
				page = "p2kbe07detail.zul";
				break;
			case "E08":
				page = "p2kbe08detail.zul";
				break;
			case "E09":
				page = "p2kbe09detail.zul";
				break;
			case "E10":
				page = "p2kbe10detail.zul";
				break;
			case "E11":
				page = "p2kbe11detail.zul";
				break;
			}

			if (page.equals("")) {
				Messagebox.show("Page not available. Please contact the administrator",
						WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.INFORMATION);
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("obj", obj);
				Window win = (Window) Executions.createComponents("/view/p2kb/" + page, null, map);
				win.setClosable(true);
				win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getData() != null) {
							Map<String, Object> mapEvent = (Map<String, Object>) event.getData();
							String action = (String) mapEvent.get("action");
							String page = (String) mapEvent.get("page");
							Mp2kbkegiatan p2kbkegiatan = (Mp2kbkegiatan) mapEvent.get("p2kbkegiatan");
							if (action != null && action.equals("edit")) {

								Map<String, Object> map = new HashMap<String, Object>();
								map.put("obj", p2kbkegiatan);
								map.put("objForm", mapEvent.get("p2kb"));
								map.put("book", tpb);
								Window win = (Window) Executions.createComponents("/view/p2kb/" + page, null, map);
								win.setClosable(true);
								win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

									@Override
									public void onEvent(Event event) throws Exception {

									}
								});
								win.doModal();

							}
						} else {
							doReset();
						}
					}
				});
				win.doModal();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("*")
	public void doSearch() {
		filter = "tanggota.tanggotapk = " + anggota.getTanggotapk() + " and tp2kbbookfk = " + tpb.getTp2kbbookpk();

		refreshModel();
	}

	@Command
	@NotifyChange("*")
	public void doReset() {
		doSearch();
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

	public BigDecimal getTotalskp() {
		return totalskp;
	}

	public void setTotalskp(BigDecimal totalskp) {
		this.totalskp = totalskp;
	}

	public Integer getTotalkegiatan() {
		return totalkegiatan;
	}

	public void setTotalkegiatan(Integer totalkegiatan) {
		this.totalkegiatan = totalkegiatan;
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

	public Tp2kbbook getTpb() {
		return tpb;
	}

	public void setTpb(Tp2kbbook tpb) {
		this.tpb = tpb;
	}

	public BigDecimal getTotalwaitingskp() {
		return totalwaitingskp;
	}

	public void setTotalwaitingskp(BigDecimal totalwaitingskp) {
		this.totalwaitingskp = totalwaitingskp;
	}

}
