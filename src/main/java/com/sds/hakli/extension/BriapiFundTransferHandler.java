package com.sds.hakli.extension;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.bean.CallbackBean;
import com.sds.hakli.dao.MfeeDAO;
import com.sds.hakli.dao.MsysparamDAO;
import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.dao.TtransferDAO;
import com.sds.hakli.domain.Mfee;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.domain.Ttransfer;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.BrivaReport;
import com.sds.hakli.pojo.BrivaReportResp;
import com.sds.hakli.pojo.FundInqReq;
import com.sds.hakli.pojo.FundInqResp;
import com.sds.hakli.pojo.FundTrfReq;
import com.sds.hakli.pojo.FundTrfResp;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

@DisallowConcurrentExecution
public class BriapiFundTransferHandler implements InterruptableJob  {
	
	private BriapiBean bean;
	private BriApiToken briapiToken;
	private TinvoiceDAO invDao = new TinvoiceDAO();
	private MsysparamDAO sysparamDao = new MsysparamDAO();
	private TtransferDAO trfDao = new TtransferDAO();
	private TcounterengineDAO counterDao = new TcounterengineDAO();
	private SimpleDateFormat dateLocalFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	private Map<String, Mfee> mapFee = new HashMap<>();
	private BriApiExt briapi;
	private String sourceacc;
	
	private Thread thread;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			List<Tinvoice> listInvoice = invDao.listByFilter("ispaid = 'Y' and (istrfprov = 'N' or istrfkab = 'N')", "tinvoicepk");
			//List<Tinvoice> listInvoice = invDao.listByFilter("tinvoicepk = 1021", "tinvoicepk");
			if (listInvoice.size() > 0) {
				sourceacc = sysparamDao.getParamvalue(AppUtils.SYSPARAM_BANK_ACCNO);
				if (sourceacc != null)
					sourceacc = sourceacc.trim();
				
				bean = AppData.getBriapibean();
				briapi = new BriApiExt(bean);
				briapiToken = briapi.getTokenFundTransfer();
				
				for (Mfee fee: new MfeeDAO().listAll()) {
					mapFee.put(fee.getFeetype(), fee);
				}
				
				if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
					for (Tinvoice inv: listInvoice) {
						doFundTransfer(inv, "PROV");
						
//						if (inv.getIstrfprov().equals("N"))
//							doFundTransfer(inv, "PROV");
//						if (inv.getIstrfkab().equals("N"))
//							doFundTransfer(inv, "KAB");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void doFundTransfer(Tinvoice inv, String beneftype) throws Exception {
		try {
			Ttransfer trf = new Ttransfer();
			trf.setTinvoice(inv);
			trf.setTrfto(beneftype);
			if (inv.getInvoicetype().equals(AppUtils.INVOICETYPE_EVENT)) {
				if (beneftype.equals("PROV"))
					trf.setAmount(inv.getTeventreg().getTevent().getFeeprov());
				else if (beneftype.equals("KAB"))
					trf.setAmount(inv.getTeventreg().getTevent().getFeekab());
			} else {
				Mfee fee = mapFee.get(inv.getInvoicetype());
				if (beneftype.equals("PROV"))
					trf.setAmount(fee.getFeeprov());
				else if (beneftype.equals("KAB"))
					trf.setAmount(fee.getFeekab());
			}
			
			trf.setSourceacc(sourceacc);
			trf.setFeetype("BEN");
			//trf.setNoreferral(counterDao.generateSeqnum());
			trf.setNoreferral("99999999999999999918");
			//trf.setNoreferral("99999999999999999993");
			trf.setTrxtime(new Date());
			if (beneftype.equals("PROV")) {
				trf.setBenefacc(inv.getTanggota().getMcabang().getMprov().getAccno());
				trf.setBenefbankcode(inv.getTanggota().getMcabang().getMprov().getBankcode());
				trf.setBenefname(inv.getTanggota().getMcabang().getMprov().getAccname());
				trf.setRemark(trf.getNoreferral() + "P" + inv.getInvoicetype() + " " + inv.getInvoiceno());
			} else if (beneftype.equals("KAB")) {
				trf.setBenefacc(inv.getTanggota().getMcabang().getAccno());
				trf.setBenefbankcode(inv.getTanggota().getMcabang().getBankcode());
				trf.setBenefname(inv.getTanggota().getMcabang().getAccname());
				trf.setRemark(trf.getNoreferral() + "K" + inv.getInvoicetype() + " " + inv.getInvoiceno());
			}
			
			FundInqReq inqReq = new FundInqReq();
			inqReq.setSourceAccount(trf.getSourceacc());
			inqReq.setBeneficiaryAccount(trf.getBenefacc());
			//inqReq.setSourceAccount("888801000157508");
			//inqReq.setBeneficiaryAccount("888809999999918");
			FundInqResp fundInq = briapi.fundInq(briapiToken.getAccess_token(), inqReq);
			if (fundInq != null && fundInq.getResponseCode() != null && fundInq.getResponseCode().equals("0100")) {
				
				//trf.setSourceacc("888801000003301");
				//trf.setBenefacc("888809999999993");
				
				FundTrfReq trfReq = new FundTrfReq();
				trfReq.setSourceAccount(trf.getSourceacc());
				trfReq.setBeneficiaryAccount(trf.getBenefacc());
				trfReq.setAmount(trf.getAmount() + ".00");
				trfReq.setFeeType(trf.getFeetype());
				trfReq.setNoReferral(trf.getNoreferral());
				trfReq.setRemark(trf.getRemark());
				trfReq.setTransactionDateTime(dateLocalFormatter.format(trf.getTrxtime()));
				
				FundTrfResp fundTrf = briapi.fundTrf(briapiToken.getAccess_token(), trfReq);
				//if (fundTrf != null && fundTrf.getResponseCode() != null && fundTrf.getResponseCode().equals("0200")) {
				if (fundTrf != null) {
					trf.setResponsecode(fundTrf.getResponseCode());
					trf.setJournalseq(fundTrf.getJournalSeq());
					trf.setResponsedesc(fundTrf.getResponseDescription());
					trf.setErrordesc(fundTrf.getErrorDescription());
					
					if (beneftype.equals("PROV")) {
						inv.setIstrfprov("Y");
						inv.setTrfprovtime(new Date());
					} else if (beneftype.equals("KAB")) {
						inv.setIstrfkab("Y");
						inv.setTrfkabtime(new Date());
					}
					
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						trfDao.save(session, trf);
						invDao.save(session, inv);
						trx.commit();
					} catch (Exception e) {
						trx.rollback();
					} finally {
						session.close();
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
