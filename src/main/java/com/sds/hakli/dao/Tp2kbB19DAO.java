package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Tp2kbb19;
import com.sds.utils.db.StoreHibernateUtil;

public class Tp2kbB19DAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tp2kbb19> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tp2kbb19> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from tp2kbb19  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Tp2kbb19.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tp2kbb19 "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tp2kbb19> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tp2kbb19> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tp2kbb19 where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tp2kbb19> listNativeByFilter(String filter, String orderby) throws Exception {		
    	List<Tp2kbb19> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Tp2kbb19 where " + filter + " order by " + orderby).addEntity(Tp2kbb19.class).list();
		session.close();
        return oList;
    }
	
	public Tp2kbb19 findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kbb19 oForm = (Tp2kbb19) session.createQuery("from Tp2kbb19 where tp2kbb19pk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tp2kbb19 order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Tp2kbb19 findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kbb19 oForm = (Tp2kbb19) session.createQuery("from Tp2kbb19 where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Tp2kbb19 oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tp2kbb19 oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
