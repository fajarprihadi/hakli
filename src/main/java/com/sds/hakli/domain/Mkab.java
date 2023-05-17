package com.sds.hakli.domain;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
public class Mkab {

	private Integer mkabpk;
	
	private String provcode;
	
	private String kabcode;
	
	private String kabname;

	@Id
	@SequenceGenerator(name = "mkab_seq", sequenceName = "mkab_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mkab_seq")
	public Integer getMkabpk() {
		return mkabpk;
	}

	public void setMkabpk(Integer mkabpk) {
		this.mkabpk = mkabpk;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getProvcode() {
		return provcode;
	}

	public void setProvcode(String provcode) {
		this.provcode = provcode;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getKabcode() {
		return kabcode;
	}

	public void setKabcode(String kabcode) {
		this.kabcode = kabcode;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getKabname() {
		return kabname;
	}

	public void setKabname(String kabname) {
		this.kabname = kabname;
	}

}
