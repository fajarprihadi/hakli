package com.sds.hakli.viewmodel;

import java.text.SimpleDateFormat;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.McabangDAO;
import com.sds.hakli.dao.MprovinsiDAO;
import com.sds.hakli.domain.Mcabang;
import com.sds.hakli.domain.Mprovinsi;
import com.sds.hakli.domain.Muser;
import com.sds.utils.AppData;
import com.sds.utils.db.StoreHibernateUtil;

public class BranchVm {
	
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Muser oUser;
	
	private McabangDAO oDao = new McabangDAO();
	
	private Mcabang objForm;
	private List<Mcabang> objList;
	private String filter;
	private String cabang;
	private String provinsi;
	private Integer totalrecords;
	private boolean isInsert;
	
	@Wire
	private Window winBranch;
	@Wire
	private Grid grid;
	@Wire
	private Button btAdd;
	@Wire
	private Button btSave;
	@Wire
	private Div divForm;
	@Wire
	private Combobox cbRegion;
	@Wire
	private Combobox cbBank;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
		oUser = (Muser) zkSession.getAttribute("oUser");
		grid.setRowRenderer(new RowRenderer<Mcabang>() {

			@Override
			public void render(Row row, Mcabang data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index+1)));
				row.getChildren().add(new Label(data.getCabang()));
				row.getChildren().add(new Label(data.getMprovinsi() != null ? data.getMprovinsi().getProvname() : ""));
				row.getChildren().add(new Label(data.getBankname()));
				row.getChildren().add(new Label(data.getAccno()));
				row.getChildren().add(new Label(data.getAccname()));
				Button btEdit = new Button();
				btEdit.setIconSclass("z-icon-edit");
				btEdit.setSclass("btn btn-primary btn-sm");
				btEdit.setAutodisable("self");
				btEdit.setTooltiptext("Edit");
				btEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						doAdd(data);
						BindUtils.postNotifyChange(BranchVm.this, "objForm");
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
										BindUtils.postNotifyChange(BranchVm.this, "*");
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
		cabang = null;
		provinsi = null;
		doSearch();
		divForm.setVisible(false);
		btAdd.setLabel("Tambah Cabang");
		btAdd.setIconSclass("z-icon-plus-square");
		cbRegion.setValue(null);
	}
	
	@Command
	@NotifyChange("totalrecords")
	public void doRefresh() {
		try {
			objList = oDao.listByFilter(filter, "provcode, cabang");
			totalrecords = objList.size();
			grid.setModel(new ListModelList<>(objList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("totalrecords")
	public void doSearch() {
		filter = "0=0";
		if (cabang != null && cabang.trim().length() > 0)
			filter += " and upper(cabang) like '%" + cabang.trim().toUpperCase() + "%'";
		if (provinsi != null && provinsi.trim().length() > 0)
			filter += " and upper(mprovinsi.provname) like '%" + provinsi.trim().toUpperCase() + "%'";
		doRefresh();
	}
	
	@Command
	@NotifyChange("*")
	public void doAdd(Mcabang obj) {
		if (obj != null) {
			isInsert = false;
			objForm = obj;
			divForm.setVisible(true);
			btAdd.setLabel("Cancel");
			btAdd.setIconSclass("z-icon-reply");
			btSave.setLabel("Perbarui");
			
			cbRegion.setValue(objForm.getMprovinsi().getProvname());
			cbBank.setValue(objForm.getBankname());
		} else if (btAdd.getLabel().equals("Tambah Cabang")) {
			isInsert = true;
			objForm = new Mcabang();
			divForm.setVisible(true);
			btAdd.setLabel("Cancel");
			btAdd.setIconSclass("z-icon-reply");
			btSave.setLabel("Submit");
			
			cbRegion.setValue(null);
		} else {
			divForm.setVisible(false);
			btAdd.setLabel("Tambah Cabang");
			btAdd.setIconSclass("z-icon-plus-square");
		}
	}
	
	@Command
	@NotifyChange("objForm")
	public void doSave() {
		Session session = StoreHibernateUtil.openSession();
		Transaction trx = session.beginTransaction();
		try {
			if (isInsert) {
				objForm.setCreatetime(new Date());
				objForm.setCreatedby(oUser.getUserid());
			} else {
				objForm.setLastupdated(new Date());
				objForm.setUpdatedby(oUser.getUserid());
			}
			objForm.setBankname(AppData.getBankName(objForm.getBankcode()));
			oDao.save(session, objForm);
			trx.commit();
			if (isInsert) {
				Clients.showNotification("Proses simpan data berhasil", "info", null, "middle_center", 1500);
			} else { 
				Clients.showNotification("Proses pembaruan data berhasil", "info", null, "middle_center", 1500);
			}doReset();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String cabang = (String) ctx.getProperties("cabang")[0].getValue();
				if (cabang == null || "".equals(cabang.trim()))
					this.addInvalidMessage(ctx, "cabang", Labels.getLabel("common.validator.empty"));
				
				Mprovinsi mprovinsi = (Mprovinsi) ctx.getProperties("mprovinsi")[0].getValue();
				if (mprovinsi == null)
					this.addInvalidMessage(ctx, "mprovinsi", Labels.getLabel("common.validator.empty"));
				
				String bankcode = (String) ctx.getProperties("bankcode")[0].getValue();
				if (bankcode == null || "".equals(bankcode.trim()))
					this.addInvalidMessage(ctx, "bankcode", Labels.getLabel("common.validator.empty"));
				
				String accno = (String) ctx.getProperties("accno")[0].getValue();
				if (accno == null || "".equals(accno.trim()))
					this.addInvalidMessage(ctx, "accno", Labels.getLabel("common.validator.empty"));
				
				String accname = (String) ctx.getProperties("accname")[0].getValue();
				if (accname == null || "".equals(accname.trim()))
					this.addInvalidMessage(ctx, "accname", Labels.getLabel("common.validator.empty"));
			
			}
		};
	}
	
	public ListModelList<Mprovinsi> getProvModel() {
		ListModelList<Mprovinsi> oList = null;
		try {
			oList = new ListModelList<>(new MprovinsiDAO().listAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	public Mcabang getObjForm() {
		return objForm;
	}

	public void setObjForm(Mcabang objForm) {
		this.objForm = objForm;
	}

	public Integer getTotalrecords() {
		return totalrecords;
	}

	public void setTotalrecords(Integer totalrecords) {
		this.totalrecords = totalrecords;
	}

	public String getCabang() {
		return cabang;
	}

	public void setCabang(String cabang) {
		this.cabang = cabang;
	}

	public String getProvinsi() {
		return provinsi;
	}

	public void setProvinsi(String provinsi) {
		this.provinsi = provinsi;
	}

}
