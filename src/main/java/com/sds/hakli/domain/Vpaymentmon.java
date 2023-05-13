package com.sds.hakli.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Vpaymentmon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String invoicetype;
	private Date paidtime;
	private BigDecimal paidamount;
	
	@Id
	public String getInvoicetype() {
		return invoicetype;
	}
	public void setInvoicetype(String invoicetype) {
		this.invoicetype = invoicetype;
	}
	@Id
	@Temporal(TemporalType.DATE)
	public Date getPaidtime() {
		return paidtime;
	}
	public void setPaidtime(Date paidtime) {
		this.paidtime = paidtime;
	}
	public BigDecimal getPaidamount() {
		return paidamount;
	}
	public void setPaidamount(BigDecimal paidamount) {
		this.paidamount = paidamount;
	}
	
	
}
