package com.sds.hakli.extension;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
import com.sds.hakli.pojo.BIFastAmount;
import com.sds.hakli.pojo.BIFastInqReq;
import com.sds.hakli.pojo.BIFastInqResp;
import com.sds.hakli.pojo.BIFastToken;
import com.sds.hakli.pojo.BIFastTrfReq;
import com.sds.hakli.pojo.BIFastTrfResp;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.FundInqReq;
import com.sds.hakli.pojo.FundInqResp;
import com.sds.hakli.pojo.FundTrfReq;
import com.sds.hakli.pojo.FundTrfResp;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class BriapiBIFastHandler implements Job {

	private BriapiBean bean;
	private BIFastToken briapiToken;
	private TinvoiceDAO invDao = new TinvoiceDAO();
	private MsysparamDAO sysparamDao = new MsysparamDAO();
	private TtransferDAO trfDao = new TtransferDAO();
	private TcounterengineDAO counterDao = new TcounterengineDAO();
	private SimpleDateFormat dateLocalFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	private Map<String, Mfee> mapFee = new HashMap<>();
	private BriApiExt briapi;
	private String sourceacc;
	BigDecimal disburse_min_balance = new BigDecimal(0);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {		
		synchronized(this) {
			if (!AppData.isActiveFundTransfer) {
				System.out.println(new Date() + " BIFast Fund Transfer Handler is Started...");
				AppData.isActiveFundTransfer = true;
				try {
					int disburse_limit = 1;
					Msysparam param = new MsysparamDAO()
							.findByCode("DISBURSE_LIMIT");
					if (param != null) {
						disburse_limit = Integer.parseInt(param.getParamvalue());
					}
					
					param = new MsysparamDAO()
							.findByCode("DISBURSE_MIN_BALANCE");
					if (param != null) {
						disburse_min_balance = new BigDecimal(param.getParamvalue());
					}
					
					List<Tinvoice> listInvoice = invDao.listPaidPendingDisburseBIFast("BSMDIDJA", disburse_limit);
					//List<Tinvoice> listInvoice = invDao.listByFilter("ispaid = 'Y' and (istrfprov = 'N' or istrfkab = 'N')", "tinvoicepk");
					//List<Tinvoice> listInvoice = invDao.listByFilter("tinvoicepk = 6035", "tinvoicepk");
					System.out.println("Fund Transfer Records : " + listInvoice.size());
					if (listInvoice.size() > 0) {
						sourceacc = sysparamDao.getParamvalue(AppUtils.SYSPARAM_BANK_ACCNO);
						if (sourceacc != null)
							sourceacc = sourceacc.trim();

						bean = AppData.getBriapibean();
						briapi = new BriApiExt(bean);
						briapiToken = briapi.getTokenBIFast();

						for (Mfee fee : new MfeeDAO().listAll()) {
							mapFee.put(fee.getFeetype(), fee);
						}

						if (briapiToken != null) {
							for (Tinvoice inv : listInvoice) {
								// doFundTransfer(inv, "PROV");
																
								if (inv.getIstrfprov().equals("N")
										&& inv.getTanggota().getMcabang().getMprov().getIsdisburse() != null
										&& inv.getTanggota().getMcabang().getMprov().getIsdisburse().equals("Y")
										&& inv.getTanggota().getMcabang().getMprov().getAccno().length() > 0) {											
									doFundTransfer(inv, "PROV");
								}
								if (inv.getIstrfkab().equals("N") && inv.getTanggota().getMcabang().getIsdisburse() != null
										&& inv.getTanggota().getMcabang().getIsdisburse().equals("Y")
										&& inv.getTanggota().getMcabang().getAccno().length() > 0) {
									doFundTransfer(inv, "KAB");								
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					AppData.isActiveFundTransfer = false;
					System.out.println(new Date() + " BIFast Fund Transfer Handler is Finished...");
				}
			} else {
				System.out.println(
						new Date().toString() + " BIFast Fund Transfer Handler Start Canceled [Prev Thread Still Running]");
			}
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
			} else if (inv.getInvoicetype().equals(AppUtils.INVOICETYPE_IURAN) || inv.getInvoicetype().equals(AppUtils.INVOICETYPE_BORANG)) {
				if (beneftype.equals("PROV")) {
					//BigDecimal amounttrf = inv.getProvamounttrf() == null ? new BigDecimal(0) : inv.getProvamounttrf();
					//trf.setAmount(inv.getProvamount().subtract(amounttrf));
					trf.setAmount(inv.getProvamount());
				} else if (beneftype.equals("KAB")) {
					//BigDecimal amounttrf = inv.getKabamounttrf() == null ? new BigDecimal(0) : inv.getKabamounttrf();
					//trf.setAmount(inv.getKabamount().subtract(amounttrf));
					trf.setAmount(inv.getKabamount());
				}
			} else {
				Mfee fee = mapFee.get(inv.getInvoicetype());
				if (beneftype.equals("PROV"))
					trf.setAmount(fee.getFeeprov());
				else if (beneftype.equals("KAB"))
					trf.setAmount(fee.getFeekab());
			}
			
			trf.setBankfee(new BigDecimal(2500));
			trf.setAmount(trf.getAmount().subtract(trf.getBankfee()));

			trf.setSourceacc(sourceacc);
			trf.setFeetype("BEN");
			trf.setNoreferral(counterDao.generateSeqnum());
			trf.setTrxtime(new Date());
			if (beneftype.equals("PROV")) {
				trf.setBenefacc(inv.getTanggota().getMcabang().getMprov().getAccno());
				trf.setBenefbankcode(inv.getTanggota().getMcabang().getMprov().getBankcode());
				trf.setBenefname(inv.getTanggota().getMcabang().getMprov().getAccname());
				trf.setRemark(trf.getNoreferral() + " P" + inv.getInvoicetype() + inv.getInvoiceno());
				trf.setTrftocode(inv.getTanggota().getMcabang().getMprov().getProvcode());
				trf.setTrftoname(inv.getTanggota().getMcabang().getMprov().getProvname());
			} else if (beneftype.equals("KAB")) {
				trf.setBenefacc(inv.getTanggota().getMcabang().getAccno());
				trf.setBenefbankcode(inv.getTanggota().getMcabang().getBankcode());
				trf.setBenefname(inv.getTanggota().getMcabang().getAccname());
				trf.setRemark(trf.getNoreferral() + " K" + inv.getInvoicetype() + inv.getInvoiceno());
				trf.setTrftocode(inv.getTanggota().getMcabang().getKodecabang());
				trf.setTrftoname(inv.getTanggota().getMcabang().getCabang());
			}
			trf.setRemark(trf.getRemark().replaceAll("/", ""));
			
			BIFastInqReq bifastInqReq = new BIFastInqReq();
			bifastInqReq.setBeneficiaryBankCode(trf.getBenefbankcode());
			bifastInqReq.setBeneficiaryAccountNo(trf.getBenefacc());
			BIFastInqResp inqResp = briapi.bifastInq(briapiToken.getAccessToken(), bifastInqReq);
			
			if (inqResp != null && inqResp.getResponseCode() != null && inqResp.getResponseCode().equals("2008100")) {
				
				trf.setBankrefno(inqResp.getReferenceNo());
				
				BIFastTrfReq bifastTrfReq = new BIFastTrfReq();
				bifastTrfReq.setCustomerReference(trf.getNoreferral());
				bifastTrfReq.setSenderIdentityNumber(sourceacc);
				bifastTrfReq.setSourceAccountNo(sourceacc);
				BIFastAmount objamount = new BIFastAmount();
				objamount.setValue(trf.getAmount() + ".00");
				objamount.setCurrency("IDR");
				bifastTrfReq.setAmount(objamount);
				bifastTrfReq.setBeneficiaryBankCode(trf.getBenefbankcode());
				bifastTrfReq.setBeneficiaryAccountNo(trf.getBenefacc());
				bifastTrfReq.setReferenceNo(inqResp.getReferenceNo());
				bifastTrfReq.setExternalId(inqResp.getExternalId());
				
				SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
				String xtimestamp = dateFormatter.format(trf.getTrxtime());
				
				bifastTrfReq.setTransactionDate(xtimestamp);
				bifastTrfReq.setPaymentInfo(trf.getTinvoice().getInvoicedesc());
				bifastTrfReq.setSenderType("01");
				bifastTrfReq.setSenderResidentStatus("01");
				bifastTrfReq.setSenderTownName("0300");
//				BIFastAdditionalInfo additional = new BIFastAdditionalInfo();
//				additional.setDeviceId("12345679237");
//				additional.setChannel("mobilephone");
//				bifastTrfReq.setAdditionalInfo(additional);
				
				BIFastTrfResp trfResp = briapi.bifastTrf(briapiToken.getAccessToken(), bifastTrfReq);			
				if (trfResp != null) {
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						trf.setResponsecode(trfResp.getResponseCode());
						trf.setJournalseq(trfResp.getJournalSequence());
						trf.setResponsedesc(trfResp.getResponseMessage());
						trf.setErrordesc(trfResp.getResponseMessage());
						trfDao.save(session, trf);

						if (trfResp.getResponseCode().equals("2008000")) {
							if (beneftype.equals("PROV")) {
								inv.setIstrfprov("Y");
								inv.setTrfprovamount(trf.getAmount());
								inv.setProvamounttrf(trf.getAmount());
								inv.setTrfprovtime(new Date());
							} else if (beneftype.equals("KAB")) {
								inv.setIstrfkab("Y");
								inv.setTrfkabamount(trf.getAmount());
								inv.setKabamounttrf(trf.getAmount());
								inv.setTrfkabtime(new Date());
							}
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
