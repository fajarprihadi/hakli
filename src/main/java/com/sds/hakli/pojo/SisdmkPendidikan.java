package com.sds.hakli.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SisdmkPendidikan {

	@JsonProperty("JENJANG")
	private String jenjang;
	@JsonProperty("PRODI")
	private String prodi;
	@JsonProperty("PERGURUAN_TINGGI")
	private String perguruan_tinggi;
	@JsonProperty("TAHUN_LULUS")
	private String tahun_lulus;
	
	public String getJenjang() {
		return jenjang;
	}
	public void setJenjang(String jenjang) {
		this.jenjang = jenjang;
	}
	public String getProdi() {
		return prodi;
	}
	public void setProdi(String prodi) {
		this.prodi = prodi;
	}
	public String getPerguruan_tinggi() {
		return perguruan_tinggi;
	}
	public void setPerguruan_tinggi(String perguruan_tinggi) {
		this.perguruan_tinggi = perguruan_tinggi;
	}
	public String getTahun_lulus() {
		return tahun_lulus;
	}
	public void setTahun_lulus(String tahun_lulus) {
		this.tahun_lulus = tahun_lulus;
	}
	
	
}
