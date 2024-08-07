package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
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
import org.zkoss.zul.A;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.MprovDAO;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.domain.Mprov;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Vp2kb;
import com.sds.utils.AppUtils;

public class VerifikasiP2kbVm {
	private Session session = Sessions.getCurrent();
	private Tanggota obj;

	private List<Vp2kb> objList = new ArrayList<>();

	private String filter;
	private String orderby;

	private String nama;
	private Mprov region;

	private Integer pageTotalSize;

	private SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat datelocalFormatter = new SimpleDateFormat("dd-MM-yyyy");

	@Wire
	private Grid grid;
	@Wire
	private Window winVerifikasip2kb;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		obj = (Tanggota) session.getAttribute("anggota");

		try {
			obj = new TanggotaDAO().findByPk(obj.getTanggotapk());
			Map<String, Object> map = new HashMap<>();
			Window win = new Window();
			if (obj.getPeriodekta() != null) {
				Date d1 = sdformat.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				Date d2 = sdformat.parse(new SimpleDateFormat("yyyy-MM-dd").format(obj.getPeriodekta()));

				if (d1.compareTo(d2) < 0) {
					doReset();
					grid.setRowRenderer(new RowRenderer<Vp2kb>() {

						@Override
						public void render(Row row, Vp2kb data, int index) throws Exception {
							row.getChildren().add(new Label(String.valueOf(index + 1)));
							row.getChildren().add(new Label(data.getCabang()));
							row.getChildren().add(new Label(data.getNoanggota()));
							A a = new A(data.getNama());
							a.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

								@Override
								public void onEvent(Event event) throws Exception {
									Map<String, Object> map = new HashMap<>();
									Window win = new Window();
									map.put("obj", data);
									win = (Window) Executions.createComponents("/view/timp2kb/verifikasip2kbdata.zul",
											null, map);
									//win.setWidth("70%");
									win.setClosable(true);
									win.doModal();
									win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
										@Override
										public void onEvent(Event event) throws Exception {
											doReset();
											BindUtils.postNotifyChange(null, null, VerifikasiP2kbVm.this,
													"pageTotalSize");
										}

									});
								}
							});
							row.getChildren().add(a);
							row.getChildren().add(new Label(data.getNostr()));
							row.getChildren().add(new Label(datelocalFormatter.format(data.getTglmulai()) + " s/d " + datelocalFormatter.format(data.getTglakhir())));
							row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getTotalkegiatanwv())));
						}
					});
				} else {
					win = (Window) Executions.createComponents("/view/infoperiodekta.zul", null, map);
					win.setWidth("50%");
					win.setClosable(true);
					win.doModal();
					win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
						@Override
						public void onEvent(Event event) throws Exception {
							winVerifikasip2kb.getChildren().clear();
							winVerifikasip2kb.setBorder(false);
							Executions.createComponents("/view/payment/payment.zul", winVerifikasip2kb, null);
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
						winVerifikasip2kb.getChildren().clear();
						winVerifikasip2kb.setBorder(false);
						Executions.createComponents("/view/payment/payment.zul", winVerifikasip2kb, null);
					}

				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Command
	@NotifyChange("*")
	public void doSearch() {
		try {
			filter = "";
			if (obj.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSPROVINSI)) {
				if (filter.length() > 0)
					filter += " and ";
				filter += "mprovfk = " + obj.getMcabang().getMprov().getMprovpk();
			} else if (obj.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN)) {
				if (filter.length() > 0)
					filter += " and ";
				filter += "mcabangpk = " + obj.getMcabang().getMcabangpk();
			}
			
			//filter = "mcabangpk = " + obj.getMcabang().getMcabangpk();
			orderby = "nama";

			if (nama != null && nama.trim().length() > 0) {
				if (filter.length() > 0)
					filter += " and ";
				filter += "upper(nama) like '%" + nama.trim().toUpperCase() + "%'";
			}
			
			if (region != null)
				filter += " ";

			objList = new Tp2kbDAO().listVerifikasiTim(filter, orderby);
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
		region = null;
		pageTotalSize = 0;

		doSearch();
	}

	public ListModelList<Mprov> getRegionmodel() {
		ListModelList<Mprov> lm = null;
		try {
			lm = new ListModelList<Mprov>(new MprovDAO().listAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lm;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Mprov getRegion() {
		return region;
	}

	public void setRegion(Mprov region) {
		this.region = region;
	}

	public Integer getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(Integer pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

}
