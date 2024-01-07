package io.springbatch.springbatchlecture.ch15.scheduler;

import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;

@RequiredArgsConstructor
@Component
public class FileJobRunner extends JobRunner {

    protected final Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {

        String[] sourceArgs = args.getSourceArgs();


        JobDetail jobDetail = buildJobDetail(FileSchJob.class, "fileJob", "batch", new HashMap<>());
        Trigger trigger = buildJobTrigger("0/50 * * * * ?");
        jobDetail.getJobDataMap().put("requestDate", sourceArgs[0]);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
