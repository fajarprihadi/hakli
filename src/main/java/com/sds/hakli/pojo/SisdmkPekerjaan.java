package com.sds.hakli.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SisdmkPekerjaan {

	@JsonProperty("KODE_UNIT")
	private String kode_unit;
	@JsonProperty("UNIT")
	private String unit;
	@JsonProperty("ALAMAT")
	private String alamat;
	@JsonProperty("KABKOT")
	private String kabkot;
	@JsonProperty("PROVINSI")
	private String provinsi;
	@JsonProperty("JENIS_SDMK")
	private String jenis_sdmk;
	@JsonProperty("STATUS")
	private String status;
	@JsonProperty("NIP")
	private String nip;
	@JsonProperty("STR")
	private String str;
	@JsonProperty("TANGGAL_STR")
	private String tanggal_str;
	@JsonProperty("SIP")
	private String sip;
	@JsonProperty("TANGGAL_SIP")
	private String tanggal_sip;
	@JsonProperty("TANGGAL_SIP_AKHIR")
	private String tanggal_sip_akhir;
	private String id_biodata;
	
	public String getKode_unit() {
		return kode_unit;
	}
	public void setKode_unit(String kode_unit) {
		this.kode_unit = kode_unit;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public String getKabkot() {
		return kabkot;
	}
	public void setKabkot(String kabkot) {
		this.kabkot = kabkot;
	}
	public String getProvinsi() {
		return provinsi;
	}
	public void setProvinsi(String provinsi) {
		this.provinsi = provinsi;
	}
	public String getJenis_sdmk() {
		return jenis_sdmk;
	}
	public void setJenis_sdmk(String jenis_sdmk) {
		this.jenis_sdmk = jenis_sdmk;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNip() {
		return nip;
	}
	public void setNip(String nip) {
		this.nip = nip;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public String getTanggal_str() {
		return tanggal_str;
	}
	public void setTanggal_str(String tanggal_str) {
		this.tanggal_str = tanggal_str;
	}
	public String getSip() {
		return sip;
	}
	public void setSip(String sip) {
		this.sip = sip;
	}
	public String getTanggal_sip() {
		return tanggal_sip;
	}
	public void setTanggal_sip(String tanggal_sip) {
		this.tanggal_sip = tanggal_sip;
	}
	public String getTanggal_sip_akhir() {
		return tanggal_sip_akhir;
	}
	public void setTanggal_sip_akhir(String tanggal_sip_akhir) {
		this.tanggal_sip_akhir = tanggal_sip_akhir;
	}
	public String getId_biodata() {
		return id_biodata;
	}
	public void setId_biodata(String id_biodata) {
		this.id_biodata = id_biodata;
	}
	
	
}
