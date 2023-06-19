package com.sds.hakli.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@Entity
public class Tmailing {
	
	private Integer tmailingpk;
	
	private Tanggota tanggota;
	
	private Tinvoice tinvoice;
	
	private Date mailtime;
	
	private Integer mailstatus;
	
	private String subject;
	
	private String recipient;
	
	private String mailerror;
	
	@Id
	@SequenceGenerator(name = "tmailing_seq", sequenceName = "tmailing_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tmailing_seq")
	public Integer getTmailingpk() {
		return tmailingpk;
	}

	public void setTmailingpk(Integer tmailingpk) {
		this.tmailingpk = tmailingpk;
	}

	@ManyToOne
	@JoinColumn(name = "tanggotafk")
	public Tanggota getTanggota() {
		return tanggota;
	}

	public void setTanggota(Tanggota tanggota) {
		this.tanggota = tanggota;
	}

	@ManyToOne
	@JoinColumn(name = "tinvoicefk")
	public Tinvoice getTinvoice() {
		return tinvoice;
	}

	public void setTinvoice(Tinvoice tinvoice) {
		this.tinvoice = tinvoice;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getMailtime() {
		return mailtime;
	}

	public void setMailtime(Date mailtime) {
		this.mailtime = mailtime;
	}

	public Integer getMailstatus() {
		return mailstatus;
	}

	public void setMailstatus(Integer mailstatus) {
		this.mailstatus = mailstatus;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Type(type = "com.sds.utils.usertype.TrimLowerCaseUserType")
	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getMailerror() {
		return mailerror;
	}

	public void setMailerror(String mailerror) {
		this.mailerror = mailerror;
	}

	

}
