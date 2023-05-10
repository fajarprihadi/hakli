package com.sds.hakli.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Vp2kb implements Serializable {
	private static final long serialVersionUID = 1L;
	private String cabang;
	private String noanggota;
	private String nama;
	private String alamat;
	private Integer totalwaiting;
	private Integer tanggotapk;
	
	@Id
	public String getCabang() {
		return cabang;
	}
	public void setCabang(String cabang) {
		this.cabang = cabang;
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
	
	@Id
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	
	public Integer getTotalwaiting() {
		return totalwaiting;
	}
	public void setTotalwaiting(Integer totalwaiting) {
		this.totalwaiting = totalwaiting;
	}
	
	@Id
	public Integer getTanggotapk() {
		return tanggotapk;
	}
	public void setTanggotapk(Integer tanggotapk) {
		this.tanggotapk = tanggotapk;
	}
	
	
	

}
