package com.sds.hakli.utils;

import java.math.BigDecimal;
import java.util.Date;

import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Teventreg;
import com.sds.hakli.domain.Tinvoice;
import com.sds.hakli.domain.Tp2kbbook;

public class InvoiceGenerator {

	public Tinvoice doInvoice(Object obj, String vano, String invtype, BigDecimal amount, String desc, Date duedate) {
		Tinvoice inv = new Tinvoice();
		try {
			inv.setInvoiceno(new TcounterengineDAO().getInvoiceCounter());
			if (obj instanceof Tanggota)
				inv.setTanggota((Tanggota) obj);
			else if (obj instanceof Teventreg)
				inv.setTeventreg((Teventreg) obj);
			else if (obj instanceof Tp2kbbook)
				inv.setTp2kbbook((Tp2kbbook) obj);
			//inv.setCreatedby(anggota.getCreatedby());
			inv.setCreatetime(new Date());
			inv.setInvoiceamount(amount);
			inv.setInvoicedate(new Date());
			inv.setInvoicedesc(desc);
			inv.setInvoiceduedate(duedate);
			inv.setInvoicetype(invtype);
			inv.setVano(vano);
			inv.setIspaid("N");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inv;
	}
}
