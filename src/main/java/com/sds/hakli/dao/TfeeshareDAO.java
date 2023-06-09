package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Tfeeshare;
import com.sds.utils.db.StoreHibernateUtil;

public class TfeeshareDAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tfeeshare> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tfeeshare> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from tfeeshare  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Tfeeshare.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tfeeshare "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tfeeshare> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tfeeshare> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tfeeshare where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Tfeeshare findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tfeeshare oForm = (Tfeeshare) session.createQuery("from Tfeeshare where tfeesharepk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tfeeshare order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Tfeeshare findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tfeeshare oForm = (Tfeeshare) session.createQuery("from Tfeeshare where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Tfeeshare oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tfeeshare oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
