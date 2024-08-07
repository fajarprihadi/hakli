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
public class Tinvoice {
	
	private Integer tinvoicepk;
	
	private Tanggota tanggota;
	
	private Teventreg teventreg;
	
	private Tp2kbbook tp2kbbook;
	
	private Date createtime;
	
	private String createdby;
	
	private Date invoicedate;
	
	private Date invoiceduedate;
	
	private String invoicedesc;
	
	private String invoiceno;
	
	private String invoicetype;
	
	private BigDecimal invoiceamount;
	
	private String vano;
	
	private String ispaid;
	
	private Date paidtime;
	
	private String paidrefno;
	
	private BigDecimal paidamount;
	
	private Date vanotiftime;

	private String vaterminalid;
	
	private String istrfprov = "N";
	
	private Date trfprovtime;
	
	private String istrfkab = "N";
	
	private Date trfkabtime;
	
	private BigDecimal trfprovamount;
	
	private BigDecimal trfkabamount;
	
	private BigDecimal baseamount;
	
	private int invoiceqty;
	
	private BigDecimal provamount;
	
	private BigDecimal kabamount;
	
	private BigDecimal provamounttrf;
	
	private BigDecimal kabamounttrf;
	
	private String istrfsys = "N";
	private BigDecimal sysamounttrf;
	private Date trfsystime;
	
	
	@Id
	@SequenceGenerator(name = "tinvoice_seq", sequenceName = "tinvoice_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tinvoice_seq")
	public Integer getTinvoicepk() {
		return tinvoicepk;
	}

	public void setTinvoicepk(Integer tinvoicepk) {
		this.tinvoicepk = tinvoicepk;
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
	@JoinColumn(name = "teventregfk")
	public Teventreg getTeventreg() {
		return teventreg;
	}

	public void setTeventreg(Teventreg teventreg) {
		this.teventreg = teventreg;
	}
	
	@ManyToOne
	@JoinColumn(name = "tp2kbbookfk")
	public Tp2kbbook getTp2kbbook() {
		return tp2kbbook;
	}

	public void setTp2kbbook(Tp2kbbook tp2kbbook) {
		this.tp2kbbook = tp2kbbook;
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

	public Date getInvoicedate() {
		return invoicedate;
	}

	public void setInvoicedate(Date invoicedate) {
		this.invoicedate = invoicedate;
	}

	public Date getInvoiceduedate() {
		return invoiceduedate;
	}

	public void setInvoiceduedate(Date invoiceduedate) {
		this.invoiceduedate = invoiceduedate;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getInvoicedesc() {
		return invoicedesc;
	}

	public void setInvoicedesc(String invoicedesc) {
		this.invoicedesc = invoicedesc;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getInvoicetype() {
		return invoicetype;
	}

	public void setInvoicetype(String invoicetype) {
		this.invoicetype = invoicetype;
	}

	public BigDecimal getInvoiceamount() {
		return invoiceamount;
	}

	public void setInvoiceamount(BigDecimal invoiceamount) {
		this.invoiceamount = invoiceamount;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getVano() {
		return vano;
	}

	public void setVano(String vano) {
		this.vano = vano;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getIspaid() {
		return ispaid;
	}

	public void setIspaid(String ispaid) {
		this.ispaid = ispaid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getPaidtime() {
		return paidtime;
	}

	public void setPaidtime(Date paidtime) {
		this.paidtime = paidtime;
	}

	@Type(type = "com.sds.utils.usertype.TrimUserType")
	public String getPaidrefno() {
		return paidrefno;
	}

	public void setPaidrefno(String paidrefno) {
		this.paidrefno = paidrefno;
	}

	public BigDecimal getPaidamount() {
		return paidamount;
	}

	public void setPaidamount(BigDecimal paidamount) {
		this.paidamount = paidamount;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getVanotiftime() {
		return vanotiftime;
	}

	public void setVanotiftime(Date vanotiftime) {
		this.vanotiftime = vanotiftime;
	}

	public String getVaterminalid() {
		return vaterminalid;
	}

	public void setVaterminalid(String vaterminalid) {
		this.vaterminalid = vaterminalid;
	}

	public String getIstrfprov() {
		return istrfprov;
	}

	public void setIstrfprov(String istrfprov) {
		this.istrfprov = istrfprov;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getTrfprovtime() {
		return trfprovtime;
	}

	public void setTrfprovtime(Date trfprovtime) {
		this.trfprovtime = trfprovtime;
	}

	public String getIstrfkab() {
		return istrfkab;
	}

	public void setIstrfkab(String istrfkab) {
		this.istrfkab = istrfkab;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getTrfkabtime() {
		return trfkabtime;
	}

	public void setTrfkabtime(Date trfkabtime) {
		this.trfkabtime = trfkabtime;
	}

	public BigDecimal getTrfprovamount() {
		return trfprovamount;
	}

	public void setTrfprovamount(BigDecimal trfprovamount) {
		this.trfprovamount = trfprovamount;
	}

	public BigDecimal getTrfkabamount() {
		return trfkabamount;
	}

	public void setTrfkabamount(BigDecimal trfkabamount) {
		this.trfkabamount = trfkabamount;
	}

	public BigDecimal getBaseamount() {
		return baseamount;
	}

	public void setBaseamount(BigDecimal baseamount) {
		this.baseamount = baseamount;
	}

	public int getInvoiceqty() {
		return invoiceqty;
	}

	public void setInvoiceqty(int invoiceqty) {
		this.invoiceqty = invoiceqty;
	}

	public BigDecimal getProvamount() {
		return provamount;
	}

	public void setProvamount(BigDecimal provamount) {
		this.provamount = provamount;
	}

	public BigDecimal getKabamount() {
		return kabamount;
	}

	public void setKabamount(BigDecimal kabamount) {
		this.kabamount = kabamount;
	}

	public BigDecimal getProvamounttrf() {
		return provamounttrf;
	}

	public void setProvamounttrf(BigDecimal provamounttrf) {
		this.provamounttrf = provamounttrf;
	}

	public BigDecimal getKabamounttrf() {
		return kabamounttrf;
	}

	public void setKabamounttrf(BigDecimal kabamounttrf) {
		this.kabamounttrf = kabamounttrf;
	}

	public String getIstrfsys() {
		return istrfsys;
	}

	public void setIstrfsys(String istrfsys) {
		this.istrfsys = istrfsys;
	}

	public BigDecimal getSysamounttrf() {
		return sysamounttrf;
	}

	public void setSysamounttrf(BigDecimal sysamounttrf) {
		this.sysamounttrf = sysamounttrf;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getTrfsystime() {
		return trfsystime;
	}

	public void setTrfsystime(Date trfsystime) {
		this.trfsystime = trfsystime;
	}

}
