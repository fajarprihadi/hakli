package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Tp2kbbook;
import com.sds.utils.db.StoreHibernateUtil;

public class Tp2kbbookDAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tp2kbbook> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tp2kbbook> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from Tp2kbbook  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Tp2kbbook.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tp2kbbook "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tp2kbbook> listAll() throws Exception {		
    	List<Tp2kbbook> oList = null;
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tp2kbbook order by Tp2kbbookpk").list();
		session.close();
        return oList;
    }	
	
	@SuppressWarnings("unchecked")
	public List<Tp2kbbook> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tp2kbbook> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tp2kbbook where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Tp2kbbook findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kbbook oForm = (Tp2kbbook) session.createQuery("from Tp2kbbook where Tp2kbbookpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tp2kbbook order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Tp2kbbook findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kbbook oForm = (Tp2kbbook) session.createQuery("from Tp2kbbook where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Tp2kbbook oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tp2kbbook oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
