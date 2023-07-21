package com.sds.hakli;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TcounterengineDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.utils.AppData;

public class TEST {

	public static void main(String[] args) {
		try {
			Tanggota obj = new TanggotaDAO().findByFilter("noanggota = '3200016'");
			Map<Integer, String> mapRomawi = new HashMap<>();
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

			mapRomawi = AppData.getAngkaRomawi();
			String nosurat =obj.getProvcode() + new TcounterengineDAO()
					.getLetterRecomNo("/REKOM/PP-HAKLI/" + mapRomawi.get(month) + "/" + year, 4);


			System.out.println("NO SURAT : " + nosurat);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
