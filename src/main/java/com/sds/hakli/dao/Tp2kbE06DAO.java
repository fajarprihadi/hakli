package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Tp2kbe06;
import com.sds.utils.db.StoreHibernateUtil;

public class Tp2kbE06DAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tp2kbe06> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tp2kbe06> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from tp2kbe06  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Tp2kbe06.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tp2kbe06 "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tp2kbe06> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tp2kbe06> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tp2kbe06 where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	@SuppressWarnings("unchecked")
	public List<Tp2kbe06> listNativeByFilter(String filter, String orderby) throws Exception {		
    	List<Tp2kbe06> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Tp2kbe06 where " + filter + " order by " + orderby).addEntity(Tp2kbe06.class).list();
		session.close();
        return oList;
    }
	
	public Tp2kbe06 findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kbe06 oForm = (Tp2kbe06) session.createQuery("from Tp2kbe06 where tp2kbe06pk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tp2kbe06 order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Tp2kbe06 findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kbe06 oForm = (Tp2kbe06) session.createQuery("from Tp2kbe06 where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Tp2kbe06 oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tp2kbe06 oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
