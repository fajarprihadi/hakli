package com.sds.hakli.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.domain.Vpaymentbranch;
import com.sds.hakli.domain.Vpaymentmon;
import com.sds.utils.db.StoreHibernateUtil;

public class TinvoiceDAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tinvoice> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tinvoice> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from tinvoice join tanggota on tanggotafk = tanggotapk join mcabang on mcabangfk = mcabangpk "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Tinvoice.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tinvoice join tanggota on tanggotafk = tanggotapk join mcabang on mcabangfk = mcabangpk "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tinvoice> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tinvoice> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tinvoice where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tinvoice> listPaidPendingDisburse(int limit) throws Exception {		
    	List<Tinvoice> oList = null;
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Tinvoice where ispaid = 'Y' and (istrfprov = 'N' or istrfkab = 'N') "
				+ "and tinvoicepk >= 6906 " //12 Feb 2024
				+ "order by tinvoicepk limit " + limit).addEntity(Tinvoice.class).list();
		session.close();
        return oList;
    }
	
	public Integer countPaidPendingDisburseProv(String invoicetype, String prov) throws Exception {		
		Integer count = 0;
    	session = StoreHibernateUtil.openSession();
    	count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tinvoice "
    			+ "join tanggota on tanggotafk = tanggotapk join mcabang on mcabangfk = mcabangpk "
    			+ "join mprov on mprovfk = mprovpk where ispaid = 'Y' and istrfprov != 'X' and invoicetype = '" + invoicetype + "' and provamount - provamounttrf > 0 and mprov.provname = '" + prov + "'").uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tinvoice> listPaidPendingDisburseProv(String invoicetype, String prov, int limit) throws Exception {		
    	List<Tinvoice> oList = null;
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Tinvoice "
				+ "join tanggota on tanggotafk = tanggotapk join mcabang on mcabangfk = mcabangpk "
				+ "join mprov on mprovfk = mprovpk "
				+ "where ispaid = 'Y' and istrfprov != 'X' and "
				+ "invoicetype = '" + invoicetype + "' and provamount - provamounttrf > 0 and mprov.provname = '" + prov + "' order by tinvoicepk limit " + limit).addEntity(Tinvoice.class).list();
		session.close();
        return oList;
    }
	
	public Integer countPaidPendingDisburseKab(String invoicetype, String kab) throws Exception {		
		Integer count = 0;
    	session = StoreHibernateUtil.openSession();
    	count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tinvoice "
    			+ "join tanggota on tanggotafk = tanggotapk join mcabang on mcabangfk = mcabangpk "
    			+ "where ispaid = 'Y' and istrfkab != 'X' and invoicetype = '" + invoicetype + "' and kabamount - kabamounttrf > 0 and mcabang.cabang = '" + kab + "'").uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tinvoice> listPaidPendingDisburseKab(String invoicetype, String kab, int limit) throws Exception {		
    	List<Tinvoice> oList = null;
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Tinvoice "
				+ "join tanggota on tanggotafk = tanggotapk join mcabang on mcabangfk = mcabangpk "
				+ "where ispaid = 'Y' and istrfkab != 'X' and "
				+ "invoicetype = '" + invoicetype + "' and kabamount - kabamounttrf > 0 and mcabang.cabang = '" + kab + "' order by tinvoicepk limit " + limit).addEntity(Tinvoice.class).list();
		session.close();
        return oList;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tinvoice> listPaidPendingDisburseKab() throws Exception {		
    	List<Tinvoice> oList = null;
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Tinvoice where ispaid = 'Y' and kabamount - kabamounttrf > 0 order by tinvoicepk").addEntity(Tinvoice.class).list();
		session.close();
        return oList;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tinvoice> listNative(String filter, String orderby) throws Exception {		
    	List<Tinvoice> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Tinvoice join tanggota on tanggotafk = tanggotapk join mcabang on mcabangfk = mcabangpk "
				+ "where " + filter + " order by " + orderby).addEntity(Tinvoice.class).list();
		session.close();
        return oList;
    }	
	
	public Tinvoice findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tinvoice oForm = (Tinvoice) session.createQuery("from Tinvoice where tinvoicepk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tinvoice order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Tinvoice findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tinvoice oForm = (Tinvoice) session.createQuery("from Tinvoice where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Tinvoice oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tinvoice oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }
	
	@SuppressWarnings("unchecked")
	public List<Vpaymentmon> listPaymentMonitor(String filter) throws Exception {		
    	List<Vpaymentmon> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("SELECT INVOICETYPE, DATE(PAIDTIME) AS PAIDTIME, SUM(PAIDAMOUNT) AS PAIDAMOUNT "
				+ "FROM TINVOICE JOIN TANGGOTA ON TANGGOTAFK = TANGGOTAPK JOIN MCABANG ON MCABANGFK = MCABANGPK WHERE ISPAID = 'Y' AND " + filter + " GROUP BY INVOICETYPE, DATE(PAIDTIME)").addEntity(Vpaymentmon.class).list();
		session.close();
        return oList;
    }
	
	@SuppressWarnings("unchecked")
	public List<Vpaymentbranch> listPaymentBranch(String filter) throws Exception {		
    	List<Vpaymentbranch> oList = null;
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("SELECT MCABANGPK, CABANG, MPROV.PROVNAME AS PROVNAME, DATE(PAIDTIME) AS PAIDTIME, SUM(PAIDAMOUNT) AS PAIDAMOUNT "
				+ "FROM TINVOICE JOIN TANGGOTA ON TANGGOTAFK = TANGGOTAPK JOIN MCABANG ON MCABANGFK = MCABANGPK JOIN MPROV ON MPROVFK = MPROVPK "
				+ "WHERE " + filter + " GROUP BY MCABANGPK, CABANG, MPROV.PROVNAME, DATE(PAIDTIME) ORDER BY MPROV.PROVNAME, CABANG").addEntity(Vpaymentbranch.class).list();
		session.close();
        return oList;
    }
	
	public BigDecimal sumAmount(String filter) throws Exception {
		BigDecimal amount = new BigDecimal(0);
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		amount = (BigDecimal) session.createSQLQuery("select coalesce(sum(invoiceamount),0) from Tinvoice  join tanggota on tanggotafk = tanggotapk join mcabang on mcabangfk = mcabangpk "
				+ "where " + filter).uniqueResult();
		session.close();
        return amount;
    }

}
