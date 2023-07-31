package com.sds.hakli.domain;

import java.util.Date;

import javax.json.bind.annotation.JsonbTransient;
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
public class Tanggota {
	
	private Integer tanggotapk;
	
	private String noanggota;
	
	private String nama;
	
	private String noktp;
	
	private String nostr;
	
	private Date tglstr;
	
	private String tempatlahir;
	
	private Date tgllahir;
	
	private String gender;
	
	private String agama;
	
	private String warganegara;
	
	private String gelardepan;
	
	private String gelarbelakang;
	
	private String provcode;
	
	private String kabcode;
	
	private String provname;
	
	private String kabname;
	
	private String alamat;
	
	private String kota;
	
	private String kodepos;
	
	private String telp;
	
	private String hp;
	
	private String email;
	
	private String statusanggota;
	
	private String namadarurat;
	
	private String hubungan;
	
	private String nohpdarurat;
	
	private String photolink;
	
	private Date lastlogin;
	
	private Date createtime;
	
	private String createdby;
	
	private Date lastupdated;
	
	private String updatedby;
	
	private Mcabang mcabang;
	
	private Mnegara mnegara;
	
	private String statusreg;
	
	private String regmemo;
	
	private String vano;
	
	private Date regdecisiontime;
	
	private String password;
	
	private Date periodekta;
	
	private Date periodektanext;
	
	private String vareg;
	
	private Integer varegstatus;
	
	private String vaevent;
	
	private Integer vaeventstatus;
	
	private String issisdmk;
	
	private Date sisdmklastupdated;
	
	private Musergroup musergroup;
	
	@Id
	@SequenceGenerator(name = "tanggota_seq", sequenceName = "tanggota_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tanggota_seq")
	public Integer getTanggotapk() {
		return tanggotapk;
	}
	public void setTanggotapk(Integer tanggotapk) {
		this.tanggotapk = tanggotapk;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getNoanggota() {
		return noanggota;
	}
	public void setNoanggota(String noanggota) {
		this.noanggota = noanggota;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getNoktp() {
		return noktp;
	}
	public void setNoktp(String noktp) {
		this.noktp = noktp;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getNostr() {
		return nostr;
	}
	public void setNostr(String nostr) {
		this.nostr = nostr;
	}
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getTempatlahir() {
		return tempatlahir;
	}
	public void setTempatlahir(String tempatlahir) {
		this.tempatlahir = tempatlahir;
	}
	
	public Date getTgllahir() {
		return tgllahir;
	}
	public void setTgllahir(Date tgllahir) {
		this.tgllahir = tgllahir;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getAgama() {
		return agama;
	}
	public void setAgama(String agama) {
		this.agama = agama;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getWarganegara() {
		return warganegara;
	}
	public void setWarganegara(String warganegara) {
		this.warganegara = warganegara;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getGelardepan() {
		return gelardepan;
	}
	public void setGelardepan(String gelardepan) {
		this.gelardepan = gelardepan;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getGelarbelakang() {
		return gelarbelakang;
	}
	public void setGelarbelakang(String gelarbelakang) {
		this.gelarbelakang = gelarbelakang;
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
	public String getProvname() {
		return provname;
	}
	public void setProvname(String provname) {
		this.provname = provname;
	}
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getKabname() {
		return kabname;
	}
	public void setKabname(String kabname) {
		this.kabname = kabname;
	}
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getKodepos() {
		return kodepos;
	}
	public void setKodepos(String kodepos) {
		this.kodepos = kodepos;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getTelp() {
		return telp;
	}
	public void setTelp(String telp) {
		this.telp = telp;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getHp() {
		return hp;
	}
	public void setHp(String hp) {
		this.hp = hp;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getStatusanggota() {
		return statusanggota;
	}
	public void setStatusanggota(String statusanggota) {
		this.statusanggota = statusanggota;
	}
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getNamadarurat() {
		return namadarurat;
	}
	public void setNamadarurat(String namadarurat) {
		this.namadarurat = namadarurat;
	}
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getHubungan() {
		return hubungan;
	}
	public void setHubungan(String hubungan) {
		this.hubungan = hubungan;
	}
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getNohpdarurat() {
		return nohpdarurat;
	}
	public void setNohpdarurat(String nohpdarurat) {
		this.nohpdarurat = nohpdarurat;
	}
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getPhotolink() {
		return photolink;
	}
	@JsonbTransient
	public void setPhotolink(String photolink) {
		this.photolink = photolink;
	}
	
//	@Temporal(TemporalType.TIMESTAMP)
//	public Date getLastlogin() {
//		return lastlogin;
//	}
//	public void setLastlogin(Date lastlogin) {
//		this.lastlogin = lastlogin;
//	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatetime() {
		return createtime;
	}
	@JsonbTransient
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getCreatedby() {
		return createdby;
	}
	@JsonbTransient
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastupdated() {
		return lastupdated;
	}
	@JsonbTransient
	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getUpdatedby() {
		return updatedby;
	}
	@JsonbTransient
	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}
	@ManyToOne
	@JoinColumn(name="mcabangfk")
	public Mcabang getMcabang() {
		return mcabang;
	}
	public void setMcabang(Mcabang mcabang) {
		this.mcabang = mcabang;
	}
	@ManyToOne
	@JoinColumn(name="mnegarafk")
	public Mnegara getMnegara() {
		return mnegara;
	}
	public void setMnegara(Mnegara mnegara) {
		this.mnegara = mnegara;
	}
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getStatusreg() {
		return statusreg;
	}
	public void setStatusreg(String statusreg) {
		this.statusreg = statusreg;
	}
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastlogin() {
		return lastlogin;
	}
	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getRegmemo() {
		return regmemo;
	}
	public void setRegmemo(String regmemo) {
		this.regmemo = regmemo;
	}
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getVano() {
		return vano;
	}
	public void setVano(String vano) {
		this.vano = vano;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRegdecisiontime() {
		return regdecisiontime;
	}
	public void setRegdecisiontime(Date regdecisiontime) {
		this.regdecisiontime = regdecisiontime;
	}
	public Date getPeriodekta() {
		return periodekta;
	}
	public void setPeriodekta(Date periodekta) {
		this.periodekta = periodekta;
	}
	
	public Date getPeriodektanext() {
		return periodektanext;
	}
	public void setPeriodektanext(Date periodektanext) {
		this.periodektanext = periodektanext;
	}
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getVareg() {
		return vareg;
	}
	public void setVareg(String vareg) {
		this.vareg = vareg;
	}
	public Integer getVaregstatus() {
		return varegstatus;
	}
	public void setVaregstatus(Integer varegstatus) {
		this.varegstatus = varegstatus;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getVaevent() {
		return vaevent;
	}
	public void setVaevent(String vaevent) {
		this.vaevent = vaevent;
	}
	public Integer getVaeventstatus() {
		return vaeventstatus;
	}
	public void setVaeventstatus(Integer vaeventstatus) {
		this.vaeventstatus = vaeventstatus;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getKota() {
		return kota;
	}
	public void setKota(String kota) {
		this.kota = kota;
	}
	public Date getTglstr() {
		return tglstr;
	}
	public void setTglstr(Date tglstr) {
		this.tglstr = tglstr;
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
	
	@ManyToOne
	@JoinColumn(name="musergroupfk")
	public Musergroup getMusergroup() {
		return musergroup;
	}
	public void setMusergroup(Musergroup musergroup) {
		this.musergroup = musergroup;
	}
	
}
