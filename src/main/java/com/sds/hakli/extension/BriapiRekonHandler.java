package com.sds.hakli.extension;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TeventregDAO;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Teventreg;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.BrivaReport;
import com.sds.hakli.pojo.BrivaReportResp;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

@DisallowConcurrentExecution
public class BriapiRekonHandler implements InterruptableJob  {
	
	private BriapiBean bean;
	private BriApiToken briapiToken;
	private BrivaReportResp obj;
	private TinvoiceDAO invDao = new TinvoiceDAO();
	private TanggotaDAO anggotaDao = new TanggotaDAO();
	private TeventregDAO eventregDao = new TeventregDAO();
	
	private Thread thread;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			bean = AppData.getBriapibean();
			
			BriApiExt briapi = new BriApiExt(bean);
			briapiToken = briapi.getToken();
			if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -1);
				String period = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
				obj = briapi.getBrivaReport(briapiToken.getAccess_token(), period, period);
				if (obj != null && obj.getStatus() != null && obj.getStatus()) {
					Session session = null;
					Transaction trx = null;
					for (BrivaReport report: obj.getData()) {
						 List<Tinvoice> oList = invDao.listByFilter("vano = '" + report.getBrivaNo() + report.getCustCode() + "' and ispaid = 'N'", "tinvoicepk desc");
						if (oList.size() > 0) {
							Tinvoice inv = oList.get(0);
							inv.setIspaid("Y");
							inv.setPaidtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(report.getPaymentDate()));
							inv.setPaidamount(new BigDecimal(report.getAmount()));
							inv.setVaterminalid(report.getTellerid());
							inv.setVanotiftime(new Date());
							
							session = StoreHibernateUtil.openSession();
							trx = session.beginTransaction();
							try {
								invDao.save(session, inv);
								
								if (inv.getInvoicetype().equals(AppUtils.INVOICETYPE_REG) || inv.getInvoicetype().equals(AppUtils.INVOICETYPE_IURAN)) {
									Tanggota anggota = inv.getTanggota();
									if (anggota.getVareg().equals(inv.getVano()) || inv.getInvoiceduedate().compareTo(new Date()) < 0) {
										anggota.setVaregstatus(0);
										anggotaDao.save(session, anggota);
									} 
								} else if (inv.getInvoicetype().equals(AppUtils.INVOICETYPE_EVENT)) {
									Teventreg eventreg = inv.getTeventreg();
									eventreg.setIspaid("Y");
									eventreg.setPaidamount(inv.getPaidamount());
									eventreg.setPaidat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(report.getPaymentDate()));
									eventreg.setVaterminalid(inv.getVaterminalid());
									eventreg.setVanotiftime(new Date());
									eventregDao.save(session, eventreg);
									
									Tanggota anggota = inv.getTanggota();
									if (anggota.getVaevent().equals(inv.getVano()) || inv.getInvoiceduedate().compareTo(new Date()) < 0) {
										anggota.setVaeventstatus(0);
										anggotaDao.save(session, anggota);
									} 
								}
								
								trx.commit();
							} catch (Exception e) {
								e.printStackTrace();
								trx.rollback();
							} finally {
								session.close();
							}
						}	
					}	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new UnableToInterruptJobException(e);
        } finally {
            // ... do cleanup
        }
		
	}

}
