package com.sds.hakli.viewmodel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

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
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Separator;

import com.sds.hakli.dao.MchargeDAO;
import com.sds.hakli.dao.MfeeDAO;
import com.sds.hakli.domain.Mcharge;
import com.sds.hakli.domain.Mfee;
import com.sds.hakli.domain.Tanggota;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class ChargeVm {
	
	private org.zkoss.zk.ui.Session session = Sessions.getCurrent();
	private Tanggota oUser;
	private MchargeDAO chargeDao = new MchargeDAO();
	private MfeeDAO feeDao = new MfeeDAO();
	
	private Mcharge objReg;
	private Mcharge objIuran;
	private Mcharge objP2kb;
	
	private BigDecimal totalreg;
	private BigDecimal totaliuran;
	private BigDecimal totalp2kb;
	
	private Mfee objFee;
	private BigDecimal totalfeepercent;
	private BigDecimal amount;
	private BigDecimal feepusat;
	private BigDecimal feeprov;
	private BigDecimal feekab;
	
	@Wire
	private Groupbox gbReg;
	@Wire
	private Groupbox gbIuran;
	@Wire
	private Groupbox gbP2kb;
	@Wire
	private Grid gridReg;
	@Wire
	private Grid gridIuran;
	@Wire
	private Grid gridP2kb;
	@Wire
	private Button btAddReg;
	@Wire
	private Button btAddIuran;
	@Wire
	private Button btAddP2kb;
	@Wire
	private Button btSaveReg;
	@Wire
	private Button btSaveIuran;
	@Wire
	private Button btSaveP2kb;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			oUser = (Tanggota) session.getAttribute("anggota");
			
			gridReg.setRowRenderer(new RowRenderer<Mcharge>() {

				@Override
				public void render(Row row, Mcharge data, int index) throws Exception {
					row.getChildren().add(new Label(String.valueOf(index + 1)));
					row.getChildren().add(new Label(data.getIsbase().equals("Y") ? data.getChargedesc() + " *" : data.getChargedesc()));
					row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getChargeamount())));
					Button btEdit = new Button();
					btEdit.setIconSclass("z-icon-edit");
					btEdit.setSclass("btn btn-primary btn-sm");
					btEdit.setAutodisable("self");
					btEdit.setTooltiptext("Edit");
					btEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							doAddReg(data);
							BindUtils.postNotifyChange(ChargeVm.this, "objReg");
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
							if (data.getIsbase().equals("Y")) {
								Messagebox.show("Komponen pokok tidak dapat dihapus", WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.INFORMATION);
							} else {
								Messagebox.show("Anda ingin menghapus data ini?", "Confirm Dialog", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

									@Override
									public void onEvent(Event event)
											throws Exception {
										if (event.getName().equals("onOK")) {
											Session session = StoreHibernateUtil.openSession();
											Transaction trx = null;
											try {
												trx = session.beginTransaction();
												chargeDao.delete(session, data);
												trx.commit();
												Clients.showNotification("Proses hapus data komponen biaya pendaftaran berhasil", "info", null, "middle_center", 1500);
												doLoadReg();
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
							
						}
					});
					
					Div div = new Div();
					div.appendChild(btEdit);
					div.appendChild(new Separator("vertical"));
					div.appendChild(btDel);

					row.getChildren().add(div);
					totalreg = totalreg.add(data.getChargeamount());
					BindUtils.postNotifyChange(ChargeVm.this, "totalreg");
				}
			});
			
			gridIuran.setRowRenderer(new RowRenderer<Mcharge>() {

				@Override
				public void render(Row row, Mcharge data, int index) throws Exception {
					row.getChildren().add(new Label(String.valueOf(index + 1)));
					row.getChildren().add(new Label(data.getIsbase().equals("Y") ? data.getChargedesc() + " *" : data.getChargedesc()));
					row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getChargeamount())));
					Button btEdit = new Button();
					btEdit.setIconSclass("z-icon-edit");
					btEdit.setSclass("btn btn-primary btn-sm");
					btEdit.setAutodisable("self");
					btEdit.setTooltiptext("Edit");
					btEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							doAddIuran(data);
							BindUtils.postNotifyChange(ChargeVm.this, "objIuran");
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
										try {
											Session session = StoreHibernateUtil.openSession();
											Transaction trx = null;
											try {
												trx = session.beginTransaction();
												chargeDao.delete(session, data);
												trx.commit();
												Clients.showNotification("Proses hapus data komponen biaya iuran berhasil", "info", null, "middle_center", 1500);
												doLoadIuran();
											} catch (Exception e) {	
												Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
												e.printStackTrace();
											} finally {
												session.close();
											}
										} catch (Exception e) {	
											Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
											e.printStackTrace();
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
					totaliuran = totaliuran.add(data.getChargeamount());
					BindUtils.postNotifyChange(ChargeVm.this, "totaliuran");
				}
			});
			
			gridP2kb.setRowRenderer(new RowRenderer<Mcharge>() {

				@Override
				public void render(Row row, Mcharge data, int index) throws Exception {
					row.getChildren().add(new Label(String.valueOf(index + 1)));
					row.getChildren().add(new Label(data.getIsbase().equals("Y") ? data.getChargedesc() + " *" : data.getChargedesc()));
					row.getChildren().add(new Label(NumberFormat.getInstance().format(data.getChargeamount())));
					Button btEdit = new Button();
					btEdit.setIconSclass("z-icon-edit");
					btEdit.setSclass("btn btn-primary btn-sm");
					btEdit.setAutodisable("self");
					btEdit.setTooltiptext("Edit");
					btEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							doAddP2kb(data);
							BindUtils.postNotifyChange(ChargeVm.this, "objP2kb");
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
										try {
											Session session = StoreHibernateUtil.openSession();
											Transaction trx = null;
											try {
												trx = session.beginTransaction();
												chargeDao.delete(session, data);
												trx.commit();
												Clients.showNotification("Proses hapus data komponen biaya rekomendasi STR berhasil", "info", null, "middle_center", 1500);
												doLoadP2kb();
											} catch (Exception e) {	
												Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
												e.printStackTrace();
											} finally {
												session.close();
											}
										} catch (Exception e) {	
											Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
											e.printStackTrace();
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
					totalp2kb = totalp2kb.add(data.getChargeamount());
					BindUtils.postNotifyChange(ChargeVm.this, "totalp2kb");
				}
			});
			
			doLoadReg();
			doLoadIuran();
			doLoadP2kb();
			
			List<Mfee> fees = feeDao.listAll();
			if (fees.size() == 0)
				objFee = new Mfee();
			else objFee = fees.get(0);
			totalfeepercent = new BigDecimal(0);
			doTotalpercent(objFee);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doLoadReg() {
		try {
			totalreg = new BigDecimal(0);
			gridReg.setModel(new ListModelList<>(chargeDao.listByFilter("chargetype = '" + AppUtils.CHARGETYPE_REG + "'", "mchargepk")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doLoadIuran() {
		try {
			totaliuran = new BigDecimal(0);
			gridIuran.setModel(new ListModelList<>(chargeDao.listByFilter("chargetype = '" + AppUtils.CHARGETYPE_IURAN + "'", "mchargepk")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doLoadP2kb() {
		try {
			totalp2kb = new BigDecimal(0);
			gridP2kb.setModel(new ListModelList<>(chargeDao.listByFilter("chargetype = '" + AppUtils.CHARGETYPE_P2KB + "'", "mchargepk")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("objReg")
	public void doAddReg(Mcharge obj) {
		try {
			if (obj != null) {
				objReg = obj;
				gbReg.setVisible(true);
				btAddReg.setLabel("Cancel");
				btAddReg.setIconSclass("z-icon-reply");
				btSaveReg.setLabel("Perbarui");
			} else if (btAddReg.getLabel().equals("Tambah Komponen Biaya Pendaftaran")) {
				objReg = new Mcharge();
				gbReg.setVisible(true);
				btAddReg.setLabel("Cancel");
				btAddReg.setIconSclass("z-icon-reply");
				btSaveReg.setLabel("Submit");
			} else {
				objReg = null;
				gbReg.setVisible(false);
				btAddReg.setLabel("Tambah Komponen Biaya Pendaftaran");
				btAddReg.setIconSclass("z-icon-plus-square");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("objIuran")
	public void doAddIuran(Mcharge obj) {
		try {
			if (obj != null) {
				objIuran = obj;
				gbIuran.setVisible(true);
				btAddIuran.setLabel("Cancel");
				btAddIuran.setIconSclass("z-icon-reply");
				btSaveIuran.setLabel("Perbarui");
			} else if (btAddIuran.getLabel().equals("Tambah Komponen Biaya Iuran")) {
				objIuran = new Mcharge();
				gbIuran.setVisible(true);
				btAddIuran.setLabel("Cancel");
				btAddIuran.setIconSclass("z-icon-reply");
				btSaveIuran.setLabel("Submit");
			} else {
				objIuran = null;
				gbIuran.setVisible(false);
				btAddIuran.setLabel("Tambah Komponen Biaya Iuran");
				btAddIuran.setIconSclass("z-icon-plus-square");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("objP2kb")
	public void doAddP2kb(Mcharge obj) {
		try {
			if (obj != null) {
				objP2kb= obj;
				gbP2kb.setVisible(true);
				btAddP2kb.setLabel("Cancel");
				btAddP2kb.setIconSclass("z-icon-reply");
				btSaveP2kb.setLabel("Perbarui");
			} else if (btAddP2kb.getLabel().equals("Tambah Komponen Biaya Rekomendasi STR")) {
				objP2kb = new Mcharge();
				gbP2kb.setVisible(true);
				btAddP2kb.setLabel("Cancel");
				btAddP2kb.setIconSclass("z-icon-reply");
				btSaveP2kb.setLabel("Submit");
			} else {
				objP2kb = null;
				gbP2kb.setVisible(false);
				btAddP2kb.setLabel("Tambah Komponen Biaya Rekomendasi STR");
				btAddP2kb.setIconSclass("z-icon-plus-square");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("objReg")
	public void doSaveReg() {
		Session session = StoreHibernateUtil.openSession();
		Transaction trx = null;
		try {
			boolean isInsert = false;
			if (objReg.getMchargepk() == null) {
				isInsert = true;
				objReg.setCreatetime(new Date());
				objReg.setCreatedby(oUser.getNoanggota());
				objReg.setIsbase("N");
			} else {
				objReg.setLastupdated(new Date());
				objReg.setUpdatedby(oUser.getNoanggota());
			}
			trx = session.beginTransaction();
			
			objReg.setChargetype(AppUtils.CHARGETYPE_REG);
			
			chargeDao.save(session, objReg);
			trx.commit();
			
			if (isInsert) {
				Clients.showNotification("Proses tambah data komponen biaya pendaftaran berhasil", "info", null, "middle_center", 1500);
			} else {
				Clients.showNotification("Proses pembaruan data komponen biaya pendaftaran berhasil", "info", null, "middle_center", 1500);
			}
			
			objReg = null;
			gbReg.setVisible(false);
			btAddReg.setLabel("Tambah Komponen Biaya Pendaftaran");
			btAddReg.setIconSclass("z-icon-plus-square");
			
			doLoadReg();
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(),
					Messagebox.OK, Messagebox.ERROR);
		} finally {
			session.close();
		}
	}
	
	@Command
	@NotifyChange("objIuran")
	public void doSaveIuran() {
		Session session = StoreHibernateUtil.openSession();
		Transaction trx = null;
		try {
			boolean isInsert = false;
			if (objIuran.getMchargepk() == null) {
				isInsert = true;
				objIuran.setCreatetime(new Date());
				objIuran.setCreatedby(oUser.getNoanggota());
				objIuran.setIsbase("N");
			} else {
				objIuran.setLastupdated(new Date());
				objIuran.setUpdatedby(oUser.getNoanggota());
			}
			
			trx = session.beginTransaction();
			
			objIuran.setChargetype(AppUtils.CHARGETYPE_IURAN);;
			chargeDao.save(session, objIuran);
			trx.commit();
			
			if (isInsert) {
				Clients.showNotification("Proses tambah data komponen biaya pendaftaran berhasil", "info", null, "middle_center", 1500);
			} else {
				Clients.showNotification("Proses pembaruan data komponen biaya pendaftaran berhasil", "info", null, "middle_center", 1500);
			}
			
			objIuran = null;
			gbIuran.setVisible(false);
			btAddIuran.setLabel("Tambah Komponen Biaya Iuran");
			btAddIuran.setIconSclass("z-icon-plus-square");
			
			doLoadIuran();
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(),
					Messagebox.OK, Messagebox.ERROR);
		} finally {
			session.close();
		}
	}
	
	@Command
	@NotifyChange("objP2kb")
	public void doSaveP2kb() {
		Session session = StoreHibernateUtil.openSession();
		Transaction trx = null;
		try {
			boolean isInsert = false;
			if (objP2kb.getMchargepk() == null) {
				isInsert = true;
				objP2kb.setCreatetime(new Date());
				objP2kb.setCreatedby(oUser.getNoanggota());
				objP2kb.setIsbase("N");
			} else {
				objP2kb.setLastupdated(new Date());
				objP2kb.setUpdatedby(oUser.getNoanggota());
			}
			
			trx = session.beginTransaction();
			
			objP2kb.setChargetype(AppUtils.CHARGETYPE_P2KB);
			chargeDao.save(session, objP2kb);
			trx.commit();
			
			if (isInsert) {
				Clients.showNotification("Proses tambah data komponen biaya rekomendasi STR berhasil", "info", null, "middle_center", 1500);
			} else {
				Clients.showNotification("Proses pembaruan data komponen biaya rekomendasi STR berhasil", "info", null, "middle_center", 1500);
			}
			
			objP2kb = null;
			gbP2kb.setVisible(false);
			btAddP2kb.setLabel("Tambah Komponen Biaya Rekomendasi STR");
			btAddP2kb.setIconSclass("z-icon-plus-square");
			
			doLoadIuran();
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(),
					Messagebox.OK, Messagebox.ERROR);
		} finally {
			session.close();
		}
	}
	
	public Validator getValidatorReg() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String chargedesc = (String) ctx.getProperties("chargedesc")[0].getValue();
				if (chargedesc == null || "".equals(chargedesc.trim()))
					this.addInvalidMessage(ctx, "chargedescreg", Labels.getLabel("common.validator.empty"));
				BigDecimal chargeamount = (BigDecimal) ctx.getProperties("chargeamount")[0].getValue();
				if (chargeamount == null)
					this.addInvalidMessage(ctx, "chargeamountreg", Labels.getLabel("common.validator.empty"));
			}
		};
	}
	
	public Validator getValidatorIuran() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String chargedesc = (String) ctx.getProperties("chargedesc")[0].getValue();
				if (chargedesc == null || "".equals(chargedesc.trim()))
					this.addInvalidMessage(ctx, "chargedesciuran", Labels.getLabel("common.validator.empty"));
				BigDecimal chargeamount = (BigDecimal) ctx.getProperties("chargeamount")[0].getValue();
				if (chargeamount == null)
					this.addInvalidMessage(ctx, "chargeamountiuran", Labels.getLabel("common.validator.empty"));
			}
		};
	}
	
	public Validator getValidatorP2kb() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String chargedesc = (String) ctx.getProperties("chargedesc")[0].getValue();
				if (chargedesc == null || "".equals(chargedesc.trim()))
					this.addInvalidMessage(ctx, "chargedescp2kb", Labels.getLabel("common.validator.empty"));
				BigDecimal chargeamount = (BigDecimal) ctx.getProperties("chargeamount")[0].getValue();
				if (chargeamount == null)
					this.addInvalidMessage(ctx, "chargeamountp2kb", Labels.getLabel("common.validator.empty"));
			}
		};
	}
	
	@Command
	@NotifyChange("totalfeepercent")
	public void doTotalpercent(@BindingParam("item") Mfee obj) {
		if (obj != null) {
			totalfeepercent = new BigDecimal(0);
			totalfeepercent = totalfeepercent.add(obj.getFeepusat() != null ? obj.getFeepusat() : new BigDecimal(0));
			totalfeepercent = totalfeepercent.add(obj.getFeeprov() != null ? obj.getFeeprov() : new BigDecimal(0));
			totalfeepercent = totalfeepercent.add(obj.getFeekab() != null ? obj.getFeekab() : new BigDecimal(0));
		}
	}
	
	@Command
	@NotifyChange({"feepusat", "feeprov", "feekab"})
	public void doCalFee() {
		if (amount == null) {
			Messagebox.show("Silahkan isi nilai pemasukan", WebApps.getCurrent().getAppName(),
					Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			feepusat = amount.multiply((objFee.getFeepusat().divide(new BigDecimal(100))));
			feeprov = amount.multiply((objFee.getFeeprov().divide(new BigDecimal(100))));
			feekab = amount.multiply((objFee.getFeekab().divide(new BigDecimal(100))));
		}
	}
	
	@Command
	@NotifyChange("objFee")
	public void doSaveFee() {
		if (totalfeepercent.compareTo(new BigDecimal(100)) == 0) {
			Session session = StoreHibernateUtil.openSession();
			Transaction trx = null;
			try {
				trx = session.beginTransaction();
				objFee.setCreatetime(new Date());
				objFee.setCreatedby(oUser.getNoanggota());
				objFee.setLastupdated(new Date());
				objFee.setUpdatedby(oUser.getNoanggota());
				feeDao.save(session, objFee);
				trx.commit();
				Clients.showNotification("Pengaturan nilai distrubsi fee berhasil disimpan", "info", null, "middle_center", 1500);
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(),
						Messagebox.OK, Messagebox.ERROR);
			} finally {
				session.close();
			}
		} else {
			Messagebox.show("Total % harus 100%", WebApps.getCurrent().getAppName(),
					Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	public Mcharge getObjReg() {
		return objReg;
	}

	public void setObjReg(Mcharge objReg) {
		this.objReg = objReg;
	}

	public Mcharge getObjIuran() {
		return objIuran;
	}

	public void setObjIuran(Mcharge objIuran) {
		this.objIuran = objIuran;
	}

	public BigDecimal getTotalreg() {
		return totalreg;
	}

	public void setTotalreg(BigDecimal totalreg) {
		this.totalreg = totalreg;
	}

	public BigDecimal getTotaliuran() {
		return totaliuran;
	}

	public void setTotaliuran(BigDecimal totaliuran) {
		this.totaliuran = totaliuran;
	}

	public Mfee getObjFee() {
		return objFee;
	}

	public void setObjFee(Mfee objFee) {
		this.objFee = objFee;
	}

	public BigDecimal getTotalfeepercent() {
		return totalfeepercent;
	}

	public void setTotalfeepercent(BigDecimal totalfeepercent) {
		this.totalfeepercent = totalfeepercent;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFeepusat() {
		return feepusat;
	}

	public void setFeepusat(BigDecimal feepusat) {
		this.feepusat = feepusat;
	}

	public BigDecimal getFeeprov() {
		return feeprov;
	}

	public void setFeeprov(BigDecimal feeprov) {
		this.feeprov = feeprov;
	}

	public BigDecimal getFeekab() {
		return feekab;
	}

	public void setFeekab(BigDecimal feekab) {
		this.feekab = feekab;
	}

	public Mcharge getObjP2kb() {
		return objP2kb;
	}

	public void setObjP2kb(Mcharge objP2kb) {
		this.objP2kb = objP2kb;
	}

	public BigDecimal getTotalp2kb() {
		return totalp2kb;
	}

	public void setTotalp2kb(BigDecimal totalp2kb) {
		this.totalp2kb = totalp2kb;
	}
}
