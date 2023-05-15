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
public class Tp2kbb03 {
	
	private Integer tp2kbb03pk;
	
	private Tanggota tanggota;
	
	private Mp2kbkegiatan mp2kbkegiatan;
	
	private String jenisjabatan;
	
	private String namajabatan;
	
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
	
	@Id
	@SequenceGenerator(name = "tp2kbb03_seq", sequenceName = "tp2kbb03_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tp2kbb03_seq")
	public Integer getTp2kbb03pk() {
		return tp2kbb03pk;
	}

	public void setTp2kbb03pk(Integer tp2kbb03pk) {
		this.tp2kbb03pk = tp2kbb03pk;
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
	
	public String getJenisjabatan() {
		return jenisjabatan;
	}

	public void setJenisjabatan(String jenisjabatan) {
		this.jenisjabatan = jenisjabatan;
	}

	public String getNamajabatan() {
		return namajabatan;
	}

	public void setNamajabatan(String namajabatan) {
		this.namajabatan = namajabatan;
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

	
}
