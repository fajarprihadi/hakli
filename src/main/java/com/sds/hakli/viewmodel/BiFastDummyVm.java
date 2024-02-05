package com.sds.hakli.viewmodel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
import com.sds.hakli.pojo.BIFastAdditionalInfo;
import com.sds.hakli.pojo.BIFastAmount;
import com.sds.hakli.pojo.BIFastInqReq;
import com.sds.hakli.pojo.BIFastInqResp;
import com.sds.hakli.pojo.BIFastStatusReq;
import com.sds.hakli.pojo.BIFastStatusResp;
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

public class BiFastDummyVm {
	
	private MsysparamDAO sysparamDao = new MsysparamDAO();
	
	private String sourceacc;
	private String benefaccno;
	private String bankcode;
	private BigDecimal amount;
	private String custref;
	private String refno;
	private String trxdate;
	
	private BriapiBean bean;
	private BriApiExt briapi;
	private BIFastToken briapiToken;
	private BIFastInqResp inqResp;
	private String inqRespMsg;
	private BIFastTrfResp trfResp;
	private String trfRespMsg;
	private String statusRespMsg;
	
	private SimpleDateFormat dateLocalFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	@Wire
	private Groupbox gbResultInq;
	@Wire
	private Groupbox gbResultTrf;
	@Wire
	private Groupbox gbResultStatus;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			sourceacc = sysparamDao.getParamvalue(AppUtils.SYSPARAM_BANK_ACCNO);
			if (sourceacc != null)
				sourceacc = sourceacc.trim();
			bean = AppData.getBriapibean();
			briapi = new BriApiExt(bean);
			briapiToken = briapi.getTokenBIFast();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void doInq() {
		try {
			if (briapiToken != null) {
				BIFastInqReq bifastInqReq = new BIFastInqReq();
				bifastInqReq.setBeneficiaryBankCode(bankcode == null ? "" : bankcode);
				bifastInqReq.setBeneficiaryAccountNo(benefaccno == null ? "" : benefaccno);
				inqResp = briapi.bifastInq(briapiToken.getAccessToken(), bifastInqReq);
				inqRespMsg = inqResp.toString();
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
			if (inqResp != null && inqResp.getResponseCode() != null && inqResp.getResponseCode().equals("2008100")) {
				BIFastTrfReq bifastTrfReq = new BIFastTrfReq();
				bifastTrfReq.setCustomerReference(custref == null ? "" : custref);
				//bifastTrfReq.setSenderIdentityNumber("3515085211930002");
				bifastTrfReq.setSenderIdentityNumber(sourceacc);
				bifastTrfReq.setSourceAccountNo(sourceacc);
				BIFastAmount objamount = new BIFastAmount();
				objamount.setValue(amount + ".00");
				objamount.setCurrency("IDR");
				bifastTrfReq.setAmount(objamount);
				bifastTrfReq.setBeneficiaryBankCode(bankcode == null ? "" : bankcode);
				bifastTrfReq.setBeneficiaryAccountNo(benefaccno == null ? "" : benefaccno);
				bifastTrfReq.setReferenceNo(refno == null ? "" : refno);
				bifastTrfReq.setExternalId(inqResp.getExternalId());
				
				SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
				String xtimestamp = dateFormatter.format(new Date());
				
				bifastTrfReq.setTransactionDate(xtimestamp);
				bifastTrfReq.setPaymentInfo("testing bifast");
				bifastTrfReq.setSenderType("01");
				bifastTrfReq.setSenderResidentStatus("01");
				bifastTrfReq.setSenderTownName("0300");
//				BIFastAdditionalInfo additional = new BIFastAdditionalInfo();
//				additional.setDeviceId("12345679237");
//				additional.setChannel("mobilephone");
//				bifastTrfReq.setAdditionalInfo(additional);
				
				trfResp = briapi.bifastTrf(briapiToken.getAccessToken(), bifastTrfReq);
				trfRespMsg = trfResp.toString();
				gbResultTrf.setVisible(true);
			} else {
				Messagebox.show("Inquiry Failed", WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.EXCLAMATION);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.ERROR);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doStatus() {
		try {
			BIFastStatusReq bifastStatusReq = new BIFastStatusReq();
			bifastStatusReq.setOriginalPartnerReferenceNo(custref == null ? "" : custref);
			bifastStatusReq.setServiceCode("80");
			bifastStatusReq.setTransactionDate(trxdate == null ? "" : trxdate);
			BIFastStatusResp bifaststatusResp = briapi.bifastStatus(briapiToken.getAccessToken(), bifastStatusReq);
			statusRespMsg = bifaststatusResp.toString();
			gbResultStatus.setVisible(true);
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.ERROR);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		bankcode = null;
		benefaccno = null;
		amount = null;
		custref = null;
		refno = null;
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

	public BIFastInqResp getInqResp() {
		return inqResp;
	}

	public void setInqResp(BIFastInqResp inqResp) {
		this.inqResp = inqResp;
	}

	public BIFastTrfResp getTrfResp() {
		return trfResp;
	}

	public void setTrfResp(BIFastTrfResp trfResp) {
		this.trfResp = trfResp;
	}

	public String getBankcode() {
		return bankcode;
	}

	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}

	public String getInqRespMsg() {
		return inqRespMsg;
	}

	public void setInqRespMsg(String inqRespMsg) {
		this.inqRespMsg = inqRespMsg;
	}

	public String getTrfRespMsg() {
		return trfRespMsg;
	}

	public void setTrfRespMsg(String trfRespMsg) {
		this.trfRespMsg = trfRespMsg;
	}

	public String getCustref() {
		return custref;
	}

	public void setCustref(String custref) {
		this.custref = custref;
	}

	public String getRefno() {
		return refno;
	}

	public void setRefno(String refno) {
		this.refno = refno;
	}

	public String getSourceacc() {
		return sourceacc;
	}

	public void setSourceacc(String sourceacc) {
		this.sourceacc = sourceacc;
	}

	public String getTrxdate() {
		return trxdate;
	}

	public void setTrxdate(String trxdate) {
		this.trxdate = trxdate;
	}

	public String getStatusRespMsg() {
		return statusRespMsg;
	}

	public void setStatusRespMsg(String statusRespMsg) {
		this.statusRespMsg = statusRespMsg;
	}
}
