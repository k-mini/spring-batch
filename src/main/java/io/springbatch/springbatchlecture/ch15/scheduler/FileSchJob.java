package io.springbatch.springbatchlecture.ch15.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class FileSchJob extends QuartzJobBean {

    private final Job fileJob;
    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;

    @SneakyThrows
    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {

        String requestDate = (String) context.getJobDetail().getJobDataMap().get("requestDate");

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .addString("requestDate", requestDate)
                .toJobParameters();
        int jobInstanceCount = jobExplorer.getJobInstanceCount(fileJob.getName());
        List<JobInstance> jobInstances = jobExplorer.getJobInstances(fileJob.getName(), 0, jobInstanceCount);

        if (jobInstances.size() > 0) {
            for (JobInstance jobInstance : jobInstances) {
                List<JobExecution> jobExecutionList = jobExplorer.getJobExecutions(jobInstance)
                        .stream()
                        .filter(jobExecution ->
                                jobExecution
                                        .getJobParameters()
                                        .getString("requestDate").equals(requestDate)
                        ).collect(Collectors.toList());

                if (jobExecutionList.size() > 0) {
                    throw new JobExecutionException(requestDate + " is already exists");
                }
            }
        }

        try {
            jobLauncher.run(fileJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            throw new RuntimeException(e);
        } catch (JobRestartException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        } catch (JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }
}
