package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Tp2kba05;
import com.sds.utils.db.StoreHibernateUtil;

public class Tp2kbA05DAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tp2kba05> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tp2kba05> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from tp2kba05  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Tp2kba05.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tp2kba05 "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tp2kba05> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tp2kba05> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tp2kba05 where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	@SuppressWarnings("unchecked")
	public List<Tp2kba05> listNativeByFilter(String filter, String orderby) throws Exception {		
    	List<Tp2kba05> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Tp2kba05 where " + filter + " order by " + orderby).addEntity(Tp2kba05.class).list();
		session.close();
        return oList;
    }
	
	public Tp2kba05 findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kba05 oForm = (Tp2kba05) session.createQuery("from Tp2kba05 where tp2kba05pk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tp2kba05 order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Tp2kba05 findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kba05 oForm = (Tp2kba05) session.createQuery("from Tp2kba05 where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Tp2kba05 oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tp2kba05 oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
