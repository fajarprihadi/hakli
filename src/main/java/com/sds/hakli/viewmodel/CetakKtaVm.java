package com.sds.hakli.viewmodel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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

import com.sds.hakli.domain.Tanggota;
import com.sds.utils.AppUtils;
import com.sds.utils.StringUtils;

public class CetakKtaVm {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota obj;

	private List<Tanggota> objList = new ArrayList<>();
	private Map<String, Object> parameters = new HashMap<>();

	private String filepath = "";

	@Wire
	private Div div;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		obj = (Tanggota) zkSession.getAttribute("anggota");
		objList.add(obj);
		String currentdate = "";
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		
		currentdate = day + " " + StringUtils.getMonthLabel(month) + " " + year;
		parameters.put("CURRENTDATE", currentdate);
		parameters.put("FOTO", Executions.getCurrent().getDesktop().getWebApp()
				.getRealPath(AppUtils.PATH_PHOTO + "/" + obj.getPhotolink()));
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
