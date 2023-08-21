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
public class Teventreg {
	
	private Integer teventregpk;
	
	private Tevent tevent;
	
	private Tanggota tanggota;
	
	private Date vacreatedat;
	
	private String vano;
	
	private BigDecimal vaamount;
	
	private Date vaexpdate;
	
	private String ispaid;
	
	private BigDecimal paidamount;
	
	private Date paidat;
	
	private Date vanotiftime;

	private String vajournalseq;
	
	private String vaterminalid;
	
	private String spno;
	
	private String keno;
	
	@Id
	@SequenceGenerator(name = "teventreg_seq", sequenceName = "teventreg_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teventreg_seq")
	public Integer getTeventregpk() {
		return teventregpk;
	}

	public void setTeventregpk(Integer teventregpk) {
		this.teventregpk = teventregpk;
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
	@JoinColumn(name = "teventfk")
	public Tevent getTevent() {
		return tevent;
	}

	public void setTevent(Tevent tevent) {
		this.tevent = tevent;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getVacreatedat() {
		return vacreatedat;
	}

	public void setVacreatedat(Date vacreatedat) {
		this.vacreatedat = vacreatedat;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getVano() {
		return vano;
	}

	public void setVano(String vano) {
		this.vano = vano;
	}

	public BigDecimal getVaamount() {
		return vaamount;
	}

	public void setVaamount(BigDecimal vaamount) {
		this.vaamount = vaamount;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getVaexpdate() {
		return vaexpdate;
	}

	public void setVaexpdate(Date vaexpdate) {
		this.vaexpdate = vaexpdate;
	}

	public String getIspaid() {
		return ispaid;
	}

	public void setIspaid(String ispaid) {
		this.ispaid = ispaid;
	}

	public BigDecimal getPaidamount() {
		return paidamount;
	}

	public void setPaidamount(BigDecimal paidamount) {
		this.paidamount = paidamount;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getPaidat() {
		return paidat;
	}

	public void setPaidat(Date paidat) {
		this.paidat = paidat;
	}

	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getVanotiftime() {
		return vanotiftime;
	}

	public void setVanotiftime(Date vanotiftime) {
		this.vanotiftime = vanotiftime;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getVajournalseq() {
		return vajournalseq;
	}

	public void setVajournalseq(String vajournalseq) {
		this.vajournalseq = vajournalseq;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getVaterminalid() {
		return vaterminalid;
	}

	public void setVaterminalid(String vaterminalid) {
		this.vaterminalid = vaterminalid;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getSpno() {
		return spno;
	}

	public void setSpno(String spno) {
		this.spno = spno;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getKeno() {
		return keno;
	}

	public void setKeno(String keno) {
		this.keno = keno;
	}
	
}
