import java.math.BigDecimal;
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

public class P2kbSum {

	public static void main(String[] args) {
		System.out.println("P2KB Updating Summary Kegiatan");
		Tp2kbbookDAO bookDao = new Tp2kbbookDAO();
		try {
			List<Mp2kbkegiatan> kegiatans = new Mp2kbkegiatanDAO().listByFilter("0=0", "idkegiatan");
			List<Tp2kbbook> objList = bookDao.listNativeByFilter("isscrapp2kb != 'Y'", "tp2kbbookpk");
			System.out.println("Total Data Book " + objList.size());
			int counter = 0;
			for (Tp2kbbook book: objList) {
				counter++;
				System.out.println("Data Book Ke " + counter);
				for (Mp2kbkegiatan kegiatan: kegiatans) {
					if (kegiatan.getIdkegiatan().equals("A01")) {
						try {
							List<Tp2kba01> a01 = new Tp2kbA01DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kba01pk");
							if (a01.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kba01 data: a01) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("A02")) {
						try {
							List<Tp2kba02> a02 = new Tp2kbA02DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kba02pk");
							if (a02.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kba02 data: a02) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("A03")) {
						try {
							List<Tp2kba03> a03 = new Tp2kbA03DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kba03pk");
							if (a03.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kba03 data: a03) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("A04")) {
						try {
							List<Tp2kba04> a04 = new Tp2kbA04DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kba04pk");
							if (a04.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kba04 data: a04) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("A05")) {
						try {
							List<Tp2kba05> a05 = new Tp2kbA05DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kba05pk");
							if (a05.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kba05 data: a05) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("A06")) {
						try {
							List<Tp2kba06> a06 = new Tp2kbA06DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kba06pk");
							if (a06.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kba06 data: a06) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B01")) {
						try {
							List<Tp2kbb01> b01 = new Tp2kbB01DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb01pk");
							if (b01.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb01 data: b01) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B02")) {
						try {
							List<Tp2kbb02> b02 = new Tp2kbB02DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb02pk");
							if (b02.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb02 data: b02) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B03")) {
						try {
							List<Tp2kbb03> b03 = new Tp2kbB03DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb03pk");
							if (b03.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb03 data: b03) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B04")) {
						try {
							List<Tp2kbb04> b04 = new Tp2kbB04DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb04pk");
							if (b04.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb04 data: b04) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B05")) {
						try {
							List<Tp2kbb05> b05 = new Tp2kbB05DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb05pk");
							if (b05.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb05 data: b05) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B06")) {
						try {
							List<Tp2kbb06> b06 = new Tp2kbB06DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb06pk");
							if (b06.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb06 data: b06) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B07")) {
						try {
							List<Tp2kbb07> b07 = new Tp2kbB07DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb07pk");
							if (b07.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb07 data: b07) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B08")) {
						try {
							List<Tp2kbb08> b08 = new Tp2kbB08DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb08pk");
							if (b08.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb08 data: b08) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B09")) {
						try {
							List<Tp2kbb09> b09 = new Tp2kbB09DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb09pk");
							if (b09.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb09 data: b09) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B10")) {
						try {
							List<Tp2kbb10> b10 = new Tp2kbB10DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb10pk");
							if (b10.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb10 data: b10) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B11")) {
						try {
							List<Tp2kbb11> b11 = new Tp2kbB11DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb11pk");
							if (b11.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb11 data: b11) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B12")) {
						try {
							List<Tp2kbb12> b12 = new Tp2kbB12DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb12pk");
							if (b12.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb12 data: b12) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B13")) {
						try {
							List<Tp2kbb13> b13 = new Tp2kbB13DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb13pk");
							if (b13.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb13 data: b13) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B14")) {
						try {
							List<Tp2kbb14> b14 = new Tp2kbB14DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb14pk");
							if (b14.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb14 data: b14) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B15")) {
						try {
							List<Tp2kbb15> b15 = new Tp2kbB15DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb15pk");
							if (b15.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb15 data: b15) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B16")) {
						try {
							List<Tp2kbb16> b16 = new Tp2kbB16DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb16pk");
							if (b16.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb16 data: b16) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B17")) {
						try {
							List<Tp2kbb17> b17 = new Tp2kbB17DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb17pk");
							if (b17.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb17 data: b17) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B18")) {
						try {
							List<Tp2kbb18> b18 = new Tp2kbB18DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb18pk");
							if (b18.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb18 data: b18) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B19")) {
						try {
							List<Tp2kbb19> b19 = new Tp2kbB19DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb19pk");
							if (b19.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb19 data: b19) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("B20")) {
						try {
							List<Tp2kbb20> b20 = new Tp2kbB20DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbb20pk");
							if (b20.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbb20 data: b20) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("C01")) {
						try {
							List<Tp2kbc01> c01 = new Tp2kbC01DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbc01pk");
							if (c01.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbc01 data: c01) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("C02")) {
						try {
							List<Tp2kbc02> c02 = new Tp2kbC02DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbc02pk");
							if (c02.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbc02 data: c02) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("D01")) {
						try {
							List<Tp2kbd01> d01 = new Tp2kbD01DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbd01pk");
							if (d01.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbd01 data: d01) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("D02")) {
						try {
							List<Tp2kbd02> d02 = new Tp2kbD02DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbd02pk");
							if (d02.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbd02 data: d02) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("E01")) {
						try {
							List<Tp2kbe01> e01 = new Tp2kbE01DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbe01pk");
							if (e01.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbe01 data: e01) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("E02")) {
						try {
							List<Tp2kbe02> e02 = new Tp2kbE02DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbe02pk");
							if (e02.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbe02 data: e02) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("E03")) {
						try {
							List<Tp2kbe03> e03 = new Tp2kbE03DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbe03pk");
							if (e03.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbe03 data: e03) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("E04")) {
						try {
							List<Tp2kbe04> e04 = new Tp2kbE04DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbe04pk");
							if (e04.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbe04 data: e04) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("E05")) {
						try {
							List<Tp2kbe05> e05 = new Tp2kbE05DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbe05pk");
							if (e05.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbe05 data: e05) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("E06")) {
						try {
							List<Tp2kbe06> e06 = new Tp2kbE06DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbe06pk");
							if (e06.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbe06 data: e06) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("E07")) {
						try {
							List<Tp2kbe07> e07 = new Tp2kbE07DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbe07pk");
							if (e07.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbe07 data: e07) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("E08")) {
						try {
							List<Tp2kbe08> e08 = new Tp2kbE08DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbe08pk");
							if (e08.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbe08 data: e08) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("E09")) {
						try {
							List<Tp2kbe09> e09 = new Tp2kbE09DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbe09pk");
							if (e09.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbe09 data: e09) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("E10")) {
						try {
							List<Tp2kbe10> e10 = new Tp2kbE10DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbe10pk");
							if (e10.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbe10 data: e10) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} else if (kegiatan.getIdkegiatan().equals("E11")) {
						try {
							List<Tp2kbe11> e11 = new Tp2kbE11DAO().listByFilter("tanggota.tanggotapk = " + book.getTanggota().getTanggotapk(), "tp2kbe11pk");
							if (e11.size() > 0) {
								Integer totalkegiatan = 0;
								Integer totalwaiting = 0;
								BigDecimal totalskp = new BigDecimal(0);
								BigDecimal totalskpwaiting = new BigDecimal(0);
								for (Tp2kbe11 data: e11) {
									if (data.getStatus() != null && data.getStatus().equals("W")) {
										totalwaiting = totalwaiting + 1;
										totalskpwaiting = totalskpwaiting.add(data.getNilaiskp());
									}
									
									if (data.getStatus() != null && (data.getStatus().equals("W") || data.getStatus().equals("A"))) {
										totalkegiatan = totalkegiatan + 1;
										totalskp = totalskp.add(data.getNilaiskp());
									}
								}
								p2kbInsert(book, kegiatan, totalkegiatan, totalwaiting, totalskp, totalskpwaiting);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
					
				}
				
				Session session = StoreHibernateUtil.openSession();
				Transaction trx = session.beginTransaction();
				try {
					book.setIsscrapp2kb("Y");
					bookDao.save(session, book);
					trx.commit();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					session.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void p2kbInsert(Tp2kbbook book, Mp2kbkegiatan kegiatan, Integer totalkegiatan, Integer totalwaiting, BigDecimal totalskp, BigDecimal totalskpwaiting) throws Exception {
		
//		System.out.println(book.getNostr());
//		System.out.println(book.getTanggota().getNama());
//		System.out.println(kegiatan.getIdkegiatan());
//		System.out.println(totalkegiatan);
//		System.out.println(totalwaiting);
//		System.out.println(totalskp);
//		System.out.println(totalskpwaiting);
//		System.out.println("---------------");
		
		Session session = StoreHibernateUtil.openSession();
		Transaction trx = session.beginTransaction();
		try {
			Tp2kb p2kb = new Tp2kb();
			p2kb.setIsscrap("Y");
			p2kb.setTp2kbbook(book);
			p2kb.setTanggota(book.getTanggota());
			p2kb.setMp2kbkegiatan(kegiatan);
			p2kb.setTotalkegiatan(totalkegiatan);
			p2kb.setTotalwaiting(totalwaiting);
			p2kb.setTotalskp(totalskp);
			p2kb.setTotalskpwaiting(totalskpwaiting);
			p2kb.setLastupdated(new Date());
			new Tp2kbDAO().save(session, p2kb);
			trx.commit();
		} catch (Exception e) {
			trx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

}
