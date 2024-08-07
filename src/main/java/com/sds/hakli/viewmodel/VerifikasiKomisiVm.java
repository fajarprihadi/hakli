package com.sds.hakli.viewmodel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
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
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.MprovDAO;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.dao.Tp2kbbookDAO;
import com.sds.hakli.domain.Mprov;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tp2kbbook;
import com.sds.utils.AppData;
import com.sds.utils.db.StoreHibernateUtil;

public class VerifikasiKomisiVm {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota anggota;

	private List<Tp2kbbook> objList = new ArrayList<>();

	private String filter;
	private String orderby;

	private String nama;
	private Mprov region;

	private String action;

	private Integer pageTotalSize;
	private Map<Integer, Tp2kbbook> mapData = new HashMap<>();

	private SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

	@Wire
	private Grid grid;
	@Wire
	private Window winVerifikasikomisi;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		anggota = (Tanggota) zkSession.getAttribute("anggota");

		try {
			anggota = new TanggotaDAO().findByPk(anggota.getTanggotapk());
			Map<String, Object> map = new HashMap<>();
			Window win = new Window();
			if (anggota.getPeriodekta() != null) {
				Date d1 = sdformat.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				Date d2 = sdformat.parse(new SimpleDateFormat("yyyy-MM-dd").format(anggota.getPeriodekta()));

				if (d1.compareTo(d2) < 0) {
					doReset();
					grid.setRowRenderer(new RowRenderer<Tp2kbbook>() {

						@Override
						public void render(Row row, Tp2kbbook data, int index) throws Exception {
							row.getChildren().add(new Label(String.valueOf(index + 1)));
							Checkbox check = new Checkbox();
							check.setAttribute("obj", data);
							check.addEventListener(Events.ON_CHECK, new EventListener<Event>() {
								@Override
								public void onEvent(Event event) throws Exception {
									Checkbox checked = (Checkbox) event.getTarget();
									if (checked.isChecked()) {
										if (data.getIspaid() != null && data.getIspaid().trim().equals("Y"))
											mapData.put(data.getTp2kbbookpk(), data);
										else {
											check.setChecked(false);
											Messagebox.show("Status belum melakukan pembayaran.");
										}
									} else {
										mapData.remove(data.getTp2kbbookpk());
									}
								}
							});
							if (mapData.get(data.getTp2kbbookpk()) != null)
								check.setChecked(true);

							row.getChildren().add(check);
							row.getChildren().add(new Label(data.getNostr()));
							row.getChildren().add(new Label(data.getTanggota().getMcabang().getCabang()));
							row.getChildren().add(new Label(data.getTanggota().getNoanggota()));
							row.getChildren().add(new Label(data.getTanggota().getNama()));
							row.getChildren()
									.add(new Label(new SimpleDateFormat("dd MMMMM yyyy").format(data.getTglmulai())));
							row.getChildren()
									.add(new Label(new SimpleDateFormat("dd MMMMM yyyy").format(data.getTglakhir())));
							row.getChildren().add(new Label(DecimalFormat.getInstance().format(data.getTotalskp())));
							row.getChildren()
									.add(new Label(data.getIspaid() != null && data.getIspaid().equals("Y") ? "LUNAS"
											: "BELUM BAYAR"));
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
							winVerifikasikomisi.getChildren().clear();
							winVerifikasikomisi.setBorder(false);
							Executions.createComponents("/view/payment/payment.zul", winVerifikasikomisi, null);
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
						winVerifikasikomisi.getChildren().clear();
						winVerifikasikomisi.setBorder(false);
						Executions.createComponents("/view/payment/payment.zul", winVerifikasikomisi, null);
					}

				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Command
	public void doCheckedall(@BindingParam("checked") Boolean checked) {
		try {
			List<Row> components = grid.getRows().getChildren();
			for (Row comp : components) {
				Checkbox chk = (Checkbox) comp.getChildren().get(1);
				Tp2kbbook obj = (Tp2kbbook) chk.getAttribute("obj");
				if (checked) {
					if (!chk.isChecked()) {
						if (obj.getIspaid() != null && obj.getIspaid().trim().equals("Y")) {
							chk.setChecked(true);
							mapData.put(obj.getTp2kbbookpk(), obj);
						}
					}
				} else {
					if (mapData.get(obj.getTp2kbbookpk()) != null) {
						mapData.remove(obj.getTp2kbbookpk());
						chk.setChecked(false);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("*")
	public void doSave() {
		if (mapData.size() > 0) {
			Messagebox.show("Apakah anda yakin untuk menyetujui data ini?", "Confirm Dialog",
					Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							if (event.getName().equals("onOK")) {
								try {
									Map<Integer, String> mapRomawi = new HashMap<>();

									Session session = StoreHibernateUtil.openSession();
									Transaction trx = session.beginTransaction();

									int year = Calendar.getInstance().get(Calendar.YEAR);
									int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

									mapRomawi = AppData.getAngkaRomawi();

									for (Entry<Integer, Tp2kbbook> entry : mapData.entrySet()) {
										Tp2kbbook obj = entry.getValue();
//										String nosurat = obj.getTanggota().getProvcode() + new TcounterengineDAO()
//												.getLetterRecomNo("/REKOM/PP-HAKLI/" + mapRomawi.get(month) + "/" + year, 4);
										String nosurat = obj.getTanggota().getProvcode() + new TcounterengineDAO()
												.getLetterRecomNo("/REKOM/KOLEGIUM/" + mapRomawi.get(month) + "/" + year, 4);
										obj.setLetterno(nosurat);
										obj.setReviewerid(anggota.getNoanggota());
										obj.setReviewername(anggota.getNama());
										obj.setReviewtime(new Date());
										obj.setStatus("C");
										new Tp2kbbookDAO().save(session, obj);
									}

									trx.commit();
									session.close();
									doReset();
									Clients.showNotification("Data berhasil disetujui.", "info", null, "middle_center",
											1500);
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						}
					});
		} else {
			Messagebox.show("Tidak ada data yang dipilih.");
		}
	}

	@Command
	@NotifyChange("*")
	public void doSearch() {
		try {
			//filter = "totalskp >= 50 and status = 'R'";
			filter = "status = 'R'";
			orderby = "tglmulai";

			if (nama != null && nama.trim().length() > 0)
				filter += " and upper(tanggota.nama) like '%" + nama.trim().toUpperCase() + "%'";

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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
