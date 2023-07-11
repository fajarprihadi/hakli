package com.sds.hakli.viewmodel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.McabangDAO;
import com.sds.hakli.dao.MprovDAO;
import com.sds.hakli.domain.Anggota;
import com.sds.hakli.domain.Mcabang;
import com.sds.hakli.domain.Mprov;
import com.sds.hakli.domain.Tanggota;
import com.sds.utils.AppData;
import com.sds.utils.db.StoreHibernateUtil;

public class BranchVm {
	
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota oUser;
	
	private McabangDAO oDao = new McabangDAO();
	
	private Mcabang objForm;
	private List<Mcabang> objList;
	private String filter;
	private String kodecabang;
	private String cabang;
	private String prov;
	private Integer totalrecords;
	private boolean isInsert;
	
	private String ketuaid;
	private String ketuanama;
	private String sekretaris1id;
	private String sekretaris1nama;
	private String sekretaris2id;
	private String sekretaris2nama;
	private String bendahara1id;
	private String bendahara1nama;
	private String bendahara2id;
	private String bendahara2nama;
	
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
	@Wire
	private Textbox tbCode;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
		oUser = (Tanggota) zkSession.getAttribute("anggota");
		grid.setRowRenderer(new RowRenderer<Mcabang>() {

			@Override
			public void render(Row row, Mcabang data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index+1)));
				row.getChildren().add(new Label(data.getKodecabang()));
				row.getChildren().add(new Label(data.getCabang()));
				row.getChildren().add(new Label(data.getMprov() != null ? data.getMprov().getProvname() : ""));
				row.getChildren().add(new Label(data.getBankname()));
				row.getChildren().add(new Label(data.getAccno()));
				row.getChildren().add(new Label(data.getAccname()));
				
				A aKetua = new A();
				if (data.getKetuaid() != null && !data.getKetuaid().equals(""))
					aKetua.setLabel(data.getKetuaid() + " - " + data.getKetuanama());
				aKetua.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						doViewAnggota(data.getKetuaid());
					}
				});
				
				A aSekretaris1 = new A();
				if (data.getSekretaris1id() != null && !data.getSekretaris1id().equals(""))
					aSekretaris1.setLabel(data.getSekretaris1id() + " - " + data.getSekretaris1nama());
				aSekretaris1.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						doViewAnggota(data.getSekretaris1id());
					}
				});
				
				A aSekretaris2 = new A();
				if (data.getSekretaris2id() != null && !data.getSekretaris2id().equals(""))
					aSekretaris2.setLabel(data.getSekretaris2id() + " - " + data.getSekretaris2nama());
				aSekretaris2.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						doViewAnggota(data.getSekretaris2id());
					}
				});
				
				A aBendahara1 = new A();
				if (data.getBendahara1id() != null && !data.getBendahara1id().equals(""))
					aBendahara1.setLabel(data.getBendahara1nama() + " - " + data.getBendahara1nama());
				aBendahara1.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						doViewAnggota(data.getBendahara1id());
					}
				});
				
				A aBendahara2 = new A();
				if (data.getBendahara2id() != null && !data.getBendahara2id().equals(""))
					aBendahara2.setLabel(data.getBendahara2nama() + " - " + data.getBendahara2nama());
				aBendahara2.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						doViewAnggota(data.getBendahara2id());
					}
				});
				
				row.getChildren().add(aKetua);
				row.getChildren().add(aSekretaris1);
				row.getChildren().add(aSekretaris2);
				row.getChildren().add(aBendahara1);
				row.getChildren().add(aBendahara2);
				
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
				
				Button btOrg = new Button();
				btOrg.setIconSclass("z-icon-address-card");
				btOrg.setSclass("btn btn-success btn-sm");
				btOrg.setAutodisable("self");
				btOrg.setTooltiptext("Lihat Detail Susunan Organisasi");
				btOrg.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						Window win = (Window) Executions
								.createComponents("/view/branchorg.zul", null, map);
						win.setClosable(true);
						win.doModal();
					}
				});
				
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
				div.appendChild(btOrg);
				div.appendChild(new Separator("vertical"));
				div.appendChild(btDel);
				row.getChildren().add(div);
			}
		});
		
		doReset();
	}
	
	private void doViewAnggota(String noanggota) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noanggota", noanggota);
		map.put("acttype", "view");
		Window win = (Window) Executions
				.createComponents("/view/anggota/anggotaedit.zul", null, map);
		win.setClosable(true);
		win.setWidth("98%");
		win.doModal();
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		cabang = null;
		prov = null;
		kodecabang = null;
		ketuaid = null;
		ketuanama = null;
		sekretaris1id = null;
		sekretaris1nama = null;
		sekretaris2id = null;
		sekretaris2nama = null;
		bendahara1id = null;
		bendahara1nama = null;
		bendahara2id = null;
		bendahara2nama = null;
		doSearch();
		divForm.setVisible(false);
		btAdd.setLabel("Tambah Kabupaten/Kota");
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
		if (kodecabang != null && kodecabang.trim().length() > 0)
			filter += " and upper(kodecabang) like '%" + kodecabang.trim().toUpperCase() + "%'";
		if (cabang != null && cabang.trim().length() > 0)
			filter += " and upper(cabang) like '%" + cabang.trim().toUpperCase() + "%'";
		if (prov != null && prov.trim().length() > 0)
			filter += " and upper(mprov.provname) like '%" + prov.trim().toUpperCase() + "%'";
		doRefresh();
	}
	
	@Command
	@NotifyChange({"ketuaid", "ketuanama", "sekretaris1id", "sekretaris1nama", "sekretaris2id", "sekretaris2nama", 
		"bendahara1id", "bendahara1nama", "bendahara2id", "bendahara2nama"})
	public void doLookup(@BindingParam("type") String type) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("act", "lookup");
			Window win = (Window) Executions
					.createComponents("/view/anggota/anggota.zul", null, map);
			win.setClosable(true);
			win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getData() != null) {
						if (type.equals("ketua")) {
							ketuaid = ((Tanggota) event.getData()).getNoanggota();
							ketuanama = ((Tanggota) event.getData()).getNama();
							BindUtils.postNotifyChange(BranchVm.this, "ketuaid");
							BindUtils.postNotifyChange(BranchVm.this, "ketuanama");
						} else if (type.equals("sekretaris1")) {
							sekretaris1id = ((Tanggota) event.getData()).getNoanggota();
							sekretaris1nama = ((Tanggota) event.getData()).getNama();
							BindUtils.postNotifyChange(BranchVm.this, "sekretaris1id");
							BindUtils.postNotifyChange(BranchVm.this, "sekretaris1nama");
						} else if (type.equals("sekretaris2")) {
							sekretaris2id = ((Tanggota) event.getData()).getNoanggota();
							sekretaris2nama = ((Tanggota) event.getData()).getNama();
							BindUtils.postNotifyChange(BranchVm.this, "sekretaris2id");
							BindUtils.postNotifyChange(BranchVm.this, "sekretaris2nama");
						} else if (type.equals("bendahara1")) {
							bendahara1id = ((Tanggota) event.getData()).getNoanggota();
							bendahara1nama = ((Tanggota) event.getData()).getNama();
							BindUtils.postNotifyChange(BranchVm.this, "bendahara1id");
							BindUtils.postNotifyChange(BranchVm.this, "bendahara1nama");
						} else if (type.equals("bendahara2")) {
							bendahara2id = ((Tanggota) event.getData()).getNoanggota();
							bendahara2nama = ((Tanggota) event.getData()).getNama();
							BindUtils.postNotifyChange(BranchVm.this, "bendahara2id");
							BindUtils.postNotifyChange(BranchVm.this, "bendahara2nama");
						}
					}
					
				}
			});
			win.setWidth("98%");
			win.doModal();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doAdd(Mcabang obj) {
		if (obj != null) {
			isInsert = false;
			objForm = obj;
			if (objForm.getKetuaid() != null) {
				ketuaid = objForm.getKetuaid();
				ketuanama = objForm.getKetuanama();
			}
			if (objForm.getSekretaris1id() != null) {
				sekretaris1id = objForm.getSekretaris1id();
				sekretaris1nama = objForm.getSekretaris1nama();
			}
			if (objForm.getSekretaris2id() != null) {
				sekretaris2id = objForm.getSekretaris2id();
				sekretaris2nama = objForm.getSekretaris2nama();
			}
			if (objForm.getBendahara1id() != null) {
				bendahara1id = objForm.getBendahara1id();
				bendahara1nama = objForm.getBendahara1nama();
			}
			if (objForm.getBendahara2id() != null) {
				bendahara2id = objForm.getBendahara2id();
				bendahara2nama = objForm.getBendahara2nama();
			}
			divForm.setVisible(true);
			btAdd.setLabel("Cancel");
			btAdd.setIconSclass("z-icon-reply");
			btSave.setLabel("Perbarui");
			
			cbRegion.setValue(objForm.getMprov().getProvname());
			cbBank.setValue(objForm.getBankname());
			tbCode.setDisabled(true);
		} else if (btAdd.getLabel().equals("Tambah Kabupaten/Kota")) {
			isInsert = true;
			objForm = new Mcabang();
			divForm.setVisible(true);
			btAdd.setLabel("Cancel");
			btAdd.setIconSclass("z-icon-reply");
			btSave.setLabel("Submit");
			
			cbRegion.setValue(null);
			tbCode.setDisabled(false);
		} else {
			divForm.setVisible(false);
			btAdd.setLabel("Tambah Kabupaten/Kota");
			btAdd.setIconSclass("z-icon-plus-square");
			tbCode.setDisabled(false);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doSave() {
		Session session = StoreHibernateUtil.openSession();
		Transaction trx = session.beginTransaction();
		try {
			if (isInsert) {
				objForm.setCreatetime(new Date());
				//objForm.setCreatedby(oUser.getUserid());
			} else {
				objForm.setLastupdated(new Date());
				//objForm.setUpdatedby(oUser.getUserid());
			}
			
			if (ketuaid != null && !ketuaid.equals("")) {
				objForm.setKetuaid(ketuaid);
				objForm.setKetuanama(ketuanama);
			}
			if (sekretaris1id != null && !sekretaris1id.equals("")) {
				objForm.setSekretaris1id(sekretaris1id);
				objForm.setSekretaris1nama(sekretaris1nama);
			}
			if (sekretaris2id != null && !sekretaris2id.equals("")) {
				objForm.setSekretaris2id(sekretaris2id);
				objForm.setSekretaris2nama(sekretaris2nama);
			}
			if (bendahara1id != null && !bendahara1id.equals("")) {
				objForm.setBendahara1id(bendahara1id);
				objForm.setBendahara1nama(bendahara1nama);
			}
			if (bendahara2id != null && !bendahara2id.equals("")) {
				objForm.setBendahara2id(bendahara2id);
				objForm.setBendahara2nama(bendahara2nama);
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
				
				Mprov mprov = (Mprov) ctx.getProperties("mprov")[0].getValue();
				if (mprov == null)
					this.addInvalidMessage(ctx, "mprov", Labels.getLabel("common.validator.empty"));
				
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
	
	public ListModelList<Mprov> getProvModel() {
		ListModelList<Mprov> oList = null;
		try {
			oList = new ListModelList<>(new MprovDAO().listAll());
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
		return prov;
	}

	public void setProvinsi(String prov) {
		this.prov = prov;
	}

	public String getKodecabang() {
		return kodecabang;
	}

	public void setKodecabang(String kodecabang) {
		this.kodecabang = kodecabang;
	}

	public String getKetuaid() {
		return ketuaid;
	}

	public void setKetuaid(String ketuaid) {
		this.ketuaid = ketuaid;
	}

	public String getKetuanama() {
		return ketuanama;
	}

	public void setKetuanama(String ketuanama) {
		this.ketuanama = ketuanama;
	}

	public String getSekretaris1id() {
		return sekretaris1id;
	}

	public void setSekretaris1id(String sekretaris1id) {
		this.sekretaris1id = sekretaris1id;
	}

	public String getSekretaris1nama() {
		return sekretaris1nama;
	}

	public void setSekretaris1nama(String sekretaris1nama) {
		this.sekretaris1nama = sekretaris1nama;
	}

	public String getSekretaris2id() {
		return sekretaris2id;
	}

	public void setSekretaris2id(String sekretaris2id) {
		this.sekretaris2id = sekretaris2id;
	}

	public String getSekretaris2nama() {
		return sekretaris2nama;
	}

	public void setSekretaris2nama(String sekretaris2nama) {
		this.sekretaris2nama = sekretaris2nama;
	}

	public String getBendahara1id() {
		return bendahara1id;
	}

	public void setBendahara1id(String bendahara1id) {
		this.bendahara1id = bendahara1id;
	}

	public String getBendahara1nama() {
		return bendahara1nama;
	}

	public void setBendahara1nama(String bendahara1nama) {
		this.bendahara1nama = bendahara1nama;
	}

	public String getBendahara2id() {
		return bendahara2id;
	}

	public void setBendahara2id(String bendahara2id) {
		this.bendahara2id = bendahara2id;
	}

	public String getBendahara2nama() {
		return bendahara2nama;
	}

	public void setBendahara2nama(String bendahara2nama) {
		this.bendahara2nama = bendahara2nama;
	}

}
