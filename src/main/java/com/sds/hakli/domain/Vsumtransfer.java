package com.sds.hakli.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Vsumtransfer {

	private BigDecimal totalamount;
	private BigDecimal totaltransfered;
	
	@Id
	public BigDecimal getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(BigDecimal totalamount) {
		this.totalamount = totalamount;
	}
	public BigDecimal getTotaltransfered() {
		return totaltransfered;
	}
	public void setTotaltransfered(BigDecimal totaltransfered) {
		this.totaltransfered = totaltransfered;
	}
	
	
}
