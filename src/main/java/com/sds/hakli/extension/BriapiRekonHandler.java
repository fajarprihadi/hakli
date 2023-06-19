package com.sds.hakli.extension;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.bean.CallbackBean;
import com.sds.hakli.dao.MsysparamDAO;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.BrivaReport;
import com.sds.hakli.pojo.BrivaReportResp;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;

@DisallowConcurrentExecution
public class BriapiRekonHandler implements InterruptableJob  {
	
	private BriapiBean bean;
	private BriApiToken briapiToken;
	private BrivaReportResp obj;
	private TinvoiceDAO invDao = new TinvoiceDAO();
	private MsysparamDAO sysparamDao = new MsysparamDAO();
	private SimpleDateFormat dateParserFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private Thread thread;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			bean = AppData.getBriapibean();
			
			BriApiExt briapi = new BriApiExt(bean);
			briapiToken = briapi.getToken();
			if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -3);
				Date enddate = cal.getTime();
				cal.setTime(new Date());
				do {
					String periodstart = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
					String periodend = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
					obj = briapi.getBrivaReport(briapiToken.getAccess_token(), periodstart, periodend);
					if (obj != null && obj.getStatus() != null && obj.getStatus()) {
						
						String callback_path = sysparamDao.getParamvalue(AppUtils.SYSPARAM_CALLBACK_PATH);
						String callback_url = sysparamDao.getParamvalue(AppUtils.SYSPARAM_CALLBACK_URL);
						String callback_token = sysparamDao.getParamvalue(AppUtils.SYSPARAM_BRIAPI_CONSUMER_KEY);
						String callback_secret = sysparamDao.getParamvalue(AppUtils.SYSPARAM_BRIAPI_CONSUMER_SECRET);
						
						for (BrivaReport report: obj.getData()) {
							 List<Tinvoice> oList = invDao.listByFilter("vano = '" + report.getBrivaNo() + report.getCustCode() + "' and ispaid = 'N'", "tinvoicepk desc");
							if (oList.size() > 0) {
								try {
									Date trxdatetime = dateParserFormatter.parse(report.getPaymentDate());
									
									Tinvoice inv = oList.get(0);
									
									CallbackBean data = new CallbackBean();
									data.setBrivaNo(inv.getVano());
									data.setBillAmount(String.valueOf(inv.getInvoiceamount()));
									data.setTransactionDateTime(dateFormatter.format(trxdatetime));
									data.setJournalSeq(report.getKeterangan());
									data.setTerminalId("1");
									new CallbackExt().notifPayment(data, callback_path, callback_url, callback_token, callback_secret);
									
								} catch (Exception e) {
									e.printStackTrace();
								}
							}	
						}	
					}
					
					cal.add(Calendar.DAY_OF_MONTH, -1);
				} while (cal.getTime().compareTo(enddate) > 0);
				
				
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
