package com.sds.hakli.domain;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
public class Mprov {

	private Integer mprovpk;
	
	private String provcode;
	
	private String provname;
	
	private String accno;
	
	private String accname;
	
	private String bankcode;
	
	private String bankname;
	
	private String ketuaid;
	
	private String ketuanama;
	
	private String sekretaris1id;
	
	private String sekretaris1nama;
	
	private String sekretaris2id;
	
	private String sekretaris2nama;
	
	private String bendahara1id;
	
	private String bendahara1nama;
	
	private String bendahara2id;
	
	private String bendahara2nama;

	@Id
	@SequenceGenerator(name = "mprov_seq", sequenceName = "mprov_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mprov_seq")
	public Integer getMprovpk() {
		return mprovpk;
	}

	public void setMprovpk(Integer mprovpk) {
		this.mprovpk = mprovpk;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getProvcode() {
		return provcode;
	}

	public void setProvcode(String provcode) {
		this.provcode = provcode;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getProvname() {
		return provname;
	}

	public void setProvname(String provname) {
		this.provname = provname;
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
	public String getKetuaid() {
		return ketuaid;
	}

	public void setKetuaid(String ketuaid) {
		this.ketuaid = ketuaid;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getKetuanama() {
		return ketuanama;
	}

	public void setKetuanama(String ketuanama) {
		this.ketuanama = ketuanama;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getSekretaris1id() {
		return sekretaris1id;
	}

	public void setSekretaris1id(String sekretaris1id) {
		this.sekretaris1id = sekretaris1id;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getSekretaris1nama() {
		return sekretaris1nama;
	}

	public void setSekretaris1nama(String sekretaris1nama) {
		this.sekretaris1nama = sekretaris1nama;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getSekretaris2id() {
		return sekretaris2id;
	}

	public void setSekretaris2id(String sekretaris2id) {
		this.sekretaris2id = sekretaris2id;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getSekretaris2nama() {
		return sekretaris2nama;
	}

	public void setSekretaris2nama(String sekretaris2nama) {
		this.sekretaris2nama = sekretaris2nama;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getBendahara1id() {
		return bendahara1id;
	}

	public void setBendahara1id(String bendahara1id) {
		this.bendahara1id = bendahara1id;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getBendahara1nama() {
		return bendahara1nama;
	}

	public void setBendahara1nama(String bendahara1nama) {
		this.bendahara1nama = bendahara1nama;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getBendahara2id() {
		return bendahara2id;
	}

	public void setBendahara2id(String bendahara2id) {
		this.bendahara2id = bendahara2id;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getBendahara2nama() {
		return bendahara2nama;
	}

	public void setBendahara2nama(String bendahara2nama) {
		this.bendahara2nama = bendahara2nama;
	}
	
}
