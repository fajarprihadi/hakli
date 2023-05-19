package com.sds.hakli.extension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.mail.MessagingException;

import org.zkoss.zk.ui.Executions;

import com.sds.hakli.domain.Tinvoice;
import com.sds.utils.AppData;

public class MailHandler implements Runnable {

	private SimpleDateFormat filenameFormatter = new SimpleDateFormat("MMyyyy");
	private SimpleDateFormat dateLocalFormatter = new SimpleDateFormat("dd-MM-yyyy");
	
	private Tinvoice obj;
	private String bodymail;
	
	public MailHandler(Tinvoice obj, String bodymail) {
		this.obj = obj;
		this.bodymail = bodymail;
	}
	
	public static void main(String[] args) {
		MailHandler mail = new MailHandler(null, null);
		mail.run();
	}
	
	@Override
	public void run() {
		String errormsg = "";
		try {
			MailBean mailbean = AppData.getSmtpParam();
			mailbean.setAppid(obj.getTinvoicepk());
			//mailbean.setSmtpname("mail.sdd.co.id");
			//mailbean.setSmtpport(465);
			//mailbean.setMailid("fajar.prihadi@swadharma.com");
			//mailbean.setMailpassword("fprihadi3458");
			//mailbean.setRecipient(obj.getTanggota().getEmail());
			//mailbean.setFrom("HAKLI <fajar.prihadi@swadharma.com>");
			
			mailbean.setRecipient("fprihadi@gmail.com");
			mailbean.setSubject(obj.getInvoicedesc());
			
			try {
				if (obj != null) {
					File file = new File(bodymail);

					BufferedReader br = new BufferedReader(new FileReader(file));
					StringBuilder template = new StringBuilder();
					String st;
					while ((st = br.readLine()) != null) {
						template.append(st);
					}
					br.close();
					String bodymsg = template.toString();
					bodymsg = bodymsg.replaceAll("%nama%", obj.getTanggota().getNama());
					bodymsg = bodymsg.replaceAll("%email%", obj.getTanggota().getEmail());
					bodymsg = bodymsg.replaceAll("%vano%", obj.getVano());
					bodymsg = bodymsg.replaceAll("%invoiceno%", obj.getInvoiceno());
					bodymsg = bodymsg.replaceAll("%invoiceamount%", NumberFormat.getInstance().format(obj.getInvoiceamount()));
					bodymsg = bodymsg.replaceAll("%invoicedesc%", obj.getInvoicedesc());
					bodymsg = bodymsg.replaceAll("%invoicedate%", dateLocalFormatter.format(obj.getInvoicedate()));
					bodymsg = bodymsg.replaceAll("%invoiceduedate%", dateLocalFormatter.format(obj.getInvoiceduedate()));
					mailbean.setBodymsg(bodymsg);
				} 
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mailbean.setAttachment(null);
			mailbean.setFilename("");
			MailSender.sendSSLMessage(mailbean);
		} catch (MessagingException e) {
			errormsg = e.getMessage();
			e.printStackTrace();
		} catch (Exception e) {
			errormsg = e.getMessage();
			e.printStackTrace();
		}
	}
}