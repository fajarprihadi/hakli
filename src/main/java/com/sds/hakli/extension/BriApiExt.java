package com.sds.hakli.extension;

import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
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
import com.sds.utils.AppData;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class BriApiExt {
	
//	String url_briapi = "https://sandbox.partner.api.bri.co.id/oauth/client_credential/accesstoken";
//	String url_briva = "https://sandbox.partner.api.bri.co.id/v1/briva";
//	String client_id = "P0HhD42Hk5zNQ8Me58gxRA9XIznRBQkN";
//	String client_secret = "nUmPkHfO5qnxgZ9a";
	
	String url_fund = "https://sandbox.partner.api.bri.co.id/v3.1/transfer/internal";
	
	private BriapiBean bean;
	
	public BriApiExt(BriapiBean bean) {
		this.bean = bean;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(new Date().toString());
			BriapiBean bean = AppData.getBriapibean();
			bean.setConsumerkey("HwsO7wCvFZY3lPNsXXGrwMzHi3roBvDC");
			bean.setConsumersecret("LP4yghyt4hwGSsjX");
			BriApiExt briapi = new BriApiExt(bean);
			BriApiToken briapiToken = briapi.getToken();
			
			if (briapiToken != null && briapiToken.getStatus().equals("approved")) {
				FundInqReq inqReq = new FundInqReq();
				inqReq.setSourceAccount("888801000157508");
				inqReq.setBeneficiaryAccount("888809999999918");
				briapi.fundInq(briapiToken.getAccess_token(), inqReq);
				
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
		    TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return null;
		        }
		        public void checkClientTrusted(X509Certificate[] certs, String authType) {
		        }
		        public void checkServerTrusted(X509Certificate[] certs, String authType) {
		        }
		    }
		    };

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
			ClientResponse response = webResource.queryParam("grant_type", "client_credentials").type(MediaType.APPLICATION_FORM_URLENCODED)
					.post(ClientResponse.class, input);

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
		    TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return null;
		        }
		        public void checkClientTrusted(X509Certificate[] certs, String authType) {
		        }
		        public void checkServerTrusted(X509Certificate[] certs, String authType) {
		        }
		    }
		    };

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
			ClientResponse response = webResource.header("Authorization", auth)
					.header("BRI-Timestamp", xtimestamp)
					.header("BRI-Signature", signature)
					.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, jsonReq);

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
		    TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return null;
		        }
		        public void checkClientTrusted(X509Certificate[] certs, String authType) {
		        }
		        public void checkServerTrusted(X509Certificate[] certs, String authType) {
		        }
		    }
		    };

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
			payload.append("path=" + bean.getBriva_pathget() + "/" + bean.getBriva_institutioncode() +"/" + bean.getBriva_cid() + "/" + custcode);
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
			
			WebResource webResource = client.resource(bean.getUrl() + bean.getBriva_pathget() + "/" + bean.getBriva_institutioncode() +"/" + bean.getBriva_cid() + "/" + custcode);
			ClientResponse response = webResource.header("Authorization", auth)
					.header("BRI-Timestamp", xtimestamp)
					.header("BRI-Signature", signature)
					.accept(MediaType.APPLICATION_JSON)
					.get(ClientResponse.class);

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
				data.setBrivaNo(json.getJSONObject("data").getString("BrivaNo") + json.getJSONObject("data").getString("CustCode"));
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
		    TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return null;
		        }
		        public void checkClientTrusted(X509Certificate[] certs, String authType) {
		        }
		        public void checkServerTrusted(X509Certificate[] certs, String authType) {
		        }
		    }
		    };

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
			ClientResponse response = webResource.header("Authorization", auth)
					.header("BRI-Timestamp", xtimestamp)
					.header("BRI-Signature", signature)
					.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(ClientResponse.class, jsonReq);

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
		    TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return null;
		        }
		        public void checkClientTrusted(X509Certificate[] certs, String authType) {
		        }
		        public void checkServerTrusted(X509Certificate[] certs, String authType) {
		        }
		    }
		    };

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
			ClientResponse response = webResource.header("Authorization", auth)
					.header("BRI-Timestamp", xtimestamp)
					.header("BRI-Signature", signature)
					.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.put(ClientResponse.class, jsonReq);

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
		    TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return null;
		        }
		        public void checkClientTrusted(X509Certificate[] certs, String authType) {
		        }
		        public void checkServerTrusted(X509Certificate[] certs, String authType) {
		        }
		    }
		    };

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
			payload.append("path=" + bean.getBriva_pathreport() + "/" + bean.getBriva_institutioncode() +"/" + bean.getBriva_cid() + "/" + startdate + "/" + enddate);
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
			
			WebResource webResource = client.resource(bean.getUrl() + bean.getBriva_pathreport() + "/" + bean.getBriva_institutioncode() +"/" + bean.getBriva_cid() + "/" + startdate + "/" + enddate);
			ClientResponse response = webResource.header("Authorization", auth)
					.header("BRI-Timestamp", xtimestamp)
					.header("BRI-Signature", signature)
					.accept(MediaType.APPLICATION_JSON)
					.get(ClientResponse.class);

			output = response.getEntity(String.class);
			System.out.println(output);
			
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, BrivaReportResp.class);
			if (obj.getStatus()) {
				JSONObject json = new JSONObject(output);
				
				List<BrivaReport> datas = new ArrayList<>();
				JSONArray jsonArray = json.getJSONArray("data");
				for (int i=0; i<jsonArray.length(); i++) {
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
		    TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return null;
		        }
		        public void checkClientTrusted(X509Certificate[] certs, String authType) {
		        }
		        public void checkServerTrusted(X509Certificate[] certs, String authType) {
		        }
		    }
		    };

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
			//payload.append("path=" + bean.getBriva_pathget() + "/" + bean.getBriva_institutioncode() +"/" + bean.getBriva_cid() + "/" + custcode);
			payload.append("path=/v3.1/transfer/internal/accounts");
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
			System.out.println("Payload : " + payload.toString());
			
			WebResource webResource = client.resource(url_fund + "/accounts");
			ClientResponse response = webResource.header("Authorization", auth)
					.header("BRI-Timestamp", xtimestamp)
					.header("BRI-Signature", signature)
					.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, jsonReq);

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
	
	public static String encode(String key, String data) throws Exception {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
		sha256_HMAC.init(secret_key);
		return Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
	}
}
