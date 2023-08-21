package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Tp2kbe04;
import com.sds.utils.db.StoreHibernateUtil;

public class Tp2kbE04DAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tp2kbe04> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tp2kbe04> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from tp2kbe04  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Tp2kbe04.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tp2kbe04 "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tp2kbe04> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tp2kbe04> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tp2kbe04 where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	@SuppressWarnings("unchecked")
	public List<Tp2kbe04> listNativeByFilter(String filter, String orderby) throws Exception {		
    	List<Tp2kbe04> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Tp2kbe04 where " + filter + " order by " + orderby).addEntity(Tp2kbe04.class).list();
		session.close();
        return oList;
    }	
	
	public Tp2kbe04 findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kbe04 oForm = (Tp2kbe04) session.createQuery("from Tp2kbe04 where tp2kbe04pk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tp2kbe04 order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Tp2kbe04 findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kbe04 oForm = (Tp2kbe04) session.createQuery("from Tp2kbe04 where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Tp2kbe04 oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tp2kbe04 oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
