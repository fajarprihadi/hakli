package com.sds.hakli.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Vreportproduktivitas implements Serializable {
	private static final long serialVersionUID = 1L;
	private String checkedbyid;
	private String checkedby;
	private String usergroupname;
	private String cabang;
	private Integer total;
	
	@Id
	public String getCheckedbyid() {
		return checkedbyid;
	}
	public void setCheckedbyid(String checkedbyid) {
		this.checkedbyid = checkedbyid;
	}
	
	@Id
	public String getCheckedby() {
		return checkedby;
	}
	public void setCheckedby(String checkedby) {
		this.checkedby = checkedby;
	}
	
	@Id
	public String getUsergroupname() {
		return usergroupname;
	}
	public void setUsergroupname(String usergroupname) {
		this.usergroupname = usergroupname;
	}
	
	@Id
	public String getCabang() {
		return cabang;
	}
	public void setCabang(String cabang) {
		this.cabang = cabang;
	}
	
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}

}
