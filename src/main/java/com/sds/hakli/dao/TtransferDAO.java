package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Ttransfer;
import com.sds.utils.db.StoreHibernateUtil;

public class TtransferDAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Ttransfer> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Ttransfer> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from ttransfer  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Ttransfer.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Ttransfer "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Ttransfer> listByFilter(String filter, String orderby) throws Exception {		
    	List<Ttransfer> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Ttransfer where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Ttransfer findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Ttransfer oForm = (Ttransfer) session.createQuery("from Ttransfer where ttransferpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Ttransfer order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Ttransfer findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Ttransfer oForm = (Ttransfer) session.createQuery("from Ttransfer where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Ttransfer oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Ttransfer oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
