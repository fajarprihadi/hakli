package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Tp2kbc02;
import com.sds.utils.db.StoreHibernateUtil;

public class Tp2kbC02DAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tp2kbc02> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tp2kbc02> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from tp2kbc02  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Tp2kbc02.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tp2kbc02 "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tp2kbc02> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tp2kbc02> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tp2kbc02 where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	@SuppressWarnings("unchecked")
	public List<Tp2kbc02> listNativeByFilter(String filter, String orderby) throws Exception {		
    	List<Tp2kbc02> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Tp2kbc02 where " + filter + " order by " + orderby).addEntity(Tp2kbc02.class).list();
		session.close();
        return oList;
    }	
	
	public Tp2kbc02 findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kbc02 oForm = (Tp2kbc02) session.createQuery("from Tp2kbc02 where tp2kbc02pk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tp2kbc02 order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Tp2kbc02 findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kbc02 oForm = (Tp2kbc02) session.createQuery("from Tp2kbc02 where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Tp2kbc02 oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tp2kbc02 oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
