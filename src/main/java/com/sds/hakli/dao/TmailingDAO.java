package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Tmailing;
import com.sds.utils.db.StoreHibernateUtil;

public class TmailingDAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tmailing> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tmailing> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from tmailing  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Tmailing.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tmailing "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tmailing> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tmailing> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tmailing where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Tmailing findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tmailing oForm = (Tmailing) session.createQuery("from Tmailing where tmailingpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tmailing order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Tmailing findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tmailing oForm = (Tmailing) session.createQuery("from Tmailing where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Tmailing oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tmailing oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
