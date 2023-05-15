package com.sds.hakli.extension;

import java.security.Security;
import java.util.Properties;
import java.util.Map.Entry;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender {

	public static void sendSSLMessage(MailBean mailbean) throws MessagingException {

		//Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		boolean debug = false;
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", mailbean.getSmtpname());
		props.put("mail.smtp.port", mailbean.getSmtpport());
		props.put("mail.smtp.from", mailbean.getMailid());
		props.put("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
		
		// remark untuk relay server dan unremark untuk mail server
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.socketFactory.fallback", "true");

		Session session = Session.getDefaultInstance(props);
		session.setDebug(debug);
		Message message = new MimeMessage(session);
		InternetAddress addressFrom;
		addressFrom = new InternetAddress(mailbean.getFrom());
		message.setFrom(addressFrom);
		message.setReplyTo(InternetAddress.parse(mailbean.getMailid()));

		if (mailbean.getRecipients() != null) {
			InternetAddress[] recipientAddress = new InternetAddress[mailbean.getRecipients().length];
			int i = 0;
			for (String recipient : mailbean.getRecipients()) {
				recipientAddress[i++] = new InternetAddress(recipient.trim());
			}
			message.setRecipients(Message.RecipientType.TO, recipientAddress);
		} else
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailbean.getRecipient(), true));
		message.setSubject(mailbean.getSubject());

		// Create a message part to represent the body text
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(mailbean.getBodymsg(), "text/html");
		// use a MimeMultipart as we need to handle the file attachments
		Multipart multipart = new MimeMultipart();
		// add the message body to the mime message
		multipart.addBodyPart(messageBodyPart);

		// add any file attachments to the message

		if (mailbean.getAttachment() != null && !mailbean.getAttachment().equals(""))
			addAtachments(mailbean.getFilename(), mailbean.getAttachment(), multipart);

		// Put all message parts in the message

		message.setContent(multipart);
		message.addHeader("appid", String.valueOf(mailbean.getAppid()));

		//Transport t = session.getTransport("smtp");
		//t.connect(mailbean.getSmtpname(), mailbean.getSmtpport(), mailbean.getMailid(), mailbean.getMailpassword());
		//t.connect("mail.sdd.co.id", 465, "fajar.prihadi@swadharma.com", "fprihadi3458");
		//t.sendMessage(message, message.getAllRecipients());
		//t.close();

//		Transport t = session.getTransport("smtp");
//		t.connect();
//		t.sendMessage(message, message.getAllRecipients());
//		t.close();

	}

	protected static void addAtachments(String filename, String docpath, Multipart multipart)
			throws MessagingException, AddressException {
		MimeBodyPart attachmentBodyPart = new MimeBodyPart();

		// use a JAF FileDataSource as it does MIME type detection
		DataSource source = new FileDataSource(docpath);
		attachmentBodyPart.setDataHandler(new DataHandler(source));

		// assume that the filename you want to send is the same as the
		// actual file name - could alter this to remove the file path
		attachmentBodyPart.setFileName(filename);

		// add the attachment
		multipart.addBodyPart(attachmentBodyPart);
	}
}
