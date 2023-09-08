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
	
	@SuppressWarnings("unchecked")
	public List<Tanggota> listNative(String filter, String orderby) throws Exception {		
    	List<Tanggota> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from tanggota join mcabang on mcabangfk = mcabangpk "
				+ "where " + filter + " order by " + orderby).addEntity(Tanggota.class).list();
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
		List<BranchTop> oList = session.createSQLQuery("SELECT MPROVPK AS ID, MPROV.PROVNAME AS NAME, COUNT(MPROVPK) AS TOTAL \r\n"
				+ "FROM MPROV LEFT JOIN MCABANG ON MPROV.MPROVPK = MCABANG.MPROVFK \r\n"
				+ "LEFT JOIN TANGGOTA ON MCABANGPK = MCABANGFK \r\n"
				+ "WHERE STATUSREG = '3' GROUP BY MPROVPK, MPROV.PROVNAME \r\n"
				+ "ORDER BY COUNT(MPROVPK) DESC, MPROV.PROVNAME").addEntity(BranchTop.class).list();
		session.close();
		return oList;
	}
	
	@SuppressWarnings("unchecked")
	public List<BranchTop> listBranchByProv(long provid) {
		session = StoreHibernateUtil.openSession();
		List<BranchTop> oList = session.createSQLQuery("SELECT MCABANGPK AS ID, CABANG AS NAME, COUNT(MCABANGPK) AS TOTAL \r\n"
				+ "FROM MCABANG LEFT JOIN TANGGOTA ON MCABANGPK = MCABANGFK \r\n"
				+ "WHERE STATUSREG = '3' AND MPROVFK = " + provid + " GROUP BY MCABANGPK, CABANG \r\n"
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
	
	@SuppressWarnings("unchecked")
	public List<BranchTop> listSumPendingReg(String filter) throws Exception {
		List<BranchTop> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("SELECT MPROVPK AS ID, MPROV.PROVNAME AS NAME, COUNT(*) AS TOTAL FROM TANGGOTA " + 
				"JOIN MCABANG ON MCABANGFK = MCABANGPK JOIN MPROV ON MPROVFK = MPROVPK " +
				"WHERE STATUSREG = '1' AND " + filter + " GROUP BY MPROVPK, MPROV.PROVNAME ORDER BY COUNT(*) DESC").addEntity(BranchTop.class)
				.list();
		session.close();
		return oList;
	}
	
	@SuppressWarnings("unchecked")
	public List<BranchTop> listSumPendingRegKab(String filter) throws Exception {
		List<BranchTop> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("SELECT MCABANGPK AS ID, CABANG AS NAME, COUNT(*) AS TOTAL FROM TANGGOTA " + 
				"JOIN MCABANG ON MCABANGFK = MCABANGPK " +
				"WHERE STATUSREG = '1' AND " + filter + " " + 
				"GROUP BY MCABANGPK, CABANG ORDER BY COUNT(*) DESC").addEntity(BranchTop.class)
				.list();
		session.close();
		return oList;
	}

}
