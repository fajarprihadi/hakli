package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Musergroup;
import com.sds.utils.db.StoreHibernateUtil;

public class MusergroupDAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Musergroup> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Musergroup> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from musergroup "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Musergroup.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Musergroup "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Musergroup> listByFilter(String filter, String orderby) throws Exception {		
    	List<Musergroup> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Musergroup where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Musergroup findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Musergroup oForm = (Musergroup) session.createQuery("from Musergroup where musergrouppk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	public Musergroup findByCode(String code) throws Exception {
		session = StoreHibernateUtil.openSession();
		Musergroup oForm = (Musergroup) session.createQuery("from Musergroup where usergroupcode = '" + code + "'").uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Musergroup order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Musergroup findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Musergroup oForm = (Musergroup) session.createQuery("from Musergroup where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Musergroup oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Musergroup oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }
	
}

