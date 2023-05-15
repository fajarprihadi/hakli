package com.sds.hakli.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Id;

public class Vrecp2kb implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer tanggotapk;
	private String noanggota;
	private String nama;
	private BigDecimal totalskp;
	
	@Id
	public Integer getTanggotapk() {
		return tanggotapk;
	}
	public void setTanggotapk(Integer tanggotapk) {
		this.tanggotapk = tanggotapk;
	}
	
	@Id
	public String getNoanggota() {
		return noanggota;
	}
	public void setNoanggota(String noanggota) {
		this.noanggota = noanggota;
	}
	
	@Id
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	
	public BigDecimal getTotalskp() {
		return totalskp;
	}
	public void setTotalskp(BigDecimal totalskp) {
		this.totalskp = totalskp;
	}
	
	
}
