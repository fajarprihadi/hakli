package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Tp2kbb03;
import com.sds.utils.db.StoreHibernateUtil;

public class Tp2kbB03DAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tp2kbb03> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tp2kbb03> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from tp2kbb03  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Tp2kbb03.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tp2kbb03 "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tp2kbb03> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tp2kbb03> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tp2kbb03 where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	@SuppressWarnings("unchecked")
	public List<Tp2kbb03> listNativeByFilter(String filter, String orderby) throws Exception {		
    	List<Tp2kbb03> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Tp2kbb03 where " + filter + " order by " + orderby).addEntity(Tp2kbb03.class).list();
		session.close();
        return oList;
    }	
	
	public Tp2kbb03 findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kbb03 oForm = (Tp2kbb03) session.createQuery("from Tp2kbb03 where tp2kbb03pk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tp2kbb03 order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Tp2kbb03 findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kbb03 oForm = (Tp2kbb03) session.createQuery("from Tp2kbb03 where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Tp2kbb03 oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tp2kbb03 oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
