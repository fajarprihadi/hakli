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
public class Tmutasi {

	private Integer tmutasipk;
	private String kodecabangcurr;
	private String kodeprovcurr;
	private String cabangcurr;
	private String provcurr;
	private String docid;
	private String docpath;
	private String status;
	private String memo;
	private String createdby;
	private Date createtime;
	private String checkedby;
	private Date checktime;
	private String decisionmemo;
	private Tanggota tanggota;
	private Mcabang mcabang;

	@Id
	@SequenceGenerator(name = "tmutasi_seq", sequenceName = "tmutasi_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tmutasi_seq")
	public Integer getTmutasipk() {
		return tmutasipk;
	}

	public void setTmutasipk(Integer tmutasipk) {
		this.tmutasipk = tmutasipk;
	}

	@Type(type = "com.sds.utils.usertype.TrimUpperCaseUserType")
	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getDocpath() {
		return docpath;
	}

	public void setDocpath(String docpath) {
		this.docpath = docpath;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
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
	@JoinColumn(name = "mcabangfk")
	public Mcabang getMcabang() {
		return mcabang;
	}

	public void setMcabang(Mcabang mcabang) {
		this.mcabang = mcabang;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getKodecabangcurr() {
		return kodecabangcurr;
	}

	public void setKodecabangcurr(String kodecabangcurr) {
		this.kodecabangcurr = kodecabangcurr;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getKodeprovcurr() {
		return kodeprovcurr;
	}

	public void setKodeprovcurr(String kodeprovcurr) {
		this.kodeprovcurr = kodeprovcurr;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getCabangcurr() {
		return cabangcurr;
	}

	public void setCabangcurr(String cabangcurr) {
		this.cabangcurr = cabangcurr;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getProvcurr() {
		return provcurr;
	}

	public void setProvcurr(String provcurr) {
		this.provcurr = provcurr;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getDecisionmemo() {
		return decisionmemo;
	}

	public void setDecisionmemo(String decisionmemo) {
		this.decisionmemo = decisionmemo;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getCheckedby() {
		return checkedby;
	}

	public void setCheckedby(String checkedby) {
		this.checkedby = checkedby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getChecktime() {
		return checktime;
	}

	public void setChecktime(Date checktime) {
		this.checktime = checktime;
	}

}
