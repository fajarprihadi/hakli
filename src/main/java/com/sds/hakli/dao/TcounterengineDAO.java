package com.sds.hakli.dao;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sds.utils.db.StoreHibernateUtil;

public class TcounterengineDAO {
	
	public String generateNoAnggota(String counterName) throws Exception {
		Integer lastCounter = 0;
		String strCounter = "";
		String finalCounter = "";
		String counterCode = "NOANGGOTA" + counterName;
		int counter = 4;
		char[] fillUploadid = new char[counter];
		Session session = StoreHibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Query q = session
					.createQuery("select lastcounter from Tcounterengine where countername = '" + counterCode + "'");
			lastCounter = (Integer) q.uniqueResult();
			if (lastCounter != null) {
				lastCounter++;
				session.createSQLQuery("update Tcounterengine set lastcounter = lastcounter + 1 where countername = '"
						+ counterCode + "'").executeUpdate();
			} else {
				lastCounter = 1;
				session.createSQLQuery("insert into Tcounterengine values ('" + counterCode + "', " + lastCounter + ")")
						.executeUpdate();
			}
			transaction.commit();
			session.close();
			Arrays.fill(fillUploadid, '0');
			strCounter = new String(fillUploadid) + lastCounter;
			finalCounter = counterName
					+ strCounter.substring(strCounter.length()-counter, strCounter.length());
			System.out.println("finalCounter : " + finalCounter);
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return finalCounter;
	}
	
	public String getLastCounter(String counterName, int counter) throws Exception {
		Integer lastCounter = 0;
		String strCounter = "";
		String finalCounter = "";
		char[] fillUploadid = new char[counter];
		Session session = StoreHibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Query q = session
					.createQuery("select lastcounter from Tcounterengine where countername = '" + counterName + "'");
			lastCounter = (Integer) q.uniqueResult();
			if (lastCounter != null) {
				lastCounter++;
				session.createSQLQuery("update Tcounterengine set lastcounter = lastcounter + 1 where countername = '"
						+ counterName + "'").executeUpdate();
			} else {
				lastCounter = 1;
				session.createSQLQuery("insert into Tcounterengine values ('" + counterName + "', " + lastCounter + ")")
						.executeUpdate();
			}
			transaction.commit();
			session.close();
			Arrays.fill(fillUploadid, '0');
			strCounter = new String(fillUploadid) + lastCounter;
			finalCounter = counterName
					+ strCounter.substring(strCounter.length()-counter, strCounter.length());
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return finalCounter;
	}
	
	public String getVaCounter(String counterName) throws Exception {
		Integer lastCounter = 0;
		String strCounter = "";
		String finalCounter = "";
		char[] fillUploadid = new char[5];
		Session session = StoreHibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Query q = session
					.createQuery("select lastcounter from Tcounterengine where countername = 'VA" + counterName + "'");
			lastCounter = (Integer) q.uniqueResult();
			if (lastCounter != null) {
				lastCounter++;
				session.createSQLQuery("update Tcounterengine set lastcounter = lastcounter + 1 where countername = 'VA"
						+ counterName + "'").executeUpdate();
			} else {
				lastCounter = 1;
				session.createSQLQuery("insert into Tcounterengine values ('VA" + counterName + "', " + lastCounter + ")")
						.executeUpdate();
			}
			transaction.commit();
			session.close();
			Arrays.fill(fillUploadid, '0');
			strCounter = new String(fillUploadid) + lastCounter;
			finalCounter = counterName
					+ strCounter.substring(strCounter.length()-5, strCounter.length());
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return finalCounter;
	}
	
	public String getInvoiceCounter() throws Exception {
		Integer lastCounter = 0;
		String counterName = "";
		String strCounter = "";
		String finalCounter = "";
		char[] fillUploadid = new char[5];
		Session session = StoreHibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			counterName = "INV/" + new SimpleDateFormat("yyMM").format(new Date()) + "/";
			Query q = session
					.createQuery("select lastcounter from Tcounterengine where countername = '" + counterName + "'");
			lastCounter = (Integer) q.uniqueResult();
			if (lastCounter != null) {
				lastCounter++;
				session.createSQLQuery("update Tcounterengine set lastcounter = lastcounter + 1 where countername = '"
						+ counterName + "'").executeUpdate();
			} else {
				lastCounter = 1;
				session.createSQLQuery("insert into Tcounterengine values ('" + counterName + "', " + lastCounter + ")")
						.executeUpdate();
			}
			transaction.commit();
			session.close();
			Arrays.fill(fillUploadid, '0');
			strCounter = new String(fillUploadid) + lastCounter;
			finalCounter = counterName
					+ strCounter.substring(strCounter.length()-5, strCounter.length());
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return finalCounter;
	}
	
	public String getP2kbCounter(String counterName) throws Exception {
		Integer lastCounter = 0;
		String strCounter = "";
		String finalCounter = "";
		char[] fillUploadid = new char[5];
		Session session = StoreHibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			counterName = counterName + new SimpleDateFormat("MMyy").format(new Date());
			Query q = session
					.createQuery("select lastcounter from Tcounterengine where countername = '" + counterName + "'");
			lastCounter = (Integer) q.uniqueResult();
			if (lastCounter != null) {
				lastCounter++;
				session.createSQLQuery("update Tcounterengine set lastcounter = lastcounter + 1 where countername = '"
						+ counterName + "'").executeUpdate();
			} else {
				lastCounter = 1;
				session.createSQLQuery("insert into Tcounterengine values ('" + counterName + "', " + lastCounter + ")")
						.executeUpdate();
			}
			transaction.commit();
			session.close();
			Arrays.fill(fillUploadid, '0');
			strCounter = new String(fillUploadid) + lastCounter;
			finalCounter = counterName
					+ strCounter.substring(strCounter.length()-5, strCounter.length());
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return finalCounter;
	}
	
	public String generateSeqnum() throws Exception {
		Integer lastCounter = 0;
		String strCounter = "";
		String finalCounter = "";
		int len = 3;
		char[] fillUploadid = new char[len];
		Session session = StoreHibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			String counterName = new SimpleDateFormat("YY").format(new Date()) + new SimpleDateFormat("MM").format(new Date());
			
			Query q = session.createQuery("select lastcounter from Tcounterengine where countername = '" + counterName + "'");
			lastCounter = (Integer) q.uniqueResult();
			if (lastCounter != null) {
				lastCounter++;
				session.createSQLQuery("update Tcounterengine set lastcounter = lastcounter + 1 where countername = '" + counterName + "'").executeUpdate();				
			} else {
				lastCounter = 1;
				session.createSQLQuery("insert into Tcounterengine values ('" + counterName + "', " + lastCounter + ")").executeUpdate();
			}			
			transaction.commit();
			session.close();
			Arrays.fill(fillUploadid, '0');
			strCounter = new String(fillUploadid) + lastCounter;
			finalCounter = counterName + strCounter.substring(strCounter.length()-len, strCounter.length());			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}		
		return finalCounter;
	}
	
	public String getLetterRecomNo(String counterName, int counter) throws Exception {
		Integer lastCounter = 0;
		String strCounter = "";
		String finalCounter = "";
		char[] fillUploadid = new char[counter];
		Session session = StoreHibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Query q = session
					.createQuery("select lastcounter from Tcounterengine where countername = '" + counterName + "'");
			lastCounter = (Integer) q.uniqueResult();
			if (lastCounter != null) {
				lastCounter++;
				session.createSQLQuery("update Tcounterengine set lastcounter = lastcounter + 1 where countername = '"
						+ counterName + "'").executeUpdate();
			} else {
				lastCounter = 1;
				session.createSQLQuery("insert into Tcounterengine values ('" + counterName + "', " + lastCounter + ")")
						.executeUpdate();
			}
			transaction.commit();
			session.close();
			Arrays.fill(fillUploadid, '0');
			strCounter = new String(fillUploadid) + lastCounter;
			finalCounter = strCounter.substring(strCounter.length()-counter, strCounter.length()) + counterName;
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return finalCounter;
	}
}
