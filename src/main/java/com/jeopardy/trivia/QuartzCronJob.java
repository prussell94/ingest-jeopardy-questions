package com.jeopardy.trivia;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * This class is used for executing quartz job
 * using SimpleTrigger(Quartz 2.1.5).
 * @author w3spoint
 */
public class QuartzCronJob {
    public static void main(String args[]){
        try{
            //Set job details.
            JobDetail job = JobBuilder.newJob(RunIngestQuestions.class)
                    .withIdentity("jeopardyQuestions", "group1").build();

            //Set the scheduler timings.
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("simpleTrigger", "group1")
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(60).repeatForever()).build();

            //Execute the job.
            Scheduler scheduler =
                    new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
