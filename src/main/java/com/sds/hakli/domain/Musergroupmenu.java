package com.sds.hakli.domain;

import java.io.Serializable;
import java.util.Comparator;

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
	
	public static Comparator<Musergroupmenu> fieldComparator = new Comparator<Musergroupmenu>() {

		public int compare(Musergroupmenu obj1, Musergroupmenu obj2) {
			Integer id1 = obj1.getMmenu().getMenuorderno();
			Integer id2 = obj2.getMmenu().getMenuorderno();
			
			return id1.compareTo(id2);
		}

	};

}