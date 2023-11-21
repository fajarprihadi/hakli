package com.sds.hakli.viewmodel;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.dao.McabangDAO;
import com.sds.hakli.dao.MprovDAO;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TmutasiDAO;
import com.sds.hakli.domain.Mcabang;
import com.sds.hakli.domain.Mprov;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tmutasi;
import com.sds.hakli.domain.Tpekerjaan;
import com.sds.hakli.domain.Tpendidikan;
import com.sds.hakli.model.TanggotaListModel;
import com.sds.hakli.model.TmutasiListModel;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class ReportMutasiVm {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota oUser;

	private TmutasiDAO oDao = new TmutasiDAO();
	private String action;
	private String decisionmemo;
	
	private TmutasiListModel model;
	private ListModelList<Mcabang> cabangasalModel;
	private ListModelList<Mcabang> cabangtujuanModel;
	private int pageStartNumber;
	private int pageTotalSize;
	private boolean needsPageUpdate;
	private String filter;
	private String nama;
	private Mprov provasal;
	private Mcabang cabangasal;
	private Mprov provtujuan;
	private Mcabang cabangtujuan;
	
	private SimpleDateFormat datelocalFormatter = new SimpleDateFormat("dd-MM-yyyy");
	
	@Wire
	private Combobox cbProvAsal;
	@Wire
	private Combobox cbProvTujuan;
	@Wire
	private Combobox cbCabangAsal;
	@Wire
	private Combobox cbCabangTujuan;
	@Wire
	private Paging paging;
	@Wire
	private Grid grid;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		oUser = (Tanggota) zkSession.getAttribute("anggota");

		doReset();
		
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
		
		grid.setRowRenderer(new RowRenderer<Tmutasi>() {

			@Override
			public void render(Row row, Tmutasi data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf((AppUtils.PAGESIZE * pageStartNumber) + index + 1)));
				row.getChildren().add(new Label(data.getTanggota().getNoanggota()));
				row.getChildren().add(new Label(data.getTanggota().getNama()));
				row.getChildren().add(new Label(data.getProvcurr()));
				row.getChildren().add(new Label(data.getCabangcurr()));
				row.getChildren().add(new Label(new SimpleDateFormat("dd-MM-yyyy").format(data.getCreatetime())));
				row.getChildren().add(new Label(data.getMcabang().getMprov().getProvname()));
				row.getChildren().add(new Label(data.getMcabang().getCabang()));
				A a = new A(data.getDocid());
				a.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, String> mapDocument = new HashMap<>();
						mapDocument.put("src", data.getDocpath());
						zkSession.setAttribute("mapDocument", mapDocument);
						Executions.getCurrent().sendRedirect("/view/docviewer.zul", "_blank");
					}
				});
				
				row.getChildren().add(a);
				row.getChildren().add(new Label(data.getMemo()));
				row.getChildren().add(new Label(AppUtils.getStatusLabel(data.getStatus())));
				row.getChildren().add(new Label(data.getChecktime() != null ? new SimpleDateFormat("dd-MM-yyyy").format(data.getChecktime()) : "-"));
				row.getChildren().add(new Label(data.getCheckedby()));
			}
		});
	}
	
	@Command
	@NotifyChange("cabangasalModel")
	public void doLoadCabangAsal(@BindingParam("prov") Mprov prov) {
		try {
			if (prov != null) {
				String filter = "";
				if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN))
					filter = "mcabangpk = " + oUser.getMcabang().getMcabangpk();
				else filter = "mprov.mprovpk = " + prov.getMprovpk(); 
				
				cbCabangAsal.setValue(null);
				cabangasalModel = new ListModelList<>(
						new McabangDAO().listByFilter(filter, "cabang"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("cabangtujuanModel")
	public void doLoadCabangTujuan(@BindingParam("prov") Mprov prov) {
		try {
			if (prov != null) {
				String filter = "";
				if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN))
					filter = "mcabangpk = " + oUser.getMcabang().getMcabangpk();
				else filter = "mprov.mprovpk = " + prov.getMprovpk(); 
				
				cbCabangTujuan.setValue(null);
				cabangtujuanModel = new ListModelList<>(
						new McabangDAO().listByFilter(filter, "cabang"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	@NotifyChange("pageTotalSize")
	public void refreshModel(int activePage) {				
		paging.setPageSize(AppUtils.PAGESIZE);
		model = new TmutasiListModel(activePage, AppUtils.PAGESIZE, filter, "tmutasipk desc");
		if (needsPageUpdate) {
			pageTotalSize = model.getTotalSize(filter);
			needsPageUpdate = false;
		}
		paging.setTotalSize(pageTotalSize);
		grid.setModel(model);
	}
	
	@Command
	public void doSearch() {
		filter ="0=0";
		
		if (provasal != null) {
			if (filter.length() > 0)
				filter += " and ";
			filter += "kodeprovcurr = '" + provasal.getProvcode() + "'";
		}	
		if (cabangasal != null) {
			if (filter.length() > 0)
				filter += " and ";
			filter += "kodecabangcurr = '" + cabangasal.getKodecabang() + "'";
		}
		if (provtujuan != null) {
			if (filter.length() > 0)
				filter += " and ";
			filter += "mprovfk = " + provtujuan.getMprovpk();
		}	
		if (cabangtujuan != null) {
			if (filter.length() > 0)
				filter += " and ";
			filter += "mcabangfk = " + cabangtujuan.getMcabangpk();
		}
		
		needsPageUpdate = true;
		pageStartNumber = 0;
		refreshModel(pageStartNumber);
	}

	@NotifyChange("*")
	public void doReset() {
		provasal = null;
		cabangasal = null;
		provtujuan = null;
		cabangtujuan = null;
		cbProvAsal.setValue(null);
		cbProvTujuan.setValue(null);
		cbCabangAsal.setValue(null);
		cbCabangTujuan.setValue(null);
		
		doSearch();
	}
	
	@Command
	public void doExport() {
		try {
			List<Tmutasi> objList = oDao.listNative(filter, "tmutasipk desc");
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
//				org.apache.poi.ss.usermodel.Row row = sheet.createRow(rownum++);
//				Cell cell = row.createCell(0);
//				cell.setCellValue("Daftar Mutasi Anggota");
				org.apache.poi.ss.usermodel.Row row = null;
				Cell cell = null;
				//rownum++;
				Map<Integer, Object[]> datamap = new TreeMap<Integer, Object[]>();
				datamap.put(1, new Object[] { "No", "No Anggota", "Nama", "Prov Asal", "Cabang Asal", "Tgl Pengajuan", 
						"Prov Tujuan", "Cabang Tujuan", "Keterangan", "Status", "Tgl Diperiksa", "Pemeriksa" });
				no = 2;
				for (Tmutasi data : objList) {
					datamap.put(no,
							new Object[] { no - 1, data.getTanggota().getNoanggota(), data.getTanggota().getNama(), data.getProvcurr(), data.getCabangcurr(), 
									datelocalFormatter.format(data.getCreatetime()), data.getMcabang().getMprov().getProvname(), data.getMcabang().getCabang(), data.getDecisionmemo() != null ? data.getDecisionmemo() : "", 
									AppUtils.getStatusLabel(data.getStatus()), data.getChecktime() != null ? datelocalFormatter.format(data.getChecktime()) : "", data.getCheckedby() != null ? data.getCheckedby() : "" });
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
				String filename = "HAKLI_MUTASI_" + new SimpleDateFormat("yyMMddHHmm").format(new Date()) + ".xlsx";
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
	
	public ListModelList<Mprov> getProvasalModel() {
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
	
	public ListModelList<Mprov> getProvtujuanModel() {
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

	public String getDecisionmemo() {
		return decisionmemo;
	}

	public void setDecisionmemo(String decisionmemo) {
		this.decisionmemo = decisionmemo;
	}

	

	public Mprov getProvasal() {
		return provasal;
	}

	public void setProvasal(Mprov provasal) {
		this.provasal = provasal;
	}

	public Mcabang getCabangasal() {
		return cabangasal;
	}

	public void setCabangasal(Mcabang cabangasal) {
		this.cabangasal = cabangasal;
	}

	public Mprov getProvtujuan() {
		return provtujuan;
	}

	public void setProvtujuan(Mprov provtujuan) {
		this.provtujuan = provtujuan;
	}

	public Mcabang getCabangtujuan() {
		return cabangtujuan;
	}

	public void setCabangtujuan(Mcabang cabangtujuan) {
		this.cabangtujuan = cabangtujuan;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public ListModelList<Mcabang> getCabangasalModel() {
		return cabangasalModel;
	}

	public void setCabangasalModel(ListModelList<Mcabang> cabangasalModel) {
		this.cabangasalModel = cabangasalModel;
	}

	public ListModelList<Mcabang> getCabangtujuanModel() {
		return cabangtujuanModel;
	}

	public void setCabangtujuanModel(ListModelList<Mcabang> cabangtujuanModel) {
		this.cabangtujuanModel = cabangtujuanModel;
	}
}
