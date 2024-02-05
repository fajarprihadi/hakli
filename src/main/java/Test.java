import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Test {

	public static String encode(String key, String data) throws Exception {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
		sha256_HMAC.init(secret_key);
		return Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
	}

	public static void main_(String[] args) throws Exception {
//		String encrypted = encode("nUmPkHfO5qnxgZ9a",
//				"path=/v1/briva/j104408/77777/1&verb=GET&token=Bearer OVk4G80sgJFVo4mdVP4m6ccDO2tZ&timestamp=2023-04-06T00:42:52.153Z&body=");
//		System.out.println(encrypted);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -14);
		System.out.println(cal.getTime()); 
		
//		try {
//			String tablename = "api_hit_";
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.MONTH, -1);
//			System.out.println(tablename + cal.get(Calendar.YEAR));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public static void main(String[] args) throws Exception {
		Calendar cal = Calendar.getInstance();
		
		if (1 > 2 || 2 > 1 || 1 > 3 || 1 > 4) {
			System.out.println("TRUE");
		} else {
			System.out.println("FALSE");
		}
		
		if (cal.get(Calendar.MONTH) < 6) {
			cal.set(Calendar.MONTH, 5);
			cal.set(Calendar.DAY_OF_MONTH, 30);
		} else {
			cal.set(Calendar.MONTH, 11);
			cal.set(Calendar.DAY_OF_MONTH, 31);
		}
		
		System.out.println(cal.getTime());
	}
	
	

}
