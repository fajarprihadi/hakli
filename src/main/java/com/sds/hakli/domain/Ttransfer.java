package com.sds.hakli.domain;

import java.math.BigDecimal;
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
public class Ttransfer {
	
	private Integer ttransferpk;
	
	private Tinvoice tinvoice;
	
	private String noreferral;
	
	private String sourceacc;
	
	private String benefacc;
	
	private String benefname;
	
	private String benefbankcode;
	
	private BigDecimal amount;
	
	private String feetype;
	
	private Date trxtime;
	
	private String remark;
	
	private String responsecode;
	
	private String responsedesc;
	
	private String errordesc;
	
	private String journalseq;
	
	private String trfto;
	
	private String trftocode;
	
	private String trftoname;
	
	private BigDecimal bankfee;
	
	private String bankrefno;
	
	@Id
	@SequenceGenerator(name = "ttransfer_seq", sequenceName = "ttransfer_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ttransfer_seq")
	public Integer getTtransferpk() {
		return ttransferpk;
	}

	public void setTtransferpk(Integer ttransferpk) {
		this.ttransferpk = ttransferpk;
	}

	@ManyToOne
	@JoinColumn(name = "tinvoicefk")
	public Tinvoice getTinvoice() {
		return tinvoice;
	}

	public void setTinvoice(Tinvoice tinvoice) {
		this.tinvoice = tinvoice;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getNoreferral() {
		return noreferral;
	}

	public void setNoreferral(String noreferral) {
		this.noreferral = noreferral;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getSourceacc() {
		return sourceacc;
	}

	public void setSourceacc(String sourceacc) {
		this.sourceacc = sourceacc;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getBenefacc() {
		return benefacc;
	}

	public void setBenefacc(String benefacc) {
		this.benefacc = benefacc;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getBenefname() {
		return benefname;
	}

	public void setBenefname(String benefname) {
		this.benefname = benefname;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getBenefbankcode() {
		return benefbankcode;
	}

	public void setBenefbankcode(String benefbankcode) {
		this.benefbankcode = benefbankcode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getFeetype() {
		return feetype;
	}

	public void setFeetype(String feetype) {
		this.feetype = feetype;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getTrxtime() {
		return trxtime;
	}

	public void setTrxtime(Date trxtime) {
		this.trxtime = trxtime;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getResponsecode() {
		return responsecode;
	}

	public void setResponsecode(String responsecode) {
		this.responsecode = responsecode;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getResponsedesc() {
		return responsedesc;
	}

	public void setResponsedesc(String responsedesc) {
		this.responsedesc = responsedesc;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getErrordesc() {
		return errordesc;
	}

	public void setErrordesc(String errordesc) {
		this.errordesc = errordesc;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getJournalseq() {
		return journalseq;
	}

	public void setJournalseq(String journalseq) {
		this.journalseq = journalseq;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getTrfto() {
		return trfto;
	}

	public void setTrfto(String trfto) {
		this.trfto = trfto;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getTrftocode() {
		return trftocode;
	}

	public void setTrftocode(String trftocode) {
		this.trftocode = trftocode;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getTrftoname() {
		return trftoname;
	}

	public void setTrftoname(String trftoname) {
		this.trftoname = trftoname;
	}

	public BigDecimal getBankfee() {
		return bankfee;
	}

	public void setBankfee(BigDecimal bankfee) {
		this.bankfee = bankfee;
	}

	public String getBankrefno() {
		return bankrefno;
	}

	public void setBankrefno(String bankrefno) {
		this.bankrefno = bankrefno;
	}

	
}
