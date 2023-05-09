package com.sds.utils;

import java.util.List;

import com.sds.hakli.dao.MsysparamDAO;
import com.sds.hakli.domain.Msysparam;
import com.sds.hakli.bean.BriapiBean;

public class AppData {

	public static BriapiBean getBriapibean() throws Exception {
		BriapiBean bean = new BriapiBean();
		List<Msysparam> params = new MsysparamDAO()
				.listByFilter("paramgroup = '" + AppUtils.SYSPARAM_GROUP_BRIAPI + "'", "orderno");
		for (Msysparam obj : params) {
			if (obj.getParamcode().equals(AppUtils.SYSPARAM_BRIAPI_URL))
				bean.setUrl(obj.getParamvalue());
			else if (obj.getParamcode().equals(AppUtils.SYSPARAM_BRIAPI_CONSUMER_KEY))
				bean.setConsumerkey(obj.getParamvalue());
			else if (obj.getParamcode().equals(AppUtils.SYSPARAM_BRIAPI_CONSUMER_SECRET))
				bean.setConsumersecret(obj.getParamvalue());
			else if (obj.getParamcode().equals(AppUtils.SYSPARAM_BRIVA_INSTITUTION_CODE))
				bean.setBriva_institutioncode(obj.getParamvalue());
			else if (obj.getParamcode().equals(AppUtils.SYSPARAM_BRIVA_CID))
				bean.setBriva_cid(obj.getParamvalue());
			else if (obj.getParamcode().equals(AppUtils.SYSPARAM_BRIAPI_PATH_TOKEN))
				bean.setBriapi_pathtoken(obj.getParamvalue());
			else if (obj.getParamcode().equals(AppUtils.SYSPARAM_BRIVA_PATH_CREATE))
				bean.setBriva_pathcreate(obj.getParamvalue());
			else if (obj.getParamcode().equals(AppUtils.SYSPARAM_BRIVA_PATH_GET))
				bean.setBriva_pathget(obj.getParamvalue());
			else if (obj.getParamcode().equals(AppUtils.SYSPARAM_BRIVA_PATH_UPDATE))
				bean.setBriva_pathupdate(obj.getParamvalue());
			else if (obj.getParamcode().equals(AppUtils.SYSPARAM_BRIVA_PATH_REPORT))
				bean.setBriva_pathreport(obj.getParamvalue());
		}
		return bean;
	}
	
	public static String getLabel(String code) {
		if (code.trim().equals(AppUtils.STATUS_APPROVED))
			return "Approve";
		else if (code.trim().equals(AppUtils.STATUS_REJECTED))
			return "Reject";
		else
			return code;
	}
	
	public static String getStatusLabel(String code) {
		if (code.trim().equals(AppUtils.STATUS_APPROVED))
			return "APPROVED";
		else if (code.trim().equals(AppUtils.STATUS_REJECTED))
			return "REJECTED";
		else
			return code;
	}
}
