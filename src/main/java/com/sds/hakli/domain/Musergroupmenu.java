package com.sds.hakli.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the musergroupmenu database table.
 * 
 */
@Entity
@Table(name="musergroupmenu")
@NamedQuery(name="Musergroupmenu.findAll", query="SELECT m FROM Musergroupmenu m")
public class Musergroupmenu implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer musergroupmenupk;
	private Mmenu mmenu;
	private Musergroup musergroup;

	public Musergroupmenu() {
	}


	@Id
	@SequenceGenerator(name="MUSERGROUPMENU_MUSERGROUPMENUPK_GENERATOR", sequenceName = "MUSERGROUPMENU_SEQ", initialValue = 100, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MUSERGROUPMENU_MUSERGROUPMENUPK_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getMusergroupmenupk() {
		return this.musergroupmenupk;
	}

	public void setMusergroupmenupk(Integer musergroupmenupk) {
		this.musergroupmenupk = musergroupmenupk;
	}


	//bi-directional many-to-one association to Mmenu
	@ManyToOne
	@JoinColumn(name="mmenufk", nullable=false)
	public Mmenu getMmenu() {
		return this.mmenu;
	}

	public void setMmenu(Mmenu mmenu) {
		this.mmenu = mmenu;
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