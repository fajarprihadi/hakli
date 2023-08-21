import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sds.hakli.dao.Mp2kbkegiatanDAO;
import com.sds.hakli.dao.Tp2kbA01DAO;
import com.sds.hakli.dao.Tp2kbA02DAO;
import com.sds.hakli.dao.Tp2kbA03DAO;
import com.sds.hakli.dao.Tp2kbA04DAO;
import com.sds.hakli.dao.Tp2kbA05DAO;
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
import com.sds.hakli.dao.Tp2kbbookDAO;
import com.sds.hakli.domain.Mp2kbkegiatan;
import com.sds.hakli.domain.Tp2kb;
import com.sds.hakli.domain.Tp2kba01;
import com.sds.hakli.domain.Tp2kba02;
import com.sds.hakli.domain.Tp2kba03;
import com.sds.hakli.domain.Tp2kba04;
import com.sds.hakli.domain.Tp2kba05;
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
import com.sds.hakli.domain.Tp2kbbook;
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

public class P2kbbookMap {

	public static void main(String[] args) {
		System.out.println("P2KB Updating FK");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Tp2kbbookDAO bookDao = new Tp2kbbookDAO();
		try {
			List<Tp2kbbook> objList = bookDao.listNativeByFilter("tp2kbbookpk >= 8932", "tp2kbbookpk");
			System.out.println("Total Data Book " + objList.size());
			int counter = 0;
			for (Tp2kbbook book: objList) {
				counter++;
				System.out.println("Data Book Ke " + counter);
				System.out.println("Book PK " + book.getTp2kbbookpk());
				System.out.println("Tgl Mulai : " + book.getTglmulai());
				System.out.println("Tgl Akhir : " + book.getTglakhir());
				if (book.getTglmulai() != null && book.getTglakhir() != null) {
					List<Tp2kba01> a01 = new Tp2kbA01DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglkegiatan between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kba01pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan A01 " + a01.size());
					if (a01.size() > 0) {					
						for (Tp2kba01 data: a01) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kba02> a02 = new Tp2kbA02DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglkegiatan between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kba02pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan A02 " + a02.size());
					if (a02.size() > 0) {
						for (Tp2kba02 data: a02) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kba03> a03 = new Tp2kbA03DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglkegiatan between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kba03pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan A03 " + a03.size());
					if (a03.size() > 0) {
						for (Tp2kba03 data: a03) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kba04> a04 = new Tp2kbA04DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kba04pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan A04 " + a04.size());
					if (a04.size() > 0) {
						for (Tp2kba04 data: a04) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kba05> a05 = new Tp2kbA05DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kba05pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan A05 " + a05.size());
					if (a05.size() > 0) {
						for (Tp2kba05 data: a05) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kba06> a06 = new Tp2kbA06DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kba06pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan A06 " + a06.size());
					if (a06.size() > 0) {
						for (Tp2kba06 data: a06) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb01> b01 = new Tp2kbB01DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb01pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B01 " + b01.size());
					if (b01.size() > 0) {
						for (Tp2kbb01 data: b01) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb02> b02 = new Tp2kbB02DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglkegiatan between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb02pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B02 " + b02.size());
					if (b02.size() > 0) {
						for (Tp2kbb02 data: b02) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb03> b03 = new Tp2kbB03DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb03pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B03 " + b03.size());
					if (b03.size() > 0) {
						for (Tp2kbb03 data: b03) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb04> b04 = new Tp2kbB04DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb04pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B04 " + b04.size());
					if (b04.size() > 0) {
						for (Tp2kbb04 data: b04) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb05> b05 = new Tp2kbB05DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb05pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B05 " + b05.size());
					if (b05.size() > 0) {
						for (Tp2kbb05 data: b05) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb06> b06 = new Tp2kbB06DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglkegiatan between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb06pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B06 " + b06.size());
					if (b06.size() > 0) {
						for (Tp2kbb06 data: b06) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb07> b07 = new Tp2kbB07DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglkegiatan between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb07pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B07 " + b07.size());
					if (b07.size() > 0) {
						for (Tp2kbb07 data: b07) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb08> b08 = new Tp2kbB08DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglkegiatan between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb08pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B08 " + b08.size());
					if (b08.size() > 0) {
						for (Tp2kbb08 data: b08) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb09> b09 = new Tp2kbB09DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb09pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B09 " + b09.size());
					if (b09.size() > 0) {
						for (Tp2kbb09 data: b09) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb10> b10 = new Tp2kbB10DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb10pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B10 " + b10.size());
					if (b10.size() > 0) {
						for (Tp2kbb10 data: b10) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb11> b11 = new Tp2kbB11DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb11pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B11 " + b11.size());
					if (b11.size() > 0) {
						for (Tp2kbb11 data: b11) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb12> b12 = new Tp2kbB12DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb12pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B12 " + b12.size());
					if (b12.size() > 0) {
						for (Tp2kbb12 data: b12) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb13> b13 = new Tp2kbB13DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb13pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B12 " + b12.size());
					if (b13.size() > 0) {
						for (Tp2kbb13 data: b13) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb14> b14 = new Tp2kbB14DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb14pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B14 " + b14.size());
					if (b14.size() > 0) {
						for (Tp2kbb14 data: b14) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb15> b15 = new Tp2kbB15DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb15pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B15 " + b15.size());
					if (b15.size() > 0) {
						for (Tp2kbb15 data: b15) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb16> b16 = new Tp2kbB16DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb16pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B16 " + b16.size());
					if (b16.size() > 0) {
						for (Tp2kbb16 data: b16) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb17> b17 = new Tp2kbB17DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb17pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B17 " + b17.size());
					if (b17.size() > 0) {
						for (Tp2kbb17 data: b17) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb18> b18 = new Tp2kbB18DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb18pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B18 " + b18.size());
					if (b18.size() > 0) {
						for (Tp2kbb18 data: b18) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb19> b19 = new Tp2kbB19DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb19pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B19 " + b19.size());
					if (b19.size() > 0) {
						for (Tp2kbb19 data: b19) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbb20> b20 = new Tp2kbB20DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbb20pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan B20 " + b20.size());
					if (b20.size() > 0) {
						for (Tp2kbb20 data: b20) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbc01> c01 = new Tp2kbC01DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbc01pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan C01 " + c01.size());
					if (c01.size() > 0) {
						for (Tp2kbc01 data: c01) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbc02> c02 = new Tp2kbC02DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglkegiatan between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbc02pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan C02 " + c02.size());
					if (c02.size() > 0) {
						for (Tp2kbc02 data: c02) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbd01> d01 = new Tp2kbD01DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglkegiatan between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbd01pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan D01 " + d01.size());
					if (d01.size() > 0) {
						for (Tp2kbd01 data: d01) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbd02> d02 = new Tp2kbD02DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglkegiatan between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbd02pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan D02 " + d02.size());
					if (d02.size() > 0) {
						for (Tp2kbd02 data: d02) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbe01> e01 = new Tp2kbE01DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbe01pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan E01 " + e01.size());
					if (e01.size() > 0) {
						for (Tp2kbe01 data: e01) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbe02> e02 = new Tp2kbE02DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglkegiatan between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbe02pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan E02 " + e02.size());
					if (e02.size() > 0) {
						for (Tp2kbe02 data: e02) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbe03> e03 = new Tp2kbE03DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglkegiatan between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbe03pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan E03 " + e03.size());
					if (e03.size() > 0) {
						for (Tp2kbe03 data: e03) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbe04> e04 = new Tp2kbE04DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglkegiatan between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbe04pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan E04 " + e04.size());
					if (e04.size() > 0) {
						for (Tp2kbe04 data: e04) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbe05> e05 = new Tp2kbE05DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglkegiatan between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbe05pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan E05 " + e05.size());
					if (e05.size() > 0) {
						for (Tp2kbe05 data: e05) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbe06> e06 = new Tp2kbE06DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbe06pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan E06 " + e06.size());
					if (e06.size() > 0) {
						for (Tp2kbe06 data: e06) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbe07> e07 = new Tp2kbE07DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbe07pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan E07 " + e07.size());
					if (e07.size() > 0) {
						for (Tp2kbe07 data: e07) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbe08> e08 = new Tp2kbE08DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbe08pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan E08 " + e08.size());
					if (e08.size() > 0) {
						for (Tp2kbe08 data: e08) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbe09> e09 = new Tp2kbE09DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbe09pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan E09 " + e09.size());
					if (e09.size() > 0) {
						for (Tp2kbe09 data: e09) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbe10> e10 = new Tp2kbE10DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbe10pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan E10 " + e10.size());
					if (e10.size() > 0) {
						for (Tp2kbe10 data: e10) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
					
					List<Tp2kbe11> e11 = new Tp2kbE11DAO().listNativeByFilter("tanggotafk = " + 
							book.getTanggota().getTanggotapk() + " and tp2kbbookfk is null and tglmulai between '" + dateFormatter.format(book.getTglmulai()) + "' and '" + dateFormatter.format(book.getTglakhir()) + "'", "tp2kbe11pk");
					System.out.println("Book Ke " + counter + " Total Kegiatan E11 " + e11.size());
					if (e11.size() > 0) {
						for (Tp2kbe11 data: e11) {
							Session session = StoreHibernateUtil.openSession();
							Transaction trx = session.beginTransaction();
							try {
								data.setTp2kbbook(book);
								trx.commit();
							} catch (Exception e) {
								trx.rollback();
								e.printStackTrace();
							} finally {
								session.close();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
