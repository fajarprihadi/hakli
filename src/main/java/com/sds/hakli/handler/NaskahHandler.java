package com.sds.hakli.handler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;

import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Teventreg;
import com.sds.utils.AppUtils;

public class NaskahHandler {
	
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	
	public void downloadNaskah(Teventreg data, String arg) {
		try {
			Map<String, Object> parameters = new HashMap<>();
			List<Tanggota> objList = new ArrayList<>();
			objList.add(data.getTanggota());
			zkSession.setAttribute("objList", objList);
			String currentdate = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id", "ID")).format(data.getPaidat());
			String localdate = new SimpleDateFormat("EEEE, dd MMMMM yyyy", new Locale("id", "ID")).format(new Date());
			String tgllahir = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id", "ID")).format(data.getTanggota().getTgllahir());

			String nosurat = "";
			String filepath = "naskahsumpah.jasper";
			String filepath2 = "naskahsumpah2.jasper";
			if (arg.equals("sumpah")) {
				nosurat = data.getSpno();
				System.out.println("'" + data.getTanggota().getAgama() + "'");
				if (data.getTanggota().getAgama().toUpperCase().equals("ISLAM")) {
					parameters.put("SALAMNASKAH", "Semoga Allah Subhanahu wa ta’ala memberikan kekuatan kepada saya");
					parameters.put("SUMPAHAGAMA", "Demi Allah saya bersumpah");
					parameters.put("AGAMA", "Islam");
				} else if (data.getTanggota().getAgama().toUpperCase().equals("KHATOLIK")) {
					parameters.put("SALAMNASKAH", "Kiranya Tuhan menolong saya");
					parameters.put("SUMPAHAGAMA", "Demi Tuhan saya berjanji");
					parameters.put("AGAMA", "Khatolik");
				} else if (data.getTanggota().getAgama().toUpperCase().equals("PROTESTAN")) {
					parameters.put("SALAMNASKAH", "Kiranya Tuhan menolong saya");
					parameters.put("SUMPAHAGAMA", "Demi Tuhan saya berjanji");
					parameters.put("AGAMA", "Protestan");
				} else if (data.getTanggota().getAgama().toUpperCase().equals("HINDU")) {
					parameters.put("SALAMNASKAH", "Om Santi Santi Santi Om");
					parameters.put("SUMPAHAGAMA", "Om Attah Paramawisesa saya bersumpah");
					parameters.put("AGAMA", "Hindu");
				} else if (data.getTanggota().getAgama().toUpperCase().equals("BUDHA")) {
					parameters.put("SALAMNASKAH", "Om Santi Santi Santi Om");
					parameters.put("SUMPAHAGAMA", "Demi yang Tiratana saya berjanji");
					parameters.put("AGAMA", "Budha");
				}
			} else if (arg.equals("etik")) {
				nosurat = data.getKeno();
				filepath = "naskahetika.jasper";
				filepath2 = "naskahetika2.jasper";
			} else if (arg.equals("sertifikasi")) {
				filepath = "naskahsertifikasi.jasper";
				filepath2 = "";
			}

			String photoname = "";

			File file = new File(AppUtils.PATH_PHOTO + "/" + data.getTanggota().getPhotolink());
			if (file.exists()) {
				//System.out.println("ADA FOTO");
				photoname = data.getTanggota().getPhotolink();
			} else {
				//System.out.println("TIDAK ADA FOTO");
				photoname = "default.png";
			}
			
			String location = data.getTevent().getEventlocation();

			String gelardepan = "";
			String gelarbelakang = "";

			if (data.getTanggota().getGelarbelakang() != null)
				gelarbelakang = " " + data.getTanggota().getGelarbelakang();

			if (data.getTanggota().getGelardepan() != null)
				gelardepan = data.getTanggota().getGelardepan() + ". ";

			parameters.put("GELARBELAKANG", gelarbelakang);
			parameters.put("GELARDEPAN", gelardepan);
			parameters.put("NOSURAT", nosurat);
			parameters.put("LOCATION", location);
			parameters.put("CURRENTDATE", currentdate);
			parameters.put("LOCALDATE", localdate);
			parameters.put("TGLLAHIR", tgllahir);
			parameters.put("FOTO", Executions.getCurrent().getDesktop().getWebApp()
					.getRealPath(AppUtils.PATH_PHOTO + "/" + photoname));
			parameters.put("TTD_KETUAUMUM",
					Executions.getCurrent().getDesktop().getWebApp().getRealPath("images/ttd_ketum.png"));
			parameters.put("TTD_SAKSI",
					Executions.getCurrent().getDesktop().getWebApp().getRealPath("images/ttd_saksi.jpg"));
			parameters.put("LOGO", Executions.getCurrent().getDesktop().getWebApp().getRealPath("img/hakli.png"));

			zkSession.setAttribute("parameters", parameters);
			zkSession.setAttribute("reportPath", Executions.getCurrent().getDesktop().getWebApp()
					.getRealPath(AppUtils.PATH_JASPER + "/" + filepath));

			if (filepath2.trim().length() > 0) {
				zkSession.setAttribute("reportPath2", Executions.getCurrent().getDesktop().getWebApp()
						.getRealPath(AppUtils.PATH_JASPER + "/" + filepath2));
			}

			Executions.getCurrent().sendRedirect("/view/jasperviewer.zul", "_blank");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
