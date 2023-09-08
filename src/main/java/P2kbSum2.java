import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sds.hakli.dao.Tp2kbA01DAO;
import com.sds.hakli.dao.Tp2kbA02DAO;
import com.sds.hakli.dao.Tp2kbA03DAO;
import com.sds.hakli.dao.Tp2kbA04DAO;
import com.sds.hakli.dao.Tp2kbA05DAO;
import com.sds.hakli.dao.Tp2kbA06DAO;
import com.sds.hakli.dao.Tp2kbB01DAO;
import com.sds.hakli.dao.Tp2kbB02DAO;
import com.sds.hakli.dao.Tp2kbB03DAO;
import com.sds.hakli.dao.Tp2kbB04DAO;
import com.sds.hakli.dao.Tp2kbB05DAO;
import com.sds.hakli.dao.Tp2kbB06DAO;
import com.sds.hakli.dao.Tp2kbB07DAO;
import com.sds.hakli.dao.Tp2kbB08DAO;
import com.sds.hakli.dao.Tp2kbB09DAO;
import com.sds.hakli.dao.Tp2kbB10DAO;
import com.sds.hakli.dao.Tp2kbB11DAO;
import com.sds.hakli.dao.Tp2kbB12DAO;
import com.sds.hakli.dao.Tp2kbB13DAO;
import com.sds.hakli.dao.Tp2kbB14DAO;
import com.sds.hakli.dao.Tp2kbB15DAO;
import com.sds.hakli.dao.Tp2kbB16DAO;
import com.sds.hakli.dao.Tp2kbB17DAO;
import com.sds.hakli.dao.Tp2kbB18DAO;
import com.sds.hakli.dao.Tp2kbB19DAO;
import com.sds.hakli.dao.Tp2kbB20DAO;
import com.sds.hakli.dao.Tp2kbC01DAO;
import com.sds.hakli.dao.Tp2kbC02DAO;
import com.sds.hakli.dao.Tp2kbD01DAO;
import com.sds.hakli.dao.Tp2kbD02DAO;
import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.dao.Tp2kbE01DAO;
import com.sds.hakli.dao.Tp2kbE02DAO;
import com.sds.hakli.dao.Tp2kbE03DAO;
import com.sds.hakli.dao.Tp2kbE04DAO;
import com.sds.hakli.dao.Tp2kbE05DAO;
import com.sds.hakli.dao.Tp2kbE06DAO;
import com.sds.hakli.dao.Tp2kbE07DAO;
import com.sds.hakli.dao.Tp2kbE08DAO;
import com.sds.hakli.dao.Tp2kbE09DAO;
import com.sds.hakli.dao.Tp2kbE10DAO;
import com.sds.hakli.dao.Tp2kbE11DAO;
import com.sds.hakli.domain.Tp2kb;
import com.sds.hakli.domain.Tp2kba01;
import com.sds.hakli.domain.Tp2kba02;
import com.sds.hakli.domain.Tp2kba03;
import com.sds.hakli.domain.Tp2kba04;
import com.sds.hakli.domain.Tp2kba05;
import com.sds.hakli.domain.Tp2kba06;
import com.sds.hakli.domain.Tp2kbb01;
import com.sds.hakli.domain.Tp2kbb02;
import com.sds.hakli.domain.Tp2kbb03;
import com.sds.hakli.domain.Tp2kbb04;
import com.sds.hakli.domain.Tp2kbb05;
import com.sds.hakli.domain.Tp2kbb06;
import com.sds.hakli.domain.Tp2kbb07;
import com.sds.hakli.domain.Tp2kbb08;
import com.sds.hakli.domain.Tp2kbb09;
import com.sds.hakli.domain.Tp2kbb10;
import com.sds.hakli.domain.Tp2kbb11;
import com.sds.hakli.domain.Tp2kbb12;
import com.sds.hakli.domain.Tp2kbb13;
import com.sds.hakli.domain.Tp2kbb14;
import com.sds.hakli.domain.Tp2kbb15;
import com.sds.hakli.domain.Tp2kbb16;
import com.sds.hakli.domain.Tp2kbb17;
import com.sds.hakli.domain.Tp2kbb18;
import com.sds.hakli.domain.Tp2kbb19;
import com.sds.hakli.domain.Tp2kbb20;
import com.sds.hakli.domain.Tp2kbc01;
import com.sds.hakli.domain.Tp2kbc02;
import com.sds.hakli.domain.Tp2kbd01;
import com.sds.hakli.domain.Tp2kbd02;
import com.sds.hakli.domain.Tp2kbe01;
import com.sds.hakli.domain.Tp2kbe02;
import com.sds.hakli.domain.Tp2kbe03;
import com.sds.hakli.domain.Tp2kbe04;
import com.sds.hakli.domain.Tp2kbe05;
import com.sds.hakli.domain.Tp2kbe06;
import com.sds.hakli.domain.Tp2kbe07;
import com.sds.hakli.domain.Tp2kbe08;
import com.sds.hakli.domain.Tp2kbe09;
import com.sds.hakli.domain.Tp2kbe10;
import com.sds.hakli.domain.Tp2kbe11;
import com.sds.utils.db.StoreHibernateUtil;

public class P2kbSum2 {

	public static void main(String[] args) {
		Tp2kbDAO p2kbDao = new Tp2kbDAO();
		try {
			List<Tp2kb> objList = p2kbDao.listByFilter("0=0", "tp2kbpk");
			System.out.println("Total Data " + objList.size());
			Integer counter = 0;
			for (Tp2kb obj: objList) {
				System.out.println("Data Ke " + (++counter));
				System.out.println("P2KB PK " + obj.getTp2kbpk());
				if (obj.getMp2kbkegiatan().getIdkegiatan().equals("A01")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kba01 data: new Tp2kbA01DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kba01pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("A02")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kba02 data: new Tp2kbA02DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kba02pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("A03")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kba03 data: new Tp2kbA03DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kba03pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("A04")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kba04 data: new Tp2kbA04DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kba04pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("A05")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kba05 data: new Tp2kbA05DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kba05pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("A06")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kba06 data: new Tp2kbA06DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kba06pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B01")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb01 data: new Tp2kbB01DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb01pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B02")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb02 data: new Tp2kbB02DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb02pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B03")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb03 data: new Tp2kbB03DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb03pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B04")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb04 data: new Tp2kbB04DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb04pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B05")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb05 data: new Tp2kbB05DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb05pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B06")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb06 data: new Tp2kbB06DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb06pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B07")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb07 data: new Tp2kbB07DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb07pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B08")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb08 data: new Tp2kbB08DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb08pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B09")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb09 data: new Tp2kbB09DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb09pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B10")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb10 data: new Tp2kbB10DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb10pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B11")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb11 data: new Tp2kbB11DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb11pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B12")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb12 data: new Tp2kbB12DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb12pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B13")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb13 data: new Tp2kbB13DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb13pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B14")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb14 data: new Tp2kbB14DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb14pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B15")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb15 data: new Tp2kbB15DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb15pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B16")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb16 data: new Tp2kbB16DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb16pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B17")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb17 data: new Tp2kbB17DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb17pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B18")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb18 data: new Tp2kbB18DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb18pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B19")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb19 data: new Tp2kbB19DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb19pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("B20")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbb20 data: new Tp2kbB20DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbb20pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("C01")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbc01 data: new Tp2kbC01DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbc01pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("C02")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbc02 data: new Tp2kbC02DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbc02pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("D01")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbd01 data: new Tp2kbD01DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbd01pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("D02")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbd02 data: new Tp2kbD02DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbd02pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("E01")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbe01 data: new Tp2kbE01DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbe01pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("E02")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbe02 data: new Tp2kbE02DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbe02pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("E03")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbe03 data: new Tp2kbE03DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbe03pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("E04")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbe04 data: new Tp2kbE04DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbe04pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("E05")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbe05 data: new Tp2kbE05DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbe05pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("E06")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbe06 data: new Tp2kbE06DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbe06pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("E07")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbe07 data: new Tp2kbE07DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbe07pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("E08")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbe08 data: new Tp2kbE08DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbe08pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("E09")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbe09 data: new Tp2kbE09DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbe09pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("E10")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbe10 data: new Tp2kbE10DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbe10pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				} else if (obj.getMp2kbkegiatan().getIdkegiatan().equals("E11")) {
					Integer totalkegiatan = 0;
					Integer totalkegiatanok = 0;
					Integer totalkegiatanwv = 0;
					Integer totalkegiatanrj = 0;
					BigDecimal totalskp = new BigDecimal(0);
					BigDecimal totalskpok = new BigDecimal(0);
					BigDecimal totalskpwv = new BigDecimal(0);
					BigDecimal totalskprj = new BigDecimal(0);
					for (Tp2kbe11 data: new Tp2kbE11DAO().listByFilter("tp2kbbook.tp2kbbookpk = " + obj.getTp2kbbook().getTp2kbbookpk(), "tp2kbe11pk")) {
						totalkegiatan += 1;
						totalskp = totalskp.add(data.getNilaiskp());
						if (data.getStatus().equals("A")) {
							totalkegiatanok += 1;
							totalskpok = totalskpok.add(data.getNilaiskp());
						} else if (data.getStatus().equals("W")) {
							totalkegiatanwv += 1;
							totalskpwv = totalskpwv.add(data.getNilaiskp());
						} else if (data.getStatus().equals("R")) {
							totalkegiatanrj += 1;
							totalskprj = totalskprj.add(data.getNilaiskp());
						} 
					}
					Session session = StoreHibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					try {
						obj.setTotalkegiatan(totalkegiatan);
						obj.setTotalskp(totalskp);
						obj.setTotalkegiatanok(totalkegiatanok);
						obj.setTotalskpok(totalskpok);
						obj.setTotalkegiatanwv(totalkegiatanwv);
						obj.setTotalskpwv(totalskpwv);
						obj.setTotalkegiatanrj(totalkegiatanrj);
						obj.setTotalskprj(totalskprj);
						p2kbDao.save(session, obj);
						trx.commit();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						session.close();
					}
				}  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
