package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.BranchTop;
import com.sds.hakli.domain.Tanggota;
import com.sds.utils.db.StoreHibernateUtil;

public class TanggotaDAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tanggota> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tanggota> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from tanggota join mcabang on mcabangfk = mcabangpk  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Tanggota.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tanggota join mcabang on mcabangfk = mcabangpk "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tanggota> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tanggota> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tanggota where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Tanggota findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tanggota oForm = (Tanggota) session.createQuery("from Tanggota where tanggotapk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tanggota order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Tanggota findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tanggota oForm = (Tanggota) session.createQuery("from Tanggota where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Tanggota oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tanggota oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }
	
	@SuppressWarnings("unchecked")
	public List<BranchTop> listBranchTop() {
		session = StoreHibernateUtil.openSession();
		List<BranchTop> oList = session.createSQLQuery("SELECT MPROVINSIPK AS ID, MPROVINSI.PROVNAME AS NAME, COUNT(MPROVINSIPK) AS TOTAL \r\n"
				+ "FROM MPROVINSI LEFT JOIN MCABANG ON MPROVINSI.MPROVINSIPK = MCABANG.MPROVINSIFK \r\n"
				+ "LEFT JOIN TANGGOTA ON MCABANGPK = MCABANGFK \r\n"
				+ "WHERE STATUSREG = '3' GROUP BY MPROVINSIPK, MPROVINSI.PROVNAME \r\n"
				+ "ORDER BY COUNT(MPROVINSIPK) DESC, MPROVINSI.PROVNAME").addEntity(BranchTop.class).list();
		session.close();
		return oList;
	}
	
	@SuppressWarnings("unchecked")
	public List<BranchTop> listBranchByProv(long provid) {
		session = StoreHibernateUtil.openSession();
		List<BranchTop> oList = session.createSQLQuery("SELECT MCABANGPK AS ID, CABANG AS NAME, COUNT(MCABANGPK) AS TOTAL \r\n"
				+ "FROM MCABANG LEFT JOIN TANGGOTA ON MCABANGPK = MCABANGFK \r\n"
				+ "WHERE STATUSREG = '3' AND MPROVINSIFK = " + provid + " GROUP BY MCABANGPK, CABANG \r\n"
				+ "ORDER BY COUNT(MCABANGPK) DESC, CABANG").addEntity(BranchTop.class).list();
		session.close();
		return oList;
	}
	
	@SuppressWarnings("unchecked")
	public List<BranchTop> listTop10() {
		session = StoreHibernateUtil.openSession();
		List<BranchTop> oList = session.createSQLQuery("SELECT MCABANGPK AS ID, CABANG AS NAME, COUNT(MCABANGFK) AS TOTAL "
				+ "FROM MCABANG LEFT JOIN TANGGOTA ON MCABANGPK = MCABANGFK WHERE STATUSREG = '3' "
				+ "GROUP BY MCABANGPK, CABANG "
				+ "ORDER BY COUNT(MCABANGFK) DESC, CABANG LIMIT 10").addEntity(BranchTop.class).list();
		session.close();
		return oList;
	}

}
