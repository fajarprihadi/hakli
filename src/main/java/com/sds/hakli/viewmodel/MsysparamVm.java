package com.sds.hakli.viewmodel;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.sds.hakli.dao.MsysparamDAO;
import com.sds.hakli.domain.Msysparam;
import com.sds.hakli.domain.Tanggota;
import com.sds.utils.db.StoreHibernateUtil;

public class MsysparamVm {
	
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota oUser;
	
	private MsysparamDAO oDao = new MsysparamDAO();

	@Wire
	private Grid grid;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		oUser = (Tanggota) zkSession.getAttribute("anggota");				
		try {		
			String group = "";
			List<Msysparam> params = oDao.listByFilter("0=0", "orderno");
			Row row = null;
			for (Msysparam obj: params) {
				if (!group.equals(obj.getParamgroup())) {
					row = new Row();
					Cell cellGroup = new Cell();
					cellGroup.setColspan(3);
					cellGroup.setAlign("center");
					Label lblGroup = new Label(obj.getParamgroup());
					lblGroup.setStyle("font-size: 14px; font-weight: bold");
					cellGroup.appendChild(lblGroup);
					row.appendChild(cellGroup);
					grid.getRows().appendChild(row);
				}
				group = obj.getParamgroup();
				
				row = new Row();
				row.appendChild(new Label(obj.getParamcode()));
				row.appendChild(new Label(obj.getParamdesc()));
				Textbox tbox = new Textbox(obj.getParamvalue());
				tbox.setCols(30);
				tbox.setMaxlength(100);
				if (obj.getIsmasked().equals("Y"))
					tbox.setType("password");
				row.appendChild(tbox);
				row.setAttribute("obj", obj);
				grid.getRows().appendChild(row);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Command
	public void doSave() {
		Session session = null;
		Transaction transaction = null;
		try {
			session = StoreHibernateUtil.openSession();
			transaction = session.beginTransaction();
			
			List<Row> components = grid.getRows().getChildren();
			for (Row comp : components) {
				if (comp.getChildren().size() > 1) {
					Textbox tbox = (Textbox) comp.getChildren().get(2);
					Msysparam obj = (Msysparam) comp.getAttribute("obj");
					obj.setParamvalue(tbox.getValue());
					obj.setUpdatedby(oUser.getNoanggota());
					obj.setLastupdated(new Date());
					oDao.save(session, obj);
				}				
			}			
			transaction.commit();
			Clients.showNotification("Pembaruan data parameter sistem berhasil", "info", null, "middle_center", 3000);
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
		} finally {
			session.close();
		}
	}
}
