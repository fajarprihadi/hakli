package com.sds.hakli.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

/**
 * The persistent class for the TSCHEDULER database table.
 * 
 */
@Entity
@Table(name = "TSCHEDULER")
@NamedQuery(name = "Tscheduler.findAll", query = "SELECT t FROM Tscheduler t")
public class Tscheduler implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer tschedulerpk;
	private String jobclass;
	private Date lastupdated;
	private Integer repeatinterval;
	private String schedulerdesc;
	private String schedulergroup;
	private String schedulername;
	private String schedulerrepeattype;
	private String schedulerstatus;
	private String updatedby;

	public Tscheduler() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Integer getTschedulerpk() {
		return this.tschedulerpk;
	}

	public void setTschedulerpk(Integer tschedulerpk) {
		this.tschedulerpk = tschedulerpk;
	}

	@Column(length = 70)
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getJobclass() {
		return this.jobclass;
	}

	public void setJobclass(String jobclass) {
		this.jobclass = jobclass;
	}

	public Integer getRepeatinterval() {
		return this.repeatinterval;
	}

	public void setRepeatinterval(Integer repeatinterval) {
		this.repeatinterval = repeatinterval;
	}

	@Column(length = 200)
	@Type(type = "com.sds.utils.usertype.TrimUpperCaseUserType")
	public String getSchedulerdesc() {
		return this.schedulerdesc;
	}

	public void setSchedulerdesc(String schedulerdesc) {
		this.schedulerdesc = schedulerdesc;
	}

	@Column(length = 30)
	@Type(type = "com.sds.utils.usertype.TrimUpperCaseUserType")
	public String getSchedulergroup() {
		return this.schedulergroup;
	}

	public void setSchedulergroup(String schedulergroup) {
		this.schedulergroup = schedulergroup;
	}

	@Column(length = 30)
	@Type(type = "com.sds.utils.usertype.TrimUpperCaseUserType")
	public String getSchedulername() {
		return this.schedulername;
	}

	public void setSchedulername(String schedulername) {
		this.schedulername = schedulername;
	}

	@Column(length = 10)
	@Type(type = "com.sds.utils.usertype.TrimUpperCaseUserType")
	public String getSchedulerrepeattype() {
		return this.schedulerrepeattype;
	}

	public void setSchedulerrepeattype(String schedulerrepeattype) {
		this.schedulerrepeattype = schedulerrepeattype;
	}

	@Column(length = 3)
	@Type(type = "com.sds.utils.usertype.TrimUpperCaseUserType")
	public String getSchedulerstatus() {
		return this.schedulerstatus;
	}

	public void setSchedulerstatus(String schedulerstatus) {
		this.schedulerstatus = schedulerstatus;
	}

	@Column(length = 30)
	@Type(type = "com.sds.utils.usertype.TrimUpperCaseUserType")
	public String getUpdatedby() {
		return this.updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}

	public Date getLastupdated() {
		return lastupdated;
	}

}