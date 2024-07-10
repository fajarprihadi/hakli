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
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.dao.MfeeDAO;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.dao.TtransferDAO;
import com.sds.hakli.domain.Mfee;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.domain.Ttransfer;
import com.sds.hakli.domain.Vsumtransfer;
import com.sds.hakli.model.TtransferListModel;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;

public class DashboardPinbukSysVm {
	
	private Session session = Sessions.getCurrent();
	private Tanggota oUser;
	
	private TtransferListModel model;
	
	private TtransferDAO oDao = new TtransferDAO();
	
	private int pageStartNumber;
	private int pageTotalSize;
	private String filter;
	
	private BigDecimal totalpinbuk;
	private BigDecimal totalamount;
	private BigDecimal totaltransfered;
	private Vsumtransfer vsumtransfer;
	
	private String invoicetype;
	private String invno;
	private String nama;
	private String status;
	private String benefacc;	
	private Date begindate;
	private Date enddate;
	private String period;
	private BigDecimal totalpending;
	
	final BigDecimal SYSTEM_DISBURSE_PERCENT = new BigDecimal(0.35);
	private Map<String, Mfee> mapFee = new HashMap<>();
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat datelocalFormatter = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat datetimelocalFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	
	@Wire
	private Groupbox gbSearch;
	@Wire
	private Grid grid;
	@Wire
	private Paging paging;
	@Wire
	private Div divHead;
	@Wire
	private Textbox tbBenefAcc;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			oUser = (Tanggota) session.getAttribute("anggota");
			if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_ADMIN) || 
					oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSPUSAT)) {
				divHead.setVisible(true);
			} else {
				divHead.setVisible(false);
				tbBenefAcc.setReadonly(true);
			}
			
			for (Mfee fee : new MfeeDAO().listAll()) {
				mapFee.put(fee.getFeetype(), fee);
			}
			
			paging.addEventListener("onPaging", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					PagingEvent pe = (PagingEvent) event;
					pageStartNumber = pe.getActivePage();
					refreshModel(pageStartNumber);
				}
			});
			
			grid.setRowRenderer(new RowRenderer<Ttransfer>() {

				@Override
				public void render(Row row, Ttransfer data, int index) throws Exception {
					row.getChildren().add(new Label(String.valueOf((AppUtils.PAGESIZE * pageStartNumber) + index + 1)));
					row.getChildren().add(new Label(AppData.getInvoiceType(data.getTinvoice().getInvoicetype())));
					A aNama = new A(data.getTinvoice().getTanggota().getNama());
					aNama.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("obj", data.getTinvoice().getTanggota());
							map.put("acttype", "view");
							Window win = (Window) Executions
									.createComponents("/view/anggota/anggotaedit.zul", null, map);
							win.setWidth("98%");
							win.setClosable(true);
							win.doModal();
						}
					});
					row.getChildren().add(aNama);
					row.getChildren().add(new Label(data.getTinvoice().getInvoiceno()));
					row.getChildren().add(new Label(datetimelocalFormatter.format(data.getTinvoice().getPaidtime())));
					row.getChildren().add(new Label(data.getNoreferral()));
					row.getChildren().add(new Label(data.getBenefacc()));
					row.getChildren().add(new Label(data.getBenefname()));
					row.getChildren().add(new Label(data.getBenefbankcode()));
					row.getChildren().add(new Label(data.getTrfto() + " - " + data.getTrftoname()));
					row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getAmount())));					
					row.getChildren().add(new Label(datetimelocalFormatter.format(data.getTrxtime())));
					row.getChildren().add(new Label(data.getResponsecode() != null && data.getResponsecode().equals("0200") ? data.getResponsedesc() : data.getErrordesc()));
				}
			
			});
			
			doReset();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Command
	public void doExport() {
		try {
			List<Ttransfer> objList = oDao.listNative(filter, "ttransferpk desc");
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
				datamap.put(1, new Object[] { "No", "Tipe Tagihan", "Nama", "Kode Tagihan", "Tanggal Bayar", "No Referral", "No Rekening Tujuan", 
						"Nama Rekening Tujuan", "Bank Tujuan", "Tingkat Tujuan", 
						"Nominal", "Waktu", "Keterangan" });
				no = 2;
				for (Ttransfer data : objList) {
					datamap.put(no,
							new Object[] { no - 1, AppData.getInvoiceType(data.getTinvoice().getInvoicetype()), data.getTinvoice().getTanggota().getNama(), 
									data.getTinvoice().getInvoiceno(), datetimelocalFormatter.format(data.getTinvoice().getPaidtime()), data.getNoreferral(), 
									data.getBenefacc() , data.getBenefname(), data.getBenefbankcode(), data.getTrfto() + " - " + data.getTrftoname(), 
									data.getAmount().doubleValue(), datetimelocalFormatter.format(data.getTrxtime()),
									data.getResponsecode() != null && data.getResponsecode().equals("0200") ? data.getResponsedesc() : data.getErrordesc() });
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
				String filename = "HAKLI_DISBURSE_" + new SimpleDateFormat("yyMMddHHmm").format(new Date()) + ".xlsx";
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
	public void refreshModel(int activePage) {
		paging.setPageSize(AppUtils.PAGESIZE);
		model = new TtransferListModel(activePage, AppUtils.PAGESIZE, filter, "ttransferpk desc");
		pageTotalSize = model.getTotalSize(filter);
		paging.setTotalSize(pageTotalSize);
		grid.setModel(model);
		
		doListSummary();
	}
	
	@Command
	@NotifyChange("*")
	public void doSearch() {
		filter = "trfto = 'SYS'";
		
		if (begindate == null || enddate == null) {
			Calendar cal = Calendar.getInstance();
			enddate = cal.getTime();
			cal.add(Calendar.MONTH, -1);
			begindate = cal.getTime();	 
		}
		
		if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSPROVINSI)) {
			benefacc = oUser.getMcabang().getMprov().getAccno();
		} else if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN)) {
			benefacc = oUser.getMcabang().getAccno();
		}
		
		if (invoicetype != null)
			filter += " and invoicetype = '" + invoicetype + "'";
		if (nama != null  && nama.trim().length() > 0)
			filter += " and upper(tanggota.nama) like '%" + nama.trim().toUpperCase() + "%'";
		if (benefacc != null  && benefacc.trim().length() > 0)
			filter += " and benefacc = '" + benefacc.trim() + "'";
		if (invno != null  && invno.trim().length() > 0)
			filter += " and invoiceno = '" + invno.trim() + "'";
		if (status != null  && status.trim().length() > 0) {
			if (status.equals("Y"))
				filter += " and responsecode in ('0200', '2008100')";
			else filter += " and responsecode != '0200' and responsecode != '2008100'";
		}
		if (begindate != null && enddate != null) {
			filter += " and date(trxtime) between '" + dateFormatter.format(begindate) + "' and '" + dateFormatter.format(enddate) + "'";
		}
		period = "Periode Pemindahbukuan " + datelocalFormatter.format(begindate) + " s/d " + datelocalFormatter.format(enddate);
		
		pageStartNumber = 0;
		refreshModel(pageStartNumber);
	}
	
	
	@Command
	@NotifyChange("*")
	public void doListSummary() {
		try {
			String filterperiod = "trfto = 'SYS' and date(trxtime) between '" + dateFormatter.format(begindate) + "' and '" + dateFormatter.format(enddate) + "' and responsecode in ('0200', '2008100')";
			totalpinbuk = oDao.sumAmount(filterperiod);
			totalamount = oDao.sumAmount(filter);
			vsumtransfer = oDao.sumTransfer(filter);
			
			List<Tinvoice> listInvoice = new TinvoiceDAO().listSysPendingDisburse(0);
			totalpending = new BigDecimal(0);
			BigDecimal sysamount = new BigDecimal(0);
			for (Tinvoice inv : listInvoice) {
				if (inv.getInvoicetype().equals(AppUtils.INVOICETYPE_EVENT)) {
					sysamount = inv.getPaidamount().subtract(inv.getTeventreg().getTevent().getFeeprov()).subtract(inv.getTeventreg().getTevent().getFeekab());
					sysamount = sysamount.multiply(SYSTEM_DISBURSE_PERCENT);
					totalpending = totalpending.add(sysamount);
				} else if (inv.getInvoicetype().equals(AppUtils.INVOICETYPE_IURAN)) {
					Mfee fee = mapFee.get(inv.getInvoicetype());
					sysamount = (fee.getFeepusat().multiply(SYSTEM_DISBURSE_PERCENT)).multiply(new BigDecimal(inv.getInvoiceqty()));
					totalpending = totalpending.add(sysamount);
				} else if (inv.getInvoicetype().equals(AppUtils.INVOICETYPE_BORANG)) {
					sysamount = inv.getPaidamount().subtract(inv.getProvamount()).subtract(inv.getKabamount());
					sysamount = sysamount.multiply(SYSTEM_DISBURSE_PERCENT);
					totalpending = totalpending.add(sysamount);
				} else {
					Mfee fee = mapFee.get(inv.getInvoicetype());
					sysamount = fee.getFeepusat().multiply(SYSTEM_DISBURSE_PERCENT);
					totalpending = totalpending.add(sysamount);
				}	
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {		
		invoicetype = null;
		invno = null;
		nama = null;
		benefacc = null;
		status = "Y";
		
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

	public BigDecimal getTotalpinbuk() {
		return totalpinbuk;
	}

	public void setTotalpinbuk(BigDecimal totalpinbuk) {
		this.totalpinbuk = totalpinbuk;
	}

	public String getBenefacc() {
		return benefacc;
	}

	public void setBenefacc(String benefacc) {
		this.benefacc = benefacc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public BigDecimal getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(BigDecimal totalamount) {
		this.totalamount = totalamount;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public BigDecimal getTotaltransfered() {
		return totaltransfered;
	}

	public void setTotaltransfered(BigDecimal totaltransfered) {
		this.totaltransfered = totaltransfered;
	}

	public Vsumtransfer getVsumtransfer() {
		return vsumtransfer;
	}

	public void setVsumtransfer(Vsumtransfer vsumtransfer) {
		this.vsumtransfer = vsumtransfer;
	}

	public BigDecimal getTotalpending() {
		return totalpending;
	}

	public void setTotalpending(BigDecimal totalpending) {
		this.totalpending = totalpending;
	}

	
}
