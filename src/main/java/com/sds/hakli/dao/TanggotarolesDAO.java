package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Tanggotaroles;
import com.sds.utils.db.StoreHibernateUtil;

public class TanggotarolesDAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tanggotaroles> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tanggotaroles> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from tanggotaroles  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Tanggotaroles.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tanggotaroles "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tanggotaroles> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tanggotaroles> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tanggotaroles where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Tanggotaroles findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tanggotaroles oForm = (Tanggotaroles) session.createQuery("from Tanggotaroles where tanggotarolespk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tanggotaroles order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Tanggotaroles findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tanggotaroles oForm = (Tanggotaroles) session.createQuery("from Tanggotaroles where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Tanggotaroles oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tanggotaroles oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
