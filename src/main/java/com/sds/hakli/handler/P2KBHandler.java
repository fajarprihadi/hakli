package com.sds.hakli.handler;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.zk.ui.Sessions;

import com.sds.hakli.dao.Tp2kbbookDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tp2kb;
import com.sds.hakli.domain.Tp2kbbook;
import com.sds.utils.db.StoreHibernateUtil;

public class P2KBHandler {
	private static org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private static Tanggota anggota = (Tanggota) zkSession.getAttribute("anggota");

	public static Tp2kb setApproval(Tp2kb obj, String grouptype, String actiontype, BigDecimal skp) {
		
		try {
			if(grouptype.equals("T")) {
				obj.setTotalwaiting(obj.getTotalwaiting() - 1);
				obj.setTotalskpwaiting(obj.getTotalskpwaiting().subtract(skp));
				
				if(actiontype.equals("A")) {
					obj.setTotaltimapprove(obj.getTotaltimapprove() + 1);
					
					Session session = StoreHibernateUtil.openSession();
					Transaction trx  = session.beginTransaction();
					
					Tp2kbbook objBook = obj.getTp2kbbook();
					objBook.setLastupdated(new Date());
					objBook.setUpdatedby(anggota.getNoanggota());
					objBook.setTotalskp(objBook.getTotalskp().add(skp));
					new Tp2kbbookDAO().save(session, objBook);
					
					trx.commit();
					session.close();
				}
			} 
//			else if(grouptype.equals("K")) {
//				obj.setTotaltimapprove(obj.getTotaltimapprove() - 1);
//				
//				if(actiontype.equals("A")) {
//					Session session = StoreHibernateUtil.openSession();
//					Transaction trx  = session.beginTransaction();
//					
//					Tp2kbbook objBook = obj.getTp2kbbook();
//					objBook.setLastupdated(new Date());
//					objBook.setUpdatedby(anggota.getNoanggota());
//					objBook.setTotalskp(objBook.getTotalskp().add(obj.getTotalskp()));
//					new Tp2kbbookDAO().save(session, objBook);
//					
//					trx.commit();
//					session.close();
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static Tp2kb init(Tp2kb obj) {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}
