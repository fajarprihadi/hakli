package com.sds.hakli.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Type;

@Entity
public class Tevent {
	
	private Integer teventpk;
	
	private String eventid;
	
	private Date eventdate;
	
	private String eventdesc;
	
	private String eventname;
	
	private String eventimg;
	
	private String eventtype;
	
	private String eventlocation;
	
	private BigDecimal eventprice;
	
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

}
