package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlNativeComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.TeventDAO;
import com.sds.hakli.dao.TeventregDAO;
import com.sds.hakli.domain.Tevent;
import com.sds.hakli.domain.Veventamount;
import com.sds.utils.AppUtils;

public class EventVm {

	private TeventDAO oDao = new TeventDAO();
	private TeventregDAO eventregDao = new TeventregDAO();

	@Wire
	private Window winEvent;
	@Wire
	private Div divCards;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			doRender();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doAdd() {
		Map<String, Object> map = new HashMap<String, Object>();
		Window win = (Window) Executions
				.createComponents("/view/event/eventform.zul", null, map);
		win.setClosable(true);
		win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				doRender();
			}
		});
		win.doModal();
	}

	public void doRender() {
		try {
			divCards.getChildren().clear();
			for (Tevent obj : oDao.listByFilter("0=0", "eventdate desc")) {
				Div card = new Div();
				card.setSclass("card mx-4 mx-md-5 shadow-5-strong text-center");
				Image img = new Image(AppUtils.PATH_EVENT + "/" + obj.getEventimg());
				img.setSclass("card-img-top");
				img.setParent(card);
				Div cardbody = new Div();
				cardbody.setSclass("card-body");
				cardbody.setParent(card);
				HtmlNativeComponent h5 = new HtmlNativeComponent("h5");
				h5.appendChild(new Html(obj.getEventname()));
				h5.setParent(cardbody);
				HtmlNativeComponent p = new HtmlNativeComponent("p");
				p.appendChild(new Html(obj.getEventdesc()));
				p.setParent(cardbody);
				
				Veventamount vsum = eventregDao.sumAmount(obj.getTeventpk());

				HtmlNativeComponent ul = new HtmlNativeComponent("ul");
				ul.setClientAttribute("class", "list-group list-group-light list-group-small");
				ul.setParent(card);
				HtmlNativeComponent li0 = new HtmlNativeComponent("li");
				li0.setClientAttribute("class", "list-group-item px-4");
				li0.appendChild(new Html("Event ID " + obj.getEventid()));
				li0.setParent(ul);
				HtmlNativeComponent li1 = new HtmlNativeComponent("li");
				li1.setClientAttribute("class", "list-group-item px-4");
				li1.appendChild(new Html("Jumlah Pendaftar " + vsum.getTotalreg()));
				li1.setParent(ul);
				HtmlNativeComponent li2 = new HtmlNativeComponent("li");
				li2.setClientAttribute("class", "list-group-item px-4");
				li2.appendChild(new Html("Potensi Pendapatan Rp. " + NumberFormat.getInstance().format(vsum.getInvamount())));
				li2.setParent(ul);
				HtmlNativeComponent li3 = new HtmlNativeComponent("li");
				li3.setClientAttribute("class", "list-group-item px-4");
				li3.appendChild(new Html("Realisasi Pendapatan Rp. " + NumberFormat.getInstance().format(vsum.getPaymentamount())));
				li3.setParent(ul);
				HtmlNativeComponent li4 = new HtmlNativeComponent("li");
				li4.setClientAttribute("class", "list-group-item px-4");
				Button btDetail = new Button("Detail");
				btDetail.setSclass("btn btn-primary");
				btDetail.setAutodisable("self");
				btDetail.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						winEvent.getChildren().clear();
						Map<String, Object> map = new HashMap<>();
						map.put("obj", obj);
						Executions.createComponents("/view/event/eventdetail.zul", winEvent, map);
					}
				});
				li4.appendChild(btDetail);
				li4.setParent(ul);

				Div cardfooter = new Div();
				cardfooter.setSclass("card-footer text-muted");
				cardfooter.appendChild(new Html(new SimpleDateFormat("dd-MM-yyyy").format(obj.getEventdate())));
				cardfooter.setParent(card);

				divCards.appendChild(card);
				divCards.appendChild(new HtmlNativeComponent("br"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
