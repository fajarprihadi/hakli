package com.sds.hakli.extension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.dao.MfeeDAO;
import com.sds.hakli.dao.MsysparamDAO;
import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.dao.TtransferDAO;
import com.sds.hakli.domain.Mfee;
import com.sds.hakli.domain.Msysparam;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.domain.Ttransfer;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.FundInqReq;
import com.sds.hakli.pojo.FundInqResp;
import com.sds.hakli.pojo.FundTrfReq;
import com.sds.hakli.pojo.FundTrfResp;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class SysFundTransferHandler implements Job {

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
	private BigDecimal disburse_min_balance = new BigDecimal(0);
	final BigDecimal SYSTEM_DISBURSE_PERCENT = new BigDecimal(0.35);
	final String BENEF_ACCNO = "187101000140569";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {		
		synchronized(this) {
			if (!AppData.isActiveSysFundTransfer) {
				System.out.println(new Date() + " System Fund Transfer Handler is Started...");
				AppData.isActiveSysFundTransfer = true;
				try {
					int disburse_limit = 1;
					Msysparam param = new MsysparamDAO()
							.findByCode("SYS_DISBURSE_LIMIT");
					if (param != null) {
						disburse_limit = Integer.parseInt(param.getParamvalue());
					}
					
					param = new MsysparamDAO()
							.findByCode("DISBURSE_MIN_BALANCE");
					if (param != null) {
						disburse_min_balance = new BigDecimal(param.getParamvalue());
					}
					
					List<Tinvoice> listInvoice = invDao.listSysPendingDisburse(disburse_limit);
					System.out.println("System Fund Transfer Records : " + listInvoice.size());
					if (listInvoice.size() > 0) {
						sourceacc = sysparamDao.getParamvalue(AppUtils.SYSPARAM_BANK_ACCNO);
						if (sourceacc != null)
							sourceacc = sourceacc.trim();

						bean = AppData.getBriapibean();
						briapi = new BriApiExt(bean);
						briapiToken = briapi.getTokenFundTransfer();

						for (Mfee fee : new MfeeDAO().listAll()) {
							mapFee.put(fee.getFeetype(), fee);
						}

						if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
							BigDecimal totalamount = new BigDecimal(0);
							BigDecimal sysamount = new BigDecimal(0);
							Map<Integer, BigDecimal> mapAmount = new HashMap<>();
							for (Tinvoice inv : listInvoice) {
								System.out.println("paidamount : " + inv.getPaidamount());
								if (inv.getInvoicetype().equals(AppUtils.INVOICETYPE_EVENT)) {
									sysamount = inv.getPaidamount().subtract(inv.getTeventreg().getTevent().getFeeprov()).subtract(inv.getTeventreg().getTevent().getFeekab());
									sysamount = sysamount.multiply(SYSTEM_DISBURSE_PERCENT);
									totalamount = totalamount.add(sysamount);
								} else if (inv.getInvoicetype().equals(AppUtils.INVOICETYPE_IURAN)) {
									Mfee fee = mapFee.get(inv.getInvoicetype());
									sysamount = (fee.getFeepusat().multiply(SYSTEM_DISBURSE_PERCENT)).multiply(new BigDecimal(inv.getInvoiceqty()));
									totalamount = totalamount.add(sysamount);
								} else if (inv.getInvoicetype().equals(AppUtils.INVOICETYPE_BORANG)) {
									sysamount = inv.getPaidamount().subtract(inv.getProvamount()).subtract(inv.getKabamount());
									sysamount = sysamount.multiply(SYSTEM_DISBURSE_PERCENT);
									totalamount = totalamount.add(sysamount);
								} else {
									Mfee fee = mapFee.get(inv.getInvoicetype());
									sysamount = fee.getFeepusat().multiply(SYSTEM_DISBURSE_PERCENT);
									totalamount = totalamount.add(sysamount);
								}	
								
								mapAmount.put(inv.getTinvoicepk(), sysamount);
							}
							
							System.out.println("totalamount : " + totalamount);
							totalamount = totalamount.setScale(2, RoundingMode.UP);
							System.out.println("totalamount after roundup : " + totalamount);
							if (disburse_min_balance.compareTo(totalamount) > 0)		
								doFundTransfer(listInvoice, totalamount, mapAmount);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					AppData.isActiveSysFundTransfer = false;
					System.out.println(new Date() + " System Fund Transfer Handler is Finished...");
				}
			} else {
				System.out.println(
						new Date().toString() + " System Fund Transfer Handler Start Canceled [Prev Thread Still Running]");
			}
		}
	}
	
	private void doFundTransfer(List<Tinvoice> objList, BigDecimal totalamount, Map<Integer, BigDecimal> mapAmount) throws Exception {
		try {
			Date trxtime = new Date();
			String noreff = counterDao.generateSeqnum();
			String remark = noreff + " SYS" + totalamount;
			remark = remark.replaceAll("/", "");
			remark = remark.substring(0, remark.length()-3);
			
			FundInqReq inqReq = new FundInqReq();
			inqReq.setSourceAccount(sourceacc);
			inqReq.setBeneficiaryAccount(BENEF_ACCNO);
			FundInqResp fundInq = briapi.fundInq(briapiToken.getAccess_token(), inqReq);
			if (fundInq != null && fundInq.getResponseCode() != null && fundInq.getResponseCode().equals("0100")) {				
				if (new BigDecimal(fundInq.getData().getSourceAccountBalance()).compareTo(disburse_min_balance) > 0) {
					FundTrfReq trfReq = new FundTrfReq();
					trfReq.setSourceAccount(sourceacc);
					trfReq.setBeneficiaryAccount(BENEF_ACCNO);
					//trfReq.setAmount(totalamount + ".00");
					trfReq.setAmount(String.valueOf(totalamount));
					trfReq.setFeeType("BEN");
					trfReq.setNoReferral(noreff);
					trfReq.setRemark(remark);
					trfReq.setTransactionDateTime(dateLocalFormatter.format(trxtime));

					FundTrfResp fundTrf = briapi.fundTrf(briapiToken.getAccess_token(), trfReq);
					// if (fundTrf != null && fundTrf.getResponseCode() != null &&
					// fundTrf.getResponseCode().equals("0200")) {
					if (fundTrf != null) {
						for (Tinvoice inv: objList) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								Ttransfer trf = new Ttransfer();
								trf.setTinvoice(inv);
								trf.setAmount(mapAmount.get(inv.getTinvoicepk()));
								trf.setTrfto("SYS");
								trf.setBankfee(new BigDecimal(1000));
								trf.setSourceacc(sourceacc);
								trf.setFeetype("BEN");
								trf.setNoreferral(noreff);
								trf.setTrxtime(trxtime);
								trf.setBenefacc(BENEF_ACCNO);
								trf.setBenefbankcode("BRINIDJA");
								trf.setBenefname("PT. OLAHDATA MEDIA KREASI");
								trf.setRemark(remark);
								trf.setTrftocode("SYS");
								trf.setTrftoname("PT. OLAHDATA MEDIA KREASI");
								trf.setResponsecode(fundTrf.getResponseCode());
								trf.setJournalseq(fundTrf.getJournalSeq());
								trf.setResponsedesc(fundTrf.getResponseDescription());
								trf.setErrordesc(fundTrf.getErrorDescription());
								trfDao.save(session, trf);

								if (fundTrf.getResponseCode().equals("0200")) {
									inv.setIstrfsys("Y");
									inv.setSysamounttrf(trf.getAmount());
									inv.setTrfsystime(new Date());
									invDao.save(session, inv);
								}
								
								trx.commit();
							} catch (Exception e) {
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

}
