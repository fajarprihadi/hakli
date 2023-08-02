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

public class P2kbbookSum {

	public static void main(String[] args) {
		System.out.println("P2KBBOOK Updating Summary Kegiatan");
		Tp2kbbookDAO bookDao = new Tp2kbbookDAO();
		try {
			List<Tp2kbbook> objList = bookDao.listNativeByFilter("tp2kbbookpk > 6500 and isscrap = 'Y'", "tp2kbbookpk");
			System.out.println("Total Data Book " + objList.size());
			int counter = 0;
			for (Tp2kbbook book: objList) {
				counter++;
				System.out.println("Data Book Ke " + counter);
				Integer totalkegiatan = bookDao.countKegiatan(book.getTp2kbbookpk());
				BigDecimal totalskpbook = bookDao.sumSkp(book.getTp2kbbookpk());
				
				Session session = StoreHibernateUtil.openSession();
				Transaction trx = session.beginTransaction();
				try {
					book.setTotalkegiatan(totalkegiatan);
					book.setTotalskp(totalskpbook);
					trx.commit();
				} catch (Exception e) {
					trx.rollback();
					e.printStackTrace();
				} finally {
					session.close();
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
