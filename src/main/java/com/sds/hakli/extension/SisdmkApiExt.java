package com.sds.hakli.extension;

import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sds.hakli.pojo.SisdmkData;
import com.sds.hakli.pojo.SisdmkToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class SisdmkApiExt {
	
	public static void main(String[] args) {
		try {
			SisdmkApiExt main = new SisdmkApiExt();
			SisdmkToken objToken = main.getToken();
			main.getBiodata(objToken.getToken(), "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SisdmkToken getToken() throws Exception {
		SisdmkToken obj = null;
		ObjectMapper mapper = new ObjectMapper();
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
		    
		    System.out.println("***Begin Access Token SISDMK***");
			
		    MultivaluedMap<String, String> input = new MultivaluedHashMap<String, String>();
			input.add("username", "hakli");
			input.add("password", "jz0n7Vt0OgKU6GM9VbYoe9RGbZAQCm89");
			
			Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			WebResource webResource = client.resource("https://sisdmk.kemkes.go.id/rest/login_ws");
			ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED)
					.post(ClientResponse.class, input);

			String output = response.getEntity(String.class);
			System.out.println(output);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, SisdmkToken.class);
			System.out.println("TOKEN : " + obj.getToken());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public SisdmkData getBiodata(String token, String nik) throws Exception {	
		SisdmkData obj = null;
		ObjectMapper mapper = new ObjectMapper();
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
		    
		    System.out.println("***Begin Get Biodata SISDMK***");
			
		    Client client = Client.create();
			client.setConnectTimeout(60 * 1000);
			client.setReadTimeout(60 * 1000);

			WebResource webResource = client.resource("https://sisdmk.kemkes.go.id/rest/getBiodataByStr");
			ClientResponse response = webResource.queryParam("nik", nik).queryParam("token", token)
					.get(ClientResponse.class);

			String output = response.getEntity(String.class);
			System.out.println(output);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(output, SisdmkData.class);
			System.out.println("NIK : " + obj.getData().getNik());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

}
