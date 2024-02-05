package com.sds.hakli.viewmodel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindUtils;
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
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.dao.MchargeDAO;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.domain.Mcharge;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.extension.BriApiExt;
import com.sds.hakli.extension.MailHandler;
import com.sds.hakli.model.TinvoiceListModel;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.BrivaCreateResp;
import com.sds.hakli.pojo.BrivaData;
import com.sds.hakli.utils.InvoiceGenerator;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class PaymentVm {

	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota anggota;
	private TanggotaDAO anggotaDao = new TanggotaDAO();
	private MchargeDAO chargeDao = new MchargeDAO();
	private TinvoiceDAO invDao = new TinvoiceDAO();
	private List<Mcharge> objList;
	private List<Mcharge> oList;
	private Integer qty;
	private BigDecimal totalpayment = new BigDecimal(0);
	private BigDecimal amountbase;
	private String processinfo;
	
	private TinvoiceListModel model;
	
	private int pageStartNumber;
	private int pageTotalSize;
	private String filter;
	
	private Date periode;
	private String keterangan;
	
	private SimpleDateFormat datelocalFormatter = new SimpleDateFormat("dd-MM-yyyy");

	@Wire
	private Groupbox gbForm;
	@Wire
	private Grid gridCharge;
//	@Wire
//	private Combobox cbCharge;
	@Wire
	private Button btSave;
	@Wire
	private Div divProcessinfo;
	@Wire
	private Grid gridHist;
	@Wire
	private Paging paging;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			anggota = (Tanggota) zkSession.getAttribute("anggota");
			anggota = anggotaDao.findByPk(anggota.getTanggotapk());
			gridCharge.setRowRenderer(new RowRenderer<Mcharge>() {

				@Override
				public void render(Row row, Mcharge data, int index) throws Exception {
					row.getChildren().add(new Label(data.getChargedesc()));
					row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getChargeamount())));

					totalpayment = totalpayment.add(data.getChargeamount());
					BindUtils.postNotifyChange(PaymentVm.this, "totalpayment");
				}
			});

//			objList = chargeDao.listByFilter("chargetype = '" + AppUtils.CHARGETYPE_IURAN + "'", "isbase desc");
//			oList = objList;
//			amountbase = new BigDecimal(0);
//			for (Mcharge obj : objList) {
//				if (obj.getIsbase().equals("Y")) {
//					amountbase = obj.getChargeamount();
//					break;
//				}
//			}
//			
//			Calendar cal = Calendar.getInstance();
//			Calendar calNext = Calendar.getInstance();
//			if (anggota.getPeriodekta() == null) {
//				cal.setTime(new Date());
//				calNext.add(Calendar.MONTH, 6);
//			} else {
//				cal.setTime(anggota.getPeriodekta());
//			}
//			oList = new ArrayList<>();
//			for (Mcharge obj : objList) {
//				if (obj.getIsbase().equals("Y")) {
//					qty = 0;
//					BigDecimal totalbase = new BigDecimal(0);
//					while (cal.getTime().compareTo(calNext.getTime()) == -1) {
//						qty++;
//						totalbase = amountbase.multiply(new BigDecimal(qty));
//						cal.add(Calendar.MONTH, 6);
//					}
//					periode = cal.getTime();
//					keterangan = "Pembayaran Iuran Untuk " + (6 * qty) + " Bulan";
//					obj.setChargeamount(totalbase);
//					obj.setChargedesc(keterangan);
//				}
//				
//				oList.add(obj);
//			}
//
//			gridCharge.setModel(new ListModelList<>(objList));
			
			doCalInvoice();
			
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
			gridHist.setRowRenderer(new RowRenderer<Tinvoice>() {

				@Override
				public void render(Row row, Tinvoice data, int index) throws Exception {
					row.getChildren().add(new Label(String.valueOf((AppUtils.PAGESIZE * pageStartNumber) + index + 1)));
					row.getChildren().add(new Label(AppData.getInvoiceType(data.getInvoicetype())));
					row.getChildren().add(new Label(data.getInvoiceno()));
					row.getChildren().add(new Label(datelocalFormatter.format(data.getInvoicedate())));
					row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getInvoiceamount())));
					row.getChildren().add(new Label(datelocalFormatter.format(data.getInvoiceduedate())));
					row.getChildren().add(new Label(data.getVano()));
					row.getChildren().add(new Label(data.getInvoicedesc()));
					
					String status = "";
					Calendar cal = Calendar.getInstance();
					if (data.getIspaid().equals("Y")) {
						status = "Sudah Dibayar";
					} else {
						if (cal.getTime().compareTo(data.getInvoiceduedate()) > 0)
							status = "Tidak Terbayar";
						else status = "Belum Dibayar";
					}
					row.getChildren().add(new Label(status));
				}
			});
			
			refreshModel(pageStartNumber);
			
			List<Tinvoice> invList = invDao.listByFilter("tanggota.tanggotapk = '" + anggota.getTanggotapk() + "' and invoicetype = '" + AppUtils.INVOICETYPE_IURAN + "' and ispaid = 'N'", "tinvoicepk desc");
			if (invList.size() > 0) {
				if (invList.get(0).getInvoiceduedate().compareTo(new Date()) >= 0) {
					keterangan = "Anda tidak dapat melakukan generate tagihan baru dikarenakan Anda masih memiliki tagihan yang belum dibayar dengan Nomor VA " + invList.get(0).getVano() + " yang akan kadaluarsa pada tanggal" + datelocalFormatter.format(invList.get(0).getInvoiceduedate()) + ". \n Silahkan lihat tabel Riwayat Tagihan dihalaman bawah.";
					btSave.setDisabled(true);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doCalInvoice() {
		try {
			objList = chargeDao.listByFilter("chargetype = '" + AppUtils.CHARGETYPE_IURAN + "'", "isbase desc");
			amountbase = new BigDecimal(0);
			for (Mcharge obj : objList) {
				if (obj.getIsbase().equals("Y")) {
					amountbase = obj.getChargeamount();
					break;
				}
			}
			
			Calendar calCurrent = Calendar.getInstance();
			
			if (calCurrent.get(Calendar.MONTH) < 4) {
				calCurrent.set(Calendar.MONTH, 5);
				calCurrent.set(Calendar.DAY_OF_MONTH, 30);
			} else if (calCurrent.get(Calendar.MONTH) >= 4 && calCurrent.get(Calendar.MONTH) < 10) {
				calCurrent.set(Calendar.MONTH, 11);
				calCurrent.set(Calendar.DAY_OF_MONTH, 31);
			} else if (calCurrent.get(Calendar.MONTH) >= 10) {
				calCurrent.set(Calendar.MONTH, 5);
				calCurrent.set(Calendar.DAY_OF_MONTH, 30);
				calCurrent.add(Calendar.YEAR, 1);
			}
			calCurrent.set(Calendar.HOUR_OF_DAY, 0);
			calCurrent.set(Calendar.MINUTE, 0);
			calCurrent.set(Calendar.SECOND, 0);
			calCurrent.set(Calendar.MILLISECOND, 0);
			
			Calendar calPeriodeKta = Calendar.getInstance();
			calPeriodeKta.setTime(anggota.getPeriodekta());
			
			if (calPeriodeKta.get(Calendar.MONTH) <= 4) {
				calPeriodeKta.set(Calendar.MONTH, 11);
				calPeriodeKta.set(Calendar.DAY_OF_MONTH, 31);
				calPeriodeKta.add(Calendar.YEAR, -1);
			} else if (calPeriodeKta.get(Calendar.MONTH) > 5 && calPeriodeKta.get(Calendar.MONTH) <= 10) {
				calPeriodeKta.set(Calendar.MONTH, 5);
				calPeriodeKta.set(Calendar.DAY_OF_MONTH, 30);
			} 
			calPeriodeKta.set(Calendar.HOUR_OF_DAY, 0);
			calPeriodeKta.set(Calendar.MINUTE, 0);
			calPeriodeKta.set(Calendar.SECOND, 0);
			calPeriodeKta.set(Calendar.MILLISECOND, 0);
			
			if (calPeriodeKta.getTime().compareTo(calCurrent.getTime()) > 0) {
				periode = calCurrent.getTime();
				keterangan = "Anda sudah melakukan pembayaran Iuran Untuk periode iuran " + datelocalFormatter.format(calCurrent.getTime());
				btSave.setDisabled(true);
			} else {
				
				qty = 0;
				while (calPeriodeKta.getTime().compareTo(calCurrent.getTime()) == -1) {
					if (calPeriodeKta.get(Calendar.MONTH) == 11) {
						calPeriodeKta.set(Calendar.MONTH, 5);
						calPeriodeKta.set(Calendar.DAY_OF_MONTH, 30);
						calPeriodeKta.add(Calendar.YEAR, 1);
					} else {
						calPeriodeKta.set(Calendar.MONTH, 11);
						calPeriodeKta.set(Calendar.DAY_OF_MONTH, 31);
					}
					//calPeriodeKta.add(Calendar.MONTH, 6);
					qty++;
				}
				periode = calPeriodeKta.getTime();
				
				oList = new ArrayList<>();
				for (Mcharge obj : objList) {
					if (obj.getIsbase().equals("Y")) {
						BigDecimal totalbase = amountbase.multiply(new BigDecimal(qty));
						keterangan = "Pembayaran Iuran Untuk " + (6 * qty) + " Bulan";
						obj.setChargeamount(totalbase);
						obj.setChargedesc(keterangan);
					}
					oList.add(obj);
				}
				
				gridCharge.setModel(new ListModelList<>(objList));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@NotifyChange("pageTotalSize")
	public void refreshModel(int activePage) {
		paging.setPageSize(AppUtils.PAGESIZE);
		filter = "tanggotafk = " + anggota.getTanggotapk() + " and invoicetype = '" + AppUtils.INVOICETYPE_IURAN + "'";
		model = new TinvoiceListModel(activePage, AppUtils.PAGESIZE, filter, "tinvoicepk desc");
		pageTotalSize = model.getTotalSize(filter);
		paging.setTotalSize(pageTotalSize);
		gridHist.setModel(model);
	}

	@Command
	@NotifyChange("*")
	public void doSave() {
		if (totalpayment == null || totalpayment.compareTo(new BigDecimal(0)) <= 0) {
			Messagebox.show("Tidak ada data tagihan yang dapat di generate.", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.INFORMATION);
		} else {
			Messagebox.show(
					"Apakah data pembayaran yang anda input sudah benar? Jika sudah benar silahkan pilih OK untuk mengirim data tagihan ke email anda",
					"Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							if (event.getName().equals("onOK")) {
								Session session = StoreHibernateUtil.openSession();
								Transaction trx = null;
								try {
									BriapiBean bean = AppData.getBriapibean();
									BriApiExt briapi = new BriApiExt(bean);
									BriApiToken briapiToken = briapi.getToken();
									
									Date vaexpdate = null;
									BrivaData briva = new BrivaData();
									if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
										briva.setAmount(totalpayment.toString());
										briva.setInstitutionCode(bean.getBriva_institutioncode());
										briva.setBrivaNo(bean.getBriva_cid());
										
										String custcode_cabang = "0000" + anggota.getMcabang().getKodecabang();
										String custcode = custcode_cabang.substring(custcode_cabang.length()-4, custcode_cabang.length());
										briva.setCustCode(new TcounterengineDAO().getVaCounter());
										
//										Date startperiod = anggota.getPeriodekta() != null ? anggota.getPeriodekta() : new Date();
//										Calendar cal = Calendar.getInstance();
//										cal.setTime(startperiod);
//										cal.add(Calendar.MONTH, 6 * qty);
//										if (cal.get(Calendar.MONTH) == 5) {
//											cal.set(Calendar.DAY_OF_MONTH, 30);
//										} else if (cal.get(Calendar.MONTH) == 11) {
//											cal.set(Calendar.DAY_OF_MONTH, 31);
//										}
//										Date periodektanext = cal.getTime();
										String invdesc = "Iuran Periode " + datelocalFormatter.format(anggota.getPeriodekta()) + " s/d " + datelocalFormatter.format(periode);
										
										briva.setKeterangan(invdesc);
										briva.setNama(anggota.getNama().trim().length() > 40 ? anggota.getNama().trim().substring(0, 40) : anggota.getNama().trim());
										Calendar cal = Calendar.getInstance();
										cal.add(Calendar.DAY_OF_MONTH, 14);
										vaexpdate = cal.getTime();
										briva.setExpiredDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vaexpdate));
										
										BrivaCreateResp brivaCreated = briapi.createBriva(briapiToken.getAccess_token(), briva);
										if (brivaCreated != null && brivaCreated.getStatus()) {
											trx = session.beginTransaction();
											
											Tinvoice inv = new InvoiceGenerator().doInvoice(anggota, briva.getBrivaNo() + briva.getCustCode(), AppUtils.INVOICETYPE_IURAN, totalpayment, invdesc, vaexpdate);
											inv.setBaseamount(amountbase);
											invDao.save(session, inv);
											
											anggota.setPeriodektanext(periode);
											anggotaDao.save(session, anggota);
											
											trx.commit();
											
											btSave.setDisabled(true);
											
											String bodymail_path = Executions.getCurrent().getDesktop().getWebApp()
													.getRealPath("/themes/mail/mailinv.html");
											new Thread(new MailHandler(inv, inv.getInvoicedesc().trim(), inv.getTanggota().getEmail(), bodymail_path)).start();

											processinfo = "Proses generate pembayaran berhasil. Informasi permintaan pembayaran sudah dikirim ke e-mail anggota dengan Nomor VA "
													+ briva.getBrivaNo() + briva.getCustCode();
											divProcessinfo.setVisible(true);
											
											pageStartNumber = 0;
											refreshModel(pageStartNumber);
											
											BindUtils.postNotifyChange(PaymentVm.this, "*");
											
											gbForm.setOpen(false);
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
									Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
											Messagebox.ERROR);
								} finally {
									session.close();
								}
							}
						}
					});
		}
	}

	public BigDecimal getTotalpayment() {
		return totalpayment;
	}

	public void setTotalpayment(BigDecimal totalpayment) {
		this.totalpayment = totalpayment;
	}

	public String getProcessinfo() {
		return processinfo;
	}

	public void setProcessinfo(String processinfo) {
		this.processinfo = processinfo;
	}

	public Tanggota getAnggota() {
		return anggota;
	}

	public void setAnggota(Tanggota anggota) {
		this.anggota = anggota;
	}

	public int getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(int pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

	public Date getPeriode() {
		return periode;
	}

	public void setPeriode(Date periode) {
		this.periode = periode;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	
	
}
