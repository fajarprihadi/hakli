package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Musergroupmenu;
import com.sds.utils.db.StoreHibernateUtil;

public class MusergroupmenuDAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Musergroupmenu> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Musergroupmenu> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from musergroupmenu "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Musergroupmenu.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Musergroupmenu "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Musergroupmenu> listByFilter(String filter, String orderby) throws Exception {		
    	List<Musergroupmenu> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Musergroupmenu where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Musergroupmenu findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Musergroupmenu oForm = (Musergroupmenu) session.createQuery("from Musergroupmenu where musergroupmenupk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	public Musergroupmenu findByCode(String code) throws Exception {
		session = StoreHibernateUtil.openSession();
		Musergroupmenu oForm = (Musergroupmenu) session.createQuery("from Musergroupmenu where usergroupmenucode = '" + code + "'").uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Musergroupmenu order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Musergroupmenu findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Musergroupmenu oForm = (Musergroupmenu) session.createQuery("from Musergroupmenu where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Musergroupmenu oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Musergroupmenu oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }
	
	public void deleteBySQL(Session session, String filter) throws HibernateException, Exception {
		session.createSQLQuery("delete from Musergroupmenu where "+ filter).executeUpdate();    
    }
	
}

