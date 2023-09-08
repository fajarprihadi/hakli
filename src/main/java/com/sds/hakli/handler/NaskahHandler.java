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
			String currentdate = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id", "ID"))
					.format(data.getTevent().getEventdate());
			String localdate = new SimpleDateFormat("EEEE, dd MMMMM yyyy", new Locale("id", "ID")).format(data.getTevent().getEventdate());
			String tgllahir = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id", "ID"))
					.format(data.getTanggota().getTgllahir());

			String nosurat = "";
			String eventcity = data.getTevent().getEventcity();
			String filepath = "naskahsumpah.jasper";
			String filepath2 = "naskahsumpah2.jasper";
			if (arg.equals("sumpah")) {
				nosurat = data.getSpno();
				if (data.getTanggota().getAgama() != null) {
					//System.out.println("'" + data.getTanggota().getAgama() + "'");
					if (data.getTanggota().getAgama().toUpperCase().equals("ISLAM")) {
						parameters.put("SALAMNASKAH",
								"Semoga Allah Subhanahu wa ta’ala memberikan kekuatan kepada saya");
						parameters.put("SUMPAHAGAMA", "Demi Allah saya bersumpah");
						parameters.put("AGAMA", "Islam");
					} else if (data.getTanggota().getAgama().toUpperCase().equals("KATOLIK")) {
						parameters.put("SALAMNASKAH", "Kiranya Tuhan menolong saya");
						parameters.put("SUMPAHAGAMA", "Demi Tuhan saya berjanji");
						parameters.put("AGAMA", "Katolik");
					} else if (data.getTanggota().getAgama().toUpperCase().equals("PROTESTAN")) {
						parameters.put("SALAMNASKAH", "Kiranya Tuhan menolong saya");
						parameters.put("SUMPAHAGAMA", "Demi Tuhan saya berjanji");
						parameters.put("AGAMA", "Protestan");
					} else if (data.getTanggota().getAgama().toUpperCase().equals("HINDU")) {
						parameters.put("SALAMNASKAH", "Om Santi Santi Santi Om");
						parameters.put("SUMPAHAGAMA", "Om Attah Paramawisesa saya bersumpah");
						parameters.put("AGAMA", "Hindu");
					} else if (data.getTanggota().getAgama().toUpperCase().equals("BUDDHA")) {
						parameters.put("SALAMNASKAH", "Om Santi Santi Santi Om");
						parameters.put("SUMPAHAGAMA", "Demi yang Tiratana saya berjanji");
						parameters.put("AGAMA", "Buddha");
					}
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

			File file = new File(Executions.getCurrent().getDesktop().getWebApp()
					.getRealPath(AppUtils.PATH_PHOTO + "/" + data.getTanggota().getPhotolink()));
			if (file.exists()) {
				// System.out.println("ADA FOTO");
				photoname = data.getTanggota().getPhotolink();
			} else {
				// System.out.println("TIDAK ADA FOTO");
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
			parameters.put("EVENTCITY", eventcity);
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
	
	public void downloadSertifikat(Teventreg data) {
		try {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("NAMA", data.getTanggota().getGelarbelakang() != null && data.getTanggota().getGelarbelakang().trim().length() > 0 ? 
					data.getTanggota().getNama() + ", " + data.getTanggota().getGelarbelakang() : data.getTanggota().getNama());
			List<Tanggota> objList = new ArrayList<>();
			objList.add(data.getTanggota());
			zkSession.setAttribute("objList", objList);
			
			parameters.put("TEMPLATEPATH", Executions.getCurrent().getDesktop().getWebApp()
					.getRealPath("/") + data.getTevent().getCertpath());
			
			zkSession.setAttribute("parameters", parameters);
			zkSession.setAttribute("reportPath", Executions.getCurrent().getDesktop().getWebApp()
					.getRealPath(AppUtils.PATH_JASPER + "/cert.jasper"));
			Executions.getCurrent().sendRedirect("/view/jasperviewer.zul", "_blank");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
