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
public class Tpendidikan {
	
	private Integer tpendidikanpk;
	
	private Tanggota tanggota;
	
	private Date createtime;
	
	private String createdby;
	
	private String peminatan1;
	
	private String peminatan2;
	
	private String periodethawal;
	
	private String periodeblawal;
	
	private String periodethakhir;
	
	private String periodeblakhir;
	
	private String noijazah;
	
	private String ijazahfilename;
	
	private String ijazahlink;
	
	private Date lastupdated;
	
	private String updatedby;
	
	private Muniversitas muniversitas;
	
	private Mjenjang mjenjang;
	
	@Id
	@SequenceGenerator(name = "tpendidikan_seq", sequenceName = "tpendidikan_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tpendidikan_seq")
	public Integer getTpendidikanpk() {
		return tpendidikanpk;
	}

	public void setTpendidikanpk(Integer tpendidikanpk) {
		this.tpendidikanpk = tpendidikanpk;
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

	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getIjazahfilename() {
		return ijazahfilename;
	}

	public void setIjazahfilename(String ijazahfilename) {
		this.ijazahfilename = ijazahfilename;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getIjazahlink() {
		return ijazahlink;
	}

	public void setIjazahlink(String ijazahlink) {
		this.ijazahlink = ijazahlink;
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
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getPeminatan1() {
		return peminatan1;
	}

	public void setPeminatan1(String peminatan1) {
		this.peminatan1 = peminatan1;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getPeminatan2() {
		return peminatan2;
	}

	public void setPeminatan2(String peminatan2) {
		this.peminatan2 = peminatan2;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getPeriodethawal() {
		return periodethawal;
	}

	public void setPeriodethawal(String periodethawal) {
		this.periodethawal = periodethawal;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getPeriodeblawal() {
		return periodeblawal;
	}

	public void setPeriodeblawal(String periodeblawal) {
		this.periodeblawal = periodeblawal;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getPeriodethakhir() {
		return periodethakhir;
	}

	public void setPeriodethakhir(String periodethakhir) {
		this.periodethakhir = periodethakhir;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getPeriodeblakhir() {
		return periodeblakhir;
	}

	public void setPeriodeblakhir(String periodeblakhir) {
		this.periodeblakhir = periodeblakhir;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getNoijazah() {
		return noijazah;
	}

	public void setNoijazah(String noijazah) {
		this.noijazah = noijazah;
	}

	@ManyToOne
	@JoinColumn(name = "muniversitasfk")
	public Muniversitas getMuniversitas() {
		return muniversitas;
	}

	public void setMuniversitas(Muniversitas muniversitas) {
		this.muniversitas = muniversitas;
	}

	@ManyToOne
	@JoinColumn(name = "mjenjangfk")
	public Mjenjang getMjenjang() {
		return mjenjang;
	}

	public void setMjenjang(Mjenjang mjenjang) {
		this.mjenjang = mjenjang;
	}	
	
}
