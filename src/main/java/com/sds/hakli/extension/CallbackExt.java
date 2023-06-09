package com.sds.hakli.extension;

import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sds.hakli.bean.CallbackBean;
import com.sds.hakli.bean.CallbackResp;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class CallbackExt {
	
	public CallbackExt() {
		
	}

	public CallbackResp notifPayment(CallbackBean data) throws Exception {
		CallbackResp obj = null;
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
		    
		    System.out.println("***Begin Hit Callback***");
	
		    Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);
			
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			String xtimestamp = dateFormatter.format(new Date());
			String jsonReq = mapper.writeValueAsString(data);
			
			StringBuffer payload = new StringBuffer();
			payload.append("path=http://103.224.65.71:8081/api/v1.0/notification");
			payload.append("&");
			payload.append("verb=POST");
			payload.append("&");
			payload.append("token=Bearer dMc7msxH9AHSjhcCiFflVc2nVUVgCPZp");
			payload.append("&");
			payload.append("timestamp=" + xtimestamp);
			payload.append("&");
			payload.append("body=" + jsonReq.trim());
			
			System.out.println("payload : " + payload.toString());
			
			String signature = BriApiExt.encode("Zi1EjzAjL2BuQIQ6", payload.toString());
			
			System.out.println("Request Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("Header - BRI-Signature : " + signature);
			System.out.println("Header - BRI-Timestamp : " + xtimestamp);
			System.out.println("Request body : " + jsonReq);
			System.out.println("Payload : " + payload.toString());
			
			WebResource webResource = client.resource("http://localhost:8081/api/v1.0/notification");
			ClientResponse response = webResource.header("BRI-Timestamp", xtimestamp)
					.header("BRI-Signature", signature)
					.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, jsonReq);

			output = response.getEntity(String.class);
			System.out.println("Response : " + output);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, CallbackResp.class);
			client.destroy();
			System.out.println("***End Hit Callback***");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}
