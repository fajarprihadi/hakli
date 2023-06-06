package com.sds.hakli.viewmodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TeventregDAO;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.extension.BriApiExt;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.BrivaDataUpdate;
import com.sds.hakli.pojo.BrivaReport;
import com.sds.hakli.pojo.BrivaReportResp;
import com.sds.hakli.pojo.BrivaUpdateResp;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class VaUpdateVm {
	
	private TanggotaDAO anggotaDao = new TanggotaDAO();
	private TinvoiceDAO invDao = new TinvoiceDAO();
	private TeventregDAO eventregDao = new TeventregDAO();
	
	private BrivaReportResp obj;
	private BriApiToken briapiToken;
	private String vano;
	private Date date;
	private Tinvoice objInv;
	
	private BriapiBean bean;
	
	@Wire
	private Groupbox gbResult;
	@Wire
	private Grid grid;
	@Wire
	private Button btUpdate;
	
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
		if (date != null && vano != null && vano.trim().length() > 5) {
			try {
				//objForm = eventregDao.findByFilter("vano = '" + vano.trim() + "' and ispaid = 'N'");
				objInv = invDao.findByFilter("vano = '" + vano.trim() + "' and ispaid = 'N'");
				if (objInv == null) {
					Messagebox.show("Nomor VA " + vano.trim() + " tidak ditemukan pada sistem" , WebApps.getCurrent().getAppName(), Messagebox.OK,
							Messagebox.EXCLAMATION);
//				} else if (objForm.getIspaid().equals("Y")) {
//					Messagebox.show("Nomor VA " + vano + " tidak dapat dilakukan update status dikarenakan status sudah terbayar pada tanggal " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(objForm.getPaidat()), WebApps.getCurrent().getAppName(), Messagebox.OK,
//							Messagebox.EXCLAMATION);
				} else {
					BriApiExt briapi = new BriApiExt(bean);
					briapiToken = briapi.getToken();
					if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
						String period = new SimpleDateFormat("yyyyMMdd").format(date);
						obj = briapi.getBrivaReport(briapiToken.getAccess_token(), period, period);
						if (obj != null && obj.getStatus() != null && obj.getStatus()) {
							List<BrivaReport> objList = new ArrayList<>();
							for (BrivaReport report: obj.getData()) {
								if ((report.getBrivaNo() + report.getCustCode()).equals(vano.trim())) {
									objList.add(report);
								}
							}
							
							if (objList.size() > 0) {
								grid.setModel(new ListModelList<>(objList));
								gbResult.setVisible(true);
							} else {
								Messagebox.show("Data transaksi atas nomor VA " + vano + " tidak ditemukan", WebApps.getCurrent().getAppName(), Messagebox.OK,
										Messagebox.INFORMATION);
							}
							
						} else 
							Messagebox.show("Tidak ada data transaksi tanggal " + new SimpleDateFormat("dd-MM-yyyy").format(date), WebApps.getCurrent().getAppName(), Messagebox.OK,
								Messagebox.INFORMATION);
					}
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.ERROR);
			}
		} else {
			Messagebox.show("Silahkan masukkan nomor VA dan tanggal transaksi", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		gbResult.setVisible(false);
		btUpdate.setDisabled(false);
		date = null;
		vano = null;
	}
	
	@Command
	@NotifyChange("*")
	public void doUpdate() {
		Messagebox.show("Anda ingin melakukan update status VA?", "Confirm Dialog", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

			@Override
			public void onEvent(Event event)
					throws Exception {
				if (event.getName().equals("onOK")) {
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						BriApiExt briapi = new BriApiExt(bean);
						
						BrivaDataUpdate data = new BrivaDataUpdate();
						data.setBrivaNo(bean.getBriva_cid());
						data.setCustCode(vano.substring(5));
						data.setInstitutionCode(bean.getBriva_institutioncode());
						data.setStatusBayar("Y");
						BrivaUpdateResp objResp = briapi.updateBriva(briapiToken.getAccess_token(), data);
						if (objResp != null && objResp.getStatus() != null && objResp.getStatus()) {
							
							objInv.setIspaid("Y");
							invDao.save(session, objInv);
							
							if (objInv.getInvoicetype().equals(AppUtils.INVOICETYPE_EVENT)) {
								objInv.getTeventreg().setIspaid("Y");
								eventregDao.save(session, objInv.getTeventreg());
								
								if (objInv.getTanggota().getVaevent().equals(data.getBrivaNo() + data.getCustCode())) {
									objInv.getTanggota().setVaeventstatus(0);
									anggotaDao.save(session, objInv.getTanggota());
								}
							} else {
								if (objInv.getTanggota().getVareg().equals(data.getBrivaNo() + data.getCustCode())) {
									objInv.getTanggota().setVaregstatus(0);
									anggotaDao.save(session, objInv.getTanggota());
								}
							}
							
							trx.commit();
							
							btUpdate.setDisabled(true);
							Messagebox.show("Update status virtual account berhasil", WebApps.getCurrent().getAppName(), Messagebox.OK,
									Messagebox.INFORMATION);
						} else {
							Messagebox.show("Update status virtual account gagal", WebApps.getCurrent().getAppName(), Messagebox.OK,
									Messagebox.EXCLAMATION);
						}
					} catch (Exception e) {	
						Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
						e.printStackTrace();
					} finally {
						session.close();
					}
				} 									
			}
			
		});
		
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BrivaReportResp getObj() {
		return obj;
	}

	public void setObj(BrivaReportResp obj) {
		this.obj = obj;
	}

	public String getVano() {
		return vano;
	}

	public void setVano(String vano) {
		this.vano = vano;
	}
	
}
