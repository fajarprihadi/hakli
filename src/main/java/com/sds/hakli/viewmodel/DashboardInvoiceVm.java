package com.sds.hakli.viewmodel;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.zkoss.zul.A;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.domain.Ttransfer;
import com.sds.hakli.model.TinvoiceListModel;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;

public class DashboardInvoiceVm {
	
	private Session session = Sessions.getCurrent();
	private Tanggota oUser;
	
	private TinvoiceListModel model;
	
	private TinvoiceDAO oDao = new TinvoiceDAO();
	
	private int pageStartNumber;
	private int pageTotalSize;
	private String filter;
	
	private Integer totaldue;
	private BigDecimal totaldueamount;
	private Integer totalunpaid;
	private BigDecimal totalunpaidamount;
	
	private BigDecimal invamount;
	private BigDecimal paidamount;
	private BigDecimal unpaidamount;
	
	private String invoicetype;
	private String invno;
	private String invstatus;
	private String vano;	
	private String nama;
	private Date begindate;
	private Date enddate;
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat datelocalFormatter = new SimpleDateFormat("dd-MM-yyyy");
	
	@Wire
	private Groupbox gbSearch;
	@Wire
	private Grid grid;
	@Wire
	private Paging paging;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		oUser = (Tanggota) session.getAttribute("anggota");
		
		paging.addEventListener("onPaging", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				PagingEvent pe = (PagingEvent) event;
				pageStartNumber = pe.getActivePage();
				refreshModel(pageStartNumber);
			}
		});
		
		grid.setRowRenderer(new RowRenderer<Tinvoice>() {

			@Override
			public void render(Row row, Tinvoice data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf((AppUtils.PAGESIZE * pageStartNumber) + index + 1)));
				row.getChildren().add(new Label(AppData.getInvoiceType(data.getInvoicetype())));
				A aNama = new A(data.getTanggota().getNama());
				aNama.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data.getTanggota());
						map.put("acttype", "view");
						Window win = (Window) Executions
								.createComponents("/view/anggota/anggotaedit.zul", null, map);
						win.setWidth("98%");
						win.setClosable(true);
						win.doModal();
					}
				});
				row.getChildren().add(aNama);
				row.getChildren().add(new Label(data.getTanggota().getMcabang().getMprov().getProvname()));
				row.getChildren().add(new Label(data.getTanggota().getMcabang().getCabang()));
				row.getChildren().add(new Label(data.getInvoiceno()));
				row.getChildren().add(new Label(datelocalFormatter.format(data.getInvoicedate())));
				row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getInvoiceamount())));
				row.getChildren().add(new Label(datelocalFormatter.format(data.getInvoiceduedate())));
				row.getChildren().add(new Label(data.getVano()));
				row.getChildren().add(new Label(data.getInvoicedesc()));
				row.getChildren().add(new Label(data.getIspaid().equals("Y") ? "Sudah Dibayar" : "Belum Dibayar"));
				row.getChildren().add(new Label(data.getPaidtime() != null ? datelocalFormatter.format(data.getPaidtime()) : ""));
				row.getChildren().add(new Label(data.getPaidrefno() != null ? data.getPaidrefno() : ""));
			}
		
		});
		
		doSummary();
		doReset();
	}
	
	@Command
	public void doExport() {
		try {
			List<Tinvoice> objList = oDao.listNative(filter, "tinvoicepk desc");
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
				datamap.put(1, new Object[] { "No", "Tipe Tagihan", "Nama", "Provoinsi", "Cabang", "Kode Tagihan", "Tanggal Tagihan", "Nominal Tagihan", "Tanggal Jatuh Tempo", 
						"Nomor VA", "Keterangan", "Status", 
						"Tanggal Bayar", "No Ref Bayar" });
				no = 2;
				for (Tinvoice data : objList) {
					datamap.put(no,
							new Object[] { no - 1, AppData.getInvoiceType(data.getInvoicetype()), data.getTanggota().getNama(), data.getTanggota().getMcabang().getMprov().getProvname(), data.getTanggota().getMcabang().getCabang(),  
									data.getInvoiceno(), datelocalFormatter.format(data.getInvoicedate()), data.getInvoiceamount().doubleValue(), 
									datelocalFormatter.format(data.getInvoiceduedate()), data.getVano(), data.getInvoicedesc(), data.getIspaid().equals("Y") ? "Sudah Dibayar" : "Belum Dibayar", 
									data.getPaidtime() != null ? datelocalFormatter.format(data.getPaidtime()) : "",
									data.getPaidrefno() != null ? data.getPaidrefno() : "" });
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
				String filename = "HAKLI_INVOICE_" + new SimpleDateFormat("yyMMddHHmm").format(new Date()) + ".xlsx";
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
	
	@NotifyChange("*")
	public void doSummary() {
		try {
			filter = "CURRENT_DATE - INVOICEDUEDATE BETWEEN 0 AND 7 AND INVOICEDUEDATE >= DATE(NOW())";
			if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSPROVINSI)) {
				filter += " and mprovfk = " + oUser.getMcabang().getMprov().getMprovpk();
			} else if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN)) {
				filter += " and mcabangfk = " + oUser.getMcabang().getMcabangpk();
			}
			totaldue = oDao.pageCount(filter);
			totaldueamount = oDao.sumAmount(filter);
			filter = "ISPAID = 'N' AND INVOICEDUEDATE >= DATE(NOW())";
			if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSPROVINSI)) {
				filter += " and mprovfk = " + oUser.getMcabang().getMprov().getMprovpk();
			} else if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN)) {
				filter += " and mcabangfk = " + oUser.getMcabang().getMcabangpk();
			}
			totalunpaid = oDao.pageCount(filter);
			totalunpaidamount = oDao.sumAmount(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@NotifyChange("pageTotalSize")
	public void refreshModel(int activePage) {
		paging.setPageSize(AppUtils.PAGESIZE);
		model = new TinvoiceListModel(activePage, AppUtils.PAGESIZE, filter, "tinvoicepk desc");
		pageTotalSize = model.getTotalSize(filter);
		paging.setTotalSize(pageTotalSize);
		grid.setModel(model);
		
		doListSummary();
	}
	
	@Command
	@NotifyChange("*")
	public void doSearchDue() {
		doReset();
		gbSearch.setOpen(false);
		filter = "CURRENT_DATE - INVOICEDUEDATE BETWEEN 0 AND 7 AND INVOICEDUEDATE >= DATE(NOW())";
		if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSPROVINSI)) {
			filter += " and mprovfk = " + oUser.getMcabang().getMprov().getMprovpk();
		} else if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN)) {
			filter += " and mcabangfk = " + oUser.getMcabang().getMcabangpk();
		}
		
		refreshModel(pageStartNumber);
	}
	
	@Command
	@NotifyChange("*")
	public void doSearchUnpaid() {
		doReset();
		gbSearch.setOpen(false);
		filter = "ispaid = 'N' AND INVOICEDUEDATE >= DATE(NOW())";
		if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSPROVINSI)) {
			filter += " and mprovfk = " + oUser.getMcabang().getMprov().getMprovpk();
		} else if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN)) {
			filter += " and mcabangfk = " + oUser.getMcabang().getMcabangpk();
		}
		refreshModel(pageStartNumber);
	}
	
	@Command
	@NotifyChange("*")
	public void doSearch() {
		filter = "0=0";
		if (invoicetype != null)
			filter += " and invoicetype = '" + invoicetype + "'";
		if (vano != null  && vano.trim().length() > 0)
			filter += " and tinvoice.vano = '" + vano.trim() + "'";
		if (nama != null  && nama.trim().length() > 0)
			filter += " and upper(tanggota.nama) like '%" + nama.trim().toUpperCase() + "%'";
		if (invno != null  && invno.trim().length() > 0)
			filter += " and invoiceno = '" + invno.trim() + "'";
		if (invstatus != null  && invstatus.trim().length() > 0)
			filter += " and tinvoice.ispaid = '" + invstatus.trim() + "'";
		if (begindate != null && enddate != null) {
			filter += " and invoicedate between '" + dateFormatter.format(begindate) + "' and '" + dateFormatter.format(enddate) + "'";
		}
		
		if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSPROVINSI)) {
			filter += " and mprovfk = " + oUser.getMcabang().getMprov().getMprovpk();
		} else if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN)) {
			filter += " and mcabangfk = " + oUser.getMcabang().getMcabangpk();
		}
		
		refreshModel(pageStartNumber);
	}
	
	
	@Command
	@NotifyChange("*")
	public void doListSummary() {
		try {
			invamount = oDao.sumAmount(filter);
			paidamount = oDao.sumAmount(filter + " and ispaid = 'Y'");
			unpaidamount = oDao.sumAmount(filter + " and ispaid = 'N'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		
		invoicetype = null;
		invno = null;
		vano = null;
		nama = null;
		invstatus = null;
		
		Calendar cal = Calendar.getInstance();
		enddate = cal.getTime();
		cal.add(Calendar.MONTH, -1);
		begindate = cal.getTime();
		
		doSearch();
	}

	public int getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(int pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

	public String getInvoicetype() {
		return invoicetype;
	}

	public void setInvoicetype(String invoicetype) {
		this.invoicetype = invoicetype;
	}

	public String getInvno() {
		return invno;
	}

	public void setInvno(String invno) {
		this.invno = invno;
	}

	public String getInvstatus() {
		return invstatus;
	}

	public void setInvstatus(String invstatus) {
		this.invstatus = invstatus;
	}

	public String getVano() {
		return vano;
	}

	public void setVano(String vano) {
		this.vano = vano;
	}

	public Date getBegindate() {
		return begindate;
	}

	public void setBegindate(Date begindate) {
		this.begindate = begindate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Integer getTotalunpaid() {
		return totalunpaid;
	}

	public void setTotalunpaid(Integer totalunpaid) {
		this.totalunpaid = totalunpaid;
	}

	public BigDecimal getTotalunpaidamount() {
		return totalunpaidamount;
	}

	public void setTotalunpaidamount(BigDecimal totalunpaidamount) {
		this.totalunpaidamount = totalunpaidamount;
	}

	public Integer getTotaldue() {
		return totaldue;
	}

	public void setTotaldue(Integer totaldue) {
		this.totaldue = totaldue;
	}

	public BigDecimal getTotaldueamount() {
		return totaldueamount;
	}

	public void setTotaldueamount(BigDecimal totaldueamount) {
		this.totaldueamount = totaldueamount;
	}

	public BigDecimal getInvamount() {
		return invamount;
	}

	public void setInvamount(BigDecimal invamount) {
		this.invamount = invamount;
	}

	public BigDecimal getPaidamount() {
		return paidamount;
	}

	public void setPaidamount(BigDecimal paidamount) {
		this.paidamount = paidamount;
	}

	public BigDecimal getUnpaidamount() {
		return unpaidamount;
	}

	public void setUnpaidamount(BigDecimal unpaidamount) {
		this.unpaidamount = unpaidamount;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}
	
}
