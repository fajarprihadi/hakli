package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Tmutasi;
import com.sds.utils.db.StoreHibernateUtil;

public class TmutasiDAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tmutasi> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tmutasi> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from Tmutasi join Mcabang on mcabangfk = mcabangpk "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Tmutasi.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tmutasi join Mcabang on mcabangfk = mcabangpk "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tmutasi> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tmutasi> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tmutasi where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	@SuppressWarnings("unchecked")
	public List<Tmutasi> listNative(String filter, String orderby) throws Exception {		
    	List<Tmutasi> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select Tmutasi.* from Tmutasi join Mcabang on mcabangfk = mcabangpk where " + filter + " order by " + orderby).addEntity(Tmutasi.class).list();
		session.close();
        return oList;
    }	
	
	public Tmutasi findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tmutasi oForm = (Tmutasi) session.createQuery("from Tmutasi where Tmutasipk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tmutasi order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Tmutasi findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tmutasi oForm = (Tmutasi) session.createQuery("from Tmutasi where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Tmutasi oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tmutasi oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }

}
