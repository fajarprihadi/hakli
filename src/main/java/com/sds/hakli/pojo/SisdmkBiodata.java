package com.sds.hakli.pojo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SisdmkBiodata {

	@JsonProperty("NIK")
	private String nik;
	@JsonProperty("NAMA")
	private String nama;
	@JsonProperty("JENIS_KELAMIN")
	private String jenis_kelamin;
	@JsonProperty("TEMPAT_LAHIR")
	private String tempat_lahir;
	@JsonProperty("TANGGAL_LAHIR")
	private Date tanggal_lahir;
	@JsonProperty("PEKERJAAN")
	private List<SisdmkPekerjaan> pekerjaan;
	@JsonProperty("PENDIDIKAN")
	private List<SisdmkPendidikan> pendidikan;
	public String getNik() {
		return nik;
	}
	public void setNik(String nik) {
		this.nik = nik;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getJenis_kelamin() {
		return jenis_kelamin;
	}
	public void setJenis_kelamin(String jenis_kelamin) {
		this.jenis_kelamin = jenis_kelamin;
	}
	public String getTempat_lahir() {
		return tempat_lahir;
	}
	public void setTempat_lahir(String tempat_lahir) {
		this.tempat_lahir = tempat_lahir;
	}
	public Date getTanggal_lahir() {
		return tanggal_lahir;
	}
	public void setTanggal_lahir(Date tanggal_lahir) {
		this.tanggal_lahir = tanggal_lahir;
	}
	public List<SisdmkPekerjaan> getPekerjaan() {
		return pekerjaan;
	}
	public void setPekerjaan(List<SisdmkPekerjaan> pekerjaan) {
		this.pekerjaan = pekerjaan;
	}
	public List<SisdmkPendidikan> getPendidikan() {
		return pendidikan;
	}
	public void setPendidikan(List<SisdmkPendidikan> pendidikan) {
		this.pendidikan = pendidikan;
	}
	
}
