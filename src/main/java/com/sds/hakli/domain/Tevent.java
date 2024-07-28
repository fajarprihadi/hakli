package com.sds.hakli.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@Entity
public class Tevent {
	
	private Integer teventpk;
	
	private String bodymail;
	
	private Date docactivedate;
	
	private String eventid;
	
	private Date eventdate;
	
	private Date closedate;
	
	private String eventdesc;
	
	private String eventname;
	
	private String eventimg;
	
	private String eventtype;
	
	private String eventlocation;
	
	private String eventcity;
	
	private BigDecimal eventprice;
	
	private String iscert;
	
	private String certpath;
	
	private String isfree;
	
	private String ismember;
	
	private String isprivate;
	
	private String issharefee;
	
	private String isskp;
	
	private Integer skp;
	
	private BigDecimal feepusat;
	
	private BigDecimal feeprov;
	
	private BigDecimal feekab;
	
	private Date periodekta;
	
	private Date periodeborang;
	
	@Id
	@SequenceGenerator(name = "tevent_seq", sequenceName = "tevent_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tevent_seq")
	public Integer getTeventpk() {
		return teventpk;
	}

	public void setTeventpk(Integer teventpk) {
		this.teventpk = teventpk;
	}
	
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getBodymail() {
		return bodymail;
	}

	public void setBodymail(String bodymail) {
		this.bodymail = bodymail;
	}
	
	public Date getDocactivedate() {
		return docactivedate;
	}

	public void setDocactivedate(Date docactivedate) {
		this.docactivedate = docactivedate;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getEventid() {
		return eventid;
	}

	public void setEventid(String eventid) {
		this.eventid = eventid;
	}

	public Date getEventdate() {
		return eventdate;
	}

	public void setEventdate(Date eventdate) {
		this.eventdate = eventdate;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getClosedate() {
		return closedate;
	}

	public void setClosedate(Date closedate) {
		this.closedate = closedate;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getEventdesc() {
		return eventdesc;
	}

	public void setEventdesc(String eventdesc) {
		this.eventdesc = eventdesc;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getEventname() {
		return eventname;
	}

	public void setEventname(String eventname) {
		this.eventname = eventname;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getEventimg() {
		return eventimg;
	}

	public void setEventimg(String eventimg) {
		this.eventimg = eventimg;
	}

	public BigDecimal getEventprice() {
		return eventprice;
	}

	public void setEventprice(BigDecimal eventprice) {
		this.eventprice = eventprice;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getEventtype() {
		return eventtype;
	}

	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getEventlocation() {
		return eventlocation;
	}

	public void setEventlocation(String eventlocation) {
		this.eventlocation = eventlocation;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getEventcity() {
		return eventcity;
	}

	public void setEventcity(String eventcity) {
		this.eventcity = eventcity;
	}
	
	public String getIsprivate() {
		return isprivate;
	}

	public void setIsprivate(String isprivate) {
		this.isprivate = isprivate;
	}

	public String getIssharefee() {
		return issharefee;
	}

	public void setIssharefee(String issharefee) {
		this.issharefee = issharefee;
	}

	public String getIsskp() {
		return isskp;
	}

	public void setIsskp(String isskp) {
		this.isskp = isskp;
	}

	public Integer getSkp() {
		return skp;
	}

	public void setSkp(Integer skp) {
		this.skp = skp;
	}

	public BigDecimal getFeepusat() {
		return feepusat;
	}

	public void setFeepusat(BigDecimal feepusat) {
		this.feepusat = feepusat;
	}

	public BigDecimal getFeeprov() {
		return feeprov;
	}

	public void setFeeprov(BigDecimal feeprov) {
		this.feeprov = feeprov;
	}

	public BigDecimal getFeekab() {
		return feekab;
	}

	public void setFeekab(BigDecimal feekab) {
		this.feekab = feekab;
	}

	public String getIscert() {
		return iscert;
	}

	public void setIscert(String iscert) {
		this.iscert = iscert;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getCertpath() {
		return certpath;
	}

	public void setCertpath(String certpath) {
		this.certpath = certpath;
	}

	public String getIsfree() {
		return isfree;
	}

	public void setIsfree(String isfree) {
		this.isfree = isfree;
	}

	public String getIsmember() {
		return ismember;
	}

	public void setIsmember(String ismember) {
		this.ismember = ismember;
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
