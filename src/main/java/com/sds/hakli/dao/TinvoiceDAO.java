package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Tinvoice;
import com.sds.utils.db.StoreHibernateUtil;

public class TinvoiceDAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tinvoice> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tinvoice> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from tinvoice  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Tinvoice.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tinvoice "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tinvoice> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tinvoice> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tinvoice where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Tinvoice findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tinvoice oForm = (Tinvoice) session.createQuery("from Tinvoice where tinvoicepk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tinvoice order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Tinvoice findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tinvoice oForm = (Tinvoice) session.createQuery("from Tinvoice where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Tinvoice oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tinvoice oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
