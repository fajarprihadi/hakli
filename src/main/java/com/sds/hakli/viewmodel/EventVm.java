package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
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
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.TeventDAO;
import com.sds.hakli.dao.TeventregDAO;
import com.sds.hakli.domain.Tevent;
import com.sds.hakli.domain.Veventamount;
import com.sds.utils.AppUtils;

public class EventVm {

	private TeventDAO oDao = new TeventDAO();
	private TeventregDAO eventregDao = new TeventregDAO();
	
	private String eventname;
	private Date begindate;
	private Date enddate;
	private String filter;
	private Integer totalrecord;
	
	private SimpleDateFormat datelocalFormatter = new SimpleDateFormat("yyyy-MM-dd");

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
	
	public void doDefaultPeriod() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		begindate = cal.getTime();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, 2);
		enddate = cal.getTime();
	}
	
	@Command
	@NotifyChange("*")
	public void doAdd(Tevent obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("obj", obj);
		Window win = (Window) Executions
				.createComponents("/view/event/eventform.zul", null, map);
		win.setClosable(true);
		win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				doRender();
				BindUtils.postNotifyChange(EventVm.this, "*");
			}
		});
		win.doModal();
	}

	@Command
	@NotifyChange("*")
	public void doRender() {
		try {
			if (begindate == null || enddate == null) {
				doDefaultPeriod();
			}
			filter = "eventdate between '" + datelocalFormatter.format(begindate) + "' and '" + datelocalFormatter.format(enddate) + "'";
			if (eventname != null && eventname.trim().length() > 0)
				filter += " and upper(eventname) like '%" + eventname.trim().toUpperCase() + "%'";
			
			divCards.getChildren().clear();
			Div divRow = new Div();
			divRow.setSclass("row d-flex");
			divRow.setParent(divCards);
			int i = 0;
			List<Tevent> objList = oDao.listByFilter(filter, "eventdate desc");
			totalrecord = objList.size();
			for (Tevent obj : objList) {
				if (i % 2 == 0) {
					divCards.appendChild(new HtmlNativeComponent("br"));
					divRow = new Div();
					divRow.setSclass("row d-flex");
					divRow.setParent(divCards);
				}
				Div divCol = new Div();
				divCol.setSclass("col-md-6 col-sm-12");
				Div card = new Div();
				card.setSclass("card mx-4 mx-md-5 shadow-5-strong text-center");
				Image img = new Image(AppUtils.PATH_EVENT + "/" + obj.getEventimg());
				img.setSclass("card-img-top");
				//img.setHeight("90px");
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
				HtmlNativeComponent li11 = new HtmlNativeComponent("li");
				li11.setClientAttribute("class", "list-group-item px-4");
				li11.appendChild(new Html("Tanggal Pelaksanaan " + new SimpleDateFormat("dd-MM-yyyy").format(obj.getEventdate())));
				li11.setParent(ul);
				HtmlNativeComponent li12 = new HtmlNativeComponent("li");
				li12.setClientAttribute("class", "list-group-item px-4");
				li12.appendChild(new Html("Biaya Pendaftaran Rp. " + NumberFormat.getInstance().format(obj.getEventprice())));
				li12.setParent(ul);
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
				
				Div cardfooter = new Div();
				cardfooter.setSclass("card-footer text-muted");
				
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
				
				Button btEdit = new Button("Edit");
				btEdit.setSclass("btn btn-success");
				btEdit.setAutodisable("self");
				btEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						doAdd(obj);
					}
				});
				
				Hlayout hlayout = new Hlayout();
				hlayout.appendChild(btDetail);
				hlayout.appendChild(new Separator("vertical"));
				hlayout.appendChild(btEdit);
				
				cardfooter.appendChild(hlayout);
				cardfooter.setParent(card);

				divCol.appendChild(card);
				divRow.appendChild(divCol);
				
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getEventname() {
		return eventname;
	}

	public void setEventname(String eventname) {
		this.eventname = eventname;
	}

	public Date getBegindate() {
		return begindate;
	}

	public void setBegindate(Date begindate) {
		this.begindate = begindate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Integer getTotalrecord() {
		return totalrecord;
	}

	public void setTotalrecord(Integer totalrecord) {
		this.totalrecord = totalrecord;
	}
}
