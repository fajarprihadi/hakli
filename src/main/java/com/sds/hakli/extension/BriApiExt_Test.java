package com.sds.hakli.extension;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sds.hakli.bean.BriapiBean;
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
import com.sds.hakli.pojo.BrivaCreateResp;
import com.sds.hakli.pojo.BrivaData;
import com.sds.hakli.pojo.BrivaDataUpdate;
import com.sds.hakli.pojo.BrivaInquiryResp;
import com.sds.hakli.pojo.BrivaReport;
import com.sds.hakli.pojo.BrivaReportResp;
import com.sds.hakli.pojo.BrivaStatus;
import com.sds.hakli.pojo.BrivaUpdateResp;
import com.sds.hakli.pojo.FundInqReq;
import com.sds.hakli.pojo.FundInqResp;
import com.sds.hakli.pojo.FundOtherValidationResp;
import com.sds.hakli.pojo.FundRcReq;
import com.sds.hakli.pojo.FundRcResp;
import com.sds.hakli.pojo.FundTrfReq;
import com.sds.hakli.pojo.FundTrfResp;
import com.sds.utils.AppData;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class BriApiExt_Test {

//	String url_briapi = "https://sandbox.partner.api.bri.co.id/oauth/client_credential/accesstoken";
//	String url_briva = "https://sandbox.partner.api.bri.co.id/v1/briva";
//	String client_id = "P0HhD42Hk5zNQ8Me58gxRA9XIznRBQkN";
//	String client_secret = "nUmPkHfO5qnxgZ9a";

	String url_fund = "https://sandbox.partner.api.bri.co.id/v3.1/transfer/internal";
	String url_bifast = "https://sandbox.partner.api.bri.co.id/";

	private BriapiBean bean;

	public BriApiExt_Test(BriapiBean bean) {
		this.bean = bean;
	}

	public static void main_transfer(String[] args) {
		try {
			System.out.println(new Date().toString());
			BriapiBean bean = AppData.getBriapibean();
			// System.out.println("FT Consumer Key : " + bean.getBrift_consumerkey());
			// System.out.println("FT Consumer Secret : " + bean.getBrift_consumersecret());
			bean.setConsumerkey("HwsO7wCvFZY3lPNsXXGrwMzHi3roBvDC");
			bean.setConsumersecret("LP4yghyt4hwGSsjX");
			BriApiExt briapi = new BriApiExt(bean);
			BriApiToken briapiToken = briapi.getTokenFundTransfer();
			if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
				FundInqReq inqReq = new FundInqReq();
				inqReq.setSourceAccount("888801000157508");
				inqReq.setBeneficiaryAccount("888809999999918");
				briapi.fundInq(briapiToken.getAccess_token(), inqReq);
//				
//				FundTrfReq trfReq = new FundTrfReq();
//				trfReq.setSourceAccount("888801000157508");
//				trfReq.setBeneficiaryAccount("888809999999918");
//				trfReq.setAmount("10000.00");
//				trfReq.setFeeType("OUR");
//				trfReq.setNoReferral("99999999999999999918");
//				trfReq.setRemark("9999999999999999991801");
//				trfReq.setTransactionDateTime("04-07-2023 15:08:00");
//				
//				briapi.fundTrf(briapiToken.getAccess_token(), trfReq);

				FundRcReq rcReq = new FundRcReq();
				// rcReq.setNoReferral("99999999999999999918");
				rcReq.setNoReferral("88888888888888888884");
				rcReq.setTransactionDate("09-07-2021");
				briapi.fundRcStatus(briapiToken.getAccess_token(), rcReq);

			} else {
				System.out.println("NOT OK");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			System.out.println(new Date().toString());
			BriapiBean bean = AppData.getBriapibean();
			// System.out.println("FT Consumer Key : " + bean.getBrift_consumerkey());
			// System.out.println("FT Consumer Secret : " + bean.getBrift_consumersecret());
			bean.setConsumerkey("HwsO7wCvFZY3lPNsXXGrwMzHi3roBvDC");
			bean.setConsumersecret("LP4yghyt4hwGSsjX");
			BriApiExt_Test briapi = new BriApiExt_Test(bean);
			BIFastToken token = briapi.getTokenBIFast();
			if (token != null) {
//				BIFastStatusReq bifastStatusReq = new BIFastStatusReq();
//				bifastStatusReq.setOriginalPartnerReferenceNo("54321");
//				bifastStatusReq.setServiceCode("80");
//				bifastStatusReq.setTransactionDate("22-02-2022");
//				briapi.bifastStatus(token.getAccessToken(), bifastStatusReq);
//				System.exit(0);

				BIFastInqReq bifastInqReq = new BIFastInqReq();
				bifastInqReq.setBeneficiaryBankCode("CENAIDJA");
				bifastInqReq.setBeneficiaryAccountNo("12345678900");

				// bifastInqReq.setBeneficiaryBankCode("CENAIDJA");
				// bifastInqReq.setBeneficiaryAccountNo("14040");
				BIFastInqResp bifastInqResp = briapi.bifastInq(token.getAccessToken(), bifastInqReq);
				if (bifastInqResp != null && bifastInqResp.getResponseCode().equals("2008100")) {

					BIFastTrfReq bifastTrfReq = new BIFastTrfReq();
					bifastTrfReq.setCustomerReference("9999999999999999999999999999999999999999");
					bifastTrfReq.setSenderIdentityNumber("3515085211930002");
					bifastTrfReq.setSourceAccountNo("001901000378301");
					BIFastAmount amount = new BIFastAmount();
					amount.setValue("10000.00");
					amount.setCurrency("IDR");
					bifastTrfReq.setAmount(amount);
					bifastTrfReq.setBeneficiaryBankCode("CENAIDJA");
					bifastTrfReq.setBeneficiaryAccountNo("12345678900");
					bifastTrfReq.setReferenceNo("20220127BRINIDJA61050000018");
					bifastTrfReq.setExternalId("53394951711");
					bifastTrfReq.setTransactionDate("2022-02-22T13:07:24Z");
					bifastTrfReq.setPaymentInfo("testing bifast");
					bifastTrfReq.setSenderType("01");
					bifastTrfReq.setSenderResidentStatus("01");
					bifastTrfReq.setSenderTownName("0300");
					BIFastAdditionalInfo additional = new BIFastAdditionalInfo();
					additional.setDeviceId("12345679237");
					additional.setChannel("mobilephone");
					bifastTrfReq.setAdditionalInfo(additional);

					BIFastTrfResp bifastTrfResp = briapi.bifastTrf(token.getAccessToken(), bifastTrfReq);

				}
			} else {
				System.out.println("NOT OK");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BriApiToken getToken() throws Exception {
		BriApiToken obj = null;
		ObjectMapper mapper = new ObjectMapper();
		String output = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return false;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			System.out.println("***Begin Access Token***");

			MultivaluedMap<String, String> input = new MultivaluedHashMap<String, String>();
			input.add("client_id", bean.getConsumerkey());
			input.add("client_secret", bean.getConsumersecret());

			Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			WebResource webResource = client.resource(bean.getUrl() + bean.getBriapi_pathtoken());
			ClientResponse response = webResource.queryParam("grant_type", "client_credentials")
					.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, input);

			output = response.getEntity(String.class);
			System.out.println(output);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, BriApiToken.class);
			client.destroy();
			System.out.println(obj.getAccess_token());
			System.out.println("***End Access Token***");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public BriApiToken getTokenFundTransfer() throws Exception {
		BriApiToken obj = null;
		ObjectMapper mapper = new ObjectMapper();
		String output = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return false;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			System.out.println("***Begin Access Token***");

			MultivaluedMap<String, String> input = new MultivaluedHashMap<String, String>();
			input.add("client_id", bean.getBrift_consumerkey());
			input.add("client_secret", bean.getBrift_consumersecret());

			Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			WebResource webResource = client.resource(bean.getUrl() + bean.getBriapi_pathtoken());
			ClientResponse response = webResource.queryParam("grant_type", "client_credentials")
					.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, input);

			output = response.getEntity(String.class);
			System.out.println(output);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, BriApiToken.class);
			client.destroy();
			System.out.println(obj.getAccess_token());
			System.out.println("***End Access Token***");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public BrivaCreateResp createBriva(String token, BrivaData data) throws Exception {
		BrivaCreateResp obj = null;
		ObjectMapper mapper = new ObjectMapper();
		String output = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return false;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			System.out.println("***Begin Create BRIVA***");

			Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			String auth = "Bearer " + token;
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			String xtimestamp = dateFormatter.format(new Date());
			String jsonReq = mapper.writeValueAsString(data);

			StringBuffer payload = new StringBuffer();
			payload.append("path=" + bean.getBriva_pathcreate());
			payload.append("&");
			payload.append("verb=POST");
			payload.append("&");
			payload.append("token=" + auth);
			payload.append("&");
			payload.append("timestamp=" + xtimestamp);
			payload.append("&");
			payload.append("body=" + jsonReq);

			String signature = encode(bean.getConsumersecret(), payload.toString());

			System.out.println("Request Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("Header - Authorization : " + auth);
			System.out.println("Header - BRI-Signature : " + signature);
			System.out.println("Header - BRI-Timestamp : " + xtimestamp);
			System.out.println("Request body : " + jsonReq);
			System.out.println("Payload : " + payload.toString());

			WebResource webResource = client.resource(bean.getUrl() + bean.getBriva_pathcreate());
			ClientResponse response = webResource.header("Authorization", auth).header("BRI-Timestamp", xtimestamp)
					.header("BRI-Signature", signature).type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonReq);

			output = response.getEntity(String.class);
			System.out.println("Response : " + output);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, BrivaCreateResp.class);
			client.destroy();
			System.out.println("***End Create BRIVA***");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public BrivaInquiryResp getBriva(String token, String custcode) throws Exception {
		BrivaInquiryResp obj = new BrivaInquiryResp();
		ObjectMapper mapper = new ObjectMapper();
		String output = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return false;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			System.out.println("***Begin Get BRIVA***");

			Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			String auth = "Bearer " + token;
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			String xtimestamp = dateFormatter.format(new Date());

			StringBuffer payload = new StringBuffer();
			payload.append("path=" + bean.getBriva_pathget() + "/" + bean.getBriva_institutioncode() + "/"
					+ bean.getBriva_cid() + "/" + custcode);
			payload.append("&");
			payload.append("verb=GET");
			payload.append("&");
			payload.append("token=" + auth);
			payload.append("&");
			payload.append("timestamp=" + xtimestamp);
			payload.append("&");
			payload.append("body=");

			String signature = encode(bean.getConsumersecret(), payload.toString());

			System.out.println("Request Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("Header - Authorization : " + auth);
			System.out.println("Header - BRI-Signature : " + signature);
			System.out.println("Header - BRI-Timestamp : " + xtimestamp);
			System.out.println("Payload : " + payload.toString());

			WebResource webResource = client.resource(bean.getUrl() + bean.getBriva_pathget() + "/"
					+ bean.getBriva_institutioncode() + "/" + bean.getBriva_cid() + "/" + custcode);
			ClientResponse response = webResource.header("Authorization", auth).header("BRI-Timestamp", xtimestamp)
					.header("BRI-Signature", signature).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			output = response.getEntity(String.class);
			System.out.println("Response : " + output);

			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, BrivaInquiryResp.class);
			if (obj.getStatus()) {
				JSONObject json = new JSONObject(output);
				BrivaStatus data = new BrivaStatus();
				data.setInstitutionCode(json.getJSONObject("data").getString("institutionCode"));
				data.setAmount(json.getJSONObject("data").getString("Amount"));
				data.setNama(json.getJSONObject("data").getString("Nama"));
				data.setBrivaNo(json.getJSONObject("data").getString("BrivaNo")
						+ json.getJSONObject("data").getString("CustCode"));
				data.setKeterangan(json.getJSONObject("data").getString("Keterangan"));
				data.setStatusBayar(json.getJSONObject("data").getString("statusBayar"));
				data.setExpiredDate(json.getJSONObject("data").getString("expiredDate"));
				obj.setData(data);
			}
			client.destroy();
			System.out.println("***End Get BRIVA***");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public BrivaUpdateResp updateBriva(String token, BrivaDataUpdate data) throws Exception {
		BrivaUpdateResp obj = null;
		ObjectMapper mapper = new ObjectMapper();
		String output = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return false;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			System.out.println("***Begin Update Status BRIVA***");

			Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			String auth = "Bearer " + token;
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			String xtimestamp = dateFormatter.format(new Date());
			String jsonReq = mapper.writeValueAsString(data);

			StringBuffer payload = new StringBuffer();
			payload.append("path=" + bean.getBriva_pathupdate());
			payload.append("&");
			payload.append("verb=PUT");
			payload.append("&");
			payload.append("token=" + auth);
			payload.append("&");
			payload.append("timestamp=" + xtimestamp);
			payload.append("&");
			payload.append("body=" + jsonReq);

			String signature = encode(bean.getConsumersecret(), payload.toString());

			System.out.println("Request Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("Header - Authorization : " + auth);
			System.out.println("Header - BRI-Signature : " + signature);
			System.out.println("Header - BRI-Timestamp : " + xtimestamp);
			System.out.println("Payload : " + payload.toString());
			System.out.println("Request Body : " + jsonReq);

			WebResource webResource = client.resource(bean.getUrl() + bean.getBriva_pathupdate());
			ClientResponse response = webResource.header("Authorization", auth).header("BRI-Timestamp", xtimestamp)
					.header("BRI-Signature", signature).type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, jsonReq);

			output = response.getEntity(String.class);
			System.out.println("Response : " + output);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, BrivaUpdateResp.class);
			client.destroy();
			System.out.println("***End Update Status BRIVA***");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public BrivaCreateResp updateDataBriva(String token, BrivaData data) throws Exception {
		BrivaCreateResp obj = null;
		ObjectMapper mapper = new ObjectMapper();
		String output = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return false;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			System.out.println("***Begin Update Data BRIVA***");

			Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			String auth = "Bearer " + token;
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			String xtimestamp = dateFormatter.format(new Date());
			String jsonReq = mapper.writeValueAsString(data);

			StringBuffer payload = new StringBuffer();
			payload.append("path=" + bean.getBriva_pathupdateva());
			payload.append("&");
			payload.append("verb=PUT");
			payload.append("&");
			payload.append("token=" + auth);
			payload.append("&");
			payload.append("timestamp=" + xtimestamp);
			payload.append("&");
			payload.append("body=" + jsonReq);

			String signature = encode(bean.getConsumersecret(), payload.toString());

			System.out.println("Request Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("Header - Authorization : " + auth);
			System.out.println("Header - BRI-Signature : " + signature);
			System.out.println("Header - BRI-Timestamp : " + xtimestamp);
			System.out.println("Request body : " + jsonReq);
			System.out.println("Payload : " + payload.toString());

			WebResource webResource = client.resource(bean.getUrl() + bean.getBriva_pathupdateva());
			ClientResponse response = webResource.header("Authorization", auth).header("BRI-Timestamp", xtimestamp)
					.header("BRI-Signature", signature).type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, jsonReq);

			output = response.getEntity(String.class);
			System.out.println("Response : " + output);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, BrivaCreateResp.class);
			client.destroy();
			System.out.println("***End Update Data BRIVA***");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public BrivaReportResp getBrivaReport(String token, String startdate, String enddate) throws Exception {
		BrivaReportResp obj = new BrivaReportResp();
		ObjectMapper mapper = new ObjectMapper();
		String output = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return false;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			System.out.println("***Begin Get BRIVA Report***");

			Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			String auth = "Bearer " + token;
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			String xtimestamp = dateFormatter.format(new Date());

			StringBuffer payload = new StringBuffer();
			payload.append("path=" + bean.getBriva_pathreport() + "/" + bean.getBriva_institutioncode() + "/"
					+ bean.getBriva_cid() + "/" + startdate + "/" + enddate);
			payload.append("&");
			payload.append("verb=GET");
			payload.append("&");
			payload.append("token=" + auth);
			payload.append("&");
			payload.append("timestamp=" + xtimestamp);
			payload.append("&");
			payload.append("body=");

			String signature = encode(bean.getConsumersecret(), payload.toString());

			System.out.println("Request Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("Header - Authorization : " + auth);
			System.out.println("Header - BRI-Signature : " + signature);
			System.out.println("Header - BRI-Timestamp : " + xtimestamp);
			System.out.println("Payload : " + payload.toString());

			WebResource webResource = client.resource(bean.getUrl() + bean.getBriva_pathreport() + "/"
					+ bean.getBriva_institutioncode() + "/" + bean.getBriva_cid() + "/" + startdate + "/" + enddate);
			ClientResponse response = webResource.header("Authorization", auth).header("BRI-Timestamp", xtimestamp)
					.header("BRI-Signature", signature).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			output = response.getEntity(String.class);
			System.out.println(output);

			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, BrivaReportResp.class);
			if (obj.getStatus()) {
				JSONObject json = new JSONObject(output);

				List<BrivaReport> datas = new ArrayList<>();
				JSONArray jsonArray = json.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					BrivaReport data = new BrivaReport();
					data.setAmount(jsonObj.getString("amount"));
					data.setNama(jsonObj.getString("nama"));
					data.setBrivaNo(jsonObj.getString("brivaNo"));
					data.setKeterangan(jsonObj.getString("keterangan"));
					data.setPaymentDate(jsonObj.getString("paymentDate"));
					data.setTellerid(jsonObj.getString("tellerid"));
					data.setNo_rek(jsonObj.getString("no_rek"));
					data.setCustCode(jsonObj.getString("custCode"));
					datas.add(data);
				}

				obj.setData(datas);
			}
			client.destroy();
			System.out.println("***End Get BRIVA Report***");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public FundInqResp fundInq(String token, FundInqReq req) throws Exception {
		FundInqResp obj = new FundInqResp();
		ObjectMapper mapper = new ObjectMapper();
		String output = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return false;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			System.out.println("***Begin Fund Inquiry***");

			Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			String auth = "Bearer " + token;
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			String xtimestamp = dateFormatter.format(new Date());

			String jsonReq = mapper.writeValueAsString(req);

			StringBuffer payload = new StringBuffer();
			// payload.append("path=/v3.1/transfer/internal/accounts");
			payload.append("path=" + bean.getBrift_pathaccount());
			payload.append("&");
			payload.append("verb=POST");
			payload.append("&");
			payload.append("token=" + auth);
			payload.append("&");
			payload.append("timestamp=" + xtimestamp);
			payload.append("&");
			payload.append("body=" + jsonReq);

			String signature = encode(bean.getBrift_consumersecret(), payload.toString());

			System.out.println("Request Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("Header - Authorization : " + auth);
			System.out.println("Header - BRI-Signature : " + signature);
			System.out.println("Header - BRI-Timestamp : " + xtimestamp);
			System.out.println("Payload : " + payload.toString());

			// WebResource webResource = client.resource(url_fund + "/accounts");
			WebResource webResource = client.resource(bean.getUrl() + bean.getBrift_pathaccount());
			ClientResponse response = webResource.header("Authorization", auth).header("BRI-Timestamp", xtimestamp)
					.header("BRI-Signature", signature).type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonReq);

			output = response.getEntity(String.class);
			System.out.println("Response : " + output);

			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, FundInqResp.class);
			client.destroy();
			System.out.println("***End Fund Inquiry***");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public FundTrfResp fundTrf(String token, FundTrfReq req) throws Exception {
		FundTrfResp obj = new FundTrfResp();
		ObjectMapper mapper = new ObjectMapper();
		String output = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return false;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			System.out.println("***Begin Fund Trf***");

			Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			String auth = "Bearer " + token;
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			String xtimestamp = dateFormatter.format(new Date());

			String jsonReq = mapper.writeValueAsString(req);

			StringBuffer payload = new StringBuffer();
			// payload.append("path=/v3.1/transfer/internal");
			payload.append("path=" + bean.getBrift_pathtransfer());
			payload.append("&");
			payload.append("verb=POST");
			payload.append("&");
			payload.append("token=" + auth);
			payload.append("&");
			payload.append("timestamp=" + xtimestamp);
			payload.append("&");
			payload.append("body=" + jsonReq);

			// String signature = encode(bean.getConsumersecret(), payload.toString());
			String signature = encode(bean.getBrift_consumersecret(), payload.toString());

			System.out.println("Request Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("Header - Authorization : " + auth);
			System.out.println("Header - BRI-Signature : " + signature);
			System.out.println("Header - BRI-Timestamp : " + xtimestamp);
			System.out.println("Payload : " + payload.toString());

			// WebResource webResource = client.resource(url_fund);
			WebResource webResource = client.resource(bean.getUrl() + bean.getBrift_pathtransfer());
			ClientResponse response = webResource.header("Authorization", auth).header("BRI-Timestamp", xtimestamp)
					.header("BRI-Signature", signature).type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonReq);

			output = response.getEntity(String.class);
			System.out.println("Response : " + output);

			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, FundTrfResp.class);
			client.destroy();
			System.out.println("***End Fund Trf***");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public FundRcResp fundRcStatus(String token, FundRcReq req) throws Exception {
		FundRcResp obj = new FundRcResp();
		ObjectMapper mapper = new ObjectMapper();
		String output = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return false;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			System.out.println("***Begin Fund RC Status***");

			Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			String auth = "Bearer " + token;
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			String xtimestamp = dateFormatter.format(new Date());

			String jsonReq = mapper.writeValueAsString(req);

			StringBuffer payload = new StringBuffer();
			// payload.append("path=/v3.1/transfer/internal/check-rekening");
			payload.append("path=" + bean.getBrift_pathcheckstatus());
			payload.append("&");
			payload.append("verb=POST");
			payload.append("&");
			payload.append("token=" + auth);
			payload.append("&");
			payload.append("timestamp=" + xtimestamp);
			payload.append("&");
			payload.append("body=" + jsonReq);

			// String signature = encode(bean.getConsumersecret(), payload.toString());
			String signature = encode(bean.getBrift_consumersecret(), payload.toString());

			System.out.println("Request Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("Header - Authorization : " + auth);
			System.out.println("Header - BRI-Signature : " + signature);
			System.out.println("Header - BRI-Timestamp : " + xtimestamp);
			System.out.println("Payload : " + payload.toString());

			// WebResource webResource = client.resource(url_fund + "/check-rekening");
			WebResource webResource = client.resource(bean.getUrl() + bean.getBrift_pathcheckstatus());
			ClientResponse response = webResource.header("Authorization", auth).header("BRI-Timestamp", xtimestamp)
					.header("BRI-Signature", signature).type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonReq);

			output = response.getEntity(String.class);
			System.out.println("Response : " + output);

			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, FundRcResp.class);
			client.destroy();
			System.out.println("***End Fund RC Status***");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public BIFastToken getTokenBIFast() throws Exception {
		BIFastToken obj = null;
		ObjectMapper mapper = new ObjectMapper();
		String output = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return false;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			System.out.println("***Begin Access Token***");

			MultivaluedMap<String, String> input = new MultivaluedHashMap<String, String>();
			input.add("client_id", bean.getConsumerkey());
			input.add("client_secret", bean.getConsumersecret());

			Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			String xtimestamp = dateFormatter.format(new Date());
			// xtimestamp = "2021-11-02T13:14:15.000+07:00";

			String privateKeyPath = "C:/Projects/SDS/HAKLI/BRIAPI/Key/hakli_key.pem";
			String privKeyPEM = getKeyFromFile(privateKeyPath);
			byte[] privKeyBytes = Base64.getDecoder().decode(privKeyPEM);
			String signature = generateSignatureToken(privKeyBytes, bean.getBrift_consumerkey(), xtimestamp);
			System.out.println("signature : " + signature);

			// System.exit(0);
			String json = "{\"grantType\": \"client_credentials\"}";

			WebResource webResource = client
					.resource("https://sandbox.partner.api.bri.co.id/snap/v1.0/access-token/b2b");
			ClientResponse response = webResource.header("X-TIMESTAMP", xtimestamp)
					.header("X-CLIENT-KEY", bean.getConsumerkey()).header("X-SIGNATURE", signature)
					.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, json);

			output = response.getEntity(String.class);
			System.out.println(output);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, BIFastToken.class);
			client.destroy();
			System.out.println(obj.getAccessToken());
			System.out.println("***End Access Token***");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	private static String generateSignatureToken(byte[] privateKeyBytes, String clientId, String formattedTimestamp)
			throws Exception {
		String stringToSign = clientId + "|" + formattedTimestamp;
		Signature privateSignature = Signature.getInstance("SHA256withRSA");
		privateSignature.initSign(getPrivateKey(privateKeyBytes));
		privateSignature.update(stringToSign.getBytes("UTF-8"));
		byte[] signatureBytes = privateSignature.sign();
		return Base64.getEncoder().encodeToString(signatureBytes);
	}

	private static PrivateKey getPrivateKey(byte[] keyBytes) throws Exception {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
	}

	private static String getKeyFromFile(String filename) throws IOException {
		// Create a File object from the URL
		File file = new File(filename);

		// Create a FileReader for the File
		FileReader fileReader = new FileReader(file);
		BufferedReader br = new BufferedReader(fileReader);
		String line;
		StringBuilder key = new StringBuilder();
		while ((line = br.readLine()) != null) {
			if (!line.contains("-----BEGIN") && !line.contains("-----END")) {
				key.append(line);
			}
		}
		br.close();
		return key.toString();
	}

	public BIFastInqResp bifastInq(String token, BIFastInqReq req) throws Exception {
		BIFastInqResp obj = new BIFastInqResp();
		ObjectMapper mapper = new ObjectMapper();
		String output = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return false;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			System.out.println("***Begin BI-FAST Account Information***");

			Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			String auth = "Bearer " + token;
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			String xtimestamp = dateFormatter.format(new Date());
			// xtimestamp = "2023-12-13T13:14:15.000+07:00";

			String jsonReq = mapper.writeValueAsString(req);
			System.out.println("Req : " + jsonReq);

			StringBuffer payload = new StringBuffer();
			payload.append("path=/v1.0/transfer-bifast/inquiry-bifast");
			// payload.append("path=" + bean.getBrift_pathaccount());
			payload.append("&");
			payload.append("verb=POST");
			payload.append("&");
			payload.append("token=" + auth);
			payload.append("&");
			payload.append("timestamp=" + xtimestamp);
			payload.append("&");
			payload.append("body=" + jsonReq);

			String signature = generateSignatureRequest(bean.getConsumersecret(), "POST", xtimestamp, token, jsonReq,
					"/v1.0/transfer-bifast/inquiry-bifast");
			System.out.println("signature : " + signature);
			// String signature = encode(bean.getBrift_consumersecret(),
			// payload.toString());

			System.out.println("Request Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("Header - Authorization : " + auth);
			System.out.println("Header - BRI-Signature : " + signature);
			System.out.println("Header - BRI-Timestamp : " + xtimestamp);
			// System.out.println("Payload : " + payload.toString());

			// WebResource webResource = client.resource(url_bifast +
			// "/v1.0/transfer-bifast/inquiry-bifast");
			WebResource webResource = client
					.resource("https://sandbox.partner.api.bri.co.id/v1.0/transfer-bifast/inquiry-bifast");
			// WebResource webResource = client.resource(bean.getUrl() +
			// bean.getBrift_pathaccount());
			ClientResponse response = webResource.header("Authorization", auth).header("X-TIMESTAMP", xtimestamp)
					// .header("X-CLIENT-KEY", bean.getConsumerkey())
					.header("X-SIGNATURE", signature)
					// .header("X-PARTNER-ID", "Markicob")
					// .header("CHANNEL-ID", "BRIAP")
					// .header("X-EXTERNAL-ID", "123456789012345")
					.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, jsonReq);

			output = response.getEntity(String.class);
			System.out.println("Response : " + output);

			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, BIFastInqResp.class);
			client.destroy();
			System.out.println("***End BI-FAST Account Information***");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	private String generateSignatureRequest(String clientSecret, String method, String formattedTimestamp,
			String accessToken, String bodyRequest, String path) throws Exception {
		String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(bodyRequest);

		String stringToSign = method + ":" + path + ":" + accessToken + ":" + sha256hex + ":" + formattedTimestamp;
		System.out.println("stringToSign : " + stringToSign);
		// byte[] bytes = hmac("HmacSHA256", clientSecret.getBytes(),
		// stringToSign.getBytes());
		String hmac = calculateHMAC(stringToSign, clientSecret);
		return hmac;
	}

	private static String toHexString(byte[] bytes) {
		Formatter formatter = new Formatter();
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}
		return formatter.toString();
	}

	public static String calculateHMAC(String data, String key)
			throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
		Mac mac = Mac.getInstance("HmacSHA512");
		mac.init(secretKeySpec);
		return toHexString(mac.doFinal(data.getBytes()));
	}

	String bytesToHex(byte[] bytes) {
		final char[] hexArray = "0123456789abcdef".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0, v; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	byte[] hmac(String algorithm, byte[] key, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
		Mac mac = Mac.getInstance(algorithm);
		mac.init(new SecretKeySpec(key, algorithm));
		return mac.doFinal(message);
	}

	public BIFastTrfResp bifastTrf(String token, BIFastTrfReq req) throws Exception {
		BIFastTrfResp obj = new BIFastTrfResp();
		ObjectMapper mapper = new ObjectMapper();
		String output = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return false;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			System.out.println("***Begin BI-Fast Trf***");

			Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			String auth = "Bearer " + token;
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			String xtimestamp = dateFormatter.format(new Date());

			String jsonReq = mapper.writeValueAsString(req);
			System.out.println("Req : " + jsonReq);

			StringBuffer payload = new StringBuffer();
			payload.append("path=/v1.0/transfer-bifast/payment-bifast");
			// payload.append("path=" + bean.getBrift_pathtransfer());
			payload.append("&");
			payload.append("verb=POST");
			payload.append("&");
			payload.append("token=" + auth);
			payload.append("&");
			payload.append("timestamp=" + xtimestamp);
			payload.append("&");
			payload.append("body=" + jsonReq);

			String signature = generateSignatureRequest(bean.getConsumersecret(), "POST", xtimestamp, token, jsonReq,
					"/v1.0/transfer-bifast/payment-bifast");
			System.out.println("signature : " + signature);

			System.out.println("Request Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("Header - Authorization : " + auth);
			System.out.println("Header - BRI-Signature : " + signature);
			System.out.println("Header - BRI-Timestamp : " + xtimestamp);
			System.out.println("Payload : " + payload.toString());

			WebResource webResource = client
					.resource("https://sandbox.partner.api.bri.co.id/v1.0/transfer-bifast/payment-bifast");
			// WebResource webResource = client.resource(bean.getUrl() +
			// bean.getBrift_pathtransfer());
			ClientResponse response = webResource.header("Authorization", auth).header("X-Timestamp", xtimestamp)
					.header("X-Signature", signature).type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonReq);

			output = response.getEntity(String.class);
			System.out.println("Response : " + output);

			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, BIFastTrfResp.class);
			client.destroy();
			System.out.println("***End BI-Fast Trf***");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public BIFastStatusResp bifastStatus(String token, BIFastStatusReq req) throws Exception {
		BIFastStatusResp obj = new BIFastStatusResp();
		ObjectMapper mapper = new ObjectMapper();
		String output = null;
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return false;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			System.out.println("***Begin BI-Fast Status***");

			Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			String auth = "Bearer " + token;
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			String xtimestamp = dateFormatter.format(new Date());

			String jsonReq = mapper.writeValueAsString(req);
			System.out.println("Req : " + jsonReq);

			StringBuffer payload = new StringBuffer();
			payload.append("path=/v1.0/transfer-bifast/check-status-bifast");
			// payload.append("path=" + bean.getBrift_pathtransfer());
			payload.append("&");
			payload.append("verb=POST");
			payload.append("&");
			payload.append("token=" + auth);
			payload.append("&");
			payload.append("timestamp=" + xtimestamp);
			payload.append("&");
			payload.append("body=" + jsonReq);

			String signature = generateSignatureRequest(bean.getConsumersecret(), "POST", xtimestamp, token, jsonReq,
					"/v1.0/transfer-bifast/check-status-bifast");
			System.out.println("signature : " + signature);

			System.out.println("Request Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("Header - Authorization : " + auth);
			System.out.println("Header - BRI-Signature : " + signature);
			System.out.println("Header - BRI-Timestamp : " + xtimestamp);
			System.out.println("Payload : " + payload.toString());

			WebResource webResource = client
					.resource("https://sandbox.partner.api.bri.co.id/v1.0/transfer-bifast/check-status-bifast");
			// WebResource webResource = client.resource(bean.getUrl() +
			// bean.getBrift_pathtransfer());
			ClientResponse response = webResource.header("Authorization", auth).header("X-Timestamp", xtimestamp)
					.header("X-Signature", signature).type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonReq);

			output = response.getEntity(String.class);
			System.out.println("Response : " + output);

			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, BIFastStatusResp.class);
			client.destroy();
			System.out.println("***End BI-Fast Status***");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static String encode(String key, String data) throws Exception {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
		sha256_HMAC.init(secret_key);
		return Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
	}

	public static String encode2(PrivateKey key, String data) throws Exception {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(key.getEncoded(), "HmacSHA256");
		sha256_HMAC.init(secret_key);
		return Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
	}

	public void encryptHash(String hashToEncrypt, String pathOfKey, String Algorithm) throws Exception {
		FileInputStream fis = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len;

		File f = new File(pathOfKey);

		fis = new FileInputStream(pathOfKey);
		len = 0;
		while ((len = fis.read()) != -1) {
			baos.write(len);
		}

		KeyFactory kf = KeyFactory.getInstance(Algorithm); // Algorithm = "RSA"
		KeySpec keySpec = new PKCS8EncodedKeySpec(baos.toByteArray());
		baos.close();
		PrivateKey privateKey = kf.generatePrivate(keySpec); // Here's the exception thrown

		Signature rsaSigner = Signature.getInstance("SHA1withRSA");
		rsaSigner.initSign(privateKey);

		fis = new FileInputStream(hashToEncrypt);
		BufferedInputStream bis = new BufferedInputStream(fis);
		byte[] buffer = new byte[1024];
		len = 0;
		while ((len = bis.read(buffer)) >= 0) {
			try {
				rsaSigner.update(buffer, 0, len);
			} catch (SignatureException ex) {
				ex.printStackTrace();
			}
		}
		bis.close();

		byte[] signature = rsaSigner.sign();

		System.out.println(new String(signature));
	}

	public void test() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KeyPair kp = kpg.generateKeyPair();
			try (FileOutputStream out = new FileOutputStream("C:\\Projects\\SDS\\HAKLI\\BRIAPI\\Key\\hakli_key.pem")) {
				out.write(kp.getPrivate().getEncoded());
			}

			try (FileOutputStream out = new FileOutputStream("C:\\Projects\\SDS\\HAKLI\\BRIAPI\\Key\\hakli_pub.pem")) {
				out.write(kp.getPublic().getEncoded());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
