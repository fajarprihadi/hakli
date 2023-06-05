package com.sds.hakli.viewmodel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;

import com.sds.hakli.domain.Tanggota;
import com.sds.utils.AppUtils;
import com.sds.utils.StringUtils;

public class NaskahVm {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota obj;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		obj = (Tanggota) zkSession.getAttribute("anggota");

	}

	@Command
	public void doDownload(@BindingParam("arg") String arg) {
		try {
			Map<String, Object> parameters = new HashMap<>();
			List<Tanggota> objList = new ArrayList<>();
			objList.add(obj);
			zkSession.setAttribute("objList", objList);
			String currentdate = "";
			
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
			int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			
			currentdate = day + " " + StringUtils.getMonthLabel(month) + " " + year;

			String filepath = "naskahsumpah.jasper";
			String filepath2 = "naskahsumpah2.jasper";
			if (arg.equals("islam")) {
				parameters.put("SALAMNASKAH", "Semoga Allah Subhanahu wa ta’ala memberikan kekuatan kepada saya");
				parameters.put("SUMPAHAGAMA", "Demi Allah saya bersumpah");
				parameters.put("AGAMA", "Islam");
			} else if (arg.equals("khatolik")) {
				parameters.put("SALAMNASKAH", "Kiranya Tuhan menolong saya");
				parameters.put("SUMPAHAGAMA", "Demi Tuhan saya berjanji");
				parameters.put("AGAMA", "Khatolik");
			} else if (arg.equals("protestan")) {
				parameters.put("SALAMNASKAH", "Kiranya Tuhan menolong saya");
				parameters.put("SUMPAHAGAMA", "Demi Tuhan saya berjanji");
				parameters.put("AGAMA", "Protestan");
			} else if (arg.equals("hindu")) {
				parameters.put("SALAMNASKAH", "Om Santi Santi Santi Om");
				parameters.put("SUMPAHAGAMA", "Om Attah Paramawisesa saya bersumpah");
				parameters.put("AGAMA", "Hindu");
			} else if (arg.equals("budha")) {
				parameters.put("SALAMNASKAH", "Om Santi Santi Santi Om");
				parameters.put("SUMPAHAGAMA", "Demi yang Tiratana saya berjanji");
				parameters.put("AGAMA", "Budha");
			} else if (arg.equals("etik")) {
				filepath = "naskahetika.jasper";
				filepath2 = "naskahetika2.jasper";
			} else if (arg.equals("sertifikasi")) {
				filepath = "naskahsertifikasi.jasper";
				filepath2 = "";
			}

			parameters.put("CURRENTDATE", currentdate);
			parameters.put("FOTO",
					Executions.getCurrent().getDesktop().getWebApp().getRealPath(AppUtils.PATH_PHOTO + "/" + obj.getPhotolink()));
			parameters.put("TTD_KETUAUMUM",
					Executions.getCurrent().getDesktop().getWebApp().getRealPath("images/ttd_mengangkatsumpah.png"));
			parameters.put("TTD_SAKSI",
					Executions.getCurrent().getDesktop().getWebApp().getRealPath("images/ttd_saksi.jpg"));
			parameters.put("LOGO",
					Executions.getCurrent().getDesktop().getWebApp().getRealPath("img/hakli.png"));

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
