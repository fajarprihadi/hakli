package com.sds.hakli.extension;

import javax.mail.MessagingException;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

public class MailBlast {
	
	public static void main(String[] args) {
		try {
			sendSSLMessage(null);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void sendSSLMessage(MailBean mailbean) throws MessagingException {
		try {
			Email email = new SimpleEmail();
	        email.setHostName("mail.bangzk.tech");
	        email.setSmtpPort(587);
	        email.setDebug(true);
	        email.setAuthenticator(new DefaultAuthenticator("admin@pphakli.org", "Qwert1234!"));
	        email.setSSL(false);
	        email.setSSLCheckServerIdentity(false);

	        email.setFrom("admin@pphakli.org");
	        email.setSubject("TestMail Dev");
	        email.setMsg("This is a test dev mail ... :-)");
	        email.addTo("fprihadi@gmail.com");

	        email.getMailSession().getProperties().put("mail.smtps.auth", "true");
	        email.getMailSession().getProperties().put("mail.debug", "true");
	        email.getMailSession().getProperties().put("mail.smtps.port", "587");
	        email.getMailSession().getProperties().put("mail.smtps.socketFactory.port", "587");
	        email.getMailSession().getProperties().put("mail.smtps.socketFactory.class",   "javax.net.ssl.SSLSocketFactory");
	        email.getMailSession().getProperties().put("mail.smtps.socketFactory.fallback", "false");
	        email.getMailSession().getProperties().put("mail.smtp.starttls.enable", "true");
	        email.getMailSession().getProperties().put("mail.smtp.ssl.trust", "");
	        email.getMailSession().getProperties().put("mail.smtp.starttls.required", "true");
	        email.getMailSession().getProperties().put("mail.smtp.ssl.protocols", "TLSv1.2");

	        email.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
