package com.sds.hakli.viewmodel;

import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.chart.AxisTitle;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Tooltip;
import org.zkoss.chart.model.CategoryModel;
import org.zkoss.chart.model.DefaultCategoryModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.dao.TeventregDAO;
import com.sds.hakli.dao.TpekerjaanDAO;
import com.sds.hakli.dao.TpendidikanDAO;
import com.sds.hakli.domain.Tevent;
import com.sds.hakli.domain.Teventreg;
import com.sds.hakli.domain.Tpekerjaan;
import com.sds.hakli.domain.Tpendidikan;
import com.sds.hakli.domain.Veventamount;
import com.sds.hakli.handler.NaskahHandler;
import com.sds.hakli.model.TeventregListModel;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;

public class EventDetailVm {

	private TeventregDAO eventregDao = new TeventregDAO();
	private TpekerjaanDAO pekerjaanDao = new TpekerjaanDAO();
	private TpendidikanDAO pendidikanDao = new TpendidikanDAO();
	private TeventregListModel model;
	private Tevent obj;
	
	private int pageStartNumber;
	private int pageTotalSize;
	private boolean needsPageUpdate;
	private String filter;
	private String nama;
	private String email;
	private String vano;
	private String status;
	private String gender;
	private String agama;
	
	private SimpleDateFormat dateLocalFormatter = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat datetimeLocalFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	@Wire
	private Window winEventDetail;
	@Wire
	private Paging paging;
	@Wire
	private Grid grid;
	@Wire
	private Charts chart;
	@Wire
	private Combobox cbStatus;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tevent obj) {
		Selectors.wireComponents(view, this, false);
		try {
			this.obj = obj;
			
			if (paging != null) {
				paging.addEventListener("onPaging", new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						PagingEvent pe = (PagingEvent) event;
						pageStartNumber = pe.getActivePage();
						refreshModel(pageStartNumber);
					}
				});
			}
			
			grid.setRowRenderer(new RowRenderer<Teventreg>() {

				@Override
				public void render(Row row, Teventreg data, int index) throws Exception {
					row.getChildren().add(new Label(String.valueOf((AppUtils.PAGESIZE * pageStartNumber) + index + 1)));
					row.getChildren().add(new Label(data.getTanggota().getNama()));
					row.getChildren().add(new Label(data.getTanggota().getEmail()));
					row.getChildren().add(new Label(data.getTanggota().getMcabang().getMprov().getProvname()));
					row.getChildren().add(new Label(data.getTanggota().getMcabang().getCabang()));
					row.getChildren().add(new Label(data.getVacreatedat() != null ? datetimeLocalFormatter.format(data.getVacreatedat()) : ""));
					row.getChildren().add(new Label(data.getVano()));
					row.getChildren().add(new Label(data.getVaexpdate() != null ? datetimeLocalFormatter.format(data.getVaexpdate()) : ""));
					row.getChildren().add(new Label(data.getIspaid().equals("Y") ? "Sudah Dibayar" : "Belum Dibayar"));
					row.getChildren().add(new Label(data.getPaidamount() != null ? NumberFormat.getInstance().format(data.getPaidamount()) : ""));
					row.getChildren().add(new Label(data.getPaidat() != null ? datetimeLocalFormatter.format(data.getPaidat()) : ""));
					
					if (data.getIspaid().equals("Y")) {
						Button btNaskah = new Button("Sumpah Profesi");
						btNaskah.setIconSclass("z-icon-download");
						btNaskah.setSclass("btn btn-success btn-sm");
						btNaskah.setAutodisable("self");
						btNaskah.setTooltiptext("Download Naskah Sumpah Profesi");
						btNaskah.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								new NaskahHandler().downloadNaskah(data, "sumpah");
							}
						});

						Button btEtika = new Button("Etika Profesi");
						btEtika.setIconSclass("z-icon-download");
						btEtika.setSclass("btn btn-success btn-sm");
						btEtika.setAutodisable("self");
						btEtika.setTooltiptext("Download Naskah Etika");
						btEtika.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								new NaskahHandler().downloadNaskah(data, "etik");
							}
						});
						
						Hlayout hlayout = new Hlayout();
						hlayout.appendChild(btNaskah);
						hlayout.appendChild(new Separator("vertical"));
						hlayout.appendChild(btEtika);
						row.getChildren().add(hlayout);
					} else {
						row.getChildren().add(new Label());
					}
					
					Button btProcess = new Button();
					btProcess.setIconSclass("z-icon-eye");
					btProcess.setSclass("btn btn-primary btn-sm");
					btProcess.setAutodisable("self");
					btProcess.setTooltiptext("Detail");
					btProcess.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("obj", data.getTanggota());
							map.put("acttype", "view");
							Window win = (Window) Executions
									.createComponents("/view/anggota/anggotaedit.zul", null, map);
							win.setClosable(true);
							win.setWidth("98%");
							win.doModal();
						}
					});
					row.getChildren().add(btProcess);
				}
			});
			
			doReset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@NotifyChange("pageTotalSize")
	public void refreshModel(int activePage) {
		paging.setPageSize(AppUtils.PAGESIZE);
		model = new TeventregListModel(activePage, AppUtils.PAGESIZE, filter, "teventregpk desc");
		if (needsPageUpdate) {
			pageTotalSize = model.getTotalSize(filter);
			needsPageUpdate = false;
		}
		paging.setTotalSize(pageTotalSize);
		grid.setModel(model);
	}
	
	@Command
	@NotifyChange("pageTotalSize")
	public void doSearch() {
		filter = "teventfk = " + obj.getTeventpk();
		if (nama != null && nama.trim().length() > 0)
			filter += " and upper(nama) like '%" + nama.trim().toUpperCase() + "%'";
		if (email != null && email.trim().length() > 0)
			filter += " and upper(email) like '%" + email.trim().toUpperCase() + "%'";
		if (vano != null && vano.trim().length() > 0)
			filter += " and teventreg.vano like '%" + vano.trim() + "%'";
		if (status != null && status.trim().length() > 0)
			filter += " and ispaid = '" + status + "'";
		if (gender != null && gender.trim().length() > 0)
			filter += " and gender = '" + gender.trim() + "'";
		if (agama != null && agama.trim().length() > 0)
			filter += " and agama = '" + agama.trim() + "'";
		needsPageUpdate = true;
		pageStartNumber = 0;
		refreshModel(pageStartNumber);
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		nama = null;
		email = null;
		vano = null;
		status = null;
		gender = null;
		agama = null;
		cbStatus.setValue(null);
		doSearch();
		doChart();
	}
	
	public void doChart() {
		try {
			chart.setSubtitle("Potensi Pendapatan VS Realisasi Pendapatan");
			
			Veventamount vsum = eventregDao.sumAmount(obj.getTeventpk());
			
			CategoryModel model = new DefaultCategoryModel();
			model.setValue("Potensi", "Pendapatan", vsum.getInvamount());
	        model.setValue("Realisasi", "Pendapatan", vsum.getPaymentamount());
			
	        chart.setModel(model);
	        
	        chart.getXAxis().setCrosshair(true);
	        
	        AxisTitle title = chart.getYAxis().getTitle();
	        title.setUseHTML(true);
	        title.setText("Nominal Pendapatan");
	        
	        Tooltip tooltip = chart.getTooltip();
	        tooltip.setHeaderFormat("<span style=\"font-size:10px\">{point.key}</span><table>");
	        tooltip.setPointFormat("<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>"
	            + "<td style=\"padding:0\"><b>{point.y:.1f}</b></td></tr>");
	        tooltip.setFooterFormat("</table>");
	        tooltip.setShared(true);
	        tooltip.setUseHTML(true);
	        
	        chart.getPlotOptions().getColumn().setPointPadding(0.2);
	        chart.getPlotOptions().getColumn().setBorderWidth(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doBack() {
		try {
			Component comp = winEventDetail.getParent().getParent();
			comp.getChildren().clear();
			Executions.createComponents("/view/event/event.zul", comp, null);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Command
	public void doExport() {
		try {
			List<Teventreg> objList = eventregDao.listNative(filter, "nama");
			if (objList.size() > 0) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet();
				XSSFCellStyle style = workbook.createCellStyle();
				style.setBorderTop(BorderStyle.MEDIUM);
				style.setBorderBottom(BorderStyle.MEDIUM);
				style.setBorderLeft(BorderStyle.MEDIUM);
				style.setBorderRight(BorderStyle.MEDIUM);

				int rownum = 0;
				int cellnum = 0;
				Integer no = 0;
				org.apache.poi.ss.usermodel.Row row = sheet.createRow(rownum++);
				Cell cell = row.createCell(1);
				cell.setCellValue("Event");
				cell = row.createCell(2);
				cell.setCellValue(obj.getEventname());
				row = sheet.createRow(rownum++);
				cell = row.createCell(1);
				cell.setCellValue("Tipe Event");
				cell = row.createCell(2);
				cell.setCellValue(AppData.getEventType(obj.getEventtype()));
				row = sheet.createRow(rownum++);
				cell = row.createCell(1);
				cell.setCellValue("Tanggal Pelaksanaan");
				cell = row.createCell(2);
				cell.setCellValue(dateLocalFormatter.format(obj.getEventdate()));
				row = sheet.createRow(rownum++);
				cell = row.createCell(1);
				cell.setCellValue("Lokasi");
				cell = row.createCell(2);
				cell.setCellValue(obj.getEventlocation());
				row = sheet.createRow(rownum++);
				cell = row.createCell(1);
				cell.setCellValue("Biaya");
				cell = row.createCell(2);
				cell.setCellValue(obj.getEventprice().doubleValue());
				rownum++;
				Map<Integer, Object[]> datamap = new TreeMap<Integer, Object[]>();
				datamap.put(1, new Object[] { "No", "No Anggota", "Nama", "No STR", "E-Mail", "Agama", "Region", "Cabang",
						"Provinsi Domisili", "Kabupaten Domisili", "Alamat Domisili", 
						"Tempat Kerja", "Provinsi", "Kabupaten", "Alamat", "Rumpun", "Kepegawaian", "Sub Kepegawian", 
						"Perguruan Tinggi", "Jenjang", "Peminatan 1", "Peminatan 2", "Periode Awal", "Periode Akhir", "No Ijazah", 
						"Nomor VA", "Status Bayar", "Jumlah Bayar", "Waktu Bayar"});
				no = 2;
				for (Teventreg data : objList) {
					List<Tpekerjaan> pekerjaans = pekerjaanDao.listByFilter("tanggota.tanggotapk = " + data.getTanggota().getTanggotapk(), "tpekerjaanpk desc");
					List<Tpendidikan> pendidikans = pendidikanDao.listByFilter("tanggota.tanggotapk = " + data.getTanggota().getTanggotapk(), "tpendidikanpk desc");
					Tpekerjaan pekerjaan = new Tpekerjaan();
					if (pekerjaans.size() > 0)
						pekerjaan = pekerjaans.get(0);
					Tpendidikan pendidikan = new Tpendidikan();
					if (pendidikans.size() > 0)
						pendidikan = pendidikans.get(0);
					
					datamap.put(no,
							new Object[] { no - 1, data.getTanggota().getNoanggota(), data.getTanggota().getNama(), data.getTanggota().getNostr(), data.getTanggota().getEmail(), data.getTanggota().getAgama(),
									data.getTanggota().getMcabang().getMprov().getProvname(), data.getTanggota().getMcabang().getCabang(), data.getTanggota().getProvname(), data.getTanggota().getKabname(), data.getTanggota().getAlamat(), 
									pekerjaan.getNamakantor(), pekerjaan.getProvname(), pekerjaan.getKabname(), pekerjaan.getAlamatkantor(), 
									pekerjaan.getMrumpun() != null ? pekerjaan.getMrumpun().getRumpun() : "", 
									pekerjaan.getMkepegawaian() != null ? pekerjaan.getMkepegawaian().getKepegawaian() : "",
									pekerjaan.getMkepegawaiansub() != null ? pekerjaan.getMkepegawaiansub().getKepegawaiansub() : "",
									pendidikan.getMuniversitas() != null ? pendidikan.getMuniversitas().getUniversitas() : "", 
									pendidikan.getMjenjang() != null ? pendidikan.getMjenjang().getJenjang() : "", pendidikan.getPeminatan1(), pendidikan.getPeminatan2(), 
									pendidikan.getPeriodethawal(), pendidikan.getPeriodethakhir(), pendidikan.getNoijazah(), data.getVano(), data.getIspaid().equals("Y") ? "Sudah Bayar" : "Belum Bayar", data.getPaidamount() != null ? data.getPaidamount().doubleValue() : "", data.getPaidat() != null ? datetimeLocalFormatter.format(data.getPaidat()) : "" });
					no++;
				}
				Set<Integer> keyset = datamap.keySet();
				for (Integer key : keyset) {
					row = sheet.createRow(rownum++);
					Object[] objArr = datamap.get(key);
					cellnum = 0;
					if (rownum == 7) {
						for (Object obj : objArr) {
							cell = row.createCell(cellnum++);
							if (obj instanceof String) {
								cell.setCellValue((String) obj);
								cell.setCellStyle(style);
							} else if (obj instanceof Integer) {
								cell.setCellValue((Integer) obj);
								cell.setCellStyle(style);
							} else if (obj instanceof Double) {
								cell.setCellValue((Double) obj);
								cell.setCellStyle(style);
							}
						}
					} else {
						for (Object obj : objArr) {
							cell = row.createCell(cellnum++);
							if (obj instanceof String) {
								cell.setCellValue((String) obj);
								//cell.setCellStyle(style);
							} else if (obj instanceof Integer) {
								cell.setCellValue((Integer) obj);
								//cell.setCellStyle(style);
							} else if (obj instanceof Double) {
								cell.setCellValue((Double) obj);
								//cell.setCellStyle(style);
							}
						}
					}
				}

				String path = Executions.getCurrent().getDesktop().getWebApp()
						.getRealPath(AppUtils.PATH_REPORT);
				String filename = "HAKLI_EVENT_" + new SimpleDateFormat("yyMMddHHmm").format(new Date()) + ".xlsx";
				FileOutputStream out = new FileOutputStream(new File(path + "/" + filename));
				workbook.write(out);
				out.close();
				workbook.close();

				Filedownload.save(new File(path + "/" + filename),
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			} else {
				Messagebox.show("Tidak ada data", WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.INFORMATION);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(int pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

	public String getVano() {
		return vano;
	}

	public void setVano(String vano) {
		this.vano = vano;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Tevent getObj() {
		return obj;
	}

	public void setObj(Tevent obj) {
		this.obj = obj;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAgama() {
		return agama;
	}

	public void setAgama(String agama) {
		this.agama = agama;
	}
}
