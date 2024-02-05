package com.sds.hakli.utils;

import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.sds.hakli.dao.TschedulerDAO;
import com.sds.hakli.domain.Tscheduler;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;

public class ScheduleManager {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initializer(String realpath) {
		System.out.println("Job Scheduler Initialization...");
		if (!AppData.isInitFundTransfer) {
			AppData.isInitFundTransfer = true;
			
			System.out.println("SERVICES");
			String schedule = "";
			try {				
				SchedulerFactory factory = new StdSchedulerFactory();
				Scheduler scheduler = factory.getScheduler();
				scheduler.start();
				
				List<Tscheduler> listTscheduler = new TschedulerDAO().listByFilter("0=0", "tschedulerpk");
				for (Tscheduler tscheduler: listTscheduler) {
					Class cls = Class.forName(tscheduler.getJobclass().trim());				
					System.out.println("Class = " + cls.getName());
						
					boolean isValid = true;
					for (String groupName : scheduler.getJobGroupNames()) {
						 for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
							 if (jobKey.getName().equals(tscheduler.getSchedulername())) {
								 isValid = false;
								 break;
							 }
						 }	 
					}
					
					if (isValid) {
						JobDetail job = JobBuilder.newJob(cls).withIdentity(tscheduler.getSchedulername(), tscheduler.getSchedulergroup()).build();
						job.getJobDataMap().put("realpath", realpath);
						System.out.println(tscheduler.getRepeatinterval());
						System.out.println(tscheduler.getSchedulerrepeattype());
						if (tscheduler.getSchedulerrepeattype().trim().equals(AppUtils.SCHEDULER_REPEAT_PERMINUTE)) {
							schedule = "0 0/" + tscheduler.getRepeatinterval() + " * * * ?";
						} else if (tscheduler.getSchedulerrepeattype().trim().equals(AppUtils.SCHEDULER_REPEAT_ATHOUR)) {
							schedule = "0 0 " + tscheduler.getRepeatinterval() + " * * ?";
						}
						System.out.println("Default Schedule : "+schedule);
						Trigger trigger = TriggerBuilder.newTrigger().withIdentity(tscheduler.getSchedulername(), tscheduler.getSchedulergroup())
								.withSchedule(CronScheduleBuilder.cronSchedule(schedule)).forJob(job).build();																					
						
						scheduler.scheduleJob(job, trigger);
						
						if (tscheduler.getSchedulerstatus().equals(AppUtils.SCHEDULER_DISABLE_VALUE))
							scheduler.pauseTrigger(new TriggerKey(tscheduler.getSchedulername(), tscheduler.getSchedulergroup()));
						//else scheduler.resumeTrigger(new TriggerKey(tscheduler.getSchedulername(), tscheduler.getSchedulergroup()));
					} else {
						System.out.println("Job has been exist!");
					}
				}			
			} catch (SchedulerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
	}
}
