package com.sds.hakli.viewmodel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.zkoss.zul.Label;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.dao.Tp2kbbookDAO;
import com.sds.hakli.domain.Tp2kbbook;
import com.sds.hakli.model.Tp2kbbookListModel;
import com.sds.utils.AppUtils;

public class LetterRecVm {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
//	private List<Tp2kbbook> objList = new ArrayList<>();
	
	private Tp2kbbookListModel model;

	private String filter;
	private String orderby;

	private String nama;

	private int pageStartNumber;
	private int pageTotalSize;
	private boolean needsPageUpdate;
	
	private SimpleDateFormat dateLocalFormatter = new SimpleDateFormat("dd-MM-yyyy");

	@Wire
	private Grid grid;
	@Wire
	private Paging paging;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
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
				row.getChildren().add(new Label(data.getTanggota().getMcabang().getCabang()));
				row.getChildren().add(new Label(data.getTanggota().getNoanggota()));
				row.getChildren().add(new Label(data.getTanggota().getNama()));
				row.getChildren().add(new Label(dateLocalFormatter.format(data.getTglmulai())));
				row.getChildren().add(new Label(dateLocalFormatter.format(data.getTglakhir())));
				row.getChildren().add(new Label(DecimalFormat.getInstance().format(data.getTotalskp())));
				
				Button btLetter = new Button("Download");
				btLetter.setIconSclass("z-icon-download");
				btLetter.setSclass("btn btn-primary btn-sm");
				btLetter.setAutodisable("self");
				btLetter.setTooltiptext("Download surat rekomendasi");
				btLetter.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> parameters = new HashMap<>();
						List<Tp2kbbook>dataList = new ArrayList<>();
						String currentdate = "";
						
						//currentdate = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id", "ID")).format(data.getReviewtime());
						currentdate = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id", "ID")).format(new Date());
						
						String nosurat = data.getLetterno();
						dataList.add(data);
						zkSession.setAttribute("objList", dataList);
						
						parameters.put("NOSURAT", nosurat);
						parameters.put("CURRENTDATE", currentdate);
						parameters.put("TTD_KETUAUMUM",
								Executions.getCurrent().getDesktop().getWebApp().getRealPath("img/ttd_kolegium.jpg"));
						parameters.put("LOGO",
								Executions.getCurrent().getDesktop().getWebApp().getRealPath("img/kolegium2.png"));
						
						zkSession.setAttribute("parameters", parameters);
//						zkSession.setAttribute("reportPath", Executions.getCurrent().getDesktop().getWebApp()
//								.getRealPath(AppUtils.PATH_JASPER + "/suratrekomendasi.jasper"));
						if (data.getTotalskp().compareTo(new BigDecimal(50)) >= 0)
							zkSession.setAttribute("reportPath", Executions.getCurrent().getDesktop().getWebApp()
								.getRealPath(AppUtils.PATH_JASPER + "/suratrekomendasi.jasper"));
						else 
							zkSession.setAttribute("reportPath", Executions.getCurrent().getDesktop().getWebApp()
									.getRealPath(AppUtils.PATH_JASPER + "/suratrekomendasi_belumcukup.jasper"));
						
						Executions.getCurrent().sendRedirect("/view/jasperviewer.zul", "_blank");
					}
				});
				
				row.getChildren().add(btLetter);
			}
		});
	}

	@Command
	@NotifyChange("*")
	public void doSearch() {
		try {
			filter = "status = 'C' and ispaid = 'Y'";
			orderby = "tp2kbbookpk";

			if (nama != null && nama.trim().length() > 0)
				filter += " and upper(nama) like '%" + nama.trim().toUpperCase() + "%'";

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
		nama = "";
		pageTotalSize = 0;

		doSearch();
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Integer getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(Integer pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}
}
