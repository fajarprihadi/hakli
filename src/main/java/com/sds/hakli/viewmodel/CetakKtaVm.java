package com.sds.hakli.viewmodel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;

import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.domain.Tanggota;
import com.sds.utils.AppUtils;

public class CetakKtaVm {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota obj;

	private List<Tanggota> objList = new ArrayList<>();
	private Map<String, Object> parameters = new HashMap<>();

	private String filepath = "";

	@Wire
	private Div div;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		Selectors.wireComponents(view, this, false);
		obj = (Tanggota) zkSession.getAttribute("anggota");
		obj = new TanggotaDAO().findByFilter("noanggota = '" + obj.getNoanggota() + "'");
		if (obj != null) {
			objList.add(obj);
			String currentdate = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id", "ID")).format(new Date());
			String localdate = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id", "ID")).format(obj.getTgllahir());
			String periodekta = "";
			if (obj.getPeriodekta() != null)
				periodekta = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id", "ID")).format(obj.getPeriodekta());

			String photoname = "";

			File file = new File(Executions.getCurrent().getDesktop().getWebApp()
					.getRealPath(AppUtils.PATH_PHOTO + "/" + obj.getPhotolink()));
			if (file.exists()) {
				System.out.println("ADA FOTO");
				photoname = obj.getPhotolink();
			} else {
				System.out.println("TIDAK ADA FOTO");
				photoname = "default.png";
			}

			parameters.put("TGLLAHIR", localdate);
			parameters.put("PERIODEKTA", periodekta);
			parameters.put("CURRENTDATE", currentdate);
			parameters.put("FOTO", Executions.getCurrent().getDesktop().getWebApp()
					.getRealPath(AppUtils.PATH_PHOTO + "/" + photoname));
			parameters.put("CARD1",
					Executions.getCurrent().getDesktop().getWebApp().getRealPath("images/cetakkta1.jpg"));
			parameters.put("CARD2",
					Executions.getCurrent().getDesktop().getWebApp().getRealPath("images/cetakkta2.jpg"));

			filepath = Executions.getCurrent().getDesktop().getWebApp()
					.getRealPath(AppUtils.PATH_JASPER + "/cetakkta.jasper");

			zkSession.setAttribute("parameters", parameters);
			zkSession.setAttribute("reportPath", filepath);
			zkSession.setAttribute("objList", objList);

			Executions.createComponents("/view/jasperviewer.zul", div, null);
		}
	}

}
