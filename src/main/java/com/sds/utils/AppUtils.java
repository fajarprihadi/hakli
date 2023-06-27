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
	
	public static final String ANGGOTA_ROLE_ADMIN = "ADM";
	public static final String ANGGOTA_ROLE_ANGGOTABIASA = "AB";
	public static final String ANGGOTA_ROLE_PENGURUSPUSAT = "PPU";
	public static final String ANGGOTA_ROLE_PENGURUSPROVINSI = "PPR";
	public static final String ANGGOTA_ROLE_PENGURUSKABUPATEN = "PKA";
	public static final String ANGGOTA_ROLE_KOMISIP2KB = "KP2";
	public static final String ANGGOTA_ROLE_TIMP2KB = "TP2";

	public static final String STATUS_ANGGOTA_REG = "1";
	public static final String STATUS_ANGGOTA_REG_PAYMENT = "2";
	public static final String STATUS_ANGGOTA_REG_ACTIVE = "3";
	public static final String STATUS_ANGGOTA_REG_DECLINE = "9";

	public static final String STATUS_APPROVEDTIM = "AT";
	public static final String STATUS_REJECTEDTIM = "RT";
	
	public static final String STATUS_APPROVEDKOMISI = "AK";
	public static final String STATUS_REJECTEDKOMISI = "RK";
	
	public static final String STATUS_WAITCONFIRM = "WC";

	public static final String CHARGETYPE_REG = "01";
	public static final String CHARGETYPE_IURAN = "02";
	
	public static final String INVOICETYPE_REG = "01";
	public static final String INVOICETYPE_IURAN = "02";
	public static final String INVOICETYPE_EVENT = "03";
	public static final String INVOICETYPE_P2KB = "04";
	
	public static final String EVENTTYPE_SUMPAHPROFESI = "SP";
	public static final String EVENTTYPE_SEMINAR = "SM";
	public static final String EVENTTYPE_WORKSHOP = "WS";

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
	
	public static final String SYSPARAM_CALLBACK_PATH = "CALLBACK_PATH";
	public static final String SYSPARAM_CALLBACK_URL = "CALLBACK_URL";
	
	public static final String SYSPARAM_GROUP_SISDMK = "SISDMK";
	public static final String SYSPARAM_SISDMK_URL = "SISDMK_URL";
	public static final String SYSPARAM_SISDMK_USERNAME = "SISDMK_USERNAME";
	public static final String SYSPARAM_SISDMK_PASSWORD = "SISDMK_PASSWORD";
	public static final String SYSPARAM_SISDMK_PATH_TOKEN = "SISDMK_PATH_TOKEN";
	public static final String SYSPARAM_SISDMK_PATH_BIODATA = "SISDMK_PATH_BIODATA";
	
	public static final String SYSPARAM_GROUP_SMTP = "SMTP";
	public static final String SYSPARAM_SMTP_HOST = "SMTP_HOST";
	public static final String SYSPARAM_SMTP_PORT = "SMTP_PORT";
	public static final String SYSPARAM_SMTP_EMAILID = "SMTP_EMAILID";
	public static final String SYSPARAM_SMTP_EMAILPASSWORD = "SMTP_EMAILPASSWORD";
	public static final String SYSPARAM_SMTP_EMAILSENDER = "SMTP_EMAILSENDER";
	
	public static final String SCHEDULER_ENABLE_LABEL = "ENABLE";
	public static final String SCHEDULER_ENABLE_VALUE = "1";
	public static final String SCHEDULER_DISABLE_LABEL = "DISABLE";
	public static final String SCHEDULER_DISABLE_VALUE = "0";
	public static final String SCHEDULER_REPEAT_PERMINUTE = "PER MINUTE";
	public static final String SCHEDULER_REPEAT_ATHOUR = "AT HOUR";
	public static final String SCHEDULER_REPEAT_ATDAY = "PER DAY";
	

	public static String getStatusLabel(String code) {
		if (code.trim().equals(AppUtils.STATUS_APPROVEDTIM))
			return "DISETUJUI TIMP2KB";
		else if (code.trim().equals(AppUtils.STATUS_REJECTEDTIM))
			return "DITOLAK TIMP2KB";
		else if (code.trim().equals(AppUtils.STATUS_APPROVEDKOMISI))
			return "DISETUJUI KOMISI";
		else if (code.trim().equals(AppUtils.STATUS_REJECTEDKOMISI))
			return "DITOLAK KOMISI";
		else if (code.trim().equals(AppUtils.STATUS_WAITCONFIRM))
			return "MENUNGGU KONFIRMASI";
		else if (code.trim().equals("A"))
			return "DISETUJUI";
		else if (code.trim().equals("R"))
			return "DITOLAK";
		else
			return code;
	}
	
	
}
