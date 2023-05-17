package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Mkab;
import com.sds.utils.db.StoreHibernateUtil;

public class MkabDAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Mkab> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Mkab> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from mkab  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Mkab.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Mkab "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Mkab> listByFilter(String filter, String orderby) throws Exception {		
    	List<Mkab> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Mkab where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Mkab findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Mkab oForm = (Mkab) session.createQuery("from Mkab where mkabpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Mkab order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Mkab findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Mkab oForm = (Mkab) session.createQuery("from Mkab where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Mkab oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Mkab oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
