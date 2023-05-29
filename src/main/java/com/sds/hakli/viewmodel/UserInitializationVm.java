package com.sds.hakli.viewmodel;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlNativeComponent;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;

import com.sds.hakli.dao.MusergroupmenuDAO;
import com.sds.hakli.domain.Musergroupmenu;
import com.sds.hakli.domain.Tanggota;
import com.sds.utils.AppUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInitializationVm {
	
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota oUser;
	
	@Wire
	private Div divContent;
	@Wire
	private Div divAccord;

//	@Init
//	public void init(){
//		oUser = (Tanggota) zkSession.getAttribute("anggota");
//		if(oUser == null){
//			Executions.sendRedirect("/");
//		}
//	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			oUser = (Tanggota) zkSession.getAttribute("anggota");
			
			if (oUser != null) {
				doRenderMenu();
				Map<String, Object> map = new HashMap<>();
				
				if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_ANGGOTABIASA)) {
					map.put("obj", oUser);
					map.put("acttype", "edit");
					
					Executions.createComponents("/view/anggota/anggotaedit.zul", divContent, map);
				} else if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSPROVINSI)) {
					Executions.createComponents("/view/dashboardprov.zul", divContent, null);
				} else if (oUser.getMusergroup().getUsergroupcode().equals(AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN)) {
					Executions.createComponents("/view/dashboardkab.zul", divContent, null);
				} else {
					Executions.createComponents("/view/dashboard.zul", divContent, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doRenderMenu() {
		try {
			String menugroup = "";
			
			Div divAccordItem = null;
			Div divAccordBody = null;
			int idx = 0;
			//int idxParent = 0;
			
			List<Musergroupmenu> oList = new MusergroupmenuDAO().listByFilter("musergroup.musergrouppk = "
					+ oUser.getMusergroup().getMusergrouppk(),
					"mmenu.menuorderno, mmenu.menuname");

			for (final Musergroupmenu obj : oList) {
				if (!menugroup.equals(obj.getMmenu().getMenugroup())) {
					idx++;
					
					divAccordItem = new Div();
					divAccordItem.setSclass("accordion-item");
					divAccordItem.setParent(divAccord);
					
					HtmlNativeComponent h2 = new HtmlNativeComponent("h2");
					h2.setClientAttribute("id", "headingOne" + idx);
					h2.setClientAttribute("class", "accordion-header headingOne" + idx);
					h2.setParent(divAccordItem);
					
					HtmlNativeComponent buttonHeader = new HtmlNativeComponent("button");
					buttonHeader.setClientAttribute("class", "accordion-button collapsed");
					buttonHeader.setClientAttribute("type", "button");
					buttonHeader.setClientAttribute("data-mdb-toggle", "collapse");
					buttonHeader.setClientAttribute("data-mdb-target", ".collapseOne" + idx);
					buttonHeader.setClientAttribute("aria-expanded", "false");
					buttonHeader.setClientAttribute("aria-controls", "collapseOne" + idx);
					buttonHeader.appendChild(new Html(obj.getMmenu().getMenugroup()));
					buttonHeader.setParent(h2);
					
					HtmlNativeComponent divCollapse = new HtmlNativeComponent("div");
					divCollapse.setClientAttribute("class", "accordion-collapse collapse collapseOne" + idx);
					divCollapse.setClientAttribute("aria-labelledby", ".headingOne" + idx);
					divCollapse.setClientAttribute("data-mdb-parent", ".headingOne"+ idx);
					divCollapse.setParent(divAccordItem);
					
					divAccordBody = new Div();
					divAccordBody.setSclass("accordion-body");
					divAccordBody.setParent(divCollapse);
					
				}
				menugroup = obj.getMmenu().getMenugroup();
				
				A a = new A();
				a.setHref("");
				a.setSclass("list-group-item list-group-item-action py-2 ");
				a.setParent(divAccordBody);
				
				HtmlNativeComponent i = new HtmlNativeComponent("i");
				i.setClientAttribute("class", "fas fa-circle fa-fw me-3");
				HtmlNativeComponent span = new HtmlNativeComponent("span");
				span.appendChild(new Html(obj.getMmenu().getMenuname()));
				a.appendChild(i);
				a.appendChild(span);
				a.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						A aPrev = (A) divAccord.getAttribute("active");
						if (aPrev != null)
							aPrev.setSclass("list-group-item list-group-item-action py-2");
						
						a.setSclass("list-group-item list-group-item-action py-2 active");
						divAccord.setAttribute("active", a);
						
						Clients.evalJavaScript("hidenavbar()");
						
						divContent.getChildren().clear();
						divContent.setVisible(false);
						
						Map<String, String> map = new HashMap<String, String>();
						map.put(obj.getMmenu().getMenuparamname(), obj.getMmenu().getMenuparamvalue());
						
						System.out.println(obj.getMmenu().getMenuparamname() + " : " + obj.getMmenu().getMenuparamvalue());
						
						Executions.createComponents(obj.getMmenu().getMenupath(), divContent, map);
						divContent.setVisible(true);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doLogout() {
		if (zkSession.getAttribute("anggota") != null) {
			zkSession.removeAttribute("anggota");
		}
		Executions.sendRedirect("/logout.zul");
	}

	@Command
	public void openProfile(){
		divContent.getChildren().clear();
		divContent.setVisible(false);
		Executions.createComponents("/view/profile/profile.zul", divContent, null);
		divContent.setVisible(true);
	}

	
}