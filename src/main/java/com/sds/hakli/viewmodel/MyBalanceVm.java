package com.sds.hakli.viewmodel;

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
import com.sds.hakli.extension.BriApiExt;
import com.sds.hakli.pojo.BriApiToken;
import com.sds.hakli.pojo.FundInqReq;
import com.sds.hakli.pojo.FundInqResp;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;

public class MyBalanceVm {
	
	private MsysparamDAO sysparamDao = new MsysparamDAO();
	
	private String sourceacc;
	private String benefaccno;
	
	private BriapiBean bean;
	private BriApiExt briapi;
	private BriApiToken briapiToken;
	private FundInqResp inqResp;
	
	@Wire
	private Groupbox gbResultInq;
	
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
			doReset();
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
	public void doReset() {
		benefaccno = "033901089934506";
		gbResultInq.setVisible(false);
	}

	public String getBenefaccno() {
		return benefaccno;
	}

	public void setBenefaccno(String benefaccno) {
		this.benefaccno = benefaccno;
	}

	public FundInqResp getInqResp() {
		return inqResp;
	}

	public void setInqResp(FundInqResp inqResp) {
		this.inqResp = inqResp;
	}

}
