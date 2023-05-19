package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Mprov;
import com.sds.utils.db.StoreHibernateUtil;

public class MprovDAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Mprov> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Mprov> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from mprov  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Mprov.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Mprov "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Mprov> listAll() throws Exception {		
    	List<Mprov> oList = null;
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Mprov order by provname").list();
		session.close();
        return oList;
    }	
	
	@SuppressWarnings("unchecked")
	public List<Mprov> listByFilter(String filter, String orderby) throws Exception {		
    	List<Mprov> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Mprov where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Mprov findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Mprov oForm = (Mprov) session.createQuery("from Mprov where mprovpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Mprov order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Mprov findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Mprov oForm = (Mprov) session.createQuery("from Mprov where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Mprov oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Mprov oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
