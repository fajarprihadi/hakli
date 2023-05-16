package com.sds.hakli.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@Entity
public class Tpekerjaan {
	
	private Integer tpekerjaanpk;
	
	private Tanggota tanggota;
	
	private String alamatkantor;
	
	private Date createtime;
	
	private String createdby;
	
	private String faxkantor;
	
	private String jabatankantor;
	
	private String kabcode;
	
	private String kabname;
	
	private String keterangankerja;
	
	private String kotakantor;
	
	private String namakantor;
	
	private String nip;
	
	private String noskkantor;
	
	private String nostr;
	
	private Date tglstr;
	
	private String nosip;
	
	private Date tglsipmulai;
	
	private Date tglsipakhir;
	
	private String provcode;
	
	private String provname;
	
	private String telpkantor;
	
	private Date tglmulai;
	
	private Date tglakhir;
	
	private Date lastupdated;
	
	private String updatedby;
	
	private Mrumpun mrumpun;
	
	private Mkepegawaian mkepegawaian;
	
	private Mkepegawaiansub mkepegawaiansub;
	
	private String issisdmk;
	
	private Date sisdmklastupdated;
	
	
	@Id
	@SequenceGenerator(name = "tpekerjaan_seq", sequenceName = "tpekerjaan_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tpekerjaan_seq")
	public Integer getTpekerjaanpk() {
		return tpekerjaanpk;
	}

	public void setTpekerjaanpk(Integer tpekerjaanpk) {
		this.tpekerjaanpk = tpekerjaanpk;
	}

	@ManyToOne
	@JoinColumn(name = "tanggotafk")
	public Tanggota getTanggota() {
		return tanggota;
	}

	public void setTanggota(Tanggota tanggota) {
		this.tanggota = tanggota;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
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

	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastupdated() {
		return lastupdated;
	}

	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}

	public String getUpdatedby() {
		return updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}

	@ManyToOne
	@JoinColumn(name = "mkepegawaianfk")
	public Mkepegawaian getMkepegawaian() {
		return mkepegawaian;
	}

	public void setMkepegawaian(Mkepegawaian mkepegawaian) {
		this.mkepegawaian = mkepegawaian;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getKabcode() {
		return kabcode;
	}

	public void setKabcode(String kabcode) {
		this.kabcode = kabcode;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getProvcode() {
		return provcode;
	}

	public void setProvcode(String provcode) {
		this.provcode = provcode;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getKabname() {
		return kabname;
	}

	public void setKabname(String kabname) {
		this.kabname = kabname;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getProvname() {
		return provname;
	}

	public void setProvname(String provname) {
		this.provname = provname;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getAlamatkantor() {
		return alamatkantor;
	}

	public void setAlamatkantor(String alamatkantor) {
		this.alamatkantor = alamatkantor;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getFaxkantor() {
		return faxkantor;
	}

	public void setFaxkantor(String faxkantor) {
		this.faxkantor = faxkantor;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getJabatankantor() {
		return jabatankantor;
	}

	public void setJabatankantor(String jabatankantor) {
		this.jabatankantor = jabatankantor;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getKeterangankerja() {
		return keterangankerja;
	}

	public void setKeterangankerja(String keterangankerja) {
		this.keterangankerja = keterangankerja;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getKotakantor() {
		return kotakantor;
	}

	public void setKotakantor(String kotakantor) {
		this.kotakantor = kotakantor;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getNamakantor() {
		return namakantor;
	}

	public void setNamakantor(String namakantor) {
		this.namakantor = namakantor;
	}
	
	public String getNip() {
		return nip;
	}

	public void setNip(String nip) {
		this.nip = nip;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getNoskkantor() {
		return noskkantor;
	}

	public void setNoskkantor(String noskkantor) {
		this.noskkantor = noskkantor;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getTelpkantor() {
		return telpkantor;
	}

	public void setTelpkantor(String telpkantor) {
		this.telpkantor = telpkantor;
	}

	@ManyToOne
	@JoinColumn(name = "mrumpunfk")
	public Mrumpun getMrumpun() {
		return mrumpun;
	}

	public void setMrumpun(Mrumpun mrumpun) {
		this.mrumpun = mrumpun;
	}

	@ManyToOne
	@JoinColumn(name = "mkepegawaiansubfk")
	public Mkepegawaiansub getMkepegawaiansub() {
		return mkepegawaiansub;
	}

	public void setMkepegawaiansub(Mkepegawaiansub mkepegawaiansub) {
		this.mkepegawaiansub = mkepegawaiansub;
	}

	public String getNostr() {
		return nostr;
	}

	public void setNostr(String nostr) {
		this.nostr = nostr;
	}

	public Date getTglstr() {
		return tglstr;
	}

	public void setTglstr(Date tglstr) {
		this.tglstr = tglstr;
	}

	public String getNosip() {
		return nosip;
	}

	public void setNosip(String nosip) {
		this.nosip = nosip;
	}

	public Date getTglsipmulai() {
		return tglsipmulai;
	}

	public void setTglsipmulai(Date tglsipmulai) {
		this.tglsipmulai = tglsipmulai;
	}

	public Date getTglsipakhir() {
		return tglsipakhir;
	}

	public void setTglsipakhir(Date tglsipakhir) {
		this.tglsipakhir = tglsipakhir;
	}

	public String getIssisdmk() {
		return issisdmk;
	}

	public void setIssisdmk(String issisdmk) {
		this.issisdmk = issisdmk;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getSisdmklastupdated() {
		return sisdmklastupdated;
	}

	public void setSisdmklastupdated(Date sisdmklastupdated) {
		this.sisdmklastupdated = sisdmklastupdated;
	}

	
}
