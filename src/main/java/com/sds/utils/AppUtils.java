package com.sds.utils;

public class AppUtils {

	public static final String PATH_PHOTO = "/files/photo";
	public static final String PATH_EVENT = "/files/event";
	public static final String PATH_DOC = "/files/doc";
	public static final String PATH_IJAZAH = "/files/ijazah";
	public static final String PATH_P2KB = "/files/p2kb";
	public static final String PATH_REPORT = "/files/report";
	public static final String PATH_JASPER = "/jasper";

	public static final int PAGESIZE = 20;
	public static final String USERS_PASSWORD_DEFAULT = "123456";

	public static final String STATUS_ANGGOTA_REG = "1";
	public static final String STATUS_ANGGOTA_REG_PAYMENT = "2";
	public static final String STATUS_ANGGOTA_REG_ACTIVE = "3";
	public static final String STATUS_ANGGOTA_REG_DECLINE = "9";

	public static final String STATUS_APPROVED = "A";
	public static final String STATUS_REJECTED = "R";

	public static final String CHARGETYPE_REG = "01";
	public static final String CHARGETYPE_IURAN = "02";
	
	public static final String INVOICETYPE_REG = "01";
	public static final String INVOICETYPE_IURAN = "02";
	public static final String INVOICETYPE_EVENT = "03";
	

	public static final String SYSPARAM_GROUP_BRIAPI = "BRIAPI";
	public static final String SYSPARAM_BRIAPI_URL = "BRIAPI_URL";
	public static final String SYSPARAM_BRIAPI_CONSUMER_KEY = "BRIAPI_CONSUMER_KEY";
	public static final String SYSPARAM_BRIAPI_CONSUMER_SECRET = "BRIAPI_CONSUMER_SECRET";
	public static final String SYSPARAM_BRIAPI_PATH_TOKEN = "BRIAPI_PATH_TOKEN";
	public static final String SYSPARAM_BRIVA_INSTITUTION_CODE = "BRIVA_INSTITUTION_CODE";
	public static final String SYSPARAM_BRIVA_CID = "BRIVA_CID";
	public static final String SYSPARAM_BRIVA_PATH_CREATE = "BRIVA_PATH_CREATE";
	public static final String SYSPARAM_BRIVA_PATH_GET = "BRIVA_PATH_GET";
	public static final String SYSPARAM_BRIVA_PATH_UPDATE = "BRIVA_PATH_UPDATE";
	public static final String SYSPARAM_BRIVA_PATH_UPDATEVA = "BRIVA_PATH_UPDATEVA";
	public static final String SYSPARAM_BRIVA_PATH_REPORT = "BRIVA_PATH_REPORT";
	
	public static final String SCHEDULER_ENABLE_LABEL = "ENABLE";
	public static final String SCHEDULER_ENABLE_VALUE = "1";
	public static final String SCHEDULER_DISABLE_LABEL = "DISABLE";
	public static final String SCHEDULER_DISABLE_VALUE = "0";
	public static final String SCHEDULER_REPEAT_PERMINUTE = "PER MINUTE";
	public static final String SCHEDULER_REPEAT_ATHOUR = "AT HOUR";
	public static final String SCHEDULER_REPEAT_ATDAY = "PER DAY";
	

	public static String getStatusLabel(String code) {
		if (code.trim().equals(AppUtils.STATUS_APPROVED))
			return "APPROVED";
		else if (code.trim().equals(AppUtils.STATUS_REJECTED))
			return "REJECTED";
		else
			return code;
	}
}
