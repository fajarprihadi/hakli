package com.sds.hakli.domain;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
public class Mcabang {

	private Integer mcabangpk;
	
	private String cabang;
	
	private String kodecabang;
	
	private String accno;
	
	private String accname;
	
	private String bankcode;
	
	private String bankname;
	
	private String provcode;
	
	private Date createtime;
	
	private String createdby;
	
	private Date lastupdated;
	
	private String updatedby;
	
	private Mprov mprov;

	@Id
	@SequenceGenerator(name = "mcabang_seq", sequenceName = "mcabang_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mcabang_seq")
	public Integer getMcabangpk() {
		return mcabangpk;
	}

	public void setMcabangpk(Integer mcabangpk) {
		this.mcabangpk = mcabangpk;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getCabang() {
		return cabang;
	}

	public void setCabang(String cabang) {
		this.cabang = cabang;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getKodecabang() {
		return kodecabang;
	}

	public void setKodecabang(String kodecabang) {
		this.kodecabang = kodecabang;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getAccno() {
		return accno;
	}

	public void setAccno(String accno) {
		this.accno = accno;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getAccname() {
		return accname;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getBankcode() {
		return bankcode;
	}

	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getProvcode() {
		return provcode;
	}

	public void setProvcode(String provcode) {
		this.provcode = provcode;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public Date getLastupdated() {
		return lastupdated;
	}

	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getUpdatedby() {
		return updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}

	@ManyToOne
	@JoinColumn(name="mprovfk")
	public Mprov getMprov() {
		return mprov;
	}

	public void setMprov(Mprov mprov) {
		this.mprov = mprov;
	}

}
