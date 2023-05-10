package com.sds.hakli.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.Tp2kb;
import com.sds.hakli.domain.Vp2kb;
import com.sds.utils.db.StoreHibernateUtil;

public class Tp2kbDAO {

	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tp2kb> listPaging(int first, int second, String filter, String orderby) throws Exception {
		List<Tp2kb> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from tp2kb  " + "where " + filter + " order by " + orderby + " limit "
				+ second + " offset " + first).addEntity(Tp2kb.class).list();

		session.close();
		return oList;
	}

	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tp2kb " + "where " + filter)
				.uniqueResult().toString());
		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Tp2kb> listByFilter(String filter, String orderby) throws Exception {
		List<Tp2kb> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tp2kb where " + filter + " order by " + orderby).list();
		session.close();
		return oList;
	}

	public Tp2kb findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kb oForm = (Tp2kb) session.createQuery("from Tp2kb where tp2kbpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}

	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("select " + fieldname + " from Tp2kb order by " + fieldname).list();
		session.close();
		return oList;
	}

	public Tp2kb findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kb oForm = (Tp2kb) session.createQuery("from Tp2kb where " + filter).uniqueResult();
		session.close();
		return oForm;
	}

	@SuppressWarnings("unchecked")
	public List<Vp2kb> listVerifikasi(String filter, String orderby) throws Exception {
		List<Vp2kb> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session
				.createSQLQuery("SELECT CABANG, TANGGOTAPK, NOANGGOTA, NAMA, ALAMAT, SUM(TOTALWAITING) AS TOTALWAITING "
						+ "FROM TP2KB JOIN TANGGOTA ON TANGGOTAFK = TANGGOTAPK JOIN MCABANG ON MCABANGFK = MCABANGPK "
						+ "JOIN MP2KBKEGIATAN ON MP2KBKEGIATANFK = MP2KBKEGIATANPK WHERE " + filter
						+ " GROUP BY CABANG, TANGGOTAPK, NOANGGOTA, NAMA, ALAMAT HAVING SUM(TOTALWAITING) > 0 ORDER BY " + orderby)
				.addEntity(Vp2kb.class).list();

		session.close();
		return oList;
	}

	public void save(Session session, Tp2kb oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}

	public void delete(Session session, Tp2kb oForm) throws HibernateException, Exception {
		session.delete(oForm);
	}

}
