package com.sds.hakli.domain;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
public class Mprov {

	private Integer mprovpk;
	
	private String provcode;
	
	private String provname;

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

}
