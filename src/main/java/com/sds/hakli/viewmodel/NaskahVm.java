package com.sds.hakli.viewmodel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;

import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Teventreg;
import com.sds.utils.AppUtils;

public class NaskahVm {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota objAnggota;
	private Teventreg obj;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Teventreg obj) {
		Selectors.wireComponents(view, this, false);
		
		if(obj != null) {
			this.obj = obj;
			objAnggota = obj.getTanggota();
		}

	}

	@Command
	public void doDownload(@BindingParam("arg") String arg) {
		try {
			Map<String, Object> parameters = new HashMap<>();
			List<Tanggota> objList = new ArrayList<>();
			objList.add(objAnggota);
			zkSession.setAttribute("objList", objList);
			String currentdate = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id", "ID")).format(new Date());
			String localdate = new SimpleDateFormat("EEEE, dd MMMMM yyyy", new Locale("id", "ID")).format(new Date());
			String tgllahir = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id", "ID")).format(objAnggota.getTgllahir());

			String filepath = "naskahsumpah.jasper";
			String filepath2 = "naskahsumpah2.jasper";
			if (arg.equals("naskah")) {
				if (objAnggota.getAgama().toUpperCase().equals("ISLAM")) {
					parameters.put("SALAMNASKAH", "Semoga Allah Subhanahu wa ta’ala memberikan kekuatan kepada saya");
					parameters.put("SUMPAHAGAMA", "Demi Allah saya bersumpah");
					parameters.put("AGAMA", "Islam");
				} else if (objAnggota.getAgama().toUpperCase().equals("KHATOLIK")) {
					parameters.put("SALAMNASKAH", "Kiranya Tuhan menolong saya");
					parameters.put("SUMPAHAGAMA", "Demi Tuhan saya berjanji");
					parameters.put("AGAMA", "Khatolik");
				} else if (objAnggota.getAgama().toUpperCase().equals("PROTESTAN")) {
					parameters.put("SALAMNASKAH", "Kiranya Tuhan menolong saya");
					parameters.put("SUMPAHAGAMA", "Demi Tuhan saya berjanji");
					parameters.put("AGAMA", "Protestan");
				} else if (objAnggota.getAgama().toUpperCase().equals("HINDU")) {
					parameters.put("SALAMNASKAH", "Om Santi Santi Santi Om");
					parameters.put("SUMPAHAGAMA", "Om Attah Paramawisesa saya bersumpah");
					parameters.put("AGAMA", "Hindu");
				} else if (objAnggota.getAgama().toUpperCase().equals("BUDHA")) {
					parameters.put("SALAMNASKAH", "Om Santi Santi Santi Om");
					parameters.put("SUMPAHAGAMA", "Demi yang Tiratana saya berjanji");
					parameters.put("AGAMA", "Budha");
				}
			}  else if (arg.equals("etik")) {
				filepath = "naskahetika.jasper";
				filepath2 = "naskahetika2.jasper";
			} else if (arg.equals("sertifikasi")) {
				filepath = "naskahsertifikasi.jasper";
				filepath2 = "";
			}

			String photoname = "";

			File file = new File(AppUtils.PATH_PHOTO + "/" + objAnggota.getPhotolink());
			if (file.exists()) {
				System.out.println("ADA FOTO");
				photoname = objAnggota.getPhotolink();
			} else {
				System.out.println("TIDAK ADA FOTO");
				photoname = "default.png";
			}
			
			String nosurat = obj.getTevent().getEventid().trim();

			parameters.put("NOSURAT", nosurat);
			parameters.put("CURRENTDATE", currentdate);
			parameters.put("LOCALDATE", localdate);
			parameters.put("TGLLAHIR", tgllahir);
			parameters.put("FOTO", Executions.getCurrent().getDesktop().getWebApp()
					.getRealPath(AppUtils.PATH_PHOTO + "/" + photoname));
			parameters.put("TTD_KETUAUMUM",
					Executions.getCurrent().getDesktop().getWebApp().getRealPath("images/ttd_mengangkatsumpah.png"));
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
