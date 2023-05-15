package com.sds.hakli.viewmodel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
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
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tp2kb;
import com.sds.hakli.domain.Vp2kb;
import com.sds.utils.AppUtils;

public class VerifikasiKomisiDataVm {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota objAnggota;

	private List<Tp2kb> objList = new ArrayList<>();

	private String filter;
	private String orderby;

	private Vp2kb obj;
	private Integer pageTotalSize;
	private Integer totalkegiatan;
	private BigDecimal totalskp;

	@Wire
	private Grid grid;
	@Wire
	private Image image;
	@Wire
	private Window winVerifikasidata;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Vp2kb obj) {
		Selectors.wireComponents(view, this, false);
		objAnggota = (Tanggota) zkSession.getAttribute("anggota");
		if (obj != null) {
			this.obj = obj;
			image.setSrc(AppUtils.PATH_PHOTO + "/" + objAnggota.getPhotolink());
			image.setHeight("100px");
		}

		doRefresh();
		grid.setRowRenderer(new RowRenderer<Tp2kb>() {

			@Override
			public void render(Row row, Tp2kb data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index + 1)));
				row.getChildren().add(new Label(data.getMp2kbkegiatan().getKegiatan()));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getTotalkegiatan())));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getTotalskp())));

				Button btnDetail = new Button();
				btnDetail.setIconSclass("z-icon-list");
				btnDetail.setTooltiptext("Lihat Detail");
				btnDetail.setAutodisable("self");
				btnDetail.setClass("btn-info btn-sm");
				btnDetail.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						map.put("isApprove", "K");
						Window win = (Window) Executions.createComponents("/view/p2kb/p2kb"
								+ data.getMp2kbkegiatan().getIdkegiatan().trim().toLowerCase() + "detail.zul", null,
								map);
						win.setWidth("90%");
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
							@Override
							public void onEvent(Event event) throws Exception {
								Event closeEvent = new Event("onClose", winVerifikasidata, null);
								Events.postEvent(closeEvent);
							}

						});
						win.setClosable(true);
						win.doModal();
					}
				});
				row.appendChild(btnDetail);
			}
		});
	}

	public void doRefresh() {
		try {
			filter = "tanggotafk = " + obj.getTanggotapk() + " and totaltimapprove > 0";
			orderby = "tp2kbpk asc";

			objList = new Tp2kbDAO().listByFilter(filter, orderby);
			totalkegiatan = 0;
			totalskp = new BigDecimal(0);
			for(Tp2kb data: objList) {
				totalkegiatan = totalkegiatan + data.getTotalkegiatan();
				totalskp = totalskp.add(data.getTotalskp());
			}
			pageTotalSize = objList.size();
			grid.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Vp2kb getObj() {
		return obj;
	}

	public void setObj(Vp2kb obj) {
		this.obj = obj;
	}

	public Integer getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(Integer pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

	public Integer getTotalkegiatan() {
		return totalkegiatan;
	}

	public void setTotalkegiatan(Integer totalkegiatan) {
		this.totalkegiatan = totalkegiatan;
	}

	public BigDecimal getTotalskp() {
		return totalskp;
	}

	public void setTotalskp(BigDecimal totalskp) {
		this.totalskp = totalskp;
	}

}
