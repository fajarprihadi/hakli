package com.sds.hakli.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Vpaymentbranch {

	private Integer mcabangpk;
	private String cabang;
	private String provname;
	private Date paidtime;
	private BigDecimal paidamount;
	
	@Id
	public Integer getMcabangpk() {
		return mcabangpk;
	}
	public void setMcabangpk(Integer mcabangpk) {
		this.mcabangpk = mcabangpk;
	}
	public String getCabang() {
		return cabang;
	}
	public void setCabang(String cabang) {
		this.cabang = cabang;
	}
	public String getProvname() {
		return provname;
	}
	public void setProvname(String provname) {
		this.provname = provname;
	}
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
