package com.sds.hakli.viewmodel;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.MuniversitasDAO;
import com.sds.hakli.domain.Muniversitas;
import com.sds.hakli.domain.Tanggota;
import com.sds.utils.db.StoreHibernateUtil;

public class UniversityVm {
	
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota oUser;
	
	private MuniversitasDAO oDao = new MuniversitasDAO();
	
	private Muniversitas objForm;
	private List<Muniversitas> objList;
	private String universitas;
	private String filter;
	private Integer totalrecords;
	private boolean isInsert;
	
	@Wire
	private Window winUniversity;
	@Wire
	private Grid grid;
	@Wire
	private Button btAdd;
	@Wire
	private Button btSave;
	@Wire
	private Div divForm;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
		oUser = (Tanggota) zkSession.getAttribute("anggota");
		grid.setRowRenderer(new RowRenderer<Muniversitas>() {

			@Override
			public void render(Row row, Muniversitas data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index+1)));
				row.getChildren().add(new Label(data.getUniversitas()));
				Button btEdit = new Button();
				btEdit.setIconSclass("z-icon-edit");
				btEdit.setSclass("btn btn-primary btn-sm");
				btEdit.setAutodisable("self");
				btEdit.setTooltiptext("Edit");
				btEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						doAdd(data);
						BindUtils.postNotifyChange(UniversityVm.this, "objForm");
					}
				});
				row.getChildren().add(btEdit);
				
				Button btDel = new Button();
				btDel.setIconSclass("z-icon-trash-o");
				btDel.setSclass("btn btn-danger btn-sm");
				btDel.setAutodisable("self");
				btDel.setTooltiptext("Hapus");
				
				btDel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Messagebox.show("Anda ingin menghapus data ini?", "Confirm Dialog", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

							@Override
							public void onEvent(Event event)
									throws Exception {
								if (event.getName().equals("onOK")) {
									Session session = StoreHibernateUtil.openSession();
									Transaction trx = session.beginTransaction();
									try {
										oDao.delete(session, data);
										trx.commit();
										Clients.showNotification("Proses hapus data berhasil", "info", null, "middle_center", 1500);
										doReset();
										BindUtils.postNotifyChange(UniversityVm.this, "*");
									} catch (Exception e) {	
										Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
										e.printStackTrace();
									} finally {
										session.close();
									}																													
								} 									
							}
							
						});
					}
				});
				
				Div div = new Div();
				div.appendChild(btEdit);
				div.appendChild(new Separator("vertical"));
				div.appendChild(btDel);
				row.getChildren().add(div);
			}
		});
		
		doReset();
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		universitas = null;
		doSearch();
		divForm.setVisible(false);
		btAdd.setLabel("Tambah Perguruan Tinggi");
		btAdd.setIconSclass("z-icon-plus-square");
	}
	
	@Command
	@NotifyChange("totalrecords")
	public void doSearch() {
		filter = "0=0";
		if (universitas != null && universitas.trim().length() > 0)
			filter += " and upper(universitas) like '%" + universitas.trim().toUpperCase() + "%'";
		doRefresh();
	}
	
	@Command
	@NotifyChange("totalrecords")
	public void doRefresh() {
		try {
			objList = oDao.listByFilter(filter, "universitas");
			totalrecords = objList.size();
			grid.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doAdd(Muniversitas obj) {
		if (obj != null) {
			isInsert = false;
			objForm = obj;
			divForm.setVisible(true);
			btAdd.setLabel("Cancel");
			btAdd.setIconSclass("z-icon-reply");
			btSave.setLabel("Perbarui");
			
		} else if (btAdd.getLabel().equals("Tambah Perguruan Tinggi")) {
			isInsert = true;
			objForm = new Muniversitas();
			divForm.setVisible(true);
			btAdd.setLabel("Cancel");
			btAdd.setIconSclass("z-icon-reply");
			btSave.setLabel("Submit");
			
		} else {
			divForm.setVisible(false);
			btAdd.setLabel("Tambah Perguruan Tinggi");
			btAdd.setIconSclass("z-icon-plus-square");
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doSave() {
		Session session = StoreHibernateUtil.openSession();
		Transaction trx = session.beginTransaction();
		try {
			if (isInsert) {
				objForm.setCreatedby(oUser.getNoanggota());
				objForm.setCreatetime(new Date());
			} else {
				objForm.setUpdatedby(oUser.getNoanggota());
				objForm.setLastupdated(new Date());
			}
			
			oDao.save(session, objForm);
			trx.commit();
			if (isInsert) {
				Clients.showNotification("Proses simpan data berhasil", "info", null, "middle_center", 1500);
			} else { 
				Clients.showNotification("Proses pembaruan data berhasil", "info", null, "middle_center", 1500);
			}
			doReset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String universitas = (String) ctx.getProperties("universitas")[0].getValue();
				
				if (universitas == null || "".equals(universitas.trim()))
					this.addInvalidMessage(ctx, "universitas", Labels.getLabel("common.validator.empty"));
				
			}
		};
	}

	public Integer getTotalrecords() {
		return totalrecords;
	}

	public void setTotalrecords(Integer totalrecords) {
		this.totalrecords = totalrecords;
	}

	public Muniversitas getObjForm() {
		return objForm;
	}

	public void setObjForm(Muniversitas objForm) {
		this.objForm = objForm;
	}

	public String getUniversitas() {
		return universitas;
	}

	public void setUniversitas(String universitas) {
		this.universitas = universitas;
	}
	
}
