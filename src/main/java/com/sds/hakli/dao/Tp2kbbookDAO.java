package com.sds.hakli.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.sds.hakli.domain.BranchTop;
import com.sds.hakli.domain.Tp2kbbook;
import com.sds.utils.db.StoreHibernateUtil;

public class Tp2kbbookDAO {

	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tp2kbbook> listPaging(int first, int second, String filter, String orderby) throws Exception {
		List<Tp2kbbook> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery(
				"select * from Tp2kbbook Join Tanggota on tanggotafk = tanggotapk join Mcabang on mcabangfk = mcabangpk "
						+ "where " + filter + " order by " + orderby + " limit " + second + " offset " + first)
				.addEntity(Tp2kbbook.class).list();

		session.close();
		return oList;
	}

	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery(
				"select count(*) from Tp2kbbook Join Tanggota on tanggotafk = tanggotapk join Mcabang on mcabangfk = mcabangpk "
						+ "where " + filter)
				.uniqueResult().toString());
		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Tp2kbbook> listAll() throws Exception {
		List<Tp2kbbook> oList = null;
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tp2kbbook order by Tp2kbbookpk").list();
		session.close();
		return oList;
	}

	@SuppressWarnings("unchecked")
	public List<Tp2kbbook> listByFilter(String filter, String orderby) throws Exception {
		List<Tp2kbbook> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tp2kbbook where " + filter + " order by " + orderby).list();
		session.close();
		return oList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Tp2kbbook> listNativeByFilter(String filter, String orderby) throws Exception {
		List<Tp2kbbook> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Tp2kbbook where " + filter + " order by " + orderby).addEntity(Tp2kbbook.class).list();
		session.close();
		return oList;
	}

	public Tp2kbbook findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kbbook oForm = (Tp2kbbook) session.createQuery("from Tp2kbbook where Tp2kbbookpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}

	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("select " + fieldname + " from Tp2kbbook order by " + fieldname).list();
		session.close();
		return oList;
	}

	public Tp2kbbook findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tp2kbbook oForm = (Tp2kbbook) session.createQuery("from Tp2kbbook where " + filter).uniqueResult();
		session.close();
		return oForm;
	}

	public void save(Session session, Tp2kbbook oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}

	public void delete(Session session, Tp2kbbook oForm) throws HibernateException, Exception {
		session.delete(oForm);
	}

	@SuppressWarnings("unchecked")
	public List<BranchTop> listSumWaitKomisiP2KB(String filter) throws Exception {
		List<BranchTop> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("SELECT MPROVPK AS ID, MPROV.PROVNAME AS NAME, COUNT(*) AS TOTAL FROM TP2KBBOOK "
				+ "JOIN TANGGOTA ON TANGGOTAFK = TANGGOTAPK JOIN MCABANG ON MCABANGFK = MCABANGPK JOIN MPROV ON MPROVFK = MPROVPK "
				+ "WHERE STATUS = 'R' AND ISPAID = 'Y' GROUP BY MPROVPK, MPROV.PROVNAME ORDER BY COUNT(*) DESC")
				.addEntity(BranchTop.class).list();
		session.close();
		return oList;
	}

	@SuppressWarnings("unchecked")
	public List<BranchTop> listSumWaitKomisiP2KBKab(String filter) throws Exception {
		List<BranchTop> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session
				.createSQLQuery("SELECT MCABANGPK AS ID, CABANG AS NAME, COUNT(*) AS TOTAL FROM TP2KBBOOK "
						+ "JOIN TANGGOTA ON TANGGOTAFK = TANGGOTAPK JOIN MCABANG ON MCABANGFK = MCABANGPK " + "WHERE "
						+ filter + " " + "GROUP BY MCABANGPK, CABANG ORDER BY COUNT(*) DESC")
				.addEntity(BranchTop.class).list();
		session.close();
		return oList;
	}
	
	public Integer countKegiatan(Integer pk) throws Exception {
		int val = 0;
		session = StoreHibernateUtil.openSession();
		val = Integer.parseInt((String) session.createSQLQuery(
				"SELECT COALESCE(SUM(TOTAL),0) FROM (\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBA01 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBA02 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBA03 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBA04 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBA05 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBA06 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB01 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB02 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB03 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB04 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB05 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB06 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB07 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB08 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB09 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB10 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB11 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB12 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB13 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB14 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB15 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB16 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB17 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB18 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB19 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBB20 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBC01 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBC02 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBD01 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBD02 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBE01 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBE02 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBE03 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBE04 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBE05 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBE06 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBE07 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBE08 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBE09 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBE10 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT COUNT(*) AS TOTAL FROM TP2KBE11 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' ) AS A")
				.uniqueResult().toString());
		session.close();
		return val;
	}
	
	public BigDecimal sumSkp(Integer pk) throws Exception {
		BigDecimal val = new BigDecimal(0);
		session = StoreHibernateUtil.openSession();
		val = (BigDecimal) session.createSQLQuery(
				"SELECT COALESCE(SUM(NILAISKP),0) FROM (\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBA01 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBA02 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBA03 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBA04 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBA05 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBA06 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB01 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB02 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB03 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB04 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB05 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB06 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB07 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB08 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB09 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB10 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB11 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB12 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB13 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB14 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB15 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB16 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB17 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB18 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB19 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBB20 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBC01 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBC02 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBD01 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBD02 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBE01 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBE02 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBE03 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBE04 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBE05 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBE06 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBE07 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBE08 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBE09 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBE10 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' UNION\r\n"
				+ "SELECT SUM(NILAISKP) AS NILAISKP FROM TP2KBE11 WHERE TP2KBBOOKFK  = " + pk + " AND STATUS = 'A' ) AS A")
				.uniqueResult();
		session.close();
		return val;
	}

}
