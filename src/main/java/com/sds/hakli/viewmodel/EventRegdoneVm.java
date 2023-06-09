package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlNativeComponent;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Separator;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.dao.TeventregDAO;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.domain.Teventreg;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.extension.BriApiExt;
import com.sds.hakli.extension.MailHandler;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.BrivaCreateResp;
import com.sds.hakli.pojo.BrivaData;
import com.sds.hakli.utils.InvoiceGenerator;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class EventRegdoneVm {
	
	private Teventreg obj;
	
	private String processinfo;
	private String eventimg;
	
	@Wire
	private Div divInfo;
	@Wire
	private Button btInv;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Teventreg obj) {
		Selectors.wireComponents(view, this, false);
		try {
			this.obj = obj;
			if (obj == null) {
				Executions.sendRedirect("/view/event/eventinit.zul");
			} else {
				eventimg = AppUtils.PATH_EVENT + "/" + obj.getTevent().getEventimg();
				boolean isValid = true;
				if (obj.getIspaid().equals("Y")) {
					HtmlNativeComponent strong = new HtmlNativeComponent("strong");
					strong.appendChild(new Html("Proses pendaftaran dan pembayaran berhasil."));
					divInfo.appendChild(strong);
					divInfo.appendChild(new Separator());
				} else {
					if (obj.getVano() == null || obj.getVano().trim().length() == 0) {
						HtmlNativeComponent strong = new HtmlNativeComponent("strong");
						strong.appendChild(new Html("Anda telah mengisi data pendaftaran Anda, tapi terjadi kegagalan sistem saat proses pembuatan Virtual Account. Silakan klik tombol Re-Generate Virtual Account untuk membuat tagihan."));
						divInfo.appendChild(strong);
						divInfo.appendChild(new Separator());
						
						btInv.setVisible(true);
						isValid = false;
					} else {
						HtmlNativeComponent strong = new HtmlNativeComponent("strong");
						strong.appendChild(new Html("Anda telah mengisi data pendaftaran Anda. Selanjutnya silakan Anda menyelesaikan pembayaran tagihan melalui Virtual Account Bank BRI atas data berikut :"));
						divInfo.appendChild(strong);
						divInfo.appendChild(new Separator());
					}
				}
				
				if (isValid) {
					doInfo();
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void doInfo() {
		HtmlNativeComponent table = new HtmlNativeComponent("table");
		table.setClientAttribute("class", "table table-sm table-striped mb-0");
		HtmlNativeComponent tr = new HtmlNativeComponent("tr");
		HtmlNativeComponent td1 = new HtmlNativeComponent("td");
		td1.setClientAttribute("width", "50%");
		td1.setClientAttribute("align", "right");
		td1.appendChild(new Html("Nama :"));
		HtmlNativeComponent td2 = new HtmlNativeComponent("td");
		td2.appendChild(new Html(obj.getTanggota().getNama()));
		td2.setClientAttribute("align", "left");
		tr.appendChild(td1);
		tr.appendChild(td2);
		table.appendChild(tr);
		
		tr = new HtmlNativeComponent("tr");
		td1 = new HtmlNativeComponent("td");
		td1.appendChild(new Html("E-Mail :"));
		td1.setClientAttribute("align", "right");
		td2 = new HtmlNativeComponent("td");
		td2.setClientAttribute("align", "left");
		td2.appendChild(new Html(obj.getTanggota().getEmail()));
		tr.appendChild(td1);
		tr.appendChild(td2);
		table.appendChild(tr);
		
		tr = new HtmlNativeComponent("tr");
		td1 = new HtmlNativeComponent("td");
		td1.appendChild(new Html("Nomor Virtual Account :"));
		td1.setClientAttribute("align", "right");
		td2 = new HtmlNativeComponent("td");
		td2.setClientAttribute("align", "left");
		td2.appendChild(new Html(obj.getVano()));
		tr.appendChild(td1);
		tr.appendChild(td2);
		table.appendChild(tr);
		
		tr = new HtmlNativeComponent("tr");
		td1 = new HtmlNativeComponent("td");
		td1.appendChild(new Html("Nominal Tagihan :"));
		td1.setClientAttribute("align", "right");
		td2 = new HtmlNativeComponent("td");
		td2.setClientAttribute("align", "left");
		td2.appendChild(new Html("Rp. " + NumberFormat.getInstance().format(obj.getVaamount())));
		tr.appendChild(td1);
		tr.appendChild(td2);
		table.appendChild(tr);
		
		tr = new HtmlNativeComponent("tr");
		td1 = new HtmlNativeComponent("td");
		td1.appendChild(new Html("Tanggal Jatuh Tempo :"));
		td1.setClientAttribute("align", "right");
		td2 = new HtmlNativeComponent("td");
		td2.setClientAttribute("align", "left");
		td2.appendChild(new Html(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(obj.getVaexpdate())));
		tr.appendChild(td1);
		tr.appendChild(td2);
		table.appendChild(tr);
		
		tr = new HtmlNativeComponent("tr");
		td1 = new HtmlNativeComponent("td");
		td1.appendChild(new Html("Status Pembayaran :"));
		td1.setClientAttribute("align", "right");
		td2 = new HtmlNativeComponent("td");
		td2.setClientAttribute("align", "left");
		td2.appendChild(new Html(obj.getIspaid().equals("Y") ? "Sudah dibayar Tanggal " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(obj.getPaidat()) : "Belum dibayar"));
		tr.appendChild(td1);
		tr.appendChild(td2);
		table.appendChild(tr);
		
		divInfo.appendChild(table);
	}
	
	@Command
	public void doReInvoice() {
		TanggotaDAO anggotaDao = new TanggotaDAO();
		TeventregDAO eventregDao = new TeventregDAO();
		TinvoiceDAO invDao = new TinvoiceDAO();
		try {
			boolean isVaCreate = false;
			boolean isVaUpdate = false;
			if (obj.getTanggota().getVaevent() == null || obj.getTanggota().getVaevent().trim().length() == 0) {
				isVaCreate = true;
				isVaUpdate = true;
			} else {
				if (obj.getTanggota().getVaeventstatus() == 1) {
					isVaCreate = true;
					isVaUpdate = false;
				} else {
					isVaCreate = false;
				}
			}
			
			BriapiBean bean = AppData.getBriapibean();
			BriApiExt briapi = new BriApiExt(bean);
			BriApiToken briapiToken = briapi.getToken();
			
			Date vaexpdate = null;
			BrivaData briva = new BrivaData();
			if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
				Session session = StoreHibernateUtil.openSession();
				Transaction trx = session.beginTransaction();
				try {
					briva.setAmount(obj.getTevent().getEventprice().toString());
					briva.setInstitutionCode(bean.getBriva_institutioncode());
					briva.setBrivaNo(bean.getBriva_cid());
					
					String custcode_cabang = "0000" + obj.getTanggota().getMcabang().getKodecabang();
					String custcode = custcode_cabang.substring(custcode_cabang.length()-4, custcode_cabang.length());
					if (isVaCreate)
						briva.setCustCode(new TcounterengineDAO().getVaCounter(custcode));
					else briva.setCustCode(obj.getTanggota().getVaevent().substring(5));
					briva.setKeterangan(obj.getTevent().getEventname().trim().length() > 40 ? obj.getTevent().getEventname().substring(0, 40) : obj.getTevent().getEventname());
					briva.setNama(obj.getTanggota().getNama().trim().length() > 40 ? obj.getTanggota().getNama().trim().substring(0, 40) : obj.getTanggota().getNama().trim());
					Calendar cal = Calendar.getInstance();
					cal.setTime(obj.getTevent().getEventdate());
					vaexpdate = cal.getTime();
					briva.setExpiredDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vaexpdate));
					
					BrivaCreateResp brivaCreated = null;
					if (isVaCreate) {
						brivaCreated = briapi.createBriva(briapiToken.getAccess_token(), briva);
						if (brivaCreated != null && brivaCreated.getStatus() && isVaUpdate) {
							obj.getTanggota().setVaevent(briva.getBrivaNo() + briva.getCustCode());
							obj.getTanggota().setVaeventstatus(1);
							anggotaDao.save(session, obj.getTanggota());
						}
					} else {
						brivaCreated = briapi.updateDataBriva(briapiToken.getAccess_token(), briva);
						if (brivaCreated != null && brivaCreated.getStatus()) {
							obj.getTanggota().setVaeventstatus(1);
							anggotaDao.save(session, obj.getTanggota());
						}
					}
					
					if (brivaCreated != null && brivaCreated.getStatus()) {
						obj.setVano(briva.getBrivaNo() + briva.getCustCode());
						obj.setVaamount(obj.getTevent().getEventprice());
						obj.setVacreatedat(new Date());
						obj.setVaexpdate(vaexpdate);
						
						eventregDao.save(session, obj);
						
						Tinvoice inv = invDao.findByFilter("teventreg.teventregpk = " + obj.getTeventregpk());
						if (inv == null) {
							inv = new InvoiceGenerator().doInvoice(obj, obj.getVano(), AppUtils.INVOICETYPE_EVENT, obj.getTevent().getEventprice(), obj.getTevent().getEventname(), vaexpdate);
							inv.setTanggota(obj.getTanggota());
						} else {
							inv.setCreatetime(new Date());
							inv.setInvoiceamount(obj.getTevent().getEventprice());
							inv.setInvoicedate(new Date());
							inv.setInvoicedesc(obj.getTevent().getEventname());
							inv.setInvoiceduedate(vaexpdate);
							inv.setInvoicetype(AppUtils.INVOICETYPE_EVENT);
							inv.setVano(obj.getVano());
							inv.setIspaid("N");
						}
						
						new TinvoiceDAO().save(session, inv);
						
						trx.commit();
						
						divInfo.getChildren().clear();
						HtmlNativeComponent strong = new HtmlNativeComponent("strong");
						strong.appendChild(new Html("Proses Re-Generate Virtual Account berhasil."));
						divInfo.appendChild(strong);
						divInfo.appendChild(new Separator());
						doInfo();
						
						if (inv != null) {
							String bodymail_path = Executions.getCurrent().getDesktop().getWebApp()
									.getRealPath("/themes/mail/mailinv.html");
							new Thread(new MailHandler(inv, inv.getInvoicedesc(), inv.getTanggota().getEmail(), bodymail_path)).start();
						}
					} else {
						HtmlNativeComponent strong = new HtmlNativeComponent("strong");
						divInfo.appendChild(new HtmlNativeComponent("hr"));
						strong.appendChild(new Html("Pembuatan Nomor VA Gagal : " + brivaCreated.getErrDesc()));
						divInfo.appendChild(strong);
						divInfo.appendChild(new Separator());
					}
					
					btInv.setDisabled(true);
				} catch (Exception e) {
					trx.rollback();
					e.printStackTrace();
				} finally {
					session.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getProcessinfo() {
		return processinfo;
	}

	public void setProcessinfo(String processinfo) {
		this.processinfo = processinfo;
	}

	public String getEventimg() {
		return eventimg;
	}

	public void setEventimg(String eventimg) {
		this.eventimg = eventimg;
	}
	
	

}
