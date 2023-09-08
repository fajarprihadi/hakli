package com.sds.hakli.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Vp2kb {
	
	private Integer tp2kbbookpk;
	private Integer tanggotapk;
	private String cabang;
	private String nostr;
	private String noanggota;
	private String nama;
	private String alamat;
	private Date tglmulai;
	private Date tglakhir;
	private Integer totalkegiatanwv;
	
	@Id
	public Integer getTp2kbbookpk() {
		return tp2kbbookpk;
	}
	public void setTp2kbbookpk(Integer tp2kbbookpk) {
		this.tp2kbbookpk = tp2kbbookpk;
	}
	public Integer getTanggotapk() {
		return tanggotapk;
	}
	public void setTanggotapk(Integer tanggotapk) {
		this.tanggotapk = tanggotapk;
	}
	public String getCabang() {
		return cabang;
	}
	public void setCabang(String cabang) {
		this.cabang = cabang;
	}
	public String getNostr() {
		return nostr;
	}
	public void setNostr(String nostr) {
		this.nostr = nostr;
	}
	public String getNoanggota() {
		return noanggota;
	}
	public void setNoanggota(String noanggota) {
		this.noanggota = noanggota;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public Date getTglmulai() {
		return tglmulai;
	}
	public void setTglmulai(Date tglmulai) {
		this.tglmulai = tglmulai;
	}
	public Date getTglakhir() {
		return tglakhir;
	}
	public void setTglakhir(Date tglakhir) {
		this.tglakhir = tglakhir;
	}
	public Integer getTotalkegiatanwv() {
		return totalkegiatanwv;
	}
	public void setTotalkegiatanwv(Integer totalkegiatanwv) {
		this.totalkegiatanwv = totalkegiatanwv;
	}
	
	

}
