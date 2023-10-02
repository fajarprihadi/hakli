package com.sds.hakli.viewmodel;

import java.io.File;
import java.io.FileOutputStream;
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
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.dao.McabangDAO;
import com.sds.hakli.dao.MprovDAO;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TpekerjaanDAO;
import com.sds.hakli.dao.TpendidikanDAO;
import com.sds.hakli.domain.Mcabang;
import com.sds.hakli.domain.Mprov;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tpekerjaan;
import com.sds.hakli.domain.Tpendidikan;
import com.sds.hakli.model.TanggotaListModel;
import com.sds.utils.AppUtils;
import com.sds.utils.StringUtils;

public class AnggotaVm {
	
	private Session session = Sessions.getCurrent();
	private Tanggota oUser;
	
	private TanggotaDAO oDao = new TanggotaDAO();
	private TpekerjaanDAO pekerjaanDao = new TpekerjaanDAO();
	private TpendidikanDAO pendidikanDao = new TpendidikanDAO();
	
	private TanggotaListModel model;
	private ListModelList<Mcabang> cabangModel;
	
	private int pageStartNumber;
	private int pageTotalSize;
	private boolean needsPageUpdate;
	private String filter;
	
	private String nama;
	private String agama;
	private String jenjang;
	private Mprov region;
	private Mcabang cabang;
	
	@Wire
	private Window winAnggota;
	@Wire
	private Combobox cbRegion;
	@Wire
	private Combobox cbCabang;
	@Wire
	private Paging paging;
	@Wire
	private Grid grid;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("act") String act) {
		Selectors.wireComponents(view, this, false);
		oUser = (Tanggota) session.getAttribute("anggota");
		
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
		
		grid.setRowRenderer(new RowRenderer<Tanggota>() {

			@Override
			public void render(Row row, Tanggota data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf((AppUtils.PAGESIZE * pageStartNumber) + index + 1)));
				row.getChildren().add(new Label(data.getNama()));
				row.getChildren().add(new Label(data.getNoanggota()));
				row.getChildren().add(new Label(data.getNoktp()));
				row.getChildren().add(new Label(data.getMcabang().getMprov().getProvname()));
				row.getChildren().add(new Label(data.getMcabang().getCabang()));
				row.getChildren().add(new Label(data.getNostr()));
				Button btView = new Button();
				btView.setIconSclass("z-icon-eye");
				btView.setSclass("btn btn-primary btn-sm");
				btView.setAutodisable("self");
				btView.setTooltiptext("Detail");
				btView.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						map.put("acttype", "view");
						Window win = (Window) Executions
								.createComponents("/view/anggota/anggotaedit.zul", null, map);
						win.setClosable(true);
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								doSearch();
								BindUtils.postNotifyChange(AnggotaVm.this, "pageTotalSize");
							}
						});
						win.setWidth("98%");
						win.doModal();
					}
				});
				
				Hlayout hlayout = new Hlayout();
				hlayout.appendChild(btView);
				
				if (act == null) {
					Button btDel = new Button();
					btDel.setIconSclass("z-icon-trash");
					btDel.setSclass("btn btn-danger btn-sm");
					btDel.setAutodisable("self");
					btDel.setTooltiptext("Delete");
					btDel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							Messagebox.show("Apakah anda yakin ingin menghapus data anggota atas nama " + data.getNama() + "?", "Confirm Dialog",
									Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
										@Override
										public void onEvent(Event event) throws Exception {
											if (event.getName().equals("onOK")) {
												try {
													Map<String, Object> map = new HashMap<String, Object>();
													map.put("obj", data);
													Window win = (Window) Executions
															.createComponents("/view/anggota/anggotadel.zul", null, map);
													win.setClosable(true);
													win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

														@Override
														public void onEvent(Event event) throws Exception {
															doReset();
															BindUtils.postNotifyChange(AnggotaVm.this, "*");
														}
													});
													win.doModal();
												} catch(Exception e) {
													e.printStackTrace();
												}
											}
										}
							});							
						}
					});
					hlayout.appendChild(btDel);
				}
				
				if (act != null && act.equals("role")) {
					Button btSetRoles = new Button();
					btSetRoles.setIconSclass("z-icon-user-secret");
					btSetRoles.setSclass("btn btn-danger btn-sm");
					btSetRoles.setAutodisable("self");
					btSetRoles.setTooltiptext("Set Kewenangan");
					btSetRoles.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("obj", data);
							Window win = (Window) Executions
									.createComponents("/view/anggota/anggotarole.zul", null, map);
							win.setClosable(true);
							win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

								@Override
								public void onEvent(Event event) throws Exception {
									doSearch();
									BindUtils.postNotifyChange(AnggotaVm.this, "pageTotalSize");
								}
							});
							win.doModal();
						}
					});
					
					hlayout.appendChild(btSetRoles);
				} else if (act != null && act.equals("lookup")) {
					Button btSetRoles = new Button("Pilih");
					btSetRoles.setSclass("btn btn-warning btn-sm");
					btSetRoles.setAutodisable("self");
					//btSetRoles.setTooltiptext("Pilih");
					btSetRoles.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							Event closeEvent = new Event("onClose", winAnggota, data);
							Events.postEvent(closeEvent);
						}
					});
					
					hlayout.appendChild(btSetRoles);
				}
				
				row.getChildren().add(hlayout);
			}
		});
		
		if (act == null || !act.equals("lookup"))
			doReset();
	}
	
	@NotifyChange("pageTotalSize")
	public void refreshModel(int activePage) {
		paging.setPageSize(AppUtils.PAGESIZE);
		model = new TanggotaListModel(activePage, AppUtils.PAGESIZE, filter, "nama");
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
		filter = "statusreg = '" + AppUtils.STATUS_ANGGOTA_REG_ACTIVE + "'";
		
		if (nama != null && nama.trim().length() > 0)
			filter += " and upper(nama) like '%" + nama.trim().toUpperCase() + "%'" ;
		if (region != null) {
			if (cabang != null)
				filter += " and mcabangfk = " + cabang.getMcabangpk();
			else filter += " and mcabang.mprovfk = " + region.getMprovpk();
		}
		
		if (agama != null && agama.trim().length() > 0)
			filter += " and upper(agama) like '%" + agama.trim().toUpperCase() + "%'" ;
		if (jenjang != null && jenjang.trim().length() > 0)
			filter += " and upper(jenjang) like '%" + nama.trim().toUpperCase() + "%'" ;
		
		if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSPROVINSI)) {
			filter += " and mprovfk = " + oUser.getMcabang().getMprov().getMprovpk();
		} else if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN)) {
			filter += " and mcabangfk = " + oUser.getMcabang().getMcabangpk();
		}

		needsPageUpdate = true;
		pageStartNumber = 0;
		refreshModel(pageStartNumber);
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		nama = null;
		agama = null;
		jenjang = null;
		region = null;
		cabang = null;
		cbRegion.setValue(null);
		cbCabang.setValue(null);
		doSearch();
	}
	
	@Command
	@NotifyChange("cabangModel")
	public void doLoadCabang(@BindingParam("prov") Mprov prov) {
		try {
			if (prov != null) {
				String filter = "";
				if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN))
					filter = "mcabangpk = " + oUser.getMcabang().getMcabangpk();
				else filter = "mprov.mprovpk = " + prov.getMprovpk(); 
				
				cbCabang.setValue(null);
				cabangModel = new ListModelList<>(
						new McabangDAO().listByFilter(filter, "cabang"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doExport() {
		try {
			List<Tanggota> objList = oDao.listNative(filter, "nama");
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
				//org.apache.poi.ss.usermodel.Row row = sheet.createRow(rownum++);
				//Cell cell = row.createCell(0);
				org.apache.poi.ss.usermodel.Row row = null;
				Cell cell = null;
				rownum++;
				Map<Integer, Object[]> datamap = new TreeMap<Integer, Object[]>();
				datamap.put(1, new Object[] { "No", "No Anggota", "Nama", "No STR", "E-Mail", "Agama", "Region", "Cabang",
						"Provinsi Domisili", "Kabupaten Domisili", "Alamat Domisili", 
						"Tempat Kerja", "Provinsi", "Kabupaten", "Alamat", "Rumpun", "Kepegawaian", "Sub Kepegawian", 
						"Perguruan Tinggi", "Jenjang", "Peminatan 1", "Peminatan 2", "Periode Awal", "Periode Akhir", "No Ijazah" });
				no = 2;
				for (Tanggota data : objList) {
					List<Tpekerjaan> pekerjaans = pekerjaanDao.listByFilter("tanggota.tanggotapk = " + data.getTanggotapk(), "tpekerjaanpk desc");
					List<Tpendidikan> pendidikans = pendidikanDao.listByFilter("tanggota.tanggotapk = " + data.getTanggotapk(), "tpendidikanpk desc");
					Tpekerjaan pekerjaan = new Tpekerjaan();
					if (pekerjaans.size() > 0)
						pekerjaan = pekerjaans.get(0);
					Tpendidikan pendidikan = new Tpendidikan();
					if (pendidikans.size() > 0)
						pendidikan = pendidikans.get(0);
					
					datamap.put(no,
							new Object[] { no - 1, data.getNoanggota(), data.getNama(), data.getNostr(), data.getEmail(), data.getAgama(), 
									data.getMcabang().getMprov().getProvname(), data.getMcabang().getCabang(), data.getProvname(), data.getKabname(), data.getAlamat(), 
									pekerjaan.getNamakantor(), pekerjaan.getProvname(), pekerjaan.getKabname(), pekerjaan.getAlamatkantor(), 
									pekerjaan.getMrumpun() != null ? pekerjaan.getMrumpun().getRumpun() : "", 
									pekerjaan.getMkepegawaian() != null ? pekerjaan.getMkepegawaian().getKepegawaian() : "",
									pekerjaan.getMkepegawaiansub() != null ? pekerjaan.getMkepegawaiansub().getKepegawaiansub() : "",
									pendidikan.getMuniversitas() != null ? pendidikan.getMuniversitas().getUniversitas() : "", 
									pendidikan.getMjenjang() != null ? pendidikan.getMjenjang().getJenjang() : "", pendidikan.getPeminatan1(), pendidikan.getPeminatan2(), 
									pendidikan.getPeriodethawal(), pendidikan.getPeriodethakhir(), pendidikan.getNoijazah() });
					no++;
				}
				Set<Integer> keyset = datamap.keySet();
				for (Integer key : keyset) {
					row = sheet.createRow(rownum++);
					Object[] objArr = datamap.get(key);
					cellnum = 0;
					if (rownum == 6) {
						XSSFCellStyle styleHeader = workbook.createCellStyle();
						styleHeader.setBorderTop(BorderStyle.MEDIUM);
						styleHeader.setBorderBottom(BorderStyle.MEDIUM);
						styleHeader.setBorderLeft(BorderStyle.MEDIUM);
						styleHeader.setBorderRight(BorderStyle.MEDIUM);
						styleHeader.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
						//styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
						for (Object obj : objArr) {
							cell = row.createCell(cellnum++);
							if (obj instanceof String) {
								cell.setCellValue((String) obj);
								cell.setCellStyle(styleHeader);
							} else if (obj instanceof Integer) {
								cell.setCellValue((Integer) obj);
								cell.setCellStyle(styleHeader);
							} else if (obj instanceof Double) {
								cell.setCellValue((Double) obj);
								cell.setCellStyle(styleHeader);
							}
						}
					} else {
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
					}
				}

				String path = Executions.getCurrent().getDesktop().getWebApp()
						.getRealPath(AppUtils.PATH_REPORT);
				String filename = "HAKLI_ANGGOTA_" + new SimpleDateFormat("yyMMddHHmm").format(new Date()) + ".xlsx";
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
	
	public ListModelList<Mprov> getRegionModel() {
		ListModelList<Mprov> oList = null;
		try {
			if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_ADMIN) || oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSPUSAT))
				oList = new ListModelList<>(new MprovDAO().listAll());
			else oList = new ListModelList<>(new MprovDAO().listByFilter("mprovpk = " + oUser.getMcabang().getMprov().getMprovpk(), "provname"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}
	
	public int getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(int pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

	public ListModelList<Mcabang> getCabangModel() {
		return cabangModel;
	}

	public void setCabangModel(ListModelList<Mcabang> cabangModel) {
		this.cabangModel = cabangModel;
	}

	public Mprov getRegion() {
		return region;
	}

	public void setRegion(Mprov region) {
		this.region = region;
	}

	public Mcabang getCabang() {
		return cabang;
	}

	public void setCabang(Mcabang cabang) {
		this.cabang = cabang;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getAgama() {
		return agama;
	}

	public void setAgama(String agama) {
		this.agama = agama;
	}

	public String getJenjang() {
		return jenjang;
	}

	public void setJenjang(String jenjang) {
		this.jenjang = jenjang;
	}
	
}
