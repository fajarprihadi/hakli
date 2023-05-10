package com.sds.hakli.viewmodel;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;

import com.sds.hakli.domain.User;

public class UserInitializationVm {
	
	private Session session = Sessions.getCurrent();
	private User oUser;
	private String currentmenu;
	private Integer currentmenuidx;
	
	@Wire
	private Div divMenuAnggota;
	@Wire
	private Div divMenuP2KB;
	@Wire
	private Div divMenuNaskah;
	@Wire
	private Div divMenuTimP2KB;
	@Wire
	private Div divContent;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		oUser = (User) session.getAttribute("oUser");
		currentmenuidx = 0;
		currentmenu = "anggota";
		//doRedirect("/view/dashboard.zul", 0);
		//Executions.createComponents("/view/dashboard.zul", divContent, null);
		Executions.createComponents("/view/anggota/anggotaedit.zul", divContent, null);
	}
	
	@Command
	public void doRedirect(@BindingParam("path") String path, @BindingParam("menu") String menu, @BindingParam("index") Integer index) {
		try {
			Map<String, Object> map = new HashMap<>();
			
			Div divMenu = null;
			if (menu.equals("anggota"))
				divMenu = divMenuAnggota;
			else if (menu.equals("p2kb"))
				divMenu = divMenuP2KB;
			else if (menu.equals("naskah"))
				divMenu = divMenuNaskah;
			else if (menu.equals("timp2kb"))
				divMenu = divMenuTimP2KB;
			
			((A) divMenu.getChildren().get(index)).setSclass("list-group-item list-group-item-action py-2 ripple active");
			if (currentmenuidx != null && (currentmenuidx.compareTo(index) != 0 || 
					(currentmenuidx.compareTo(index) == 0 && !menu.equals(currentmenu)))) {
				Div divMenuPrev = null;
				if (currentmenu.equals("anggota"))
					divMenuPrev = divMenuAnggota;
				else if (currentmenu.equals("p2kb"))
					divMenuPrev = divMenuP2KB;
				else if (currentmenu.equals("naskah"))
					divMenuPrev = divMenuNaskah;
				else if (menu.equals("timp2kb"))
					divMenuPrev = divMenuTimP2KB;
				
				((A) divMenuPrev.getChildren().get(currentmenuidx)).setSclass("list-group-item list-group-item-action py-2 ripple");
			}
			currentmenu = menu;
			currentmenuidx = index;
			
			Clients.evalJavaScript("hidenavbar()");
			
			divContent.getChildren().clear();
			Executions.createComponents(path, divContent, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Command
	public void doLogout() {
		if (session.getAttribute("oUser") != null) {
			session.removeAttribute("oUser");
		}
		Executions.sendRedirect("/logout.zul");
	}

}
