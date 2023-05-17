package com.sds.hakli.viewmodel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.domain.Vrecp2kb;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;

public class LetterRecVm {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private List<Vrecp2kb> objList = new ArrayList<>();

	private String filter;
	private String orderby;

	private String nama;

	private Integer pageTotalSize;

	@Wire
	private Grid grid;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
		doReset();
		grid.setRowRenderer(new RowRenderer<Vrecp2kb>() {

			@Override
			public void render(Row row, Vrecp2kb data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index + 1)));
				row.getChildren().add(new Label(data.getNoanggota()));
				row.getChildren().add(new Label(data.getNama()));
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
						List<Vrecp2kb>dataList = new ArrayList<>();
						
						Map<Integer, String> mapRomawi = new HashMap<>();
						
						int year = Calendar.getInstance().get(Calendar.YEAR);
						int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
						mapRomawi = AppData.getAngkaRomawi();
						
						String nosurat = new TcounterengineDAO().generateSeqnum() + " / REKOM/PP-HAKLI / " + mapRomawi.get(month) + " / " + year;
						dataList.add(data);
						zkSession.setAttribute("objList", dataList);
						
						parameters.put("NOSURAT", nosurat);
						parameters.put("TTD_KETUAUMUM",
								Executions.getCurrent().getDesktop().getWebApp().getRealPath("images/ttd_mengangkatsumpah.png"));
						parameters.put("LOGO",
								Executions.getCurrent().getDesktop().getWebApp().getRealPath("img/hakli.png"));
						
						zkSession.setAttribute("parameters", parameters);
						zkSession.setAttribute("reportPath", Executions.getCurrent().getDesktop().getWebApp()
								.getRealPath(AppUtils.PATH_JASPER + "/suratrekomendasi.jasper"));
						
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
			filter = "totalwaiting = 0 and totaltimapprove = 0";
			orderby = "nama";

			if (nama != null && nama.trim().length() > 0)
				filter += " and nama like '%" + nama.trim().toUpperCase() + "'";

			objList = new Tp2kbDAO().listRecLetter(filter, orderby);
			pageTotalSize = objList.size();
			grid.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
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