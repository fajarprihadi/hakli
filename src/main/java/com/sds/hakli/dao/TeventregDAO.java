package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Teventreg;
import com.sds.hakli.domain.Veventamount;
import com.sds.hakli.domain.Veventreg;
import com.sds.utils.db.StoreHibernateUtil;

public class TeventregDAO {
	
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Teventreg> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Teventreg> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from teventreg join Tanggota on tanggotafk = tanggotapk  "
				+ "where " + filter + " order by " + orderby + " limit " + second +" offset " + first)
				.addEntity(Teventreg.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Teventreg join Tanggota on tanggotafk = tanggotapk "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Teventreg> listByFilter(String filter, String orderby) throws Exception {		
    	List<Teventreg> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Teventreg where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	@SuppressWarnings("unchecked")
	public List<Teventreg> listNative(String filter, String orderby) throws Exception {		
    	List<Teventreg> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from teventreg join Tanggota on tanggotafk = tanggotapk where " + filter + " order by " + orderby).addEntity(Teventreg.class).list();
		session.close();
        return oList;
    }	
	
	public Teventreg findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Teventreg oForm = (Teventreg) session.createQuery("from Teventreg where teventregpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Teventreg order by " + fieldname).list();   
        session.close();
        return oList;
	}
	
	public Teventreg findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Teventreg oForm = (Teventreg) session.createQuery("from Teventreg where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
		
	public void save(Session session, Teventreg oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Teventreg oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }
	
	public Veventamount sumAmount(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Veventamount oForm = (Veventamount) session.createSQLQuery("select teventpk, eventname, "
				+ "count(teventregpk) as totalreg, coalesce(sum(vaamount),0) as invamount, coalesce(sum(paidamount),0) as paymentamount "
				+ "from Tevent left join Teventreg on teventpk = teventfk where teventpk = " + pk + " group by teventpk, eventname").addEntity(Veventamount.class).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("unchecked")
	public List<Veventreg> listEventReg(Integer tanggotafk, String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		List<Veventreg> oList = session.createSQLQuery("select teventpk, eventname, "
				+ "eventdate, closedate, eventprice, eventtype, teventregpk, vacreatedat, tanggotafk, periodekta, periodeborang "
				+ "from Tevent left join Teventreg on teventpk = teventfk "
				+ "and tanggotafk = " + tanggotafk + " where " + filter + " order by eventdate desc").addEntity(Veventreg.class).list();
		session.close();
		return oList;
	}

}
