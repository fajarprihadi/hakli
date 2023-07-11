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
public class BriapiFundTransferHandler implements InterruptableJob  {
	
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
			BriApiExt briapi = null;
			List<Tinvoice> listInvoice = invDao.listByFilter("ispaid = 'Y' and isdisburse = 'N'", "tinvoicepk");
			if (listInvoice.size() > 0) {
				bean = AppData.getBriapibean();
				briapi = new BriApiExt(bean);
				briapiToken = briapi.getToken();
				
				if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
					for (Tinvoice inv: listInvoice) {
						
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
