package com.sds.hakli.domain;

import java.math.BigDecimal;
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
public class Tp2kbe08 {
	
	private Integer tp2kbe08pk;
	
	private Tanggota tanggota;
	
	private Mp2kbkegiatan mp2kbkegiatan;
	
	private String judul;
	
	private String jeniskegiatan;
	
	private String docid;
	
	private String docpath;
	
	private Date tglmulai;
	
	private Date tglakhir;
	
	private BigDecimal nilaiskp;
	
	private String status;
	
	private String memo;
	
	private String memokomisi;
	
	private String createdby;
	
	private Date createtime;
	
	private String checkedby;
	
	private Date checktime;
	
	private String checkedbykomisi;

	private Date checktimekomisi;
	
	private String checkedbyid;
	
	private Tp2kbbook tp2kbbook;

	@ManyToOne
	@JoinColumn(name = "tp2kbbookfk")
	public Tp2kbbook getTp2kbbook() {
		return tp2kbbook;
	}

	public void setTp2kbbook(Tp2kbbook tp2kbbook) {
		this.tp2kbbook = tp2kbbook;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getCheckedbyid() {
		return checkedbyid;
	}

	public void setCheckedbyid(String checkedbyid) {
		this.checkedbyid = checkedbyid;
	}
	
	@Id
	@SequenceGenerator(name = "tp2kbe08_seq", sequenceName = "tp2kbe08_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tp2kbe08_seq")
	public Integer getTp2kbe08pk() {
		return tp2kbe08pk;
	}

	public void setTp2kbe08pk(Integer tp2kbe08pk) {
		this.tp2kbe08pk = tp2kbe08pk;
	}

	@ManyToOne
	@JoinColumn(name = "tanggotafk")
	public Tanggota getTanggota() {
		return tanggota;
	}

	public void setTanggota(Tanggota tanggota) {
		this.tanggota = tanggota;
	}

	@ManyToOne
	@JoinColumn(name = "mp2kbkegiatanfk")
	public Mp2kbkegiatan getMp2kbkegiatan() {
		return mp2kbkegiatan;
	}

	public void setMp2kbkegiatan(Mp2kbkegiatan mp2kbkegiatan) {
		this.mp2kbkegiatan = mp2kbkegiatan;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getJeniskegiatan() {
		return jeniskegiatan;
	}

	public void setJeniskegiatan(String jeniskegiatan) {
		this.jeniskegiatan = jeniskegiatan;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getJudul() {
		return judul;
	}

	public void setJudul(String judul) {
		this.judul = judul;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getDocpath() {
		return docpath;
	}

	public void setDocpath(String docpath) {
		this.docpath = docpath;
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

	public BigDecimal getNilaiskp() {
		return nilaiskp;
	}

	public void setNilaiskp(BigDecimal nilaiskp) {
		this.nilaiskp = nilaiskp;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getCheckedby() {
		return checkedby;
	}

	public void setCheckedby(String checkedby) {
		this.checkedby = checkedby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getChecktime() {
		return checktime;
	}

	public void setChecktime(Date checktime) {
		this.checktime = checktime;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getMemokomisi() {
		return memokomisi;
	}

	public void setMemokomisi(String memokomisi) {
		this.memokomisi = memokomisi;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getCheckedbykomisi() {
		return checkedbykomisi;
	}

	public void setCheckedbykomisi(String checkedbykomisi) {
		this.checkedbykomisi = checkedbykomisi;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getChecktimekomisi() {
		return checktimekomisi;
	}

	public void setChecktimekomisi(Date checktimekomisi) {
		this.checktimekomisi = checktimekomisi;
	}	
}
