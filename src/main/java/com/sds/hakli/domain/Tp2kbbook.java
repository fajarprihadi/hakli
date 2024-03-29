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
public class Tp2kbbook {

	private Integer tp2kbbookpk;
	private String nostr;
	private Date tglmulai;
	private Date tglakhir;
	private Date tgllulus;
	private String status;
	private Date createtime;
	private String createdby;
	private Date lastupdated;
	private String updatedby;
	private Integer totalkegiatan = 0;
	private BigDecimal totalskp = new BigDecimal(0);
	private Tanggota tanggota;
	private Muniversitas muniversitas;
	private String vano;
	private Date vacreatedat;
	private String ispaid;
	private String letterno;
	private String reviewerid;
	private String reviewername;
	private Date reviewtime;
	private String isscrapp2kb;
	
	
	@Id
	@SequenceGenerator(name = "tp2kbbook_seq", sequenceName = "tp2kbbook_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tp2kbbook_seq")
	public Integer getTp2kbbookpk() {
		return tp2kbbookpk;
	}
	public void setTp2kbbookpk(Integer tp2kbbookpk) {
		this.tp2kbbookpk = tp2kbbookpk;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getNostr() {
		return nostr;
	}
	public void setNostr(String nostr) {
		this.nostr = nostr;
	}
	
	@Temporal(TemporalType.DATE)
	public Date getTglmulai() {
		return tglmulai;
	}
	public void setTglmulai(Date tglmulai) {
		this.tglmulai = tglmulai;
	}
	
	@Temporal(TemporalType.DATE)
	public Date getTglakhir() {
		return tglakhir;
	}
	public void setTglakhir(Date tglakhir) {
		this.tglakhir = tglakhir;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastupdated() {
		return lastupdated;
	}
	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getUpdatedby() {
		return updatedby;
	}
	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}
	
	@ManyToOne
	@JoinColumn(name = "tanggotafk")
	public Tanggota getTanggota() {
		return tanggota;
	}
	public void setTanggota(Tanggota tanggota) {
		this.tanggota = tanggota;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public Integer getTotalkegiatan() {
		return totalkegiatan;
	}
	public void setTotalkegiatan(Integer totalkegiatan) {
		this.totalkegiatan = totalkegiatan;
	}
	public BigDecimal getTotalskp() {
		return totalskp;
	}
	public void setTotalskp(BigDecimal totalskp) {
		this.totalskp = totalskp;
	}
	public Date getTgllulus() {
		return tgllulus;
	}
	public void setTgllulus(Date tgllulus) {
		this.tgllulus = tgllulus;
	}
	@ManyToOne
	@JoinColumn(name = "muniversitasfk")
	public Muniversitas getMuniversitas() {
		return muniversitas;
	}
	public void setMuniversitas(Muniversitas muniversitas) {
		this.muniversitas = muniversitas;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getReviewtime() {
		return reviewtime;
	}
	public void setReviewtime(Date reviewtime) {
		this.reviewtime = reviewtime;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getVano() {
		return vano;
	}
	public void setVano(String vano) {
		this.vano = vano;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getVacreatedat() {
		return vacreatedat;
	}
	public void setVacreatedat(Date vacreatedat) {
		this.vacreatedat = vacreatedat;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getIspaid() {
		return ispaid;
	}
	public void setIspaid(String ispaid) {
		this.ispaid = ispaid;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getLetterno() {
		return letterno;
	}
	public void setLetterno(String letterno) {
		this.letterno = letterno;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getReviewerid() {
		return reviewerid;
	}
	public void setReviewerid(String reviewerid) {
		this.reviewerid = reviewerid;
	}
	
	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getReviewername() {
		return reviewername;
	}
	public void setReviewername(String reviewername) {
		this.reviewername = reviewername;
	}
	public String getIsscrapp2kb() {
		return isscrapp2kb;
	}
	public void setIsscrapp2kb(String isscrapp2kb) {
		this.isscrapp2kb = isscrapp2kb;
	}
	
	
}
