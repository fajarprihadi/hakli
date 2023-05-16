package com.sds.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			else if (obj.getParamcode().equals(AppUtils.SYSPARAM_BRIVA_PATH_UPDATEVA))
				bean.setBriva_pathupdateva(obj.getParamvalue());
			else if (obj.getParamcode().equals(AppUtils.SYSPARAM_BRIVA_PATH_REPORT))
				bean.setBriva_pathreport(obj.getParamvalue());
		}
		return bean;
	}
	
	public static String getBankName(String code) {
		String name = "";
		if (code.equals("BRINIDJA"))
			name = "Bank Rakyat Indonesia";
		else if (code.equals("DJARIDJ1"))
			name = "Bank Syariah Indonesia";
		return name;
	}
	
	public static String getInvoiceType(String code) {
		String name = "";
		if (code.equals(AppUtils.INVOICETYPE_REG))
			name = "Pendaftaran";
		else if (code.equals(AppUtils.INVOICETYPE_IURAN))
			name = "Iuran";
		else if (code.equals(AppUtils.INVOICETYPE_EVENT))
			name = "Event";
		return name;
	}
	
	public static String getLabel(String code) {
		if (code.trim().equals("A"))
			return "APPROVE";
		else if (code.trim().equals("R"))
			return "REJECT";
		else
			return code;
	}
	
	public static Map<Integer, String> getAngkaRomawi() throws Exception {
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "I");
		map.put(2, "II");
		map.put(3, "III");
		map.put(4, "IV");
		map.put(5, "V");
		map.put(6, "VI");
		map.put(7, "VII");
		map.put(8, "VIII");
		map.put(9, "IX");
		map.put(10, "X");
		map.put(11, "XI");
		map.put(12, "XII");
		return map;
	}
	
}
