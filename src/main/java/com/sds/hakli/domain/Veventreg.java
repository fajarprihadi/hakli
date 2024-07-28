package com.sds.hakli.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Veventreg {

	private Integer teventpk;
	private Integer teventregpk;
	private Integer tanggotafk;
	private Date eventdate;
	private Date closedate;
	private String eventname;
	private String eventtype;
	private BigDecimal eventprice;
	private Date vacreatedat;
	private Date periodekta;
	private Date periodeborang;
	
	@Id
	public Integer getTeventpk() {
		return teventpk;
	}
	public void setTeventpk(Integer teventpk) {
		this.teventpk = teventpk;
	}
	public Integer getTeventregpk() {
		return teventregpk;
	}
	public void setTeventregpk(Integer teventregpk) {
		this.teventregpk = teventregpk;
	}
	public Integer getTanggotafk() {
		return tanggotafk;
	}
	public void setTanggotafk(Integer tanggotafk) {
		this.tanggotafk = tanggotafk;
	}
	public Date getEventdate() {
		return eventdate;
	}
	public void setEventdate(Date eventdate) {
		this.eventdate = eventdate;
	}
	public Date getClosedate() {
		return closedate;
	}
	public void setClosedate(Date closedate) {
		this.closedate = closedate;
	}
	public String getEventname() {
		return eventname;
	}
	public void setEventname(String eventname) {
		this.eventname = eventname;
	}
	public String getEventtype() {
		return eventtype;
	}
	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}
	public BigDecimal getEventprice() {
		return eventprice;
	}
	public void setEventprice(BigDecimal eventprice) {
		this.eventprice = eventprice;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getVacreatedat() {
		return vacreatedat;
	}
	public void setVacreatedat(Date vacreatedat) {
		this.vacreatedat = vacreatedat;
	}
	public Date getPeriodekta() {
		return periodekta;
	}
	public void setPeriodekta(Date periodekta) {
		this.periodekta = periodekta;
	}
	public Date getPeriodeborang() {
		return periodeborang;
	}
	public void setPeriodeborang(Date periodeborang) {
		this.periodeborang = periodeborang;
	}
	
	
}
