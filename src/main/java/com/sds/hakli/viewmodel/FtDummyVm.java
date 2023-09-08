package com.sds.hakli.viewmodel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;

import com.sds.hakli.bean.BriapiBean;
import com.sds.hakli.dao.MsysparamDAO;
import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.dao.TtransferDAO;
import com.sds.hakli.domain.Ttransfer;
import com.sds.hakli.extension.BriApiExt;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.FundInqReq;
import com.sds.hakli.pojo.FundInqResp;
import com.sds.hakli.pojo.FundTrfReq;
import com.sds.hakli.pojo.FundTrfResp;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class FtDummyVm {
	
	private MsysparamDAO sysparamDao = new MsysparamDAO();
	
	private String sourceacc;
	private String benefaccno;
	private BigDecimal amount;
	
	private BriapiBean bean;
	private BriApiExt briapi;
	private BriApiToken briapiToken;
	private FundInqResp inqResp;
	private FundTrfResp trfResp;
	
	private SimpleDateFormat dateLocalFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	@Wire
	private Groupbox gbResultInq;
	@Wire
	private Groupbox gbResultTrf;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			sourceacc = sysparamDao.getParamvalue(AppUtils.SYSPARAM_BANK_ACCNO);
			if (sourceacc != null)
				sourceacc = sourceacc.trim();
			bean = AppData.getBriapibean();
			briapi = new BriApiExt(bean);
			briapiToken = briapi.getTokenFundTransfer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void doInq() {
		try {
			if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
				FundInqReq inqReq = new FundInqReq();
				inqReq.setSourceAccount(sourceacc);
				inqReq.setBeneficiaryAccount(benefaccno);
				inqResp = briapi.fundInq(briapiToken.getAccess_token(), inqReq);
				
				gbResultInq.setVisible(true);
			} else 
				Messagebox.show("Invalid Token", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.EXCLAMATION);
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.ERROR);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doTrf() {
		try {
			if (inqResp != null && inqResp.getResponseCode() != null && inqResp.getResponseCode().equals("0100")) {
				Ttransfer trf = new Ttransfer();
				trf.setTrfto("TES");
				trf.setAmount(amount);					
				trf.setSourceacc(sourceacc);
				trf.setFeetype("OUR");
				trf.setNoreferral(new TcounterengineDAO().generateSeqnum());
				trf.setTrxtime(new Date());
				trf.setBenefacc(benefaccno);
				trf.setBenefbankcode("BRINIDJA");
				trf.setBenefname("FP");
				trf.setRemark(trf.getNoreferral() + new SimpleDateFormat("MMddHHmmss").format(new Date()));				
				
				FundTrfReq trfReq = new FundTrfReq();
				trfReq.setSourceAccount(sourceacc);
				trfReq.setBeneficiaryAccount(benefaccno);
				trfReq.setAmount(amount + ".00");
				trfReq.setFeeType("OUR");
				trfReq.setNoReferral(trf.getNoreferral());
				trfReq.setRemark(trf.getRemark());
				trfReq.setTransactionDateTime(dateLocalFormatter.format(new Date()));
				
				trfResp = briapi.fundTrf(briapiToken.getAccess_token(), trfReq);
				if (trfResp != null) {
					trf.setResponsecode(trfResp.getResponseCode());
					trf.setJournalseq(trfResp.getJournalSeq());
					trf.setResponsedesc(trfResp.getResponseDescription());
					trf.setErrordesc(trfResp.getErrorDescription());
				}
				
				Session session = StoreHibernateUtil.openSession();
				Transaction trx = session.beginTransaction();
				try {
					new TtransferDAO().save(session, trf);
					trx.commit();
				} catch (Exception e) {
					trx.rollback();
					e.printStackTrace();
				} finally {
					session.close();
				}
				gbResultTrf.setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.ERROR);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		benefaccno = null;
		amount = null;
		gbResultInq.setVisible(false);
		gbResultTrf.setVisible(false);
	}

	public String getBenefaccno() {
		return benefaccno;
	}

	public void setBenefaccno(String benefaccno) {
		this.benefaccno = benefaccno;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public FundInqResp getInqResp() {
		return inqResp;
	}

	public void setInqResp(FundInqResp inqResp) {
		this.inqResp = inqResp;
	}

	public FundTrfResp getTrfResp() {
		return trfResp;
	}

	public void setTrfResp(FundTrfResp trfResp) {
		this.trfResp = trfResp;
	}
}
