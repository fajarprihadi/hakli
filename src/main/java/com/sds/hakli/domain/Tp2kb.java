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

@Entity
public class Tp2kb {
	
	private Integer tp2kbpk;
	
	private Tanggota tanggota;
	
	private Mp2kbkegiatan mp2kbkegiatan;
	
	private Integer totalkegiatan;
	
	private BigDecimal totalskp;
	
	private Date lastupdated;
	
	private Integer totalwaiting;
	
	@Id
	@SequenceGenerator(name = "tp2kb_seq", sequenceName = "tp2kb_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tp2kb_seq")
	public Integer getTp2kbpk() {
		return tp2kbpk;
	}

	public void setTp2kbpk(Integer tp2kbpk) {
		this.tp2kbpk = tp2kbpk;
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

	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastupdated() {
		return lastupdated;
	}

	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}

	public Integer getTotalwaiting() {
		return totalwaiting;
	}

	public void setTotalwaiting(Integer totalwaiting) {
		this.totalwaiting = totalwaiting;
	}
	
}
