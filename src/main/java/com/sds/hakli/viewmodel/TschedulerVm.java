package com.sds.hakli.viewmodel;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.quartz.CronScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Spinner;

import com.sds.hakli.dao.TschedulerDAO;
import com.sds.hakli.domain.Muser;
import com.sds.hakli.domain.Tscheduler;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class TschedulerVm {
	
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();

	private Session session;
	private Transaction transaction;
	
	private TschedulerDAO oDao = new TschedulerDAO();	
	
	@Wire
	private Grid grid;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);	
		refreshModel();
		if (grid != null) {
			grid.setRowRenderer(new RowRenderer() {

				@Override
				public void render(Row row, Object data, int index)
						throws Exception {
					final Tscheduler oForm = (Tscheduler) data;
					row.getChildren().add(new Label(String.valueOf(index + 1)));
					row.getChildren().add(new Label(oForm.getSchedulername()));
					row.getChildren().add(new Label(oForm.getSchedulergroup()));					
					row.getChildren().add(new Label(oForm.getSchedulerdesc()));
					
					final Combobox cboxStatus = new Combobox(oForm.getSchedulerstatus().equals(AppUtils.SCHEDULER_DISABLE_VALUE) ? AppUtils.SCHEDULER_DISABLE_LABEL : AppUtils.SCHEDULER_ENABLE_LABEL);
					cboxStatus.setCols(10);
					cboxStatus.setReadonly(true);
					cboxStatus.appendChild(new Comboitem(AppUtils.SCHEDULER_ENABLE_LABEL));
					cboxStatus.appendChild(new Comboitem(AppUtils.SCHEDULER_DISABLE_LABEL));
					row.getChildren().add(cboxStatus);
			
					
					final Combobox cboxType = new Combobox(oForm.getSchedulerrepeattype());
					cboxType.setCols(10);
					cboxType.setReadonly(true);
					cboxType.appendChild(new Comboitem(AppUtils.SCHEDULER_REPEAT_PERMINUTE));
					cboxType.appendChild(new Comboitem(AppUtils.SCHEDULER_REPEAT_ATHOUR));
					row.getChildren().add(cboxType);
					
					final Spinner spinner = new Spinner(oForm.getRepeatinterval());
					spinner.setCols(3);
					row.getChildren().add(spinner);
					
					
					cboxType.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							if (cboxType.getValue().equals(AppUtils.SCHEDULER_REPEAT_PERMINUTE)) {
								spinner.setConstraint("no empty,min 1 max 180");
							} else if (cboxType.getValue().equals(AppUtils.SCHEDULER_REPEAT_ATHOUR)) {
								spinner.setConstraint("no empty,min 0 max 23");
							}
						}
					});
					
					
					Button btnEdit = new Button("Update");	
					btnEdit.setAutodisable("self");
					btnEdit.setSclass("btn btn-primary btn-sm");
					btnEdit.addEventListener(Events.ON_CLICK, new EventListener() {

						@Override
						public void onEvent(Event event) throws Exception {
							Messagebox.show("Anda ingin melakukan pembaruan data ini?", "Confirm Dialog", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {

								@Override
								public void onEvent(Event event)
										throws Exception {
									if (event.getName().equals("onOK")) {
										try {
											Muser oUser = (Muser) zkSession.getAttribute("oUser");
											session = StoreHibernateUtil.openSession();
											transaction = session.beginTransaction();
											oForm.setSchedulerstatus(cboxStatus.getValue().equals(AppUtils.SCHEDULER_DISABLE_LABEL) ? AppUtils.SCHEDULER_DISABLE_VALUE : AppUtils.SCHEDULER_ENABLE_VALUE);
											oForm.setSchedulerrepeattype(cboxType.getValue());
											oForm.setRepeatinterval(spinner.getValue());
											oForm.setLastupdated(new Date());
											oForm.setUpdatedby(oUser.getUserid());
											oDao.save(session, oForm);
											transaction.commit();
											session.close();		
											
											Clients.showNotification(Labels.getLabel("common.update.success"), "info", null, "middle_center", 3000);
											
											SchedulerFactory factory = new StdSchedulerFactory();
											Scheduler scheduler = factory.getScheduler();
											scheduler.start();	
											
											String schedule = "";
											if (oForm.getSchedulerrepeattype().equals(AppUtils.SCHEDULER_REPEAT_PERMINUTE)) {
												schedule = "0 0/" + oForm.getRepeatinterval() + " * * * ?";
											} else if (oForm.getSchedulerrepeattype().equals(AppUtils.SCHEDULER_REPEAT_ATHOUR)) {
												schedule = "0 0 " + oForm.getRepeatinterval() + " * * ?";
											}
											
											Trigger oldTrigger = scheduler.getTrigger(new TriggerKey(oForm.getSchedulername(), oForm.getSchedulergroup()));
											TriggerBuilder triggerBuilder = oldTrigger.getTriggerBuilder();											
											Trigger newTrigger = triggerBuilder.withIdentity(oForm.getSchedulername(), oForm.getSchedulergroup())
													.withSchedule(CronScheduleBuilder.cronSchedule(schedule)).build();
											scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);											
											
											if (oForm.getSchedulerstatus().equals(AppUtils.SCHEDULER_DISABLE_VALUE))
												scheduler.pauseTrigger(new TriggerKey(oForm.getSchedulername(), oForm.getSchedulergroup()));
											else scheduler.resumeTrigger(new TriggerKey(oForm.getSchedulername(), oForm.getSchedulergroup()));
																						
											refreshModel();
										} catch (HibernateException e) {	
											transaction.rollback();
											Messagebox.show("Error : " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
											e.printStackTrace();
										} catch (Exception e) {
											Messagebox.show("Error : " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
											e.printStackTrace();
										}																																					
									} 									
								}
								
							});			
						}
					});											
										
					row.getChildren().add(btnEdit);										
				}
				
			});
		}		
	}
	
	public void refreshModel() {
		try {
			grid.setModel(new ListModelList<Tscheduler>(new TschedulerDAO().listByFilter("0=0", "tschedulerpk")));
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}	
	
}
