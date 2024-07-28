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
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.dao.MchargeDAO;
import com.sds.hakli.dao.MfeeDAO;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.dao.Tp2kbbookDAO;
import com.sds.hakli.domain.Mcharge;
import com.sds.hakli.domain.Mfee;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.domain.Tp2kbbook;
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

public class P2KBEvalVm {

	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota anggota;
	private Tp2kbbookDAO p2kbbookDao = new Tp2kbbookDAO();
	private MchargeDAO chargeDao = new MchargeDAO();
	private TinvoiceDAO invDao = new TinvoiceDAO();
	private List<Mcharge> objList;
	private List<Mcharge> oList;
	private BigDecimal totalpayment = new BigDecimal(0);
	private BigDecimal amountbase;
	private String processinfo;

	private Date periode;
	private String keterangan;

	private ListModelList<Tp2kbbook> model;
	private Tp2kbbook p2kbbook;
	private boolean isUpdate;

	private SimpleDateFormat datelocalFormatter = new SimpleDateFormat("dd-MM-yyyy");

	@Wire
	private Window winP2kbEval;
	@Wire
	private Combobox cbPeriod;
	@Wire
	private Grid gridCharge;
	@Wire
	private Groupbox gbForm;
	@Wire
	private Button btSave;
	@Wire
	private Div divProcessinfo;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			anggota = (Tanggota) zkSession.getAttribute("anggota");
			
			try {
				SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
				if (anggota.getPeriodekta() != null) {
					Date d1 = sdformat.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					Date d2 = sdformat
							.parse(new SimpleDateFormat("yyyy-MM-dd").format(anggota.getPeriodekta()));
					
					Date d3 = anggota.getPeriodeborang() != null ? sdformat
							.parse(new SimpleDateFormat("yyyy-MM-dd").format(anggota.getPeriodeborang())) : null;

					if (d1.compareTo(d2) < 0 || (d3 != null && d1.compareTo(d3) < 0)) {
						
					} else {
						Window win = (Window) Executions.createComponents("/view/infoperiodekta.zul", null, null);
						win.setWidth("50%");
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
							@Override
							public void onEvent(Event event) throws Exception {
								winP2kbEval.getChildren().clear();
								winP2kbEval.setBorder(false);
								Executions.createComponents("/view/payment/payment.zul", winP2kbEval,
										null);
							}

						});
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			gridCharge.setRowRenderer(new RowRenderer<Mcharge>() {

				@Override
				public void render(Row row, Mcharge data, int index) throws Exception {
					row.getChildren().add(new Label(data.getChargedesc()));
					row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getChargeamount())));

					totalpayment = totalpayment.add(data.getChargeamount());
					BindUtils.postNotifyChange(P2KBEvalVm.this, "totalpayment");
				}
			});
			
			objList = chargeDao.listByFilter("chargetype = '" + AppUtils.CHARGETYPE_P2KB + "'", "isbase desc");
			gridCharge.setModel(new ListModelList<>(objList));
			doRefreshModel();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@NotifyChange("model")
	public void doRefreshModel() {
		try {
			cbPeriod.getChildren().clear();
			for (Tp2kbbook book: p2kbbookDao.listByFilter(
					"tanggota.tanggotapk = " + anggota.getTanggotapk() + " and status != 'C'", "tp2kbbookpk")) {
				Comboitem item = new Comboitem(datelocalFormatter.format(book.getTglmulai()) + " S/D " + datelocalFormatter.format(book.getTglakhir()));
				item.setValue(book);
				cbPeriod.appendChild(item);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("keterangan")
	public void doSelectPeriod(@BindingParam("p2kbbook") Tp2kbbook p2kbbook) {
		if (p2kbbook != null) {
			try {
				isUpdate = false;
				keterangan = "";
				btSave.setDisabled(true);
				
				if (p2kbbook.getTotalskp().compareTo(new BigDecimal(50)) < 0) {
					keterangan = "SKP ANDA BELUM TERCUKUPI. Ingin mengajukan Surat Keterangan Pemutakhiran dan Verifikasi Data Satuan Kredit Profesi? Silakan Generate Tagihan.";
				} else {
					keterangan = "";
				}
				
				Tinvoice inv = invDao.findByFilter("tp2kbbook.tp2kbbookpk = " + p2kbbook.getTp2kbbookpk());
				if (inv != null) {
					if (inv.getIspaid().equals("Y"))
						keterangan = "Anda sudah melakukan pembayaran dan pengajuan evaluasi Anda sedang dalam proses review oleh Komisi P2KB. Mohon ditunggu.";
					else if (inv.getIspaid().equals("N") && inv.getInvoiceduedate().compareTo(new Date()) >= 0) {
						keterangan = "Anda sudah melakukan generate tagihan dan tagihan Anda masih aktif sampai dengan " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(inv.getInvoiceduedate()) + " dengan Nomor VA " + inv.getVano() + ". Silakan segera lakukan pembayaran sebelum tanggal jatuh tempo tagihan.";
					} else if (inv.getIspaid().equals("N") && inv.getInvoiceduedate().compareTo(new Date()) < 0) {
						keterangan = "Tagihan Anda sudah kadaluarsa. Silakan Klik tombol Generate Tagihan untuk membuat tagihan baru.";
						isUpdate = true;
						btSave.setDisabled(false);
					}
				} else {
					btSave.setDisabled(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Command
	@NotifyChange("*")
	public void doSave() {
		if (p2kbbook == null) {
			Messagebox.show("Tidak ada data periode P2KB yang terisi", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.INFORMATION);
//		} else if (p2kbbook.getTotalskp().compareTo(new BigDecimal(50)) < 0) {
//			Messagebox.show("Anda belum dapat melakukan pengajuan evaluasi dikarenakan total nilai SKP Anda belum mencapai minimum kecukupan nilai SKP, yaitu 50 SKP.", WebApps.getCurrent().getAppName(), Messagebox.OK,
//					Messagebox.INFORMATION);
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
									BigDecimal provamount = new BigDecimal(0);
									BigDecimal kabamount = new BigDecimal(0);
									for (Mfee fee : new MfeeDAO().listAll()) {
										if (fee.getFeetype().equals(AppUtils.CHARGETYPE_P2KB)) {
											provamount = fee.getFeeprov();
											kabamount = fee.getFeekab();
											break;
										}
									}
									
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
										String custcode = custcode_cabang.substring(custcode_cabang.length() - 4,
												custcode_cabang.length());
										briva.setCustCode(new TcounterengineDAO().getVaCounter());

										String invdesc = "Evaluasi P2KB " + datelocalFormatter.format(p2kbbook.getTglmulai())
												+ " s/d " + datelocalFormatter.format(p2kbbook.getTglakhir());

										briva.setKeterangan(invdesc);
										briva.setNama(anggota.getNama().trim().length() > 40
												? anggota.getNama().trim().substring(0, 40)
												: anggota.getNama().trim());
										Calendar cal = Calendar.getInstance();
										cal.add(Calendar.DAY_OF_MONTH, 14);
										vaexpdate = cal.getTime();
										briva.setExpiredDate(
												new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vaexpdate));

										BrivaCreateResp brivaCreated = briapi.createBriva(briapiToken.getAccess_token(), briva);
										if (brivaCreated != null && brivaCreated.getStatus()) {
											trx = session.beginTransaction();
											
											Tinvoice inv = new InvoiceGenerator().doInvoice(p2kbbook,
													briva.getBrivaNo() + briva.getCustCode(),
													AppUtils.INVOICETYPE_P2KB, totalpayment, invdesc, vaexpdate);
											inv.setTanggota(anggota);
											inv.setInvoiceqty(1);
											inv.setBaseamount(totalpayment);
											inv.setProvamount(provamount);
											inv.setKabamount(kabamount);
											invDao.save(session, inv);
											
											p2kbbook.setStatus("R");
											p2kbbook.setVano(inv.getVano());
											p2kbbook.setVacreatedat(new Date());
											p2kbbook.setIspaid("N");
											p2kbbookDao.save(session, p2kbbook);

											trx.commit();

											btSave.setDisabled(true);

											String bodymail_path = Executions.getCurrent().getDesktop().getWebApp()
													.getRealPath("/themes/mail/mailinv.html");
											new Thread(new MailHandler(inv, inv.getInvoicedesc().trim(),
													inv.getTanggota().getEmail(), bodymail_path, null)).start();

											processinfo = "Proses generate pembayaran berhasil. Informasi permintaan pembayaran sudah dikirim ke e-mail anggota dengan Nomor VA "
													+ briva.getBrivaNo() + briva.getCustCode();
											divProcessinfo.setVisible(true);

											doRefreshModel();

											BindUtils.postNotifyChange(P2KBEvalVm.this, "*");

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

	public ListModelList<Tp2kbbook> getModel() {
		return model;
	}

	public void setModel(ListModelList<Tp2kbbook> model) {
		this.model = model;
	}

	public Tp2kbbook getP2kbbook() {
		return p2kbbook;
	}

	public void setP2kbbook(Tp2kbbook p2kbbook) {
		this.p2kbbook = p2kbbook;
	}

}
