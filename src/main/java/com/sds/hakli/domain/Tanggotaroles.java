package com.sds.hakli.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tanggotaroles database table.
 * 
 */
@Entity
@Table(name="tanggotaroles")
@NamedQuery(name="Tanggotaroles.findAll", query="SELECT m FROM Tanggotaroles m")
public class Tanggotaroles implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer tanggotarolespk;
	private Tanggota tanggota;
	private Musergroup musergroup;

	public Tanggotaroles() {
	}


	@Id
	@SequenceGenerator(name="TANGGOTAROLES_TANGGOTAROLESPK_GENERATOR", sequenceName = "TANGGOTAROLES_SEQ", initialValue = 100, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TANGGOTAROLES_TANGGOTAROLESPK_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getTanggotarolespk() {
		return this.tanggotarolespk;
	}

	public void setTanggotarolespk(Integer tanggotarolespk) {
		this.tanggotarolespk = tanggotarolespk;
	}


	@ManyToOne
	@JoinColumn(name="tanggotafk", nullable=false)
	public Tanggota getTanggota() {
		return this.tanggota;
	}

	public void setTanggota(Tanggota tanggota) {
		this.tanggota = tanggota;
	}


	//bi-directional many-to-one association to Musergroup
	@ManyToOne
	@JoinColumn(name="musergroupfk", nullable=false)
	public Musergroup getMusergroup() {
		return this.musergroup;
	}

	public void setMusergroup(Musergroup musergroup) {
		this.musergroup = musergroup;
	}

}