package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlNativeComponent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;
import org.zkoss.zul.Separator;

import com.sds.hakli.domain.Teventreg;
import com.sds.utils.AppUtils;

public class EventRegdoneVm {
	
	private String processinfo;
	private String eventimg;
	
	@Wire
	private Div divInfo;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Teventreg obj) {
		Selectors.wireComponents(view, this, false);
		try {
			if (obj == null) {
				Executions.sendRedirect("/view/event/eventinit.zul");
			} else {
				eventimg = AppUtils.PATH_EVENT + "/" + obj.getTevent().getEventimg();
				if (obj.getIspaid().equals("Y")) {
					HtmlNativeComponent strong = new HtmlNativeComponent("strong");
					strong.appendChild(new Html("Proses pendaftaran dan pembayaran berhasil."));
					divInfo.appendChild(strong);
					divInfo.appendChild(new Separator());
				} else {
					HtmlNativeComponent strong = new HtmlNativeComponent("strong");
					strong.appendChild(new Html("Anda telah mengisi data pendaftaran Anda. Selanjutnya silakan Anda menyelesaikan pembayaran tagihan melalui Virtual Account Bank BRI atas data berikut :"));
					divInfo.appendChild(strong);
					divInfo.appendChild(new Separator());
				}
				
				HtmlNativeComponent table = new HtmlNativeComponent("table");
				table.setClientAttribute("class", "table table-sm table-striped mb-0");
				HtmlNativeComponent tr = new HtmlNativeComponent("tr");
				HtmlNativeComponent td1 = new HtmlNativeComponent("td");
				td1.setClientAttribute("width", "50%");
				td1.setClientAttribute("align", "right");
				td1.appendChild(new Html("Nama :"));
				HtmlNativeComponent td2 = new HtmlNativeComponent("td");
				td2.appendChild(new Html(obj.getTanggota().getNama()));
				td2.setClientAttribute("align", "left");
				tr.appendChild(td1);
				tr.appendChild(td2);
				table.appendChild(tr);
				
				tr = new HtmlNativeComponent("tr");
				td1 = new HtmlNativeComponent("td");
				td1.appendChild(new Html("E-Mail :"));
				td1.setClientAttribute("align", "right");
				td2 = new HtmlNativeComponent("td");
				td2.setClientAttribute("align", "left");
				td2.appendChild(new Html(obj.getTanggota().getEmail()));
				tr.appendChild(td1);
				tr.appendChild(td2);
				table.appendChild(tr);
				
				tr = new HtmlNativeComponent("tr");
				td1 = new HtmlNativeComponent("td");
				td1.appendChild(new Html("Nomor Virtual Account :"));
				td1.setClientAttribute("align", "right");
				td2 = new HtmlNativeComponent("td");
				td2.setClientAttribute("align", "left");
				td2.appendChild(new Html(obj.getVano()));
				tr.appendChild(td1);
				tr.appendChild(td2);
				table.appendChild(tr);
				
				tr = new HtmlNativeComponent("tr");
				td1 = new HtmlNativeComponent("td");
				td1.appendChild(new Html("Nominal Tagihan :"));
				td1.setClientAttribute("align", "right");
				td2 = new HtmlNativeComponent("td");
				td2.setClientAttribute("align", "left");
				td2.appendChild(new Html("Rp. " + NumberFormat.getInstance().format(obj.getVaamount())));
				tr.appendChild(td1);
				tr.appendChild(td2);
				table.appendChild(tr);
				
				tr = new HtmlNativeComponent("tr");
				td1 = new HtmlNativeComponent("td");
				td1.appendChild(new Html("Tanggal Jatuh Tempo :"));
				td1.setClientAttribute("align", "right");
				td2 = new HtmlNativeComponent("td");
				td2.setClientAttribute("align", "left");
				td2.appendChild(new Html(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(obj.getVaexpdate())));
				tr.appendChild(td1);
				tr.appendChild(td2);
				table.appendChild(tr);
				
				tr = new HtmlNativeComponent("tr");
				td1 = new HtmlNativeComponent("td");
				td1.appendChild(new Html("Status Pembayaran :"));
				td1.setClientAttribute("align", "right");
				td2 = new HtmlNativeComponent("td");
				td2.setClientAttribute("align", "left");
				td2.appendChild(new Html(obj.getIspaid().equals("Y") ? "Sudah dibayar Tanggal " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(obj.getPaidat()) : "Belum dibayar"));
				tr.appendChild(td1);
				tr.appendChild(td2);
				table.appendChild(tr);
				
				divInfo.appendChild(table);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getProcessinfo() {
		return processinfo;
	}

	public void setProcessinfo(String processinfo) {
		this.processinfo = processinfo;
	}

	public String getEventimg() {
		return eventimg;
	}

	public void setEventimg(String eventimg) {
		this.eventimg = eventimg;
	}
	
	

}
