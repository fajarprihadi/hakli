package com.sds.hakli.viewmodel;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
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
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.extension.BriApiExt;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.BrivaReport;
import com.sds.hakli.pojo.BrivaReportResp;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;

public class VaReportVm {
	
	private BrivaReportResp obj;
	private Date startperiod;
	private Date endperiod;
	private int pageTotalSize;
	
	private BriapiBean bean;
	
	@Wire
	private Groupbox gbResult;
	@Wire
	private Grid grid;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
		try {
			bean = AppData.getBriapibean();
			
			grid.setRowRenderer(new RowRenderer<BrivaReport>() {

				@Override
				public void render(Row row, BrivaReport data, int index) throws Exception {
					row.getChildren().add(new Label(String.valueOf(index + 1)));
					row.getChildren().add(new Label(data.getBrivaNo() + data.getCustCode()));
					row.getChildren().add(new Label(data.getNama()));
					row.getChildren().add(new Label(data.getAmount()));
					row.getChildren().add(new Label(data.getKeterangan()));
					row.getChildren().add(new Label(data.getPaymentDate()));
					row.getChildren().add(new Label(data.getTellerid()));
					row.getChildren().add(new Label(data.getNo_rek()));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Command
	@NotifyChange("*")
	public void doSubmit() {
		if (startperiod != null && endperiod != null) {
			try {
				BriApiExt briapi = new BriApiExt(bean);
				BriApiToken briapiToken = briapi.getToken();
				if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
					String startdate = new SimpleDateFormat("yyyyMMdd").format(startperiod);
					String enddate = new SimpleDateFormat("yyyyMMdd").format(endperiod);
					obj = briapi.getBrivaReport(briapiToken.getAccess_token(), startdate, enddate);
					if (obj != null && obj.getStatus() != null && obj.getStatus()) {
						gbResult.setVisible(true);	
						grid.setModel(new ListModelList<>(obj.getData()));
						pageTotalSize = obj.getData().size();
					} else 
						Messagebox.show("Laporan tidak tersedia", WebApps.getCurrent().getAppName(), Messagebox.OK,
							Messagebox.INFORMATION);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.ERROR);
			}
		} else {
			Messagebox.show("Silahkan masukkan periode tanggal laporan", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		gbResult.setVisible(false);
		startperiod = null;
		endperiod = null;
		pageTotalSize = 0;
	}
	
	@Command
	public void doExport() {
		try {
			if (obj != null && obj.getData().size() > 0) {
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
				org.apache.poi.ss.usermodel.Row row = null;
				Cell cell = null;
				rownum++;
				Map<Integer, Object[]> datamap = new TreeMap<Integer, Object[]>();
				datamap.put(1, new Object[] { "No", "Nomor VA", "Nama", "Tagihan", "Keterangan", "Waktu Bayar", "Teller Id", "No Rekening" });
				no = 2;
				for (BrivaReport data: obj.getData()) {
					datamap.put(no,
							new Object[] { no - 1, data.getBrivaNo() + data.getCustCode(), data.getNama(), data.getAmount(), data.getKeterangan(), data.getPaymentDate(), 
									data.getTellerid(), data.getNo_rek()
							});
					no++;
				}
				
				Set<Integer> keyset = datamap.keySet();
				for (Integer key : keyset) {
					row = sheet.createRow(rownum++);
					Object[] objArr = datamap.get(key);
					cellnum = 0;
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

				String path = Executions.getCurrent().getDesktop().getWebApp()
						.getRealPath(AppUtils.PATH_REPORT);
				String filename = "HAKLI_REPORTVA_" + new SimpleDateFormat("yyMMddHHmm").format(new Date()) + ".xlsx";
				FileOutputStream out = new FileOutputStream(new File(path + "/" + filename));
				workbook.write(out);
				out.close();
				workbook.close();

				Filedownload.save(new File(path + "/" + filename),
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			} else {
				Messagebox.show("Tidak ada data laporan", WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.INFORMATION);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Date getStartperiod() {
		return startperiod;
	}

	public void setStartperiod(Date startperiod) {
		this.startperiod = startperiod;
	}

	public Date getEndperiod() {
		return endperiod;
	}

	public void setEndperiod(Date endperiod) {
		this.endperiod = endperiod;
	}

	public BrivaReportResp getObj() {
		return obj;
	}

	public void setObj(BrivaReportResp obj) {
		this.obj = obj;
	}

	public int getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(int pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}
}
