package com.sds.hakli.extension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.zk.ui.Executions;

import com.sds.hakli.dao.TmailingDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.domain.Tmailing;
import com.sds.utils.AppData;
import com.sds.utils.db.StoreHibernateUtil;

public class MailHandler implements Runnable {

	private SimpleDateFormat filenameFormatter = new SimpleDateFormat("MMyyyy");
	private SimpleDateFormat dateLocalFormatter = new SimpleDateFormat("dd-MM-yyyy");
	
	private Object obj;
	private String subject;
	private String recipient;
	private String bodymail;
	
	public MailHandler(Object obj, String subject, String recipient, String bodymail) {
		this.obj = obj;
		this.subject = subject;
		this.recipient = recipient;
		this.bodymail = bodymail;
	}
	
	@Override
	public void run() {
		String errormsg = "";
		MailBean mailbean = null;
		try {
			mailbean = AppData.getSmtpParam();	
			mailbean.setSmtpname("mail.bangzk.tech");
			mailbean.setSmtpport(587);
			mailbean.setMailid("admin@pphakli.org");
			mailbean.setMailpassword("Qwert1234!");
			mailbean.setFrom("HAKLI <admin@pphakli.org>");
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
					
					mailbean.setRecipient(recipient);
					mailbean.setSubject(subject);
					
					if (obj instanceof Tinvoice) {
						//mailbean.setRecipient(((Tinvoice)obj).getTanggota().getEmail());
						//mailbean.setSubject(((Tinvoice)obj).getInvoicedesc());
						
						bodymsg = bodymsg.replaceAll("%nama%", ((Tinvoice)obj).getTanggota().getNama());
						bodymsg = bodymsg.replaceAll("%noanggota%", ((Tinvoice)obj).getTanggota().getNoanggota());
						bodymsg = bodymsg.replaceAll("%email%", ((Tinvoice)obj).getTanggota().getEmail());
						bodymsg = bodymsg.replaceAll("%vano%", ((Tinvoice)obj).getVano());
						bodymsg = bodymsg.replaceAll("%invoiceno%", ((Tinvoice)obj).getInvoiceno());
						bodymsg = bodymsg.replaceAll("%invoiceamount%", NumberFormat.getInstance().format(((Tinvoice)obj).getInvoiceamount()));
						bodymsg = bodymsg.replaceAll("%invoicedesc%", ((Tinvoice)obj).getInvoicedesc());
						bodymsg = bodymsg.replaceAll("%invoicedate%", dateLocalFormatter.format(((Tinvoice)obj).getInvoicedate()));
						bodymsg = bodymsg.replaceAll("%invoiceduedate%", dateLocalFormatter.format(((Tinvoice)obj).getInvoiceduedate()));
						
					} else if (obj instanceof Tanggota) {
						//mailbean.setRecipient(((Tanggota)obj).getEmail());
						//mailbean.setSubject("Penolakan Permohonan Pendaftaran Keanggotaan HAKLI");
						bodymsg = bodymsg.replaceAll("%nama%", ((Tanggota)obj).getNama());
						bodymsg = bodymsg.replaceAll("%noanggota%", ((Tanggota)obj).getNoanggota());
						bodymsg = bodymsg.replaceAll("%password%", ((Tanggota)obj).getPassword());
						bodymsg = bodymsg.replaceAll("%regmemo%", ((Tanggota)obj).getRegmemo());
					}
					
					mailbean.setBodymsg(bodymsg);
				} 
				
			} catch (Exception e) {
				e.printStackTrace();
				errormsg = e.getMessage();
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
		
		Session session = StoreHibernateUtil.openSession();
		Transaction trx = session.beginTransaction();
		try {
			Tmailing mail = new Tmailing();
			mail.setSubject(mailbean.getSubject());
			mail.setMailtime(new Date());
			mail.setRecipient(recipient);
			mail.setMailerror(errormsg);
			if (errormsg.equals(""))
				mail.setMailstatus(1);
			else mail.setMailstatus(-1);
			
			if (obj instanceof Tinvoice) {
				mail.setTinvoice((Tinvoice) obj);
				mail.setTanggota(((Tinvoice) obj).getTanggota());
			} else if (obj instanceof Tanggota) {
				mail.setTanggota((Tanggota) obj);
			}
			
			new TmailingDAO().save(session, mail);
			trx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	
}