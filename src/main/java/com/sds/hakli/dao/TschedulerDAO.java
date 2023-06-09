package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Tscheduler;
import com.sds.utils.db.StoreHibernateUtil;

public class TschedulerDAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tscheduler> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tscheduler> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from tscheduler  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Tscheduler.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tscheduler "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tscheduler> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tscheduler> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tscheduler where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Tscheduler findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tscheduler oForm = (Tscheduler) session.createQuery("from Tscheduler where tschedulerpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tscheduler order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Tscheduler findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tscheduler oForm = (Tscheduler) session.createQuery("from Tscheduler where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Tscheduler oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tscheduler oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
