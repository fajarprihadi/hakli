package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Msysparam;
import com.sds.utils.db.StoreHibernateUtil;

public class MsysparamDAO {
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Msysparam> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Msysparam> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from Msysparam "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Msysparam.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Msysparam "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Msysparam> listByFilter(String filter, String orderby) throws Exception {		
    	List<Msysparam> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Msysparam where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Msysparam findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Msysparam oForm = (Msysparam) session.createQuery("from Msysparam where mparameterpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Msysparam order by " + fieldname).list();   
        session.close();
        return oList;
	}
		
	public void save(Session session, Msysparam oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Msysparam oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }
	
	public String getParamvalue(String paramcode) throws Exception {
		session = StoreHibernateUtil.openSession();
		String oList = (String) session.createSQLQuery("select paramvalue from Msysparam where paramcode = '" + paramcode +"'").uniqueResult();
		session.close();
		return oList;
	}
	
	public Msysparam findByCode(String paramcode) throws Exception {
		session = StoreHibernateUtil.openSession();
		Msysparam oForm = (Msysparam) session.createQuery("from Msysparam where paramcode = '" + paramcode + "'").uniqueResult();
		session.close();
		return oForm;
	}
	
}
